package com.enonic.wem.uitest.content

import com.enonic.autotests.utils.TestUtils

class ContentBrowsePanel_ContextMenuImage_Spec
    extends BaseContentSpec
{
    def "GIVEN a folder WHEN context menu opened  THEN all items have correct state"()
    {
        when: "context menu opened"
        filterPanel.typeSearchText( IMPORTED_BOOK_IMAGE );
        contentBrowsePanel.openContextMenu( IMPORTED_BOOK_IMAGE );
        TestUtils.saveScreenshot( getSession(), "image-context-menu" );

        then: "Delete menu item is enabled"
        contentBrowsePanel.isEnabledContextMenuItem( "Delete" );
        and: "Preview menu item is disabled"
        !contentBrowsePanel.isEnabledContextMenuItem( "Preview" );
        and: "Edit menu item is enabled"
        contentBrowsePanel.isEnabledContextMenuItem( "Edit" );
        and: "New menu item is enabled"
        !contentBrowsePanel.isEnabledContextMenuItem( "New" );
        and: "Sort menu item is enabled"
        !contentBrowsePanel.isEnabledContextMenuItem( "Sort" );
        and: "New menu item is enabled"
        contentBrowsePanel.isEnabledContextMenuItem( "Duplicate" );
        and: "Move menu item is enabled"
        contentBrowsePanel.isEnabledContextMenuItem( "Move" );
        and: "Publish menu item is enabled"
        contentBrowsePanel.isEnabledContextMenuItem( "Publish" );
    }
}