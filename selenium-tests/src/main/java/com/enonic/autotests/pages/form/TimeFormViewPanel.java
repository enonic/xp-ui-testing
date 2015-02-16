package com.enonic.autotests.pages.form;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public class TimeFormViewPanel
    extends FormViewPanel
{
    public static String REQUIRED_TIME_PROPERTY = "req_time";

    private final String FORM_VIEW = "//div[contains(@id,'api.form.FormView')]";

    @FindBy(xpath = FORM_VIEW +
        "//div[contains(@id,'api.form.InputView') and descendant::div[contains(@title,'Required Time')]]//input[contains(@id,'TextInput')]")
    private WebElement reqTimeInput;


    public TimeFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String time = data.getString( REQUIRED_TIME_PROPERTY );
        // type a date time
        reqTimeInput.sendKeys( time );
        sleep( 300 );
        return this;
    }

    public String getRequiredTimeValue()
    {
        return reqTimeInput.getAttribute( "value" );
    }
}