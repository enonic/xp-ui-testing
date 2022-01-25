package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.contentmanager.browsepanel.MoveContentDialog
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
@Ignore
class MoveContentDialogSpec
    extends BaseContentSpec
{

    @Shared
    String FIRST_DISPLAY_CONTENT_NAME = "first";

    @Shared
    String SECOND_DISPLAY_CONTENT_NAME = "second";

    @Shared
    Content FIRST_CONTENT;

    @Shared
    Content SECOND_CONTENT;


    def "First folder has been added"()
    {
        when: "First folder has been added"
        FIRST_CONTENT = buildFolderContent( "movetest", FIRST_DISPLAY_CONTENT_NAME );
        addContent( FIRST_CONTENT );

        then: "content should be listed in the root"
        filterPanel.typeSearchText( FIRST_CONTENT.getName() );
        contentBrowsePanel.exists( FIRST_CONTENT.getName() );
    }

    def "Second folder has been added"()
    {
        when: "Second folder has been added"
        SECOND_CONTENT = buildFolderContent( "movetest", SECOND_DISPLAY_CONTENT_NAME );
        addContent( SECOND_CONTENT );

        then: "content should be listed in the root"
        filterPanel.typeSearchText( SECOND_CONTENT.getName() );
        contentBrowsePanel.exists( SECOND_CONTENT.getName() );
    }

    def "GIVEN selected folder WHEN 'Move' button on toolbar has been pressed THEN modal dialog with correct title appears"()
    {
        given: "one content selected"
        findAndSelectContent( FIRST_CONTENT.getName() );

        when: "button 'Move' has been pressed"
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();
        saveScreenshot( "test_move_dialog" );

        then: "modal dialog with correct title appears"
        dialog.isOpened();
        and:
        dialog.getTitle() == MoveContentDialog.DIALOG_TITLE;
    }

    def "GIVEN first content is selected AND 'Move' button on toolbar pressed WHEN content moved to the second folder THEN first content should be listed beneath the second content"()
    {
        given:
        findAndSelectContent( FIRST_CONTENT.getName() )
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();

        when: "first content has been moved to the second content"
        dialog.typeSearchText( SECOND_CONTENT.getName() ).selectDestinationAndClickOnMove( SECOND_CONTENT.getName() );

        and: "second content has been expanded"
        filterPanel.typeSearchText( SECOND_CONTENT.getName() );
        contentBrowsePanel.expandContent( SECOND_CONTENT.getPath() );

        then: "first content should be listed beneath the second content"
        contentBrowsePanel.exists( FIRST_CONTENT.getName() );
    }

    def "GIVEN existing folder with a child WHEN parent folder expanded AND one more content moved to the parent THEN new child appears beneath the parent"()
    {
        given: "new folder has been added in ROOT"
        Content folderToMove = buildFolderContent( "move_expanded", "move to expanded folder" );
        addContent( folderToMove );

        and: "existing parent folder has been expanded"
        contentBrowsePanel.expandContent( SECOND_CONTENT.getPath() );
        filterPanel.clickOnCleanFilter();

        and: "folder in the root is selected and 'Move' button has been pressed"
        contentBrowsePanel.clickCheckboxAndSelectRow( folderToMove.getName() );
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();

        when: "just added folder moved to the parent folder"
        dialog.typeSearchText( SECOND_CONTENT.getName() ).selectDestinationAndClickOnMove( SECOND_CONTENT.getName() );
        saveScreenshot( "test_move_content_to_expanded_folder" );

        then: "new folder should be listed beneath the parent"
        contentBrowsePanel.exists( folderToMove.getName() );
    }

    def "GIVEN 'move' dialog is opened WHEN 'close' button has been clicked THEN dialog should be closed"()
    {
        given: "'move' dialog is opened"
        findAndSelectContent( FIRST_CONTENT.getName() );
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();

        when: "the 'cancel' button on the bottom of dialog pressed"
        dialog.clickOnCancelBottomButton();

        then: "dialog should be closed"
        !dialog.isOpened();
    }

    def "GIVEN 'move' dialog is opened WHEN 'cancel' button clicked  THEN modal dialog disappears"()
    {
        given: "'move' dialog is opened"
        findAndSelectContent( FIRST_CONTENT.getName() );
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();

        when: "the 'cancel' button on the top of dialog has been pressed"
        dialog.clickOnCancelTopButton();

        then: "dialog should be closed"
        !dialog.isOpened()
    }

    def "GIVEN 'move' dialog is opened WHEN 'Esc' key has been pressed  THEN dialog should be closed"()
    {
        given: "'move' dialog is opened"
        findAndSelectContent( FIRST_CONTENT.getName() );
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();
        saveScreenshot( "test_move_dialog_esc_before" );

        when: "the 'Esc' key pressed"
        dialog.closeByClickingOnEsc();
        saveScreenshot( "move_dialog_escaped" );

        then: "dialog should be closed"
        !dialog.isOpened()
    }
}
