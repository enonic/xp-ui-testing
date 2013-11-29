package com.enonic.autotests.services;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.Space;
import com.enonic.autotests.pages.spaceadmin.AddNewSpaceWizard;
import com.enonic.autotests.pages.spaceadmin.SpaceAdminPage;

/**
 * Service for 'Space Admin' application.
 * 
 */
public class SpaceAdminService
{

	/**
	 * Navigates to space-admin application and create a new space.
	 * 
	 * @param testSession
	 * @param space
	 * @param isCloseWizard
	 * @return
	 */
	public SpaceAdminPage createNewSpace(TestSession testSession, Space space, boolean isCloseWizard)
	{
		SpaceAdminPage spacesPage = NavigatorHelper.openSpaceAdmin(testSession);
		spacesPage.addNewSpace(space, isCloseWizard);
		return spacesPage;

	}

	/**
	 * Navigates to space-admin application and update a space.
	 * 
	 * @param testSession
	 * @param spaceToEdit
	 * @param space
	 * @param isCloseWizard
	 * @return
	 */
	public SpaceAdminPage updateSpace(TestSession testSession, Space spaceToEdit, Space space, boolean isCloseWizard)
	{
		SpaceAdminPage spacesPage = NavigatorHelper.openSpaceAdmin(testSession);

		spacesPage.doUpdateSpace(spaceToEdit, space, isCloseWizard);
		return new SpaceAdminPage(testSession);

	}

	/**
	 * Navigates to space-admin application select a space in the table and
	 * delete it.
	 * 
	 * @param testSession
	 * @param spaceToDelete
	 * @return
	 */
	public SpaceAdminPage deleteSpace(TestSession testSession, Space spaceToDelete)
	{
		SpaceAdminPage spacesPage = NavigatorHelper.openSpaceAdmin(testSession);

		spacesPage.doDeleteSpace(spaceToDelete);
		return spacesPage;
	}

	/**
	 * When save o update wizard was opened, so 'Red Circle' with number 1
	 * should be present on the Home page. <br>
	 * 
	 * @param testSession
	 */
	public void verifyRedCircleOnHomePage(TestSession testSession)
	{
		SpaceAdminPage page = NavigatorHelper.openSpaceAdmin(testSession);

		AddNewSpaceWizard wizard = page.openNewSpaceWizard();
		wizard.verifyRedCircleOnHomePage(AddNewSpaceWizard.START_TITLE);
	}

	/**
	 * Checks all elements on the page, when save or update Space wizard opened.
	 * 
	 * @param testSession
	 * @param spaceTitle
	 * @return
	 */
	public boolean openAndVerifySpaceWizardPage(TestSession testSession, String spaceTitle)
	{
		SpaceAdminPage page = NavigatorHelper.openSpaceAdmin(testSession);

		AddNewSpaceWizard wizard = page.openNewSpaceWizard();
		wizard.waitUntilWizardOpened( spaceTitle, 1);
		return wizard.verifyAllEmptyFields();

	}

}
