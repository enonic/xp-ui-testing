package com.enonic.wem.uitest.application

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.modules.InstallAppDialog
import spock.lang.Shared

class InstallApplication_TypingURL_Spec
    extends BaseApplicationSpec
{
    @Shared
    String WRONG_APP_URL = "http://test.com";

    @Shared
    String WRONG_PROTOCOL_URL = "file:c:/";

    @Shared
    String WARNING = "Failed to process application from %s"

    def "GIVEN 'install app' dialog is opened  WHEN wrong URL for the application was typed THEN correct validation message should appear on the dialog"()
    {
        given: "'install app' dialog is "
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();

        when: "wrong URL to an application was typed"
        appDialog.typeInApplicationInput( WRONG_APP_URL );
        saveScreenshot( "install_dialog_validation_message" );
        String validationMessage = appDialog.getValidationMessage( Application.EXPLICIT_NORMAL );
        saveScreenshot( "wrong_app_url" )

        then: "correct validation message should appear on the dialog"
        validationMessage == String.format( WARNING, WRONG_APP_URL );
    }

    def "GIVEN 'install app' dialog is opened WHEN incorrect protocol was typed THEN validation message should not be displayed"()
    {
        given: "'install app' dialog is opened"
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();

        when: "wrong protocol was typed"
        appDialog.typeInApplicationInput( WRONG_PROTOCOL_URL );
        saveScreenshot( "wrong_protocol_url" );

        then: "validation message should not be displayed"
        !appDialog.waitUntilValidationMessageAppears( Application.EXPLICIT_NORMAL );
    }
}
