package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel.ContentTypeDisplayNames
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.api.schema.content.ContentTypeName
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_GridPanel_FilterSpec
    extends BaseGebSpec
{

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    @Shared
    ContentBrowseFilterPanel filterPanel;

    @Shared
    String INITIAL_CONTENT_FOLDER_NAME = NameHelper.uniqueName( "initfolder" );

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
        filterPanel = contentBrowsePanel.getFilterPanel();
    }

    def "add initial content"()
    {
        when:
        Content initialFolder = Content.builder().
            name( INITIAL_CONTENT_FOLDER_NAME ).
            displayName( "initialFolder" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.folder() ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( initialFolder.getContentTypeName() ).typeData(
            initialFolder ).save().close();
        contentBrowsePanel.waitsForSpinnerNotVisible();

        then:
        contentBrowsePanel.exists( initialFolder.getPath() );
    }

    def "GIVEN No selections in filter WHEN Selecting one entry in ContentTypes-filter THEN all existing Content of the selected type should be listed in gridPanel"()
    {
        given:
        String name = NameHelper.uniqueName( "data" );
        Content data = Content.builder().
            name( name ).
            displayName( "data" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.dataMedia() ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( data.getContentTypeName() ).typeData( data ).save().close();

        when:
        filterPanel.selectEntryInContentTypesFilter( ContentTypeDisplayNames.DATA.getValue() );
        contentBrowsePanel.waitsForSpinnerNotVisible();

        then:
        Integer numberOfFilteredContent = filterPanel.getNumberFilteredByContentType( ContentTypeDisplayNames.DATA.getValue() );
        numberOfFilteredContent == contentBrowsePanel.getRowNumber();
    }

    def "GIVEN Selections in any filter WHEN clicking clean filter THEN initial grid view displayed"()
    {
        given:
        filterPanel.selectEntryInContentTypesFilter( ContentTypeDisplayNames.DATA.getValue() );
        contentBrowsePanel.waitsForSpinnerNotVisible();
        boolean beforeClean = contentBrowsePanel.exists( ContentPath.from( INITIAL_CONTENT_FOLDER_NAME ) );

        when:
        filterPanel.clickOnCleanFilter();
        contentBrowsePanel.waitsForSpinnerNotVisible();

        then:
        !beforeClean && contentBrowsePanel.exists( ContentPath.from( INITIAL_CONTENT_FOLDER_NAME ) );
    }

    def "GIVEN One selection in ContentTypes-filter WHEN Selecting one additional entry in ContentTypes-filter THEN all existing Content of the both selected types should be listed in gridPanel"()
    {
        given:
        filterPanel.selectEntryInContentTypesFilter( ContentTypeDisplayNames.DATA.getValue() );
        contentBrowsePanel.waitsForSpinnerNotVisible();
        Integer numberOfData = filterPanel.getNumberFilteredByContentType( ContentTypeDisplayNames.DATA.getValue() );

        when:
        filterPanel.selectEntryInContentTypesFilter( ContentTypeDisplayNames.FOLDER.getValue() );
        contentBrowsePanel.waitsForSpinnerNotVisible( 1 );

        then:
        Integer numberOfFolder = filterPanel.getNumberFilteredByContentType( ContentTypeDisplayNames.FOLDER.getValue() );
        ( numberOfFolder + numberOfData ) == contentBrowsePanel.getRowNumber();
    }

    def "GIVEN One one selection in any filter WHEN deselecting selection THEN initial grid view displayed "()
    {
        given:
        filterPanel.selectEntryInContentTypesFilter( ContentTypeDisplayNames.DATA.getValue() );
        contentBrowsePanel.waitsForSpinnerNotVisible( 1 );
        TestUtils.saveScreenshot( getTestSession(), "one-selection" );
        boolean existsBeforeDeselect = contentBrowsePanel.exists( ContentPath.from( INITIAL_CONTENT_FOLDER_NAME ) );

        when:
        filterPanel.deselectEntryInContentTypesFilter( ContentTypeDisplayNames.DATA.getValue() );
        TestUtils.saveScreenshot( getTestSession(), "one-selection-deselected" );
        contentBrowsePanel.waitsForSpinnerNotVisible();


        then:
        !existsBeforeDeselect && contentBrowsePanel.exists( ContentPath.from( INITIAL_CONTENT_FOLDER_NAME ) );
    }

    def "GIVEN empty text-search WHEN adding text-search THEN all Content matching the text-search should be listed in gridPanel"()
    {

        when:
        filterPanel.typeSearchText( INITIAL_CONTENT_FOLDER_NAME );
        contentBrowsePanel.waitsForSpinnerNotVisible();
        TestUtils.saveScreenshot( getTestSession(), "text-search1" );

        then:
        contentBrowsePanel.exists( ContentPath.from( INITIAL_CONTENT_FOLDER_NAME ) ) &&
            isStringPresentInName( contentBrowsePanel.getContentNamesFromBrowsePanel(), INITIAL_CONTENT_FOLDER_NAME );
    }

    def "GIVEN any value in text-search WHEN clicking clean filter THEN initial grid view displayed"()
    {
        given:
        String name = NameHelper.uniqueName( "archive" );
        Content page = Content.builder().
            name( name ).
            displayName( "archive" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.archiveMedia() ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( page.getContentTypeName() ).typeData( page ).save().close();
        contentBrowsePanel.waitUntilPageLoaded( Application.PAGE_LOAD_TIMEOUT );
        filterPanel.typeSearchText( name );
        contentBrowsePanel.waitsForSpinnerNotVisible();

        when:
        filterPanel.clickOnCleanFilter();
        contentBrowsePanel.waitsForSpinnerNotVisible();
        TestUtils.saveScreenshot( getTestSession(), "text-search2" );

        then:
        contentBrowsePanel.getRowNumber() > 1 && contentBrowsePanel.exists( ContentPath.from( INITIAL_CONTENT_FOLDER_NAME ) );
    }

    /**
     *
     * Checks that a string is present in all names of content
     * @param allNames
     * @param name
     * @return if each name contains a searched text.
     */
    private isStringPresentInName( List<String> allNames, String name )
    {
        if ( allNames.isEmpty() )
        {
            return false;
        }
        for ( String uiName : allNames )
        {
            if ( !uiName.contains( name ) )
            {
                return false;
            }
        }
        return true;
    }
}
