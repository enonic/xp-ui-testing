package com.enonic.autotests.pages.modules;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;

public class ModuleBrowseItemsSelectionPanel
    extends Application
{
    private final String CONTAINER = "//div[contains(@id,'ModuleBrowseItemPanel')]";

    private final String ITEMS_SELECTION_PANEL = CONTAINER + "//div[contains(@id,'app.browse.ModuleBrowseItemsSelectionPanel')]";

    private final String ALL_SELECTED_ITEMS = ITEMS_SELECTION_PANEL + "//div[contains(@id,'api.app.browse.SelectionItem')]";

    private final String SELECTED_ITEM_DISPLAY_NAME = "//p[@class='sub-name']";


    public ModuleBrowseItemsSelectionPanel( final TestSession session )
    {
        super( session );
    }


    public int getSelectedItemCount()
    {
        List<WebElement> divElements = getDriver().findElements( By.xpath( ALL_SELECTED_ITEMS ) );
        return divElements.size();
    }

    public List<String> getSelectedItemDisplayNames()
    {
        if ( !isVisible() )
        {
            return Collections.emptyList();
        }
        List<WebElement> h6Elements = getDriver().findElements( By.xpath( ALL_SELECTED_ITEMS + SELECTED_ITEM_DISPLAY_NAME ) );
        return h6Elements.stream().map( WebElement::getText ).collect( Collectors.toList() );

    }

    public boolean isVisible()
    {
        List<WebElement> elements = findElements( By.xpath( ITEMS_SELECTION_PANEL ) );
        if ( elements.size() == 0 )
        {
            return false;
        }
        else
        {
            return !getAttribute( elements.get( 0 ), "style", 1 ).contains( "display: none" );
        }

    }
}