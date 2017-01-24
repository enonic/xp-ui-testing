package com.enonic.wem.uitest.application

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.modules.InstallAppDialog
import spock.lang.Ignore
import spock.lang.Shared

//Ignored due to https://youtrack.enonic.net/issue/XP-4912
@Ignore
class InstallApplication_TypingURL_Spec
    extends BaseApplicationSpec
{
    @Shared
    String WRONG_APP_URL = "http://test.com";

    @Shared
    String WRONG_PROTOCOL_URL = "file:c:/";

    @Shared
    String WARNING = "Failed to process application from %s"

    def "GIVEN 'install app' dialog opened AND 'Upload' tab activated WHEN wrong URL to an application typed THEN correct validation message appears in the dialog"()
    {
        given: "'install app' dialog opened AND 'Upload' tab activated"
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();


        when: "wrong URL to an application typed"
        appDialog.typeInApplicationInput( WRONG_APP_URL );
        String validationMessage = appDialog.waitValidationViewerText( Application.EXPLICIT_NORMAL );
        saveScreenshot( "wrong_app_url" )

        then: "correct validation message appears in the dialog"
        validationMessage == String.format( WARNING, WRONG_APP_URL );
    }

    def "GIVEN 'install app' dialog opened AND 'Upload' tab activated WHEN incorrect protocol typed THEN validation message not displayed"()
    {
        given: "'install app' dialog opened AND 'Upload' tab activated"
        applicationBrowsePanel.clickOnToolbarInstall();
        InstallAppDialog appDialog = new InstallAppDialog( getSession() );
        appDialog.waitUntilDialogLoaded();

        when: "wrong protocol typed"
        appDialog.typeInApplicationInput( WRONG_PROTOCOL_URL );
        saveScreenshot( "wrong_protocol_url" );

        then: "validation message not displayed"
        !appDialog.waitUntilValidationViewerAppears( Application.EXPLICIT_NORMAL );
    }
}
