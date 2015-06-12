package com.enonic.autotests.pages.form;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public class TimeFormViewPanel
    extends FormViewPanel
{
    public static String TIME_PROPERTY = "time";

    private final String FORM_VIEW = "//div[contains(@id,'api.form.FormView')]";

    @FindBy(xpath = FORM_VIEW + "//div[contains(@id,'TimePicker')]//input[contains(@id,'TextInput')]")
    private WebElement reqTimeInput;


    public TimeFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String time = data.getString( TIME_PROPERTY );
        // type a date time
        reqTimeInput.sendKeys( time );
        sleep( 300 );
        return this;
    }

    public String getTimeValue()
    {
        return reqTimeInput.getAttribute( "value" );
    }
}