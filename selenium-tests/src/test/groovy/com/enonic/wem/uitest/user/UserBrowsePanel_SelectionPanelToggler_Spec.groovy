package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.usermanager.browsepanel.RoleStatisticsPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserItemName
import com.enonic.autotests.pages.usermanager.browsepanel.UserStoreStatisticsPanel

/**
 * Created on 3/10/2017.
 *
 * Tasks:
 * enonic/xp-ui-testing#31 Add Selenium tests for 'Show Selected Items' button(grid toolbar)
 * */
class UserBrowsePanel_SelectionPanelToggler_Spec
    extends BaseUsersSpec
{
    def "GIVEN Users app is opened  WHEN no one checkbox is checked THEN the 'selection toggler' should not be displayed"()
    {
        given: "Users app is opened"
        userBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        expect: "'Show Selected Items' button should not be displayed"
        !userBrowsePanel.isSelectionTogglerDisplayed();
    }

    def "GIVEN Users app is opened  WHEN the row has been clicked THEN the 'selection toggler' should not be displayed"()
    {
        given: "Users app is opened"
        userBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        when: "one row has been clicked and 'System User Store' is selected"
        userBrowsePanel.clickOnRowByName( "/" + UserItemName.SYSTEM.getValue() );

        then: "the 'selection toggler' should not be displayed"
        !userBrowsePanel.isSelectionTogglerDisplayed();
    }

    def "GIVEN existing 'System User Store' WHEN the checkbox is clicked and the store is selected THEN the 'selection toggler' should be displayed AND '1' should be displayed in the circle"()
    {
        given: "Users app is opened"
        userBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        when: "checkbox is checked and 'System User Store' is selected"
        userBrowsePanel.clickCheckboxAndSelectUserStore( UserItemName.SYSTEM.getValue() );
        saveScreenshot( "system_user_store_selected" );

        then: "the 'selection toggler' should be displayed"
        userBrowsePanel.isSelectionTogglerDisplayed();

        and: "the 'selection toggler' should be not active"
        !userBrowsePanel.isSelectionTogglerActive();
        and: "'1' should be displayed in the circle"
        userBrowsePanel.getNumberInSelectionToggler() == "1";
    }

    def "GIVEN existing 'System User Store' and 'Roles' folders  WHEN the checkbox is clicked and both folders are selected THEN cart should be active AND '2' should be displayed in the cart"()
    {
        given: "Users app is opened"
        userBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        when: "checkbox is checked and 'System User Store' is selected"
        userBrowsePanel.clickCheckboxAndSelectUserStore( UserItemName.SYSTEM.getValue() );

        and: "'Roles' folder has been selected"
        userBrowsePanel.clickCheckboxAndSelectRole( UserItemName.ROLES_FOLDER.getValue() );
        saveScreenshot( "user_store_and_roles_selected" );

        then: "the 'selection toggler' should be not active"
        !userBrowsePanel.isSelectionTogglerActive();

        and: "'2' should be displayed in the circle"
        userBrowsePanel.getNumberInSelectionToggler() == "2";

        and: "last selected item should be displayed on the 'statistic panel'"
        RoleStatisticsPanel roleStatisticsPanel = new RoleStatisticsPanel( getSession() );
        roleStatisticsPanel.getItemDisplayName() == "Roles";
    }

    def "GIVEN 'System User Store' and 'Roles' folders are selected WHEN checkbox for 'Roles' folder was unchecked  THEN cart should not be active AND '1' should be displayed in the cart"()
    {
        given: "'System User Store' and 'Roles' folders are selected"
        userBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        userBrowsePanel.clickCheckboxAndSelectUserStore( UserItemName.SYSTEM.getValue() );
        userBrowsePanel.clickCheckboxAndSelectRole( UserItemName.ROLES_FOLDER.getValue() );

        when: "checkbox is checked and 'System User Store' is selected"
        userBrowsePanel.clickCheckboxAndUnselectRow( "Roles" );
        saveScreenshot( "user_store_and_roles_roles_item_unchecked" );

        then: "'1' should be displayed on the circle"
        userBrowsePanel.getNumberInSelectionToggler() == "1";

        and: "User Store Statistics Panel should be displayed with the correct header"
        UserStoreStatisticsPanel itemStatisticsPanel = new UserStoreStatisticsPanel( getSession() );
        itemStatisticsPanel.getItemDisplayName() == "System User Store";
    }

    def "GIVEN 'System User Store' and 'Roles' folders are selected WHEN 'SelectionController' checkbox has been clicked THEN the 'selection toggler' circle should not be displayed"()
    {
        given: "'System User Store' and 'Roles' folders are selected"
        userBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        userBrowsePanel.clickCheckboxAndSelectUserStore( UserItemName.SYSTEM.getValue() );
        userBrowsePanel.clickCheckboxAndSelectRole( UserItemName.ROLES_FOLDER.getValue() );

        when: "'SelectionController' checkbox has been clicked"
        userBrowsePanel.doClearSelection();
        saveScreenshot( "sel_controller_clicked_circle_not_displayed" );

        then: "the 'selection toggler' circle should not be displayed"
        !userBrowsePanel.isSelectionTogglerDisplayed();
    }

    def "GIVEN checkbox for 'Roles' folders is checked WHEN 'Show Selected Item' has been clicked THEN only 'Roles' folder should be displayed in the grid"()
    {
        given: "checkbox for 'Roles' folders is checked"
        userBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        userBrowsePanel.clickCheckboxAndSelectRole( UserItemName.ROLES_FOLDER.getValue() );

        when: "'Show Selected Item' has been clicked"
        userBrowsePanel.clickOnSelectionToggler();

        then: "only 'Roles' folder should be displayed in the grid"
        userBrowsePanel.getRowsCount() == 1;

        and: "'Selection Controller' checkbox should be checked"
        userBrowsePanel.isSelectionControllerChecked();
    }


    def "GIVEN checkbox for 'Roles' folders is checked AND 'Show Selections' button has been clicked WHEN 'Hide Selection' button has been pressed THEN initial grid should be displayed"()
    {
        given: "checkbox for 'Roles' folders is checked"
        userBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        userBrowsePanel.clickCheckboxAndSelectRole( UserItemName.ROLES_FOLDER.getValue() );

        and: "'Show Selected Item' has been clicked"
        userBrowsePanel.clickOnSelectionToggler();

        when: "'Hide Selection' button has been pressed"
        userBrowsePanel.clickOnSelectionToggler();
        saveScreenshot( "hide_selection_pressed" );
        List<String> names = userBrowsePanel.getItemsNameFromGrid();

        then: "initial grid should be displayed"
        names.size() > 1;

        and: "System folder should be displayed"
        names.stream().anyMatch { s -> s.contains( "system" ) };

        and: "'Selection Controller' checkbox should be 'partial'"
        userBrowsePanel.isSelectionPartial();
    }
}