package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.LocationFormViewPanel
import spock.lang.Shared

/**
 * Created on 27.09.2016.
 * */
class Location_ImageWizard_Spec
    extends BaseContentSpec
{
    @Shared
    String TEST_ALTITUDE = "100";

    @Shared
    String TEST_GEO_POINT = "10,10";

    @Shared
    String TEST_DIRECTION = "direction";

    def "GIVEN image content is opened WHEN 'Location' step has been clicked THEN expected control elements should be present"()
    {
        given: "content wizard is opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEditAndSwitchToWizardTab();

        when: "'Location' step was clicked"
        wizard.clickOnWizardStep( "Location" );
        LocationFormViewPanel gpsInfoFormViewPanel = new LocationFormViewPanel( getSession() );
        saveScreenshot( "image_gps_info_empty" )

        then: "input for 'direction' should be present"
        gpsInfoFormViewPanel.isDirectionInputPresent();

        and: "input for 'geo point' should be displayed"
        gpsInfoFormViewPanel.isGeoPointInputPresent();

        and: "input for 'altitude' should be present"
        gpsInfoFormViewPanel.isAltitudeInputPresent();
    }

    def "GIVEN image content is opened WHEN new Location typed and content has been saved THEN expected info should be present on the page"()
    {
        given: "content wizard is opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME ).clickToolbarEditAndSwitchToWizardTab();
        wizard.clickOnWizardStep( "Location" );
        LocationFormViewPanel locationFormViewPanel = new LocationFormViewPanel( getSession() );

        when: "gps info has been typed"
        locationFormViewPanel.typeAltitude( TEST_ALTITUDE );
        locationFormViewPanel.typeDirection( TEST_DIRECTION );
        locationFormViewPanel.typeGeoPoint( TEST_GEO_POINT );

        and: "data saved and the wizard has been closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        and: "image is opened again"
        contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab();
        wizard.clickOnWizardStep( "Location" );
        saveScreenshot( "test_gps_info_saved" );

        then: "correct value for 'altitude' should be displayed"
        locationFormViewPanel.getAltitude() == TEST_ALTITUDE;

        and: "correct value for 'direction' should be displayed"
        locationFormViewPanel.getDirection() == TEST_DIRECTION;

        and: "correct value for 'geo point' should be displayed"
        locationFormViewPanel.getGeoPoint() == TEST_GEO_POINT;
    }
}
