package com.enonic.wem.selenium.schemamanger;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.pages.schemamanager.SchemaGridPage;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.testdata.TestDataConvertor;
import com.enonic.autotests.testdata.schemamanger.ContentTypeXml;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.schemamanger.ContentType;
import com.enonic.wem.selenium.BaseTest;
import com.enonic.wem.selenium.dataproviders.SchemaManagerTestsProvider;

public class ContentTypeTest extends BaseTest
{
	private ContentTypeService contentTypeService = new ContentTypeService();
	private final String TEST_CONTENTYPE_KEY = "delete_ctype_key";

	@Test(description = "create new content-types of several types", dataProvider = "addContentType", dataProviderClass = SchemaManagerTestsProvider.class)
	public void test_create_contenttype(ContentTypeXml xmlData)
	{
		ContentType contentType = TestDataConvertor.convertXmlDataToContentType(xmlData);
		String contentTypeName = "cttype" + Math.abs(new Random().nextInt());
		contentType.setName(contentTypeName);
		SchemaGridPage schemasPage = (SchemaGridPage) contentTypeService.createContentType(getTestSession(), contentType, true);
		boolean isPresent = schemasPage.isContentTypePresentInTable(contentType);
		Assert.assertTrue(isPresent, String.format("contentType with name: %s was not found in the table", contentType.getName()));
		getTestSession().put(TEST_CONTENTYPE_KEY, contentType);

	}

    @Test(description = "create new content-type and edit it", dataProvider= "editContentType",dataProviderClass = SchemaManagerTestsProvider.class)
	public void test_change_dispalyname_in_config(ContentTypeXml xmlData)
	{
		ContentType ct = TestDataConvertor.convertXmlDataToContentType(xmlData);

		String contentTypeName = "cttoedit" + Math.abs(new Random().nextInt());
		ct.setName(contentTypeName);
		SchemaGridPage schemasPage = (SchemaGridPage) contentTypeService.createContentType(getTestSession(), ct, true);
		boolean isPresent = schemasPage.isContentTypePresentInTable(ct);
		Assert.assertTrue(isPresent, String.format("contentType with name: %s  and displayName %s was not found in the table", ct.getName(), ct.getDisplayNameFromConfig()));
		ContentType newContentType = ct.cloneContentType();

		newContentType.setDisplayNameInConfig("edited");

		schemasPage = contentTypeService.editContentType(getTestSession(), ct, newContentType);
		isPresent = schemasPage.isContentTypePresentInTable(newContentType);
		Assert.assertTrue(isPresent, String.format("contentType with name: %s  and new  displayName %s was not  found in the table",newContentType.getName(), newContentType.getDisplayNameFromConfig()));
		isPresent = schemasPage.isContentTypePresentInTable(ct);
		Assert.assertFalse(isPresent,String.format("Display name have changed, but contentType with name: %s  and displayName %s was found in the table", ct.getName(),
						ct.getDisplayNameFromConfig()));
	}

	@Test(dependsOnMethods = "test_create_contenttype")
	public void test_delete_contenttype()
	{
		ContentType ctype = (ContentType) getTestSession().get(TEST_CONTENTYPE_KEY);
		SchemaGridPage schemasPage = contentTypeService.deleteContentType(getTestSession(), ctype);
		logger.info("check that deleted and not present in the Grid:");
		TestUtils.getInstance().saveScreenshot(getTestSession());
		boolean isPresent = schemasPage.isContentTypePresentInTable(ctype);
		TestUtils.getInstance().saveScreenshot(getTestSession());
		Assert.assertFalse(isPresent, "content type with name :" + ctype.getName() + " should be delete, but actual is present in the grid-view");
		logger.info("FINISHED $$$  select a content type and delete it ");
	}

}
