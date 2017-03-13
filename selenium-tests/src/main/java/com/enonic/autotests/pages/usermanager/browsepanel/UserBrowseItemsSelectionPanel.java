package com.enonic.autotests.pages.usermanager.browsepanel;

import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.BrowseItemsSelectionPanel;

/**
 * Created on 3/10/2017.
 */
public class UserBrowseItemsSelectionPanel
    extends BrowseItemsSelectionPanel
{

    {
        CONTAINER = "//div[contains(@id,'UserBrowsePanel')]";
        ITEMS_SELECTION_PANEL = CONTAINER + "//div[contains(@id,'UserBrowseItemsSelectionPanel')]";
        ALL_SELECTED_ITEMS = ITEMS_SELECTION_PANEL + "//div[contains(@id,'SelectionItem')]";
        CLEAR_SELECTION_BUTTON = ITEMS_SELECTION_PANEL + "//button[child::span[contains(.,'Clear Selection')]]";
    }

    /**
     * @param session
     */
    public UserBrowseItemsSelectionPanel( TestSession session )
    {
        super( session );
    }

    public List<String> getDisplayNameOfSelectedItems()
    {
        if ( !isVisible() )
        {
            return Collections.emptyList();
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
        return false;
    }

    @Override
    public String getNumberInClearSelectionLink()
    {
        String xpath = CLEAR_SELECTION_BUTTON + "/span";
        String linkText = getDisplayedString( xpath );
        return linkText.substring( linkText.indexOf( "(" ) + 1, linkText.indexOf( ")" ) );
    }
}