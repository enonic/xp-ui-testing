package com.enonic.wem.uitest.application

import com.enonic.autotests.pages.modules.ApplicationBrowseItemsSelectionPanel
import com.enonic.autotests.pages.modules.ApplicationBrowsePanel
import com.enonic.autotests.pages.modules.ApplicationItemStatisticsPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class BaseApplicationSpec
    extends BaseGebSpec
{
    @Shared
    String SYSTEM_REQUIRED = ">= 5.0 and <=5.1";

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

}
