package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.liveedit.PartComponentView
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

import static com.enonic.autotests.utils.SleepHelper.sleep

@Stepwise
class MyFirstSiteWithTemplateSpec
    extends BaseSiteSpec
{
    @Shared
    Content MY_FIRST_SITE

    @Shared
    String COUNTRY_REGION_PAGE_CONTROLLER = "Country Region";

    @Shared
    String PART_NAME = "country";

    @Shared
    Content USA_CONTENT;

    @Shared
    Content PAGE_TEMPLATE;

    @Shared
    String TEMPLATE_DISPLAY_NAME = "country template";

    @Shared
    String SUPPORTS_TYPE = "country";

    @Shared
    String SF_LOCATION = "37.7833,-122.4167";

    @Shared
        SF_POPULATION = "837,442";


    def "GIVEN existing Site based on 'My First App' WHEN template with the 'country' region as a controller added and wizard closed THEN new template should be listed"()
    {
        given: "existing Site based on 'My First App'"
        MY_FIRST_SITE = buildMySite( "site-template" );
        contentBrowsePanel.clickToolbarNew().selectContentType( MY_FIRST_SITE.getContentTypeName() ).typeData( MY_FIRST_SITE ).save().close(
            MY_FIRST_SITE.getDisplayName() );
        filterPanel.typeSearchText( MY_FIRST_SITE.getName() );
        contentBrowsePanel.expandContent( ContentPath.from( MY_FIRST_SITE.getName() ) );
        PAGE_TEMPLATE = buildPageTemplate( COUNTRY_REGION_PAGE_CONTROLLER, SUPPORTS_TYPE, TEMPLATE_DISPLAY_NAME,
                                           MY_FIRST_SITE.getName() );


        when: "'Templates' folder selected and new page-template added"
        contentBrowsePanel.selectContentInTable( "_templates" ).clickToolbarNew().selectContentType(
            PAGE_TEMPLATE.getContentTypeName() ).clickOnLiveToolbarButton().typeData( PAGE_TEMPLATE ).save().close(
            PAGE_TEMPLATE.getDisplayName() );
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
        PartComponentView partComponentView = contentWizard.showContextWindow().clickOnInsertLink().insertPartByDragAndDrop(
            "RegionPlaceholder", LIVE_EDIT_FRAME_SITE_HEADER );
        partComponentView.selectItem( PART_NAME );
        NavigatorHelper.switchToContentManagerFrame( getSession() );
        contentWizard.save().clickToolbarPreview();
        TestUtils.saveScreenshot( getSession(), "country_part_added" );

        then: "correct page-sources are present in the HTML"
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_TITLE );
        source.contains( COUNTRY_REGION_TITLE );
        and: "correct header displayed"
        source.contains( TEMPLATE_DISPLAY_NAME );
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
        NavigatorHelper.switchToContentManagerFrame( getSession() );
        contentWizard.save().clickToolbarPreview();
        TestUtils.saveScreenshot( getSession(), "city_part_added" );

        then: "correct page-sources are present in the HTML"
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_TITLE );
        source.contains( COUNTRY_REGION_TITLE );
        and: "correct header displayed"
        source.contains( TEMPLATE_DISPLAY_NAME );
    }

    def "SETUP new USA-content added and a child city content added into the country WHEN country content selected AND 'Preview' button pressed THEN correct text present in the page-source "()
    {
        setup: "new USA- content added"
        USA_CONTENT = buildCountry_Content( "USA", USA_DESCRIPTION, USA_POPULATION, MY_FIRST_SITE.getName() );

        ContentWizardPanel wizard = selectSiteOpenWizard( MY_FIRST_SITE.getName(), USA_CONTENT.getContentTypeName() );
        wizard.typeData( USA_CONTENT ).save().waitNotificationMessage(); wizard.close( USA_CONTENT.getDisplayName() );
        and: "and it content selected and the 'New' button on the toolbar pressed"
        contentBrowsePanel.clickOnClearSelection();
        Content sanFrancisco = buildCity_Content( "San Francisco", SF_LOCATION, SF_POPULATION, USA_CONTENT.getName() );
        and: "new City-content added beneath the USA-content"
        findAndSelectContent( USA_CONTENT.getName() ).clickToolbarNew().selectContentType( sanFrancisco.getContentTypeName() ).typeData(
            sanFrancisco ).save().close( sanFrancisco.getDisplayName() );
        findAndSelectContent( USA_CONTENT.getName() );
        sleep( 3000 );
        TestUtils.saveScreenshot( getSession(), "san francisco" )
        contentBrowsePanel.clickOnClearSelection();

        when: "country-content selected in the grid and the 'Preview' button pressed"
        findAndSelectContent( USA_CONTENT.getName() );
        sleep( 3000 );
        TestUtils.saveScreenshot( getSession(), "USA_City" )
        contentBrowsePanel.clickToolbarPreview();

        then: "correct text present in the 'page source'"
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_TITLE );
        source.contains( "<h3>San Francisco</h3>" );
        and: "correct description displayed"
        source.contains( "Population: " + SF_POPULATION );

    }

    @Ignore
    def "WHEN content opened by the URL THEN correct text present in the sources"()
    {
        // go browser.baseUrl + "admin/portal/preview/draft/" + MY_FIRST_SITE.getName()+"/" + USA_CONTENT.getName();
        given:
        getDriver().navigate().to(
            browser.baseUrl + "admin/portal/preview/draft/" + MY_FIRST_SITE.getName() + "/" + USA_CONTENT.getName() );
        expect:
        sleep( 2000 );
        TestUtils.saveScreenshot( getSession(), "country-preview" )
        String source = getDriver().getPageSource();
        source.contains( "Population: " + SF_POPULATION );
        and: ""
        source.contains( USA_DESCRIPTION );
    }
}
