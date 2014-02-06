package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.accounts.AccountsPage;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel;
import com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel;
import com.enonic.autotests.services.NavigatorHelper;
import com.enonic.autotests.utils.SleepWaitHelper;

/**
 * Page Object for 'Home' page. Version 5.0
 * 
 */
public class HomePage extends Page
{
	public static String TITLE = "Enonic CMS - Boot Page";

	@FindBy(xpath = "//a[contains(@href,'content-manager')]//div[contains(.,'Content Manager')]")
	private WebElement contentManager;
	
	
	@FindBy(xpath = "//div[@class = 'name-container' and text()='Schema Manager']")
	private WebElement schemaManager;

	@FindBy(xpath = "//div[@class = 'name-container' and text()='Space Admin']")
	private WebElement spaceAdmin;

	@FindBy(xpath = "//div[@class = 'name-container' and text()='Accounts']")
	private WebElement accounts;

	/**
	 * @param session
	 */
	public HomePage(TestSession session)
	{
		super(session);
	}

	public void open(String username, String password)
	{
		String wh = getSession().getWindowHandle();
		if(wh == null)
		{
			getSession().setWindowHandle(getDriver().getWindowHandle());
		}		
		// open page via the driver.get(BASE_URL)
		if(getSession().getBaseUrl()!=null)
		{
			getDriver().get(getSession().getBaseUrl());
		}
		
		if (!getSession().isLoggedIn())
		{
			getLogger().info("try to login with userName:" + username + " password: " + password);
			long start = System.currentTimeMillis();
			LoginPage loginPage = new LoginPage(getSession());
			loginPage.doLogin(username, password);

			getLogger().perfomance("user logged in " + username + "  password:" + password, start);
			getSession().setLoggedIn(true);
		}
		else{
			getDriver().switchTo().window(wh);
		}
		waitUntilAllFramesLoaded();

	}

	/**
	 * 
	 */
	public void waitUntilAllFramesLoaded()
	{
		
		SleepWaitHelper.waitUntilVisible(getDriver(), By.xpath("//div[@class = 'name-container' and text()='Accounts']"));
		SleepWaitHelper.waitUntilVisible(getDriver(), By.xpath("//div[@class = 'name-container' and text()='Schema Manager']"));
	}
	
	/**
	 * @return
	 */
	public SchemaBrowsePanel openSchemaManagerApplication()
	{
		schemaManager.click();	
		String whandle = getSession().getDriver().getWindowHandle();
		getSession().setWindowHandle(whandle);
		NavigatorHelper.switchToIframe(getSession(), Application.APP_SCHEMA_MANAGER_FRAME_XPATH);
		
		return new SchemaBrowsePanel(getSession());
	}

	
	public ContentBrowsePanel openContentManagerApplication()
	{
		contentManager.click();
		String whandle = getSession().getDriver().getWindowHandle();
		getSession().setWindowHandle(whandle);
		NavigatorHelper.switchToIframe(getSession(), Application.APP_CONTENT_MANAGER_FRAME_XPATH);	
		return new ContentBrowsePanel(getSession());
	}

	public AccountsPage openAccountsApplication()
	{
		accounts.click();
		AccountsPage accountsPage = new AccountsPage(getSession());
		accountsPage.waituntilPageLoaded(2l);
		
		String whandle = getSession().getDriver().getWindowHandle();
		getSession().setWindowHandle(whandle);
		NavigatorHelper.switchToIframe(getSession(), Application.APP_ACCOUNTS_FRAME_XPATH);	
		return accountsPage;
	}


	/**
	 * @param username
	 * @param password
	 * @return
	 */
	public void openAdminConsole(String username, String password)
	{
		spaceAdmin.click();
	}

}
