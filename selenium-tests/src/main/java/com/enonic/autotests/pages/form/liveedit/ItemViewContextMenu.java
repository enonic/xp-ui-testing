package com.enonic.autotests.pages.form.liveedit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.services.NavigatorHelper;


public class ItemViewContextMenu
    extends Application
{
    public final String CONTAINER_DIV = "//div[contains(@id,'api.liveedit.ItemViewContextMenu')]";

    public ItemViewContextMenu( TestSession session )
    {
        super( session );
    }

    public boolean isOpened()
    {
        //return findElements( By.xpath( CONTAINER_DIV ) ).stream().filter(
        //  e -> !e.getAttribute( "style" ).contains( "display: block" ) ).count() == 1;
        return findElements( By.xpath( CONTAINER_DIV ) ).stream().filter( WebElement::isDisplayed ).count() == 1;
    }

    public void clickOnCustomizeMenuItem()
    {
        findElements( By.xpath( CONTAINER_DIV + "//dl//dt[text()='Customize']" ) ).stream().filter(
            WebElement::isDisplayed ).findFirst().get().click();
        NavigatorHelper.switchToContentManagerFrame( getSession() );

    }
}
