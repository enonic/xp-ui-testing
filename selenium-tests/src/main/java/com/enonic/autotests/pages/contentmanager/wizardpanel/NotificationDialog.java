package com.enonic.autotests.pages.contentmanager.wizardpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class NotificationDialog
    extends Application
{
    public static final String TITLE = "Notification";

    private final String CONTAINER = "//div[contains(@id,'NotificationDialog')]";

    private final String BUTTON_OK = CONTAINER + "//button[contains(@id,'DialogButton') and descendant::span[text()='OK']]";

    private final String BUTTON_CLOSE = CONTAINER + APP_CANCEL_BUTTON_TOP;

    @FindBy(xpath = BUTTON_OK)
    WebElement buttonOk;

    @FindBy(xpath = BUTTON_CLOSE)
    WebElement buttonClose;

    public NotificationDialog( final TestSession session )
    {
        super( session );
    }

    public void waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( CONTAINER ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_notification_dialog" );
            throw new TestFrameworkException( "'Notification' dialog is not opened!" );
        }
    }

    public void waitForClosed()
    {
        try
        {
            waitsElementNotVisible( By.xpath( CONTAINER ), Application.EXPLICIT_NORMAL );
        }
        catch ( Exception e )
        {
            saveScreenshot( "err_notification_dialog" );
            throw new TestFrameworkException( "'Notification' dialog is not opened!" );
        }
    }

    public boolean isOpened()
    {
        return isElementDisplayed( CONTAINER );
    }

    public String getTitle()
    {
        return getDisplayedString( TITLE );
    }

    public boolean isButtonOkPresent()
    {
        return buttonOk.isDisplayed();
    }

    public boolean isButtonClosePresent()
    {
        return buttonClose.isDisplayed();
    }

    public void clickOnOkButton()
    {
        waitUntilElementEnabledNoException( By.xpath( BUTTON_OK ), Application.EXPLICIT_NORMAL );
        buttonOk.click();
        sleep( 1000 );
        this.waitForClosed();
    }

}

