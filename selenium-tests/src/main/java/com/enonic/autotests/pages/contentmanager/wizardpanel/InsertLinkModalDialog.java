package com.enonic.autotests.pages.contentmanager.wizardpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class InsertLinkModalDialog
    extends Application
{
    private final String CONTAINER = "//div[contains(@id,'LinkModalDialog')]";

    private final String URL_INPUT = "//div[contains(@id,'FormItem') and child::label[text()='Url']]//input[@type='text']";

    private final String LINK_TEXT_INPUT = "//div[contains(@id,'FormItem') and child::label[text()='Text']]//input[@type='text']";

    private final String BAR_ITEM_URL = CONTAINER + "//li[contains(@id,'TabBarItem')]/span[text()='URL']";

    private final String BAR_ITEM_CONTENT = CONTAINER + "//li[contains(@id,'TabBarItem')]/span[text()='Content']";

    private final String INSERT_OR_UPDATE_BUTTON =
        CONTAINER + "//button[contains(@id,'DialogButton') and child::span[text()='Insert' or text()='Update']]";


    public InsertLinkModalDialog( TestSession session )
    {
        super( session );
    }

    public InsertLinkModalDialog clickURLBarItem()
    {
        getDisplayedElement( By.xpath( BAR_ITEM_URL ) ).click();
        return this;
    }

    public InsertLinkModalDialog clickContentBarItem()
    {
        getDisplayedElement( By.xpath( BAR_ITEM_CONTENT ) ).click();
        return this;
    }

    public InsertLinkModalDialog typeURL( String url )
    {
        getDisplayedElement( By.xpath( URL_INPUT ) ).sendKeys( url );
        return this;
    }

    public InsertLinkModalDialog selectOption( String name )
    {
        WebElement optionsInput = getDisplayedElement( By.xpath( CONTAINER + COMBOBOX_OPTION_FILTER_INPUT ) );
        clearAndType( optionsInput, name );
        sleep( 500 );

        getDisplayedElement( By.xpath( String.format( COMBOBOX_OPTIONS_ITEM_BY_DISPLAY_NAME, name ) ) ).click();
        return this;
    }

    public InsertLinkModalDialog typeText( String text )
    {
        WebElement textInput = getDisplayedElement( By.xpath( LINK_TEXT_INPUT ) );
        clearAndType( textInput, text );
        return this;
    }

    public void pressInsertButton()
    {
        getDisplayedElement( By.xpath( INSERT_OR_UPDATE_BUTTON ) ).click();
        sleep( 400 );
    }
}
