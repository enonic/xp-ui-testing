package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.HomePage
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.CityCreationPage
import com.enonic.autotests.pages.form.CityFormView
import com.enonic.autotests.pages.form.liveedit.PartComponentView
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

    @Shared
    Content SITE;


    def "GIVEN existing Site based on 'My First App' WHEN template with the 'country' region as a controller was added THEN new template should be listed"()
    {
        given: "existing Site based on 'My First App'"
        SITE = buildMyFirstAppSite( "mysite" );
        addSite( SITE );
        filterPanel.typeSearchText( SITE.getName() );

        and: "site expanded"
        contentBrowsePanel.expandContent( ContentPath.from( SITE.getName() ) );

        and: "new template with the controller should be added"
        PAGE_TEMPLATE = buildPageTemplate( COUNTRY_REGION_PAGE_CONTROLLER, TEMPLATE_SUPPORTS_COUNTRY, TEMPLATE_DISPLAY_NAME,
                                           SITE.getName() );

        when: "'Templates' folder selected and new page-template is added"
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable( "_templates" ).clickToolbarNew().selectContentType(
            PAGE_TEMPLATE.getContentTypeName() ).showPageEditor().typeData( PAGE_TEMPLATE );

        and: "the template is saved"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        sleep( 500 );

        then: "new page-template should be listed"
        filterPanel.typeSearchText( PAGE_TEMPLATE.getName() );
        contentBrowsePanel.exists( PAGE_TEMPLATE.getName() );
    }

    def "GIVEN existing page-template is opened WHEN 'country region' controller selected and 'country' part inserted THEN correct page-sources should be present in the HTML"()
    {
        given: "existing page-template is opened"
        ContentWizardPanel wizard = findAndSelectContent( PAGE_TEMPLATE.getName() ).clickToolbarEdit();

        when: " 'country region' controller selected and 'country' part inserted"
        PartComponentView partComponentView = wizard.showPageEditor().showContextWindow().clickOnInsertLink().insertPartByDragAndDrop(
            "RegionView", LIVE_EDIT_FRAME_SITE_HEADER );
        partComponentView.selectItem( "City Creation" );

        and: "'Preview' button has been pressed"
        wizard.save().clickToolbarPreview();
        saveScreenshot( "country_part_added2" );

        then: "correct page-sources are present in the HTML"
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_TITLE );
        source.contains( "City Creation/Update" );
    }

    def "GIVEN new country-content is added  WHEN city-creation page opened AND 'SUBMIT' button pressed  AND city-content added as child into the country THEN new child content should be listed beneath the parent"()
    {
        given: "new country-content added"
        NOR_CONTENT = buildCountry_Content( "Norway", NOR_DESCRIPTION, "7000000", SITE.getName() );

        ContentWizardPanel wizard = selectSitePressNew( NOR_CONTENT.getContentTypeName(), SITE.getName() );
        wizard.typeData( NOR_CONTENT ).save().waitNotificationMessage();
        wizard.closeBrowserTab().switchToBrowsePanelTab();

        when: "the submit button pressed and new city-content added as child into the country"
        contentBrowsePanel.doClearSelection();
        openResourceInDraft( SITE.getName() + "/" + NOR_CONTENT.getName() );
        saveScreenshot( "oslo-creation-page" );
        CityCreationPage cityCreationPage = new CityCreationPage( getSession() );
        cityCreationPage.typeCityLocation( OSLO_LOCATION ).typeCityName( OSLO_CITY_NAME ).typeCityPopulation( OSLO_POPULATION );
        saveScreenshot( "oslo-creation-page" );
        cityCreationPage.clickSubmit();
        openHomePage();
        HomePage homePage = new HomePage( getSession() );
        homePage.openContentStudioApplication();

        then: "content with name 'Norway'  should be listed beneath the site"
        filterPanel.typeSearchText( "Norway" );
        contentBrowsePanel.expandContent( NOR_CONTENT.getPath() );
        saveScreenshot( "norway-expanded" );
        and: "Oslo sity should be present beneath the Norway"
        contentBrowsePanel.exists( OSLO_CITY_NAME );
    }

    def "WHEN just created through the portal Oslo-content is opened  THEN correct location and population should be displayed"()
    {
        when: "Oslo-content is opened"
        findAndSelectContent( "oslo" ).clickToolbarEdit()
        CityFormView cityFormView = new CityFormView( getSession() );
        saveScreenshot( "oslo-city-content-opened" );

        then: "correct location should be displayed"
        cityFormView.getLocationValue() == OSLO_LOCATION;
        and: "and correct population should be displayed"
        cityFormView.getPopulationValue() == OSLO_POPULATION;
    }

    def "GIVEN existing Oslo-content WHEN 'creating/updating' page is opened AND new population for Oslo is typed THEN Oslo-content with the new population should be present in the grid"()
    {
        given: "Creation-page is opened AND Oslo-content is updated, new population is typed"
        openResourceInDraft( SITE.getName() + "/" + NOR_CONTENT.getName() );
        CityCreationPage cityCreationPage = new CityCreationPage( getSession() );
        cityCreationPage.typeCityName( "oslo" ).typeCityPopulation( NEW_OSLO_POPULATION ).typeCityLocation( OSLO_LOCATION ).clickSubmit();

        when: "navigate to Content Studio and Oslo-content is opened"
        openHomePage();
        HomePage homePage = new HomePage( getSession() );
        homePage.openContentStudioApplication();
        findAndSelectContent( "oslo" ).clickToolbarEdit();
        sleep( 500 );

        then: "new population for Oslo should be displayed"
        CityFormView cityFormView = new CityFormView( getSession() );
        saveScreenshot( "oslo-city-content-new-population" );
        cityFormView.getPopulationValue() == NEW_OSLO_POPULATION;
    }
}
