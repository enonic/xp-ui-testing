package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.WizardPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import spock.lang.Shared

class GroupWizardPanel_TabMenuSpec
    extends BaseUsersSpec
{

    @Shared
    String GROUP_TAB_TITLE = "<Unnamed Group>"

    def "WHEN 'System' folder is expanded AND 'Group' is selected AND 'New' button has been pressed THEN new tab with the name 'New Role' is added "()
    {
        when: "'System' folder is expanded"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        saveScreenshot( "system_folder_expanded" );

        and: "'Group' folder is selected AND 'New' button has been pressed"
        userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.GROUPS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();

        then: "new tab with name 'New Role' should be present"
        userBrowsePanel.isTabMenuItemPresent( GROUP_TAB_TITLE );
    }

    def "GIVEN Group Wizard is opened, no any data typed WHEN 'close' button has been clicked THEN wizard closed and BrowsePanel is shown"()
    {
        given: "group wizard is opened"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        WizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.GROUPS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();

        when: "no any data was typed and 'close' button pressed"
        SaveBeforeCloseDialog dialog = wizard.close( GROUP_TAB_TITLE );
        saveScreenshot( "gr_wizard_closed" );

        then: "close dialog should not be present"
        dialog == null;
    }

    def "GIVEN Group Wizard opened and name has been typed WHEN 'close' button pressed THEN 'SaveBeforeClose' dialog should appear"()
    {
        given: "group Wizard opened and name has been typed"
        String displayName = "testname";
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        WizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.GROUPS_FOLDER ).clickToolbarNew().waitUntilWizardOpened().typeDisplayName( displayName );

        when: "'Close' button has been clicked"
        SaveBeforeCloseDialog dialog = wizard.close( displayName );
        saveScreenshot( "gr_wizard_closed2" );

        then: "'SaveBeforeClose' dialog should appear"
        dialog != null;
    }
}
