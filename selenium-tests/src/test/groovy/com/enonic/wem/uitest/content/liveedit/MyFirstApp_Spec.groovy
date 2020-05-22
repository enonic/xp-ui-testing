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

    def "Preconditions: new site should be added"()
    {
        given:
        MY_FIRST_SITE = buildMyFirstAppSite( "country-site" );

        when: "data was typed and saved and wizard closed"
        contentBrowsePanel.clickToolbarNew().selectContentType( MY_FIRST_SITE.getContentTypeName() ).typeData(
            MY_FIRST_SITE ).save().closeBrowserTab().switchToBrowsePanelTab();
        saveScreenshot( "country_site_added" );

        then: "new site should be listed"
        filterPanel.typeSearchText( MY_FIRST_SITE.getName() );
        contentBrowsePanel.exists( MY_FIRST_SITE.getName() );
    }

    def "GIVEN new country-wizard is opened WHEN data have been typed AND 'save' pressed THEN new added content should be listed"()
    {
        given: "site is selected and wizard for adding of new country-content is opened"
        USA_CONTENT = buildCountry_Content( "USA", USA_DESCRIPTION, USA_POPULATION, MY_FIRST_SITE.getName() );
        ContentWizardPanel wizard = selectSitePressNew( USA_CONTENT.getContentTypeName(), MY_FIRST_SITE.getName() );

        when: "all data was typed and saved and wizard closed"
        wizard.typeData( USA_CONTENT ).save().waitForNotificationMessage();
        saveScreenshot( "usa_content_added" );
        wizard.closeBrowserTab().switchToBrowsePanelTab();

        then: "new 'country' content should be listed"
        filterPanel.typeSearchText( USA_CONTENT.getName() );
        contentBrowsePanel.exists( USA_CONTENT.getName() );
    }

    def "WHEN child country-content is opened THEN 'Show Page Editor' button should be present in the toolbar"()
    {
        when: "child country-content is opened"
        filterPanel.typeSearchText( USA_CONTENT.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( USA_CONTENT.getName() ).clickToolbarEdit();
        saveScreenshot( "button_show_page_editor_displayed" )

        then: "'Show Page Editor' button should be present on the toolbar"
        wizard.isShowPageEditorButtonDisplayed();
    }

    def "GIVEN country-content is opened WHEN 'Show Page Editor' button clicked  THEN LiveEdit frame should be loaded AND controller selector should be present"()
    {
        given: "child country-content is opened"
        filterPanel.typeSearchText( USA_CONTENT.getName() );
        ContentWizardPanel countryContentWizard = contentBrowsePanel.clickCheckboxAndSelectRow( USA_CONTENT.getName() ).clickToolbarEdit();

        when: "'Show Page Editor' button has been clicked"
        countryContentWizard.showPageEditor();

        then: "the 'LiveEdit' frame should be displayed"
        countryContentWizard.isLiveEditFrameDisplayed();

        and: "page descriptor option filter should be displayed"
        countryContentWizard.isPageDescriptorOptionsFilterDisplayed();
    }

    def "GIVEN child country-content is opened AND 'Page Editor' is loaded WHEN 'Hide Page Editor' button has been pressed THEN 'Live Edit' frame gets not visible"()
    {
        given: "child country-content is opened"
        filterPanel.typeSearchText( USA_CONTENT.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( USA_CONTENT.getName() ).clickToolbarEdit();

        and: "'Page Editor' have been opened"
        wizard.showPageEditor();

        when: "'Hide Page Editor' button has been pressed"
        wizard.hidePageEditor();
        saveScreenshot( "editor_hidden" );

        then: "'LiveEdit' frame gets not visible"
        !wizard.isLiveEditFrameDisplayed();
    }

    def "GIVEN 'country' content opened WHEN controller has been selected and 'Preview' button pressed THEN region component should be shown in the new browser window"()
    {
        given: "'country' content is opened"
        filterPanel.typeSearchText( USA_CONTENT.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( USA_CONTENT.getName() ).clickToolbarEdit();
        wizard.showPageEditor().selectPageDescriptor( COUNTRY_REGION_TITLE ).switchToDefaultWindow();
        sleep( 500 )

        when: "the 'Preview' button pressed on the wizard-toolbar"
        saveScreenshot( "page_descriptor_added_in_country_content" );
        wizard.clickToolbarPreview();
        saveScreenshot( "country_preview_clicked" );

        then: "Expected title should be loaded in the page"
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_TITLE );
        source.contains( COUNTRY_REGION_TITLE );

        and: "expected header should be displayed"
        source.contains( COUNTRY_REGION_HEADER );
    }

    def "GIVEN existing country-content is opened WHEN new part has been inserted THEN page source should be updated"()
    {
        given: "existing country-content is opened"
        ContentWizardPanel wizard = findAndSelectContent( USA_CONTENT.getName() ).clickToolbarEdit();

        and: "open Component View:"
        wizard.showComponentView();
        PageComponentsViewDialog pageComponentsView = new PageComponentsViewDialog( getSession() );

        when: "new part has been inserted"
        pageComponentsView.openMenu( "country" ).selectMenuItem( "Insert", "Part" );
        pageComponentsView.doCloseDialog();
        wizard.switchToLiveEditFrame();
        PartComponentView partComponentView = new PartComponentView( getSession() );
        partComponentView.selectItem( COUNTRY_PART_DEFAULT_NAME );
        saveScreenshot( "part_country_added" );
        wizard.switchToDefaultWindow();
        and: "'Preview' button has been pressed"
        wizard.clickToolbarPreview();

        then: "page source should be updated"
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_TITLE );
        source.contains( "Population: " + USA_POPULATION );
        and: "expected description should be present"
        source.contains( USA_DESCRIPTION );
    }
}
