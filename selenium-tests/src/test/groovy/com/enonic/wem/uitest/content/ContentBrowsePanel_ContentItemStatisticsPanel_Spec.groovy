package com.enonic.wem.uitest.content

import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath

class ContentBrowsePanel_ContentItemStatisticsPanel_Spec
    extends BaseContentSpec
{
    def "WHEN existing image is selected THEN preview panel displayed with a image"()
    {
        when: "existing image is selected"
        findAndSelectContent( IMPORTED_BOOK_IMAGE );

        then: "preview panel displayed with a image"
        contentItemPreviewPanel.isImageDisplayed();
    }

    def "WHEN existing svg-file is selected THEN 'svg' displayed in the preview panel"()
    {
        when: "existing svg-file is selected"
        findAndSelectContent( CIRCLES );

        then: "'svg' displayed in the preview panel"
        contentItemPreviewPanel.isSVGDisplayed();
    }

    def "WHEN existing folder is selected THEN 'preview not available' displayed in the preview panel"()
    {
        when: "existing folder is selected"
        findAndSelectContent( IMPORTED_FOLDER_NAME );

        then: "'preview not available' displayed in the preview panel"
        contentItemPreviewPanel.isPreviewNotAvailAble();
    }

    def "WHEN existing page-template is selected THEN 'page-preview' is displayed in the panel"()
    {
        given: "site with a template added"
        Content site = buildMyFirstAppSite( "site-statistics" );
        addSiteBasedOnFirstApp( site );
        Content template = buildPageTemplate( COUNTRY_REGION_PAGE_CONTROLLER, TEMPLATE_SUPPORTS_SITE, "test statistics template",
                                              site.getName() );

        filterPanel.typeSearchText( site.getName() );
        contentBrowsePanel.expandContent( ContentPath.from( site.getName() ) );
        contentBrowsePanel.selectContentInTable( "_templates" ).clickToolbarNew().selectContentType(
            template.getContentTypeName() ).showPageEditor().typeData( template ).save().close( template.getDisplayName() );
        contentBrowsePanel.clickOnClearSelection();

        when: "the template is selected"
        findAndSelectContent( template.getName() );

        then: "'page' displayed in the preview panel"
        contentItemPreviewPanel.isPageDisplayed();
    }
}
