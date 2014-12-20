package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class ContentBrowsePanelToolbarSpec
    extends BaseGebSpec
{
    @Shared
    ContentBrowsePanel contentBrowsePanel;


    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }

    def "GIVEN Content BrowsePanel WHEN no selected content THEN Delete button should be disabled"()
    {
        expect:
        !contentBrowsePanel.isDeleteButtonEnabled();
    }

    def "GIVEN Content BrowsePanel WHEN no selected content THEN New button should be enabled"()
    {
        expect:
        contentBrowsePanel.isNewButtonEnabled();
    }
}
