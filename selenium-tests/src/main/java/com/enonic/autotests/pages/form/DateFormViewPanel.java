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

    private final String DATE_INPUT_XPATH =
        FORM_VIEW + "//div[contains(@id,'api.form.InputView') and descendant::div[@title='Date']]//input[contains(@id,'TextInput')]";

    @FindBy(xpath = DATE_INPUT_XPATH)
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

    public boolean isInvalidDate()
    {
        return waitAndCheckAttrValue( dateInput, "class", "invalid", 1 );
    }

    public String getDateValue()
    {
        return dateInput.getAttribute( "value" );
    }
}
