package com.enonic.autotests.pages;

import java.util.List;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.utils.TestUtils;
import com.google.common.base.Predicate;

public abstract class Page
{

	private TestSession session;

	private  Logger logger = Logger.getLogger(this.getClass());


	public Page( TestSession session )
	{
		this.session = session;
		PageFactory.initElements(session.getDriver(), this);
	}

	/**
	 * The constructor
	 * 
	 * @param driver
	 */
	public Page( WebDriver driver )
	{
		this.session = new TestSession();
		session.setDriver(driver);
		PageFactory.initElements(driver, this);
	}

	public void logError(String message)
	{
		logger.error(message);
		TestUtils.saveScreenshot(getSession());
	}
	
	/**
	 * Types text in input field.
	 * 
	 * @param input
	 *            input type=text
	 * @param text
	 *            text for input.
	 */
	public void clearAndType(WebElement input, String text)
	{
		if (session.getIsRemote() !=null && session.getIsRemote() )
		{
			input.sendKeys(Keys.chord(Keys.CONTROL, "a"), text);
		} else
		{

			String os = System.getProperty("os.name").toLowerCase();
			logger.info("clearAndType: OS System is " + os);
			if (os.indexOf("mac") >= 0)
			{
				input.sendKeys(Keys.chord(Keys.COMMAND, "a"), text);
			} else
			{
				logger.info("text will be typed: " + text);
				input.sendKeys(Keys.chord(Keys.CONTROL, "a"), " ");
				input.sendKeys(Keys.chord(Keys.CONTROL, "a"), text);
                                 logger.info("text in input: " + text);
			}

		}

	}
	public TestSession getSession()
	{
		return session;
	}
	

	public void setSession(TestSession session)
	{
		this.session = session;
	}

	public Logger getLogger()
	{
		return logger;
	}

	public WebElement findElement(By by)
	{
		FluentWait<By> fluentWait = new FluentWait<By>(by);
		fluentWait.pollingEvery(500, TimeUnit.MILLISECONDS);
		fluentWait.withTimeout(8l, TimeUnit.SECONDS);
		fluentWait.until(new Predicate<By>()
		{
			public boolean apply(By by)
			{
				try
				{
					return getDriver().findElement(by).isDisplayed();
				} catch (NoSuchElementException ex)
				{
					return false;
				}
			}
		});
		return getDriver().findElement(by);
	}

	public List<WebElement> findElements(By by)
	{
		return session.getDriver().findElements(by);
	}

	public WebDriver getDriver()
	{
		return session.getDriver();
	}
	
	public boolean isDynamicElementPresent(By locator, int tries)
	{
		getLogger().info("Get element by locator: " + locator.toString());
		long startTime = System.currentTimeMillis();
		getDriver().manage().timeouts().implicitlyWait(9, TimeUnit.SECONDS);
		boolean isFound = false;
		for (int i = 0; i <= tries; i++)
		{
			try
			{
				getDriver().findElement(locator);
				isFound = true;
				break;
			} catch (StaleElementReferenceException ser)
			{
				getLogger().info("ERROR: Stale element. " + locator.toString());

			} catch (NoSuchElementException nse)
			{
				getLogger().info("ERROR: No such element. " + locator.toString());

			} catch (Exception e)
			{
				getLogger().info(e.getMessage());
			}
		}
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		getLogger().info("Finished click after waiting for " + totalTime + " milliseconds.");
		getDriver().manage().timeouts().implicitlyWait(Application.DEFAULT_IMPLICITLY_WAIT, TimeUnit.SECONDS);
		return isFound;
	}
	/**
	 * @param locator
	 * @param tries
	 * @return
	 */
	public WebElement getDynamicElement(By locator, int tries)
	{
		getLogger().info("Get element by locator: " + locator.toString());
		long startTime = System.currentTimeMillis();
		getDriver().manage().timeouts().implicitlyWait(9, TimeUnit.SECONDS);
		WebElement we = null;
		for (int i = 0; i <= tries; i++)
		{

			getLogger().info("Locating remaining time: " + ((tries* 9) - (9 * (tries - i))) + " seconds.");
			try
			{
				we = getDriver().findElement(locator);
				getLogger().info("TestUtils.getDynamicElement: dynamic webelement was found!");
				return we;
			} catch (StaleElementReferenceException ser)
			{
				getLogger().info("ERROR: Stale element. " + locator.toString());

			} catch (NoSuchElementException nse)
			{
				getLogger().info("ERROR: No such element. " + locator.toString());

			} catch (Exception e)
			{
				getLogger().info(e.getMessage());
			}
		}
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		getLogger().info("Finished click after waiting for " + totalTime + " milliseconds.");
		getDriver().manage().timeouts().implicitlyWait(Application.IMPLICITLY_WAIT, TimeUnit.SECONDS);
		getLogger().info("getDynamicElement is  null: ");
		return we;
	}
	

	public WebElement findDynamicElement(final WebDriver driver, final String xpath, long timeout)
	{
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		return wait.until(new ExpectedCondition<WebElement>()
		{
			@Override
			public WebElement apply(WebDriver webDriver)
			{
				try
				{
					return driver.findElement(By.xpath(xpath));

				} catch (StaleElementReferenceException e)
				{
					return null;
				}
			}
		});
	}

	

	public WebElement scrollTableAndFind( String elementXpath, String scrollXpath)
	{
		WebElement element = null;
		List<WebElement> divScroll = getDriver().findElements(By.xpath(scrollXpath));
		if (divScroll.size() == 0)
		{
			throw new TestFrameworkException("Div was not found xpath: " + scrollXpath);
		}
		long gridHeight = (Long) ((JavascriptExecutor) getDriver()).executeScript("return arguments[0].scrollHeight", divScroll.get(0));

		for (int scrollTop = 0; scrollTop <= gridHeight;)
		{
			scrollTop += 40;
			((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollTop=arguments[1]", divScroll.get(0), scrollTop);
			element = getDriver().findElement(By.xpath(elementXpath));
			if (element.isDisplayed())
			{
				return element;
			}
		}
		return null;
	}
	
}
