package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.AccountStatisticsPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.GroupWizardPanel
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.usermanager.Group
import com.enonic.autotests.vo.usermanager.RoleDisplayName
import com.enonic.autotests.vo.usermanager.RoleName
import com.enonic.autotests.vo.usermanager.User
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class UserBrowsePanel_AccountStatisticPanel_Spec
    extends BaseUsersSpec
{
    @Shared
    AccountStatisticsPanel accountStatisticsPanel;

    @Shared
    User TEST_USER

    @Shared
    String TEST_ROLE_NAME = RoleName.CM_APP.getValue();

    @Shared
    Group TEST_GROUP;

    def "setup: add a test group"()
    {
        when:
        GroupWizardPanel groupWizardPanel = openSystemGroupWizard();
        TEST_GROUP = buildGroup( "gr-statistics", "st-panel", "for testing of statistic panel" );
        groupWizardPanel.typeData( TEST_GROUP ).save().close( TEST_GROUP.getDisplayName() );

        then: "test group listed"
        userBrowseFilterPanel.typeSearchText( TEST_GROUP.getName() );
        userBrowsePanel.exists( TEST_GROUP.getName() );
    }

    def "WHEN 'super user' selected THEN correct info have been shown "()
    {
        when:
        userBrowseFilterPanel.typeSearchText( "su" );
        userBrowsePanel.clickCheckboxAndSelectUser( "users/su" );
        TestUtils.saveScreenshot( getSession(), "system-su-statistic-panel" );
        accountStatisticsPanel = new AccountStatisticsPanel( getSession() );
        List<String> roleDisplayNamesActual = accountStatisticsPanel.getRoleDisplayNames();

        then: "correct display name shown"
        accountStatisticsPanel.getItemDisplayName() == "Super User";

        and: "correct email displayed"
        accountStatisticsPanel.getEmail() == "";

        and: "Administration Console Login role displayed"
        roleDisplayNamesActual.contains( RoleDisplayName.ADMIN_CONSOLE.getValue() );

        and: "Administrator(system.admin) role displayed"
        roleDisplayNamesActual.contains( RoleDisplayName.SYSTEM_ADMIN.getValue() );
    }

    def "GIVEN a existing user with a role WHEN this user selected THEN correct user-info displayed"()
    {
        given: "a existing user with a role"
        List<String> rolesExpected = new ArrayList<>();
        rolesExpected.add( TEST_ROLE_NAME );

        List<String> groupExpected = new ArrayList<>();
        groupExpected.add( TEST_GROUP.getName() );
        TEST_USER = buildUserWithRolesAndGroups( "user", "password", generateEmail( "test-user" ), rolesExpected, groupExpected );
        addUser( TEST_USER );
        userBrowsePanel.clickOnClearSelection();

        when: "user selected"
        userBrowseFilterPanel.typeSearchText( TEST_USER.getDisplayName() );
        userBrowsePanel.clickCheckboxAndSelectUser( TEST_USER.getDisplayName() );
        TestUtils.saveScreenshot( getSession(), "user-statistic-panel" );
        List<String> roleNamesActual = accountStatisticsPanel.getRoleNames();
        List<String> groupsNamesActual = accountStatisticsPanel.getGroupNames();

        then: "correct display name is shown in the statistics panel"
        accountStatisticsPanel.getItemDisplayName() == TEST_USER.getDisplayName();

        and: "correct e-mail displayed"
        accountStatisticsPanel.getEmail() == TEST_USER.getEmail();

        and: "one role, that was added is shown"
        roleNamesActual.size() == 1;

        and: "correct role's display-name shown in the statistics panel"
        roleNamesActual.get( 0 ).contains( rolesExpected.get( 0 ) );

        and: "correct group-name displayed as well"
        groupsNamesActual.get( 0 ).contains( groupExpected.get( 0 ) );
    }

    def "GIVEN a existing user with a role WHEN user opened AND role removed AND user saved AND wizard closed THEN removed role not present in the selections panel"()
    {
        given: "a existing user with a role"
        userBrowseFilterPanel.typeSearchText( TEST_USER.getName() );
        UserWizardPanel wizardPanel = userBrowsePanel.clickCheckboxAndSelectUser( TEST_USER.getName() ).clickToolbarEdit();

        when: "role was removed AND user saved AND wizard closed"
        wizardPanel.removeRoleByName( TEST_ROLE_NAME ).save().close( TEST_USER.getDisplayName() );

        then: "removed role not displayed in the selections panel"
        accountStatisticsPanel.getRoleNames().size() == 0;
    }

    def "WHEN 'Users' folder selected THEN correct info shown in statistics panel"()
    {
        when: "Users folder selected"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        userBrowsePanel.clickCheckboxAndSelectFolder( UserBrowsePanel.BrowseItemType.USERS_FOLDER );
        TestUtils.saveScreenshot( getSession(), "users-statistic-panel" );

        then: "correct info shown in statistics panel"
        accountStatisticsPanel.getItemDisplayName() == "Users";
    }
}
