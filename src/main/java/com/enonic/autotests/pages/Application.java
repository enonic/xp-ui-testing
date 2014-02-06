package com.enonic.autotests.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.TestSession;

public class Application extends Page
{
	
	public static final long PAGELOAD_TIMEOUT = 4l;
	
    public static final long IMPLICITLY_WAIT = 4l;
    
	public static final long DEFAULT_IMPLICITLY_WAIT = 2l;
	
	public static final String APP_SPACE_ADMIN_FRAME_XPATH = "//iframe[contains(@src,'space-manager')]";
	
	public static final String APP_CONTENT_MANAGER_FRAME_XPATH = "//iframe[contains(@src,'content-manager')]";
	
	public static final String APP_SCHEMA_MANAGER_FRAME_XPATH = "//iframe[contains(@src,'schema-manager')]";
	
	public static final String APP_ACCOUNTS_FRAME_XPATH = "//iframe[contains(@src,'app-account.html')]";
	
	public static final String DIALOG_CLOSE_BUTTON_XPATH = "//img[@class='x-tool-close']";
	
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
