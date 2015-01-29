package com.enonic.wem.uitest.module

import com.enonic.autotests.pages.modules.InstallModuleDialog
import com.enonic.autotests.pages.modules.ModuleBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.BaseGebSpec
import org.openqa.selenium.Keys
import spock.lang.Shared

class ModuleBrowsePanel_GridPanel_Spec
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

    def setup()
    {
        go "admin"
        moduleBrowsePanel = NavigatorHelper.openModules( getTestSession() );
    }

    def "GIVEN modules listed on root WHEN no selection THEN all rows are white"()
    {
        given:
        int rowNumber = moduleBrowsePanel.getRowNumber();

        expect:
        moduleBrowsePanel.getSelectedRowsNumber() == 0 && rowNumber > 0;

    }

    def "GIVEN modules listed on root WHEN one row is clicked THEN it row is blue"()
    {
        given:
        int before = moduleBrowsePanel.getSelectedRowsNumber();

        when:
        moduleBrowsePanel.clickCheckboxAndSelectRow( XEON_MODULE_NAME );

        then:
        moduleBrowsePanel.getSelectedRowsNumber() == 1 && before == 0;
    }

    def "GIVEN a selected module  WHEN spacebar is typed THEN row is no longer selected"()
    {
        given:

        moduleBrowsePanel.clickCheckboxAndSelectRow( XEON_MODULE_NAME );
        TestUtils.saveScreenshot( getTestSession(), "modulespacebartest1" );

        when:
        moduleBrowsePanel.pressKeyOnRow( XEON_MODULE_NAME, Keys.SPACE );

        then:
        TestUtils.saveScreenshot( getTestSession(), "modulespacebartest2" );
        moduleBrowsePanel.getSelectedRowsNumber() == 0;
    }

    def "GIVEN a selected module  WHEN 'Clear selection'-link is clicked THEN row is no longer selected"()
    {
        given:
        moduleBrowsePanel.clickCheckboxAndSelectRow( XEON_MODULE_NAME );
        int before = moduleBrowsePanel.getSelectedRowsNumber();

        when:
        moduleBrowsePanel.clickOnClearSelection();

        then:
        moduleBrowsePanel.getSelectedRowsNumber() == 0 && before == 1;
    }


    def "GIVEN no Content selected WHEN 'Select all'-link is clicked THEN all rows are selected"()
    {
        given:
        moduleBrowsePanel.clickOnClearSelection();

        when:
        moduleBrowsePanel.clickOnSelectAll();
        TestUtils.saveScreenshot( getTestSession(), "select-all-modules" );

        then:
        moduleBrowsePanel.getRowNumber() == moduleBrowsePanel.getSelectedRowsNumber();
    }


    def "GIVEN a selected module  WHEN arrow down is typed THEN next row is selected"()
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

    def "WHEN  install dialog opened and module string typed THEN new module appears in the grid"()
    {
        when: "url typed and 'Install' button "
        InstallModuleDialog dialog = moduleBrowsePanel.clickToolbarInstall();
        moduleBrowsePanel = dialog.typeModuleURL( TEST_MODULE_URL ).clickOnInstall();

        then: "new module exists in the browse panel "
        moduleBrowsePanel.exists( TEST_MODULE_NAME, true );
    }


    def "GIVEN a selected module  WHEN arrow up is typed THEN previous row is selected"()
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