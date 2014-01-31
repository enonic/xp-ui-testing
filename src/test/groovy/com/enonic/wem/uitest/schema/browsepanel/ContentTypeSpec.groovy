package com.enonic.wem.uitest.schema.browsepanelimport java.util.Random;import spock.lang.Shared;

import com.enonic.autotests.pages.schemamanager.KindOfContentTypes;import com.enonic.autotests.pages.schemamanager.SchemaGridPage;import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.vo.schemamanger.ContentType;import com.enonic.wem.uitest.BaseGebSpec;
import com.enonic.wem.uitest.schema.cfg.FolderContentTypeCfg;import com.enonic.wem.uitest.schema.cfg.TextLineContentTypeCfg;


class ContentTypeSpec extends BaseGebSpec
{

	@Shared ContentTypeService contentTypeService = new ContentTypeService();
	
	def "Given BrowsePanel When adding Folder ContentType Then the new contentype should be listed in the table"()
	{
		given:
		go "admin"
		String folderCFG = FolderContentTypeCfg.FOLDER_CFG
		ContentType ctype = ContentType.with().name("folderctype").kind(KindOfContentTypes.CONTENT_TYPE).configuration(folderCFG).build();
		
		when:
		SchemaGridPage grid = contentTypeService.createContentType(getTestSession(), ctype, true)
		
		then:
		grid.isContentTypePresentInTable(ctype)

	}			def "Given schema BrowsePanel and exist Contentype  When Contentype editet, display-name changed  Then the Contentype whith new display-name should be listed in the table"()	{		given:		go "admin"		String folderCFG = FolderContentTypeCfg.FOLDER_CFG		ContentType ctype = ContentType.with().name("editdisplaynametest").kind(KindOfContentTypes.CONTENT_TYPE).configuration(folderCFG).build();			SchemaGridPage grid = contentTypeService.createContentType(getTestSession(), ctype, true);					when:		ContentType newContentType = ctype.cloneContentType();		String displayName = "edited" + Math.abs(new Random().nextInt());		newContentType.setDisplayNameInConfig(displayName);		contentTypeService.editContentType(getTestSession(), ctype, newContentType);			then:		grid.isContentTypePresentInTable(newContentType);		}	def "Given schema BrowsePanel and exist Contentype  When Contentype editet, name changed  Then the Contentype whith new name should be listed in the table"()	{			given:		go "admin"		String folderCFG = FolderContentTypeCfg.FOLDER_CFG		ContentType ctype = ContentType.with().name("editnametest").kind(KindOfContentTypes.CONTENT_TYPE).configuration(folderCFG).build();			SchemaGridPage grid = contentTypeService.createContentType(getTestSession(), ctype, true);				when:		ContentType newContentType = ctype.cloneContentType();		String name = "edited" + Math.abs(new Random().nextInt());		newContentType.setName(name);		contentTypeService.editContentType(getTestSession(), ctype, newContentType);				then:		grid.isContentTypePresentInTable(newContentType);			}
	

	
	def "Given BrowsePanel When adding TextLine ContentType Then the new contentype should be listed in the table"()
	{
		given:
		go "admin"
		String textLineCFG = TextLineContentTypeCfg.CFG
		ContentType ctype = ContentType.with().name("textlinectype").kind(KindOfContentTypes.CONTENT_TYPE).configuration(textLineCFG).build();
		
		when:
		contentTypeService.createContentType(getTestSession(), ctype, true)
		
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
	