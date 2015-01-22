package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.modules.InstallModuleDialog
import com.enonic.autotests.pages.modules.ModuleBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class InstallModules
    extends BaseGebSpec
{
    @Shared
    ModuleBrowsePanel moduleBrowsePanel;

    @Shared
    String XEON_MODULE_NAME = "com.enonic.wem.modules.xeon";

    @Shared
    String MODULE_STRING = "mvn:com.enonic.wem.modules/xeon/1.0.0";

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

    def "GIVEN install dialog was opened  WHEN  module string typed THEN new module appears in the grid"()
    {
        given:
        InstallModuleDialog dialog = moduleBrowsePanel.clickToolbarInstall();
        when:
        moduleBrowsePanel = dialog.typeModuleURL( MODULE_STRING ).clickOnInstall();

        then:
        moduleBrowsePanel.exists( XEON_MODULE_NAME, true );
    }
}
