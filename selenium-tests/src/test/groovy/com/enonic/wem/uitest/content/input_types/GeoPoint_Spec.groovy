package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.GeoPointFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class GeoPoint_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    String TEST_GEO_LOCATION = "10,10";

    @Shared
    String WRONG_GEO_LOCATION = "1,181";

    def "GIVEN content type with name 'Geo Location' selected and wizard opened WHEN geo point value typed and content saved THEN new content with correct value listed "()
    {
        given: "add a content with type 'Geo point'"
        Content geopointContent = buildGeoPoint0_0_Content( TEST_GEO_LOCATION );
        selectSitePressNew( geopointContent.getContentTypeName() ).waitUntilWizardOpened().typeData(
            geopointContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "just created content selected and 'Edit' button clicked"
        findAndSelectContent( geopointContent.getName() ).clickToolbarEditAndSwitchToWizardTab();
        GeoPointFormViewPanel geoPointFormViewPanel = new GeoPointFormViewPanel( getSession() );

        then: "actual value in the form view and expected should be equals"
        geoPointFormViewPanel.getGeoPointValue().equals( TEST_GEO_LOCATION );
    }

    def "GIVEN wizard for adding a content with type 'Geo Location' opened WHEN value of 'Geo Location' is not within range and content saved and wizard closed THEN incorrect value is not saved"()
    {
        given: "value of 'Geo Location' is not within range"
        Content notValidContent = buildGeoPoint0_0_Content( WRONG_GEO_LOCATION );

        and: "content saved"
        selectSitePressNew( notValidContent.getContentTypeName() ).typeData(
            notValidContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "the content opened"
        findAndSelectContent( notValidContent.getName() ).clickToolbarEditAndSwitchToWizardTab();
        GeoPointFormViewPanel geoPointFormViewPanel = new GeoPointFormViewPanel( getSession() );

        then: "incorrect value not saved"
        geoPointFormViewPanel.getGeoPointValue().isEmpty();
    }

    def "GIVEN wizard for adding a content with type 'Geo Location' opened WHEN value of 'Geo Location' is not within range THEN red icon present on the wizard-tab"()
    {
        given: "add a content with type 'Geo point'"
        Content notValidContent = buildGeoPoint1_1_Content( WRONG_GEO_LOCATION );
        ContentWizardPanel wizard = selectSitePressNew( notValidContent.getContentTypeName() );

        when: "'Save' button on toolbar pressed"
        wizard.typeData( notValidContent );
        GeoPointFormViewPanel geoPointFormViewPanel = new GeoPointFormViewPanel( getSession() );

        then: "input has a red border"
        !geoPointFormViewPanel.isGeoLocationValid();
    }

    def "GIVEN wizard for adding a content with type 'Geo Location' opened WHEN value of 'Geo Location' is not within range THEN red icon present in the browse panel and content is not valid "()
    {
        given: "add a content with type 'Geo point'"
        Content notValidContent = buildGeoPoint1_1_Content( WRONG_GEO_LOCATION );
        ContentWizardPanel wizard = selectSitePressNew( notValidContent.getContentTypeName() ).typeData( notValidContent );
        saveScreenshot( "geo-location-incorrect-value" );

        when: "'Save' button on toolbar pressed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        then: "the content has red icon(invalid) in the grid, because input is required"
        filterPanel.typeSearchText( notValidContent.getName() );
        contentBrowsePanel.isContentInvalid( notValidContent.getName() );
    }

    def "GIVEN valid data typed WHEN switched to the browse panel THEN the content displayed as valid "()
    {
        given: "add a content with type 'Geo point'"
        Content validContent = buildGeoPoint1_1_Content( TEST_GEO_LOCATION );
        ContentWizardPanel wizard = selectSitePressNew( validContent.getContentTypeName() ).typeData( validContent );

        when: "content saved and HomeButton clicked"
        wizard.save();
        and: "switched to the browse panel"
        wizard.switchToBrowsePanelTab();
        filterPanel.typeSearchText( validContent.getName() );
        saveScreenshot( "geo-location-valid-in-grid" );

        then: "the content displayed as valid"
        !contentBrowsePanel.isContentInvalid( validContent.getName() );
    }

    def "GIVEN wizard for adding a content with type 'Geo Location' opened WHEN all data typed THEN red icon not present in the wizard "()
    {
        when: "add a content with type 'Geo point'"
        Content validContent = buildGeoPoint1_1_Content( TEST_GEO_LOCATION );
        selectSitePressNew( validContent.getContentTypeName() ).typeData( validContent );
        saveScreenshot( "geo-location-wizard-valid" );

        then: "red icon not present in the wizard, because content is valid"
        GeoPointFormViewPanel geoPointFormViewPanel = new GeoPointFormViewPanel( getSession() );
        geoPointFormViewPanel.isGeoLocationValid();
    }
}
