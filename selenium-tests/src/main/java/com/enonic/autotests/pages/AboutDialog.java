package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 14.09.2016.
 */
public class AboutDialog
    extends Application
{
    private final String DIALOG_CONTAINER = "//div[contains(@id,'ModalDialog') and contains(@class,'xp-about')]";

    private final String CANCEL_BUTTON = DIALOG_CONTAINER + APP_CANCEL_BUTTON_TOP;

    private final String LICENSING_BUTTON = DIALOG_CONTAINER + "//button[child::span[text()='Licensing']]";

    @FindBy(xpath = CANCEL_BUTTON)
    private WebElement cancelButtonTop;

    @FindBy(xpath = LICENSING_BUTTON)
    private WebElement licensingButton;

    //@FindBy(linkText = "Source Code")
    // private WebElement sourceCodeLink;

    //@FindBy(linkText = "Downloads")
    //private WebElement downloadsLink;


    public AboutDialog( final TestSession session )
    {
        super( session );
    }

    public boolean isOpened()
    {
        return isElementDisplayed( DIALOG_CONTAINER );
    }

    public AboutDialog waitUntilDialogShown( long timeout )
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), timeout ) )
        {
            saveScreenshot( "err_xp_about" );
            throw new TestFrameworkException( "about dialog was not shown!" );
        }
        return this;
    }

    public void clickOnCancelButton()
    {
        cancelButtonTop.click();
        sleep( 500 );
    }

    public boolean isCancelButtonDisplayed()
    {
        return cancelButtonTop.isDisplayed();
    }

    boolean isLicensingButtonDisplayed()
    {
        return licensingButton.isDisplayed();
    }
}
