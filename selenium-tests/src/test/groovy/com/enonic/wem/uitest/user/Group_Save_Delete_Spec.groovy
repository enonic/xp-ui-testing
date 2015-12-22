package com.enonic.wem.uitest.user

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

    def "GIVEN creating new group in System User Store WHEN saved and wizard closed THEN new group should be listed"()
    {
        given: "creating new Group in System User Store"
        GroupWizardPanel groupWizardPanel = openSystemGroupWizard();
        TEST_GROUP = buildGroup( "group", "test-group", "description" );

        when: " saved and wizard closed"
        groupWizardPanel.typeData( TEST_GROUP ).save().close( TEST_GROUP.getDisplayName() );
        sleep( 500 );
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.GROUP.getValue() );

        then: "new Group should be listed"
        TestUtils.saveScreenshot( getSession(), TEST_GROUP.getName() );
        userBrowsePanel.exists( TEST_GROUP.getName() );
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
        TestUtils.saveScreenshot( getSession(), TEST_GROUP.getName() );
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
        TestUtils.saveScreenshot( getSession(), "group-deleted" );

        then: "group not displayed in grid"
        TestUtils.saveScreenshot( getSession(), group.getName() );
        !userBrowsePanel.exists( group.getName() );
    }

    @Ignore
    def "GIVEN existing group in System User Store WHEN name changed THEN  group with new  name should be listed"()
    {
        given: "existing group opened"
        userBrowseFilterPanel.typeSearchText( TEST_GROUP.getName() );
        GroupWizardPanel groupWizardPanel = userBrowsePanel.clickCheckboxAndSelectGroup( TEST_GROUP.getName() ).clickToolbarEdit();

        when: "new name typed and saved, and wizard closed"
        groupWizardPanel.typeName( NEW_NAME ).save().close( NEW_DISPLAY_NAME );
        userBrowseFilterPanel.typeSearchText( NEW_NAME );

        then: "group with new display name should be listed"
        TestUtils.saveScreenshot( getSession(), NEW_NAME );
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
        TestUtils.saveScreenshot( getSession(), group.getName() );
        !userBrowsePanel.exists( group.getName() );
    }

    def "GIVEN group wizard opened, data saved WHEN HomeButton pressed THEN new group displayed in grid"()
    {
        given: "group wizard opened, data saved"
        GroupWizardPanel groupWizardPanel = openSystemGroupWizard();
        Group testGroup = buildGroup( "group", "test-group", "description" );
        groupWizardPanel.typeData( testGroup ).save();

        when: "HomeButton pressed"
        userBrowsePanel.goToAppHome();

        then: "new group displayed in grid"
        userBrowseFilterPanel.typeSearchText( testGroup.getName() );
        userBrowsePanel.exists( testGroup.getName() );
    }
}
