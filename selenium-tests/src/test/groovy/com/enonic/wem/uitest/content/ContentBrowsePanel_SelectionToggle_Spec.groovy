package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application

/**
 * Created on 2/21/2017.
 *
 * Tasks:
 * xp-ui-testing #10 Add Selenium tests for 'Shopping Cart' Icon on the toolbar
 * https://github.com/enonic/xp-ui-testing/issues/10
 * */
class ContentBrowsePanel_SelectionToggle_Spec
    extends BaseContentSpec
{
    def "GIVEN existing folder WHEN the row has been clicked THEN the 'selection toggler' should not be displayed"()
    {
        given: "content grid is opened"
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        when: "checkbox is checked and existing content is selected"
        contentBrowsePanel.clickOnRowByName( IMPORTED_FOLDER_NAME );

        then: "the 'selection toggler' should not be displayed"
        !contentBrowsePanel.isSelectionTogglerDisplayed();
    }

    def "GIVEN existing folder  WHEN checkbox has been clicked and the folder is selected THEN the 'selection toggler' should be displayed"()
    {
        when: "checkbox is checked and existing content is selected"
        findAndSelectContent( IMPORTED_FOLDER_NAME );

        then: "the 'selection toggler' should be displayed"
        contentBrowsePanel.isSelectionTogglerDisplayed();

        and: "the cart should be not active"
        !contentBrowsePanel.isSelectionTogglerActive();

        and: "correct number of selected contents should be displayed"
        contentBrowsePanel.getNumberInSelectionToggler() == "1";
    }

    def "GIVEN existing folder is selected WHEN 'Show Selected Items' has been clicked THEN the button should be active"()
    {
        when: "checkbox is checked and existing content is selected"
        findAndSelectContent( IMPORTED_FOLDER_NAME );

        then: "click on 'Show Selected Items' circle"
        contentBrowsePanel.clickOnSelectionToggler();

        and: "the 'Show Selected Items' button should be active"
        contentBrowsePanel.waitUntilSelectionTogglerActive();

        and: "correct number of selected contents should be displayed"
        contentBrowsePanel.getNumberInSelectionToggler() == "1";
    }

    def "GIVEN content grid id opened WHEN two checkboxes have been clicked THEN correct number should be displayed in the 'circle'"()
    {
        given: "content grid is opened"
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        when: "two checkboxes have been clicked"
        contentBrowsePanel.clickCheckboxAndSelectRow( 0 );
        contentBrowsePanel.clickCheckboxAndSelectRow( 1 );

        then: "the 'Show Selected Items' button should be not active"
        !contentBrowsePanel.isSelectionTogglerActive();

        and: "2 should be displayed on the circle"
        contentBrowsePanel.getNumberInSelectionToggler() == "2";
    }

    def "GIVEN two contents were selected WHEN 'Selection Controller' checkbox has been clicked THEN 'Show Selected Items' button should not be displayed"()
    {
        given: "two contents were selected"
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        contentBrowsePanel.clickCheckboxAndSelectRow( 0 );
        contentBrowsePanel.clickCheckboxAndSelectRow( 1 );

        when: "'Selection Controller' checkbox has been clicked"
        !contentBrowsePanel.clickOnSelectionController();

        then: "the 'Show Selected Items' button should not be displayed"
        !contentBrowsePanel.isSelectionTogglerDisplayed();
    }
}
