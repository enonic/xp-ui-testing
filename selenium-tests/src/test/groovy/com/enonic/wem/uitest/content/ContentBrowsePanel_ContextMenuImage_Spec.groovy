package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.ContentMenuItem

class ContentBrowsePanel_ContextMenuImage_Spec
    extends BaseContentSpec
{
    def "GIVEN existing offline image WHEN context menu for the image has been opened THEN all menu-items should have correct state"()
    {
        when: "context menu is opened"
        filterPanel.typeSearchText( IMPORTED_IMAGE_BOOK_NAME );
        contentBrowsePanel.openContextMenu( IMPORTED_IMAGE_BOOK_NAME );
        saveScreenshot( "image-context-menu" );

        then: "Delete menu item should be enabled"
        contentBrowsePanel.isContextMenuItemEnabled( ContentMenuItem.DELETE.getName() );
        and: "Preview menu item should be disabled"
        !contentBrowsePanel.isContextMenuItemEnabled( ContentMenuItem.PREVIEW.getName() );
        and: "Edit menu item should be enabled"
        contentBrowsePanel.isContextMenuItemEnabled( ContentMenuItem.EDIT.getName() );
        and: "New menu item should be disabled"
        !contentBrowsePanel.isContextMenuItemEnabled( ContentMenuItem.NEW.getName() );
        and: "Sort menu item should be disabled"
        !contentBrowsePanel.isContextMenuItemEnabled( ContentMenuItem.SORT.getName() );
        and: "Duplicate menu item should be enabled"
        contentBrowsePanel.isContextMenuItemEnabled( ContentMenuItem.DUPLICATE.getName() );
        and: "Move menu item should be enabled"
        contentBrowsePanel.isContextMenuItemEnabled( ContentMenuItem.MOVE.getName() );
        and: "Publish menu item is enabled"
        contentBrowsePanel.isContextMenuItemEnabled( ContentMenuItem.PUBLISH.getName() );

        and: "'unpublish'-item not present in the context menu, because content is 'offline'"
        !contentBrowsePanel.isContextMenuItemDisplayed( ContentMenuItem.UNPUBLISH.getName() );
    }
}
