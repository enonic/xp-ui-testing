package com.enonic.wem.uitest.content

import com.enonic.autotests.exceptions.TestFrameworkException
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class ContentBrowsePanel_FilterPanel_Spec
    extends BaseGebSpec
{

    @Shared
    ContentBrowsePanel contentBrowsePanel

    @Shared
    String PAGE_CONTENT_TYPE_NAME = "Page"


    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }

    def "GIVEN No selections in filter WHEN Selecting one entry in any filter THEN Clean Filter link should appear"()
    {
        when:
        String label = contentBrowsePanel.getFilterPanel().selectEntryInContentTypesFilter( PAGE_CONTENT_TYPE_NAME )

        then:
        contentBrowsePanel.getContentNamesFromBrowsePanel().size() == parseFilterLabel( label )
    }

    def "GIVEN Selections in any filter WHEN clicking CleanFilter THEN CleanFilter link should dissapear"()
    {
        given:
        String label = contentBrowsePanel.getFilterPanel().selectEntryInContentTypesFilter( PAGE_CONTENT_TYPE_NAME )

        when:
        contentBrowsePanel.getFilterPanel().clickByCleanFilter()

        then:
        contentBrowsePanel.getFilterPanel().waitForClearFilterLinkNotvisible();
    }


    def "GIVEN Selections in any filter WHEN clicking CleanFilter THEN all selections should dissapear"()
    {
        given:
        String label = contentBrowsePanel.getFilterPanel().selectEntryInContentTypesFilter( PAGE_CONTENT_TYPE_NAME )

        when:
        contentBrowsePanel.getFilterPanel().clickByCleanFilter()

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
