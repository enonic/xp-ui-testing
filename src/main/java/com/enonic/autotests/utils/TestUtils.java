package com.enonic.autotests.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.logger.Logger;
import com.enonic.autotests.pages.Application;
import com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog.ContentTypeName;

public class TestUtils
{
	public static Logger logger = Logger.getLogger();
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd-HH-mm-ss";

	private static TestUtils instance;

	

	/**
	 * @return
	 */
	public static synchronized TestUtils getInstance()
	{
		if (instance == null)
		{
			instance = new TestUtils();

		}
		return instance;
	}

	public String buildFullNameOfContent(String contentName, String... parentNames)
	{
		StringBuilder builder = new StringBuilder();
		if(parentNames == null)
		{
			builder.append("/").append(contentName);
			return builder.toString();
		}
		if (parentNames.length == 0)
		{
			builder.append(contentName).append("/");
			return builder.toString();
		}
		builder.append(parentNames[0].toLowerCase()).append("/");
		for (int i = 1; i < parentNames.length; i++)
		{
			builder.append(parentNames[i].toLowerCase()).append("/");
		}

		String fullContentName = builder.append(contentName).toString();
		return fullContentName;
	}

	/**
	 * The Default constructor.
	 */
	private TestUtils()
	{

	}


	public ContentTypeName getContentType(String ctype)
	{
		ContentTypeName result = null;
		ContentTypeName[] values = ContentTypeName.values();
		for (ContentTypeName val : values)
		{
			if (val.getValue().equalsIgnoreCase(ctype))
			{
				result = val;

			}
		}
		return result;
	}


	/**
	 * @param screenshotFileName
	 * @param driver
	 */
	public String saveScreenshot(final TestSession testSession)
	{
		WebDriver driver = testSession.getDriver();
		String fileName = timeNow() + ".png";
		File folder  = new File("target/screenshots");
		//File folder = new File(System.getProperty("user.dir") + File.separator + "snapshots");

		if (!folder.exists())
		{
			if (!folder.mkdir())
			{
				System.out.println("Folder for snapshots was not created ");
			} else
			{
				System.out.println("Folder for snapshots was created " + folder.getAbsolutePath());
			}
		}
		File screenshot = null;

		//if ((Boolean) testSession.get(TestSession.IS_REMOTE))
		//{

		//	WebDriver augmentedDriver = new Augmenter().augment(driver);
		//	screenshot = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
		//}// else
		{
			screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		}

		String fullFileName = folder.getAbsolutePath() + File.separator + fileName;

		try
		{
			FileUtils.copyFile(screenshot, new File(fullFileName));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block

		}
		return fileName;
	}

	public String timeNow()
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());

	}

	public void clickByLocator(final By locator, WebDriver driver)
	{
		WebElement myDynamicElement = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(locator));
		myDynamicElement.click();
	}

	/**
	 * @param locator
	 * @param driver
	 */
	public void clickByElement(final By locator, final WebDriver driver)
	{
		final long startTime = System.currentTimeMillis();
		driver.manage().timeouts().implicitlyWait(Application.IMPLICITLY_WAIT, TimeUnit.SECONDS);
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(90000, TimeUnit.MILLISECONDS).pollingEvery(5500, TimeUnit.MILLISECONDS);
		// .ignoring( StaleElementReferenceException.class );
		wait.until(new ExpectedCondition<Boolean>()
		{
			@Override
			public Boolean apply(WebDriver webDriver)
			{
				try
				{
					webDriver.findElement(locator).click();
					return true;
				} catch (StaleElementReferenceException e)
				{
					logger.info(e.getMessage() + "\n");
					logger.info("Trying again...");
					return false;
				}
			}
		});
		final long endTime = System.currentTimeMillis();
		logger.info("clickByElement time is " + (endTime - startTime));
		driver.manage().timeouts().implicitlyWait(Application.DEFAULT_IMPLICITLY_WAIT, TimeUnit.SECONDS);
	}

	public String getNotificationMessage(final By locator, final WebDriver driver, long timeout)
	{
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		return element.getText();

	}

	/**
	 * @param by
	 * @param driver
	 * @return
	 */
	public boolean checkIfDisplayed(final By by, final WebDriver driver)
	{
		List<WebElement> elements = driver.findElements(by);
		return ((elements.size() > 0) && (elements.get(0).isDisplayed()));
	}

	public WebElement getIfDisplayed(final By by, final WebDriver driver)
	{
		List<WebElement> elements = driver.findElements(by);
		if ((elements.size() > 0) && (elements.get(0).isDisplayed()))
		{
			return elements.get(0);
		}
		return null;
	}

	public String createTempFile(String s)
	{
		try
		{
			File f = File.createTempFile("uploadTest", "tempfile");
			f.deleteOnExit();
			writeStringToFile(s, f);
			return f.getAbsolutePath();
		} catch (Exception e)
		{
			throw new TestFrameworkException("Error during creation TMP-file");

		}
	}

	public void writeStringToFile(String s, File file) throws IOException
	{
		FileOutputStream in = null;
		try
		{
			in = new FileOutputStream(file);
			FileChannel fchan = in.getChannel();
			BufferedWriter bf = new BufferedWriter(Channels.newWriter(fchan, "UTF-8"));
			bf.write(s);
			bf.close();
		} finally
		{
			if (in != null)
			{
				in.close();
			}
		}
	}

}
