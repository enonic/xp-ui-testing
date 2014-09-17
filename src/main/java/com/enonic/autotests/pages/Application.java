package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

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

    public static final String APP_SPACE_ADMIN_FRAME_XPATH = "//iframe[contains(@src,'space-manager')]";

    public static final String APP_CONTENT_MANAGER_FRAME_XPATH = "//iframe[contains(@src,'content-manager')]";

    public static final String APP_SCHEMA_MANAGER_FRAME_XPATH = "//iframe[contains(@src,'schema-manager')]";

    public static final String APP_ACCOUNTS_FRAME_XPATH = "//iframe[contains(@src,'app-account.html')]";

    public static final String DIALOG_CLOSE_BUTTON_XPATH = "//img[@class='x-tool-close']";

    public static final String HOME_BUTTON_XPATH = "//div[contains(@class,'appbar')]/button[@class='launcher-button']";

    public static final String SPINNER_XPATH = "//div[@id='api.ui.LoadMask']";

    @FindBy(xpath = HOME_BUTTON_XPATH)
    protected WebElement gotoHomeButton;

    public Application( TestSession session )
    {
        super( session );
    }

    public HomePage openHomePage()
    {
        gotoHomeButton.click();
        getDriver().switchTo().window( getSession().getWindowHandle() );
        HomePage page = new HomePage( getSession() );
        page.waitUntilAllFramesLoaded();
        return page;
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
