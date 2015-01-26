package com.enonic.autotests.pages;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;

public abstract class BrowseItemsSelectionPanel
    extends Application
{
    protected String CONTAINER;

    protected String ITEMS_SELECTION_PANEL;

    protected String ALL_SELECTED_ITEMS;

    protected String SELECTED_ITEM_NAME;


    public BrowseItemsSelectionPanel( final TestSession session )
    {
        super( session );
    }


    public int getSelectedItemCount()
    {
        List<WebElement> divElements = getDriver().findElements( By.xpath( ALL_SELECTED_ITEMS ) );
        return divElements.size();
    }

    public List<String> getSelectedItemNames()
    {
        if ( !isVisible() )
        {
            return Collections.emptyList();
        }
        List<WebElement> h6Elements = getDriver().findElements( By.xpath( ALL_SELECTED_ITEMS + SELECTED_ITEM_NAME ) );
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
