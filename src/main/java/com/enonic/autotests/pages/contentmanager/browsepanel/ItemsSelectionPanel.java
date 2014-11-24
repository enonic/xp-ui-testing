package com.enonic.autotests.pages.contentmanager.browsepanel;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;

public class ItemsSelectionPanel
    extends Application
{
    private final String ALL_SELECTED_ITEMS =
        "//div[contains(@class,'panel items-selection-panel')]//div[contains(@id,'api.app.browse.SelectionItem')]";

    private final String SELECTED_ITEM_DISPLAY_NAME = "//h6";

    /**
     * @param session
     */
    public ItemsSelectionPanel( TestSession session )
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
        List<WebElement> h6Elements = getDriver().findElements( By.xpath( ALL_SELECTED_ITEMS + SELECTED_ITEM_DISPLAY_NAME ) );
        return h6Elements.stream().map( WebElement::getText ).collect( Collectors.toList() );

    }

}
