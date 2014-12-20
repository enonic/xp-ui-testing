package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.UserStoreWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class UserStoreWizardPanel_TabMenuSpec
    extends BaseGebSpec
{

    @Shared
    UserBrowsePanel userBrowsePanel;

    @Shared
    String TAB_MENU_ITEM = "New User Store"

    def setup()
    {
        go "admin"
        userBrowsePanel = NavigatorHelper.openUserManager( getTestSession() );
    }


    def "GIVEN started adding a 'User Store' and Wizard opened WHEN tab-menu button clicked THEN list of items with one name 'User Store' is present"()
    {
        given: "the 'New' button on toolbar pressed and the 'user store' wizard opened"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() )
        UserStoreWizardPanel wizard = userBrowsePanel.clickToolbarNew().waitUntilWizardOpened();

        when: "AppBarTabMenu on the group wizard clicked and menu items showed"
        wizard.expandTabMenu();

        then: "item with title 'New User Store' is present "
        wizard.isTabMenuItemPresent( TAB_MENU_ITEM );

    }

    def "GIVEN 'user store' Wizard opened, no any data typed WHEN TabmenuItem(close) clicked THEN wizard closed and BrowsePanel showed"()
    {
        given: "'user store' wizard was opened ad AppBarTabMenu clicked"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() )
        UserStoreWizardPanel wizard = userBrowsePanel.clickToolbarNew().waitUntilWizardOpened().expandTabMenu();

        when: "no any data typed and 'close' button pressed"
        SaveBeforeCloseDialog dialog = wizard.closeInTabMenuItem( TAB_MENU_ITEM );
        TestUtils.saveScreenshot( getTestSession(), "user_close1" );


        then: "close dialog should not be showed"
        dialog == null;

    }

    def "GIVEN 'user store' Wizard opened and name is typed WHEN TabmenuItem(close) clicked THEN 'SaveBeforeClose' dialog showed"()
    {
        given: "'user store' Wizard opened and name is typed"
        String displayName = "testname";
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        UserStoreWizardPanel wizard = userBrowsePanel.clickToolbarNew().waitUntilWizardOpened().typeDisplayName(
            displayName ).expandTabMenu();

        when: "TabmenuItem(close) clicked"
        SaveBeforeCloseDialog dialog = wizard.closeInTabMenuItem( displayName );
        TestUtils.saveScreenshot( getTestSession(), "user_close2" );

        then: "'SaveBeforeClose' dialog showed"
        dialog != null;

    }

}
