package com.enonic.autotests.services;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel;
import com.enonic.autotests.pages.contentmanager.browsepanel.FilterPanelLastModified;
import com.enonic.autotests.vo.Space;

import java.util.List;

public class ContentFilterService
{
	public void doClearFilter(TestSession session)
	{
	
		ContentBrowsePanel cmPage = new ContentBrowsePanel(session);
		ContentBrowseFilterPanel filterPanel = cmPage.getContentBrowseFilterPanel();
		filterPanel.doClearFilter();
	}

	public List<String> doFilterByText(TestSession session, String text)
	{
		// 1. open a 'content manager'
		ContentBrowsePanel cmPage = NavigatorHelper.openContentApp(session);
		ContentBrowseFilterPanel filterPanel = cmPage.getContentBrowseFilterPanel();
		filterPanel.doSearchByText(text);
		List<String> names = cmPage.getShowedContentNames();
		return names;
	}
	
	public List<String> doFilterByDate(TestSession session, FilterPanelLastModified dateRange)
	{
		// 1. open a 'content manager'
		ContentBrowsePanel cmPage = NavigatorHelper.openContentApp(session);
		ContentBrowseFilterPanel filterPanel = cmPage.getContentBrowseFilterPanel();
		filterPanel.doFilterByDate(dateRange);
		List<String> names = cmPage.getShowedContentNames();
		return names;
	}

	/**
	 * Select space-name in the 'search panel' and gets all names of contents
	 * from the table.
	 * 
	 * @param session
	 * @param space
	 * @param content
	 * @return
	 */
	public List<String> seletcSpaceAndCheckContent(TestSession session, Space space)
	{
		// 1. open a 'content manager'
		ContentBrowsePanel cmPage = NavigatorHelper.openContentApp(session);
		ContentBrowseFilterPanel filterPanel = cmPage.getContentBrowseFilterPanel();
		// 2. select space in the search panel and filter by space-name:
		filterPanel.doFilterBySpaceDisplayName(space.getDisplayName());
		// 3. content were filtered, gets all names from the table :
		List<String> names = cmPage.getShowedContentNames();
		return names;
	}

	/**
	 * Selects the 'content type' in the filter panel and checks contents. <br>
	 * Tries to find new added content in the table, when filtering by 'Content
	 * Type' was applied.
	 * 
	 * @param session
	 * @param contentToAdd
	 * @param parentNames
	 * @return
	 */
	public ContentBrowsePanel doFilterContentByContentTypeName(TestSession session, String contentTypeName)
	{
		// 1. open CM application(if not opened yet)
		ContentBrowsePanel cmPage = NavigatorHelper.openContentApp(session);

		ContentBrowseFilterPanel filterPanel = cmPage.getContentBrowseFilterPanel();
		// 2. verify filtering: select content type in the search panel and try
		// to find new added content in the table:
		filterPanel.doFilterByContentType(contentTypeName);
		//boolean isContentFiltered = true;
		return cmPage;

	}
}
