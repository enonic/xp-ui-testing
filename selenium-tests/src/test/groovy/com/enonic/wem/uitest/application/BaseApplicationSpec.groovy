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
    String FIRST_APP_NAME = "com.enonic.xp.testing.first_app";

    @Shared
    String SECOND_APP_NAME = "com.enonic.xp.testing.second_app";

    @Shared
    String THIRD_APP_NAME = "com.enonic.xp.testing.third_app";

    @Shared
    String FOURTH_APP_NAME = "com.enonic.xp.testing.fourth_app";

    @Shared
    String SIMPLE_APP_NAME = "com.enonic.xp.testing.simple_app";

    @Shared
    String STARTED_STATE = "started";

    @Shared
    String STOPPED_STATE = "stopped";

    @Shared
    String GOOGLE_ANALYTICS_APP_NAME = "com.enonic.app.ga";

    @Shared
    String GOOGLE_ANALYTICS_DISPLAY_NAME = "Google Analytics";

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
