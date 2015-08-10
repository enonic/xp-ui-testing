package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.contentmanager.browsepanel.MoveContentDialog
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.schema.content.ContentTypeName
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


    def "add first folder content"()
    {
        when: "content saved"
        FIRST_CONTENT = buildFolderContent( "movetest", FIRST_DISPLAY_CONTENT_NAME );
        contentBrowsePanel.clickToolbarNew().selectContentType( FIRST_CONTENT.getContentTypeName() ).typeData( FIRST_CONTENT ).save().close(
            FIRST_CONTENT.getDisplayName() );

        then: "content listed on the root"
        filterPanel.typeSearchText( FIRST_CONTENT.getName() );
        contentBrowsePanel.exists( FIRST_CONTENT.getName() );
    }

    def "add second folder content"()
    {
        when: "content saved"
        SECOND_CONTENT = buildFolderContent( "movetest", SECOND_DISPLAY_CONTENT_NAME );
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).typeData(
            SECOND_CONTENT ).save().close( SECOND_CONTENT.getDisplayName() );

        then: "content listed on the root"
        filterPanel.typeSearchText( SECOND_CONTENT.getName() );
        contentBrowsePanel.exists( SECOND_CONTENT.getName() );
    }

    def "GIVEN selected folder WHEN 'Move' button on toolbar pressed THEN modal dialog with correct title appears"()
    {
        given: "one content selected"
        filterPanel.typeSearchText( FIRST_CONTENT.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( FIRST_CONTENT.getName() );

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
        filterPanel.typeSearchText( FIRST_CONTENT.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( FIRST_CONTENT.getName() );
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
        filterPanel.typeSearchText( FIRST_CONTENT.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( FIRST_CONTENT.getName() );
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();

        when: "the 'cancel' button on the bottom of dialog pressed"
        dialog.clickOnCancelBottomButton();

        then: "dialog not present"
        !dialog.isOpened()

    }

    def "GIVEN 'move' dialog opened WHEN 'cancel' button clicked  THEN modal dialog disappears"()
    {
        given:
        filterPanel.typeSearchText( FIRST_CONTENT.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( FIRST_CONTENT.getName() );
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();

        when: "the 'cancel' button on the top of dialog pressed"
        dialog.clickOnCancelTopButton();

        then: "dialog not present"
        !dialog.isOpened()
    }


}
