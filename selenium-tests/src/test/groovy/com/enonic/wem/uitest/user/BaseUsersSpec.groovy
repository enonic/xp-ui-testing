package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowseFilterPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.GroupWizardPanel
import com.enonic.autotests.pages.usermanager.wizardpanel.RoleWizardPanel
import com.enonic.autotests.pages.usermanager.wizardpanel.UserStoreWizardPanel
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.security.UserStoreAclEntry
import com.enonic.autotests.vo.usermanager.Group
import com.enonic.autotests.vo.usermanager.Role
import com.enonic.autotests.vo.usermanager.User
import com.enonic.autotests.vo.usermanager.UserStore
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class BaseUsersSpec
    extends BaseGebSpec
{
    @Shared
    String STANDARD_ID_PROVIDER = "Standard ID Provider";

    @Shared
    String USER_DELETING_NOTIFICATION_MESSAGE = "Principal \"user:system:%s\" is deleted";

    @Shared
    String GROUP_DELETING_NOTIFICATION_MESSAGE = "Principal \"group:system:%s\" is deleted";

    @Shared
    String ROLE_DELETED_MESSAGE = "Principal \"role:%s\" is deleted";

    @Shared
    String USER_CREATED_MESSAGE = "User was created";

    @Shared
    String GROUP_CREATED_MESSAGE = "Group was created"

    @Shared
    String ROLE_CREATED_MESSAGE = "Role was created";

    @Shared
    String USER_STORE_CREATED_MESSAGE = "User store was created";

    @Shared
    String USER_STORE_DELETED_MESSAGE = "User Store \"%s\" is deleted";

    @Shared
    String SUPER_USER_DISPLAY_NAME = "Super User";

    @Shared
    String ANONYMOUS_USER_DISPLAY_NAME = "Anonymous User";

    @Shared
    String ADMIN_CONSOLE_LOGIN_ROLE_DISPLAY_NAME = "Administration Console Login";

    @Shared
    String EVERYONE_ROLE_DISPLAY_NAME = "Everyone";

    @Shared
    String ROLES_FOLDER = "Roles";

    @Shared
    String SYSTEM_USER_STORE_DISPLAY_NAME = "System User Store";

    @Shared
    String SYSTEM_USER_STORE_NAME = "/system"

    @Shared
    UserBrowsePanel userBrowsePanel;

    @Shared
    UserBrowseFilterPanel userBrowseFilterPanel;

    @Shared
    String ROLE_EXISTS = "Principal [%s] could not be created. A principal with that name already exists";

    @Shared
    String USER_STORE_EXISTS = "User Store [%s] could not be created. A User Store with that name already exists";

    @Shared
    String GROUP_EXISTS = "A group with that name already exists";


    def setup()
    {
        go "admin"
        userBrowsePanel = NavigatorHelper.openUsersApp( getTestSession() );
        userBrowseFilterPanel = userBrowsePanel.getUserBrowseFilterPanel();
        getSession().put( UserBrowsePanel.USER_ITEM_TYPE, null );
    }

    protected GroupWizardPanel openSystemGroupWizard()
    {
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        return userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.GROUPS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();
    }

    protected UserStoreWizardPanel openUserStore( String userStoreName )
    {
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.clickCheckboxAndSelectRow( userStoreName ).clickToolbarEdit();
        userStoreWizardPanel.waitUntilWizardOpened();
        return userStoreWizardPanel;
    }

    protected UserWizardPanel openSystemUserWizard()
    {
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        return userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.USERS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();
    }

    protected RoleWizardPanel openRoleWizard()
    {
        return userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.ROLES_FOLDER ).clickToolbarNew().waitUntilWizardOpened();
    }

    protected Group buildGroup( String name, String displayName, String description )
    {
        String generated = NameHelper.uniqueName( name );
        return Group.builder().displayName( displayName ).name( generated ).description( description ).build();
    }

    protected Group buildGroupWithMembers( String name, String displayName, String description, List<String> members )
    {
        String generated = NameHelper.uniqueName( name );
        return Group.builder().displayName( displayName ).name( generated ).description( description ).memberDisplayNames(
            members ).build();
    }

    protected Role buildRole( String name, String displayName, String description )
    {
        String generated = NameHelper.uniqueName( name );
        return Role.builder().displayName( displayName ).name( generated ).description( description ).build();
    }

    protected Role buildRoleWithMembers( String name, String displayName, String description, List<String> roles )
    {
        String generated = NameHelper.uniqueName( name );
        return Role.builder().displayName( displayName ).name( generated ).description( description ).roles( roles ).build();
    }

    protected User buildUser( String userDisplayName, String password )
    {
        String generated = NameHelper.uniqueName( userDisplayName );
        return User.builder().displayName( generated ).email( generated + "@gmail.com" ).password( password ).build();
    }

    protected User buildUser( String userDisplayName, String password, String email )
    {
        String generated = NameHelper.uniqueName( userDisplayName );
        return User.builder().displayName( generated ).email( email ).password( password ).build();
    }

    protected String generateEmail( String userName )
    {
        return NameHelper.uniqueName( userName ) + "@gmail.com";
    }

    protected User buildUserWithRolesAndGroups( String userName, String password, List<String> roles, List<String> groups )
    {
        String generated = NameHelper.uniqueName( userName );
        return User.builder().displayName( generated ).email( generated + "@gmail.com" ).password( password ).roles( roles ).groups(
            groups ).build();
    }

    protected User buildUserWithRolesAndGroups( String userName, String password, String email, List<String> roles, List<String> groups )
    {
        String generated = NameHelper.uniqueName( userName );
        return User.builder().name( generated ).displayName( generated ).email( email ).password( password ).roles( roles ).groups(
            groups ).build();
    }

    protected UserStore buildUserStore( String name, String displayName, String description )
    {
        String generated = NameHelper.uniqueName( name );
        return UserStore.builder().displayName( displayName ).name( generated ).description( description ).build();
    }

    protected UserStore buildUserStoreWitIdProvider( String name, String displayName, String description, String idProviderDisplayName )
    {
        String generated = NameHelper.uniqueName( name );
        return UserStore.builder().displayName( displayName ).name( generated ).description( description ).idProviderDisplayName(
            idProviderDisplayName ).build();
    }

    protected UserStore buildUserStoreWithPermissions( String name, String displayName, String description,
                                                       List<UserStoreAclEntry> entries )
    {
        String generated = NameHelper.uniqueName( name );
        return UserStore.builder().displayName( displayName ).name( generated ).description( description ).aclEntries( entries ).build();
    }

    protected void addUser( User user )
    {
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        userBrowsePanel.clickCheckboxAndSelectFolder( UserBrowsePanel.BrowseItemType.USERS_FOLDER ).clickToolbarNew().typeData(
            user ).save().close( user.getDisplayName() );
    }
}
