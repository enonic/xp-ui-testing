package com.enonic.autotests.services;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.accounts.AccountsPage;
import com.enonic.autotests.pages.accounts.AddNewUserWizard;
import com.enonic.autotests.vo.SystemUser;

/**
 * Service for 'Accounts' application.
 * 
 */
public class AccountService
{

	/**
	 * @param session
	 * @param userToUpdate
	 *            this user will b e updated.
	 * @param newSysUser
	 * @param isCloseWizard
	 *            if true, therefore wizard should be closed.
	 * @return {@link AccountsPage} instance. This page contains table with
	 *         users and groups.
	 */
	public AccountsPage updateSystemUser(TestSession session, SystemUser userToUpdate, SystemUser newSysUser, boolean isCloseWizard)
	{
		AccountsPage accountsPage = NavigatorHelper.openAccounts(session);

		accountsPage.doEditAccount(userToUpdate, newSysUser, isCloseWizard);
		return new AccountsPage(session);
	}

	/**
	 * @param session
	 * @param user
	 *            this user will be deleted.
	 * @param isCloseWizard
	 *            @param isCloseWizard if true, therefore wizard should be
	 *            closed.
	 * @return {@link AccountsPage} instance. This page contains table with
	 *         users and groups.
	 */
	public AccountsPage deleteSystemUser(TestSession session, SystemUser user, boolean isCloseWizard)
	{

		AccountsPage accountsPage = NavigatorHelper.openAccounts(session);
		accountsPage.doDeleteAccount(user);

		return new AccountsPage(session);
	}

	/**
	 * Creates new 'System User'
	 * 
	 * @param session
	 * @param user
	 *            {@link SystemUser} instance.
	 * @param isCloseWizard
	 *            if true, so Wizard will be closed after saving user info.
	 * @return {@link AccountsPage} instance. This page contains table with
	 *         users and groups.
	 */
	public AccountsPage createSystemUser(TestSession session, SystemUser user, boolean isCloseWizard)
	{

		AccountsPage accountsPage = NavigatorHelper.openAccounts(session);
		accountsPage.createNewSystemUser(user, isCloseWizard);
		return new AccountsPage(session);
	}

	/**
	 * Verify all elements on the 'add new user wizard'
	 * 
	 * @param testSession
	 * @param wizardTitle
	 *            This string should be present near the 'Red Circle'
	 * @return true if all elements displayed on the wizard, otherwise false.
	 */
	public boolean openAndVerifySystemUserWizardPage(TestSession testSession)
	{
		AccountsPage accountspage = NavigatorHelper.openAccounts(testSession);

		AddNewUserWizard wizard = accountspage.openNewSystemUserWizard();
		wizard.waitUntilWizardOpened(  1);
		return wizard.verifyAllEmptyFields(testSession);
	}

	/**
	 * When save o update wizard was opened, so 'Red Circle' with number 1
	 * should be present on the Home page. <br>
	 * 
	 * 
	 * @param testSession
	 */
	public void verifyRedCircleOnHomePage(TestSession testSession)
	{
		AccountsPage accountspage = NavigatorHelper.openAccounts(testSession);

		AddNewUserWizard wizard = accountspage.openNewSystemUserWizard();
		wizard.verifyRedCircleOnHomePage(AddNewUserWizard.PAGE_TITLE);
	}

}
