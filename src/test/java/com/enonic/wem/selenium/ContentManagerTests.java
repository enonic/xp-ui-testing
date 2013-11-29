package com.enonic.wem.selenium;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.services.ContentManagerService;

/**
 * 
 *
 */
public class ContentManagerTests extends BaseTest
{

	private ContentManagerService cManagerService = new ContentManagerService();
	

	@Test(description ="Opens ")
	public void createSpace()
	{
		boolean result = cManagerService.openContentManagerAppAndVerify(getTestSession());
		Assert.assertTrue(result);
	}

	
}
