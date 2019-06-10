package com.enonic.wem.uitest.application

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.modules.InstallAppDialog
import com.enonic.autotests.pages.modules.InstallAppDialog_MarketAppPanel
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class InstallApplication_Spec
    extends BaseApplicationSpec
{
    @Shared
    String LOCAL_PATH_TO_APP = "/test-data/app/install-app.jar";

    @Shared
    String APP_DISPLAY_NAME = "application for installing";

    @Shared
    String APP_NAME = "install_app";

    @Shared
    String LOCAL_APP_DISPLAY_NAME = "Second Selenium App";

    @Shared
    String CONTENT_VIEWER_APP = "Content Viewer App";

    @Shared
    String CONTENT_VIEWER_APP_INSTALLED_NAME = "Inspect your content object JSON";

    @Ignore
    def "GIVEN 'install app' dialog opened  WHEN an application has been uploaded THEN new application should be listed in the grid"()
    {
        given: "'install app' dialog opened AND 'upload' tab activated"
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();


        when: "the application has been uploaded"
        appDialog.duUploadApplication( LOCAL_PATH_TO_APP );
        String notificationMessage = applicationBrowsePanel.waitForNotificationMessage( Application.EXPLICIT_NORMAL );
        saveScreenshot( "local_app_installed" );

        then: "new application should be listed in the grid"
        applicationBrowsePanel.exists( APP_NAME );

        and: "actual notification message and expected should be identical"
        notificationMessage == String.format( APP_INSTALLED_MESSAGE, APP_DISPLAY_NAME )
    }

    def "GIVEN existing 'local' application EXPECTED the application should be displayed with 'local'-icon"()
    {
        expect: "existing 'local' application"
        applicationBrowsePanel.isGridItemPresent( LOCAL_APP_DISPLAY_NAME );

        and: "the application should be displayed with 'local'-icon"
        applicationBrowsePanel.isApplicationByDisplayNameLocal( LOCAL_APP_DISPLAY_NAME );
    }

    def "WHEN existing local application is selected THEN 'uninstall' button should be disabled"()
    {
        when: "existing local application is selected"
        applicationBrowsePanel.selectRowByItemDisplayName( LOCAL_APP_DISPLAY_NAME )

        then: "'uninstall' button should be disabled"
        !applicationBrowsePanel.isUninstallButtonEnabled();
    }

    //TODO remove Ignore, when the 'updating' message will bew added in XP
    @Ignore
    def "GIVEN 'install app' dialog opened WHEN an application, that already installed, uploaded again THEN correct notification message appears "()
    {
        given:
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();

        when: "an application uploaded"
        appDialog.duUploadApplication( LOCAL_PATH_TO_APP );
        String appUpdatedMessage = applicationBrowsePanel.waitForNotificationMessage( Application.EXPLICIT_NORMAL );
        saveScreenshot( "app_was_uploaded" )

        then: "updated application listed in the grid"
        applicationBrowsePanel.exists( APP_NAME );

        and: "actual notification message and expected are identical"
        appUpdatedMessage == String.format( APP_UPDATED_MESSAGE, APP_DISPLAY_NAME )
    }


    @Ignore
    def "WHEN one local application and one not local application are selected THEN 'uninstall' button is disabled"()
    {
        when:
        applicationBrowsePanel.clickCheckboxAndSelectRow( APP_NAME );
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( LOCAL_APP_DISPLAY_NAME );

        then:
        !applicationBrowsePanel.isUninstallButtonEnabled();
    }

    @Ignore
    def "GIVEN existing not local application selected WHEN 'uninstall' button pressed THEN application not listed AND correct notification appears"()
    {
        given: "existing not local application selected"
        applicationBrowsePanel.clickOnRowByName( APP_NAME )

        when: "'uninstall' button pressed"
        String message = applicationBrowsePanel.clickOnToolbarUninstall().clickOnYesButton().waitForNotificationMessage();
        saveScreenshot( "app_uninstalled" );

        then: "application not listed"
        !applicationBrowsePanel.exists( APP_NAME );

        and: "correct notification appears"
        message == String.format( APP_UNINSTALLED, APP_DISPLAY_NAME );
    }

    def "GIVEN 'install app' dialog is opened WHEN an application has been installed from the 'Enonic Market' THEN new application should be listed in the browse panel "()
    {
        given: "'install app' dialog is opened"
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();
        InstallAppDialog_MarketAppPanel marketPanel = new InstallAppDialog_MarketAppPanel( getSession() );
        sleep( 2000 );

        when: "application has been installed from the 'Enonic Market'"
        marketPanel.doInstallApp( CONTENT_VIEWER_APP_DISPLAY_NAME );
        String notificationMessage = applicationBrowsePanel.waitForNotificationMessage();
        saveScreenshot( "app_installed_notification_message" );
        appDialog.clickOnCancelButton();
        sleep( 1000 );

        then: "correct notification message should appear"
        notificationMessage == String.format( APP_INSTALLED_MESSAGE, CONTENT_VIEWER_APP );
        saveScreenshot( "app_from_market" );

        and: "new application should be listed in the browse panel"
        applicationBrowsePanel.exists( CONTENT_VIEWER_APP_INSTALLED_NAME );

        and: "icon for local applications should not be displayed"
        !applicationBrowsePanel.isApplicationLocal( CONTENT_VIEWER_APP_INSTALLED_NAME );

        and: "application status should be 'started'"
        applicationBrowsePanel.waitApplicationStatus( CONTENT_VIEWER_APP_INSTALLED_NAME, STARTED_STATE );
    }

    def "GIVEN existing installed from the market application WHEN ;Install App; Dialog is opened THEN the application should be disabled on the dialog"()
    {
        when: "Install App Dialog is opened"
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();
        InstallAppDialog_MarketAppPanel marketPanel = new InstallAppDialog_MarketAppPanel( getSession() );
        sleep( 1000 );
        saveScreenshot( "test_app_installed_in_dialog" );

        then: "install button should be disabled for application that was already installed"
        marketPanel.isApplicationAlreadyInstalled( CONTENT_VIEWER_APP_DISPLAY_NAME );
    }

    def "WHEN existing not local application is selected THEN 'uninstall' button should be enabled"()
    {
        when: "existing not local application is selected"
        applicationBrowsePanel.clickOnRowByName( CONTENT_VIEWER_APP_INSTALLED_NAME )

        then: "'uninstall' button should be enabled"
        applicationBrowsePanel.isUninstallButtonEnabled();
    }

    def "GIVEN installed from 'Enonic Market' application WHEN the application selected and context-menu shown THEN all menu-items have correct state"()
    {
        when: "context menu is opened"
        applicationBrowsePanel.openContextMenu( CONTENT_VIEWER_APP_INSTALLED_NAME );
        saveScreenshot( "not-local-app-context-menu" );

        then: "'Stop' menu item should be enabled"
        applicationBrowsePanel.isContextMenuItemEnabled( "Stop" );

        and: "'Start' menu item should be disabled"
        !applicationBrowsePanel.isContextMenuItemEnabled( "Start" );

        and: "'Uninstall' menu item should be enabled"
        applicationBrowsePanel.isContextMenuItemEnabled( "Uninstall" );
    }

    def "GIVEN existing installed application from 'Enonic Market' WHEN application uninstalled THEN it not listed in the grid AND correct notification message appears"()
    {
        given: "existing application from 'Enonic Market' is selected"
        applicationBrowsePanel.clickOnRowByName( CONTENT_VIEWER_APP_INSTALLED_NAME );

        when: "'uninstall' button has been pressed"
        String message = applicationBrowsePanel.clickOnToolbarUninstall().clickOnYesButton().waitForNotificationMessage();
        saveScreenshot( "enonic_app_uninstalled" );

        then: "application should not be listed"
        !applicationBrowsePanel.exists( CONTENT_VIEWER_APP_INSTALLED_NAME );

        and: "correct notification should be displayed"
        message == String.format( APP_UNINSTALLED, CONTENT_VIEWER_APP );
    }
}
