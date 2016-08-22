package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.wizardpanel.UserStoreWizardPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.security.UserStoreAccess
import com.enonic.autotests.vo.contentmanager.security.UserStoreAclEntry
import com.enonic.autotests.vo.usermanager.RoleDisplayName
import com.enonic.autotests.vo.usermanager.UserStore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class UserStore_Permissions_Spec
    extends BaseUsersSpec
{

    @Shared
    UserStore USER_STORE_WITH_PERMISSIONS;

    def "WHEN user store wizard opened THEN two readonly permissions are displayed"()
    {
        when: "wizard for creating of new user store is opened"
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();
        List<UserStoreAclEntry> entries = userStoreWizardPanel.getPermissions();
        TestUtils.saveScreenshot( getSession(), "test_user_store_default_permission" );

        then: "two readonly permissions are displayed"
        entries.size() == 2;

        and:
        entries.get( 0 ).getPrincipalDisplayName() == RoleDisplayName.SYSTEM_AUTHENTICATED.getValue();

        and: "default permissions displayed with correct values"
        entries.get( 0 ).getUserStoreAccess().getValue() == UserStoreAccess.READ.getValue();

        and: "default permissions displayed with correct values"
        userStoreWizardPanel.isAclEntryReadOnly( RoleDisplayName.SYSTEM_AUTHENTICATED.getValue() );

        and: "default permissions displayed with correct values"
        entries.get( 1 ).getPrincipalDisplayName() == RoleDisplayName.SYSTEM_ADMIN.getValue();

        and: "default permissions displayed with correct values"
        entries.get( 1 ).getUserStoreAccess().getValue() == UserStoreAccess.ADMINISTRATOR.getValue();

        and: "default permissions displayed with correct values"
        userStoreWizardPanel.isAclEntryReadOnly( RoleDisplayName.SYSTEM_AUTHENTICATED.getValue() );

    }

    def "GIVEN adding of a new user-store  with a provider WHEN data typed and 'Save' button pressed  THEN store saved AND correct notification message appears"()
    {
        given: "start adding a new user-store"
        List<UserStoreAclEntry> aclEntries = new ArrayList<>();
        UserStoreAclEntry aclEntry = UserStoreAclEntry.builder().access( UserStoreAccess.CREATE_USERS ).principalName(
            RoleDisplayName.USERS_ADMINISTRATOR.getValue() ).build();
        aclEntries.add( aclEntry );
        USER_STORE_WITH_PERMISSIONS = buildUserStoreWithPermissions( "store", "test store", "store with a permissions", aclEntries );
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();

        when: "data typed and user store saved"
        String message = userStoreWizardPanel.typeData( USER_STORE_WITH_PERMISSIONS ).save().waitNotificationMessage();
        TestUtils.saveScreenshot( getSession(), "test_user_store_permission_added" );
        List<UserStoreAclEntry> entries = userStoreWizardPanel.getPermissions();

        then: "correct notification message appears"
        message == "UserStore was created!"

        and: "three permissions are displayed "
        entries.size() == 3;

        and: "new added permission displayed with correct values"
        entries.get( 2 ).getPrincipalDisplayName() == RoleDisplayName.USERS_ADMINISTRATOR.getValue();

        and: "new added permission displayed with correct values"
        entries.get( 2 ).getUserStoreAccess().getValue() == UserStoreAccess.CREATE_USERS.getValue();
    }

    def "GIVEN existing user store with one added permission WHEN this permission has been removed THEN correct permissions list is displayed on the wizard"()
    {
        given: "existing store with added permission is opened"
        UserStoreWizardPanel userStoreWizardPanel = openUserStore( USER_STORE_WITH_PERMISSIONS.getName() );

        when: "remove permission button clicked"
        userStoreWizardPanel.removePermission( RoleDisplayName.USERS_ADMINISTRATOR.getValue() );
        List<UserStoreAclEntry> entries = userStoreWizardPanel.getPermissions();
        TestUtils.saveScreenshot( getSession(), "test_user_store_permission_removed" );

        then: "number of permissions has been reduced"
        entries.size() == 2;

        and: "removed permission is not displayed on the wizard panel"
        !userStoreWizardPanel.isPermissionDisplayed( RoleDisplayName.USERS_ADMINISTRATOR.getValue() );
    }
}
