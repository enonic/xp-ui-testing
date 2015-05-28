package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class LoginPage
    extends Page
{

    private String TITLE = "Enonic XP Admin";

    private long LOGIN_PAGE_TIMEOUT = 10;

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

    /**
     * Types the user name and password and press the 'login' button.
     *
     * @param username
     * @param password
     */
    public void doLogin( String username, String password )
    {
        boolean isLoginPageLoaded = waitUntilTitleLoad( TITLE, LOGIN_PAGE_TIMEOUT );
        if ( !isLoginPageLoaded )
        {
            String name = NameHelper.uniqueName( "login_page_error" );
            TestUtils.saveScreenshot( getSession(), name );
            throw new TestFrameworkException( "Login page was not loaded, timeout sec:" + LOGIN_PAGE_TIMEOUT );
        }
        getLogger().info( "Login page title: " + getDriver().getTitle() );
        getLogger().info( "Login action started. Username: " + username + " Password:" + password );

        boolean isEmailInputPresent = waitAndFind( By.xpath( EMAIL_INPUT_XPATH ) );
        if ( !isEmailInputPresent )
        {
            throw new TestFrameworkException( " input 'user id or e-mail' was not found on page!" );
        }
        usernameInput.sendKeys( username );
        sleep( 300 );

        passwordInput.sendKeys( password );

//        boolean isEnabledButton = waitAndFind( By.xpath( String.format( loginButtonXpath, loginEnabledClass ) ) );
//        if ( !isEnabledButton )
//        {
//            logError( "The button 'Log in' is disabled" );
//            throw new AuthenticationException( "wrong password or username" );
//        }
//        WebElement loginButton = findElement( By.xpath( String.format( loginButtonXpath, loginEnabledClass ) ) );
//        loginButton.click();
        passwordInput.sendKeys( Keys.ENTER );

    }

}
