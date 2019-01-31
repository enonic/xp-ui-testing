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

    def "GIVEN Geo point' content has been added WHEN the content is opened THEN expected value should be present"()
    {
        given: "'Geo point' content has been added"
        Content geopointContent = buildGeoPoint0_0_Content( TEST_GEO_LOCATION );
        selectSitePressNew( geopointContent.getContentTypeName() ).waitUntilWizardOpened().typeData(
            geopointContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "just created content is selected and 'Edit' button clicked"
        findAndSelectContent( geopointContent.getName() ).clickToolbarEditAndSwitchToWizardTab();
        GeoPointFormViewPanel geoPointFormViewPanel = new GeoPointFormViewPanel( getSession() );

        then: "actual value in the form view and expected should be equals"
        geoPointFormViewPanel.getGeoPointValue().equals( TEST_GEO_LOCATION );
    }

    def "GIVEN wizard for adding a content with type 'Geo Location' opened WHEN value of 'Geo Location' is not within range and content saved and wizard closed THEN incorrect value is not saved"()
    {
        given: "value of 'Geo Location' is not within range"
        Content notValidContent = buildGeoPoint0_0_Content( WRONG_GEO_LOCATION );

        and: "data has been typed and the content saved"
        selectSitePressNew( notValidContent.getContentTypeName() ).typeData(
            notValidContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "the content has been reopened"
        findAndSelectContent( notValidContent.getName() ).clickToolbarEditAndSwitchToWizardTab();
        GeoPointFormViewPanel geoPointFormViewPanel = new GeoPointFormViewPanel( getSession() );

        then: "incorrect value should not be saved and input is empty"
        geoPointFormViewPanel.getGeoPointValue().isEmpty();
    }

    def "GIVEN wizard for 'Geo Location' is opened WHEN value of 'Geo Location' is not within range THEN red icon should be present on the wizard"()
    {
        given: "'Geo point' content with wrong value"
        Content notValidContent = buildGeoPoint1_1_Content( WRONG_GEO_LOCATION );
        ContentWizardPanel wizard = selectSitePressNew( notValidContent.getContentTypeName() );

        when: "data was typed and 'Save' button on the toolbar pressed"
        wizard.typeData( notValidContent );
        GeoPointFormViewPanel geoPointFormViewPanel = new GeoPointFormViewPanel( getSession() );

        then: "input should be displayed with red border"
        !geoPointFormViewPanel.isGeoLocationValid();

        and: "red icon should be present on the wizard"
        wizard.isContentInvalid();
    }

    def "GIVEN wizard for adding a content with type 'Geo Location' is opened WHEN value of 'Geo Location' is not within range THEN red icon present in the browse panel and content is not valid "()
    {
        given: "wizard for adding a content with type 'Geo Location' is opened"
        Content notValidContent = buildGeoPoint1_1_Content( WRONG_GEO_LOCATION );
        and: "wrong value has been typed"
        ContentWizardPanel wizard = selectSitePressNew( notValidContent.getContentTypeName() ).typeData( notValidContent );
        saveScreenshot( "geo-location-incorrect-value" );

        when: "'Save' button on the toolbar was pressed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        then: "the content should be with red icon(invalid) in the grid, because input is required"
        filterPanel.typeSearchText( notValidContent.getName() );
        contentBrowsePanel.isContentInvalid( notValidContent.getName() );
    }

    def "GIVEN valid data has been typed WHEN switched to the browse panel THEN the content displayed as valid "()
    {
        given: "wizard opened and the valid data has been typed"
        Content validContent = buildGeoPoint1_1_Content( TEST_GEO_LOCATION );
        ContentWizardPanel wizard = selectSitePressNew( validContent.getContentTypeName() ).typeData( validContent );

        when: "the content is saved"
        wizard.save();
        and: "switched to the browse panel"
        wizard.switchToBrowsePanelTab();
        filterPanel.typeSearchText( validContent.getName() );
        saveScreenshot( "geo-location-valid-in-grid" );

        then: "the content should be displayed as valid"
        !contentBrowsePanel.isContentInvalid( validContent.getName() );
    }

    def "GIVEN wizard for new geoPoint is opened WHEN and the valid data has been typed THEN red icon should not be present on the wizard "()
    {
        when: "wizard opened and the valid data has been typed"
        Content validContent = buildGeoPoint1_1_Content( TEST_GEO_LOCATION );
        ContentWizardPanel wizard = selectSitePressNew( validContent.getContentTypeName() ).typeData( validContent );
        saveScreenshot( "geo-location-wizard-valid" );

        then: "red icon should not be present on the wizard, because content is valid"
        GeoPointFormViewPanel geoPointFormViewPanel = new GeoPointFormViewPanel( getSession() );
        geoPointFormViewPanel.isGeoLocationValid();

        and:
        !wizard.isContentInvalid();
    }
}
