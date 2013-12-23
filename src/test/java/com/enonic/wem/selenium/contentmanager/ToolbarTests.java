package com.enonic.wem.selenium.contentmanager;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentGrid;
import com.enonic.autotests.pages.contentmanager.browsepanel.SelectContentTypeDialog.ContentTypeName;
import com.enonic.autotests.services.NavigatorHelper;
import com.enonic.autotests.vo.contentmanager.StructuredContent;


public class ToolbarTests extends BaseContentManagerTest
{
	private final String TOOLBAR_DELETE_CONTENT_DIALOG_KEY = "toolbar_test_delete_dialog";
	@Test
	public void setup()
	{
		String name = "toolbar"+Math.abs( new Random().nextInt() );
		StructuredContent content =  StructuredContent.builder().withName(name).withDisplayName("deletecontentdialog").withType(ContentTypeName.STRUCTURED.getValue()).build();
		content.setParentNames( new String[]{REPONAME});
		//1. add a content to the space
		ContentGrid grid = (ContentGrid)cManagerService.addContent(getTestSession(), content, true);
		Assert.assertTrue(grid.findContentInTable(content, 1), "test content was not created!");
		getTestSession().put(TOOLBAR_DELETE_CONTENT_DIALOG_KEY, content);
	}

	//@Test(dependsOnMethods ="setup")
	public void one_selected_content_clicking_Delete_displays_ContentDeleteDialog()
	{
		
	}
	
	@Test
	public void no_selected_content_then_Delete_is_disabled()
	{
		ContentGrid cmPage = NavigatorHelper.openContentManager(getTestSession());
		boolean result = cmPage.isDeleteButtonEnabled();
		Assert.assertFalse(result,"Button 'Delete' should be disabled when no one content selected");
		
		
	}
}
