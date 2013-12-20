package com.enonic.autotests.pages.spaceadmin;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.pages.BaseWizardPage;
import com.enonic.autotests.pages.HomePage;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.Space;

/**
 * 'Space Admin' application, Add new Space Wizard page.
 * 
 */
public class AddNewSpaceWizard extends BaseWizardPage
{

	public static final String START_TITLE = "New Space";

	//private String NOTIF_MESSAGE = "Space \"%s\" was saved";

	@FindBy(name = "template")
	private WebElement templateCombo;

	/**
	 * The Constructor.
	 * 
	 * @param session
	 *            {@link TestSession} instance
	 */
	public AddNewSpaceWizard( TestSession session )
	{
		super(session);
	}

	public void verifyRedCircleOnHomePage(String spaceName)
	{
		// 1. Click 'Home' button and verify, that the red circle is present on the HomePage:
		HomePage homepage = showHomePageAndVerifyCircle();
		// 2. Click 'Space Admin' and Go back to the 'AddNewSpaceWizard'
		homepage.openSpaceAdminApplication();
		// 3. verify that the wizard is opened.
		waitUntilWizardOpened(spaceName, 1);

	}

	public boolean verifyAllEmptyFields()
	{
		boolean result = true;
		//TODO should be present GREEN button on page?
		
//		result &= !greenSaveButton.isDisplayed();
//
//		if (greenSaveButton.isDisplayed())
//		{
//			getLogger().error("'green' save button displayed on the page, but should not be displayed, because required fields are empty!",
//					getSession());
//		}
		result &= nameInput.isDisplayed();
		if (!nameInput.isDisplayed())
		{
			getLogger().error("Input field for Space name should be present, this is required field!", getSession());
		}
		result &= toolbarSaveButton.isDisplayed();
		if (!toolbarSaveButton.isDisplayed())
		{
			getLogger().error("'Save' button is not presented on the toolbar", getSession());
		}
		result &= !toolbarSaveButton.isEnabled();
		if (toolbarSaveButton.isEnabled())
		{
			getLogger().error("'Save' button on toolbar should be disabled, because required fields are empty!", getSession());
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

	/**
	 * Types space's data and press the 'Save' button on the toolbar.
	 * 
	 * @param session
	 *            {@link TestSession} instance
	 * @param space
	 *            this space should be created.
	 */
	public void doTypeDataAndSave(TestSession session, Space space)
	{
		TestUtils.getInstance().clearAndType(session, displayNameInput, space.getDisplayName());
				
		String os = System.getProperty("os.name").toLowerCase();
		getLogger().info("OS System is " + os);
		nameInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), space.getName());
		
		//TODO select a template-name
		//templateCombo.sendKeys("Template 2");
		
		doSaveFromToolbar();
		String mess = getNotificationMessage(session, AppConstants.APP_SPACE_ADMIN_FRAME_XPATH);
		
		//TODO check of Alert dialog postponed.
//		boolean isAlertDialogPresent = checkAlerts(getSession());
//		if (isAlertDialogPresent)
//		{
//			getLogger().info("Modal Dialog appeared after a button 'Save' has been pressed ");
//			throw new SaveOrUpdateException("Button 'Save' was pressed, but Modal Dialog is present");
//		}

		if (mess == null)
		{
			throw new SaveOrUpdateException("A notification, that the space with name" + space.getName() + " is saved - was not showed");
		}
		//TODO verifying of notification message postponed (not finished in CMS)
//		String expectedNotificationMessage = String.format(NOTIF_MESSAGE, space.getName());
//		if (!mess.equals(expectedNotificationMessage))
//		{
//			getLogger().error(
//					"the actual notification and expected are not equals!  actual message:" + mess + " but expected:" + expectedNotificationMessage,
//					session);
//			throw new SaveOrUpdateException("the actual notification, that the space with name" + space.getName()
//					+ " is saved - is not equals expected!");
//		}

	}

	public void doTypeDataSaveAndClose(TestSession session, Space space)
	{
		doTypeDataAndSave(session, space);
		closeButton.click();

	}

	/**
	 * Checks errors messages and alert, when 'Save' button was pressed.
	 * 
	 * @param session
	 *            {@link TestSession} instance.
	 * @param by
	 */
	public boolean checkAlerts(TestSession session)
	{
		return TestUtils.getInstance().waitUntilVisibleNoException(session, By.xpath(AppConstants.DIALOG_CLOSE_BUTTON_XPATH), 1l);

	}

}
