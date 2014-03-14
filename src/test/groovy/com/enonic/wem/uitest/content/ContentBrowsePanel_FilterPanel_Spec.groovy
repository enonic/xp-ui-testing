package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.browsepanel.FilterPanelLastModified
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent
import com.enonic.autotests.vo.contentmanager.FolderContent
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Ignore
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
        TestUtils.saveScreenshot( getTestSession() )

        then:
        contentBrowsePanel.getContentNamesFromBrowsePanel().size() == TestUtils.getNumberFromFilterLabel( label )
    }

    def "GIVEN Selections in any filter WHEN clicking CleanFilter THEN CleanFilter link should dissapear"()
    {
        given:
        String label = filterPanel.selectEntryInContentTypesFilter( PAGE_CONTENT_TYPE_NAME )

        when:
        filterPanel.clickByCleanFilter()
        contentBrowsePanel.waitsForSpinnerNotVisible();
        TestUtils.saveScreenshot( getTestSession(), "CleanFilter1" )

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
        TestUtils.saveScreenshot( getTestSession(), "CleanFilter2" )

        then:
        !contentBrowsePanel.getFilterPanel().isAnySelectionPresent()
    }


    def "GIVEN creating new Content WHEN saved and HomeButton clicked THEN new ContentType-filter and LastModified-filter should be updated with new count"()
    {
        given:
        String name = NameHelper.unqiueName( "folder" );
        BaseAbstractContent content = FolderContent.builder().
            withName( name ).
            withDisplayName( "folder" ).
            withParent( ContentPath.ROOT ).build()
        int beforeAdding = filterPanel.getNumberFilteredByContenttype( "Folder" )
        int lastModifiedBeforeAdding = filterPanel.getLastModifiedCount( "hour" )
        when:
        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save()
        contentBrowsePanel.goToAppHome()

        then:
        filterPanel.getNumberFilteredByContenttype( "Folder" ) - beforeAdding == 1 && filterPanel.getLastModifiedCount( "hour" ) -
            lastModifiedBeforeAdding == 1
    }

    def "GIVEN creating new Content WHEN saved and wizard closed THEN new ContentType-filter and LastModified-filter should be updated with new count"()
    {
        given:
        String name = NameHelper.unqiueName( "folder" );
        BaseAbstractContent content = FolderContent.builder().
            withName( name ).
            withDisplayName( "folder" ).
            withParent( ContentPath.ROOT ).build()
        int beforeAdding = filterPanel.getNumberFilteredByContenttype( "Folder" )
        int lastModifiedBeforeAdding = filterPanel.getLastModifiedCount( "hour" )
        when:
        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close();
        contentBrowsePanel.waitsForSpinnerNotVisible()

        then:
        filterPanel.getNumberFilteredByContenttype( "Folder" ) - beforeAdding == 1 && filterPanel.getLastModifiedCount( "hour" ) -
            lastModifiedBeforeAdding == 1
    }

    //This test ignored due the application BUG
    @Ignore
    def "GIVEN a Content WHEN deleted THEN new ContentType-filter and LastModified-filter should be updated with new count"()
    {
        given:
        String name = NameHelper.unqiueName( "folder" );
        BaseAbstractContent content = FolderContent.builder().
            withName( name ).
            withDisplayName( "folder" ).
            withParent( ContentPath.ROOT ).build()

        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close();
        contentBrowsePanel.waitsForSpinnerNotVisible()
        int beforeRemoving = filterPanel.getNumberFilteredByContenttype( "Folder" )
        int lastModifiedBeforeRemoving = filterPanel.getLastModifiedCount( "hour" )
        List<BaseAbstractContent> contentList = new ArrayList()
        contentList.add( content )
        when:
        contentBrowsePanel.selectContentInTable( contentList ).clickToolbarDelete().doDelete()
        contentBrowsePanel.waitsForSpinnerNotVisible()


        then:
        beforeRemoving - filterPanel.getNumberFilteredByContenttype( "Folder" ) == 1 && lastModifiedBeforeRemoving -
            filterPanel.getLastModifiedCount( "hour" ) == 1
    }


    def "GIVEN No selections or text-search WHEN adding text-search THEN all filters should be updated to only contain entries with matches in text-search"()
    {
        given:
        String name = NameHelper.unqiueName( "folder" );
        BaseAbstractContent content = FolderContent.builder().
            withName( name ).
            withDisplayName( "folder" ).
            withParent( ContentPath.ROOT ).build()

        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close();
        contentBrowsePanel.waitsForSpinnerNotVisible()
		contentBrowsePanel.waituntilPageLoaded(1);

        when:
        filterPanel.typeSearchText( name )
        contentBrowsePanel.waitsForSpinnerNotVisible()
        TestUtils.saveScreenshot( getTestSession(), "SearchText" )

        then:
        filterPanel.getNumberFilteredByContenttype( "Folder" ) == 1 && filterPanel.getLastModifiedCount( "hour" ) == 1
    }

    def "GIVEN No selections in filter WHEN Selecting one entry in ContentTypes-filter THEN no changes in ContentTypes-filter"()
    {
        given:
        List<String> beforeSelect = filterPanel.getAllContentTypesFilterEntries();

        when:
        filterPanel.selectEntryInContentTypesFilter( "Folder" )
        List<String> afterSelect = filterPanel.getAllContentTypesFilterEntries();

        then:
        beforeSelect.equals( afterSelect )
    }

    def "GIVEN No selections in filter WHEN Selecting one entry in ContentTypes-filter THEN LastModified-filter should be updated with filtered values"()
    {
        given:
        String name = NameHelper.unqiueName( "folder" );
        BaseAbstractContent content = FolderContent.builder().
            withName( name ).
            withDisplayName( "folder" ).
            withParent( ContentPath.ROOT ).build()

        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close();
		contentBrowsePanel.waituntilPageLoaded(1);
        Integer lastModifiedNumberBefore = filterPanel.getContentNumberFilteredByLastModified( FilterPanelLastModified.HOUR )


        when:
        String folderCount = filterPanel.selectEntryInContentTypesFilter( "Folder" )
        Integer newLastModifiedNumber = filterPanel.getContentNumberFilteredByLastModified( FilterPanelLastModified.HOUR )

        then:
        if ( lastModifiedNumberBefore == 0 )
        {
            TestUtils.getNumberFromFilterLabel( folderCount ) == newLastModifiedNumber
        }

        else
        {
            newLastModifiedNumber > lastModifiedNumberBefore
        }


    }
    //@Ignored
    //GIVEN Selections in ContentTypes-filter WHEN Selecting one entry in LastModified-filter THEN entries with no selection in ContentTypes-filter should dissapear from view

    def "GIVEN selection in any filter WHEN adding text-search THEN all filters should be updated to only contain entries with selection and new count with match on text-search"()
    {
        given:
        String name = NameHelper.unqiueName( "folder" );
        BaseAbstractContent content = FolderContent.builder().
            withName( name ).
            withDisplayName( "folder" ).
            withParent( ContentPath.ROOT ).build()

        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close();
		contentBrowsePanel.waituntilPageLoaded(1);
        String label = filterPanel.selectEntryInContentTypesFilter( "Folder" )
        Integer folderCountBefore = TestUtils.getNumberFromFilterLabel( label );

        when:
        Integer newFolderCount = filterPanel.typeSearchText( content.getName() ).getNumberFilteredByContenttype( "Folder" )
        
        then:
        ( newFolderCount == 1 ) && ( newFolderCount != folderCountBefore ) && ( filterPanel.getAllContentTypesFilterEntries().size() == 1 )
    }

}
