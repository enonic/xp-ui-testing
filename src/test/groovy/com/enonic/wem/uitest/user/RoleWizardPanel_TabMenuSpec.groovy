package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.RoleWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class RoleWizardPanel_TabMenuSpec
    extends BaseGebSpec
{

    @Shared
    UserBrowsePanel userBrowsePanel;

    @Shared
    String TAB_MENU_ITEM = "New Role"

    def setup()
    {
        go "admin"
        userBrowsePanel = NavigatorHelper.openUserManager( getTestSession() );
    }


    def "GIVEN started adding a 'Role' and Wizard opened WHEN tab-menu button clicked THEN list of items with one name 'New Role' is present"()
    {
        given: "'Roles' folder clicked and role wizard opened"
        RoleWizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectRow(
            UserBrowsePanel.BrowseItemType.ROLES ).clickToolbarNew().waitUntilWizardOpened();

        when: "AppBarTabMenu on the role wizard clicked and menu items showed"
        wizard.expandTabMenu();
        TestUtils.saveScreenshot( getTestSession(), "tabmenu_role" );

        then: "item with title 'New Role' is present "
        wizard.isTabMenuItemPresent( TAB_MENU_ITEM );

    }

    def "GIVEN role Wizard opened, no any data typed WHEN TabmenuItem(close) clicked THEN wizard closed and BrowsePanel showed"()
    {
        given: "content wizard was opened ad AppBarTabMenu clicked"
        RoleWizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectRow(
            UserBrowsePanel.BrowseItemType.ROLES ).clickToolbarNew().waitUntilWizardOpened().expandTabMenu();

        when: "no any data typed and 'close' button pressed"
        SaveBeforeCloseDialog dialog = wizard.closeInTabMenuItem( TAB_MENU_ITEM );
        TestUtils.saveScreenshot( getTestSession(), "role_close1" );

        then: "close dialog should not be showed"
        dialog == null;

    }

    def "GIVEN role Wizard opened and name is typed WHEN TabmenuItem(close) clicked THEN 'SaveBeforeClose' dialog showed"()
    {
        given: "role Wizard opened and name is typed"
        String displayName = "testname";
        RoleWizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectRow(
            UserBrowsePanel.BrowseItemType.ROLES ).clickToolbarNew().waitUntilWizardOpened().typeDisplayName( displayName ).expandTabMenu();

        when: "TabmenuItem(close) clicked"
        SaveBeforeCloseDialog dialog = wizard.closeInTabMenuItem( displayName );
        TestUtils.saveScreenshot( getTestSession(), "role_close2" );

        then: "'SaveBeforeClose' dialog showed"
        dialog != null;

    }

}
