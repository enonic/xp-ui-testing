package com.enonic.wem.uitest.application

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.modules.InstallAppDialog
import com.enonic.autotests.utils.TestUtils
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class InstallApplication_Spec
    extends BaseApplicationSpec
{
    @Shared
    String LOCAL_PATH_TO_FILE = "/test-data/app/install-app.jar";

    @Shared
    String APP_DISPLAY_NAME = "application for installing";

    @Shared
    String APP_NAME = "install_app";

    @Shared
    String LOCAL_APP_NAME = "first_app";

    @Shared
    String CONTENT_VIEWER_APP_NAME = "Content viewer";

    @Shared
    String CONTENT_VIEWER_APP_DISPLAY_NAME = "Content Viewer App";

    @Shared
    String CONTENT_VIEWER_APP_INSTALLED_NAME = "contentviewer";

    def "GIVEN 'install app' dialog opened WHEN an application uploaded THEN new application successfully installed"()
    {
        given:
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();


        when: "an application uploaded"
        appDialog.duUploadApplication( LOCAL_PATH_TO_FILE );
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
        appDialog.duUploadApplication( LOCAL_PATH_TO_FILE );
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
        appDialog.clickOnEnonicMarketTab();

        when: "an application from the 'Enonic Market' installed"
        appDialog.doInstallAppFromEnonicMarket( CONTENT_VIEWER_APP_NAME );
        String notificationMessage = applicationBrowsePanel.waitNotificationMessage( Application.EXPLICIT_NORMAL );
        appDialog.clickOnCancelButton();

        then: "correct notification message appears"
        TestUtils.saveScreenshot( getSession(), "app_from_market" );
        notificationMessage == String.format( Application.APP_INSTALLED_MESSAGE, CONTENT_VIEWER_APP_DISPLAY_NAME );

        and: "new application listed in the browse panel"
        applicationBrowsePanel.exists( CONTENT_VIEWER_APP_INSTALLED_NAME );

        and: "icon for local applications not present"
        !applicationBrowsePanel.isApplicationLocal( CONTENT_VIEWER_APP_INSTALLED_NAME );

        and: "application status is 'started'"
        applicationBrowsePanel.getApplicationStatus( CONTENT_VIEWER_APP_INSTALLED_NAME ) == STARTED_STATE;
    }

    def "GIVEN installed from 'Enonic Market' application WHEN the application selected and context-menu shown THEN all menu-items have correct state"()
    {
        when: "context menu opened"
        applicationBrowsePanel.openContextMenu( CONTENT_VIEWER_APP_INSTALLED_NAME );
        TestUtils.saveScreenshot( getSession(), "not-local-app-context-menu" );

        then: "Delete menu item is enabled"
        applicationBrowsePanel.isEnabledContextMenuItem( "Stop" );

        and: "Edit menu item is enabled"
        !applicationBrowsePanel.isEnabledContextMenuItem( "Start" );

        and: "New menu item is enabled"
        applicationBrowsePanel.isEnabledContextMenuItem( "Uninstall" );
    }

    def "GIVEN installed application from 'Enonic Market' WHEN application uninstalled THEN it not listed in the grid AND correct notification message appears"()
    {
        given: "existing application from 'Enonic Market' selected"
        applicationBrowsePanel.selectRowByName( CONTENT_VIEWER_APP_INSTALLED_NAME );

        when: "'uninstall' button pressed"
        String message = applicationBrowsePanel.clickOnToolbarUninstall().clickOnYesButton().waitNotificationMessage(
            Application.EXPLICIT_NORMAL );
        TestUtils.saveScreenshot( getSession(), "enonic_app_uninstall" );

        then: "application not listed"
        !applicationBrowsePanel.exists( CONTENT_VIEWER_APP_INSTALLED_NAME );

        and: "correct notification appears"
        message == String.format( Application.APP_UNINSTALLED, APP_DISPLAY_NAME );
    }
}
