package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowseFilterPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.GroupWizardPanel
import com.enonic.autotests.pages.usermanager.wizardpanel.RoleWizardPanel
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
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
    String USER_DELETING_NOTIFICATION_MESSAGE = "Principal [user:system:%s] deleted!";

    @Shared
    String GROUP_DELETING_NOTIFICATION_MESSAGE = "Principal [group:system:%s] deleted!";

    @Shared
    String ROLE_DELETED_MESSAGE = "Principal [role:%s] deleted!";

    @Shared
    String USER_CREATED_MESSAGE = "User was created!";

    @Shared
    String GROUP_CREATED_MESSAGE = "Group was created!"

    @Shared
    String ROLE_CREATED_MESSAGE = "Role was created!";

    @Shared
    String USERSTORE_CREATED_MESSAGE = "UserStore was created!";

    @Shared
    UserBrowsePanel userBrowsePanel;

    @Shared
    UserBrowseFilterPanel userBrowseFilterPanel;

    @Shared
    String ROLE_EXISTS = "A role with name %s already exists";

    @Shared
    String GROUP_EXISTS = "A group with that name already exists";


    def setup()
    {
        go "admin"
        userBrowsePanel = NavigatorHelper.openUserManager( getTestSession() );
        userBrowseFilterPanel = userBrowsePanel.getUserBrowseFilterPanel();
    }

    protected GroupWizardPanel openSystemGroupWizard()
    {
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        return userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.GROUPS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();
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

    protected Role buildRole( String name, String displayName, String description )
    {
        String generated = NameHelper.uniqueName( name );
        return Role.builder().displayName( displayName ).name( generated ).description( description ).build();
    }

    protected User buildUser( String userName, String password )
    {
        String generated = NameHelper.uniqueName( userName );
        return User.builder().displayName( generated ).email( generated + "@gmail.com" ).password( password ).build();
    }

    protected User buildUserWithRolesAndGroups( String userName, String password, List<String> roles, List<String> groups )
    {
        String generated = NameHelper.uniqueName( userName );
        return User.builder().displayName( generated ).email( generated + "@gmail.com" ).password( password ).roles( roles ).groups(
            groups ).build();
    }

    protected UserStore buildUserStore( String name, String displayName, String description )
    {
        String generated = NameHelper.uniqueName( name );
        return UserStore.builder().displayName( displayName ).name( generated ).description( description ).build();

    }
}
