package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel.ContentTypeDisplayNames
import com.enonic.autotests.pages.contentmanager.browsepanel.FilterPanelLastModified
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared
import spock.lang.Stepwise

import static com.enonic.autotests.utils.SleepHelper.sleep

@Stepwise
class ContentBrowsePanel_FilterPanel_Spec
    extends BaseContentSpec
{
    @Shared
    Content TEST_FOLDER

    def "GIVEN No selections in filter WHEN Selecting one entry in any filter THEN Clean Filter link should appear"()
    {
        given:
        String name = NameHelper.uniqueName( "unstructured" );
        Content content = Content.builder().
            name( name ).
            displayName( "unstructured" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.unstructured() ).
            build();
        addContent( content );
        contentBrowsePanel.doShowFilterPanel();
        when:
        String label = filterPanel.selectEntryInContentTypesFilter( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );
        TestUtils.saveScreenshot( getTestSession(), "filter-panel" );
        contentBrowsePanel.waitsForSpinnerNotVisible();
        then:
        contentBrowsePanel.getRowNumber() == TestUtils.getNumberFromFilterLabel( label );
    }

    def "GIVEN Selections in any filter WHEN clicking CleanFilter THEN CleanFilter link should disappear"()
    {
        given:
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.selectEntryInContentTypesFilter( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );

        when:
        filterPanel.clickOnCleanFilter();
        contentBrowsePanel.waitsForSpinnerNotVisible();
        TestUtils.saveScreenshot( getTestSession(), "CleanFilter1" );

        then:
        filterPanel.waitForClearFilterLinkNotVisible();
    }

    def "GIVEN Selections in any filter WHEN clicking CleanFilter THEN all selections should disappear"()
    {
        given: "Selections in any filter"
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.selectEntryInContentTypesFilter( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );

        when: "clicking CleanFilter"
        contentBrowsePanel.getFilterPanel().clickOnCleanFilter();
        contentBrowsePanel.waitsForSpinnerNotVisible();
        TestUtils.saveScreenshot( getTestSession(), "CleanFilter2" );

        then: "all selections should disappear"
        !contentBrowsePanel.getFilterPanel().isAnySelectionPresent();
    }

    def "GIVEN creating new Content WHEN saved and HomeButton clicked THEN new ContentType-filter and LastModified-filter should be updated with new count"()
    {
        given:
        Content folder = buildFolderContent( "folder", "last modified test" );
        contentBrowsePanel.doShowFilterPanel();
        int beforeAdding = filterPanel.getNumberFilteredByContentType( "Folder" );
        int lastModifiedBeforeAdding = filterPanel.getLastModifiedCount( "hour" );

        when: "content saved and the HomeButton clicked"
        contentBrowsePanel.clickToolbarNew().selectContentType( folder.getContentTypeName() ).typeData( folder ).save();
        contentBrowsePanel.goToAppHome();

        then: "new ContentType-filter and LastModified-filter should be updated with new count"
        filterPanel.getNumberFilteredByContentType( "Folder" ) - beforeAdding == 1 && filterPanel.getLastModifiedCount( "hour" ) -
            lastModifiedBeforeAdding == 1;
    }

    def "GIVEN creating new Content WHEN saved and wizard closed THEN new ContentType-filter and LastModified-filter should be updated with new count"()
    {
        given: "opened a content wizard and data typed"
        contentBrowsePanel.doShowFilterPanel();
        TEST_FOLDER = buildFolderContent( "folder", "last modified test 2" );
        int beforeAdding = filterPanel.getNumberFilteredByContentType( "Folder" );
        int lastModifiedBeforeAdding = filterPanel.getLastModifiedCount( "hour" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( TEST_FOLDER.getContentTypeName() ).
            typeData( TEST_FOLDER );

        when: "content saved and wizard closed"
        wizard.save().close( TEST_FOLDER.getDisplayName() );
        sleep( 700 );
        TestUtils.saveScreenshot( getSession(), "last-mod-hour" );

        then: "new ContentType-filter and LastModified-filter should be updated with new count"
        filterPanel.getNumberFilteredByContentType( "Folder" ) - beforeAdding == 1 && filterPanel.getLastModifiedCount( "hour" ) -
            lastModifiedBeforeAdding == 1;
    }

    def "GIVEN a Content WHEN deleted THEN new ContentType-filter and LastModified-filter should be updated with new count"()
    {
        given:
        contentBrowsePanel.doShowFilterPanel();
        TestUtils.saveScreenshot( getSession(), "LastModified_filter1" )
        int beforeRemoving = filterPanel.getNumberFilteredByContentType( "Folder" );
        int lastModifiedBeforeRemoving = filterPanel.getLastModifiedCount( "hour" );
        List<String> contentList = new ArrayList();
        contentList.add( TEST_FOLDER.getName() );

        when:
        contentBrowsePanel.selectContentInTable( contentList ).clickToolbarDelete().doDelete();
        contentBrowsePanel.waitUntilPageLoaded( 3 );
        sleep( 1000 );
        TestUtils.saveScreenshot( getSession(), "LastModified_filter2" )

        then:
        beforeRemoving - filterPanel.getNumberFilteredByContentType( "Folder" ) == 1 && lastModifiedBeforeRemoving -
            filterPanel.getLastModifiedCount( "hour" ) == 1;
    }

    def "GIVEN No selections or text-search WHEN adding text-search THEN all filters should be updated to only contain entries with matches in text-search"()
    {
        given: "a folder-content added and No selections or text-search"
        contentBrowsePanel.doShowFilterPanel();
        TEST_FOLDER = buildFolderContent( "folder", "filtering test" );
        addContent( TEST_FOLDER );

        when: "folder's name typed in the text input"
        filterPanel.typeSearchText( TEST_FOLDER.getName() );
        contentBrowsePanel.waitsForSpinnerNotVisible();
        TestUtils.saveScreenshot( getTestSession(), "SearchText" );

        then: "all filters should be updated to only contain entries with matches in text-search"
        filterPanel.getNumberFilteredByContentType( "Folder" ) == 1 && filterPanel.getLastModifiedCount( "hour" ) == 1;
    }

    def "GIVEN No selections in filter WHEN Selecting one entry in ContentTypes-filter THEN no changes in ContentTypes-filter"()
    {
        given: " No selections in filter and filter panel shown"
        contentBrowsePanel.doShowFilterPanel();
        List<String> beforeSelect = filterPanel.getAllContentTypesFilterEntries();

        when: "Selecting one entry in ContentTypes-filter"
        filterPanel.selectEntryInContentTypesFilter( "Folder" );

        then: "no changes in ContentTypes-filter"
        List<String> afterSelect = filterPanel.getAllContentTypesFilterEntries();
        beforeSelect.equals( afterSelect );
    }

    def "GIVEN No selections in filter WHEN Selecting one entry in ContentTypes-filter THEN LastModified-filter should be updated with filtered values"()
    {
        given: "No selections in the filter panel"
        contentBrowsePanel.doShowFilterPanel();
        Integer lastModifiedNumberBefore = filterPanel.getContentNumberFilteredByLastModified( FilterPanelLastModified.HOUR );

        when: "Selecting one entry in ContentTypes-filter (Folder)"
        filterPanel.selectEntryInContentTypesFilter( "Folder" );
        Integer newLastModifiedNumber = filterPanel.getContentNumberFilteredByLastModified( FilterPanelLastModified.HOUR );

        then: "LastModified-filter should be updated with filtered values"
        newLastModifiedNumber != lastModifiedNumberBefore;
    }

    def "GIVEN selection in any filter WHEN adding text-search THEN all filters should be updated to only contain entries with selection and new count with match on text-search"()
    {
        given: "added a folder content and selection in any filter(folder)"
        String label = filterPanel.selectEntryInContentTypesFilter( "Folder" );
        Integer folderCountBefore = TestUtils.getNumberFromFilterLabel( label );

        when: "adding text-search"
        filterPanel.typeSearchText( TEST_FOLDER.getName() );
        TestUtils.saveScreenshot( getTestSession(), "typed_name" );

        then:
        Integer newFolderCount = filterPanel.getNumberFilteredByContentType( "Folder" );
        ( newFolderCount == 1 ) && ( newFolderCount != folderCountBefore ) && ( filterPanel.getAllContentTypesFilterEntries().size() == 1 );
    }
}
