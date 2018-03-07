package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.pages.usermanager.browsepanel.UserItemName
import com.enonic.autotests.pages.usermanager.wizardpanel.RoleWizardPanel
import com.enonic.autotests.vo.usermanager.Role
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Role_Save_Delete_Spec
    extends BaseUsersSpec
{

    @Shared
    Role TEST_ROLE;

    @Shared
    String NEW_DISPLAY_NAME = "new display name";

    @Shared
    String NEW_NAME = "new_name";

    def "GIVEN adding of a new role WHEN data typed  and 'Save' button pressed  AND page refreshed in the browser THEN wizard shown with a correct data"()
    {
        given: "creating new role "
        RoleWizardPanel roleWizardPanel = openRoleWizard();
        Role refreshWizardRole = buildRole( "role", "test-wizard-role", "refresh wizard page" );

        when: "data typed and role saved"
        roleWizardPanel.typeData( refreshWizardRole ).save().waitForNotificationMessage();
        userBrowsePanel.refreshPanelInBrowser();
        saveScreenshot( "role_wizard_refreshed" );

        then: "wizard is opened"
        roleWizardPanel.isOpened();

        and: "correct display name displayed"
        roleWizardPanel.getNameInputValue() == refreshWizardRole.getName();
    }

    def "GIVEN creating new role WHEN role saved and wizard closed THEN new role should be listed"()
    {
        given: "creating new role "
        RoleWizardPanel roleWizardPanel = openRoleWizard();
        TEST_ROLE = buildRole( "role", "test-role", "description" );

        when: "role saved and wizard closed"
        String roleCreatingMessage = roleWizardPanel.typeData( TEST_ROLE ).save().waitForNotificationMessage();
        roleWizardPanel.close( TEST_ROLE.getDisplayName() );
        userBrowsePanel.clickOnExpander( UserItemName.ROLES_FOLDER.getValue() );
        userBrowseFilterPanel.typeSearchText( TEST_ROLE.getName() );

        then: "new role should be listed"
        saveScreenshot( "role-added" );
        userBrowsePanel.exists( TEST_ROLE.getName() );

        and: "correct notification message appears"
        roleCreatingMessage == ROLE_CREATED_MESSAGE;
    }

    def "GIVEN existing role WHEN the role has been opened THEN correct description should be isplayed"()
    {
        given: "existing role"
        userBrowseFilterPanel.typeSearchText( TEST_ROLE.getName() );

        when: "the role has been opened"
        RoleWizardPanel roleWizardPanel = userBrowsePanel.clickCheckboxAndSelectRole( TEST_ROLE.getName() ).clickToolbarEdit();

        then: "correct description should be displayed"
        saveScreenshot( "check-role-description" );
        roleWizardPanel.getDescription() == TEST_ROLE.getDescription();
    }

    def "GIVEN a existing role WHEN creating new role with the same name THEN correct notification message appears"()
    {
        given: "creating of new role"
        RoleWizardPanel roleWizardPanel = openRoleWizard();

        when: "role has been saved and wizard closed"
        roleWizardPanel.typeData( TEST_ROLE ).save();
        String errorMessage = userBrowsePanel.waitErrorNotificationMessage( Application.EXPLICIT_NORMAL );
        then: "message, that role with it name already exists"
        errorMessage == String.format( ROLE_EXISTS, TEST_ROLE.getName() );
    }

    def "GIVEN existing role WHEN role selected and 'Delete' button pressed THEN role not displayed in a grid"()
    {
        given: "existing role"
        RoleWizardPanel roleWizardPanel = openRoleWizard();
        Role role = buildRole( "role", "test-role", "description" );
        roleWizardPanel.typeData( role ).save().close( role.getDisplayName() );

        when: "role was selected and 'Delete' button pressed"
        userBrowsePanel.doClearSelection();
        userBrowseFilterPanel.typeSearchText( role.getName() );
        userBrowsePanel.clickCheckboxAndSelectRole( role.getName() ).clickToolbarDelete().doDelete();
        String message = userBrowsePanel.waitForNotificationMessage();
        saveScreenshot( "role-was-deleted" );

        then: "role should not be displayed in the grid"
        saveScreenshot( role.getName() );
        !userBrowsePanel.exists( role.getName() );
        and: "correct notification message should be displayed"
        message == String.format( ROLE_DELETED_MESSAGE, role.getName() );
    }

    def "GIVEN existing role WHEN display name has been changed THEN role with new display name should be listed"()
    {
        given: "existing role opened"
        userBrowseFilterPanel.typeSearchText( TEST_ROLE.getName() );
        RoleWizardPanel roleWizardPanel = userBrowsePanel.clickCheckboxAndSelectRole( TEST_ROLE.getName() ).clickToolbarEdit();

        when: "display name has been changed, and wizard closed"
        roleWizardPanel.typeDisplayName( NEW_DISPLAY_NAME ).save().close( NEW_DISPLAY_NAME );
        userBrowseFilterPanel.typeSearchText( NEW_DISPLAY_NAME );

        then: "role with new display name should be listed"
        saveScreenshot( "role-d-name-changed" );
        userBrowsePanel.exists( TEST_ROLE.getName() );
    }

    def "GIVEN creating new role WHEN data saved and 'Delete' button on wizard-toolbar pressed THEN wizard closes and role should not be displayed in the grid"()
    {
        given: "creating of new role"
        RoleWizardPanel roleWizardPanel = openRoleWizard();
        Role role = buildRole( "role", "test-role", "description" );
        roleWizardPanel.typeData( role ).save();

        when: "data was saved and 'Delete' button on wizard-toolbar has been pressed"
        ConfirmationDialog confirmationDialog = roleWizardPanel.clickToolbarDelete();
        confirmationDialog.pressYesButton();
        userBrowsePanel.clickOnExpander( UserItemName.ROLES_FOLDER.getValue() );

        then: "wizard closes and role should not be displayed in the grid"
        saveScreenshot( "role-was-deleted-from-wizard" );
        !userBrowsePanel.exists( role.getName() );
    }

    def "GIVEN role wizard is opened, data saved WHEN HomeButton has been pressed THEN new role should be displayed in the grid"()
    {
        given: "role wizard opened, data saved"
        RoleWizardPanel roleWizardPanel = openRoleWizard();
        Role testRole = buildRole( "role", "test-role", "description" );
        roleWizardPanel.typeData( testRole ).save();

        when: "HomeButton has been pressed"
        userBrowsePanel.pressAppHomeButton();

        then: "new role should be displayed in the grid"
        userBrowseFilterPanel.typeSearchText( testRole.getName() );
        saveScreenshot( "role-app-home-clicked" );
        userBrowsePanel.exists( testRole.getName() );
    }
}
