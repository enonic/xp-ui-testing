package com.enonic.autotests.pages.contentmanager.browsepanel;

import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.BrowseItemsSelectionPanel;
import com.enonic.autotests.utils.SleepHelper;

public class ContentBrowseItemsSelectionPanel
    extends BrowseItemsSelectionPanel
{

    {
        CONTAINER = "//div[contains(@id,'ContentBrowsePanel')]";
        ITEMS_SELECTION_PANEL = CONTAINER + "//div[contains(@id,'ContentBrowseItemsSelectionPanel')]";
        ALL_SELECTED_ITEMS = ITEMS_SELECTION_PANEL + "//div[contains(@id,'SelectionItem')]";
        CLEAR_SELECTION_BUTTON = ITEMS_SELECTION_PANEL + "//button[child::span[contains(.,'Clear Selection')]]";
        SHOW_ALL_BUTTON = ITEMS_SELECTION_PANEL + "//button[child::span[contains(.,'Show All')]]";
    }

    /**
     * @param session
     */
    public ContentBrowseItemsSelectionPanel( TestSession session )
    {
        super( session );
    }

    public List<String> getDisplayNameOfSelectedItems()
    {
        if ( !isVisible() )
        {
            return Collections.emptyList();
        }
        if ( isShowAllButtonDisplayed() )
        {
            getDisplayedElement( By.xpath( SHOW_ALL_BUTTON ) ).click();
            SleepHelper.sleep( 500 );
        }
        return getDisplayedStrings( By.xpath( ALL_SELECTED_ITEMS + H6_DISPLAY_NAME ) );
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
