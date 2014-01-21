package com.enonic.autotests.pages.contentmanager.wizardpanel;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.DeleteCMSObjectException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentGridPage;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent;

public class ItemViewPanelPage extends Page
{
	
	public static  String RED_CIRCLE_XPATH = "//span[@class='tabcount' and contains(.,'%s')]";

	public static  String VERIFY_TITLE_SPAN_XPATH = "//span[@class='label' and @title='%s']";
	public static String TITLE_SPAN_XPATH  = "//span[@class='label' and @title]";
	
	public static final String HOME_BUTTON_XPATH = "//div[contains(@class,'x-btn start-button')]";

	private static  String H1_DISPLAY_NAME_XPATH = "//div[@class='panel item-view-panel']//h1[text()='%s']";
	private static  String H4_FULL_NAME_XPATH = "//div[@class='panel item-view-panel']//h4[text()='%s']";
	
	private static final String TOOLBAR_EDIT_BUTTON_XPATH = "//div[contains(@class,'panel item-view-panel')]//div[@class='toolbar']/button[text()='Edit']";
	
	private static final String TOOLBAR_DELETE_BUTTON_XPATH = "//div[contains(@class,'panel item-view-panel')]//div[@class='toolbar']/button[text()='Delete']";
	
	
	@FindBy(xpath = TOOLBAR_EDIT_BUTTON_XPATH)
	private WebElement editButtonToolbar;
	
	@FindBy(xpath = TOOLBAR_DELETE_BUTTON_XPATH)
	private WebElement deleteButtonToolbar;
	
	
	@FindBy(xpath = "//div[contains(@class,'panel item-view-panel')]//div[@class='toolbar']/button[text()='Close']")
	protected WebElement closeButton;
	
	
	/**
	 * The constructor.
	 * @param session
	 */
	public ItemViewPanelPage( TestSession session )
	{
		super(session);
		
	}

	public ItemViewPanelPage( WebDriver driver )
	{
		super(driver);
		
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
		String titleXpath = String.format(VERIFY_TITLE_SPAN_XPATH, displayName);
		TestUtils.getInstance().waitUntilVisible(session, By.xpath(circleXpath));
		TestUtils.getInstance().waitUntilVisible(session, By.xpath(titleXpath));

	}
	
	public void doCloseContentInfoView()
	{
		closeButton.click();
	}

	public void doEditContentAndCloseWizard(String contentDisplayName,BaseAbstractContent newContent)
	{
		editButtonToolbar.click();
		AddContentWizardPage wizard = new AddContentWizardPage(getSession());
		//when content opened and the 'Edit' button pressed, new wizard page appears and '2'  should be present in the red circle.
		wizard.waitUntilWizardOpened( 2);
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
		
		ContentGridPage table = new ContentGridPage(getSession());
		table.waituntilPageLoaded(TestUtils.TIMEOUT_IMPLICIT);
	}
	
	/**
	 * @param content
	 * @return
	 */
	public boolean  verifyContentInfoPage(BaseAbstractContent content)
	{
		boolean result = true;
		List<WebElement> elems = findElements(By.xpath(String.format(H1_DISPLAY_NAME_XPATH, content.getDisplayName()))) ;
		result &=elems.size()>0;
		String dispalayName = elems.get(0).getText();
		result &= content.getDisplayName().equals(dispalayName);
		if(!result)
		{
			getLogger().info("the actual dispalyName and expected are not equal!");
		}
		String fullContentName =  TestUtils.getInstance().buildFullNameOfContent(content.getName(), content.getParentNames());
		elems = findElements(By.xpath(String.format(H4_FULL_NAME_XPATH, fullContentName))) ;
		result &=elems.size()>0;
		
		
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

	public String getTitle()
	{
		return findElements(By.xpath(TITLE_SPAN_XPATH)).get(0).getText();
	}
}
