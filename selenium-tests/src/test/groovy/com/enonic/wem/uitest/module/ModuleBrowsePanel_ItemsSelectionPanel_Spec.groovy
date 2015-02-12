package com.enonic.wem.uitest.module

import com.enonic.autotests.pages.modules.ModuleBrowseItemsSelectionPanel
import com.enonic.autotests.services.NavigatorHelper
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ModuleBrowsePanel_ItemsSelectionPanel_Spec
    extends BaseModuleSpec
{

    @Shared
    ModuleBrowseItemsSelectionPanel itemsSelectionPanel;

    def setup()
    {
        go "admin"
        moduleBrowsePanel = NavigatorHelper.openModules( getTestSession() );
        itemsSelectionPanel = moduleBrowsePanel.getItemSelectionPanel();
    }


    def "GIVEN one selected module WHEN selecting one more THEN two SelectionItem-s are listed"()
    {
        given: " there is a one selected module"
        moduleBrowsePanel.clickAndSelectRow( FIRST_MODULE_NAME );

        when: "selected a one more module"
        moduleBrowsePanel.clickCheckboxAndSelectRow( SECOND_MODULE_NAME );
        then:
        itemsSelectionPanel.getSelectedItemCount() == 2;
    }

    def "GIVEN two selected module WHEN selecting one more THEN three SelectionItem-s are listed"()
    {
        given: " there is a two selected module"
        moduleBrowsePanel.clickAndSelectRow( FIRST_MODULE_NAME );
        moduleBrowsePanel.clickCheckboxAndSelectRow( SECOND_MODULE_NAME );

        when: "selected a one more module"

        moduleBrowsePanel.clickCheckboxAndSelectRow( THIRD_MODULE_NAME );

        then: "three SelectionItem-s are listed"
        itemsSelectionPanel.getSelectedItemCount() == 3;
    }

    def "GIVEN three selected module WHEN deselecting one THEN two SelectionItem-s are listed"()
    {
        given: "there are three selected module in browse panel"
        moduleBrowsePanel.clickCheckboxAndSelectRow( FIRST_MODULE_NAME );
        moduleBrowsePanel.clickCheckboxAndSelectRow( SECOND_MODULE_NAME );
        moduleBrowsePanel.clickCheckboxAndSelectRow( THIRD_MODULE_NAME );

        when: "one module was deselected"
        moduleBrowsePanel.deSelectModuleInTable( THIRD_MODULE_NAME );

        then: "only two items are listed in the browse panel"
        itemsSelectionPanel.getSelectedItemCount() == 2;

    }

    def "WHEN two selected module  THEN two SelectionItem-s with the same name are listed"()
    {
        when: "two module are selected"
        moduleBrowsePanel.clickCheckboxAndSelectRow( FIRST_MODULE_NAME );
        moduleBrowsePanel.clickCheckboxAndSelectRow( SECOND_MODULE_NAME );

        then: "three SelectionItem-s are listed"
        List actualNames = itemsSelectionPanel.getSelectedItemNames();
        actualNames.size() == 2 && actualNames.contains( FIRST_MODULE_NAME ) && actualNames.contains( SECOND_MODULE_NAME );
    }
}
