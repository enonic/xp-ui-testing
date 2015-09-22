package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
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
class MyFirstSiteWithTemplateSpec
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
    String TEMPLATE_DISPLAY_NAME = "country template";

    @Shared
    String SF_LOCATION = "37.7833,-122.4167";

    @Shared
    String SF_POPULATION = "837,442";

    @Shared
    String PAGE_CONTROLLER_NAME = "Country Region";

    @Shared
    String CITY_HEADER = "<h3>San Francisco</h3>";


    def "GIVEN existing Site based on 'My First App' WHEN template with the 'country' region as a controller added and wizard closed THEN new template should be listed"()
    {
        given: "existing Site based on 'My First App'"
        filterPanel.typeSearchText( FIRST_SITE_NAME );
        contentBrowsePanel.expandContent( ContentPath.from( FIRST_SITE_NAME ) );
        PAGE_TEMPLATE = buildPageTemplate( COUNTRY_REGION_PAGE_CONTROLLER, SUPPORTS_TYPE, TEMPLATE_DISPLAY_NAME,
                                           FIRST_SITE_NAME );


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
        partComponentView.selectItem( COUNTRY_PART_NAME );
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

    def "GIVEN existing page template WHEN the template opened for edit and ''"()
    {
        given: "existing page-template"
        filterPanel.typeSearchText( PAGE_TEMPLATE.getName() );
        ContentWizardPanel contentWizard = contentBrowsePanel.selectContentInTable( PAGE_TEMPLATE.getName() ).clickToolbarEdit();

        when: "the 'Inspect' link clicked"
        String name = contentWizard.showContextWindow().clickOnInspectLink().getSelectedPageController();
        TestUtils.saveScreenshot( getSession(), "city_part_added" );

        then: "correct region's name shown"
        name == PAGE_CONTROLLER_NAME;
    }

    def "GIVEN new USA-content added and a child city content added into the country WHEN country content selected AND 'Preview' button pressed THEN correct text present in the page-source "()
    {
        given: "new USA- content added"
        USA_CONTENT = buildCountry_Content( "USA", USA_DESCRIPTION, USA_POPULATION, FIRST_SITE_NAME );

        ContentWizardPanel wizard = selectSiteOpenWizard( USA_CONTENT.getContentTypeName(), FIRST_SITE_NAME );
        wizard.typeData( USA_CONTENT ).save().waitNotificationMessage(); wizard.close( USA_CONTENT.getDisplayName() );
        and: "and it content selected and the 'New' button on the toolbar pressed"
        contentBrowsePanel.clickOnClearSelection();
        SAN_FR_CONTENT = buildCity_Content( "San Francisco", SF_LOCATION, SF_POPULATION, USA_CONTENT.getName() );
        and: "new City-content added beneath the USA-content"
        findAndSelectContent( USA_CONTENT.getName() ).clickToolbarNew().selectContentType( SAN_FR_CONTENT.getContentTypeName() ).typeData(
            SAN_FR_CONTENT ).save().close( SAN_FR_CONTENT.getDisplayName() );
        findAndSelectContent( USA_CONTENT.getName() );
        sleep( 3000 );
        TestUtils.saveScreenshot( getSession(), "san francisco" )
        contentBrowsePanel.clickOnClearSelection();

        when: "country-content selected in the grid and the 'Preview' button pressed"
        findAndSelectContent( USA_CONTENT.getName() );
        sleep( 2000 );
        TestUtils.saveScreenshot( getSession(), "USA_City" )
        contentBrowsePanel.clickToolbarPreview();

        then: "correct text present in the 'page source'"
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_TITLE );
        source.contains( CITY_HEADER );
        and: "correct description displayed"
        source.contains( "Population: " + SF_POPULATION );

    }

    def "WHEN site not published yet WHEN site opened in 'master', through the portal THEN '404' present in the sources"()
    {
        given: "site not published and opened in the 'master'"
        openResourceInMaster( FIRST_SITE_NAME + "/" + USA_CONTENT.getName() );
        sleep( 2000 );

        expect:
        TestUtils.saveScreenshot( getSession(), "portal-country-preview-master-offline" );
        String source = getDriver().getPageSource();
        source.contains( "404" );
    }

    def "WHEN site not published yet AND site opened in 'draft', through the portal THEN correct data present in page sources"()
    {
        when: "site not published and opened in the 'master'"
        openResourceInDraft( FIRST_SITE_NAME + "/" + USA_CONTENT.getName() );
        sleep( 2000 );
        TestUtils.saveScreenshot( getSession(), "portal-country-preview-draft-offline" );

        then:
        then: "correct data present in page sources"
        String source = getDriver().getPageSource();
        source.contains( "Population: " + SF_POPULATION );
        and: "correct description shown"
        source.contains( USA_DESCRIPTION );
    }

    def "GIVEN ite not published yet WHEN site published AND site opened through the portal THEN correct data present in page sources"()
    {
        given: "site with child is 'Published'"
        filterPanel.typeSearchText( FIRST_SITE_NAME, );
        ContentPublishDialog dialog = contentBrowsePanel.clickCheckboxAndSelectRow( FIRST_SITE_NAME, ).clickToolbarPublish();
        dialog.setIncludeChildCheckbox( true ).clickOnPublishNowButton();
        sleep( 3000 );

        when: "site opened in master"
        openResourceInMaster( FIRST_SITE_NAME + "/" + USA_CONTENT.getName() );

        then: "correct data present in page sources"
        String source = getDriver().getPageSource();
        source.contains( "Population: " + SF_POPULATION );
        and: "correct description shown"
        source.contains( USA_DESCRIPTION );
    }

    def "GIVEN city content changed  and content is not 'Published' WHEN site opened in 'master', through the portal THEN old data for city-content present"()
    {
        given:
        ContentWizardPanel wizard = findAndSelectContent( SAN_FR_CONTENT.getName() ).clickToolbarEdit();
        CityFormView cityFormView = new CityFormView( getSession() );
        cityFormView.typePopulation( NEW_SF_POPULATION );
        wizard.save().close( SAN_FR_CONTENT.getDisplayName() );

        when: "site opened in master"
        openResourceInMaster( FIRST_SITE_NAME + "/" + USA_CONTENT.getName() );

        then: "population is not changed"
        String source = getDriver().getPageSource();
        source.contains( "Population: " + SF_POPULATION );
    }

    def "GIVEN city content changed  and  'Published' WHEN site opened in 'master', through the portal THEN old data for city-content present"()
    {
        given:
        ContentWizardPanel wizard = findAndSelectContent( SAN_FR_CONTENT.getName() ).clickToolbarEdit();
        wizard.clickOnWizardPublishButton().clickOnPublishNowButton().waitNotificationMessage( Application.EXPLICIT_NORMAL );
        wizard.save().close( SAN_FR_CONTENT.getDisplayName() );

        when: "site opened in master"
        openResourceInMaster( FIRST_SITE_NAME + "/" + USA_CONTENT.getName() );

        then: "population is not changed"
        String source = getDriver().getPageSource();
        source.contains( "Population: " + NEW_SF_POPULATION );
    }
}
