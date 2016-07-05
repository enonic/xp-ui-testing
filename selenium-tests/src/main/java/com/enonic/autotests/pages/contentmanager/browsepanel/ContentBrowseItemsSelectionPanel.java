package com.enonic.autotests.pages.contentmanager.browsepanel;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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
    }

    private String SHOW_ALL_BUTTON = ITEMS_SELECTION_PANEL + "//button[child::span[contains(.,'Show All')]]";

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
        List<WebElement> h6Elements = getDriver().findElements( By.xpath( ALL_SELECTED_ITEMS + H6_DISPLAY_NAME ) );
        return h6Elements.stream().filter( e -> !e.getText().isEmpty() ).filter( WebElement::isDisplayed ).map(
            WebElement::getText ).collect( Collectors.toList() );
    }

    public boolean isShowAllButtonDisplayed()
    {
        return isElementDisplayed( SHOW_ALL_BUTTON );
    }
}
