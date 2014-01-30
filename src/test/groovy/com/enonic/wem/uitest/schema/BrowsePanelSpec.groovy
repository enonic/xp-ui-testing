package com.enonic.wem.uitest.schema
import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.pages.schemamanager.SchemaGridPage;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.schemamanger.ContentType;
import spock.lang.Shared;

import com.enonic.autotests.pages.schemamanager.KindOfContentTypes;
import com.enonic.autotests.pages.schemamanager.SchemaGridPage;
import com.enonic.autotests.services.ContentManagerService;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.vo.schemamanger.ContentType;
import com.enonic.wem.uitest.BaseGebSpec;
import com.enonic.wem.uitest.schema.cfg.TextLineContentTypeCfg;
import com.enonic.wem.uitest.schema.cfg.FolderContentTypeCfg;

class BrowsePanelSpec extends BaseGebSpec
{

	@Shared ContentTypeService contentTypeService = new ContentTypeService();
	
	def "Given BrowsePanel When adding Folder ContentType Then the new contentype should be listed in the table"()
	{
		given:
		go "admin"
		String folderCFG = FolderContentTypeCfg.FOLDER_CFG
		ContentType ctype = ContentType.with().name("folderctype").kind(KindOfContentTypes.CONTENT_TYPE).configuration(folderCFG).build();
		
		when:
		contentTypeService.createContentType(getTestSession(), ctype, true)
		report "Content types GridPage opened, try to find a new contenttype with name: "+ ctype.getName()
		
		then:
		SchemaGridPage grid = new SchemaGridPage(getTestSession())
		grid.isContentTypePresentInTable(ctype)

	}
	
	def "Given BrowsePanel When adding TextLine ContentType Then the new contentype should be listed in the table"()
	{
		given:
		go "admin"
		String textLineCFG = TextLineContentTypeCfg.CFG
		ContentType ctype = ContentType.with().name("textlinectype").kind(KindOfContentTypes.CONTENT_TYPE).configuration(textLineCFG).build();
		
		when:
		contentTypeService.createContentType(getTestSession(), ctype, true)
		report "Content types GridPage opened, try to find a new contenttype with name: "+ ctype.getName()
		
		then:
		SchemaGridPage grid = new SchemaGridPage(getTestSession())
		grid.isContentTypePresentInTable(ctype)

	}
	
	def "Given BrowsePanel and created a contentType When contenttype deleted Then the the contentype should not be listed in the table"()
	{
		given:
		go "admin"
		String folderCFG = FolderContentTypeCfg.FOLDER_CFG
		ContentType ctypeToDelete = ContentType.with().name("ctypetodelete").kind(KindOfContentTypes.CONTENT_TYPE).configuration(folderCFG).build();
		
		when:
		contentTypeService.createContentType(getTestSession(), ctypeToDelete, true)
		
		then:
		SchemaGridPage schemasPage = contentTypeService.deleteContentType(getTestSession(), ctypeToDelete);
		!schemasPage.isContentTypePresentInTable(ctypeToDelete);
		
	}
	
}
