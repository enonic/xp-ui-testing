package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel;
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel;
import com.enonic.autotests.services.NavigatorHelper;
import com.enonic.autotests.utils.NameHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Page Object for 'Home' page.
 */
public class HomePage
    extends Application
{
    public static final String CONTENT_STUDIO_URL_PART = "contentstudio";

    public static final String USER_MANAGER_URL_PART = "users";

    public static final String APPLICATIONS_URL_PART = "applications";

    public static final String HOME_PAGE_TAB_HANDLE = "home_window_id_key";

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
        return true;
    }

    public boolean isDashboardToolbarDisplayed()
    {
        return isElementDisplayed( HOME_DASHBOARD );
    }

    public ContentBrowsePanel openContentStudioApplication()
    {
        LauncherPanel launcherPanel = new LauncherPanel( getSession() );
        checkLauncher();
        launcherPanel.clickOnContentStudio();
        sleep( 500 );
        switchToContentBrowseTab();
        ContentBrowsePanel panel = new ContentBrowsePanel( getSession() );
        panel.waitUntilPageLoaded( Application.PAGE_LOAD_TIMEOUT );
        getLogger().info( "Content App loaded" );
        return panel;
    }

    private void switchToContentBrowseTab()
    {
        String contentBrowseTabHandle = NavigatorHelper.switchToBrowserTab( getSession(), CONTENT_STUDIO_URL_PART );
        getSession().put( HomePage.CONTENT_STUDIO_TAB_HANDLE, contentBrowseTabHandle );
    }

    private void switchToUsersTab()
    {
        NavigatorHelper.switchToBrowserTab( getSession(), USER_MANAGER_URL_PART );
    }

    public UserBrowsePanel openUsersApplication()
    {
        LauncherPanel launcherPanel = new LauncherPanel( getSession() );
        checkLauncher();
        launcherPanel.clickOnUsers();
        sleep( 700 );
        switchToUsersTab();
        UserBrowsePanel panel = new UserBrowsePanel( getSession() );
        panel.waitUntilPageLoaded( Application.PAGE_LOAD_TIMEOUT );
        panel.waitInvisibilityOfSpinner( Application.EXPLICIT_LONG );
        getLogger().info( "User App loaded" );
        return panel;
    }

    public boolean checkLauncher()
    {
        boolean isLauncherPresent = waitUntilVisibleNoException( By.xpath( LauncherPanel.CLOSE_LAUNCHER_BUTTON ), EXPLICIT_LONG );
        if ( !isLauncherPresent )
        {
            saveScreenshot( NameHelper.uniqueName( "err_launcher" ) );
            throw new TestFrameworkException( "launcher panel was not loaded in 3 seconds! It should be loaded by default" );
        }
        return true;
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
