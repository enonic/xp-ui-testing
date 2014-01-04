package com.enonic.wem.selenium.schemamanger;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.pages.schemamanager.SchemaGridPage;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.autotests.testdata.TestDataConvertor;
import com.enonic.autotests.testdata.schemamanger.ContentTypeXml;
import com.enonic.autotests.vo.schemamanger.ContentType;
import com.enonic.wem.selenium.BaseTest;
import com.enonic.wem.selenium.dataproviders.SchemaManagerTestsProvider;

public class MixinTests extends BaseTest
{
	private ContentTypeService contentTypeService = new ContentTypeService();
	private final String TEST_MIXIN_KEY  ="mixin_key";
	
	@Test(description = "create new mixin", dataProvider = "addMixin", dataProviderClass = SchemaManagerTestsProvider.class)
	public void test_create_mixin(ContentTypeXml xmlData)
	{
		logger.info("Started ### test_create_relationship, new relationship will be created: "+ xmlData.getName());
		//1.read xml data
		ContentType mixin = TestDataConvertor.convertXmlDataToContentType(xmlData);
		String mixinName = "mixin" + Math.abs(new Random().nextInt());
		mixin.setName(mixinName);
		//2. create new contenttype
		SchemaGridPage schemasPage = (SchemaGridPage) contentTypeService.createContentType(getTestSession(), mixin, true);
		logger.info("create new relationship: new relationship  type was created! name is" + mixin.getName());
		//3. check if present:
		boolean isPresent = schemasPage.isContentTypePresentInTable(mixin);
		Assert.assertTrue(isPresent, String.format("relationship with name: %s was not found in the table", mixin.getName()));
		getTestSession().put(TEST_MIXIN_KEY, mixin);
		logger.info("Finished $$$  test_create_relationship, relationship was created: "+ mixin.getName());

	}

	@Test(description = "rename a mixin", dependsOnMethods = "test_create_mixin")
	public void  test_rename_mixin_name()
	{
		logger.info("Started ### test_rename_mixin_name");
		ContentType ct = (ContentType)getTestSession().get(TEST_MIXIN_KEY);
		ContentType newMixin = ct.cloneContentType();
		String newName = "mixinrenamed"+ Math.abs(new Random().nextInt());
		newMixin.setName(newName);
        //1. edit mixin: set a new name: 
		SchemaGridPage schemasPage = contentTypeService.editContentType(getTestSession(), ct, newMixin);
		//2.check that mixin with new name present in the table
		boolean isPresent = schemasPage.isContentTypePresentInTable(newMixin);
		Assert.assertTrue(isPresent, String.format("mixin with name: %s  and new  displayName %s was not  found in the table",newMixin.getName(), newMixin.getDisplayNameFromConfig()));
		logger.info("Finished $$$  rename a name of mixin");
		ct.setName(newName);
	}
	
	@Test(description = "rename a mixin display name", dependsOnMethods = "test_rename_mixin_name")
	public void  rename_mixin_display_name()
	{
		logger.info("Started ### rename_mixin_display_name");
		ContentType ct = (ContentType)getTestSession().get(TEST_MIXIN_KEY);
		ContentType newMixin = ct.cloneContentType();
		
		String newDisplayName = "change display name test";
		//1. set a new display name:
		newMixin.setDisplayNameInConfig(newDisplayName );
        //2. edit the  mixin:
		SchemaGridPage schemasPage = contentTypeService.editContentType(getTestSession(), ct, newMixin);
		//3.check that mixin with new displayName present in the table
		boolean isPresent = schemasPage.isContentTypePresentInTable(newMixin);
		Assert.assertTrue(isPresent, String.format("mixin with name: %s  and new  displayName %s was not  found in the table",newMixin.getName(), newMixin.getDisplayNameFromConfig()));
		logger.info("Finished $$$  rename a display-name of mixin");
	}
	
	@Test(description = "delete a mixin",  dataProvider = "deleteMixin", dataProviderClass = SchemaManagerTestsProvider.class)
	public void  test_delete_mixin(ContentTypeXml xmlData)
	{
		ContentType mixinToDelete = TestDataConvertor.convertXmlDataToContentType(xmlData);
		String mixinName = "mixindelete" + Math.abs(new Random().nextInt());
		mixinToDelete.setName(mixinName);
		//1. create new mixin
		SchemaGridPage schemasPage = (SchemaGridPage) contentTypeService.createContentType(getTestSession(), mixinToDelete, true);
		//delete mixin
		contentTypeService.deleteContentType(getTestSession(), mixinToDelete);
		//3.check that mixin with  displayName not  present in the table
		boolean isPresent = schemasPage.isContentTypePresentInTable(mixinToDelete);
		Assert.assertFalse(isPresent, String.format("mixin with name: %s  and new  displayName %s was found in the table, but it should be deleted!",mixinToDelete.getName(), mixinToDelete.getDisplayNameFromConfig()));
		logger.info("Finished $$$  delete a mixin");
	}
}
