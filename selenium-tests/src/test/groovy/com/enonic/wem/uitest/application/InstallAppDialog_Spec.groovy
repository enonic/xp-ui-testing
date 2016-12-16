package com.enonic.wem.uitest.application

import com.enonic.autotests.pages.modules.InstallAppDialog
import com.enonic.autotests.utils.TestUtils

class InstallAppDialog_Spec
    extends BaseApplicationSpec
{

    def "GIVEN application BrowsePanel WHEN install button on toolbar pressed THEN 'Install App Dialog' appears with correct control elements"()
    {
        when:
        saveScreenshot( "test_toolbar_is_available5" );
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();

        then: "'Install App Dialog' appears"
        appDialog.isDisplayed();

        and: "correct title displayed on the dialog"
        appDialog.getHeader() == InstallAppDialog.HEADER;

        and: "'upload' and 'enonic market' tabs are present"
        appDialog.isEnonicMarketTabPresent();

        and:
        appDialog.isUploadTabPresent();

        and: "'Enonic Market' tab is activated by default "
        appDialog.isEnonicMarketTabActivated();

        and: "'Upload' tab is not activated by default "
        !appDialog.isUploadTabActivated();

        and: "Application Input not displayed by default"
        !appDialog.isApplicationInputDisplayed();

        and: "'cancel' button present on the dialog"
        appDialog.isCancelButtonDisplayed();

    }

    def "GIVEN 'Install App Dialog' opened WHEN 'Upload' tab clicked THEN this tab activated"()
    {
        given:
        saveScreenshot( "test_toolbar_is_available3" );
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();

        when:
        appDialog.clickOnUploadTab();
        TestUtils.saveScreenshot( getSession(), "upload-activated" );

        then: "'Upload' tab is activated"
        appDialog.isUploadTabActivated();

        and: "Application Input displayed"
        appDialog.isApplicationInputDisplayed();

        and: "Enonic Market tab is not activated "
        !appDialog.isEnonicMarketTabActivated();
    }

    def "GIVEN 'Install App Dialog' opened WHEN 'cancel' button clicked THEN dialog not present"()
    {
        given:
        saveScreenshot( "test_toolbar_is_available1" );
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();

        when: "'cancel' button clicked"
        appDialog.clickOnCancelButton();
        appDialog.waitUntilDialogClosed();
        saveScreenshot( "close-install-dialog" );

        then: "dialog not present"
        !appDialog.isDisplayed();
    }

    def "WHEN 'Install App Dialog' opened  THEN table with applications appears"()
    {
        when:
        saveScreenshot( "test_toolbar_is_available2" );
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();
        saveScreenshot( "enonic-market" );

        then: "table with applications appears"
        appDialog.isEnonicMarketTableDisplayed();
    }
}
