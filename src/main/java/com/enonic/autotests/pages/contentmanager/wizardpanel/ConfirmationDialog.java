package com.enonic.autotests.pages.contentmanager.wizardpanel;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.utils.SleepWaitHelper;

public class ConfirmationDialog
{

	public static final String YES_BUTTON_XPATH = "//div[@class='modal-dialog confirmation-dialog']//div[@class='button-row']//button[text()='Yes']";
	
	private final String TITLE_XPATH = "//div[@class='modal-dialog confirmation-dialog']//div[@class='dialog-header' and contains(.,'Confirmation')]";
	
	private TestSession session;
	
	@FindBy(xpath = YES_BUTTON_XPATH)
	private WebElement yesButton;

	/**
	 * The constructor
	 * 
	 * @param session
	 */
	public ConfirmationDialog( TestSession session)
	{
		this.session = session;
		PageFactory.initElements(session.getDriver(), this);
	}
	
	public boolean verifyIsOpened()
	{
		return SleepWaitHelper.waitUntilVisibleNoException(session.getDriver(), By.xpath(TITLE_XPATH), 2);
	}

	public boolean verifyIsClosed()
	{
		return SleepWaitHelper.waitsElementNotVisible(session.getDriver(), By.xpath(TITLE_XPATH), 2);
	}

	public void doConfirm()
	{
		yesButton.click();
	}
}
