package com.enonic.autotests.pages;

import java.util.ArrayList;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class Application
    extends Page
{
    protected final String SCRIPT_SET_CKE = "CKEDITOR.instances[arguments[0]].setData(arguments[1])";

    protected final String CKE_HTML_AREA = "//div[contains(@id,'HtmlArea')]//textarea[contains(@id,'api.ui.text.TextArea')]";

    protected final String SCRIPT_DATA_CKE = "return CKEDITOR.instances[arguments[0]].getData()";

    public static final String NO_SELECTION_MESSAGE = "You are wasting this space - select something!";

    public static final String SWITCHING_TEMPLATE_MESSAGE =
        "Switching to a page template will discard all of the custom changes made to the page. Are you sure?";

    public static final String CONTENT_STUDIO_TITLE = "Content Studio - Enonic XP Admin";

    public static final String ERROR_MESSAGE_404 = "404 Not Found";

    public static final String CONTENT_STUDIO_TAB_HANDLE = "content_studio_window_id_key";

    public static String ONE_CONTENT_MARKED_FOR_DELETION_MESSAGE = "Item \"%s\" is marked for deletion.";

    public static String ITEM_IS_UNDELETED = "Item is undeleted";

    public static String ITEMS_IS_UNDELETED = "Items are undeleted";

    public static String CONTENTS_DELETED_AND_MARKED_FOR_DELETION_MESSAGE = "%s items are deleted ( %s are marked for deletion ).";

    public static String CONTENTS_PUBLISHED_AND_DELETED_MESSAGE = "%s items are published ( %s deleted ).";

    public static String CONTENT_ALREADY_IN_USE_WARNING = "Content [%s] could not be updated. A content with that name already exists";

    public static String GROUP_ALREADY_IN_USE_WARNING = "Principal [%s] could not be created. A principal with that name already exists";

    public static String GROUP_CREATED_MESSAGE = "Group was created";

    public static String ONE_CONTENT_UNPUBLISHED_NOTIFICATION_MESSAGE = "Item \"%s\" is unpublished.";

    public static String CONTENTS_UNPUBLISHED_NOTIFICATION_MESSAGE = "%s items are unpublished";

    public static String ONE_PENDING_ITEM_IS_DELETED = "Item \"%s\" is deleted.";

    public static String PENDING_ITEMS_ARE_DELETED = "%s items are deleted.";

    public static String ISSUE_IS_CLOSED_MESSAGE = "The issue is closed.";

    public static String ISSUE_ITEM_DELETED = "One or more items from the issue cannot be found.";

    public static String ISSUE_IS_CREATED_MESSAGE = "New issue created successfully.";

    public static String ISSUE_IS_CLOSED = "Issue \"%s\" is closed.";

    public static String CONTENT_SAVED = "Item \"%s\" is saved.";

    public static String ITEM_IS_DELETED = "Item \"%s\" is deleted.";

    public static final String FULL_ACCESS = "Full Access";

    public static final String CAN_READ = "Can Read";

    public static final String CAN_PUBLISH = "Can Publish";

    public static String ELEMENT_BY_ID = "return window.api.dom.ElementRegistry.getElementById('%s')";

    protected final String GRID_CANVAS = "//div[contains(@class,'grid-canvas')]";

    protected final String APP_CANCEL_BUTTON_TOP = "//div[contains(@class,'cancel-button-top')]";

    protected String OPTION_IMAGE_SELECTOR_VIEW_BY_NAME =
        "//div[contains(@id,'ImageSelectorViewer') and descendant::img[contains(@title,'%s')]]";

    protected String OPTION_IMAGE_SELECTOR_VIEW = "//div[contains(@id,'ImageSelectorViewer') ]//img";

    protected final String DATA_TIME_PICKER_INPUT = "//div[contains(@id,'DateTimePicker')]//input[contains(@id,'TextInput')]";

    protected final String TEXT_INPUT = "//input[contains(@id,'TextInput')]";

    protected final String CKE_TEXT_AREA = "//div[contains(@id,'cke_api.ui.text.TextArea')]";

    protected final String TEXT_AREA_INPUT = "//textarea[contains(@id,'TextArea')]";

    public final String CONTENT_SUMMARY_VIEWER = "//div[contains(@id,'ContentSummaryViewer')]";

    protected final String DROPDOWN_OPTION_FILTER_INPUT = "//input[contains(@id,'DropdownOptionFilterInput')]";

    protected final String DROP_DOWN_HANDLE_BUTTON = "//button[contains(@id,'DropdownHandle')]";

    protected final String ACTION_BUTTON = "//button[contains(@id,'ActionButton') and child::span[contains(.,'%s')]]";

    protected final String PRINCIPAL_SELECTED_OPTION = "//div[contains(@id,'PrincipalSelectedOptionView')]";

    protected final String COMBOBOX_OPTION_FILTER_INPUT = "//input[contains(@id,'ComboBoxOptionFilterInput')]";

    protected String NAMES_VIEW_BY_NAME = "//div[contains(@id,'NamesView') and child::p[contains(@class,'sub-name') and contains(.,'%s')]]";

    protected String TAB_BAR_ITEM = "//li[contains(@id,'TabBarItem') and child::a[text()='%s']]";

    protected String NAMES_VIEW_BY_DISPLAY_NAME =
        "//div[contains(@id,'NamesView') and child::h6[contains(@class,'main-name') and contains(.,'%s')]]";

    protected final String SLICK_ROW = "//div[contains(@class,'slick-row')]";

    protected final String SLICK_CELL = "//div[contains(@class,'slick-cell')]";

    protected final String VALIDATION_RECORDING_VIEWER = "//div[contains(@id,'ValidationRecordingViewer')]//li";

    public String SLICK_ROW_BY_NAME =
        "//div[contains(@class,'slick-viewport')]//div[contains(@class,'slick-row') and descendant::p[contains(@class,'sub-name') and contains(.,'%s')]]";

    public String SLICK_ROW_BY_DISPLAY_NAME =
        "//div[contains(@class,'slick-viewport')]//div[contains(@class,'slick-row') and descendant::h6[contains(@class,'main-name') and contains(.,'%s')]]";

    protected final String NAMES_VIEW = "//div[contains(@id,'NamesView')]";

    protected final String H6_MAIN_NAME = "//h6[contains(@class,'main-name')]";

    protected final String P_SUB_NAME = "//p[contains(@class,'sub-name')]";

    protected final String ICON_REMOVE = "//a[@class='remove']";

    protected final String H6_DISPLAY_NAME = NAMES_VIEW + H6_MAIN_NAME;

    protected final String P_NAME = NAMES_VIEW + P_SUB_NAME;

    protected final String CHECKBOX_ELEMENT = "//div[contains(@id,'api.ui.Checkbox')]";

    public final String NOTIFICATION_ERROR = "//div[@class='notification error']//div[@class='notification-content']/span";

    public final String NOTIFICATION_WARNING = "//div[@class='notification warning']//div[@class='notification-content']/span";

    public static String ONE_CONTENT_PUBLISHED_NOTIFICATION_MESSAGE_TMP = "Item \"%s\" is published.";

    public static String CONTENTS_PUBLISHED_NOTIFICATION_MESSAGE = "%s items are published.";

    public static final long PAGE_LOAD_TIMEOUT = 15l;

    public static final long EXPLICIT_LONG = 6l;

    public static final long EXPLICIT_NORMAL = 3;

    public static final long EXPLICIT_QUICK = 2;

    public static final long EXPLICIT_1 = 1;

    public static final long APP_INSTALL_TIMEOUT = 10;

    public static final String LIVE_EDIT_FRAME = "//iframe[contains(@class,'live-edit-frame')]";

    public static final String SPINNER_XPATH = "//div[@class='spinner']";

    public final String NOTIFICATION_MESSAGE_XPATH = "//div[contains(@id,'NotificationMessage')]//div[@class='notification-content']/span";

    public String EXPECTED_NOTIFICATION_MESSAGE_XPATH =
        "//div[contains(@id,'NotificationMessage')]//div[contains(@class,'notification-content')]//span[contains(.,'%s')]";

    public final String ERROR_NOTIFICATION_MESSAGE_XPATH =
        "//div[contains(@id,'NotificationMessage') and @class='notification error']//div[contains(@class,'notification-content')]/span";

    public String PUBLISH_SUCCESS_NOTIFICATION_MESSAGE_XPATH =
        "//div[contains(@id,'NotificationMessage') and contains(@class,'success')]//div[@class='notification-content']/span";

    public static String REQUIRED_MESSAGE = "This field is required";

    public static String MIN_OCCURRENCES_REQUIRED_MESSAGE = "Min %s occurrences required";

    public Application( TestSession session )
    {
        super( session );
    }

    protected String getDisplayedString( String xpath )
    {
        return findElements( By.xpath( xpath ) ).stream().filter( WebElement::isDisplayed ).map( WebElement::getText ).findFirst().orElse(
            "" );
    }

    public void waitsForSpinnerNotVisible()
    {
        boolean isDisplayed = true;
        int i = 0;
        do
        {
            isDisplayed = isElementDisplayed( SPINNER_XPATH );
            sleep( 300 );
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
            throw new TestFrameworkException( "after a" + timeout + " seconds, spinner still present" );
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
        return (JavascriptExecutor) getDriver();
    }

    protected Actions buildActions()
    {
        return new Actions( getDriver() );
    }

    public void switchToNewWizardTab()
    {
        ArrayList<String> tabHandles = new ArrayList<String>( getDriver().getWindowHandles() );
        getDriver().switchTo().window( tabHandles.get( tabHandles.size() - 1 ) );
        sleep( 400 );
    }

    public boolean isAlertPresent()
    {
        try
        {
            Alert alert = getDriver().switchTo().alert();
            alert.accept();
            return true;
        }
        catch ( NoAlertPresentException var3 )
        {
            return false;
        }
        catch ( NoSuchWindowException exception )
        {
            getLogger().warn( "no such window: target window already closed" );
            return false;
        }
    }

    public void pressEscapeKey()
    {
        buildActions().sendKeys( Keys.ESCAPE ).build().perform();
        sleep( 500 );
    }

    public void setTextInCKE( String id, String text )
    {
        getJavaScriptExecutor().executeScript( SCRIPT_SET_CKE, id, text );
    }


}
