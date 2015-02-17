package com.enonic.autotests.pages.form;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class DateFormViewPanel
    extends FormViewPanel
{
    public static String DATE_PROPERTY = "date";


    @FindBy(xpath = FORM_VIEW +
        "//div[contains(@id,'api.form.InputView') and descendant::div[@title='Date']]//input[contains(@id,'TextInput')]")
    private WebElement dateInput;


    public DateFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String date = data.getString( DATE_PROPERTY );
        // type a date
        dateInput.sendKeys( date );
        sleep( 300 );
        return this;
    }

    public String getDateValue()
    {
        return dateInput.getAttribute( "value" );
    }
}
