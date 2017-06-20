package com.enonic.autotests.pages;

import java.awt.AWTException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class SaveBeforeCloseDialog
    extends Application
{
    private final String DIALOG_DIV = "//div[contains(@id,'SaveBeforeCloseDialog')]";

    private final String YES_BUTTON_XPATH =
        DIALOG_DIV + "//button[contains(@id,'api.ui.dialog.DialogButton') and child::span[contains(.,'es')]]";

    private final String NO_BUTTON_XPATH = DIALOG_DIV + "//button[contains(@id,'DialogButton') and child::span[contains(.,'o')]]";

    private final String CANCEL_BUTTON_XPATH = DIALOG_DIV + "//div[contains(@class,'cancel-button')]";

    public final String WARNING_MESSAGE = DIALOG_DIV + "//h6[text()='There are unsaved changes, do you want to save them before closing?']";

    /**
     * The constructor
     *
     * @param session
     */
    public SaveBeforeCloseDialog( TestSession session )
    {
        super( session );
    }

    public void clickYesButton()
    {
        boolean isPresent = waitAndFind( By.xpath( YES_BUTTON_XPATH ) );
        if ( !isPresent )
        {
            throw new TestFrameworkException( "'Yes' button was not found on modal dialog!" );
        }
        findElement( By.xpath( YES_BUTTON_XPATH ) ).click();
        sleep( 500 );
    }

    public void clickCancelButton()
    {
        boolean isPresent = waitAndFind( By.xpath( CANCEL_BUTTON_XPATH ) );
        if ( !isPresent )
        {
            throw new TestFrameworkException( "'Cancel' button was not found on modal dialog!" );
        }
        findElement( By.xpath( CANCEL_BUTTON_XPATH ) ).click();
        sleep( 500 );
    }

    public void press_Y_key()
        throws AWTException
    {
        Actions actions = new Actions( getDriver() );
        String y = Keys.chord( "y" );
        actions.sendKeys( y );
        actions.build().perform();
        sleep( 500 );
    }

    public void press_N_key()
        throws AWTException
    {
        Actions actions = new Actions( getDriver() );
        String y = Keys.chord( "n" );
        actions.sendKeys( y );
        actions.build().perform();
        sleep( 500 );
    }

    public void clickNoButton()
    {
        boolean isPresent = waitAndFind( By.xpath( NO_BUTTON_XPATH ) );
        if ( !isPresent )
        {
            saveScreenshot( "err_no_button_was_not_found" );
            throw new TestFrameworkException( "'No' button was not found on modal dialog!" );
        }
        findElement( By.xpath( NO_BUTTON_XPATH ) ).click();
    }

    /**
     * @return
     */
    public boolean waitForPresent( long timeout )
    {
        return waitUntilVisibleNoException( By.xpath( WARNING_MESSAGE ), timeout );
    }

    /**
     * @return
     */
    public boolean waitForPresent()
    {
        return waitForPresent( Application.EXPLICIT_NORMAL );
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( WARNING_MESSAGE );
    }
}
