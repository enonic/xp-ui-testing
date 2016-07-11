package com.enonic.autotests.pages.contentmanager.browsepanel;


import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentDetailsPanel;

public class ContentBrowseItemPanel
    extends Application
{
    public static final String CONTAINER_DIV = "//div[contains(@id,'ContentBrowseItemPanel')]";

    private ContentItemStatisticsPanel contentItemStatisticsPanel;

    public ContentBrowseItemPanel( final TestSession session )
    {
        super( session );
    }

    private ContentDetailsPanel contentDetailsPanel;

    public ContentDetailsPanel getContentDetailsPanel()
    {
        if ( contentDetailsPanel == null )
        {
            contentDetailsPanel = new ContentDetailsPanel( getSession() );
        }
        return contentDetailsPanel;
    }

    public ContentItemStatisticsPanel getContentItemStatisticsPanel()
    {
        if ( contentItemStatisticsPanel == null )
        {
            contentItemStatisticsPanel = new ContentItemStatisticsPanel( getSession() );
        }
        return contentItemStatisticsPanel;
    }

}
