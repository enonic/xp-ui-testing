package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.context_window.PageInspectionPanel
import com.enonic.autotests.pages.form.CityFormView
import com.enonic.autotests.pages.form.liveedit.ContextWindow
import com.enonic.autotests.pages.form.liveedit.PartComponentView
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.PageComponent
import com.enonic.xp.content.ContentPath
import spock.lang.Ignore
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

    def "GIVEN existing Site based on 'My First App' WHEN new template with the 'country' region as a controller was added THEN new template should be listed"()
    {
        given: "existing Site based on 'My First App'"
        SITE = buildMyFirstAppSite( "mysite" );
        addSite( SITE );
        filterPanel.typeSearchText( SITE.getName() );
        contentBrowsePanel.expandContent( ContentPath.from( SITE.getName() ) );
        PAGE_TEMPLATE = buildPageTemplate( COUNTRY_REGION_PAGE_CONTROLLER, TEMPLATE_SUPPORTS_COUNTRY, COUNTRY_TEMPLATE_DISPLAY_NAME,
                                           SITE.getName() );

        when: "'Templates' folder selected and new page-template is added"
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable( "_templates" ).clickToolbarNew().selectContentType(
            PAGE_TEMPLATE.getContentTypeName() ).typeData( PAGE_TEMPLATE );
        wizard.clickOnMarkAsReadyButton();
        sleep( 500 );
        wizard.closeBrowserTab().switchToBrowsePanelTab();
        sleep( 500 );

        then: "new page-template should be listed"
        filterPanel.typeSearchText( PAGE_TEMPLATE.getName() );
        contentBrowsePanel.exists( PAGE_TEMPLATE.getName() );
    }

    def "GIVEN existing page-template is opened WHEN the template is opened and the and 'country' part is added THEN correct page-sources should be present in the HTML"()
    {
        given: "existing page-template is opened"
        filterPanel.typeSearchText( PAGE_TEMPLATE.getName() );
        ContentWizardPanel contentWizard = contentBrowsePanel.selectContentInTable( PAGE_TEMPLATE.getName() ).clickToolbarEdit();

        when: "the template is opened and the 'country region' controller selected and 'country' part inserted"
        PageComponentsViewDialog pageComponentsView = contentWizard.showComponentView();
        and: "Part has been inserted(the site should be saved automatically)"
        insertPart( pageComponentsView, "country", contentWizard, "country" );

        and: "Preview button has been pressed"
        contentWizard.clickToolbarPreview();
        saveScreenshot( "country_part_added" );

        then: "sources should not be empty"
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_TITLE );
        source != null;

        and: "correct title of the region should be present in the HTML"
        source.contains( COUNTRY_REGION_TITLE );
        and: "correct header should be present"
        source.contains( COUNTRY_TEMPLATE_DISPLAY_NAME );
    }

    def "GIVEN existing page-template with a 'country' part is opened WHEN the template opened and the 'city list' part inserted THEN correct page-sources should be present in the HTML"()
    {
        given: "existing page-template is opened"
        ContentWizardPanel contentWizard = findAndSelectContent( PAGE_TEMPLATE.getName() ).clickToolbarEdit(); ;

        when: "'city list' part has been inserted"
        PageComponentsViewDialog pageComponentsView = contentWizard.showComponentView();
        insertPart( pageComponentsView, "country", contentWizard, "City list" );
        contentWizard.clickOnMarkAsReadyButton();
        contentWizard.clickToolbarPreview();
        saveScreenshot( "city_list_part_added" );

        then: "page sources should not be empty"
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_TITLE );
        source != null;

        and: "'Cities' header should be present in the HTML"
        source.contains( "Cities" );
    }

    def "GIVEN existing page template is opened WHEN Context Window has been opened AND 'Inspect' link is clicked THEN correct page controller should be displayed on the panel"()
    {
        given: "existing page-template is opened"
        filterPanel.typeSearchText( PAGE_TEMPLATE.getName() );
        ContentWizardPanel contentWizard = contentBrowsePanel.selectContentInTable( PAGE_TEMPLATE.getName() ).clickToolbarEdit();

        when: "the 'Inspect' link is clicked"
        ContextWindow contextWindow = contentWizard.showContextWindow();
        contextWindow.clickOnTabBarItem( "Page" );
        PageInspectionPanel inspectionPanel = new PageInspectionPanel( getSession() );
        String name = inspectionPanel.getSelectedPageController();
        saveScreenshot( "city_part_added" );

        then: "correct page controller should be displayed on the panel"
        name == PAGE_CONTROLLER_NAME;
    }

    // @Ignore
    def "GIVEN new USA-content has been added AND child city-content for USA was added  WHEN USA-content selected AND 'Preview' button is pressed THEN correct text should be present on the page-source "()
    {
        given: "new USA-content has been added"
        USA_CONTENT = buildCountry_Content( "USA", USA_DESCRIPTION, USA_POPULATION, SITE.getName() );
        ContentWizardPanel wizard = selectSitePressNew( USA_CONTENT.getContentTypeName(), SITE.getName() );
        wizard.typeData( USA_CONTENT ).clickOnMarkAsReadyButton();
        wizard.closeBrowserTab().switchToBrowsePanelTab();

        and: "child city-content for USA was added "
        contentBrowsePanel.doClearSelection();
        SAN_FR_CONTENT = buildCity_Content( "San Francisco", SF_LOCATION, SF_POPULATION, USA_CONTENT.getName() );

        and: "new City-content added beneath the USA-content"
        findAndSelectContent( USA_CONTENT.getName() ).clickToolbarNew().selectContentType( SAN_FR_CONTENT.getContentTypeName() ).typeData(
            SAN_FR_CONTENT ).clickOnMarkAsReadyButton();
        wizard.closeBrowserTab().switchToBrowsePanelTab();
        findAndSelectContent( USA_CONTENT.getName() );
        sleep( 3000 );
        saveScreenshot( "san_francisco_added" )
        contentBrowsePanel.doClearSelection();

        when: "country-content is selected in the grid and the 'Preview' button pressed"
        findAndSelectContent( USA_CONTENT.getName() );
        sleep( 1000 );
        saveScreenshot( "USA_City" )
        contentBrowsePanel.clickToolbarPreview();
        sleep( 1000 );

        then: "correct headers "
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_TITLE );
        source.contains( CITY_HEADER );
        and: "correct Population of the city should be present in the 'page source'"
        source.contains( "Population: " + SF_POPULATION );

    }

    // @Ignore
    def "WHEN site is not published yet WHEN site opened in 'master', through the portal THEN '404' should be present in the sources"()
    {
        given: "site not published and opened in the 'master'"
        openResourceInMaster( SITE.getName() + "/" + USA_CONTENT.getName() );
        sleep( 2000 );

        expect: "'404' should be present in the sources"
        saveScreenshot( "portal-country-preview-master-offline" );
        String source = getDriver().getPageSource();
        source.contains( "404" );
    }

    ///@Ignore
    def "WHEN site is not published yet AND site opened in 'draft', through the portal THEN correct data should be present in page sources"()
    {
        when: "site not published and opened in the 'master'"
        openResourceInDraft( SITE.getName() + "/" + USA_CONTENT.getName() );
        sleep( 2000 );
        saveScreenshot( "portal-country-preview-draft-offline" );

        then: "correct population should be displayed"
        String source = getDriver().getPageSource();
        source.contains( "Population: " + SF_POPULATION );

        and: "correct description shown"
        source.contains( USA_DESCRIPTION );
    }

    //@Ignore
    def "WHEN site has been published AND site opened through the portal THEN correct description and population should be present in page sources"()
    {
        given: "site has been 'published'"
        filterPanel.typeSearchText( SITE.getName(), );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName(), );
        contentBrowsePanel.showPublishMenu().clickOnMarkAsReadyMenuItem();
        ContentPublishDialog dialog = contentBrowsePanel.clickToolbarPublish();
        dialog.includeChildren( true ).clickOnPublishButton();
        sleep( 2000 );
        saveScreenshot( "country_site_published" );

        when: "site was opened in master"
        openResourceInMaster( SITE.getName() + "/" + USA_CONTENT.getName() );

        then: "correct population should be present in page sources"
        String source = getDriver().getPageSource();
        source.contains( "Population: " + SF_POPULATION );

        and: "sources should contains the description for USA country"
        source.contains( USA_DESCRIPTION );
    }

    //@Ignore
    def "GIVEN city content changed and content is not 'Published' now WHEN site opened in 'master', through the portal THEN old data for city-content should be displayed"()
    {
        given: "city content was changed and content was not 'Published'"
        ContentWizardPanel wizard = findAndSelectContent( SAN_FR_CONTENT.getName() ).clickToolbarEdit();
        CityFormView cityFormView = new CityFormView( getSession() );
        cityFormView.typePopulation( NEW_SF_POPULATION );
        wizard.save();
        sleep( 1000 );

        when: "site was opened in the master"
        openResourceInMaster( SITE.getName() + "/" + USA_CONTENT.getName() );

        then: "population should not be changed"
        String source = getDriver().getPageSource();
        source.contains( "Population: " + SF_POPULATION );
    }

    //@Ignore
    def "GIVEN city content was changed and 'Published' WHEN site opened in 'master', through the portal THEN new population should be displayed"()
    {
        given: "city content changed and 'Published'"
        ContentWizardPanel wizard = findAndSelectContent( SAN_FR_CONTENT.getName() ).clickToolbarEdit();
        wizard.clickOnMarkAsReadyAndDoPublish();
        contentBrowsePanel.waitForNotificationMessage();
        sleep( 1000 );

        when: "site was opened in master"
        openResourceInMaster( SITE.getName() + "/" + USA_CONTENT.getName() );

        then: "new population should be displayed"
        String source = getDriver().getPageSource();
        source.contains( "Population: " + NEW_SF_POPULATION );
    }

    //@Ignore
    def "GIVEN existing country content WHEN 'Page Component View' is opened THEN all added components should be displayed"()
    {
        given: "existing country content is opened"
        ContentWizardPanel wizard = findAndSelectContent( USA_CONTENT.getName() ).clickToolbarEdit();

        when: "'Page Component View' is opened"
        wizard.showComponentView();
        PageComponentsViewDialog view = new PageComponentsViewDialog( getSession() );
        List<PageComponent> components = view.getPageComponents();
        saveScreenshot( "order-components" )

        then: "all added components should be displayed"
        components.size() == 4;

        and:
        components.get( 0 ).getName().equals( COUNTRY_TEMPLATE_DISPLAY_NAME );
        and:
        components.get( 1 ).getName() == "country";
        and:
        components.get( 3 ).getName() == COUNTRY_PART_DEFAULT_NAME;
        and:
        components.get( 2 ).getName() == "City list";
    }
}
