package com.enonic.wem.uitest.application

import spock.lang.Stepwise

@Stepwise
class ApplicationBrowsePanel_AppSelection_Spec
    extends BaseApplicationSpec
{

    def "GIVEN existing application is selected WHEN selecting one more THEN last selected application should be displayed on the Selection Panel"()
    {
        given: "existing application is selected"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );

        when: "selected a one more application"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( SECOND_APP_DISPLAY_NAME );
        saveScreenshot( "test_two_app_selected" );

        then: "last selected application should be displayed on the Selection Panel"
        applicationItemStatisticsPanel.getItemStatisticHeader() == "Second Selenium App"
    }

    def "GIVEN 2 existing applications are selected WHEN selecting one more app THEN last selected application should be displayed on the Selection Panel"()
    {
        given: "2 existing applications are selected"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( SECOND_APP_DISPLAY_NAME );

        when: "one more application has been selected"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( THIRD_APP_DISPLAY_NAME );
        saveScreenshot( "test_three_app_selected1" );

        then: "last selected application should be displayed on the Selection Panel"
        applicationItemStatisticsPanel.getItemStatisticHeader() == "Third Selenium App"
    }

    def "GIVEN three application are selected WHEN deselecting one THEN second application should be present on the Statistic Panel"()
    {
        given: "there are three selected application in browse panel"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( SECOND_APP_DISPLAY_NAME );
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( THIRD_APP_DISPLAY_NAME );
        saveScreenshot( "test_three_app_selected2" );

        when: "one application was deselected"
        applicationBrowsePanel.deSelectAppInTable( THIRD_APP_DISPLAY_NAME );
        saveScreenshot( "test_three_app_one_deselected" );

        then: "second application should be present on the Statistic Panel"
        applicationItemStatisticsPanel.getItemStatisticHeader() == "Second Selenium App"
    }
}
