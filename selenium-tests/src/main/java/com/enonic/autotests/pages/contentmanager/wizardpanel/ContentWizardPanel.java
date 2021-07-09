package com.enonic.autotests.pages.contentmanager.wizardpanel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.SaveBeforeCloseDialog;
import com.enonic.autotests.pages.WizardPanel;
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog;
import com.enonic.autotests.pages.contentmanager.ContentUnpublishDialog;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus;
import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog;
import com.enonic.autotests.pages.form.liveedit.ContextWindow;
import com.enonic.autotests.pages.form.liveedit.ItemViewContextMenu;
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.WaitHelper;
import com.enonic.autotests.vo.contentmanager.Content;
import com.enonic.autotests.vo.contentmanager.security.ContentAclEntry;
import com.enonic.autotests.vo.contentmanager.security.PermissionSuite;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * 'Content Studio' application, Content Wizard page.
 */
public class ContentWizardPanel
    extends WizardPanel<Content>
{
    public static final String SHOW_PAGE_EDITOR_BUTTON_TITLE = "Show Page Editor";

    public static final String SCHEDULE_VALIDATION_MESSAGE = "Online to cannot be in the past";

    public static final String ONLINE_FROM_MISSED_NOTIFICATION_MESSAGE = "[Online to] date/time cannot be set without [Online from]";

    private final String TOOLBAR = "//div[contains(@id,'ContentWizardToolbar')]";

    private final String CONTAINER = "//div[contains(@id,'ContentWizard')]";

    private final String DIV_CONTENT_WIZARD_PANEL = "//div[contains(@id,'ContentWizardPanel') and not(contains(@style,'display: none'))]";

    private final String CONTENT_STATUS = "//div[@class='content-status-wrapper']/span[contains(@class,'status')]";

    private final String TOOLBAR_DUPLICATE_BUTTON_XPATH =
        TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Duplicate...']]";

    private final String TOOLBAR_UNDO_DELETE_BUTTON_XPATH =
        TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Undo delete']]";

    private final String TOOLBAR_SAVE_BUTTON_XPATH = TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Save']]";

    private final String TOOLBAR_SAVED_BUTTON_XPATH = TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Saved']]";

    private final String TOOLBAR_PUBLISH = "//div[contains(@id,'ContentWizardToolbarPublishControls')]";

    private final String TOOLBAR_PUBLISH_DROPDOWN_HANDLER = TOOLBAR_PUBLISH + "//button[contains(@id,'DropdownHandle')]";

    private final String SCHEDULE_WIZARD_STEP = "//div[contains(@id,'ScheduleWizardStepForm')]";

    private final String ONLINE_FROM_DATETIME_INPUT = SCHEDULE_WIZARD_STEP +
        "//div[contains(@id,'DateTimePicker') and preceding-sibling::label[text()='Online from']]//input[contains(@id,'TextInput')]";

    private final String ONLINE_TO_DATETIME_INPUT = SCHEDULE_WIZARD_STEP +
        "//div[contains(@id,'DateTimePicker') and preceding-sibling::label[text()='Online to']]//input[contains(@id,'TextInput')]";

    private final String SCHEDULE_VALIDATION_MESSAGE_XPATH =
        SCHEDULE_WIZARD_STEP + "//div[contains(@id,'InputView')]" + "//div[contains(@id,'ValidationRecordingViewer')]//li";


    private final String UNPUBLISH_MENU_ITEM =
        TOOLBAR_PUBLISH + "//ul[contains(@id,'Menu')]//li[contains(@id,'MenuItem') and text()='Unpublish...']";

    private final String TOOLBAR_PUBLISH_BUTTON_XPATH =
        TOOLBAR + "//button[contains(@id,'ActionButton') and child::span[text()='Publish...']]";

    private final String TOOLBAR_MARK_AS_READY_BUTTON_XPATH =
        TOOLBAR + "//button[contains(@id,'ActionButton') and child::span[text()='Mark as ready']]";

    private final String TOOLBAR_DELETE_BUTTON_XPATH = TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Delete...']]";

    private final String TOOLBAR_PREVIEW_BUTTON_XPATH = TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Preview']]";

    //Show Context Panel button:
    private final String CONTEXT_PANEL_TOGGLER =
        DIV_CONTENT_WIZARD_PANEL + "//div[contains(@id,'ContentWizardToolbar')]" + "//button[contains(@id, 'NonMobileContextPanelToggleButton')]";

    private final String EDIT_PERMISSIONS_BUTTON =
        "//div[contains(@id,'WizardStepNavigatorAndToolbar')]" + "//div[contains(@class,'edit-permissions-button')]";

    @FindBy(xpath = TOOLBAR_SAVE_BUTTON_XPATH)
    protected WebElement toolbarSaveButton;

    private String CONTENT_STATE_ICON = DIV_CONTENT_WIZARD_PANEL + "//div[contains(@class,'toolbar-state-icon')]";

    private String TOGGLE_PAGE_EDITOR_TOOLBAR_BUTTON = TOOLBAR + "//button[contains(@id, 'CycleButton') ]";

    private String COMPONENT_VIEW_TOGGLER = TOOLBAR + "/*[contains(@id, 'TogglerButton') and contains(@class,'icon-clipboard')]";

    private final String HOME_BUTTON = "//div[contains(@class,'font-icon-default icon-tree-2')]";

    protected final String SCHEDULE_ERROR_BLOCK =
        "//div[contains(@id,'ScheduleWizardStepForm')]//div[contains(@id,'InputOccurrenceView')]//div[contains(@class,'error-block')]";

    @FindBy(xpath = TOOLBAR_PUBLISH_BUTTON_XPATH)
    private WebElement toolbarPublishButton;

    @FindBy(xpath = TOOLBAR_DELETE_BUTTON_XPATH)
    private WebElement toolbarDeleteButton;

    @FindBy(xpath = TOOLBAR_PREVIEW_BUTTON_XPATH)
    private WebElement toolbarPreviewButton;

    @FindBy(xpath = TOOLBAR_DUPLICATE_BUTTON_XPATH)
    private WebElement toolbarDuplicateButton;

    @FindBy(xpath = TOOLBAR_UNDO_DELETE_BUTTON_XPATH)
    private WebElement toolbarUndoDeleteButton;

    @FindBy(xpath = CONTEXT_PANEL_TOGGLER)
    private WebElement toolbarShowContextWindow;

    @FindBy(xpath = TOOLBAR_PUBLISH_DROPDOWN_HANDLER)
    private WebElement publishMenuDropDownHandler;

    @FindBy(xpath = ONLINE_FROM_DATETIME_INPUT)
    private WebElement onlineFromInput;

    @FindBy(xpath = ONLINE_TO_DATETIME_INPUT)
    private WebElement onlineToInput;

    @FindBy(xpath = HOME_BUTTON)
    private WebElement homeButton;


    /**
     * The constructor.
     *
     * @param session
     */
    public ContentWizardPanel( TestSession session )
    {
        super( session );
    }

    public ContentWizardPanel showPublishMenu()
    {
        waitUntilVisibleNoException( By.xpath( TOOLBAR_PUBLISH_DROPDOWN_HANDLER ), Application.EXPLICIT_NORMAL );
        try
        {
            publishMenuDropDownHandler.click();
        }
        catch ( Exception e )
        {
            sleep( 1500 );
            publishMenuDropDownHandler.click();
        }
        sleep( 500 );
        return this;
    }

    public void clickOnHomeButton()
    {
        homeButton.click();
    }

    public boolean isHomeButtonClickable()
    {
        return homeButton.isEnabled();
    }

    public boolean isDeleteButtonDisplayed()
    {
        return isElementDisplayed( By.xpath( TOOLBAR_DELETE_BUTTON_XPATH ) );
    }

    public boolean isUndoDeleteButtonDisplayed()
    {
        return isElementDisplayed( By.xpath( TOOLBAR_UNDO_DELETE_BUTTON_XPATH ) );
    }

    public boolean isSaveButtonDisplayed()
    {
        return isElementDisplayed( By.xpath( TOOLBAR_SAVE_BUTTON_XPATH ) );
    }

    public boolean isSavedButtonDisplayed()
    {
        return isElementDisplayed( By.xpath( TOOLBAR_SAVED_BUTTON_XPATH ) );
    }

    /**
     * Keyboard shortcut to 'Edit selected content'
     */
    public ContentWizardPanel pressSaveKeyboardShortcut()
    {

        String os = System.getProperty( "os.name" ).toLowerCase();

        if ( os.indexOf( "mac" ) >= 0 )
        {
            buildActions().keyDown( Keys.COMMAND ).sendKeys( "s" ).build().perform();
        }
        else
        {
            buildActions().keyDown( Keys.CONTROL ).sendKeys( "s" ).build().perform();
        }
        return this;
    }

    /**
     * Keyboard shortcut to 'Save and close'  Ctrl+Enter  or Cmd+Enter
     */
    public ContentWizardPanel pressSaveAndCloseKeyboardShortcut()
    {
        String os = System.getProperty( "os.name" ).toLowerCase();

        if ( os.indexOf( "mac" ) >= 0 )
        {
            buildActions().keyDown( Keys.COMMAND ).sendKeys( Keys.ENTER ).build().perform();
        }
        else
        {
            buildActions().keyDown( Keys.CONTROL ).sendKeys( Keys.ENTER ).build().perform();
        }
        return this;
    }

    /**
     * Keyboard shortcut to 'Close'  Alt+W
     */
    public ContentWizardPanel pressCloseKeyboardShortcut()
    {
        buildActions().keyDown( Keys.ALT ).sendKeys( "w" ).build().perform();
        sleep( 1000 );
        return this;
    }


    public boolean isPublishMenuAvailable()
    {
        if ( !isElementDisplayed( TOOLBAR_PUBLISH_DROPDOWN_HANDLER ) )
        {
            saveScreenshot( "err_publish_dropdown_handler_wizard" );
            throw new TestFrameworkException( "dropdown handler for publish menu is not displayed" );
        }
        return !getAttribute( getDisplayedElement( By.xpath( TOOLBAR_PUBLISH_DROPDOWN_HANDLER ) ), "class",
                              Application.EXPLICIT_NORMAL ).contains( "disabled" );
    }

    public boolean isMarkAsReadyMenuItemEnabled()
    {
        if ( !isElementDisplayed( CONTAINER + MARK_AS_READY_MENU_ITEM ) )
        {
            saveScreenshot( "err_mark_as_ready_menu_item_not_visible " );
            throw new TestFrameworkException( "'mark as ready' menu item is not visible!" );
        }
        return !getAttribute( getDisplayedElement( By.xpath( CONTAINER + MARK_AS_READY_MENU_ITEM ) ), "class",
                              Application.EXPLICIT_NORMAL ).contains( "disabled" );
    }

    public String clickOnMarkAsReadyMenuItem()
    {
        if ( !isMarkAsReadyMenuItemEnabled() )
        {
            saveScreenshot( "err_mark_as_ready_menu_item" );
            throw new TestFrameworkException( "menu item is not enabled" );
        }
        getDisplayedElement( By.xpath( CONTAINER + MARK_AS_READY_MENU_ITEM ) ).click();
        sleep( 400 );
        return waitForNotificationMessage();
    }

    public String clickOnMarkAsReadyButton()
    {
        waitUntilVisible( By.xpath( TOOLBAR_MARK_AS_READY_BUTTON_XPATH ) );
        getDisplayedElement( By.xpath( TOOLBAR_MARK_AS_READY_BUTTON_XPATH ) ).click();
        sleep( 1000 );
        return waitForNotificationMessage();
    }

    public ContentWizardPanel clickOnMarkAsReadyAndDoPublish()
    {
        clickOnMarkAsReadyButton();
        clickOnPublishButton().clickOnPublishNowButton();
        return this;
    }

    public ContentUnpublishDialog selectUnPublishMenuItem()
    {
        if ( !isUnPublishMenuItemEnabled() )
        {
            saveScreenshot( "err_unpublish_menu_item" );
            throw new TestFrameworkException( "menu item was not found!" + "unpublish_item" );
        }
        getDisplayedElement( By.xpath( UNPUBLISH_MENU_ITEM ) ).click();
        ContentUnpublishDialog dialog = new ContentUnpublishDialog( getSession() );
        dialog.waitForDialogLoaded();
        return dialog;
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
            saveScreenshot( "err_publish_menu_item_not_visible_1" );
            throw new TestFrameworkException( "'Publish' menu item is not visible!" );
        }
        return !getAttribute( getDisplayedElement( By.xpath( PUBLISH_MENU_ITEM ) ), "class", Application.EXPLICIT_NORMAL ).contains(
            "disabled" );
    }

    /*
     * clicks on the toggler for 'Inspection Panel' and waits until the panel is shown
     */
    public ContextWindow showContextWindow()
    {
        if ( isInLiveEditFrame() )
        {
            switchToDefaultWindow();
        }
        ContextWindow cw = new ContextWindow( getSession() );
        if ( !cw.waitForContextWindowVisible() )
        {
            boolean isTogglerVisible = waitUntilVisibleNoException( By.xpath( CONTEXT_PANEL_TOGGLER ), Application.EXPLICIT_NORMAL );
            if ( !isTogglerVisible )
            {
                saveScreenshot( NameHelper.uniqueName( "err_icon-cog" ) );
                throw new TestFrameworkException( "Toggler for Context Panel (icon-cog) was not found" );
            }
            getDisplayedElement( By.xpath( CONTEXT_PANEL_TOGGLER ) ).click();
            cw.waitUntilWindowLoaded( 1l );
            sleep( 1000 );
        }
        return cw;
    }

    public ContentWizardPanel clickToolbarPreview()
    {
        boolean isEnabled = waitUntilElementEnabledNoException( By.xpath( TOOLBAR_PREVIEW_BUTTON_XPATH ), 3 );
        if ( !isEnabled )
        {
            saveScreenshot( "err_preview_button_status" );
            throw new TestFrameworkException( "button Preview disabled, but expected is enabled" );
        }
        toolbarPreviewButton.click();
        sleep( 1500 );
        return this;
    }

    @Override
    public DeleteContentDialog clickToolbarDelete()
    {
        toolbarDeleteButton.click();
        sleep( 1000 );
        DeleteContentDialog deleteContentDialog = new DeleteContentDialog( getSession() );
        deleteContentDialog.waitForOpened();
        return deleteContentDialog;
    }

    /**
     * @return true if red icon is displayed on the wizard page
     */
    public boolean isContentInvalid()
    {

        if ( !isElementDisplayed( By.xpath( CONTENT_STATE_ICON ) ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_wizard_status)" ) );
            throw new TestFrameworkException( "status icon was not found" );
        }
        return waitAndCheckAttrValue( getDisplayedElement( By.xpath( CONTENT_STATE_ICON ) ), "class", "invalid",
                                      Application.EXPLICIT_NORMAL );
    }

    public boolean waitForContentInProgress()
    {
        if ( !isElementDisplayed( By.xpath( CONTENT_STATE_ICON ) ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_wizard_status)" ) );
            throw new TestFrameworkException( "status icon was not found" );
        }
        return waitAndCheckAttrValue( getDisplayedElement( By.xpath( CONTENT_STATE_ICON ) ), "class", "in-progress",
                                      Application.EXPLICIT_NORMAL );
    }

    public boolean isLiveEditLocked()
    {
        switchToLiveEditFrame();
        LiveFormPanel liveEdit = new LiveFormPanel( getSession() );
        boolean result = liveEdit.isShaderDisplayed();
        return result;
    }

    public ItemViewContextMenu showItemViewContextMenu()
    {
        waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        sleep( 2000 );
        clickOnPageView();
        sleep( 300 );
        ItemViewContextMenu menu = new ItemViewContextMenu( getSession() );
        menu.waitForMenuOpened();
        return menu;
    }

    public ContentWizardPanel unlockPageEditorAndSwitchToContentStudio()
    {
        ItemViewContextMenu itemViewContextMenu = showItemViewContextMenu();
        itemViewContextMenu.clickOnCustomizeMenuItem();
        sleep( 500 );
        switchToDefaultWindow();
        return this;
    }

    private void clickOnPageView()
    {
        switchToLiveEditFrame();
        waitUntilVisibleNoException( By.xpath( "//body[@data-portal-component-type='page']" ), Application.EXPLICIT_NORMAL );
        WebElement body = getDisplayedElement( By.xpath( "//body[@data-portal-component-type='page']" ) );
        Actions builder = new Actions( getDriver() );
        builder.click( body ).build().perform();
    }

    /**
     * Types a content-data in the wizard.
     *
     * @param content
     */
    @Override
    public ContentWizardPanel typeData( Content content )
    {
        // 1. type a data: 'name' and 'Display Name'.
        //waitUntilElementEnabledNoException( By.name( "displayName" ), 2 );
        if ( content.getDisplayName() != null )
        {
            getLogger().info( "types displayName: " + content.getDisplayName() );
            clearAndType( displayNameInput, content.getDisplayName() );
            sleep( 100 );
        }
        if ( StringUtils.isNotEmpty( content.getName() ) )
        {
            waitElementClickable( By.name( "name" ), 2 );
            getLogger().info( "types name: " + content.getName() );
            clearAndType( nameInput, content.getName().trim() );
        }
        // 2. populate main tab
        if ( content.getData() != null )
        {
            ContentWizardStepForm stepForm = new ContentWizardStepForm( getSession() );
            stepForm.type( content.getData(), content.getContentTypeName() );
        }
        if ( content.getContentSettings() != null )
        {
            SettingsWizardStepForm settings = new SettingsWizardStepForm( getSession() );
            settings.typeSettings( content.getContentSettings() );
        }

        if ( content.getAclEntries() != null )
        {
            this.clickOnEditPermissionsButton().uncheckInheritCheckbox().updatePermissions( content.getAclEntries() ).clickOnApply();
            sleep( 700 );
            saveScreenshot( NameHelper.uniqueName( "acl_" + content.getName() ) );
        }
        return this;
    }

    public SettingsWizardStepForm clickOnSettingsTabLink()
    {
        String securityTabXpath = String.format( NAVIGATOR_TAB_ITEM_LINK, SETTINGS_LINK_TEXT );
        if ( findElements( By.xpath( securityTabXpath ) ).size() == 0 )
        {
            saveScreenshot( "err_settings_tab" );
            throw new TestFrameworkException( "settings tab was not found!" );
        }
        findElement( By.xpath( securityTabXpath ) ).click();
        sleep( 500 );
        return new SettingsWizardStepForm( getSession() );
    }

    public ContentWizardPanel typeName( String name )
    {
        clearAndType( nameInput, name );
        return this;
    }

    public ContentWizardPanel typeDisplayName( String displayName )
    {
        clearAndType( displayNameInput, displayName );
        return this;
    }

    public ContentWizardPanel waitForSaveButtonClickable()
    {
        boolean isVisible = waitUntilVisibleNoException( By.xpath( TOOLBAR_SAVE_BUTTON_XPATH ), Application.EXPLICIT_NORMAL );
        boolean isEnabled = isSaveButtonEnabled();
        if ( !isVisible || !isEnabled )
        {
            saveScreenshot( NameHelper.uniqueName( "err_save_button" ) );
            throw new TestFrameworkException( "Save button should be visible on the wizard-toolbar" );
        }
        return this;
    }

    @Override
    public ContentWizardPanel save()
    {
        if ( isInLiveEditFrame() )
        {
            switchToDefaultWindow();
        }
        waitForSaveButtonClickable();
        toolbarSaveButton.click();
        sleep( 900 );
        return this;
    }

    public boolean waitExpectedNotificationMessage( String message, long timeout )
    {
        String expectedMessage = String.format( EXPECTED_NOTIFICATION_MESSAGE_XPATH, message );
        return waitUntilVisibleNoException( By.xpath( expectedMessage ), timeout );
    }

    public boolean waitForPublishButtonVisible( long timeout )
    {
        return waitUntilVisibleNoException( By.xpath( TOOLBAR_PUBLISH_BUTTON_XPATH ), timeout );
    }

    //Click on Default Publish... button in the toolbar
    public ContentPublishDialog clickOnPublishButton()
    {
        if ( !waitForPublishButtonVisible( Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_toolbar_publish" );
            throw new TestFrameworkException( "publish button was not found in the wizard" );
        }
        toolbarPublishButton.click();
        ContentPublishDialog dialog = new ContentPublishDialog( getSession() );
        dialog.waitForDialogLoaded();
        dialog.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        return dialog;
    }

    @Override
    public boolean isSaveButtonEnabled()
    {
        return waitUntilElementEnabledNoException( By.xpath( TOOLBAR_SAVE_BUTTON_XPATH ), Application.EXPLICIT_NORMAL );
    }

    public boolean isSavedButtonEnabled()
    {
        return waitUntilElementEnabledNoException( By.xpath( TOOLBAR_SAVED_BUTTON_XPATH ), Application.EXPLICIT_NORMAL );
    }

    public void isPublishButtonEnabled()
    {
        waitUntilElementEnabled( By.xpath( TOOLBAR_PUBLISH_BUTTON_XPATH ), Application.EXPLICIT_NORMAL );
    }

    public boolean isMarAsReadyEnabled()
    {
        return waitUntilElementEnabledNoException( By.xpath( TOOLBAR_MARK_AS_READY_BUTTON_XPATH ), Application.EXPLICIT_NORMAL );
    }

    public void waitForMarkAsReadyButtonVisible()
    {
        waitUntilVisible( By.xpath( TOOLBAR_MARK_AS_READY_BUTTON_XPATH ) );
    }

    public boolean isPreviewButtonEnabled()
    {
        return waitUntilElementEnabledNoException( By.xpath( TOOLBAR_PREVIEW_BUTTON_XPATH ), Application.EXPLICIT_NORMAL );
    }

    @Override
    public boolean isOpened()
    {
        return isElementDisplayed( TOOLBAR );
    }

    @Override
    public ContentWizardPanel waitUntilWizardOpened()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( "//div[contains(@id,'ContentWizardHeader')]" ), 8l );
        if ( !result )
        {
            saveScreenshot( NameHelper.uniqueName( "err_wizard" ) );
            throw new TestFrameworkException( "ContentWizard was not loaded!" );
        }
        return this;
    }

    @Override
    public SaveBeforeCloseDialog close( String displayName )
    {
        closeBrowserTab().switchToBrowsePanelTab();
        return null;
    }

    public ContentWizardPanel waitUntilWizardClosed()
    {
        boolean result = waitsElementNotVisible( By.xpath( DIV_CONTENT_WIZARD_PANEL ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            saveScreenshot( NameHelper.uniqueName( "err_close_wizard" ) );
            throw new TestFrameworkException( "ContentWizard was not closed!" );
        }
        return this;
    }

    @Override
    public String getWizardDivXpath()
    {
        return DIV_CONTENT_WIZARD_PANEL;
    }

    /**
     * closes the Page Editor if it displayed
     */
    public ContentWizardPanel showPageEditor()
    {
        if ( isLiveEditFrameDisplayed() )
        {
            return this;
        }
        if ( !waitUntilVisibleNoException( By.xpath( TOGGLE_PAGE_EDITOR_TOOLBAR_BUTTON ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err-show-button" );
            throw new TestFrameworkException( "The 'toggle Page Editor' button not displayed!" );
        }
        getDisplayedElement( By.xpath( TOGGLE_PAGE_EDITOR_TOOLBAR_BUTTON ) ).click();
        sleep( 700 );
        return this;
    }

    /**
     * checks if LiveEdit frame is displayed if it not displayed, clicks on the toggler for Page Editor and opens the editor
     */
    public ContentWizardPanel hidePageEditor()
    {
        if ( !isLiveEditFrameDisplayed() )
        {
            return this;
        }
        if ( !isElementDisplayed( TOGGLE_PAGE_EDITOR_TOOLBAR_BUTTON ) )
        {
            saveScreenshot( "err-hide-button" );
            throw new TestFrameworkException( "The 'Hide Page Editor' button was not found!" );
        }
        getDisplayedElement( By.xpath( TOGGLE_PAGE_EDITOR_TOOLBAR_BUTTON ) ).click();
        sleep( 500 );
        return this;
    }

    /**
     * clicks on the "Show Component View" toggler and waits until the view is displayed
     */
    public PageComponentsViewDialog showComponentView()
    {
        if ( isInLiveEditFrame() )
        {
            switchToDefaultWindow();
        }
        if ( !waitUntilVisibleNoException( By.xpath( COMPONENT_VIEW_TOGGLER ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_component-view-button" );
            throw new TestFrameworkException( "The 'Show Component View' button was not found!" );
        }
        getDisplayedElement( By.xpath( COMPONENT_VIEW_TOGGLER ) ).click();
        PageComponentsViewDialog dialog = new PageComponentsViewDialog( getSession() );
        dialog.waitForOpened();
        sleep( 300 );
        return dialog;
    }

    /**
     * return true if button "Show Component View" is displayed
     */
    public boolean isComponentViewTogglerDisplayed()
    {
        return isElementDisplayed( COMPONENT_VIEW_TOGGLER );
    }

    /**
     * return true if button "Show Context Panel" is displayed
     */
    public boolean isContextPanelTogglerDisplayed()
    {
        return isElementDisplayed( CONTEXT_PANEL_TOGGLER );
    }

    public ContentWizardPanel selectPageDescriptor( String pageDescriptorDisplayName )
    {
        if ( !isInLiveEditFrame() )
        {
            switchToLiveEditFrame();
        }
        boolean result = waitUntilVisibleNoException( By.xpath( DROPDOWN_OPTION_FILTER_INPUT ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            saveScreenshot( "err_content_wizard_dropdown_not_displayed" );
            throw new TestFrameworkException( "ContentWizard -tried to select page descriptor: option filter input was not found" );
        }
        findElement( By.xpath( DROPDOWN_OPTION_FILTER_INPUT ) ).sendKeys( pageDescriptorDisplayName );
        String pageDescriptor = String.format( "//h6[contains(@class,'main-name') and text()='%s']", pageDescriptorDisplayName );
        if ( !waitUntilVisibleNoException( By.xpath( pageDescriptor ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_" + pageDescriptorDisplayName );
            throw new TestFrameworkException( "drop-down-option-filter: item was not found: " + pageDescriptorDisplayName );
        }
        getDisplayedElement( By.xpath( pageDescriptor ) ).click();
        sleep( 1100 );
        return this;
    }

    /**
     * switches to the LiveEdit frame and checks if input for filtering of options is displayed
     */
    public boolean isPageDescriptorOptionsFilterDisplayed()
    {
        switchToLiveEditFrame();
        return isElementDisplayed( DROPDOWN_OPTION_FILTER_INPUT );
    }

    public boolean isLiveEditFrameDisplayed()
    {
        return isElementDisplayed( Application.LIVE_EDIT_FRAME );
    }

    /**
     * return true if toggler "Show Page Editor" is displayed
     */
    public boolean isShowPageEditorButtonDisplayed()
    {
        String button = String.format( TOGGLE_PAGE_EDITOR_TOOLBAR_BUTTON, SHOW_PAGE_EDITOR_BUTTON_TITLE );
        return waitUntilVisibleNoException( By.xpath( button ), Application.EXPLICIT_NORMAL );
    }

    @Override
    public boolean isDeleteButtonEnabled()
    {
        return toolbarDeleteButton.isEnabled();
    }

    public boolean isDuplicateButtonEnabled()
    {
        return toolbarDuplicateButton.isEnabled();
    }

    public String getStatus()
    {
        this.waitUntilVisible( By.xpath( CONTENT_STATUS ) );
        return getDisplayedString( CONTENT_STATUS );
    }

    public void waitStatus( ContentStatus status, long timeout )
    {
        String expectedStatus = String.format( CONTENT_STATUS + "[text()='%s']", status.getValue() );
        boolean result = waitUntilVisibleNoException( By.xpath( expectedStatus ), timeout );
        if ( !result )
        {
            saveScreenshot( "err_wizard_status" );
            throw new TestFrameworkException( "expected status was not found" );
        }
    }

    public LiveFormPanel switchToLiveEditFrame()
    {
        if ( !isInLiveEditFrame() )
        {
            List<WebElement> liveEditFrames = findElements( By.xpath( LIVE_EDIT_FRAME ) );
            if ( liveEditFrames.size() == 0 )
            {
                throw new TestFrameworkException( "Unable to switch to the live-edit iframe " );
            }
            //switch to 'live edit' frame
            getDriver().switchTo().frame( liveEditFrames.get( 0 ) );
            setInLiveEditFrame( true );
        }
        return new LiveFormPanel( getSession() );
    }

    public int getHeightOfPageEditor()
    {
        if ( isInLiveEditFrame() )
        {
            switchToDefaultWindow();
        }
        String height = getDisplayedElement( By.xpath( LIVE_EDIT_FRAME ) ).getCssValue( "height" );
        return Integer.valueOf( height.substring( 0, height.indexOf( "px" ) ) );
    }

    public int getWidthOfPageEditor()
    {
        if ( isInLiveEditFrame() )
        {
            switchToDefaultWindow();
        }
        String width = getDisplayedElement( By.xpath( LIVE_EDIT_FRAME ) ).getCssValue( "width" );

        return Integer.valueOf( width.substring( 0, width.indexOf( "px" ) ) );
        //String value = width.substring( 0, width.indexOf( "px" ) );
        //String[] integralPart = String.valueOf(value).split("[.]");
        //return Integer.valueOf(integralPart[0]);
    }

    public ContentBrowsePanel switchToBrowsePanelTab()
    {
        getDriver().switchTo().window( getHandleForContentBrowseTab() );
        return new ContentBrowsePanel( getSession() );
    }

    public ContentWizardPanel switchToDefaultWindow()
    {
        getDriver().switchTo().defaultContent();
        setInLiveEditFrame( false );
        return this;
    }

    public ContentWizardPanel closeBrowserTab()
    {
        getDriver().close();
        sleep( 300 );
        return this;
    }

    public ContentWizardPanel executeCloseWizardScript()
    {
        getJavaScriptExecutor().executeScript( "window.close();" );
        return this;
    }

    private String getHandleForContentBrowseTab()
    {
        String contentBrowseTabHandle = (String) getSession().get( CONTENT_STUDIO_TAB_HANDLE );
        if ( contentBrowseTabHandle == null )
        {
            throw new TestFrameworkException( "Handle for content browse panel was not set" );
        }
        return contentBrowseTabHandle;
    }

    public boolean waitIsAlertDisplayed()
    {
        return waitIsAlertPresent( Application.EXPLICIT_NORMAL );
    }

    public void acceptAlertAndLeavePage()
    {
        if ( !waitIsAlertDisplayed() )
        {
            throw new TestFrameworkException( "Expected Alert dialog was not loaded!" );
        }
        Alert alert = getDriver().switchTo().alert();
        alert.accept();
        switchToBrowsePanelTab();
    }

    private boolean waitIsAlertPresent( long timeout )
    {
        return WaitHelper.waitUntilAlertPresentNoException( getDriver(), timeout );
    }

    public void dismissAlertAndStayOnPage()
    {
        if ( !waitIsAlertDisplayed() )
        {
            throw new TestFrameworkException( "Expected Alert dialog was not displayed!" );
        }
        Alert alert = getDriver().switchTo().alert();
        alert.dismiss();
    }

    public boolean isInLiveEditFrame()
    {
        return getSession().isInLiveEditFrame();
    }

    public void setInLiveEditFrame( boolean value )
    {
        getSession().setInLiveEditFrame( value );
    }

    public boolean isOnlineFromInputDisplayed()
    {
        return isElementDisplayed( ONLINE_FROM_DATETIME_INPUT );
    }

    public boolean isOnlineToInputDisplayed()
    {
        return isElementDisplayed( ONLINE_TO_DATETIME_INPUT );
    }

    public String getOnlineFromDateTime()
    {
        if ( !isElementDisplayed( ONLINE_FROM_DATETIME_INPUT ) )
        {
            saveScreenshot( "err_online_from_input_not_present" );
            throw new TestFrameworkException( "Online From input was not found!" );
        }
        return onlineFromInput.getAttribute( "value" );
    }

    public String getOnlineToDateTime()
    {
        if ( !isElementDisplayed( ONLINE_TO_DATETIME_INPUT ) )
        {
            saveScreenshot( "err_online_to_input_not_present" );
            throw new TestFrameworkException( "Online to input was not found!" );
        }
        return onlineToInput.getAttribute( "value" );
    }

    public ContentWizardPanel typeOnlineFrom( String dateTime )
    {
        clearAndType( onlineFromInput, dateTime );
        return this;
    }

    public ContentWizardPanel typeOnlineTo( String dateTime )
    {
        clearAndType( onlineToInput, dateTime );
        return this;
    }


    protected String getScheduleValidationMessage()
    {
        List<WebElement> elements = findElements( By.xpath( SCHEDULE_ERROR_BLOCK ) );
        if ( elements.size() == 0 )
        {
            throw new Error( "Schedule Element was not found:" + SCHEDULE_ERROR_BLOCK );
        }
        return elements.get( 0 ).getText();
    }

    /**
     * Gets Permissions from the Security tab
     */
    public List<ContentAclEntry> getAclEntries()
    {
        ContentAclEntry.Builder builder;
        List<ContentAclEntry> entries = new ArrayList<>();
        List<WebElement> principalViews =
            findElements( By.xpath( DIV_CONTENT_WIZARD_PANEL + "//div[contains(@id,'AccessControlEntryView')]" ) );
        for ( WebElement el : principalViews )
        {
            String principalName = el.findElement( By.xpath( "." + P_NAME ) ).getText();
            String suite = el.findElement( By.xpath( "." + "//div[contains(@id,'TabMenuButton')]//a[@class='label']" ) ).getText();
            builder = ContentAclEntry.builder();
            builder.principalName( principalName );
            builder.suite( PermissionSuite.getSuite( suite ) );
            entries.add( builder.build() );
        }
        return entries;
    }

    public ContentPublishDialog clickOnPublishMenuItem()
    {
        if ( !isPublishMenuItemEnabled() )
        {
            saveScreenshot( "err_publish_menu_item" );
            throw new TestFrameworkException( "menu item is not enabled" );
        }
        getDisplayedElement( By.xpath( PUBLISH_MENU_ITEM ) ).click();
        sleep( 300 );
        ContentPublishDialog dialog = new ContentPublishDialog( getSession() );
        dialog.waitForDialogLoaded();
        return dialog;
    }

    public EditPermissionsDialog clickOnEditPermissionsButton()
    {
        boolean isDisplayed = waitUntilVisibleNoException( By.xpath( EDIT_PERMISSIONS_BUTTON ), Application.EXPLICIT_NORMAL );
        if ( !isDisplayed )
        {
            this.saveScreenshot( NameHelper.uniqueName( "err_edit_permission_button" ) );
        }
        findElement( By.xpath( EDIT_PERMISSIONS_BUTTON ) ).click();
        EditPermissionsDialog editPermissionsDialog = new EditPermissionsDialog( getSession() );
        editPermissionsDialog.waitForOpened();
        sleep( 300 );
        return editPermissionsDialog;
    }
}
