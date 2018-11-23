package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.usermanager.browsepanel.AccountStatisticsPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserItemName
import com.enonic.autotests.pages.usermanager.wizardpanel.GroupWizardPanel
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
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

        then: "test group should be listed"
        userBrowseFilterPanel.typeSearchText( TEST_GROUP.getName() );
        userBrowsePanel.exists( TEST_GROUP.getName() );
    }

    def "WHEN 'super user' is selected THEN correct info should be displayed on the statistic panel "()
    {
        when: "'super user' is selected"
        userBrowseFilterPanel.typeSearchText( "su" );
        userBrowsePanel.clickCheckboxAndSelectUser( "users/su" );
        saveScreenshot( "system-su-statistic-panel" );
        accountStatisticsPanel = new AccountStatisticsPanel( getSession() );
        List<String> roleDisplayNamesActual = accountStatisticsPanel.getRoleDisplayNames();

        then: "correct display name should be displayed"
        accountStatisticsPanel.getItemDisplayName() == "Super User";

        and: "correct email should be displayed"
        accountStatisticsPanel.getEmail() == "";

        //and: "'Administration Console Login' role should be displayed"
        //roleDisplayNamesActual.contains( RoleDisplayName.ADMIN_CONSOLE.getValue() );

        and: "'Administrator(system.admin)' role should be displayed"
        roleDisplayNamesActual.contains( RoleDisplayName.SYSTEM_ADMIN.getValue() );
    }

    def "GIVEN a existing user with a role WHEN this user is selected THEN correct user-info displayed"()
    {
        given: "existing user with a role"
        List<String> rolesExpected = new ArrayList<>();
        rolesExpected.add( TEST_ROLE_NAME );

        List<String> groupExpected = new ArrayList<>();
        groupExpected.add( TEST_GROUP.getName() );
        TEST_USER = buildUserWithRolesAndGroups( "user", "password", generateEmail( "test-user" ), rolesExpected, groupExpected );
        addUser( TEST_USER );
        userBrowsePanel.doClearSelection();

        when: "user is selected"
        userBrowseFilterPanel.typeSearchText( TEST_USER.getDisplayName() );
        userBrowsePanel.clickCheckboxAndSelectUser( TEST_USER.getDisplayName() );
        saveScreenshot( "user-statistic-panel" );
        List<String> roleNamesActual = accountStatisticsPanel.getRoleNames();
        List<String> groupsNamesActual = accountStatisticsPanel.getGroupNames();

        then: "correct display name should be shown on the statistics panel"
        accountStatisticsPanel.getItemDisplayName() == TEST_USER.getDisplayName();

        and: "correct e-mail should be displayed"
        accountStatisticsPanel.getEmail() == TEST_USER.getEmail();

        and: "one role should be shown"
        roleNamesActual.size() == 1;

        and: "correct role's display-name shown in the statistics panel"
        roleNamesActual.get( 0 ).contains( rolesExpected.get( 0 ) );

        and: "correct group-name should be displayed"
        groupsNamesActual.get( 0 ).contains( groupExpected.get( 0 ) );
    }

    def "GIVEN existing user with a role WHEN user is opened AND role was removed AND user saved AND wizard closed THEN removed role should not be present on the selections panel"()
    {
        given: "existing user with a role"
        userBrowseFilterPanel.typeSearchText( TEST_USER.getName() );
        UserWizardPanel wizardPanel = userBrowsePanel.clickCheckboxAndSelectUser( TEST_USER.getName() ).clickToolbarEdit();
        wizardPanel.waitUntilWizardOpened();
        wizardPanel.clickOnRolesAndGroupsTabLink();
        sleep( 1000 );

        when: "role has been removed AND user saved AND wizard closed"
        wizardPanel.removeRoleByName( TEST_ROLE_NAME ).save().close( TEST_USER.getDisplayName() );
        userBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        saveScreenshot( "user_role_removed_stat_panel" );

        then: "removed role should not be displayed on the selections panel"
        accountStatisticsPanel.getRoleNames().size() == 0;
    }

    def "WHEN 'Users' folder is selected THEN correct info should be shown on the statistics panel"()
    {
        when: "'Users' folder is selected"
        userBrowsePanel.clickOnExpander( UserItemName.SYSTEM.getValue() );
        userBrowsePanel.clickCheckboxAndSelectFolder( UserItemName.USERS_FOLDER );
        saveScreenshot( "users-folder-selected" );

        then: "correct info shown should be displayed on the statistics panel"
        accountStatisticsPanel.getItemDisplayName() == "Users";
    }
}
