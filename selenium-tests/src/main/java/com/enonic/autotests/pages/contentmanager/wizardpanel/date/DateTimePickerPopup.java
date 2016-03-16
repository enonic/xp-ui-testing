package com.enonic.autotests.pages.contentmanager.wizardpanel.date;


import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;

public class DateTimePickerPopup
    extends Application
{
    private final String DIV_CONTAINER = "//div[contains(@id,'DateTimePickerPopup')]";

    private TimePickerPopup timePickerPopup;

    private DatePickerPopup datePickerPopup;

    public DateTimePickerPopup( TestSession session )
    {
        super( session );
    }

    public TimePickerPopup getTimePickerPopup()
    {
        if ( timePickerPopup == null )
        {
            timePickerPopup = new TimePickerPopup( getSession() );
        }
        return timePickerPopup;
    }

    public DatePickerPopup getDatePickerPopup()
    {
        if ( datePickerPopup == null )
        {
            datePickerPopup = new DatePickerPopup( getSession() );
        }
        return datePickerPopup;
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( DIV_CONTAINER );
    }

    public void waitUntilDialogLoaded()
    {
        boolean isLoaded = waitUntilVisibleNoException( By.xpath( DIV_CONTAINER ), Application.EXPLICIT_NORMAL );
        if ( !isLoaded )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_date-time-picker-popup" ) );
            throw new TestFrameworkException( "DateTimePickerPopup  dialog not loaded!" );
        }
    }

}
