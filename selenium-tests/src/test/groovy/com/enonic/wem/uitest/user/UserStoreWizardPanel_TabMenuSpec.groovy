package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.UserStoreWizardPanel
import com.enonic.autotests.utils.TestUtils
import spock.lang.Shared

class UserStoreWizardPanel_TabMenuSpec
    extends BaseUsersSpec
{

    @Shared
    String TAB_MENU_ITEM = "<Unnamed User Store>"

    def "WHEN started adding a 'User Store' and Wizard opened THEN new tab with name '[New User Store]' is present"()
    {
        when: "the 'New' button on toolbar pressed and the 'user store' wizard opened"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() )
        UserStoreWizardPanel wizard = userBrowsePanel.clickToolbarNew().waitUntilWizardOpened();

        then: "item with title 'New User Store' is present "
        wizard.isTabMenuItemPresent( TAB_MENU_ITEM );
    }

    def "GIVEN 'user store' Wizard opened, no any data typed WHEN TabmenuItem(close) clicked THEN wizard closed and BrowsePanel showed"()
    {
        given: "'user store' wizard was opened ad AppBarTabMenu clicked"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() )
        UserStoreWizardPanel wizard = userBrowsePanel.clickToolbarNew().waitUntilWizardOpened();

        when: "no any data typed and 'close' button pressed"
        SaveBeforeCloseDialog dialog = wizard.closeTabMenuItem( TAB_MENU_ITEM );
        TestUtils.saveScreenshot( getTestSession(), "user_close1" );


        then: "close dialog should not be showed"
        dialog == null;
    }

    def "GIVEN 'user store' Wizard opened and name is typed WHEN TabmenuItem(close) clicked THEN 'SaveBeforeClose' dialog showed"()
    {
        given: "'user store' Wizard opened and name is typed"
        String displayName = "testname";
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        UserStoreWizardPanel wizard = userBrowsePanel.clickToolbarNew().waitUntilWizardOpened().typeDisplayName( displayName );

        when: "TabmenuItem(close) clicked"
        SaveBeforeCloseDialog dialog = wizard.closeTabMenuItem( displayName );
        TestUtils.saveScreenshot( getTestSession(), "user_close2" );

        then: "'SaveBeforeClose' dialog showed"
        dialog != null;
    }
}
