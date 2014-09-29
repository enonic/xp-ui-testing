package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.versiontest.VersionTestPage
import com.enonic.autotests.utils.SleepHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class ResetAppSpec
    extends BaseGebSpec
{
    @Shared
    VersionTestPage versiontestPage;


    def setup()
    {
        go "http://versiontest2/"
        versiontestPage = new VersionTestPage( getTestSession() );

    }

    def "clickOnReset"()
    {
        when:
        TestUtils.saveScreenshot( getTestSession(), "versiontest2-opened" );
        versiontestPage.clickResetWem();
        versiontestPage.waitUntilResetWemButtonEnabled();
        SleepHelper.sleep( 100000 );

        then:
        browser.go getSession().getBaseUrl();
        waitFor { title == "Enonic WEM Admin" }
    }


}
