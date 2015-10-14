package com.enonic.autotests.pages.contentmanager.browsepanel;


import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;

public class ContentInfoWidget
    extends Application
{
    private final String STATUS_TEXT = ContentDetailsPanel.DETAILS_PANEL + "//div[contains(@id,'detail.StatusWidgetItemView')]/span";

    public ContentInfoWidget( final TestSession session )
    {
        super( session );
    }

    public String getContentStatus()
    {
        return getDisplayedString( STATUS_TEXT );
    }
}
