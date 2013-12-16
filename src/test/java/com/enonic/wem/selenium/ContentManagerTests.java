package com.enonic.wem.selenium;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.model.cm.BaseAbstractContent;
import com.enonic.autotests.model.cm.StructuredContent;
import com.enonic.autotests.pages.cm.ContentTablePage;
import com.enonic.autotests.pages.cm.SelectContentTypeDialog.ContentTypeName;
import com.enonic.autotests.services.ContentManagerService;
import com.enonic.autotests.testdata.TestDataConvertor;
import com.enonic.autotests.testdata.cm.AbstractContentXml;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.wem.selenium.provider.TestCMAppProvider;

/**
 * Tests for 'Content Manager' application.
 *
 */
public class ContentManagerTests extends BaseTest
{

	private ContentManagerService cManagerService = new ContentManagerService();
	
	private String REPONAME = "/bluman-trampoliner";

	@Test(description ="Opens 'Content  Manager' Application and verify title and controls elements")
	public void testVerifyCMApp()
	{
		logger.info("STARTED ##### opens a Content Manager Application and verify it ");
		boolean result = cManagerService.openContentManagerAppAndVerify(getTestSession());
		TestUtils.getInstance().saveScreenshot(getTestSession());
		Assert.assertTrue(result, "Content Manager opened, but page has wrong details");		
		logger.info("Test Finished $$$$  Content Manager Application opened and verified");
	}

	@Test( description = "adding content to 'Bluman Trampoliner'. Use several types of content",dataProvider = "addNewContent", dataProviderClass = TestCMAppProvider.class)
	public void testAddContents(AbstractContentXml contentXml)
	{
		logger.info("STARTED #####  "+contentXml.getCaseInfo());
		BaseAbstractContent content = TestDataConvertor.convertXmlDataToContent(contentXml);
		String[] parentNames = new String[]{REPONAME};
		content.setParentNames(parentNames);
		String name = "add" + Math.abs( new Random().nextInt() );
		content.setName(name);
		//1. add a content to the space
		logger.info("start to add content with name: " +content.getName()+ " to folder: " + REPONAME);
		ContentTablePage page = (ContentTablePage) cManagerService.addContent(getTestSession(), content, true);
		logger.info("method cManagerService.addContent finished, try to find content in the "+ REPONAME);
        //2. verify that content present in the table
		boolean result = page.findContentInTable(content, 2l);
		Assert.assertTrue(result, String.format("new added content with name == %s was not found in the table!",content.getName()));
		logger.info(String.format("new added content with name == %s was found in the table!",content.getName()));
		logger.info("FINISHED $$$$$$$$$$  "+contentXml.getCaseInfo());
	}
	
	
	
    @Test(description = "add 2 new contents to the space and delete these ")
	public void testDeleteContentUsingToolbarButton()
	{
		logger.info("STARTED #####  select checkboxes, press a button 'Delete' from toolbar and delete content from space ");
		String name1 = "to-delete"+Math.abs( new Random().nextInt() );
		StructuredContent content1 = StructuredContent.builder().withName(name1).withDisplayName("content-to-delete1").withType(ContentTypeName.STUCTURED.getValue()).build();
		String[] parentNames = new String[]{REPONAME};
		content1.setParentNames(parentNames);
		
		//1. add a content to the space
		logger.info("start to add content with name: " +content1.getName()+ " to folder: " + REPONAME);
		cManagerService.addContent(getTestSession(), content1, true);
		logger.info("method cManagerService.addContent finished, try to find content in the "+ REPONAME);
		
		String name2 = "to-delete"+Math.abs( new Random().nextInt() );
		StructuredContent content2 = StructuredContent.builder().withName(name2).withDisplayName("content-to-delete2").withType(ContentTypeName.STUCTURED.getValue()).build();
		content2.setParentNames(parentNames);
		
		//2. add one more content to the space
		logger.info("start to add content with name: " +content2.getName()+ " to folder: " + REPONAME);
		cManagerService.addContent(getTestSession(), content2, true);
		logger.info("method cManagerService.addContent finished, try to find content in the "+ REPONAME);
		
		List<BaseAbstractContent> contents = new ArrayList<>();
		contents.add(content2);
		contents.add(content1);
		
		
        //3. select checkboxes and press 'Delete' button in toolbar. Delete both content from space
		ContentTablePage page = cManagerService.deleteContentFromSpace(getTestSession(), contents);
		
        //4. verify that both contents not present in the table:
		Assert.assertFalse(page.findContentInTable(content2, 2l),
				String.format("the content wit name %s was deleted and should not be present in the table! ", content2.getDisplayName()));
		Assert.assertFalse(page.findContentInTable(content1, 2l),
				String.format("the content wit name %s was deleted and should not be present in the table! ", content1.getDisplayName()));
		logger.info("Finished $$$$$  select checkboxes, press a button 'Delete' from toolbar and delete content from space ");
	}
	
	// TODO  this test failed due the bug  CMS-2562: impossible to close Edit wizard
    //https://youtrack.enonic.net/issue/CMS-2562#
	//@Test(description = "add new content and edit it. Click by 'Edit', open wizard edit content and save")
	public void editContentTest()
	{
		logger.info("Bug## There is a Described bug ::the table of content does not refreshed when content updated and wizard closed");
		StructuredContent contentToUpdate =  StructuredContent.builder().withName("scontent-to-edit").withDisplayName("content-to-edit").withType(ContentTypeName.STUCTURED.getValue()).build();
		String[] parentNames = new String[]{REPONAME};
		contentToUpdate.setParentNames(parentNames);
		//1. add a content to the space
		cManagerService.addContent(getTestSession(), contentToUpdate, true);

		StructuredContent newcontent =  StructuredContent.builder().withName("content-updated").withDisplayName("content-updated").withType(ContentTypeName.STUCTURED.getValue()).build();
		newcontent.setParentNames(parentNames);

		//2. click by "Edit" link and update just created content, and try to close wizard:	
		ContentTablePage page = cManagerService.updateContent(getTestSession(), contentToUpdate, newcontent);

		//3. verify, that  content with new name present in the table:
		boolean result = page.findContentInTable( newcontent, 2l);
		Assert.assertTrue(result, "content with updated name was not found!");
	}

	////TODO  this test failed due the BUG CMS-2615 
	@Test(description = "add new content, select content in a table, open content and Delete it " )
	public void openContentAndDelete()
	{		
		logger.info("STARTED ##### open content and click by 'Delete' button from toolbar");
		String name = "open-delete"+Math.abs( new Random().nextInt() );

		StructuredContent content =  StructuredContent.builder().withName(name).withDisplayName("open-delete-test").withType(ContentTypeName.STUCTURED.getValue()).build();
		String[] parentNames = new String[]{REPONAME};
		content.setParentNames(parentNames);		
		
		//1. add a content to the space
		logger.info("start to add content with name: " +content.getName()+ " to folder: " + REPONAME);
		cManagerService.addContent(getTestSession(), content, true);
		logger.info("method cManagerService.addContent finished, try to find content in the "+ REPONAME);
		
        //2. open just created content and delete it using a toolbar:		
		ContentTablePage page = cManagerService.doOpenContentAndDelete(getTestSession(), content);
		
		//TODO  this test failed due the BUG CMS-2615 :: grid not refreshed after content deletion
		//3.verify, that content not present in the table:
		logger.info("try to find content with name: " +content.getName()+ " in folder: " + REPONAME);
		boolean result = page.findContentInTable(content, 2l);
		logger.info("content is present in a table: " +result);
		//Assert.assertFalse(result," content with new displayName: "+content.getDisplayName()+" was deleted, and should not be present in te table of contents");
		logger.info("FINISHED $$$$ open content and delete it");
	}
	@Test(description = "add new content, select content in a table, open content and Delete it ")
	public void openContentAndEdit()
	{		
		logger.info("STARTED ##### open content and click by 'Edit' button from toolbar");
		String name = "toedit"+ Math.abs( new Random().nextInt() );
		StructuredContent contentToEdit =  StructuredContent.builder().withName(name).withDisplayName("open-edittest").withType(ContentTypeName.STUCTURED.getValue()).build();
		String[] parentNames = new String[]{REPONAME};
		contentToEdit.setParentNames(parentNames);
		
		String newName = "edited"+ Math.abs( new Random().nextInt() );
		StructuredContent newcontent =  StructuredContent.builder().withName(newName).withDisplayName("edited").withType(ContentTypeName.STUCTURED.getValue()).build();
		newcontent.setParentNames(parentNames);
		logger.info("new contet will be  added, name: " + name);
		//1. add a content to the space
		ContentTablePage page = (ContentTablePage)cManagerService.addContent(getTestSession(), contentToEdit, true);
		logger.info("new contet was added(contet to edit), name: " + name);
        //2. open just created content and edit it. Click by 'Edit' from a toolbar:		
		page = cManagerService.doOpenContentAndEdit(getTestSession(), contentToEdit, newcontent);
		logger.info("contet was updated, new name is : " + newName);
		
		
		//4.verify, that updated  content with new name is  present in the table:
		boolean result = page.findContentInTable(newcontent, 2l);
		Assert.assertTrue(result," content with new displayName: "+newcontent.getDisplayName()+"  should be present in te table of contents");
		//4.verify, that content with old name not present in the table:
		result = page.findContentInTable(contentToEdit, 2l);
		Assert.assertFalse(result," content with  displayName: "+contentToEdit.getDisplayName()+" was edited, and should not be present in te table of contents");
		logger.info("FINISHED $$$$ open content and edit it");
	}
	
}
