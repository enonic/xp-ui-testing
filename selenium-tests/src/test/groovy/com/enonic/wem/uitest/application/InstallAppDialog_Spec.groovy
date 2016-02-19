package com.enonic.wem.uitest.application

import com.enonic.autotests.pages.modules.InstallAppDialog
import com.enonic.autotests.utils.TestUtils

class InstallAppDialog_Spec
    extends BaseApplicationSpec
{

    def "GIVEN application BrowsePanel WHEN install button on toolbar pressed THEN 'Install App Dialog' appears with correct control elements"()
    {
        when:
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

        and: "Enonic Market tab is not activated "
        !appDialog.isEnonicMarketTabActivated();

        and: "Upload tab is activated by default "
        appDialog.isUploadTabActivated();

        and: "Application Input displayed by default"
        appDialog.isApplicationInputDisplayed();

        and: "'cancel' button present on the dialog"
        appDialog.isCancelButtonDisplayed();

    }

    def "GIVEN 'Install App Dialog' opened WHEN 'Enonic Market' tab clicked THEN this tab activated"()
    {
        given:
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();

        when:
        appDialog.clickOnEnonicMarketTab();
        TestUtils.saveScreenshot( getSession(), "enonic-market-activated" );

        then: "'Upload' tab is not activated"
        !appDialog.isUploadTabActivated();

        and: "Enonic Market tab is activated "
        appDialog.isEnonicMarketTabActivated();
    }

    def "GIVEN 'Install App Dialog' opened WHEN 'cancel' button clicked THEN dialog not present"()
    {
        given:
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();

        when: "'cancel' button clicked"
        appDialog.clickOnCancelButton();
        appDialog.waitUntilDialogClosed();
        TestUtils.saveScreenshot( getSession(), "close-install-dialog" );

        then: "dialog not present"
        !appDialog.isDisplayed();
    }

    def "GIVEN 'Install App Dialog' opened WHEN 'Enonic Market' link clicked THEN table with applications appears"()
    {
        given:
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();

        when: "'Enonic Market' link clicked"
        appDialog.clickOnEnonicMarketTab();
        TestUtils.saveScreenshot( getSession(), "enonic-market" );

        then: "table with applications appears"
        appDialog.isEnonicMarketTableDisplayed();
    }
}
