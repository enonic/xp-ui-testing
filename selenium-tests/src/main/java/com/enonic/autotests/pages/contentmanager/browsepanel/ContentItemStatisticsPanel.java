package com.enonic.autotests.pages.contentmanager.browsepanel;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;

public class ContentItemStatisticsPanel
    extends Application
{

    private final String STATISTIC_PANEL_CONTAINER = "//div[contains(@id,'ContentItemStatisticsPanel')]";

    private ContentItemPreviewPanel contentItemPreviewPanel;

    public ContentItemStatisticsPanel( final TestSession session )
    {
        super( session );
    }

    public ContentItemPreviewPanel getContentItemPreviewPanel()
    {
        if ( contentItemPreviewPanel == null )
        {
            contentItemPreviewPanel = new ContentItemPreviewPanel( getSession() );
        }
        return contentItemPreviewPanel;
    }
}
