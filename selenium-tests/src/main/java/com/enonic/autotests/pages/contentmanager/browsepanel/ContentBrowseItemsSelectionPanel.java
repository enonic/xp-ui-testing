package com.enonic.autotests.pages.contentmanager.browsepanel;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.BrowseItemsSelectionPanel;

public class ContentBrowseItemsSelectionPanel
    extends BrowseItemsSelectionPanel
{

    {
        CONTAINER = "//div[contains(@id,'ContentBrowsePanel')]";
        ITEMS_SELECTION_PANEL = CONTAINER + "//div[contains(@id,'ContentBrowseItemsSelectionPanel')]";
        ALL_SELECTED_ITEMS = ITEMS_SELECTION_PANEL + "//div[contains(@id,'SelectionItem')]";
    }

    /**
     * @param session
     */
    public ContentBrowseItemsSelectionPanel( TestSession session )
    {
        super( session );
    }
}
