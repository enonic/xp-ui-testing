package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.usermanager.RoleName
import com.enonic.autotests.vo.usermanager.UserStore

class UserBrowsePanel_ContextMenu_Spec
    extends BaseUsersSpec
{
    def "GIVEN a system 'user store' WHEN store selected and context menu opened  THEN all menu-items have correct state"()
    {
        when: "context menu opened"
        userBrowsePanel.openContextMenu( "/system" );
        TestUtils.saveScreenshot( getSession(), "system-user-store-context-menu" );

        then: "Delete menu item is enabled"
        !userBrowsePanel.isContextMenuItemEnabled( "Delete" );

        and: "Edit menu item is enabled"
        userBrowsePanel.isContextMenuItemEnabled( "Edit" );

        and: "New menu item is enabled"
        userBrowsePanel.isContextMenuItemEnabled( "New" );
    }

    def "WHEN 'system/groups' folder selected and context menu opened THEN all menu-items have correct state"()
    {
        when: "context menu opened"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        userBrowsePanel.openContextMenu( "groups" );
        TestUtils.saveScreenshot( getSession(), "system-groups-context-menu" );

        then: "Delete menu item is disabled"
        !userBrowsePanel.isContextMenuItemEnabled( "Delete" );

        and: "Edit menu item is disabled"
        !userBrowsePanel.isContextMenuItemEnabled( "Edit" );

        and: "New menu item is enabled"
        userBrowsePanel.isContextMenuItemEnabled( "New" );
    }


    def "WHEN 'Roles' folder selected and context menu opened THEN all menu-items have correct state"()
    {
        when: "context menu opened"
        userBrowsePanel.openContextMenu( "roles" );
        TestUtils.saveScreenshot( getSession(), "roles-context-menu" );

        then: "Delete menu item is disabled"
        !userBrowsePanel.isContextMenuItemEnabled( "Delete" );

        and: "Edit menu item is disabled"
        !userBrowsePanel.isContextMenuItemEnabled( "Edit" );

        and: "New menu item is enabled"
        userBrowsePanel.isContextMenuItemEnabled( "New" );
    }

    def "WHEN 'system/users' folder selected and context menu opened THEN all menu-items have correct state"()
    {
        when: "context menu opened"
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        userBrowsePanel.openContextMenu( "users" );
        TestUtils.saveScreenshot( getSession(), "system-users-context-menu" );

        then: "Delete menu item is disabled"
        !userBrowsePanel.isContextMenuItemEnabled( "Delete" );

        and: "Edit menu item is disabled"
        !userBrowsePanel.isContextMenuItemEnabled( "Edit" );
        and: "New menu item is enabled"
        userBrowsePanel.isContextMenuItemEnabled( "New" );
    }

    def "GIVEN a system 'super user' WHEN 'su' selected and context menu opened  THEN all menu-items have correct state"()
    {
        when: "context menu opened"
        userBrowseFilterPanel.typeSearchText( "su" );
        userBrowsePanel.openContextMenu( "users/su" );
        TestUtils.saveScreenshot( getSession(), "system-su-context-menu" );

        then: "Delete menu item is enabled"
        userBrowsePanel.isContextMenuItemEnabled( "Delete" );

        and: "Edit menu item is enabled"
        userBrowsePanel.isContextMenuItemEnabled( "Edit" );

        and: "New menu item is enabled"
        userBrowsePanel.isContextMenuItemEnabled( "New" );
    }

    def "WHEN a role selected and context menu opened THEN all menu-items have correct state"()
    {
        when: "a role selected and context menu opened"
        userBrowseFilterPanel.typeSearchText( RoleName.SYSTEM_USER_MANAGER.getValue() );
        userBrowsePanel.openContextMenu( "roles/" + RoleName.SYSTEM_USER_MANAGER.getValue() );
        TestUtils.saveScreenshot( getSession(), "role-context-menu" );

        then: "Delete menu item is enabled"
        userBrowsePanel.isContextMenuItemEnabled( "Delete" );

        and: "Edit menu item is enabled"
        userBrowsePanel.isContextMenuItemEnabled( "Edit" );

        and: "New menu item is enabled"
        userBrowsePanel.isContextMenuItemEnabled( "New" );
    }

    def "GIVEN new 'user store' added WHEN this store selected and context menu opened THEN all menu-items have correct state"()
    {
        given: "new user store added"
        UserStore us = buildUserStore( "us", "context-menu-user-store", "context menu spec" );
        userBrowsePanel.openUserStoreWizard().typeData( us ).save().close( us.getDisplayName() );

        when:
        userBrowsePanel.openContextMenu( us.getName() );

        then: "Delete menu item is enabled"
        userBrowsePanel.isContextMenuItemEnabled( "Delete" );

        and: "Edit menu item is enabled"
        userBrowsePanel.isContextMenuItemEnabled( "Edit" );

        and: "New menu item is enabled"
        userBrowsePanel.isContextMenuItemEnabled( "New" );
    }
}
