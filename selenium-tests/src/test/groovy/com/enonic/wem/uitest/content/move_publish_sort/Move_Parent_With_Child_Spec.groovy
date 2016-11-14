package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.contentmanager.browsepanel.MoveContentDialog
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.content.ContentPath
import spock.lang.Stepwise

/**
 * Created  on 02.11.2016.
 *
 * Task: XP-4139 Add selenium tests for XP-4111
 * verifies: XP-4111 Incorrect filtering of options when parent and its child were selected for moving
 *
 * */
@Stepwise
class Move_Parent_With_Child_Spec
    extends BaseContentSpec
{
    def "GIVEN existing folder is selected AND 'Move' button pressed WHEN name of the folder is typed THEN 'No matching items' message appears"()
    {
        given: "existing folder is selected AND 'Move' button pressed"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();

        when: "name of the folder is typed"
        dialog.typeSearchText( IMPORTED_FOLDER_NAME )

        then: "'No matching items' message appears"
        saveScreenshot( "is_destination_present" );
        dialog.isNoMatchingItemsMessageDisplayed();

        and: "no matching contents were found"
        !dialog.isDestinationMatches( IMPORTED_FOLDER_NAME )
    }

    def "GIVEN parent folder and its child are selected AND 'Move' button pressed WHEN name of parent folder is typed THEN 'No matching items' message appears"()
    {
        given: "parent folder and its child are selected AND 'Move' button pressed"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        contentBrowsePanel.expandContent( ContentPath.from( IMPORTED_FOLDER_NAME ) );
        contentBrowsePanel.clickCheckboxAndSelectRow( IMPORTED_BOOK_IMAGE );
        MoveContentDialog dialog = contentBrowsePanel.clickToolbarMove();

        when: "name of parent folder is typed"
        dialog.typeSearchText( IMPORTED_FOLDER_NAME )

        then: "'No matching items' message appears"
        saveScreenshot( "is_destination_present" );
        dialog.isNoMatchingItemsMessageDisplayed();

        and: "no matching contents were found"
        !dialog.isDestinationMatches( IMPORTED_FOLDER_NAME )
    }

    def "GIVEN new created folder is selected AND 'Move' pressed WHEN match destination typed THEN filtered destination is displayed "()
    {
        given: "new created folder is selected is selected AND 'Move' pressed"
        Content folder = buildFolderContent( "move-test", "folder to move" );
        addContent( folder );
        MoveContentDialog dialog = findAndSelectContent( folder.getName() ).clickToolbarMove()

        when: "match destination typed"
        dialog.typeSearchText( IMPORTED_FOLDER_NAME );

        then: "filtered destination is displayed"
        dialog.isDestinationMatches( IMPORTED_FOLDER_NAME );
    }
}