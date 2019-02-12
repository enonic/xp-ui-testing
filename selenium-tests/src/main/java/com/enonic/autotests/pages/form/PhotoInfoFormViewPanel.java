package com.enonic.autotests.pages.form;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 22.09.2016.
 */
public class PhotoInfoFormViewPanel
    extends FormViewPanel
{
    private final String PHOTO_INFO_STEP_FORM =
        "//div[contains(@id,'XDataWizardStepForm') and preceding-sibling::div[child::span[text()='Photo Info']]]";

    private final String LENS_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'lens')]";

    private final String FOCAL_LENGTH_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'focalLength')]";

    private final String MAKE_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'make')]";

    private final String MODEL_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'model')]";

    private final String ISO_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'iso')]";

    private final String EXPOSURE_BIAS_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'exposureBias')]";

    private final String APERTURE_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'aperture')]";

    private final String DATE_TIME_INPUT = PHOTO_INFO_STEP_FORM + "//div[contains(@id,'DateTimePicker')]//input[contains(@id,'TextInput')]";

    private final String FOCAL_LENGTH35_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'focalLength35')]";

    private final String ORIENTATION_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'orientation')]";

    private final String FOCUS_DISTANCE_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'focusDistance')]";

    private final String EXPOSURE_MODE_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'exposureMode')]";

    private final String SHOOTING_MODE_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'shootingMode')]";

    private final String EXPOSURE_PROGRAM_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'exposureProgram')]";

    private final String WHITE_BALANCE_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'whiteBalance')]";

    private final String AUTO_FLASH_COMPENSATION_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'autoFlashCompensation')]";

    private final String FLASH_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'flash')]";

    private final String SHUTTER_TIME_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'shutterTime')]";

    private final String METERING_MODE_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'meteringMode')]";


    @FindBy(xpath = FOCAL_LENGTH35_INPUT)
    private WebElement focalLength35Input;

    @FindBy(xpath = LENS_INPUT)
    private WebElement lensInput;

    @FindBy(xpath = FOCAL_LENGTH_INPUT)
    private WebElement focalLengthInput;

    @FindBy(xpath = DATE_TIME_INPUT)
    private WebElement dateTimeInput;

    @FindBy(xpath = MAKE_INPUT)
    private WebElement makeInput;

    @FindBy(xpath = MODEL_INPUT)
    private WebElement modelInput;

    @FindBy(xpath = ISO_INPUT)
    private WebElement isoInput;

    @FindBy(xpath = EXPOSURE_BIAS_INPUT)
    private WebElement exposureBiasInput;

    @FindBy(xpath = APERTURE_INPUT)
    private WebElement apertureInput;

    @FindBy(xpath = FOCUS_DISTANCE_INPUT)
    private WebElement focusDistanceInput;

    @FindBy(xpath = EXPOSURE_MODE_INPUT)
    private WebElement exposureModeInput;

    @FindBy(xpath = SHOOTING_MODE_INPUT)
    private WebElement shootingModeInput;

    @FindBy(xpath = EXPOSURE_PROGRAM_INPUT)
    private WebElement exposureProgramInput;

    @FindBy(xpath = WHITE_BALANCE_INPUT)
    private WebElement whiteBalanceInput;

    @FindBy(xpath = METERING_MODE_INPUT)
    private WebElement meteringModeInput;

    @FindBy(xpath = SHUTTER_TIME_INPUT)
    private WebElement shutterTimeInput;

    @FindBy(xpath = AUTO_FLASH_COMPENSATION_INPUT)
    private WebElement autoFlashCompensationInput;

    @FindBy(xpath = FLASH_INPUT)
    private WebElement flashInput;

    @FindBy(xpath = ORIENTATION_INPUT)
    private WebElement orientationInput;


    public PhotoInfoFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        throw new TestFrameworkException( "method not implemented  in PhotoInfoFormViewPanel" );
    }

    public void typeDateTime( String datetime )
    {
        clearAndType( dateTimeInput, datetime );
        sleep( 500 );
    }

    public boolean isFocalLengthInputPresent()
    {
        return focalLengthInput.isDisplayed();
    }

    public boolean isFocalLength35InputPresent()
    {
        return focalLength35Input.isDisplayed();
    }


    public boolean isDateTimeInputPresent()
    {
        return dateTimeInput.isDisplayed();
    }

    public boolean isLensInputPresent()
    {
        return lensInput.isDisplayed();
    }

    public boolean isMakeInputPresent()
    {
        return makeInput.isDisplayed();
    }

    public boolean isModelInputPresent()
    {
        return modelInput.isDisplayed();
    }

    public boolean isISOInputPresent()
    {
        return isoInput.isDisplayed();
    }

    public boolean isExposureBiasInputPresent()
    {
        return exposureBiasInput.isDisplayed();
    }

    public boolean isApertureInputPresent()
    {
        return apertureInput.isDisplayed();
    }

    public boolean isFocusDistancePresent()
    {
        return focusDistanceInput.isDisplayed();
    }

    public boolean isExposureModePresent()
    {
        return exposureModeInput.isDisplayed();
    }

    public boolean isShootingModePresent()
    {
        return shootingModeInput.isDisplayed();
    }

    public boolean isExposureProgramPresent()
    {
        return exposureProgramInput.isDisplayed();
    }

    public boolean isWhiteBalancePresent()
    {
        return whiteBalanceInput.isDisplayed();
    }

    public boolean isAutoFlashCompensationPresent()
    {
        return autoFlashCompensationInput.isDisplayed();
    }

    public boolean isPhotoFlashPresent()
    {
        return flashInput.isDisplayed();
    }

    public boolean isShutterTimePresent()
    {
        return shutterTimeInput.isDisplayed();
    }

    public boolean isOrientationPresent()
    {
        return orientationInput.isDisplayed();
    }

    public boolean isMeteringModePresent()
    {
        return meteringModeInput.isDisplayed();
    }
}
