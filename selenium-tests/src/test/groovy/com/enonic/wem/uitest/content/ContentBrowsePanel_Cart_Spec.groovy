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

    def "GIVEN existing folder  WHEN the folder is selected THEN correct number should be displayed"()
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
}
