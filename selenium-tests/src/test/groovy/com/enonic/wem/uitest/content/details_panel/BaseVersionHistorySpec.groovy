package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.VersionHistoryWidget
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentDetailsPanel
import com.enonic.wem.uitest.content.BaseContentSpec

class BaseVersionHistorySpec
    extends BaseContentSpec
{
    protected VersionHistoryWidget openVersionPanel()
    {
        contentBrowsePanel.openContentDetailsPanel();
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        ContentDetailsPanel contentDetailsPanel = contentBrowsePanel.getContentDetailsPanel();
        VersionHistoryWidget contentItemVersionsPanel = contentDetailsPanel.openVersionHistory();
        contentItemVersionsPanel.waitUntilLoaded();
        sleep( 500 );
        return contentItemVersionsPanel;
    }
}
