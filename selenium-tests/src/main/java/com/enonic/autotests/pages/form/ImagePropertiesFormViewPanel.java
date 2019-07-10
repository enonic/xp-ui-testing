package com.enonic.autotests.pages.form;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.xp.data.PropertyTree;

/**
 * Created  on 22.09.2016.
 */
public class ImagePropertiesFormViewPanel
    extends FormViewPanel
{
    private final String IMAGE_INFO_STEP_FORM =
        "//div[contains(@id,'XDataWizardStepForm') and preceding-sibling::div[child::span[text()='Properties']]]";

    private final String SIZE_PIXELS_LONG_INPUT = IMAGE_INFO_STEP_FORM + "//input[contains(@name,'pixelSize')]";

    private final String IMAGE_HEIGHT_INPUT = IMAGE_INFO_STEP_FORM + "//input[contains(@name,'imageHeight')]";

    private final String IMAGE_WIDTH_INPUT = IMAGE_INFO_STEP_FORM + "//input[contains(@name,'imageWidth')]";

    private final String IMAGE_TYPE = IMAGE_INFO_STEP_FORM + "//input[contains(@name,'contentType')]";

    private final String IMAGE_DESCRIPTION = IMAGE_INFO_STEP_FORM + "//input[contains(@name,'description')]";

    private final String IMAGE_FILE_SOURCE_INPUT = IMAGE_INFO_STEP_FORM + "//input[contains(@name,'fileSource')]";

    private final String IMAGE_COLOR_SPACE_INPUT = IMAGE_INFO_STEP_FORM + "//input[contains(@name,'colorSpace')]";

    private final String BYTE_SIZE_INPUT = IMAGE_INFO_STEP_FORM + "//input[contains(@name,'byteSize')]";

    @FindBy(xpath = SIZE_PIXELS_LONG_INPUT)
    private WebElement sizeInPixelsInput;

    @FindBy(xpath = IMAGE_HEIGHT_INPUT)
    private WebElement imageHeightInput;

    @FindBy(xpath = IMAGE_WIDTH_INPUT)
    private WebElement imageWidthInput;

    @FindBy(xpath = IMAGE_DESCRIPTION)
    private WebElement imageDescriptionInput;


    public ImagePropertiesFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        return null;
    }

    public boolean isInputForSizeInPixelsDisplayed()
    {
        return isElementDisplayed( SIZE_PIXELS_LONG_INPUT );
    }

    public boolean isInputForImageHeightDisplayed()
    {
        return isElementDisplayed( IMAGE_HEIGHT_INPUT );
    }

    public boolean isInputForImageTypeDisplayed()
    {
        return isElementDisplayed( IMAGE_TYPE );
    }

    public boolean isInputForImageDescriptionDisplayed()
    {
        return isElementDisplayed( IMAGE_DESCRIPTION );
    }

    public boolean isInputForFileSourceDisplayed()
    {
        return isElementDisplayed( IMAGE_FILE_SOURCE_INPUT );
    }

    public boolean isInputForColorSpaceDisplayed()
    {
        return isElementDisplayed( IMAGE_COLOR_SPACE_INPUT );
    }

    public boolean isInputForImageWidthDisplayed()
    {
        return isElementDisplayed( IMAGE_WIDTH_INPUT );
    }

    public boolean isInputForSizeInBytesDisplayed()
    {
        return isElementDisplayed( BYTE_SIZE_INPUT );
    }


    public ImagePropertiesFormViewPanel typeSizeInPixels( String size )
    {
        clearAndType( sizeInPixelsInput, size );
        return this;
    }

    public ImagePropertiesFormViewPanel typeDescription( String description )
    {
        clearAndType( imageDescriptionInput, description );
        return this;
    }

    public String getDescription()
    {
        return imageDescriptionInput.getAttribute( "value" );
    }
}
