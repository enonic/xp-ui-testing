package com.enonic.wem.uitest.content;

import spock.lang.Shared

import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage
import com.enonic.autotests.services.ContentManagerService
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent
import com.enonic.autotests.vo.contentmanager.FolderContent
import com.enonic.wem.uitest.BaseGebSpec

class ContentItemViewPanelSpec extends BaseGebSpec
{
	
	@Shared ContentManagerService contentService = new ContentManagerService();
	
	def "Go to Content App, select a folder, click by 'Open' button, open the 'Edit Wizard' and check a title"()
	{	
		given:
		go "admin"
		String contentName = "itemviewtest";
		BaseAbstractContent content = FolderContent.builder().withName("itemviewtest").withDisplayName("itemviewtest").build();
		contentService.addContent(getTestSession(), content, true)

		when:
		contentService.doOpenContent(getTestSession(), content)
		
		then:
		ItemViewPanelPage itemView = new ItemViewPanelPage(getTestSession())
		itemView.getTitle().equals(contentName);

	}	
}