package com.enonic.autotests.pages.modules;


import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.BrowseItemsSelectionPanel;

public class ApplicationBrowseItemsSelectionPanel
    extends BrowseItemsSelectionPanel
{

    {
        CONTAINER = "//div[contains(@id,'ApplicationBrowseItemPanel')]";
        ITEMS_SELECTION_PANEL = CONTAINER + "//div[contains(@id,'ApplicationBrowseItemsSelectionPanel')]";
        ALL_SELECTED_ITEMS = ITEMS_SELECTION_PANEL + "//div[contains(@id,'SelectionItem')]";
        SELECTED_ITEM_NAME = P_NAME;
    }


    public ApplicationBrowseItemsSelectionPanel( final TestSession session )
    {
        super( session );
    }

}