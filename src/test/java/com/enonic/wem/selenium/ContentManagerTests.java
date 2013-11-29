package com.enonic.wem.selenium;

import java.util.Random;

import org.testng.annotations.Test;

import com.enonic.autotests.model.Space;
import com.enonic.autotests.pages.spaceadmin.SpaceAdminPage;
import com.enonic.autotests.services.SpaceAdminService;

/**
 * 
 *
 */
public class ContentManagerTests extends BaseTest
{
	private static final String REPOSITORY_KEY = "repository_key";


	private SpaceAdminService spaceService = new SpaceAdminService();

	@Test(description ="create space for content")
	public void createSpace()
	{
		String repoName = "testcm" + Math.abs( new Random().nextInt() );
		Space repository = new Space();
		repository.setName(repoName);
		repository.setDisplayName("TestCM");
		repository.setTemplate("Tpl1");	
		SpaceAdminPage page = spaceService.createNewSpace(getTestSession(), repository, true);
		getTestSession().put(REPOSITORY_KEY, repository);
		
		// TODO this part is blocked: BUG - "when customer saved a space and try to close wizard- dialog appears."
		//boolean result = page.checkIsSpacePresentInTable(repository) ;
		//Assert.assertTrue(result,"Space with name 'testcm' was not crated");
	}

	
}
