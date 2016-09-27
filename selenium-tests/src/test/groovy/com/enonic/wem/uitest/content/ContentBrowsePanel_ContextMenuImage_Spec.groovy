package com.enonic.wem.uitest.content

import com.enonic.autotests.utils.TestUtils

class ContentBrowsePanel_ContextMenuImage_Spec
    extends BaseContentSpec
{
    def "GIVEN an offline folder WHEN context menu opened  THEN all items have correct state"()
    {
        when: "context menu opened"
        filterPanel.typeSearchText( IMPORTED_BOOK_IMAGE );
        contentBrowsePanel.openContextMenu( IMPORTED_BOOK_IMAGE );
        TestUtils.saveScreenshot( getSession(), "image-context-menu" );

        then: "Delete menu item is enabled"
        contentBrowsePanel.isContextMenuItemEnabled( "Delete" );
        and: "Preview menu item is disabled"
        !contentBrowsePanel.isContextMenuItemEnabled( "Preview" );
        and: "Edit menu item is enabled"
        contentBrowsePanel.isContextMenuItemEnabled( "Edit" );
        and: "New menu item is enabled"
        !contentBrowsePanel.isContextMenuItemEnabled( "New" );
        and: "Sort menu item is enabled"
        !contentBrowsePanel.isContextMenuItemEnabled( "Sort" );
        and: "Duplicate menu item is enabled"
        contentBrowsePanel.isContextMenuItemEnabled( "Duplicate" );
        and: "Move menu item is enabled"
        contentBrowsePanel.isContextMenuItemEnabled( "Move" );
        and: "Publish menu item is enabled"
        contentBrowsePanel.isContextMenuItemEnabled( "Publish" );

        and: "'unpublish'-item not present in the context menu, because content is 'offline'"
        !contentBrowsePanel.isContextMenuItemDisplayed( "Unpublish" );
    }
}
