package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel.ContentTypeDisplayNames
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_GridPanel_FilterSpec
    extends BaseContentSpec
{

    def "GIVEN grid panel is opened WHEN button 'show filter' has been clicked THEN filter panel should be present"()
    {
        given: "grid panel is opened"
        def displayed = filterPanel.isFilterPanelDisplayed();

        when: "button 'show filter' has been clicked "
        contentBrowsePanel.doShowFilterPanel();

        then: "filter panel should be displayed"
        filterPanel.isFilterPanelDisplayed();
    }

    def "GIVEN 'Filter Panel' is opened and 'Shortcut' checkbox is checked WHEN 'Clear' link has been clicked THEN initial grid view should be displayed"()
    {
        given: "selections in any filter"
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.selectContentTypeInAggregationView( ContentTypeDisplayNames.SHORTCUT.getValue() );
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        boolean beforeClean = contentBrowsePanel.exists( IMPORTED_FOLDER_NAME );

        when: "'Clear' link has been clicked"
        filterPanel.clickOnCleanFilter();
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        then: "initial grid view should be displayed"
        !beforeClean && contentBrowsePanel.exists( IMPORTED_FOLDER_NAME );
    }

    def "GIVEN 'Shortcut' checkbox is checked WHEN 'Folder' checkbox has been checked THEN all content should be correctly filtered"()
    {
        given: "'Shortcut' checkbox is checked"
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.selectContentTypeInAggregationView( ContentTypeDisplayNames.SHORTCUT.getValue() );
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        Integer numberOfData = filterPanel.getNumberAggregatedByContentType( ContentTypeDisplayNames.SHORTCUT.getValue() );

        when: "'Folder' checkbox has been checked "
        filterPanel.selectContentTypeInAggregationView( ContentTypeDisplayNames.FOLDER.getValue() );
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        saveScreenshot( "filtering_one-selection1" );

        then: "all content in the grid should be correctly filtered"
        Integer numberOfFolder = filterPanel.getNumberAggregatedByContentType( ContentTypeDisplayNames.FOLDER.getValue() );
        ( numberOfFolder + numberOfData ) == contentBrowsePanel.getRowsCount();
    }

    def "GIVEN 'Shortcut' checkbox is checked WHEN 'Shortcut' checkbox has been unchecked THEN initial grid should be displayed"()
    {
        given: "'Shortcut' checkbox is checked"
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.selectContentTypeInAggregationView( ContentTypeDisplayNames.SHORTCUT.getValue() );
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        saveScreenshot( "deselection_test1" );
        boolean existsBeforeDeselect = contentBrowsePanel.exists( IMPORTED_FOLDER_NAME );

        when: "'Shortcut' checkbox has been unchecked"
        filterPanel.deselectContentTypeInAggregationView( ContentTypeDisplayNames.SHORTCUT.getValue() );
        saveScreenshot( "deselection_test2" );
        contentBrowsePanel.waitsForSpinnerNotVisible();

        then: "initial grid view should be displayed"
        !existsBeforeDeselect && contentBrowsePanel.exists( IMPORTED_FOLDER_NAME );
    }

    def "GIVEN search-input is empty WHEN existing name of folder has been typed THEN one content should be present in the grid"()
    {
        when: "adding text-search in filter panel"
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.typeSearchText( IMPORTED_FOLDER_NAME );
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        then: "ne content should be present in the grid"
        contentBrowsePanel.exists( IMAGES_FOLDER_DISPLAY_NAME );
        and: ""
        contentBrowsePanel.getRowsCount() == 1;
    }
}
