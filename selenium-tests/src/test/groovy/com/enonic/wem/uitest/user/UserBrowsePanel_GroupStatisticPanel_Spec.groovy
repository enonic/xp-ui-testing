package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.GroupStatisticsPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.GroupWizardPanel
import com.enonic.autotests.vo.usermanager.Group
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Tasks
 * enonic/xp-ui-testing#23  Add selenium tests for displaying of members on the UserItemStatisticsPanel*/
@Stepwise
class UserBrowsePanel_GroupStatisticPanel_Spec
    extends BaseUsersSpec
{
    @Shared
    GroupStatisticsPanel groupStatisticsPanel;

    @Shared
    Group TEST_GROUP;

    @Shared
    String NEW_DISPLAY_NAME = "new display name";

    def "WHEN 'Groups' folder is selected THEN correct info should be displayed on the statistics panel"()
    {
        when: "'Groups' folder is selected"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.UserItemName.SYSTEM.getValue() );
        userBrowsePanel.clickCheckboxAndSelectFolder( UserBrowsePanel.UserItemName.GROUPS_FOLDER );
        saveScreenshot( "groups-folder-selected" );
        groupStatisticsPanel = new GroupStatisticsPanel( getSession() );

        then: "correct info should be displayed on the statistics panel"
        groupStatisticsPanel.getItemDisplayName() == "Groups";
    }

    def "GIVEN new group has been added(no members) WHEN the group is selected THEN empty members should be displayed on the statistics panel"()
    {
        given: "new group has been added(no members)"
        GroupWizardPanel groupWizardPanel = openSystemGroupWizard();
        TEST_GROUP = buildGroup( "group", "empty-group-statistics", "description" );
        groupWizardPanel.typeData( TEST_GROUP ).save().close( TEST_GROUP.getDisplayName() );
        sleep( 500 );
        userBrowsePanel.clickOnExpander( UserBrowsePanel.UserItemName.GROUP.getValue() );
        userBrowsePanel.doClearSelection();

        when: "the group is selected in the grid"
        userBrowsePanel.clickCheckboxAndSelectGroup( TEST_GROUP.getName() );
        saveScreenshot( "empty-group-selected" );

        then: "empty members should be displayed in the statistics panel"
        groupStatisticsPanel.getMemberDisplayNames().size() == 0;

        and: "correct display name of group is shown"
        groupStatisticsPanel.getItemDisplayName() == TEST_GROUP.getDisplayName();
    }

    def "GIVEN existing group is opened WHEN display name of the group is changed THEN group with new display name should be displayed on the statistics panel"()
    {
        given: "existing group is opened"
        userBrowseFilterPanel.typeSearchText( TEST_GROUP.getName() );
        GroupWizardPanel groupWizardPanel = userBrowsePanel.clickCheckboxAndSelectGroup( TEST_GROUP.getName() ).clickToolbarEdit();

        when: "display name of the group is changed"
        groupWizardPanel.typeDisplayName( NEW_DISPLAY_NAME ).save().close( NEW_DISPLAY_NAME );
        saveScreenshot( "new-display-name-group" );

        then: "group with new display name should be displayed"
        groupStatisticsPanel.getItemDisplayName() == NEW_DISPLAY_NAME;
    }

    //enonic/xp-ui-testing#23 Add selenium tests for displaying of members on the UserItemStatisticsPanel
    def "GIVEN new group with a member has been added WHEN the group is selected THEN one member should be displayed on the statistics panel"()
    {
        given: "new group has been added"
        GroupWizardPanel groupWizardPanel = openSystemGroupWizard();
        List<String> memberDisplayNames = new ArrayList<>();
        memberDisplayNames.add( SUPER_USER_DISPLAY_NAME );
        Group group = buildGroupWithMembers( "group", "group with members", "description", memberDisplayNames );
        groupWizardPanel.typeData( group ).save().close( group.getDisplayName() );
        sleep( 500 );
        and: "'Group' folder is expanded"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.UserItemName.GROUP.getValue() );
        userBrowsePanel.doClearSelection();

        when: "the group is selected"
        userBrowsePanel.clickCheckboxAndSelectGroup( group.getName() );
        saveScreenshot( "group-with-member-selected" );

        then: "one member should be displayed on the statistics panel"
        groupStatisticsPanel.getMemberDisplayNames().size() == 1;

        and: "'super user' should be displayed on the statistics panel"
        groupStatisticsPanel.getMemberDisplayNames().contains( SUPER_USER_DISPLAY_NAME );
    }
}
