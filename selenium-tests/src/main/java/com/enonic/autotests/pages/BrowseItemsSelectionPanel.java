package com.enonic.autotests.pages;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public abstract class BrowseItemsSelectionPanel
    extends Application
{
    protected String CONTAINER;

    protected String ITEMS_SELECTION_PANEL;

    protected String ALL_SELECTED_ITEMS;

    protected String SELECTED_ITEM_NAME = "//p[@class='sub-name']";

    protected String SELECTED_ITEM = "//div[contains(@id,'SelectionItem')]";


    public BrowseItemsSelectionPanel( final TestSession session )
    {
        super( session );
    }

    public int getSelectedItemCount()
    {
        List<WebElement> divElements = getDisplayedElements( By.xpath( ALL_SELECTED_ITEMS ) );
        return divElements.size();
    }

    public BrowseItemsSelectionPanel removeItem( String itemName )
    {
        String removeButton =
            CONTAINER + SELECTED_ITEM + String.format( NAMES_VIEW_BY_NAME, itemName ) + "/../../..//div[@class='icon remove']";
        if ( !isElementDisplayed( removeButton ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_" + "icon_" + itemName );
            throw new TestFrameworkException( "remove-icon for item: " + itemName + " was not found!" );
        }
        getDisplayedElement( By.xpath( removeButton ) ).click();
        sleep( 400 );

        List<String> contentList = new ArrayList<>();
        String[] aa = {"Agra", "Mysore", "Chandigarh", "Bhopal"};
        contentList.addAll( Arrays.asList( aa ) );
        return this;
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

    public List<String> getDisplayNameOfSelectedItems()
    {
        if ( !isVisible() )
        {
            return Collections.emptyList();
        }
        List<WebElement> h6Elements = getDriver().findElements( By.xpath( ALL_SELECTED_ITEMS + H6_DISPLAY_NAME ) );
        return h6Elements.stream().filter( e -> !e.getText().isEmpty() ).filter( WebElement::isDisplayed ).map(
            WebElement::getText ).collect( Collectors.toList() );
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
