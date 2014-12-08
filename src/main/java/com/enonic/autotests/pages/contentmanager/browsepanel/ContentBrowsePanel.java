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
import com.enonic.autotests.services.NavigatorHelper;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.contentmanager.Content;
import com.enonic.wem.api.content.ContentPath;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * 'Content Manager' application, the Browse Panel page.
 */
public class ContentBrowsePanel
    extends BrowsePanel
{
    public static final String CONTENT_MANAGER_BUTTON = "//button[@id='api.app.bar.HomeButton' and text()='Content Manager']";


    public final String NEW_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.ContentBrowseToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='New']]";

    public final String DUPLICATE_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.ContentBrowseToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Duplicate']]";

    public final String OPEN_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.ContentBrowseToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Open']]";

    public final String MOVE_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.ContentBrowseToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Move']]";

    protected final String EDIT_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.ContentBrowseToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Edit']]";

    protected final String DELETE_BUTTON_XPATH =
        "//div[contains(@id,'app.browse.ContentBrowseToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Delete']]";

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

    private ContentBrowseFilterPanel filterPanel;

    private ItemsSelectionPanel itemsSelectionPanel;

    /**
     * The constructor.
     *
     * @param session {@link TestSession} instance
     */
    public ContentBrowsePanel( TestSession session )
    {
        super( session );
    }

    /**
     * @param session {@link TestSession} instance
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

    public ContentBrowsePanel refreshPanelInBrowser()
    {
        getDriver().navigate().refresh();
        NavigatorHelper.switchToIframe( getSession(), Application.CONTENT_MANAGER_FRAME_XPATH );
        return this;
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
        waitUntilPageLoaded( Application.IMPLICITLY_WAIT );
        TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "gotoapphome" ) );
        return this;
    }

    /**
     * Gets all content names, showed in the browse panel.
     *
     * @return list of names.
     */
    public List<String> getContentNamesFromBrowsePanel()
    {
        List<String> allNames = new ArrayList<>();
        List<WebElement> rows = getDriver().findElements( By.xpath( ALL_NAMES_FROM_BROWSE_PANEL_XPATH ) );
        for ( WebElement row : rows )
        {
            allNames.add( row.getAttribute( "title" ) );
        }
        return allNames;
    }


    /**
     * @param contentPath
     * @return
     */
    public boolean exists( ContentPath contentPath )
    {
        return exists( contentPath.toString(), false );
    }

    public boolean exists( ContentPath contentPath, boolean saveScreenshot )
    {
        return exists( contentPath.toString(), saveScreenshot );
    }


    public boolean doScrollAndFindContent( ContentPath contentPath )
    {
        return doScrollAndFindGridItem( contentPath.toString() );
    }


    /**
     * @param contentPath
     * @return {@link ContentBrowsePanel} instance
     */
    public ContentBrowsePanel unExpandContent( ContentPath contentPath )
    {

        if ( !doScrollAndFindContent( contentPath ) )
        {
            throw new TestFrameworkException( "unExpandContent: content was not found! " + contentPath );
        }

        if ( isRowExpanded( contentPath.toString() ) )
        {
            this.clickOnExpander( contentPath.toString() );
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
     * Clicks on expander icon near the content.
     *
     * @param contentPath
     */
    public ContentBrowsePanel expandContent( ContentPath contentPath )
    {
        if ( !doScrollAndFindContent( contentPath ) )
        {
            throw new TestFrameworkException( "expandContent: content was not found! " + contentPath );
        }

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

                if ( !this.clickOnExpander( path.toString() ) )
                {
                    getLogger().info( "content with name " + parentContent + "has no children! " );
                }
            }
        }
        waitsForSpinnerNotVisible();
        sleep( 700 );
        return this;
    }

    public ContentWizardPanel doubleClickOnContent( ContentPath contentPath )
    {
        sleep( 500 );
        String rowXpath = String.format( DIV_NAMES_VIEW, contentPath );
        boolean result = waitAndFind( By.xpath( rowXpath ) );
        Actions builder = new Actions( getDriver() );
        builder.doubleClick( findElement( By.xpath( rowXpath ) ) ).build().perform();
        sleep( 500 );
        return new ContentWizardPanel( getSession() );
    }

    /**
     * Gets content names, that are children for parent.
     *
     * @param contentPath parent content path
     * @return list of content names.
     */
    public List<String> getChildNames( ContentPath contentPath )
    {
        List<String> listNames = new ArrayList<>();
        String pElement =
            String.format( "//div[contains(@id,'api.app.NamesView')and child::p[contains(@title,'%s/')]]/p[@class='sub-name']",
                           contentPath );
        List<WebElement> elems = findElements( By.xpath( String.format( pElement, contentPath ) ) );
        for ( WebElement el : elems )
        {
            listNames.add( el.getAttribute( "title" ) );
        }
        return listNames;
    }

    /**
     * Clicks on 'Delete' button in toolbar, confirms deleting when 'Confirm Deleting' dialog appears.
     */
    public void deleteSelected()
    {
        clickToolbarDelete().doDelete();
    }

    /**
     * Clicks on 'Delete' button on toolbar.
     *
     * @return {@link DeleteContentDialog} instance.
     */
    public DeleteContentDialog clickToolbarDelete()
    {
        boolean isEnabledDeleteButton = waitUntilElementEnabledNoException( By.xpath( DELETE_BUTTON_XPATH ), 2l );
        if ( !isEnabledDeleteButton )
        {
            throw new SaveOrUpdateException( "Impossible to delete content, because the 'Delete' button is disabled!" );
        }
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
        boolean isExist = exists( contentPath, false );

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
        findElement( By.xpath( contentCheckBoxXpath ) ).click();
        getLogger().info( "check box was selected, content path is:" + path.toString() );

        return this;
    }

    public BrowsePanel pressKeyOnRow( ContentPath path, Keys key )
    {
        return pressKeyOnRow( path.toString(), key );
    }

    public boolean isExpanderPresent( ContentPath contentPath )
    {
        return isExpanderPresent( contentPath.toString() );
    }
    /**
     * Clicks on 'New' button and opens NewContentDialog
     *
     * @return {@link NewContentDialog} instance.
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
     * Expands all contents from 'content path' and clicks on a checkbox near the content, that will be parent for new content.
     *
     * @param contentPath {@link ContentPath} instance.
     */
    public ContentBrowsePanel clickOnParentCheckbox( ContentPath contentPath )
    {
        if ( contentPath.elementCount() == 0 )
        {
            return this;
        }

        if ( !doScrollAndFindContent( contentPath ) )
        {
            throw new TestFrameworkException( "content was not found: " + contentPath.toString() );
        }

        String checkBoxXpath = String.format( CHECKBOX_ROW_CHECKER, contentPath.toString() );
        boolean isPresentCheckbox = isDynamicElementPresent( By.xpath( checkBoxXpath ), 3 );
        if ( !isPresentCheckbox )
        {
            TestUtils.saveScreenshot( getSession(), "checkbox" + contentPath.getName() );
            throw new TestFrameworkException(
                "Time: " + TestUtils.timeNow() + "  wrong xpath:" + checkBoxXpath + " or item with name " + contentPath.toString() +
                    " was not found!" );
        }
        getDriver().findElement( By.xpath( checkBoxXpath ) ).click();
        sleep( 200 );
        boolean isNewEnabled = waitUntilElementEnabledNoException( By.xpath( NEW_BUTTON_XPATH ), 2l );
        if ( !isNewEnabled )
        {
            throw new SaveOrUpdateException( "Impossible to open 'ContentWizardPanel', because the 'New' button is disabled!" );
        }
        return this;
    }

    /**
     * Clicks on row with content(not clicks by a checkbox)
     *
     * @param contentPath {@link ContentPath} instance.
     */
    public ContentBrowsePanel selectRowByContentPath( String contentPath )
    {
        clickAndSelectRow( contentPath );
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
        return new ItemViewPanelPage( getSession() );
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
     * Right-clicks on a content and opens a context menu.
     *
     * @param contentPath
     */
    private void openContextMenu( ContentPath contentPath )
    {
        String path = contentPath.toString();
        getLogger().info( "opening a context menu, content path of content: " + path );
        String contentDescriptionXpath = String.format( DIV_NAMES_VIEW, path );
        WebElement element = findElement( By.xpath( contentDescriptionXpath ) );
        Actions action = new Actions( getDriver() );

        action.contextClick( element ).build().perform();
        sleep( 100 );
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
