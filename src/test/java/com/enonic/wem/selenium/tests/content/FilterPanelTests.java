package com.enonic.wem.selenium.tests.content;

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentGridPage;
import com.enonic.autotests.pages.contentmanager.browsepanel.NewContentDialog.ContentTypeName;
import com.enonic.autotests.services.ContentFilterService;
import com.enonic.autotests.vo.contentmanager.ArchiveContent;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Random;

public class FilterPanelTests extends BaseContentManagerTest
{
	ContentFilterService filterService = new ContentFilterService();

	// TODO this test failed due the CMS-2616
	@Test(description = "filtering by content-type: select a checkbox with label the same as name of content type, and verify that content present in the table")
	public void test_filter_content_by_name_of_contenttype()
	{
		String name = "archive-filtering" + Math.abs( new Random().nextInt() );
		ArchiveContent archive = ArchiveContent.builder().withName(name).withDisplayName("archive-filtering").withType(ContentTypeName.ARCHIVE.getValue()).build();
		String[] parentNames = new String[]{REPONAME};
		archive.setParentNames(parentNames);
		// 1. add a content to the space and close wizard:
		cManagerService.addContent(getTestSession(), archive, true);
		
		// 2. do filter by ContentType and verify, that only this type of content is present on the page:
		ContentGridPage contentGrid = filterService.doFilterContentByContentTypeName(getTestSession(), archive.getContentTypeName());
		
		boolean isContentFiltered = true;
		boolean result = contentGrid.findContentInTable(archive, TEST_TIMEOUT, isContentFiltered );
		Assert.assertTrue(result, "Filter by Content Type applyed, but content with ctype equals  " + archive.getContentTypeName() + " was not showed");
		
	}

}
