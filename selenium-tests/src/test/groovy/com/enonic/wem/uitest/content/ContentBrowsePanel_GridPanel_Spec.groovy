package com.enonic.wem.uitest.content

import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.schema.content.ContentTypeName
import org.openqa.selenium.Keys
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_GridPanel_Spec
    extends BaseContentSpec
{

    @Shared
    Content PARENT_CONTENT;

    @Shared
    Content CHILD_CONTENT;

    def "GIVEN some content are listed in root WHEN no selection THEN all rows should be white"()
    {
        given:
        int rowNumber = contentBrowsePanel.getRowsCount();

        expect:
        contentBrowsePanel.getSelectedRowsNumber() == 0 && rowNumber > 0;

        and: "refresh button should be present on the tree grid toolbar"
        contentBrowsePanel.isRefreshButtonDisplayed();
    }

    def "GIVEN one content is selected in the root WHEN refresh button was pressed THEN the row stays should be selected"()
    {
        given:
        List<String> contentNames = contentBrowsePanel.getContentNamesFromGrid();
        contentBrowsePanel.clickCheckboxAndSelectRow( contentNames.get( 0 ) );

        when:
        contentBrowsePanel.clickOnRefreshButton();
        saveScreenshot( "test_refresh_button_clicked" );

        then:
        contentBrowsePanel.getSelectedRowsNumber() == 1;
    }

    def "GIVEN existing contents in the root WHEN checkbox for the first content is checked THEN first row should be blue"()
    {
        given:
        List<String> contentNames = contentBrowsePanel.getContentNamesFromGrid();

        when:
        contentBrowsePanel.clickCheckboxAndSelectRow( contentNames.get( 0 ) );
        saveScreenshot( "test_first_row_selected" );

        then:
        contentBrowsePanel.getSelectedRowsNumber() == 1;
    }

    def "GIVEN existing Content is selected WHEN spacebar was pressed THEN row is no longer selected"()
    {
        given:"existing Content is selected"
        List<String> contentNames = contentBrowsePanel.getContentNamesFromGrid();
        contentBrowsePanel.clickCheckboxAndSelectRow( contentNames.get( 0 ) );
        saveScreenshot( "test_spacebar_before" );

        when:"spacebar was pressed"
        contentBrowsePanel.pressKeyOnRow( ContentPath.from( contentNames.get( 0 ) ), Keys.SPACE );

        then:"row is no longer selected"
        saveScreenshot( "test_spacebar_after" );
        contentBrowsePanel.getSelectedRowsNumber() == 0;
    }

    def "GIVEN existing content is selected WHEN 'Selection Controller' checkbox was clicked twice THEN row is no longer selected"()
    {
        given:"existing content is selected"
        List<String> contentNames = contentBrowsePanel.getContentNamesFromGrid();
        contentBrowsePanel.clickCheckboxAndSelectRow( contentNames.get( 0 ) );

        when:"'Selection Controller' checkbox was clicked twice"
        contentBrowsePanel.doClearSelection();

        then:"row is no longer selected"
        contentBrowsePanel.getSelectedRowsNumber() == 0;
    }

    def "WHEN 'Selection Controller' has been checked THEN all rows should be selected"()
    {
        when: "'Selection Controller ' has been checked"
        contentBrowsePanel.doSelectAll();
        saveScreenshot( "test_select_all_content" );

        then: "all rows should be selected"
        contentBrowsePanel.getRowsCount() == contentBrowsePanel.getSelectedRowsNumber();
    }

    def "GIVEN existing parent folder with a child WHEN the folder is selcted THEN 'expand icon' should be displayed for this folder"()
    {
        given: "existing folder"
        PARENT_CONTENT = buildFolderContent( "parentfolder", "folder-test" );
        CHILD_CONTENT = buildFolderContentWithParent( "child-content", "child folder", PARENT_CONTENT.getName() );
        addContent( PARENT_CONTENT );
        findAndSelectContent( PARENT_CONTENT.getName() );

        when: "child folder has been added"
        addContent( CHILD_CONTENT );

        then: "'expand icon' should be displayed for the parent folder"
        contentBrowsePanel.isExpanderPresent( PARENT_CONTENT.getName() )
    }

    def "GIVEN existing parent folder with a child WHEN 'expand icon' for the content has been clicked THEN child content should be listed"()
    {
        given: "existing parent folder with a child is selected"
        findAndSelectContent( PARENT_CONTENT.getName() )

        when: "'expand icon' for the content has been clicked"
        contentBrowsePanel.expandContent( PARENT_CONTENT.getPath() );

        then: "child content should be listed"
        contentBrowsePanel.exists( CHILD_CONTENT.getName() );
    }

    def "GIVEN existing content without children WHEN listed THEN 'expand icon' should not be displayed for this content"()
    {
        given: "existing content without children"
        String name = NameHelper.uniqueName( "unstructured" );
        Content unstructured = Content.builder().
            name( name ).
            displayName( "unstructured" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.unstructured() ).
            build();
        addContent( unstructured );

        expect: "'expand icon' should not be displayed for this content"
        !contentBrowsePanel.isExpanderPresent( ContentPath.from( name ) );
    }

    def "GIVEN existing collapsed folder WHEN 'expand icon' has been clicked THEN child content should be displayed"()
    {
        expect: "existing collapsed folder"
        !contentBrowsePanel.isRowExpanded( PARENT_CONTENT.getName() );

        when: "'expand icon' has been clicked"
        filterPanel.typeSearchText( PARENT_CONTENT.getName() );
        contentBrowsePanel.expandContent( PARENT_CONTENT.getPath() );
        List<String> childNames = contentBrowsePanel.getChildNames()

        then: "child content should be displayed"
        childNames.size() == 1;
        and:
        childNames.get( 0 ) == CHILD_CONTENT.getName();
    }

    def "GIVEN existing unexpanded folder WHEN 'expand icon' has been clicked THEN the folder should be collapsed"()
    {
        given: "existing unexpanded folder"
        contentBrowsePanel.expandContent( PARENT_CONTENT.getPath() );

        when:
        contentBrowsePanel.unExpandContent( PARENT_CONTENT.getPath() );
        saveScreenshot( "test_unexpand_folder" );

        then: "the folder should be collapsed"
        contentBrowsePanel.getChildNames().size() == 0;
    }

    def "GIVEN existing content is selected WHEN arrow down has been pressed THEN next row should be selected"()
    {
        given:
        contentBrowsePanel.selectContentInTable( PARENT_CONTENT.getName() );
        int before = contentBrowsePanel.getSelectedRowsNumber();
        saveScreenshot( "test_arrow_down_before" );

        when: "arrow down has been pressed"
        contentBrowsePanel.pressKeyOnRow( PARENT_CONTENT.getPath(), Keys.ARROW_DOWN );
        saveScreenshot( "test_arrow_down_after" );
        then: " next row should be selected"
        !contentBrowsePanel.isRowSelected( PARENT_CONTENT.getName() ) && contentBrowsePanel.getSelectedRowsNumber() == before;
    }

    def "GIVEN existing content is selected WHEN arrow up has been pressed THEN previous row should be selected"()
    {
        given:
        String name = NameHelper.uniqueName( "unstructured" );
        Content content = Content.builder().
            name( name ).
            displayName( "unstructured_arrow" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.unstructured() ).
            build();
        addContent( content );
        contentBrowsePanel.selectContentInTable( PARENT_CONTENT.getName() );
        int before = contentBrowsePanel.getSelectedRowsNumber();
        saveScreenshot( "test_arrow_up_before" );


        when: "arrow up has been pressed"
        contentBrowsePanel.pressKeyOnRow( PARENT_CONTENT.getPath(), Keys.ARROW_UP );
        saveScreenshot( "test_arrow_up_after" );

        then: "another row should be selected"
        !contentBrowsePanel.isRowSelected( PARENT_CONTENT.getPath().toString() ) && contentBrowsePanel.getSelectedRowsNumber() == before;
    }

    def "GIVEN a selected and expanded content and  WHEN arrow left is typed THEN folder becomes collapsed"()
    {
        given: "a selected and expanded folder(content)"
        contentBrowsePanel.selectContentInTable( PARENT_CONTENT.getName() );
        contentBrowsePanel.expandContent( PARENT_CONTENT.getPath() );
        saveScreenshot( "test_arrow_left_before" );

        when: "arrow left pressed"
        contentBrowsePanel.pressKeyOnRow( PARENT_CONTENT.getPath(), Keys.ARROW_LEFT );
        saveScreenshot( "test_arrow_left_after" );

        then: "folder should be collapsed"
        !contentBrowsePanel.isRowExpanded( PARENT_CONTENT.getName() );
    }

    def "GIVEN a selected and collapsed content and  WHEN arrow right is typed THEN folder is getting expanded"()
    {
        given: "a selected and collapsed folder"
        contentBrowsePanel.selectContentInTable( PARENT_CONTENT.getName() );

        when: "arrow right pressed"
        contentBrowsePanel.pressKeyOnRow( PARENT_CONTENT.getPath(), Keys.ARROW_RIGHT );
        saveScreenshot( "content_arrow_right" );

        then: "folder should be expanded"
        contentBrowsePanel.isRowExpanded( PARENT_CONTENT.getName() );
    }

    def "GIVEN selected content and WHEN hold a shift and arrow down pressed 3-times THEN 4 selected rows appears in the grid "()
    {
        given: "one content has been selected "
        contentBrowsePanel.selectContentInTable( IMPORTED_FOLDER_NAME );
        saveScreenshot( "test_arrow_down_shift_before" );

        when: "arrow down was pressed 3 times"
        contentBrowsePanel.holdShiftAndPressArrow( 3, Keys.ARROW_DOWN );
        saveScreenshot( "test_arrow_down_shift_after" );

        then: "n+1 rows should be selected in the browse panel"
        contentBrowsePanel.getSelectedRowsNumber() == 4
    }

    def "GIVEN one selected content WHEN hold a shift + arrow up pressed 3-times THEN 4 selected rows appears in the grid "()
    {
        given: "one content has been selected "
        sleep( 500 );
        contentBrowsePanel.clickCheckboxAndSelectRow( 4 );
        saveScreenshot( "test_arrow_up_shift_before2" );

        when: "arrow up was pressed 3 times"
        contentBrowsePanel.holdShiftAndPressArrow( 3, Keys.ARROW_UP );
        saveScreenshot( "test_arrow_up_shift_after" );

        then: "n+1 rows should be selected in the browse panel"
        contentBrowsePanel.getSelectedRowsNumber() == 4
    }

    //verifies  enonic/xp#4463 After a node is unhighlighted in the grid the toolbar action buttons are still active
    def "GIVEN row with the content is highlighted WHEN highlighting has been removed THEN 'Edit', 'Delete', 'Duplicate 'buttons  should be disabled on the toolbar"()
    {
        given: "row with the content is highlighted"
        contentBrowsePanel.clickOnRowByName( IMPORTED_FOLDER_NAME );

        when: "node is unhighlighted"
        contentBrowsePanel.clickOnRowByName( IMPORTED_FOLDER_NAME );

        then: "'Edit' button should be disabled"
        !contentBrowsePanel.isEditButtonEnabled();
        and: "'Sort' button should be disabled"
        !contentBrowsePanel.isSortButtonEnabled();
        and: "'Move' button should be disabled"
        !contentBrowsePanel.isMoveButtonEnabled();
        and: "'Delete' button should be disabled"
        !contentBrowsePanel.isDeleteButtonEnabled();
    }
}
