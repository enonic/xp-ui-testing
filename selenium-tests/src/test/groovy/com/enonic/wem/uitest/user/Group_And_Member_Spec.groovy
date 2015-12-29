package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.GroupWizardPanel
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
import com.enonic.autotests.utils.TestUtils
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


    def "WHEN system group saved THEN new group present in a System Store "()
    {
        when: "group saved and wizard closed"
        GroupWizardPanel groupWizardPanel = openSystemGroupWizard();
        TEST_GROUP = buildGroup( "group", "group-for-user", "description" );
        groupWizardPanel.typeData( TEST_GROUP ).save().close( TEST_GROUP.getDisplayName() );
        sleep( 500 );
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.GROUP.getValue() );

        then: "new Group should be listed"
        TestUtils.saveScreenshot( getSession(), "group-for-adding-user" );
        userBrowsePanel.exists( TEST_GROUP.getName() );
    }

    def "WHEN user with role saved THEN user is searchable and displayed in a grid"()
    {
        given: "start adding a new user"
        List<String> groups = new ArrayList<>();
        groups.add( TEST_GROUP.getName() );
        TEST_USER = buildUserWithRolesAndGroups( "user", "password", null, groups );
        UserWizardPanel userWizardPanel = openSystemUserWizard();

        when: "data typed and user saved"
        userWizardPanel.typeData( TEST_USER ).save().close( TEST_USER.getDisplayName() );

        then: "new user present beneath a store"
        userBrowseFilterPanel.typeSearchText( TEST_USER.getDisplayName() );
        userBrowsePanel.exists( TEST_USER.getDisplayName() );
    }

    def "WHEN user was added to a group and this group opened THEN correct display name of user is shown on members-form"()
    {
        when: "user was added to a group and this group opened"
        userBrowseFilterPanel.typeSearchText( TEST_GROUP.getName() );
        GroupWizardPanel groupWizardPanel = userBrowsePanel.clickCheckboxAndSelectGroup( TEST_GROUP.getName() ).clickToolbarEdit();
        TestUtils.saveScreenshot( getSession(), "group-with-user" );

        then: "correct display name of user is shown on members-form"
        List<String> members = groupWizardPanel.getMembersDisplayNames();
        members.get( 0 ) == TEST_USER.getDisplayName();
    }

    def "GIVEN a group with a member WHEN this group opened AND member was removed AND group saved THEN member not displayed in form"()
    {
        given: "a group with a member"
        userBrowseFilterPanel.typeSearchText( TEST_GROUP.getName() );
        GroupWizardPanel groupWizardPanel = userBrowsePanel.clickCheckboxAndSelectGroup( TEST_GROUP.getName() ).clickToolbarEdit();
        TestUtils.saveScreenshot( getSession(), "group-with-member" );

        when: "member was removed AND group saved"
        groupWizardPanel.removeMember( TEST_USER.getDisplayName() ).save();

        then: "member not displayed in form"
        List<String> members = groupWizardPanel.getMembersDisplayNames();
        TestUtils.saveScreenshot( getSession(), "member-removed" );
        members.size() == 0;
    }


}
