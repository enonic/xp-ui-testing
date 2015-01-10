package com.enonic.autotests.pages;

import org.openqa.selenium.By;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.WaitHelper;

public class Application
    extends Page
{

    public static final int NUMBER_TRIES_TO_CLOSE = 2;

    public static final int NUMBER_TRIES_TO_FIND_ELEMENT = 2;

    public static final long PAGE_LOAD_TIMEOUT = 15l;

    public static final long ONE_SEC = 1l;

    public static final long IMPLICITLY_WAIT = 4l;

    public static final long EXPLICIT_4 = 4l;

    public static final long EXPLICIT_3 = 3l;

    public static final long EXPLICIT_2 = 2l;

    public static final int DEFAULT_IMPLICITLY_WAIT = 2;

    public static final String CONTENT_MANAGER_FRAME_XPATH = "//iframe[contains(@src,'content-manager')]";

    public static final String LIVE_EDIT_FRAME = "//iframe[@class='live-edit-frame']";

    public static final String USER_MANAGER_FRAME_XPATH = "//iframe[contains(@src,'user-manager')]";

    public static final String SPINNER_XPATH = "//div[@id='api.ui.LoadMask']";

    public Application( TestSession session )
    {
        super( session );
    }

    public void waitsForSpinnerNotVisible()
    {
        waitsForSpinnerNotVisible( IMPLICITLY_WAIT );
    }

    public void waitsForSpinnerNotVisible( long timeout )
    {
        boolean result = waitsElementNotVisible( By.xpath( SPINNER_XPATH ), timeout );
        if ( !result )
        {
            throw new TestFrameworkException( "after " + IMPLICITLY_WAIT + " second, spinner still present" );
        }
    }

    public boolean waitElementNotVisible( By by, long timeout )
    {
        return WaitHelper.waitsElementNotVisible( getDriver(), by, timeout );
    }


}
