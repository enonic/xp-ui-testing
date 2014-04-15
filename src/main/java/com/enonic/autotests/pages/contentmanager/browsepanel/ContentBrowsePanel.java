package com.enonic.autotests.pages.contentmanager.browsepanel;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.BrowsePanel;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.contentmanager.Content;
import com.enonic.wem.api.content.ContentPath;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * 'Content Manager' application, the dashboard page.
 */
public class ContentBrowsePanel
    extends BrowsePanel
{
    public static final String SPACES_TABLE_CELLS_XPATH = "//table[contains(@class,'x-grid-table')]//td[contains(@class,'x-grid-cell')]";

    public static final String CONTENT_MANAGER_BUTTON = "//button[@id='api.app.HomeButton']";

    private static final String TABLE_ITEM_XPATH = "//h6[text()='BildeArkiv']";

    public final String NEW_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.ContentBrowseToolbar')]/*[contains(@id, 'api.ui.ActionButton') and child::span[text()='New']]";

    public final String DUPLICATE_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.ContentBrowseToolbar')]/*[contains(@id, 'api.ui.ActionButton') and child::span[text()='Duplicate']]";

    public final String OPEN_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.ContentBrowseToolbar')]/*[contains(@id, 'api.ui.ActionButton') and child::span[text()='Open']]";

    public final String MOVE_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.ContentBrowseToolbar')]/*[contains(@id, 'api.ui.ActionButton') and child::span[text()='Move']]";

    protected final String EDIT_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.ContentBrowseToolbar')]/*[contains(@id, 'api.ui.ActionButton') and child::span[text()='Edit']]";

    protected final String DELETE_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.ContentBrowseToolbar')]/*[contains(@id, 'api.ui.ActionButton') and child::span[text()='Delete']]";

    private final String ALL_NAMES_IN_CONTENT_TABLE_XPATH =
        "//table[contains(@class,'x-grid-table')]//tr[contains(@class,'x-grid-row')]//div[@class='admin-tree-description']/descendant::p";

    @FindBy(xpath = DELETE_BUTTON_XPATH)
    protected WebElement deleteButton;

    @FindBy(xpath = NEW_BUTTON_XPATH)
    private WebElement newButton;

    @FindBy(xpath = EDIT_BUTTON_XPATH)
    private WebElement editButton;

    @FindBy(xpath = DUPLICATE_BUTTON_XPATH)
    private WebElement duplicateButton;

    @FindBy(xpath = CONTENT_MANAGER_BUTTON)
    private WebElement contentManagerButton;

    @FindBy(xpath = OPEN_BUTTON_XPATH)
    private WebElement openButton;

    @FindBy(xpath = MOVE_BUTTON_XPATH)
    private WebElement moveButton;

    private String CHECKBOX_ROW_CHECKER = TD_CONTENT_NAME + "/..//div[@class='x-grid-row-checker']";

    private String DIV_CONTENT_NAME_IN_TABLE =
        "//div[contains(@class,'x-grid-cell-inner ')]//div[@class='admin-tree-description' and descendant::p[contains(.,'%s')]]";

    private ContentBrowseFilterPanel filterPanel;

    private ItemsSelectionPanel itemsSelectionPanel;

    /**
     * The constructor.
     *
     * @param session
     */
    public ContentBrowsePanel( TestSession session )
    {
        super( session );
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

    public ContentBrowseFilterPanel getFilterPanel()
    {
        if ( filterPanel == null )
        {
            filterPanel = new ContentBrowseFilterPanel( getSession() );
        }
        return filterPanel;
    }

    public ItemsSelectionPanel getItemSelectionPanel()
    {
        if ( itemsSelectionPanel == null )
        {
            itemsSelectionPanel = new ItemsSelectionPanel( getSession() );
        }
        return itemsSelectionPanel;
    }

    public ContentBrowsePanel goToAppHome()
    {
        contentManagerButton.click();
        sleep( 1000 );
        waituntilPageLoaded( Application.IMPLICITLY_WAIT );
        TestUtils.saveScreenshot(getSession(), NameHelper.uniqueName("gotoapphome"));
        return this;
    }

    /**
     * Gets all content names, showed in the contents-table.
     *
     * @return list of names.
     */
    public List<String> getContentNamesFromBrowsePanel()
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
        waitsForSpinnerNotVisible();
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

                if ( !this.<String>clickByExpander( path.toString() ) )
                {
                    getLogger().info( "content with name " + parentContent + "has no children! " );
                }
            }
        }
        waitsForSpinnerNotVisible();
        return this;
    }

    /**
     * Clicks by 'Delete' button in toolbar, confirms deleting when 'Confirm Deleting' dialog appears.
     */
    public void deleteSelected()
    {
        clickToolbarDelete().doDelete();
    }

    /**
     * @return
     */
    public DeleteContentDialog clickToolbarDelete()
    {
        boolean isEnabledDeleteButton = waitUntilElementEnabledNoException( By.xpath( DELETE_BUTTON_XPATH ), 2l );
        if ( !isEnabledDeleteButton )
        {
            throw new SaveOrUpdateException( "Impossible to delete content, because the 'Delete' button is disabled!" );
        }
        // 4. click on 'Delete' link and open a confirm dialog.
        deleteButton.click();
        DeleteContentDialog dialog = new DeleteContentDialog( getSession() );
        dialog.waitForOpened();
        return dialog;
    }

    public ContentBrowsePanel selectContentInTable( List<Content> contents )
    {
        waitAndCheckContent( contents );
        for ( Content content : contents )
        {
            if ( !isRowSelected( content.getPath().toString() ) )
            {
                clickCheckbox( content );
            }

        }
        return this;
    }

    public ContentBrowsePanel selectContentInTable( Content content )
    {
        waitAndCheckContent( content.getPath() );
        if ( !isRowSelected( content.getPath().toString() ) )
        {
            clickCheckbox( content );
        }
        return this;
    }

    public ContentBrowsePanel deSelectContentInTable( Content content )
    {
        waitAndCheckContent( content.getPath() );
        if ( isRowSelected( content.getPath().toString() ) )
        {
            clickCheckbox( content );
        }
        return this;
    }

    private void waitAndCheckContent( List<Content> contents )
    {
        for ( Content content : contents )
        {
            waitAndCheckContent( content.getPath() );
        }
    }

    private void waitAndCheckContent( ContentPath content )
    {
        boolean isExist = exists( content );

        if ( !isExist )
        {
            TestUtils.saveScreenshot( getSession(), content.getName() );
            throw new TestFrameworkException( "The content with name " + content.getName() + " was not found!" );
        }
    }

    /**
     * Clicks by a checkbox, linked with content and select row in the table.
     *
     * @param content
     */
    private ContentBrowsePanel clickCheckbox( Content content )
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

    public ContentBrowsePanel clickCheckboxAndSelectRow( ContentPath path )
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
      
        return this;
    }
    
    public ContentBrowsePanel pressSpacebarOnCheckbox( ContentPath path )
    {
        String contentCheckBoxXpath = String.format( CHECKBOX_ROW_CHECKER, path.toString() );
        getLogger().info( "tries to find content in table:" + path.toString() );

        getLogger().info( "Xpath of checkbox for content is :" + contentCheckBoxXpath );
        boolean isPresent = waitUntilVisibleNoException( By.xpath( contentCheckBoxXpath ), 3l );
        if ( !isPresent )
        {
            throw new SaveOrUpdateException( "checkbox for content: " + path.toString() + "was not found" );
        }
        
        findElement( By.xpath( contentCheckBoxXpath ) ).sendKeys(Keys.SPACE);
        getLogger().info( "check box was selected, content path is:" + path.toString() );     
        return this;
    }

    /**
     * Clicks by 'New' button and opens NewContentDialog
     *
     * @return
     */
    public NewContentDialog clickToolbarNew()
    {
        newButton.click();
        sleep(1000);
        TestUtils.saveScreenshot(getSession(),NameHelper.uniqueName("clicked"));
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
    public ContentBrowsePanel clickByParentCheckbox( ContentPath parentContentPath )
    {
        if ( parentContentPath.elementCount() == 0 )
        {
            return this;
        }

        // 1. select a checkbox and press the 'New' from toolbar.
        String spaceCheckBoxXpath = String.format( CHECKBOX_ROW_CHECKER, parentContentPath );
        boolean isPresentCheckbox = isDynamicElementPresent( By.xpath( spaceCheckBoxXpath ), 3 );
        if ( !isPresentCheckbox )
        {
            TestUtils.saveScreenshot( getSession(), "checkbox"+ parentContentPath.toString());
            throw new TestFrameworkException(
                "Time: " + TestUtils.timeNow() + "  wrong xpath:" + spaceCheckBoxXpath + " or Space with name " + parentContentPath +
                    " was not found!" );
        }
        getDriver().findElement( By.xpath( spaceCheckBoxXpath ) ).click();
        sleep(200);
        boolean isNewEnabled = waitUntilElementEnabledNoException( By.xpath( NEW_BUTTON_XPATH ), 2l );
        if ( !isNewEnabled )
        {
            throw new SaveOrUpdateException( "Impossible to open 'ContentWizardPanel', because the 'New' button is disabled!" );
        }
        return this;
    }

    /**
     * Clicks by row with content(not clicks by a checkbox)
     *
     * @param contentPath
     */
    public ContentBrowsePanel selectRowByContentPath( String contentPath )
    {
        String rowXpath = String.format( TD_CONTENT_NAME, contentPath );
        waitAndFind( By.xpath( rowXpath ) );

        Actions builder = new Actions( getDriver() );
        builder.click( findElement( By.xpath( rowXpath ) ) ).build().perform();
        sleep( 500 );
        return this;
    }

    public ItemViewPanelPage clickToolbarOpen()
    {
        openButton.click();
        ItemViewPanelPage cinfo = new ItemViewPanelPage( getSession() );
        return cinfo;
    }

    public ContentWizardPanel clickToolbarEdit()
    {
        editButton.click();
        ContentWizardPanel wizard = new ContentWizardPanel( getSession() );
        wizard.waitUntilWizardOpened();
        return wizard;
    }

    /**
     * Select a content and right click on  mouse, opens a Item view panel.
     *
     * @param content
     * @return {@ItemViewPanelPage} instance.
     */
    public ItemViewPanelPage doOpenContentFromContextMenu( Content content )
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
        sleep( 2000 );//mac mini issue
        boolean isGridLoaded = waitAndFind( By.xpath( TABLE_ITEM_XPATH ), timeout );
        if ( !isGridLoaded )
        {
            TestUtils.saveScreenshot( getSession(), "empty_grid_bug" );
            throw new TestFrameworkException(
                "content with xpath:" + TABLE_ITEM_XPATH + "was not visible, probably content was not loaded and grid is empty!" );
        }
    }

    public boolean isDeleteButtonEnabled()
    {
        return deleteButton.isEnabled();
    }

    public boolean isNewButtonEnabled()
    {
        return newButton.isEnabled();
    }

}
