package com.enonic.autotests.pages.accounts;

import java.util.List;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.CreateUserException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.User;

/**
 * 'Accounts' application, the dashboard page.
 * 
 */
public class AccountsPage extends Application
{
	@FindBy(xpath = "//span[@class = 'x-btn-inner' and text()='Accounts']")
	private WebElement titleElement;

	@FindBy(xpath = "//div[contains(@class,'x-toolbar-item')]//button[contains(@class,'x-btn-center') and descendant::span[contains(.,'New')]]")
	private WebElement newButtonMenu;

	public static final String ACCOUNTS_TABLE_XPATH = "//table[contains(@class,'x-grid-table')]";
	@FindBy(xpath = "//div[contains(@class,'x-toolbar-item')]//button[contains(@class,'x-btn-center') and descendant::span[contains(.,'Edit')]]")
	private WebElement editButton;

	@FindBy(xpath = "//div[contains(@class,'x-toolbar-item')]//button[contains(@class,'x-btn-center') and descendant::span[contains(.,'Delete')]]")
	private WebElement deleteButton;

	@FindBy(xpath = "//div[contains(@class,'x-toolbar-item')]//button[contains(@class,'x-btn-center') and descendant::span[contains(.,'Change Password')]]")
	private WebElement changePasswordButton;

	@FindBy(xpath = "//div[contains(@class,'x-toolbar-item')]//button[contains(@class,'x-btn-center') and descendant::span[contains(.,'open')]]")
	private WebElement openButton;
	@FindBy(xpath = "//div[contains(@class,'x-toolbar-item')]//button[contains(@class,'x-btn-center') and descendant::span[contains(.,'Export')]]")
	private WebElement exportButton;

	// private static final String USER_MENU_ITEM_XPATH =
	// "//div[contains(@class,'admin-mega-menu-item')]//span[@class='x-menu-item-text' and contains(.,'User')]";
	// private static final String GROUP_MENU_ITEM_XPATH =
	// "//div[contains(@class,'admin-mega-menu-item')]//span[@class='x-menu-item-text' and contains(.,'Group')]";

	private String SYSTEMUSER_NAME_AND_DISPLAYNAME_IN_TABLE = "//table[contains(@class,'x-grid-table')]//div[@class='admin-grid-description'  and descendant::h6[contains(.,'%s')] and descendant::p[text()='system\\%s']]";
	private String CHECKBOX_ROW_CHECKER = SYSTEMUSER_NAME_AND_DISPLAYNAME_IN_TABLE + "//ancestor::td/preceding-sibling::td";

	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public AccountsPage( TestSession session )
	{
		super(session);

	}

	/**
	 * Selects user in the table and edit.
	 * 
	 * @param userToUpdate
	 * @param newUser
	 * @param isCloseWizard
	 */
	public void doEditAccount(User userToUpdate, User newUser, boolean isCloseWizard)
	{
		// 1. click by checkbox.
		String accountXpath = String.format(CHECKBOX_ROW_CHECKER, userToUpdate.getUserInfo().getName(), userToUpdate.getUserInfo().getDisplayName());
		boolean isPresentCheckbox = TestUtils.getInstance().waitUntilVisibleNoException(getSession(), By.xpath(accountXpath), 2l);
		if (!isPresentCheckbox)
		{
			throw new TestFrameworkException("user was not found! ::" + userToUpdate.getUserInfo().getName());
		}
		WebElement checkbox = getSession().getDriver().findElement(By.xpath(accountXpath));
		checkbox.click();
		// 2. click by 'Edit' and open Wizard page.

		AddNewUserWizard wizardPage = openForEditSystemUser();

		wizardPage.waitUntilWizardOpened(1);
		getLogger().info("AddNewUserWizard  was successfully opened,  username  is : " + userToUpdate.getUserInfo().getName());
		if (isCloseWizard)
		{
			wizardPage.doTypeDataSaveAndClose(getSession(), newUser, false);
		} else
		{
			wizardPage.doTypeDataAndSave(getSession(), newUser, false);
		}
		getLogger().info("System User  with name: " + userToUpdate.getUserInfo().getName() + " was suceessfully updated!");
	}

	/**
	 * @param user
	 * @param isCloseWizard
	 */
	public void createNewSystemUser(User user, boolean isCloseWizard)
	{
		AddNewUserWizard wizardPage = openNewSystemUserWizard();

		wizardPage.waitUntilWizardOpened(1);
		getLogger().info("AddNewUserWizard  was successfully opened,  username  is : " + user.getUserInfo().getName());
		if (isCloseWizard)
		{
			wizardPage.doTypeDataSaveAndClose(getSession(), user, true);
		} else
		{
			wizardPage.doTypeDataAndSave(getSession(), user, true);
		}
		getLogger().info("new System User  with name: " + user.getUserInfo().getName() + " was created!");
	}

	/**
	 * Select user in the table and delete it.
	 * 
	 * @param userToDelete
	 */
	public void doDeleteAccount(User userToDelete)
	{
		// 1. click by checkbox.
		String accountXpath = String.format(CHECKBOX_ROW_CHECKER, userToDelete.getUserInfo().getName(), userToDelete.getUserInfo().getDisplayName());
		boolean isPresentCheckbox = TestUtils.getInstance().waitUntilVisibleNoException(getSession(), By.xpath(accountXpath), 2l);
		if (!isPresentCheckbox)
		{
			throw new TestFrameworkException("user was not found! ::" + userToDelete.getUserInfo().getName());
		}
		WebElement checkbox = getSession().getDriver().findElement(By.xpath(accountXpath));
		checkbox.click();
		// 2. click by 'Delete' and open confirm dialog.
		deleteButton.click();

		DeleteAccountDialog dialog = new DeleteAccountDialog(getSession());
		boolean result = dialog.verifyIsOpened();
		if (!result)
		{
			throw new TestFrameworkException("Confirm delete space dialog was not opened!");
		}
		// 3. confirm and press the "Delete" button.
		dialog.doDelete(userToDelete.getUserInfo().getDisplayName());
		// 4. verify that dialog closed.
		dialog.verifyIsClosed();
		getLogger().info("the Account with name: " + userToDelete.getUserInfo().getName() + " was deleted!");
	}

	/**
	 * @param user
	 * @return
	 */
	public boolean checkIsUserPresentInTable(User user)
	{
		String userDescriptionXpath = String.format(SYSTEMUSER_NAME_AND_DISPLAYNAME_IN_TABLE, user.getUserInfo().getDisplayName(), user.getUserInfo()
				.getName());
		getLogger().info("Check is new created user present in the table: " + user.getUserInfo().getDisplayName());
		
		boolean result = TestUtils.getInstance().waitUntilVisibleNoException(getSession(), By.xpath(userDescriptionXpath), 2l);
		if (result)
		{
			getLogger().info(
					"Account  was found in the Table! name:" + user.getUserInfo().getName() + " displayName:" + user.getUserInfo().getDisplayName());
		} else
		{
			getLogger().info(
					"Account  was not found in the Table! name: " + user.getUserInfo().getName() + " displayName:"
							+ user.getUserInfo().getDisplayName());
		}

		return result;
	}

	/**
	 * Clicks "New" button on the 'Accounts' page, select 'User' item
	 * 
	 * @return {@link AddNewUserWizard} instance.
	 */
	public AddNewUserWizard openNewSystemUserWizard()
	{
		newButtonMenu.click();
		SelectUserStoreDialog selectDialog = new SelectUserStoreDialog(getSession());
		boolean isLoaded = selectDialog.isDialogLoaded();
		if (!isLoaded)
		{
			throw new TestFrameworkException("The SelectUserStoreDialog was not opened!");
		}
		selectDialog.doSelectStore(AddNewUserWizard.SYSTEM_STORE_NAME);
		return new AddNewUserWizard(getSession());
	}

	/**
	 * Clicks "Edit" button on the 'Accounts' page
	 * 
	 * @return {@link AddNewUserWizard} instance.
	 */
	public AddNewUserWizard openForEditSystemUser()
	{
		editButton.click();

		return new AddNewUserWizard(getSession());
	}

	public void waituntilPageLoaded(long timeout)
	{
		new WebDriverWait(getSession().getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By
				.xpath(AppConstants.APP_ACCOUNTS_FRAME_XPATH)));
	}

	public boolean isPageLoaded()
	{
		return TestUtils.getInstance().waitUntilVisibleNoException(getSession(), By.xpath(ACCOUNTS_TABLE_XPATH), 2l);
	}

	/**
	 * Representation of "Select User Store" dialog, this dialog appears, when
	 * buton New->
	 * 
	 */
	public static class SelectUserStoreDialog
	{

		private TestSession session;
		private final String HEADER_XPATH = "//div[contains(@class,'admin-window')]/div[contains(@class, 'admin-window-header') and descendant::h1[contains(.,'New User')]]";
		private String STORE_ROW = "//div[@class='admin-data-view-row']//p[contains(.,'%s')]";

		public SelectUserStoreDialog( TestSession session )
		{

			this.session = session;
		}

		public boolean isDialogLoaded()
		{
			// admin-data-view-row
			boolean result = true;
			result &= TestUtils.getInstance().waitUntilVisibleNoException(session, By.xpath(HEADER_XPATH), 2l);
			result &= TestUtils.getInstance().waitUntilVisibleNoException(session,
					By.xpath("//div[@class='admin-data-view-row']//p[contains(.,'userstores')]"), 2l);
			return result;
		}

		public void doSelectStore(String storeName)
		{
			String storeNameXpath = String.format(STORE_ROW, storeName);
			List<WebElement> elements = session.getDriver().findElements(By.xpath(storeNameXpath));
			if (elements.size() == 0)
			{
				throw new CreateUserException("store with name -" + storeName + " was not found!");
			}
			elements.get(0).click();
		}

	}

}
