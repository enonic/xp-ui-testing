package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.contentmanager.browsepanel.MoveContentDialog
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.content.ContentPath
import spock.lang.Stepwise

/**
 * Created  on 02.11.2016.
 * verifies: XP-4111 Incorrect filtering of options when parent and its child were selected for moving
 * */
@Stepwise
class Move_Parent_With_Child_Spec
    extends BaseContentSpec
{
    def "GIVEN existing folder is selected AND 'Move' button pressed WHEN own name of the folder has been typed THEN 'No matching items' message appears"()
    {
        given: "existing folder is selected AND 'Move' button pressed"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();

        when: "own name of the folder has been typed"
        dialog.typeSearchText( IMPORTED_FOLDER_NAME );
        sleep( 500 );

        then: "the option should be disabled"
        saveScreenshot( "destination_should_be_disabled1" );
        dialog.isDestinationDisabled( IMPORTED_FOLDER_NAME )
    }

    def "GIVEN parent folder and its child are selected AND 'Move' button pressed WHEN name of parent folder is typed THEN 'No matching items' message appears"()
    {
        given: "parent folder and its child are selected AND 'Move' button pressed"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        contentBrowsePanel.expandContent( ContentPath.from( IMPORTED_FOLDER_NAME ) );
        contentBrowsePanel.clickCheckboxAndSelectRow( IMPORTED_IMAGE_BOOK_NAME );
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();

        when: "name of the parent folder has been typed"
        dialog.typeSearchText( IMPORTED_FOLDER_NAME )

        then: "the filtered option should be disabled"
        saveScreenshot( "destination_should_be_disabled2" );
        dialog.isDestinationDisabled( IMPORTED_FOLDER_NAME )
    }

    def "GIVEN new created folder is selected AND 'Move' pressed WHEN matched destination has been typed THEN filtered destination should be displayed"()
    {
        given: "new created folder is selected is selected AND 'Move' pressed"
        Content folder = buildFolderContent( "move-test", "folder to move" );
        addContent( folder );
        MoveContentDialog dialog = findAndSelectContent( folder.getName() ).clickToolbarMove()

        when: "matched destination has been typed"
        dialog.typeSearchText( IMPORTED_FOLDER_NAME );
        saveScreenshot( "destination_should_be_enabled" );

        then: "filtered destination should be enabled"
        !dialog.isDestinationDisabled( IMPORTED_FOLDER_NAME );
    }
}
