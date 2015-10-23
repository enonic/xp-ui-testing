package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.HomePage
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.CityCreationPage
import com.enonic.autotests.pages.form.CityFormView
import com.enonic.autotests.pages.form.liveedit.PartComponentView
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import spock.lang.Shared
import spock.lang.Stepwise

import static com.enonic.autotests.utils.SleepHelper.sleep

@Stepwise
class PortalContentCreating_Spec
    extends BaseSiteSpec
{

    @Shared
    String TEMPLATE_DISPLAY_NAME = "country with city creation";

    @Shared
    Content PAGE_TEMPLATE;

    @Shared
    Content NOR_CONTENT;

    @Shared
    String OSLO_POPULATION = "618,683";

    @Shared
    String NEW_OSLO_POPULATION = "1000000";

    @Shared
    String OSLO_LOCATION = "59.95,10.75";

    @Shared
    String OSLO_CITY_NAME = "oslo";

    def "GIVEN existing Site based on 'My First App' WHEN template with the 'country' region as a controller added and wizard closed THEN new template should be listed"()
    {
        given: "existing Site based on 'My First App'"
        filterPanel.typeSearchText( FIRST_SITE_NAME );
        contentBrowsePanel.expandContent( ContentPath.from( FIRST_SITE_NAME ) );
        PAGE_TEMPLATE = buildPageTemplate( COUNTRY_REGION_PAGE_CONTROLLER, SUPPORTS_TYPE, TEMPLATE_DISPLAY_NAME,
                                           FIRST_SITE_NAME );

        when: "'Templates' folder selected and new page-template added"
        contentBrowsePanel.selectContentInTable( "_templates" ).clickToolbarNew().selectContentType(
            PAGE_TEMPLATE.getContentTypeName() ).showPageEditor().typeData( PAGE_TEMPLATE ).save().close( PAGE_TEMPLATE.getDisplayName() );
        sleep( 500 );

        then: "new page-template listed"
        filterPanel.typeSearchText( PAGE_TEMPLATE.getName() );
        contentBrowsePanel.exists( PAGE_TEMPLATE.getName() );
    }


    def "GIVEN existing page-template WHEN the template opened for edit and the 'country region' controller selected and 'country' part inserted THEN correct page-sources are present in the HTML"()
    {
        given: "existing page-template"
        filterPanel.typeSearchText( PAGE_TEMPLATE.getName() );
        ContentWizardPanel contentWizard = contentBrowsePanel.selectContentInTable( PAGE_TEMPLATE.getName() ).clickToolbarEdit();

        when: "the template opened for edit and the 'country region' controller selected and 'country' part inserted"
        PartComponentView partComponentView = contentWizard.showPageEditor().showContextWindow().clickOnInsertLink().insertPartByDragAndDrop(
            "RegionPlaceholder", LIVE_EDIT_FRAME_SITE_HEADER );
        partComponentView.selectItem( "City Creation" );
        NavigatorHelper.switchToContentManagerFrame( getSession() );
        contentWizard.save().clickToolbarPreview();
        TestUtils.saveScreenshot( getSession(), "country_part_added2" );

        then: "correct page-sources are present in the HTML"
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_TITLE );
        source.contains( "City Creation/Update" );
    }

    def "GIVEN new country-content added  WHEN city-creation page opened AND 'SUBMIT' button pressed  AND city-content added as child into the country THEN new child content exist beneath a parent"()
    {
        given: "new country-content added"
        NOR_CONTENT = buildCountry_Content( "Norway", NOR_DESCRIPTION, "7000000", FIRST_SITE_NAME );

        ContentWizardPanel wizard = selectSiteOpenWizard( NOR_CONTENT.getContentTypeName(), FIRST_SITE_NAME );
        wizard.typeData( NOR_CONTENT ).save().waitNotificationMessage(); wizard.close( NOR_CONTENT.getDisplayName() );

        when: "the submit button pressed and new city-content added as child into the country"
        contentBrowsePanel.clickOnClearSelection();
        openResourceInDraft( FIRST_SITE_NAME + "/" + NOR_CONTENT.getName() );
        TestUtils.saveScreenshot( getSession(), "oslo-creation-page" );
        CityCreationPage cityCreationPage = new CityCreationPage( getSession() );
        cityCreationPage.typeCityLocation( OSLO_LOCATION ).typeCityName( OSLO_CITY_NAME ).typeCityPopulation( OSLO_POPULATION );
        TestUtils.saveScreenshot( getSession(), "oslo-creation-page" );
        cityCreationPage.clickSubmit();
        openHomePage();
        HomePage homePage = new HomePage( getSession() );
        homePage.openContentManagerApplication();

        then: "correct child content exist beneath a parent"
        filterPanel.typeSearchText( "Norway" );
        contentBrowsePanel.expandContent( NOR_CONTENT.getPath() );
        TestUtils.saveScreenshot( getSession(), "norway-expanded" );
        contentBrowsePanel.exists( OSLO_CITY_NAME );
    }

    def "GIVEN just created through the portal content WHEN city-content opened for edit THEN correct location and population displayed"()
    {
        when: "city-content opened for edit"
        findAndSelectContent( "oslo" ).clickToolbarEdit()
        CityFormView cityFormView = new CityFormView( getSession() );
        TestUtils.saveScreenshot( getSession(), "oslo-city-content-opened" );

        then: "correct location displayed"
        cityFormView.getLocationValue() == OSLO_LOCATION;
        and: "and correct population displayed"
        cityFormView.getPopulationValue() == OSLO_POPULATION;
    }

    def "GIVEN existing city content WHEN page for 'creating/updating' content and new population typed THEN the city-content with the new population present in the grid "()
    {
        given: "city-content opened for edit and new population typed"
        openResourceInDraft( FIRST_SITE_NAME + "/" + NOR_CONTENT.getName() );
        CityCreationPage cityCreationPage = new CityCreationPage( getSession() );
        cityCreationPage.typeCityName( "oslo" ).typeCityPopulation( NEW_OSLO_POPULATION ).typeCityLocation( OSLO_LOCATION ).clickSubmit();

        when:
        openHomePage();
        HomePage homePage = new HomePage( getSession() );
        homePage.openContentManagerApplication();

        then: "new population displayed"
        findAndSelectContent( "oslo" ).clickToolbarEdit();
        sleep( 500 );
        CityFormView cityFormView = new CityFormView( getSession() );
        TestUtils.saveScreenshot( getSession(), "oslo-city-content-new-population" );
        cityFormView.getLocationValue() == OSLO_LOCATION;
        and: "and correct population displayed"
        cityFormView.getPopulationValue() == NEW_OSLO_POPULATION;
    }
}
