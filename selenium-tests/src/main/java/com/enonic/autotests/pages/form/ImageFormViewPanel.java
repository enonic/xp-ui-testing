package com.enonic.autotests.pages.form;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditor;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 20.09.2016.
 */
public class ImageFormViewPanel
    extends FormViewPanel
{
    private final String STEP_NAVIGATOR = "//ul[contains(@id,'WizardStepNavigator')]";

    private final String IMG = FORM_VIEW + "//img[contains(@class,'image-bg')]";

    private final String IMAGE_INFO_TAB_BAR_ITEM = STEP_NAVIGATOR + String.format( TAB_BAR_ITEM, "Properties" );

    private final String PHOTO_INFO_TAB_BAR_ITEM = STEP_NAVIGATOR + String.format( TAB_BAR_ITEM, "Photo" );

    private final String GPS_INFO_TAB_BAR_ITEM = STEP_NAVIGATOR + String.format( TAB_BAR_ITEM, "Location" );

    private final String CAPTION_TEXTAREA = FORM_VIEW + "//textarea[contains(@name,'caption')]";

    private final String COPYRIGHT_TEXT_INPUT = FORM_VIEW + "//input[contains(@name,'copyright')]";

    private final String IMAGE_UPLOADER = "//div[contains(@id,'ImageUploader')]";

    private final String IMAGE_EDITOR = IMAGE_UPLOADER + "//div[contains(@id,'ImageEditor')]";

    private final String BUTTON_CROP = IMAGE_EDITOR + "//button[contains(@class,'button-crop')]";

    private final String BUTTON_RESET = IMAGE_EDITOR + "//button[contains(@class,'button-reset')]";

    private final String BUTTON_FOCUS = IMAGE_EDITOR + "//button[contains(@class,'button-focus')]";

    private final String ARTISTS_TAGS_INPUT =
        FORM_VIEW + "//div[contains(@id,'InputView') and descendant::div[@class='label' and text()='Artist']]//input[@type='text']";

    private final String TAGS_INPUT =
        FORM_VIEW + "//div[contains(@id,'InputView') and descendant::div[@class='label' and text()='Tags']]//input[@type='text']";

    private final String ARTISTS_TAGS = FORM_VIEW +
        "//div[contains(@id,'InputView') and descendant::div[@class='label' and text()='Artist']]//ul/li[contains(@id,'Tag')]/span";

    private final String TAGS = FORM_VIEW +
        "//div[contains(@id,'InputView') and descendant::div[@class='label' and text()='Tags']]//ul/li[contains(@id,'Tag')]/span";

    @FindBy(xpath = CAPTION_TEXTAREA)
    private WebElement captionTextArea;

    @FindBy(xpath = ARTISTS_TAGS_INPUT)
    private WebElement artistTagsInput;

    @FindBy(xpath = TAGS_INPUT)
    private WebElement tagsInput;

    @FindBy(xpath = COPYRIGHT_TEXT_INPUT)
    private WebElement copyrightInput;

    @FindBy(xpath = BUTTON_CROP)
    private WebElement buttonCrop;

    @FindBy(xpath = BUTTON_RESET)
    private WebElement buttonReset;

    @FindBy(xpath = BUTTON_FOCUS)
    private WebElement buttonFocus;


    public ImageFormViewPanel( final TestSession session )
    {
        super( session );
    }

    public ImageEditor clickOnCropButton()
    {
        boolean isClickable = waitUntilClickableNoException( By.xpath( BUTTON_CROP ), EXPLICIT_NORMAL );
        if ( !isClickable )
        {
            saveScreenshot( "err_crop_button" );
            throw new TestFrameworkException( "button crop was not found" );
        }
        buttonCrop.click();
        sleep( 700 );
        return new ImageEditor( getSession() );
    }

    public ImageEditor clickOnFocusButton()
    {
        boolean isClickable = waitUntilClickableNoException( By.xpath( BUTTON_FOCUS ), EXPLICIT_LONG );
        if ( !isClickable )
        {
            saveScreenshot( "err_focus_button" );
            throw new TestFrameworkException( "button focus was not found" );
        }
        buttonFocus.click();
        sleep( 200 );
        return new ImageEditor( getSession() );
    }

    public ImageFormViewPanel clickOnResetButton()
    {
        buttonReset.click();
        waitsElementNotVisible( By.xpath( BUTTON_RESET ), EXPLICIT_NORMAL );
        sleep( 2000 );
        return this;
    }

    public ImageFormViewPanel waitUntilImageLoaded()
    {
        boolean isLoaded = waitUntilVisibleNoException( By.xpath( IMG ), EXPLICIT_NORMAL );
        sleep( 1000 );
        if ( !isLoaded )
        {
            saveScreenshot( "err_image_not_loaded" );
            throw new TestFrameworkException( "Image was not loaded in the wizard" );
        }
        return this;
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        return null;
    }

    public boolean isImageInfoTabBarItemPresent()
    {
        return isElementDisplayed( IMAGE_INFO_TAB_BAR_ITEM );
    }

    public boolean isPhotoInfoTabBarItemPresent()
    {
        return isElementDisplayed( PHOTO_INFO_TAB_BAR_ITEM );
    }

    public boolean isGpsInfoTabBarItemPresent()
    {
        return isElementDisplayed( GPS_INFO_TAB_BAR_ITEM );
    }

    public boolean isCaptionTextAreaPresent()
    {
        return isElementDisplayed( CAPTION_TEXTAREA );
    }

    public boolean isCopyrightInputPresent()
    {
        return isElementDisplayed( COPYRIGHT_TEXT_INPUT );
    }

    public boolean isImageUploaderPresent()
    {
        return isElementDisplayed( IMAGE_UPLOADER );
    }

    public boolean isButtonCropPresent()
    {
        return isElementDisplayed( BUTTON_CROP );
    }

    public boolean isButtonFocusPresent()
    {
        return isElementDisplayed( BUTTON_FOCUS );
    }

    public boolean isButtonResetPresent()
    {
        return waitUntilVisibleNoException( By.xpath( BUTTON_RESET ), Application.EXPLICIT_NORMAL );
    }

    public boolean isArtistTagInputPresent()
    {
        return isElementDisplayed( ARTISTS_TAGS_INPUT );
    }

    public ImageFormViewPanel typeInArtistsInput( String... tags )
    {
        for ( int i = 0; i < tags.length; i++ )
        {
            artistTagsInput.sendKeys( tags[i] );
            sleep( 250 );
            artistTagsInput.sendKeys( Keys.ENTER );
            sleep( 250 );
        }
        return this;
    }

    public ImageFormViewPanel typeTags( String... tags )
    {
        for ( int i = 0; i < tags.length; i++ )
        {
            tagsInput.sendKeys( tags[i] );
            sleep( 200 );
            tagsInput.sendKeys( Keys.ENTER );
            sleep( 200 );
        }
        return this;
    }

    public ImageFormViewPanel typeInCaptionTextArea( String captionText )
    {
        clearAndType( captionTextArea, captionText );
        sleep( 200 );
        return this;
    }

    public ImageFormViewPanel typeInCopyRightInput( String text )
    {
        clearAndType( copyrightInput, text );
        sleep( 200 );
        return this;
    }

    public String getCaptionText()
    {
        String id = captionTextArea.getAttribute( "id" );
        return (String) getJavaScriptExecutor().executeScript( String.format( ELEMENT_BY_ID, id ) + ".getValue()" );
    }

    public String getTextFromCopyright()
    {
        return copyrightInput.getAttribute( "value" );
    }

    public List<String> getArtistsTagsText()
    {
        return getDisplayedStrings( By.xpath( ARTISTS_TAGS ) );
    }

    public List<String> getTagsText()
    {
        return getDisplayedStrings( By.xpath( TAGS ) );
    }
}
