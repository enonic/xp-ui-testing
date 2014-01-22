package com.enonic.wem.uitest.content;
import spock.lang.Shared;


import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage;
import com.enonic.autotests.services.ContentManagerService;
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent;
import com.enonic.autotests.vo.contentmanager.FolderContent;
import com.enonic.wem.uitest.BaseGebSpec;
class ContentItemViewPanelSpec extends BaseGebSpec
{
	@Shared ContentManagerService cManagerService = new ContentManagerService();
	
	String CHECK_TITLE_TEST_FOLDER_DISPLAYNAME = "BildeArkiv";
	
	def "Go to Content App, select a folder, click by 'Open' button, open the 'Edit Wizard' and check a title"()
	{	
		given:
		go "admin"	
		BaseAbstractContent content = FolderContent.builder().withName(CHECK_TITLE_TEST_FOLDER_DISPLAYNAME.toLowerCase()).withDisplayName(CHECK_TITLE_TEST_FOLDER_DISPLAYNAME).build();
		
		when:		
		cManagerService.doOpenContent(getTestSession(), content)		
		
		then:
		ItemViewPanelPage itemView = new ItemViewPanelPage(getTestSession())
		itemView.getTitle().equals(CHECK_TITLE_TEST_FOLDER_DISPLAYNAME);

	}
	
	
	
	
}