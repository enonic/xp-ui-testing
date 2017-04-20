package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel.ContentTypeDisplayNames
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_GridPanel_FilterSpec
    extends BaseContentSpec
{
    @Shared
    Content initialFolder;


    def "add initial content"()
    {
        when:
        initialFolder = buildFolderContent( "initcontent", "filter tests" );
        addContent( initialFolder );

        then:
        filterPanel.typeSearchText( initialFolder.getName() )
        contentBrowsePanel.exists( initialFolder.getName() );
    }

    def "GIVEN browse panel opened WHEN filter panel not displayed AND button 'show filter' clicked THEN filter panel appears"()
    {
        given: 'check for filter panel displayed'
        def displayed = filterPanel.isFilterPanelDisplayed();

        when: "panel not displayed adn button 'show filter panel' clicked"
        displayed || contentBrowsePanel.doShowFilterPanel();
        saveScreenshot( "filter_panel_shown" )

        then: "filter panel displayed"
        filterPanel.isFilterPanelDisplayed();
    }

    def "GIVEN No selections in filter WHEN Selecting one entry in ContentTypes-filter THEN all existing Content of the selected type should be listed in gridPanel"()
    {
        given: "No selections in filter"
        String name = NameHelper.uniqueName( "unstructured" );
        Content content = Content.builder().
            name( name ).
            displayName( "unstructured" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.unstructured() ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData(
            content ).save().closeBrowserTab().switchToBrowsePanelTab();

        and: "filter panel is opened"
        contentBrowsePanel.doShowFilterPanel();

        when: "Selecting one entry in ContentTypes-filter"
        filterPanel.selectContentTypeInAggregationView( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        saveScreenshot( "filtered_unstructured" )

        then: "all existing Content of the selected type should be listed in gridPanel"
        Integer numberOfFilteredContent = filterPanel.getNumberAggregatedByContentType( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );
        numberOfFilteredContent == contentBrowsePanel.getRowsCount();
    }

    def "GIVEN selections in any filter WHEN clicking clean filter THEN initial grid view displayed"()
    {
        given: "selections in any filter"
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.selectContentTypeInAggregationView( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        boolean beforeClean = contentBrowsePanel.exists( initialFolder.getName() );

        when: "clicking on the 'clean filter' "
        filterPanel.clickOnCleanFilter();
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        then: "initial grid view should be displayed"
        !beforeClean && contentBrowsePanel.exists( initialFolder.getName() );
    }

    def "GIVEN one selection in ContentTypes-filter WHEN selecting one additional entry in ContentTypes-filter THEN all existing content of the both selected types should be listed in gridPanel"()
    {
        given: "one selection in ContentTypes-filter"
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.selectContentTypeInAggregationView( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        Integer numberOfData = filterPanel.getNumberAggregatedByContentType( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );

        when: "selecting one additional entry in ContentTypes-filter"
        filterPanel.selectContentTypeInAggregationView( ContentTypeDisplayNames.FOLDER.getValue() );
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        saveScreenshot( "filtering_one-selection1" );

        then: "all existing content of the both selected types should be listed in gridPanel"
        Integer numberOfFolder = filterPanel.getNumberAggregatedByContentType( ContentTypeDisplayNames.FOLDER.getValue() );
        ( numberOfFolder + numberOfData ) == contentBrowsePanel.getRowsCount();
    }

    def "GIVEN one selection in any filter WHEN deselecting selection THEN initial grid view displayed"()
    {
        given: "one selection in any filter"
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.selectContentTypeInAggregationView( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        saveScreenshot( "one-selection2" );
        boolean existsBeforeDeselect = contentBrowsePanel.exists( initialFolder.getName() );

        when: "deselecting of the selection"
        filterPanel.deselectContentTypeInAggregationView( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );
        saveScreenshot( "one-selection-deselected" );
        contentBrowsePanel.waitsForSpinnerNotVisible();

        then: "initial grid view should be displayed"
        !existsBeforeDeselect && contentBrowsePanel.exists( initialFolder.getName() );
    }

    def "GIVEN empty text-search WHEN adding text-search THEN all Content matching the text-search should be listed in gridPanel"()
    {
        when: "adding text-search in filter panel"
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.typeSearchText( initialFolder.getName() );
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        saveScreenshot( "filtered_text-search1" );

        then: "all Content matching the text-search should be listed in gridPanel"
        contentBrowsePanel.exists( initialFolder.getName() );
    }

    def "GIVEN any value in text-search WHEN clicking clean filter THEN initial grid view displayed"()
    {
        given: "any value in text-search"
        contentBrowsePanel.doShowFilterPanel();
        Content folder = buildFolderContent( "folder", "filter test" )
        filterPanel.typeSearchText( folder.getName() );
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        when: "clicking clean filter"
        filterPanel.clickOnCleanFilter();
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        saveScreenshot( "filtered_text-search2" );

        then: "initial grid view should be displayed"
        contentBrowsePanel.getRowsCount() > 1 && contentBrowsePanel.exists( initialFolder.getName() );
    }
}
