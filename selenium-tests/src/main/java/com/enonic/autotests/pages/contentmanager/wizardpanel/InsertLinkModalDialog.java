package com.enonic.autotests.pages.contentmanager.wizardpanel;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;


public class InsertLinkModalDialog
    extends Application
{
    private final String CONTAINER = "//div[contains(@id,'LinkModalDialog')]";

    private final String URL_INPUT = "//div[contains(@id,'FormItem') and child::label[text()='Url']]//input[@type='text']";

    private final String BAR_ITEM_URL = CONTAINER + "//li[contains(@id,'TabBarItem')]/span[text()='URL']";

    public InsertLinkModalDialog( TestSession session )
    {
        super( session );
    }

    public InsertLinkModalDialog clickURLBarItem()
    {
        getDisplayedElement( By.xpath( BAR_ITEM_URL ) ).click();
        return this;
    }

    public InsertLinkModalDialog typeURL( String url )
    {
        getDisplayedElement( By.xpath( URL_INPUT ) ).sendKeys( url );
        return this;
    }

    public void pressInsertButton()
    {

    }
}
