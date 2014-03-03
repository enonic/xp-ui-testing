package com.enonic.autotests.pages.contentmanager.browsepanel;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.BrowsePanel;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage;
import com.enonic.autotests.services.ContentFilterService;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent;
import com.enonic.wem.api.content.ContentPath;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * 'Content Manager' application, the dashboard page.
 */
public class ContentBrowsePanel
    extends BrowsePanel
{
    private static final String TITLE_XPATH = "//button[contains(@class,'home-button') and contains(.,'Content Manager')]";

    public static final String SPACES_TABLE_CELLS_XPATH = "//table[contains(@class,'x-grid-table')]//td[contains(@class,'x-grid-cell')]";

    @FindBy(xpath = "//div[@class='toolbar']/button[text()='Duplicate']")
    private WebElement duplicateButton;

    @FindBy(xpath = "//div[@class='toolbar']/button[text()='Open']")
    private WebElement openButton;

    @FindBy(xpath = "//div[@class='toolbar']/button[text()='Move']")
    private WebElement moveButton;


    private String CHECKBOX_ROW_CHECKER = TD_CONTENT_NAME + "/..//div[@class='x-grid-row-checker']";

    private String DIV_CONTENT_NAME_IN_TABLE =
        "//div[contains(@class,'x-grid-cell-inner ')]//div[@class='admin-tree-description' and descendant::p[contains(.,'%s')]]";

    private final String ALL_NAMES_IN_CONTENT_TABLE_XPATH =
        "//table[contains(@class,'x-grid-table')]//tr[contains(@class,'x-grid-row')]//div[@class='admin-tree-description']/descendant::p";

    private final String CONTENT_DETAILS_ALL_NAMES_XPATH =
        "//div[contains(@id, 'contentDetail')]//div[contains(@class,'admin-selected-item-box')]//p";

    private ContentBrowseFilterPanel contentBrowseFilterPanel;

    /**
     * The constructor.
     *
     * @param session
     */
    public ContentBrowsePanel( TestSession session )
    {
        super( session );
    }

    public ContentBrowseFilterPanel getContentBrowseFilterPanel()
    {
        if ( contentBrowseFilterPanel == null )
        {
            contentBrowseFilterPanel = new ContentBrowseFilterPanel( getSession() );
        }
        return contentBrowseFilterPanel;
    }

    /**
     * Gets content's names from 'Details panel'.
     *
     * @return list of names, or empty list if there are no seleted items
     */
    public List<String> getNamesFromContentDetails()
    {
        List<String> contentNames = new ArrayList<>();
        List<WebElement> elems = getDriver().findElements( By.xpath( CONTENT_DETAILS_ALL_NAMES_XPATH ) );
        for ( WebElement el : elems )
        {
            contentNames.add( el.getText() );
        }
        return contentNames;
    }


    /**
     * Gets all content names, showed in the contents-table.
     *
     * @return list of names.
     */
    public List<String> getShowedContentNames()
    {
        List<String> allNames = new ArrayList<>();
        List<WebElement> rows = getDriver().findElements( By.xpath( ALL_NAMES_IN_CONTENT_TABLE_XPATH ) );
        for ( WebElement row : rows )
        {
            allNames.add( row.getText() );
        }
        return allNames;
    }

    public boolean exists( ContentPath contentPath )
    {

        String contentDescriptionXpath = String.format( DIV_CONTENT_NAME_IN_TABLE, contentPath.toString() );
        getLogger().info("will verify is exists:"+ contentDescriptionXpath);
        boolean result  =  waitUntilVisibleNoException( By.xpath( contentDescriptionXpath ), 1l );
        getLogger().info("content with path:" + contentDescriptionXpath + " isExists: "+ result);
        TestUtils.saveScreenshot(getSession());
        return result;
    }

    public void expandAndSelectContent( ContentPath contentPath )
    {
        expandContent( contentPath.getParentPath() );
        selectContent( contentPath );
    }

    public void expandContent( ContentPath contentPath )
    {
        ContentPath path = null;
        if ( contentPath != null )
        {
            for ( int i = 0; i < contentPath.elementCount(); i++ )
            {
                String parentContent = contentPath.getElement( i );
                if ( path == null )
                {
                    path = ContentPath.from( parentContent );
                }
                else
                {
                    path = ContentPath.from( path, parentContent );
                }

                if ( !clickByExpander( path.toString() ) )
                {
                    getLogger().info( "content with name " + parentContent + "has no children! " );
                }
            }
        }
    }

    /**
     * Delete contents from a space.
     *
     * @param contents
     */
    public void doDeleteContent( List<BaseAbstractContent> contents )
    {
        DeleteContentDialog dialog = openDeleteContentDialog( contents );
        // 5. press the button "Delete" on the dialog.
        dialog.doDelete();
        boolean isClosed = dialog.verifyIsClosed();
        if ( !isClosed )
        {
            throw new TestFrameworkException( "Confirm 'delete content' dialog was not closed!" );
        }
    }

    public DeleteContentDialog openDeleteContentDialog( List<BaseAbstractContent> contents )
    {
        ContentPath contentPath = contents.get( 0 ).getPath();
        //TODO remove it, when bug will be fixed! 
        doWorkAround();
        // 1. expand all folders
        if ( contentPath.elementCount() > 1 )
        {
            expandContent( contentPath.getParentPath() );
        }

        // 2. check for existence and select a content to delete.
        selectContentInTable( contents );

        // 3. check if enabled 'Delete' link.
        boolean isEnabledDeleteButton = waitUntilElementEnabledNoException( By.xpath( DELETE_BUTTON_XPATH ), 2l );
        if ( !isEnabledDeleteButton )
        {
            throw new SaveOrUpdateException( "CM application, impossible to delete content, because the 'Delete' button is disabled!" );
        }
        // 4. click by 'Delete' link and open a confirm dialog.
        deleteButton.click();
        DeleteContentDialog dialog = new DeleteContentDialog( getSession() );
        return dialog;
    }

    /**
     * Clicks by 'Delete' button in toolbar, confirms deleting when 'Confirm Deleting' dialog appears.
     */
    public void deleteSelected()
    {

        clickToolbarDelete().doDelete();
    }

    public DeleteContentDialog clickToolbarDelete()
    {

        boolean isEnabledDeleteButton = waitUntilElementEnabledNoException( By.xpath( DELETE_BUTTON_XPATH ), 2l );
        if ( !isEnabledDeleteButton )
        {
            throw new SaveOrUpdateException( "CM application, impossible to delete content, because the 'Delete' button is disabled!" );
        }
        // 4. click by 'Delete' link and open a confirm dialog.
        deleteButton.click();

        return new DeleteContentDialog( getSession() );
    }

    /**
     * Selects a content in a space or folder, throws exception if content was
     * not found.
     *
     * @param contents
     */
    private void selectContentInTable( List<BaseAbstractContent> contents )
    {
        waitAndCheckContent( contents );
        for ( BaseAbstractContent content : contents )
        {
            selectCheckbox( content );
        }
    }

    private void waitAndCheckContent( List<BaseAbstractContent> contents )
    {
        //TODO  this is a workaround for app issue, should be deleted after fixing
        doWorkAround();

        for ( BaseAbstractContent content : contents )
        {
            boolean isExist = exists( content.getPath() );

            if ( !isExist )
            {
                //
                TestUtils.saveScreenshot( getSession() );
                throw new TestFrameworkException( "The content with name " + content.getName() + " was not found!" );
            }
        }
    }

    private void doWorkAround()
    {
        ContentFilterService fs = new ContentFilterService();
        fs.doFilterByText( getSession(), "test" );
        fs.doClearFilter( getSession() );
    }

    /**
     * Clicks by a checkbox, linked with content and select row in the table.
     *
     * @param content
     */
    private void selectCheckbox( BaseAbstractContent content )
    {
        String fullName = content.getPath().toString();
        String contentCheckBoxXpath = String.format( CHECKBOX_ROW_CHECKER, fullName );
        getLogger().info( "tries to find the content in a table, fullName of content is :" + fullName );

        getLogger().info( "Xpath of checkbox for content is :" + contentCheckBoxXpath );
        boolean isPresent = waitUntilVisibleNoException( By.xpath( contentCheckBoxXpath ), 3l );
        if ( !isPresent )
        {
            throw new SaveOrUpdateException( "checkbox for content with name : " + content.getName() + "was not found" );
        }
        sleep( 700 );
        findElement( By.xpath( contentCheckBoxXpath ) ).click();
    }


    public void selectContent( ContentPath path )
    {
        String contentCheckBoxXpath = String.format( CHECKBOX_ROW_CHECKER, path.toString() );
        getLogger().info( "tries to find content in table:" + path.toString() );

        getLogger().info( "Xpath of checkbox for content is :" + contentCheckBoxXpath );
        boolean isPresent = waitUntilVisibleNoException( By.xpath( contentCheckBoxXpath ), 3l );
        if ( !isPresent )
        {
            throw new SaveOrUpdateException( "checkbox for content: " + path.toString() + "was not found" );
        }
        sleep( 700 );
        findElement( By.xpath( contentCheckBoxXpath ) ).click();
        getLogger().info( "check box was selected, content path is:" + path.toString() );
    }

    /**
     * Adds the content to a space or folder.
     *
     * @param content
     * @param isWizardShouldBeClosed
     */
    public void doAddContent( BaseAbstractContent content, boolean isWizardShouldBeClosed )
    {
        doWorkAround();
        ContentPath contentPath = content.getPath();
        ContentWizardPanel wizard = openContentWizardPanel( content.getContentTypeName(), contentPath );
        if ( isWizardShouldBeClosed )
        {
            wizard.doTypeDataSaveAndClose( content );
            ContentBrowsePanel panel = new ContentBrowsePanel( getSession() );
            panel.waituntilPageLoaded( Application.PAGELOAD_TIMEOUT );
        }
        else
        {
            wizard.doTypeDataAndSave( content );
        }

    }

    public ContentWizardPanel openEditWizardPage( BaseAbstractContent content )
    {
        expandContent( content.getParent() );
        boolean isExist = exists( content.getPath() );
        //        boolean isPresent = findContentInTable( content, 2l );
        if ( !isExist )
        {
            throw new TestFrameworkException( "The content with name " + content.getName() + " was not found!" );
        }
        // 2. check out is content present  in a parent space and select it to edit.
        selectCheckbox( content );
        editButton.click();
        ContentWizardPanel wizard = new ContentWizardPanel( getSession() );
        wizard.waitUntilWizardOpened( 1 );
        return wizard;
    }

    /**
     * Select a content type and opens "Add new Content Wizard".
     *
     * @param contentTypeName
     * @param contentPath
     * @return
     */
    public ContentWizardPanel openContentWizardPanel( String contentTypeName, ContentPath contentPath )
    {
        //1. click by a checkbox and select a parent folder
        if ( contentPath != null && !contentPath.isRoot() )
        {
            selectParentForContent( contentPath.getParentPath() );
        }

        newButton.click();
        return selectKindOfContentAndOpenWizardPanel( contentTypeName );
    }

    /**
     * Clicks by 'New' button from toolbar and open a dialog with title: "What do you want to create?"
     *
     * @param contentTypeName the kind of content
     * @return {@ContentWizardPanel} instance.
     */
    private ContentWizardPanel selectKindOfContentAndOpenWizardPanel( String contentTypeName )
    {
        NewContentDialog newContentDialog = new NewContentDialog( getSession() );
        boolean isOpened = newContentDialog.isOpened();
        if ( !isOpened )
        {
            throw new TestFrameworkException( "Error during add content, NewContentDialog dialog was not opened!" );
        }
        getLogger().info( "NewContentDialog, content type should be selected:" + contentTypeName );
        ContentWizardPanel wizard = newContentDialog.selectContentType( contentTypeName );
        wizard.waitUntilWizardOpened( 1 );
        return wizard;
    }

    /**
     * Expands all folders, that present in the 'content path' and  clicks by a checkbox related to parent folder for new content.
     *
     * @param parentContentPath
     */
    private void selectParentForContent( ContentPath parentContentPath )
    {
        if ( parentContentPath.elementCount() == 0 )
        {
            return;
        }
        if ( parentContentPath.elementCount() > 1 )
        {
            expandContent( parentContentPath );

        }
        // doExpandAllFoldersFromContentPath( contentPath );
        // 1. select a checkbox and press the 'New' from toolbar.
        String spaceCheckBoxXpath = String.format( CHECKBOX_ROW_CHECKER, parentContentPath );
        boolean isPresentCheckbox = isDynamicElementPresent( By.xpath( spaceCheckBoxXpath ), 3 );

        //TODO workaround: issue with empty grid(this is a application issue, it  will be fixed some later )
        if ( !isPresentCheckbox )
        {
            getLogger().info( "Grid is empty, test-folder was not found! try to find again ..." );
            //TODO type a word and do 'Clear filter'
            ContentFilterService fs = new ContentFilterService();
            fs.doFilterByText( getSession(), "test" );
            TestUtils.saveScreenshot( getSession() );
            fs.doClearFilter( getSession() );

            isPresentCheckbox = isDynamicElementPresent( By.xpath( spaceCheckBoxXpath ), 3 );
        }
        if ( !isPresentCheckbox )
        {
            TestUtils.saveScreenshot( getSession() );
            throw new TestFrameworkException(
                "Time: " + TestUtils.timeNow() + "  wrong xpath:" + spaceCheckBoxXpath + " or Space with name " + parentContentPath +
                    " was not found!" );
        }
        WebElement checkboxElement = getDriver().findElement( By.xpath( spaceCheckBoxXpath ) );

        checkboxElement.click();
        //selectRowByContentDisplayName(parentName);
        boolean isNewEnabled = waitUntilElementEnabledNoException( By.xpath( NEW_BUTTON_XPATH ), 2l );
        if ( !isNewEnabled )
        {
            throw new SaveOrUpdateException( "CM application, impossible to open NewContentDialog, because the 'New' button is disabled!" );
        }

    }

    /**
     * Clicks by row with content and  clicks by 'Open' button.
     *
     * @param content
     * @return {@ItemViewPanelPage} instance
     */
    public ItemViewPanelPage doOpenContent( BaseAbstractContent content )
    {
        doWorkAround();
        expandContent( content.getParent() );
        boolean isPresent = exists( content.getPath() );
        if ( !isPresent )
        {
            throw new TestFrameworkException( "The content with name " + content.getName() + " was not found!" );
        }
        else
        {
            getLogger().info( "doOpenContent::: content with name equals " + content.getDisplayName() + " was found" );
        }
        String fullName = content.getPath().toString();
        sleep( 700 );
        //1. select a content
        selectRowByContentFullName( fullName );
        if ( !openButton.isEnabled() )
        {
            getLogger().info( "'Open' link is disabled!" );
            new WebDriverWait( getDriver(), 2 ).until(
                ExpectedConditions.elementToBeClickable( By.xpath( "//div[@class='toolbar']/button[text()='Open']" ) ) );
        }
        //2. click by 'Open' button
        openButton.click();
        ItemViewPanelPage cinfo = new ItemViewPanelPage( getSession() );
        int expectedNumberOfPage = 1;
        cinfo.waitUntilOpened( getSession(), content.getDisplayName(), expectedNumberOfPage );
        return cinfo;
    }

    /**
     * Select a content and right click on  mouse, opens a Item view panel.
     *
     * @param content
     * @return {@ItemViewPanelPage} instance.
     */
    public ItemViewPanelPage doOpenContentFromContextMenu( BaseAbstractContent content )
    {
        expandContent( content.getParent() );
        boolean isExists = exists( content.getPath() );
        if ( !isExists )
        {
            throw new TestFrameworkException(
                "The content with name " + content.getName() + " and displayName:" + content.getDisplayName() + " was not found!" );
        }
        else
        {
            getLogger().info( "doOpenContent::: content with name equals " + content.getDisplayName() + " was found" );
        }
        // 2. check for existence of content in a parent space and select a content to open.
        String fullName = content.getPath().toString();
        getLogger().info( "Full name of content: " + fullName );
        String contentDescriptionXpath = String.format( DIV_CONTENT_NAME_IN_TABLE, fullName );
        WebElement element = findElement( By.xpath( contentDescriptionXpath ) );
        Actions action = new Actions( getDriver() );
        //action.contextClick(element).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).build().perform();
        action.contextClick( element ).click().build().perform();

        ItemViewPanelPage cinfo = new ItemViewPanelPage( getSession() );
        int expectedNumberOfPage = 1;
        cinfo.waitUntilOpened( getSession(), content.getDisplayName(), expectedNumberOfPage );
        return cinfo;
    }

    /**
     * Waits until page loaded.
     *
     * @param timeout
     */
    public void waituntilPageLoaded( long timeout )
    {
        new WebDriverWait( getDriver(), timeout ).until( ExpectedConditions.visibilityOfElementLocated( By.xpath( TITLE_XPATH ) ) );
    }

    /**
     * @param session
     * @return true if 'Content Manager' opened and CMSpacesPage showed, otherwise false.
     */
    public static boolean isOpened( TestSession session )
    {
        List<WebElement> searchInput = session.getDriver().findElements( By.xpath( ContentBrowseFilterPanel.SEARCH_INPUT_XPATH ) );
        if ( searchInput.size() > 0 && searchInput.get( 0 ).isDisplayed() )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean verifyTitle()
    {
        return waitAndFind( By.xpath( TITLE_XPATH ) );
    }

    public boolean verifyAllControls()
    {
        boolean result = true;
        result &= verifyTollbar();
        //result &= verifySearchPannel();
        return result;
    }

    private boolean verifyTollbar()
    {
        boolean result = true;
        result &= newButton.isDisplayed() && !newButton.isEnabled();
        result &= editButton.isDisplayed() && !editButton.isEnabled();
        result &= deleteButton.isDisplayed() && !deleteButton.isEnabled();
        result &= duplicateButton.isDisplayed() && !duplicateButton.isEnabled();
        result &= openButton.isDisplayed() && !openButton.isEnabled();
        result &= moveButton.isDisplayed() && !moveButton.isEnabled();
        return result;
    }


}
