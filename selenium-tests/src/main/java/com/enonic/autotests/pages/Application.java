package com.enonic.autotests.pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.utils.WaitHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class Application
    extends Page
{
    public static String EXPECTED_PUBLISH_MESSAGE = "\"%s\" published";

    public static String ONLINE_DELETED_MESSAGE = "\"%s\" marked for deletion";

    public static String DELETE_PENDING_MESSAGE = "\"%s\" deleted";

    public static String ELEMENT_BY_ID = "return window.api.dom.ElementRegistry.getElementById('%s')";

    protected String DIV_NAMES_VIEW = "//div[contains(@id,'api.app.NamesView') and child::p[@class='sub-name' and contains(.,'%s')]]";

    public final String NOTIFICATION_ERROR = "//div[@class='notification error']//div[@class='notification-content']/span";

    public final String NOTIFICATION_WARNING = "//div[@class='notification warning']//div[@class='notification-content']/span";

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

    public static final String SPINNER_XPATH = "//div[contains(@id,'api.ui.LoadMask')]";

    public Application( TestSession session )
    {
        super( session );
    }

    public void waitsForSpinnerNotVisible()
    {
        List<WebElement> list = null;
        int i = 0;
        do
        {
            list = findElements( By.xpath( SPINNER_XPATH ) ).stream().filter( WebElement::isDisplayed ).collect( Collectors.toList() );
            sleep( 500 );
            i++;
            if ( i == 5 )
            {
                throw new TestFrameworkException( "timeout exceeded, but the Spinner still displayed" );
            }
        }
        while ( list.size() > 0 );
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

    public String waitNotificationWarning( long timeout )
    {
        String message = TestUtils.waitNotificationMessage( By.xpath( NOTIFICATION_WARNING ), getDriver(), timeout );
        getLogger().info( "Notification warning " + message );
        return message;
    }

    public String waitNotificationMessage( long timeout )
    {
        if ( !waitUntilVisibleNoException( By.xpath( "//div[@class='notification-content']/span" ), timeout ) )
        {
            return null;
        }
        String message = findElements( By.xpath( "//div[@class='notification-content']/span" ) ).get( 0 ).getText();
        getLogger().info( "Notification message " + message );
        return message;
    }

}
