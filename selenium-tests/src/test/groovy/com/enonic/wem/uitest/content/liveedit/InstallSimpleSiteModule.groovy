package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.modules.InstallModuleDialog
import com.enonic.autotests.pages.modules.ModuleBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class InstallSimpleSiteModule

    extends BaseGebSpec
{
    @Shared
    ModuleBrowsePanel moduleBrowsePanel;

    @Shared
    String SIMPLE_SITE_MODULE_NAME = "com.enonic.xp.ui-testing.simple-page";

    @Shared
    String SIMPLE_SITE_MODULE_URL = "mvn:com.enonic.xp.ui-testing/simple-page/5.1.0-SNAPSHOT"


    def setup()
    {
        go "admin"
        moduleBrowsePanel = NavigatorHelper.openModules( getTestSession() );
    }

    def "GIVEN install dialog was opened  WHEN  module string typed THEN new module appears in the grid"()
    {
        given: "click on 'Install' button on the toolbar, dialog window appears"
        InstallModuleDialog dialog = moduleBrowsePanel.clickToolbarInstall();

        when: "url typed and 'Install' button "
        moduleBrowsePanel = dialog.typeModuleURL( SIMPLE_SITE_MODULE_URL ).clickOnInstall();

        then: "new module exists in the browse panel and status is stopped "
        moduleBrowsePanel.exists( SIMPLE_SITE_MODULE_NAME, true ) &&
            moduleBrowsePanel.getModuleStatus( SIMPLE_SITE_MODULE_NAME ).equals( "stopped" );
    }

    def "GIVEN module selected in the browse panel WHEN 'Start' button pressed  THEN status 'started' appears near the module in browse panel "()
    {
        given: "select a existing  module"
        moduleBrowsePanel.clickAndSelectRow( SIMPLE_SITE_MODULE_NAME );
        moduleBrowsePanel.waitAndCheckIsButtonEnabled( ModuleBrowsePanel.START_BUTTON );

        when: "button 'Start' on the toolbar pressed"
        moduleBrowsePanel.clickOnToolbarStart();

        then: "status for module becomes 'started'"
        moduleBrowsePanel.getModuleStatus( SIMPLE_SITE_MODULE_NAME ).equals( "started" );
    }
}