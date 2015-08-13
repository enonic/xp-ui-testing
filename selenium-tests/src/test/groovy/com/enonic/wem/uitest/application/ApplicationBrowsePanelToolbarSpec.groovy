package com.enonic.wem.uitest.application

import spock.lang.Stepwise

@Stepwise
class ApplicationBrowsePanelToolbarSpec
    extends BaseApplicationSpec
{

    def "GIVEN Module BrowsePanel WHEN no selected module THEN Start button should be disabled"()
    {
        expect:
        !applicationBrowsePanel.isStartButtonEnabled();
    }

    def "GIVEN application BrowsePanel WHEN no selected application THEN 'Stop' button should be disabled"()
    {
        expect:
        !applicationBrowsePanel.isStopButtonEnabled();
    }


    def "GIVEN a started application WHEN one selected started application THEN 'Stop' button should be enabled AND 'Start' button is disabled"()
    {
        when: " one application selected in the table"
        applicationBrowsePanel.clickCheckboxAndSelectRow( 1 );

        then: "Stop button becomes enabled"
        applicationBrowsePanel.isStopButtonEnabled();

        and: "'Start' button is disabled"
        !applicationBrowsePanel.isStartButtonEnabled()
    }

    def "GIVEN a started application WHEN  module selected in the grid and stopped THEN Stop button should be disabled AND 'Start' button should be enabled"()
    {
        given: " one application selected in the table"
        applicationBrowsePanel.clickCheckboxAndSelectRow( 1 );

        when:
        applicationBrowsePanel.clickOnToolbarStop();

        then: "Stop button becomes disabled"
        !applicationBrowsePanel.isStopButtonEnabled();

        and: "'Start' button is enabled now"
        applicationBrowsePanel.isStartButtonEnabled()
    }

    def "GIVEN one stopped application WHEN started and stopped are selected THEN 'Stop' and 'Start' buttons should be enabled"()
    {
        when: " one application selected in the table"
        applicationBrowsePanel.clickCheckboxAndSelectRow( 1 );
        applicationBrowsePanel.clickCheckboxAndSelectRow( 2 );

        then: "Stop button is enabled"
        applicationBrowsePanel.isStopButtonEnabled();

        and: "'Start' button is disabled"
        applicationBrowsePanel.isStartButtonEnabled()
    }

}