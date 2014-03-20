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
    ContentBrowsePanel contentBrowsePanel

    @Shared
    String CONTENT_TO_DELETE_KEY = "deletecomntent_dialog_test"

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() )

    }

    def "setup: add a folder-content"()
    {
        given:
        String name = "foldertodelete";
        BaseAbstractContent content = FolderContent.builder().
            withName( NameHelper.uniqueName( name ) ).
            withDisplayName( "foldertodelete" ).
            withParent( ContentPath.ROOT ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close();
        getTestSession().put( CONTENT_TO_DELETE_KEY, content );
    }

    def "GIVEN content App BrowsePanel and existing content WHEN content selected and Delete button clicked THEN delete dialog with title 'Delete Content' showed"()
    {
        given:
        List<BaseAbstractContent> contentList = new ArrayList<>();
        BaseAbstractContent content = (BaseAbstractContent) getTestSession().get( CONTENT_TO_DELETE_KEY );
        contentList.add( content );

        when:
        DeleteContentDialog dialog = contentBrowsePanel.expandContent( content.getParent() ).selectContentInTable(
            contentList ).clickToolbarDelete()

        then:
        dialog.waitForOpened();
    }

    def "GIVEN content BrowsePanel and existing content WHEN one content selected and Delete button clicked THEN delete dialog with one content is displayed"()
    {
        given:
        go "admin"
        List<BaseAbstractContent> contentList = new ArrayList<>();
        BaseAbstractContent content = (BaseAbstractContent) getTestSession().get( CONTENT_TO_DELETE_KEY );
        contentList.add( content );

        when:
        DeleteContentDialog dialog = contentBrowsePanel.expandContent( content.getParent() ).selectContentInTable(
            contentList ).clickToolbarDelete()
        dialog.waitForOpened()

        then:
        List<String> namesFromDialog = dialog.getContentNameToDelete();
        namesFromDialog.size() == 1 && content.getDisplayName().equals( namesFromDialog.get( 0 ) )
    }
}
