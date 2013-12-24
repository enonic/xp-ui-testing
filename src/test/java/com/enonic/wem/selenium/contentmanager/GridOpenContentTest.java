package com.enonic.wem.selenium.contentmanager;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.pages.contentmanager.browsepanel.SelectContentTypeDialog.ContentTypeName;
import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage;
import com.enonic.autotests.vo.contentmanager.StructuredContent;

public class GridOpenContentTest extends BaseContentManagerTest
{
    private final String TEST_OPEN_CONTENT_KEY  ="open_content_test";
    
	@Test
	public void setup()
	{
		String name = "open-test"+Math.abs( new Random().nextInt() );
		StructuredContent content =  StructuredContent.builder().withName(name).withDisplayName("open-test").withType(ContentTypeName.STRUCTURED.getValue()).build();
		String[] parentNames = new String[]{REPONAME};
		content.setParentNames(parentNames);		
		
		//1. add a content to the space
		logger.info("start to add content with name: " +content.getName()+ " to folder: " + REPONAME);
		cManagerService.addContent(getTestSession(), content, true);
		getTestSession().put(TEST_OPEN_CONTENT_KEY, content);
		
	}
	@Test(description = "add new content, select content in a table, click by 'open' button and Delete it " )
	public void test_open_content_use_toolbar()
	{	
		StructuredContent content = (StructuredContent)getTestSession().get(TEST_OPEN_CONTENT_KEY);
		ItemViewPanelPage contentInfoPage = cManagerService.doOpenContentUseToolbar(getTestSession(), content);
		boolean result =contentInfoPage.verifyContentInfoPage(content);
		Assert.assertTrue(result, "expected content-info and actal are not equals!");
		
	}
	//@Test(description = "open a context, use a context menu " ,dependsOnMethods ="setup")
	public void test_open_content_use_context_menu()
	{	
		StructuredContent content = (StructuredContent)getTestSession().get(TEST_OPEN_CONTENT_KEY);
		ItemViewPanelPage contentInfoPage = cManagerService.doOpenContentUseContextMenu(getTestSession(), content);
		boolean result =contentInfoPage.verifyContentInfoPage(content);
		Assert.assertTrue(result, "expected content-info and actal are not equals!");
	}
}
