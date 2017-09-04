package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.WizardPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserItemName
import spock.lang.Shared

class UserWizardPanel_TabMenuSpec
    extends BaseUsersSpec
{

    @Shared
    String USER_TAB_TITLE = "<Unnamed User>"

    def "WHEN 'User' Wizard is opened THEN new tab with 'New User' title should be present on the tab menu"()
    {
        when: "'Users' folder was selected and 'New' button has been pressed and 'user wizard' is opened"
        userBrowsePanel.clickOnExpander( UserItemName.SYSTEM.getValue() );
        userBrowsePanel.clickCheckboxAndSelectFolder( UserItemName.USERS_FOLDER ).clickToolbarNew( UserItemName.USERS_FOLDER )

        then: "new tab with 'New User' title should be present on the tab menu"
        userBrowsePanel.isTabMenuItemPresent( USER_TAB_TITLE );
    }

    def "GIVEN user Wizard is opened WHEN TabmenuItem(close) has been clicked THEN wizard should be closed and BrowsePanel is shown"()
    {
        given: "group wizard is opened ad AppBarTabMenu clicked"
        userBrowsePanel.clickOnExpander( UserItemName.SYSTEM.getValue() )
        WizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectFolder( UserItemName.USERS_FOLDER ).clickOnToolbarNew(
            UserItemName.USERS_FOLDER );

        when: "'close' button has been pressed"
        SaveBeforeCloseDialog dialog = wizard.close( USER_TAB_TITLE );
        saveScreenshot( "user_tab_closed" );

        then: "close dialog should not be shown"
        dialog == null;

    }

    def "GIVEN user Wizard is opened AND name has been typed WHEN TabmenuItem(close) clicked THEN 'SaveBeforeClose' dialog showed"()
    {
        given: "user Wizard is opened and name has been typed"
        String displayName = "testname";
        userBrowsePanel.clickOnExpander( UserItemName.SYSTEM.getValue() );
        WizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectFolder( UserItemName.USERS_FOLDER ).clickOnToolbarNew(
            UserItemName.USERS_FOLDER );
        wizard.waitUntilWizardOpened().typeDisplayName( displayName );

        when: "TabmenuItem(close) has been clicked"
        SaveBeforeCloseDialog dialog = wizard.close( displayName );
        saveScreenshot( "user_save_before_close" );

        then: "'SaveBeforeClose' dialog should be present, because there are unsaved changes"
        dialog != null;
    }
}