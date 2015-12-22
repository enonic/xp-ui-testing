package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel.ContentTypeDisplayNames
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
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
        contentBrowsePanel.exists( initialFolder.getName() );
    }

    def "GIVEN browse panel opened WHEN filter panel not displayed AND button 'show filter' clicked THEN filter panel appears"()
    {
        given: 'check for filter panel displayed'
        def displayed = filterPanel.isFilterPanelDisplayed();

        when: "panel not displayed adn button 'show filter panel' clicked"
        displayed || contentBrowsePanel.doShowFilterPanel();
        TestUtils.saveScreenshot( getSession(), "filter_panel_shown" )

        then: "filter panel displayed"
        filterPanel.isFilterPanelDisplayed();
    }

    def "GIVEN No selections in filter WHEN Selecting one entry in ContentTypes-filter THEN all existing Content of the selected type should be listed in gridPanel"()
    {
        given:
        String name = NameHelper.uniqueName( "unstructured" );
        Content content = Content.builder().
            name( name ).
            displayName( "unstructured" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.unstructured() ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close(
            content.getDisplayName() );
        contentBrowsePanel.doShowFilterPanel();

        when:
        filterPanel.selectEntryInContentTypesFilter( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );
        contentBrowsePanel.waitsForSpinnerNotVisible();
        TestUtils.saveScreenshot( getSession(), "filter_unstructured" )

        then:
        Integer numberOfFilteredContent = filterPanel.getNumberFilteredByContentType( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );
        numberOfFilteredContent == contentBrowsePanel.getRowNumber();
    }

    def "GIVEN selections in any filter WHEN clicking clean filter THEN initial grid view displayed"()
    {
        given:
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.selectEntryInContentTypesFilter( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );
        contentBrowsePanel.waitsForSpinnerNotVisible();
        boolean beforeClean = contentBrowsePanel.exists( initialFolder.getName() );

        when:
        filterPanel.clickOnCleanFilter();
        contentBrowsePanel.waitsForSpinnerNotVisible();

        then:
        !beforeClean && contentBrowsePanel.exists( initialFolder.getName() );
    }

    def "GIVEN one selection in ContentTypes-filter WHEN selecting one additional entry in ContentTypes-filter THEN all existing content of the both selected types should be listed in gridPanel"()
    {
        given:
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.selectEntryInContentTypesFilter( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );
        contentBrowsePanel.waitsForSpinnerNotVisible();
        Integer numberOfData = filterPanel.getNumberFilteredByContentType( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );

        when:
        filterPanel.selectEntryInContentTypesFilter( ContentTypeDisplayNames.FOLDER.getValue() );
        contentBrowsePanel.waitsForSpinnerNotVisible( 1 );
        TestUtils.saveScreenshot( getTestSession(), "one-selection1" );

        then:
        Integer numberOfFolder = filterPanel.getNumberFilteredByContentType( ContentTypeDisplayNames.FOLDER.getValue() );
        ( numberOfFolder + numberOfData ) == contentBrowsePanel.getRowNumber();
    }

    def "GIVEN one selection in any filter WHEN deselecting selection THEN initial grid view displayed "()
    {
        given:
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.selectEntryInContentTypesFilter( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );
        contentBrowsePanel.waitsForSpinnerNotVisible( 1 );
        TestUtils.saveScreenshot( getTestSession(), "one-selection2" );
        boolean existsBeforeDeselect = contentBrowsePanel.exists( initialFolder.getName() );

        when:
        filterPanel.deselectEntryInContentTypesFilter( ContentTypeDisplayNames.UNSTRUCTURED.getValue() );
        TestUtils.saveScreenshot( getTestSession(), "one-selection-deselected" );
        contentBrowsePanel.waitsForSpinnerNotVisible();

        then:
        !existsBeforeDeselect && contentBrowsePanel.exists( initialFolder.getName() );
    }

    def "GIVEN empty text-search WHEN adding text-search THEN all Content matching the text-search should be listed in gridPanel"()
    {
        when:
        contentBrowsePanel.doShowFilterPanel();
        filterPanel.typeSearchText( initialFolder.getName() );
        contentBrowsePanel.waitsForSpinnerNotVisible();
        TestUtils.saveScreenshot( getTestSession(), "text-search1" );

        then:
        contentBrowsePanel.exists( initialFolder.getName() );
    }

    def "GIVEN any value in text-search WHEN clicking clean filter THEN initial grid view displayed"()
    {
        given:
        contentBrowsePanel.doShowFilterPanel();
        Content folder = buildFolderContent( "folder", "filter test" )
        filterPanel.typeSearchText( folder.getName() );
        contentBrowsePanel.waitsForSpinnerNotVisible();

        when:
        filterPanel.clickOnCleanFilter();
        contentBrowsePanel.waitsForSpinnerNotVisible();
        TestUtils.saveScreenshot( getTestSession(), "text-search2" );

        then:
        contentBrowsePanel.getRowNumber() > 1 && contentBrowsePanel.exists( initialFolder.getName() );
    }
}
