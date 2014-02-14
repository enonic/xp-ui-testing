package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;

public class CloseWizardDialog
    extends BaseModalDialog
{
    private final String YES_BUTTON_XPATH = "//div[@class='modal-dialog']//button[contains(.,'es')]";

    private final String NO_BUTTON_XPATH = "//div[@class='modal-dialog']//button[contains(.,'o')]";

    private final String CANCEL_BUTTON_XPATH = "//div[@class='modal-dialog']//button[contains(.,'Cancel')]";

    public final String TITLE_XPATH = "//h2[text()='Close wizard']";

    /**
     * The constructor
     *
     * @param session
     */
    public CloseWizardDialog( TestSession session )
    {
        super( session );
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

    public void doCloseAndSave()
    {

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
    public boolean isDialogPresent()
    {
        return waitUntilVisibleNoException( By.xpath( TITLE_XPATH ), 1l );
    }

}
