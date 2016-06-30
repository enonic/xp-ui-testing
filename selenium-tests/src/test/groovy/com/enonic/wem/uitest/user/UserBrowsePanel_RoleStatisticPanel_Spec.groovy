package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.RoleStatisticsPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.RoleWizardPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.usermanager.Role
import com.enonic.autotests.vo.usermanager.RoleName
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class UserBrowsePanel_RoleStatisticPanel_Spec
    extends BaseUsersSpec
{
    @Shared
    RoleStatisticsPanel roleStatisticsPanel;

    @Shared
    String ADMINISTRATOR_CONSOLE_LOGIN_ROLE_NAME = RoleName.ADMIN_CONSOLE.getValue();

    @Shared
    String SYSTEM_EVERYONE_NAME = RoleName.SYSTEM_EVERYONE.getValue();

    @Shared
    Role ROLE_WITH_MEMBER;

    def "WHEN 'Roles' folder selected THEN correct info shown in statistics panel"()
    {
        when: "Roles folder selected"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        userBrowsePanel.clickCheckboxAndSelectFolder( UserBrowsePanel.BrowseItemType.ROLES_FOLDER );
        TestUtils.saveScreenshot( getSession(), "roles-statistic-panel" );
        roleStatisticsPanel = new RoleStatisticsPanel( getSession() );

        then: "correct info shown in statistics panel"
        roleStatisticsPanel.getItemDisplayName() == ROLES_FOLDER;
    }

    def "WHEN 'administrator console login' role selected THEN correct members and header are displayed in a statistics panel"()
    {
        when: "the 'administrator console login' role selected in a grid"
        userBrowseFilterPanel.typeSearchText( ADMINISTRATOR_CONSOLE_LOGIN_ROLE_NAME );
        userBrowsePanel.clickCheckboxAndSelectGroup( ADMINISTRATOR_CONSOLE_LOGIN_ROLE_NAME );
        TestUtils.saveScreenshot( getSession(), "adm-console-login-selected" );
        List<String> members = roleStatisticsPanel.getMemberDisplayNames();
        TestUtils.saveScreenshot( getSession(), "su-selected" );

        then: "'Super User' is member of the role"
        members.contains( SUPER_USER_DISPLAY_NAME );

        and: "correct display name of role is shown"
        roleStatisticsPanel.getItemDisplayName() == ADMIN_CONSOLE_LOGIN_ROLE_DISPLAY_NAME;
    }

    def "WHEN system 'everyone' selected THEN correct members displayed in a statistics panel"()
    {
        when: "system 'everyone' selected"
        userBrowseFilterPanel.typeSearchText( SYSTEM_EVERYONE_NAME );
        userBrowsePanel.clickCheckboxAndSelectGroup( SYSTEM_EVERYONE_NAME );
        TestUtils.saveScreenshot( getSession(), "everyone-selected" );
        List<String> members = roleStatisticsPanel.getMemberDisplayNames();
        TestUtils.saveScreenshot( getSession(), "everyone-selected" );

        then: "members is empty"
        members.isEmpty();

        and: "correct display name of role is shown"
        roleStatisticsPanel.getItemDisplayName() == EVERYONE_ROLE_DISPLAY_NAME;
    }

    def "GIVEN role that has members WHEN the role selected in a browse panel THEN correct member shown in a role statistics panel"()
    {
        given: "new role created"
        RoleWizardPanel roleWizardPanel = openRoleWizard();
        List<String> memberDisplayNames = new ArrayList<>();
        memberDisplayNames.add( SUPER_USER_DISPLAY_NAME );
        ROLE_WITH_MEMBER = buildRoleWithMembers( "role", "role-selections", "description", memberDisplayNames );
        roleWizardPanel.typeData( ROLE_WITH_MEMBER ).save().close( ROLE_WITH_MEMBER.getDisplayName() );
        userBrowsePanel.clickOnClearSelection();

        when: "role selected in a browse panel"
        userBrowseFilterPanel.typeSearchText( ROLE_WITH_MEMBER.getName() );
        userBrowsePanel.clickCheckboxAndSelectRole( ROLE_WITH_MEMBER.getName() );
        List<String> membersActual = roleStatisticsPanel.getMemberDisplayNames();
        TestUtils.saveScreenshot( getSession(), "member-in-st-panel" );

        then: "members in in a role statistics panel contains the 'Super User'"
        membersActual.contains( SUPER_USER_DISPLAY_NAME );

        and: "correct header displayed"
        roleStatisticsPanel.getItemDisplayName() == ROLE_WITH_MEMBER.getDisplayName();
    }

    def "GIVEN role with a member WHEN the role opened AND member removed AND role saved THEN members is empty in a role statistics panel"()
    {
        given: "the new role selected"
        userBrowseFilterPanel.typeSearchText( ROLE_WITH_MEMBER.getName() );
        RoleWizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectRole( ROLE_WITH_MEMBER.getName() ).clickToolbarEdit();
        TestUtils.saveScreenshot( getSession(), "before-member-removing" );

        when: "a member was removed and role saved"
        wizard.removeMember( SUPER_USER_DISPLAY_NAME ).save().close( ROLE_WITH_MEMBER.getDisplayName() );
        TestUtils.saveScreenshot( getSession(), "member-removed" );

        then: "members is empty in a role statistics panel"
        roleStatisticsPanel.getMemberDisplayNames().size() == 0;
    }

    def "GIVEN creating new Role WHEN saved and HomeButton clicked THEN correct display name shown in a statistics panel"()
    {
        given: "creating new Role"
        RoleWizardPanel roleWizardPanel = openRoleWizard();

        Role testRole = buildRole( "role", "role-selections", "app home button test" );
        roleWizardPanel.typeData( testRole );

        when: "role saved and HomeButton clicked"
        roleWizardPanel.save();
        userBrowsePanel.pressAppHomeButton();
        userBrowsePanel.clickOnClearSelection();

        then: "correct display name shown in a statistics panel"
        userBrowseFilterPanel.typeSearchText( testRole.getDisplayName() );
        userBrowsePanel.clickCheckboxAndSelectRole( testRole.getName() );
        roleStatisticsPanel.getItemDisplayName().equals( testRole.getDisplayName() );
    }
}
