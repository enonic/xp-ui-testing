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
    Group group;

    @Shared
    String NEW_DISPLAY_NAME = "new display name";

    @Ignore
    def "GIVEN creating new group in System User Store WHEN saved and wizard closed THEN new group should be listed"()
    {
        given: "creating new Group in System User Store"
        GroupWizardPanel groupWizardPanel = openSystemGroupWizard();
        group = buildGroup( "group", "test-group", "description" );

        when: " saved and wizard closed"
        groupWizardPanel.typeData( group ).save().close( group.getDisplayName() );
        sleep( 500 );
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.GROUP.getValue() );

        then: "new Group should be listed"
        TestUtils.saveScreenshot( getSession(), group.getName() );
        userBrowsePanel.exists( group.getName() );
    }

    def "GIVEN existing group in System User Store WHEN display name changed THEN  group with new display name should be listed"()
    {
        given: "creating new Group in System User Store"
        userBrowseFilterPanel.typeSearchText( group.getName() );
        GroupWizardPanel groupWizardPanel = userBrowsePanel.clickCheckboxAndSelectGroup( group.getName() ).clickToolbarEdit();

        when: " saved and wizard closed"
        groupWizardPanel.typeDisplayName( NEW_DISPLAY_NAME ).save().close( NEW_DISPLAY_NAME );
        userBrowseFilterPanel.typeSearchText( NEW_DISPLAY_NAME );

        then: "group with new display name should be listed"
        TestUtils.saveScreenshot( getSession(), group.getName() );
        userBrowsePanel.exists( group.getName() );
    }

    def "GIVEN existing group in System User Store WHEN group selected and 'Delete' button pressed THEN group should not be listed"()
    {
        given: "creating new Group in System User Store"
        userBrowseFilterPanel.typeSearchText( group.getName() );
        GroupWizardPanel groupWizardPanel = userBrowsePanel.clickCheckboxAndSelectGroup( group.getName() ).clickToolbarEdit();

        when: "saved and wizard closed"
        groupWizardPanel.typeDisplayName( NEW_DISPLAY_NAME ).save().close( NEW_DISPLAY_NAME );
        userBrowseFilterPanel.typeSearchText( NEW_DISPLAY_NAME );

        then: "group with new display name should be listed"
        TestUtils.saveScreenshot( getSession(), group.getName() );
        userBrowsePanel.exists( group.getName() );
    }

    def "GIVEN existing group in System User Store WHEN name changed THEN  group with new  name should be listed"()
    {
        given: "existing group in System User Store"
        userBrowseFilterPanel.typeSearchText( group.getName() );
        GroupWizardPanel groupWizardPanel = userBrowsePanel.clickCheckboxAndSelectGroup( group.getName() ).clickToolbarEdit();

        when: " saved and wizard closed"
        groupWizardPanel.typeDisplayName( NEW_DISPLAY_NAME ).save().close( NEW_DISPLAY_NAME );
        userBrowseFilterPanel.typeSearchText( NEW_DISPLAY_NAME );

        then: "group with new display name should be listed"
        TestUtils.saveScreenshot( getSession(), group.getName() );
        userBrowsePanel.exists( group.getName() );
    }

    def "GIVEN creating new group in System User Store WHEN data saved and 'Delete' button on wizard-toolbar pressed THEN wizard closed and group not displayed in grid"()
    {
        given: " creating new group in System User Store"
        GroupWizardPanel groupWizardPanel = openSystemGroupWizard();
        group = buildGroup( "group", "test-group", "description" );
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

//    def "GIVEN saving of new group, data saved WHEN  HomeButton pressed THEN new group displayed in grid"()
//    {
//        given: "group wizard opened, data saved"
//        GroupWizardPanel groupWizardPanel = openSystemGroupWizard();
//        Group testGroup = buildGroup( "group", "test-group", "description" );
//        groupWizardPanel.typeData( testGroup ).save().close( testGroup.getDisplayName() );
//
//        when: "HomeButton pressed"
//        userBrowseFilterPanel.typeSearchText( testGroup.getName() );
//        groupWizardPanel = userBrowsePanel.clickCheckboxAndSelectGroup( testGroup.getName() );
//
//        then: "new group displayed in grid"
//        groupWizardPanel.getDescription() == testGroup.getDescription();
//        and: ""
//        groupWizardPanel.getMembers() =
//    }
}
