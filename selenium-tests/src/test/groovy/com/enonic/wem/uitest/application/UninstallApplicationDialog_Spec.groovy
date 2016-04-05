package com.enonic.wem.uitest.application

import com.enonic.autotests.pages.modules.UninstallApplicationDialog
import spock.lang.Stepwise

@Stepwise
class UninstallApplicationDialog_Spec
    extends BaseApplicationSpec
{
    def "GIVEN installed from 'Enonic Market' application WHEN app selected and 'uninstall' button pressed THEN 'UnInstallDialog' appears"()
    {
        given: "installed from 'Enonic Market' application"
        installAppAndCloseDialog( GOOGLE_ANALYTICS_APP_NAME, GOOGLE_ANALYTICS_DISPLAY_NAME );

        when: "app selected and 'uninstall' button pressed"
        applicationBrowsePanel.selectRowByName( GOOGLE_ANALYTICS_APP_NAME );
        UninstallApplicationDialog dialog = applicationBrowsePanel.clickOnToolbarUninstall();
        dialog.waitUntilDialogLoaded();

        then: "dialog displayed"
        dialog.isDisplayed();

        and: "correct header displayed"
        dialog.getHeader() == UninstallApplicationDialog.HEADER_TEXT;

        and:
        dialog.isYesButtonDisplayed();

        and: "button 'No' displayed"
        dialog.isNoButtonDisplayed()

        and: "button 'Cancel' displayed"
        dialog.isCancelButtonDisplayed();

        and: "correct message present in the dialog"
        dialog.getContentString() == UninstallApplicationDialog.CONTENT_TEXT;
    }

    def "GIVEN 'UnInstallDialog' is opened WHEN 'cancel' button pressed THEN 'UnInstallDialog' not displayed"()
    {
        given: "'UnInstallDialog' is opened"
        applicationBrowsePanel.selectRowByName( GOOGLE_ANALYTICS_APP_NAME );
        UninstallApplicationDialog dialog = applicationBrowsePanel.clickOnToolbarUninstall();
        dialog.waitUntilDialogLoaded();

        when: "app selected and 'uninstall' button pressed"
        dialog.clickOnCancelButton();

        then: "dialog not displayed"
        !dialog.isDisplayed()
    }

    def "GIVEN 'UnInstallDialog' is opened WHEN 'No' button pressed THEN 'UnInstallDialog' not displayed AND the application listed in the grid"()
    {
        given: "'UnInstallDialog' is opened"
        applicationBrowsePanel.selectRowByName( GOOGLE_ANALYTICS_APP_NAME );
        UninstallApplicationDialog dialog = applicationBrowsePanel.clickOnToolbarUninstall();
        dialog.waitUntilDialogLoaded();

        when: "'No' button pressed"
        dialog.clickOnNoButton();

        then: "dialog not displayed"
        !dialog.isDisplayed();

        and: "the application listed in the grid"
        applicationBrowsePanel.exists( GOOGLE_ANALYTICS_APP_NAME );
    }

    def "GIVEN 'UnInstallDialog' is opened WHEN 'Yes' button pressed THEN 'UnInstallDialog' not displayed AND the application have been uninstalled"()
    {
        given: "'UnInstallDialog' is opened"
        applicationBrowsePanel.selectRowByName( GOOGLE_ANALYTICS_APP_NAME );
        UninstallApplicationDialog dialog = applicationBrowsePanel.clickOnToolbarUninstall();
        dialog.waitUntilDialogLoaded();

        when: "'Yes' button pressed"
        dialog.clickOnYesButton();

        then: "dialog not displayed"
        !dialog.isDisplayed();

        and: "the application listed in the grid"
        !applicationBrowsePanel.exists( GOOGLE_ANALYTICS_APP_NAME );
    }
}
