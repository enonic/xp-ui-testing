package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.GroupStatisticsPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.GroupWizardPanel
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
import com.enonic.autotests.vo.usermanager.Group
import com.enonic.autotests.vo.usermanager.User
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Group_And_Member_Spec
    extends BaseUsersSpec
{
    @Shared
    Group TEST_GROUP;

    @Shared
    User TEST_USER;


    def "WHEN system group has been added THEN new group is listed in a System User Store "()
    {
        when: "new group added and wizard was closed"
        GroupWizardPanel groupWizardPanel = openSystemGroupWizard();
        TEST_GROUP = buildGroup( "group", "group-for-user", "description" );
        groupWizardPanel.typeData( TEST_GROUP ).save().close( TEST_GROUP.getDisplayName() );
        sleep( 500 );
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.GROUP.getValue() );

        then: "new Group should be listed in the grid"
        saveScreenshot( "group-for-adding-user" );
        userBrowsePanel.exists( TEST_GROUP.getName() );
    }

    def "WHEN 'User'-wizard is opened WHEN the data was typed AND wizard was closed THEN user is searchable and it displayed in a grid"()
    {
        given: "start adding a new user"
        List<String> groups = new ArrayList<>();
        groups.add( TEST_GROUP.getName() );
        TEST_USER = buildUserWithRolesAndGroups( "user", "password", null, groups );
        UserWizardPanel userWizardPanel = openSystemUserWizard();

        when: "data typed and user saved and the wizard was closed"
        userWizardPanel.typeData( TEST_USER ).save().close( TEST_USER.getDisplayName() );

        then: "new user listed beneath the system user store"
        userBrowseFilterPanel.typeSearchText( TEST_USER.getDisplayName() );
        userBrowsePanel.exists( TEST_USER.getDisplayName() );
    }

    def "WHEN existing user was added to a group and this group is opened THEN correct display name of user is shown on members-form"()
    {
        when: "the group has been selected and opened"
        userBrowseFilterPanel.typeSearchText( TEST_GROUP.getName() );
        GroupWizardPanel groupWizardPanel = userBrowsePanel.clickCheckboxAndSelectGroup( TEST_GROUP.getName() ).clickToolbarEdit();
        saveScreenshot( "group-with-user" );

        then: "correct display name of user is displayed on the  members-form"
        List<String> members = groupWizardPanel.getMembersDisplayNames();
        members.get( 0 ) == TEST_USER.getDisplayName();
    }

    def "GIVEN existing group with a user WHEN group is selected in browse panel THEN correct member displayed in statistics panel "()
    {
        given: "existing group with a user"
        userBrowseFilterPanel.typeSearchText( TEST_GROUP.getName() );

        when: "group is selected in browse panel"
        userBrowsePanel.clickCheckboxAndSelectGroup( TEST_GROUP.getName() );
        GroupStatisticsPanel groupStatisticsPanel = new GroupStatisticsPanel( getSession() );

        then: "correct member displayed in statistics panel "
        groupStatisticsPanel.getMemberDisplayNames().contains( TEST_USER.getDisplayName() );

    }

    def "GIVEN existing a group with a member WHEN this group opened AND member was removed AND group saved THEN member not displayed in form"()
    {
        given: "existing a group with a member"
        userBrowseFilterPanel.typeSearchText( TEST_GROUP.getName() );
        GroupWizardPanel groupWizardPanel = userBrowsePanel.clickCheckboxAndSelectGroup( TEST_GROUP.getName() ).clickToolbarEdit();
        saveScreenshot( "group-with-member" );

        when: "member was removed AND group saved"
        groupWizardPanel.removeMember( TEST_USER.getDisplayName() ).save();

        then: "member not displayed in the form"
        List<String> members = groupWizardPanel.getMembersDisplayNames();
        saveScreenshot( "member-removed" );
        members.size() == 0;
    }


}
