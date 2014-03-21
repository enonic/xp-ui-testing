package com.enonic.autotests.pages.accounts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.WizardPanel;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.User;

public class AddNewUserWizard
    extends WizardPanel
{

    public static final String SYSTEM_STORE_NAME = "userstores\\system";

    private final String EMAIL_NOT_AVAILABLE_MESSAGE = "Not available";

    private final String EMAIL_AVAILABLE_MESSAGE = "Available";

    private final String USER_NAME_INVALID_CHARS = "Invalid characters";

    public static final String TOOLBAR_CLOSE_WIZARD_BUTTON_XPATH =
        "//div[@id='app.wizard.UserWizardToolbar']/*[contains(@id, 'api.ui.ActionButton') and child::span[text()='Close']]";

    public static final String TOOLBAR_SAVE_BUTTON_XPATH =
        "//div[@id='app.wizard.UserWizardToolbar']/*[contains(@id, 'api.ui.ActionButton') and child::span[text()='Save']]";

    private final String EMAIL_VALIDATION_MESSAGE_XPATH =
        "//table[contains(@class,'x-form-item')]//div[contains(@class,'x-box-inner') and descendant::input[@name='email']]/descendant::div[@class='validationError' or @class='validationInfo']";

    private final String USERNAME_VALIDATION_ERRORMESSAGE_XPATH =
        "//table[contains(@class,'x-form-item')]//div[contains(@class,'x-box-inner') and descendant::input[@name='name']]/descendant::div[@class='validationError']";

    public static final String PAGE_TITLE = "New User";


    @FindBy(how = How.NAME, using = "qualifiedName")
    private WebElement qualifiedName;

    @FindBy(how = How.NAME, using = "name")
    private WebElement nameInput;

    @FindBy(how = How.NAME, using = "email")
    private WebElement emailInput;

    @FindBy(how = How.NAME, using = "password")
    private WebElement passwordInput;

    @FindBy(how = How.NAME, using = "repeatPassword")
    private WebElement repeatPasswordInput;

    @FindBy(xpath = TOOLBAR_CLOSE_WIZARD_BUTTON_XPATH)
    protected WebElement closeButton;

    @FindBy(xpath = TOOLBAR_SAVE_BUTTON_XPATH)
    protected WebElement toolbarSaveButton;

    /**
     * The constructor.
     *
     * @param session {@link TestSession} instance
     */
    public AddNewUserWizard( TestSession session )
    {
        super( session );
    }

    public void doTypeDataSaveAndClose( User user, boolean isNew )
    {
        doTypeDataAndSave( user, isNew );
        closeButton.click();
        AccountsPage accountspage = new AccountsPage( getSession() );
        long start = System.currentTimeMillis();
        boolean isLoaded = accountspage.isPageLoaded();
        long end = System.currentTimeMillis();
        long res = end - start;

        if ( !isLoaded )
        {
            logError( "expected page was not loaded. Waiting time is :" + res );
            throw new SaveOrUpdateException(
                "buttons 'Save ' and 'Close' in the wizard page were pressed, but Page, that contains all accounts, was not loaded" );
        }

    }

    /**
     * Gets validation message for UserName input.
     *
     * @return validation message.('Invalid characters').
     */
    public String getUserNameValidationMessage()
    {
        boolean isPresentMessage = waitUntilVisibleNoException( By.xpath( USERNAME_VALIDATION_ERRORMESSAGE_XPATH ), 1 );
        if ( isPresentMessage )
        {
            return findElement( By.xpath( USERNAME_VALIDATION_ERRORMESSAGE_XPATH ) ).getText();
        }
        else
        {
            return null;
        }
    }

    /**
     * Gets validation message for Email input.
     *
     * @return validation message.('Invalid e-mail', 'available' or 'Not
     * available')
     */
    public String getEmailValidationMessage()
    {
        boolean isPresentMessage = waitUntilVisibleNoException( By.xpath( EMAIL_VALIDATION_MESSAGE_XPATH ), 2 );
        if ( isPresentMessage )
        {
            return getDriver().findElement( By.xpath( EMAIL_VALIDATION_MESSAGE_XPATH ) ).getText();
        }
        else
        {
            return null;
        }

    }

    /**
     * Types space's data and press the 'Save' button on the toolbar.
     *
     * @param user  {@link User} instance
     * @param isNew
     */
    public void doTypeDataAndSave( User user, boolean isNew )
    {
        clearAndType( displayNameInput, user.getUserInfo().getDisplayName() );
        clearAndType( nameInput, user.getUserInfo().getDisplayName() );
        if ( isNew )
        {
            nameInput.sendKeys( user.getUserInfo().getName() );
        }
        else
        {
            getLogger().info( "The user name input is disabled. When user is edited, this element should be disabled!" );
        }

        String nameValdationErrorMessage = getUserNameValidationMessage();
        if ( nameValdationErrorMessage != null )
        {
            getLogger().info( "userinfo:  There validation message for User Name. " + nameValdationErrorMessage );
            boolean isInvalid = waitAndCheckAttrValue( nameInput, "aria-invalid", "true", 2l );
            getLogger().info( "userinfo, nameInput attribute 'aria-invalid' has value:  " + nameValdationErrorMessage );
            // TODO should be "Save" disabled if there is error validation
            // message?
            // Assert.fail("Add new User Wizard: there is error message near the 'User Name' input, but the state of the input is valid! The value of attribute 'aria-invalid' is true! ");
            // verifySaveButtonState(nameValdationErrorMessage);
        }// nameInput.getAttribute("aria-invalid").contains("true");

        clearAndType( emailInput, user.getUserInfo().getEmail() );
        String emailValdationMessage = getEmailValidationMessage();
        if ( emailValdationMessage != null )
        {
            getLogger().info( "userinfo: email is" + emailValdationMessage );
            verifySaveButtonState( emailValdationMessage );
        }

        if ( isNew )
        {
            passwordInput.sendKeys( user.getUserInfo().getPassword() );

            if ( user.getUserInfo().getRepeatPassword() != null )
            {
                repeatPasswordInput.sendKeys( user.getUserInfo().getRepeatPassword() );
            }
            else
            {
                repeatPasswordInput.sendKeys( user.getUserInfo().getPassword() );
            }
            boolean isInvalid = waitAndCheckAttrValue( repeatPasswordInput, "aria-invalid", "true", 1l );
            getLogger().info( "userinfo, repeatPasswordInput has  attribute 'invalid' with value equals:  " + isInvalid );
            if ( isInvalid )
            {
                if ( toolbarSaveButton.isEnabled() )
                {
                    TestUtils.saveScreenshot( getSession() );
                    Assert.fail(
                        "There is repeatPassword-validation error message, Button 'Save' on the toolbar is enabled, but should be disabled! " );
                }
            }
        }

        TestUtils.saveScreenshot( getSession() );
        // if save button is disabled, so exception will be thrown:
        save();
        boolean isSaveEnabled = isEnabledSaveButton();
        if ( !isSaveEnabled )
        {
            throw new SaveOrUpdateException(
                "the user with name" + user.getUserInfo().getName() + " was not correctly saved, button 'Save' still disabled!" );
        }
    }


    private void verifySaveButtonState( String validationMessage )
    {
        if ( validationMessage.equals( EMAIL_NOT_AVAILABLE_MESSAGE ) )
        {
            if ( toolbarSaveButton.isEnabled() )
            {
                TestUtils.saveScreenshot( getSession() );
                Assert.fail( "There is email-validation error message, Button 'Save' on the toolbar is enabled, but should be disabled! " );
            }
        }
        if ( validationMessage.equals( USER_NAME_INVALID_CHARS ) )
        {
            if ( toolbarSaveButton.isEnabled() )
            {
                TestUtils.saveScreenshot( getSession() );
                Assert.fail(
                    "There is username-validation error message, Button 'Save' on the toolbar is enabled, but should be disabled! " );
            }
        }
    }

    /**
     * Press the button 'Save', which located in the wizard's toolbar.
     */
    public WizardPanel save()
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

    public void close()
    {
        closeButton.click();
    }
}
