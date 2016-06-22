package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.contentmanager.browsepanel.MoveContentDialog
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
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


    def "adding of the first folder"()
    {
        when: "new content added in root"
        FIRST_CONTENT = buildFolderContent( "movetest", FIRST_DISPLAY_CONTENT_NAME );
        addContent( FIRST_CONTENT );

        then: "content listed on the root"
        filterPanel.typeSearchText( FIRST_CONTENT.getName() );
        contentBrowsePanel.exists( FIRST_CONTENT.getName() );
    }

    def "adding of the second folder"()
    {
        when: "second content added"
        SECOND_CONTENT = buildFolderContent( "movetest", SECOND_DISPLAY_CONTENT_NAME );
        addContent( SECOND_CONTENT );

        then: "content listed on the root"
        filterPanel.typeSearchText( SECOND_CONTENT.getName() );
        contentBrowsePanel.exists( SECOND_CONTENT.getName() );
    }

    def "GIVEN selected folder WHEN 'Move' button on toolbar pressed THEN modal dialog with correct title appears"()
    {
        given: "one content selected"
        findAndSelectContent( FIRST_CONTENT.getName() );

        when: "button 'Move' pressed"
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();

        then: "modal dialog with correct title appears"
        dialog.isOpened();
        and:
        dialog.getTitle() == MoveContentDialog.DIALOG_TITLE;
    }

    def "GIVEN selected content and 'Move' button on toolbar pressed WHEN content moved to another location  THEN content listed beneath the content that was destination for moving"()
    {
        given:
        findAndSelectContent( FIRST_CONTENT.getName() )
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();

        when: "content moved to another location"
        dialog.typeSearchText( SECOND_CONTENT.getName() ).doMove( SECOND_CONTENT.getName() );

        then: "parent content expanded"
        filterPanel.typeSearchText( SECOND_CONTENT.getName() );
        contentBrowsePanel.expandContent( SECOND_CONTENT.getPath() );

        and: "content listed beneath the content that was destination for moving"
        contentBrowsePanel.exists( FIRST_CONTENT.getName() );
    }

    def "GIVEN 'move' dialog opened WHEN 'close' button clicked  THEN modal dialog disappears"()
    {
        given: "'move' dialog opened"
        findAndSelectContent( FIRST_CONTENT.getName() );
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();

        when: "the 'cancel' button on the bottom of dialog pressed"
        dialog.clickOnCancelBottomButton();

        then: "dialog not present"
        !dialog.isOpened()
    }

    def "GIVEN 'move' dialog opened WHEN 'cancel' button clicked  THEN modal dialog disappears"()
    {
        given:
        findAndSelectContent( FIRST_CONTENT.getName() );
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();

        when: "the 'cancel' button on the top of dialog pressed"
        dialog.clickOnCancelTopButton();

        then: "dialog not present"
        !dialog.isOpened()
    }

    def "GIVEN 'move' dialog opened WHEN 'Esc' button clicked  THEN modal dialog disappears"()
    {
        given:
        findAndSelectContent( FIRST_CONTENT.getName() );
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();
        TestUtils.saveScreenshot( getSession(), "test_move_dialog_esc_before" );

        when: "the 'Esc' key pressed"
        dialog.closeByClickingOnEsc();
        TestUtils.saveScreenshot( getSession(), "test_move_dialog_esc_after" );

        then: "dialog not present"
        !dialog.isOpened()
    }
}
