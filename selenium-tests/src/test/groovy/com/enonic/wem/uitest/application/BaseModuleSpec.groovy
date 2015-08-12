package com.enonic.wem.uitest.application

import com.enonic.autotests.pages.modules.ModuleBrowsePanel
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class BaseModuleSpec
    extends BaseGebSpec
{
    @Shared
    ModuleBrowsePanel moduleBrowsePanel;

    @Shared
    String FIRST_APP_NAME = "com.enonic.xp.testing.first_app";

    @Shared
    String SECOND_APP_NAME = "com.enonic.xp.testing.second_app";

    @Shared
    String THIRD_APP_NAME = "com.enonic.xp.testing.third_app";

    @Shared
    String FOURTH_APP_NAME = "com.enonic.xp.testing.fourth_app";

    @Shared
    String TEST_APP_NAME = "com.enonic.xp.testing.test_app";

}
