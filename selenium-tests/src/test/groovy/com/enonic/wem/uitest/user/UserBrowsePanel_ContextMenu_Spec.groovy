package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.UserItemName
import com.enonic.autotests.vo.usermanager.RoleName
import com.enonic.autotests.vo.usermanager.UserStore

class UserBrowsePanel_ContextMenu_Spec
    extends BaseUsersSpec
{
    def "GIVEN a system 'user store' WHEN store selected and context menu opened  THEN all menu-items have correct state"()
    {
        when: "context menu opened"
        userBrowsePanel.openContextMenu( "/system" );
        saveScreenshot( "system-user-store-context-menu" );

        then: "Delete menu item is enabled"
        !userBrowsePanel.isContextMenuItemEnabled( "Delete" );

        and: "Edit menu item is enabled"
        userBrowsePanel.isContextMenuItemEnabled( "Edit" );

        and: "New menu item should be enabled"
        userBrowsePanel.isContextMenuItemEnabled( "New" );
    }

    def "WHEN 'system/groups' folder has been selected and context menu opened THEN all menu-items have correct state"()
    {
        when: "context menu is opened"
        userBrowsePanel.clickOnExpander( UserItemName.SYSTEM.getValue() );
        userBrowsePanel.openContextMenu( "groups" );
        saveScreenshot( "system-groups-context-menu" );

        then: "Delete menu item is disabled"
        !userBrowsePanel.isContextMenuItemEnabled( "Delete" );

        and: "Edit menu item should be disabled, because the folder is system"
        !userBrowsePanel.isContextMenuItemEnabled( "Edit" );

        and: "New menu item should be enabled"
        userBrowsePanel.isContextMenuItemEnabled( "New" );
    }


    def "WHEN 'Roles' folder selected and context menu opened THEN all menu-items have correct state"()
    {
        when: "context menu is opened"
        userBrowsePanel.openContextMenu( "roles" );
        saveScreenshot( "roles-context-menu" );

        then: "Delete menu item should be disabled"
        !userBrowsePanel.isContextMenuItemEnabled( "Delete" );

        and: "Edit menu item should be disabled, because the folder is system"
        !userBrowsePanel.isContextMenuItemEnabled( "Edit" );

        and: "New menu item should be enabled"
        userBrowsePanel.isContextMenuItemEnabled( "New" );
    }

    def "WHEN 'system/users' folder selected and context menu opened THEN all menu-items have correct state"()
    {
        when: "context menu is opened"
        userBrowsePanel.clickOnExpander( UserItemName.SYSTEM.getValue() );
        userBrowsePanel.openContextMenu( "users" );
        saveScreenshot( "system-users-context-menu" );

        then: "Delete menu item should be disabled"
        !userBrowsePanel.isContextMenuItemEnabled( "Delete" );

        and: "Edit menu item should be disabled"
        !userBrowsePanel.isContextMenuItemEnabled( "Edit" );
        and: "New menu item should be enabled"
        userBrowsePanel.isContextMenuItemEnabled( "New" );
    }

    def "GIVEN system 'super user' is selected WHEN context menu has been opened  THEN all menu-items have correct state"()
    {
        when: "context menu has been opened"
        userBrowseFilterPanel.typeSearchText( "su" );
        userBrowsePanel.openContextMenu( "users/su" );
        saveScreenshot( "system-su-context-menu" );

        then: "Delete menu item should be disabled"
        !userBrowsePanel.isContextMenuItemEnabled( "Delete" );

        and: "Edit menu item should be enabled"
        userBrowsePanel.isContextMenuItemEnabled( "Edit" );

        and: "New menu item should be enabled"
        userBrowsePanel.isContextMenuItemEnabled( "New" );
    }

    def "WHEN existing role is selected and context menu opened THEN all menu-items have correct state"()
    {
        when: "a role selected and context menu opened"
        userBrowseFilterPanel.typeSearchText( RoleName.SYSTEM_USER_MANAGER.getValue() );
        userBrowsePanel.openContextMenu( "roles/" + RoleName.SYSTEM_USER_MANAGER.getValue() );
        saveScreenshot( "role-context-menu" );

        then: "Delete menu item should be enabled"
        userBrowsePanel.isContextMenuItemEnabled( "Delete" );

        and: "Edit menu item should be enabled"
        userBrowsePanel.isContextMenuItemEnabled( "Edit" );

        and: "New menu item should be enabled"
        userBrowsePanel.isContextMenuItemEnabled( "New" );
    }

    def "GIVEN existing 'user store' WHEN this store has been selected and context menu opened THEN all menu-items have correct state"()
    {
        given: "new 'user store' was added"
        UserStore us = buildUserStore( "us", "context-menu-user-store", "context menu spec" );
        userBrowsePanel.openUserStoreWizard().typeData( us ).save().close( us.getDisplayName() );

        when: "the 'store' has been selected and context menu opened"
        userBrowsePanel.openContextMenu( us.getName() );
        saveScreenshot( "context_menu_user_store" );

        then: "Delete menu item should be enabled"
        userBrowsePanel.isContextMenuItemEnabled( "Delete" );

        and: "Edit menu item should be enabled"
        userBrowsePanel.isContextMenuItemEnabled( "Edit" );

        and: "New menu item should be enabled"
        userBrowsePanel.isContextMenuItemEnabled( "New" );
    }
}
