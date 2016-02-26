package com.enonic.wem.uitest.application

import com.enonic.autotests.utils.TestUtils
import spock.lang.Shared
import spock.lang.Stepwise

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
    String VERSION_OF_TEST_APPLICATION = "2.0.0.SNAPSHOT";

    def "WHEN One application is selected THEN The details panel should show 'Build'-date, version, key and 'System requirements'"()
    {
        when: "application started and it selected in browse panel"
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );

        then: "the Build date shown"
        applicationItemStatisticsPanel.isBuildDatePresent();

        and: "the version shown"
        applicationItemStatisticsPanel.isVersionPresent();

        and: "the key shown"
        applicationItemStatisticsPanel.isKeyPresent();

        and: "the system required shown"
        applicationItemStatisticsPanel.isSystemRequiredPresent();
    }

    def "WHEN One application is selected THEN The details panel should show correct values for Build date, version, key and System requirement"()
    {
        when: "application started and it selected in browse panel"
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );

        then: "the Build date as a correct value"
        applicationItemStatisticsPanel.getBuildDate() == "TBA";

        and: "the version as a correct value"
        applicationItemStatisticsPanel.getVersion() == VERSION_OF_TEST_APPLICATION;

        and: "the key as a correct value"
        applicationItemStatisticsPanel.getKey() == FIRST_APP_KEY;

        and: "the system required has a correct value"
        applicationItemStatisticsPanel.getSystemRequired() == SYSTEM_REQUIRED;
    }


    def "GIVEN Application is 'started' WHEN the application have been stopped THEN detail page should not show any content types"()
    {
        given:
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );

        when: "application stopped"
        applicationBrowsePanel.clickOnToolbarStop();
        TestUtils.saveScreenshot( getSession(), "app-stopped" );

        then: "the application has status is stopped"
        applicationBrowsePanel.getApplicationStatus( FIRST_APP_NAME ) == STOPPED_STATE;

        and: "'Content Types' - header present"
        applicationItemStatisticsPanel.isContentTypesHeaderPresent();

        and: "page should not show any content types"
        applicationItemStatisticsPanel.getContentTypes().size() == 0;
    }

    def "WHEN application that just stopped and selected  THEN detail page should not show any mixins, but header for mixins should be displayed"()
    {
        when: "stopped application have been selected"
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );

        then: "Mixin header present"
        applicationItemStatisticsPanel.isMixinsHeaderPresent();

        and: "detail page should not show any mixins"
        applicationItemStatisticsPanel.getMixins().size() == 0;

        and: "application is stopped"
        applicationBrowsePanel.getApplicationStatus( FIRST_APP_NAME ) == STOPPED_STATE;
    }

    def "WHEN application that just stopped and selected THEN detail page should not show any relationship types"()
    {
        when: "stopped application have been selected"
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );

        then: "RelationshipTypes header present"
        applicationItemStatisticsPanel.isRelationShipTypesHeaderPresent();

        and: "detail page should not show any relationship types"
        applicationItemStatisticsPanel.getRelationShipTypes().size() == 0;

        and: "application is stopped"
        applicationBrowsePanel.getApplicationStatus( FIRST_APP_NAME ) == STOPPED_STATE;
    }

    def "WHEN application that just stopped and selected THEN detail page should not show any pages"()
    {
        when: "stopped application have been selected"
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );

        then: "Page header present"
        applicationItemStatisticsPanel.isPageHeaderPresent();

        and: "detail page should not show any pages"
        applicationItemStatisticsPanel.getPages().size() == 0;

        and: "application is stopped"
        applicationBrowsePanel.getApplicationStatus( FIRST_APP_NAME ) == STOPPED_STATE;
    }

    def "WHEN stopped application have been selected THEN detail page should not show any parts"()
    {
        when: "stopped application have been selected"
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );

        then: "Part header present"
        applicationItemStatisticsPanel.isPartHeaderPresent();

        and: "detail page should not show any parts"
        applicationItemStatisticsPanel.getParts().size() == 0;

        and: "application is stopped"
        applicationBrowsePanel.getApplicationStatus( FIRST_APP_NAME ) == STOPPED_STATE;
    }

    def "WHEN stopped application have been selected THEN detail page should not show any layout"()
    {
        when:
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );

        then: "Layout - header present"
        applicationItemStatisticsPanel.isLayoutHeaderPresent();

        and: "detail page should not show any layout"
        applicationItemStatisticsPanel.getLayouts().size() == 0;

        and: "application is stopped"
        applicationBrowsePanel.getApplicationStatus( FIRST_APP_NAME ) == STOPPED_STATE;
    }

    def "GIVEN Application is stopped WHEN the application started again THEN detail page should show all its content types"()
    {
        when: "the application started again"
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );
        applicationBrowsePanel.clickOnToolbarStart();

        then: "detail page should show all its content types"
        TestUtils.saveScreenshot( getSession(), "stopped_started_ct" )
        applicationItemStatisticsPanel.getContentTypes().size() > 0;
    }

    def "GIVEN Application that was stopped and started again WHEN the application have been selected THEN detail page should show all its mixins"()
    {
        when: "stopped and started again application have been selected"
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );
        TestUtils.saveScreenshot( getSession(), "stopped_started_mixins" )
        List<String> mixins = applicationItemStatisticsPanel.getMixins();

        then: "detail page should show all its mixins"
        mixins.size() > 0;

        and: "required mixin is present"
        mixins.contains( MIXIN_ADDRESS_NAME );

        and: "application is started"
        applicationBrowsePanel.getApplicationStatus( FIRST_APP_NAME ) == STARTED_STATE;
    }

    def "GIVEN Application that was stopped and started again WHEN the application selected THEN detail page should show all its relationship types"()
    {
        when: "Application that was stopped and started again"
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );
        TestUtils.saveScreenshot( getSession(), "stopped_started_relationships" );

        then: "detail page should show all its relationship types"
        List<String> relationshipsTypes = applicationItemStatisticsPanel.getRelationShipTypes();
        relationshipsTypes.size() > 0;

        and: "required relationship is present"
        relationshipsTypes.contains( RELATIONSHIP_TYPE_NAME );

        and: "application is started"
        applicationBrowsePanel.getApplicationStatus( FIRST_APP_NAME ) == STARTED_STATE;
    }

    def "GIVEN Application that was stopped and started again WHEN the application selected  THEN detail page should show all its parts"()
    {
        when: "the application selected"
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );

        then: "detail page should show all its parts"
        List<String> parts = applicationItemStatisticsPanel.getParts();
        parts.size() > 0;

        and: "parts contains the required part"
        parts.contains( PART_NAME );

        and: "application is started"
        applicationBrowsePanel.getApplicationStatus( FIRST_APP_NAME ) == STARTED_STATE;
    }

    def "GIVEN Application that was stopped and started again WHEN the application selected  THEN detail page should show all its layouts"()
    {
        when: "the application selected "
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );

        then: "detail page should show all its layouts"
        List<String> layouts = applicationItemStatisticsPanel.getLayouts();
        layouts.size() > 0;

        and: "layouts contains the required layout"
        layouts.contains( LAYOUT_NAME );

        and: "application is started"
        applicationBrowsePanel.getApplicationStatus( FIRST_APP_NAME ) == STARTED_STATE;
    }
}
