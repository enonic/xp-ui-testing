package com.enonic.wem.uitest.module

import com.enonic.autotests.pages.modules.InstallModuleDialog
import com.enonic.autotests.pages.modules.ModuleBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.TestUtils
import spock.lang.Stepwise

@Stepwise
class InstallAndUninstallModuleSpec
    extends BaseModuleSpec
{

    def setup()
    {
        go "admin"
        moduleBrowsePanel = NavigatorHelper.openModules( getTestSession() );
    }

    def "WHEN 'Install' button on toolbar clicked THEN install module dialog appears"()
    {
        when:
        InstallModuleDialog dialog = moduleBrowsePanel.clickToolbarInstall();

        then:
        dialog.isOpened();
    }

    def "GIVEN install dialog was opened  WHEN  module url typed THEN new module appears in the grid and this module has 'stopped' status"()
    {
        given: "click on 'Install' button on the toolbar, dialog window appears"
        InstallModuleDialog dialog = moduleBrowsePanel.clickToolbarInstall();

        when: "url typed and 'Install' button "
        moduleBrowsePanel = dialog.typeModuleURL( TEST_MODULE_URL ).clickOnInstall();
        TestUtils.saveScreenshot( getSession(), "test_module_install" )


        then: "new module exists in the browse panel and status is stopped "
        moduleBrowsePanel.exists( TEST_MODULE_NAME, true ) && moduleBrowsePanel.getModuleStatus( TEST_MODULE_NAME ).equals( "stopped" );
    }

    def "GIVEN new installed module with status equals 'stopped'  WHEN module selected  THEN 'Start' button enabled and 'Stop' button disabled "()
    {
        when: "select a existing stopped module"
        moduleBrowsePanel.clickAndSelectRow( TEST_MODULE_NAME );

        then: "button 'Start' on the toolbar pressed"
        moduleBrowsePanel.isStartButtonEnabled() && !moduleBrowsePanel.isStopButtonEnabled();
    }

    def "GIVEN new installed module with status equals 'stopped'  WHEN module selected and 'Start' button pressed THEN 'Start' button disabled and 'Stop' button enabled "()
    {
        when: "select a existing stopped module"
        moduleBrowsePanel.clickAndSelectRow( TEST_MODULE_NAME );
        moduleBrowsePanel.clickOnToolbarStart();

        then: "button 'Start' on the toolbar pressed"
        !moduleBrowsePanel.waitAndCheckIsButtonEnabled( ModuleBrowsePanel.START_BUTTON ) && moduleBrowsePanel.isStopButtonEnabled() &&
            moduleBrowsePanel.getModuleStatus( TEST_MODULE_NAME ).equals( "started" );
    }

    def "GIVEN a module with status equals 'started'  WHEN module selected and 'Stop' button pressed THEN module becomes stopped"()
    {
        when: "select a existing started module and click on 'Stop' button on toolbar"
        moduleBrowsePanel.clickAndSelectRow( TEST_MODULE_NAME );
        moduleBrowsePanel.clickOnToolbarStop();

        then:
        moduleBrowsePanel.getModuleStatus( TEST_MODULE_NAME ).equals( "stopped" );
    }

    def "GIVEN a module with status equals 'stopped'  WHEN module selected and 'Uninstall' button pressed THEN module no longer exists in browse panel"()
    {
        when: "select a existing stopped module and click on 'Uninstall' button on toolbar"
        moduleBrowsePanel.clickAndSelectRow( TEST_MODULE_NAME );
        moduleBrowsePanel.clickOnToolbarUninstall();

        then: "module should not be present"
        !moduleBrowsePanel.exists( TEST_MODULE_NAME, false );
    }
}