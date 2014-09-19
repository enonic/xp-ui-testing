package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.versiontest.VersiontestPage
import com.enonic.autotests.utils.SleepHelper
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class ResetAppSpec
    extends BaseGebSpec
{
    @Shared
    VersiontestPage versiontestPage;


    def setup()
    {
        go "http://versiontest2/"
        versiontestPage = new VersiontestPage( getTestSession() );

    }

    def "clickOnReset"()
    {
        when:
        versiontestPage.clickResetWem();
        versiontestPage.waitUntilResetWemButtonEnabled();
        SleepHelper.sleep( 100000 );

        then:
        browser.go getSession().getBaseUrl();
        waitFor { title == "Enonic WEM Admin" }
    }


}
