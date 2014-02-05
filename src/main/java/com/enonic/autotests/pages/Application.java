package com.enonic.autotests.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;

public class Application extends Page
{
	public static final String HOME_BUTTON_XPATH = "//div[contains(@class,'appbar')]/button[@class='launcher-button']";
	@FindBy(xpath = HOME_BUTTON_XPATH)
	protected WebElement gotoHomeButton;

	public Application( TestSession session )
	{
		super(session);
	}

	public HomePage openHomePage()
	{
		gotoHomeButton.click();
		HomePage page = new HomePage(getSession());
		page.waitUntilAllFramesLoaded();
		return page;
	}

}
