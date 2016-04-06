package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.WizardPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.utils.TestUtils
import spock.lang.Shared

class GroupWizardPanel_TabMenuSpec
    extends BaseUsersSpec
{

    @Shared
    String GROUP_TAB_TITLE = "<Unnamed Group>"

    def "WHEN started adding a 'system Group' and Wizard opened  THEN list of items with one name 'New Group' is present"()
    {
        when: "'Groups' folder,that located beneath a 'System' folder, was selected, the 'New' button pressed  and group wizard opened"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        TestUtils.saveScreenshot( getTestSession(), "system_exp" );
        WizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.GROUPS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();

        then: "item with title 'New Role' is present "
        userBrowsePanel.isTabMenuItemPresent( GROUP_TAB_TITLE );
    }

    def "GIVEN Group Wizard opened, no any data typed WHEN TabmenuItem(close) clicked THEN wizard closed and BrowsePanel showed"()
    {
        given: "group wizard was opened ad AppBarTabMenu clicked"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        WizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.GROUPS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();

        when: "no any data typed and 'close' button pressed"
        SaveBeforeCloseDialog dialog = wizard.close( GROUP_TAB_TITLE );
        TestUtils.saveScreenshot( getTestSession(), "grw_close1" );

        then: "close dialog should not be showed"
        dialog == null;
    }

    def "GIVEN Group Wizard opened and name is typed WHEN TabmenuItem(close) clicked THEN 'SaveBeforeClose' dialog showed"()
    {
        given: "group Wizard opened and name is typed"
        String displayName = "testname";
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        WizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.GROUPS_FOLDER ).clickToolbarNew().waitUntilWizardOpened().typeDisplayName( displayName );

        when: "'Close' button clicked"
        SaveBeforeCloseDialog dialog = wizard.close( displayName );
        TestUtils.saveScreenshot( getTestSession(), "grw_close2" );

        then: "'SaveBeforeClose' dialog showed"
        dialog != null;
    }
}
