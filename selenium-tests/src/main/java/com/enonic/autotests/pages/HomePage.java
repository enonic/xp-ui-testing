package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel;
import com.enonic.autotests.pages.modules.ModuleBrowsePanel;
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel;
import com.enonic.autotests.services.NavigatorHelper;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page Object for 'Home' page. Version 5.0
 */
public class HomePage
    extends Page
{
    @FindBy(xpath = "//a[contains(@href,'content-manager')]//div[contains(.,'Content Manager')]")
    private WebElement contentManager;

    @FindBy(xpath = "//a[contains(@href,'user-manager')]//div[contains(.,'User Manager')]")
    private WebElement userManager;

    @FindBy(xpath = "//a[contains(@href,'module-manager')]//div[contains(.,'Modules')]")
    private WebElement modules;


    @FindBy(xpath = "//div[@class = 'name-container' and text()='Schema Manager']")
    private WebElement schemaManager;

    @FindBy(xpath = "//div[@class = 'name-container' and text()='Accounts']")
    private WebElement accounts;

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
        waitUntilAllFramesLoaded();

    }

    /**
     *
     */
    public void waitUntilAllFramesLoaded()
    {
        waitUntilVisibleNoException( By.xpath( "//div[contains(@id,'app.launcher.AppSelector')]" ), Application.EXPLICIT_3 );
    }


    public ContentBrowsePanel openContentManagerApplication()
    {
        TestUtils.saveScreenshot( getSession(), "home3" );
        contentManager.click();
        NavigatorHelper.switchToIframe( getSession(), Application.CONTENT_MANAGER_FRAME_XPATH );
        ContentBrowsePanel panel = new ContentBrowsePanel( getSession() );
        // panel.waitUntilPageLoaded( Application.PAGE_LOAD_TIMEOUT );
        panel.waitsForSpinnerNotVisible();
        getLogger().info( "Content App loaded" );
        return panel;
    }

    public UserBrowsePanel openUserManagerApplication()
    {
        userManager.click();
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
        modules.click();
        String whandle = getSession().getDriver().getWindowHandle();
        getSession().setWindowHandle( whandle );
        NavigatorHelper.switchToIframe( getSession(), Application.MODULE_MANAGER_FRAME_XPATH );
        ModuleBrowsePanel panel = new ModuleBrowsePanel( getSession() );
        // panel.waitUntilPageLoaded( Application.PAGE_LOAD_TIMEOUT );
        panel.waitsForSpinnerNotVisible();
        getLogger().info( "Module Manger App loaded" );
        return panel;
    }
}
