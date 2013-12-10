package com.enonic.autotests;

public interface AppConstants {
	
	final long PAGELOAD_TIMEOUT = 4l;
	
	final long IMPLICITLY_WAIT = 4l;
	
	final String APP_SPACE_ADMIN_FRAME_XPATH = "//iframe[contains(@src,'space-manager')]";
	
	final String APP_CONTENT_MANAGER_FRAME_XPATH = "//iframe[contains(@src,'content-manager')]";
	
	final String APP_SCHEMA_MANAGER_FRAME_XPATH = "//iframe[contains(@src,'schema-manager')]";
	
	final String APP_ACCOUNTS_FRAME_XPATH = "//iframe[contains(@src,'app-account.html')]";
	
	final String DIALOG_CLOSE_BUTTON_XPATH = "//img[@class='x-tool-close']";
}
