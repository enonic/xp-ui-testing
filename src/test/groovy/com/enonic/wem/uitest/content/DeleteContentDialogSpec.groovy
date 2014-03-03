package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent
import com.enonic.autotests.vo.contentmanager.FolderContent
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class DeleteContentDialogSpec
    extends BaseGebSpec
{

    @Shared
    String CONTENT_TO_DELETE_KEY = "deletecomntent_dialog_test"

    def "setup: add a folder-content"()
    {
        given:
        go "admin"
        String name = "foldertodelete";
        BaseAbstractContent content = FolderContent.builder().
            withName( NameHelper.unqiueName( name ) ).
            withDisplayName( "foldertodelete" ).
            withParent( ContentPath.ROOT ).
            build();
        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() )
        contentBrowsePanel.doAddContent( content, true )
        getTestSession().put( CONTENT_TO_DELETE_KEY, content );
    }

    def "GIVEN content App BrowsePanel and existing content WHEN content selected and Delete button clicked THEN delete dialog with title 'Delete Content' showed"()
    {
        given:
        go "admin"
        List<BaseAbstractContent> contents = new ArrayList<>();
        BaseAbstractContent content = (BaseAbstractContent) getTestSession().get( CONTENT_TO_DELETE_KEY );
        contents.add( content );

        when:
        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentApp( session )
        DeleteContentDialog dialog = contentBrowsePanel.openDeleteContentDialog( contents )

        then:
        dialog.isOpened();
    }

    def "GIVEN content BrowsePanel and existing content WHEN one content selected and Delete button clicked THEN delete dialog with one content is displayed"()
    {
        given:
        go "admin"
        List<BaseAbstractContent> contents = new ArrayList<>();
        BaseAbstractContent content = (BaseAbstractContent) getTestSession().get( CONTENT_TO_DELETE_KEY );
        contents.add( content );

        when:
        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentApp( session )
        DeleteContentDialog dialog = contentBrowsePanel.openDeleteContentDialog( contents )

        then:
        List<String> namesFromDialog = dialog.getContentNameToDelete();
        namesFromDialog.size() == 1 && content.getDisplayName().equals( namesFromDialog.get( 0 ) )
    }
}
