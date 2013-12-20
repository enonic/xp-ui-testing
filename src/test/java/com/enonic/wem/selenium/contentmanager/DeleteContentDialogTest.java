package com.enonic.wem.selenium.contentmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentGrid;
import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog;
import com.enonic.autotests.pages.contentmanager.browsepanel.SelectContentTypeDialog.ContentTypeName;
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent;
import com.enonic.autotests.vo.contentmanager.StructuredContent;

public class DeleteContentDialogTest extends BaseContentManagerTest
{
	private final String DELETE_CONTENT_DIALOG_KEY = "delete_content_dialog";
	@Test
	public void setup()
	{
		String name = "dialogtest"+Math.abs( new Random().nextInt() );
		StructuredContent content =  StructuredContent.builder().withName(name).withDisplayName("deletecontentdialog").withType(ContentTypeName.STRUCTURED.getValue()).build();
		content.setParentNames( new String[]{REPONAME});
		//1. add a content to the space
		ContentGrid grid = (ContentGrid)cManagerService.addContent(getTestSession(), content, true);
		Assert.assertTrue(grid.findContentInTable(content, 1), "test content was not created!");
		getTestSession().put(DELETE_CONTENT_DIALOG_KEY, content);
	}
	
    @Test(dependsOnMethods ="setup")
	public void opened_with_one_selected_content_then_one_content_is_displayed()
	{
    	List<BaseAbstractContent> contents = new ArrayList<>();
    	BaseAbstractContent content = (BaseAbstractContent)getTestSession().get(DELETE_CONTENT_DIALOG_KEY);
		contents.add(content );
		DeleteContentDialog dialog = cManagerService.selectContentClickDeleteInToolbar(getTestSession(), contents );
		boolean result = dialog.isOpened();
		Assert.assertTrue(result,"Delete content Dialog was not opened");
	}
    
    @Test(dependsOnMethods ="opened_with_one_selected_content_then_one_content_is_displayed")
   	public void delete_test_data()
   	{
       	List<BaseAbstractContent> contents = new ArrayList<>();
       	BaseAbstractContent content = (BaseAbstractContent)getTestSession().get(DELETE_CONTENT_DIALOG_KEY);
   		contents.add(content);
   		
   		
   		cManagerService.deleteContentUseToolbar(getTestSession(), contents);
   	}
}
