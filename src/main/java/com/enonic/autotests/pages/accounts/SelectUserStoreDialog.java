package com.enonic.autotests.pages.accounts;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.CreateUserException;
import com.enonic.autotests.pages.BaseModalDialog;

/**
 * Representation of "Select User Store" dialog, this dialog appears, when button
 * New->
 * 
 */
public class SelectUserStoreDialog extends BaseModalDialog
{
	private final String HEADER_XPATH = "//div[contains(@class,'admin-window')]/div[contains(@class, 'admin-window-header') and descendant::h1[contains(.,'New User')]]";
	private String STORE_ROW = "//div[@class='admin-data-view-row']//p[contains(.,'%s')]";

	/**
	 * The constructor 
	 * 
	 * @param session
	 */
	public SelectUserStoreDialog( TestSession session )
	{
		super(session);
	}

	/**
	 * @return
	 */
	public boolean isDialogLoaded()
	{
		boolean result = true;
		result &= waitUntilVisibleNoException(By.xpath(HEADER_XPATH), 2l);
		result &= waitUntilVisibleNoException(By.xpath("//div[@class='admin-data-view-row']//p[contains(.,'userstores')]"), 2l);
		return result;
	}

	public void doSelectStore(String storeName)
	{
		String storeNameXpath = String.format(STORE_ROW, storeName);
		List<WebElement> elements = getDriver().findElements(By.xpath(storeNameXpath));
		if (elements.size() == 0)
		{
			throw new CreateUserException("store with name -" + storeName + " was not found!");
		}
		elements.get(0).click();
	}
}
