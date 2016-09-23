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
        "//div[contains(@id,'ContentWizardStepForm') and preceding-sibling::h2[text()='Photo Info']]";

    private final String PHOTO_LENS_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'lens')]";

    private final String PHOTO_FOCAL_LENGTH_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'focalLength')]";

    private final String PHOTO_MAKE_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'make')]";

    private final String PHOTO_MODEL_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'model')]";

    private final String PHOTO_ISO_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'iso')]";

    private final String PHOTO_EXPOSURE_BIAS_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'exposureBias')]";

    private final String DATE_TIME_INPUT = PHOTO_INFO_STEP_FORM + "//div[contains(@id,'DateTimePicker')]//input[contains(@id,'TextInput')]";

    private final String PHOTO_FOCAL_LENGTH35_INPUT = PHOTO_INFO_STEP_FORM + "//input[contains(@name,'focalLength35')]";

    @FindBy(xpath = PHOTO_FOCAL_LENGTH35_INPUT)
    private WebElement focalLength35Input;

    @FindBy(xpath = PHOTO_LENS_INPUT)
    private WebElement lensInput;

    @FindBy(xpath = PHOTO_FOCAL_LENGTH_INPUT)
    private WebElement focalLengthInput;

    @FindBy(xpath = DATE_TIME_INPUT)
    private WebElement dateTimeInput;

    @FindBy(xpath = PHOTO_MAKE_INPUT)
    private WebElement makeInput;

    @FindBy(xpath = PHOTO_MODEL_INPUT)
    private WebElement modelInput;

    @FindBy(xpath = PHOTO_ISO_INPUT)
    private WebElement isoInput;

    @FindBy(xpath = PHOTO_EXPOSURE_BIAS_INPUT)
    private WebElement exposureBiasInput;


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
}
