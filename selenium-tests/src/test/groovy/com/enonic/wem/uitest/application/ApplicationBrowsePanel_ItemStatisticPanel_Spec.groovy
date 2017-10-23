package com.enonic.wem.uitest.application

import spock.lang.Shared
import spock.lang.Stepwise

/*
 Tasks:
  XP-3944 Add selenium test for verifying of "XP-3932 Show idproviders in application panel"

  */
@Stepwise
class ApplicationBrowsePanel_ItemStatisticPanel_Spec
    extends BaseApplicationSpec
{
    @Shared
    String MIXIN_ADDRESS_NAME = "address";

    @Shared
    String PART_NAME = "cities-list"

    @Shared
    String LAYOUT_NAME = "centered";

    @Shared
    String RELATIONSHIP_TYPE_NAME = "citation";

    @Shared
    String VERSION_OF_TEST_APPLICATION = "2.0.0";

    def "set up: start all applications"()
    {
        when:
        applicationBrowsePanel.doSelectAll();
        saveScreenshot( "test_select_all_app" );

        then: "start button is enabled"
        applicationBrowsePanel.isStartButtonEnabled();

        and: "start all"
        applicationBrowsePanel.clickOnToolbarStart();

    }

    def "WHEN existing application is selected THEN 'Build'-date, version, key and 'System requirements' should be present on the statistic panel"()
    {
        when: " existing application is selected"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );
        saveScreenshot( "test_app_stopped" );

        then: "'build date' should be shown"
        applicationItemStatisticsPanel.isBuildDatePresent();

        and: "application's version should be shown"
        applicationItemStatisticsPanel.isVersionPresent();

        and: "application's key should be shown"
        applicationItemStatisticsPanel.isKeyPresent();

        and: "'system required' should be shown"
        applicationItemStatisticsPanel.isSystemRequiredPresent();
    }

    def "WHEN existing application is selected THEN correct values for Build date, version, key and System requirement should be displayed on the statistic panel"()
    {
        when: "application started and it selected in browse panel"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );
        saveScreenshot( "id_provider" );

        then: "build date has a correct value"
        applicationItemStatisticsPanel.getBuildDate() == "TBA";

        and: "version has a correct value"
        applicationItemStatisticsPanel.getVersion() == VERSION_OF_TEST_APPLICATION;

        and: "application's key has a correct value"
        applicationItemStatisticsPanel.getKey() == FIRST_APP_KEY;

        and: "'system required' has a correct value"
        applicationItemStatisticsPanel.getSystemRequired() == SYSTEM_REQUIRED;

        and: "'providers data group' should be displayed"
        applicationItemStatisticsPanel.isProvidersDataGroupDisplayed();

        and: "'Provider's key' should be displayed"
        applicationItemStatisticsPanel.isProvidersDataKeyDisplayed();
    }

    def "GIVEN existing started application is selected WHEN the application has been stopped THEN content types should not be displayed on the statistic panel"()
    {
        given:"existing started application is selected"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );

        when: "application was stopped"
        applicationBrowsePanel.clickOnToolbarStop();
        saveScreenshot( "test_app_stopped" );

        then: "application's  status should be 'stopped'"
        applicationBrowsePanel.findAppByDisplayNameAndGetStatus( FIRST_APP_DISPLAY_NAME ) == STOPPED_STATE;

        and: "'Content Types' - header should not be displayed"
        !applicationItemStatisticsPanel.isContentTypesHeaderPresent();

        and: "content types should not be present on the panel"
        applicationItemStatisticsPanel.getContentTypes().size() == 0;

        and: "'Info data group' should be displayed"
        applicationItemStatisticsPanel.isInfoDataGroupDisplayed();

        and: "correct 'Build date' should be displayed"
        applicationItemStatisticsPanel.getBuildDate() == "TBA";

        and: "'schema data group' should not be displayed"
        !applicationItemStatisticsPanel.isSchemaDataGroupDisplayed();

        and: "'descriptors data group' should not be displayed"
        !applicationItemStatisticsPanel.isDescriptorsDataGroupDisplayed();

        and: "'providers data group' should not be displayed"
        !applicationItemStatisticsPanel.isProvidersDataGroupDisplayed();
    }

    def "WHEN existing stopped application is selected THEN mixins should not be displayed on the statistic panel"()
    {
        when: "stopped application has been selected"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );

        then: "mixins should not be displayed"
        applicationItemStatisticsPanel.getMixins().size() == 0;

        and: "application's  status should be 'stopped'"
        applicationBrowsePanel.findAppByDisplayNameAndGetStatus( FIRST_APP_DISPLAY_NAME ) == STOPPED_STATE;
    }

    def "WHEN existing stopped application is selected THEN 'pages' should not be displayed on the statistic panel"()
    {
        when: "stopped application have been selected"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );

        then: "detail page should not show any pages"
        applicationItemStatisticsPanel.getPages().size() == 0;

        and: "application's  status should be 'stopped'"
        applicationBrowsePanel.findAppByDisplayNameAndGetStatus( FIRST_APP_DISPLAY_NAME ) == STOPPED_STATE;
    }

    def "WHEN existing stopped application is selected THEN 'parts' should not be displayed on the statistic panel"()
    {
        when: "stopped application have been selected"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );

        then: "detail page should not show any parts"
        applicationItemStatisticsPanel.getParts().size() == 0;

        and: "application is stopped"
        applicationBrowsePanel.findAppByDisplayNameAndGetStatus( FIRST_APP_DISPLAY_NAME ) == STOPPED_STATE;
    }

    def "GIVEN existing stopped application WHEN the application was started THEN content types should be displayed on the panel"()
    {
        when: "existing stopped application is selected and 'Start' was pressed"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );
        applicationBrowsePanel.clickOnToolbarStart();

        then: "content types should be displayed on the panel"
        saveScreenshot( "stopped_started_ct" )
        applicationItemStatisticsPanel.getContentTypes().size() > 0;

        and: "'providers data group' should be displayed"
        applicationItemStatisticsPanel.isProvidersDataGroupDisplayed();
    }

    def "GIVEN Application that was stopped and started again WHEN the application have been selected THEN mixins should be displayed on the panel"()
    {
        when: "stopped and started again application have been selected"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );
        saveScreenshot( "stopped_started_mixins" )
        List<String> mixins = applicationItemStatisticsPanel.getMixins();

        then: "detail page should show all its mixins"
        mixins.size() > 0;

        and: "required mixin should be present"
        mixins.contains( MIXIN_ADDRESS_NAME );

        and: "application's status is 'started'"
        applicationBrowsePanel.findAppByDisplayNameAndGetStatus( FIRST_APP_DISPLAY_NAME ) == STARTED_STATE;
    }

    def "GIVEN Application that was stopped and started again WHEN the application selected THEN detail page should show all its relationship types"()
    {
        when: "Application that was stopped and started again"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );
        saveScreenshot( "app_restarted_relationships" );

        then: "detail page should show all its relationship types"
        List<String> relationshipsTypes = applicationItemStatisticsPanel.getRelationShipTypes();
        relationshipsTypes.size() > 0;

        and: "required relationship should be present"
        relationshipsTypes.contains( RELATIONSHIP_TYPE_NAME );

        and: "application's status is 'started'"
        applicationBrowsePanel.findAppByDisplayNameAndGetStatus( FIRST_APP_DISPLAY_NAME ) == STARTED_STATE;
    }

    def "GIVEN Application that was stopped and started again WHEN the application selected  THEN detail page should show all its parts"()
    {
        when: "the application selected"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );

        then: "detail page should show all its parts"
        List<String> parts = applicationItemStatisticsPanel.getParts();
        parts.size() > 0;

        and: "parts contains the required part"
        parts.contains( PART_NAME );

        and: "application's status is 'started'"
        applicationBrowsePanel.findAppByDisplayNameAndGetStatus( FIRST_APP_DISPLAY_NAME ) == STARTED_STATE;
    }

    def "GIVEN Application that was stopped and started again WHEN the application selected  THEN detail page should show all its layouts"()
    {
        when: "the application selected "
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );

        then: "detail page should show all its layouts"
        List<String> layouts = applicationItemStatisticsPanel.getLayouts();
        layouts.size() > 0;

        and: "layouts contains the required layout"
        layouts.contains( LAYOUT_NAME );

        and: "application's status is 'started'"
        applicationBrowsePanel.findAppByDisplayNameAndGetStatus( FIRST_APP_DISPLAY_NAME ) == STARTED_STATE;
    }
}
