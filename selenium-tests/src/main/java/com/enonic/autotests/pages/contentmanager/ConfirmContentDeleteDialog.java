package com.enonic.autotests.pages.contentmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Confirm (Site)Delete Dialog
 */
public class ConfirmContentDeleteDialog
    extends Application
{
    public static final String DIALOG_HEADER = "Confirm delete";

    private final String DIALOG_CONTAINER = "//div[contains(@id,'ConfirmContentDeleteDialog')]";

    private final String DIALOG_HEADER_XPATH = "div[contains(@id,'ModalDialogHeader')]";

    private final String CANCEL_BUTTON_TOP = DIALOG_CONTAINER + "//div[contains(@class,'cancel-button-top')]";

    private final String CANCEL_BUTTON_BOTTOM = DIALOG_CONTAINER + "//button[contains(@class,'cancel-button-bottom')]";

    private final String CONFIRM_BUTTON = DIALOG_CONTAINER + "//button[contains(@class,'confirm-delete-action')]";

    private final String DIALOG_CONTENT_PANEL = DIALOG_CONTAINER + "//div[contains(@id,'ModalDialogContentPanel')]";

    private final String NUMBER_OF_CONTENT_TO_DELETE = DIALOG_CONTENT_PANEL + "//span[contains(@class,'confirm-delete-number')]";

    private final String INPUT_FOR_CONFIRM_NUMBER = DIALOG_CONTENT_PANEL + "//input[contains(@id,'TextInput')]";

    @FindBy(xpath = CONFIRM_BUTTON)
    private WebElement confirmButton;

    @FindBy(xpath = CANCEL_BUTTON_TOP)
    private WebElement cancelButtonTop;

    @FindBy(xpath = CANCEL_BUTTON_BOTTOM)
    private WebElement cancelButtonBottom;

    @FindBy(xpath = INPUT_FOR_CONFIRM_NUMBER)
    private WebElement confirmInput;

    public ConfirmContentDeleteDialog( final TestSession session )
    {
        super( session );
    }

    public void clickOnCancelBottomButton()
    {
        cancelButtonBottom.click();
        sleep( 200 );
    }

    public boolean isCancelButtonBottomDisplayed()
    {
        return cancelButtonBottom.isDisplayed();
    }

    public boolean isCancelButtonTopDisplayed()
    {
        return cancelButtonTop.isDisplayed();
    }

    public boolean isConfirmButtonButtonEnabled()
    {
        return confirmButton.isEnabled();
    }

    public boolean isConfirmButtonDisplayed()
    {
        return confirmButton.isDisplayed();
    }


    public boolean isOpened()
    {
        return isElementDisplayed( DIALOG_CONTAINER );
    }

    public void clickOnCancelTopButton()
    {
        cancelButtonTop.click();
        sleep( 200 );
    }

    public int getNumberOfContentToDelete()
    {
        if ( !isElementDisplayed( NUMBER_OF_CONTENT_TO_DELETE ) )
        {
            saveScreenshot( "err_number_content_input" );
            throw new TestFrameworkException( "input for number of content to delete was not found!" );
        }
        return Integer.valueOf( getDisplayedString( NUMBER_OF_CONTENT_TO_DELETE ) );
    }

    public ConfirmContentDeleteDialog typeNumber( String numberOfContent )
    {
        clearAndType( confirmInput, numberOfContent );
        return this;
    }

    public boolean waitUntilConfirmButtonEnabled()
    {
        return waitUntilElementEnabledNoException( By.xpath( CONFIRM_BUTTON ), Application.EXPLICIT_NORMAL );
    }

    public ConfirmContentDeleteDialog clickOnConfirmButton()
    {
        confirmButton.click();
        sleep( 1000 );
        return this;
    }

    public ConfirmContentDeleteDialog waitUntilDialogShown( long timeout )
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), timeout ) )
        {
            TestUtils.saveScreenshot( getSession(), "err_confirm_delete_dialog_not_opened" );
            throw new TestFrameworkException( "Content confirm delete dialog was not shown!" );
        }
        return this;
    }

    public boolean waitUntilDialogClosed( long timeout )
    {
        boolean isPresent = waitsElementNotVisible( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_NORMAL );
        if ( !isPresent )
        {
            TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "err_confirm_dialog-not-closed" ) );
            throw new TestFrameworkException( "Delete confirmation dialog not closed!" );
        }
        return true;
    }
}
