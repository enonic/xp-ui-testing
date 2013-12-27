package com.enonic.wem.selenium.contentmanager;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentGridPage;
import com.enonic.autotests.pages.contentmanager.browsepanel.SelectContentTypeDialog.ContentTypeName;
import com.enonic.autotests.testdata.TestDataConvertor;
import com.enonic.autotests.testdata.cm.AbstractContentXml;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent;
import com.enonic.autotests.vo.contentmanager.StructuredContent;
import com.enonic.wem.selenium.dataproviders.ContentManagerTestsProvider;

public class GridAddEditContenetTets extends BaseContentManagerTest
{

	@Test(description = "Opens 'Content  Manager' Application and verify title and controls elements")
	public void test_open_cm_and_check_title()
	{

		logger.info("STARTED ##### opens a Content Manager Application and verify it ");
		boolean result = cManagerService.openContentManagerAppAndVerify(getTestSession());

		Assert.assertTrue(result, "Content Manager opened, but page has wrong details");
		logger.info("Test Finished $$$$  Content Manager Application opened and verified");
	}

	@Test(description = "adding content to 'Bluman Trampoliner'. Use several types of content", dataProvider = "addNewContent", dataProviderClass = ContentManagerTestsProvider.class)
	public void test_add_content_to_folder(AbstractContentXml contentXml)
	{
		logger.info("STARTED #####  " + contentXml.getCaseInfo());
		BaseAbstractContent content = TestDataConvertor.convertXmlDataToContent(contentXml);
		content.setParentNames(new String[] { REPONAME });
		String displayName = contentXml.getContentType().toLowerCase() + Math.abs(new Random().nextInt());
		content.setDisplayName(displayName);

		// 1. add a content to the space
		logger.info("start to add content with name: " + content.getName() + " to folder: " + REPONAME);
		ContentGridPage page = (ContentGridPage) cManagerService.addContent(getTestSession(), content, true);
		logger.info("method cManagerService.addContent finished, try to find content in the " + REPONAME);

		// 2. verify that content present in the table
		boolean result = page.findContentInTable(content, TEST_TIMEOUT);
		TestUtils.getInstance().saveScreenshot(getTestSession());
		Assert.assertTrue(result, String.format("new added content with name == %s was not found in the table!", content.getName()));
		logger.info(String.format("new added content with name == %s was found in the table!", content.getName()));
		logger.info("FINISHED $$$$$$  " + contentXml.getCaseInfo());
	}

	@Test(description = "add new content, select content in a table, open this content and Delete it ")
	public void test_open_content_rename_content_name()
	{
		logger.info("STARTED ##### test_open_content_rename_content_name: open content-info page and click by 'Edit' button from toolbar");
		String name = "toedit" + Math.abs(new Random().nextInt());
		StructuredContent contentToEdit = StructuredContent.builder().withName(name).withDisplayName("open-edittest")
				.withType(ContentTypeName.STRUCTURED.getValue()).build();
		String[] parentNames = new String[] { REPONAME };
		contentToEdit.setParentNames(parentNames);

		String newName = "edited" + Math.abs(new Random().nextInt());
		StructuredContent newcontent = StructuredContent.builder().withName(newName).withDisplayName("edited").withType(ContentTypeName.STRUCTURED.getValue()).build();
		newcontent.setParentNames(parentNames);
		logger.info("new contet will be  added, name: " + name);
		// 1. add a content to the space
		ContentGridPage page = (ContentGridPage) cManagerService.addContent(getTestSession(), contentToEdit, true);
		logger.info("new contet was added(contet to edit), name: " + name);
		// 2. open just created content and edit it. Click by 'Edit' from a toolbar:
		page = cManagerService.doOpenContentAndEdit(getTestSession(), contentToEdit, newcontent);
		logger.info("contet was updated, new name is : " + newName);

		// 4.verify, that updated content with new name is present in the table:
		boolean result = page.findContentInTable(newcontent, TEST_TIMEOUT);
		Assert.assertTrue(result, " content with new displayName: " + newcontent.getDisplayName() + "  should be present in te table of contents");
		// 4.verify, that content with old name not present in the table:
		result = page.findContentInTable(contentToEdit, TEST_TIMEOUT);
		Assert.assertFalse(result, " content with  displayName: " + contentToEdit.getDisplayName()+ " was edited, and should not be present in te table of contents");
		logger.info("FINISHED $$$$ open content and edit it");
	}

	@Test(description = "Add new content and edit it. select a content, click by 'Edit' button , open wizard edit content and save")
	public void test_rename_content_display_name()
	{
		logger.info("STARTED ### test_rename_content_display_name: Select a content, click by 'Edit' button , open wizard edit content and save");
		String name = "content-to-edit"+Math.abs( new Random().nextInt() );
		StructuredContent contentToUpdate =  StructuredContent.builder().withName(name).withDisplayName("content-to-edit").withType(ContentTypeName.STRUCTURED.getValue()).build();
		String[] parentNames = new String[]{REPONAME};
		contentToUpdate.setParentNames(parentNames);
		//1. add a content to the space
		cManagerService.addContent(getTestSession(), contentToUpdate, true);

		StructuredContent newcontent =  StructuredContent.builder().withName(name).withDisplayName("content-updated").withType(ContentTypeName.STRUCTURED.getValue()).build();
		newcontent.setParentNames(parentNames);

		//2. click by "Edit" link and update just created content, and try to close wizard:	
		ContentGridPage page = cManagerService.updateContent(getTestSession(), contentToUpdate, newcontent);

		//3. verify, that  content with new name present in the table:
		boolean result = page.findContentInTable( newcontent, TEST_TIMEOUT);
		Assert.assertTrue(result, "content with updated name was not found!");
		logger.info("FINISHED $$$$ test_rename_content_display_name");
	}
	//@Test(description = "Open add(edit) content wizard and verify it")
	public void test_open_edit_content_wizard()
	{
		logger.info("Open add(edit) content wizard and verify it");
		
		
		String[] parentNames = new String[]{REPONAME};

		//2. click by "Edit" link and update just created content, and try to close wizard:	
		//boolean result = cManagerService.openAndVerifyAddContentWizardPage(getTestSession(), content);

		
		//Assert.assertTrue(result, "content with updated name was not found!");
	}
}
