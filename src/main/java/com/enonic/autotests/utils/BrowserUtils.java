package com.enonic.autotests.utils;

import java.io.IOException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.enonic.autotests.BrowserName;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;

public class BrowserUtils {
	public static Logger logger = Logger.getLogger(BrowserUtils.class);

	/**
	 * @param browser
	 * @return
	 */
	public static void createDriverAndOpenBrowser(final TestSession testSession) throws IOException {
		WebDriver driver = null;

		String browserName = (String) testSession.get(TestSession.BROWSER_NAME);
		BrowserName cfgBrowser = BrowserName.findByValue(browserName);
		Boolean isRemote = (Boolean) testSession.get(TestSession.IS_REMOTE);

		if (isRemote != null && isRemote) {
			String hubUrl = (String) testSession.get(TestSession.HUB_URL);
			Capabilities desiredCapabilities = createDesiredCapabilities(testSession);
			driver = new RemoteWebDriver(new URL(hubUrl), desiredCapabilities);
			testSession.put(TestSession.WEBDRIVER, driver);
			return;
		}

		if (cfgBrowser == null) {
			throw new RuntimeException("browser was not specified in the suite file!");
		}
		if (cfgBrowser.equals(BrowserName.FIREFOX)) {
			FirefoxProfile myProfile = new FirefoxProfile();
			myProfile.setPreference("capability.policy.default.Window.frameElement.get", "allAccess");
			driver = new FirefoxDriver(myProfile);
			driver.manage().window().maximize();
		} else if (cfgBrowser.equals(BrowserName.CHROME)) {
			 //TODO create app properties for path to chrome driver
			System.setProperty("webdriver.chrome.driver","c:/chrome/chromedriver.exe");
			driver = new ChromeDriver();

		} else if (cfgBrowser.equals(BrowserName.IE)) {
			 System.setProperty("webdriver.ie.driver", "c:/ie/IEDriverServer.exe"); 
			driver = new InternetExplorerDriver();
			 
		} else if (cfgBrowser.equals(BrowserName.HTMLUNIT)) {
			driver = new HtmlUnitDriver();
		} else {
			throw new RuntimeException("Driver not supported:" + cfgBrowser.getName());
		}

		testSession.put(TestSession.WEBDRIVER, driver);

	}

	private static Capabilities createDesiredCapabilities(final TestSession testSession) {
		String browserName = (String) testSession.get(TestSession.BROWSER_NAME);

		String browserVersion = (String) testSession.get(TestSession.BROWSER_VERSION);
		String platform = (String) testSession.get(TestSession.PLATFORM);

		BrowserName cfgBrowser = BrowserName.findByValue(browserName);
		DesiredCapabilities capability = null;

		if (cfgBrowser.equals(BrowserName.FIREFOX)) {
			capability = DesiredCapabilities.firefox();
			capability.setPlatform(getPLatform(platform));
			capability.setVersion(browserVersion);

		} else if (cfgBrowser.equals(BrowserName.CHROME)) {
			capability = DesiredCapabilities.chrome();
			capability.setCapability("chrome.binary", "c:/chrome/chromedriver.exe");
			capability.setPlatform(getPLatform(platform));
			
		} else if (cfgBrowser.equals(BrowserName.IE)) {
			capability = DesiredCapabilities.internetExplorer();
			capability.setPlatform(Platform.WINDOWS);
			capability.setVersion(browserVersion);
		} else {
			throw new TestFrameworkException("support of browser " + cfgBrowser.getName() + " not implemented yet");
		}
		logger.info("DesiredCapabilities for RemoteWebDriver uses:  platform:" + platform + " browser:" + browserName + "version:" + browserVersion
				+ " created");
		return capability;
	}

	private static Platform getPLatform(String platform) {
		if (platform.equalsIgnoreCase("windows")) {
			return Platform.WINDOWS;
		} else if (platform.equalsIgnoreCase("linux")) {
			return Platform.LINUX;
		} else if (platform.equalsIgnoreCase("mac")) {
			return Platform.MAC;
		} else
			return Platform.ANY;
	}
}
