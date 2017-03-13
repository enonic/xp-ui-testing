package com.enonic.wem.uitest.application

import com.enonic.autotests.pages.modules.InstallAppDialog

class InstallAppDialog_Spec
    extends BaseApplicationSpec
{

    def "GIVEN application BrowsePanel WHEN install button on toolbar was pressed THEN 'Install App Dialog' should appear with correct control elements"()
    {
        when:
        saveScreenshot( "test_toolbar_is_available5" );
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();

        then: "'Install App' Dialog should appear"
        appDialog.isDisplayed();

        and: "correct title should be displayed on the dialog"
        appDialog.getHeader() == InstallAppDialog.HEADER;

        and: "'enonic market' panel should be present"
        appDialog.isEnonicMarketPanelPresent();

        and: "'Upload' button should be displayed"
        appDialog.isAppUploaderButtonPresent();

        and: "Application Input should be displayed"
        appDialog.isApplicationInputDisplayed();

        and: "'cancel' button should be present on the dialog"
        appDialog.isCancelButtonDisplayed();
    }

    def "GIVEN 'Install App Dialog' is opened WHEN 'cancel' button was clicked THEN dialog should not be displayed"()
    {
        given: "'Install App Dialog' is opened "
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();

        when: "'cancel' button was clicked"
        appDialog.clickOnCancelButton();
        appDialog.waitUntilDialogClosed();
        saveScreenshot( "cancel-install-dialog" );

        then: "dialog should not be displayed"
        !appDialog.isDisplayed();
    }

    def "GIVEN 'Install App Dialog' is opened WHEN the name of an application has been typed THEN only one application should be displayed"()
    {
        given: "'Install App Dialog' is opened "
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog installAppDialog = new InstallAppDialog( getSession() );
        installAppDialog.waitUntilDialogLoaded();

        when: "the name of an application has been typed "
        installAppDialog.typeInApplicationInput( CONTENT_VIEWER_APP_DISPLAY_NAME );

        then: "only one application should be displayed"
        installAppDialog.countDisplayedApplications() == 1
    }
}
