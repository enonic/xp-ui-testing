package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class Application
    extends Page
{
    public static String ONE_CONTENT_MARKED_FOR_DELETION_MESSAGE = "The item is marked for deletion";

    public static String CONTENTS_MARKED_FOR_DELETION_MESSAGE = "%s items are marked for deletion";

    public static String CONTENT_ALREADY_IN_USE_WARNING = "Content [%s] could not be updated. A content with that name already exists";

    public static String GROUP_ALREADY_IN_USE_WARNING = "Content [%s] could not be updated. A content with that name already exists";

    public static String ONE_CONTENT_UNPUBLISHED_NOTIFICATION_MESSAGE = "\"%s\" is unpublished";

    public static String CONTENTS_UNPUBLISHED_NOTIFICATION_MESSAGE = "%s items are unpublished";

    public static String ONE_PENDING_ITEM_IS_DELETED = "The item is deleted";

    public static String PENDING_ITEMS_ARE_DELETED = "%s items are deleted";

    public static String CONTENT_SAVED = "\"%s\" saved";

    public static String CONTENT_DELETED_MESSAGE = "The item is deleted";

    public static final String FULL_ACCESS = "Full Access";

    public static final String CAN_READ = "Can Read";

    public static String ELEMENT_BY_ID = "return window.api.dom.ElementRegistry.getElementById('%s')";

    protected final String GRID_CANVAS = "//div[@class='grid-canvas']";

    protected final String DROPDOWN_OPTION_FILTER_INPUT = "//input[contains(@id,'DropdownOptionFilterInput')]";

    protected final String COMBOBOX_OPTION_FILTER_INPUT = "//input[contains(@id,'ComboBoxOptionFilterInput')]";

    protected String NAMES_VIEW_BY_NAME = "//div[contains(@id,'NamesView') and child::p[@class='sub-name' and contains(.,'%s')]]";

    protected String NAMES_VIEW_BY_DISPLAY_NAME = "//div[contains(@id,'NamesView') and child::h6[@class='main-name' and contains(.,'%s')]]";

    protected final String SLICK_ROW = "//div[contains(@class,'slick-row')]";

    protected final String SLICK_CELL = "//div[contains(@class,'slick-cell')]";

    public String SLICK_ROW_BY_NAME =
        "//div[@class='slick-viewport']//div[contains(@class,'slick-row') and descendant::p[@class='sub-name' and contains(.,'%s')]]";

    public String SLICK_ROW_BY_DISPLAY_NAME =
        "//div[@class='slick-viewport']//div[contains(@class,'slick-row') and descendant::h6[@class='main-name' and contains(.,'%s')]]";

    protected final String NAMES_VIEW = "//div[contains(@id,'NamesView')]";

    protected final String H6_MAIN_NAME = "//h6[@class='main-name']";

    protected final String H6_DISPLAY_NAME = NAMES_VIEW + H6_MAIN_NAME;

    protected final String P_NAME = NAMES_VIEW + "//p[@class='sub-name']";

    public final String NOTIFICATION_ERROR = "//div[@class='notification error']//div[@class='notification-content']/span";

    public final String NOTIFICATION_WARNING = "//div[@class='notification warning']//div[@class='notification-content']/span";

    public static String ONE_CONTENT_PUBLISHED_NOTIFICATION_MESSAGE = "\"%s\" is published";

    public static String CONTENTS_PUBLISHED_NOTIFICATION_MESSAGE = "%s items are published";

    public static final long PAGE_LOAD_TIMEOUT = 15l;

    public static final long EXPLICIT_LONG = 4l;

    public static final long EXPLICIT_NORMAL = 3;

    public static final long EXPLICIT_QUICK = 2;

    public static final long APP_INSTALL_TIMEOUT = 10;

    public static final String LIVE_EDIT_FRAME = "//iframe[@class='live-edit-frame']";

    public static final String SPINNER_XPATH = "//div[@class='spinner']";

    public final String NOTIFICATION_MESSAGE_XPATH = "//div[contains(@id,'NotificationMessage')]//div[@class='notification-content']/span";

    public String EXPECTED_NOTIFICATION_MESSAGE_XPATH =
        "//div[contains(@id,'NotificationMessage')]//div[@class='notification-content']/span[text()='%s']";

    public final String ERROR_NOTIFICATION_MESSAGE_XPATH =
        "//div[contains(@id,'NotificationMessage') and @class='notification error']//span";

    public String PUBLISH_SUCCESS_NOTIFICATION_MESSAGE_XPATH =
        "//div[contains(@id,'NotificationMessage') and contains(@class,'success')]//div[@class='notification-content']/span";

    public Application( TestSession session )
    {
        super( session );
    }

    protected String getDisplayedString( String xpath )
    {
        return findElements( By.xpath( xpath ) ).stream().filter( WebElement::isDisplayed ).map( WebElement::getText ).findFirst().get();
    }

    public void waitsForSpinnerNotVisible()
    {
        boolean isDisplayed = true;
        int i = 0;
        do
        {
            isDisplayed = isElementDisplayed( SPINNER_XPATH );
            sleep( 100 );
            i++;
            if ( i == 5 )
            {
                throw new TestFrameworkException( "timeout exceeded, but the Spinner still displayed" );
            }
        }
        while ( isDisplayed );
    }

    public void waitInvisibilityOfSpinner( long timeout )
    {
        boolean result = waitInvisibilityOfElement( By.xpath( SPINNER_XPATH ), timeout );
        if ( !result )
        {
            saveScreenshot( NameHelper.uniqueName( "err_spinner" ) );
            throw new TestFrameworkException( "after a" + EXPLICIT_NORMAL + " seconds, spinner still present" );
        }
    }

    public String waitNotificationWarning( long timeout )
    {
        String message = TestUtils.waitNotification( By.xpath( NOTIFICATION_WARNING ), getDriver(), timeout );
        getLogger().info( "Notification warning " + message );
        return message;
    }

    protected void saveScreenshot( String name )
    {
        TestUtils.saveScreenshot( getSession(), name );
    }

    protected JavascriptExecutor getJavaScriptExecutor()
    {
        return (JavascriptExecutor) getSession().getDriver();
    }
}
