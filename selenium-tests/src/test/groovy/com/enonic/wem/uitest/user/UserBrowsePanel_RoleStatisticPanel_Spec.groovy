package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.RoleStatisticsPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserItemName
import com.enonic.autotests.pages.usermanager.wizardpanel.RoleWizardPanel
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

    def "WHEN 'Roles' folder is selected THEN correct info should be shown on the statistics panel"()
    {
        when: "'Roles' folder is selected"
        userBrowsePanel.clickOnExpander( UserItemName.SYSTEM.getValue() );
        userBrowsePanel.clickCheckboxAndSelectFolder( UserItemName.ROLES_FOLDER );
        saveScreenshot( "roles-statistic-panel" );
        roleStatisticsPanel = new RoleStatisticsPanel( getSession() );

        then: "correct info should be shown on the statistics panel"
        roleStatisticsPanel.getItemDisplayName() == ROLES_FOLDER;
    }

    def "WHEN 'administrator console login' role is selected THEN correct members and header are displayed in a statistics panel"()
    {
        when: "the 'administrator console login' role is selected"
        userBrowseFilterPanel.typeSearchText( ADMINISTRATOR_CONSOLE_LOGIN_ROLE_NAME );
        userBrowsePanel.clickCheckboxAndSelectGroup( ADMINISTRATOR_CONSOLE_LOGIN_ROLE_NAME );
        saveScreenshot( "adm-console-login-selected" );
        List<String> members = roleStatisticsPanel.getMemberDisplayNames();
        saveScreenshot( "su-selected" );

        then: "'Super User' should be in members of the role"
        roleStatisticsPanel.getItemDisplayName() == ADMIN_CONSOLE_LOGIN_ROLE_DISPLAY_NAME;

        //and: "correct display name of the role should be shown"
    }

    def "WHEN system 'everyone' is selected THEN correct members should be displayed on the statistics panel"()
    {
        when: "system 'everyone' is selected"
        userBrowseFilterPanel.typeSearchText( SYSTEM_EVERYONE_NAME );
        userBrowsePanel.clickCheckboxAndSelectGroup( SYSTEM_EVERYONE_NAME );
        List<String> members = roleStatisticsPanel.getMemberDisplayNames();
        saveScreenshot( "everyone-group-selected" );

        then: "members should be empty"
        members.isEmpty();

        and: "correct display name of the role should be shown"
        roleStatisticsPanel.getItemDisplayName() == EVERYONE_ROLE_DISPLAY_NAME;
    }

    def "GIVEN role with member was added WHEN the role is selected in the browse panel THEN correct member shown on the role statistics panel"()
    {
        given: "role with member was added"
        RoleWizardPanel roleWizardPanel = openRoleWizard();
        List<String> memberDisplayNames = new ArrayList<>();
        memberDisplayNames.add( SUPER_USER_DISPLAY_NAME );
        ROLE_WITH_MEMBER = buildRoleWithMembers( "role", "role-selections", "description", memberDisplayNames );
        roleWizardPanel.typeData( ROLE_WITH_MEMBER ).save().close( ROLE_WITH_MEMBER.getDisplayName() );
        userBrowsePanel.doClearSelection();

        when: "role has been selected in the browse panel"
        userBrowseFilterPanel.typeSearchText( ROLE_WITH_MEMBER.getName() );
        userBrowsePanel.clickCheckboxAndSelectRole( ROLE_WITH_MEMBER.getName() );
        List<String> membersActual = roleStatisticsPanel.getMemberDisplayNames();
        saveScreenshot( "member-in-st-panel" );

        then: "members in in a role statistics panel should contain the 'Super User'"
        membersActual.contains( SUPER_USER_DISPLAY_NAME );

        and: "correct header should be displayed"
        roleStatisticsPanel.getItemDisplayName() == ROLE_WITH_MEMBER.getDisplayName();
    }

    def "GIVEN role with a member WHEN the role is opened AND member removed AND role saved THEN members should be empty on the role statistics panel"()
    {
        given: "the new role selected"
        userBrowseFilterPanel.typeSearchText( ROLE_WITH_MEMBER.getName() );
        RoleWizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectRole( ROLE_WITH_MEMBER.getName() ).clickToolbarEdit();
        saveScreenshot( "before-member-removing" );

        when: "a member was removed and role saved"
        wizard.removeMember( SUPER_USER_DISPLAY_NAME ).save().close( ROLE_WITH_MEMBER.getDisplayName() );
        saveScreenshot( "member-removed" );

        then: "members should be empty on the role statistics panel"
        roleStatisticsPanel.getMemberDisplayNames().size() == 0;
    }

    def "GIVEN creating of new Role WHEN saved and HomeButton clicked THEN correct display name shown in a statistics panel"()
    {
        given: "creating of new Role"
        RoleWizardPanel roleWizardPanel = openRoleWizard();

        Role testRole = buildRole( "role", "role-selections", "app home button test" );
        and: "data has been typed"
        roleWizardPanel.typeData( testRole );

        when: "role is saved and HomeButton clicked"
        roleWizardPanel.save();
        userBrowsePanel.pressAppHomeButton();
        userBrowsePanel.doClearSelection();

        then: "correct display name should be shown on the statistics panel"
        userBrowseFilterPanel.typeSearchText( testRole.getDisplayName() );
        userBrowsePanel.clickCheckboxAndSelectRole( testRole.getName() );
        roleStatisticsPanel.getItemDisplayName().equals( testRole.getDisplayName() );
    }
}
