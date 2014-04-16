package com.enonic.wem.uitest.content

import java.util.List;

import spock.lang.Ignore;
import spock.lang.Shared

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.SleepHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.wem.api.content.ContentPath;
import com.enonic.wem.uitest.BaseGebSpec

class ContentBrowsePanel_GridPanel_Spec  extends BaseGebSpec
{

	
	@Shared
	ContentBrowsePanel contentBrowsePanel;
	@Shared
	String BILDERAKIV = "bildearkiv";


	def setup()
	{
		go "admin"
		contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
	}
	
	def "GIVEN Content listed on root WHEN no selection THEN all rows are white"()
	{
		given:		
		List<String> contentNames = contentBrowsePanel.getContentNamesFromBrowsePanel();	
		
		expect:
		contentBrowsePanel.getSelectedRowsNumber() == 0 && contentNames.size()>0;
			
	}
	
	def "GIVEN Content listed on root WHEN first is clicked THEN first row is blue"()
	{
		given:
		List<String> contentNames = contentBrowsePanel.getContentNamesFromBrowsePanel();
		
		when:
		contentBrowsePanel.clickCheckboxAndSelectRow(ContentPath.from(contentNames.get(0)));
		
		then:
		contentBrowsePanel.getSelectedRowsNumber() == 1;
	}
	
	def "GIVEN a Content selected WHEN spacebar is typed THEN row is no longer selected"()
	{
		given:
		List<String> contentNames = contentBrowsePanel.getContentNamesFromBrowsePanel();
		contentBrowsePanel.clickCheckboxAndSelectRow(ContentPath.from(contentNames.get(0)));
		
		when:
		contentBrowsePanel.pressSpacebarOnCheckbox(ContentPath.from(contentNames.get(0))); 
		
		then:
		TestUtils.saveScreenshot(getTestSession(), "spacebartest");
		contentBrowsePanel.getSelectedRowsNumber() == 0;
	}
	
	def "GIVEN a Content selected WHEN 'Clear selection'-link is clicked THEN row is no longer selected"()
	{
		given:
		List<String> contentNames = contentBrowsePanel.getContentNamesFromBrowsePanel();
		contentBrowsePanel.clickCheckboxAndSelectRow(ContentPath.from(contentNames.get(0)));
		
		when:
		contentBrowsePanel.doClearSelection();
		
		then:
		contentBrowsePanel.getSelectedRowsNumber() == 0;
	}
	
	def "GIVEN no Content selected WHEN 'Select all'-link is clicked THEN all rows are selected"()
	{
		given:
		contentBrowsePanel.doClearSelection();
		
		when:
		int selectedNumber = contentBrowsePanel.doSelectAll();
		
		then:
		contentBrowsePanel.getContentNamesFromBrowsePanel().size() == selectedNumber;
	}
	
	def "GIVEN a Content on root having a child WHEN listed THEN expander is shown"()
	{
		given:
		List<String> contentNames = contentBrowsePanel.getContentNamesFromBrowsePanel();
		
		expect:
		getTestContentName(contentNames) !=null && contentBrowsePanel.isExpanderPresent(ContentPath.from(BILDERAKIV))
	}
	
	String getTestContentName(List<String> contentNames)
	{
		for(String name: contentNames)
		{
			if(name.contains(BILDERAKIV)){
				return name;
			}
		}
		return null;
	}
}
