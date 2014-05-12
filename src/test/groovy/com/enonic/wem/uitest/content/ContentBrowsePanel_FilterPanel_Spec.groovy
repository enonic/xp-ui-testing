package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel.ContenTypeDispalyNames
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.browsepanel.FilterPanelLastModified
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.api.schema.content.ContentTypeName
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class ContentBrowsePanel_FilterPanel_Spec
    extends BaseGebSpec
{

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    @Shared
    ContentBrowseFilterPanel filterPanel;

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
        filterPanel = contentBrowsePanel.getFilterPanel();
    }
    //bug CMS-3536
    def "GIVEN No selections in filter WHEN Selecting one entry in any filter THEN Clean Filter link should appear"()
    {
        given:
        String name = NameHelper.uniqueName( "page" );
        Content page = Content.builder().
            name( name ).
            displayName( "page" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.page() ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( page.getContentTypeName() ).typeData( page ).save().close();

        when:
        String label = filterPanel.selectEntryInContentTypesFilter( ContenTypeDispalyNames.PAGE.getValue() );
        TestUtils.saveScreenshot( getTestSession() );

        then:
        contentBrowsePanel.getContentNamesFromBrowsePanel().size() == TestUtils.getNumberFromFilterLabel( label );
    }

    def "GIVEN Selections in any filter WHEN clicking CleanFilter THEN CleanFilter link should disappear"()
    {
        given:
        String label = filterPanel.selectEntryInContentTypesFilter( ContenTypeDispalyNames.FOLDER.getValue() );

        when:
        filterPanel.clickByCleanFilter();
        contentBrowsePanel.waitsForSpinnerNotVisible();
        TestUtils.saveScreenshot( getTestSession(), "CleanFilter1" );

        then:
        filterPanel.waitForClearFilterLinkNotvisible();
    }


    def "GIVEN Selections in any filter WHEN clicking CleanFilter THEN all selections should disappear"()
    {
        given:
        String label = filterPanel.selectEntryInContentTypesFilter( ContenTypeDispalyNames.FOLDER.getValue() );

        when:
        contentBrowsePanel.getFilterPanel().clickByCleanFilter();
        contentBrowsePanel.waitsForSpinnerNotVisible();
        TestUtils.saveScreenshot( getTestSession(), "CleanFilter2" );

        then:
        !contentBrowsePanel.getFilterPanel().isAnySelectionPresent();
    }


    def "GIVEN creating new Content WHEN saved and HomeButton clicked THEN new ContentType-filter and LastModified-filter should be updated with new count"()
    {
        given:
        String name = NameHelper.uniqueName( "folder" );
        Content content = Content.builder().
            name( name ).
            displayName( "folder" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.folder() ).
            build();

        int beforeAdding = filterPanel.getNumberFilteredByContenttype( "Folder" );
        int lastModifiedBeforeAdding = filterPanel.getLastModifiedCount( "hour" );

        when:
        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save();
        contentBrowsePanel.goToAppHome();

        then:
        filterPanel.getNumberFilteredByContenttype( "Folder" ) - beforeAdding == 1 && filterPanel.getLastModifiedCount( "hour" ) -
            lastModifiedBeforeAdding == 1;
    }

	//bug CMS-3536
    def "GIVEN creating new Content WHEN saved and wizard closed THEN new ContentType-filter and LastModified-filter should be updated with new count"()
    {
        given:
        String name = NameHelper.uniqueName( "folder" );
        Content content = Content.builder().
            name( name ).
            displayName( "folder" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.folder() ).
            build();
        int beforeAdding = filterPanel.getNumberFilteredByContenttype( "Folder" );
        int lastModifiedBeforeAdding = filterPanel.getLastModifiedCount( "hour" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).
            typeData( content );

        when:
        wizard.save().close();

        then:
        filterPanel.getNumberFilteredByContenttype( "Folder" ) - beforeAdding == 1 && filterPanel.getLastModifiedCount( "hour" ) -
            lastModifiedBeforeAdding == 1;
    }
    //bug CMS-3536
    def "GIVEN a Content WHEN deleted THEN new ContentType-filter and LastModified-filter should be updated with new count"()
    {
        given:
        String name = NameHelper.uniqueName( "folder" );
        Content content = Content.builder().
            name( name ).
            displayName( "folder" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.ROOT ).
            build();

        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close();
        int beforeRemoving = filterPanel.getNumberFilteredByContenttype( "Folder" );
        int lastModifiedBeforeRemoving = filterPanel.getLastModifiedCount( "hour" );
        List<Content> contentList = new ArrayList();
        contentList.add( content );

        when:
        contentBrowsePanel.selectContentInTable( contentList ).clickToolbarDelete().doDelete();
        contentBrowsePanel.waituntilPageLoaded( 3 );


        then:
        beforeRemoving - filterPanel.getNumberFilteredByContenttype( "Folder" ) == 1 && lastModifiedBeforeRemoving -
            filterPanel.getLastModifiedCount( "hour" ) == 1;
    }


    def "GIVEN No selections or text-search WHEN adding text-search THEN all filters should be updated to only contain entries with matches in text-search"()
    {
        given:
        String name = NameHelper.uniqueName( "folder" );
        Content content = Content.builder().
            name( name ).
            displayName( "folder" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.ROOT ).
            build();

        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close();
        contentBrowsePanel.waitsForSpinnerNotVisible();

        when:
        filterPanel.typeSearchText( name );
        contentBrowsePanel.waitsForSpinnerNotVisible();
        TestUtils.saveScreenshot( getTestSession(), "SearchText" );

        then:
        filterPanel.getNumberFilteredByContenttype( "Folder" ) == 1 && filterPanel.getLastModifiedCount( "hour" ) == 1;
    }

    def "GIVEN No selections in filter WHEN Selecting one entry in ContentTypes-filter THEN no changes in ContentTypes-filter"()
    {
        given:
        List<String> beforeSelect = filterPanel.getAllContentTypesFilterEntries();

        when:
        filterPanel.selectEntryInContentTypesFilter( "Folder" );


        then:
        List<String> afterSelect = filterPanel.getAllContentTypesFilterEntries();
        beforeSelect.equals( afterSelect );
    }

    def "GIVEN No selections in filter WHEN Selecting one entry in ContentTypes-filter THEN LastModified-filter should be updated with filtered values"()
    {
        given:
        String name = NameHelper.uniqueName( "folder" );
        Content content = Content.builder().
            name( name ).
            displayName( "folder" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.ROOT ).build();

        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).
            save().close();
        Integer lastModifiedNumberBefore = filterPanel.getContentNumberFilteredByLastModified( FilterPanelLastModified.HOUR );

        when:
        String folderCount = filterPanel.selectEntryInContentTypesFilter( "Folder" );

        then:
        Integer newLastModifiedNumber = filterPanel.getContentNumberFilteredByLastModified( FilterPanelLastModified.HOUR );
        if ( lastModifiedNumberBefore == 0 )
        {
            TestUtils.getNumberFromFilterLabel( folderCount ) == newLastModifiedNumber;
        }

        else
        {
            newLastModifiedNumber > lastModifiedNumberBefore;
        }


    }

    def "GIVEN selection in any filter WHEN adding text-search THEN all filters should be updated to only contain entries with selection and new count with match on text-search"()
    {
        given:
        String name = NameHelper.uniqueName( "folder" );
        Content content = Content.builder().
            name( name ).
            displayName( "folder" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.ROOT ).build();

        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).
            save().close();

        String label = filterPanel.selectEntryInContentTypesFilter( "Folder" );
        Integer folderCountBefore = TestUtils.getNumberFromFilterLabel( label );

        when:
        filterPanel.typeSearchText( content.getName() );

        then:
        Integer newFolderCount = filterPanel.getNumberFilteredByContenttype( "Folder" );
        ( newFolderCount == 1 ) && ( newFolderCount != folderCountBefore ) && ( filterPanel.getAllContentTypesFilterEntries().size() == 1 );
    }

}
