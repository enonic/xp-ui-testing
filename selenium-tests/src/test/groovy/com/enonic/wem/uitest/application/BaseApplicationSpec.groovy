package com.enonic.wem.uitest.application

import com.enonic.autotests.pages.modules.ApplicationBrowsePanel
import com.enonic.autotests.pages.modules.ApplicationItemStatisticsPanel
import com.enonic.autotests.pages.modules.InstallAppDialog
import com.enonic.autotests.pages.modules.InstallAppDialog_MarketAppPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class BaseApplicationSpec
    extends BaseGebSpec
{
    @Shared
    String SYSTEM_REQUIRED = ">= 6.0.0 and < 7.0.0";

    @Shared
    String FIRST_APP_KEY = "com.enonic.xp.testing.first_app";

    @Shared
    String FIRST_APP_DISPLAY_NAME = "First Selenium App";

    @Shared
    String SECOND_APP_DISPLAY_NAME = "Second Selenium App";

    @Shared
    String THIRD_APP_DISPLAY_NAME = "Third Selenium App";

    @Shared
    String TEST_DESCRIPTION = "Test application for ui-testing";

    @Shared
    String FOURTH_APP_NAME = "com.enonic.xp.testing.fourth_app";

    @Shared
    String FOURTH_APP_DISPLAY_NAME = "Fourth Selenium App";

    @Shared
    String SIMPLE_APP_DISPLAY_NAME = "Simple Site App";

    @Shared
    String STARTED_STATE = "started";

    @Shared
    String STOPPED_STATE = "stopped";

    @Shared
    String GOOGLE_ANALYTICS_APP_NAME = "Track and report website traffic with Google Analytics";

    @Shared
    String GOOGLE_ANALYTICS_DISPLAY_NAME = "Google Analytics";

    @Shared
    String CONTENT_VIEWER_APP_NAME = "com.enonic.app.contentviewer";

    @Shared
    String CONTENT_VIEWER_APP_DISPLAY_NAME = "Content viewer";

    @Shared
    String DISQUS_APP_DISPLAY_NAME = "Disqus";

    @Shared
    String DISQUS_APP_NAME = "Add Disqus comments to your website pages";

    @Shared
    String APP_UNINSTALLED = "Application '%s' uninstalled successfully";

    @Shared
    String APP_INSTALLED_MESSAGE = "Application '%s' installed successfully";

    @Shared
    String APP_UPDATED_MESSAGE = "Application '%s' updated successfully";

    @Shared
    ApplicationItemStatisticsPanel applicationItemStatisticsPanel;

    @Shared
    ApplicationBrowsePanel applicationBrowsePanel;


    def setup()
    {
        go "admin"
        applicationBrowsePanel = NavigatorHelper.openApplications( getTestSession() );
        applicationItemStatisticsPanel = applicationBrowsePanel.getItemStatisticPanel();
    }

    protected installAppAndCloseDialog( String name, String displayName )
    {
        if ( !applicationBrowsePanel.exists( name ) )
        {
            applicationBrowsePanel.clickOnToolbarInstall();
            InstallAppDialog appDialog = new InstallAppDialog( getSession() );
            appDialog.waitUntilDialogLoaded();
            saveScreenshot( NameHelper.uniqueName( "install-dlg-opened" ) );
            appDialog.typeInApplicationInput( displayName );
            saveScreenshot( NameHelper.uniqueName( "app-name-typed" ) );
            InstallAppDialog_MarketAppPanel marketPanel = appDialog.getMarketAppPanel();
            marketPanel.doInstallApp( displayName );
            appDialog.clickOnCancelButton();
            sleep( 8000 );
            applicationBrowsePanel.waitNewInstalledApplicationAppears( name, 10 );
            applicationBrowsePanel.waitApplicationStatus( name, "started" )
        }
    }
}
