package com.enonic.wem.uitest.application

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.modules.InstallAppDialog
import com.enonic.autotests.pages.modules.InstallAppDialog_MarketAppPanel
import com.enonic.autotests.utils.TestUtils
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
    String LOCAL_APP_NAME = "first_app";

    @Shared
    String CONTENT_VIEWER_APP = "Content Viewer App";

    @Shared
    String CONTENT_VIEWER_APP_INSTALLED_NAME = "contentviewer";

    def "GIVEN 'install app' dialog opened AND 'upload' tab activated WHEN an application uploaded THEN new application successfully installed"()
    {
        given: "'install app' dialog opened AND 'upload' tab activated"
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();
        appDialog.clickOnUploadTab();

        when: "the application uploaded"
        appDialog.duUploadApplication( LOCAL_PATH_TO_APP );
        String notificationMessage = applicationBrowsePanel.waitNotificationMessage( Application.EXPLICIT_NORMAL );
        TestUtils.saveScreenshot( getSession(), "app_install" )

        then: "new application listed in the grid"
        applicationBrowsePanel.exists( APP_NAME );

        and: "actual notification message and expected are identical"
        notificationMessage == String.format( Application.APP_INSTALLED_MESSAGE, APP_DISPLAY_NAME )
    }

    def "GIVEN existing not local application EXPECTED icon for local application not displayed"()
    {
        expect:
        !applicationBrowsePanel.isApplicationLocal( APP_NAME );

        and: "but an application, that is 'local' has a required icon"
        applicationBrowsePanel.isApplicationLocal( LOCAL_APP_NAME );
    }

    def "WHEN existing local application selected THEN 'uninstall' button is disabled"()
    {
        when:
        applicationBrowsePanel.selectRowByName( LOCAL_APP_NAME )

        then:
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
        String appUpdatedMessage = applicationBrowsePanel.waitNotificationMessage( Application.EXPLICIT_NORMAL );
        TestUtils.saveScreenshot( getSession(), "app_install" )

        then: "updated application listed in the grid"
        applicationBrowsePanel.exists( APP_NAME );

        and: "actual notification message and expected are identical"
        appUpdatedMessage == String.format( Application.APP_UPDATED_MESSAGE, APP_DISPLAY_NAME )
    }

    def "WHEN existing not local application selected THEN 'uninstall' button is enabled"()
    {
        when:
        applicationBrowsePanel.selectRowByName( APP_NAME )

        then:
        applicationBrowsePanel.isUninstallButtonEnabled();
    }

    def "WHEN one local application and one not local application are selected THEN 'uninstall' button is disabled"()
    {
        when:
        applicationBrowsePanel.clickCheckboxAndSelectRow( APP_NAME );
        applicationBrowsePanel.clickCheckboxAndSelectRow( LOCAL_APP_NAME );

        then:
        !applicationBrowsePanel.isUninstallButtonEnabled();
    }

    def "GIVEN existing not local application selected WHEN 'uninstall' button pressed THEN application not listed AND correct notification appears"()
    {
        given: "existing not local application selected"
        applicationBrowsePanel.selectRowByName( APP_NAME )

        when: "'uninstall' button pressed"
        String message = applicationBrowsePanel.clickOnToolbarUninstall().clickOnYesButton().waitNotificationMessage(
            Application.EXPLICIT_NORMAL );
        TestUtils.saveScreenshot( getSession(), "app_uninstall" );

        then: "application not listed"
        !applicationBrowsePanel.exists( APP_NAME );

        and: "correct notification appears"
        message == String.format( Application.APP_UNINSTALLED, APP_DISPLAY_NAME );
    }

    def "GIVEN 'install app' dialog opened and 'Enonic Market' selected WHEN an application from the 'Enonic Market' installed THEN new application listed in the browse panel "()
    {
        given:
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();
        InstallAppDialog_MarketAppPanel marketPanel = new InstallAppDialog_MarketAppPanel( getSession() );
        sleep( 2000 );

        when: "an application from the 'Enonic Market' installed"
        marketPanel.doInstallApp( CONTENT_VIEWER_APP_DISPLAY_NAME );
        String notificationMessage = applicationBrowsePanel.waitNotificationMessage( Application.EXPLICIT_NORMAL );
        TestUtils.saveScreenshot( getSession(), "app_notification_message" );
        appDialog.clickOnCancelButton();

        then: "correct notification message appears"
        notificationMessage == String.format( Application.APP_INSTALLED_MESSAGE, CONTENT_VIEWER_APP );
        TestUtils.saveScreenshot( getSession(), "app_from_market" );

        and: "new application listed in the browse panel"
        applicationBrowsePanel.exists( CONTENT_VIEWER_APP_INSTALLED_NAME );

        and: "icon for local applications not present"
        !applicationBrowsePanel.isApplicationLocal( CONTENT_VIEWER_APP_INSTALLED_NAME );

        and: "application status is 'started'"
        applicationBrowsePanel.waitApplicationStatus( CONTENT_VIEWER_APP_INSTALLED_NAME, STARTED_STATE );
    }

    def "GIVEN existing installed from the market application WHEN Install App Dialog opened THEN the application is disabled in the 'market'"()
    {
        when:
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();
        InstallAppDialog_MarketAppPanel marketPanel = new InstallAppDialog_MarketAppPanel( getSession() );
        sleep( 1000 );

        then: "install button disabled for application that was already installed"
        marketPanel.isApplicationAlreadyInstalled( CONTENT_VIEWER_APP_DISPLAY_NAME );
    }

    def "GIVEN installed from 'Enonic Market' application WHEN the application selected and context-menu shown THEN all menu-items have correct state"()
    {
        when: "context menu opened"
        applicationBrowsePanel.openContextMenu( CONTENT_VIEWER_APP_INSTALLED_NAME );
        TestUtils.saveScreenshot( getSession(), "not-local-app-context-menu" );

        then: "Delete menu item is enabled"
        applicationBrowsePanel.isContextMenuItemEnabled( "Stop" );

        and: "Edit menu item is enabled"
        !applicationBrowsePanel.isContextMenuItemEnabled( "Start" );

        and: "New menu item is enabled"
        applicationBrowsePanel.isContextMenuItemEnabled( "Uninstall" );
    }

    def "GIVEN existing installed application from 'Enonic Market' WHEN application uninstalled THEN it not listed in the grid AND correct notification message appears"()
    {
        given: "existing application from 'Enonic Market' is selected"
        applicationBrowsePanel.selectRowByName( CONTENT_VIEWER_APP_INSTALLED_NAME );

        when: "'uninstall' button pressed"
        String message = applicationBrowsePanel.clickOnToolbarUninstall().clickOnYesButton().waitNotificationMessage(
            Application.EXPLICIT_NORMAL );
        TestUtils.saveScreenshot( getSession(), "enonic_app_uninstall" );

        then: "application not listed"
        !applicationBrowsePanel.exists( CONTENT_VIEWER_APP_INSTALLED_NAME );

        and: "correct notification appears"
        message == String.format( Application.APP_UNINSTALLED, CONTENT_VIEWER_APP );
    }
}
