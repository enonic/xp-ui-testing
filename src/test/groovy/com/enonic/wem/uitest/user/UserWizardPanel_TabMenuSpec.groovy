package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.WizardPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class UserWizardPanel_TabMenuSpec
    extends BaseGebSpec
{

    @Shared
    UserBrowsePanel userBrowsePanel;

    @Shared
    String TAB_MENU_ITEM = "New User"

    def setup()
    {
        go "admin"
        userBrowsePanel = NavigatorHelper.openUserManager( getTestSession() );
    }


    def "GIVEN started adding a 'User' and Wizard opened WHEN tab-menu button clicked THEN list of items with one name 'New User' is present"()
    {
        given: "'Users' folder clicked and user wizard opened"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        WizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectRow(
            UserBrowsePanel.BrowseItemType.USERS ).clickToolbarNew().waitUntilWizardOpened();
        when: "AppBarTabMenu on the user wizard clicked and menu items showed"
        wizard.expandTabMenu();

        then: "item with title 'New User' is present on the tab menu "
        wizard.isTabMenuItemPresent( TAB_MENU_ITEM );

    }

    def "GIVEN user Wizard opened, no any data typed WHEN TabmenuItem(close) clicked THEN wizard closed and BrowsePanel showed"()
    {
        given: "group wizard was opened ad AppBarTabMenu clicked"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() )
        WizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectRow(
            UserBrowsePanel.BrowseItemType.USERS ).clickToolbarNew().waitUntilWizardOpened().expandTabMenu();

        when: "no any data typed and 'close' button pressed"
        SaveBeforeCloseDialog dialog = wizard.closeInTabMenuItem( TAB_MENU_ITEM );
        TestUtils.saveScreenshot( getTestSession(), "user_close1" );

        then: "close dialog should not be showed"
        dialog == null;

    }

    def "GIVEN user Wizard opened and name is typed WHEN TabmenuItem(close) clicked THEN 'SaveBeforeClose' dialog showed"()
    {
        given: "user Wizard opened and name is typed"
        String displayName = "testname";
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        WizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectRow(
            UserBrowsePanel.BrowseItemType.USERS ).clickToolbarNew().waitUntilWizardOpened().typeDisplayName( displayName ).expandTabMenu();

        when: "TabmenuItem(close) clicked"
        SaveBeforeCloseDialog dialog = wizard.closeInTabMenuItem( displayName );
        TestUtils.saveScreenshot( getTestSession(), "user_close2" );

        then: "'SaveBeforeClose' dialog showed"
        dialog != null;

    }
}