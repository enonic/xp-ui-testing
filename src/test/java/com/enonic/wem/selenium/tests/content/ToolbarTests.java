package com.enonic.wem.selenium.tests.content;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentGridPage;
import com.enonic.autotests.pages.contentmanager.browsepanel.SelectContentTypeDialog.ContentTypeName;
import com.enonic.autotests.services.NavigatorHelper;
import com.enonic.autotests.vo.contentmanager.StructuredContent;


public class ToolbarTests extends BaseContentManagerTest
{
	private final String TOOLBAR_DELETE_CONTENT_DIALOG_KEY = "toolbar_test_delete_dialog";
	

	//@Test(dependsOnMethods ="setup")
	//TODO is this test redundant? Because there is a test:  DeleteContentDialogTest.opened_with_one_selected_content_then_one_content_is_displayed
	public void one_selected_content_clicking_Delete_displays_ContentDeleteDialog()
	{
		
	}
	
}
