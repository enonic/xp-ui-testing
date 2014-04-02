package com.enonic.autotests.pages.contentmanager.browsepanel;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;

public class ItemsSelectionPanel
    extends Application
{

    public final String SELECTED_ITEMS_DIV_XPATH =
        "//div[contains(@id,'api.app.browse.SelectionItem') and contains(@class,'browse-selection-item')]";

    /**
     * @param session
     */
    public ItemsSelectionPanel( TestSession session )
    {
        super( session );
    }

    public int getSeletedItemCount()
    {
        List<WebElement> divElements = getDriver().findElements( By.xpath( SELECTED_ITEMS_DIV_XPATH ) );
        return divElements.size();
    }

}
