package com.enonic.wem.selenium.tests.schema;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.pages.schemamanager.SchemaGridPage;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.testdata.TestDataConvertor;
import com.enonic.autotests.testdata.schemamanger.ContentTypeXml;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.schemamanger.ContentType;
import com.enonic.wem.selenium.tests.BaseTest;
import com.enonic.wem.selenium.dataproviders.SchemaTestsProvider;

public class ContentTypeTest extends BaseTest
{
	private ContentTypeService contentTypeService = new ContentTypeService();
	
    @Test(description = "create new content-type and change displayName it", dataProvider= "changeDisplayName",dataProviderClass = SchemaTestsProvider.class)
	public void test_change_dispalyname_in_config(ContentTypeXml xmlData)
	{
    	logger.info("Started ###  rename a display-name of content-type");
    	//read a xml data
		ContentType ct = TestDataConvertor.convertXmlDataToContentType(xmlData);

		String contentTypeName = "cttoedit" + Math.abs(new Random().nextInt());
		ct.setName(contentTypeName);
		//1. create new content type.
		SchemaGridPage schemasPage = (SchemaGridPage) contentTypeService.createContentType(getTestSession(), ct, true);
		logger.info("test_change_dispalyname_in_config:new content type was created! name is" + ct.getName());
		//2.check new content
		boolean isPresent = schemasPage.isContentTypePresentInTable(ct);
		Assert.assertTrue(isPresent, String.format("contentType with name: %s  and displayName %s was not found in the table", ct.getName(), ct.getDisplayNameFromConfig()));
		ContentType newContentType = ct.cloneContentType();

		newContentType.setDisplayNameInConfig("edited");
        //3. edit content: change display name in config: 
		schemasPage = contentTypeService.editContentType(getTestSession(), ct, newContentType);
		//4.check that content type with new displayName present in the table
		isPresent = schemasPage.isContentTypePresentInTable(newContentType);
		Assert.assertTrue(isPresent, String.format("contentType with name: %s  and new  displayName %s was not  found in the table",newContentType.getName(), newContentType.getDisplayNameFromConfig()));
		logger.info("Finished $$$  rename a display-name of content-type");
	
	}



	@Test( dataProvider="changeName", dataProviderClass = SchemaTestsProvider.class)
	public void test_rename_content_type_name(ContentTypeXml xmlData) 
	{
		logger.info("STARTED ###  rename a name of content-type");
		ContentType ct = TestDataConvertor.convertXmlDataToContentType(xmlData);
		String contentTypeName = "cttochangename" + Math.abs(new Random().nextInt());
		ct.setName(contentTypeName);
		//1. create a contentype
		SchemaGridPage schemasPage = (SchemaGridPage) contentTypeService.createContentType(getTestSession(), ct, true);
		
		ContentType newContentType = ct.cloneContentType();
		String newName = "name"+ Math.abs(new Random().nextInt());
		newContentType.setName(newName);
        //2. edit content: set a new name: 
		schemasPage = contentTypeService.editContentType(getTestSession(), ct, newContentType);
		//4.check that content type with new displayName present in the table
		boolean isPresent = schemasPage.isContentTypePresentInTable(newContentType);
		Assert.assertTrue(isPresent, String.format("contentType with name: %s  and new  displayName %s was not  found in the table",newContentType.getName(), newContentType.getDisplayNameFromConfig()));
		logger.info("Finished $$$  rename a name of content-type");
		
	}
	
}
