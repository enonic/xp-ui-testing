package com.enonic.wem.uitest.content

class ContentBrowsePanel_ContextMenuImage_Spec
    extends BaseContentSpec
{
    def "GIVEN an offline image WHEN context menu opened  THEN all menu-items have correct state"()
    {
        when: "context menu opened"
        filterPanel.typeSearchText( IMPORTED_IMAGE_BOOK_NAME );
        contentBrowsePanel.openContextMenu( IMPORTED_IMAGE_BOOK_NAME );
        saveScreenshot( "image-context-menu" );

        then: "Delete menu item is enabled"
        contentBrowsePanel.isContextMenuItemEnabled( "Delete" );
        and: "Preview menu item is disabled"
        !contentBrowsePanel.isContextMenuItemEnabled( "Preview" );
        and: "Edit menu item is enabled"
        contentBrowsePanel.isContextMenuItemEnabled( "Edit" );
        and: "New menu item is disabled"
        !contentBrowsePanel.isContextMenuItemEnabled( "New" );
        and: "Sort menu item is disabled"
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
