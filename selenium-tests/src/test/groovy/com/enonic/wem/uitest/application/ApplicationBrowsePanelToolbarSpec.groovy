package com.enonic.wem.uitest.application

import com.enonic.autotests.services.NavigatorHelper
import spock.lang.Stepwise

@Stepwise
class ApplicationBrowsePanelToolbarSpec
    extends BaseModuleSpec
{

    def setup()
    {
        go "admin"
        moduleBrowsePanel = NavigatorHelper.openApplications( getTestSession() );
    }

    def "GIVEN Module BrowsePanel WHEN no selected module THEN Start button should be disabled"()
    {
        expect:
        !moduleBrowsePanel.isStartButtonEnabled();
    }

    def "GIVEN application BrowsePanel WHEN no selected application THEN 'Stop' button should be disabled"()
    {
        expect:
        !moduleBrowsePanel.isStopButtonEnabled();
    }


    def "GIVEN a started application WHEN one selected started application THEN 'Stop' button should be enabled AND 'Start' button is disabled"()
    {
        when: " one application selected in the table"
        moduleBrowsePanel.clickCheckboxAndSelectRow( 1 );

        then: "Stop button becomes enabled"
        moduleBrowsePanel.isStopButtonEnabled();

        and: "'Start' button is disabled"
        !moduleBrowsePanel.isStartButtonEnabled()
    }

    def "GIVEN a started application WHEN  module selected in the grid and stopped THEN Stop button should be disabled AND 'Start' button should be enabled"()
    {
        given: " one application selected in the table"
        moduleBrowsePanel.clickCheckboxAndSelectRow( 1 );

        when:
        moduleBrowsePanel.clickOnToolbarStop();

        then: "Stop button becomes disabled"
        !moduleBrowsePanel.isStopButtonEnabled();

        and: "'Start' button is enabled now"
        moduleBrowsePanel.isStartButtonEnabled()
    }

    def "GIVEN one stopped application WHEN started and stopped are selected THEN 'Stop' and 'Start' buttons should be enabled"()
    {
        when: " one application selected in the table"
        moduleBrowsePanel.clickCheckboxAndSelectRow( 1 );
        moduleBrowsePanel.clickCheckboxAndSelectRow( 2 );

        then: "Stop button is enabled"
        moduleBrowsePanel.isStopButtonEnabled();

        and: "'Start' button is disabled"
        moduleBrowsePanel.isStartButtonEnabled()
    }

}