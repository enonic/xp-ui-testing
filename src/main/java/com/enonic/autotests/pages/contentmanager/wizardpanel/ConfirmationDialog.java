package com.enonic.autotests.pages.contentmanager.wizardpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.BaseModalDialog;

public class ConfirmationDialog extends BaseModalDialog
{

	public static final String YES_BUTTON_XPATH = "//div[@class='modal-dialog confirmation-dialog']//div[@class='button-row']//button[text()='Yes']";
	
	private final String TITLE_XPATH = "//div[@class='modal-dialog confirmation-dialog']//div[@class='dialog-header' and contains(.,'Confirmation')]";
	
	@FindBy(xpath = YES_BUTTON_XPATH)
	private WebElement yesButton;

	/**
	 * The constructor
	 * 
	 * @param session
	 */
	public ConfirmationDialog( TestSession session)
	{
		super(session);
	}
	
	public boolean verifyIsOpened()
	{
		return waitUntilVisibleNoException(By.xpath(TITLE_XPATH), 2);
	}

	public boolean verifyIsClosed()
	{
		return waitElementNotVisible(By.xpath(TITLE_XPATH), 2);
	}

	public void doConfirm()
	{
		yesButton.click();
	}
}
