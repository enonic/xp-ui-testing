package com.enonic.autotests.pages.contentmanager.wizardpanel;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.WizardPanel;
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog;
import com.enonic.autotests.pages.form.liveedit.ContextWindow;
import com.enonic.autotests.pages.form.liveedit.ItemViewContextMenu;
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel;
import com.enonic.autotests.services.NavigatorHelper;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.contentmanager.Content;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * 'Content Manager' application, Content Wizard page.
 */
public class ContentWizardPanel
    extends WizardPanel<Content>
{
    private final String TOOLBAR = "//div[contains(@id,'ContentWizardToolbar')]";

    private final String CONTENT_STATUS = "//span[@class='content-status']/span";

    private final String TOOLBAR_DUPLICATE_BUTTON_XPATH = TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Duplicate']]";

    private String SHOW_HIDE_PAGE_EDITOR_TOOLBAR_BUTTON = TOOLBAR + "//button[contains(@id, 'CycleButton') ]";

    public static final String SHOW_PAGE_EDITOR_BUTTON_TITLE = "Show Page Editor";

    public static final String HIDE_PAGE_EDITOR_BUTTON_TITLE = "Hide Page Editor";

    public static final String SHOW_INSPECTION_PANEL_TITLE = "Show Inspection Panel";

    private final String DIV_CONTENT_WIZARD_PANEL = "//div[contains(@id,'ContentWizardPanel') and not(contains(@style,'display: none'))]";

    private final String TOOLBAR_SAVE_BUTTON_XPATH = TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Save draft']]";

    private final String TOOLBAR_PUBLISH_BUTTON_XPATH =
        TOOLBAR + "//button[contains(@id,'DialogButton') and child::span[text()='Publish']]";

    private final String TOOLBAR_DELETE_BUTTON_XPATH = TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Delete']]";

    private final String TOOLBAR_PREVIEW_BUTTON_XPATH = TOOLBAR + "/*[contains(@id, 'ActionButton') and child::span[text()='Preview']]";

    private final String INSPECTION_PANEL_TOGGLER = TOOLBAR + "/*[contains(@id, 'TogglerButton') and contains(@class,'icon-cog')]";

    private String COMPONENT_VIEW_TOGGLER = TOOLBAR + "/*[contains(@id, 'TogglerButton') and contains(@class,'icon-clipboard')]";

    @FindBy(xpath = TOOLBAR_SAVE_BUTTON_XPATH)
    protected WebElement toolbarSaveButton;

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

    /**
     * The constructor.
     *
     * @param session
     */
    public ContentWizardPanel( TestSession session )
    {
        super( session );
    }

    public ContextWindow showContextWindow()
    {
        TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "context_window" ) );
        ContextWindow cw = new ContextWindow( getSession() );
        if ( !cw.isContextWindowPresent() )
        {
            if ( !isElementDisplayed( INSPECTION_PANEL_TOGGLER ) )
            {
                TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_icon-cog" ) );
                throw new TestFrameworkException( "button with 'icon-cog' was not found" );
            }
            getDisplayedElement( By.xpath( INSPECTION_PANEL_TOGGLER ) ).click();
            cw.waitUntilWindowLoaded( 1l );
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "context_window_opened" ) );
        }
        return cw;
    }

    public ContentWizardPanel clickToolbarPreview()
    {
        toolbarPreviewButton.click();
        sleep( 1000 );
        return this;
    }

    @Override
    public ConfirmationDialog clickToolbarDelete()
    {
        toolbarDeleteButton.click();
        sleep( 1000 );
        ConfirmationDialog confirmationDialog = new ConfirmationDialog( getSession() );
        return confirmationDialog;
    }

    public boolean isContentInvalid( String contentDisplayName )
    {
        List<WebElement> elements = findElements( By.xpath( String.format( TAB_MENU_ITEM, contentDisplayName ) ) ).stream().filter(
            WebElement::isDisplayed ).collect( Collectors.toList() );
        if ( elements.size() == 0 )
        {
            throw new TestFrameworkException( "tab menu item with name: " + contentDisplayName + " was not found" );
        }
        return waitAndCheckAttrValue( elements.get( 0 ), "class", "invalid", Application.EXPLICIT_NORMAL );
    }

    public boolean isLiveEditLocked()
    {
        NavigatorHelper.switchToLiveEditFrame( getSession() );
        LiveFormPanel liveEdit = new LiveFormPanel( getSession() );
        boolean result = liveEdit.isShaderDisplayed();
        NavigatorHelper.switchToContentManagerFrame( getSession() );
        return result;
    }

    public ItemViewContextMenu showItemViewContextMenu()
    {
        clickOnPageView();
        sleep( 500 );
        return new ItemViewContextMenu( getSession() );
    }

    public ContentWizardPanel unlockPageEditor()
    {
        ItemViewContextMenu itemViewContextMenu = showItemViewContextMenu();
        itemViewContextMenu.clickOnCustomizeMenuItem();
        NavigatorHelper.switchToContentManagerFrame( getSession() );
        return this;
    }


    private void clickOnPageView()
    {
        NavigatorHelper.switchToLiveEditFrame( getSession() );
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
        waitElementClickable( By.name( "displayName" ), 1 );
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
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "acl_" + content.getName() ) );
        }
        return this;
    }

    public SecurityWizardStepForm clickOnSecurityTabLink()
    {
        String securityTabXpath = String.format( NAVIGATOR_TAB_ITEM_LINK, SECURITY_LINK_TEXT );
        if ( findElements( By.xpath( securityTabXpath ) ).size() == 0 )
        {
            throw new TestFrameworkException( "security tab was not found!" );
        }
        findElements( By.xpath( securityTabXpath ) ).get( 0 ).click();
        sleep( 1000 );
        return new SecurityWizardStepForm( getSession() );
    }

    public SettingsWizardStepForm clickOnSettingsTabLink()
    {
        String securityTabXpath = String.format( NAVIGATOR_TAB_ITEM_LINK, SETTINGS_LINK_TEXT );
        if ( findElements( By.xpath( securityTabXpath ) ).size() == 0 )
        {
            throw new TestFrameworkException( "settings tab was not found!" );
        }
        findElements( By.xpath( securityTabXpath ) ).get( 0 ).click();
        sleep( 1000 );
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
        boolean isSaveButtonEnabled = waitUntilElementEnabledNoException( By.xpath( TOOLBAR_SAVE_BUTTON_XPATH ), 2l );
        if ( !isSaveButtonEnabled )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_save_button" ) );
            throw new SaveOrUpdateException( "Impossible to save, button 'Save' is not available!!" );
        }
        toolbarSaveButton.click();
        sleep( 700 );
        return this;
    }

    public ContentPublishDialog clickOnWizardPublishButton()
    {
        toolbarPublishButton.click();
        ContentPublishDialog dialog = new ContentPublishDialog( getSession() );
        dialog.waitUntilDialogShowed( Application.EXPLICIT_NORMAL );
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
        boolean result = waitUntilVisibleNoException( By.xpath( DIV_CONTENT_WIZARD_PANEL ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_wizard" ) );
            throw new TestFrameworkException( "ContentWizard was not showed!" );
        }
        return this;
    }

    @Override
    public String getWizardDivXpath()
    {
        return DIV_CONTENT_WIZARD_PANEL;
    }

    public static ContentWizardPanel getWizard( TestSession session )
    {
        ContentWizardPanel wizard = new ContentWizardPanel( session );
        if ( wizard.isOpened() )
        {
            return wizard;
        }
        else
        {
            return null;
        }
    }

    public ContentWizardPanel showPageEditor()
    {
        if ( isLiveEditFrameDisplayed() )
        {
            return this;
        }
        if ( !waitUntilVisibleNoException( By.xpath( SHOW_HIDE_PAGE_EDITOR_TOOLBAR_BUTTON ), Application.EXPLICIT_NORMAL ) )
        {
            TestUtils.saveScreenshot( getSession(), "err-show-button" );
            throw new TestFrameworkException( "The 'Show Page Editor' button was not found!" );
        }
        getDisplayedElement( By.xpath( SHOW_HIDE_PAGE_EDITOR_TOOLBAR_BUTTON ) ).click();
        sleep( 700 );
        return this;
    }

    public ContentWizardPanel hidePageEditor()
    {
        if ( !isElementDisplayed( SHOW_HIDE_PAGE_EDITOR_TOOLBAR_BUTTON ) )
        {
            TestUtils.saveScreenshot( getSession(), "err-hide-button" );
            throw new TestFrameworkException( "The 'Hide Page Editor' button was not found!" );
        }
        getDisplayedElement( By.xpath( SHOW_HIDE_PAGE_EDITOR_TOOLBAR_BUTTON ) ).click();
        sleep( 500 );
        return this;
    }

    public ContentWizardPanel showComponentView()
    {
        if ( !waitUntilVisibleNoException( By.xpath( COMPONENT_VIEW_TOGGLER ), Application.EXPLICIT_NORMAL ) )
        {
            TestUtils.saveScreenshot( getSession(), "component-view-button" );
            throw new TestFrameworkException( "The 'Show Component View' button was not found!" );
        }
        getDisplayedElement( By.xpath( COMPONENT_VIEW_TOGGLER ) ).click();
        PageComponentsViewDialog dialog = new PageComponentsViewDialog( getSession() );
        dialog.waitForOpened();
        return this;
    }

    public boolean isShowComponentViewButtonDisplayed()
    {
        return isElementDisplayed( COMPONENT_VIEW_TOGGLER );
    }

    public boolean isShowInspectionPanelButtonDisplayed()
    {
        return isElementDisplayed( INSPECTION_PANEL_TOGGLER );
    }

    public ContentWizardPanel selectPageDescriptor( String pageDescriptorDisplayName )
    {
        NavigatorHelper.switchToLiveEditFrame( getSession() );
        findElements( By.xpath( OPTION_FILTER_INPUT ) ).get( 0 ).sendKeys( pageDescriptorDisplayName );
        String pageDescriptor = String.format( "//h6[@class='main-name' and text()='%s']", pageDescriptorDisplayName );
        if ( !waitUntilVisibleNoException( By.xpath( pageDescriptor ), Application.EXPLICIT_NORMAL ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_" + pageDescriptorDisplayName );
            throw new TestFrameworkException( "drop-down-option-filter: item was not found!" + pageDescriptorDisplayName );
        }
        getDisplayedElement( By.xpath( pageDescriptor ) ).click();
        sleep( 1000 );
        NavigatorHelper.switchToContentManagerFrame( getSession() );
        return this;
    }

    public boolean isPageDescriptorOptionsFilterDisplayed()
    {
        NavigatorHelper.switchToLiveEditFrame( getSession() );
        return isElementDisplayed( OPTION_FILTER_INPUT );
    }

    public boolean isLiveEditFrameDisplayed()
    {
        return isElementDisplayed( Application.LIVE_EDIT_FRAME );
    }

    public boolean isShowPageEditorButtonDisplayed()
    {
        String button = String.format( SHOW_HIDE_PAGE_EDITOR_TOOLBAR_BUTTON, SHOW_PAGE_EDITOR_BUTTON_TITLE );
        return isElementDisplayed( button );
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
}
