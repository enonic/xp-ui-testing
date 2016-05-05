package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.GeoPointFormViewPanel
import com.enonic.autotests.utils.TestUtils
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
        selectSiteOpenWizard( geopointContent.getContentTypeName() ).waitUntilWizardOpened().typeData( geopointContent ).save().close(
            geopointContent.getDisplayName() ); ;

        when: "site expanded and just created content selected and 'Edit' button clicked"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( geopointContent );
        GeoPointFormViewPanel geoPointFormViewPanel = new GeoPointFormViewPanel( getSession() );

        then: "actual value in the form view and expected should be equals"
        geoPointFormViewPanel.getGeoPointValue().equals( TEST_GEO_LOCATION );
    }

    def "GIVEN wizard for adding a content with type 'Geo Location' opened WHEN value of 'Geo Location' is not within range and content saved and wizard closed THEN incorrect value not saved "()
    {
        given: "add a content with type 'Geo point'"
        Content notValidContent = buildGeoPoint0_0_Content( WRONG_GEO_LOCATION );
        selectSiteOpenWizard( notValidContent.getContentTypeName() ).waitUntilWizardOpened().typeData( notValidContent ).save().close(
            notValidContent.getDisplayName() )

        when: "'Save' button on toolbar pressed"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( notValidContent );
        GeoPointFormViewPanel geoPointFormViewPanel = new GeoPointFormViewPanel( getSession() );

        then: "correct warning-message appears"
        geoPointFormViewPanel.getGeoPointValue().isEmpty();
    }

    def "GIVEN wizard for adding a content with type 'Geo Location' opened WHEN value of 'Geo Location' is not within range THEN red icon present in the wizard and content is not valid "()
    {
        given: "add a content with type 'Geo point'"
        Content notValidContent = buildGeoPoint1_1_Content( WRONG_GEO_LOCATION );
        ContentWizardPanel wizard = selectSiteOpenWizard( notValidContent.getContentTypeName() ).waitUntilWizardOpened().typeData(
            notValidContent )

        when: "'Save' button on toolbar pressed"
        wizard.save();
        GeoPointFormViewPanel geoPointFormViewPanel = new GeoPointFormViewPanel( getSession() );

        then: "correct warning-message appears"
        wizard.isContentInvalid( notValidContent.getDisplayName() );
        and: "input has a red border"
        !geoPointFormViewPanel.isGeoLocationValid();
    }

    def "GIVEN wizard for adding a content with type 'Geo Location' opened WHEN value of 'Geo Location' is not within range THEN red icon present in the browse panel and content is not valid "()
    {
        given: "add a content with type 'Geo point'"
        Content notValidContent = buildGeoPoint1_1_Content( WRONG_GEO_LOCATION );
        ContentWizardPanel wizard = selectSiteOpenWizard( notValidContent.getContentTypeName() ).waitUntilWizardOpened().typeData(
            notValidContent )

        when: "'Save' button on toolbar pressed"
        SaveBeforeCloseDialog dialog = wizard.save().close( notValidContent.getDisplayName() );

        then: "the 'save before close' dialog closed"
        dialog == null;
        and: "correct warning-message appears"
        filterPanel.typeSearchText( notValidContent.getName() );
        contentBrowsePanel.isContentInvalid( notValidContent.getName() );
    }

    def "GIVEN all valid data typed WHEN saved and HomeButton clicked THEN the content displayed as valid "()
    {
        given: "add a content with type 'Geo point'"
        Content validContent = buildGeoPoint1_1_Content( TEST_GEO_LOCATION );
        ContentWizardPanel wizard = selectSiteOpenWizard( validContent.getContentTypeName() ).waitUntilWizardOpened().typeData(
            validContent );

        when: "content saved and HomeButton clicked"
        wizard.save();
        contentBrowsePanel.pressAppHomeButton();
        filterPanel.typeSearchText( validContent.getName() );
        sleep( 1000 );
        TestUtils.saveScreenshot( getSession(), "geo-location-grid-valid" );

        then: "the content displayed as valid"
        !contentBrowsePanel.isContentInvalid( validContent.getName() );
    }

    def "GIVEN wizard for adding a content with type 'Geo Location' opened WHEN all data typed THEN red icon not present in the wizard "()
    {
        when: "add a content with type 'Geo point'"
        Content validContent = buildGeoPoint1_1_Content( TEST_GEO_LOCATION );
        ContentWizardPanel wizard = selectSiteOpenWizard( validContent.getContentTypeName() ).waitUntilWizardOpened().typeData(
            validContent );
        TestUtils.saveScreenshot( getSession(), "geo-location-wizard-valid" )

        then: "red icon not present in the wizard, because content is valid"
        !wizard.isContentInvalid( validContent.getDisplayName() );
    }
}
