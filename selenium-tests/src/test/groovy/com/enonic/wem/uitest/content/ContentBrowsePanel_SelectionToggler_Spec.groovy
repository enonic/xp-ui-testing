package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application

/**
 * Created on 2/21/2017.
 * */
class ContentBrowsePanel_SelectionToggler_Spec
    extends BaseContentSpec
{
    def "WHEN  existing folder has been highlighted THEN the 'selection toggler' should not be displayed"()
    {
        given: "content grid is opened"
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        when: "the row has been highlighted"
        sleep( 1000 );
        contentBrowsePanel.clickOnRowByName( IMPORTED_FOLDER_NAME );
        sleep( 1000 );

        then: "the 'selection toggler' should not be displayed"
        !contentBrowsePanel.isSelectionTogglerDisplayed();
    }

    def "WHEN existibg folder has been checked THEN the 'selection toggler' should be displayed"()
    {
        when: "checkbox is checked and existing content is selected"
        findAndSelectContent( IMPORTED_FOLDER_NAME );

        then: "the 'selection toggler' should be displayed"
        contentBrowsePanel.isSelectionTogglerDisplayed();

        and: "the 'selection toggler' should be not active"
        !contentBrowsePanel.isSelectionTogglerActive();

        and: "correct number of selected contents should be displayed"
        contentBrowsePanel.getNumberInSelectionToggler() == "1";
    }

    def "GIVEN existing folder has been checked WHEN 'Show Selected Items' has been clicked THEN grid should be filterd"()
    {
        when: "checkbox is checked and existing content is selected"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        filterPanel.clickOnCleanFilter();

        then: "click on 'Show Selected Items' circle"
        contentBrowsePanel.clickOnSelectionToggler();
        saveScreenshot( "show_selected_content" );
        List<String> names = contentBrowsePanel.getContentNamesFromGrid();

        and: "the 'Show Selected Items' button should be active"
        contentBrowsePanel.waitUntilSelectionTogglerActive();

        and: "1 number should be displayed in the circle"
        contentBrowsePanel.getNumberInSelectionToggler() == "1";

        and: "only one content should be present in the grid"
        names.size() == 1;
        and: "correct name should be displayed in the grid"
        names.get( 0 ).contains( IMPORTED_FOLDER_NAME );
    }

    def "WHEN two checkboxes have been clicked THEN expected number should be displayed in the 'circle'"()
    {
        given: "content grid is opened"
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        when: "two checkboxes have been clicked"
        sleep( 1000 );
        contentBrowsePanel.clickCheckboxAndSelectRow( 0 );
        contentBrowsePanel.clickCheckboxAndSelectRow( 1 );
        sleep( 1000 );

        then: "the 'Show Selected Items' button should be not active"
        !contentBrowsePanel.isSelectionTogglerActive();

        and: "2 should be displayed on the circle"
        contentBrowsePanel.getNumberInSelectionToggler() == "2";
    }

    def "GIVEN two items are checked WHEN 'Select All Rows' checkbox has been clicked THEN 'Show Selected Items' button gets not visible"()
    {
        given: "two items are checked"
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        sleep( 1000 );
        contentBrowsePanel.clickCheckboxAndSelectRow( 0 );
        contentBrowsePanel.clickCheckboxAndSelectRow( 1 );

        when: "'Select All Rows' checkbox has been clicked"
        contentBrowsePanel.clickOnSelectionController();

        then: "'Show Selected Items' circle-button gets not visible"
        !contentBrowsePanel.isSelectionTogglerDisplayed();
    }
}
