package com.enonic.autotests.pages.contentmanager.wizardpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created  on 2/28/2017.
 */
public class InsertAnchorModalDialog
    extends Application
{
    public static final String HEADER_TEXT = "Insert Anchor";

    private final String DIALOG_CONTAINER = "//div[contains(@id,'AnchorModalDialog')]";

    private final String HEADER = DIALOG_CONTAINER + "//div[contains(@id,'ModalDialogHeader')]";

    private final String ANCHOR_INPUT = DIALOG_CONTAINER + "//input[contains(@id,'TextInput')]";

    private final String INSERT_BUTTON = DIALOG_CONTAINER + "//button[child::span[text()='Insert']]";

    private final String CANCEL_BUTTON = DIALOG_CONTAINER + "//button[contains(@class,'cancel-button-bottom')]";

    private final String CANCEL_BUTTON_TOP = DIALOG_CONTAINER + APP_CANCEL_BUTTON_TOP;

    @FindBy(xpath = HEADER)
    WebElement header;

    @FindBy(xpath = ANCHOR_INPUT)
    WebElement anchorInput;

    @FindBy(xpath = CANCEL_BUTTON)
    WebElement cancelButton;

    @FindBy(xpath = CANCEL_BUTTON_TOP)
    WebElement cancelButtonTop;

    @FindBy(xpath = INSERT_BUTTON)
    WebElement insertButton;

    public InsertAnchorModalDialog( TestSession session )
    {
        super( session );
    }

    public boolean isHeaderDisplayed()
    {
        return header.isDisplayed();
    }

    public String getHeader()
    {
        return header.getText();
    }

    public boolean isAnchorInputDisplayed()
    {
        return anchorInput.isDisplayed();
    }

    public InsertAnchorModalDialog typeText( String anchorText )
    {
        clearAndType( anchorInput, anchorText );
        return this;
    }

    public InsertAnchorModalDialog waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), EXPLICIT_LONG ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err-anchor-dialog" ) );
            throw new TestFrameworkException( "Insert Anchor Dialog was not opened!" );
        }
        return this;
    }

    public InsertAnchorModalDialog clickOnCancelButton()
    {
        cancelButton.click();
        return this;
    }

    public boolean isCancelButtonDisplayed()
    {
        return cancelButton.isDisplayed();
    }

    public boolean waitForClosed()
    {
        return waitsElementNotVisible( By.xpath( DIALOG_CONTAINER ), 2 );
    }

    public InsertAnchorModalDialog clickOnCancelTopButton()
    {
        cancelButtonTop.click();
        return this;
    }

    public boolean isInsertButtonDisplayed()
    {
        return insertButton.isDisplayed();
    }

    public InsertAnchorModalDialog clickOnInsertButton()
    {
        insertButton.click();
        return this;
    }

    @Override
    public void pressEscapeKey()
    {
        findElement( By.xpath( HEADER ) ).click();
        sleep( 500 );
        super.pressEscapeKey();
    }
}
