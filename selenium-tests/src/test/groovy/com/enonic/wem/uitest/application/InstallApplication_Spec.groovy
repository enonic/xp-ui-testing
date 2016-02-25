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

        and:
        applicationBrowsePanel.isApplicationLocal( LOCAL_APP_NAME );
    }

    def "WHEN existing local application selected THEN 'uninstall' button is disabled"()
    {
        when:
        applicationBrowsePanel.selectRowByName( LOCAL_APP_NAME )

        then:
        !applicationBrowsePanel.isUninstallButtonEnabled();
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
        String message = applicationBrowsePanel.clickOnToolbarUninstall().waitNotificationMessage( Application.EXPLICIT_NORMAL );
        TestUtils.saveScreenshot( getSession(), "app_uninstall" );

        then: "application not listed"
        !applicationBrowsePanel.exists( APP_NAME );

        and: "correct notification appears"
        message == String.format( Application.APP_UNINSTALLED, APP_DISPLAY_NAME );
    }

    @Ignore
    def "GIVEN 'install app' dialog opened and 'Enonic Market' selected WHEN an application from the 'Enonic Market' installed THEN new application listed in the browse panel "()
    {
        given:
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();
        appDialog.clickOnEnonicMarketTab();

        when: "an application from the 'Enonic Market' installed"
        appDialog.doInstallAppFromEnonicMarket( "Content viewer" );
        String notificationMessage = applicationBrowsePanel.waitNotificationMessage( Application.EXPLICIT_NORMAL );

        then: "correct notification message appears"
        TestUtils.saveScreenshot( getSession(), "app_from_market" );
        notificationMessage == String.format( Application.APP_INSTALLED_MESSAGE, "Content Viewer App" );

        and: "new application listed in the browse panel"
        applicationBrowsePanel.exists( "contentviewer" );

    }
}
