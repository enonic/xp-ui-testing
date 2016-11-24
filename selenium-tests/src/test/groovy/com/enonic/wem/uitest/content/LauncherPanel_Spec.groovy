package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.LauncherPanel
import com.enonic.autotests.pages.LoginPage
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class LauncherPanel_Spec
    extends BaseGebSpec
{
    @Shared
    ContentBrowsePanel contentBrowsePanel;

    def setup()
    {
        go "admin"
    }

    def "WHEN home page opened THEN 'Launcher Panel' is displayed AND all control elements are present"()
    {
        when: "home page opened"
        NavigatorHelper.loginAndOpenHomePage( getTestSession() );

        and: "Xp Tour modal dialog closed"
        NavigatorHelper.closeXpTourDialogIfPresent( getSession() );
        TestUtils.saveScreenshot( getSession(), "launcher-panel-test" );
        LauncherPanel launcherPanel = new LauncherPanel( getSession() );

        then: "launcher panel displayed"
        launcherPanel.isDisplayed();

        and: "Applications link present"
        launcherPanel.isApplicationsLinkDisplayed();

        and: "Users link present"
        launcherPanel.isUsersLinkDisplayed();

        and: "Content Studio link present"
        launcherPanel.isContentStudioLinkDisplayed();

        and: "Home link present"
        launcherPanel.isHomeLinkDisplayed();

        and: "Logout link present"
        launcherPanel.isLogoutLinkDisplayed();

        and: "correct user's display name shown"
        launcherPanel.getUserDisplayName() == "Super User";

        and: "'close' button present on panel"
        launcherPanel.isCloseButtonDisplayed();
    }

    def "WHEN home page opened THEN 'Home' link is active on 'Launcher Panel'"()
    {
        when: "home page opened"
        NavigatorHelper.loginAndOpenHomePage( getTestSession() );

        and: "'Xp Tour' dialog closed"
        NavigatorHelper.closeXpTourDialogIfPresent( getSession() );
        TestUtils.saveScreenshot( getSession(), "launcher-panel-test" );
        LauncherPanel launcherPanel = new LauncherPanel( getSession() );

        then: "'Home' link is active on 'Launcher Panel'"
        launcherPanel.getActiveLink() == "Home";
    }

    def "WHEN 'content studio' opened  THEN 'Content Studio' link is active in 'Launcher Panel'"()
    {
        when: "'content studio' opened"
        NavigatorHelper.openContentStudioApp( getTestSession() );
        NavigatorHelper.closeXpTourDialogIfPresent( getSession() );

        and: "toggler pressed and launcher panel opened"
        LauncherPanel launcherPanel = new LauncherPanel( getSession() );
        launcherPanel.openPanel().waitUntilPanelLoaded();

        then:
        launcherPanel.getActiveLink() == "Content Studio";
    }

    def "WHEN 'Applications' opened  THEN 'Applications' link is active in 'Launcher Panel'"()
    {
        when: "'Applications' app opened "
        NavigatorHelper.openApplications( getTestSession() );

        and: "toggler pressed and launcher panel opened"
        LauncherPanel launcherPanel = new LauncherPanel( getSession() );
        launcherPanel.openPanel().waitUntilPanelLoaded();

        then:
        launcherPanel.getActiveLink() == "Applications";
    }

    def "WHEN 'Users' app opened  THEN 'Users' link is active in 'Launcher Panel'"()
    {
        when: "'Users' app opened"
        NavigatorHelper.openUsersApp( getTestSession() );
        NavigatorHelper.closeXpTourDialogIfPresent( getSession() );

        and: "launcher panel opened"
        LauncherPanel launcherPanel = new LauncherPanel( getSession() );
        launcherPanel.openPanel().waitUntilPanelLoaded();
        TestUtils.saveScreenshot( getSession(), "test_launcher_application" );

        then:
        launcherPanel.getActiveLink() == "Users";
    }

    def "GIVEN home page opened WHEN button 'close' clicked THEN 'Launcher Panel' not displayed AND button with lines appears"()
    {
        given: "home page opened"
        NavigatorHelper.loginAndOpenHomePage( getTestSession() );
        NavigatorHelper.closeXpTourDialogIfPresent( getSession() );
        LauncherPanel launcherPanel = new LauncherPanel( getSession() );

        when: "close button pressed"
        launcherPanel.clickOnCloseButton();
        sleep( 500 );

        then: "'Launcher Panel' not displayed"
        launcherPanel.waitUntilLauncherClosed();

        and: "button with 'lines' appears"
        launcherPanel.isOpenLauncherButtonPresent();
        TestUtils.saveScreenshot( getSession(), "launcher-closed-home-page" );

        and: "button 'close' not displayed now"
        !launcherPanel.isCloseButtonDisplayed();
    }

    def "GIVEN home page opened WHEN 'Log out' link clicked THEN 'login page' appears AND correct title displayed"()
    {
        given: "home page opened"
        NavigatorHelper.loginAndOpenHomePage( getTestSession() );
        NavigatorHelper.closeXpTourDialogIfPresent( getSession() );

        when: "'Log out' link clicked"
        LauncherPanel launcherPanel = new LauncherPanel( getSession() );
        launcherPanel.clickOnLogout();
        LoginPage loginPage = new LoginPage( getSession() );

        then: "login page loaded"
        loginPage.isPageLoaded();

        and: "correct title displayed"
        loginPage.getTitle().equals( LoginPage.TITLE );
    }
}
