package com.enonic.autotests.pages.form;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.contentmanager.wizardpanel.date.TimePickerPopup;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;


public class TimeFormViewPanel
    extends FormViewPanel
{
    public static String TIME_PROPERTY = "time";

    private final String TIME_PICKER = "//div[contains(@id,'TimePicker')]";

    private final String ICON_CLOCK = TIME_PICKER + "//button[contains(@class,'icon-clock')]";

    @FindBy(xpath = FORM_VIEW + TIME_PICKER + "//input[contains(@id,'TextInput')]")
    private WebElement timeInput;

    @FindBy(xpath = FORM_VIEW + ICON_CLOCK)
    private WebElement iconClockButton;

    public TimeFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        String time = data.getString( TIME_PROPERTY );
        // type a date time
        timeInput.sendKeys( time );
        sleep( 300 );
        return this;
    }

    public TimePickerPopup clickOnInputAndShowPicker()
    {
        Actions builder = new Actions( getDriver() );
        builder.click( timeInput ).build().perform();
        TimePickerPopup popup = new TimePickerPopup( getSession() );
        popup.waitUntilDialogLoaded();
        return popup;
    }

    public boolean isTimeInvalid()
    {
        return waitAndCheckAttrValue( timeInput, "class", "invalid", 1l );
    }

    public String getTimeValue()
    {
        return timeInput.getAttribute( "value" );
    }
}