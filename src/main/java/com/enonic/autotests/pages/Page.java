package com.enonic.autotests.pages;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.logger.Logger;
import com.google.common.base.Predicate;

public abstract class Page
{

	private TestSession session;

	private Logger logger = Logger.getLogger();

	public Page( TestSession session )
	{
		this.session = session;
		PageFactory.initElements(session.getDriver(), this);
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
		fluentWait.withTimeout(4l, TimeUnit.SECONDS);
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

}
