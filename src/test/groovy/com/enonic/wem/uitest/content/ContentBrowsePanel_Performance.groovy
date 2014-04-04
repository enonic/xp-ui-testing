package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class ContentBrowsePanel_Performance
    extends BaseGebSpec
{

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    def setup()
    {
        go "admin"

    }

    def "GIVEN admin console WHEN Content app opened THEN grid should appears in 1 second"()
    {
        when:
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );

        then:
        contentBrowsePanel.waituntilPageLoaded( 1 );
    }
}
