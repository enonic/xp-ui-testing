package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.WaitHelper;

public class Application
    extends Page
{
    public String ELEMENT_BY_ID = "return window.api.dom.ElementRegistry.getElementById('%s')";

    public static final int NUMBER_TRIES_TO_CLOSE = 2;

    public static final long PAGE_LOAD_TIMEOUT = 15l;

    public static final long EXPLICIT_LONG = 4l;

    public static final long EXPLICIT_NORMAL = 3;

    public static final long EXPLICIT_QUICK = 2;


    public static final String CONTENT_MANAGER_FRAME_XPATH = "//iframe[contains(@src,'content-manager')]";

    public static final String LIVE_EDIT_MAIN_REGION = "//div[@id='main' and @data-live-edit-id]";

    public static final String LIVE_EDIT_FRAME = "//iframe[@class='live-edit-frame']";

    public static final String USER_MANAGER_FRAME_XPATH = "//iframe[contains(@src,'user-manager')]";

    public static final String MODULE_MANAGER_FRAME_XPATH = "//iframe[contains(@src,'module-manager')]";

    public static final String SPINNER_XPATH = "//div[@id='api.ui.LoadMask']";

    public Application( TestSession session )
    {
        super( session );
    }

    public void waitsForSpinnerNotVisible()
    {
        waitsForSpinnerNotVisible( EXPLICIT_NORMAL );
    }

    public void waitsForSpinnerNotVisible( long timeout )
    {
        boolean result = waitsElementNotVisible( By.xpath( SPINNER_XPATH ), timeout );
        if ( !result )
        {
            throw new TestFrameworkException( "after " + EXPLICIT_NORMAL + " second, spinner still present" );
        }
    }

    public boolean waitElementNotVisible( By by, long timeout )
    {
        return WaitHelper.waitsElementNotVisible( getDriver(), by, timeout );
    }

    public Application setChecked( String checkboxId, boolean value )
    {
        JavascriptExecutor executor = (JavascriptExecutor) getSession().getDriver();

        String script = String.format( ELEMENT_BY_ID + ".setChecked(arguments[0])", checkboxId );
        executor.executeScript( script, value );
        return this;
    }

    public boolean isCheckBoxChecked( String checkboxId )
    {
        JavascriptExecutor executor = (JavascriptExecutor) getSession().getDriver();

        String script = String.format( ELEMENT_BY_ID + ".isChecked()", checkboxId );
        return (Boolean) executor.executeScript( script );

    }

}
