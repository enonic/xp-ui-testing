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
    String FIRST_MODULE_NAME = "com.enonic.xp.ui-testing.first-module";

    @Shared
    String SECOND_MODULE_NAME = "com.enonic.xp.ui-testing.second-module";

    @Shared
    String THIRD_MODULE_NAME = "com.enonic.xp.ui-testing.third-module";

    @Shared
    String FOURTH_MODULE_NAME = "com.enonic.xp.ui-testing.fourth-module";


    @Shared
    String FIRST_MODULE_URL = "mvn:com.enonic.xp.ui-testing/first-module/5.0.0-SNAPSHOT";

    @Shared
    String SECOND_MODULE_URL = "mvn:com.enonic.xp.ui-testing/second-module/5.0.0-SNAPSHOT";

    @Shared
    String THIRD_MODULE_URL = "mvn:com.enonic.xp.ui-testing/third-module/5.0.0-SNAPSHOT";

    @Shared
    String FOURTH_MODULE_URL = "mvn:com.enonic.xp.ui-testing/fourth-module/5.0.0-SNAPSHOT";

    @Shared
    String TEST_MODULE_NAME = "com.enonic.xp.ui-testing.test-module";


    @Shared
    String TEST_MODULE_URL = "mvn:com.enonic.xp.ui-testing/test-module/5.0.0-SNAPSHOT";
}
