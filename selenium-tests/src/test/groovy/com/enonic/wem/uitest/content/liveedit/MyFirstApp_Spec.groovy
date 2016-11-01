package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.liveedit.PartComponentView
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class MyFirstApp_Spec
    extends BaseSiteSpec
{
    @Shared
    Content MY_FIRST_SITE;

    @Shared
    Content USA_CONTENT;

    def "GIVEN creating new Site based on 'My First App' WHEN saved and wizard closed THEN new site should be listed"()
    {
        given:
        MY_FIRST_SITE = buildMyFirstAppSite( "country-site" );

        when: "data typed and saved and wizard closed"
        contentBrowsePanel.clickToolbarNew().selectContentType( MY_FIRST_SITE.getContentTypeName() ).typeData( MY_FIRST_SITE ).save().close(
            MY_FIRST_SITE.getDisplayName() );
        saveScreenshot( "country_site_added" );

        then: "new site should be listed"
        contentBrowsePanel.exists( MY_FIRST_SITE.getName() );
    }

    def "GIVEN adding a content with type 'country' WHEN 'save' pressed THEN new added content listed"()
    {
        given: "site selected and wizard for adding of new country-content is opened"
        USA_CONTENT = buildCountry_Content( "USA", USA_DESCRIPTION, USA_POPULATION, MY_FIRST_SITE.getName() );
        ContentWizardPanel wizard = selectSiteOpenWizard( USA_CONTENT.getContentTypeName(), MY_FIRST_SITE.getName() );

        when: "data typed and saved and wizard closed"
        wizard.typeData( USA_CONTENT ).save().waitNotificationMessage();
        saveScreenshot( "usa_content_added" );
        wizard.close( USA_CONTENT.getDisplayName() );

        then: "new 'country' content should be listed"
        filterPanel.typeSearchText( USA_CONTENT.getName() );
        contentBrowsePanel.exists( USA_CONTENT.getName() );
    }

    def "WHEN a child country-content of the site is opened for edit THEN 'Show Page Editor' button should be present on the toolbar"()
    {
        when: "a child country-content of the site is opened for edit"
        filterPanel.typeSearchText( USA_CONTENT.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( USA_CONTENT.getName() ).clickToolbarEdit();

        then: "'Show Page Editor' button should be present on the toolbar"
        wizard.isShowPageEditorButtonDisplayed();
    }

    def "GIVEN country-content is opened WHEN  'Show Page Editor' button clicked  THEN LiveEdit frame displayed AND option filter displayed"()
    {
        given: "child country-content is opened"
        filterPanel.typeSearchText( USA_CONTENT.getName() );
        ContentWizardPanel countryContentWizard = contentBrowsePanel.clickCheckboxAndSelectRow( USA_CONTENT.getName() ).clickToolbarEdit();

        when: "'Show Page Editor' button clicked"
        countryContentWizard.showPageEditor();

        then: "the 'LiveEdit' frame displayed"
        countryContentWizard.isLiveEditFrameDisplayed();

        and: "page descriptor option filter displayed"
        countryContentWizard.isPageDescriptorOptionsFilterDisplayed();
    }

    def "GIVEN child country-content is opened AND 'Page Editor' has been shown WHEN 'Hide Page Editor' button pressed THEN 'Live Edit' frame is not displayed"()
    {
        given: "child country-content is opened"
        filterPanel.typeSearchText( USA_CONTENT.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( USA_CONTENT.getName() ).clickToolbarEdit();

        and: "'Page Editor' have been opened"
        wizard.showPageEditor();

        when: "the 'Hide Page Editor' button pressed"
        wizard.hidePageEditor();
        saveScreenshot( "editor_hidden" );

        then: "the 'LiveEdit' frame not displayed"
        !wizard.isLiveEditFrameDisplayed();
    }

    def "GIVEN 'country' content opened WHEN region selected and 'Preview' button pressed THEN region component correctly shown in the new browser window"()
    {
        given: "'country' content opened"
        filterPanel.typeSearchText( USA_CONTENT.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( USA_CONTENT.getName() ).clickToolbarEdit();
        wizard.showPageEditor().selectPageDescriptor( COUNTRY_REGION_TITLE ).save();

        when: "the 'Preview' button pressed on the wizard-toolbar"
        saveScreenshot( "page_descriptor_added_in_country_content" );
        wizard.clickToolbarPreview();
        saveScreenshot( "country_preview_clicked" );

        then: "the region page opened in a browser with correct title and correct header"
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_TITLE );
        source.contains( COUNTRY_REGION_TITLE );

        and: "correct header displayed"
        source.contains( COUNTRY_REGION_HEADER );
    }

    def "GIVEN country-content with a controller WHEN content opened for edit and part inserted into the region THEN correct page source displayed"()
    {
        given: "country-content with a controller is opened"
        ContentWizardPanel wizard = findAndSelectContent( USA_CONTENT.getName() ).clickToolbarEdit();

        and: "Component View is shown"
        wizard.showComponentView();
        PageComponentsViewDialog pageComponentsView = new PageComponentsViewDialog( getSession() );

        when: "new part inserted into the region"
        pageComponentsView.openMenu( "country" ).selectMenuItem( "Insert", "Part" );
        pageComponentsView.doCloseDialog();
        wizard.switchToLiveEditFrame();
        PartComponentView partComponentView = new PartComponentView( getSession() );
        partComponentView.selectItem( COUNTRY_PART_DEFAULT_NAME )
        saveScreenshot( "part_country_added" );
        wizard.save();

        and: "'Preview' button pressed on the wizard-toolbar"
        wizard.clickToolbarPreview();

        then: "the content opened in a browser and page's sources are correct"
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_TITLE );
        source.contains( "Population: " + USA_POPULATION );
        and: "correct description displayed"
        source.contains( USA_DESCRIPTION );
    }
}
