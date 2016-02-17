package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.AuthenticationException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class LoginPage
    extends Page
{

    public static final String TITLE = "Login - Enonic XP Admin";

    private final String EMAIL_INPUT_XPATH = "//input[@placeholder = 'userid or e-mail']";

    private String loginButtonXpath = "//div[@class='password-container']//button[contains(@class,'login-button')]";


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
        sleep( 1000 );
        Boolean isLauncherPresent =
            waitUntilVisibleNoException( By.xpath( LauncherPanel.CLOSE_LAUNCHER_BUTTON ), Application.EXPLICIT_NORMAL );
        if ( !isLauncherPresent )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_login" ) );
            throw new AuthenticationException( "Authentication failed, launcher panel was not loaded!" );
        }
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
            String name = NameHelper.uniqueName( "login_page_error" );
            TestUtils.saveScreenshot( getSession(), name );
            throw new TestFrameworkException( "Login page was not loaded, timeout sec:" + Application.EXPLICIT_NORMAL );
        }
        getLogger().info( "Login action started. Username: " + username + " Password:" + password );

        boolean isEmailInputPresent = waitAndFind( By.xpath( EMAIL_INPUT_XPATH ) );
        if ( !isEmailInputPresent )
        {
            throw new TestFrameworkException( " input 'user id or e-mail' was not found on page!" );
        }
        clearAndType( usernameInput, username );
        sleep( 300 );
        clearAndType( passwordInput, password );
        sleep( 300 );
        passwordInput.sendKeys( Keys.ENTER );
        sleep( 200 );
    }

    public boolean isPageLoaded()
    {
        boolean isLoginPageLoaded = waitUntilVisibleNoException( By.xpath( EMAIL_INPUT_XPATH ), Application.EXPLICIT_NORMAL );
        if ( !isLoginPageLoaded )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_login_page" ) );
            throw new TestFrameworkException( "login page not loaded!" );
        }
        return isLoginPageLoaded;
    }

    public String getTitle()
    {
        return getDriver().getTitle();
    }
}
