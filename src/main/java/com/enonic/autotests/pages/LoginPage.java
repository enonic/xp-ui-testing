package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.AuthenticationException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.logger.Logger;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page Object for Login page version 5.0
 * 
 */
public class LoginPage extends Page
{

	private static Logger logger = Logger.getLogger();

	private String TITLE = "Enonic WEM Admin";

	private long LOGIN_PAGE_TIMEOUT = 10;
	
	private final String EMAIL_INPUT_XPATH = "//input[@placeholder = 'userid or e-mail']";

	@FindBy(xpath = EMAIL_INPUT_XPATH)
	private WebElement usernameInput;

	@FindBy(xpath = "//input[@placeholder = 'password']")
	private WebElement passwordInput;

	private final String loginEnabledClass = "login-button";
	private String loginButtonXpath = "//button[contains(@class,'%s')]";

	/**
	 * The constructor.
	 * 
	 * @param session
	 */
	public LoginPage( TestSession session )
	{
		super(session);
		
	}

	/**
	 * Types the username and password and click the 'login' button.
	 * 
	 * @param username
	 * @param password
	 */
	public void doLogin(String username, String password)
	{
		(new WebDriverWait(getSession().getDriver(), 20l)).until(new ExpectedCondition<Boolean>()
				{
					public Boolean apply(WebDriver d)
					{
						return d.getTitle().trim().contains(title);
					}
				});
		logger.info("Login action started. Username: " + username + " Password:" + password);
		usernameInput.sendKeys(username);

		passwordInput.sendKeys(password);

		boolean isEnabledButton = TestUtils.getInstance().waitAndFind(By.xpath(String.format(loginButtonXpath, loginEnabledClass)), getDriver());
		if (!isEnabledButton)
		{
			logger.info("the Button 'Log in' is disabled");
			throw new AuthenticationException("wrong password or username");
		}
		WebElement loginButton = findElement(By.xpath(String.format(loginButtonXpath, loginEnabledClass)));
		loginButton.click();

	}

}
