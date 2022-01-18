package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel.ContentTypeDisplayNames
import spock.lang.Ignore
import spock.lang.Stepwise

@Stepwise
@Ignore
class ContentBrowsePanel_GridPanel_FilterSpec
    extends BaseContentSpec
{

    def "GIVEN grid panel is opened WHEN button 'show filter' has been clicked THEN filter panel should be present"()
    {
        when: "button 'show filter' has been clicked "
        contentBrowsePanel.doShowFilterPanel();

        then: "filter panel should appear"
        filterPanel.isFilterPanelDisplayed();
    }

    def "GIVEN 'Filter Panel' is opened and 'Shortcut' aggregation-checkbox is checked WHEN 'Clear' link has been clicked THEN initial grid view should be displayed"()
    {
        given: "'Filter Panel' is opened and 'Shortcut' checkbox is checked"
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.selectContentTypeInAggregationView( ContentTypeDisplayNames.SHORTCUT.getValue() );
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        boolean beforeClean = contentBrowsePanel.exists( IMPORTED_FOLDER_NAME );

        when: "'Clear' link has been clicked"
        filterPanel.clickOnCleanFilter();
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        then: "initial grid view should be restored"
        !beforeClean && contentBrowsePanel.exists( IMPORTED_FOLDER_NAME );
    }

    def "GIVEN 'Shortcut' aggregation-checkbox is checked WHEN 'Folder' aggregation-checkbox has been checked THEN number of items in grid should be updated"()
    {
        given: "'Shortcut' checkbox is checked"
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.selectContentTypeInAggregationView( ContentTypeDisplayNames.SHORTCUT.getValue() );
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        Integer numberOfShortcuts = filterPanel.getNumberAggregatedByContentType( ContentTypeDisplayNames.SHORTCUT.getValue() );

        when: "'Folder' checkbox has been checked "
        filterPanel.selectContentTypeInAggregationView( ContentTypeDisplayNames.IMAGE.getValue() );
        sleep( 1000 );
        saveScreenshot( "folder_shortcut_aggregated" );

        then: "all content in the grid should be correctly filtered"
        Integer numberOfImages = filterPanel.getNumberAggregatedByContentType( ContentTypeDisplayNames.IMAGE.getValue() );
        ( numberOfImages + numberOfShortcuts ) == contentBrowsePanel.getRowsCount();
    }

    def "GIVEN 'Shortcut' aggregation-checkbox is checked WHEN 'Shortcut' aggregation-checkbox has been unchecked THEN initial grid should be displayed"()
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
        when: "existing name of folder has been typed in the Filter Panel"
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.typeSearchText( IMAGES_FOLDER_DISPLAY_NAME );
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        then: "one content should be present in the grid"
        contentBrowsePanel.exists( IMPORTED_FOLDER_NAME );
        and: "one item should be present in the grid:"
        contentBrowsePanel.getRowsCount() == 1;
    }
}
