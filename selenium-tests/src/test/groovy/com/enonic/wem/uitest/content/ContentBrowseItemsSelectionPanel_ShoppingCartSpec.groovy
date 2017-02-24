package com.enonic.wem.uitest.content

import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import spock.lang.Stepwise

@Stepwise
class ContentBrowseItemsSelectionPanel_ShoppingCartSpec
    extends BaseContentSpec
{
    private final FOLDER_WITH_IMAGES = "all-content-types-images"

    private final CHILD_CONTENT_NAME = "man.jpg"


    def "GIVEN expanded parent content and content beneath the parent, both contents are selected  WHEN parent content is collapsed  THEN only one item is selected in the grid panel but two items present in selection panel"()
    {
        given:
        contentBrowsePanel.expandContent( ContentPath.from( FOLDER_WITH_IMAGES ) );
        List<String> contentNames = new ArrayList<>();
        contentNames.add( CHILD_CONTENT_NAME );
        contentNames.add( FOLDER_WITH_IMAGES );
        contentBrowsePanel.selectContentInTable( contentNames );

        when: "un expand a parent content "
        contentBrowsePanel.unExpandContent( ContentPath.from( FOLDER_WITH_IMAGES ) );

        then: "if parent and child content are selected and parent content collapsed, item selection panel should contains two items"
        List<String> selectedNames = itemsSelectionPanel.getSelectedItemNames();
        selectedNames.contains( "/" + FOLDER_WITH_IMAGES + "/" + CHILD_CONTENT_NAME ) && selectedNames.contains( "/" + FOLDER_WITH_IMAGES )
        and:
        selectedNames.size() == 2
        and: "but only one row is selected in the grid "
        contentBrowsePanel.getSelectedRowsNumber() == 1;
    }

    def "GIVEN a selected content WHEN search text typed and one more row with content clicked  THEN selection panel has no any items"()
    {
        setup: "select a root content and type search text in filter panel"
        contentBrowsePanel.selectContentInTable( FOLDER_WITH_IMAGES );
        filterPanel.typeSearchText( CHILD_CONTENT_NAME );

        when: "all contents filtered and one more row with content clicked "
        contentBrowsePanel.clickOnRowByName( CHILD_CONTENT_NAME );

        then: "no any items should not be present in the selection panel  "
        List<String> selectedNames = itemsSelectionPanel.getDisplayNameOfSelectedItems();
        selectedNames.size() == 0;
    }

    def "GIVEN a selected content AND search text typed WHEN one more checkbox near a content clicked THEN two items should be  present in selection panel"()
    {
        setup: "select a root content and type search text in filter panel"
        contentBrowsePanel.selectContentInTable( FOLDER_WITH_IMAGES );
        filterPanel.typeSearchText( CHILD_CONTENT_NAME );

        when: "search text typed and one more checkbox clicked and  content selected "
        contentBrowsePanel.clickCheckboxAndSelectRow( CHILD_CONTENT_NAME );

        then: "two items should be present in selection panel"
        List<String> selectedNames = itemsSelectionPanel.getDisplayNameOfSelectedItems();
        selectedNames.size() == 2;
    }

    def "GIVEN a selected content AND search text typed AND one more checkbox near a  content clicked WHEN 'Clear filter' clicked THEN two items should be  present in the selection panel"()
    {
        setup: "select a root content and type search text in filter panel"
        contentBrowsePanel.selectContentInTable( FOLDER_WITH_IMAGES );
        filterPanel.typeSearchText( CHILD_CONTENT_NAME );

        and: "search text typed and one more checkbox clicked and  content selected "
        contentBrowsePanel.clickCheckboxAndSelectRow( CHILD_CONTENT_NAME );

        when: "search text typed and one more checkbox clicked and  content selected "
        filterPanel.clickOnCleanFilter();

        then: "two items should be present in selection panel"
        List<String> selectedNames = itemsSelectionPanel.getDisplayNameOfSelectedItems();
        selectedNames.size() == 2;
    }

    def "GIVEN two folders in the root AND search text typed AND both folder selected WHEN filter cleared  THEN two contents still selected"()
    {
        setup: "select a root content and type search text in filter panel"
        Content folder = buildFolderContent( "folder", "shopingcart" )
        and: "add new content: click on 'new' button, populate a wizard and close it"
        addContent( folder );

        and: "type a search text and select the first folder "
        findAndSelectContent( FOLDER_WITH_IMAGES );

        and: "type a search text and select the second folder "
        findAndSelectContent( folder.getName() );

        when: "click on 'Clear filter' link"
        filterPanel.clickOnCleanFilter();

        then: "still two items should be present in the selection panel "
        List<String> selectedNames = itemsSelectionPanel.getDisplayNameOfSelectedItems();
        selectedNames.size() == 2;
    }

    def "GIVEN a parent content and child beneath a parent AND parent content selected AND name of child typed in the search input AND checkbox selected near the child  WHEN filter cleared and parent content expanded  THEN two contents selected"()
    {
        setup: "select a existing root content, that has a child "
        contentBrowsePanel.selectContentInTable( FOLDER_WITH_IMAGES )

        and: "type a name of child content in the filter panel"
        filterPanel.typeSearchText( CHILD_CONTENT_NAME );

        and: "click on checkbox near the child content, when all content were filtered"
        contentBrowsePanel.clickCheckboxAndSelectRow( CHILD_CONTENT_NAME );

        when: "click on 'Clear filter' link and expand a parent content"
        filterPanel.clickOnCleanFilter();
        contentBrowsePanel.expandContent( ContentPath.from( FOLDER_WITH_IMAGES ) );
        sleep( 1000 );
        TestUtils.saveScreenshot( getSession(), "shopping_cart_issue" );

        then: "two items should be present in the selection panel and two rows are selected in the grid "
        List<String> selectedNames = itemsSelectionPanel.getDisplayNameOfSelectedItems();
        selectedNames.size() == 2
        and:
        contentBrowsePanel.getSelectedRowsNumber() == 2;
    }

    def "GIVEN a parent content and child beneath a parent AND parent content selected AND name of child typed in the search input AND row with the child was clicked  WHEN filter cleared and parent content expanded  THEN only one child content selected"()
    {
        setup: "select a existing root content, that has a child "
        contentBrowsePanel.selectContentInTable( FOLDER_WITH_IMAGES )

        and: "and type a name of child content in the filter panel"
        filterPanel.typeSearchText( CHILD_CONTENT_NAME );

        and: "click on row with the child content"
        contentBrowsePanel.clickOnRowByName( CHILD_CONTENT_NAME );

        when: "click on 'Clear filter' link and expand a parent content"
        filterPanel.clickOnCleanFilter();
        contentBrowsePanel.expandContent( ContentPath.from( FOLDER_WITH_IMAGES ) );

        then: "selection panel has no any items and only one row is selected in the grid "
        List<String> selectedNames = itemsSelectionPanel.getDisplayNameOfSelectedItems();
        selectedNames.size() == 0 && contentBrowsePanel.getSelectedRowsNumber() == 1;
    }

    def "GIVEN browse panel opened AND  checkbox 'Select All' was clicked  WHEN 'Selection Controller' checkbox is clicked one more THEN no any items should be displayed on the selection panel"()
    {
        setup: "checkbox 'Select All' was clicked"
        contentBrowsePanel.doSelectAll();

        when: "'Selection Controller' checkbox is clicked"
        contentBrowsePanel.clickOnSelectionController();

        then: "no any items should be displayed on the selection panel"
        itemsSelectionPanel.getDisplayNameOfSelectedItems().size() == 0;
    }
}
