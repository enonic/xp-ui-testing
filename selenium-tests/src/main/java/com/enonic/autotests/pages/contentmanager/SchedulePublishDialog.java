package com.enonic.autotests.pages.contentmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;


/**
 * Created  on 19.12.2016.
 */
public class SchedulePublishDialog
    extends Application
{
    public static final String DIALOG_HEADER = "Scheduled Publishing";

    private final String DIALOG_CONTAINER = "//div[contains(@id,'SchedulePublishDialog')]";

    private final String DIALOG_HEADER_XPATH = DIALOG_CONTAINER + "//div[contains(@id,'ModalDialogHeader')]//h2";

    private final String CANCEL_BUTTON_TOP = DIALOG_CONTAINER + "//div[contains(@class,'cancel-button-top')]";

    private final String BACK_BUTTON = DIALOG_CONTAINER + "//a[@class='back-button']";

    private final String SCHEDULE_BUTTON = DIALOG_CONTAINER + "//button[contains(@class,'schedule')]";

    private final String DIALOG_CONTENT_PANEL = DIALOG_CONTAINER + "//div[contains(@id,'ModalDialogContentPanel')]";

    private final String ONLINE_FROM_INPUT = DIALOG_CONTAINER +
        "//div[contains(@id,'InputView') and descendant::div[text()='Online from']]" + DATA_TIME_PICKER_INPUT;

    private final String ONLINE_TO_INPUT =
        "//div[contains(@id,'InputView') and descendant::div[text()='Online to']]" + DATA_TIME_PICKER_INPUT;

    private final String VALIDATION_MESSAGE = DIALOG_CONTAINER + VALIDATION_RECORDING_VIEWER;

    @FindBy(xpath = ONLINE_FROM_INPUT)
    private WebElement onlineFromInput;

    @FindBy(xpath = ONLINE_TO_INPUT)
    private WebElement onlineToInput;

    @FindBy(xpath = CANCEL_BUTTON_TOP)
    private WebElement cancelButtonTop;

    @FindBy(xpath = BACK_BUTTON)
    private WebElement backButton;

    @FindBy(xpath = SCHEDULE_BUTTON)
    private WebElement scheduleButton;

    public SchedulePublishDialog( final TestSession session )
    {
        super( session );
    }

    public boolean isDisplayed()
    {
        return isElementDisplayed( DIALOG_CONTAINER );
    }

    public SchedulePublishDialog waitUntilDialogShown( long timeout )
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), timeout ) )
        {
            saveScreenshot( "err_schedule_dialog_not_opened" );
            throw new TestFrameworkException( "Schedule dialog was not opened!" );
        }
        return this;
    }

    public void clickOnCancelTopButton()
    {
        cancelButtonTop.click();
        sleep( 200 );
    }

    public void clickOnBackButton()
    {
        backButton.click();
        sleep( 200 );
    }

    public void clickOnScheduleButton()
    {
        scheduleButton.click();
        sleep( 200 );
    }

    public SchedulePublishDialog hideTimePickerPopup()
    {
        buildActions().click( findElement( By.xpath( DIALOG_HEADER_XPATH ) ) ).build().perform();
        sleep( 500 );
        return this;
    }

    public SchedulePublishDialog typeOnlineFrom( String dateTime )
    {
        buildActions().moveToElement( onlineFromInput ).click().build().perform();
        clearAndType( onlineFromInput, dateTime );
        sleep( 300 );
        return this;
    }

    public SchedulePublishDialog typeOnlineTo( String dateTime )
    {
        clearAndType( onlineToInput, dateTime );
        sleep( 300 );
        return this;
    }

    public boolean isCancelTopButtonDisplayed()
    {
        return isElementDisplayed( CANCEL_BUTTON_TOP );
    }

    public boolean isBackButtonDisplayed()
    {
        return isElementDisplayed( BACK_BUTTON );
    }

    public boolean isOnlineFromInputDisplayed()
    {
        return isElementDisplayed( ONLINE_FROM_INPUT );
    }

    public boolean isOnlineToInputDisplayed()
    {
        return isElementDisplayed( ONLINE_TO_INPUT );
    }

    public boolean isValidationMessagePresent()
    {
        return isElementDisplayed( VALIDATION_MESSAGE );
    }

    public String getValidationMessage()
    {
        return getDisplayedString( VALIDATION_MESSAGE );
    }


}
