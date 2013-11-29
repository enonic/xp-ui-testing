package com.enonic.autotests.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.accounts.AccountsPage;
import com.enonic.autotests.pages.cm.CMSpacesPage;
import com.enonic.autotests.pages.schemamanager.SchemasPage;
import com.enonic.autotests.pages.spaceadmin.SpaceAdminPage;
import com.enonic.autotests.services.NavigatorHelper;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page Object for 'Home' page. Version 5.0
 * 
 */
public class HomePage extends Page
{
	public static String TITLE = "Enonic CMS - Boot Page";

	@FindBy(xpath = "//div[@class = 'name-container' and text()='Content Manager']")
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
			getSession().setWindowHandle(getSession().getDriver().getWindowHandle());
		}
		
		// open page via the driver.get(BASE_URL)
		getSession().getDriver().get(getSession().getBaseUrl());

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

	public void waitUntilAllFramesLoaded()
	{
		
		TestUtils.getInstance().waitUntilVisible(getSession(), By.xpath("//div[@class = 'name-container' and text()='Space Admin']"));
		TestUtils.getInstance().waitUntilVisible(getSession(), By.xpath("//div[@class = 'name-container' and text()='Accounts']"));
		TestUtils.getInstance().waitUntilVisible(getSession(), By.xpath("//div[@class = 'name-container' and text()='Schema Manager']"));

	}
	public SchemasPage openSchemaManagerApplication()
	{
		schemaManager.click();
		
		String whandle = getSession().getDriver().getWindowHandle();
		getSession().setWindowHandle(whandle);
		NavigatorHelper.switchToIframe(getSession(), AppConstants.APP_SCHEMA_MANAGER_FRAME_XPATH);
		
		return new SchemasPage(getSession());
	}

	public SpaceAdminPage openSpaceAdminApplication()
	{
		spaceAdmin.click();
		
		SpaceAdminPage page = new SpaceAdminPage(getSession());
		page.waituntilPageLoaded(TestUtils.TIMEOUT_IMPLICIT);
		//String whandle = getSession().getDriver().getWindowHandle();
		//getSession().setWindowHandle(whandle);
		NavigatorHelper.switchToIframe(getSession(), AppConstants.APP_SPACE_ADMIN_FRAME_XPATH);
		page.waituntilToolbarLoaded(2l);
		
		return  page;
	}

	

	public CMSpacesPage openContentManagerApplication()
	{
		contentManager.click();
		
		String whandle = getSession().getDriver().getWindowHandle();
		getSession().setWindowHandle(whandle);
		NavigatorHelper.switchToIframe(getSession(), AppConstants.APP_CONTENT_MANAGER_FRAME_XPATH);
		
		return new CMSpacesPage(getSession());
	}

	public AccountsPage openAccountsApplication()
	{
		accounts.click();
		AccountsPage accountsPage = new AccountsPage(getSession());
		accountsPage.waituntilPageLoaded(2l);
		
		String whandle = getSession().getDriver().getWindowHandle();
		getSession().setWindowHandle(whandle);
		NavigatorHelper.switchToIframe(getSession(), AppConstants.APP_ACCOUNTS_FRAME_XPATH);
		
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

		TestUtils.getInstance().saveScreenshot(getSession());
	}

}
