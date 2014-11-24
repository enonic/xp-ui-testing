package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.api.schema.content.ContentTypeName
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class ContentBrowseItemsSelectionPanel_ShoppingCart
    extends BaseGebSpec
{

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }


    def "GIVEN expanded parent content and content beneath the parent, both contents a selected  WHEN parent content is unexpanded  THEN only one item is selected in the grid panel but items present in selection panel"()
    {
        setup: "build a new folder-content and archive content"
        String name = NameHelper.uniqueName( "folder" );

        Content parentContent = Content.builder().
            name( name ).
            displayName( "parentContent" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.ROOT ).
            build();
        String archiveName = NameHelper.uniqueName( "archive" );
        Content archiveContent = Content.builder().
            name( archiveName ).
            displayName( "childArchive" ).
            contentType( ContentTypeName.archiveMedia() ).
            parent( ContentPath.from( parentContent.getName() ) ).
            build();

        and: "add new content: click on 'new' button, populate a wizard and close it"
        contentBrowsePanel.clickToolbarNew().selectContentType( parentContent.getContentTypeName() ).typeData(
            parentContent ).save().close();
        contentBrowsePanel.waitsForSpinnerNotVisible();

        //workaround for  CMS-4406
        contentBrowsePanel.refreshPanelInBrowser();
        contentBrowsePanel.waitsForSpinnerNotVisible();

        contentBrowsePanel.selectContentInTable( parentContent.getPath() );
        contentBrowsePanel.clickToolbarNew().selectContentType( archiveContent.getContentTypeName() ).typeData(
            archiveContent ).save().close();
        contentBrowsePanel.waitsForSpinnerNotVisible();

        //workaround for  CMS-4406
        contentBrowsePanel.refreshPanelInBrowser();
        contentBrowsePanel.waitsForSpinnerNotVisible();

        contentBrowsePanel.expandContent( parentContent.getPath() );
        List<Content> contents = new ArrayList<>();
        contents.add( parentContent );
        contents.add( archiveContent );
        contentBrowsePanel.selectContentInTable( contents );
        int before = contentBrowsePanel.getItemSelectionPanel().getSelectedItemCount()

        when: "un expand a parent content "
        contentBrowsePanel.unExpandContent( parentContent.getPath() );


        then: "content deleted and  not exist in view"
        // contentBrowsePanel.isRowSelected(  )
        List<String> selectedNames = contentBrowsePanel.getItemSelectionPanel().getSelectedItemDisplayNames();
        selectedNames.contains( "parentContent" ) && selectedNames.contains( "childArchive" ) && selectedNames.size() == 2;
        // contentBrowsePanel.getItemSelectionPanel(  ).getSelectedItemCount(  )  == before ;
    }

    def "GIVEN expanded parent content and content beneath the parent, both contents a selected  WHEN parent content is unexpanded  THEN only"()
    {

    }

}
