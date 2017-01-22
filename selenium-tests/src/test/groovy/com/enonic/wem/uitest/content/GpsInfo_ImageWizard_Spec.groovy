package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.GpsInfoFormViewPanel
import spock.lang.Shared

/**
 * XP-4175 Add selenium tests for Gps info in ImageWizard
 *
 * Created on 27.09.2016.
 * */
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
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEditAndSwitchToWizardTab();

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

    def "GIVEN image content opened WHEN new gps-info data typed and wizard saved THEN correct info should be present on the page"()
    {
        given: "content wizard opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEditAndSwitchToWizardTab();
        wizard.clickOnWizardStep( "Gps Info" );
        GpsInfoFormViewPanel gpsInfoFormViewPanel = new GpsInfoFormViewPanel( getSession() );

        when: "gps info typed"
        gpsInfoFormViewPanel.typeAltitude( TEST_ALTITUDE );
        gpsInfoFormViewPanel.typeDirection( TEST_DIRECTION );
        gpsInfoFormViewPanel.typeGeoPoint( TEST_GEO_POINT );

        and: "data saved"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        and: "image opened again"
        contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab();
        wizard.clickOnWizardStep( "Gps Info" );
        saveScreenshot( "test_gps_info_saved" );

        then: "correct value for 'altitude' should be displayed"
        gpsInfoFormViewPanel.getAltitude() == TEST_ALTITUDE;

        and: "correct value for 'direction' should be displayed"
        gpsInfoFormViewPanel.getDirection() == TEST_DIRECTION;

        and: "correct value for 'geo point' should be displayed"
        gpsInfoFormViewPanel.getGeoPoint() == TEST_GEO_POINT;
    }
}
