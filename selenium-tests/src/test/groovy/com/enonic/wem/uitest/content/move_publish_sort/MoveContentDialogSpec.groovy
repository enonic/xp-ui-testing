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
        when:
        FIRST_CONTENT = buildFolderContent( "movetest", FIRST_DISPLAY_CONTENT_NAME );
        contentBrowsePanel.clickToolbarNew().selectContentType( FIRST_CONTENT.getContentTypeName() ).typeData( FIRST_CONTENT ).save().close(
            FIRST_CONTENT.getDisplayName() );
        then:
        contentBrowsePanel.exists( FIRST_CONTENT.getName() );
    }

    def "add second folder content"()
    {
        when:
        SECOND_CONTENT = buildFolderContent( "movetest", SECOND_DISPLAY_CONTENT_NAME );
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).typeData(
            SECOND_CONTENT ).save().close( SECOND_CONTENT.getDisplayName() );
        then:
        contentBrowsePanel.exists( SECOND_CONTENT.getName() );
    }

    def "GIVEN selected folder WHEN 'Move' button on toolbar pressed THEN modal dialog with correct title appears"()
    {
        given:
        filterPanel.typeSearchText( FIRST_CONTENT.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( FIRST_CONTENT.getName() );
        when:
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();
        then:
        dialog.isOpened();
        and:
        dialog.getTitle() == MoveContentDialog.DIALOG_TITLE;
    }

    def "GIVEN selected content and 'Move' button on toolbar pressed WHEN content moved to the parent  THEN content listed beneath the parent"()
    {
        given:
        filterPanel.typeSearchText( FIRST_CONTENT.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( FIRST_CONTENT.getName() );
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();

        when:
        dialog.typeSearchText( SECOND_CONTENT.getName() ).doMove( SECOND_CONTENT.getName() );
        then:
        filterPanel.typeSearchText( SECOND_CONTENT.getName() );
        contentBrowsePanel.expandContent( SECOND_CONTENT.getPath() );

        and:
        contentBrowsePanel.exists( FIRST_CONTENT.getName() );
    }

    def "GIVEN 'move' dialog opened WHEN 'close' button clicked  THEN modal dialog disappears"()
    {
        given:
        filterPanel.typeSearchText( FIRST_CONTENT.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( FIRST_CONTENT.getName() );
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();

        when:
        dialog.clickOnCancelBottomButton();
        then:
        !dialog.isOpened()

    }

    def "GIVEN 'move' dialog opened WHEN 'cancel' button clicked  THEN modal dialog disappears"()
    {
        given:
        filterPanel.typeSearchText( FIRST_CONTENT.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( FIRST_CONTENT.getName() );
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();

        when:
        dialog.clickOnCancelTopButton();
        then:
        !dialog.isOpened()

    }


}
