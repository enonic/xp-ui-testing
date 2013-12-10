package com.enonic.autotests.pages.cm;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.DeleteCMSObjectException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.cm.BaseAbstractContent;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.utils.TestUtils;

public class ContentInfoPage extends Page
{
	
	public static  String RED_CIRCLE_XPATH = "//span[@class='tabcount' and contains(.,'%s')]";

	public static  String OBJECT_NAME_XPATH = "//span[@class='label' and contains(.,'%s')]";
	
	public static final String HOME_BUTTON_XPATH = "//div[contains(@class,'x-btn start-button')]";

	private static  String H1_NAME_XPATH = "//div[contains(@class,'x-panel-body-default-closable')]//div[contains(@class,'admin-detail-header')]/h1[contains(.,'%s')]";
	private static  String SPAN_FULL_NAME_XPATH = "//div[contains(@class,'x-panel-body-default-closable')]//div[contains(@class,'admin-detail-header')]//span[contains(.,'%s')]";
	
	private static final String TOOLBAR_EDIT_BUTTON_XPATH = "//div[contains(@class,'panel item-view-panel')]//div[@class='toolbar']/button[text()='Edit']";
	
	private static final String TOOLBAR_DELETE_BUTTON_XPATH = "//div[contains(@class,'panel item-view-panel')]//div[@class='toolbar']/button[text()='Delete']";
	
	
	@FindBy(xpath = TOOLBAR_EDIT_BUTTON_XPATH)
	private WebElement editButtonToolbar;
	
	@FindBy(xpath = TOOLBAR_DELETE_BUTTON_XPATH)
	private WebElement deleteButtonToolbar;
	
	
	@FindBy(xpath = "//div[contains(@class,'admin-preview-panel')]//button[@class = 'x-btn-center']//span[@class='x-btn-inner' and contains(., 'Close')]")
	protected WebElement closeButton;
	
	
	/**
	 * The constructor.
	 * @param session
	 */
	public ContentInfoPage( TestSession session )
	{
		super(session);
		
	}

	/**
	 * Verify that red circle and "New Space" message presented on the top of
	 * Page.
	 * 
	 * @param session
	 *            {@link TestSession} instance
	 */
	public void waitUntilOpened(TestSession session, String displayName, Integer numberPage) {
		String circleXpath = String.format(RED_CIRCLE_XPATH, numberPage.toString());
		String titleXpath = String.format(OBJECT_NAME_XPATH, displayName);
		TestUtils.getInstance().waitUntilVisible(session, By.xpath(circleXpath));
		TestUtils.getInstance().waitUntilVisible(session, By.xpath(titleXpath));

	}
	
	public void doEditContentAndCloseWizard(String contentDisplayName,BaseAbstractContent newContent)
	{
		editButtonToolbar.click();
		AddNewContentWizard wizard = new AddNewContentWizard(getSession());
		//when content opened and the 'Edit' button pressed, new wizard page appears and '2'  should be present in the red cirkle.
		wizard.waitUntilWizardOpened(contentDisplayName, 2);
		wizard.doTypeDataSaveAndClose(newContent);
	}
	/**
	 * Clicks by 'Delete' button on toolbar and confirms deletion. 
	 * @param contentDisplayName
	 */
	public void doDeleteContent(String contentDisplayName)
	{
		//1. click by delete and open a confirm dialog:
		deleteButtonToolbar.click();
		ConfirmationDialog dialog = new ConfirmationDialog(getSession());
		boolean isOpened = dialog.verifyIsOpened();
		if (!isOpened)
		{
			throw new TestFrameworkException("Confirm 'delete content' dialog was not opened!");
		}
		//2. confirm and close dialog
		dialog.doConfirm();
		boolean isClosed = dialog.verifyIsClosed();
		if (!isClosed)
		{
			throw new DeleteCMSObjectException("Confirm 'delete content' dialog was not closed!");
		}
		
		ContentTablePage table = new ContentTablePage(getSession());
		table.waituntilPageLoaded(TestUtils.TIMEOUT_IMPLICIT);
	}
	
	/**
	 * @param content
	 * @return
	 */
	public boolean  verifyContentInfoPage(BaseAbstractContent content)
	{
		boolean result = true;
		List<WebElement> elems = getSession().getDriver().findElements(By.xpath(String.format(H1_NAME_XPATH, content.getDisplayName()))) ;
		
		String dispalayName = elems.get(0).getText();
		result &= content.getDisplayName().equals(dispalayName);
		if(!result)
		{
			getLogger().info("the actual dispalyName and expected are not equal!");
		}
		
		elems = getSession().getDriver().findElements(By.xpath(String.format(SPAN_FULL_NAME_XPATH, content.getDisplayName()))) ;
		String fullNameActual = elems.get(0).getText();
		String fullContentNameExpected =  TestUtils.getInstance().buildFullNameOfContent(content.getName(), content.getParentNames());
		if(!fullNameActual.equals(fullContentNameExpected))
		{
			getLogger().info("wrong fullName, fullName should contains a name and name of parent space");
		}
		return result;
	}
	
	/**
	 * @param content
	 * @return
	 */
	public boolean verifyToolbar(BaseAbstractContent content)
	{
		boolean result = true;
		boolean tmp;
		
		
		tmp = deleteButtonToolbar.isDisplayed() && deleteButtonToolbar.isEnabled();
		if(!tmp)
		{
			getLogger().info("the delete button has wrong state");
		}
		result &= tmp;
		
		
		tmp = editButtonToolbar.isDisplayed() && editButtonToolbar.isEnabled();
		if(!tmp)
		{
			getLogger().info("the edit button has wrong state");
		}
		result &= tmp;		
		return result;
	}
}
