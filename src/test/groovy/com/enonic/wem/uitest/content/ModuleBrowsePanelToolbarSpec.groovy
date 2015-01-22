package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.modules.ModuleBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class ModuleBrowsePanelToolbarSpec
    extends BaseGebSpec
{
    @Shared
    ModuleBrowsePanel moduleBrowsePanel;

    @Shared
    String XEON_MODULE_NAME = "com.enonic.wem.modules.xeon";


    def setup()
    {
        go "admin"
        moduleBrowsePanel = NavigatorHelper.openModules( getTestSession() );
    }

    def "GIVEN Module BrowsePanel WHEN no selected module THEN Uninstall button should be disabled"()
    {
        expect:
        !moduleBrowsePanel.isUninstallButtonEnabled();
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

    def "GIVEN Module BrowsePanel WHEN no selected module THEN Install button should be enabled"()
    {
        expect:
        moduleBrowsePanel.isInstallButtonEnabled();
    }

    def "GIVEN Module BrowsePanel WHEN no selected module THEN Update button should be disabled"()
    {
        expect:
        !moduleBrowsePanel.isUpdateButtonEnabled();
    }

    def "GIVEN Module BrowsePanel WHEN one selected started module THEN Stop button should be disabled"()
    {
        when: " one module selected in the table"
        moduleBrowsePanel.clickAndSelectRow( XEON_MODULE_NAME );

        then: "Stop button becomes enabled"
        moduleBrowsePanel.isStopButtonEnabled();
    }

    def "GIVEN Module BrowsePanel WHEN one selected started module THEN Update button should be enabled"()
    {
        when: " one module selected in the table"
        moduleBrowsePanel.clickAndSelectRow( XEON_MODULE_NAME );

        then: "Update button becomes enabled"
        moduleBrowsePanel.isUpdateButtonEnabled();
    }

}