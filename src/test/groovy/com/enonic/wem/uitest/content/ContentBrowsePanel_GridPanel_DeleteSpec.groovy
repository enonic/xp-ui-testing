package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.ArchiveContent;
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent
import com.enonic.autotests.vo.contentmanager.FolderContent;
import com.enonic.wem.api.content.ContentPath;
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_GridPanel_DeleteSpec
    extends BaseGebSpec
{

    @Shared
    ContentBrowsePanel contentBrowsePanel

    @Shared
    String DELETE_CONTENT_KEY = "deletecontent_test"

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }

    def "GIVEN existing content, WHEN content opened and delete button pressed THEN the content should not be listed in the table"()
    {
        given:
        BaseAbstractContent content = addRootContentToBeDeleted();
		ItemViewPanelPage contentInfoPage = contentBrowsePanel.doOpenContent( content )

        when:
		contentInfoPage.openDeleteConfirmationDialog().doConfirm()
        TestUtils.saveScreenshot( getTestSession() )

        then:
        !contentBrowsePanel.exists( content.getPath() )
    }
	
    def "GIVEN existing two contents, WHEN all content selected and delete button pressed THEN the content should not be listed in the table"()
    {
        given:
        BaseAbstractContent content1 = addRootContentToBeDeleted()
        BaseAbstractContent content2 = addRootContentToBeDeleted()
        List<BaseAbstractContent> contentList = new ArrayList<>()
        contentList.add( content1 )
        contentList.add( content2 )

        when:
		contentBrowsePanel.doClearSelection()
		contentBrowsePanel.expandContent( content1.getParent() ).selectContentInTable(contentList).clickToolbarDelete().doDelete()
		
     
        then:
        !contentBrowsePanel.exists( content1.getPath() ) && !contentBrowsePanel.exists( content2.getPath() )
    }

    def "GIVEN a Content on root WHEN deleted THEN deleted content is no longer listed at root"()
    {
        given:
        BaseAbstractContent content = addRootContentToBeDeleted()
        List<BaseAbstractContent> contents = new ArrayList<>()
        contents.add( content )
        ContentBrowsePanel browsePanel = new ContentBrowsePanel( getTestSession() )
        browsePanel.doClearSelection()

        when:
        contentBrowsePanel.selectRowWithContent( content.getPath() )
        contentBrowsePanel.deleteSelected();

        then:
        !contentBrowsePanel.exists( content.getPath() )
    }
	
	def "GIVEN a Content beneath an existing WHEN deleted THEN deleted Content is no longer listed beneath parent"()
	{
		given:
		BaseAbstractContent parent = FolderContent.builder().
			withParent( ContentPath.ROOT ).
			withName( NameHelper.unqiueName("parent") ).
			withDisplayName( "parent" ).
			build();
			addContent(contentBrowsePanel, parent, true)
		
		addArchiveToParent(parent.getName())
		
		List<BaseAbstractContent> contentList = new ArrayList<>()
		contentList.add( addArchiveToParent(parent.getName()) )
		

		when:
		contentBrowsePanel.expandContent( contentList.get(0).getParent() ).selectContentInTable(contentList).clickToolbarDelete().doDelete()
		
		then:
		!contentBrowsePanel.exists( contentList.get(0).getPath() )
	}
}
