package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class ContentUploadSpec
    extends BaseGebSpec
{
    @Shared
    ContentBrowsePanel contentBrowsePanel;

    @Shared
    String path = "test-data/upload/ea.png";

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }

    def "GIVEN opened a new content dialog WHEN drop zone clicked and file selected THEN new content present in browse panel "()
    {
        when: "un expand a parent content "
        contentBrowsePanel.clickToolbarNew().doUploadFile( path );
        TestUtils.saveScreenshot( getSession(), "uploadpng1" )

        then: ""
        TestUtils.saveScreenshot( getSession(), "uploadpng2" )
        contentBrowsePanel.exists( ContentPath.from( "ea-png" ) )
    }
}