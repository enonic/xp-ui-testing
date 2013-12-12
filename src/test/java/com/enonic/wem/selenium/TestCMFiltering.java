package com.enonic.wem.selenium;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.model.cm.ArchiveContent;
import com.enonic.autotests.pages.cm.ContentTablePage;
import com.enonic.autotests.pages.cm.SelectContentTypeDialog.ContentTypeName;
import com.enonic.autotests.services.ContentFilterService;
import com.enonic.autotests.services.ContentManagerService;

public class TestCMFiltering extends BaseTest
{

	private ContentManagerService contentManagerService = new ContentManagerService();


	private String REPONAME = "/bluman-trampoliner";
	private ContentFilterService filterService = new ContentFilterService();

	

	// TODO this test failed due the CMS-2616
	@Test(description = "filtering by content-type: select a checkbox with label the same as name of content type, and verify that content present in the table")
	public void testAddNewArchiveAndFilter()
	{
		String name = "archive-filtering" + Math.abs( new Random().nextInt() );
		ArchiveContent archive = ArchiveContent.builder().withName(name).withDisplayName("archive-filtering").withType(ContentTypeName.ARCHIVE.getValue()).build();
		String[] parentNames = new String[]{REPONAME};
		archive.setParentNames(parentNames);
		// 1. add a content to the space and close wizard:
		contentManagerService.addContent(getTestSession(), archive, true);
		// 2. do filter by ContentType and verify, that only this type of content is present on the page:
		ContentTablePage contentGrid = filterService.doFilterContentByContentTypeName(getTestSession(), archive.getContentTypeName());
		
		boolean isContentFiltered = true;
		boolean result = contentGrid.findContentInTable(archive, 1l, isContentFiltered );
		Assert.assertTrue(result, "Filter by Content Type applyed, but content with ctype equals  " + archive.getContentTypeName() + " was not showed");
		
	}

}
