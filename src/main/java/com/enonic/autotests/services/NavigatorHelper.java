package com.enonic.autotests.services;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.logger.Logger;
import com.enonic.autotests.model.User;
import com.enonic.autotests.pages.HomePage;
import com.enonic.autotests.pages.accounts.AccountsPage;
import com.enonic.autotests.pages.cm.CMSpacesPage;
import com.enonic.autotests.pages.schemamanager.SchemasPage;
import com.enonic.autotests.pages.spaceadmin.SpaceAdminPage;
import com.enonic.autotests.utils.TestUtils;

public class NavigatorHelper
{

	/**
	 * Opens 'Space Admin' application.
	 * 
	 * @param testSession {@link TestSession} instance
	 * @return {@link SpaceAdminPage} instance.
	 */
	public static SpaceAdminPage openSpaceAdmin(TestSession testSession)
	{

		HomePage home = loginAndOpenHomePage(testSession);
		return home.openSpaceAdminApplication();
	}

	/**
	 * Opens 'Content Manager' application.
	 * 
	 * @param testSession {@link TestSession} instance.
	 * @return {@link SpaceAdminPage} instance.
	 */
	public static CMSpacesPage openContentManager(TestSession testSession)
	{
//		if(CMSpacesPage.isOpened(testSession))
//		{
//			return new CMSpacesPage(testSession);
//		}else
		//{
			HomePage home = loginAndOpenHomePage(testSession);
			CMSpacesPage cmPage = home.openContentManagerApplication();
			cmPage.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
			return cmPage;
		//}
		
	}
	
	public static SchemasPage openSchemaManager(TestSession testSession)
	{
		if(CMSpacesPage.isOpened(testSession))
		{
			return new SchemasPage(testSession);
		}else
		{
			HomePage home = loginAndOpenHomePage(testSession);
			SchemasPage schemasPage = home.openSchemaManagerApplication();
			schemasPage.waituntilPageLoaded(TestUtils.TIMEOUT_IMPLICIT);
			return schemasPage;
		}
		
	}

	/**
	 * Open 'Home' page, click by 'Accounts' link and open application's page.
	 * 
	 * @param testSession {@link TestSession} instance.
	 * @return {@link AccountsPage} instance.
	 */
	public static AccountsPage openAccounts(TestSession testSession)
	{
		HomePage home = loginAndOpenHomePage(testSession);
		return home.openAccountsApplication();
	}

	/**
	 * @param testSession {@link TestSession} instance.
	 * @param iframeXpath
	 *            frame's xpath.
	 */
	public static void switchToIframe(TestSession testSession, String iframeXpath)
	{
		String whandle = testSession.getDriver().getWindowHandle();
		testSession.getDriver().switchTo().window(whandle);
		List<WebElement> frames = testSession.getDriver().findElements(By.xpath(iframeXpath));
		if (frames.size() == 0)
		{
			throw new TestFrameworkException("Unable to switch to the iframe" + iframeXpath);
		}
		testSession.getDriver().switchTo().frame(frames.get(0));
	}
	
	/**
	 * 'Login' to cms and opens the 'Home' page that contains links to all applications.
	 * 
	 * @param testSession {@link TestSession} instance.
	 * @return {@link HomePage} instance.
	 */
	public static HomePage loginAndOpenHomePage(TestSession testSession)
	{
		User user = testSession.getCurrentUser();
		HomePage home = new HomePage(testSession);
		if (user != null)
		{
			home.open(user.getUserInfo().getName(), user.getUserInfo().getPassword());
		} else
		{
			home.open("admin", "pass");
		}
		return home;
	}

}
