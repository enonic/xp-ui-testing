package com.enonic.autotests.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.NameHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public class LauncherPanel
    extends Application
{
    public static final String PANEL_DIV = "//div[contains(@class,'launcher-panel')]";

    public static final String CLOSE_LAUNCHER_BUTTON = "//button[contains(@class,'launcher-button')]";

    private final String ACTIVE_ROW_TEXT = PANEL_DIV + "//div[@class='app-row active']//p[@class='app-name']";

    public static final String OPEN_LAUNCHER_BUTTON = "//button[contains(@class,'launcher-button')]/span[@class='lines']";

    private final String APPLICATIONS_LINK = PANEL_DIV + "//a[contains(@href,'applications')]//p[@class='app-name']";

    private final String USERS_LINK = PANEL_DIV + "//a[contains(@href,'users')]//p[@class='app-name']";

    private final String CONTENT_STUDIO_LINK = PANEL_DIV + "//a[contains(@href,'contentstudio')]//p[@class='app-name']";

    private final String HOME_LINK = PANEL_DIV + "//a[@href='/admin/tool']";

    private final String LOGOUT_LINK = PANEL_DIV + "//a[contains(@href,'logout')]";

    private final String USER_DISPLAY_NAME = PANEL_DIV + "//div[@class='user-info']//p";

    @FindBy(xpath = CLOSE_LAUNCHER_BUTTON)
    WebElement closePanelButton;

    @FindBy(xpath = APPLICATIONS_LINK)
    WebElement applicationsLink;

    @FindBy(xpath = USERS_LINK)
    WebElement usersLink;

    @FindBy(xpath = CONTENT_STUDIO_LINK)
    WebElement contentStudioLink;

    @FindBy(xpath = LOGOUT_LINK)
    WebElement logoutLink;

    @FindBy(xpath = HOME_LINK)
    WebElement homeLink;

    public LauncherPanel( final TestSession session )
    {
        super( session );
    }

    public boolean isDisplayed()
    {
        if ( findElements( By.xpath( PANEL_DIV ) ).size() == 0 )
        {
            throw new TestFrameworkException( "Launcher was not found!" );
        }
        WebElement launcherPanel = findElement( By.xpath( PANEL_DIV ) );
        return waitAndCheckAttrValue( launcherPanel, "class", "visible", Application.EXPLICIT_NORMAL );
    }

    public LauncherPanel clickOnApplications()
    {
        boolean isClickable = waitUntilClickableNoException( By.xpath( APPLICATIONS_LINK ), Application.EXPLICIT_NORMAL );
        if ( !isClickable )
        {
            saveScreenshot( NameHelper.uniqueName( "err_app_link" ) );
            throw new TestFrameworkException( "applications link is not displayed" );
        }
        applicationsLink.click();
        return this;
    }

    public LauncherPanel clickOnCloseButton()
    {
        if ( !isElementDisplayed( CLOSE_LAUNCHER_BUTTON ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err_close_launcher" ) );
            throw new TestFrameworkException( "close button is not displayed" );
        }
        closePanelButton.click();
        return this;
    }

    public LauncherPanel clickOnUsers()
    {
        boolean isClickable = waitUntilClickableNoException( By.xpath( USERS_LINK ), Application.EXPLICIT_NORMAL );
        if ( !isClickable )
        {
            saveScreenshot( NameHelper.uniqueName( "err_user_link" ) );
            throw new TestFrameworkException( "user-manager link is not displayed" );
        }
        usersLink.click();
        return this;
    }

    public String getActiveLink()
    {
        if ( !waitUntilVisibleNoException( By.xpath( ACTIVE_ROW_TEXT ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_launcher_active_link" );
            throw new TestFrameworkException( "link was not found!" );
        }
        return getDisplayedString( ACTIVE_ROW_TEXT );
    }

    public LauncherPanel clickOnContentStudio()
    {
        boolean isDisplayed = waitUntilVisibleNoException( By.xpath( CONTENT_STUDIO_LINK ), Application.EXPLICIT_NORMAL );
        if ( !isDisplayed )
        {
            //saveScreenshot( NameHelper.uniqueName( "err_content_studio_link" ) );
            throw new TestFrameworkException( "content-studio link is not displayed" );
        }
        contentStudioLink.click();
        return this;
    }

    public LauncherPanel clickOnLogout()
    {
        boolean isClickable = waitUntilClickableNoException( By.xpath( LOGOUT_LINK ), Application.EXPLICIT_NORMAL );
        if ( !isClickable )
        {
            saveScreenshot( NameHelper.uniqueName( "err_logout_link" ) );
            throw new TestFrameworkException( "logout link is not displayed" );
        }
        logoutLink.click();
        return this;
    }

    public boolean waitUntilLauncherClosed()
    {
        By launcherBy = By.xpath( PANEL_DIV );
        if ( findElements( launcherBy ).size() == 0 )
        {
            saveScreenshot( NameHelper.uniqueName( "err_launcher" ) );
            throw new TestFrameworkException( "Launcher was not found!" );
        }
        return waitAndCheckAttrValue( findElement( launcherBy ), "class", "slideout", Application.EXPLICIT_NORMAL );
    }

    public boolean isUsersLinkDisplayed()
    {
        return isElementDisplayed( USERS_LINK );
    }

    public boolean isApplicationsLinkDisplayed()
    {
        return isElementDisplayed( APPLICATIONS_LINK );
    }

    public boolean isContentStudioLinkDisplayed()
    {
        return isElementDisplayed( CONTENT_STUDIO_LINK );
    }

    public boolean isHomeLinkDisplayed()
    {
        return isElementDisplayed( HOME_LINK );
    }

    public boolean isLogoutLinkDisplayed()
    {
        return isElementDisplayed( LOGOUT_LINK );
    }

    public boolean isCloseButtonDisplayed()
    {
        return isElementDisplayed( CLOSE_LAUNCHER_BUTTON );
    }


    public String getUserDisplayName()
    {
        if ( !isElementDisplayed( USER_DISPLAY_NAME ) )
        {
            saveScreenshot( "err_launcher_user_name" );
            throw new TestFrameworkException( "user name was not found on the launcher panel!" );
        }
        return getDisplayedString( USER_DISPLAY_NAME );
    }

    public boolean isOpenLauncherButtonPresent()
    {
        return isElementDisplayed( OPEN_LAUNCHER_BUTTON );
    }

    public LauncherPanel openPanel()
    {
        boolean isLoaded = waitUntilVisibleNoException( By.xpath( OPEN_LAUNCHER_BUTTON ), Application.EXPLICIT_NORMAL );
        if ( !isLoaded )
        {
            saveScreenshot( NameHelper.uniqueName( "err_launcher_button_load" ) );
            throw new TestFrameworkException( "launcher button was not loaded!" );
        }
        getDisplayedElement( By.xpath( OPEN_LAUNCHER_BUTTON ) ).click();

        return this;
    }

    public void waitUntilPanelLoaded()
    {
        boolean isVisible = waitUntilVisibleNoException( By.xpath( PANEL_DIV ), Application.EXPLICIT_NORMAL );
        if ( !isVisible )
        {
            saveScreenshot( NameHelper.uniqueName( "err_launcher_load" ) );
            throw new TestFrameworkException( "launcher panel was not loaded!" );
        }
    }
}
