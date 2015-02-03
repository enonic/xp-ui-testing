package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.WizardPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class GroupWizardPanel_TabMenuSpec
    extends BaseGebSpec
{

    @Shared
    UserBrowsePanel userBrowsePanel;

    @Shared
    String TAB_MENU_ITEM = "<New Group>"

    def setup()
    {
        go "admin"
        userBrowsePanel = NavigatorHelper.openUserManager( getTestSession() );
    }


    def "WHEN started adding a 'system Group' and Wizard opened  THEN list of items with one name 'New Group' is present"()
    {
        when: "'Groups' folder,that located beneath a 'System' folder, was selected, the 'New' button pressed  and group wizard opened"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        TestUtils.saveScreenshot( getTestSession(), "system_exp" );
        WizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectRow(
            UserBrowsePanel.BrowseItemType.GROUPS ).clickToolbarNew().waitUntilWizardOpened();

        then: "item with title 'New Role' is present "
        wizard.isTabMenuItemPresent( TAB_MENU_ITEM );

    }

    def "GIVEN Group Wizard opened, no any data typed WHEN TabmenuItem(close) clicked THEN wizard closed and BrowsePanel showed"()
    {
        given: "group wizard was opened ad AppBarTabMenu clicked"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        WizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectRow(
            UserBrowsePanel.BrowseItemType.GROUPS ).clickToolbarNew().waitUntilWizardOpened();

        when: "no any data typed and 'close' button pressed"
        SaveBeforeCloseDialog dialog = wizard.closeTabMenuItem( TAB_MENU_ITEM );
        TestUtils.saveScreenshot( getTestSession(), "grw_close1" );

        then: "close dialog should not be showed"
        dialog == null;

    }

    def "GIVEN Group Wizard opened and name is typed WHEN TabmenuItem(close) clicked THEN 'SaveBeforeClose' dialog showed"()
    {
        given: "group Wizard opened and name is typed"
        String displayName = "testname";
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        WizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectRow(
            UserBrowsePanel.BrowseItemType.GROUPS ).clickToolbarNew().waitUntilWizardOpened().typeDisplayName( displayName );

        when: "TabmenuItem(close) clicked"
        SaveBeforeCloseDialog dialog = wizard.closeTabMenuItem( displayName );
        TestUtils.saveScreenshot( getTestSession(), "grw_close2" );

        then: "'SaveBeforeClose' dialog showed"
        dialog != null;

    }

}
