package com.enonic.autotests.pages.contentmanager.wizardpanel.date;


import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;


public class TimePickerPopup
    extends Application
{
    private final String DIV_CONTAINER = "//ul[contains(@id,'TimePickerPopup')]";

    private final String TIMEZONE = DIV_CONTAINER + "//li[@class='timezone']";

    public TimePickerPopup( TestSession session )
    {
        super( session );
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
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_time-picker-popup" ) );
            throw new TestFrameworkException( "TimePickerPopup  dialog not loaded!" );
        }
    }

    public boolean isTimeZoneDisplayed()
    {
        return isElementDisplayed( TIMEZONE );
    }
}
