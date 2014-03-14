package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class ContentBrowsePanel_GridPanel_FilterSpec
    extends BaseGebSpec
{

    @Shared
    ContentBrowsePanel contentBrowsePanel


    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }

    def "GIVEN No selections in filter WHEN Selecting one entry in ContentTypes-filter THEN all existing Content of the selected type should be listed in gridPanel"()
    {

    }

    def "GIVEN Selections in any filter WHEN clicking clean filter THEN initial grid view displayed "()
    {

    }


}
