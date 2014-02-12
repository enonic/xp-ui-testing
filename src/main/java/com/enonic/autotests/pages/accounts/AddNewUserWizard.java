package com.enonic.autotests.pages.accounts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.Assert;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.pages.HomePage;
import com.enonic.autotests.pages.WizardPanel;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.User;

public class AddNewUserWizard extends WizardPanel
{

	public static final String SYSTEM_STORE_NAME = "userstores\\system";

	private final String EMAIL_NOT_AVAILABLE_MESSAGE = "Not available";
	private final String EMAIL_AVAILABLE_MESSAGE = "Available";
	private final String USER_NAME_INVALID_CHARS = "Invalid characters";

	private final String EMAIL_VALIDATION_MESSAGE_XPATH = "//table[contains(@class,'x-form-item')]//div[contains(@class,'x-box-inner') and descendant::input[@name='email']]/descendant::div[@class='validationError' or @class='validationInfo']";
	private final String USERNAME_VALIDATION_ERRORMESSAGE_XPATH = "//table[contains(@class,'x-form-item')]//div[contains(@class,'x-box-inner') and descendant::input[@name='name']]/descendant::div[@class='validationError']";

	public static final String PAGE_TITLE = "New User";


	@FindBy(how = How.NAME, using = "qualifiedName")
	private WebElement qualifiedName;

	@FindBy(how = How.NAME, using = "name")
	private WebElement nameInput;

	@FindBy(how = How.NAME, using = "email")
	private WebElement emailInput;

	@FindBy(how = How.NAME, using = "password")
	private WebElement passwordInput;

	@FindBy(how = How.NAME, using = "repeatPassword")
	private WebElement repeatPasswordInput;

	/**
	 * The constructor.
	 * 
	 * @param session
	 *            {@link TestSession} instance
	 */
	public AddNewUserWizard( TestSession session )
	{
		super(session);
	}

	public void doTypeDataSaveAndClose( User user, boolean isNew)
	{
		doTypeDataAndSave(user, isNew);
		closeButton.click();
		AccountsPage accountspage = new AccountsPage(getSession());
		long start = System.currentTimeMillis();
		boolean isLoaded = accountspage.isPageLoaded();
		long end = System.currentTimeMillis();
		long res = end - start;

		if (!isLoaded)
		{
			logError("expected page was not loaded. Waiting time is :" + res);
			throw new SaveOrUpdateException(
					"buttons 'Save ' and 'Close' in the wizard page were pressed, but Page, that contains all accounts, was not loaded");
		}

	}

	/**
	 * Gets validation message for UserName input.
	 * 
	 * @return validation message.('Invalid characters').
	 */
	public String getUserNameValidationMessage()
	{
		boolean isPresentMessage = waitUntilVisibleNoException(By.xpath(USERNAME_VALIDATION_ERRORMESSAGE_XPATH), 1);
		if (isPresentMessage)
		{
			return findElement(By.xpath(USERNAME_VALIDATION_ERRORMESSAGE_XPATH)).getText();
		} else
		{
			return null;
		}
	}

	/**
	 * Gets validation message for Email input.
	 * 
	 * @return validation message.('Invalid e-mail', 'available' or 'Not
	 *         available')
	 */
	public String getEmailValidationMessage()
	{
		boolean isPresentMessage = waitUntilVisibleNoException(By.xpath(EMAIL_VALIDATION_MESSAGE_XPATH), 2);
		if (isPresentMessage)
		{
			return getDriver().findElement(By.xpath(EMAIL_VALIDATION_MESSAGE_XPATH)).getText();
		} else
		{
			return null;
		}

	}

	/**
	 * Types space's data and press the 'Save' button on the toolbar.
	 * 
	 * @param session
	 *            {@link TestSession} instance
	 * @param space
	 *            this space should be created.
	 */
	public void doTypeDataAndSave( User user, boolean isNew)
	{
		clearAndType(displayNameInput, user.getUserInfo().getDisplayName());
		clearAndType(nameInput, user.getUserInfo().getDisplayName());
		if (isNew)
		{
			nameInput.sendKeys(user.getUserInfo().getName());
		} else
		{
			getLogger().info("The user name input is disabled. When user is edited, this element should be disabled!");
		}

		String nameValdationErrorMessage = getUserNameValidationMessage();
		if (nameValdationErrorMessage != null)
		{
			getLogger().info("userinfo:  There validation message for User Name. " + nameValdationErrorMessage);
			boolean isInvalid = waitAndCheckAttrValue(nameInput, "aria-invalid", "true", 2l);
			getLogger().info("userinfo, nameInput attribute 'aria-invalid' has value:  " + nameValdationErrorMessage);
			// TODO should be "Save" disabled if there is error validation
			// message?
			// Assert.fail("Add new User Wizard: there is error message near the 'User Name' input, but the state of the input is valid! The value of attribute 'aria-invalid' is true! ");
			// verifySaveButtonState(nameValdationErrorMessage);
		}// nameInput.getAttribute("aria-invalid").contains("true");

		clearAndType(emailInput, user.getUserInfo().getEmail());
		String emailValdationMessage = getEmailValidationMessage();
		if (emailValdationMessage != null)
		{
			getLogger().info("userinfo: email is" + emailValdationMessage);
			verifySaveButtonState(emailValdationMessage);
		}

		if (isNew)
		{
			passwordInput.sendKeys(user.getUserInfo().getPassword());

			if (user.getUserInfo().getRepeatPassword() != null)
			{
				repeatPasswordInput.sendKeys(user.getUserInfo().getRepeatPassword());
			} else
			{
				repeatPasswordInput.sendKeys(user.getUserInfo().getPassword());
			}
			boolean isInvalid = waitAndCheckAttrValue(repeatPasswordInput, "aria-invalid", "true", 1l);
			getLogger().info("userinfo, repeatPasswordInput has  attribute 'invalid' with value equals:  " + isInvalid);
			if (isInvalid)
			{
				if (toolbarSaveButton.isEnabled())
				{
					TestUtils.saveScreenshot(getSession());
					Assert.fail("There is repeatPassword-validation error message, Button 'Save' on the toolbar is enabled, but should be disabled! ");
				}
			}
		}

		TestUtils.saveScreenshot(getSession());
		// if save button is disabled, so exception will be thrown:
		doSaveFromToolbar();

		String mess = getNotificationMessage(APP_ACCOUNTS_FRAME_XPATH);
		if (mess == null)
		{
			throw new SaveOrUpdateException("A notification, that the User with name" + user.getUserInfo().getName() + " is saved - was not showed");
		}
		String expectedNotificationMessage = " ???";
		// TODO notification message not implemented yet in the Accounts
		// Application.
		//
		// if (!mess.equals(expectedNotificationMessage)) {
		// getLogger().error(
		// "the actual notification and expected are not equals!  actual message:"
		// + mess + " but expected:" + expectedNotificationMessage,
		// session);
		// throw new
		// SaveOrUpdateSpaceException("the actual notification, that the space with name"
		// + space.getName()
		// + " is saved - is not equals expected!");
		// }

	}


	private void verifySaveButtonState(String validationMessage)
	{
		if (validationMessage.equals(EMAIL_NOT_AVAILABLE_MESSAGE))
		{
			if (toolbarSaveButton.isEnabled())
			{
				TestUtils.saveScreenshot(getSession());
				Assert.fail("There is email-validation error message, Button 'Save' on the toolbar is enabled, but should be disabled! ");
			}
		}
		if (validationMessage.equals(USER_NAME_INVALID_CHARS))
		{
			if (toolbarSaveButton.isEnabled())
			{
				TestUtils.saveScreenshot(getSession());
				Assert.fail("There is username-validation error message, Button 'Save' on the toolbar is enabled, but should be disabled! ");
			}
		}
	}

	public boolean verifyAllEmptyFields(TestSession session)
	{
		boolean result = true;
		result &= !greenSaveButton.isDisplayed();

		if (greenSaveButton.isDisplayed())
		{
			logError("'green' save button displayed on the page, but should not be displayed, because required fields are empty!");
		}
		result &= nameInput.isDisplayed();
		if (!nameInput.isDisplayed())
		{
			logError("Input field for user name should be present, this is required field!");
		}
		result &= displayNameInput.isDisplayed();
		if (!displayNameInput.isDisplayed())
		{
			logError("Input field for dispalyed user name should be present, this is required field!");
		}
		result &= qualifiedName.isDisplayed() && qualifiedName.getAttribute("readonly") != null;
		if (!qualifiedName.isDisplayed() && qualifiedName.isEnabled())
		{
			logError("Input field for qualifiedName  should be present, this is required field!");
		}
		result &= toolbarSaveButton.isDisplayed();// toolbarSaveButton.isEnabled()
		if (!toolbarSaveButton.isDisplayed())
		{
			logError("'Save' button is not presented on the toolbar");
		}
		result &= !toolbarSaveButton.isEnabled();
		if (toolbarSaveButton.isEnabled())
		{
			logError("'Save' button on toolbar should be disabled, because required fields are empty!");
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
		result &= passwordInput.isDisplayed();
		if (!passwordInput.isDisplayed())
		{
			logError(" 'passwordInput' should be presented on the Wizard Page!");
		}

		result &= repeatPasswordInput.isDisplayed();
		if (!repeatPasswordInput.isDisplayed())
		{
			logError(" 'repeatPasswordInput' should be presented on the Wizard Page!");
		}
		result &= emailInput.isDisplayed();
		if (!emailInput.isDisplayed())
		{
			logError(" 'emailInput' should be presented on the Wizard Page!");
		}

		return result;

	}

	public void verifyRedCircleOnHomePage(String accountName)
	{
		// 1. Click 'Home' button and verify, that the red circle is present on the HomePage:
		HomePage homepage = showHomePageAndVerifyCircle();
		// 2. Click 'Accounts' and Go back to the 'AddNewSpaceWizard'
		homepage.openAccountsApplication();
		// 3. verify that the wizard is opened.
		waitUntilWizardOpened( 1);

	}
}
