package com.enonic.autotests.pages.form;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public class DateTimeFormViewPanel
    extends FormViewPanel
{
    public static String DATE_TIME_PROPERTY = "date_time";

    @FindBy(xpath = FORM_VIEW +
        "//div[contains(@id,'api.form.InputView') and descendant::div[@title='DateTime']]//input[contains(@id,'TextInput')]")
    private WebElement dateTimeInput;


    public DateTimeFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String dateTime = data.getString( DATE_TIME_PROPERTY );
        // type a date time
        dateTimeInput.sendKeys( dateTime );
        sleep( 300 );
        return this;
    }

    public String getDateTimeValue()
    {
        return dateTimeInput.getAttribute( "value" );
    }
}
