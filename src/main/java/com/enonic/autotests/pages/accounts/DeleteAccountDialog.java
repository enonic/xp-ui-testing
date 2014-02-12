package com.enonic.autotests.pages.accounts;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.DeleteCMSObjectException;
import com.enonic.autotests.pages.BaseModalDialog;

public class DeleteAccountDialog extends BaseModalDialog
{

	private final String DIALOG_TITLE_XPATH = "//div[contains(@class,'admin-window-header')]//h1[contains(.,'Delete Account(s)')]";

	private final String ITEMS_TO_DELETE = "//div[contains(@class,'admin-window')]//div[@class='delete-container']//div[@class='delete-item']//h4";

	public static final String DELETE_BUTTON_XPATH = "//div[contains(@class,'admin-window')]//button[contains(@class,'x-btn-center') and descendant::span[contains(.,'Delete')]]";

	@FindBy(xpath = DELETE_BUTTON_XPATH)
	private WebElement deleteButton;

	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public DeleteAccountDialog( TestSession session )
	{
		super(session);
	}

	/**
	 * Checks items to delete and click "Delete" button. throws
	 * DeleteSpaceException, if list of names for deleting does not contains
	 * expected names.
	 */
	public void doDelete(String displaynameToDelete)
	{
		String userXpath = String.format("//div[contains(@class,'admin-user-info')]//h2[text()='%s']", displaynameToDelete);
		if (displaynameToDelete != null)
		{
			List<WebElement> elems = findElements(By.xpath(userXpath));
			if (elems.size() > 0)
			{
				WebElement accountNameTodelete = elems.get(0);
				if (!accountNameTodelete.getText().equals(displaynameToDelete))
				{
					throw new DeleteCMSObjectException("account's display-name on the DeleteDialog not equals with expected display-name");
				}
			}

		} else
		{
			// TODO verify that message: 'Are you sure you want to delete the
			// selected 3 items?' correctly displayed.
			// BUG : wrong number of accounts(not refreshed)
		}

		// boolean result = actual.equals(spacesToDelete);
		// if (!result) {
		// logger.error("list of names in the dialog-window are not as expected!",
		// session);
		// throw new
		// DeleteSpaceException("list of names in the dialog-window are not equals with expected list of names!");
		// }
		// WebElement deleteButton =
		// session.getDriver().findElement(By.xpath(DELETE_BUTTON_XPATH));
		deleteButton.click();
	}

	/**
	 * Checks that 'DeleteAccountDialog' is opened.
	 * 
	 * @return true if dialog opened, otherwise false.
	 */
	public boolean verifyIsOpened()
	{
		return waitUntilVisibleNoException(By.xpath(DIALOG_TITLE_XPATH), 2);
	}

	public boolean verifyIsClosed()
	{
		return waitElementNotVisible(By.xpath(DIALOG_TITLE_XPATH), 2);
	}

}
