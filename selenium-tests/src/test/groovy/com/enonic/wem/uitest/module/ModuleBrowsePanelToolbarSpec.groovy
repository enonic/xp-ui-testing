package com.enonic.wem.uitest.module

import com.enonic.autotests.services.NavigatorHelper

class ModuleBrowsePanelToolbarSpec
    extends BaseModuleSpec
{

    def setup()
    {
        go "admin"
        moduleBrowsePanel = NavigatorHelper.openModules( getTestSession() );
    }

    def "GIVEN Module BrowsePanel WHEN no selected module THEN Start button should be disabled"()
    {
        expect:
        !moduleBrowsePanel.isStartButtonEnabled();
    }

    def "GIVEN Module BrowsePanel WHEN no selected module THEN Stop button should be disabled"()
    {
        expect:
        !moduleBrowsePanel.isStopButtonEnabled();
    }


    def "GIVEN Module BrowsePanel WHEN one selected started module THEN Stop button should be enabled"()
    {
        when: " one module selected in the table"
        moduleBrowsePanel.clickCheckboxAndSelectRow( 1 );

        then: "Stop button becomes enabled"
        moduleBrowsePanel.isStopButtonEnabled();
    }

}