package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath

class ContentBrowsePanel_ContentItemStatisticsPanel_Spec
    extends BaseContentSpec
{
    def "WHEN existing image has been selected THEN preview panel should be displayed with the image"()
    {
        when: "existing image has been selected"
        findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME );

        then: "the image should be present on the preview panel"
        contentItemPreviewPanel.isImageDisplayed();
    }

    def "WHEN existing svg-file has been selected THEN 'svg' should be displayed in the preview panel"()
    {
        when: "existing svg-file is selected"
        findAndSelectContent( CIRCLES );
        sleep( 400 );

        then: "'svg' displayed in the preview panel"
        contentItemPreviewPanel.isSVGDisplayed();
    }

    def "WHEN existing folder has been selected THEN 'preview not available' should be displayed in the preview panel"()
    {
        when: "existing folder is selected"
        findAndSelectContent( IMPORTED_FOLDER_NAME );

        then: "'preview not available' for the folder should be displayed"
        contentItemPreviewPanel.isPreviewNotAvailAble();
    }

    def "WHEN existing page-template is selected THEN 'page-preview' should be displayed in the panel"()
    {
        given: "site with a template has been added"
        Content site = buildMyFirstAppSite( "site-statistics" );
        addSite( site );
        Content template = buildPageTemplate( COUNTRY_REGION_PAGE_CONTROLLER, TEMPLATE_SUPPORTS_SITE, "test statistics template",
                                              site.getName() );
        and: "the site is expanded"
        filterPanel.typeSearchText( site.getName() );
        contentBrowsePanel.expandContent( ContentPath.from( site.getName() ) );
        and: "new template has been added"
        contentBrowsePanel.selectContentInTable( "_templates" ).clickToolbarNew().selectContentType(
            template.getContentTypeName() ).showPageEditor().typeData( template ).closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "the template has been selected"
        findAndSelectContent( template.getName() );
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        saveScreenshot( "page_preview_statistic_panel" )

        then: "'page' should be displayed in the preview panel"
        contentItemPreviewPanel.isPageDisplayed();
    }
}
