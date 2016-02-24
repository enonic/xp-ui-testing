package com.enonic.wem.uitest.application

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.modules.InstallAppDialog
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


    def "GIVEN 'install app' dialog opened WHEN path to jar specified THEN new application successfully installed"()
    {
        given:
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();


        when: "path to jar specified"
        appDialog.duUploadApplication( LOCAL_PATH_TO_FILE );
        String notificationMessage = applicationBrowsePanel.waitNotificationMessage( Application.EXPLICIT_NORMAL );

        then: "new application listed in the grid"
        applicationBrowsePanel.exists( APP_NAME, true );

        and: "actual notification message and expected are identical"
        notificationMessage == String.format( Application.APP_INSTALLED, APP_DISPLAY_NAME )
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

        then: "application not listed"
        !applicationBrowsePanel.exists( APP_NAME, true );

        and: "correct notification appears"
        message == String.format( Application.APP_UNINSTALLED, APP_DISPLAY_NAME );
    }

}
