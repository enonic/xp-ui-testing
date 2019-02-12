package com.enonic.autotests.pages.contentmanager.wizardpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.LoaderComboBox;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class InsertLinkModalDialog
    extends Application
{
    public static String VALIDATION_MESSAGE = "This field is required";

    private final String CONTAINER = "//div[contains(@id,'LinkModalDialog')]";

    private final String URL_INPUT = "//div[contains(@id,'FormItem') and child::label[text()='Url']]//input[@type='text']";

    private final String EMAIL_INPUT = "//div[contains(@id,'FormItem') and child::label[text()='Email']]//input[@type='text']";

    private final String EMAIL_SUBJECT_INPUT = "//div[contains(@id,'FormItem') and child::label[text()='Subject']]//input[@type='text']";

    private final String LINK_TEXT_INPUT = "//div[contains(@id,'FormItem') and child::label[text()='Text']]//input[@type='text']";

    private final String LINK_TEXT_FIELDSET = CONTAINER + "//fieldset[contains(id,'Fieldset') and child::label[text()='Text']]";

    private final String LINK_TEXT_VALIDATION_MESSAGE =
        "//div[contains(@id,'FormItem') and child::label[text()='Text']]/.." + VALIDATION_RECORDING_VIEWER;

    private final String LINK_URL_VALIDATION_MESSAGE =
        "//div[contains(@id,'FormItem') and child::label[text()='Url']]/.." + VALIDATION_RECORDING_VIEWER;

    private final String BAR_ITEM_URL = CONTAINER + String.format( TAB_BAR_ITEM, "URL" );

    private final String BAR_ITEM_CONTENT = CONTAINER + String.format( TAB_BAR_ITEM, "Content" );

    private final String BAR_ITEM_DOWNLOAD = CONTAINER + String.format( TAB_BAR_ITEM, "Download" );

    private final String BAR_ITEM_EMAIL = CONTAINER + String.format( TAB_BAR_ITEM, "Email" );

    private final String INSERT_OR_UPDATE_BUTTON =
        CONTAINER + "//button[contains(@id,'DialogButton') and child::span[text()='Insert' or text()='Update']]";

    public InsertLinkModalDialog( TestSession session )
    {
        super( session );
    }

    public boolean isValidationMessageForTextInputDisplayed()
    {
        return isElementDisplayed( By.xpath( LINK_TEXT_VALIDATION_MESSAGE ) );
    }

    public boolean isValidationMessageForUrlInputDisplayed()
    {
        return isElementDisplayed( By.xpath( LINK_URL_VALIDATION_MESSAGE ) );
    }


    public String getValidationMessageForTextInput()
    {
        return getDisplayedString( LINK_TEXT_VALIDATION_MESSAGE );
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

    public InsertLinkModalDialog doFilterComboBoxOption( String name )
    {
        WebElement optionsInput = getDisplayedElement( By.xpath( CONTAINER + COMBOBOX_OPTION_FILTER_INPUT ) );
        clearAndType( optionsInput, name );
        return this;
    }

    public boolean isNoMatchingItemsInComboBox()
    {
        String message =
            CONTAINER + "//div[contains(@id,'ContentComboBox')]" + "//div[@class='empty-options' and text()='No matching items']";
        return isElementDisplayed( message );
    }

    public InsertLinkModalDialog selectComboBoxOption( String name )
    {
        doFilterComboBoxOption( name );
        sleep( 500 );
        LoaderComboBox loaderComboBox = new LoaderComboBox( getSession() );
        loaderComboBox.selectOption( name );
        return this;
    }

    public InsertLinkModalDialog typeText( String text )
    {
        WebElement textInput = getDisplayedElement( By.xpath( LINK_TEXT_INPUT ) );
        clearAndType( textInput, text );
        sleep( 300 );
        return this;
    }

    public InsertLinkModalDialog typeSubject( String subject )
    {
        WebElement textInput = getDisplayedElement( By.xpath( EMAIL_SUBJECT_INPUT ) );
        clearAndType( textInput, subject );
        sleep( 500 );
        return this;
    }

    public InsertLinkModalDialog pressInsertButton()
    {
        getDisplayedElement( By.xpath( INSERT_OR_UPDATE_BUTTON ) ).click();
        sleep( 1000 );
        return this;
    }

    public boolean waitForDialogClosed()
    {
        boolean isDialogInvisible = waitInvisibilityOfElement( By.xpath( CONTAINER ), Application.EXPLICIT_NORMAL );
        if ( !isDialogInvisible )
        {
            saveScreenshot( "err_insert_link_dialog_not_closed" );
            throw new TestFrameworkException( "'Insert Link' dialog was not closed" );
        }
        return isDialogInvisible;
    }

    public void waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( CONTAINER ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_insert_link_dialog" );
            throw new TestFrameworkException( "InsertLink dialog was not opened!" );
        }
    }
}
