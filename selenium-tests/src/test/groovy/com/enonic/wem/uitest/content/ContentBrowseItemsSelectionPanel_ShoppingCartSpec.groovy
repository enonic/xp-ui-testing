package com.enonic.wem.uitest.content

import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Stepwise

import static com.enonic.autotests.utils.SleepHelper.sleep

@Stepwise
class ContentBrowseItemsSelectionPanel_ShoppingCartSpec
    extends BaseContentSpec
{
    private final PARENT_ROOT_FOLDER = "parent_content"

    private final ROOT_FOLDER_2 = "parent_content"

    private final UNSTRUCTURED_CHILD_CONTENT = "unstructured_content"

    private final SHOPPING_CART_BASE_NAME = "shoppingcart"


    def "GIVEN expanded parent content and content beneath the parent, both contents are selected  WHEN parent content is collapsed  THEN only one item is selected in the grid panel but two items present in selection panel"()
    {
        setup: "build a new folder-content and child content"
        String name = NameHelper.uniqueName( SHOPPING_CART_BASE_NAME );

        Content parentContent = buildFolderContent( name, "parentContent" );
        String childName = NameHelper.uniqueName( "child" );
        Content unstructuredChildContent = Content.builder().
            name( childName ).
            displayName( "childContent" ).
            contentType( ContentTypeName.unstructured() ).
            parent( ContentPath.from( parentContent.getName() ) ).
            build();

        and: "add new content: click on 'new' button, populate a wizard and close it"
        addContent( parentContent );
        contentBrowsePanel.waitsForSpinnerNotVisible();
        getTestSession().put( PARENT_ROOT_FOLDER, parentContent );

        and: "add new child content beneath the parent"
        contentBrowsePanel.selectContentInTable( parentContent.getPath() );
        addContent( unstructuredChildContent );
        getTestSession().put( UNSTRUCTURED_CHILD_CONTENT, unstructuredChildContent );
        contentBrowsePanel.waitsForSpinnerNotVisible();


        contentBrowsePanel.expandContent( parentContent.getPath() );
        List<Content> contents = new ArrayList<>();
        contents.add( parentContent );
        contents.add( unstructuredChildContent );
        contentBrowsePanel.selectContentInTable( contents );

        when: "un expand a parent content "
        contentBrowsePanel.unExpandContent( parentContent.getPath() );

        then: "if parent and child content are selected and parent content collapsed, item selection panel should contains two items, but only one row is selected in the grid "
        List<String> selectedNames = contentBrowsePanel.getItemSelectionPanel().getSelectedItemDisplayNames();
        selectedNames.contains( parentContent.getDisplayName() ) && selectedNames.contains( unstructuredChildContent.getDisplayName() ) &&
            selectedNames.size() == 2 && contentBrowsePanel.getSelectedRowsNumber() == 1;
    }

    def "GIVEN a selected content  WHEN search text typed and one more row with content clicked  THEN selection panel has no any items"()
    {
        setup: "select a root content and type search text in filter panel"
        Content parentContent = getTestSession().get( PARENT_ROOT_FOLDER );
        Content childContent = getTestSession().get( UNSTRUCTURED_CHILD_CONTENT );
        contentBrowsePanel.selectContentInTable( parentContent.getPath() );
        filterPanel.typeSearchText( childContent.getName() );

        when: "all contents filtered and one more row with content clicked "
        contentBrowsePanel.selectRowByContentPath( childContent.getPath().toString() );

        then: "no any items should not be present in the selection panel  "
        List<String> selectedNames = contentBrowsePanel.getItemSelectionPanel().getSelectedItemDisplayNames();
        selectedNames.size() == 0;
    }

    def "GIVEN a selected content AND search text typed WHEN one more checkbox near a content clicked THEN two items should be  present in selection panel"()
    {
        setup: "select a root content and type search text in filter panel"
        Content parentContent = getTestSession().get( PARENT_ROOT_FOLDER );
        Content childContent = getTestSession().get( UNSTRUCTURED_CHILD_CONTENT );
        contentBrowsePanel.selectContentInTable( parentContent.getPath() );
        filterPanel.typeSearchText( childContent.getName() );

        when: "search text typed and one more checkbox clicked and  content selected "
        contentBrowsePanel.clickCheckboxAndSelectRow( childContent.getPath() );

        then: "two item should be present in selection panel"
        List<String> selectedNames = contentBrowsePanel.getItemSelectionPanel().getSelectedItemDisplayNames();
        selectedNames.size() == 2;
    }

    def "GIVEN a selected content AND search text typed AND one more checkbox near a  content clicked WHEN 'Clear filter' clicked THEN two items should be  present in the selection panel"()
    {
        setup: "select a root content and type search text in filter panel"
        Content parentContent = getTestSession().get( PARENT_ROOT_FOLDER );
        Content childContent = getTestSession().get( UNSTRUCTURED_CHILD_CONTENT );
        contentBrowsePanel.selectContentInTable( parentContent.getPath() );
        filterPanel.typeSearchText( childContent.getName() );

        and: "search text typed and one more checkbox clicked and  content selected "
        contentBrowsePanel.clickCheckboxAndSelectRow( childContent.getPath() );

        when: "search text typed and one more checkbox clicked and  content selected "
        filterPanel.clickOnCleanFilter();

        then: "two item should be present in selection panel"
        List<String> selectedNames = contentBrowsePanel.getItemSelectionPanel().getSelectedItemDisplayNames();
        selectedNames.size() == 2;
    }

    def "GIVEN two folders in the root AND search text typed AND both folder selected WHEN filter cleared  THEN two contents still selected"()
    {
        setup: "select a root content and type search text in filter panel"
        Content folder1 = getTestSession().get( PARENT_ROOT_FOLDER );
        String name = NameHelper.uniqueName( SHOPPING_CART_BASE_NAME );
        Content folder2 = Content.builder().
            name( name ).
            displayName( "parentContent" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.ROOT ).
            build();

        and: "add new content: click on 'new' button, populate a wizard and close it"
        contentBrowsePanel.clickToolbarNew().selectContentType( folder2.getContentTypeName() ).typeData( folder2 ).save().close(
            folder2.getDisplayName() );
        contentBrowsePanel.waitsForSpinnerNotVisible();


        and: "type a part of name common for both contents "
        filterPanel.typeSearchText( SHOPPING_CART_BASE_NAME );
        List<Content> contents = new ArrayList<>();
        contents.add( folder1 );
        contents.add( folder2 );
        and: "click on checkbox and select both contents in the grid"
        contentBrowsePanel.selectContentInTable( contents );

        when: "click on 'Clear filter' link"
        filterPanel.clickOnCleanFilter();

        then: "still two items should be present in the selection panel "
        List<String> selectedNames = contentBrowsePanel.getItemSelectionPanel().getSelectedItemDisplayNames();
        selectedNames.size() == 2;
    }

    def "GIVEN a parent content and child beneath a parent AND parent content selected AND name of child typed in the search input AND checkbox selected near the child  WHEN filter cleared and parent content expanded  THEN two contents selected"()
    {
        setup: "select a existing root content, that has a child "
        Content parentFolder = getTestSession().get( PARENT_ROOT_FOLDER );
        Content childContent = getTestSession().get( UNSTRUCTURED_CHILD_CONTENT );
        contentBrowsePanel.selectContentInTable( parentFolder.getPath() )

        and: "type a name of child content in the filter panel"
        contentBrowsePanel.getFilterPanel().typeSearchText( childContent.getName() );

        and: "click on checkbox near the child content, when all content were filtered"
        contentBrowsePanel.clickCheckboxAndSelectRow( childContent.getPath() );

        when: "click on 'Clear filter' link and expand a parent content"
        filterPanel.clickOnCleanFilter();
        sleep( 2000 );
        contentBrowsePanel.expandContent( parentFolder.getPath() );

        then: "two items should be present in the selection panel and two rows are selected in the grid "
        List<String> selectedNames = contentBrowsePanel.getItemSelectionPanel().getSelectedItemDisplayNames();
        selectedNames.size() == 2 && contentBrowsePanel.getSelectedRowsNumber();
    }

    def "GIVEN a parent content and child beneath a parent AND parent content selected AND name of child typed in the search input AND row with the child was clicked  WHEN filter cleared and parent content expanded  THEN only one child content selected"()
    {
        setup: "select a existing root content, that has a child "
        Content parentFolder = getTestSession().get( PARENT_ROOT_FOLDER );
        Content child = getTestSession().get( UNSTRUCTURED_CHILD_CONTENT );
        contentBrowsePanel.selectContentInTable( parentFolder.getPath() )

        and: "and type a name of child content in the filter panel"
        filterPanel.typeSearchText( child.getName() );

        and: "click on row with the child content"
        contentBrowsePanel.selectRowByContentPath( child.getPath().toString() );

        when: "click on 'Clear filter' link and expand a parent content"
        filterPanel.clickOnCleanFilter();
        contentBrowsePanel.expandContent( parentFolder.getPath() );

        then: "selection panel has no any items and only one row is selected in the grid "
        List<String> selectedNames = contentBrowsePanel.getItemSelectionPanel().getSelectedItemDisplayNames();
        selectedNames.size() == 0 && contentBrowsePanel.getSelectedRowsNumber() == 1;
    }


    def "GIVEN browse panel opened  WHEN Select All clicked  THEN number of items in the selection panel and number of selected rows in the grid are equals "()
    {
        when: "filter cleared "
        contentBrowsePanel.clickOnSelectAll();
        TestUtils.saveScreenshot( getSession(), "select_all_clicked" )

        then: "number of items in the selection panel and number of of selected rows in the grid are equals"
        contentBrowsePanel.getItemSelectionPanel().getSelectedItemDisplayNames().size() == contentBrowsePanel.getSelectedRowsNumber();

    }

    def "GIVEN browse panel opened AND 'Select All' clicked  WHEN 'Clear Selection' clicked  THEN there are no any items in the selection panel"()
    {
        setup: "click on 'Select All' link"
        contentBrowsePanel.clickOnSelectAll();

        when: "'Clear Selection' link pressed "
        contentBrowsePanel.clickOnClearSelection();

        then: "there are no any items in the selection panel"
        contentBrowsePanel.getItemSelectionPanel().getSelectedItemDisplayNames().size() == 0;

    }


}
