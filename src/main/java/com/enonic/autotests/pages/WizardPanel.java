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
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Base class for wizards.
 */
public abstract class WizardPanel<T>
    extends Application
{
    public static String RED_CIRCLE_XPATH = "//span[@class='tabcount']";

    public static String APP_BAR_TAB_MENU_TITLE_XPATH = "//div[contains(@id,'api.app.AppBarTabMenuButton')]//span[@class='label']";

    public static String ITEM_FROM_TAB_MENU_ITEM_LIST =
        "//div[contains(@id,'api.app.AppBar')]//li[contains(@id,'api.app.AppBarTabMenuItem') and child::span[contains(.,'%s')]]";

    public static String BUTTON_CLOSE_IN_TAB_MENU_ITEM = ITEM_FROM_TAB_MENU_ITEM_LIST + "/button";

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

    public SaveBeforeCloseDialog close()
    {

        CloseStatus status = null;
        getCloseButton().click();
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

    public SaveBeforeCloseDialog closeInTabMenuItem( String title )
    {

        CloseStatus status = null;
        findElements( By.xpath( String.format( BUTTON_CLOSE_IN_TAB_MENU_ITEM, title ) ) );
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
     * @return
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
        boolean result =
            getDriver().findElements( By.xpath( "//div[@id='api.app.AppBarTabMenuButton']//span[@class='label']" ) ).size() == 1;
        if ( result )
        {
            return getDriver().findElement( By.xpath( APP_BAR_TAB_MENU_TITLE_XPATH ) ).getAttribute( "title" );
        }
        else
        {
            throw new TestFrameworkException( "title was not found in AppBarTabMenu!" );
        }
    }

    public WizardPanel<T> showTabMenuItems()
    {
        findElements( By.xpath( APP_BAR_TAB_MENU_TITLE_XPATH ) ).get( 0 ).click();
        sleep( 300 );
        return this;
    }

    public boolean isTabMenuItemPresent( String itemText )
    {
        List<WebElement> elems =
            findElements( By.xpath( "//div[contains(@id,'api.app.AppBar')]//li[contains(@id,'api.app.AppBarTabMenuItem')]/span" ) );
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
        String message = TestUtils.waitNotificationMessage( By.xpath( "//div[@class='notification-content']/span" ), getDriver(), 2l );
        getLogger().info( "No5tification message " + message );
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
}
