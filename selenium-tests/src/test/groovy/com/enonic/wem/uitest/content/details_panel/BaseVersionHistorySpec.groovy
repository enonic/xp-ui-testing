package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentDetailsPanel
import com.enonic.wem.uitest.content.BaseContentSpec

class BaseVersionHistorySpec
    extends BaseContentSpec
{
    protected AllContentVersionsView openVersionPanel()
    {
        contentBrowsePanel.clickOnDetailsToggleButton();
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        ContentDetailsPanel contentDetailsPanel = contentBrowsePanel.getContentDetailsPanel();
        AllContentVersionsView contentItemVersionsPanel = contentDetailsPanel.openVersionHistory();
        return contentItemVersionsPanel;
    }
}
