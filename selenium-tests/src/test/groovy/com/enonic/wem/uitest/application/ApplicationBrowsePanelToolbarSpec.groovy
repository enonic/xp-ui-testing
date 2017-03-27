package com.enonic.wem.uitest.application

import spock.lang.Stepwise

@Stepwise
class ApplicationBrowsePanelToolbarSpec
    extends BaseApplicationSpec
{

    def "GIVEN Applications BrowsePanel WHEN no selected module THEN 'Uninstall' button should be disabled AND 'Install' button should be enabled"()
    {
        expect:
        saveScreenshot( "test_app_toolbar1" );
        !applicationBrowsePanel.isUninstallButtonEnabled();

        and: "'Install' button should be enabled"
        applicationBrowsePanel.isInstallButtonEnabled();

        and: "Stop button should be disabled"
        !applicationBrowsePanel.isStopButtonEnabled();

        and: "Start button should be disabled"
        !applicationBrowsePanel.isStartButtonEnabled();
    }

    def "WHEN one local application is selected  THEN 'Uninstall' button should be disabled"()
    {
        when: "one local application is selected"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FOURTH_APP_DISPLAY_NAME );
        saveScreenshot( "uninstall-local-disabled" )

        then: "'Uninstall' button is disabled"
        !applicationBrowsePanel.isUninstallButtonEnabled();
    }

    def "GIVEN a started application WHEN one started application selected  THEN 'Stop' button should be enabled AND 'Start' button is disabled"()
    {
        when: " one application selected in the table"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FOURTH_APP_DISPLAY_NAME );
        saveScreenshot( "test_app_toolbar2" );

        then: "app status is started"
        applicationBrowsePanel.findAppByDisplayNameAndGetStatus( FOURTH_APP_DISPLAY_NAME ) == STARTED_STATE;

        and: "Stop button is enabled"
        applicationBrowsePanel.isStopButtonEnabled();

        and: "'Start' button is disabled"
        !applicationBrowsePanel.isStartButtonEnabled()
    }

    def "GIVEN existing started application WHEN application selected in the grid and stopped THEN Stop button should be disabled AND 'Start' button should be enabled"()
    {
        given: "existing started application selected in the table"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FOURTH_APP_DISPLAY_NAME );
        saveScreenshot( "test_app_toolbar3" );

        when: "stop button on the toolbar pressed"
        applicationBrowsePanel.clickOnToolbarStop();

        then: "Stop button becomes disabled"
        !applicationBrowsePanel.isStopButtonEnabled();

        and: "'Start' button is enabled now"
        applicationBrowsePanel.isStartButtonEnabled()
    }

    def "GIVEN one stopped application WHEN one started and one stopped applications are selected THEN 'Stop' and 'Start' buttons should be enabled"()
    {
        when: "one started and one stopped applications are selected"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FOURTH_APP_DISPLAY_NAME );
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( SECOND_APP_DISPLAY_NAME );
        saveScreenshot( "two_apps_selected_toolbar" );

        then: "'Stop' button should be enabled"
        applicationBrowsePanel.isStopButtonEnabled();

        and: "'Start' button should be enabled"
        applicationBrowsePanel.isStartButtonEnabled()
    }

    def "GIVEN two started applications are selected WHEN 'stop' button on toolbar pressed THEN both applications are 'stopped'"()
    {
        given: "started and stopped are selected"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( SECOND_APP_DISPLAY_NAME );

        when: "Stop button pressed"
        saveScreenshot( "multiple_select_stop_before" );
        applicationBrowsePanel.clickOnToolbarStop();
        saveScreenshot( "multiple_select_stop_after" )

        then: "both applications are 'stopped'"
        applicationBrowsePanel.findAppByDisplayNameAndGetStatus( FIRST_APP_DISPLAY_NAME ) == STOPPED_STATE;
        and:
        applicationBrowsePanel.findAppByDisplayNameAndGetStatus( SECOND_APP_DISPLAY_NAME ) == STOPPED_STATE;
    }

    def "GIVEN two started applications are selected WHEN 'stop' button on toolbar pressed THEN 'Stop' button is disabled and 'Start' button is enabled"()
    {
        when: " started and stopped are selected"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( SECOND_APP_DISPLAY_NAME );
        saveScreenshot( "test_app_toolbar_7" );

        then: "'Stop' button should be disabled"
        !applicationBrowsePanel.isStopButtonEnabled();

        and: "'Start' button should be enabled"
        applicationBrowsePanel.isStartButtonEnabled()
    }
}