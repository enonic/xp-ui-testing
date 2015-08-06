package com.enonic.wem.uitest.module

import com.enonic.autotests.pages.modules.ModuleBrowsePanel
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class BaseModuleSpec
    extends BaseGebSpec
{
    @Shared
    ModuleBrowsePanel moduleBrowsePanel;

    @Shared
    String FIRST_APP_NAME = "com.enonic.xp.testing.first_module";

    @Shared
    String SECOND_APP_NAME = "com.enonic.xp.testing.second_module";

    @Shared
    String THIRD_APP_NAME = "com.enonic.xp.testing.third_module";

    @Shared
    String FOURTH_APP_NAME = "com.enonic.xp.testing.fourth_module";

    @Shared
    String TEST_APP_NAME = "com.enonic.xp.testing.test_module";

}
