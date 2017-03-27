package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.RoleWizardPanel
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
import org.openqa.selenium.Keys

class UserBrowsePanel_GridPanel_Spec
    extends BaseUsersSpec
{

    def "GIVEN user browse panel is opened WHEN no selection THEN all rows should be white"()
    {
        given: "user browse panel is opened"
        int rowNumber = userBrowsePanel.getRowsCount();

        expect: "all rows should be white"
        userBrowsePanel.getSelectedRowsNumber() == 0 && rowNumber > 0;

        and: "refresh button is present on the tree grid toolbar"
        userBrowsePanel.isRefreshButtonDisplayed();
    }

    def "GIVEN user browse panel is opened WHEN first row is clicked THEN first row is blue"()
    {
        when: "first row is clicked"
        userBrowsePanel.clickCheckboxAndSelectFolder( UserBrowsePanel.BrowseItemType.SYSTEM );

        then:
        userBrowsePanel.getSelectedRowsNumber() == 1;
    }

    def "GIVEN one content is selected WHEN spacebar key pressed THEN the row is no longer selected"()
    {
        given: "one content is selected "
        userBrowsePanel.clickCheckboxAndSelectFolder( UserBrowsePanel.BrowseItemType.SYSTEM );
        saveScreenshot( "spacebar-system1" );

        when: "spacebar key pressed"
        userBrowsePanel.pressKeyOnRow( "system", Keys.SPACE );

        then: "the row is no longer selected"
        saveScreenshot( "spacebar-system2" );
        userBrowsePanel.getSelectedRowsNumber() == 0;
    }

    def "GIVEN 'system' folder is selected WHEN 'Selection Controller'-checkbox has been clicked  THEN row is no longer selected"()
    {
        given:
        List<String> userItemNames = userBrowsePanel.getItemsNameFromGrid();
        userBrowsePanel.clickCheckboxAndSelectFolder( UserBrowsePanel.BrowseItemType.SYSTEM );

        when: "'Selection Controller'-checkbox has been clicked "
        userBrowsePanel.clickOnSelectionController();
        saveScreenshot( "sel_controller_clicked_1" )

        then: "the row is no longer selected"
        userBrowsePanel.getSelectedRowsNumber() == 0 && userItemNames.size() > 0;
    }

    def "GIVEN no any items are selected WHEN 'Selection Controller' checkbox is checked THEN all rows should be selected"()
    {
        when: "'Selection Controller' checkbox is checked"
        userBrowsePanel.doSelectAll();

        then: "all rows should be selected"
        userBrowsePanel.getRowsCount() == userBrowsePanel.getSelectedRowsNumber();
    }

    def "GIVEN a 'system' folder on root having a child WHEN listed THEN expander is shown"()
    {
        expect: " expander is shown near the 'system' folder"
        userBrowsePanel.isExpanderPresent( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() )
    }

    def "GIVEN a 'system' folder on root  WHEN folder expanded THEN 'Users' and 'Groups' shown"()
    {
        when: " a 'system' folder expanded"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );

        then: "'users' should be shown"
        userBrowsePanel.exists( "users", true );
    }

    def "GIVEN a 'roles' folder on root  WHEN folder expanded THEN 'ea' enterprise administrator shown"()
    {
        when: " a 'roles' folder expanded"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.ROLES_FOLDER.getValue() );

        then: "'ea' - enterprise administrator should be shown"
        userBrowsePanel.exists( "system.admin", true );
    }

    def "GIVEN a 'system' folder with an open expander WHEN closed THEN no children are listed beneath"()
    {
        given:
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        saveScreenshot( "system-expanded" );
        when:
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        saveScreenshot( "system-collapsed" );

        then:
        userBrowsePanel.getChildNames().size() == 0;
    }

    def "GIVEN 'system' folder is selected WHEN arrow down was pressed THEN next row should be selected"()
    {
        given: "'system folder is selected'"
        userBrowsePanel.clickCheckboxAndSelectFolder( UserBrowsePanel.BrowseItemType.SYSTEM );
        int before = userBrowsePanel.getSelectedRowsNumber();

        when: "'arrow down' was pressed"
        userBrowsePanel.pressKeyOnRow( UserBrowsePanel.BrowseItemType.SYSTEM.getValue(), Keys.ARROW_DOWN );
        saveScreenshot( "arrow_down_pressed_user" );

        then: "'system' is not selected now and the next folder should be selected"
        !userBrowsePanel.isRowSelected( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() ) && userBrowsePanel.getSelectedRowsNumber() ==
            before;
    }

    def "GIVEN a 'roles' selected WHEN arrow up is typed THEN another row is selected"()
    {
        given: "'roles folder is selected'"
        userBrowsePanel.clickCheckboxAndSelectFolder( UserBrowsePanel.BrowseItemType.ROLES_FOLDER );
        int before = userBrowsePanel.getSelectedRowsNumber();

        when: "arrow up typed"
        userBrowsePanel.pressKeyOnRow( UserBrowsePanel.BrowseItemType.ROLES_FOLDER.getValue(), Keys.ARROW_UP );
        saveScreenshot( "arrow_up_roles" );

        then: "roles is not selected now, another folder in the root directory is selected"
        !userBrowsePanel.isRowSelected( UserBrowsePanel.BrowseItemType.ROLES_FOLDER.getValue() ) &&
            userBrowsePanel.getSelectedRowsNumber() == before;
    }

    def "GIVEN a 'roles' is collapsed and selected WHEN arrow right is typed THEN folder becomes expanded"()
    {
        given: "'roles folder is collapsed and selected'"
        userBrowsePanel.clickCheckboxAndSelectFolder( UserBrowsePanel.BrowseItemType.ROLES_FOLDER );

        when: "arrow right typed"
        userBrowsePanel.pressKeyOnRow( UserBrowsePanel.BrowseItemType.ROLES_FOLDER.getValue(), Keys.ARROW_RIGHT );
        saveScreenshot( "arrow_right_roles" );

        then: "'roles' folder is expanded"
        userBrowsePanel.isRowExpanded( UserBrowsePanel.BrowseItemType.ROLES_FOLDER.getValue() );
    }

    def "GIVEN a 'roles' is expanded and selected WHEN arrow left is typed THEN folder becomes collapsed"()
    {
        given: "'roles folder is expanded and selected'"
        userBrowsePanel.clickCheckboxAndSelectFolder( UserBrowsePanel.BrowseItemType.ROLES_FOLDER );
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.ROLES_FOLDER.getValue() );

        when: "arrow left typed"
        userBrowsePanel.pressKeyOnRow( UserBrowsePanel.BrowseItemType.ROLES_FOLDER.getValue(), Keys.ARROW_LEFT );
        saveScreenshot( "arrow_left_roles" );

        then: "'roles' folder is collapsed"
        !userBrowsePanel.isRowExpanded( UserBrowsePanel.BrowseItemType.ROLES_FOLDER.getValue() );
    }

    def "GIVEN selected folder and WHEN hold a shift and arrow down is typed  3-times THEN 4 selected rows appears in the grid "()
    {
        given: "selected and expanded 'System' folder"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() )
        userBrowsePanel.clickCheckboxAndSelectFolder( UserBrowsePanel.BrowseItemType.SYSTEM );
        saveScreenshot( "test_user_arrow_down_shift_before" );

        when: "arrow down typed 3 times"
        userBrowsePanel.holdShiftAndPressArrow( 3, Keys.ARROW_DOWN );
        saveScreenshot( "test_user_arrow_down_shift_after" );

        then: "n+1 rows are selected in the browse panel"
        userBrowsePanel.getSelectedRowsNumber() == 4
    }

    def "GIVEN 'anonymous' user shown in the grid WHEN user -'anonymous' double clicked THEN user wizard opened"()
    {
        given:
        userBrowseFilterPanel.typeSearchText( "anonymous" );

        when: "new user present beneath a store"
        userBrowsePanel.doubleClickOnItem( "anonymous" );

        then: "new user present beneath a system store"
        UserWizardPanel wizard = new UserWizardPanel( getSession() );
        wizard.waitUntilWizardOpened();
    }

    def "GIVEN a system role WHEN role double clicked in the grid THEN role wizard opened"()
    {
        given:
        userBrowseFilterPanel.typeSearchText( "system.authenticated" );

        when: "new user present beneath a store"
        userBrowsePanel.doubleClickOnItem( "system.authenticated" );

        then: "new user present beneath a system store"
        RoleWizardPanel wizard = new RoleWizardPanel( getSession() );
        wizard.waitUntilWizardOpened();
    }
}
