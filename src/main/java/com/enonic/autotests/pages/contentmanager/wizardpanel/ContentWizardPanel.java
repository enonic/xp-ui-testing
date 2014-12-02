package com.enonic.autotests.pages.contentmanager.wizardpanel;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.WizardPanel;
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
        "//div[@id='app.wizard.ContentWizardToolbar']/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Duplicate']]";

    public static final String DIV_CONTENT_WIZARD_PANEL =
        "//div[contains(@id,'app.wizard.ContentWizardPanel') and not(contains(@style,'display: none'))]";

    public static final String TOOLBAR_SAVE_BUTTON_XPATH =
        "//div[contains(@id,'app.wizard.ContentWizardToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Save']]";

    public static final String TOOLBAR_CLOSE_WIZARD_BUTTON_XPATH =
        "//div[contains(@id,'app.wizard.ContentWizardToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Close']]";

    private static final String TOOLBAR_PUBLISH_BUTTON_XPATH =
        "//div[@id='app.wizard.ContentWizardToolbar']/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Publish']]";

    private static final String TOOLBAR_DELETE_BUTTON_XPATH =
        "//div[contains(@id,'app.wizard.ContentWizardToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Delete']]";

    public static String START_WIZARD_TITLE = "New %s";

    @FindBy(xpath = TOOLBAR_SAVE_BUTTON_XPATH)
    protected WebElement toolbarSaveButton;

    @FindBy(xpath = TOOLBAR_CLOSE_WIZARD_BUTTON_XPATH)
    protected WebElement closeButton;

    @FindBy(xpath = TOOLBAR_PUBLISH_BUTTON_XPATH)
    private WebElement toolbarPublishButton;

    @FindBy(xpath = TOOLBAR_DELETE_BUTTON_XPATH)
    private WebElement toolbarDeleteButton;

    @FindBy(xpath = TOOLBAR_DUPLICATE_BUTTON_XPATH)
    private WebElement toolbarDuplicateButton;

    /**
     * The constructor.
     *
     * @param session
     */
    public ContentWizardPanel( TestSession session )
    {
        super( session );

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
        waitElementClickable( By.name( "displayName" ), 2 );
        getLogger().info( "types displayName: " + content.getDisplayName() );
        clearAndType( displayNameInput, content.getDisplayName() );
        sleep( 500 );
        if ( StringUtils.isNotEmpty( content.getName() ) )
        {
            waitElementClickable( By.name( "name" ), 2 );
            getLogger().info( "types name: " + content.getName() );
            clearAndType( nameInput, content.getName() );
        }

        TestUtils.saveScreenshot( getSession(), content.getName() );
        // 2. populate main tab
        if ( content.getPropertyTree() != null )
        {

            clickDataStep().type( content.getPropertyTree() );
        }

        return this;
    }

    public ContentWizardPanel typeDisplayName( String displayName )
    {
        clearAndType( displayNameInput, displayName );
        return this;
    }

    public ContentWizardPanel typeName( String name )
    {
        clearAndType( nameInput, name );
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
        boolean isSaveButtonEnabled = waitUntilElementEnabledNoException( By.xpath( TOOLBAR_SAVE_BUTTON_XPATH ), 2l );
        if ( !isSaveButtonEnabled )
        {
            throw new SaveOrUpdateException( "Impossible to save, button 'Save' is disabled!" );
        }
        toolbarSaveButton.click();
        boolean isSaveEnabled = isEnabledSaveButton();
        if ( !isSaveEnabled )
        {
            throw new SaveOrUpdateException( "the content with  was not correctly saved, button 'Save' still disabled!" );
        }
        return this;

    }

    public boolean isEnabledSaveButton()
    {
        return waitUntilElementEnabledNoException( By.xpath( TOOLBAR_SAVE_BUTTON_XPATH ), Application.IMPLICITLY_WAIT );
    }

    @Override
    public boolean isOpened()
    {
        return toolbarSaveButton.isDisplayed();

    }

    @Override
    public ContentWizardPanel waitUntilWizardOpened()
    {
        //waitUntilVisibleNoException(by, timeout)
        boolean result = waitUntilVisibleNoException( By.xpath( DIV_CONTENT_WIZARD_PANEL ), Application.EXPLICIT_4 );
        findElements( By.xpath( DIV_CONTENT_WIZARD_PANEL ) );
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

}
