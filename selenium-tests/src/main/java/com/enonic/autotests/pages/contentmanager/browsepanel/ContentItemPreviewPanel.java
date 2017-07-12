package com.enonic.autotests.pages.contentmanager.browsepanel;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;

/**
 * The preview panel foe showing of info, when a content selected in the grid
 */
public class ContentItemPreviewPanel
    extends Application
{
    private final String PREVIEW_PANEL = "//div[contains(@id,'ContentItemPreviewPanel')]";

    private String NO_PREVIEW = "//div[contains(@id,'ContentItemPreviewPanel') and contains(@class,'no-preview')]";

    private String IMAGE_PREVIEW = "//div[contains(@id,'ContentItemPreviewPanel') and contains(@class,'image-preview')]";

    private String SVG_PREVIEW = "//div[contains(@id,'ContentItemPreviewPanel') and contains(@class,'svg-preview')]";

    private String PAGE_PREVIEW = "//div[contains(@id,'ContentItemPreviewPanel') and contains(@class,'page-preview')]";


    public ContentItemPreviewPanel( final TestSession session )
    {
        super( session );
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( PREVIEW_PANEL );
    }
    public boolean isPreviewNotAvailAble()
    {
        return isElementDisplayed( NO_PREVIEW );
    }

    public boolean isImageDisplayed()
    {
        return isElementDisplayed( IMAGE_PREVIEW );
    }

    public boolean isSVGDisplayed()
    {
        return isElementDisplayed( SVG_PREVIEW );
    }

    public boolean isPageDisplayed()
    {
        return isElementDisplayed( PAGE_PREVIEW );
    }
}
