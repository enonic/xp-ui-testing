package com.enonic.autotests.pages.contentmanager.browsepanel;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.BrowseItemsSelectionPanel;

public class ContentBrowseItemsSelectionPanel
    extends BrowseItemsSelectionPanel
{
    public static final String VERSION_HISTORY = "Version History";

    public static final String PREVIEW = "Preview";

    {
        CONTAINER = "//div[contains(@id,'app.browse.ContentBrowsePanel')]";
        ITEMS_SELECTION_PANEL = CONTAINER + "//div[contains(@id,'app.browse.ContentBrowseItemsSelectionPanel')]";
        ALL_SELECTED_ITEMS = ITEMS_SELECTION_PANEL + "//div[contains(@id,'api.app.browse.SelectionItem')]";

    }

    /**
     * @param session
     */
    public ContentBrowseItemsSelectionPanel( TestSession session )
    {
        super( session );
    }
}
