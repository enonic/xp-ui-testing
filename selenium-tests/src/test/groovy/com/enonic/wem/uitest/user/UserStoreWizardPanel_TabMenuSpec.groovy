package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.usermanager.browsepanel.NewPrincipalDialog
import com.enonic.autotests.pages.usermanager.browsepanel.UserItemName
import com.enonic.autotests.pages.usermanager.wizardpanel.UserStoreWizardPanel
import spock.lang.Shared

class UserStoreWizardPanel_TabMenuSpec
    extends BaseUsersSpec
{

    @Shared
    String USERSTORE_TAB_TITLE = "<Unnamed User Store>"

    def "WHEN started adding a 'User Store' and Wizard opened THEN new tab with name '[New User Store]' is present"()
    {
        when: "the 'New' button on toolbar pressed and the 'user store' wizard opened"
        userBrowsePanel.clickOnExpander( UserItemName.SYSTEM.getValue() )
        NewPrincipalDialog newPrincipalDialog = userBrowsePanel.clickOnToolbarNew( null );
        UserStoreWizardPanel wizard = newPrincipalDialog.selectItemOpenWizard( NewPrincipalDialog.ItemsToCreate.USER_STORE, null );

        then: "title 'New User Store' should be present on the wizard page"
        userBrowsePanel.isTabMenuItemPresent( USERSTORE_TAB_TITLE );

        and: "options filter input for ID provider should be displayed"
        wizard.isSelectorForIdProviderDisplayed();

        and: "options filter input for Principals should be displayed"
        wizard.isPermissionsSelectorDisplayed();

        and: "input for description should be displayed"
        wizard.isDescriptionInputDisplayed();
    }

    def "GIVEN 'user store' Wizard opened, no any data typed WHEN TabmenuItem(close) clicked THEN wizard closed and BrowsePanel showed"()
    {
        given: "'user store' wizard was opened ad AppBarTabMenu clicked"
        userBrowsePanel.clickOnExpander( UserItemName.SYSTEM.getValue() )
        NewPrincipalDialog newPrincipalDialog = userBrowsePanel.clickOnToolbarNew( null );
        UserStoreWizardPanel wizard = newPrincipalDialog.selectItemOpenWizard( NewPrincipalDialog.ItemsToCreate.USER_STORE, null );

        when: "no any data typed and 'close' button pressed"
        SaveBeforeCloseDialog dialog = wizard.close( USERSTORE_TAB_TITLE );
        saveScreenshot( "user_store_closed" );


        then: "close dialog should not be shown"
        dialog == null;
    }

    def "GIVEN 'user store' Wizard opened and name is typed WHEN 'Close' on the tab clicked THEN 'SaveBeforeClose' dialog should appear"()
    {
        given: "'user store' Wizard opened and name is typed"
        String displayName = "testname";
        userBrowsePanel.clickOnExpander( UserItemName.SYSTEM.getValue() );
        NewPrincipalDialog newPrincipalDialog = userBrowsePanel.clickOnToolbarNew( null );
        UserStoreWizardPanel wizard = newPrincipalDialog.selectItemOpenWizard( NewPrincipalDialog.ItemsToCreate.USER_STORE, null );
        wizard.typeDisplayName( displayName );

        when: "'Close' clicked"
        SaveBeforeCloseDialog dialog = wizard.close( displayName );
        saveScreenshot( "user_store_not_closed" );

        then: "'SaveBeforeClose' dialog should appear"
        dialog != null;
    }
}
