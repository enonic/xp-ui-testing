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
import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage;
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
    private static final String TABLE_ITEM_XPATH = "//h6[text()='BildeArkiv']";

    public static final String SPACES_TABLE_CELLS_XPATH = "//table[contains(@class,'x-grid-table')]//td[contains(@class,'x-grid-cell')]";

    public static final String CONTENT_MANAGER_BUTTON = "//button[@id='api.app.HomeButton']";

    @FindBy(xpath = "//div[@class='toolbar']/button[text()='Duplicate']")
    private WebElement duplicateButton;

    @FindBy(xpath = CONTENT_MANAGER_BUTTON)
    private WebElement contentManagerButton;

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

    public ContentBrowsePanel goToAppHome()
    {
        contentManagerButton.click();
        waituntilPageLoaded( Application.IMPLICITLY_WAIT );
        return this;
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
        getLogger().info( "will verify is exists:" + contentDescriptionXpath );
        boolean result = waitUntilVisibleNoException( By.xpath( contentDescriptionXpath ), 1l );
        getLogger().info( "content with path:" + contentDescriptionXpath + " isExists: " + result );
        TestUtils.saveScreenshot( getSession() );
        return result;
    }

    /**
     * @param contentPath
     */
    public ContentBrowsePanel expandContent( ContentPath contentPath )
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
        return this;
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
    public ContentBrowsePanel selectContentInTable( List<BaseAbstractContent> contents )
    {
        waitAndCheckContent( contents );
        for ( BaseAbstractContent content : contents )
        {
            selectCheckbox( content );
        }
        return this;
    }

    private void waitAndCheckContent( List<BaseAbstractContent> contents )
    {
        //TODO  this is a workaround for app issue, should be deleted after fixing
        //doWorkAround();

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


    /**
     * Clicks by a checkbox, linked with content and select row in the table.
     *
     * @param content
     */
    private ContentBrowsePanel selectCheckbox( BaseAbstractContent content )
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
        return this;
    }


    public void selectRowWithContent( ContentPath path )
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
     * Clicks by 'New' button and opens NewContentDialog
     *
     * @return
     */
    public NewContentDialog clickToolbarNew()
    {
        newButton.click();
        NewContentDialog newContentDialog = new NewContentDialog( getSession() );
        boolean isOpened = newContentDialog.isOpened();
        if ( !isOpened )
        {
            throw new TestFrameworkException( "Error during add content, NewContentDialog dialog was not opened!" );
        }
        return newContentDialog;
    }

    /**
     * Expands all folders, that present in the 'content path' and  clicks by a checkbox related to parent folder for new content.
     *
     * @param parentContentPath
     */
    public void selectParentForContent( ContentPath parentContentPath )
    {
        if ( parentContentPath.elementCount() == 0 )
        {
            return;
        }
        if ( parentContentPath.elementCount() > 1 )
        {
            expandContent( parentContentPath );

        }

        // 1. select a checkbox and press the 'New' from toolbar.
        String spaceCheckBoxXpath = String.format( CHECKBOX_ROW_CHECKER, parentContentPath );
        boolean isPresentCheckbox = isDynamicElementPresent( By.xpath( spaceCheckBoxXpath ), 3 );
        if ( !isPresentCheckbox )
        {
            TestUtils.saveScreenshot( getSession() );
            throw new TestFrameworkException(
                "Time: " + TestUtils.timeNow() + "  wrong xpath:" + spaceCheckBoxXpath + " or Space with name " + parentContentPath +
                    " was not found!" );
        }
        WebElement checkboxElement = getDriver().findElement( By.xpath( spaceCheckBoxXpath ) );

        checkboxElement.click();
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
        //doWorkAround();
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
        selectRowByContentPath( fullName );
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
        cinfo.waitUntilOpened( content.getDisplayName(), expectedNumberOfPage );
        return cinfo;
    }

    /**
     * @param fullName
     */
    public ContentBrowsePanel selectRowByContentPath( String contentPath )
    {
        String rowXpath = String.format( TD_CONTENT_NAME, contentPath );
        waitAndFind( By.xpath( rowXpath ) );
        //findElement(By.xpath(rowXpath)).click();

        Actions builder = new Actions( getDriver() );
        builder.click( findElement( By.xpath( rowXpath ) ) ).build().perform();
        return this;
    }

    public ItemViewPanelPage clickToolbarOpen()
    {
        openButton.click();
        ItemViewPanelPage cinfo = new ItemViewPanelPage( getSession() );
        // cinfo.waitUntilOpened( getSession(), contentDisplayName, expectedNumberOfPage );
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
        cinfo.waitUntilOpened( content.getDisplayName(), expectedNumberOfPage );
        return cinfo;
    }

    /**
     * Waits until page loaded.
     *
     * @param timeout
     */
    public void waituntilPageLoaded( long timeout )
    {
        TestUtils.saveScreenshot( getSession() );
        new WebDriverWait( getDriver(), timeout ).until( ExpectedConditions.visibilityOfElementLocated( By.xpath( TABLE_ITEM_XPATH ) ) );
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

}
