package com.enonic.autotests.pages.contentmanager.wizardpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class ConfirmationDialog
    extends Application
{
    private final String DIALOG_CONTAINER = "//div[contains(@id,'ConfirmationDialog')]";

    public final String YES_BUTTON_XPATH =
        DIALOG_CONTAINER + "//button[contains(@id,'DialogButton') and descendant::u[text()='Y'] and child::span[text()='es']]";

    public final String NO_BUTTON_XPATH =
        DIALOG_CONTAINER + "//button[contains(@id,'DialogButton') and descendant::u[text()='N'] and child::span[text()='o']]";

    private final String TITLE_TEXT = DIALOG_CONTAINER + "//div[@class='dialog-header']//h2";

    public static final String QUESTION = "Are you sure you want to delete this content?";

    private final String QUESTION_XPATH = DIALOG_CONTAINER + "//h6[@class='question']";

    @FindBy(xpath = YES_BUTTON_XPATH)
    private WebElement yesButton;

    @FindBy(xpath = NO_BUTTON_XPATH)
    private WebElement noButton;

    /**
     * The constructor
     *
     * @param session
     */
    public ConfirmationDialog( TestSession session )
    {
        super( session );
    }

    public boolean isOpened()
    {
        return waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), 2 );
    }

    public String getTitle()
    {
        return getDisplayedString( TITLE_TEXT );
    }

    public String getQuestion()
    {
        return getDisplayedString( QUESTION_XPATH );
    }

    public boolean waitForClosed()
    {
        return waitsElementNotVisible( By.xpath( DIALOG_CONTAINER ), 2 );
    }
    public boolean waitForOpened()
    {
        return waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), 2 );
    }

    public void pressYesButton()
    {
        yesButton.click();
        boolean isClosed = waitForClosed();
        if ( !isClosed )
        {
            saveScreenshot( "err_close_confirm_dialog" );
            throw new TestFrameworkException( "Confirmation dialog was not closed!" );
        }
        sleep( 1000 );
    }

    public void pressNoButton()
    {
        noButton.click();
        boolean isClosed = waitForClosed();
        if ( !isClosed )
        {
            saveScreenshot( "err_close_confirm_dialog" );
            throw new TestFrameworkException( "Confirmation dialog was not closed!" );
        }
        sleep( 1000 );
    }
}
