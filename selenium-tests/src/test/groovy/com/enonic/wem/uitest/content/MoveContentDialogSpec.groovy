package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.MoveContentDialog
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class MoveContentDialogSpec
    extends BaseContentSpec
{


    @Shared
    String FIRST_CONTENT_NAME = "first";

    @Shared
    String SECOND_CONTENT_NAME = "second";

    @Shared
    Content FIRST_CONTENT;

    @Shared
    Content SECOND_CONTENT;


    def "add first folder content"()
    {
        when:
        FIRST_CONTENT = buildFolderContent( FIRST_CONTENT_NAME );
        contentBrowsePanel.clickToolbarNew().selectContentType( FIRST_CONTENT.getContentTypeName() ).typeData( FIRST_CONTENT ).save().close(
            FIRST_CONTENT.getDisplayName() );
        then:
        contentBrowsePanel.exists( FIRST_CONTENT.getPath() );
    }

    def "add second folder content"()
    {
        when:
        SECOND_CONTENT = buildFolderContent( SECOND_CONTENT_NAME );
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).typeData(
            SECOND_CONTENT ).save().close( SECOND_CONTENT.getDisplayName() );
        then:
        contentBrowsePanel.exists( SECOND_CONTENT.getPath() );
    }

    def "GIVEN selected folder WHEN 'Move' button on toolbar pressed THEN modal dialog with correct title appears"()
    {
        given:
        filterPanel.typeSearchText( FIRST_CONTENT.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( FIRST_CONTENT.getPath() );
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
        contentBrowsePanel.clickCheckboxAndSelectRow( FIRST_CONTENT.getPath() );
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();

        when:
        dialog.typeSearchText( SECOND_CONTENT.getName() ).doMove( SECOND_CONTENT.getName() );
        then:
        filterPanel.typeSearchText( SECOND_CONTENT.getName() );
        contentBrowsePanel.expandContent( SECOND_CONTENT.getPath() );

        and:
        contentBrowsePanel.exists( FIRST_CONTENT.getPath() );
    }

    def "GIVEN 'move' dialog opened WHEN 'close' button clicked  THEN modal dialog disappears"()
    {
        given:
        filterPanel.typeSearchText( FIRST_CONTENT.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( FIRST_CONTENT.getPath() );
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
        contentBrowsePanel.clickCheckboxAndSelectRow( FIRST_CONTENT.getPath() );
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();

        when:
        dialog.clickOnCancelTopButton();
        then:
        !dialog.isOpened()

    }

    private Content buildFolderContent( String displayName )
    {
        String name = NameHelper.uniqueName( "movetest" );
        Content content = Content.builder().
            name( name ).
            displayName( displayName ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.ROOT ).
            build();
        return content;
    }
}
