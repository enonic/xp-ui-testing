package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
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

    @Shared
    Content TEST_FOLDER1

    def "WHEN the 'shortcut' checkbox has been checked in Filter Panel THEN number of rows in the grid should be equals to number in Filter Panel"()
    {
        given: "'Filter panel' is opened"
        Content content = buildShortcut( "shortcut", null, "filter panel test" )
        addContent( content );
        contentBrowsePanel.doShowFilterPanel();

        when: "'Shortcut' checkbox has been checked"
        String label = filterPanel.selectContentTypeInAggregationView( ContentTypeDisplayNames.SHORTCUT.getValue() );
        saveScreenshot( "filter_panel_shortcut_selected" );
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        then: "number of rows in the grid should be equals to number for the checkbox"
        contentBrowsePanel.getRowsCount() == TestUtils.getNumberFromFilterLabel( label );
    }

    def "GIVEN selections in any filter WHEN clicking CleanFilter THEN CleanFilter link should disappears"()
    {
        given: "'Shortcut' checkbox is checked on the Filter Panel"
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.selectContentTypeInAggregationView( ContentTypeDisplayNames.SHORTCUT.getValue() );
        boolean visibleBefore = filterPanel.waitForClearFilterLinkVisible();

        when: "'Clear' link has been clicked"
        filterPanel.clickOnCleanFilter();
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        saveScreenshot( "clear_filter_link_not_displayed" );

        then: "'Clear' link should not be displayed"
        filterPanel.waitForClearFilterLinkNotVisible();

        and: "the link was visible before the clicking"
        visibleBefore;

        then: "no one checkbox on the Panel should be checked"
        !contentBrowsePanel.getFilterPanel().isAnySelectionPresent();
    }

    def "GIVEN new folder has been saved WHEN HomeButton has been clicked THEN 'Folder' and LastModified-filter should be updated with new count"()
    {
        given: "Filter Panel is opened"
        TEST_FOLDER1 = buildFolderContent( "folder", "last modified test" );
        contentBrowsePanel.doShowFilterPanel();
        int beforeAdding = filterPanel.getNumberAggregatedByContentType( "Folder" );
        int lastModifiedBeforeAdding = filterPanel.getLastModifiedCount( "day" );

        when: "the content has been saved and the HomeButton clicked"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType(
            TEST_FOLDER1.getContentTypeName() ).typeData( TEST_FOLDER1 ).save();
        wizardPanel.switchToBrowsePanelTab().waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        sleep( 1000 );

        then: "number of 'Folder' should be updated with new count"
        filterPanel.getNumberAggregatedByContentType( "Folder" ) - beforeAdding == 1

        and: "LastModified-filter should be updated with new count"
        filterPanel.getLastModifiedCount( "day" ) - lastModifiedBeforeAdding == 1;
    }

    def "GIVEN filter panel is opened WHEN folder have saved and wizard closed THEN new ContentType-filter and LastModified-filter should be updated with new count"()
    {
        given: "filter panel is opened"
        contentBrowsePanel.doShowFilterPanel();
        TEST_FOLDER = buildFolderContent( "folder", "last modified test 2" );
        saveScreenshot( "last-mod-day-before-adding" );
        int beforeAdding = filterPanel.getNumberAggregatedByContentType( "Folder" );
        int lastModifiedBeforeAdding = filterPanel.getLastModifiedCount( "day" );

        and: "wizard for creating of new folder is opened"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( TEST_FOLDER.getContentTypeName() ).
            typeData( TEST_FOLDER );

        when: "content has been saved and wizard closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        sleep( 1000 );
        saveScreenshot( "last-mod-day-increased" );

        then: "'folders' count should be increased by 1"
        filterPanel.getNumberAggregatedByContentType( "Folder" ) - beforeAdding == 1

        and: "'last modified' count should be increased by 1"
        filterPanel.getLastModifiedCount( "day" ) - lastModifiedBeforeAdding == 1;
    }

    def "GIVEN filter panel is opened WHEN one folder has been deleted THEN number of 'Folder' and LastModified-filter should be updated with new count"()
    {
        given: "filter panel is opened"
        contentBrowsePanel.doShowFilterPanel();
        saveScreenshot( "LastModified_filter_before_folder_deleting" );
        int beforeRemoving = filterPanel.getNumberAggregatedByContentType( "Folder" );
        int lastModifiedBeforeRemoving = filterPanel.getLastModifiedCount( "day" );
        saveScreenshot( "test_LastModified_aggregation_before_deleting" );

        when: "the folder has been deleted"
        contentBrowsePanel.selectContentInGrid( TEST_FOLDER.getName() ).clickToolbarArchive().clickOnDeleteAndWaitForClosed(  );
        sleep( 2000 );
        saveScreenshot( "test_LastModified_aggregation_folder_deleted" )

        then: "'folders' count should be reduced by 1"
        beforeRemoving - filterPanel.getNumberAggregatedByContentType( "Folder" ) == 1

        and: "'last modified' count should be reduced by 1"
        lastModifiedBeforeRemoving - filterPanel.getLastModifiedCount( "day" ) == 1;
    }

    def "GIVEN FilterPanel is opened and no selections or text-search WHEN the name of the content has been typed THEN all filters should be updated to only contain entries with matches in text-search"()
    {
        given: "FilterPanel is opened nd no selections or text-search"
        contentBrowsePanel.doShowFilterPanel();

        when: "folder's name  has been typed in the search-input"
        filterPanel.typeSearchText( TEST_FOLDER1.getName() );
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        saveScreenshot( "test_aggregation_searchText_typed" );

        then: "all filters should be updated to only contain entries with matches in text-search"
        filterPanel.getNumberAggregatedByContentType( "Folder" ) == 1;

        and:
        filterPanel.getLastModifiedCount( "hour" ) == 1;
    }
    //verifies the "XP-3586 Content not correctly filtered, when filter panel has been hidden"
    def "GIVEN  filter panel is opened WHEN name of folder has been typed in the search-input AND filter panel has been hidden AND new folder has been added THEN only one folder with matches in text-search should be displayed"()
    {
        given: "FilterPanel is opened AND the name of existing folder has been typed"
        filterPanel.typeSearchText( TEST_FOLDER1.getName() );

        when: "filter panel has been hidden"
        contentBrowsePanel.doHideFilterPanel();

        and: "new folder has been added"
        Content newFolder = buildFolderContent( "folder", "test for  hidden filter panel" );
        addContent( newFolder );
        saveScreenshot( "test_text_typed_filter_panel_hidden" );

        then: "only one folder with matches in text-search should be displayed"
        contentBrowsePanel.getRowsCount() == 1; ;
    }

    def "GIVEN no selections in the filter panel WHEN selecting one entry in ContentTypes-aggregation THEN no changes should be in ContentTypes-aggregation"()
    {
        given: "no selections in filter and filter panel shown"
        contentBrowsePanel.doShowFilterPanel();
        List<String> beforeSelect = filterPanel.getContentTypesAggregationValues();

        when: "selecting one entry in ContentTypes-filter"
        filterPanel.selectContentTypeInAggregationView( "Folder" );

        then: "no changes should be in ContentTypes-filter"
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
        saveScreenshot( "test_filter_panel_image_selected" );
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
        filterPanel.typeSearchText( TEST_FOLDER1.getName() );
        saveScreenshot( "test_aggregation_selected_content_name_typed" );

        then: "all filters should be updated to only contain entries with selection"
        Integer newNumberOfFolders = filterPanel.getNumberAggregatedByContentType( "Folder" );
        newNumberOfFolders == 1;

        and:
        newNumberOfFolders != numberOfFoldersBefore;

        and: "number of checkbox in aggregation view is 1"
        filterPanel.getContentTypesAggregationValues().size() == 1
    }
}
