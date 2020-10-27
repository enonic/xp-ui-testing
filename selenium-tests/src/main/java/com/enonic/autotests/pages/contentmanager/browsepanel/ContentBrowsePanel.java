package com.enonic.autotests.pages.contentmanager.browsepanel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.BrowsePanel;
import com.enonic.autotests.pages.contentmanager.ContentMenuItem;
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog;
import com.enonic.autotests.pages.contentmanager.ContentUnpublishDialog;
import com.enonic.autotests.pages.contentmanager.DuplicateContentDialog;
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentDetailsPanel;
import com.enonic.autotests.pages.contentmanager.issue.CreateIssueDialog;
import com.enonic.autotests.pages.contentmanager.issue.IssueListDialog;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage;
import com.enonic.autotests.services.NavigatorHelper;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.WaitHelper;
import com.enonic.autotests.vo.contentmanager.Content;
import com.enonic.xp.content.ContentPath;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * 'Content Studio' application, the Browse Panel page.
 */
public class ContentBrowsePanel
    extends BrowsePanel
{

    protected final String CONTENT_BROWSE_TOOLBAR_XPATH = "//div[contains(@id,'ContentBrowseToolbar')]";

    public static final String CONTENT_STUDIO_BUTTON = "//div[@id='AppIcon' ]//span[text()='Content Studio']";

    private final String BASE_PANEL_XPATH = "//div[contains(@id,'ContentBrowsePanel')]";

    private final String CONTENT_TREE_GRID = BASE_PANEL_XPATH + TREE_GREED;

    protected final String ALL_CONTENT_NAMES_FROM_TREE_GRID_XPATH = CONTENT_TREE_GRID + P_NAME;

    protected final String DISPLAY_NAMES_FROM_TREE_GRID_XPATH = CONTENT_TREE_GRID + H6_DISPLAY_NAME;

    private final String NEW_BUTTON_XPATH = BROWSE_TOOLBAR_XPATH + "//*[contains(@id, 'ActionButton') and child::span[text()='New...']]";

    private final String OPEN_TASKS_TOOLBAR_BUTTON = APP_BAR + "//button[contains(@id,'ShowIssuesDialogButton')]";

    private final String DUPLICATE_BUTTON_XPATH =
        BROWSE_TOOLBAR_XPATH + "//*[contains(@id, 'ActionButton') and child::span[text()='Duplicate...']]";

    private final String PREVIEW_BUTTON_XPATH =
        BROWSE_TOOLBAR_XPATH + "//*[contains(@id, 'ActionButton') and child::span[text()='Preview']]";

    private final String MOVE_BUTTON_XPATH = BROWSE_TOOLBAR_XPATH + "//*[contains(@id, 'ActionButton') and child::span[text()='Move...']]";

    private final String EDIT_BUTTON_XPATH =
        CONTENT_BROWSE_TOOLBAR_XPATH + "//*[contains(@id, 'ActionButton') and child::span[text()='Edit']]";

    private final String DELETE_BUTTON_XPATH =
        BROWSE_TOOLBAR_XPATH + "//*[contains(@id, 'ActionButton') and child::span[text()='Delete...']]";

    private final String SORT_BUTTON_XPATH = BROWSE_TOOLBAR_XPATH + "//*[contains(@id, 'ActionButton') and child::span[text()='Sort...']]";

    private final String MORE_BUTTON_XPATH = BROWSE_TOOLBAR_XPATH + "//div[contains(@id,'FoldButton')]";

    private final String PUBLISH_BUTTON_XPATH =
        BROWSE_TOOLBAR_XPATH + "//*[contains(@id, 'ActionButton') and child::span[text()='Publish...']]";

    private final String CREATE_TASK_BUTTON_XPATH =
        BROWSE_TOOLBAR_XPATH + "//*[contains(@id, 'ActionButton') and child::span[text()='Create Task...']]";

    private final String MARK_AS_READY_BUTTON_XPATH =
        BROWSE_TOOLBAR_XPATH + "//*[contains(@id, 'ActionButton') and child::span[text()='Mark as ready']]";

    private final String UNDO_DELETE_BUTTON_XPATH =
        BROWSE_TOOLBAR_XPATH + "//*[contains(@id, 'ActionButton') and child::span[text()='Undo delete']]";

    private final String PUBLISH_MENU_DROPDOWN_HANDLER =
        BROWSE_TOOLBAR_XPATH + "//div[contains(@id,'MenuButton')]" + DROP_DOWN_HANDLE_BUTTON;

    protected final String DETAILS_TOGGLE_BUTTON = BASE_PANEL_XPATH + "//button[contains(@id,'NonMobileContextPanelToggleButton')]";


    @FindBy(xpath = DELETE_BUTTON_XPATH)
    protected WebElement deleteButton;

    @FindBy(xpath = NEW_BUTTON_XPATH)
    private WebElement newButton;

    @FindBy(xpath = EDIT_BUTTON_XPATH)
    private WebElement editButton;

    @FindBy(xpath = DUPLICATE_BUTTON_XPATH)
    private WebElement duplicateButton;

    @FindBy(xpath = CONTENT_STUDIO_BUTTON)
    private WebElement contentStudioButton;

    @FindBy(xpath = PREVIEW_BUTTON_XPATH)
    private WebElement previewButton;

    @FindBy(xpath = SORT_BUTTON_XPATH)
    private WebElement sortButton;

    @FindBy(xpath = MOVE_BUTTON_XPATH)
    private WebElement moveButton;

    @FindBy(xpath = PUBLISH_BUTTON_XPATH)
    private WebElement publishButton;

    @FindBy(xpath = MARK_AS_READY_BUTTON_XPATH)
    private WebElement markAsReadyButton;

    @FindBy(xpath = PUBLISH_MENU_DROPDOWN_HANDLER)
    private WebElement publishMenuDropDownHandler;

    @FindBy(xpath = OPEN_TASKS_TOOLBAR_BUTTON)
    private WebElement openTasksButton;

    @FindBy(xpath = DETAILS_TOGGLE_BUTTON)
    WebElement detailsToggleButton;

    @FindBy(xpath = UNDO_DELETE_BUTTON_XPATH)
    private WebElement undoDeleteButton;

    private ContentBrowseFilterPanel filterPanel;

    private ContentBrowseItemPanel contentBrowseItemPanel;

    private ContentDetailsPanel contentDetailsPanel;

    /**
     * The constructor.
     *
     * @param session {@link TestSession} instance
     */
    public ContentBrowsePanel( TestSession session )
    {
        super( session );
    }

    public ContentBrowsePanel showPublishMenu()
    {
        waitUntilElementEnabled( By.xpath( PUBLISH_MENU_DROPDOWN_HANDLER ), Application.EXPLICIT_NORMAL );
        try
        {
            publishMenuDropDownHandler.click();
        }
        catch ( Exception e )
        {
            sleep( 500 );
            publishMenuDropDownHandler.click();
        }
        sleep( 300 );
        return this;
    }

    public boolean hasAssignedIssues()
    {
        return waitAndCheckAttrValue( openTasksButton, "class", "has-assigned-issues", 1l );
    }

    protected boolean waitForAssignedIssuesIconNotVisible()
    {
        return WaitHelper.waitAttrHasNoValue( getDriver(), openTasksButton, "class", "has-assigned-issues", Application.EXPLICIT_NORMAL );
    }

    public String getTextInIssuesButton()
    {
        return getDisplayedString( "//button[contains(@id,'ShowIssuesDialogButton')]//span" );
    }

    /**
     * Keyboard shortcut to open the 'New content' dialog
     */
    public void pressNewContentKeyboardShortcut()
    {
        //buildActions().sendKeys( Keys.chord( Keys.ALT, "n" ) ).build().perform();
        buildActions().keyDown( Keys.ALT ).sendKeys( "n" ).build().perform();
        sleep( 500 );
    }

    /**
     * Keyboard shortcut to 'Edit selected content'
     */
    public ContentWizardPanel pressEditSelectedContentKeyboardShortcut()
    {
        String os = System.getProperty( "os.name" ).toLowerCase();
        sleep( 2000 );
        if ( os.indexOf( "mac" ) >= 0 )
        {
            buildActions().keyDown( Keys.COMMAND ).sendKeys( "e" ).build().perform();
            //buildActions().sendKeys( Keys.chord( Keys.COMMAND, "e" ) ).build().perform();
        }
        else
        {
            buildActions().keyDown( Keys.CONTROL ).sendKeys( "e" ).build().perform();
            //buildActions().sendKeys( Keys.chord( Keys.CONTROL, "e" ) ).build().perform();
        }
        sleep( 2000 );
        switchToContentWizardTabBySelectedContent();
        ContentWizardPanel wizard = new ContentWizardPanel( getSession() );
        wizard.waitUntilWizardOpened();
        waitInvisibilityOfSpinner( Application.EXPLICIT_LONG );
        wizard.setInLiveEditFrame( false );
        return wizard;
    }

    /**
     * Keyboard shortcut to 'Delete selected content'
     */
    public DeleteContentDialog pressDeleteSelectedContentKeyboardShortcut()
    {
        String os = System.getProperty( "os.name" ).toLowerCase();

        if ( os.indexOf( "mac" ) >= 0 )
        {
            buildActions().keyDown( Keys.COMMAND ).sendKeys( Keys.DELETE ).build().perform();
        }
        else
        {
            buildActions().keyDown( Keys.CONTROL ).sendKeys( Keys.DELETE ).build().perform();
        }
        DeleteContentDialog deleteContentDialog = new DeleteContentDialog( getSession() );
        return deleteContentDialog;
    }


    public ContentPublishDialog selectPublishTreeMenuItem()
    {
        showPublishMenu();
        if ( !isElementDisplayed( PUBLISH_TREE_MENU_ITEM ) )
        {
            saveScreenshot( "err_publish_menu_item" );
            throw new TestFrameworkException( "menu item was not found!" + "Publish Tree" );
        }
        getDisplayedElement( By.xpath( PUBLISH_TREE_MENU_ITEM ) ).click();
        ContentPublishDialog dialog = new ContentPublishDialog( getSession() );
        dialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        return dialog;
    }

    public ContentPublishDialog clickOnPublishMenuItem()
    {
        if ( !isPublishMenuItemEnabled() )
        {
            throw new TestFrameworkException( "menu item is not enabled" );
        }
        getDisplayedElement( By.xpath( PUBLISH_MENU_ITEM ) ).click();
        sleep( 300 );
        ContentPublishDialog dialog = new ContentPublishDialog( getSession() );
        dialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        return dialog;
    }

    public ContentUnpublishDialog selectUnPublishMenuItem()
    {
        if ( !isUnPublishMenuItemEnabled() )
        {
            saveScreenshot( "err_unpublish_menu_item" );
            throw new TestFrameworkException( "menu item was not found!" );
        }
        getDisplayedElement( By.xpath( UNPUBLISH_MENU_ITEM ) ).click();
        sleep( 300 );
        ContentUnpublishDialog dialog = new ContentUnpublishDialog( getSession() );
        dialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        return dialog;
    }

    public void clickOnMarkAsReadyMenuItem()
    {
        if ( !isMarkAsReadyMenuItemEnabled() )
        {
            saveScreenshot( "err_mark_as_ready_menu_item" );
            throw new TestFrameworkException( "menu item is not enabled" );
        }
        getDisplayedElement( By.xpath( MARK_AS_READY_MENU_ITEM ) ).click();
        sleep( 1000 );

    }

    public CreateIssueDialog clickOnCreateTaskMenuItem()
    {
        if ( !isCreateIssueMenuItemEnabled() )
        {
            saveScreenshot( "err_create_task_menu_item" );
            throw new TestFrameworkException( "menu item is disabled!" );
        }
        getDisplayedElement( By.xpath( CREATE_TASK_MENU_ITEM ) ).click();
        CreateIssueDialog dialog = new CreateIssueDialog( getSession() );
        dialog.waitForOpened();
        return dialog;
    }

    public boolean isPublishMenuAvailable()
    {
        if ( !isElementDisplayed( PUBLISH_MENU_DROPDOWN_HANDLER ) )
        {
            saveScreenshot( "err_publish_dropdown_handler" );
            throw new TestFrameworkException( "dropdown handler for publish menu is not displayed" );
        }
        return !getAttribute( getDisplayedElement( By.xpath( PUBLISH_MENU_DROPDOWN_HANDLER ) ), "class",
                              Application.EXPLICIT_NORMAL ).contains( "disabled" );
    }

    public boolean isPublishTreeMenuItemEnabled()
    {
        if ( !isElementDisplayed( PUBLISH_TREE_MENU_ITEM ) )
        {
            saveScreenshot( "err_publish_tree_menu_item_not_visible " );
            throw new TestFrameworkException( "'publish tree' menu item is not visible!" );
        }
        return !getAttribute( getDisplayedElement( By.xpath( PUBLISH_TREE_MENU_ITEM ) ), "class", Application.EXPLICIT_NORMAL ).contains(
            "disabled" );
    }

    public boolean isUnPublishMenuItemEnabled()
    {
        if ( !isElementDisplayed( UNPUBLISH_MENU_ITEM ) )
        {
            saveScreenshot( "err_unpublish_menu_item_not_visible " );
            throw new TestFrameworkException( "'unpublish' menu item is not visible!" );
        }
        return !getAttribute( getDisplayedElement( By.xpath( UNPUBLISH_MENU_ITEM ) ), "class", Application.EXPLICIT_NORMAL ).contains(
            "disabled" );
    }

    public boolean isPublishMenuItemEnabled()
    {
        if ( !isElementDisplayed( PUBLISH_MENU_ITEM ) )
        {
            saveScreenshot( "err_publish_menu_item_not_enabled " );
            throw new TestFrameworkException( "'unpublish' menu item is not visible!" );
        }
        return !getAttribute( getDisplayedElement( By.xpath( PUBLISH_MENU_ITEM ) ), "class", Application.EXPLICIT_NORMAL ).contains(
            "disabled" );
    }


    public boolean isMarkAsReadyMenuItemEnabled()
    {
        if ( !isElementDisplayed( MARK_AS_READY_MENU_ITEM ) )
        {
            saveScreenshot( "err_mark_as_ready_menu_item_not_visible " );
            throw new TestFrameworkException( "'mark as ready' menu item is not visible!" );
        }
        return !getAttribute( getDisplayedElement( By.xpath( MARK_AS_READY_MENU_ITEM ) ), "class", Application.EXPLICIT_NORMAL ).contains(
            "disabled" );
    }

    public boolean isCreateIssueMenuItemEnabled()
    {
        if ( !isElementDisplayed( CREATE_TASK_MENU_ITEM ) )
        {
            saveScreenshot( "err_create_issue_menu_item_not_visible " );
            throw new TestFrameworkException( "'create issue' menu item is not visible!" );
        }
        return !getAttribute( getDisplayedElement( By.xpath( CREATE_TASK_MENU_ITEM ) ), "class", Application.EXPLICIT_NORMAL ).contains(
            "disabled" );
    }

    public ContentDetailsPanel getContentDetailsPanel()
    {
        if ( contentDetailsPanel == null )
        {
            contentDetailsPanel = new ContentDetailsPanel( getSession() );
        }
        return contentDetailsPanel;
    }

    public ContentBrowseItemPanel getContentBrowseItemPanel()
    {
        if ( contentBrowseItemPanel == null )
        {
            contentBrowseItemPanel = new ContentBrowseItemPanel( getSession() );
        }
        return contentBrowseItemPanel;
    }

    public ContentDetailsPanel openContentDetailsPanel()
    {
        if ( !getContentDetailsPanel().isDisplayed() )
        {
            clickOnDetailsToggleButton();
        }
        return contentDetailsPanel;
    }

    public ContentBrowsePanel clickOnDetailsToggleButton()
    {
        boolean result = waitUntilClickableNoException( By.xpath( DETAILS_TOGGLE_BUTTON ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            saveScreenshot( "err_toggle_button" );
            throw new TestFrameworkException( "DetailsToggle button is not clickable!" );
        }
        detailsToggleButton.click();
        sleep( 1000 );
        waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        return this;
    }

    public boolean isDetailsPanelToggleButtonDisplayed()
    {
        return isElementDisplayed( DETAILS_TOGGLE_BUTTON );
    }

    public boolean isFilterPanelShown()
    {
        return getFilterPanel().isFilterPanelDisplayed();
    }

    public boolean isContentInvalid( String contentName )
    {
        String contentInGrid = String.format( CONTENT_SUMMARY_VIEWER, contentName );
        if ( !isElementDisplayed( contentInGrid ) )
        {
            saveScreenshot( "err_" + contentName );
            throw new TestFrameworkException( "content with path was not found or browsePanel was not displayed!" + contentName );
        }
        WebElement element = getDisplayedElement( By.xpath( contentInGrid ) );
        return waitAndCheckAttrValue( element, "class", "invalid", Application.EXPLICIT_NORMAL );
    }

    public ContentWizardPanel selectAndOpenContentFromToolbarMenu( Content content )
    {
        doClearSelection();
        filterPanel.typeSearchText( content.getName() );
        ( (ContentBrowsePanel) clickCheckboxAndSelectRow( content.getName() ) ).clickToolbarEditAndSwitchToWizardTab();
        ContentWizardPanel wizard = new ContentWizardPanel( getSession() );
        wizard.waitUntilWizardOpened();
        return wizard;
    }

    public String getContentStatus( String contentName )
    {
        String statusXpath = String.format( CONTENT_TREE_GRID + SLICK_ROW_BY_NAME + "//div[contains(@class,'r3')]", contentName );
        if ( !isElementDisplayed( statusXpath ) )
        {
            saveScreenshot( NameHelper.uniqueName( "content_status_not_found" ) );
            throw new TestFrameworkException( "content status was not found in the grid: " + contentName );

        }

        return findElement( By.xpath( statusXpath ) ).getText().replace( "\n", " " );
    }

    public ContentBrowseFilterPanel getFilterPanel()
    {
        if ( filterPanel == null )
        {
            filterPanel = new ContentBrowseFilterPanel( getSession() );
        }
        return filterPanel;
    }

    public ContentBrowsePanel pressAppHomeButton()
    {
        contentStudioButton.click();
        sleep( 500 );
        waitUntilPageLoaded( Application.EXPLICIT_NORMAL );
        return this;
    }

    /**
     * Gets all content names, showed in the browse panel.
     *
     * @return list of names.
     */
    public List<String> getContentNamesFromGrid()
    {
        return this.getDisplayedStrings( By.xpath( ALL_CONTENT_NAMES_FROM_TREE_GRID_XPATH ) );
    }

    public List<String> getContentDisplayNamesFromGrid()
    {
        List<WebElement> rows = findElements( By.xpath( DISPLAY_NAMES_FROM_TREE_GRID_XPATH ) );
        return rows.stream().filter( e -> !e.getText().isEmpty() ).map( WebElement::getText ).collect( Collectors.toList() );
    }

    public List<String> getChildContentNamesFromTreeGrid( String parentName )
    {
        List<WebElement> rows = findElements( By.xpath( ALL_CONTENT_NAMES_FROM_TREE_GRID_XPATH ) );
        return rows.stream().filter( e -> !e.getText().contains( parentName ) && !e.getText().isEmpty() ).map(
            WebElement::getText ).collect( Collectors.toList() );
    }

    public List<String> getChildContentDisplayNamesFromTreeGrid( String parentDisplayName )
    {
        List<WebElement> rows = findElements( By.xpath( DISPLAY_NAMES_FROM_TREE_GRID_XPATH ) );
        return rows.stream().filter( e -> !e.getText().contains( parentDisplayName ) && !e.getText().isEmpty() ).map(
            WebElement::getText ).collect( Collectors.toList() );
    }


    public boolean exists( ContentPath contentPath, boolean saveScreenshot )
    {
        return exists( contentPath.toString(), saveScreenshot );
    }


    public ContentBrowsePanel unExpandContent( ContentPath contentPath )
    {
        if ( !doScrollAndFindGridItem( contentPath.toString() ) )
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
        if ( !doScrollAndFindGridItem( contentPath.getName() ) )
        {
            saveScreenshot( "err_" + contentPath.getName() );
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
                getLogger().info( "expandContent,  name of folder : " + path.getName() );
                if ( isRowExpanded( path.getName() ) == null )
                {
                    throw new TestFrameworkException( "Expander for the " + path.getName() + "  was not found! Or content has no child" );
                }
                if ( !isRowExpanded( path.getName() ) )
                {
                    this.clickOnExpander( path.getName() );
                    getLogger().info( "content with name " + parentContent + "was expanded! " );
                }
            }
        }
        waitsForSpinnerNotVisible();
        sleep( 300 );
        return this;
    }

    /**
     * Clicks on 'Delete' button on toolbar.
     *
     * @return {@link DeleteContentDialog} instance.
     */
    public DeleteContentDialog clickToolbarDelete()
    {
        waitUntilVisibleNoException( By.xpath( DELETE_BUTTON_XPATH ), Application.EXPLICIT_NORMAL );
        boolean isEnabledDeleteButton = waitUntilElementEnabledNoException( By.xpath( DELETE_BUTTON_XPATH ), 2l );
        if ( !isEnabledDeleteButton )
        {
            saveScreenshot( "err_delete_button" );
            throw new SaveOrUpdateException( "Impossible to delete content, because the 'Delete' button is disabled!" );
        }
        deleteButton.click();
        DeleteContentDialog dialog = new DeleteContentDialog( getSession() );
        dialog.waitForOpened();
        sleep( 500 );
        return dialog;
    }

    public ContentPublishDialog clickToolbarPublish()
    {
        waitForPublishButtonVisible( Application.EXPLICIT_NORMAL );
        if ( !isElementDisplayed( PUBLISH_BUTTON_XPATH ) )
        {
            saveScreenshot( "err_publish_button" );
            throw new TestFrameworkException( "Publish button is not visible on the toolbar" );
        }
        publishButton.click();
        ContentPublishDialog dialog = new ContentPublishDialog( getSession() );
        dialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        return dialog;
    }

    public void clickOnMarkAsReadyOnToolbarAndConfirm()
    {
        waitUntilVisibleNoException( By.xpath( MARK_AS_READY_BUTTON_XPATH ), Application.EXPLICIT_NORMAL );
        if ( !isElementDisplayed( MARK_AS_READY_BUTTON_XPATH ) )
        {
            saveScreenshot( "err_mark_as_ready_button1" );
            throw new TestFrameworkException( "Mark As Ready button is not visible on the toolbar" );
        }
        markAsReadyButton.click();
        ConfirmationDialog dialog = new ConfirmationDialog( getSession() );
        dialog.waitForOpened();
        dialog.pressYesButton();
        dialog.waitForClosed();
        sleep( 500 );
    }

    //waits for Mark as Ready button gets visible on the toolbar, then clicks on it
    public void clickOnMarkAsReadySingleContent()
    {
        waitUntilVisibleNoException( By.xpath( MARK_AS_READY_BUTTON_XPATH ), Application.EXPLICIT_NORMAL );
        if ( !isElementDisplayed( MARK_AS_READY_BUTTON_XPATH ) )
        {
            saveScreenshot( "err_mark_as_ready_button2" );
            throw new TestFrameworkException( "Mark As Ready button is not visible on the toolbar" );
        }
        markAsReadyButton.click();
        sleep( 500 );
    }

    public ContentBrowsePanel clickToolbarUndodelete()
    {
        waitUntilVisibleNoException( By.xpath( UNDO_DELETE_BUTTON_XPATH ), Application.EXPLICIT_NORMAL );
        undoDeleteButton.click();
        sleep( 500 );
        waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        return this;
    }

    public ContentBrowsePanel selectContentInTable( List<String> contentNames )
    {
        contentNames.stream().forEach( name -> selectContentInTable( name ) );
        return this;
    }

    public ContentBrowsePanel selectContentInTable( String... contentNames )
    {
        Arrays.asList( contentNames ).forEach( name -> selectContentInTable( name ) );
        return this;
    }

    public ContentBrowsePanel selectContentInTable( String contentName )
    {
        boolean exist = doScrollAndFindGridItem( contentName );
        if ( !exist )
        {
            saveScreenshot( "err_not_found" + contentName );
            throw new TestFrameworkException( "item was not found: " + contentName );
        }
        if ( !isRowSelected( contentName ) )
        {
            clickCheckboxAndSelectRow( contentName );
        }
        return this;
    }

    public ContentBrowsePanel deSelectContentInTable( String contentName )
    {
        boolean exist = doScrollAndFindGridItem( contentName );
        if ( isRowSelected( contentName ) )
        {
            clickCheckboxAndSelectRow( contentName );
        }
        return this;
    }

    public BrowsePanel pressKeyOnRow( ContentPath path, Keys key )
    {
        boolean result = doScrollAndFindGridItem( path.toString() );
        if ( !result )
        {
            throw new TestFrameworkException( "grid item was not found! " + path.toString() );
        }
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
        boolean isClickable = waitUntilElementEnabledNoException( By.xpath( NEW_BUTTON_XPATH ), Application.EXPLICIT_NORMAL );
        if ( !isClickable )
        {
            saveScreenshot( "err_new_button" );
            throw new TestFrameworkException( "button 'new' is not clickable!" );
        }
        newButton.click();
        sleep( 200 );
        NewContentDialog newContentDialog = new NewContentDialog( getSession() );
        boolean isLoaded = newContentDialog.waitUntilDialogLoaded( Application.EXPLICIT_NORMAL );
        if ( !isLoaded )
        {
            saveScreenshot( "err_open_new_content_dialog" );
            throw new TestFrameworkException( "NewContentDialog dialog is not loaded!" );
        }
        return newContentDialog;
    }

    public ContentBrowsePanel clickToolbarDuplicate()
    {
        duplicateButton.click();
        sleep( 1000 );
        return this;
    }

    public SortContentDialog clickToolbarSort()
    {
        if ( !isElementDisplayed( SORT_BUTTON_XPATH ) )
        {
            clickOnFoldButton();
        }
        sortButton.click();
        sleep( 500 );
        SortContentDialog sortContentDialog = new SortContentDialog( getSession() );
        sortContentDialog.waitForLoaded( Application.EXPLICIT_NORMAL );
        return sortContentDialog;
    }

    private void clickOnFoldButton()
    {
        if ( !isElementDisplayed( MORE_BUTTON_XPATH ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_fold_button" ) );
            throw new TestFrameworkException( "'More' button not found on the toolbar" );
        }
        getDisplayedElement( By.xpath( MORE_BUTTON_XPATH ) ).click();
        sleep( 500 );
    }

    public boolean isMoreButtonPresent()
    {
        return isElementDisplayed( MORE_BUTTON_XPATH );
    }

    public MoveContentDialog clickToolbarMove()
    {
        if ( !isElementDisplayed( MOVE_BUTTON_XPATH ) )
        {
            clickOnFoldButton();
        }
        waitForMoveButtonEnabled( Application.EXPLICIT_NORMAL );
        moveButton.click();
        sleep( 500 );
        MoveContentDialog moveContentDialog = new MoveContentDialog( getSession() );
        moveContentDialog.waitUntilDialogLoaded();
        return moveContentDialog;
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
        if ( !doScrollAndFindGridItem( contentPath.toString() ) )
        {
            throw new TestFrameworkException( "content was not found: " + contentPath.toString() );
        }

        String checkBoxXpath = String.format( ROW_CHECKBOX_BY_NAME, contentPath.toString() );
        boolean isPresentCheckbox = isDynamicElementPresent( By.xpath( checkBoxXpath ), 3 );
        if ( !isPresentCheckbox )
        {
            saveScreenshot( "checkbox" + contentPath.getName() );
            throw new TestFrameworkException( "wrong xpath:" + checkBoxXpath );
        }
        findElement( By.xpath( checkBoxXpath ) ).click();
        sleep( 200 );
        boolean isNewEnabled = waitUntilElementEnabledNoException( By.xpath( NEW_BUTTON_XPATH ), 2l );
        if ( !isNewEnabled )
        {
            throw new SaveOrUpdateException( "Impossible to open 'ContentWizardPanel', because the 'New' button is disabled!" );
        }
        return this;
    }

    /**
     * Clicks on 'Open' button in toolbar.
     *
     * @return {@link ItemViewPanelPage} instance.
     */
    public ItemViewPanelPage clickToolbarPreview()
    {
        if ( !isElementDisplayed( PREVIEW_BUTTON_XPATH ) )
        {
            clickOnFoldButton();
        }
        previewButton.click();
        sleep( 1000 );
        return new ItemViewPanelPage( getSession() );
    }

    @Override
    public ContentWizardPanel clickToolbarEdit()
    {
        return clickToolbarEditAndSwitchToWizardTab();
    }

    public IssueListDialog clickOnToolbarShowIssues()
    {
        openTasksButton.click();
        sleep( 700 );
        IssueListDialog issueListDialog = new IssueListDialog( getSession() );
        issueListDialog.waitForOpened();
        return issueListDialog;
    }

    public ContentWizardPanel clickToolbarEditAndSwitchToWizardTab()
    {
        boolean isEditButtonEnabled = waitUntilElementEnabledNoException( By.xpath( EDIT_BUTTON_XPATH ), Application.EXPLICIT_NORMAL );
        if ( !isEditButtonEnabled )
        {
            saveScreenshot( "err_edit_button" );
            throw new TestFrameworkException( "Edit button should be enabled!" );
        }
        editButton.click();
        sleep( 1000 );
        switchToContentWizardTabBySelectedContent();
        ContentWizardPanel wizard = new ContentWizardPanel( getSession() );
        wizard.waitUntilWizardOpened();
        waitInvisibilityOfSpinner( 7l );
        wizard.setInLiveEditFrame( false );
        return wizard;
    }

    public ContentWizardPanel switchToContentWizardTabBySelectedContent()
    {
        String contentTeeGridId = getDisplayedElement( By.xpath( CONTENT_TREE_GRID ) ).getAttribute( "id" );
        String contentId = (String) getJavaScriptExecutor().executeScript(
            "return libAdmin.store.map.get('ElementRegistry').elements.get(arguments[0]).getFirstSelectedItem().getId()",
            contentTeeGridId );
        NavigatorHelper.switchToBrowserTab( getSession(), contentId );
        return new ContentWizardPanel( getSession() );
    }

    public ContentWizardPanel switchToBrowserTabByTitle( String contentDisplayName )
    {
        NavigatorHelper.switchToBrowserTabByTitle( getSession(), contentDisplayName );
        waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        return new ContentWizardPanel( getSession() );
    }

    public boolean is_404_TabWindowPresent()
    {
        return NavigatorHelper.isWindowPresent( getSession(), Application.ERROR_MESSAGE_404 );
    }

    /**
     * Selects 'Delete' in a context menu.
     *
     * @param contentName
     * @return {@link DeleteContentDialog} instance.
     */
    public DeleteContentDialog selectDeleteFromContextMenu( String contentName )
    {
        if ( !doScrollAndFindGridItem( contentName ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_" + contentName ) );
            throw new TestFrameworkException( "content was not found: " + contentName );
        }
        openContextMenu( contentName );
        String deleteMenuItem = String.format( CONTEXT_MENU_ITEM, "Delete..." );
        if ( !waitIsElementEnabled( findElement( By.xpath( deleteMenuItem ) ), 2 ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_context-publish" ) );
            throw new TestFrameworkException( "Delete context-menu item is disabled!" );
        }
        getDisplayedElement( By.xpath( deleteMenuItem ) ).click();
        DeleteContentDialog dialog = new DeleteContentDialog( getSession() );
        dialog.waitForOpened();
        return dialog;
    }

    public ContentPublishDialog selectPublishFromContextMenu( String contentName )
    {
        getFilterPanel().clickOnCleanFilter().typeSearchText( contentName );
        openContextMenu( contentName );
        String publishMenuItem = String.format( CONTEXT_MENU_ITEM, ContentMenuItem.PUBLISH.getName() );

        if ( !isElementDisplayed( publishMenuItem ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_context-publish" ) );
            throw new TestFrameworkException( "publish context-menu item is not visible!" );
        }
        if ( !waitIsElementEnabled( findElement( By.xpath( publishMenuItem ) ), 2 ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_context-publish" ) );
            throw new TestFrameworkException( "publish context-menu item is disabled!" );
        }
        getDisplayedElement( By.xpath( publishMenuItem ) ).click();
        ContentPublishDialog dialog = new ContentPublishDialog( getSession() );
        dialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        sleep( 500 );
        return dialog;
    }

    public ContentUnpublishDialog selectUnPublishFromContextMenu( String contentName )
    {
        openContextMenu( contentName );
        String unpublishMenuItem = String.format( CONTEXT_MENU_ITEM, ContentMenuItem.UNPUBLISH.getName() );
        if ( !isElementDisplayed( unpublishMenuItem ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_context-unpublish" ) );
            throw new TestFrameworkException( "unpublish context-menu item is not visible!" );
        }
        if ( !waitIsElementEnabled( findElement( By.xpath( unpublishMenuItem ) ), 2 ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_context-unpublish" ) );
            throw new TestFrameworkException( "unpublish context-menu item is disabled!" );
        }
        getDisplayedElement( By.xpath( unpublishMenuItem ) ).click();
        ContentUnpublishDialog dialog = new ContentUnpublishDialog( getSession() );
        dialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        sleep( 500 );
        return dialog;
    }

    public ContentBrowsePanel selectUndoDeleteFromContextMenu( String contentName )
    {
        openContextMenu( contentName );
        String undoDeleteMenuItem = String.format( CONTEXT_MENU_ITEM, ContentMenuItem.UNDO_DELETE.getName() );
        if ( !isElementDisplayed( undoDeleteMenuItem ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_context-undo-delete" ) );
            throw new TestFrameworkException( "undo delete context-menu item is not visible!" );
        }
        getDisplayedElement( By.xpath( undoDeleteMenuItem ) ).click();
        sleep( 500 );
        return this;
    }

    public ContentBrowsePanel doDuplicateContent( String contentName )
    {
        this.selectDuplicateFromContextMenu( contentName );
        DuplicateContentDialog dialog = new DuplicateContentDialog( getSession() );
        dialog.waitForOpened();
        dialog.clickOnDuplicateButton();
        dialog.waitForClosed();
        return this;
    }

    public ContentBrowsePanel selectDuplicateFromContextMenu( String contentName )
    {
        getFilterPanel().clickOnCleanFilter().typeSearchText( contentName );
        sleep( 1000 );
        openContextMenu( contentName );
        String duplicateMenuItem = String.format( CONTEXT_MENU_ITEM, ContentMenuItem.DUPLICATE.getName() );
        if ( !isElementDisplayed( duplicateMenuItem ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_context-duplicate" ) );
            throw new TestFrameworkException( "duplicate context-menu item is not visible!" );
        }
        if ( !waitIsElementEnabled( findElement( By.xpath( duplicateMenuItem ) ), 2 ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_context-publish" ) );
            throw new TestFrameworkException( "Duplicate context-menu item is disabled!" );
        }
        getDisplayedElement( By.xpath( duplicateMenuItem ) ).click();
        sleep( 1000 );
        return this;
    }

    /**
     * Selects 'Edit' in a context menu.
     *
     * @param contentName
     * @return {@link ContentWizardPanel} instance.
     */
    public ContentWizardPanel selectEditFromContextMenu( String contentName )
    {
        openContextMenu( contentName );
        String editMenuItem = String.format( CONTEXT_MENU_ITEM, ContentMenuItem.EDIT.getName() );
        if ( !isElementDisplayed( editMenuItem ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_context-edit" ) );
            throw new TestFrameworkException( "'edit' context-menu item is not visible!" );
        }
        if ( !waitIsElementEnabled( findElement( By.xpath( editMenuItem ) ), 2 ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_context-publish" ) );
            throw new TestFrameworkException( "Edit context-menu item is disabled!" );
        }
        getDisplayedElement( By.xpath( editMenuItem ) ).click();
        sleep( 300 );
        switchToNewWizardTab();
        ContentWizardPanel wizard = new ContentWizardPanel( getSession() );
        wizard.waitUntilWizardOpened();
        return wizard;
    }

    public SortContentDialog selectSortInContextMenu( String contentName )
    {
        openContextMenu( contentName );
        String sortMenuItem = String.format( CONTEXT_MENU_ITEM, ContentMenuItem.SORT.getName() );
        if ( !waitUntilVisibleNoException( By.xpath( sortMenuItem ), Application.EXPLICIT_NORMAL ) )
        {
            throw new TestFrameworkException( "Sort item was not found in the context menu" );
        }
        if ( !waitIsElementEnabled( findElement( By.xpath( sortMenuItem ) ), 2 ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_context-publish" ) );
            throw new TestFrameworkException( "Sort context-menu item is disabled!" );
        }
        findElement( By.xpath( String.format( CONTEXT_MENU_ITEM, ContentMenuItem.SORT.getName() ) ) ).click();
        SortContentDialog sortContentDialog = new SortContentDialog( getSession() );
        sortContentDialog.waitForLoaded( Application.EXPLICIT_NORMAL );
        return sortContentDialog;
    }

    public boolean waitUntilItemDisabledInContextMenu( String contentName, String menuItem )
    {
        openContextMenu( contentName );
        if ( !waitUntilVisibleNoException( By.xpath( String.format( CONTEXT_MENU_ITEM, menuItem ) ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_" + menuItem );
            throw new TestFrameworkException( menuItem + "  item was not found in the context menu" );
        }
        WebElement previewItem = getDisplayedElement( By.xpath( String.format( CONTEXT_MENU_ITEM, menuItem ) ) );
        return waitAndCheckAttrValue( previewItem, "class", "disabled", 2 );
    }

    public boolean openContextMenuAndWaitUntilItemEnabled( String contentName, String menuItem )
    {
        openContextMenu( contentName );
        if ( !waitUntilVisibleNoException( By.xpath( String.format( CONTEXT_MENU_ITEM, menuItem ) ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_" + menuItem );
            throw new TestFrameworkException( menuItem + "  item was not found in the context menu" );
        }
        WebElement previewItem = getDisplayedElement( By.xpath( String.format( CONTEXT_MENU_ITEM, menuItem ) ) );
        return waitIsElementEnabled( previewItem, 2 );
    }

    public ContentBrowsePanel selectPreviewInContextMenu( String contentName )
    {
        openContextMenu( contentName );
        if ( !waitUntilVisibleNoException( By.xpath( String.format( CONTEXT_MENU_ITEM, ContentMenuItem.PREVIEW.getName() ) ),
                                           Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_" + "preview" );
            throw new TestFrameworkException( "'Preview' menu item is not visible" );
        }
        findElements( By.xpath( String.format( CONTEXT_MENU_ITEM, ContentMenuItem.PREVIEW.getName() ) ) ).get( 0 ).click();
        return this;
    }

    /**
     * Opens context menu and select a 'New' item
     *
     * @param contentName
     * @return {@link NewContentDialog} instance.
     */
    public NewContentDialog selectNewFromContextMenu( String contentName )
    {
        getFilterPanel().clickOnCleanFilter().typeSearchText( contentName );
        sleep( 1000 );
        openContextMenu( contentName );
        sleep( 500 );
        String newMenuItem = String.format( CONTEXT_MENU_ITEM, ContentMenuItem.NEW.getName() );
        if ( !isElementDisplayed( newMenuItem ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_context-edit" ) );
            throw new TestFrameworkException( "'New' context-menu item is not visible!" );
        }
        getDisplayedElement( By.xpath( newMenuItem ) ).click();
        NewContentDialog newContentDialog = new NewContentDialog( getSession() );
        newContentDialog.waitUntilDialogLoaded( Application.EXPLICIT_NORMAL );
        return newContentDialog;
    }

    public String waitPublishNotificationMessage( long timeout )
    {
        if ( !waitUntilVisibleNoException( By.xpath( PUBLISH_SUCCESS_NOTIFICATION_MESSAGE_XPATH ), timeout ) )
        {
            return null;
        }
        String message = findElement( By.xpath( PUBLISH_SUCCESS_NOTIFICATION_MESSAGE_XPATH ) ).getText();
        getLogger().info( "Publish Notification message " + message );
        return message;
    }

    public String getWorkflowState( String displayName )
    {
        String xpath = String.format( CONTENT_SUMMARY_VIEWER, displayName );
        waitUntilVisible( By.xpath( xpath ) );
        WebElement elem = findElement( By.xpath( xpath ) );
        String attrClass = this.getAttribute( elem, "class", EXPLICIT_QUICK );
        if ( attrClass.contains( "in-progress" ) )
        {
            return WORKFLOW_STATE_WORK_IN_PROGRESS;
        }
        else if ( attrClass.contains( "ready" ) )
        {
            return WORKFLOW_STATE_READY_FOR_PUBLISHING;
        }
        else if ( attrClass == "viewer content-summary-and-compare-status-viewer" )
        {
            return WORKFLOW_STATE_PUBLISHED;
        }
        else
        {
            throw new TestFrameworkException( "Error when getting content's state, class is:" + attrClass );
        }
    }

    /**
     * @return true if 'Delete' button enabled, otherwise false.
     */
    public boolean isDeleteButtonEnabled()
    {
        return deleteButton.isEnabled();
    }

    public boolean isDeleteButtonDisplayed()
    {
        return deleteButton.isDisplayed();
    }

    public boolean isEditButtonDisplayed()
    {
        return editButton.isDisplayed();
    }

    public boolean isDuplicateButtonDisplayed()
    {
        return duplicateButton.isDisplayed();
    }

    public boolean isNewButtonEnabled()
    {
        return newButton.isEnabled();
    }

    public boolean isSortButtonEnabled()
    {
        return sortButton.isEnabled();
    }

    public boolean isDuplicateButtonEnabled()
    {
        return duplicateButton.isEnabled();
    }

    public boolean isMoveButtonEnabled()
    {
        return moveButton.isEnabled();
    }

    public boolean isMoveButtonDisplayed()
    {
        return moveButton.isDisplayed();
    }

    public boolean isEditButtonEnabled()
    {
        return editButton.isEnabled();
    }

    public boolean isUndoDeleteButtonDisplayed()
    {
        return undoDeleteButton.isDisplayed();
    }

    public boolean isPublishButtonEnabled()
    {
        return publishButton.isEnabled();
    }

    public boolean isPublishButtonDisplayed()
    {
        return publishButton.isDisplayed();
    }

    public boolean waitForPublishButtonVisible( long timeout )
    {
        return waitUntilVisibleNoException( By.xpath( PUBLISH_BUTTON_XPATH ), timeout );
    }

    public void waitForMoveButtonEnabled( long timeout )
    {
        waitUntilElementEnabled( By.xpath( MOVE_BUTTON_XPATH ), timeout );
    }

    public boolean isPreviewButtonEnabled()
    {
        return previewButton.isEnabled();
    }

    public void isCreateTaskButtonDisplayed()
    {
        waitUntilVisible( By.xpath( CREATE_TASK_BUTTON_XPATH ) );
    }
}
