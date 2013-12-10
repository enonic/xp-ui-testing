package com.enonic.autotests.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.TestSession;

public abstract class BaseModalDialog
{
	private TestSession session;
	
	BaseModalDialog(TestSession session)
	{
		this.session = session;
	}
	
	public TestSession getSession()
	{
		return session;
	}
	public void setSession(TestSession session)
	{
		this.session = session;
	}
	public List<WebElement> findElements(By by)
	{
		return session.getDriver().findElements(by);
	}
	
	public WebElement findElement(By by)
	{
		return session.getDriver().findElement(by);
	}

	public WebDriver getDriver()
	{
		return session.getDriver();
	}
	
}
