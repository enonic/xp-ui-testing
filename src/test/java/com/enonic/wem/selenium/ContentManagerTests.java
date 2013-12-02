package com.enonic.wem.selenium;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.model.cm.StructuredContent;
import com.enonic.autotests.pages.cm.CMSpacesPage;
import com.enonic.autotests.pages.cm.SelectContentTypeDialog.ContentTypes;
import com.enonic.autotests.services.ContentManagerService;

/**
 * 
 *
 */
public class ContentManagerTests extends BaseTest
{

	private ContentManagerService cManagerService = new ContentManagerService();
	
	private String repoName = "Bluman Trampoliner";

	@Test(description ="Opens 'Content  Manager' Application and verify title and controls elements")
	public void testVerifyCMApp()
	{
		boolean result = cManagerService.openContentManagerAppAndVerify(getTestSession());
		Assert.assertTrue(result);
	}

	
	@Test(description="add content to space, select a space in the table, click by link 'New' from the toolbar and add new child content")
	public void testAddNewContent()
	{
		String name = "open-addnew" + Math.abs( new Random().nextInt() );
		String displayName = "addcontent";
		String[] parentNames = new String[]{repoName};
		StructuredContent contentToAdd = StructuredContent.with().name(name).displayName(displayName).type(ContentTypes.STUCTURED).parents(parentNames).build();// new StructuredContent();
		
		//1. add a content to the space
		CMSpacesPage page =(CMSpacesPage)cManagerService.addNewContent(getTestSession(), contentToAdd, true);
		boolean result = page.findContentInTable(contentToAdd, 2l);

		Assert.assertTrue(result," new content with new displayName: "+contentToAdd.getDisplayName()+" should be present in the table of contents");
		logger.info(String.format("new content %s was successfully added to the content with name %s",contentToAdd.getName(), contentToAdd.getName()));
	}
}
