package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent
import com.enonic.autotests.vo.contentmanager.FolderContent
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class DeleteContentDialogSpec extends BaseGebSpec
{

    @Shared String CONTENT_TO_DELETE_KEY = "deletecomntent_dialog_test"

    def "setup: add a folder-content"( )
    {
        given:
        go "admin"
        BaseAbstractContent content = FolderContent.builder().withName( "foldertodelete" ).withDisplayName( "foldertodelete" ).build();
        contentService.addContent( getTestSession(), content, true )
        getTestSession().put( CONTENT_TO_DELETE_KEY, content );
    }

    def "GIVEN content App BrowsePanel and existing content WHEN content selected and Delete button clicked THEN delete dialog with title 'Delete Content' showed"( )
    {
        given:
        go "admin"
        List<BaseAbstractContent> contents = new ArrayList<>();
        BaseAbstractContent content = (BaseAbstractContent) getTestSession().get( CONTENT_TO_DELETE_KEY );
        contents.add( content );

        when:
        DeleteContentDialog dialog = contentService.selectContentClickDeleteInToolbar( getTestSession(), contents );

        then:
        dialog.isOpened();
    }

    def "GIVEN content BrowsePanel and existing content WHEN one content selected and Delete button clicked THEN delete dialog with one content is displayed"( )
    {
        given:
        go "admin"
        List<BaseAbstractContent> contents = new ArrayList<>();
        BaseAbstractContent content = (BaseAbstractContent) getTestSession().get( CONTENT_TO_DELETE_KEY );
        contents.add( content );

        when:
        DeleteContentDialog dialog = contentService.selectContentClickDeleteInToolbar( getTestSession(), contents );

        then:
        List<String> namesFromDialog = dialog.getContentNameToDelete();
        namesFromDialog.size() == 1 && content.getDisplayName().equals( namesFromDialog.get( 0 ) )
    }
}
