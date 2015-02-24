package com.enonic.autotests.pages.usermanager.wizardpanel;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.WizardPanel;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.usermanager.User;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public class UserWizardPanel
    extends WizardPanel<User>
{
    public static final String DIV_USER_WIZARD_PANEL =
        "//div[contains(@id,'app.wizard.UserWizardPanel') and not(contains(@style,'display: none'))]";

    public static final String TOOLBAR_SAVE_BUTTON =
        "//div[contains(@id,'app.wizard.PrincipalWizardToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Save']]";

    public static final String TOOLBAR_CLOSE_WIZARD_BUTTON =
        "//div[contains(@id,'app.wizard.PrincipalWizardToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Close']]";

    private static final String TOOLBAR_DUPLICATE_BUTTON =
        "//div[@id='app.wizard.PrincipalWizardToolbar']/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Duplicate']]";

    private static final String TOOLBAR_DELETE_BUTTON =
        "//div[contains(@id,'app.wizard.PrincipalWizardToolbar')]/*[contains(@id, 'api.ui.button.ActionButton') and child::span[text()='Delete']]";

    @FindBy(xpath = "//input[@type = 'email']")
    protected WebElement emailInput;

    @FindBy(xpath = TOOLBAR_SAVE_BUTTON)
    protected WebElement toolbarSaveButton;

    @FindBy(xpath = TOOLBAR_CLOSE_WIZARD_BUTTON)
    protected WebElement closeButton;

    @FindBy(xpath = TOOLBAR_DELETE_BUTTON)
    private WebElement toolbarDeleteButton;

    @FindBy(xpath = TOOLBAR_DUPLICATE_BUTTON)
    private WebElement toolbarDuplicateButton;


    /**
     * The constructor.
     *
     * @param session
     */
    public UserWizardPanel( TestSession session )
    {
        super( session );

    }

    @Override
    public String getWizardDivXpath()
    {
        return DIV_USER_WIZARD_PANEL;
    }

    @Override
    public WizardPanel<User> save()
    {
        toolbarSaveButton.click();
        sleep( 1000 );
        return this;
    }

    @Override
    public WebElement getCloseButton()
    {
        return closeButton;
    }

    @Override
    public WizardPanel<User> typeData( final User user )
    {
        // 1. type a data: 'name' and 'Display Name'.
        waitElementClickable( By.name( "displayName" ), 2 );
        getLogger().info( "types displayName: " + user.getDisplayName() );
        clearAndType( displayNameInput, user.getDisplayName() );
        sleep( 500 );
        clearAndType( emailInput, user.getEmail() );
        sleep( 1000 );
        TestUtils.saveScreenshot( getSession(), user.getDisplayName() );
        // 2. populate main tab

        return this;
    }

    public UserWizardPanel typeDisplayName( String displayName )
    {
        clearAndType( displayNameInput, displayName );
        return this;
    }

    @Override
    public boolean isEnabledSaveButton()
    {
        return waitUntilElementEnabledNoException( By.xpath( TOOLBAR_SAVE_BUTTON ), Application.IMPLICITLY_WAIT );
    }

    @Override
    public boolean isOpened()
    {
        return toolbarSaveButton.isDisplayed();

    }

    @Override
    public UserWizardPanel waitUntilWizardOpened()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( DIV_USER_WIZARD_PANEL ), Application.EXPLICIT_4 );
        findElements( By.xpath( DIV_USER_WIZARD_PANEL ) );
        if ( !result )
        {
            throw new TestFrameworkException( "UserWizard was not showed!" );
        }
        return this;
    }


}
