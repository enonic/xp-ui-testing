package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.CityFormView
import com.enonic.autotests.pages.form.liveedit.PartComponentView
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.PageComponent
import com.enonic.xp.content.ContentPath
import spock.lang.Shared
import spock.lang.Stepwise

import static com.enonic.autotests.utils.SleepHelper.sleep

@Stepwise
class CountrySiteWithTemplateSpec
    extends BaseSiteSpec
{
    @Shared
    String NEW_SF_POPULATION = "1000000";

    @Shared
    Content SAN_FR_CONTENT;


    @Shared
    Content USA_CONTENT;

    @Shared
    Content PAGE_TEMPLATE;

    @Shared
    String COUNTRY_TEMPLATE_DISPLAY_NAME = "country template";

    @Shared
    String SF_LOCATION = "37.7833,-122.4167";

    @Shared
    String SF_POPULATION = "837,442";

    @Shared
    String PAGE_CONTROLLER_NAME = "Country Region";

    @Shared
    String CITY_HEADER = "<h3>San Francisco</h3>";

    @Shared
    Content SITE;

    def "GIVEN existing Site based on 'My First App' WHEN template with the 'country' region as a controller added and wizard closed THEN new template should be listed"()
    {
        given: "existing Site based on 'My First App'"
        SITE = buildMyFirstAppSite( "mysite" );
        addSite( SITE );
        filterPanel.typeSearchText( SITE.getName() );
        contentBrowsePanel.expandContent( ContentPath.from( SITE.getName() ) );
        PAGE_TEMPLATE = buildPageTemplate( COUNTRY_REGION_PAGE_CONTROLLER, TEMPLATE_SUPPORTS_COUNTRY, COUNTRY_TEMPLATE_DISPLAY_NAME,
                                           SITE.getName() );

        when: "'Templates' folder selected and new page-template added"
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable( "_templates" ).clickToolbarNew().selectContentType(
            PAGE_TEMPLATE.getContentTypeName() ).typeData( PAGE_TEMPLATE );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
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
        PartComponentView partComponentView = contentWizard.showContextWindow().clickOnInsertLink().insertPartByDragAndDrop( "RegionView",
                                                                                                                             LIVE_EDIT_FRAME_SITE_HEADER );
        partComponentView.selectItem( COUNTRY_PART_DEFAULT_NAME );
        contentWizard.save().clickToolbarPreview();
        saveScreenshot( "country_part_added" );

        then: "page source of new opened tab in a browser is not empty"
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_TITLE );
        source != null;

        and: "correct page-sources are present in the HTML"
        source.contains( COUNTRY_REGION_TITLE );
        and: "correct header displayed"
        source.contains( COUNTRY_TEMPLATE_DISPLAY_NAME );
    }

    def "GIVEN existing page-template with a 'country' part WHEN the template opened for edit and the 'city' part inserted THEN correct page-sources are present in the HTML"()
    {
        given: "existing page-template"
        filterPanel.typeSearchText( PAGE_TEMPLATE.getName() );
        ContentWizardPanel contentWizard = contentBrowsePanel.selectContentInTable( PAGE_TEMPLATE.getName() ).clickToolbarEdit();

        when: "the template opened for edit and the 'country region' controller selected and 'country' part inserted"
        PartComponentView partComponentView = contentWizard.showContextWindow().clickOnInsertLink().insertPartByDragAndDrop(
            "PartComponentView", LIVE_EDIT_FRAME_SITE_HEADER );
        partComponentView.selectItem( "City list" );
        contentWizard.save().clickToolbarPreview();
        saveScreenshot( "city_part_added" );

        then: "page source of new opened tab in a browser is not empty"
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_TITLE );
        source != null;

        and: "correct page-sources are present in the HTML"
        source.contains( COUNTRY_REGION_TITLE );

        and: "correct header displayed"
        source.contains( COUNTRY_TEMPLATE_DISPLAY_NAME );
    }

    def "GIVEN existing page template WHEN the template opened for edit and ''"()
    {
        given: "existing page-template"
        filterPanel.typeSearchText( PAGE_TEMPLATE.getName() );
        ContentWizardPanel contentWizard = contentBrowsePanel.selectContentInTable( PAGE_TEMPLATE.getName() ).clickToolbarEdit();

        when: "the 'Inspect' link clicked"
        String name = contentWizard.showContextWindow().clickOnInspectLink().getInspectionPanel().getSelectedPageController();
        saveScreenshot( "city_part_added" );

        then: "correct region's name is shown"
        name == PAGE_CONTROLLER_NAME;
    }

    def "GIVEN new USA-content added AND child city-content added for the country WHEN USA-content selected AND 'Preview' button pressed THEN correct text is present in the page-source "()
    {
        given: "new USA-content added"
        USA_CONTENT = buildCountry_Content( "USA", USA_DESCRIPTION, USA_POPULATION, SITE.getName() );

        ContentWizardPanel wizard = selectSitePressNew( USA_CONTENT.getContentTypeName(), SITE.getName() );
        wizard.typeData( USA_CONTENT ).save().waitNotificationMessage();
        wizard.closeBrowserTab().switchToBrowsePanelTab();

        and: "the content selected and 'New' button on the toolbar pressed"
        contentBrowsePanel.clickOnClearSelection();
        SAN_FR_CONTENT = buildCity_Content( "San Francisco", SF_LOCATION, SF_POPULATION, USA_CONTENT.getName() );

        and: "new City-content added beneath the USA-content"
        findAndSelectContent( USA_CONTENT.getName() ).clickToolbarNew().selectContentType( SAN_FR_CONTENT.getContentTypeName() ).typeData(
            SAN_FR_CONTENT ).save().closeBrowserTab().switchToBrowsePanelTab();
        findAndSelectContent( USA_CONTENT.getName() );
        sleep( 3000 );
        saveScreenshot( "san_francisco_added" )
        contentBrowsePanel.clickOnClearSelection();

        when: "country-content selected in the grid and the 'Preview' button pressed"
        findAndSelectContent( USA_CONTENT.getName() );
        sleep( 2000 );
        saveScreenshot( "USA_City" )
        contentBrowsePanel.clickToolbarPreview();

        then: "correct text present in the 'page source'"
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_TITLE );
        source.contains( CITY_HEADER );
        and: "correct description is displayed"
        source.contains( "Population: " + SF_POPULATION );

    }

    def "WHEN site is not published yet WHEN site opened in 'master', through the portal THEN '404' present in the sources"()
    {
        given: "site not published and opened in the 'master'"
        openResourceInMaster( SITE.getName() + "/" + USA_CONTENT.getName() );
        sleep( 2000 );

        expect:
        saveScreenshot( "portal-country-preview-master-offline" );
        String source = getDriver().getPageSource();
        source.contains( "404" );
    }

    def "WHEN site is not published yet AND site opened in 'draft', through the portal THEN correct data is present in page sources"()
    {
        when: "site not published and opened in the 'master'"
        openResourceInDraft( SITE.getName() + "/" + USA_CONTENT.getName() );
        sleep( 2000 );
        saveScreenshot( "portal-country-preview-draft-offline" );

        then: "correct data present in page sources"
        String source = getDriver().getPageSource();
        source.contains( "Population: " + SF_POPULATION );

        and: "correct description shown"
        source.contains( USA_DESCRIPTION );
    }

    def "WHEN site has been published AND site opened through the portal THEN correct data present in page sources"()
    {
        given: "site have been 'published'"
        filterPanel.typeSearchText( SITE.getName(), );
        ContentPublishDialog dialog = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName(), ).clickToolbarPublish();
        dialog.includeChildren( true ).clickOnPublishNowButton();
        sleep( 2000 );
        saveScreenshot( "country_site_published" );

        when: "site opened in master"
        openResourceInMaster( SITE.getName() + "/" + USA_CONTENT.getName() );

        then: "correct data present in page sources"
        String source = getDriver().getPageSource();
        source.contains( "Population: " + SF_POPULATION );

        and: "correct description shown"
        source.contains( USA_DESCRIPTION );
    }

    def "GIVEN city content changed and content is not 'Published' WHEN site opened in 'master', through the portal THEN old data for city-content present"()
    {
        given: "city content changed and content is not 'Published'"
        ContentWizardPanel wizard = findAndSelectContent( SAN_FR_CONTENT.getName() ).clickToolbarEdit();
        CityFormView cityFormView = new CityFormView( getSession() );
        cityFormView.typePopulation( NEW_SF_POPULATION );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        when: "site opened in master"
        openResourceInMaster( SITE.getName() + "/" + USA_CONTENT.getName() );

        then: "population is not changed"
        String source = getDriver().getPageSource();
        source.contains( "Population: " + SF_POPULATION );
    }

    def "GIVEN city content changed and 'Published' WHEN site opened in 'master', through the portal THEN old data for city-content present"()
    {
        given: "city content changed and 'Published'"
        ContentWizardPanel wizard = findAndSelectContent( SAN_FR_CONTENT.getName() ).clickToolbarEdit();
        wizard.clickOnWizardPublishButton().clickOnPublishNowButton();
        contentBrowsePanel.waitNotificationMessage( Application.EXPLICIT_NORMAL );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        when: "site opened in master"
        openResourceInMaster( SITE.getName() + "/" + USA_CONTENT.getName() );

        then: "population is not changed"
        String source = getDriver().getPageSource();
        source.contains( "Population: " + NEW_SF_POPULATION );
    }

    def "GIVEN existing country content WHEN 'Page Component View' is opened THEN all components that were added should be shown"()
    {
        given: "existing country content is opened"
        ContentWizardPanel wizard = findAndSelectContent( USA_CONTENT.getName() ).clickToolbarEdit();

        when: "'Page Component View' is opened"
        wizard.showComponentView();
        PageComponentsViewDialog view = new PageComponentsViewDialog( getSession() );
        List<PageComponent> components = view.getPageComponents();
        saveScreenshot( "order-components" )

        then: "all components that were added should be shown"
        components.size() == 4;

        and:
        components.get( 0 ).getType().equals( "page" ) && components.get( 0 ).getName().equals( COUNTRY_TEMPLATE_DISPLAY_NAME );
        and:
        components.get( 1 ).getType().equals( "region" ) && components.get( 1 ).getName().equals( "country" );
        and:
        components.get( 2 ).getType().equals( "part" ) && components.get( 2 ).getName().equals( COUNTRY_PART_DEFAULT_NAME );
        and:
        components.get( 3 ).getType().equals( "part" ) && components.get( 3 ).getName().equals( "City list" );
    }
}
