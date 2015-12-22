package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class SaveBeforeCloseDialog
    extends Application
{
    private final String YES_BUTTON_XPATH =
        "//div[contains(@id,'api.app.wizard.SaveBeforeCloseDialog')]//button[contains(@id,'api.ui.dialog.DialogButton') and child::span[contains(.,'es')]]";

    private final String NO_BUTTON_XPATH =
        "//div[contains(@id,'api.app.wizard.SaveBeforeCloseDialog')]//button[contains(@id,'api.ui.dialog.DialogButton') and child::span[contains(.,'o')]]";

    private final String CANCEL_BUTTON_XPATH =
        "//div[contains(@id,'api.app.wizard.SaveBeforeCloseDialog')]//button[contains(@class, 'cancel-button')]";


    public final String TITLE_XPATH = "//h2[text()='Close wizard']";

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

    public void clickNoButton()
    {
        boolean isPresent = waitAndFind( By.xpath( NO_BUTTON_XPATH ) );
        if ( !isPresent )
        {
            throw new TestFrameworkException( "'No' button was not found on modal dialog!" );
        }
        findElement( By.xpath( NO_BUTTON_XPATH ) ).click();
    }

    /**
     *
     */
    public void doCloseNoSave()
    {
        boolean isPresent = waitAndFind( By.xpath( NO_BUTTON_XPATH ) );
        if ( !isPresent )
        {
            throw new TestFrameworkException( "'No' button was not found on modal dialog!" );
        }
        findElement( By.xpath( NO_BUTTON_XPATH ) ).click();
    }


    /**
     * @param timeout
     */
    public void waituntilPageLoaded( long timeout )
    {
        new WebDriverWait( getDriver(), timeout ).until( ExpectedConditions.visibilityOfElementLocated( By.xpath( TITLE_XPATH ) ) );
    }

    /**
     * @return
     */
    public boolean waitForPresent( long timeout )
    {
        return waitUntilVisibleNoException( By.xpath( TITLE_XPATH ), timeout );
    }

    /**
     * @return
     */
    public boolean waitForPresent()
    {
        return waitForPresent( Application.EXPLICIT_NORMAL );
    }
}
