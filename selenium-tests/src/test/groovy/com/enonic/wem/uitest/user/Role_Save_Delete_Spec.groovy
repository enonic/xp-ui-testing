package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.RoleWizardPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.usermanager.Role
import spock.lang.Ignore
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
        roleWizardPanel.typeData( refreshWizardRole ).save().waitNotificationMessage();
        userBrowsePanel.refreshPanelInBrowser();
        TestUtils.saveScreenshot( getSession(), "role_wizard_refreshed" );

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
        String roleCreatingMessage = roleWizardPanel.typeData( TEST_ROLE ).save().waitNotificationMessage();
        roleWizardPanel.close( TEST_ROLE.getDisplayName() );
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.ROLES_FOLDER.getValue() );
        userBrowseFilterPanel.typeSearchText( TEST_ROLE.getName() );

        then: "new role should be listed"
        TestUtils.saveScreenshot( getSession(), "role-added" );
        userBrowsePanel.exists( TEST_ROLE.getName() );

        and: "correct notification message appears"
        roleCreatingMessage == ROLE_CREATED_MESSAGE;
    }

    def "GIVEN existing role WHEN role opened THEN correct description displayed"()
    {
        given: "existing role"
        userBrowseFilterPanel.typeSearchText( TEST_ROLE.getName() );

        when: "role opened"
        RoleWizardPanel roleWizardPanel = userBrowsePanel.clickCheckboxAndSelectRole( TEST_ROLE.getName() ).clickToolbarEdit();

        then: "correct description displayed"
        TestUtils.saveScreenshot( getSession(), "check-role-description" );
        roleWizardPanel.getDescription() == TEST_ROLE.getDescription();
    }
    //app bug    INBOX-279
    @Ignore
    def "GIVEN a existing role WHEN creating new role with the same name THEN correct notification message appears"()
    {
        given: "creating new role"
        RoleWizardPanel roleWizardPanel = openRoleWizard();

        when: "role saved and wizard closed"
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

        when: "role selected and 'Delete' button pressed"
        userBrowsePanel.clickOnClearSelection();
        userBrowseFilterPanel.typeSearchText( role.getName() );
        userBrowsePanel.clickCheckboxAndSelectRole( role.getName() ).clickToolbarDelete().doDelete();
        String message = userBrowsePanel.waitNotificationMessage( Application.EXPLICIT_NORMAL );
        TestUtils.saveScreenshot( getSession(), "role-deleted" );

        then: "role not displayed in a grid"
        TestUtils.saveScreenshot( getSession(), role.getName() );
        !userBrowsePanel.exists( role.getName() );
        and: "correct notification message appears"
        message == String.format( ROLE_DELETED_MESSAGE, role.getName() );
    }

    def "GIVEN existing role WHEN display name changed THEN role with new display name should be listed"()
    {
        given: "existing role opened"
        userBrowseFilterPanel.typeSearchText( TEST_ROLE.getName() );
        RoleWizardPanel roleWizardPanel = userBrowsePanel.clickCheckboxAndSelectRole( TEST_ROLE.getName() ).clickToolbarEdit();

        when: "new name typed and saved, and wizard closed"
        roleWizardPanel.typeDisplayName( NEW_DISPLAY_NAME ).save().close( NEW_DISPLAY_NAME );
        userBrowseFilterPanel.typeSearchText( NEW_DISPLAY_NAME );

        then: "role with new display name should be listed"
        TestUtils.saveScreenshot( getSession(), "role-d-name-changed" );
        userBrowsePanel.exists( TEST_ROLE.getName() );
    }

    //app bug
    @Ignore
    def "GIVEN existing role WHEN name changed THEN role with new name should be listed"()
    {
        given: "existing role opened"
        userBrowseFilterPanel.typeSearchText( TEST_ROLE.getName() );
        RoleWizardPanel roleWizardPanel = userBrowsePanel.clickCheckboxAndSelectRole( TEST_ROLE.getName() ).clickToolbarEdit();

        when: "new name typed and saved, and wizard closed"
        roleWizardPanel.typeName( NEW_NAME ).save().close( NEW_DISPLAY_NAME );
        userBrowseFilterPanel.typeSearchText( NEW_NAME );

        then: "role with new name should be listed"
        TestUtils.saveScreenshot( getSession(), "name-changed" );
        userBrowsePanel.exists( NEW_NAME );
    }

    def "GIVEN creating new role WHEN data saved and 'Delete' button on wizard-toolbar pressed THEN wizard closed and role not displayed in grid"()
    {
        given: "creating new role"
        RoleWizardPanel roleWizardPanel = openRoleWizard();
        Role role = buildRole( "role", "test-role", "description" );
        roleWizardPanel.typeData( role ).save();

        when: "data saved and 'Delete' button on wizard-toolbar pressed"
        ConfirmationDialog confirmationDialog = roleWizardPanel.clickToolbarDelete();
        confirmationDialog.pressYesButton();
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.ROLES_FOLDER.getValue() );

        then: "wizard closed and role not displayed in grid"
        TestUtils.saveScreenshot( getSession(), "role-deleted-from-wizard" );
        !userBrowsePanel.exists( role.getName() );
    }

    def "GIVEN role wizard opened, data saved WHEN HomeButton pressed THEN new role displayed in grid"()
    {
        given: "role wizard opened, data saved"
        RoleWizardPanel roleWizardPanel = openRoleWizard();
        Role testRole = buildRole( "role", "test-role", "description" );
        roleWizardPanel.typeData( testRole ).save();

        when: "HomeButton pressed"
        userBrowsePanel.pressAppHomeButton();

        then: "new role displayed in grid"
        userBrowseFilterPanel.typeSearchText( testRole.getName() );
        TestUtils.saveScreenshot( getSession(), "role-app-home-clicked" );
        userBrowsePanel.exists( testRole.getName() );
    }
}
