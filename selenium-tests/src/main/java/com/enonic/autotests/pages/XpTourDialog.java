package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 13.09.2016.
 */
public class XpTourDialog
    extends Application
{
    private final String DIALOG_CONTAINER = "//div[contains(@id,'ModalDialog') and contains(@class,'xp-tour-dialog')]";

    private final String CANCEL_BUTTON = DIALOG_CONTAINER + "//div[@class='cancel-button-top']";

    private final String SKIP_TOUR_BUTTON = "//button[contains(@id,'DialogButton')]/span[text()='Skip Tour']";

    private final String NEXT_BUTTON = "//button[contains(@id,'DialogButton')]/span[text()='Next']";

    @FindBy(xpath = CANCEL_BUTTON)
    private WebElement cancelButtonTop;

    @FindBy(xpath = SKIP_TOUR_BUTTON)
    private WebElement skipTourButton;

    @FindBy(xpath = SKIP_TOUR_BUTTON)
    private WebElement nextButton;


    public XpTourDialog( final TestSession session )
    {
        super( session );
    }

    public boolean isOpened()
    {
        return isElementDisplayed( DIALOG_CONTAINER );
    }

    public XpTourDialog waitUntilDialogShown( long timeout )
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), timeout ) )
        {
            saveScreenshot( "err_xp_tour" );
            throw new TestFrameworkException( "Xp Tour dialog was not shown!" );
        }
        return this;
    }

    public void clickOnCancelButton()
    {
        cancelButtonTop.click();
        sleep( 500 );
    }

    public void clickOnSkipTourButton()
    {
        skipTourButton.click();
        sleep( 500 );
    }


    public boolean isCancelButtonDisplayed()
    {
        return cancelButtonTop.isDisplayed();
    }

    public boolean isSkipButtonDisplayed()
    {
        return skipTourButton.isDisplayed();
    }

    public boolean isNextButtonDisplayed()
    {
        return nextButton.isDisplayed();
    }
}
