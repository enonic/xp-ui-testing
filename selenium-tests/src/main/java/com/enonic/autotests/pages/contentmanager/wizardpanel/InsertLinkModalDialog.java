package com.enonic.autotests.pages.contentmanager.wizardpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.RichComboBoxInput;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class InsertLinkModalDialog
    extends Application
{
    private final String CONTAINER = "//div[contains(@id,'LinkModalDialog')]";

    private final String URL_INPUT = "//div[contains(@id,'FormItem') and child::label[text()='Url']]//input[@type='text']";

    private final String EMAIL_INPUT = "//div[contains(@id,'FormItem') and child::label[text()='Email']]//input[@type='text']";

    private final String EMAIL_SUBJECT_INPUT = "//div[contains(@id,'FormItem') and child::label[text()='Subject']]//input[@type='text']";

    private final String LINK_TEXT_INPUT = "//div[contains(@id,'FormItem') and child::label[text()='Text']]//input[@type='text']";

    private final String BAR_ITEM_URL = CONTAINER + "//li[contains(@id,'TabBarItem')]/span[text()='URL']";

    private final String BAR_ITEM_CONTENT = CONTAINER + "//li[contains(@id,'TabBarItem')]/span[text()='Content']";

    private final String BAR_ITEM_DOWNLOAD = CONTAINER + "//li[contains(@id,'TabBarItem')]/span[text()='Download']";

    private final String BAR_ITEM_EMAIL = CONTAINER + "//li[contains(@id,'TabBarItem')]/span[text()='Email']";

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

    public InsertLinkModalDialog clickDownloadBarItem()
    {
        getDisplayedElement( By.xpath( BAR_ITEM_DOWNLOAD ) ).click();
        return this;
    }

    public InsertLinkModalDialog clickEmailBarItem()
    {
        getDisplayedElement( By.xpath( BAR_ITEM_EMAIL ) ).click();
        return this;
    }

    public InsertLinkModalDialog typeURL( String url )
    {
        getDisplayedElement( By.xpath( URL_INPUT ) ).sendKeys( url );
        return this;
    }

    public InsertLinkModalDialog typeEmail( String email )
    {
        getDisplayedElement( By.xpath( EMAIL_INPUT ) ).sendKeys( email );
        return this;
    }

    public InsertLinkModalDialog selectOption( String name )
    {
        WebElement optionsInput = getDisplayedElement( By.xpath( CONTAINER + COMBOBOX_OPTION_FILTER_INPUT ) );
        clearAndType( optionsInput, name );
        sleep( 500 );
        RichComboBoxInput richComboBoxInput = new RichComboBoxInput( getSession() );
        richComboBoxInput.selectOption( name );
        return this;
    }

    public InsertLinkModalDialog typeText( String text )
    {
        WebElement textInput = getDisplayedElement( By.xpath( LINK_TEXT_INPUT ) );
        clearAndType( textInput, text );
        return this;
    }

    public InsertLinkModalDialog typeSubject( String subject )
    {
        WebElement textInput = getDisplayedElement( By.xpath( EMAIL_SUBJECT_INPUT ) );
        clearAndType( textInput, subject );
        return this;
    }

    public void pressInsertButton()
    {
        getDisplayedElement( By.xpath( INSERT_OR_UPDATE_BUTTON ) ).click();
        sleep( 400 );
    }

    public void waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( CONTAINER ), Application.EXPLICIT_NORMAL ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_insert_link_dialog" );
            throw new TestFrameworkException( "InsertLink dialog was not opened!" );
        }
    }
}
