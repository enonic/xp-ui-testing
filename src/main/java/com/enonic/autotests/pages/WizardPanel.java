package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.TestUtils;

/**
 * Base class for wizards.
 */
public abstract class WizardPanel<T>
    extends Application
{

    //public static String RED_CIRCLE_XPATH = "//span[@class='tabcount' and contains(.,'%s')]";
    public static String RED_CIRCLE_XPATH = "//span[@class='tabcount']";

    public static String APP_BAR_TAB_MENU_TITLE_XPATH = "//div[@id='api.app.AppBarTabMenuButton']//span[@class='label']";

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

    public abstract void close();

    public abstract WizardPanel<T> save();

    public abstract boolean isEnabledSaveButton();

    public abstract WizardPanel<T> typeData( T object );

    /**
     * Types a name to the name-input field
     *
     * @param name
     */
    public void doTypeName( String name )
    {
        clearAndType( nameInput, name );
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
            TestUtils.waitNotificationMessage( By.xpath( "//div[@class='admin-notification-content']/span" ), getDriver(), 2l );
        return message;
    }

    /**
     * Verify that red circle and "New Space" message presented on the top of
     * Page.
     */
    public WizardPanel<T> waitUntilWizardOpened()
    {
        String circleXpath = String.format( RED_CIRCLE_XPATH );
        waitUntilVisible( By.xpath( circleXpath ) );
        //TestUtils.getInstance().waitUntilVisible(getSession(), By.xpath(titleXpath));
        return this;
    }

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
