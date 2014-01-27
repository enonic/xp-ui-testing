package com.enonic.wem.uitest.schema

import spock.lang.Shared;

import com.enonic.autotests.pages.schemamanager.KindOfContentTypes;
import com.enonic.autotests.pages.schemamanager.SchemaGridPage;
import com.enonic.autotests.services.ContentManagerService;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.vo.schemamanger.ContentType;
import com.enonic.wem.uitest.BaseGebSpec;

class BrowsePanelSpec extends BaseGebSpec
{

	@Shared ContentTypeService contentTypeService = new ContentTypeService();
	
	def "Given BrowsePanel When adding Folder ContentType Then the new contentype should be listed in the table"()
	{
		given:
		go "admin"
		String folderCFG = XMLctFolderCfg.FOLDER_CFG
		ContentType ctype = ContentType.with().name("folderctype").kind(KindOfContentTypes.CONTENT_TYPE).configuration(folderCFG).build();
		
		when:
		contentTypeService.createContentType(getTestSession(), ctype, true)
		report "Content types GridPage opened, try to find a new contenttype with name: "+ ctype.getName()
		then:
		SchemaGridPage grid = new SchemaGridPage(getTestSession())
		grid.isContentTypePresentInTable(ctype)

	}
	
}
