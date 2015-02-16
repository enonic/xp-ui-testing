package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.schema.content.ContentTypeName
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class DeleteContentDialogSpec
    extends BaseGebSpec
{
    @Shared
    ContentBrowsePanel contentBrowsePanel;

    @Shared
    String CONTENT_TO_DELETE_KEY = "deletecomntent_dialog_test";

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );

    }

    def "setup: add a folder-content"()
    {
        given:
        String name = "foldertodelete";
        Content content = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "foldertodelete" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.folder() ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close(
            content.getDisplayName() );
        getTestSession().put( CONTENT_TO_DELETE_KEY, content );
    }

    def "GIVEN content App BrowsePanel and existing content WHEN content selected and Delete button clicked THEN delete dialog with title 'Delete Content' showed"()
    {
        given:
        List<Content> contentList = new ArrayList<>();
        Content content = (Content) getTestSession().get( CONTENT_TO_DELETE_KEY );
        contentList.add( content );

        when:
        DeleteContentDialog dialog = contentBrowsePanel.selectContentInTable( contentList ).
            clickToolbarDelete();

        then:
        dialog.waitForOpened();
    }

    def "GIVEN content BrowsePanel and existing content WHEN one content selected and Delete button clicked THEN delete dialog with one content is displayed"()
    {
        given:
        List<Content> contentList = new ArrayList<>();
        Content content = (Content) getTestSession().get( CONTENT_TO_DELETE_KEY );
        contentList.add( content );

        when:
        DeleteContentDialog dialog = contentBrowsePanel.selectContentInTable( contentList ).
            clickToolbarDelete();
        dialog.waitForOpened();

        then:
        List<String> namesFromDialog = dialog.getContentNameToDelete();
        namesFromDialog.size() == 1 && content.getDisplayName().equals( namesFromDialog.get( 0 ) )
    }
}
