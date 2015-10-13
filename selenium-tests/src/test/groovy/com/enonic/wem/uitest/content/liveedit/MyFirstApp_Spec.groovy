package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.liveedit.PartComponentView
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
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
    String PART_NAME = "country";

    @Shared
    Content USA_CONTENT;

    def "GIVEN creating new Site based on 'My First App' WHEN saved and wizard closed THEN new site should be listed"()
    {
        given:
        MY_FIRST_SITE = buildMySite( NameHelper.uniqueName( "country-site" ) );
        when: "data typed and saved and wizard closed"
        contentBrowsePanel.clickToolbarNew().selectContentType( MY_FIRST_SITE.getContentTypeName() ).typeData( MY_FIRST_SITE ).save().close(
            MY_FIRST_SITE.getDisplayName() );
        TestUtils.saveScreenshot( getSession(), "site_added" );

        then: " new site should be listed"
        contentBrowsePanel.exists( MY_FIRST_SITE.getName() );
    }

    def "GIVEN adding a content with type 'country' WHEN 'save' pressed THEN new added content listed"()
    {
        given:
        USA_CONTENT = buildCountry_Content( "USA", USA_DESCRIPTION, USA_POPULATION, MY_FIRST_SITE.getName() );
        ContentWizardPanel wizard = selectSiteOpenWizard( USA_CONTENT.getContentTypeName(), MY_FIRST_SITE.getName() );

        when: "data typed and saved and wizard closed"
        String message = wizard.typeData( USA_CONTENT ).save().waitNotificationMessage();
        TestUtils.saveScreenshot( getSession(), "content_added" );
        wizard.close( USA_CONTENT.getDisplayName() );

        then: " new 'country' content should be listed"
        filterPanel.typeSearchText( USA_CONTENT.getName() );
        contentBrowsePanel.exists( USA_CONTENT.getName() );
    }

    def "WHEN content, that is child for site, opened for edit THEN 'LIVE' button should be present on the toolbar"()
    {
        when: "a page descriptor added for existing country-content"
        filterPanel.typeSearchText( USA_CONTENT.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( USA_CONTENT.getName() ).clickToolbarEdit();

        then: "'LIVE' button should be present on the toolbar"
        wizard.isLiveButtonDisplayed();
    }

    def "GIVEN content, that is child for site, opened for edit WHEN 'LIVE' button clicked THEN LiveEdit frame displayed AND option filter displayed"()
    {
        given: "a page descriptor added for existing country-content"
        filterPanel.typeSearchText( USA_CONTENT.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( USA_CONTENT.getName() ).clickToolbarEdit();

        when:
        wizard.clickOnLiveToolbarButton();
        TestUtils.saveScreenshot( getSession(), "LIVE_clicked" );
        then: "the 'LiveEdit' frame displayed"
        wizard.isLiveEditFrameDisplayed();
        and: "page descriptor option filter displayed"
        wizard.isPageDescriptorOptionsFilterDisplayed();
    }

    def "GIVEN content, that is child for site, opened for edit AND 'LIVE EDIT' is opened WHEN 'LIVE' button pressed THEN 'Live Edit' frame is not displayed"()
    {
        given: "a page descriptor added for existing country-content"
        filterPanel.typeSearchText( USA_CONTENT.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( USA_CONTENT.getName() ).clickToolbarEdit();
        wizard.clickOnLiveToolbarButton();

        when: "the 'LIVE' button pressed again"
        wizard.clickOnLiveToolbarButton();
        TestUtils.saveScreenshot( getSession(), "frame_hidden" );
        then: "the 'LiveEdit' frame not displayed"
        !wizard.isLiveEditFrameDisplayed();
    }

    def "GIVEN the 'country' content opened for edit WHEN region selected and 'Preview' button pressed THEN region component correctly shown in the new browser window"()
    {
        given: "a page descriptor added for existing country-content"
        filterPanel.typeSearchText( USA_CONTENT.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( USA_CONTENT.getName() ).clickToolbarEdit();
        wizard.clickOnLiveToolbarButton().selectPageDescriptor( COUNTRY_REGION_TITLE ).save();

        when: "the 'Preview' button pressed on the wizard-toolbar"
        TestUtils.saveScreenshot( getSession(), "region_added" );
        wizard.clickToolbarPreview();
        TestUtils.saveScreenshot( getSession(), "preview_clicked" );

        then: "the region page opened in a browser with correct title and correct header"
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_TITLE );
        source.contains( COUNTRY_REGION_TITLE );
        and: "correct header displayed"
        source.contains( COUNTRY_REGION_HEADER );
    }

    def "GIVEN a country-content with a region  WHEN content opened for edit and part inserted into the region THEN correct page source displayed"()
    {
        given: "a page descriptor added for existing country-content"
        filterPanel.typeSearchText( USA_CONTENT.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( USA_CONTENT.getName() ).clickToolbarEdit();

        when: "the 'Preview' button pressed on the wizard-toolbar"
        PartComponentView partComponentView = wizard.showContextWindow().clickOnInsertLink().insertPartByDragAndDrop( "RegionPlaceholder",
                                                                                                                      LIVE_EDIT_FRAME_SITE_HEADER )
        TestUtils.saveScreenshot( getSession(), "part_added" );
        partComponentView.selectItem( PART_NAME )
        TestUtils.saveScreenshot( getSession(), "part_country" );
        NavigatorHelper.switchToContentManagerFrame( getSession() );
        wizard.save();
        wizard.clickToolbarPreview();

        then: "the content opened in a browser and page's sources are correct"
        String source = TestUtils.getPageSource( getSession(), COUNTRY_REGION_TITLE );
        source.contains( "Population: " + USA_POPULATION );
        and: "correct description displayed"
        source.contains( USA_DESCRIPTION );
    }
}
