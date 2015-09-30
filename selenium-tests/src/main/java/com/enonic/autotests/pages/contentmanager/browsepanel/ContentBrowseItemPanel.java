package com.enonic.autotests.pages.contentmanager.browsepanel;


import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;

public class ContentBrowseItemPanel
    extends Application
{
    public static final String CONTAINER_DIV = "//div[contains(@id,'ContentBrowseItemPanel')]";

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
}
