package com.enonic.autotests.pages.contentmanager.wizardpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.pages.LoaderComboBox;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created  on 2/28/2017.
 */
public class InsertImageModalDialog
    extends Application
{
    public static final String HEADER_TEXT = "Insert Image";

    private final String DIALOG_CONTAINER = "//div[contains(@id,'ImageModalDialog')]";

    private final String HEADER = DIALOG_CONTAINER + "//div[contains(@id,'ModalDialogHeader')]";

    private final String INSERT_BUTTON = DIALOG_CONTAINER + "//button[child::span[text()='Insert']]";

    private final String CANCEL_BUTTON = DIALOG_CONTAINER + "//button[contains(@class,'cancel-button-bottom')]";

    private final String CANCEL_BUTTON_TOP = DIALOG_CONTAINER + APP_CANCEL_BUTTON_TOP;

    private final String IMAGE_TOOLBAR = DIALOG_CONTAINER + "//div[contains(@id,'ImageToolbar')]";

    private final String BUTTON_JUSTIFY = "//button[contains(@class,'icon-paragraph-justify')]";

    private final String BUTTON_ALIGN_LEFT = "//button[contains(@class,'icon-paragraph-left')]";

    private final String BUTTON_ALIGN_RIGHT = "//button[contains(@class,'icon-paragraph-right')]";

    private final String BUTTON_ALIGN_CENTER = "//button[contains(@class,'icon-paragraph-right')]";

    private final String IMAGE_CROPPING_SELECTOR = DIALOG_CONTAINER + "//div[contains(@id,'ImageCroppingSelector')]";

    @FindBy(xpath = DIALOG_CONTAINER + COMBOBOX_OPTION_FILTER_INPUT)
    WebElement optionFilterInput;

    @FindBy(xpath = HEADER)
    WebElement header;

    @FindBy(xpath = CANCEL_BUTTON)
    WebElement cancelButton;

    @FindBy(xpath = CANCEL_BUTTON_TOP)
    WebElement cancelButtonTop;

    @FindBy(xpath = INSERT_BUTTON)
    WebElement insertButton;


    public InsertImageModalDialog( TestSession session )
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

    public InsertImageModalDialog selectImage( String displayName )
    {
        LoaderComboBox loaderComboBox = new LoaderComboBox( getSession() );
        clearAndType( optionFilterInput, displayName );
        loaderComboBox.selectOption( displayName );
        sleep( 700 );
        return this;
    }

    public InsertImageModalDialog waitForOpened()
    {
        if ( !waitUntilVisibleNoException( By.xpath( DIALOG_CONTAINER ), EXPLICIT_LONG ) )
        {
            saveScreenshot( NameHelper.uniqueName( "err-insert-image-dialog" ) );
            throw new TestFrameworkException( "Insert image Dialog was not opened!" );
        }
        return this;
    }

    public boolean isOptionFilterInputDisplayed()
    {
        return optionFilterInput.isDisplayed();
    }

    public boolean isCancelButtonDisplayed()
    {
        return cancelButton.isDisplayed();
    }

    public boolean isCancelButtonTopDisplayed()
    {
        return cancelButtonTop.isDisplayed();
    }

    public InsertImageModalDialog clickOnCancelTopButton()
    {
        cancelButtonTop.click();
        return this;
    }

    public InsertImageModalDialog clickOnCancelButton()
    {
        cancelButton.click();
        return this;
    }

    public InsertImageModalDialog clickOnInsertButton()
    {
        insertButton.click();
        return this;
    }


    public boolean waitForClosed()
    {
        return waitsElementNotVisible( By.xpath( DIALOG_CONTAINER ), Application.EXPLICIT_NORMAL );
    }

    public boolean isInsertButtonDisplayed()
    {
        return insertButton.isDisplayed();
    }

    public boolean isToolbarDisplayed()
    {
        return isElementDisplayed( IMAGE_TOOLBAR );
    }

    public boolean isCroppingSelectorDisplayed()
    {
        return isElementDisplayed( IMAGE_CROPPING_SELECTOR );
    }

    public boolean isJustifyButtonDisplayed()
    {
        return isElementDisplayed( BUTTON_JUSTIFY );
    }

    public boolean isAlignRightButtonDisplayed()
    {
        return isElementDisplayed( BUTTON_ALIGN_RIGHT );
    }

    public boolean isAlignLeftButtonDisplayed()
    {
        return isElementDisplayed( BUTTON_ALIGN_LEFT );
    }

    public boolean isAlignCenterButtonDisplayed()
    {
        return isElementDisplayed( BUTTON_ALIGN_CENTER );
    }
}
