package com.enonic.wem.uitest.content

import com.enonic.autotests.exceptions.TestFrameworkException
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel;
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper;
import com.enonic.autotests.utils.TestUtils;
import com.enonic.autotests.vo.contentmanager.ArchiveContent;
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent;
import com.enonic.wem.api.content.ContentPath;
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class ContentBrowsePanel_FilterPanel_Spec
    extends BaseGebSpec
{

    @Shared
    ContentBrowsePanel contentBrowsePanel
	@Shared
	ContentBrowseFilterPanel filterPanel

    @Shared
    String PAGE_CONTENT_TYPE_NAME = "Page"


    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() )
		filterPanel = contentBrowsePanel.getFilterPanel()
    }

    def "GIVEN No selections in filter WHEN Selecting one entry in any filter THEN Clean Filter link should appear"()
    {
        when:
        String label = filterPanel.selectEntryInContentTypesFilter( PAGE_CONTENT_TYPE_NAME )
		TestUtils.saveScreenshot(getTestSession())

        then:
        contentBrowsePanel.getContentNamesFromBrowsePanel().size() == parseFilterLabel( label )
    }

    def "GIVEN Selections in any filter WHEN clicking CleanFilter THEN CleanFilter link should dissapear"()
    {
        given:
        String label = filterPanel.selectEntryInContentTypesFilter( PAGE_CONTENT_TYPE_NAME )
		
        when:
        filterPanel.clickByCleanFilter()
		contentBrowsePanel.waitsForSpinnerNotVisible();
		TestUtils.saveScreenshot(getTestSession(), "CleanFilter1")
		
        then:
		filterPanel.waitForClearFilterLinkNotvisible();
    }
	
	
	def "GIVEN Selections in any filter WHEN clicking CleanFilter THEN all selections should dissapear"()
	{
		given:
		String label = filterPanel.selectEntryInContentTypesFilter( PAGE_CONTENT_TYPE_NAME )
		
		when:
		contentBrowsePanel.getFilterPanel().clickByCleanFilter()
		contentBrowsePanel.waitsForSpinnerNotVisible();
		TestUtils.saveScreenshot(getTestSession(), "CleanFilter2")
		
		then:
		!contentBrowsePanel.getFilterPanel().isAnySelectionPresent()
	}
	
	
	

    int parseFilterLabel( String label )
    {
        int start = label.indexOf( "(" )
        int end = label.indexOf( ")" )
        if ( start == -1 || end == -1 )
        {
            throw new TestFrameworkException( "wrong label!" );
        }
        return Integer.valueOf( label.substring( start + 1, end ) )

    }
}
