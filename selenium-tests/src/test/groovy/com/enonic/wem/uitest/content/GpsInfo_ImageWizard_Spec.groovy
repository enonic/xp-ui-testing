package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.GpsInfoFormViewPanel
import spock.lang.Shared

/**
 * Created on 27.09.2016.*/
class GpsInfo_ImageWizard_Spec
    extends BaseContentSpec
{
    @Shared
    String TEST_ALTITUDE = "100";

    @Shared
    String TEST_GEO_POINT = "10,10";

    @Shared
    String TEST_DIRECTION = "direction";

    def "GIVEN image content opened WHEN 'Gps Info' step was clicked THEN all control elements are present"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_BOOK_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();

        when: "'Gps Info' step was clicked"
        wizard.clickOnWizardStep( "Gps Info" );
        GpsInfoFormViewPanel gpsInfoFormViewPanel = new GpsInfoFormViewPanel( getSession() );
        saveScreenshot( "image_gps_info_empty" )

        then: "input for 'direction'  is present"
        gpsInfoFormViewPanel.isDirectionInputPresent();

        and: "input for 'geo point' is displayed"
        gpsInfoFormViewPanel.isGeoPointInputPresent();

        and: "input for 'altitude'  is present"
        gpsInfoFormViewPanel.isAltitudeInputPresent();
    }

    def "GIVEN image content opened WHEN new gps-info data typed and wizard saved THEN correct info present on the page"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_BOOK_IMAGE ).clickToolbarEdit().waitUntilWizardOpened();
        wizard.clickOnWizardStep( "Gps Info" );
        GpsInfoFormViewPanel gpsInfoFormViewPanel = new GpsInfoFormViewPanel( getSession() );

        when: "gps info typed"
        gpsInfoFormViewPanel.typeAltitude( TEST_ALTITUDE );
        gpsInfoFormViewPanel.typeDirection( TEST_DIRECTION );
        gpsInfoFormViewPanel.typeGeoPoint( TEST_GEO_POINT );

        and: "data saved"
        wizard.save().close( IMPORTED_BOOK_IMAGE );

        and: "image opened again"
        contentBrowsePanel.clickToolbarEdit();
        wizard.clickOnWizardStep( "Gps Info" );
        saveScreenshot( "test_gps_info_saved" );

        then: "correct value for 'altitude' displayed"
        gpsInfoFormViewPanel.getAltitude() == TEST_ALTITUDE;

        and: "correct value for 'direction' displayed"
        gpsInfoFormViewPanel.getDirection() == TEST_DIRECTION;

        and: "correct value for 'geo point' displayed"
        gpsInfoFormViewPanel.getGeoPoint() == TEST_GEO_POINT;
    }
}
