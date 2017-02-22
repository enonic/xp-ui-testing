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
        SHOW_ALL_BUTTON = ITEMS_SELECTION_PANEL + "//button[child::span[contains(.,'Show All')]]";
    }

    public ApplicationBrowseItemsSelectionPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public boolean isClearSelectionLinkDisplayed()
    {
        return isElementDisplayed( CLEAR_SELECTION_BUTTON );
    }

    @Override
    public boolean isShowAllButtonDisplayed()
    {
        return isElementDisplayed( SHOW_ALL_BUTTON );
    }
    @Override
    public String getNumberInClearSelectionLink()
    {
        String xpath = CLEAR_SELECTION_BUTTON + "/span";
        String linkText = getDisplayedString( xpath );
        return linkText.substring( linkText.indexOf( "(" )+1, linkText.indexOf( ")" ) );
    }

}