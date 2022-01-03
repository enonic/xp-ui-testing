package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.ContentMenuItem
import spock.lang.Ignore

@Ignore
class ContentBrowsePanel_ContextMenuImage_Spec
    extends BaseContentSpec
{
    def "WNEN right click on existing 'New' image THEN all menu-items should have expected state"()
    {
        when: "context menu is opened"
        filterPanel.typeSearchText( IMPORTED_IMAGE_BOOK_NAME );
        contentBrowsePanel.openContextMenu( IMPORTED_IMAGE_BOOK_NAME );
        saveScreenshot( "image-context-menu" );

        then: "'Delete...' menu item should be enabled"
        contentBrowsePanel.isContextMenuItemEnabled( ContentMenuItem.ARCHIVE.getName() );
        and: "'Preview' menu item should be disabled"
        !contentBrowsePanel.isContextMenuItemEnabled( ContentMenuItem.PREVIEW.getName() );
        and: "'Edit' menu item should be enabled"
        contentBrowsePanel.isContextMenuItemEnabled( ContentMenuItem.EDIT.getName() );
        and: "'New...' menu item should be disabled"
        !contentBrowsePanel.isContextMenuItemEnabled( ContentMenuItem.NEW.getName() );
        and: "'Sort...' menu item should be disabled"
        !contentBrowsePanel.isContextMenuItemEnabled( ContentMenuItem.SORT.getName() );
        and: "'Duplicate...' menu item should be enabled"
        contentBrowsePanel.isContextMenuItemEnabled( ContentMenuItem.DUPLICATE.getName() );
        and: "'Move...' menu item should be enabled"
        contentBrowsePanel.isContextMenuItemEnabled( ContentMenuItem.MOVE.getName() );
        and: "'Publish...' menu item should be enabled"
        contentBrowsePanel.isContextMenuItemEnabled( ContentMenuItem.PUBLISH.getName() );

        and: "'Unpublish'-menu item should not be present in the context menu, because content is 'New'"
        !contentBrowsePanel.isContextMenuItemDisplayed( ContentMenuItem.UNPUBLISH.getName() );
    }
}
