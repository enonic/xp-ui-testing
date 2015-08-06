package com.enonic.wem.uitest.module

import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.TestUtils
import org.openqa.selenium.Keys

class ModuleBrowsePanel_GridPanel_Spec
    extends BaseModuleSpec
{

    def setup()
    {
        go "admin"
        moduleBrowsePanel = NavigatorHelper.openApplications( getTestSession() );
    }


    def "GIVEN applications listed on root WHEN no selection THEN all rows are white"()
    {
        given:
        int rowNumber = moduleBrowsePanel.getRowNumber();

        expect:
        moduleBrowsePanel.getSelectedRowsNumber() == 0 && rowNumber > 0;

    }

    def "GIVEN applications listed on root WHEN one row is clicked THEN its row is blue"()
    {
        given:
        int before = moduleBrowsePanel.getSelectedRowsNumber();

        when:
        moduleBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );

        then:
        moduleBrowsePanel.getSelectedRowsNumber() == 1 && before == 0;
    }

    def "GIVEN a selected application  WHEN spacebar is typed THEN row is no longer selected"()
    {
        given:
        moduleBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );
        TestUtils.saveScreenshot( getTestSession(), "modulespacebartest1" );

        when:
        moduleBrowsePanel.pressKeyOnRow( FIRST_APP_NAME, Keys.SPACE );

        then:
        TestUtils.saveScreenshot( getTestSession(), "modulespacebartest2" );
        moduleBrowsePanel.getSelectedRowsNumber() == 0;
    }

    def "GIVEN a selected application WHEN 'Clear selection'-link is clicked THEN row is no longer selected"()
    {
        given:
        moduleBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );
        int before = moduleBrowsePanel.getSelectedRowsNumber();

        when:
        moduleBrowsePanel.clickOnClearSelection();

        then:
        moduleBrowsePanel.getSelectedRowsNumber() == 0 && before == 1;
    }


    def "GIVEN no applications selected WHEN 'Select all'-link is clicked THEN all rows are selected"()
    {
        given:
        moduleBrowsePanel.clickOnClearSelection();

        when:
        moduleBrowsePanel.clickOnSelectAll();
        TestUtils.saveScreenshot( getTestSession(), "select-all-modules" );

        then:
        moduleBrowsePanel.getRowNumber() == moduleBrowsePanel.getSelectedRowsNumber();
    }


    def "GIVEN a selected application WHEN arrow down is typed THEN next row is selected"()
    {
        given:

        moduleBrowsePanel.clickCheckboxAndSelectRow( 0 );
        Set<String> namesBefore = moduleBrowsePanel.getSelectedGridItemNames();

        when:
        moduleBrowsePanel.pressKeyOnRow( 0, Keys.ARROW_DOWN );
        TestUtils.saveScreenshot( getTestSession(), "arrow_down_module" );
        Set<String> namesAfter = moduleBrowsePanel.getSelectedGridItemNames();


        then:
        moduleBrowsePanel.getSelectedRowsNumber() == 1 && !namesBefore.asList().get( 0 ).equals( namesAfter.asList().get( 0 ) );
    }

    def "GIVEN a selected application WHEN arrow up is typed THEN previous row is selected"()
    {
        given:
        moduleBrowsePanel.clickCheckboxAndSelectRow( 3 );
        Set<String> namesBefore = moduleBrowsePanel.getSelectedGridItemNames();

        when:
        moduleBrowsePanel.pressKeyOnRow( 3, Keys.ARROW_UP );
        TestUtils.saveScreenshot( getTestSession(), "module_arrow_up" );
        Set<String> namesAfter = moduleBrowsePanel.getSelectedGridItemNames();

        then:

        moduleBrowsePanel.getSelectedRowsNumber() == 1 && !namesBefore.asList().get( 0 ).equals( namesAfter.asList().get( 0 ) );
    }


    def "GIVEN selected module WHEN hold a shift and arrow down is typed  3-times THEN 4 selected rows appears in the grid "()
    {
        given: "one module is selected"
        moduleBrowsePanel.clickCheckboxAndSelectRow( 0 );

        when: "arrow down typed 3 times"
        moduleBrowsePanel.holdShiftAndPressArrow( 3, Keys.ARROW_DOWN );
        TestUtils.saveScreenshot( getTestSession(), "module_arrow_down_shift" );

        then: "n+1 rows are selected in the browse panel"
        moduleBrowsePanel.getSelectedRowsNumber() == 4
    }


    def "GIVEN selected module WHEN hold a shift and arrow up is typed  3-times THEN 4 selected rows appears in the grid "()
    {
        given: "one module is selected"
        moduleBrowsePanel.clickCheckboxAndSelectRow( 3 );

        when: "arrow up typed 3 times"
        moduleBrowsePanel.holdShiftAndPressArrow( 3, Keys.ARROW_UP );
        TestUtils.saveScreenshot( getTestSession(), "content_arrow_up_shift" );

        then: "n+1 rows are selected in the browse panel"
        moduleBrowsePanel.getSelectedRowsNumber() == 4
    }
}