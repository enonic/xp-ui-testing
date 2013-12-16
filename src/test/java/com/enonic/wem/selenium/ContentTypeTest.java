package com.enonic.wem.selenium;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.model.schemamanger.ContentType;
import com.enonic.autotests.pages.schemamanager.SchemaTablePage;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.testdata.TestDataConvertor;
import com.enonic.autotests.testdata.schemamanger.ContentTypeXml;
import com.enonic.wem.selenium.provider.SchemaManagerProvider;

public class ContentTypeTest extends BaseTest
{
	private ContentTypeService contentTypeService = new ContentTypeService();

	@Test(description = "create new content-types of several types",dataProvider= "addContentType",dataProviderClass = SchemaManagerProvider.class)
	public void createNewContentType(ContentTypeXml xmlData)
	{
		ContentType contentType = TestDataConvertor.convertXmlDataToContentType(xmlData);
		String contentTypeName = "cttype" + Math.abs(new Random().nextInt());
		contentType.setName(contentTypeName);
		SchemaTablePage schemasPage = (SchemaTablePage) contentTypeService.createContentType(getTestSession(), contentType, true);
		boolean isPresent = schemasPage.isContentTypePresentInTable(contentType);
		Assert.assertTrue(isPresent, String.format("contentType with name: %s was not found in the table", contentType.getName()));

	}

	
	@Test(description = "create new content-type and edit it", dataProvider= "editContentType",dataProviderClass = SchemaManagerProvider.class)
	public void editContentType(ContentTypeXml xmlData)
	{
		ContentType ct = TestDataConvertor.convertXmlDataToContentType(xmlData);
		
		
		String contentTypeName = "cttoedit" + Math.abs(new Random().nextInt());
		ct.setName(contentTypeName);
		SchemaTablePage schemasPage = (SchemaTablePage) contentTypeService.createContentType(getTestSession(), ct, true);
		boolean isPresent = schemasPage.isContentTypePresentInTable(ct);
		Assert.assertTrue(isPresent, String.format("contentType with name: %s  and displayName %s was not found in the table", ct.getName(), ct.getDisplayNameFromConfig()));
		ContentType newContentType = ct.cloneContentType();
		
		newContentType.setDisplayNameInConfig("edited");
		
		schemasPage = contentTypeService.editContentType(getTestSession(), ct, newContentType);
		isPresent = schemasPage.isContentTypePresentInTable(newContentType);
		Assert.assertTrue(isPresent, String.format("contentType with name: %s  and new  displayName %s was not  found in the table", newContentType.getName(), newContentType.getDisplayNameFromConfig()));
		isPresent = schemasPage.isContentTypePresentInTable(ct);
		Assert.assertFalse(isPresent, String.format("Display name have changed, but contentType with name: %s  and displayName %s was found in the table", ct.getName(), ct.getDisplayNameFromConfig()));
	}

	
}
