package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.GroupWizardPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.usermanager.Group
import spock.lang.Ignore
import spock.lang.Shared

class Group_Save_Delete_Spec
    extends BaseUsersSpec
{
    @Shared
    Group TEST_GROUP;

    @Shared
    String NEW_DISPLAY_NAME = "new display name";


    @Shared
    String NEW_NAME = "new_name";

    def "GIVEN adding of a new group WHEN data typed  and 'Save' button pressed  AND page refreshed in the browser THEN wizard shown with a correct data"()
    {
        given: "creating new role "
        GroupWizardPanel groupWizardPanel = openSystemGroupWizard();
        Group refreshGroupRole = buildGroup( "group", "test-wizard-group", "refresh wizard page" );

        when: "data typed and group saved"
        groupWizardPanel.typeData( refreshGroupRole ).save().waitNotificationMessage();
        userBrowsePanel.refreshPanelInBrowser();
        TestUtils.saveScreenshot( getSession(), "role_wizard_refreshed" );

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
        String groupCreatingMessage = groupWizardPanel.typeData( TEST_GROUP ).save().waitNotificationMessage();
        groupWizardPanel.close( TEST_GROUP.getDisplayName() );
        sleep( 500 );
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.GROUPS_FOLDER.getValue() );

        then: "new Group should be listed"
        TestUtils.saveScreenshot( getSession(), "group-added" );
        userBrowsePanel.exists( TEST_GROUP.getName() );

        and: "correct notification message appears"
        groupCreatingMessage == GROUP_CREATED_MESSAGE;
    }
    //app bug
    @Ignore
    def "GIVEN a existing group  WHEN creating new group with the same name THEN correct notification message appears"()
    {
        given: "creating new Group in System User Store"
        GroupWizardPanel groupWizardPanel = openSystemGroupWizard();

        when: " saved and wizard closed"
        String message = groupWizardPanel.typeData( TEST_GROUP ).save().waitErrorNotificationMessage( Application.EXPLICIT_NORMAL );

        then: "message that group with it  name already exists"
        message == String.format( Application.GROUP_ALREADY_IN_USE_WARNING, TEST_GROUP.getName() );
    }

    def "GIVEN existing group in System User Store WHEN display name changed THEN  group with new display name should be listed"()
    {
        given: "existing group opened"
        userBrowseFilterPanel.typeSearchText( TEST_GROUP.getName() );
        GroupWizardPanel groupWizardPanel = userBrowsePanel.clickCheckboxAndSelectGroup( TEST_GROUP.getName() ).clickToolbarEdit();

        when: "new name typed and saved, and wizard closed"
        groupWizardPanel.typeDisplayName( NEW_DISPLAY_NAME ).save().close( NEW_DISPLAY_NAME );
        userBrowseFilterPanel.typeSearchText( NEW_DISPLAY_NAME );

        then: "group with new display name should be listed"
        TestUtils.saveScreenshot( getSession(), "d-name-changed" );
        userBrowsePanel.exists( TEST_GROUP.getName() );
    }

    def "GIVEN existing group in System User Store WHEN group selected and 'Delete' button pressed THEN group not displayed in grid"()
    {
        given: " existing group in System User Store"
        GroupWizardPanel groupWizardPanel = openSystemGroupWizard();
        Group group = buildGroup( "group", "test-group", "description" );
        groupWizardPanel.typeData( group ).save().close( group.getDisplayName() );

        when: "group selected and 'Delete' button pressed"
        userBrowsePanel.clickOnClearSelection();
        userBrowseFilterPanel.typeSearchText( group.getName() );
        userBrowsePanel.clickCheckboxAndSelectGroup( group.getName() ).clickToolbarDelete().doDelete();
        String message = userBrowsePanel.waitNotificationMessage( Application.EXPLICIT_NORMAL );
        TestUtils.saveScreenshot( getSession(), "group-deleted" );

        then: "group not displayed in grid"
        TestUtils.saveScreenshot( getSession(), group.getName() );
        !userBrowsePanel.exists( group.getName() );
        and: "correct notification message appears"
        message == String.format( GROUP_DELETING_NOTIFICATION_MESSAGE, group.getName() );
    }

    //app bug
    @Ignore
    def "GIVEN existing group in System User Store WHEN name changed THEN group with new name should be listed"()
    {
        given: "existing group opened"
        userBrowseFilterPanel.typeSearchText( TEST_GROUP.getName() );
        GroupWizardPanel groupWizardPanel = userBrowsePanel.clickCheckboxAndSelectGroup( TEST_GROUP.getName() ).clickToolbarEdit();

        when: "new name typed and saved, and wizard closed"
        groupWizardPanel.typeName( NEW_NAME ).save().close( NEW_DISPLAY_NAME );
        userBrowseFilterPanel.typeSearchText( NEW_NAME );

        then: "group with new display name should be listed"
        TestUtils.saveScreenshot( getSession(), "name-changed" );
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
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.GROUP.getValue() );

        then: "wizard closed and group not displayed in grid"
        TestUtils.saveScreenshot( getSession(), "group-deleted-from-wizard" );
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
        TestUtils.saveScreenshot( getSession(), "app-home-clicked" );
        userBrowsePanel.exists( testGroup.getName() );
    }
}
