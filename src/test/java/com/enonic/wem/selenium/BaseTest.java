package com.enonic.wem.selenium;

import java.io.IOException;



import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.logger.Logger;
import com.enonic.autotests.utils.BrowserUtils;

public class BaseTest
{

	protected Logger logger = Logger.getLogger();

	private ThreadLocal<TestSession> sessionRef = new ThreadLocal<TestSession>();

	@BeforeClass(alwaysRun = true)
	public void readDesiredCapabilities(ITestContext context)
	{
		logger.info("############### method readDesiredCapabilities started    ###################");
		TestSession testSession = new TestSession();

		String browser = (String) context.getCurrentXmlTest().getParameter("browser");
		if (browser == null)
		{
			throw new IllegalArgumentException("parameter browser was not specified! ");
		}
		logger.info("browser is  "+ browser );
		String browserVersion = (String) context.getCurrentXmlTest().getParameter("browserVersion");
		if (browserVersion == null)
		{
			logger.info("browserVersion was not specified! ");
		}else
		{
			logger.info("browser version  is:  "+ browserVersion );
		}
		String platform = (String) context.getCurrentXmlTest().getParameter("platform");
		if (platform == null)
		{
			throw new IllegalArgumentException("parameter platform was not specified! ");
		}
		logger.info("platform   is:  "+ platform );
		testSession.put(TestSession.BROWSER_NAME, browser);
		testSession.put(TestSession.BROWSER_VERSION, browserVersion);
		testSession.put(TestSession.PLATFORM, platform);
		String url = (String) context.getCurrentXmlTest().getParameter("base.url");
		if (url == null)
		{
			throw new IllegalArgumentException("parameter base url was not specified! ");
		}
		logger.info("base.url   is:  "+ url );
		testSession.put(TestSession.START_URL, url);
		String remoteParam = context.getCurrentXmlTest().getParameter("isRemote");
		if (remoteParam == null)
		{
			throw new IllegalArgumentException("parameter isRemote was not specified! ");
		}
		logger.info("isRemote   is:  "+ remoteParam );
		Boolean isRemote = Boolean.valueOf(remoteParam);
		if (isRemote != null)
		{
			testSession.put(TestSession.IS_REMOTE, isRemote);
			if (isRemote)
			{
				String hubUrl = (String) context.getCurrentXmlTest().getParameter("hub.url");
				if (hubUrl == null)
				{
					throw new IllegalArgumentException("parameter hubUrl was not specified! ");
				}
				logger.info("hubUrl   is:  "+ hubUrl );
				testSession.put(TestSession.HUB_URL, hubUrl);
			}else
			{
				platform = getPlatformName();
				
				System.out.println(platform);
				logger.info("The platform is:   " +platform);
				testSession.put(TestSession.PLATFORM, platform);
			}
		}
		sessionRef.set(testSession);
		logger.info("############### method readDesiredCapabilities finished    ###################");
	}

	public String getPlatformName()
	{
		String os = System.getProperty("os.name").toLowerCase();
		if(os.indexOf("win")>=0)
		{
			return "windows";
		}else if( os.indexOf("mac") >= 0)
		{
			return "mac";
		}
		else if (os.indexOf("nux") >= 0)
		{
			return "linux";
		}
		return null;
	}

	@BeforeMethod
	public void openBrowser() throws IOException
	{
		BrowserUtils.createDriverAndOpenBrowser(getTestSession());

	}

	@AfterTest
	public  void clearSession()
	{
		logger.info("end of test:"+ this.getClass().getName()+ "try to clear session "+sessionRef.get().getBrowserName());
		System.out.println("end of test:"+ this.getClass().getName()+ "try to clear session "+sessionRef.get().getBrowserName());
		sessionRef.set(null);
	}

	@AfterMethod
	public void closeBrowser()
	{
		getTestSession().closeBrowser();

	}

	public TestSession getTestSession()
	{
		TestSession sess = sessionRef.get();
		if(sess == null)
		{
			logger.info("BaseTest: testsession is null!" );
		}
		return sess;
	}

}
