package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel.ContentTypeDisplayNames
import com.enonic.autotests.pages.contentmanager.browsepanel.FilterPanelLastModified
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore
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
        TestUtils.saveScreenshot( getTestSession(), "filter_panel_unstructured_before_selecting" );
        String label = filterPanel.selectContentTypeInAggregationView( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );
        TestUtils.saveScreenshot( getTestSession(), "filter_panel_unstructured_selected" );
        contentBrowsePanel.waitsForSpinnerNotVisible();

        then:
        contentBrowsePanel.getRowsCount() == TestUtils.getNumberFromFilterLabel( label );
    }

    def "GIVEN selections in any filter WHEN clicking CleanFilter THEN CleanFilter link should disappears"()
    {
        given: "selections in any filter"
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.selectContentTypeInAggregationView( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );
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
        filterPanel.selectContentTypeInAggregationView( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );
        TestUtils.saveScreenshot( getTestSession(), "test_cleanFilter_entry_selected" );
        when: "clicking CleanFilter"
        contentBrowsePanel.getFilterPanel().clickOnCleanFilter();
        contentBrowsePanel.waitsForSpinnerNotVisible();
        TestUtils.saveScreenshot( getTestSession(), "test_cleanFilter_cleared" );

        then: "all selections should disappear"
        !contentBrowsePanel.getFilterPanel().isAnySelectionPresent();
    }

    def "GIVEN creating new content WHEN saved and HomeButton clicked THEN new ContentType-filter and LastModified-filter should be updated with new count"()
    {
        given:
        Content folder = buildFolderContent( "folder", "last modified test" );
        contentBrowsePanel.doShowFilterPanel();
        int beforeAdding = filterPanel.getNumberAggregatedByContentType( "Folder" );
        int lastModifiedBeforeAdding = filterPanel.getLastModifiedCount( "day" );

        when: "content saved and the HomeButton clicked"
        contentBrowsePanel.clickToolbarNew().selectContentType( folder.getContentTypeName() ).typeData( folder ).save();
        contentBrowsePanel.pressAppHomeButton();
        sleep( 1000 );

        then: "new ContentType-filter should be updated with new count"
        filterPanel.getNumberAggregatedByContentType( "Folder" ) - beforeAdding == 1

        and: "LastModified-filter should be updated with new count"
        filterPanel.getLastModifiedCount( "day" ) - lastModifiedBeforeAdding == 1;
    }

    def "GIVEN creating new content WHEN saved and wizard closed THEN new ContentType-filter and LastModified-filter should be updated with new count"()
    {
        given: "opened a content wizard and data typed"
        contentBrowsePanel.doShowFilterPanel();
        TEST_FOLDER = buildFolderContent( "folder", "last modified test 2" );
        TestUtils.saveScreenshot( getSession(), "last-mod-day-before-adding" );
        int beforeAdding = filterPanel.getNumberAggregatedByContentType( "Folder" );
        int lastModifiedBeforeAdding = filterPanel.getLastModifiedCount( "day" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( TEST_FOLDER.getContentTypeName() ).
            typeData( TEST_FOLDER );

        when: "content saved and wizard closed"
        wizard.save().close( TEST_FOLDER.getDisplayName() );
        sleep( 1000 );
        TestUtils.saveScreenshot( getSession(), "last-mod-day-increased" );

        then: "folders count increased by 1"
        filterPanel.getNumberAggregatedByContentType( "Folder" ) - beforeAdding == 1

        and: "last modified count increased by 1"
        filterPanel.getLastModifiedCount( "day" ) - lastModifiedBeforeAdding == 1;
    }

    def "GIVEN a content WHEN it deleted THEN new ContentType-filter and LastModified-filter should be updated with new count"()
    {
        given: "existing folder"
        contentBrowsePanel.doShowFilterPanel();
        TestUtils.saveScreenshot( getSession(), "LastModified_filter_before_folder_deleting" );
        int beforeRemoving = filterPanel.getNumberAggregatedByContentType( "Folder" );
        int lastModifiedBeforeRemoving = filterPanel.getLastModifiedCount( "day" );
        TestUtils.saveScreenshot( getSession(), "test_LastModified_aggregation_before_deleting" );

        when: "the folder deleted"
        contentBrowsePanel.selectContentInTable( TEST_FOLDER.getName() ).clickToolbarDelete().doDelete();
        sleep( 2000 );
        TestUtils.saveScreenshot( getSession(), "test_LastModified_aggregation_folder_deleted" )

        then: "folders count reduced by 1"
        beforeRemoving - filterPanel.getNumberAggregatedByContentType( "Folder" ) == 1

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
        TestUtils.saveScreenshot( getTestSession(), "test_aggregation_searchText_typed" );

        then: "all filters should be updated to only contain entries with matches in text-search"
        filterPanel.getNumberAggregatedByContentType( "Folder" ) == 1;

        and:
        filterPanel.getLastModifiedCount( "hour" ) == 1;
    }
    //XP-3586 Content not correctly filtered, when filter panel has been hidden
    @Ignore
    def "GIVEN existing folder WHEN name of folder typed in the filter panel AND filter panel has been hidden AND new folder has been added THEN only one folder with matches in text-search is displayed"()
    {

        given: "the name of existing folder typed"
        filterPanel.typeSearchText( TEST_FOLDER.getName() );

        when: "filter panel has been hidden"
        contentBrowsePanel.doHideFilterPanel();

        and: "new folder has been added"
        Content newFolder = buildFolderContent( "folder", "test for  hidden filter panel" );
        addContent( newFolder );
        TestUtils.saveScreenshot( getSession(), "test_text_typed_filter_panel_hidden" )

        then: "only one folder with matches in text-search is displayed"
        contentBrowsePanel.getRowsCount() == 1; ;
    }

    def "GIVEN no selections in filter WHEN selecting one entry in ContentTypes-aggregation THEN no changes in ContentTypes-aggregation"()
    {
        given: "no selections in filter and filter panel shown"
        contentBrowsePanel.doShowFilterPanel();
        List<String> beforeSelect = filterPanel.getContentTypesAggregationValues();

        when: "selecting one entry in ContentTypes-filter"
        filterPanel.selectContentTypeInAggregationView( "Folder" );

        then: "no changes in ContentTypes-filter"
        List<String> afterSelect = filterPanel.getContentTypesAggregationValues();
        beforeSelect.equals( afterSelect );
    }

    def "GIVEN no selections in filter WHEN Selecting one entry in ContentTypes-aggregation THEN LastModified-aggregation should be updated with filtered values"()
    {
        given: "no selections in the filter panel"
        contentBrowsePanel.doShowFilterPanel();
        Integer lastModifiedHourBefore = filterPanel.getNumberAggregatedByLastModified( FilterPanelLastModified.HOUR );
        Integer lastModifiedDayBefore = filterPanel.getNumberAggregatedByLastModified( FilterPanelLastModified.DAY );

        when: "selecting one entry in ContentTypes-aggregation (Image)"
        filterPanel.selectContentTypeInAggregationView( "Image" );
        TestUtils.saveScreenshot( getSession(), "test_filter_panel_image_selected" );
        Integer newLastModifiedHour = filterPanel.getNumberAggregatedByLastModified( FilterPanelLastModified.HOUR );
        Integer newLastModifiedDay = filterPanel.getNumberAggregatedByLastModified( FilterPanelLastModified.DAY );

        then: "LastModified-aggregation should be updated with filtered values"
        newLastModifiedHour != lastModifiedHourBefore;
        and:
        newLastModifiedDay != lastModifiedDayBefore;
    }

    def "GIVEN selection in any filter WHEN adding text-search THEN all filters should be updated to only contain entries with selection and new count with match on text-search"()
    {
        given: "added a folder content and selection in any filter(folder)"
        contentBrowsePanel.doShowFilterPanel();
        String label = filterPanel.selectContentTypeInAggregationView( "Folder" );
        Integer numberOfFoldersBefore = TestUtils.getNumberFromFilterLabel( label );

        when: "adding text-search"
        filterPanel.typeSearchText( TEST_FOLDER.getName() );
        TestUtils.saveScreenshot( getTestSession(), "test_aggregation_selected_content_name_typed" );

        then: "all filters should be updated to only contain entries with selection"
        Integer newNumberOfFolders = filterPanel.getNumberAggregatedByContentType( "Folder" );
        newNumberOfFolders == 1;

        and:
        newNumberOfFolders != numberOfFoldersBefore;

        and: "number of checkbox in aggregation view is 1"
        filterPanel.getContentTypesAggregationValues().size() == 1
    }
}
