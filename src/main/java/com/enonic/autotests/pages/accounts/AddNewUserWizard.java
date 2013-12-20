package com.enonic.autotests.pages.accounts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.SaveOrUpdateException;
import com.enonic.autotests.pages.BaseWizardPage;
import com.enonic.autotests.pages.HomePage;
import com.enonic.autotests.services.NavigatorHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.User;

public class AddNewUserWizard extends BaseWizardPage
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

	public void doTypeDataSaveAndClose(TestSession session, User user, boolean isNew)
	{

		doTypeDataAndSave(session, user, isNew);
		closeButton.click();
		AccountsPage accountspage = new AccountsPage(getSession());
		long start = System.currentTimeMillis();
		boolean isLoaded = accountspage.isPageLoaded();
		long end = System.currentTimeMillis();
		long res = end - start;

		if (!isLoaded)
		{
			getLogger().error("expected page was not loaded. Waiting time is :" + res, session);
			getLogger()
					.error("doTypeDataSaveAndClose method. buttons 'Save ' and 'Close' were pressed, but page with table, containing the accounts does not appear  ",
							session);
			throw new SaveOrUpdateException(
					"buttons 'Save ' and 'Close' in the wizard page were pressed, but Page, that contains all accounts, was not loaded");
		}

	}

	/**
	 * Gets validation message for UserName input.
	 * 
	 * @param session
	 * @return validation message.('Invalid characters').
	 */
	public String getUserNameValidationMessage(TestSession session)
	{
		boolean isPresentMessage = TestUtils.getInstance().waitUntilVisibleNoException(session, By.xpath(USERNAME_VALIDATION_ERRORMESSAGE_XPATH), 1);
		if (isPresentMessage)
		{
			return session.getDriver().findElement(By.xpath(USERNAME_VALIDATION_ERRORMESSAGE_XPATH)).getText();
		} else
		{
			return null;
		}
	}

	/**
	 * Gets validation message for Email input.
	 * 
	 * @param session
	 * @return validation message.('Invalid e-mail', 'available' or 'Not
	 *         available')
	 */
	public String getEmailValidationMessage(TestSession session)
	{
		boolean isPresentMessage = TestUtils.getInstance().waitUntilVisibleNoException(session, By.xpath(EMAIL_VALIDATION_MESSAGE_XPATH), 2);
		if (isPresentMessage)
		{
			return session.getDriver().findElement(By.xpath(EMAIL_VALIDATION_MESSAGE_XPATH)).getText();
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
	public void doTypeDataAndSave(TestSession session, User user, boolean isNew)
	{
		TestUtils.getInstance().clearAndType(session, displayNameInput, user.getUserInfo().getDisplayName());
		TestUtils.getInstance().clearAndType(session, nameInput, user.getUserInfo().getDisplayName());
		if (isNew)
		{
			nameInput.sendKeys(user.getUserInfo().getName());
		} else
		{
			getLogger().info("The user name input is disabled. When user is edited, this element should be disabled!");
		}

		String nameValdationErrorMessage = getUserNameValidationMessage(session);
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

		TestUtils.getInstance().clearAndType(session, emailInput, user.getUserInfo().getEmail());
		String emailValdationMessage = getEmailValidationMessage(session);
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
					TestUtils.getInstance().saveScreenshot(getSession());
					Assert.fail("There is repeatPassword-validation error message, Button 'Save' on the toolbar is enabled, but should be disabled! ");
				}
			}
		}

		TestUtils.getInstance().saveScreenshot(getSession());
		// if save button is disabled, so exception will be thrown:
		doSaveFromToolbar();

		String mess = getNotificationMessage(session, AppConstants.APP_ACCOUNTS_FRAME_XPATH);

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

	private boolean waitAndCheckAttrValue(final WebElement element, final String attributeName, final String attributeValue, long timeout)
	{
		WebDriverWait wait = new WebDriverWait(getSession().getDriver(), timeout);
		try
		{
			return wait.until(new ExpectedCondition<Boolean>()
			{
				@Override
				public Boolean apply(WebDriver webDriver)
				{
					try
					{
						// System.out.println("attributeName:"+attributeName+" attributeValue:"+attributeValue);
						return element.getAttribute(attributeName).contains(attributeValue);

					} catch (Exception e)
					{

						return false;
					}
				}
			});
		} catch (org.openqa.selenium.TimeoutException e)
		{
			return false;
		}

	}

	private void verifySaveButtonState(String validationMessage)
	{
		if (validationMessage.equals(EMAIL_NOT_AVAILABLE_MESSAGE))
		{
			if (toolbarSaveButton.isEnabled())
			{
				TestUtils.getInstance().saveScreenshot(getSession());
				Assert.fail("There is email-validation error message, Button 'Save' on the toolbar is enabled, but should be disabled! ");
				// throw new
				// WebElementException("Button 'Save' on the toolbar is enabled, but should be disabled");
			}
		}
		if (validationMessage.equals(USER_NAME_INVALID_CHARS))
		{
			if (toolbarSaveButton.isEnabled())
			{
				TestUtils.getInstance().saveScreenshot(getSession());
				Assert.fail("There is username-validation error message, Button 'Save' on the toolbar is enabled, but should be disabled! ");
				// throw new
				// WebElementException("Button 'Save' on the toolbar is enabled, but should be disabled");
			}
		}
	}

	public boolean verifyAllEmptyFields(TestSession session)
	{
		boolean result = true;
		result &= !greenSaveButton.isDisplayed();

		if (greenSaveButton.isDisplayed())
		{
			getLogger().error("'green' save button displayed on the page, but should not be displayed, because required fields are empty!", session);
		}
		result &= nameInput.isDisplayed();
		if (!nameInput.isDisplayed())
		{
			getLogger().error("Input field for user name should be present, this is required field!", session);
		}
		result &= displayNameInput.isDisplayed();
		if (!displayNameInput.isDisplayed())
		{
			getLogger().error("Input field for dispalyed user name should be present, this is required field!", session);
		}
		result &= qualifiedName.isDisplayed() && qualifiedName.getAttribute("readonly") != null;
		if (!qualifiedName.isDisplayed() && qualifiedName.isEnabled())
		{
			getLogger().error("Input field for qualifiedName  should be present, this is required field!", session);
		}
		result &= toolbarSaveButton.isDisplayed();// toolbarSaveButton.isEnabled()
		if (!toolbarSaveButton.isDisplayed())
		{
			getLogger().error("'Save' button is not presented on the toolbar", session);
		}
		result &= !toolbarSaveButton.isEnabled();
		if (toolbarSaveButton.isEnabled())
		{
			getLogger().error("'Save' button on toolbar should be disabled, because required fields are empty!", session);
		}
		result &= gotoHomeButton.isDisplayed();
		if (!gotoHomeButton.isDisplayed())
		{
			getLogger().error("Go To Home Page is not presented on the Wizard Page!", session);
		}
		result &= closeButton.isDisplayed();
		if (!closeButton.isDisplayed())
		{
			getLogger().error("'Close' should be presented on the Wizard Page!", session);
		}
		result &= passwordInput.isDisplayed();
		if (!passwordInput.isDisplayed())
		{
			getLogger().error(" 'passwordInput' should be presented on the Wizard Page!", session);
		}

		result &= repeatPasswordInput.isDisplayed();
		if (!repeatPasswordInput.isDisplayed())
		{
			getLogger().error(" 'repeatPasswordInput' should be presented on the Wizard Page!", session);
		}
		result &= emailInput.isDisplayed();
		if (!emailInput.isDisplayed())
		{
			getLogger().error(" 'emailInput' should be presented on the Wizard Page!", session);
		}

		return result;

	}

	public void verifyRedCircleOnHomePage(String accountName)
	{
		// 1. Click 'Home' button and verify, that the red circle is present on
		// the HomePage:
		HomePage homepage = showHomePageAndVerifyCircle();
		// 2. Click 'Accounts' and Go back to the 'AddNewSpaceWizard'
		homepage.openAccountsApplication();

		// 3. verify that the wizard is opened.
		waitUntilWizardOpened( accountName, 1);

	}
}
