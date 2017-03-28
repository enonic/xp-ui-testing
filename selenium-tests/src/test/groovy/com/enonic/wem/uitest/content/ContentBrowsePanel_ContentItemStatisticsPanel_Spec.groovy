package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath

class ContentBrowsePanel_ContentItemStatisticsPanel_Spec
    extends BaseContentSpec
{
    def "WHEN existing image is selected THEN preview panel should be displayed with a image"()
    {
        when: "existing image is selected"
        findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME );

        then: "preview panel displayed with a image"
        contentItemPreviewPanel.isImageDisplayed();
    }

    def "WHEN existing svg-file is selected THEN 'svg' should be displayed in the preview panel"()
    {
        when: "existing svg-file is selected"
        findAndSelectContent( CIRCLES );
        sleep( 400 );

        then: "'svg' displayed in the preview panel"
        contentItemPreviewPanel.isSVGDisplayed();
    }

    def "WHEN existing folder is selected THEN 'preview not available' should be displayed in the preview panel"()
    {
        when: "existing folder is selected"
        findAndSelectContent( IMPORTED_FOLDER_NAME );

        then: "'preview not available' for the folder should be displayed"
        contentItemPreviewPanel.isPreviewNotAvailAble();
    }

    def "WHEN existing page-template is selected THEN 'page-preview' should be displayed in the panel"()
    {
        given: "site with a template was added"
        Content site = buildMyFirstAppSite( "site-statistics" );
        addSite( site );
        Content template = buildPageTemplate( COUNTRY_REGION_PAGE_CONTROLLER, TEMPLATE_SUPPORTS_SITE, "test statistics template",
                                              site.getName() );
        and:"the site is expanded"
        filterPanel.typeSearchText( site.getName() );
        contentBrowsePanel.expandContent( ContentPath.from( site.getName() ) );
        and:"new template has been added"
        contentBrowsePanel.selectContentInTable( "_templates" ).clickToolbarNew().selectContentType(
            template.getContentTypeName() ).showPageEditor().typeData( template ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "the template is selected"
        findAndSelectContent( template.getName() );
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        saveScreenshot( "page_preview_statistic_panel" )

        then: "'page' should be displayed in the preview panel"
        contentItemPreviewPanel.isPageDisplayed();
    }
}
