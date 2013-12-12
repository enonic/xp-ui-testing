package com.enonic.autotests.services;

import java.util.List;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.cm.BaseAbstractContent;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.pages.cm.AddNewContentWizard;
import com.enonic.autotests.pages.cm.ContentTablePage;
import com.enonic.autotests.pages.cm.ContentInfoPage;
import com.enonic.autotests.utils.TestUtils;

/**
 * Service for 'Content Manager' application.
 * 
 */
public class ContentManagerService
{

	public boolean openContentManagerAppAndVerify(TestSession session)
	{
		// 1. open a 'content manager'
		ContentTablePage cmPage = NavigatorHelper.openContentManager(session);
		TestUtils.getInstance().saveScreenshot(session);
		boolean result = true;
		result &=cmPage.verifyTitle();
		//result &=cmPage.verifyAllControls();
		return result;
	}
	public boolean openAndVerifyAddContentWizardPage(TestSession session, String contentTypeName, String ... parentNames)
	{

		// 1. open a 'content manager'
		ContentTablePage cmPage = NavigatorHelper.openContentManager(session);
		
		//2. select a space and open the 'add content wizard' (click by 'New') 
		AddNewContentWizard wizardPage = cmPage.openAddContentWizard(contentTypeName, parentNames);
		String expectedTitle = String.format(AddNewContentWizard.START_WIZARD_TITLE, contentTypeName.toLowerCase());
		wizardPage.waitUntilWizardOpened(expectedTitle, 1);
		return wizardPage.verifyWizardPage(session);

	}
	


	/**
	 * Selects all content in the table.
	 * @param session
	 * @return
	 */
	public int doSelectAll(TestSession session)
	{
		
		// 1. open a 'content manager'
	    ContentTablePage cmPage = NavigatorHelper.openContentManager(session);
	    // click by "Select All" link and get a number of selected rows:
	    return cmPage.doSelectAll();
			
	}

	
	/**
	 * Selects a parent folder or space in the table of content and adds new content.
	 * 
	 * @param session
	 * @param newcontent
	 * @param isCloseWizard
	 * @return
	 */
	public Page addContent(TestSession session, BaseAbstractContent newcontent, boolean isCloseWizard)
	{
		// 1. open a 'content manager'		
		ContentTablePage cmPage = NavigatorHelper.openContentManager(session);
		cmPage.doAddContent(newcontent, isCloseWizard);
		if (isCloseWizard)
		{
			return cmPage;
		} else
		{
			return new AddNewContentWizard(session);
		}

	}

	/**
	 * Finds a content, open preview page and verify this page.
	 * 
	 * @param session
	 * @param content
	 * @return
	 */
	public boolean doOpenContentVerifyPage(TestSession session, BaseAbstractContent content)
	{
		// 1. open a 'content manager'
		ContentTablePage cmPage = NavigatorHelper.openContentManager(session);
		ContentInfoPage contentInfoPage = cmPage.doOpenContent(content);
		
		boolean result = contentInfoPage.verifyContentInfoPage(content);
		result &= contentInfoPage.verifyToolbar(content);

		return result;
	}
	
	/**
	 * Finds a content, open preview for this content and click by "Edit" and updates.
	 * 
	 * @param session
	 * @param contentToEdit
	 * @param newcontent
	 * @return {@link ContentTablePage} instance. Table of content.
	 */
	public ContentTablePage doOpenContentAndEdit(TestSession session,  BaseAbstractContent contentToEdit,BaseAbstractContent newcontent)
	{
		// 1. open a 'content manager'
		ContentTablePage cmPage = NavigatorHelper.openContentManager(session);
		ContentInfoPage contentInfoPage = cmPage.doOpenContent( contentToEdit );
		contentInfoPage.doEditContentAndCloseWizard(contentToEdit.getDisplayName(), newcontent);
		contentInfoPage.doCloseContentInfoView();
		cmPage.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return cmPage;
	}
	
	/**
	 * Finds a content, open preview for this content and click by "Delete" and confirm.
	 * @param session
	 * @param contentToDelete
	 * @return {@link ContentTablePage} instance. Table of content.
	 */
	public ContentTablePage doOpenContentAndDelete(TestSession session, BaseAbstractContent contentToDelete)
	{
		// 1. open a 'content manager'
		ContentTablePage cmPage = NavigatorHelper.openContentManager(session);
		ContentInfoPage contentInfoPage = cmPage.doOpenContent(contentToDelete);
		contentInfoPage.doDeleteContent(contentToDelete.getDisplayName());
		return cmPage;
	}
	

	/**
	 * Expand a space, selects a checkbox, press 'Delete' button from a toolbar and delete content from space.
	 * 
	 * @param session
	 * @param contents 
	 * @return {@link ContentTablePage} instance. Table of content.
	 */
	public ContentTablePage deleteContentFromSpace(TestSession session,  List<BaseAbstractContent> contents)
	{
		// 1. open a 'content manager'
		ContentTablePage cmPage = NavigatorHelper.openContentManager(session);
        // expand a space and selects contents, clicks by 'Delete' button from a toolbar and confirm deletion.
		cmPage.doDeleteContent(contents);
		return cmPage;
	}

	/**
	 * Finds a content in table, selects a checkbox, clicks by 'Edit' button,  update content and close a wizard. 
	 * 
	 * @param session
	 * @param contentToUpdate
	 * @param newContent
	 * @return {@link ContentTablePage} instance. Table of content.
	 */
	public ContentTablePage updateContent(TestSession session, BaseAbstractContent contentToUpdate, BaseAbstractContent newContent)
	{

		ContentTablePage cmPage = NavigatorHelper.openContentManager(session);
		cmPage.doUpdateContent(contentToUpdate, newContent);
		return cmPage;
	}
}
