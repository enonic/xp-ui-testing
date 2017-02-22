package com.enonic.wem.uitest.content
/**
 * Created on 2/21/2017.
 *
 * Tasks:
 * xp-ui-testing#10 Add Selenium tests for 'Shopping Cart' Icon on the toolbar
 * https://github.com/enonic/xp-ui-testing/issues/10
 * */
class ContentBrowsePanel_Cart_Spec
    extends BaseContentSpec
{
    def "GIVEN existing folder  WHEN the row has been clicked THEN cart should be not active AND number should not be displayed in the cart"()
    {
        when: "checkbox is checked and existing content is selected"
        contentBrowsePanel.selectRowByName(IMPORTED_FOLDER_NAME);

        then: "the cart should be not active"
        !contentBrowsePanel.isCartActive();

        and: "number should not be displayed in the cart"
        contentBrowsePanel.getNumberInCart() == "";
    }

    def "GIVEN existing folder  WHEN the folder is selected THEN correct number should be displayed in the cart"()
    {
        when: "checkbox is checked and existing content is selected"
        findAndSelectContent( IMPORTED_FOLDER_NAME );

        then: "correct number of the selected content should be displayed"
        contentBrowsePanel.isCartDisplayed();

        and: "the cart should be not active"
        !contentBrowsePanel.isCartActive();

        and: "correct number of selected contents should be displayed"
        contentBrowsePanel.getNumberInCart() == "1";
    }

    def "GIVEN existing folder is selected WHEN cart has been clicked THEN the cart should be active"()
    {
        when: "checkbox is checked and existing content is selected"
        findAndSelectContent( IMPORTED_FOLDER_NAME );

        then: "cart has been clicked"
        contentBrowsePanel.clickOnCart();

        and: "the cart should be active"
        contentBrowsePanel.waitUntilCartActive();

        and: "'Clear Selection' link should be displayed"
        itemsSelectionPanel.isClearSelectionLinkDisplayed();

        and: "correct number of selected contents should be displayed"
        contentBrowsePanel.getNumberInCart() == "1";
    }

    def "WHEN two contents were selected THEN correct number should be displayed in the cart AND in the 'Clear Selection' button"()
    {
        when: "checkbox is checked and existing content is selected"
        contentBrowsePanel.clickCheckboxAndSelectRow( 0 );
        contentBrowsePanel.clickCheckboxAndSelectRow( 1 );

        then: "the cart should be active"
        contentBrowsePanel.isCartActive();

        and: "correct number of selected contents should be displayed in the cart"
        contentBrowsePanel.getNumberInCart() == "2";

        and: "'Clear Selection' link should be displayed"
        itemsSelectionPanel.isClearSelectionLinkDisplayed();

        and: "correct number of selected contents should be present in the 'Clear Selection' link"
        itemsSelectionPanel.getNumberInClearSelectionLink() == "2";
    }

    def "GIVEN two contents were selected WHEN 'Clear Selection' has been pressed THEN cart should not be active and the link should not be displayed"()
    {
        given: "checkbox is checked and existing content is selected"
        contentBrowsePanel.clickCheckboxAndSelectRow( 0 );
        contentBrowsePanel.clickCheckboxAndSelectRow( 1 );

        when: "'Clear Selection' has been pressed"
        itemsSelectionPanel.clickOnClearSelectionButton();

        then: "cart should be not active(disabled)"
        !contentBrowsePanel.isCartActive() ;

        and: "'Clear Selection' link should not be displayed"
        !itemsSelectionPanel.isClearSelectionLinkDisplayed();

    }
}
