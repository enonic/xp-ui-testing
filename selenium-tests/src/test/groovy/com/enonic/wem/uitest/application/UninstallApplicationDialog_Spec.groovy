package com.enonic.wem.uitest.application

import com.enonic.autotests.pages.modules.UninstallApplicationDialog
import com.enonic.autotests.utils.TestUtils
import spock.lang.Stepwise

@Stepwise
class UninstallApplicationDialog_Spec
    extends BaseApplicationSpec
{
    def "GIVEN installed from 'Enonic Market' application WHEN app was selected and 'uninstall' button pressed THEN 'UnInstallDialog' should be displayed"()
    {
        given: "installed from 'Enonic Market' application"
        installAppAndCloseDialog( GOOGLE_ANALYTICS_APP_NAME, GOOGLE_ANALYTICS_DISPLAY_NAME );

        when: "app was selected and 'uninstall' button pressed"
        applicationBrowsePanel.selectRowByName( GOOGLE_ANALYTICS_APP_NAME );
        UninstallApplicationDialog dialog = applicationBrowsePanel.clickOnToolbarUninstall();
        dialog.waitUntilDialogLoaded();

        then: "dialog is displayed"
        dialog.isDisplayed();

        and: "correct header should be displayed"
        dialog.getHeader() == UninstallApplicationDialog.HEADER_TEXT;

        and:
        dialog.isYesButtonDisplayed();

        and: "button 'No' should be displayed"
        dialog.isNoButtonDisplayed()

        and: "button 'Cancel' should be displayed"
        dialog.isCancelButtonDisplayed();

        and: "correct warning should be present on the dialog"
        dialog.getContentString() == UninstallApplicationDialog.CONTENT_TEXT;
    }

    def "GIVEN 'UnInstallDialog' is opened WHEN 'cancel' button pressed THEN 'UnInstallDialog' not displayed"()
    {
        given: "'UnInstallDialog' is opened"
        applicationBrowsePanel.selectRowByName( GOOGLE_ANALYTICS_APP_NAME );
        UninstallApplicationDialog dialog = applicationBrowsePanel.clickOnToolbarUninstall();
        dialog.waitUntilDialogLoaded();

        when: "Cancel button has been pressed"
        dialog.clickOnCancelButton();

        then: "dialog should be closed"
        !dialog.isDisplayed()
    }

    def "GIVEN 'UnInstallDialog' is opened WHEN 'No' button pressed THEN 'UnInstallDialog' not displayed AND the application should not be uninstalled"()
    {
        given: "'UnInstallDialog' is opened"
        applicationBrowsePanel.selectRowByName( GOOGLE_ANALYTICS_APP_NAME );
        UninstallApplicationDialog dialog = applicationBrowsePanel.clickOnToolbarUninstall();
        dialog.waitUntilDialogLoaded();

        when: "'No' button has been pressed"
        dialog.clickOnNoButton();

        then: "dialog should be closed"
        !dialog.isDisplayed();

        and: "the application should not be uninstalled"
        applicationBrowsePanel.exists( GOOGLE_ANALYTICS_APP_NAME );
    }

    def "GIVEN 'UnInstallDialog' is opened WHEN 'Yes' button pressed THEN 'UnInstallDialog' not displayed AND the application should be uninstalled"()
    {
        given: "'UnInstallDialog' is opened"
        applicationBrowsePanel.selectRowByName( GOOGLE_ANALYTICS_APP_NAME );
        UninstallApplicationDialog dialog = applicationBrowsePanel.clickOnToolbarUninstall();
        dialog.waitUntilDialogLoaded();

        when: "'Yes' button has been pressed"
        dialog.clickOnYesButton();

        then: "dialog should be closed"
        !dialog.isDisplayed();

        and: "the application should be uninstalled"
        !applicationBrowsePanel.exists( GOOGLE_ANALYTICS_APP_NAME );
    }
}
