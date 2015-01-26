package com.enonic.autotests.pages.modules;


import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.BrowseItemsSelectionPanel;

public class ModuleBrowseItemsSelectionPanel
    extends BrowseItemsSelectionPanel
{

    {
        CONTAINER = "//div[contains(@id,'ModuleBrowseItemPanel')]";
        ITEMS_SELECTION_PANEL = CONTAINER + "//div[contains(@id,'app.browse.ModuleBrowseItemsSelectionPanel')]";
        ALL_SELECTED_ITEMS = ITEMS_SELECTION_PANEL + "//div[contains(@id,'api.app.browse.SelectionItem')]";
    }


    public ModuleBrowseItemsSelectionPanel( final TestSession session )
    {
        super( session );
    }

}