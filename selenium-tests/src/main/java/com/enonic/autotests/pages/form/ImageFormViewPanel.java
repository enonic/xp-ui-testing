package com.enonic.autotests.pages.form;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 20.09.2016.
 */
public class ImageFormViewPanel
    extends FormViewPanel
{
    private final String STEP_NAVIGATOR = "//ul[contains(@id,'WizardStepNavigator')]";

    private final String IMAGE_INFO_TAB_BAR_ITEM = STEP_NAVIGATOR + "//li[contains(@id,'TabBarItem') and child::span[text()='Image Info']]";

    private final String PHOTO_INFO_TAB_BAR_ITEM = STEP_NAVIGATOR + "//li[contains(@id,'TabBarItem') and child::span[text()='Photo Info']]";

    private final String GPS_INFO_TAB_BAR_ITEM = STEP_NAVIGATOR + "//li[contains(@id,'TabBarItem') and child::span[text()='Gps Info']]";

    private final String CAPTION_TEXTAREA = FORM_VIEW + "//textarea[contains(@name,'caption')]";

    private final String COPYRIGHT_TEXT_INPUT = FORM_VIEW + "//input[contains(@name,'copyright')]";

    private final String IMAGE_UPLOADER = "//div[contains(@id,'ImageUploader')]";

    private final String IMAGE_EDITOR = IMAGE_UPLOADER + "//div[contains(@id,'ImageEditor')]";

    private final String BUTTON_CROP = IMAGE_EDITOR + "//button[contains(@class,'button-crop')]";

    private final String BUTTON_FOCUS = IMAGE_EDITOR + "//button[contains(@class,'button-focus')]";

    private final String ARTISTS_TAGS_INPUT =
        FORM_VIEW + "//div[contains(@id,'InputView') and descendant::div[@class='label' and text()='Artist']]//input[@type='text']";

    @FindBy(xpath = CAPTION_TEXTAREA)
    private WebElement captionTextArea;

    @FindBy(xpath = ARTISTS_TAGS_INPUT)
    private WebElement artistTagsInput;

    @FindBy(xpath = COPYRIGHT_TEXT_INPUT)
    private WebElement copyrightInput;

    public ImageFormViewPanel( final TestSession session )
    {
        super( session );
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

    public boolean isArtistTagInputPresent()
    {
        return isElementDisplayed( ARTISTS_TAGS_INPUT );
    }

    public ImageFormViewPanel typeInArtistsInput( String... tags )
    {
        for ( int i = 0; i < tags.length; i++ )
        {
            artistTagsInput.sendKeys( tags[i] );
            sleep( 300 );
            artistTagsInput.sendKeys( Keys.ENTER );
            sleep( 300 );
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

}
