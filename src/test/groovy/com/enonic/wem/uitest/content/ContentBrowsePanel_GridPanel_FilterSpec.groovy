package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel.ContenTypeDispalyNames
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class ContentBrowsePanel_GridPanel_FilterSpec
    extends BaseGebSpec
{

    @Shared
    ContentBrowsePanel contentBrowsePanel

    @Shared
    ContentBrowseFilterPanel filterPanel

    @Shared
    String PREDEFINED_FOLDER_NAME = "bildearkiv";

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() )
        filterPanel = contentBrowsePanel.getFilterPanel()
    }

    def "GIVEN No selections in filter WHEN Selecting one entry in ContentTypes-filter THEN all existing Content of the selected type should be listed in gridPanel"()
    {
        given:
        boolean isClearFilterPresent = filterPanel.waitForClearFilterLinkNotvisible()

        when:
        filterPanel.selectEntryInContentTypesFilter( ContenTypeDispalyNames.PAGE.getValue() )
        contentBrowsePanel.waitsForSpinnerNotVisible( 2 )
        Integer numberOfFilteredContent = filterPanel.getNumberFilteredByContenttype( ContenTypeDispalyNames.PAGE.getValue() )


        then:
        numberOfFilteredContent == contentBrowsePanel.getContentNamesFromBrowsePanel().size()
    }

    def "GIVEN Selections in any filter WHEN clicking clean filter THEN initial grid view displayed "()
    {
        given:
        filterPanel.selectEntryInContentTypesFilter( ContenTypeDispalyNames.PAGE.getValue() )

        when:
        boolean beforeClean = contentBrowsePanel.exists( ContentPath.from( PREDEFINED_FOLDER_NAME ) )
        filterPanel.clickByCleanFilter()
        contentBrowsePanel.waitsForSpinnerNotVisible( 2 )

        then:
        !beforeClean && contentBrowsePanel.exists( ContentPath.from( PREDEFINED_FOLDER_NAME ) )
    }

    def "GIVEN One selection in ContentTypes-filter WHEN Selecting one additional entry in ContentTypes-filter THEN all existing Content of the both selected types should be listed in gridPanel"()
    {
        given:
        filterPanel.selectEntryInContentTypesFilter( ContenTypeDispalyNames.PAGE.getValue() )
        contentBrowsePanel.waitsForSpinnerNotVisible( 1 )
        Integer numberOfPages = filterPanel.getNumberFilteredByContenttype( ContenTypeDispalyNames.PAGE.getValue() )

        when:
        filterPanel.selectEntryInContentTypesFilter( ContenTypeDispalyNames.FOLDER.getValue() )
        contentBrowsePanel.waitsForSpinnerNotVisible( 1 )
        Integer numberOfFolder = filterPanel.getNumberFilteredByContenttype( ContenTypeDispalyNames.FOLDER.getValue() )


        then:
        ( numberOfFolder + numberOfPages ) == contentBrowsePanel.getContentNamesFromBrowsePanel().size()
    }

    def "GIVEN One one selection in any filter WHEN deselecting selection THEN initial grid view displayed "()
    {
        given:
        filterPanel.selectEntryInContentTypesFilter( ContenTypeDispalyNames.PAGE.getValue() )
        contentBrowsePanel.waitsForSpinnerNotVisible( 1 )
        boolean existsBeforeUnselect = contentBrowsePanel.exists( ContentPath.from( PREDEFINED_FOLDER_NAME ) )

        when:
        filterPanel.deSelectEntryInContentTypesFilter( ContenTypeDispalyNames.PAGE.getValue() )
        contentBrowsePanel.waitsForSpinnerNotVisible( 1 )


        then:
        !existsBeforeUnselect && contentBrowsePanel.exists( ContentPath.from( PREDEFINED_FOLDER_NAME ) )
    }

    def "GIVEN empty text-search WHEN adding text-search THEN all Content matching the text-search should be listed in gridPanel"()
    {
        given:
        String text = filterPanel.typeSearchText( "" )
        contentBrowsePanel.waitsForSpinnerNotVisible()

        when:
        filterPanel.typeSearchText( PREDEFINED_FOLDER_NAME )
        contentBrowsePanel.waitsForSpinnerNotVisible()
        TestUtils.saveScreenshot( getTestSession(), "text-search1" )

        then:
        contentBrowsePanel.exists( ContentPath.from( PREDEFINED_FOLDER_NAME ) ) &&
            contentBrowsePanel.getContentNamesFromBrowsePanel().size() == 1
    }

    def "GIVEN any value in text-search WHEN clicking clean filter THEN initial grid view displayed"()
    {
        given:
        filterPanel.typeSearchText( PREDEFINED_FOLDER_NAME )
        contentBrowsePanel.waitsForSpinnerNotVisible()

        when:
        filterPanel.clickByCleanFilter()
        contentBrowsePanel.waitsForSpinnerNotVisible()
        TestUtils.saveScreenshot( getTestSession(), "text-search2" )

        then:
        contentBrowsePanel.exists( ContentPath.from( "homepage" ) ) && contentBrowsePanel.exists( ContentPath.from( "intranet" ) )
    }


}
