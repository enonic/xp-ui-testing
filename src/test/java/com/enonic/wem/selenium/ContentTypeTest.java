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

	
}
