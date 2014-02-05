package com.enonic.wem.uitest.content

import java.util.ArrayList;
import java.util.List;

import spock.lang.Shared;
import spock.lang.Stepwise;

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentGridPage;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage;
import com.enonic.autotests.services.ContentService;
import com.enonic.autotests.services.ContentService.HowOpenContent;
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent;
import com.enonic.autotests.vo.contentmanager.FolderContent;
import com.enonic.wem.uitest.BaseGebSpec;

@Stepwise
class BrowsePanelDeleteContentSpec extends BaseGebSpec
{

	@Shared ContentService contentService = new ContentService()
	@Shared String DELETE_CONTENT_KEY = "deletecontent_test"

	def "Given BrowsePanel and existing content, when content opened and delete button pressed Then the content should not be listed in the table"()
	{
		given:
		go "admin"
		String name = "deletecontent" + +Math.abs( new Random().nextInt() )
		BaseAbstractContent content = FolderContent.builder().withName(name).withDisplayName("contenttodelete").build();
		contentService.addContent(getTestSession(), content, true)
		ItemViewPanelPage view = contentService.doOpenContent(getTestSession(), content, HowOpenContent.TOOLBAR);
		
		when:
		view.doDeleteContent(content.getDisplayName())
		
		then:
		ContentGridPage grid = new ContentGridPage(getTestSession())
		!grid.findContentInTable(content,2l)
	}

	def "Given BrowsePanel and existing content, when content selected and delete button pressed Then the content should not be listed in the table"()
	{
		given:
		go "admin"
		String name = "deletecontent" + +Math.abs( new Random().nextInt() )
		BaseAbstractContent content = FolderContent.builder().withName(name).withDisplayName("contenttodelete").build();
		contentService.addContent(getTestSession(), content, true)
		
		List<BaseAbstractContent> contentList = new ArrayList<>();
		contentList.add(content);
		
		when:
		ContentGridPage page = contentService.deleteContentUseToolbar(getTestSession(), contentList)
		
		then:
		!page.findContentInTable(content, 3l)
	}
	
	def "Given BrowsePanel and existing two contents, when all content selected and delete button pressed Then the content should not be listed in the table"()
	{
		given:
		go "admin"
		String name = "deletecontent" + +Math.abs( new Random().nextInt() )
		BaseAbstractContent content1 = FolderContent.builder().withName(name).withDisplayName("contenttodelete").build();
		contentService.addContent(getTestSession(), content1, true)
		
		name = "deletecontent" + +Math.abs( new Random().nextInt() )
		BaseAbstractContent content2 = FolderContent.builder().withName(name).withDisplayName("contenttodelete").build();
		contentService.addContent(getTestSession(), content2, true)
		
		List<BaseAbstractContent> contentList = new ArrayList<>();
		contentList.add(content1);
		contentList.add(content2);
		
		when:
		ContentGridPage page = contentService.deleteContentUseToolbar(getTestSession(), contentList)
		
		then:
		!page.findContentInTable(content1, 2l) && 	!page.findContentInTable(content2, 2l)
	}
}
