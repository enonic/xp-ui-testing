package com.enonic.autotests.pages.form;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.google.common.base.Strings;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.contentmanager.wizardpanel.date.DateTimePickerPopup;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public class DateTimeFormViewPanel
    extends FormViewPanel
{
    public static String DATE_TIME_PROPERTY = "date_time";

    @FindBy(xpath = FORM_VIEW + DATA_TIME_PICKER_INPUT)
    private WebElement dateTimeInput;


    public DateTimeFormViewPanel( final TestSession session )
    {
        super( session );
    }

    public DateTimePickerPopup showPicker()
    {
        Actions builder = new Actions( getDriver() );
        builder.click( dateTimeInput ).build().perform();
        DateTimePickerPopup popup = new DateTimePickerPopup( getSession() );
        popup.waitUntilDialogLoaded();
        return popup;
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String dateTime = data.getString( DATE_TIME_PROPERTY );
        // type a date time
        if ( !Strings.isNullOrEmpty( dateTime ) )
        {
            typeDateTime( dateTime );
            sleep( 300 );
        }
        return this;
    }

    private void typeDateTime( String dateTime )
    {
        dateTimeInput.clear();
        sleep( 300 );
        String[] parts = dateTime.split( " " );
        String date = parts[0];
        String time = parts[1];
        dateTimeInput.sendKeys( date );
        sleep( 300 );
        buildActions().click( dateTimeInput ).build().perform();
        dateTimeInput.sendKeys( " " );
        sleep( 300 );
        dateTimeInput.sendKeys( time );
    }

    protected JavascriptExecutor getJavaScriptExecutor()
    {
        return (JavascriptExecutor) getDriver();
    }

    public void typeDateTime1( String datetime )
    {
        clearAndType( dateTimeInput, datetime );
        sleep( 500 );
    }

    public boolean isDateTimeInputDisplayed()
    {
        return dateTimeInput.isDisplayed();
    }

    public String getDateTimeValue()
    {
        return dateTimeInput.getAttribute( "value" );
    }

    public boolean isDateTimeInvalid()
    {
        return waitAndCheckAttrValue( dateTimeInput, "class", "invalid", 1l );
    }
}
