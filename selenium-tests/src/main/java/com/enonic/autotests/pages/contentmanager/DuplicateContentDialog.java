package com.enonic.autotests.pages.contentmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;

import static com.enonic.autotests.utils.SleepHelper.sleep;

public class DuplicateContentDialog
    extends Application
{
    public static final String DIALOG_TITLE = "Duplicate content";

    private final String CONTAINER = "//div[contains(@id,'ContentDuplicateDialog')]";


    private final String BUTTON_DUPLICATE =
        CONTAINER + "//button[contains(@id,'dialog.DialogButton') and descendant::span[contains(.,'Duplicate')]]";

    private final String BUTTON_CANCEL = CONTAINER + "//button[contains(@id,'dialog.DialogButton') and descendant::span[text()='Cancel']]";

    @FindBy(xpath = BUTTON_DUPLICATE)
    WebElement duplicateButton;

    @FindBy(xpath = BUTTON_CANCEL)
    WebElement buttonCancel;


    public DuplicateContentDialog( final TestSession session )
    {
        super( session );
    }

    public DuplicateContentDialog waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( CONTAINER ), Application.EXPLICIT_NORMAL ) )
        {
            saveScreenshot( "err_open_duplicate_dialog" );
            throw new TestFrameworkException( "'Duplicate' dialog was not opened!" );
        }
        return this;
    }

    public boolean isOpened()
    {
        return isElementDisplayed( CONTAINER );
    }

    public DuplicateContentDialog clickOnDuplicateButton()
    {
        waitUntilElementEnabledNoException( By.xpath( BUTTON_DUPLICATE ), Application.EXPLICIT_NORMAL );
        duplicateButton.click();
        sleep( 1000 );
        return this;
    }

    public boolean waitForClosed()
    {
        boolean result = waitsElementNotVisible( By.xpath( CONTAINER ), Application.EXPLICIT_LONG );
        if ( !result )
        {
            saveScreenshot( "content_duplicate_not_closed" );
            throw new TestFrameworkException( "'Duplicate' dialog was not closed!" );
        }
        return result;
    }

    public boolean isDuplicateButtonDisplayed()
    {
        return duplicateButton.isDisplayed();
    }

    public boolean isButtonCancelPresent()
    {
        return buttonCancel.isDisplayed();
    }
}
