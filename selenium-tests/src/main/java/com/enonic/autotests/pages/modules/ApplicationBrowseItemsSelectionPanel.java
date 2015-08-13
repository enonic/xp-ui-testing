package com.enonic.autotests.pages.modules;


import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.BrowseItemsSelectionPanel;

public class ApplicationBrowseItemsSelectionPanel
    extends BrowseItemsSelectionPanel
{

    {
        CONTAINER = "//div[contains(@id,'ApplicationBrowseItemPanel')]";
        ITEMS_SELECTION_PANEL = CONTAINER + "//div[contains(@id,'app.browse.ApplicationBrowseItemsSelectionPanel')]";
        ALL_SELECTED_ITEMS = ITEMS_SELECTION_PANEL + "//div[contains(@id,'api.app.browse.SelectionItem')]";
        SELECTED_ITEM_NAME = "//p[@class='sub-name']";
    }


    public ApplicationBrowseItemsSelectionPanel( final TestSession session )
    {
        super( session );
    }

}