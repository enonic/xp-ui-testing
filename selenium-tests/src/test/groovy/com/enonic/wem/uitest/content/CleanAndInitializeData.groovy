package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.WemStartPage
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.SleepHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class CleanAndInitializeData
    extends BaseGebSpec
{
    @Shared
    WemStartPage startPage;


    def setup()
    {
        String resetUrl = "http://localhost:8080"

        go resetUrl;
        startPage = new WemStartPage( getTestSession() );

    }

    def "clean data and initialize demo data"()
    {
        when: "click on 'clear data' link and click on 'initialize data' link"
        TestUtils.saveScreenshot( getTestSession(), "startPage-opened" );
        startPage.clickOnClearData();

        SleepHelper.sleep( 4000 );
        TestUtils.saveScreenshot( getSession(), "after_clean" )
        startPage.clickOnInitializeData();
        SleepHelper.sleep( 30000 );

        go "admin"
        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
        TestUtils.saveScreenshot( getTestSession(), "cm-opened" );

        then: "not empty grid should be showed"
        contentBrowsePanel.waitUntilPageLoaded( 5 );
    }


}
