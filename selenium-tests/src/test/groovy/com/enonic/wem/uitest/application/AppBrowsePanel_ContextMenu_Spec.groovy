package com.enonic.wem.uitest.application

import com.enonic.autotests.utils.TestUtils

class AppBrowsePanel_ContextMenu_Spec
    extends BaseApplicationSpec
{
    def "GIVEN a local application WHEN the application selected and context menu opened THEN all menu-items have correct state"()
    {
        when: "context menu opened"
        applicationBrowsePanel.openContextMenu( FIRST_APP_NAME );
        TestUtils.saveScreenshot( getSession(), "local-app-context-menu" );

        then: "Delete menu item is enabled"
        applicationBrowsePanel.isEnabledContextMenuItem( "Stop" );

        and: "Edit menu item is enabled"
        !applicationBrowsePanel.isEnabledContextMenuItem( "Start" );

        and: "New menu item is enabled"
        !applicationBrowsePanel.isEnabledContextMenuItem( "Uninstall" );
    }

    def "GIVEN existing not local application WHEN the application selected and context menu opened THEN all menu-items have correct state"()
    {
        given:
        installGoogleAnalytics();

        when: "context menu opened"
        applicationBrowsePanel.openContextMenu( GOOGLE_ANALYTICS_APP_NAME );
        TestUtils.saveScreenshot( getSession(), "app-market-context-menu" );

        then: "Delete menu item is enabled"
        applicationBrowsePanel.isEnabledContextMenuItem( "Stop" );

        and: "Edit menu item is enabled"
        !applicationBrowsePanel.isEnabledContextMenuItem( "Start" );

        and: "New menu item is enabled"
        applicationBrowsePanel.isEnabledContextMenuItem( "Uninstall" );
    }
}
