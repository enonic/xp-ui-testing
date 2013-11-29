package com.enonic.autotests.services;

import java.util.List;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.cm.BaseAbstractContent;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.pages.cm.AddNewContentWizard;
import com.enonic.autotests.pages.cm.CMSpacesPage;
import com.enonic.autotests.pages.cm.ContentInfoPage;

/**
 * Service for 'Content Manager' application.
 * 
 */
public class ContentManagerService
{

	public boolean openContentManagerAppAndVerify(TestSession session)
	{
		// 1. open a 'content manager'
		CMSpacesPage cmPage = NavigatorHelper.openContentManager(session);
		boolean result = true;
		result &=cmPage.verifyTitle();
		//result &=cmPage.verifyAllControls();
		return result;
	}
	public boolean openAndVerifyAddContentWizardPage(TestSession session, String type, String ... parentNames)
	{

		// 1. open a 'content manager'
		CMSpacesPage cmPage = NavigatorHelper.openContentManager(session);
		
		//2. select a space and open the 'add content wizard' (click by 'New') 
		AddNewContentWizard wizardPage = cmPage.openAddContentWizard(type, parentNames);
		String expectedTytle = String.format(AddNewContentWizard.START_WIZARD_TITLE, type.toLowerCase());
		wizardPage.waitUntilWizardOpened(expectedTytle, 1);
		return wizardPage.verifyWizardPage(session);

	}
	
	public void doSelectContentInTable(TestSession session, List<String> contents, String ... parentNames)
	{
		// 1. open a 'content manager'
	    CMSpacesPage cmPage = NavigatorHelper.openContentManager(session);
	    cmPage.selectContentsInTable(contents, parentNames);
				
	}


	/**
	 * Selects all content in the table.
	 * @param session
	 * @return
	 */
	public int doSelectAll(TestSession session)
	{
		
		// 1. open a 'content manager'
	    CMSpacesPage cmPage = NavigatorHelper.openContentManager(session);
	    // click by "Select All" link and get a number of selected rows:
	    return cmPage.doSelectAll();
			
	}

	
	/**
	 * Selects a parent folder or space in the table of content and adds new content.
	 * 
	 * @param session
	 * @param newcontent
	 * @param isCloseWizard
	 * @param parentNames
	 * @return
	 */
	public Page addNewContent(TestSession session, BaseAbstractContent newcontent, boolean isCloseWizard,String ... parentNames)
	{
		// 1. open a 'content manager'		
		CMSpacesPage cmPage = NavigatorHelper.openContentManager(session);
		cmPage.doAddContent(newcontent, isCloseWizard, parentNames);
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
	 * @param parentNames
	 * @return
	 */
	public boolean doOpenContentVerifyPage(TestSession session, BaseAbstractContent content, String ... parentNames)
	{
		// 1. open a 'content manager'
		CMSpacesPage cmPage = NavigatorHelper.openContentManager(session);
		ContentInfoPage contentInfoPage = cmPage.doOpenContent(content,parentNames);
		
		boolean result = contentInfoPage.verifyContentInfoPage(content, parentNames);
		result &= contentInfoPage.verifyToolbar(content);

		return result;
	}
	
	/**
	 * Finds a content, open preview for this content and click by "Edit" and updates.
	 * 
	 * @param session
	 * @param contentToEdit
	 * @param newcontent
	 * @param parentNames
	 * @return
	 */
	public CMSpacesPage doOpenContentAndEdit(TestSession session,  BaseAbstractContent contentToEdit,BaseAbstractContent newcontent, String ... parentNames)
	{
		// 1. open a 'content manager'
		CMSpacesPage cmPage = NavigatorHelper.openContentManager(session);
		ContentInfoPage contentInfoPage = cmPage.doOpenContent( contentToEdit, parentNames );
		contentInfoPage.doEditContentAndCloseWizard(contentToEdit.getDisplayName(), newcontent);
		return cmPage;
	}
	
	/**
	 * Finds a content, open preview for this content and click by "Delete" and confirm.
	 * @param session
	 * @param contentToDelete
	 * @param parentNames
	 * @return
	 */
	public CMSpacesPage doOpenContentAndDelete(TestSession session, BaseAbstractContent contentToDelete, String ... parentNames)
	{
		// 1. open a 'content manager'
		CMSpacesPage cmPage = NavigatorHelper.openContentManager(session);
		ContentInfoPage contentInfoPage = cmPage.doOpenContent(contentToDelete, parentNames);
		contentInfoPage.doDeleteContentAndClosePreviewPage(contentToDelete.getDisplayName());
		return cmPage;
	}
	

	/**
	 * Delete content from space.
	 * 
	 * @param session
	 * @param contents
	 * @param parentNames array of content-names that are parent for new content. 
	 * @return
	 */
	public CMSpacesPage deleteContentFromSpace(TestSession session,  List<BaseAbstractContent> contents, String ... parentNames)
	{
		// 1. open a 'content manager'
		CMSpacesPage cmPage = NavigatorHelper.openContentManager(session);

		cmPage.doDeleteContent(contents, parentNames);
		return cmPage;
	}

	/**
	 * Finds a content and update it. 
	 * 
	 * @param session
	 * @param contentToUpdate
	 * @param newContent
	 * @param parentNames
	 * @return
	 */
	public CMSpacesPage updateContent(TestSession session, BaseAbstractContent contentToUpdate, BaseAbstractContent newContent,String ... parentNames)
	{

		CMSpacesPage cmPage = NavigatorHelper.openContentManager(session);
		cmPage.doUpdateContent(contentToUpdate, newContent,parentNames);
		return cmPage;
	}
}
