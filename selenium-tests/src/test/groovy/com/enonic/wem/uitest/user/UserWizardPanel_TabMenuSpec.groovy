package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.WizardPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.utils.TestUtils
import spock.lang.Shared

class UserWizardPanel_TabMenuSpec
    extends BaseUsersSpec
{

    @Shared
    String USER_TAB_TITLE = "<Unnamed User>"

    def "GIVEN started adding a 'User' and Wizard opened WHEN tab-menu button clicked THEN list of items with one name 'New User' is present"()
    {
        when: "'Users' folder clicked and 'New' button pressed and 'user wizard' opened"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        WizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.USERS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();

        then: "item with title 'New User' is present on the tab menu "
        userBrowsePanel.isTabMenuItemPresent( USER_TAB_TITLE );

    }

    def "GIVEN user Wizard opened, no any data typed WHEN TabmenuItem(close) clicked THEN wizard closed and BrowsePanel showed"()
    {
        given: "group wizard was opened ad AppBarTabMenu clicked"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() )
        WizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.USERS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();

        when: "no any data typed and 'close' button pressed"
        SaveBeforeCloseDialog dialog = wizard.close( USER_TAB_TITLE );
        TestUtils.saveScreenshot( getTestSession(), "user_tab_closed" );

        then: "close dialog should not be showed"
        dialog == null;

    }

    def "GIVEN user Wizard opened and name is typed WHEN TabmenuItem(close) clicked THEN 'SaveBeforeClose' dialog showed"()
    {
        given: "user Wizard opened and name is typed"
        String displayName = "testname";
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        WizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.USERS_FOLDER ).clickToolbarNew().waitUntilWizardOpened().typeDisplayName( displayName );

        when: "TabmenuItem(close) clicked"
        SaveBeforeCloseDialog dialog = wizard.close( displayName );
        TestUtils.saveScreenshot( getTestSession(), "user_save_before_close" );

        then: "'SaveBeforeClose' dialog showed"
        dialog != null;

    }
}