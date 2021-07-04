package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.LauncherPanel
import com.enonic.autotests.pages.LoginPage
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
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

    def "WHEN home page has been opened THEN 'Launcher Panel' is displayed AND all expected control elements should be present on the page"()
    {
        when: "home page has been opened"
        NavigatorHelper.loginAndOpenHomePage( getTestSession() );
        LauncherPanel launcherPanel = new LauncherPanel( getSession() );

        then: "launcher panel should be displayed"
        launcherPanel.waitForDisplayed();

        and: "'Applications link' should be present"
        launcherPanel.isApplicationsLinkDisplayed();

        and: "'Users link' should be present"
        launcherPanel.isUsersLinkDisplayed();

        and: "'Content Studio' link should be present"
        launcherPanel.isContentStudioLinkDisplayed();

        and: "Home link should be present"
        launcherPanel.isHomeLinkDisplayed();

        and: "Logout link should be  present"
        launcherPanel.isLogoutLinkDisplayed();

        and: "expected user's display name should be shown"
        launcherPanel.getUserDisplayName() == "Super User";

        and: "button with 'XP' icon should be present on the panel"
        launcherPanel.iButtonCloseLauncherDisplayed();

        and:
        launcherPanel.getActiveLink() == "Home";
    }


    def "GIVEN 'content studio' is opened WHEN Launcher Panel toggler has been clicked THEN Launcher Panel gets visible AND 'Content Studio' link should be active in 'Launcher Panel'"()
    {
        given: "'content studio' is opened"
        NavigatorHelper.openContentStudioApp( getTestSession() );

        when: "toggler has been pressed and launcher panel gets visible"
        LauncherPanel launcherPanel = new LauncherPanel( getSession() );
        launcherPanel.openPanel().waitUntilPanelLoaded();

        then: "'Content Studio' link should be active in 'Launcher Panel'"
        launcherPanel.getActiveLink() == "Content Studio";
    }
    
    def "WHEN 'Users' has been opened THEN 'Users' link should be active in 'Launcher Panel'"()
    {
        when: "'Users' has been opened"
        NavigatorHelper.openUsersApp( getTestSession() );

        and: "launcher panel opened"
        LauncherPanel launcherPanel = new LauncherPanel( getSession() );
        launcherPanel.openPanel().waitUntilPanelLoaded();
        saveScreenshot( "test_launcher_application" );

        then:
        launcherPanel.getActiveLink() == "Users";
    }

    def "GIVEN home page is opened WHEN button 'close' has been clicked THEN 'Launcher Panel' should close AND button with lines appears"()
    {
        given: "home page is opened"
        NavigatorHelper.loginAndOpenHomePage( getTestSession() );
        LauncherPanel launcherPanel = new LauncherPanel( getSession() );

        when: "close button has been pressed"
        launcherPanel.clickOnCloseButton();
        sleep( 500 );

        then: "'Launcher Panel' should not be displayed"
        launcherPanel.waitUntilLauncherClosed();

        and: "button with 'XP' appears"
        launcherPanel.isOpenLauncherButtonPresent();
        saveScreenshot( "launcher-closed-home-page" );

        and: " button with 'close' icon should not be displayed"
        !launcherPanel.iButtonCloseLauncherDisplayed();
    }

    def "GIVEN home page is opened WHEN 'Log out' link has been clicked THEN 'login page' should appear AND expected title should be displayed"()
    {
        given: "home page is opened"
        NavigatorHelper.loginAndOpenHomePage( getTestSession() );

        when: "'Log out' link has been clicked"
        LauncherPanel launcherPanel = new LauncherPanel( getSession() );
        launcherPanel.clickOnLogout();
        LoginPage loginPage = new LoginPage( getSession() );

        then: "login page should be loaded"
        loginPage.isPageLoaded();

        and: "expected title should be displayed"
        loginPage.getTitle() == LoginPage.TITLE;
    }
}
