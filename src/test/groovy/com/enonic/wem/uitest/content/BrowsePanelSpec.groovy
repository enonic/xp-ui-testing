package com.enonic.wem.uitest.content

import spock.lang.Shared;

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentGridPage;
import com.enonic.autotests.pages.contentmanager.browsepanel.SelectContentTypeDialog.ContentTypeName;
import com.enonic.autotests.services.ContentManagerService;
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent;
import com.enonic.autotests.vo.contentmanager.FolderContent;
import com.enonic.wem.uitest.BaseGebSpec;

class BrowsePanelSpec extends BaseGebSpec
{
	   @Shared String REPONAME = "/bluman-trampoliner";
	   @Shared ContentManagerService cManagerService = new ContentManagerService();
		def "add content with type 'Folder' to the  folder"()
		{
			given:
			go "admin"
			
			BaseAbstractContent content = FolderContent.builder().withName("test").withDisplayName("test").withType(ContentTypeName.STRUCTURED.getValue()).build();
			String[] parent = [REPONAME]
			content.setParentNames(parent)
			
			when:
			cManagerService.addContent(getTestSession(), content, true)
	
			then:
			ContentGridPage grid = new ContentGridPage(getTestSession())
			grid.findContentInTable(content, 2l)
	
		}
}
