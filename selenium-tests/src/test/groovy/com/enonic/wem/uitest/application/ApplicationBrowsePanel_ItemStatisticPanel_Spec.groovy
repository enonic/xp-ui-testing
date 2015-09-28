package com.enonic.wem.uitest.application

import com.enonic.autotests.pages.modules.ApplicationBrowsePanel
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
    String PAGE_NAME = "main"

    @Shared
    String PART_NAME = "cities-list"

    @Shared
    String LAYOUT_NAME = "centered";

    @Shared
    String CHECKBOX_CONTENT_TYPE_NAME = "checkbox";

    @Shared
    String RELATIONSHIP_TYPE_NAME = "citation";

    def "WHEN One application is selected THEN The details panel should show Build date, version, key and System requirement"()
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
        applicationItemStatisticsPanel.getVersion() == "2.0.0.SNAPSHOT";
        and: "the key as a correct value"
        applicationItemStatisticsPanel.getKey() == FIRST_APP_KEY;
        and: "the system required has a correct value"
        applicationItemStatisticsPanel.getSystemRequired() == SYSTEM_REQUIRED;
    }


    def "WHEN One application is installed and stopped THEN detail page should not show any content types"()
    {
        setup:
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME ).<ApplicationBrowsePanel> clickOnToolbarStop();
        when: "application stopped"
        applicationBrowsePanel.getApplicationStatus( FIRST_APP_NAME ) == "Stopped"
        then: "Content Types headed present"
        applicationItemStatisticsPanel.isContentTypesHeaderPresent();
        and: "page should not show any content types"
        applicationItemStatisticsPanel.getContentTypes().size() == 0;
    }

    def "GIVEN application selected in the BrowsePanel WHEN One module is installed and stopped THEN detail page should not show any mixins"()
    {
        given:
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );

        when: "application stopped"
        applicationBrowsePanel.getApplicationStatus( FIRST_APP_NAME ) == "Stopped";

        then: "Mixin header present"
        applicationItemStatisticsPanel.isMixinsHeaderPresent();
        and: "detail page should not show any mixins"
        applicationItemStatisticsPanel.getMixins().size() == 0;
    }

    def "WHEN One module is installed and stopped THEN detail page should not show any relationship types"()
    {
        given:
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );

        when: "application stopped"
        applicationBrowsePanel.getApplicationStatus( FIRST_APP_NAME ) == "Stopped";

        then: "RelationshipTypes header present"
        applicationItemStatisticsPanel.isRelationShipTypesHeaderPresent();
        and: "detail page should not show any relationship types"
        applicationItemStatisticsPanel.getRelationShipTypes().size() == 0;
    }

    def " WHEN One module is installed and stopped THEN detail page should not show any pages"()
    {
        given:
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );

        when: "application stopped"
        applicationBrowsePanel.getApplicationStatus( FIRST_APP_NAME ) == "Stopped";

        then: "Page header present"
        applicationItemStatisticsPanel.isPageHeaderPresent();
        and: "detail page should not show any pages"
        applicationItemStatisticsPanel.getPages().size() == 0;
    }

    def " WHEN One module is installed and stopped THEN detail page should not show any parts"()
    {
        given:
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );

        when: "application stopped"
        applicationBrowsePanel.getApplicationStatus( FIRST_APP_NAME ) == "Stopped";

        then: "Part header present"
        applicationItemStatisticsPanel.isPartHeaderPresent();
        and: "detail page should not show any parts"
        applicationItemStatisticsPanel.getParts().size() == 0;
    }

    def "WHEN One module is installed and stopped THEN detail page should not show any layout"()
    {
        given:
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );

        when: "application stopped"
        applicationBrowsePanel.getApplicationStatus( FIRST_APP_NAME ) == "Stopped";

        then: "Layout header present"
        applicationItemStatisticsPanel.isLayoutHeaderPresent();
        and: "detail page should not show any layout"
        applicationItemStatisticsPanel.getLayouts().size() == 0;
    }

    def "GIVEN One application is stopped WHEN app started again  THEN detail page should show all its content types"()
    {
        when:
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );
        applicationBrowsePanel.clickOnToolbarStart();

        then:
        TestUtils.saveScreenshot( getSession(), "stopped_started_ct" )
        applicationItemStatisticsPanel.getContentTypes().size() > 0;
    }

    def "GIVEN One application is stopped WHEN app started again  THEN detail page should show all its mixins"()
    {
        given:
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );

        when:
        applicationBrowsePanel.getApplicationStatus( FIRST_APP_NAME ) == "started";

        then:
        TestUtils.saveScreenshot( getSession(), "stopped_started_mixins" )
        List<String> mixins = applicationItemStatisticsPanel.getMixins();
        mixins.size() > 0;
        and:
        mixins.contains( MIXIN_ADDRESS_NAME );
    }

    def "GIVEN One application is stopped WHEN app started again  THEN detail page should show all its relationship types"()
    {
        given:
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );

        when:
        applicationBrowsePanel.getApplicationStatus( FIRST_APP_NAME ) == "started";
        TestUtils.saveScreenshot( getSession(), "stopped_started_relationships" );

        then:
        List<String> relationshipsTypes = applicationItemStatisticsPanel.getRelationShipTypes();
        relationshipsTypes.size() > 0;
        and:
        relationshipsTypes.contains( RELATIONSHIP_TYPE_NAME );
    }

    def "GIVEN One application is stopped WHEN app started again  THEN detail page should show all its parts"()
    {
        given:
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );

        when:
        applicationBrowsePanel.getApplicationStatus( FIRST_APP_NAME ) == "started";

        then:
        List<String> parts = applicationItemStatisticsPanel.getParts();
        parts.size() > 0;
        and:
        parts.contains( PART_NAME );
    }

    def "GIVEN One application is stopped WHEN app started again  THEN detail page should show all its layouts"()
    {
        given:
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );

        when:
        applicationBrowsePanel.getApplicationStatus( FIRST_APP_NAME ) == "started";

        then:
        List<String> layouts = applicationItemStatisticsPanel.getLayouts();
        layouts.size() > 0;
        and:
        layouts.contains( LAYOUT_NAME );
    }
}
