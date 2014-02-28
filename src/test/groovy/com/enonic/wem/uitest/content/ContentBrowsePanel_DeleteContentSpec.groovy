package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage
import com.enonic.autotests.services.ContentService.HowOpenContent
import com.enonic.autotests.services.NavigatorHelper;
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_DeleteContentSpec
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
        ItemViewPanelPage view = contentService.doOpenContent( getTestSession(), content, HowOpenContent.TOOLBAR );

        when:
        view.doDeleteContent( content.getDisplayName() )
        TestUtils.saveScreenshot( getTestSession() );
        
		then:
        !contentBrowsePanel.exists( content.getContentPath() )
    }

    def "GIVEN existing two contents, WHEN all content selected and delete button pressed THEN the content should not be listed in the table"()
    {
        given:
        BaseAbstractContent content1 = addRootContentToBeDeleted();
        BaseAbstractContent content2 = addRootContentToBeDeleted();
        List<BaseAbstractContent> contentList = new ArrayList<>();
        contentList.add( content1 );
        contentList.add( content2 );

        when:
        contentService.deleteContentUseToolbar( getTestSession(), contentList )
        TestUtils.saveScreenshot( getTestSession() );
        then:
        !contentBrowsePanel.exists( content1.getContentPath() ) && !contentBrowsePanel.exists( content2.getContentPath() )
    }
	def "GIVEN a Content on root WHEN deleted THEN deleted content is no longer listed at root"()
	{
		given:
		BaseAbstractContent content = addRootContentToBeDeleted();
		List<BaseAbstractContent> contents = new ArrayList<>();
		contents.add( content );
		ContentBrowsePanel browsePanel = new ContentBrowsePanel( getTestSession() )
		browsePanel.doClearSelection();

		when:
		contentBrowsePanel.selectContent( content.getContentPath() )
		contentBrowsePanel.deleteSelected();

		then:
		!contentBrowsePanel.exists( content.getContentPath() );
	}
}
