package com.enonic.autotests.pages.form;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.wem.api.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public class DateTimeFormViewPanel
    extends FormViewPanel
{
    public static String REQUIRED_DATE_TIME_PROPERTY = "req_date_time";

    private final String FORM_VIEW = "//div[contains(@id,'api.form.FormView')]";

    @FindBy(xpath = FORM_VIEW +
        "//div[contains(@id,'api.form.InputView') and descendant::div[contains(@title,'Required Date')]]//input[contains(@id,'TextInput')]")
    private WebElement reqDateTimeInput;


    public DateTimeFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String description = data.getString( REQUIRED_DATE_TIME_PROPERTY );
        // type a date time
        reqDateTimeInput.sendKeys( description );
        sleep( 300 );
        return this;
    }

    public String getRequiredDateTimeValue()
    {
        return reqDateTimeInput.getAttribute( "value" );
    }
}
