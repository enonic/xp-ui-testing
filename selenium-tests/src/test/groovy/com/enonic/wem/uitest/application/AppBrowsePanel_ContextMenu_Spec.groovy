package com.enonic.wem.uitest.application

import com.enonic.autotests.utils.TestUtils

class AppBrowsePanel_ContextMenu_Spec
    extends BaseApplicationSpec
{
    def "GIVEN a local 'started' application WHEN the application selected and context menu opened THEN all menu-items have correct state"()
    {
        when: "context menu opened"
        applicationBrowsePanel.openContextMenu( THIRD_APP_NAME );
        TestUtils.saveScreenshot( getSession(), "local-app-context-menu" );

        then: "Delete menu item is enabled"
        applicationBrowsePanel.isEnabledContextMenuItem( "Stop" );

        and: "Edit menu item is enabled"
        !applicationBrowsePanel.isEnabledContextMenuItem( "Start" );

        and: "New menu item is enabled"
        !applicationBrowsePanel.isEnabledContextMenuItem( "Uninstall" );
    }

    def "GIVEN a local 'stopped' application WHEN the application selected and context menu opened THEN all menu-items have correct state"()
    {
        given:
        applicationBrowsePanel.selectRowByName( THIRD_APP_NAME );
        applicationBrowsePanel.clickOnToolbarStop();
        applicationBrowsePanel.clickOnClearSelection();

        when: "context menu opened"
        applicationBrowsePanel.openContextMenu( THIRD_APP_NAME );
        TestUtils.saveScreenshot( getSession(), "local-app-context-menu" );

        then: "Delete menu item is enabled"
        !applicationBrowsePanel.isEnabledContextMenuItem( "Stop" );

        and: "Edit menu item is enabled"
        applicationBrowsePanel.isEnabledContextMenuItem( "Start" );

        and: "New menu item is enabled"
        !applicationBrowsePanel.isEnabledContextMenuItem( "Uninstall" );
    }

    def "GIVEN existing not local application WHEN the application selected and context menu opened THEN all menu-items have correct state"()
    {
        given:
        installAppAndCloseDialog( DISQUS_APP_NAME, DISQUS_APP_DISPLAY_NAME );

        when: "context menu opened"
        applicationBrowsePanel.openContextMenu( DISQUS_APP_NAME );
        TestUtils.saveScreenshot( getSession(), "disqus-context-menu" );

        then: "Delete menu item is enabled"
        applicationBrowsePanel.isEnabledContextMenuItem( "Stop" );

        and: "Edit menu item is enabled"
        !applicationBrowsePanel.isEnabledContextMenuItem( "Start" );

        and: "New menu item is enabled"
        applicationBrowsePanel.isEnabledContextMenuItem( "Uninstall" );
    }
}
