package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseFilterPanel.ContenTypeDispalyNames
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.api.schema.content.ContentTypeName
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class ContentBrowsePanel_GridPanel_FilterSpec
    extends BaseGebSpec
{

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    @Shared
    ContentBrowseFilterPanel filterPanel;

    @Shared
    String INITIAL_CONTENT_FOLDER_NAME = "imagearchive";

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
        filterPanel = contentBrowsePanel.getFilterPanel();
    }

    def "GIVEN No selections in filter WHEN Selecting one entry in ContentTypes-filter THEN all existing Content of the selected type should be listed in gridPanel"()
    {
        given:
        boolean isClearFilterPresent = filterPanel.waitForClearFilterLinkNotvisible();
        String name = NameHelper.uniqueName( "page" );
        Content page = Content.builder().
            name( name ).
            displayName( "page" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.page() ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( page.getContentTypeName() ).typeData( page ).save().close();
        contentBrowsePanel.waituntilPageLoaded( 3 );

        when:
        filterPanel.selectEntryInContentTypesFilter( ContenTypeDispalyNames.PAGE.getValue() )
        contentBrowsePanel.waitsForSpinnerNotVisible( 2 )

        then:
        Integer numberOfFilteredContent = filterPanel.getNumberFilteredByContenttype( ContenTypeDispalyNames.PAGE.getValue() )
        numberOfFilteredContent == contentBrowsePanel.getContentNamesFromBrowsePanel().size()
    }

    def "GIVEN Selections in any filter WHEN clicking clean filter THEN initial grid view displayed "()
    {
        given:
        filterPanel.selectEntryInContentTypesFilter( ContenTypeDispalyNames.PAGE.getValue() );
        boolean beforeClean = contentBrowsePanel.exists( ContentPath.from( INITIAL_CONTENT_FOLDER_NAME ) );

        when:
        filterPanel.clickByCleanFilter();
        contentBrowsePanel.waitsForSpinnerNotVisible( 2 );

        then:
        !beforeClean && contentBrowsePanel.exists( ContentPath.from( INITIAL_CONTENT_FOLDER_NAME ) );
    }

    def "GIVEN One selection in ContentTypes-filter WHEN Selecting one additional entry in ContentTypes-filter THEN all existing Content of the both selected types should be listed in gridPanel"()
    {
        given:
        filterPanel.selectEntryInContentTypesFilter( ContenTypeDispalyNames.PAGE.getValue() );
        contentBrowsePanel.waitsForSpinnerNotVisible( 1 );
        Integer numberOfPages = filterPanel.getNumberFilteredByContenttype( ContenTypeDispalyNames.PAGE.getValue() );

        when:
        filterPanel.selectEntryInContentTypesFilter( ContenTypeDispalyNames.FOLDER.getValue() );
        contentBrowsePanel.waitsForSpinnerNotVisible( 1 );

        then:
        Integer numberOfFolder = filterPanel.getNumberFilteredByContenttype( ContenTypeDispalyNames.FOLDER.getValue() );
        ( numberOfFolder + numberOfPages ) == contentBrowsePanel.getContentNamesFromBrowsePanel().size();
    }

    def "GIVEN One one selection in any filter WHEN deselecting selection THEN initial grid view displayed "()
    {
        given:
        filterPanel.selectEntryInContentTypesFilter( ContenTypeDispalyNames.PAGE.getValue() );
        contentBrowsePanel.waitsForSpinnerNotVisible( 1 );
        boolean existsBeforeUnselect = contentBrowsePanel.exists( ContentPath.from( INITIAL_CONTENT_FOLDER_NAME ) );

        when:
        filterPanel.deSelectEntryInContentTypesFilter( ContenTypeDispalyNames.PAGE.getValue() );
        contentBrowsePanel.waitsForSpinnerNotVisible( 1 );


        then:
        !existsBeforeUnselect && contentBrowsePanel.exists( ContentPath.from( INITIAL_CONTENT_FOLDER_NAME ) );
    }

    def "GIVEN empty text-search WHEN adding text-search THEN all Content matching the text-search should be listed in gridPanel"()
    {
        given:
        String text = filterPanel.typeSearchText( "" );
        contentBrowsePanel.waitsForSpinnerNotVisible();

        when:
        filterPanel.typeSearchText( INITIAL_CONTENT_FOLDER_NAME );
        contentBrowsePanel.waitsForSpinnerNotVisible();
        TestUtils.saveScreenshot( getTestSession(), "text-search1" );

        then:
        contentBrowsePanel.exists( ContentPath.from( INITIAL_CONTENT_FOLDER_NAME ) ) &&
           isStringPresentInName(contentBrowsePanel.getContentNamesFromBrowsePanel(), INITIAL_CONTENT_FOLDER_NAME);
    }

    def "GIVEN any value in text-search WHEN clicking clean filter THEN initial grid view displayed"()
    {
        given:
        filterPanel.typeSearchText( INITIAL_CONTENT_FOLDER_NAME );
        contentBrowsePanel.waitsForSpinnerNotVisible();

        when:
        filterPanel.clickByCleanFilter();
        contentBrowsePanel.waitsForSpinnerNotVisible();
        TestUtils.saveScreenshot( getTestSession(), "text-search2" );

        then:
        contentBrowsePanel.getContentNamesFromBrowsePanel().size() > 1;
    }

  private isStringPresentInName(List<String>allNames, String name)
  {
	  if(allNames.isEmpty())
	  {
		  return false;
	  }
	  for(String uiName: allNames)
	  {
		  if(!uiName.contains(name))
		  {
			  return false;
		  }
	  }
	  return true;
  }
}
