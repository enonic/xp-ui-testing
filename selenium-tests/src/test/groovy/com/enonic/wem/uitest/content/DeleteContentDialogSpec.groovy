package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class DeleteContentDialogSpec
    extends BaseContentSpec
{
    @Shared
    Content CONTENT;

    def "setup: add a folder-content"()
    {
        given:
        CONTENT = buildFolderContent( "foldertodelete", "foldertodelete" );
        addContent( CONTENT );
    }

    def "GIVEN content App BrowsePanel and existing content WHEN content selected and Delete button clicked THEN delete dialog with title 'Delete Content' showed"()
    {
        given:
        List<String> contentList = new ArrayList<>();
        contentList.add( CONTENT.getName() );

        when: "content selected and Delete button clicked"
        DeleteContentDialog dialog = contentBrowsePanel.selectContentInTable( contentList ).clickToolbarDelete();

        then: "delete dialog with title 'Delete Content' showed"
        dialog.waitForOpened();
    }

    def "GIVEN content BrowsePanel and existing content WHEN one content selected and Delete button clicked THEN delete dialog with one content is displayed"()
    {
        given:
        List<String> contentList = new ArrayList<>();
        contentList.add( CONTENT.getName() );

        when: "one content selected and Delete button clicked"
        DeleteContentDialog dialog = contentBrowsePanel.selectContentInTable( contentList ).clickToolbarDelete();
        dialog.waitForOpened();

        then: "delete dialog with one content is displayed"
        List<String> namesFromDialog = dialog.getContentNameToDelete();
        namesFromDialog.size() == 1 && CONTENT.getDisplayName().equals( namesFromDialog.get( 0 ) )
    }
}
