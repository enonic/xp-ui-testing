package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.utils.WaitHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class Application
    extends Page
{
    public static final String OPTION_FILTER_INPUT = "//input[contains(@id,'DropdownOptionFilterInput')]";

    public static final String COMBOBOX_OPTION_FILTER_INPUT = "//input[contains(@id,'ComboBoxOptionFilterInput')]";

    public static String COMBOBOX_OPTIONS_ITEM_BY_DISPLAY_NAME =
        "//div[@class='slick-viewport']//div[contains(@id,'NamesView')]//h6[text()='%s']";

    public static String UNNAMED_FOLDER_TAB_NAME = "<Unnamed Folder>";

    public static String UNNAMED_SITE_DISPLAY_NAME = "<Unnamed Site>";

    public static final String TEST_FOLDER_NAME = "selenium-tests-folder";

    public static String ONLINE_DELETED_MESSAGE = "\"%s\" marked for deletion";

    public static String CONTENT_ALREADY_IN_USE_WARNING = "Content [%s] could not be updated. A content with that name already exists";

    public static String GROUP_ALREADY_IN_USE_WARNING = "Content [%s] could not be updated. A content with that name already exists";

    public static String DELETE_PENDING_MESSAGE = "\"%s\" deleted";

    public static final String PUBLISH_NOTIFICATION_WARNING = "The content cannot be published yet. One or more form values are not valid.";

    public static String ELEMENT_BY_ID = "return window.api.dom.ElementRegistry.getElementById('%s')";

    protected String NAMES_VIEW_BY_NAME = "//div[contains(@id,'NamesView') and child::p[@class='sub-name' and contains(.,'%s')]]";

    protected String NAMES_VIEW_BY_DISPLAY_NAME = "//div[contains(@id,'NamesView') and child::h6[@class='main-name' and contains(.,'%s')]]";

    protected final String SLICK_ROW = "//div[contains(@class,'slick-row')]";

    public String SLICK_ROW_BY_NAME =
        "//div[@class='slick-viewport']//div[contains(@class,'slick-row') and descendant::p[@class='sub-name' and contains(.,'%s')]]";

    public String SLICK_ROW_BY_DISPLAY_NAME =
        "//div[@class='slick-viewport']//div[contains(@class,'slick-row') and descendant::h6[@class='main-name' and contains(.,'%s')]]";

    protected final String NAMES_VIEW = "//div[contains(@id,'NamesView')]";

    protected final String H6_DISPLAY_NAME = NAMES_VIEW + "//h6[@class='main-name']";

    protected final String P_NAME = NAMES_VIEW + "//p[@class='sub-name']";

    protected String SLICK_ROW_WITH_STYLE = "//div[contains(@class,'slick-row') and @style='%s']";

    public final String NOTIFICATION_ERROR = "//div[@class='notification error']//div[@class='notification-content']/span";

    public final String NOTIFICATION_WARNING = "//div[@class='notification warning']//div[@class='notification-content']/span";

    public static String CONTENT_PUBLISHED_NOTIFICATION_MESSAGE = "\"%s\" published";

    public static final int NUMBER_TRIES_TO_CLOSE = 2;

    public static final long PAGE_LOAD_TIMEOUT = 15l;

    public static final long EXPLICIT_LONG = 4l;

    public static final long EXPLICIT_NORMAL = 3;

    public static final long EXPLICIT_QUICK = 2;

    public static final String CONTENT_MANAGER_FRAME_XPATH = "//iframe[contains(@src,'content-manager')]";

    public static final String LIVE_EDIT_MAIN_REGION = "//div[@id='main' and @data-live-edit-id]";

    public static final String LIVE_EDIT_FRAME = "//iframe[@class='live-edit-frame']";

    public static final String USER_MANAGER_FRAME_XPATH = "//iframe[contains(@src,'user-manager')]";

    public static final String APPLICATIONS_FRAME_XPATH = "//iframe[contains(@src,'applications')]";

    public static final String SPINNER_XPATH = "//div[contains(@id,'api.ui.LoadMask')]";

    public final String CONTENT_SAVE_NOTIFICATION_MESSAGE_XPATH =
        "//div[contains(@id,'NotificationMessage') and @class='notification']//span";

    public final String ERROR_NOTIFICATION_MESSAGE_XPATH =
        "//div[contains(@id,'NotificationMessage') and @class='notification error']//span";

    public String PUBLISH_SUCCESS_NOTIFICATION_MESSAGE_XPATH =
        "//div[contains(@id,'NotificationMessage') and contains(@class,'success')]//div[@class='notification-content']/span";

    public Application( TestSession session )
    {
        super( session );
    }

    public String getDisplayedString( String xpath )
    {
        return findElements( By.xpath( xpath ) ).stream().filter( WebElement::isDisplayed ).map( WebElement::getText ).findFirst().get();
    }

    public boolean isElementDisplayed( String xpath )
    {
        return findElements( By.xpath( xpath ) ).stream().filter( WebElement::isDisplayed ).count() > 0;
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

    public Application setCheckboxChecked( String checkboxId, boolean value )
    {
        JavascriptExecutor executor = (JavascriptExecutor) getSession().getDriver();
        String script = String.format( "document.getElementById('%s').checked=arguments[0]", checkboxId );
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
        String message = TestUtils.waitNotification( By.xpath( NOTIFICATION_WARNING ), getDriver(), timeout );
        getLogger().info( "Notification warning " + message );
        return message;
    }

    public String waitNotificationMessage( long timeout )
    {
        if ( !waitUntilVisibleNoException( By.xpath( CONTENT_SAVE_NOTIFICATION_MESSAGE_XPATH ), timeout ) )
        {
            return null;
        }
        String message = findElement( By.xpath( CONTENT_SAVE_NOTIFICATION_MESSAGE_XPATH ) ).getText();
        getLogger().info( "Notification message " + message );
        return message.trim();
    }

    public String waitErrorNotificationMessage( long timeout )
    {
        if ( !waitUntilVisibleNoException( By.xpath( ERROR_NOTIFICATION_MESSAGE_XPATH ), timeout ) )
        {
            return null;
        }
        String message = findElement( By.xpath( ERROR_NOTIFICATION_MESSAGE_XPATH ) ).getText();
        getLogger().info( "Notification message " + message );
        return message.trim();
    }

    public String waitPublishNotificationMessage( long timeout )
    {
        if ( !waitUntilVisibleNoException( By.xpath( PUBLISH_SUCCESS_NOTIFICATION_MESSAGE_XPATH ), timeout ) )
        {
            return null;
        }
        String message = findElement( By.xpath( PUBLISH_SUCCESS_NOTIFICATION_MESSAGE_XPATH ) ).getText();
        getLogger().info( "Publish Notification message " + message );
        return message;
    }

    public void dragAndDrop( WebElement source, WebElement target )
    {
        Actions builder = new Actions( getDriver() );
        builder.clickAndHold( source ).build().perform();
        builder.release( target );
        builder.build().perform();
    }
}
