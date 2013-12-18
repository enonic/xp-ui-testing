package com.enonic.autotests.pages.cm;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.cm.ArticleContent;
import com.enonic.autotests.model.cm.BaseAbstractContent;
import com.enonic.autotests.model.cm.MixinContent;
import com.enonic.autotests.pages.BaseWizardPage;
import com.enonic.autotests.pages.CloseWizardDialog;
import com.enonic.autotests.utils.TestUtils;

/**
 *  'Content Manager' application, Add new Content Wizard page.
 *
 */
public class AddNewContentWizard extends BaseWizardPage
{
	public static  String START_WIZARD_TITLE = "New %s";
	private String NOTIF_MESSAGE = "\"%s\" saved successfully!";
 
	public static final String TOOLBAR_PREVIEW_BUTTON_XPATH = "//div[contains(@class,'admin-toolbar')]//div[contains(@class, 'x-toolbar-item')]//button[@class='x-btn-center' and descendant::span[contains(.,'Preview')]]";
	private static final String TOOLBAR_PUBLISH_BUTTON_XPATH = "//div[contains(@class,'admin-toolbar')]//div[contains(@class, 'x-toolbar-item')]//button[@class='x-btn-center' and descendant::span[contains(.,'Publish')]]";
	private static final String TOOLBAR_DELETE_BUTTON_XPATH = "//div[contains(@class,'admin-toolbar')]//div[contains(@class, 'x-toolbar-item')]//button[@class='x-btn-center' and descendant::span[contains(.,'Delete')]]";
	public static final String TOOLBAR_MOVE_BUTTON_XPATH = "//div[contains(@class,'admin-toolbar')]//div[contains(@class, 'x-toolbar-item')]//button[@class='x-btn-center' and descendant::span[contains(.,'Move')]]";
	public static final String TOOLBAR_DUPLICTAE_BUTTON_XPATH = "//div[contains(@class,'admin-toolbar')]//div[contains(@class, 'x-toolbar-item')]//button[@class='x-btn-center' and descendant::span[contains(.,'Duplicate')]]";
	public static final String TOOLBAR_EXPORT_BUTTON_XPATH = "//div[contains(@class,'admin-toolbar')]//div[contains(@class, 'x-toolbar-item')]//button[@class='x-btn-center' and descendant::span[contains(.,'Export')]]";
	


	@FindBy(xpath = TOOLBAR_PUBLISH_BUTTON_XPATH)
	private WebElement toolbarPublishButton;

	@FindBy(xpath = TOOLBAR_DELETE_BUTTON_XPATH)
	private WebElement toolbarDeleteButton;

	@FindBy(xpath = TOOLBAR_PREVIEW_BUTTON_XPATH)
	private WebElement toolbarPreviewButton;

	@FindBy(xpath = TOOLBAR_MOVE_BUTTON_XPATH)
	private WebElement toolbarMoveButton;
	
	@FindBy(xpath = TOOLBAR_MOVE_BUTTON_XPATH)
	private WebElement toolbarExportButton;
	
	@FindBy(xpath = TOOLBAR_DUPLICTAE_BUTTON_XPATH)
	private WebElement toolbarDuplicateButton;
	
	
	

	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public AddNewContentWizard( TestSession session )
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
			getLogger().error("there are error during verifying the toolbar!", getSession());
		}
		// verify input fields , 'Go to home' and 'close ' buttons  
		result &= displayNameInput.isDisplayed();
		if (!displayNameInput.isDisplayed())
		{
			getLogger().error("'displayName' input is not present on the wizard page", getSession());
		}
		result &= nameInput.isDisplayed();
		if (!nameInput.isDisplayed())
		{
			getLogger().error("'Name' input  is not present on the wizard page", getSession());
		}
		result &= gotoHomeButton.isDisplayed();
		if (!gotoHomeButton.isDisplayed())
		{
			getLogger().error("Go To Home Page is not presented on the Wizard Page!", getSession());
		}
		result &= closeButton.isDisplayed();
		if (!closeButton.isDisplayed())
		{
			getLogger().error("'Close' should be presented on the Wizard Page!", getSession());
		}
		return result;
	}
	private boolean verifyTollbar(TestSession session)
	{
		boolean result = true;
		result &= toolbarSaveButton.isDisplayed() && !toolbarSaveButton.isEnabled();
		if (!result)
		{
			getLogger().error("error durin verifying the 'Save' toolbar-button !", getSession());
		}
		result &= toolbarDeleteButton.isDisplayed() && !toolbarDeleteButton.isEnabled();
		if (!(toolbarDeleteButton.isDisplayed() && !toolbarDeleteButton.isEnabled()))
		{
			getLogger().error("error during verifying the 'Delete' toolbar-button !", getSession());
		}
		result &= !toolbarPublishButton.isDisplayed() && !toolbarPublishButton.isEnabled();
		if (!(!toolbarPublishButton.isDisplayed() && !toolbarPublishButton.isEnabled()))
		{
			getLogger().error("error during verifying the 'Publish' toolbar-button !", getSession());
		}
		result &= toolbarPreviewButton.isDisplayed() && !toolbarPreviewButton.isEnabled();
		result &= toolbarDuplicateButton.isDisplayed() && !toolbarDuplicateButton.isEnabled();
		result &= toolbarMoveButton.isDisplayed() && !toolbarMoveButton.isEnabled();
		result &= toolbarExportButton.isDisplayed() && !toolbarExportButton.isEnabled();
		
		return result;
	}

	/**
	 * Types data and press the "Save" button from the toolbar.
	 * @param session
	 * @param content
	 */
	public void doTypeDataAndSave( BaseAbstractContent content)
	{
		try
		{
			Thread.sleep(2000);
		} catch (InterruptedException e)
		{
			
		}
		// 1. type a data: 'name' and 'Display Name'.
		waitElementClickable(By.name("displayName"), 2);
		getLogger().info("types displayName: "+ content.getDisplayName());
		TestUtils.getInstance().clearAndType(getSession(), displayNameInput, content.getDisplayName());
		
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			
		}
		TestUtils.getInstance().saveScreenshot(getSession());
		if(content.getName()!=null && !content.getName().isEmpty())
		{
		waitElementClickable(By.name("name"), 2);
		getLogger().info("types name: "+ content.getName());
		TestUtils.getInstance().clearAndType(getSession(), nameInput, content.getName());
		
		}
		
		TestUtils.getInstance().saveScreenshot(getSession());
		// 2. populate main tab
		populateMainTab(getSession(), content);

		// 3. check if enabled and press "Save".
		getLogger().info("Clicks 'Save' button in toolbar");
		doSaveFromToolbar();

		// 4. check notification message.
		String mess = getNotificationMessage(getSession(), AppConstants.APP_CONTENT_MANAGER_FRAME_XPATH);
		getLogger().info("notification message has appeared:" + mess);
		if (mess == null)
		{
			throw new SaveOrUpdateException("A notification, that the space with name" + content.getDisplayName() + " is saved - was not showed");
		}
		TestUtils.getInstance().saveScreenshot(getSession());

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
	 * @param session
	 * @param content
	 */
	public void doTypeDataSaveAndClose( BaseAbstractContent content)
	{
		doTypeDataAndSave( content);
		closeButton.click();
		boolean isPresent = checkModalDialog();
		if(isPresent)
		{
			TestUtils.getInstance().saveScreenshot(getSession());
			throw new TestFrameworkException("buttons save and close were pressed, but modal dialog appeared!");
		}
		ContentTablePage page = new ContentTablePage(getSession());
		page.waituntilPageLoaded(TestUtils.TIMEOUT_IMPLICIT);

	}
	
	private boolean checkModalDialog()
	{
		CloseWizardDialog modalDialog = new CloseWizardDialog(getSession());
		return modalDialog.isDialogPresent();
	}
	/**
	 * Populates a main tab in the wizard, Article, mixin...  tabs for example 
	 * 
	 * @param session
	 * @param content
	 */
	private void populateMainTab(TestSession session, BaseAbstractContent content)
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
