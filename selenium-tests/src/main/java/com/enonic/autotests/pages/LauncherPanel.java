package com.enonic.autotests.pages;


import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LauncherPanel
        extends Application {
    private final String PANEL_DIV = "//div[contains(@class,'launcher-panel')]";

    private final String CLOSE_LAUNCHER_BUTTON = "//button[contains(@class,'launcher-button toggled')]";

    private final String APPLICATIONS_LINK = PANEL_DIV + "//a[contains(@href,'applications')]";

    private final String USERS_LINK = PANEL_DIV + "//a[contains(@href,'user-manager')]";

    private final String CM_LINK = PANEL_DIV + "//a[contains(@href,'content-studio')]";


    @FindBy(xpath = APPLICATIONS_LINK)
    WebElement applicationsLink;

    @FindBy(xpath = USERS_LINK)
    WebElement usersLink;

    @FindBy(xpath = CM_LINK)
    WebElement contentMangerLink;

    public LauncherPanel(final TestSession session) {
        super(session);
    }

    public boolean isDisplayed() {
        return isElementDisplayed(PANEL_DIV);
    }

    public LauncherPanel clickOnApplications() {
        if (!isElementDisplayed(APPLICATIONS_LINK)) {
            TestUtils.saveScreenshot(getSession(), NameHelper.uniqueName("err_app_link"));
            throw new TestFrameworkException("applications link is not displayed");
        }
        applicationsLink.click();
        return this;
    }

    public LauncherPanel clickOnUsers() {
        if (!isElementDisplayed(USERS_LINK)) {
            TestUtils.saveScreenshot(getSession(), NameHelper.uniqueName("err_user_link"));
            throw new TestFrameworkException("user-manager link is not displayed");
        }
        usersLink.click();
        return this;
    }

    public LauncherPanel clickOnContentManager() {
        if (!isElementDisplayed(CM_LINK)) {
            TestUtils.saveScreenshot(getSession(), NameHelper.uniqueName("err_cm_link"));
            throw new TestFrameworkException("content-manager link is not displayed");
        }
        contentMangerLink.click();
        return this;
    }

    public void waitUntilLauncherLoaded() {
        if (!waitUntilVisibleNoException(By.xpath(PANEL_DIV), Application.EXPLICIT_NORMAL)) {
            TestUtils.saveScreenshot(getSession(), NameHelper.uniqueName("err_launcher"));
            throw new TestFrameworkException("Launcher Panel was not loaded!");
        }
    }

    public void closeLauncherPanel() {
        if (isElementDisplayed(CLOSE_LAUNCHER_BUTTON)) {
            TestUtils.saveScreenshot(getSession(), "err_close_launcher");
            throw new TestFrameworkException("button close was not found!");
        }
        getDisplayedElement(By.xpath(CLOSE_LAUNCHER_BUTTON)).click();
    }
}
