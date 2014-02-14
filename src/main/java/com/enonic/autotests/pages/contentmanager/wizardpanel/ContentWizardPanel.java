package com.enonic.autotests.pages.contentmanager.wizardpanel;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.pages.WizardPanel;
import static com.enonic.autotests.utils.SleepHelper.sleep;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.contentmanager.ArticleContent;
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent;
import com.enonic.autotests.vo.contentmanager.MixinContent;

/**
 *  'Content Manager' application, Add new Content Wizard page.
 *
 */
public class ContentWizardPanel extends WizardPanel
{
	public static  String START_WIZARD_TITLE = "New %s";
	private String NOTIF_MESSAGE = "\"%s\" saved successfully!";
 
	private static final String TOOLBAR_PUBLISH_BUTTON_XPATH = "//div[@class='panel wizard-panel']/div[@class='toolbar']//button[text()='Publish']";
	private static final String TOOLBAR_DELETE_BUTTON_XPATH = "//div[@class='panel wizard-panel']/div[@class='toolbar']//button[text()='Delete']";
	public static final String TOOLBAR_DUPLICTAE_BUTTON_XPATH = "//div[@class='panel wizard-panel']/div[@class='toolbar']//button[text()='Duplicate']";

	@FindBy(xpath = TOOLBAR_PUBLISH_BUTTON_XPATH)
	private WebElement toolbarPublishButton;

	@FindBy(xpath = TOOLBAR_DELETE_BUTTON_XPATH)
	private WebElement toolbarDeleteButton;
	
	@FindBy(xpath = TOOLBAR_DUPLICTAE_BUTTON_XPATH)
	private WebElement toolbarDuplicateButton;
	
	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public ContentWizardPanel(TestSession session)
	{
		super(session);

	}
	public boolean verifyWizardPage(TestSession session)
	{		
		boolean result = true;
		//1. verify the toolbar
		result &= verifyTollbar(session);
		if (!result)
		{
			logError("there are error during verifying the toolbar!");
		}
		// verify input fields , 'Go to home' and 'close ' buttons  
		result &= displayNameInput.isDisplayed();
		if (!displayNameInput.isDisplayed())
		{
			logError("'displayName' input is not present on the wizard page");
		}
		result &= nameInput.isDisplayed();
		if (!nameInput.isDisplayed())
		{
			logError("'Name' input  is not present on the wizard page");
		}
		result &= gotoHomeButton.isDisplayed();
		if (!gotoHomeButton.isDisplayed())
		{
			logError("Go To Home Page is not presented on the Wizard Page!");
		}
		result &= closeButton.isDisplayed();
		if (!closeButton.isDisplayed())
		{
			logError("'Close' should be presented on the Wizard Page!");
		}
		return result;
	}
	
	/**
	 * @param session
	 * @return
	 */
	private boolean verifyTollbar(TestSession session)
	{
		boolean result = true;
		result &= toolbarSaveButton.isDisplayed() && toolbarSaveButton.isEnabled();
		if (!result)
		{
			//getLogger().error("error durin verifying the 'Save' toolbar-button !", getSession());
			getLogger().error("error durin verifying the 'Save' toolbar-button !");
		}
		result &= toolbarDeleteButton.isDisplayed() && !toolbarDeleteButton.isEnabled();
		if (!(toolbarDeleteButton.isDisplayed() && !toolbarDeleteButton.isEnabled()))
		{
			//getLogger().error("error during verifying the 'Delete' toolbar-button !", getSession());
		}
		result &= toolbarPublishButton.isDisplayed() && toolbarPublishButton.isEnabled();
		

		result &= toolbarDuplicateButton.isDisplayed() && !toolbarDuplicateButton.isEnabled();
		return result;
	}
	
	/**
	 * @return
	 */
	public String getTitle()
	{
		List<WebElement> elems = getDriver().findElements(By.xpath("//div[child::span[@class='tabcount']]/span[@class='label']"));
		if(elems.size()>0)
		{
			return elems.get(0).getText();
		}else{
			return null;
		}
	}

	/**
	 * Types data and press the "Save" button from the toolbar.
	 * @param content
	 */
	public void doTypeDataAndSave( BaseAbstractContent content)
	{
		sleep(500);
		// 1. type a data: 'name' and 'Display Name'.	
		waitElementClickable(By.name("displayName"), 2);
		getLogger().info("types displayName: "+ content.getDisplayName());
		clearAndType(displayNameInput, content.getDisplayName());	
		sleep(500);
		if(StringUtils.isNotEmpty(content.getName()))
		{
			waitElementClickable(By.name("name"), 2);
			getLogger().info("types name: "+ content.getName());
			clearAndType(nameInput, content.getName());		
		}
		
		TestUtils.saveScreenshot(getSession());
		// 2. populate main tab
		populateContentForm(getSession(), content);

		// 3. check if enabled and press "Save".
		getLogger().info("Clicks 'Save' button in toolbar");
		doSaveFromToolbar();
		// 4. check notification message.
		String mess = getNotificationMessage(APP_CONTENT_MANAGER_FRAME_XPATH);
		getLogger().info("notification message has appeared:" + mess);
		if (mess == null)
		{
			throw new SaveOrUpdateException("A notification, that the space with name" + content.getDisplayName() + " is saved - was not showed");
		}
		TestUtils.saveScreenshot(getSession());

		//TODO verify notification message 
		//String expectedNotificationMessage = String.format(NOTIF_MESSAGE, content.getDisplayName());
		//if (!mess.contains(expectedNotificationMessage))
		//{
		//	getLogger().error(
		//			"the actual notification and expected are not equals!  actual message:" + mess + " but expected:" + expectedNotificationMessage,
		//			getSession());
		//	throw new SaveOrUpdateException("the actual notification, that the content with name" + content.getDisplayName()
		//			+ " is saved - is not equals expected!");
		//}
	}

	/**
	 * Types a data and close wizard.
	 * 
	 * @param content
	 */
	public void doTypeDataSaveAndClose( BaseAbstractContent content)
	{
		doTypeDataAndSave( content);
		closeButton.click();	
	}
	
	/**
	 * Populates a main tab in the wizard, Article, mixin...  tabs for example 
	 * 
	 * @param session
	 * @param content
	 */
	private void populateContentForm(TestSession session, BaseAbstractContent content)
	{
		if (content instanceof MixinContent)
		{
			MixinWizardTab tab = new MixinWizardTab(session);
			//tab.populateAddresses((MixinContent) content);
		}
		if (content instanceof ArticleContent)
		{
			
		}
	}
}
