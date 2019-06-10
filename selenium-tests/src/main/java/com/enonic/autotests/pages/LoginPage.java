package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.NameHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class LoginPage
    extends Application
{
    public final String HOME_CONTAINER = "//div[contains(@class,'home-main-container')]";

    public static final String TITLE = "Enonic XP - Login";

    private final String EMAIL_INPUT_XPATH = "//input[@placeholder = 'user id or email']";

    private String loginButtonXpath = "//div[@class='password-container']//button[contains(@class,'login-button')]";

    private String ERROR_LOGIN_MESSAGE = HOME_CONTAINER + "//div[contains(@id,'message-container')]";

    public static final String LOGIN_FAILED = "Login failed!";

    @FindBy(xpath = EMAIL_INPUT_XPATH)
    private WebElement usernameInput;

    @FindBy(xpath = "//input[@placeholder = 'password']")
    private WebElement passwordInput;

    /**
     * The constructor.
     *
     * @param session
     */
    public LoginPage( TestSession session )
    {
        super( session );
    }

    public HomePage doLogin( String username, String password )
    {
        getLogger().info( "try to login with userName:" + username + " password: " + password );
        LoginPage loginPage = new LoginPage( getSession() );
        loginPage.typeNameAndPassword( username, password );
        getSession().setLoggedIn( true );
        sleep( 500 );
        return new HomePage( getSession() );
    }

    /**
     * Types the user name and password and press the 'login' button.
     *
     * @param username
     * @param password
     */
    public void typeNameAndPassword( String username, String password )
    {
        if ( !isPageLoaded() )
        {
            String screenshotName = NameHelper.uniqueName( "login_page_error" );
            saveScreenshot( screenshotName );
            throw new TestFrameworkException( "Login page was not loaded, timeout sec:" + Application.EXPLICIT_NORMAL );
        }
        getLogger().info( "Login action started. Username: " + username + " Password:" + password );

        boolean isEmailInputPresent = waitAndFind( By.xpath( EMAIL_INPUT_XPATH ) );
        if ( !isEmailInputPresent )
        {
            throw new TestFrameworkException( " input 'user id or e-mail' was not found on page!" );
        }
        clearAndType( usernameInput, username );
        clearAndType( passwordInput, password );
        passwordInput.sendKeys( Keys.ENTER );
        sleep( 100 );
    }

    public boolean isPageLoaded()
    {
        boolean isLoginPageLoaded = waitUntilVisibleNoException( By.xpath( EMAIL_INPUT_XPATH ), Application.EXPLICIT_NORMAL );
        if ( !isLoginPageLoaded )
        {
            saveScreenshot( NameHelper.uniqueName( "err_load_login_page" ) );
            throw new TestFrameworkException( "login page was not loaded!" );
        }
        return isLoginPageLoaded;
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( EMAIL_INPUT_XPATH );
    }

    public String getTitle()
    {
        return getDriver().getTitle();
    }

    public boolean isErrorMessageDisplayed()
    {
        return isElementDisplayed( ERROR_LOGIN_MESSAGE );
    }

    public String getErrorMessage()
    {
        return getDisplayedString( ERROR_LOGIN_MESSAGE );
    }
}
