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
    private final PARENT_ROOT_CONTENT = "parent_content"

    private final ARCHIVE_CHILD_CONTENT = "archive_root_content"

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }


    def "GIVEN expanded parent content and content beneath the parent, both contents are selected  WHEN parent content is collapsed  THEN only one item is selected in the grid panel but two items present in selection panel"()
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
        getTestSession().put( PARENT_ROOT_CONTENT, parentContent );

        //workaround for  CMS-4406
        contentBrowsePanel.refreshPanelInBrowser();
        contentBrowsePanel.waitsForSpinnerNotVisible();

        and: "add new child content beneath the parent"
        contentBrowsePanel.selectContentInTable( parentContent.getPath() );
        contentBrowsePanel.clickToolbarNew().selectContentType( archiveContent.getContentTypeName() ).typeData(
            archiveContent ).save().close();
        getTestSession().put( ARCHIVE_CHILD_CONTENT, archiveContent );
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


        then: "if parent and child content are selected and parent content collapsed, item selection panel should contains two items "
        List<String> selectedNames = contentBrowsePanel.getItemSelectionPanel().getSelectedItemDisplayNames();
        selectedNames.contains( "parentContent" ) && selectedNames.contains( "childArchive" ) && selectedNames.size() == 2;
    }

    def "GIVEN a selected content  WHEN search text typed and one more content selected  THEN only one item should be  present in selection panel"()
    {
        setup: "select a root content and type search text in filter panel"
        Content parentContent = getTestSession().get( PARENT_ROOT_CONTENT );
        Content childContent = getTestSession().get( ARCHIVE_CHILD_CONTENT );
        contentBrowsePanel.selectContentInTable( parentContent.getPath() );
        contentBrowsePanel.getFilterPanel().typeSearchText( childContent.getName() );

        when: "all contents filtered and row with one more content clicked "
        contentBrowsePanel.selectRowByContentPath( childContent.getPath().toString() );

        then: "only one item should be present "
        List<String> selectedNames = contentBrowsePanel.getItemSelectionPanel().getSelectedItemDisplayNames();
        //selectedNames.contains( "parentContent" ) && selectedNames.contains( "childArchive" ) &&
        selectedNames.size() == 1;
    }


}
