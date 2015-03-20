package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.AuthenticationException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel;
import com.enonic.autotests.pages.modules.ModuleBrowsePanel;
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel;
import com.enonic.autotests.services.NavigatorHelper;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.SleepHelper;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Page Object for 'Home' page. Version 5.0
 */
public class HomePage
    extends Page
{
    private final String CM_LINK = "//a[contains(@href,'content-manager')]//div[contains(.,'Content Manager')]";

    private final String USER_APP_LINK = "//a[contains(@href,'user-manager')]//div[contains(.,'User Manager')]";

    @FindBy(xpath = "//a[contains(@href,'content-manager')]//div[contains(.,'Content Manager')]")
    private WebElement contentManager;

    @FindBy(xpath = USER_APP_LINK)
    private WebElement userManager;

    @FindBy(xpath = "//a[contains(@href,'module-manager')]//div[contains(.,'Modules')]")
    private WebElement modules;


    /**
     * @param session
     */
    public HomePage( TestSession session )
    {
        super( session );
    }

    public void open( String username, String password )
    {
        String wh = getSession().getWindowHandle();
        if ( wh == null )
        {
            getSession().setWindowHandle( getDriver().getWindowHandle() );
        }
        // open page via the driver.get(BASE_URL)
        if ( getSession().getBaseUrl() != null )
        {
            getDriver().get( getSession().getBaseUrl() );
        }

        if ( !getSession().isLoggedIn() )
        {
            getLogger().info( "try to login with userName:" + username + " password: " + password );
            LoginPage loginPage = new LoginPage( getSession() );
            loginPage.doLogin( username, password );

            getSession().setLoggedIn( true );
        }
        else
        {
            getDriver().switchTo().window( wh );
        }

        if ( !isLoaded() )
        {
            throw new AuthenticationException( "Authentication failed, home page was not opened!" );
        }

    }

    boolean isLoaded()
    {
        boolean result =
            waitUntilVisibleNoException( By.xpath( "//div[contains(@id,'app.launcher.AppSelector')]" ), Application.EXPLICIT_3 );
        return result;
    }


    public void waitUntilContentManagerLoaded()
    {
        if ( !waitUntilVisibleNoException( By.xpath( CM_LINK ), Application.EXPLICIT_3 ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "cm_link" ) );
            throw new TestFrameworkException( "Content Manager link not present on the home Page!!" );
        }
    }

    public ContentBrowsePanel openContentManagerApplication()
    {
        if ( !waitUntilClickableNoException( By.xpath( CM_LINK ), Application.EXPLICIT_3 ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "ContentManagerLink" ) );
            throw new TestFrameworkException( "Content Manager link not clickable !" );
        }
        contentManager.click();
        sleep( 1000 );

        NavigatorHelper.switchToIframe( getSession(), Application.CONTENT_MANAGER_FRAME_XPATH );
        ContentBrowsePanel panel = new ContentBrowsePanel( getSession() );
        panel.waitUntilPageLoaded( Application.PAGE_LOAD_TIMEOUT );
        panel.waitsForSpinnerNotVisible();
        getLogger().info( "Content App loaded" );
        return panel;
    }

    public UserBrowsePanel openUserManagerApplication()
    {
        if ( !waitUntilClickableNoException( By.xpath( USER_APP_LINK ), Application.EXPLICIT_3 ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "userManagerLink" ) );
            throw new TestFrameworkException( "Content Manager link not clickable!" );

        }
        userManager.click();
        boolean isFrameLoaded = waitUntilVisibleNoException( By.xpath( UserBrowsePanel.USER_MANAGER_FRAME_XPATH ), Application.EXPLICIT_4 );
        if ( !isFrameLoaded )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "user-app" ) );
            throw new TestFrameworkException( "User app not loaded or is loading too long!" );

        }

        String whandle = getSession().getDriver().getWindowHandle();
        getSession().setWindowHandle( whandle );
        NavigatorHelper.switchToIframe( getSession(), Application.USER_MANAGER_FRAME_XPATH );
        UserBrowsePanel panel = new UserBrowsePanel( getSession() );
        panel.waitUntilPageLoaded( Application.PAGE_LOAD_TIMEOUT );
        panel.waitsForSpinnerNotVisible();
        getLogger().info( "User Manger App loaded" );
        return panel;
    }

    public ModuleBrowsePanel openModulesApplication()
    {
        TestUtils.saveScreenshot( getSession(), "home_module_1" );
        modules.click();
        SleepHelper.sleep( 1000 );
        TestUtils.saveScreenshot( getSession(), "home_module_2" );
        String whandle = getSession().getDriver().getWindowHandle();
        getSession().setWindowHandle( whandle );
        NavigatorHelper.switchToIframe( getSession(), Application.MODULE_MANAGER_FRAME_XPATH );
        ModuleBrowsePanel panel = new ModuleBrowsePanel( getSession() );
        // panel.waitUntilPageLoaded( Application.PAGE_LOAD_TIMEOUT );
        panel.waitsForSpinnerNotVisible();
        getLogger().info( "Module Manger App loaded" );
        return panel;
    }

    public boolean isContentManagerDisplayed()
    {
        return contentManager.isDisplayed();
    }
}
