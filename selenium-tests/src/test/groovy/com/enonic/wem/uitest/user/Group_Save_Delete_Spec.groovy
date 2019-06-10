package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.pages.usermanager.browsepanel.UserItemName
import com.enonic.autotests.pages.usermanager.wizardpanel.GroupWizardPanel
import com.enonic.autotests.vo.usermanager.Group
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Group_Save_Delete_Spec
    extends BaseUsersSpec
{
    @Shared
    Group TEST_GROUP;

    @Shared
    String NEW_DISPLAY_NAME = "new display name";

    @Shared
    String NEW_NAME = NameHelper.uniqueName( "group" );

    def "GIVEN adding of a new group WHEN data typed  and 'Save' button pressed  AND page refreshed in the browser THEN wizard shown with a correct data"()
    {
        given: "creating new role "
        GroupWizardPanel groupWizardPanel = openSystemGroupWizard();
        Group refreshGroupRole = buildGroup( "group", "test-wizard-group", "refresh wizard page" );

        when: "data typed and group saved"
        groupWizardPanel.typeData( refreshGroupRole ).save().waitForNotificationMessage();
        userBrowsePanel.refreshPanelInBrowser();
        saveScreenshot( "role_wizard_was_refreshed" );

        then: "wizard is opened"
        groupWizardPanel.isOpened();

        and: "correct display name displayed"
        groupWizardPanel.getNameInputValue() == refreshGroupRole.getName();
    }

    def "GIVEN creating new group in System User Store WHEN saved and wizard closed THEN new group should be listed"()
    {
        given: "creating new Group in System User Store"
        GroupWizardPanel groupWizardPanel = openSystemGroupWizard();
        TEST_GROUP = buildGroup( "group", "test-group", "description" );

        when: " saved and wizard closed"
        String groupCreatingMessage = groupWizardPanel.typeData( TEST_GROUP ).save().waitForNotificationMessage();
        groupWizardPanel.close( TEST_GROUP.getDisplayName() );
        sleep( 500 );
        userBrowsePanel.clickOnExpander( UserItemName.GROUPS_FOLDER.getValue() );

        then: "new Group should be listed"
        saveScreenshot( "group-was-added" );
        userBrowsePanel.exists( TEST_GROUP.getName() );

        and: "correct notification message appears"
        groupCreatingMessage == GROUP_CREATED_MESSAGE;
    }

    def "GIVEN existing group WHEN group opened THEN correct description displayed"()
    {
        given: "existing group"
        userBrowseFilterPanel.typeSearchText( TEST_GROUP.getName() );

        when: "group opened"
        GroupWizardPanel groupWizardPanel = userBrowsePanel.clickCheckboxAndSelectGroup( TEST_GROUP.getName() ).clickToolbarEdit();

        then: "correct description should be displayed"
        groupWizardPanel.getDescription() == TEST_GROUP.getDescription();
    }

    def "GIVEN a existing group  WHEN creating new group with the same name THEN "()
    {
        given: "creating new Group in System User Store"
        GroupWizardPanel groupWizardPanel = openSystemGroupWizard();

        when: "group was saved and wizard closed"
        groupWizardPanel.typeData( TEST_GROUP ).save();
        String message = userBrowsePanel.waitErrorNotificationMessage( Application.EXPLICIT_NORMAL );

        then: "correct notification message should be displayed"
        message == String.format( Application.GROUP_ALREADY_IN_USE_WARNING, TEST_GROUP.getName() );
    }

    def "GIVEN existing group in System User Store WHEN display name was changed THEN group with new display name should be listed"()
    {
        given: "existing group opened"
        userBrowseFilterPanel.typeSearchText( TEST_GROUP.getName() );
        GroupWizardPanel groupWizardPanel = userBrowsePanel.clickCheckboxAndSelectGroup( TEST_GROUP.getName() ).clickToolbarEdit();

        when: "new name typed and saved, and wizard closed"
        groupWizardPanel.typeDisplayName( NEW_DISPLAY_NAME ).save().close( NEW_DISPLAY_NAME );
        userBrowseFilterPanel.typeSearchText( NEW_DISPLAY_NAME );

        then: "group with new display name should be listed"
        saveScreenshot( "d-name-changed" );
        userBrowsePanel.exists( TEST_GROUP.getName() );
    }

    def "GIVEN existing group in System User Store WHEN group was selected and 'Delete' button pressed THEN group should not be displayed in the grid"()
    {
        given: "existing group in System User Store"
        GroupWizardPanel groupWizardPanel = openSystemGroupWizard();
        Group group = buildGroup( "group", "test-group", "description" );
        groupWizardPanel.typeData( group ).save().close( group.getDisplayName() );

        when: "group is selected and 'Delete' button has been pressed"
        userBrowsePanel.doClearSelection();
        userBrowseFilterPanel.typeSearchText( group.getName() );
        userBrowsePanel.clickCheckboxAndSelectGroup( group.getName() ).clickToolbarDelete().doDelete();
        String message = userBrowsePanel.waitForNotificationMessage( );
        saveScreenshot( "group-is-deleted" );

        then: "group should not be displayed in the grid"
        !userBrowsePanel.exists( group.getName() );
        and: "correct notification message should be displayed"
        message == String.format( GROUP_DELETING_NOTIFICATION_MESSAGE, group.getName() );
    }

    //app bug
    @Ignore
    def "GIVEN existing group in System User Store WHEN name was changed THEN group with new name should be listed"()
    {
        given: "existing group is opened"
        userBrowseFilterPanel.typeSearchText( TEST_GROUP.getName() );
        GroupWizardPanel groupWizardPanel = userBrowsePanel.clickCheckboxAndSelectGroup( TEST_GROUP.getName() ).clickToolbarEdit();

        when: "new name typed and saved, and wizard closed"
        groupWizardPanel.typeName( NEW_NAME ).save().close( NEW_DISPLAY_NAME );
        userBrowseFilterPanel.typeSearchText( NEW_NAME );

        then: "group with new display name should be listed"
        saveScreenshot( "name-changed" );
        userBrowsePanel.exists( NEW_NAME );
    }

    def "GIVEN creating new group in System User Store WHEN data saved and 'Delete' button on wizard-toolbar pressed THEN wizard closed and group not displayed in grid"()
    {
        given: " creating new group in System User Store"
        GroupWizardPanel groupWizardPanel = openSystemGroupWizard();
        Group group = buildGroup( "group", "test-group", "description" );
        groupWizardPanel.typeData( group ).save();

        when: "data saved and 'Delete' button on wizard-toolbar pressed"
        ConfirmationDialog confirmationDialog = groupWizardPanel.clickToolbarDelete();
        confirmationDialog.pressYesButton();
        userBrowsePanel.clickOnExpander( UserItemName.GROUP.getValue() );

        then: "wizard closed and group not displayed in grid"
        saveScreenshot( "group-deleted-from-wizard" );
        !userBrowsePanel.exists( group.getName() );
    }

    def "GIVEN group wizard opened, data saved WHEN HomeButton pressed THEN new group displayed in grid"()
    {
        given: "group wizard opened, data saved"
        GroupWizardPanel groupWizardPanel = openSystemGroupWizard();
        Group testGroup = buildGroup( "group", "test-group", "description" );
        groupWizardPanel.typeData( testGroup ).save();

        when: "HomeButton pressed"
        userBrowsePanel.pressAppHomeButton();

        then: "new group displayed in grid"
        userBrowseFilterPanel.typeSearchText( testGroup.getName() );
        saveScreenshot( "app-home-clicked" );
        userBrowsePanel.exists( testGroup.getName() );
    }
}
