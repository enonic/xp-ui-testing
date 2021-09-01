package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.HomePage
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.CityCreationPage
import com.enonic.autotests.pages.form.CityFormView
import com.enonic.autotests.pages.form.liveedit.PartComponentView
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

import static com.enonic.autotests.utils.SleepHelper.sleep

@Stepwise
@Ignore
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


    def "GIVEN new site is added WHEN template with the 'country' controller has been added THEN new template should be listed"()
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
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInGrid( "_templates" ).clickToolbarNew().selectContentType(
            PAGE_TEMPLATE.getContentTypeName() ).showPageEditor().typeData( PAGE_TEMPLATE );
        wizard.closeBrowserTab().switchToBrowsePanelTab();
        sleep( 500 );

        then: "new page-template should be listed"
        filterPanel.typeSearchText( PAGE_TEMPLATE.getName() );
        contentBrowsePanel.exists( PAGE_TEMPLATE.getName() );
    }

    def "GIVEN existing page-template is opened WHEN 'country' part has been inserted THEN correct page-sources should be present in the HTML"()
    {
        given: "existing page-template is opened"
        ContentWizardPanel wizard = findAndSelectContent( PAGE_TEMPLATE.getName() ).clickToolbarEdit();

        when: "'country region' controller selected and 'country' part inserted"
        PageComponentsViewDialog componentView = wizard.showComponentView();
        insertPart( componentView, "country", wizard, "City Creation" );

        and: "'Preview' button has been pressed"
        wizard.switchToDefaultWindow().clickToolbarPreview();
        saveScreenshot( "country_part_added2" );

        then: "correct page-sources are present in the HTML"
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_TITLE );
        source.contains( "City Creation/Update" );
    }

    def "GIVEN new country-content is added WHEN city-creation page has been opened AND new city created THEN new child content should be listed beneath the parent"()
    {
        given: "new country-content has been added"
        NOR_CONTENT = buildCountry_Content( "Norway", NOR_DESCRIPTION, "7000000", SITE.getName() );
        ContentWizardPanel wizard = selectSitePressNew( NOR_CONTENT.getContentTypeName(), SITE.getName() );
        wizard.typeData( NOR_CONTENT ).save();
        wizard.closeBrowserTab().switchToBrowsePanelTab();

        when: "the submit button pressed and new city-content has been added"
        contentBrowsePanel.doClearSelection();
        openResourceInDraft( SITE.getName() + "/" + NOR_CONTENT.getName() );
        saveScreenshot( "oslo-creation-page" );
        CityCreationPage cityCreationPage = new CityCreationPage( getSession() );
        cityCreationPage.typeCityLocation( OSLO_LOCATION ).typeCityName( OSLO_CITY_NAME ).typeCityPopulation( OSLO_POPULATION );
        saveScreenshot( "oslo-creation-page" );
        cityCreationPage.clickSubmit();
        sleep(2000);
        openHomePage();
        sleep(700);
        HomePage homePage = new HomePage( getSession() );
        homePage.openContentStudioApplication();

        then: "content with name 'Norway'  should be listed beneath the site"
        filterPanel.typeSearchText( "Norway" );
        contentBrowsePanel.expandContent( NOR_CONTENT.getPath() );
        saveScreenshot( "norway-expanded" );
        and: "Oslo city should be present beneath the Norway"
        contentBrowsePanel.exists( OSLO_CITY_NAME );
    }

    def "WHEN just created through the portal Oslo-content is opened THEN expected location and population should be displayed"()
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
        sleep(1000);

        when: "navigate to Content Studio and Oslo-content is opened"
        openHomePage();
        sleep(700);
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
