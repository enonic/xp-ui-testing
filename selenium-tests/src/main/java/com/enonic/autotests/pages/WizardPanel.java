package com.enonic.autotests.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.exceptions.WizardPanelNotClosingException;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Base class for wizards.
 */
public abstract class WizardPanel<T>
    extends Application
{
    public final String NOTIFICATION_ERROR = "//div[@class='notification error']//div[@class='notification-content']/span";

    public final String NOTIFICATION_WARNING = "//div[@class='notification warning']//div[@class='notification-content']/span";

    public static String TABS_NAVIGATOR_LINK = "//ul[contains(@id,'wizard.WizardStepNavigator')]";

    public static String NAVIGATOR_TAB_ITEM_LINK = TABS_NAVIGATOR_LINK + "//li/span[@title='%s']";

    public static final String SECURITY_LINK_TEXT = "Security";

    public static String APP_BAR_TAB_MENU = "//div[contains(@id,'api.app.bar.AppBarTabMenu')]";

    public static String APP_BAR_TAB_MENU_ITEM =
        APP_BAR_TAB_MENU + "//li[contains(@id,'api.app.bar.AppBarTabMenuItem') and child::span[@title='%s']]";

    public static String CLOSE_TAB_BUTTON = APP_BAR_TAB_MENU_ITEM + "/button";

    public static String APP_BAR_TAB_MENU_TITLE_XPATH = "//div[contains(@id,'api.app.bar.AppBarTabMenuButton')]//span[@class='label']";

    public static String TAB_MENU_ITEM =
        "//div[contains(@id,'api.app.bar.AppBar')]//li[contains(@id,'AppBarTabMenuItem') and child::span[@title ='%s']]";

    public static String BUTTON_CLOSE_IN_TAB_MENU_ITEM = TAB_MENU_ITEM + "/button";

    @FindBy(name = "displayName")
    protected WebElement displayNameInput;

    @FindBy(name = "name")
    protected WebElement nameInput;


    /**
     * The constructor
     *
     * @param session
     */
    public WizardPanel( TestSession session )
    {
        super( session );
    }

    public SaveBeforeCloseDialog close( String displayName )
    {
        CloseStatus status = null;
        if ( findElements( By.xpath( String.format( CLOSE_TAB_BUTTON, displayName ) ) ).size() == 0 )
        {
            throw new TestFrameworkException( "close button for tab with name " + displayName + " was not found!" );
        }
        findElements( By.xpath( String.format( CLOSE_TAB_BUTTON, displayName ) ) ).get( 0 ).click();
        sleep( 500 );
        for ( int i = 0; i < NUMBER_TRIES_TO_CLOSE; i++ )
        {
            status = verifyCloseAction( By.xpath( getWizardDivXpath() ) );
            if ( status.equals( CloseStatus.CLOSED ) || status.equals( CloseStatus.MODAL_DIALOG ) )
            {
                break;
            }
        }

        if ( status == null )
        {
            throw new WizardPanelNotClosingException( "ContentWizard was not closed and Modal dialog not present!" );

        }
        else if ( status.equals( CloseStatus.MODAL_DIALOG ) )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "saveBeforeClose" ) );
            return new SaveBeforeCloseDialog( getSession() );
        }
        else
        {
            return null;
        }
    }

    public SaveBeforeCloseDialog closeTabMenuItem( String title )
    {

        CloseStatus status = null;
        if ( findElements( By.xpath( String.format( BUTTON_CLOSE_IN_TAB_MENU_ITEM, title ) ) ).size() == 0 )
        {
            throw new TestFrameworkException( "tab menu item :" + title + "  was not found!!" );
        }
        findElement( By.xpath( String.format( BUTTON_CLOSE_IN_TAB_MENU_ITEM, title ) ) ).click();
        for ( int i = 0; i < NUMBER_TRIES_TO_CLOSE; i++ )
        {
            status = verifyCloseAction( By.xpath( getWizardDivXpath() ) );
            if ( status.equals( CloseStatus.CLOSED ) || status.equals( CloseStatus.MODAL_DIALOG ) )
            {
                break;
            }
        }

        if ( status == null )
        {
            throw new WizardPanelNotClosingException( "ContentWizard was not closed and Modal dialog not present!" );

        }
        else if ( status.equals( CloseStatus.MODAL_DIALOG ) )
        {
            return new SaveBeforeCloseDialog( getSession() );
        }
        else
        {
            return null;
        }

    }

    public abstract String getWizardDivXpath();

    private CloseStatus verifyCloseAction( By by )
    {
        sleep( 100 );
        boolean isWizardClosed = findElements( by ).size() == 0;
        if ( isWizardClosed )
        {
            return CloseStatus.CLOSED;
        }
        else
        {
            SaveBeforeCloseDialog dialog = new SaveBeforeCloseDialog( getSession() );
            boolean isPresent = dialog.waitForPresent();
            if ( isPresent )
            {
                return CloseStatus.MODAL_DIALOG;
            }
            return null;
        }
    }

    public enum CloseStatus
    {
        CLOSED, MODAL_DIALOG;
    }

    public abstract WizardPanel<T> save();

    public abstract WebElement getCloseButton();

    public abstract boolean isOpened();

    public abstract boolean isEnabledSaveButton();

    public abstract WizardPanel<T> typeData( T object );
    // public abstract WizardPanel<T> typeDisplayName( String name );

    /**
     * Types a name to the name-input field
     *
     * @param name
     */
    public WizardPanel<T> doTypeName( String name )
    {
        clearAndType( nameInput, name );
        return this;
    }

    /**
     * calculates a width of input field.
     *
     * @return width of input
     */
    public int getInputNameWidth()
    {
        String width = nameInput.getAttribute( "style" );
        //input Style should be like as: "width: 300px";
        int start = width.indexOf( ":" );
        int end = width.indexOf( "px" );
        int inputWidth = Integer.valueOf( width.substring( start + 1, end ).trim() );
        return inputWidth;
    }

    public String getAppBarTabMenuTitle()
    {
        boolean result = waitUntilVisibleNoException( By.xpath( APP_BAR_TAB_MENU_TITLE_XPATH ), Application.EXPLICIT_NORMAL );
        if ( result )
        {
            return findElement( By.xpath( APP_BAR_TAB_MENU_TITLE_XPATH ) ).getAttribute( "title" );
        }
        else
        {
            throw new TestFrameworkException( "title was not found or not visible in AppBarTabMenu!" );
        }
    }

    public boolean isTabMenuItemPresent( String itemText )
    {
        List<WebElement> elems = findElements( By.xpath( APP_BAR_TAB_MENU + "//li[contains(@id,'api.app.bar.AppBarTabMenuItem')]//span" ) );

        for ( WebElement element : elems )
        {
            if ( element.getText().contains( itemText ) )
            {
                return true;
            }
        }
        return false;
    }

    public String getNameInputValue()
    {
        return nameInput.getAttribute( "value" );
    }

    /**
     * Gets notification message, that appears at
     * the bottom of the WizardPage. <br>
     *
     * @return notification message or null.
     */
    public String waitNotificationMessage()
    {
        String message = TestUtils.waitNotificationMessage( By.xpath( "//div[@class='notification-content']/span" ), getDriver(), 4l );
        getLogger().info( "Notification message " + message );
        return message;
    }

//    public String waitNotificationWarning()
//    {
//        String message =
//            TestUtils.waitNotificationMessage( By.xpath( "//div[@class='notification warning']//div[@class='notification-content']/span" ),
//                                               getDriver(), 4l );
//        getLogger().info( "Notification message " + message );
//        return message;
//    }

    public String waitNotificationWarning( long timeout )
    {
        String message = TestUtils.waitNotificationMessage( By.xpath( NOTIFICATION_WARNING ), getDriver(), timeout );
        getLogger().info( "Notification warning " + message );
        return message;
    }

//    public String waitNotificationError()
//    {
//        String message =
//            TestUtils.waitNotificationMessage( By.xpath( "//div[@class='notification error']//div[@class='notification-content']/span" ),
//                                               getDriver(), 4l );
//        getLogger().info( "Notification message " + message );
//        return message;
//    }

    public String waitNotificationError( long timeout )
    {
        if ( !waitUntilVisibleNoException( By.xpath( NOTIFICATION_ERROR ), timeout ) )
        {
            return null;
        }
        String message = findElements( By.xpath( NOTIFICATION_ERROR ) ).get( 0 ).getText();
        getLogger().info( "Notification message " + message );
        return message;
    }

    /**
     * Verify that red circle and "New Space" message presented on the top of
     * Page.
     */
    public abstract WizardPanel<T> waitUntilWizardOpened();

    public void waitElementClickable( By by, long timeout )
    {
        new WebDriverWait( getDriver(), timeout ).until( ExpectedConditions.elementToBeClickable( by ) );
    }

    public WebElement getNameInput()
    {
        return nameInput;
    }

    public void clickWizardStep( final int position )
    {
        // TODO: click step with position

    }

    public WizardPanel<T> typeDisplayName( String displayName )
    {
        clearAndType( displayNameInput, displayName );
        return this;
    }
}
