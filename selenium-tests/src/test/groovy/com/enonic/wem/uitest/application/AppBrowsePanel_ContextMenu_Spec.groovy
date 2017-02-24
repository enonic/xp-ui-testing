package com.enonic.wem.uitest.application

class AppBrowsePanel_ContextMenu_Spec
    extends BaseApplicationSpec
{
    def "GIVEN a local 'started' application WHEN the application selected and context menu opened THEN all menu-items have correct state"()
    {
        when: "context menu opened"
        applicationBrowsePanel.selectItemByDisplayNameOnOpenContextMenu( THIRD_APP_DISPLAY_NAME );
        saveScreenshot( "local-app-context-menu" );

        then: "Delete menu item is enabled"
        applicationBrowsePanel.isContextMenuItemEnabled( "Stop" );

        and: "Edit menu item is enabled"
        !applicationBrowsePanel.isContextMenuItemEnabled( "Start" );

        and: "New menu item is enabled"
        !applicationBrowsePanel.isContextMenuItemEnabled( "Uninstall" );
    }

    def "GIVEN a local 'stopped' application WHEN the application selected and context menu opened THEN all menu-items have correct state"()
    {
        given:
        applicationBrowsePanel.selectRowByItemDisplayName( THIRD_APP_DISPLAY_NAME );
        saveScreenshot( "local-app-stopped" );
        applicationBrowsePanel.clickOnToolbarStop();
        applicationBrowsePanel.doClearSelection();

        when: "context menu opened"
        applicationBrowsePanel.selectItemByDisplayNameOnOpenContextMenu( THIRD_APP_DISPLAY_NAME );
        saveScreenshot( "local-app-stopped--context-menu" );

        then: "Delete menu item is enabled"
        !applicationBrowsePanel.isContextMenuItemEnabled( "Stop" );

        and: "Edit menu item is enabled"
        applicationBrowsePanel.isContextMenuItemEnabled( "Start" );

        and: "New menu item is enabled"
        !applicationBrowsePanel.isContextMenuItemEnabled( "Uninstall" );
    }

    def "GIVEN existing not local application WHEN the application selected and context menu opened THEN all menu-items have correct state"()
    {
        given:
        installAppAndCloseDialog( DISQUS_APP_NAME, DISQUS_APP_DISPLAY_NAME );

        when: "context menu opened"
        applicationBrowsePanel.clickOnRowByName( DISQUS_APP_NAME );
        saveScreenshot( "disqus-context-menu" );

        then: "Delete menu item is enabled"
        applicationBrowsePanel.isContextMenuItemEnabled( "Stop" );

        and: "Edit menu item is enabled"
        !applicationBrowsePanel.isContextMenuItemEnabled( "Start" );

        and: "New menu item is enabled"
        applicationBrowsePanel.isContextMenuItemEnabled( "Uninstall" );
    }
}
