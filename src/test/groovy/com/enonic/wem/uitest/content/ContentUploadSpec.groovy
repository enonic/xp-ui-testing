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

    def "GIVEN "()
    {
        when: "un expand a parent content "
        contentBrowsePanel.clickToolbarNew().doUploadFile( path );
        TestUtils.saveScreenshot( getSession(), "ss" )

        then: ""
        contentBrowsePanel.exists( ContentPath.from( "ea-png" ) )
    }
}