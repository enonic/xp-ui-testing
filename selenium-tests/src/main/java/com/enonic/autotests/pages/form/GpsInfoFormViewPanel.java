package com.enonic.autotests.pages.form;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.xp.data.PropertyTree;

import static com.enonic.autotests.utils.SleepHelper.sleep;

/**
 * Created on 26.09.2016.
 */
public class GpsInfoFormViewPanel
    extends FormViewPanel
{
    private final String GPS_INFO_STEP_FORM = "//div[contains(@id,'XDataWizardStepForm') and preceding-sibling::div[child::span[text()='Location']]]";

    private final String ALTITUDE = GPS_INFO_STEP_FORM + "//input[contains(@name,'altitude')]";

    private final String DIRECTION = GPS_INFO_STEP_FORM + "//input[contains(@name,'direction')]";

    private final String GEO_POINT = GPS_INFO_STEP_FORM + "//input[contains(@title,'latitude,longitude')]";


    @FindBy(xpath = ALTITUDE)
    private WebElement altitudeInput;

    @FindBy(xpath = DIRECTION)
    private WebElement directionInput;

    @FindBy(xpath = GEO_POINT)
    private WebElement geoPointInput;


    public GpsInfoFormViewPanel( final TestSession session )
    {
        super( session );
    }

    @Override
    public FormViewPanel type( final PropertyTree data )
    {
        throw new TestFrameworkException( "method not implemented  in GpsInfoFormViewPanel" );
    }

    public void typeAltitude( String altitude )
    {
        clearAndType( altitudeInput, altitude );
        sleep( 500 );
    }

    public void typeDirection( String direction )
    {
        clearAndType( directionInput, direction );
        sleep( 500 );
    }

    public void typeGeoPoint( String geoPoint )
    {
        clearAndType( geoPointInput, geoPoint );
        sleep( 500 );
    }

    public boolean isAltitudeInputPresent()
    {
        return altitudeInput.isDisplayed();
    }

    public boolean isDirectionInputPresent()
    {
        return directionInput.isDisplayed();
    }

    public boolean isGeoPointInputPresent()
    {
        return geoPointInput.isDisplayed();
    }

    public String getAltitude()
    {
        return altitudeInput.getAttribute( "value" );
    }

    public String getGeoPoint()
    {
        return geoPointInput.getAttribute( "value" );
    }

    public String getDirection()
    {
        return directionInput.getAttribute( "value" );
    }
}

