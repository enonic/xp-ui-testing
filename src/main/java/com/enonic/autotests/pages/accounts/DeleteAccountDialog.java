package com.enonic.autotests.pages.accounts;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.DeleteCMSObjectException;
import com.enonic.autotests.logger.Logger;
import com.enonic.autotests.utils.SleepWaitHelper;

public class DeleteAccountDialog
{

	private Logger logger = Logger.getLogger();
	private final String DIALOG_TITLE_XPATH = "//div[contains(@class,'admin-window-header')]//h1[contains(.,'Delete Account(s)')]";

	private final String ITEMS_TO_DELETE = "//div[contains(@class,'admin-window')]//div[@class='delete-container']//div[@class='delete-item']//h4";

	public static final String DELETE_BUTTON_XPATH = "//div[contains(@class,'admin-window')]//button[contains(@class,'x-btn-center') and descendant::span[contains(.,'Delete')]]";

	@FindBy(xpath = DELETE_BUTTON_XPATH)
	private WebElement deleteButton;

	private TestSession session;

	public DeleteAccountDialog( TestSession session )
	{
		this.session = session;
		PageFactory.initElements(session.getDriver(), this);
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
		{// session.getDriver().findElements(By.xpath("//div[contains(@class,'admin-user-info')]")).get(0).getText();
			List<WebElement> elems = session.getDriver().findElements(By.xpath(userXpath));
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
		return  SleepWaitHelper.waitUntilVisibleNoException(session.getDriver(), By.xpath(DIALOG_TITLE_XPATH), 2);
	}

	public boolean verifyIsClosed()
	{
		return SleepWaitHelper.waitsElementNotVisible(session.getDriver(), By.xpath(DIALOG_TITLE_XPATH), 2);
	}

}
