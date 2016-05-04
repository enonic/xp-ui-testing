package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel.ContentTypeDisplayNames
import com.enonic.autotests.pages.contentmanager.browsepanel.FilterPanelLastModified
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

import static com.enonic.autotests.utils.SleepHelper.sleep

@Stepwise
class ContentBrowsePanel_FilterPanel_Spec
    extends BaseContentSpec
{
    @Shared
    Content TEST_FOLDER

    def "GIVEN no selections in filter WHEN Selecting one entry in any filter THEN Clean Filter link should appear"()
    {
        given:
        Content content = buildUnstructuredContent( "unstructured", "filter-panel-test" )
        addContent( content );
        contentBrowsePanel.doShowFilterPanel();

        when:
        String label = filterPanel.selectEntryInContentTypesFilter( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );
        TestUtils.saveScreenshot( getTestSession(), "filter_panel_unstructured_selected" );
        contentBrowsePanel.waitsForSpinnerNotVisible();

        then:
        contentBrowsePanel.getRowNumber() == TestUtils.getNumberFromFilterLabel( label );
    }

    def "GIVEN selections in any filter WHEN clicking CleanFilter THEN CleanFilter link should disappears"()
    {
        given: "selections in any filter"
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.selectEntryInContentTypesFilter( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );
        boolean visibleBefore = filterPanel.waitForClearFilterLinkVisible();

        when: "clicking CleanFilter"
        filterPanel.clickOnCleanFilter();
        contentBrowsePanel.waitsForSpinnerNotVisible();
        TestUtils.saveScreenshot( getTestSession(), "clear_filter_link_not_displayed" );

        then: "CleanFilter link should disappears"
        filterPanel.waitForClearFilterLinkNotVisible();

        and: "link was visible when selection was in any filter"
        visibleBefore;
    }

    def "GIVEN selections in any filter WHEN clicking 'CleanFilter' THEN all selections should disappear"()
    {
        given: "Selections in any filter"
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.selectEntryInContentTypesFilter( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );

        when: "clicking CleanFilter"
        contentBrowsePanel.getFilterPanel().clickOnCleanFilter();
        contentBrowsePanel.waitsForSpinnerNotVisible();
        TestUtils.saveScreenshot( getTestSession(), "CleanFilter_clicked" );

        then: "all selections should disappear"
        !contentBrowsePanel.getFilterPanel().isAnySelectionPresent();
    }

    def "GIVEN creating new content WHEN saved and HomeButton clicked THEN new ContentType-filter and LastModified-filter should be updated with new count"()
    {
        given:
        Content folder = buildFolderContent( "folder", "last modified test" );
        contentBrowsePanel.doShowFilterPanel();
        int beforeAdding = filterPanel.getNumberFilteredByContentType( "Folder" );
        int lastModifiedBeforeAdding = filterPanel.getLastModifiedCount( "day" );

        when: "content saved and the HomeButton clicked"
        contentBrowsePanel.clickToolbarNew().selectContentType( folder.getContentTypeName() ).typeData( folder ).save();
        contentBrowsePanel.pressAppHomeButton();
        sleep( 1000 );

        then: "new ContentType-filter should be updated with new count"
        filterPanel.getNumberFilteredByContentType( "Folder" ) - beforeAdding == 1

        and: "LastModified-filter should be updated with new count"
        filterPanel.getLastModifiedCount( "day" ) - lastModifiedBeforeAdding == 1;
    }

    def "GIVEN creating new content WHEN saved and wizard closed THEN new ContentType-filter and LastModified-filter should be updated with new count"()
    {
        given: "opened a content wizard and data typed"
        contentBrowsePanel.doShowFilterPanel();
        TEST_FOLDER = buildFolderContent( "folder", "last modified test 2" );
        TestUtils.saveScreenshot( getSession(), "last-mod-day-before-adding" );
        int beforeAdding = filterPanel.getNumberFilteredByContentType( "Folder" );
        int lastModifiedBeforeAdding = filterPanel.getLastModifiedCount( "day" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( TEST_FOLDER.getContentTypeName() ).
            typeData( TEST_FOLDER );

        when: "content saved and wizard closed"
        wizard.save().close( TEST_FOLDER.getDisplayName() );
        sleep( 1000 );
        TestUtils.saveScreenshot( getSession(), "last-mod-day-increased" );

        then: "folders count increased by 1"
        filterPanel.getNumberFilteredByContentType( "Folder" ) - beforeAdding == 1

        and: "last modified count increased by 1"
        filterPanel.getLastModifiedCount( "day" ) - lastModifiedBeforeAdding == 1;
    }

    def "GIVEN a content WHEN it deleted THEN new ContentType-filter and LastModified-filter should be updated with new count"()
    {
        given:
        contentBrowsePanel.doShowFilterPanel();
        TestUtils.saveScreenshot( getSession(), "LastModified_filter_before_folder_deleting" )
        int beforeRemoving = filterPanel.getNumberFilteredByContentType( "Folder" );
        int lastModifiedBeforeRemoving = filterPanel.getLastModifiedCount( "day" );

        when:
        contentBrowsePanel.selectContentInTable( TEST_FOLDER.getName() ).clickToolbarDelete().doDelete();
        sleep( 2000 );
        TestUtils.saveScreenshot( getSession(), "LastModified_filter-folder-deleted" )

        then: "folders count reduced by 1"
        beforeRemoving - filterPanel.getNumberFilteredByContentType( "Folder" ) == 1

        and: "last modified count reduced by 1"
        lastModifiedBeforeRemoving - filterPanel.getLastModifiedCount( "day" ) == 1;
    }

    def "GIVEN no selections or text-search WHEN adding text-search THEN all filters should be updated to only contain entries with matches in text-search"()
    {
        given: "folder-content added and no selections or text-search"
        contentBrowsePanel.doShowFilterPanel();
        TEST_FOLDER = buildFolderContent( "folder", "filtering test" );
        addContent( TEST_FOLDER );

        when: "folder's name typed in the text input"
        filterPanel.typeSearchText( TEST_FOLDER.getName() );
        contentBrowsePanel.waitsForSpinnerNotVisible();
        TestUtils.saveScreenshot( getTestSession(), "searchText-" + TEST_FOLDER.getName() );

        then: "all filters should be updated to only contain entries with matches in text-search"
        filterPanel.getNumberFilteredByContentType( "Folder" ) == 1;

        and:
        filterPanel.getLastModifiedCount( "hour" ) == 1;
    }

    def "GIVEN no selections in filter WHEN selecting one entry in ContentTypes-filter THEN no changes in ContentTypes-filter"()
    {
        given: "no selections in filter and filter panel shown"
        contentBrowsePanel.doShowFilterPanel();
        List<String> beforeSelect = filterPanel.getAllContentTypesFilterEntries();

        when: "selecting one entry in ContentTypes-filter"
        filterPanel.selectEntryInContentTypesFilter( "Folder" );

        then: "no changes in ContentTypes-filter"
        List<String> afterSelect = filterPanel.getAllContentTypesFilterEntries();
        beforeSelect.equals( afterSelect );
    }

    def "GIVEN no selections in filter WHEN Selecting one entry in ContentTypes-filter THEN LastModified-filter should be updated with filtered values"()
    {
        given: "no selections in the filter panel"
        contentBrowsePanel.doShowFilterPanel();
        Integer lastModifiedHourBefore = filterPanel.getContentNumberFilteredByLastModified( FilterPanelLastModified.HOUR );
        Integer lastModifiedDayBefore = filterPanel.getContentNumberFilteredByLastModified( FilterPanelLastModified.DAY );

        when: "selecting one entry in ContentTypes-filter (Image)"
        filterPanel.selectEntryInContentTypesFilter( "Image" );
        Integer newLastModifiedHour = filterPanel.getContentNumberFilteredByLastModified( FilterPanelLastModified.HOUR );
        Integer newLastModifiedDay = filterPanel.getContentNumberFilteredByLastModified( FilterPanelLastModified.DAY );

        then: "LastModified-filter should be updated with filtered values"
        newLastModifiedHour != lastModifiedHourBefore;
        and:
        newLastModifiedDay != lastModifiedDayBefore;
    }

    def "GIVEN selection in any filter WHEN adding text-search THEN all filters should be updated to only contain entries with selection and new count with match on text-search"()
    {
        given: "added a folder content and selection in any filter(folder)"
        contentBrowsePanel.doShowFilterPanel();
        String label = filterPanel.selectEntryInContentTypesFilter( "Folder" );
        Integer numberOfFoldersBefore = TestUtils.getNumberFromFilterLabel( label );

        when: "adding text-search"
        filterPanel.typeSearchText( TEST_FOLDER.getName() );
        TestUtils.saveScreenshot( getTestSession(), "typed_name" );

        then: "all filters should be updated to only contain entries with selection"
        Integer newNumberOfFolders = filterPanel.getNumberFilteredByContentType( "Folder" );
        newNumberOfFolders == 1;

        and:
        newNumberOfFolders != numberOfFoldersBefore;

        and: "number of checkbox in aggregation view is 1"
        filterPanel.getAllContentTypesFilterEntries().size() == 1
    }
}
