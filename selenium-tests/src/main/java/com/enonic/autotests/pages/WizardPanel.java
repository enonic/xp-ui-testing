package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Base class for wizards.
 */
public abstract class WizardPanel<T>
    extends Application
{
    public static String TABS_NAVIGATOR_LINK = "//ul[contains(@id,'wizard.WizardStepNavigator')]";

    public static String NAVIGATOR_TAB_ITEM_LINK = TABS_NAVIGATOR_LINK + "//li/span[text()='%s']";

    public static final String SECURITY_LINK_TEXT = "Security";

    public static final String SETTINGS_LINK_TEXT = "Settings";

    public static String TAB_MENU_ITEM =
        "//div[contains(@id,'api.app.bar.AppBar')]//li[contains(@id,'AppBarTabMenuItem') and child::span[@class='label' and text() ='%s']]";

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
        String closeButtonXpath = String.format( BUTTON_CLOSE_IN_TAB_MENU_ITEM, displayName );
        if ( !waitUntilClickableNoException( By.xpath( closeButtonXpath ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_close_wizard" );
            throw new TestFrameworkException( "'close' button for tab with name " + displayName + " was not found!" );
        }
        findElement( By.xpath( String.format( BUTTON_CLOSE_IN_TAB_MENU_ITEM, displayName ) ) ).click();
        sleep( 500 );
        status = verifyCloseAction( By.xpath( getWizardDivXpath() ) );
        if ( status == null )
        {
            saveScreenshot( NameHelper.uniqueName( "err_close" ) );
            throw new TestFrameworkException( "ContentWizard was not closed and Modal dialog not present!" );
        }
        else if ( status.equals( CloseStatus.MODAL_DIALOG ) )
        {
            saveScreenshot( NameHelper.uniqueName( "saveBeforeClose" ) );
            return new SaveBeforeCloseDialog( getSession() );
        }
        else
        {
            //wizard closed successfully
            return null;
        }
    }


    protected abstract String getWizardDivXpath();

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

    public abstract boolean isOpened();

    public abstract boolean isSaveButtonEnabled();

    public abstract boolean isDeleteButtonEnabled();

    public abstract Application clickToolbarDelete();

    public abstract WizardPanel<T> typeData( T object );

    /**
     * calculates a width of input field.
     *
     * @return width of input
     */
    protected int getInputNameWidth()
    {
        String width = nameInput.getAttribute( "style" );
        //input Style should be like as: "width: 300px";
        int start = width.indexOf( ":" );
        int end = width.indexOf( "px" );
        int inputWidth = Integer.valueOf( width.substring( start + 1, end ).trim() );
        return inputWidth;
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
        String message =
            TestUtils.waitNotification( By.xpath( "//div[@class='notification-content']/span" ), getDriver(), Application.EXPLICIT_NORMAL );
        getLogger().info( "Notification message " + message );
        return message;
    }

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

    public abstract WizardPanel<T> waitUntilWizardOpened();

    public void waitElementClickable( By by, long timeout )
    {
        new WebDriverWait( getDriver(), timeout ).until( ExpectedConditions.elementToBeClickable( by ) );
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

    public WizardPanel refreshInBrowser()
    {
        getDriver().navigate().refresh();
        sleep( 3000 );
        return this;
    }
}
