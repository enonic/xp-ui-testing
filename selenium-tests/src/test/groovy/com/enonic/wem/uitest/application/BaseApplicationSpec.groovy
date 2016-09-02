package com.enonic.wem.uitest.application

import com.enonic.autotests.pages.modules.*
import com.enonic.autotests.services.NavigatorHelper
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
    String FOURTH_APP_NAME = "com.enonic.xp.testing.fourth_app";

    @Shared
    String SIMPLE_APP_DISPLAY_NAME = "Simple Site App";

    @Shared
    String STARTED_STATE = "started";

    @Shared
    String STOPPED_STATE = "stopped";

    @Shared
    String GOOGLE_ANALYTICS_APP_NAME = "com.enonic.app.ga";

    @Shared
    String GOOGLE_ANALYTICS_DISPLAY_NAME = "Google Analytics";

    @Shared
    String CONTENT_VIEWER_APP_NAME = "com.enonic.app.contentviewer";

    @Shared
    String CONTENT_VIEWER_APP_DISPLAY_NAME = "Content viewer";

    @Shared
    String DISQUS_APP_DISPLAY_NAME = "Disqus";

    @Shared
    String DISQUS_APP_NAME = "com.enonic.app.disqus";

    @Shared
    String APP_UNINSTALLED = "Application '%s' uninstalled successfully";

    @Shared
    String APP_INSTALLED_MESSAGE = "Application '%s' installed successfully";

    @Shared
    String APP_UPDATED_MESSAGE = "Application '%s' updated successfully";


    @Shared
    ApplicationBrowseItemsSelectionPanel itemsSelectionPanel;

    @Shared
    ApplicationItemStatisticsPanel applicationItemStatisticsPanel;

    @Shared
    ApplicationBrowsePanel applicationBrowsePanel;


    def setup()
    {
        go "admin"
        applicationBrowsePanel = NavigatorHelper.openApplications( getTestSession() );
        itemsSelectionPanel = applicationBrowsePanel.getItemSelectionPanel();
        applicationItemStatisticsPanel = applicationBrowsePanel.getItemStatisticPanel();
    }

    protected installAppAndCloseDialog( String name, String displayName )
    {
        if ( !applicationBrowsePanel.exists( name ) )
        {
            applicationBrowsePanel.clickOnToolbarInstall();
            InstallAppDialog appDialog = new InstallAppDialog( getSession() );
            appDialog.waitUntilDialogLoaded();
            InstallAppDialog_MarketAppPanel marketPanel = appDialog.clickOnEnonicMarketTab();
            marketPanel.doInstallApp( displayName );
            appDialog.clickOnCancelButton();
            sleep( 3000 );
            applicationBrowsePanel.waitNewInstalledApplicationAppears( name, 10 );
            applicationBrowsePanel.waitApplicationStatus( name, "started" )
        }
    }
}
