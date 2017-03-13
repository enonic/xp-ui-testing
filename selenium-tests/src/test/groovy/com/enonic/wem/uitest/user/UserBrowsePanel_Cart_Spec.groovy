package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserStoreStatisticsPanel

/**
 * Created on 3/10/2017.
 *
 * Tasks:
 * enonic/xp-ui-testing#22 Add selenium tests for UserBrowseItemsSelectionPanel
 * */
class UserBrowsePanel_Cart_Spec
    extends BaseUsersSpec
{
    def "GIVEN Users app is opened  WHEN the row has been clicked THEN cart should be not active AND number should not be displayed in the cart"()
    {
        given: "Users app is opened"
        userBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        when: "checkbox is checked and 'System User Store' is selected"
        userBrowsePanel.clickOnRowByName( "/" + UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );

        then: "the cart should be not active"
        !userBrowsePanel.isCartActive();

        and: "number should not be displayed in the cart"
        userBrowsePanel.getNumberInCart() == "";
    }

    def "GIVEN existing 'System User Store'  WHEN the checkbox is clicked and the store is selected THEN cart should be not active AND '1' should be displayed in the cart"()
    {
        given: "Users app is opened"
        userBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        when: "checkbox is checked and 'System User Store' is selected"
        userBrowsePanel.clickCheckboxAndSelectUserStore( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        saveScreenshot( "system_user_store_selected" );

        then: "the cart should be not active"
        !userBrowsePanel.isCartActive();

        and: "'1' should be displayed in the cart"
        userBrowsePanel.getNumberInCart() == "1";
    }

    def "GIVEN existing 'System User Store' and 'Roles' folders  WHEN the checkbox is clicked and both folders are selected THEN cart should be active AND '2' should be displayed in the cart"()
    {
        given: "Users app is opened"
        userBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        when: "checkbox is checked and 'System User Store' is selected"
        userBrowsePanel.clickCheckboxAndSelectUserStore( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );

        and: "'Roles' folder is selected"
        userBrowsePanel.clickCheckboxAndSelectRole( UserBrowsePanel.BrowseItemType.ROLES_FOLDER.getValue() );
        saveScreenshot( "user_store_and_roles_selected" );

        then: "the cart should be active"
        userBrowsePanel.isCartActive();

        and: "'2' should be displayed in the cart"
        userBrowsePanel.getNumberInCart() == "2";

        and: "Correct display names should be displayed on the Selection Panel"
        userBrowseItemsSelectionPanel.getDisplayNameOfSelectedItems().contains( "System User Store" );

        and: ""
        userBrowseItemsSelectionPanel.getDisplayNameOfSelectedItems().contains( "Roles" );
    }

    def "GIVEN 'System User Store' and 'Roles' folders are selected WHEN one item was removed from the Selection Panel  THEN cart should not be active AND '1' should be displayed in the cart"()
    {
        given: "'System User Store' and 'Roles' folders are selected"
        userBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        userBrowsePanel.clickCheckboxAndSelectUserStore( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        userBrowsePanel.clickCheckboxAndSelectRole( UserBrowsePanel.BrowseItemType.ROLES_FOLDER.getValue() );

        when: "checkbox is checked and 'System User Store' is selected"
        userBrowseItemsSelectionPanel.removeItem( "roles" )
        saveScreenshot( "user_store_and_roles_one_item_removed" );

        then: "the cart should be active"
        !userBrowsePanel.isCartActive();

        and: "'1' should be displayed in the cart"
        userBrowsePanel.getNumberInCart() == "1";

        and: "User Store Statistics Panel should be displayed with the correct header"
        UserStoreStatisticsPanel itemStatisticsPanel = new UserStoreStatisticsPanel( getSession() );
        itemStatisticsPanel.getItemDisplayName() == "System User Store";
    }

    def "GIVEN 'System User Store' and 'Roles' folders are selected WHEN 'Clear Selection' link has been pressed THEN cart should not be active"()
    {
        given: "'System User Store' and 'Roles' folders are selected"
        userBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        userBrowsePanel.clickCheckboxAndSelectUserStore( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        userBrowsePanel.clickCheckboxAndSelectRole( UserBrowsePanel.BrowseItemType.ROLES_FOLDER.getValue() );

        when: "'Clear Selection' link has been pressed"
        userBrowseItemsSelectionPanel.clickOnClearSelectionButton()

        then: "the cart should be not active"
        !userBrowsePanel.isCartActive();

        and: "number should not be displayed in the cart"
        userBrowsePanel.getNumberInCart() == "";
    }
}