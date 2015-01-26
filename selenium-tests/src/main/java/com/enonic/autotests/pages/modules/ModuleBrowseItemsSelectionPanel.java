package com.enonic.autotests.pages.modules;


import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;

public class ModuleBrowseItemsSelectionPanel
    extends Application
{
    private final String CONTAINER = "//div([contains(@id,'ModuleBrowseItemPanel')]";

    private final String ITEMS_SELECTION_PANEL = CONTAINER + "//div([contains(@id,'app.browse.ModuleBrowseItemsSelectionPanel')]";

    public ModuleBrowseItemsSelectionPanel( final TestSession session )
    {
        super( session );
    }
}
