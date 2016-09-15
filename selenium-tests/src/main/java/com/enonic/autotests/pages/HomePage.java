package com.enonic.autotests.pages;

import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.XP_Windows;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel;
import com.enonic.autotests.pages.modules.ApplicationBrowsePanel;
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Page Object for 'Home' page.
 */
public class HomePage
    extends Application
{
    public static final String APP_WINDOW_ID = "app_window_id_key";

    public static final String HOME_WINDOW_ID = "home_window_id_key";

    private final String HOME_DASHBOARD = "//div[contains(@class,'home-main-dashboard')]";

    private final String DASHBOARD_ITEM = "//div[contains(@class,'dashboard-item')]";

    private final String ABOUT_LINK = DASHBOARD_ITEM + "//div[text()='About']";

    private final String XP_TOUR_LINK = DASHBOARD_ITEM + "//div[text()='XP Tour']";

    private final String DOCS_LINK = DASHBOARD_ITEM + "//div[text()='Docs']";

    private final String DISCUSS_LINK = DASHBOARD_ITEM + "//div[text()='Discuss']";

    private final String MARKET_LINK = DASHBOARD_ITEM + "//div[text()='Market']";

    private final String HOME_MAIN_CONTAINER = "//div[@class='home-main-container']";

    @FindBy(xpath = XP_TOUR_LINK)
    private WebElement xpTourLink;

    @FindBy(xpath = ABOUT_LINK)
    private WebElement aboutLink;


    /**
     * @param session
     */
    public HomePage( TestSession session )
    {
        super( session );
    }

    boolean waitUntilLoaded()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( HOME_MAIN_CONTAINER ), Application.EXPLICIT_NORMAL );
        if ( !result )
        {
            saveScreenshot( NameHelper.uniqueName( "err_home_load" ) );
            throw new TestFrameworkException( "home page was not loaded" );
        }
        return result;
    }

    public boolean isDashboardToolbarDisplayed()
    {
        return isElementDisplayed( HOME_DASHBOARD );
    }

    public ContentBrowsePanel openContentManagerApplication()
    {
        LauncherPanel launcherPanel = new LauncherPanel( getSession() );
        if ( !launcherPanel.isDisplayed() )
        {
            saveScreenshot( "err_launcher_display" );
            throw new TestFrameworkException( "launcher panel should be displayed by default" );
        }
        launcherPanel.clickOnContentStudio();
        sleep( 1300 );
        switchToAppWindow( "content-studio" );
        getSession().setCurrentWindow( XP_Windows.CONTENT_STUDIO );
        ContentBrowsePanel panel = new ContentBrowsePanel( getSession() );
        panel.waitUntilPageLoaded( Application.PAGE_LOAD_TIMEOUT );
        getLogger().info( "Content App loaded" );
        return panel;
    }

    public UserBrowsePanel openUserManagerApplication()
    {
        LauncherPanel launcherPanel = new LauncherPanel( getSession() );
        if ( !launcherPanel.isDisplayed() )
        {
            saveScreenshot( "err_launcher_display" );
            throw new TestFrameworkException( "launcher panel should be displayed by default" );
        }
        launcherPanel.clickOnUsers();
        sleep( 1000 );
        switchToAppWindow( "user-manager" );
        getSession().setCurrentWindow( XP_Windows.USER_MANAGER );
        UserBrowsePanel panel = new UserBrowsePanel( getSession() );
        panel.waitUntilPageLoaded( Application.PAGE_LOAD_TIMEOUT );
        panel.waitsForSpinnerNotVisible();
        getLogger().info( "User Manger App loaded" );
        return panel;
    }

    public ApplicationBrowsePanel openApplications()
    {
        LauncherPanel launcherPanel = new LauncherPanel( getSession() );
        if ( !launcherPanel.isDisplayed() )
        {
            saveScreenshot( "err_launcher_display" );
            throw new TestFrameworkException( "launcher panel should be displayed by default" );
        }
        launcherPanel.clickOnApplications();
        sleep( 1000 );
        switchToAppWindow( "applications" );
        getSession().setCurrentWindow( XP_Windows.APPLICATIONS );
        ApplicationBrowsePanel panel = new ApplicationBrowsePanel( getSession() );
        panel.waitsForSpinnerNotVisible();
        panel.waitUntilPageLoaded( Application.EXPLICIT_NORMAL );
        getLogger().info( "Applications App opened" );
        return panel;
    }

    public void switchToAppWindow( String appName )
    {
        String current = getDriver().getWindowHandle();
        Set<String> allWindows = getDriver().getWindowHandles();

        if ( !allWindows.isEmpty() )
        {
            for ( String windowId : allWindows )
            {
                try
                {
                    if ( getDriver().switchTo().window( windowId ).getCurrentUrl().contains( appName ) )
                    {
                        getSession().put( APP_WINDOW_ID, windowId );
                        getSession().put( HOME_WINDOW_ID, current );
                        return;
                    }
                }
                catch ( NoSuchWindowException e )
                {
                    throw new TestFrameworkException( "NoSuchWindowException- wrong ID" + e.getLocalizedMessage() );
                }
            }
        }
        else
        {
            TestUtils.saveScreenshot( getSession(), "new tab was not opened!" );
            throw new TestFrameworkException( "error during switching to   " + appName );
        }
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( HOME_MAIN_CONTAINER );
    }

    public boolean isXP_Tour_Displayed()
    {
        return isElementDisplayed( XP_TOUR_LINK );
    }

    public boolean isAbout_Displayed()
    {
        return isElementDisplayed( ABOUT_LINK );
    }

    public boolean isDocs_Displayed()
    {
        return isElementDisplayed( DOCS_LINK );
    }

    public boolean isMarket_Displayed()
    {
        return isElementDisplayed( MARKET_LINK );
    }

    public boolean isDiscus_Displayed()
    {
        return isElementDisplayed( DISCUSS_LINK );
    }

    public XpTourDialog clickOnXpTourLink()
    {
        xpTourLink.click();
        sleep( 200 );
        XpTourDialog dialog = new XpTourDialog( getSession() );
        dialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        return dialog;
    }

    public AboutDialog clickOnAboutLink()
    {
        aboutLink.click();
        sleep( 200 );
        AboutDialog dialog = new AboutDialog( getSession() );
        dialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        return dialog;
    }

}
