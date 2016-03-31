package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentDetailsPanel
import com.enonic.wem.uitest.content.BaseContentSpec

class BaseVersionHistorySpec
    extends BaseContentSpec
{
    protected AllContentVersionsView openVersionPanel()
    {
        contentBrowsePanel.clickOnDetailsToggleButton();
        ContentDetailsPanel contentDetailsPanel = contentBrowsePanel.getContentDetailsPanel();
        AllContentVersionsView contentItemVersionsPanel = contentDetailsPanel.openVersionHistory();
        return contentItemVersionsPanel;
    }
}
