package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.usermanager.browsepanel.NewPrincipalDialog
import com.enonic.autotests.pages.usermanager.browsepanel.UserItemName
import spock.lang.Ignore

/**
 * Created on 31.08.2017.
 *
 * Tasks:xp-ui-testing#84  Add Selenium tests for NewPrincipalDialog
 * */
class NewPrincipalDialog_Spec
    extends BaseUsersSpec
{

    def "GIVEN users grid is opened WHEN 'New' button has been clicked THEN modal dialog should appear with 4 items"()
    {
        when: "'New' button has been clicked"
        NewPrincipalDialog newPrincipalDialog = userBrowsePanel.clickOnToolbarNew( null );
        List<String> names = newPrincipalDialog.getItemNames();

        then: "the dialog should be loaded"
        newPrincipalDialog.waitForLoaded( Application.EXPLICIT_NORMAL );

        and: "correct header should be displayed"
        newPrincipalDialog.getHeader() == NewPrincipalDialog.HEADER_TEXT;

        and: "'Cancel' button should be displayed"
        newPrincipalDialog.isCancelButtonTopDisplayed();

        and: "number of user items should be 4"
        names.size() == 4;

        and: "'User' item should be present in the list"
        names.contains( "User" );
        and: "'User Group' item should be present in the list"
        names.contains( "User Group" );
        and: "'User Store' item should be present in the list"
        names.contains( "User Store" );
        and: "'Role' item should be present in the list"
        names.contains( "Role" );
    }

    def "GIVEN 'System User Store' is selected WHEN 'New' button has been pressed THEN modal dialog should appear with 2 items"()
    {
        given: "'System User Store' is selected"
        userBrowsePanel.clickOnRowByName( UserItemName.SYSTEM.getValue() );

        when: "New' button has been pressed"
        userBrowsePanel.clickToolbarNew();
        NewPrincipalDialog newPrincipalDialog = new NewPrincipalDialog( getSession() );
        List<String> itemNames = newPrincipalDialog.getItemNames();

        then: "two items should be present in the list"
        itemNames.size() == 2;

        and: "'User' item should be present in the list"
        itemNames.get( 0 ) == "User";

        and: "'User Group' item should be present in the list"
        itemNames.get( 1 ) == "User Group";

        and: "path should be in 'System User Store'"
        newPrincipalDialog.getPath() == "System User Store"
    }
    //TODO remove @Ignore
    // verifies the xp #5604 Incorrect label for the New-button on the toolbar, when several items was selected
    @Ignore
    def "GIVEN several items are selected WHEN 'New' button has been pressed THEN NewPrincipalDialog should be appears"()
    {
        given:
        userBrowsePanel.clickOnExpander( UserItemName.SYSTEM.getValue() );
        userBrowsePanel.clickCheckboxAndSelectFolder( UserItemName.USERS_FOLDER );
        userBrowsePanel.clickCheckboxAndSelectFolder( UserItemName.GROUPS_FOLDER );
        userBrowsePanel.clickCheckboxAndSelectFolder( UserItemName.ROLES_FOLDER );

        when: "New button has been clicked"
        userBrowsePanel.clickToolbarNew();
        saveScreenshot()

        then: "NewPrincipal dialog should appear"
        NewPrincipalDialog newPrincipalDialog = new NewPrincipalDialog( getSession() );
        newPrincipalDialog.waitForLoaded( Application.EXPLICIT_NORMAL );

        //and:""

    }
}
