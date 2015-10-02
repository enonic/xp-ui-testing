package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.GeoPointFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore
import spock.lang.Shared

class GeoPoint_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    String TEST_GEO_LOCATION = "10,10";

    @Shared
    String WRONG_GEO_LOCATION = "1,181";

    @Ignore
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

    // ignored due the application bug: XP-1526
    @Ignore
    def "GIVEN wizard for adding a content with type 'Geo Location' opened WHEN value of 'Geo Location' is not within range THEN correct warning-message appears "()
    {
        given: "add a content with type 'Geo point'"
        Content notValidContent = buildGeoPoint0_0_Content( WRONG_GEO_LOCATION );
        ContentWizardPanel wizard = selectSiteOpenWizard( notValidContent.getContentTypeName() ).waitUntilWizardOpened().typeData(
            notValidContent )

        when: "'Save' button on toolbar pressed"
        String warningMessage = wizard.save().waitNotificationWarning( Application.EXPLICIT_NORMAL );

        then: "correct warning-message appears"
        warningMessage == Application.LOCATION_NOT_WITHIN_RANGE_WARNING;
    }
    // ignored due the application bug: XP-1526
    @Ignore
    def "GIVEN wizard for adding a content with type 'Geo Location' opened WHEN value of 'Geo Location' is not within range THEN red icon present in the wizard and content is not valid "()
    {
        given: "add a content with type 'Geo point'"
        Content notValidContent = buildGeoPoint0_0_Content( WRONG_GEO_LOCATION );
        ContentWizardPanel wizard = selectSiteOpenWizard( notValidContent.getContentTypeName() ).waitUntilWizardOpened().typeData(
            notValidContent )

        when: "'Save' button on toolbar pressed"
        wizard.save();

        then: "correct warning-message appears"
        wizard.isContentInvalid( notValidContent.getDisplayName() );
    }
    // ignored due the application bug: XP-1526
    @Ignore
    def "GIVEN wizard for adding a content with type 'Geo Location' opened WHEN value of 'Geo Location' is not within range THEN red icon present in the browse panel and content is not valid "()
    {
        given: "add a content with type 'Geo point'"
        Content notValidContent = buildGeoPoint0_0_Content( WRONG_GEO_LOCATION );
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
}
