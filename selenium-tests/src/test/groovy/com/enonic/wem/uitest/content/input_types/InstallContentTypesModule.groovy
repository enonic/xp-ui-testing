package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.modules.InstallModuleDialog
import com.enonic.autotests.pages.modules.ModuleBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.SleepHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class InstallContentTypesModule
    extends BaseGebSpec
{
    @Shared
    ModuleBrowsePanel moduleBrowsePanel;

    @Shared
    String ALL_CONTENT_TYPES_MODULE_NAME = "com.enonic.xp.ui-testing.all-contenttypes";

    @Shared
    String ALL_CONTENT_TYPES_MODULE_URL = "mvn:com.enonic.xp.ui-testing/all-contenttypes/2.0.0-SNAPSHOT";


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
        moduleBrowsePanel = dialog.typeModuleURL( ALL_CONTENT_TYPES_MODULE_URL ).clickOnInstall();
        TestUtils.saveScreenshot( getSession(), "inst_module" )

        then: "new module exists in the browse panel and status is stopped "
        moduleBrowsePanel.exists( ALL_CONTENT_TYPES_MODULE_NAME, true ) &&
            moduleBrowsePanel.getModuleStatus( ALL_CONTENT_TYPES_MODULE_NAME ).equals( "stopped" );
    }

    def "GIVEN module selected in the browse panel WHEN 'Start' button pressed  THEN status 'started' appears near the module in browse panel "()
    {
        given: "select a existing  module"
        SleepHelper.sleep( 1000 );
        TestUtils.saveScreenshot( getSession(), "start_module" )
        moduleBrowsePanel.clickCheckboxAndSelectRow( ALL_CONTENT_TYPES_MODULE_NAME );
        moduleBrowsePanel.waitAndCheckIsButtonEnabled( ModuleBrowsePanel.START_BUTTON );

        when: "button 'Start' on the toolbar pressed"
        moduleBrowsePanel.clickOnToolbarStart();
        TestUtils.saveScreenshot( getSession(), "module-started" )

        then: "status for module becomes 'started'"
        moduleBrowsePanel.getModuleStatus( ALL_CONTENT_TYPES_MODULE_NAME ).equals( "started" );
    }
}