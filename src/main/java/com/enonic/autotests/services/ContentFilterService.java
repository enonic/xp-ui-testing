package com.enonic.autotests.services;

import java.util.List;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.ContentFilterException;
import com.enonic.autotests.model.Space;
import com.enonic.autotests.model.cm.BaseAbstractContent;
import com.enonic.autotests.pages.cm.ContentTablePage;
import com.enonic.autotests.pages.cm.ContentFilterPanel;
import com.enonic.autotests.pages.cm.LastModified;

public class ContentFilterService
{
	public void doClearFilter(TestSession session)
	{
	
		ContentTablePage cmPage = new ContentTablePage(session);
		ContentFilterPanel filterPanel = cmPage.getContentFilter();
		filterPanel.doClearFilter();
	}

	public List<String> filterByQuery(TestSession session, String query)
	{
		// 1. open a 'content manager'
		ContentTablePage cmPage = NavigatorHelper.openContentManager(session);
		ContentFilterPanel filterPanel = cmPage.getContentFilter();
		filterPanel.executeQuery(query);
		List<String> names = cmPage.getShowedContentNames();
		return names;
	}
	
	public List<String> filterByDate(TestSession session, LastModified dateRange)
	{
		// 1. open a 'content manager'
		ContentTablePage cmPage = NavigatorHelper.openContentManager(session);
		ContentFilterPanel filterPanel = cmPage.getContentFilter();
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
		ContentTablePage cmPage = NavigatorHelper.openContentManager(session);
		ContentFilterPanel filterPanel = cmPage.getContentFilter();
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
	public ContentTablePage doFilterContentByContentTypeName(TestSession session, String contentTypeName)
	{
		// 1. open CM application(if not opened yet)
		ContentTablePage cmPage = NavigatorHelper.openContentManager(session);

		ContentFilterPanel filterPanel = cmPage.getContentFilter();
		// 2. verify filtering: select content type in the search panel and try
		// to find new added content in the table:
		filterPanel.doFilterByContentType(contentTypeName);
		//boolean isContentFiltered = true;
		return cmPage;

	}
}
