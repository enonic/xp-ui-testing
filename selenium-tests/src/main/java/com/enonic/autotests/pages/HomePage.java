package com.enonic.autotests.pages;

import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;

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
        extends Application {
    private final String HOME_MAIN_CONTAINER = "//div[@class='home-main-container']";


    /**
     * @param session
     */
    public HomePage(TestSession session) {
        super(session);
    }

    boolean waitUntilLoaded() {
        boolean result = waitUntilVisibleNoException(By.xpath(HOME_MAIN_CONTAINER), Application.EXPLICIT_NORMAL);
        if (!result) {
            TestUtils.saveScreenshot(getSession(), NameHelper.uniqueName("err_home_load"));
            throw new TestFrameworkException("home page was not loaded");
        }
        return result;
    }

    public ContentBrowsePanel openContentManagerApplication() {
        LauncherPanel launcherPanel = new LauncherPanel(getSession());
        if (!launcherPanel.isDisplayed()) {
            TestUtils.saveScreenshot(getSession(), "err_launcher_display");
            throw new TestFrameworkException("launcher panel should be displayed by default");
        }
        launcherPanel.clickOnContentManager();
        sleep(1000);
        switchToAppWindow( "content-studio" );
        getSession().setCurrentWindow( XP_Windows.CONTENT_STUDIO );
        ContentBrowsePanel panel = new ContentBrowsePanel(getSession());
        panel.waitUntilPageLoaded(Application.PAGE_LOAD_TIMEOUT);
        panel.waitsForSpinnerNotVisible();
        getLogger().info("Content App loaded");
        return panel;
    }

    public UserBrowsePanel openUserManagerApplication() {
        LauncherPanel launcherPanel = new LauncherPanel(getSession());
        if (!launcherPanel.isDisplayed()) {
            TestUtils.saveScreenshot(getSession(), "err_launcher_display");
            throw new TestFrameworkException("launcher panel should be displayed by default");
        }
        launcherPanel.clickOnUsers();
        sleep(1000);
        switchToAppWindow("user-manager");
        getSession().setCurrentWindow( XP_Windows.USER_MANAGER );
        UserBrowsePanel panel = new UserBrowsePanel(getSession());
        panel.waitUntilPageLoaded(Application.PAGE_LOAD_TIMEOUT);
        panel.waitsForSpinnerNotVisible();
        getLogger().info("User Manger App loaded");
        return panel;
    }

    public ApplicationBrowsePanel openApplications() {
        LauncherPanel launcherPanel = new LauncherPanel(getSession());
        if (!launcherPanel.isDisplayed()) {
            TestUtils.saveScreenshot(getSession(), "err_launcher_display");
            throw new TestFrameworkException("launcher panel should be displayed by default");
        }
        launcherPanel.clickOnApplications();
        sleep(1000);
        switchToAppWindow("applications");
        getSession().setCurrentWindow( XP_Windows.APPLICATIONS );
        ApplicationBrowsePanel panel = new ApplicationBrowsePanel(getSession());
        panel.waitsForSpinnerNotVisible();
        panel.waitUntilPageLoaded(Application.EXPLICIT_NORMAL);
        getLogger().info("Applications App opened");
        return panel;
    }

    public void switchToAppWindow(String appName) {
        String current = getDriver().getWindowHandle();
        Set<String> allWindows = getDriver().getWindowHandles();

        if (!allWindows.isEmpty()) {
            for (String windowId : allWindows) {
                try {
                    if (getDriver().switchTo().window(windowId).getCurrentUrl().contains(appName)) {
                        getSession().put(APP_WINDOW_ID, windowId);
                        getSession().put(HOME_WINDOW_ID, current);
                        return;
                    }
                } catch (NoSuchWindowException e) {
                    throw new TestFrameworkException("NoSuchWindowException- wrong ID" + e.getLocalizedMessage());
                }
            }
        }
        throw new TestFrameworkException( "application was not found!" + appName );
    }

    public boolean isDisplayed() {
        return isElementDisplayed(HOME_MAIN_CONTAINER);
    }
}
