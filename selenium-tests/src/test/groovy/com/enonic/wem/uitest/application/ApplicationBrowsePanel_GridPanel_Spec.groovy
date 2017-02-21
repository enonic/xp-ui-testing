package com.enonic.wem.uitest.application

import org.openqa.selenium.Keys

class ApplicationBrowsePanel_GridPanel_Spec
    extends BaseApplicationSpec
{
    def "GIVEN some applications are listed on the root EXPECTED correct description for the application should be displayed"()
    {
        expect:
        applicationBrowsePanel.getApplicationDescription( FIRST_APP_DISPLAY_NAME ) == TEST_DESCRIPTION;
    }

    def "GIVEN some applications are listed on the root WHEN no selection THEN all rows should be white"()
    {
        given:
        int rowNumber = applicationBrowsePanel.getRowsCount();

        expect: "number of selected rows should be 0"
        applicationBrowsePanel.getSelectedRowsNumber() == 0 && rowNumber > 0;

        and: "refresh button should be present on the tree grid toolbar"
        applicationBrowsePanel.isRefreshButtonDisplayed();
    }

    def "GIVEN some applications are listed on the root WHEN one checkbox has been clicked THEN its row should be blue"()
    {
        given: "some applications are listed on the root"
        int before = applicationBrowsePanel.getSelectedRowsNumber();

        when: "one checkbox has been clicked"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );

        then: "one row should be blue"
        applicationBrowsePanel.getSelectedRowsNumber() == 1 && before == 0;
    }

    def "GIVEN existing selected application  WHEN spacebar is typed THEN row is no longer selected"()
    {
        given:
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );
        saveScreenshot( "app_spacebartest1" );

        when: "space key has been pressed"
        applicationBrowsePanel.findRowByDisplayNameAndSendKey( FIRST_APP_DISPLAY_NAME, Keys.SPACE );

        then: "row is no longer selected"
        saveScreenshot( "app_spacebartest2" );
        applicationBrowsePanel.getSelectedRowsNumber() == 0;
    }

    def "GIVEN existing application is selected WHEN 'Selection Controller' checkbos was clicked twice THEN row is no longer selected"()
    {
        given:"existign application is selected"
        applicationBrowsePanel.clickCheckboxAndSelectRowByDisplayName( FIRST_APP_DISPLAY_NAME );
        int before = applicationBrowsePanel.getSelectedRowsNumber();

        when: "'Selection Controller' checkbos was clicked twice"
        applicationBrowsePanel.doClearSelection();

        then: "row is no longer selected"
        applicationBrowsePanel.getSelectedRowsNumber() == 0 && before == 1;
    }

    def "GIVEN no applications are selected WHEN 'Select all'-link is clicked THEN all rows are selected"()
    {
        when:
        applicationBrowsePanel.doSelectAll();
        saveScreenshot( "select-all-apps-pressed" );

        then:
        applicationBrowsePanel.getRowsCount() == applicationBrowsePanel.getSelectedRowsNumber();
    }

    def "GIVEN existing application is selected WHEN arrow down was pressed THEN next row should be selected"()
    {
        given:
        applicationBrowsePanel.clickCheckboxAndSelectRow( 0 );
        Set<String> namesBefore = applicationBrowsePanel.getDisplayNamesOfSelectedGridItems();

        when:"arrow down is typed"
        applicationBrowsePanel.pressKeyOnRow( 0, Keys.ARROW_DOWN );
        saveScreenshot( "arrow_down_app" );
        Set<String> namesAfter = applicationBrowsePanel.getDisplayNamesOfSelectedGridItems();

        then:
        applicationBrowsePanel.getSelectedRowsNumber() == 1;

        and: "another application should be selected"
        !namesBefore.asList().get( 0 ).equals( namesAfter.asList().get( 0 ) );
    }

    def "GIVEN existing application is selected WHEN arrow up was pressed THEN previous row should be selected"()
    {
        given:"existin application is selected"
        applicationBrowsePanel.clickCheckboxAndSelectRow( 3 );
        Set<String> displayNamesBefore = applicationBrowsePanel.getDisplayNamesOfSelectedGridItems();

        when:
        applicationBrowsePanel.pressKeyOnRow( 3, Keys.ARROW_UP );
        saveScreenshot( "app_arrow_up" );
        Set<String> namesAfter = applicationBrowsePanel.getDisplayNamesOfSelectedGridItems();

        then:
        applicationBrowsePanel.getSelectedRowsNumber() == 1 && !displayNamesBefore.asList().get( 0 ).equals( namesAfter.asList().get( 0 ) );
    }

    def "GIVEN existing application is selected WHEN hold a shift and arrow down is typed  3-times THEN 4 selected rows appears in the grid "()
    {
        given: "one application is selected"
        applicationBrowsePanel.clickCheckboxAndSelectRow( 0 );

        when: "arrow down was typed 3 times"
        applicationBrowsePanel.holdShiftAndPressArrow( 3, Keys.ARROW_DOWN );
        saveScreenshot( "app_arrow_down_shift" );

        then: "n+1 rows should be selected in the browse panel"
        applicationBrowsePanel.getDisplayNamesOfSelectedGridItems().size() == 4
    }

    def "GIVEN existing application is selected WHEN 'shift' key is hold 'arrow up' was pressed 3-times THEN 4 selected rows should be present in the grid "()
    {
        given: "existing application is selected"
        applicationBrowsePanel.clickCheckboxAndSelectRow( 3 );

        when: "'shift' key is hold and 'arrow up' was pressed  3-times"
        applicationBrowsePanel.holdShiftAndPressArrow( 3, Keys.ARROW_UP );
        saveScreenshot( "app_arrow_up_shift" );

        then: "n+1 rows should be selected in the browse panel"
        applicationBrowsePanel.getDisplayNamesOfSelectedGridItems().size() == 4
    }
}