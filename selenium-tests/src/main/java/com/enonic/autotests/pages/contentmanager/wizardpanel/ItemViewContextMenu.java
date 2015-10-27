package com.enonic.autotests.pages.contentmanager.wizardpanel;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;

public class ItemViewContextMenu
    extends Application
{
    private final String CONTAINER = "//div[contains(@id,'ItemViewContextMenu')]";


    @FindBy(xpath = CONTAINER)
    WebElement container;

    public ItemViewContextMenu( TestSession session )
    {
        super( session );
    }

    public List<String> getMenuItems()
    {
        List<String> menuItems = getDisplayedStrings( By.xpath( "" ) );
        return menuItems;
    }

    public ItemViewContextMenu selectItem( String itemName )
    {
        return this;
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( CONTAINER );
    }
}
