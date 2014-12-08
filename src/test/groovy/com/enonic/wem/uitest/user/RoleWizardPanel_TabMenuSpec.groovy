package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.WizardPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class RoleWizardPanel_TabMenuSpec
    extends BaseGebSpec
{

    @Shared
    UserBrowsePanel userBrowsePanel;

    def setup()
    {
        go "admin"
        userBrowsePanel = NavigatorHelper.openUserManager( getTestSession() );
    }


    def "GIVEN started adding a 'Role' and Wizard opened WHEN tab-menu button clicked THEN list of items with one name 'New Role' is present"()
    {
        given: "'Roles' folder clicked and role wizard opened"
        WizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectRow(
            UserBrowsePanel.BrowseItemType.ROLES ).clickToolbarNew().waitUntilWizardOpened();

        when: "AppBarTabMenu on the role wizard clicked and menu items showed"
        wizard.expandTabMenu();

        then: "item with title 'New Role' is present "
        wizard.isTabMenuItemPresent( "New Role" );

    }

    def "GIVEN content Wizard opened, no any data typed WHEN TabmenuItem(close) clicked THEN wizard closed and BrowsePanel showed"()
    {
        given: "content wizard was opened ad AppBarTabMenu clicked"
        WizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectRow(
            UserBrowsePanel.BrowseItemType.ROLES ).clickToolbarNew().waitUntilWizardOpened().expandTabMenu();

        when: "no any data typed and 'close' button pressed"
        SaveBeforeCloseDialog dialog = wizard.closeInTabMenuItem( "New Role" );

        then: "close dialog should not be showed"
        dialog == null;

    }

    def "GIVEN role Wizard opened and name is typed WHEN TabmenuItem(close) clicked THEN 'SaveBeforeClose' dialog showed"()
    {
        given: "role Wizard opened and name is typed"
        String displayName = "testname";
        WizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectRow(
            UserBrowsePanel.BrowseItemType.ROLES ).clickToolbarNew().waitUntilWizardOpened().typeDisplayName( displayName ).expandTabMenu();

        when: "TabmenuItem(close) clicked"
        SaveBeforeCloseDialog dialog = wizard.closeInTabMenuItem( displayName );

        then: "'SaveBeforeClose' dialog showed"
        dialog != null;

    }

}
