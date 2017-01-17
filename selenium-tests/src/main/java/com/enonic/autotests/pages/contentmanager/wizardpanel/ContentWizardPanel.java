package com.enonic.autotests.pages.contentmanager.wizardpanel;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
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
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.utils.WaitHelper;
import com.enonic.autotests.vo.contentmanager.Content;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * 'Content Studio' application, Content Wizard page.
 */
public class ContentWizardPanel
    extends WizardPanel<Content>
{
    public static final String SHOW_PAGE_EDITOR_BUTTON_TITLE = "Show Page Editor";

    public static final String ONLINE_TO_VALIDATION_MESSAGE = "\"Online to\" date/time must be later than \"Online from\"";

    public static final String ONLINE_FROM_VALIDATION_MESSAGE = "\"Online from\" date/time must be earlier than \"Online to\"";

    public static final String ONLINE_FROM_MISSED_NOTIFICATION_MESSAGE = "[Online to] date/time cannot be set without [Online from]";

    private final String TOOLBAR = "//div[contains(@id,'ContentWizardToolbar')]";

    private final String CONTENT_STATUS = "//span[@class='content-status']/span";

    private final String TOOLBAR_DUPLICATE_BUTTON_XPATH = TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Duplicate']]";

    private final String DIV_CONTENT_WIZARD_PANEL = "//div[contains(@id,'ContentWizardPanel') and not(contains(@style,'display: none'))]";

    private final String TOOLBAR_SAVE_BUTTON_XPATH = TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Save draft']]";

    private final String TOOLBAR_PUBLISH = "//div[contains(@id,'ContentWizardToolbarPublishControls')]";

    private final String TOOLBAR_PUBLISH_DROPDOWN_HANDLER = TOOLBAR_PUBLISH + "//button[contains(@id,'DropdownHandle')]";

    private final String SCHEDULE_WIZARD_STEP = "//div[contains(@id,'ScheduleWizardStepForm')]";

    private final String ONLINE_FROM_DATETIME_INPUT = SCHEDULE_WIZARD_STEP +
        "//div[contains(@id,'InputView') and descendant::div[text()='Online from']]" + DATA_TIME_PICKER_INPUT;

    private final String ONLINE_TO_DATETIME_INPUT = SCHEDULE_WIZARD_STEP +
        "//div[contains(@id,'InputView') and descendant::div[text()='Online to']]" + DATA_TIME_PICKER_INPUT;

    private final String ONLINE_TO_INPUT_ERROR_MESSAGE = SCHEDULE_WIZARD_STEP +
        "//div[contains(@id,'InputView') and descendant::div[text()='Online to']]" + "//div[contains(@id,'ValidationRecordingViewer')]//li";

    private final String ONLINE_FROM_INPUT_ERROR_MESSAGE = SCHEDULE_WIZARD_STEP +
        "//div[contains(@id,'InputView') and descendant::div[text()='Online from']]" +
        "//div[contains(@id,'ValidationRecordingViewer')]//li";


    private final String UNPUBLISH_MENU_ITEM =
        TOOLBAR_PUBLISH + "//ul[contains(@id,'Menu')]//li[contains(@id,'MenuItem') and text()='Unpublish']";

    private final String TOOLBAR_PUBLISH_BUTTON_XPATH =
        TOOLBAR + "//button[contains(@id,'ActionButton') and child::span[text()='Publish...']]";

    private final String TOOLBAR_DELETE_BUTTON_XPATH = TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Delete']]";

    private final String TOOLBAR_PREVIEW_BUTTON_XPATH = TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Preview']]";

    private final String INSPECTION_PANEL_TOGGLER = TOOLBAR + "/*[contains(@id, 'TogglerButton') and contains(@class,'icon-cog')]";

    @FindBy(xpath = TOOLBAR_SAVE_BUTTON_XPATH)
    protected WebElement toolbarSaveButton;

    private String TOGGLE_PAGE_EDITOR_TOOLBAR_BUTTON = TOOLBAR + "//button[contains(@id, 'CycleButton') ]";

    private String COMPONENT_VIEW_TOGGLER = TOOLBAR + "/*[contains(@id, 'TogglerButton') and contains(@class,'icon-clipboard')]";

    @FindBy(xpath = TOOLBAR_PUBLISH_BUTTON_XPATH)
    private WebElement toolbarPublishButton;

    @FindBy(xpath = TOOLBAR_DELETE_BUTTON_XPATH)
    private WebElement toolbarDeleteButton;

    @FindBy(xpath = TOOLBAR_PREVIEW_BUTTON_XPATH)
    private WebElement toolbarPreviewButton;

    @FindBy(xpath = TOOLBAR_DUPLICATE_BUTTON_XPATH)
    private WebElement toolbarDuplicateButton;

    @FindBy(xpath = INSPECTION_PANEL_TOGGLER)
    private WebElement toolbarShowContextWindow;

    @FindBy(xpath = TOOLBAR_PUBLISH_DROPDOWN_HANDLER)
    private WebElement publishMenuDropDownHandler;

    @FindBy(xpath = ONLINE_FROM_DATETIME_INPUT)
    private WebElement onlineFromInput;

    @FindBy(xpath = ONLINE_TO_DATETIME_INPUT)
    private WebElement onlineToInput;


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
        publishMenuDropDownHandler.click();
        sleep( 400 );
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

    public ContentUnpublishDialog selectUnPublishMenuItem()
    {
        if ( !isUnPublishMenuItemEnabled() )
        {
            saveScreenshot( "err_unpublish_menu_item" );
            throw new TestFrameworkException( "menu item was not found!" + "unpublish_item" );
        }
        getDisplayedElement( By.xpath( UNPUBLISH_MENU_ITEM ) ).click();
        ContentUnpublishDialog dialog = new ContentUnpublishDialog( getSession() );
        dialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
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

    /*
     * clicks on the toggler for 'Inspection Panel' and waits until the panel is shown
     */
    public ContextWindow showContextWindow()
    {
        ContextWindow cw = new ContextWindow( getSession() );
        if ( !cw.isContextWindowPresent() )
        {
            if ( !isElementDisplayed( INSPECTION_PANEL_TOGGLER ) )
            {
                saveScreenshot( NameHelper.uniqueName( "err_icon-cog" ) );
                throw new TestFrameworkException( "button with 'icon-cog' was not found" );
            }
            getDisplayedElement( By.xpath( INSPECTION_PANEL_TOGGLER ) ).click();
            cw.waitUntilWindowLoaded( 1l );
        }
        return cw;
    }

    public ContentWizardPanel clickToolbarPreview()
    {
        boolean isEnabled = waitUntilElementEnabledNoException( By.xpath( TOOLBAR_PREVIEW_BUTTON_XPATH ), 1 );
        if ( !isEnabled )
        {
            saveScreenshot( "err_preview_button_status" );
            throw new TestFrameworkException( "button Preview disabled, but expected is enabled" );
        }
        toolbarPreviewButton.click();
        sleep( 1000 );
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

    public boolean isContentInvalid( String contentDisplayName )
    {
        List<WebElement> elements = getDisplayedElements( By.xpath( String.format( TAB_MENU_ITEM, contentDisplayName ) ) );
        if ( elements.size() == 0 )
        {
            saveScreenshot( NameHelper.uniqueName( "err_tab_item)" ) );
            throw new TestFrameworkException( "tab menu item with name: " + contentDisplayName + " was not found" );
        }
        return waitAndCheckAttrValue( elements.get( 0 ), "class", "invalid", Application.EXPLICIT_NORMAL );
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
        clickOnPageView();
        sleep( 500 );
        return new ItemViewContextMenu( getSession() );
    }

    public ContentWizardPanel unlockPageEditorAndSwitchToContentStudio()
    {
        ItemViewContextMenu itemViewContextMenu = showItemViewContextMenu();
        itemViewContextMenu.clickOnCustomizeMenuItem();
        switchToDefaultWindow();
        return this;
    }

    public ContentWizardPanel unlockPageEditor()
    {
        ItemViewContextMenu itemViewContextMenu = showItemViewContextMenu();
        itemViewContextMenu.clickOnCustomizeMenuItem();
        return this;
    }

    private void clickOnPageView()
    {
        switchToLiveEditFrame();
        WebElement body = findElements( By.xpath( "//body" ) ).get( 0 );
        Actions builder = new Actions( getDriver() );
        builder.click( body ).build().perform();
    }

    /**
     * Types data and press the "Save" button from the toolbar.
     *
     * @param content
     */
    @Override
    public ContentWizardPanel typeData( Content content )
    {
        // 1. type a data: 'name' and 'Display Name'.
        waitElementClickable( By.name( "displayName" ), 2 );
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
            SecurityWizardStepForm securityWizardStepForm = clickOnSecurityTabLink();
            securityWizardStepForm.waitUntilButtonEditPermissionsClickable().clickOnEditPermissionsButton().uncheckInheritCheckbox().updatePermissions(
                content.getAclEntries() ).clickOnApply();
            sleep( 700 );
            saveScreenshot( NameHelper.uniqueName( "acl_" + content.getName() ) );
        }
        return this;
    }

    public ContentWizardPanel clickOnWizardStep( String stepName )
    {
        if ( !isWizardStepPresent( stepName ) )
        {
            saveScreenshot( "err_" + stepName );
            throw new TestFrameworkException( "step was not found! " + stepName );
        }
        String securityTabXpath = String.format( NAVIGATOR_TAB_ITEM_LINK, stepName );
        findElement( By.xpath( securityTabXpath ) ).click();
        sleep( 100 );
        return this;
    }

    public boolean isWizardStepPresent( String stepName )
    {
        String securityTabXpath = String.format( NAVIGATOR_TAB_ITEM_LINK, stepName );
        return isElementDisplayed( securityTabXpath );
    }

    public SecurityWizardStepForm clickOnSecurityTabLink()
    {
        String securityTabXpath = String.format( NAVIGATOR_TAB_ITEM_LINK, SECURITY_LINK_TEXT );
        if ( !isElementDisplayed( securityTabXpath ) )
        {
            saveScreenshot( "err_" + SECURITY_LINK_TEXT );
            throw new TestFrameworkException( "step was not found! " + SECURITY_LINK_TEXT );
        }
        findElement( By.xpath( securityTabXpath ) ).click();
        sleep( 500 );
        return new SecurityWizardStepForm( getSession() );
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

    @Override
    public ContentWizardPanel save()
    {
        if ( isInLiveEditFrame() )
        {
            switchToDefaultWindow();
        }
        boolean isSaveButtonEnabled =
            waitUntilElementEnabledNoException( By.xpath( TOOLBAR_SAVE_BUTTON_XPATH ), Application.EXPLICIT_NORMAL );
        if ( !isSaveButtonEnabled )
        {
            saveScreenshot( NameHelper.uniqueName( "err_save_button" ) );
            throw new SaveOrUpdateException( "Impossible to save, button 'Save' is not available!!" );
        }
        toolbarSaveButton.click();
        sleep( 800 );
        return this;
    }

    public boolean waitExpectedNotificationMessage( String message, long timeout )
    {
        String expectedMessage = String.format( EXPECTED_NOTIFICATION_MESSAGE_XPATH, message );
        return waitUntilVisibleNoException( By.xpath( expectedMessage ), timeout );
    }

    public ContentPublishDialog clickOnWizardPublishButton()
    {
        if ( !isElementDisplayed( TOOLBAR_PUBLISH_BUTTON_XPATH ) )
        {
            saveScreenshot( "err_toolbar_publish" );
            throw new TestFrameworkException( "publish button was not found in the wizard" );
        }
        toolbarPublishButton.click();
        ContentPublishDialog dialog = new ContentPublishDialog( getSession() );
        dialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        return dialog;
    }

    @Override
    public boolean isSaveButtonEnabled()
    {
        return waitUntilElementEnabledNoException( By.xpath( TOOLBAR_SAVE_BUTTON_XPATH ), Application.EXPLICIT_NORMAL );
    }

    public boolean isPublishButtonEnabled()
    {
        return waitUntilElementEnabledNoException( By.xpath( TOOLBAR_PUBLISH_BUTTON_XPATH ), Application.EXPLICIT_NORMAL );
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
        boolean result = waitUntilVisibleNoException( By.xpath( DIV_CONTENT_WIZARD_PANEL ), Application.EXPLICIT_LONG );
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
        if ( !waitUntilVisibleNoException( By.xpath( COMPONENT_VIEW_TOGGLER ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_component-view-button" );
            throw new TestFrameworkException( "The 'Show Component View' button was not found!" );
        }
        getDisplayedElement( By.xpath( COMPONENT_VIEW_TOGGLER ) ).click();
        PageComponentsViewDialog dialog = new PageComponentsViewDialog( getSession() );
        dialog.waitForOpened();
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
     * return true if button "Show Inspection Panel" is displayed
     */
    public boolean isInspectionPanelTogglerDisplayed()
    {
        return isElementDisplayed( INSPECTION_PANEL_TOGGLER );
    }

    public ContentWizardPanel selectPageDescriptor( String pageDescriptorDisplayName )
    {
        if ( !isInLiveEditFrame() )
        {
            switchToLiveEditFrame();
        }
        if ( !isElementDisplayed( DROPDOWN_OPTION_FILTER_INPUT ) )
        {
            saveScreenshot( "err_content_wizard_dropdown_not_displayed" );
            throw new TestFrameworkException( "option filter input was not found" );
        }
        findElement( By.xpath( DROPDOWN_OPTION_FILTER_INPUT ) ).sendKeys( pageDescriptorDisplayName );
        String pageDescriptor = String.format( "//h6[contains(@class,'main-name') and text()='%s']", pageDescriptorDisplayName );
        if ( !waitUntilVisibleNoException( By.xpath( pageDescriptor ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_" + pageDescriptorDisplayName );
            throw new TestFrameworkException( "drop-down-option-filter: item was not found: " + pageDescriptorDisplayName );
        }
        getDisplayedElement( By.xpath( pageDescriptor ) ).click();
        sleep( 1000 );
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
        //if ( getSession().getCurrentWindow().equals( XP_Windows.LIVE_EDIT ) )
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

    public ContentWizardPanel closeWizardAndCheckAlert()
    {
        getJavaScriptExecutor().executeScript( "window.close();" );
        sleep( 500 );
        try
        {
            TestUtils.createScreenCaptureWithRobot( NameHelper.uniqueName( "alert" ) );
        }
        catch ( Exception e )
        {
            getLogger().error( "exception when saving of a screenshot!" );
        }

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
        return waitUntilAlertPresent( Application.EXPLICIT_NORMAL );
    }

    public void acceptAlertAndLeavePage()
    {
        if ( !waitIsAlertDisplayed() )
        {
            throw new TestFrameworkException( "Expected Alert dialog was not displayed!" );
        }
        Alert alert = getDriver().switchTo().alert();
        alert.accept();
        switchToBrowsePanelTab();
    }

    private boolean waitUntilAlertPresent( long timeout )
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

    public String getOnlineToValidationMessage()
    {
        return getDisplayedString( ONLINE_TO_INPUT_ERROR_MESSAGE );
    }

    public String getOnlineFromValidationMessage()
    {
        return getDisplayedString( ONLINE_FROM_INPUT_ERROR_MESSAGE );
    }


}
