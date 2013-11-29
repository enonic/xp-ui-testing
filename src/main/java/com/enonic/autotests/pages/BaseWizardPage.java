package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.services.NavigatorHelper;
import com.enonic.autotests.utils.TestUtils;

/**
 * Base class for wizards.
 *
 */
public abstract class BaseWizardPage extends Page {

	public static  String RED_CIRCLE_XPATH = "//span[@class='tabcount' and contains(.,'%s')]";

	public static  String OBJECT_NAME_XPATH = "//span[@class='label' and contains(.,'%s')]";
	
	public static final String HOME_BUTTON_XPATH = "//div[contains(@class,'appbar')]/button[@class='launcher-button']";

	
	public static final String TOOLBAR_SAVE_BUTTON_XPATH = "//div[@class='panel wizard-panel']/div[@class='toolbar']//button[text()='Save']";
	public static final String TOOLBAR_CLOSEWIZARD_BUTTON_XPATH = "//div[@class='panel wizard-panel']/div[@class='toolbar']//button[text()='Close']";
	
	public static final String GREEN_SAVE_BUTTON_XPATH = "//div[contains(@class,'x-btn-green-large')]//span[@class='x-btn-inner' and contains(., 'Save')]";

	@FindBy(xpath = TOOLBAR_CLOSEWIZARD_BUTTON_XPATH )
	protected WebElement closeButton;

	@FindBy(xpath = HOME_BUTTON_XPATH)
	protected WebElement gotoHomeButton;

	@FindBy(xpath = TOOLBAR_SAVE_BUTTON_XPATH)
	protected WebElement toolbarSaveButton;

	@FindBy(xpath = GREEN_SAVE_BUTTON_XPATH)
	protected WebElement greenSaveButton;
	
	@FindBy(name = "displayName")
	protected WebElement displayNameInput;
	
	@FindBy(name = "name")
	protected WebElement nameInput;

	public BaseWizardPage(TestSession session) {
		super(session);

	}

	/**
	 * Press the button 'Save', which located in the wizard's toolbar.
	 */
	protected void doSaveFromToolbar() 
	{
		boolean isSaveButtonEnabled = TestUtils.getInstance().waitUntilElementEnabledNoException(getSession(), By.xpath("//div[@class='panel wizard-panel']/div[@class='toolbar']//button[text()='Save']"),2l);
		if(!isSaveButtonEnabled){
			throw new SaveOrUpdateException("Impossible to save, button 'Save' is disabled!");
		}
		toolbarSaveButton.click();
	}

	/**
	 * Checks tab-count on the Home page.(checks that one wizard was opened)
	 * @return
	 */
	public HomePage showHomePageAndVerifyCircle() {
		gotoHomeButton.click();
		HomePage page = new HomePage(getSession());

		getSession().getDriver().switchTo().window(getSession().getWindowHandle());
		TestUtils.getInstance().waitUntilVisible(getSession(), By.xpath("//div[@class='tab-count-container' and contains(@title,'1 tab(s) open')]"));
		return page;
	}
	/**
	 * Gets notification message(Space 'namesapce' was saved), that appears at
	 * the bottom of the WizardPage. <br>
	 * 
	 * @param session
	 *            {@link TestSession} instance
	 * @return notification message or null.
	 * 
	 */
	protected String getNotificationMessage(TestSession session,String iframeXpath) {
		//switch to window.
		//getSession().getDriver().switchTo().window(getSession().getWindowHandle());
		String message = TestUtils.getInstance().getNotificationMessage(By.xpath("//div[@class='admin-notification-content']/span"), session.getDriver(),2l);
		//switch to iframe again.
		//NavigatorHelper.switchToIframe(getSession(),iframeXpath);
		return message;
	}
	/**
	 * Verify that red circle and "New Space" message presented on the top of
	 * Page.
	 * 
	 * @param session
	 *            {@link TestSession} instance
	 */
	public void waitUntilWizardOpened( String displayName, Integer numberPage) {
		String circleXpath = String.format(RED_CIRCLE_XPATH, numberPage.toString());
		String titleXpath = String.format(OBJECT_NAME_XPATH, displayName); 
		TestUtils.getInstance().waitUntilVisible(getSession(), By.xpath(circleXpath));
		TestUtils.getInstance().waitUntilVisible(getSession(), By.xpath(titleXpath));

	}

}
