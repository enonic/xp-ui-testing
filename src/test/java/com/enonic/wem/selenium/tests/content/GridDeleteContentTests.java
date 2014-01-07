package com.enonic.wem.selenium.tests.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentGridPage;
import com.enonic.autotests.pages.contentmanager.browsepanel.SelectContentTypeDialog.ContentTypeName;
import com.enonic.autotests.services.ContentManagerService.HowOpenContent;
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent;
import com.enonic.autotests.vo.contentmanager.StructuredContent;

/**
 * Tests for 'Content Manager' application.
 *
 */
public class GridDeleteContentTests extends BaseContentManagerTest
{
	@Test(description = "add 2 new contents to a space and delete all, use a button 'Delete' from toolbar ")
	public void delete_content()
	{
		logger.info("STARTED #####  select a content, press a button 'Delete' from toolbar and delete content from space ");
		String name = "delete-content"+Math.abs( new Random().nextInt() );
		StructuredContent content = StructuredContent.builder().withName(name).withDisplayName("content-to-delete").withType(ContentTypeName.STRUCTURED.getValue()).build();
		String[] parentNames = new String[]{REPONAME};
		content.setParentNames(parentNames);
		
		//1. add a content to the space
		logger.info("start to add content with name: " +content.getName()+ " to folder: " + REPONAME);
		cManagerService.addContent(getTestSession(), content, true);
		
		List<BaseAbstractContent> contents = new ArrayList<>();
		contents.add(content);
        ContentGridPage page = cManagerService.deleteContentUseToolbar(getTestSession(), contents);
		
        //4. verify that both contents not present in the table:
		Assert.assertFalse(page.findContentInTable(content, TEST_TIMEOUT),
				String.format("the content wit name %s was deleted and should not be present in the table! ", content.getDisplayName()));
		logger.info("FINISHED $$$  select a content, press a button 'Delete' from toolbar and delete content from space ");
	}
	
    @Test(description = "add 2 new contents to a space and delete all, use a button 'Delete' from toolbar ")
	public void test_add_two_contents_and_delete_use_toolbar_button()
	{
		logger.info("STARTED #####  select checkboxes, press a button 'Delete' from toolbar and delete contents from space ");
		String name1 = "to-delete"+Math.abs( new Random().nextInt() );
		StructuredContent content1 = StructuredContent.builder().withName(name1).withDisplayName("content-to-delete1").withType(ContentTypeName.STRUCTURED.getValue()).build();
		String[] parentNames = new String[]{REPONAME};
		content1.setParentNames(parentNames);
		
		//1. add a content to the space
		logger.info("start to add content with name: " +content1.getName()+ " to folder: " + REPONAME);
		cManagerService.addContent(getTestSession(), content1, true);
		
		String name2 = "to-delete"+Math.abs( new Random().nextInt() );
		StructuredContent content2 = StructuredContent.builder().withName(name2).withDisplayName("content-to-delete2").withType(ContentTypeName.STRUCTURED.getValue()).build();
		content2.setParentNames(parentNames);
		
		//2. add one more content to the space
		logger.info("start to add content with name: " +content2.getName()+ " to folder: " + REPONAME);
		cManagerService.addContent(getTestSession(), content2, true);
		
		List<BaseAbstractContent> contents = new ArrayList<>();
		contents.add(content2);
		contents.add(content1);
		
		logger.info("start to delete contents with from: " + REPONAME);
        //3. select checkboxes and press 'Delete' button in toolbar. Delete both content from space
		ContentGridPage page = cManagerService.deleteContentUseToolbar(getTestSession(), contents);
		
        //4. verify that both contents not present in the table:
		Assert.assertFalse(page.findContentInTable(content2, TEST_TIMEOUT),
				String.format("the content wit name %s was deleted and should not be present in the table! ", content2.getDisplayName()));
		Assert.assertFalse(page.findContentInTable(content1, TEST_TIMEOUT),
				String.format("the content wit name %s was deleted and should not be present in the table! ", content1.getDisplayName()));
		
		logger.info("Finished $$$  select checkboxes, press a button 'Delete' from toolbar and delete content from space ");
	}
		

	@Test(description = "add new content, select content in a table, click by 'open' button and Delete it ")
	public void test_open_content_and_delete()
	{		
		logger.info("STARTED ##### Add new content, select content in a table, click by 'open' button and Delete it ");
		
		String name = "open-delete"+Math.abs( new Random().nextInt() );
		StructuredContent content = StructuredContent.builder().withName(name).withDisplayName("content-to-delete").withType(ContentTypeName.STRUCTURED.getValue()).build();
		content.setParentNames(new String[]{REPONAME});
		
		//1. add a content to the space
		cManagerService.addContent(getTestSession(), content, true);
		logger.info("method cManagerService.addContent finished, contentName is: "+ content.getName());
		
        //2. open just created content and delete it using a toolbar:		
		ContentGridPage page = cManagerService.doOpenContentAndDelete(getTestSession(), content, HowOpenContent.TOOLBAR);
		
		//3.verify, that content not present in the table:
		logger.info("try to find content with name: " +content.getName()+ " in folder: " + REPONAME);
		boolean result = page.findContentInTable(content, TEST_TIMEOUT);
		logger.info("content is present in a table: " +result);
		Assert.assertFalse(result," content with new displayName: "+content.getDisplayName()+" was deleted, and should not be present in the table of contents");
		
		logger.info("FINISHED $$$ open content and delete it");
	}
	
	
}
