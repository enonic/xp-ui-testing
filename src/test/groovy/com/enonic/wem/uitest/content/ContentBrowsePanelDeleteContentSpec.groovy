package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage
import com.enonic.autotests.services.ContentService.HowOpenContent
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanelDeleteContentSpec
extends BaseGebSpec
{

    @Shared String DELETE_CONTENT_KEY = "deletecontent_test"

    def "GIVEN existing content, WHEN content opened and delete button pressed THEN the content should not be listed in the table"( )
    {
        given:
        go "admin"
        BaseAbstractContent content = addContentToBeDeleted();
        ItemViewPanelPage view = contentService.doOpenContent( getTestSession(), content, HowOpenContent.TOOLBAR );

        when:
        view.doDeleteContent( content.getDisplayName() )

        then:
        ContentBrowsePanel browsePanel = new ContentBrowsePanel( getTestSession() )  
        !browsePanel.exists(content.getContentPath())
    }

    def "GIVEN existing content, WHEN content selected and delete button pressed THEN the content should not be listed in the table"( )
    {
        given:
        go "admin"
        BaseAbstractContent content = addContentToBeDeleted();
        List<BaseAbstractContent> contentList = new ArrayList<>();
        contentList.add( content );

        when:
        ContentBrowsePanel browsePanel = contentService.deleteContentUseToolbar( getTestSession(), contentList )

        then:
        !browsePanel.exists(content.getContentPath())
    }

    def "GIVEN existing two contents, WHEN all content selected and delete button pressed THEN the content should not be listed in the table"( )
    {
        given:
        go "admin"
        BaseAbstractContent content1 = addContentToBeDeleted();
        BaseAbstractContent content2 = addContentToBeDeleted();
        List<BaseAbstractContent> contentList = new ArrayList<>();
        contentList.add( content1 );
        contentList.add( content2 );

        when:
        ContentBrowsePanel browsePanel = contentService.deleteContentUseToolbar( getTestSession(), contentList )

        then:
		!browsePanel.exists(content1.getContentPath()) && !browsePanel.exists(content2.getContentPath())
    }
}
