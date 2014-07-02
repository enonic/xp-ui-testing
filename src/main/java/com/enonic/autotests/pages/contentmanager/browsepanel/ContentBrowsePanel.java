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

    // private static final String TABLE_ITEM_XPATH = "//h6[text()='BildeArkiv']";
    private static final String TABLE_ITEM_XPATH = "//table[contains(@class,'x-grid-table')]//tr[contains(@class,'x-grid-row')]";

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

    private String CONTEXT_MENU_ITEM = "//li[contains(@id,'api.ui.menu.MenuItem') and text()='%s']";

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
        TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "gotoapphome" ) );
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

    /**
     * @param contentPath
     * @return
     */
    public boolean exists( ContentPath contentPath )
    {
        return exists( contentPath, Application.DEFAULT_IMPLICITLY_WAIT );
    }

    /**
     * @param contentPath
     * @param timeout
     * @return
     */
    public boolean exists( ContentPath contentPath, int timeout )
    {
        String contentDescriptionXpath = String.format( DIV_CONTENT_NAME_IN_TABLE, contentPath.toString() );
        getLogger().info( "will verify is exists:" + contentDescriptionXpath );
        waitsForSpinnerNotVisible();
        boolean result = waitUntilVisibleNoException( By.xpath( contentDescriptionXpath ), timeout );
        getLogger().info( "content with path:" + contentDescriptionXpath + " isExists: " + result );
        TestUtils.saveScreenshot( getSession(), contentPath.getName() );
        return result;
    }

    /**
     * @param contentPath
     * @return
     */
    public ContentBrowsePanel unExpandContent( ContentPath contentPath )
    {
        if ( isRowExapnded( contentPath.toString() ) )
        {
            this.<String>clickByExpander( contentPath.toString() );
            getLogger().info( "content have been unexpanded: " + contentPath.toString() );
        }
        else
        {
            getLogger().info( "content was not expanded: " + contentPath.toString() );
        }
        sleep( 500 );
        return this;
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
        sleep( 700 );
        return this;
    }

    public ContentWizardPanel doubleclickOnContent( ContentPath contentPath )
    {
    	String rowXpath = String.format( TD_CONTENT_NAME, contentPath );
        boolean result = waitAndFind( By.xpath( rowXpath ) );
    	Actions builder = new Actions( getDriver() );
        builder.doubleClick( findElement( By.xpath( rowXpath ) ) ).build().perform();
        sleep( 500 );
        return new ContentWizardPanel(getSession());
    }

    public List<String> getChildNames( ContentPath contentPath )
    {
        List<String> listNames = new ArrayList<>();
        List<WebElement> elems = findElements( By.xpath( String.format( TD_CHILDREN_CONTENT_NAMES, contentPath.toString() + "/" ) ) );
        for ( WebElement el : elems )
        {
            listNames.add( el.getText() );
        }
        return listNames;
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
                clickCheckboxAndSelectRow( content.getPath() );
                sleep( 500 );
            }

        }
        return this;
    }

    public ContentBrowsePanel selectContentInTable( ContentPath contentPath )
    {
        waitAndCheckContent( contentPath );
        if ( !isRowSelected( contentPath.toString() ) )
        {
            clickCheckboxAndSelectRow( contentPath );
        }
        return this;
    }

    public ContentBrowsePanel deSelectContentInTable( ContentPath contentPath )
    {
        waitAndCheckContent( contentPath );
        if ( isRowSelected( contentPath.toString() ) )
        {
            clickCheckboxAndSelectRow( contentPath );
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

    private void waitAndCheckContent( ContentPath contentPath )
    {
        boolean isExist = exists( contentPath );

        if ( !isExist )
        {
            TestUtils.saveScreenshot( getSession(), contentPath.getName() );
            throw new TestFrameworkException( "The content with name " + contentPath.getName() + " was not found!" );
        }
    }

    /**
     * Clicks by a checkbox, linked with content and select row in the table.
     *
     * @param path
     */
    public ContentBrowsePanel clickCheckboxAndSelectRow( ContentPath path )
    {
        String contentCheckBoxXpath = String.format( CHECKBOX_ROW_CHECKER, path.toString() );
        getLogger().info( "tries to find content in table:" + path.toString() );

        getLogger().info( "Xpath of checkbox for content is :" + contentCheckBoxXpath );
        boolean isPresent = waitUntilVisibleNoException( By.xpath( contentCheckBoxXpath ), 3l );
        if ( !isPresent )
        {
            throw new SaveOrUpdateException( "checkbox for content: " + path.getName() + "was not found" );
        }
        sleep( 700 );
        waitUntilVisible( By.xpath( String.format( TD_CONTENT_NAME, path.toString() ) + "//div[@class='admin-tree-thumbnail']/img" ) );
        findElement( By.xpath( contentCheckBoxXpath ) ).click();
        getLogger().info( "check box was selected, content path is:" + path.toString() );

        return this;
    }

    /**
     * When row selected, there ia ability to click on Spacebar or ARROW_DOWN, ARROW_UP
     *
     * @param path
     * @param key
     * @return {@link ContentBrowsePanel} instance.
     */
    public ContentBrowsePanel pressKeyOnRow( ContentPath path, Keys key )
    {
        String contentCheckBoxXpath = String.format( CHECKBOX_ROW_CHECKER, path.toString() );
        getLogger().info( "tries to find content in table:" + path.toString() );

        getLogger().info( "Xpath of checkbox for content is :" + contentCheckBoxXpath );
        boolean isPresent = waitUntilVisibleNoException( By.xpath( contentCheckBoxXpath ), 3l );
        if ( !isPresent )
        {
            throw new SaveOrUpdateException( "checkbox for content: " + path.toString() + "was not found" );
        }
        findElement( By.xpath( contentCheckBoxXpath ) ).sendKeys( key );
        sleep( 500 );
        getLogger().info( "key was typed:" + key.toString() + " ,  content path is:" + path.toString() );
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
        sleep( 500 );
        NewContentDialog newContentDialog = new NewContentDialog( getSession() );
        boolean isLoaded = newContentDialog.waituntilDialogShowed( Application.EXPLICIT_3 );
        if ( !isLoaded )
        {
            throw new TestFrameworkException( "Error during add content, NewContentDialog dialog was not showed!" );
        }
        return newContentDialog;
    }

    /**
     * Expands all folders, that present in the 'content path' and  clicks by a checkbox related to parent folder for new content.
     *
     * @param contentPath
     */
    public ContentBrowsePanel clickByParentCheckbox( ContentPath contentPath )
    {
        if ( contentPath.elementCount() == 0 )
        {
            return this;
        }

        // 1. select a checkbox and press the 'New' from toolbar.
        String spaceCheckBoxXpath = String.format( CHECKBOX_ROW_CHECKER, contentPath.toString() );
        boolean isPresentCheckbox = isDynamicElementPresent( By.xpath( spaceCheckBoxXpath ), 3 );
        if ( !isPresentCheckbox )
        {
            TestUtils.saveScreenshot( getSession(), "checkbox" + contentPath.getName() );
            throw new TestFrameworkException(
                "Time: " + TestUtils.timeNow() + "  wrong xpath:" + spaceCheckBoxXpath + " or Space with name " + contentPath.toString() +
                    " was not found!" );
        }
        getDriver().findElement( By.xpath( spaceCheckBoxXpath ) ).click();
        sleep( 200 );
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
        boolean result = waitAndFind( By.xpath( rowXpath ) );
        Actions builder = new Actions( getDriver() );
        builder.click( findElement( By.xpath( rowXpath ) ) ).build().perform();
        sleep( 500 );
        return this;
    }

    /**
     * Clicks on 'Open' button in toolbar.
     *
     * @return {@link ItemViewPanelPage} instance.
     */
    public ItemViewPanelPage clickToolbarOpen()
    {
        openButton.click();
        ItemViewPanelPage cinfo = new ItemViewPanelPage( getSession() );
        return cinfo;
    }

    /**
     * Clicks on 'Edit' button in toolbar.
     *
     * @return {@link ContentWizardPanel} instance.
     */
    public ContentWizardPanel clickToolbarEdit()
    {
        editButton.click();
        ContentWizardPanel wizard = new ContentWizardPanel( getSession() );
        wizard.waitUntilWizardOpened();
        return wizard;
    }


    /**
     * Start to delete a content from menu in context menu.
     *
     * @param path
     * @return {@link DeleteContentDialog} instance.
     */
    public DeleteContentDialog selectDeleteFromContextMenu( ContentPath path )
    {
        openContextMenu( path );
        findElements( By.xpath( String.format( CONTEXT_MENU_ITEM, "Delete" ) ) ).get( 0 ).click();
        DeleteContentDialog dialog = new DeleteContentDialog( getSession() );
        dialog.waitForOpened();
        return dialog;
    }

    /**
     * Start to delete a content from menu in context menu.
     *
     * @param path
     * @return {@link DeleteContentDialog} instance.
     */
    public ContentWizardPanel selectEditFromContextMenu( ContentPath path )
    {
        openContextMenu( path );
        findElements( By.xpath( String.format( CONTEXT_MENU_ITEM, "Edit" ) ) ).get( 0 ).click();
        ContentWizardPanel wizard = new ContentWizardPanel( getSession() );
        wizard.waitUntilWizardOpened();
        return wizard;
    }


    /**
     * Opens context menu and select 'Open' item
     *
     * @param path
     * @return
     */
    public ItemViewPanelPage selectOpenFromContextMenu( ContentPath path )
    {
        openContextMenu( path );
        findElements( By.xpath( String.format( CONTEXT_MENU_ITEM, "Open" ) ) ).get( 0 ).click();
        ItemViewPanelPage cinfo = new ItemViewPanelPage( getSession() );
        return cinfo;
    }

    /**
     * Opens context menu and select 'New' item
     *
     * @param path
     * @return
     */
    public NewContentDialog selectNewFromContextMenu( ContentPath path )
    {
        openContextMenu( path );
        findElements( By.xpath( String.format( CONTEXT_MENU_ITEM, "New" ) ) ).get( 0 ).click();
        NewContentDialog newContentDialog = new NewContentDialog( getSession() );
        newContentDialog.waituntilDialogShowed( Application.EXPLICIT_3 );
        return newContentDialog;
    }

    /**
     * Clicks on content and opens context menu.
     *
     * @param path
     */
    private void openContextMenu( ContentPath path )
    {
        String fullName = path.toString();
        getLogger().info( "Full name of content: " + fullName );
        String contentDescriptionXpath = String.format( DIV_CONTENT_NAME_IN_TABLE, fullName );
        WebElement element = findElement( By.xpath( contentDescriptionXpath ) );
        Actions action = new Actions( getDriver() );

        action.contextClick( element ).build().perform();
        sleep( 100 );
    }

    /**
     * Waits until page loaded.
     *
     * @param timeout
     */
    public void waituntilPageLoaded( long timeout )
    {
        boolean isGridLoaded = waitAndFind( By.xpath( TABLE_ITEM_XPATH ), timeout );
        if ( !isGridLoaded )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "grid_bug" ) );
            throw new TestFrameworkException( "ContentBrowsePanel: content grid was not loaded!" );
        }
    }

    /**
     * @return true if 'Delete' button enabled, otherwise false.
     */
    public boolean isDeleteButtonEnabled()
    {
        return deleteButton.isEnabled();
    }

    /**
     * @return true if 'New' button enabled, otherwise false.
     */
    public boolean isNewButtonEnabled()
    {
        return newButton.isEnabled();
    }

}
