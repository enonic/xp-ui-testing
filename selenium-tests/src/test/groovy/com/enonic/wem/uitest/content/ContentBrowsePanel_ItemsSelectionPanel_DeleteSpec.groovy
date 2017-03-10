package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_ItemsSelectionPanel_DeleteSpec
    extends BaseContentSpec
{
    @Shared
    Content parentFolder

    @Shared
    Content shortcut1

    @Shared
    Content shortcut2

    @Shared
    Content shortcut3

    def "setup: add test content"()
    {
        when: "content added"
        parentFolder = buildFolderContent( "folder", "selection test" )
        addContent( parentFolder );

        shortcut1 = Content.builder().
            name( NameHelper.uniqueName( "first" ) ).
            displayName( "first" ).
            parent( ContentPath.from( parentFolder.getName() ) ).
            contentType( ContentTypeName.shortcut() ).
            build();

        ContentWizardPanel wizard = contentBrowsePanel.clickOnParentCheckbox( parentFolder.getPath() ).clickToolbarNew().selectContentType(
            shortcut1.getContentTypeName() );
        wizard.typeData( shortcut1 ).save().closeBrowserTab().switchToBrowsePanelTab();

        shortcut2 = Content.builder().
            parent( ContentPath.from( parentFolder.getName() ) ).
            name( NameHelper.uniqueName( "second" ) ).
            displayName( "second" ).
            contentType( ContentTypeName.shortcut() ).
            build();

        addContent( shortcut2 );

        shortcut3 = Content.builder().
            parent( ContentPath.from( parentFolder.getName() ) ).
            name( NameHelper.uniqueName( "third" ) ).
            displayName( "third" ).
            contentType( ContentTypeName.shortcut() ).
            build();

        contentBrowsePanel.clickToolbarNew().selectContentType( shortcut3.getContentTypeName() );
        wizard.typeData( shortcut3 ).save().closeBrowserTab().switchToBrowsePanelTab();

        then:
        filterPanel.typeSearchText( parentFolder.getName() );
        contentBrowsePanel.exists( parentFolder.getName() )
    }

    def "GIVEN four selected content WHEN one 'selection item' was removed THEN two selected items are present in a browse panel"()
    {
        given: "four contents selected"
        List<String> contentList = new ArrayList<>();
        Collections.addAll( contentList, parentFolder.getName(), shortcut1.getName(), shortcut2.getName(), shortcut3.getName() );
        filterPanel.typeSearchText( parentFolder.getName() );
        contentBrowsePanel.expandContent( shortcut1.getParent() );
        saveScreenshot( "three-items-selected" );
        contentBrowsePanel.selectContentInTable( contentList );
        int beforeRemove = itemsSelectionPanel.getSelectedItemCount();
        int selectedRowsBefore = contentBrowsePanel.getSelectedRowsNumber();

        when: "one selection item was removed"
        itemsSelectionPanel.removeItem( shortcut1.getName() );
        saveScreenshot( "one-selected-item-removed" );

        then: "number of selected items in Selection Panel was reduced"
        beforeRemove - itemsSelectionPanel.getSelectedItemCount() == 1;

        and: "number of selected rows reduced as well"
        selectedRowsBefore - contentBrowsePanel.getSelectedRowsNumber() == 1;

    }

    def "GIVEN three selected Content WHEN 'Clear All' clicked THEN no SelectionItem-s are displayed"()
    {
        given: "parent and children are selected"
        List<String> contentList = new ArrayList<>();
        Collections.addAll( contentList, parentFolder.getName(), shortcut1.getName(), shortcut2.getName(),
                            shortcut3.getName() );
        filterPanel.typeSearchText( parentFolder.getName() );
        contentBrowsePanel.expandContent( shortcut1.getParent() );

        contentBrowsePanel.selectContentInTable( contentList );
        saveScreenshot( "tree_item_selected_in_grid" );

        when: "'Selection Controller' has been clicked twice"
        contentBrowsePanel.doClearSelection();

        then: "no SelectionItem-s are displayed"
        saveScreenshot( "clear_selections_done" )
        itemsSelectionPanel.getSelectedItemCount() == 0;
    }

    def "GIVEN three selected Content WHEN all deleted THEN no SelectionItem-s are displayed"()
    {
        given: "parent and children are selected"
        List<String> contentList = new ArrayList<>();
        Collections.addAll( contentList, parentFolder.getName(), shortcut1.getName(), shortcut2.getName(),
                            shortcut3.getName() );
        filterPanel.typeSearchText( parentFolder.getName() )
        contentBrowsePanel.expandContent( shortcut1.getParent() );
        saveScreenshot( "item_selection_4" );
        contentBrowsePanel.selectContentInTable( contentList )

        when: "'Delete button' pressed"
        contentBrowsePanel.clickToolbarDelete().doDelete();

        then: "no SelectionItem-s are displayed"
        saveScreenshot( "item_selection_0" );
        itemsSelectionPanel.getSelectedItemCount() == 0;
    }
}
