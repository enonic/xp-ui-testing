package com.enonic.autotests.pages.usermanager.browsepanel;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class DeleteUserItemDialog
    extends Application
{
    private final String TITLE_XPATH =
        "//div[contains(@id,'ConfirmationDialog')]//div[contains(@id,'ModalDialogHeader') and child::h2[text()='Confirmation']]";

    private final String YES_BUTTON_XPATH =
        "//div[contains(@id,'ButtonRow')]//button[contains(@id,'DialogButton') and descendant::u[text()='Y'] and child::span[text()='es']]";

    @FindBy(xpath = YES_BUTTON_XPATH)
    private WebElement yesButton;

    /**
     * The constructor.
     *
     * @param session
     */
    public DeleteUserItemDialog( TestSession session )
    {
        super( session );
    }

    public void doDelete()
    {
        yesButton.click();
        boolean isClosed = waitForClosed();
        if ( !isClosed )
        {
            throw new TestFrameworkException( "'delete user item' dialog was not closed!" );
        }
        sleep( 1000 );
    }

    public boolean waitForClosed()
    {
        return waitsElementNotVisible( By.xpath( TITLE_XPATH ), Application.EXPLICIT_NORMAL );
    }

    public boolean waitForOpened()
    {
        return waitUntilVisibleNoException( By.xpath( TITLE_XPATH ), Application.EXPLICIT_NORMAL );
    }

    public boolean isOpened()
    {
        return isElementDisplayed( TITLE_XPATH );
    }

}
