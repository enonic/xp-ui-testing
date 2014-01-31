package com.enonic.wem.selenium.tests.schema;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.pages.schemamanager.SchemaGridPage;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.testdata.TestDataConvertor;
import com.enonic.autotests.testdata.schemamanger.ContentTypeXml;
import com.enonic.autotests.vo.schemamanger.ContentType;
import com.enonic.wem.selenium.dataproviders.SchemaTestsProvider;
import com.enonic.wem.selenium.tests.BaseTest;

public class BrowsePanel extends BaseTest
{
	private ContentTypeService contentTypeService = new ContentTypeService();


	@Test(description = "create new Relationships", dataProvider = "addRelationship", dataProviderClass = SchemaTestsProvider.class)
	public void create_relationship_type(ContentTypeXml xmlData)
	{
		logger.info("Started ###  create a relationship");
		ContentType relationship = TestDataConvertor.convertXmlDataToContentType(xmlData);
		String relationshipName = "relationship" + Math.abs(new Random().nextInt());
		relationship.setName(relationshipName);
		relationship.setDisplayNameInConfig("testrelationship");
		//1. create a relationship type
		SchemaGridPage schemasPage = (SchemaGridPage) contentTypeService.createContentType(getTestSession(), relationship, true);
		
		//2. verify is present
		boolean isPresent = schemasPage.isContentTypePresentInTable(relationship);
		Assert.assertTrue(isPresent, String.format("relationship with name: %s was not found in the table, but it should be created!", relationship.getName()));
		logger.info("Finished $$$  create a relationship");
	}
	
	@Test(description = "click by 'Delete' button in toolbar and delete a relationship", dataProvider = "addRelationship", dataProviderClass = SchemaTestsProvider.class)
	public void given_existing_selected_relationship_type_when_clicking_Delete_then_relationship_type_is_removed_from_list(ContentTypeXml xmlData)
	{
		logger.info("Started ###  delete a relationship");
		ContentType relationship = TestDataConvertor.convertXmlDataToContentType(xmlData);
		String relationshipName = "relationship" + Math.abs(new Random().nextInt());
		relationship.setName(relationshipName);
		relationship.setDisplayNameInConfig("testrelationship");
		SchemaGridPage schemasPage = (SchemaGridPage) contentTypeService.createContentType(getTestSession(), relationship, true);
		
		contentTypeService.deleteContentType(getTestSession(), relationship);
		
		boolean isPresent = schemasPage.isContentTypePresentInTable(relationship);
		Assert.assertFalse(isPresent, String.format("relationship with name: %s was found in the table, but it should be deleted!", relationship.getName()));
		logger.info("Finished $$$  delete a relationship");
	}

}
