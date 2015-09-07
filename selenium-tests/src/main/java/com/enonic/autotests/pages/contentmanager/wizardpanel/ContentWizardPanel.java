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
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel;
import com.enonic.autotests.services.NavigatorHelper;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.contentmanager.Content;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * 'Content Manager' application, Add new Content Wizard page.
 */
public class ContentWizardPanel
    extends WizardPanel<Content>
{
    public static final String TOOLBAR_DUPLICATE_BUTTON_XPATH =
        "//div[contains(@id,'ContentWizardToolbar')]/*[contains(@id, 'ActionButton') and child::span[text()='Duplicate']]";

    public static final String TOOLBAR_LIVE_BUTTON_XPATH =
        "//div[contains(@id,'ContentWizardToolbar')]//div[contains(@id, 'CycleButton') and text()='LIVE']";


    public static final String DIV_CONTENT_WIZARD_PANEL =
        "//div[contains(@id,'ContentWizardPanel') and not(contains(@style,'display: none'))]";

    public static final String TOOLBAR_SAVE_BUTTON_XPATH =
        "//div[contains(@id,'ContentWizardToolbar')]/*[contains(@id, 'ActionButton') and child::span[text()='Save draft']]";

    public static final String TOOLBAR_CLOSE_WIZARD_BUTTON_XPATH =
        "//div[contains(@id,'ContentWizardToolbar')]/*[contains(@id, 'ActionButton') and child::span[text()='Close']]";

    private static final String TOOLBAR_PUBLISH_BUTTON_XPATH =
        "//div[contains(@id,'ContentWizardToolbar')]//div[contains(@id, 'ContentWizardToolbarPublishControls')]//button[contains(@id,'DialogButton') and child::span[text()='Publish']]";

    private static final String TOOLBAR_DELETE_BUTTON_XPATH =
        "//div[contains(@id,'ContentWizardToolbar')]/*[contains(@id, 'ActionButton') and child::span[text()='Delete']]";

    private static final String TOOLBAR_PREVIEW_BUTTON_XPATH =
        "//div[contains(@id,'ContentWizardToolbar')]/*[contains(@id, 'ActionButton') and child::span[text()='Preview']]";

    private static final String CONTEXT_WINDOW_TOGGLER =
        "//div[contains(@id,'ContentWizardToolbar')]/*[contains(@id, 'ContextWindowToggler')]";

    private final String UNLOCK_LINK = "//div[@class='centered']/a[text()='Unlock']";

    public static String START_WIZARD_TITLE = "New %s";

    @FindBy(xpath = TOOLBAR_SAVE_BUTTON_XPATH)
    protected WebElement toolbarSaveButton;

    @FindBy(xpath = TOOLBAR_CLOSE_WIZARD_BUTTON_XPATH)
    protected WebElement closeButton;

    @FindBy(xpath = TOOLBAR_PUBLISH_BUTTON_XPATH)
    private WebElement toolbarPublishButton;

    @FindBy(xpath = TOOLBAR_DELETE_BUTTON_XPATH)
    private WebElement toolbarDeleteButton;

    @FindBy(xpath = TOOLBAR_PREVIEW_BUTTON_XPATH)
    private WebElement toolbarPreviewButton;

    @FindBy(xpath = TOOLBAR_DUPLICATE_BUTTON_XPATH)
    private WebElement toolbarDuplicateButton;

    @FindBy(xpath = CONTEXT_WINDOW_TOGGLER)
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
        TestUtils.saveScreenshot( getSession(), "show_contwindow1" );
        ContextWindow cw = new ContextWindow( getSession() );
        if ( !cw.isContextWindowPresent() )
        {
            toolbarShowContextWindow.click();
            cw.waitUntilWindowLoaded( 1l );
            TestUtils.saveScreenshot( getSession(), "show_contwindow2" );
        }
        return cw;
    }

    public ContentWizardPanel clickToolbarPreview()
    {
        toolbarPreviewButton.click();
        sleep( 1000 );
        return this;
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

    public ContentWizardPanel unlockLiveEdit()
    {

        NavigatorHelper.switchToLiveEditFrame( getSession() );
        if ( findElements( By.xpath( UNLOCK_LINK ) ).size() == 0 )
        {
            TestUtils.saveScreenshot( getSession(), "unlock_not_present" );
            NavigatorHelper.switchToContentManagerFrame( getSession() );
            return this;
        }
        WebElement link = findElements( By.xpath( UNLOCK_LINK ) ).get( 0 );
        Actions builder = new Actions( getDriver() );
        builder.moveToElement( link ).perform();
        sleep( 1000 );
        builder.click( link ).build().perform();
        NavigatorHelper.switchToContentManagerFrame( getSession() );
        return this;
    }

    public boolean isLiveEditLocked()
    {
        WebElement frame = findElements( By.xpath( Application.LIVE_EDIT_FRAME ) ).get( 0 );
        Actions builder = new Actions( getDriver() );
        builder.moveToElement( frame ).build().perform();
        sleep( 1000 );
        TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "unlock" ) );
        NavigatorHelper.switchToLiveEditFrame( getSession() );
        LiveFormPanel liveEdit = new LiveFormPanel( getSession() );
        boolean result = liveEdit.isShaderDisplayed();
        NavigatorHelper.switchToContentManagerFrame( getSession() );
        return result;
    }

    /**
     * @return
     */
    public String getTitle()
    {
        List<WebElement> elems = getDriver().findElements( By.xpath( "//div[child::span[@class='tabcount']]/span[@class='label']" ) );
        if ( elems.size() > 0 )
        {
            return elems.get( 0 ).getText();
        }
        else
        {
            return null;
        }
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

    public ContentWizardStepForm clickDataStep()
    {
        clickWizardStep( 1 );
        return new ContentWizardStepForm( getSession() );
    }

    /**
     * Press the button 'Save', which located in the wizard's toolbar.
     */
    @Override
    public ContentWizardPanel save()
    {
        boolean isSaveButtonEnabled = waitUntilClickableNoException( By.xpath( TOOLBAR_SAVE_BUTTON_XPATH ), 2l );
        if ( !isSaveButtonEnabled )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "saving" ) );
            throw new SaveOrUpdateException( "Impossible to save, button 'Save' is not available!!" );
        }
        toolbarSaveButton.click();
        boolean isSaveEnabled = isEnabledSaveButton();
        if ( !isSaveEnabled )
        {
            throw new SaveOrUpdateException( "the content with  was not correctly saved, button 'Save' still disabled!" );
        }
        return this;
    }

    public ContentPublishDialog clickOnWizardPublishButton()
    {
        toolbarPublishButton.click();
        ContentPublishDialog dialog = new ContentPublishDialog( getSession() );
        dialog.waitUntilDialogShowed( Application.EXPLICIT_NORMAL );
        return dialog;
    }

    public boolean isEnabledSaveButton()
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
        return toolbarSaveButton.isDisplayed();

    }

    @Override
    public ContentWizardPanel waitUntilWizardOpened()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( DIV_CONTENT_WIZARD_PANEL ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            throw new TestFrameworkException( "ContentWizard was not showed!" );
        }
        return this;
    }

    @Override
    public String getWizardDivXpath()
    {

        return DIV_CONTENT_WIZARD_PANEL;
    }

    @Override
    public WebElement getCloseButton()
    {
        return closeButton;
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

    public ContentWizardPanel clickOnLiveToolbarButton()
    {
        if ( !waitUntilVisibleNoException( By.xpath( TOOLBAR_LIVE_BUTTON_XPATH ), Application.EXPLICIT_NORMAL ) )
        {
            throw new TestFrameworkException( "The 'Live' button not present!" );
        }
        findElements( By.xpath( TOOLBAR_LIVE_BUTTON_XPATH ) ).get( 0 ).click();
        sleep( 500 );
        return this;
    }

    public ContentWizardPanel selectPageDescriptor( String pageDescriptorDisplayName )
    {
        NavigatorHelper.switchToLiveEditFrame( getSession() );
        findElements( By.xpath( "//input[contains(@id,'DropdownOptionFilterInput')]" ) ).get( 0 ).sendKeys( pageDescriptorDisplayName );
        String item = String.format( "//h6[@class='main-name' and text()='%s']", pageDescriptorDisplayName );
        if ( !waitUntilVisibleNoException( By.xpath( item ), Application.EXPLICIT_NORMAL ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_" + pageDescriptorDisplayName );
            throw new TestFrameworkException( "drop-down-option-filter: item was not found!" + pageDescriptorDisplayName );
        }
        findElements( By.xpath( item ) ).stream().filter( WebElement::isDisplayed ).findFirst().get().click();
        sleep( 1000 );
        NavigatorHelper.switchToContentManagerFrame( getSession() );
        return this;
    }

    public boolean isPageDescriptorOptionsFilterDisplayed()
    {
        NavigatorHelper.switchToLiveEditFrame( getSession() );
        return findElements( By.xpath( "//input[contains(@id,'DropdownOptionFilterInput')]" ) ).stream().filter(
            WebElement::isDisplayed ).count() > 0;
    }

    public boolean isLiveEditFrameDisplayed()
    {
        return findElements( By.xpath( Application.LIVE_EDIT_FRAME ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

    public boolean isLiveButtonDisplayed()
    {
        return findElements( By.xpath( TOOLBAR_LIVE_BUTTON_XPATH ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
    }

}
