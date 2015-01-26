package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.modules.ModuleBrowseItemsSelectionPanel
import com.enonic.autotests.pages.modules.ModuleBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.WaitHelper
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ModuleBrowsePanel_ItemsSelectionPanel_Spec
    extends BaseGebSpec
{
    @Shared
    ModuleBrowsePanel moduleBrowsePanel;


    @Shared
    String XEON_MODULE_NAME = "com.enonic.wem.modules.xeon";

    @Shared
    String TEST_MODULE_NAME = "com.enonic.xp.ui-testing.first-module";

    @Shared
    String TEST_MODULE_URL = "mvn:com.enonic.xp.ui-testing/first-module/5.0.0-SNAPSHOT";

    @Shared
    String XSLT_MODULE_URL = "mvn:com.enonic.wem.modules/xslt/1.0.0";

    @Shared
    String XSLT_MODULE_NAME = "com.enonic.wem.modules.xslt";


    @Shared
    ModuleBrowseItemsSelectionPanel itemsSelectionPanel;

    def setup()
    {
        go "admin"
        moduleBrowsePanel = NavigatorHelper.openModules( getTestSession() );
        itemsSelectionPanel = moduleBrowsePanel.getItemSelectionPanel();
    }

    def "install a new test module"()
    {
        expect:
        moduleBrowsePanel.clickToolbarInstall().typeModuleURL( TEST_MODULE_URL ).clickOnInstall();
        WaitHelper.sleep( 1000 );
        moduleBrowsePanel.clickToolbarInstall().typeModuleURL( XSLT_MODULE_URL ).clickOnInstall();
    }

    def "GIVEN one selected module WHEN selecting one more THEN two SelectionItem-s are listed"()
    {
        given: " there is a one selected module"
        moduleBrowsePanel.clickAndSelectRow( XEON_MODULE_NAME );

        when: "selected a one more module"
        moduleBrowsePanel.clickCheckboxAndSelectRow( TEST_MODULE_NAME );
        then:
        itemsSelectionPanel.getSelectedItemCount() == 2;
    }

    def "GIVEN two selected module WHEN selecting one more THEN three SelectionItem-s are listed"()
    {
        given: " there is a two selected module"
        moduleBrowsePanel.clickAndSelectRow( XEON_MODULE_NAME );

        when: "selected a one more module"
        moduleBrowsePanel.clickCheckboxAndSelectRow( TEST_MODULE_NAME );
        moduleBrowsePanel.clickCheckboxAndSelectRow( XSLT_MODULE_NAME );

        then: "three SelectionItem-s are listed"
        itemsSelectionPanel.getSelectedItemCount() == 3;
    }

    def "GIVEN three selected module WHEN deselecting one THEN two SelectionItem-s are listed"()
    {

        given: "there are three selected module in browse panel"
        moduleBrowsePanel.clickCheckboxAndSelectRow( TEST_MODULE_NAME );
        moduleBrowsePanel.clickCheckboxAndSelectRow( XSLT_MODULE_NAME );
        moduleBrowsePanel.clickCheckboxAndSelectRow( XEON_MODULE_NAME );


        when: "one module was deselected"
        moduleBrowsePanel.deSelectModuleInTable( XEON_MODULE_NAME );

        then: "only two items are listed in the browse panel"
        itemsSelectionPanel.getSelectedItemCount() == 2;

    }
}
