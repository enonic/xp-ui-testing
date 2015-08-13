package com.enonic.wem.uitest.application

import com.enonic.autotests.utils.TestUtils
import org.openqa.selenium.Keys

class ApplicationBrowsePanel_GridPanel_Spec
    extends BaseApplicationSpec
{

    def "GIVEN applications listed on root WHEN no selection THEN all rows are white"()
    {
        given:
        int rowNumber = applicationBrowsePanel.getRowNumber();

        expect:
        applicationBrowsePanel.getSelectedRowsNumber() == 0 && rowNumber > 0;

    }

    def "GIVEN applications listed on root WHEN one row is clicked THEN its row is blue"()
    {
        given:
        int before = applicationBrowsePanel.getSelectedRowsNumber();

        when:
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );

        then:
        applicationBrowsePanel.getSelectedRowsNumber() == 1 && before == 0;
    }

    def "GIVEN a selected application  WHEN spacebar is typed THEN row is no longer selected"()
    {
        given:
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );
        TestUtils.saveScreenshot( getTestSession(), "app_spacebartest1" );

        when:
        applicationBrowsePanel.pressKeyOnRow( FIRST_APP_NAME, Keys.SPACE );

        then:
        TestUtils.saveScreenshot( getTestSession(), "app_spacebartest2" );
        applicationBrowsePanel.getSelectedRowsNumber() == 0;
    }

    def "GIVEN a selected application WHEN 'Clear selection'-link is clicked THEN row is no longer selected"()
    {
        given:
        applicationBrowsePanel.clickCheckboxAndSelectRow( FIRST_APP_NAME );
        int before = applicationBrowsePanel.getSelectedRowsNumber();

        when:
        applicationBrowsePanel.clickOnClearSelection();

        then:
        applicationBrowsePanel.getSelectedRowsNumber() == 0 && before == 1;
    }


    def "GIVEN no applications selected WHEN 'Select all'-link is clicked THEN all rows are selected"()
    {
        given:
        applicationBrowsePanel.clickOnClearSelection();

        when:
        applicationBrowsePanel.clickOnSelectAll();
        TestUtils.saveScreenshot( getTestSession(), "select-all-apps" );

        then:
        applicationBrowsePanel.getRowNumber() == applicationBrowsePanel.getSelectedRowsNumber();
    }


    def "GIVEN a selected application WHEN arrow down is typed THEN next row is selected"()
    {
        given:

        applicationBrowsePanel.clickCheckboxAndSelectRow( 0 );
        Set<String> namesBefore = applicationBrowsePanel.getSelectedGridItemNames();

        when:
        applicationBrowsePanel.pressKeyOnRow( 0, Keys.ARROW_DOWN );
        TestUtils.saveScreenshot( getTestSession(), "arrow_down_module" );
        Set<String> namesAfter = applicationBrowsePanel.getSelectedGridItemNames();


        then:
        applicationBrowsePanel.getSelectedRowsNumber() == 1 && !namesBefore.asList().get( 0 ).equals( namesAfter.asList().get( 0 ) );
    }

    def "GIVEN a selected application WHEN arrow up is typed THEN previous row is selected"()
    {
        given:
        applicationBrowsePanel.clickCheckboxAndSelectRow( 3 );
        Set<String> namesBefore = applicationBrowsePanel.getSelectedGridItemNames();

        when:
        applicationBrowsePanel.pressKeyOnRow( 3, Keys.ARROW_UP );
        TestUtils.saveScreenshot( getTestSession(), "module_arrow_up" );
        Set<String> namesAfter = applicationBrowsePanel.getSelectedGridItemNames();

        then:
        applicationBrowsePanel.getSelectedRowsNumber() == 1 && !namesBefore.asList().get( 0 ).equals( namesAfter.asList().get( 0 ) );
    }


    def "GIVEN selected application WHEN hold a shift and arrow down is typed  3-times THEN 4 selected rows appears in the grid "()
    {
        given: "one application is selected"
        applicationBrowsePanel.clickCheckboxAndSelectRow( 0 );

        when: "arrow down typed 3 times"
        applicationBrowsePanel.holdShiftAndPressArrow( 3, Keys.ARROW_DOWN );
        TestUtils.saveScreenshot( getTestSession(), "module_arrow_down_shift" );

        then: "n+1 rows are selected in the browse panel"
        applicationBrowsePanel.getSelectedRowsNumber() == 4
    }


    def "GIVEN selected application WHEN hold a shift and arrow up is typed  3-times THEN 4 selected rows appears in the grid "()
    {
        given: "one application is selected"
        applicationBrowsePanel.clickCheckboxAndSelectRow( 3 );

        when: "arrow up typed 3 times"
        applicationBrowsePanel.holdShiftAndPressArrow( 3, Keys.ARROW_UP );
        TestUtils.saveScreenshot( getTestSession(), "content_arrow_up_shift" );

        then: "n+1 rows are selected in the browse panel"
        applicationBrowsePanel.getSelectedRowsNumber() == 4
    }
}