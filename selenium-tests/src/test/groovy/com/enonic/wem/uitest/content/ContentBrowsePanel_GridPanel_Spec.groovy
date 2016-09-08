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

    def "GIVEN Content listed on root WHEN no selection THEN all rows are white"()
    {
        given:
        int rowNumber = contentBrowsePanel.getRowsCount();

        expect:
        contentBrowsePanel.getSelectedRowsNumber() == 0 && rowNumber > 0;

        and: "refresh button is present on the tree grid toolbar"
        contentBrowsePanel.isRefreshButtonDisplayed();
    }

    def "GIVEN one content is selected in the root WHEN refresh button pressed THEN the row stays selected"()
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

    def "GIVEN Content listed on root WHEN first is clicked THEN first row is blue"()
    {
        given:
        List<String> contentNames = contentBrowsePanel.getContentNamesFromGrid();

        when:
        contentBrowsePanel.clickCheckboxAndSelectRow( contentNames.get( 0 ) );
        saveScreenshot( "test_first_row_selected" );

        then:
        contentBrowsePanel.getSelectedRowsNumber() == 1;
    }

    def "GIVEN a Content selected WHEN spacebar is typed THEN row is no longer selected"()
    {
        given:
        List<String> contentNames = contentBrowsePanel.getContentNamesFromGrid();
        contentBrowsePanel.clickCheckboxAndSelectRow( contentNames.get( 0 ) );
        saveScreenshot( "test_spacebar_before" );

        when:
        contentBrowsePanel.pressKeyOnRow( ContentPath.from( contentNames.get( 0 ) ), Keys.SPACE );

        then:
        saveScreenshot( "test_spacebar_after" );
        contentBrowsePanel.getSelectedRowsNumber() == 0;
    }

    def "GIVEN a Content selected WHEN 'Clear selection'-link is clicked THEN row is no longer selected"()
    {
        given:
        List<String> contentNames = contentBrowsePanel.getContentNamesFromGrid();
        contentBrowsePanel.clickCheckboxAndSelectRow( contentNames.get( 0 ) );

        when:
        contentBrowsePanel.clickOnClearSelection();

        then:
        contentBrowsePanel.getSelectedRowsNumber() == 0;
    }

    def "GIVEN no Content selected WHEN 'Select all'-link is clicked THEN all rows are selected"()
    {
        given:
        contentBrowsePanel.clickOnClearSelection();

        when:
        contentBrowsePanel.clickOnSelectAll();
        saveScreenshot( "test_select_all_content" );

        then:
        contentBrowsePanel.getRowsCount() == contentBrowsePanel.getSelectedRowsNumber();
    }

    def "GIVEN a Content on root having a child WHEN listed THEN expander is shown"()
    {
        given:
        PARENT_CONTENT = buildFolderContent( "parentfolder", "folder-test" );
        CHILD_CONTENT = buildFolderContentWithParent( "child-content", "child folder", PARENT_CONTENT.getName() );
        addContent( PARENT_CONTENT );
        contentBrowsePanel.clickCheckboxAndSelectRow( PARENT_CONTENT.getName() );
        addContent( CHILD_CONTENT );

        expect:
        contentBrowsePanel.exists( PARENT_CONTENT.getName() ) && contentBrowsePanel.isExpanderPresent( PARENT_CONTENT.getName() )
    }

    def "GIVEN a parent folder with child WHEN the name of parent typed in the TextSearchField and folder expanded THEN child content appears "()
    {
        given: "a parent folder with child and filter panel shown"
        contentBrowsePanel.doShowFilterPanel();

        when: "the name of parent typed in the TextSearchField and folder expanded"
        filterPanel.typeSearchText( PARENT_CONTENT.getName() )
        contentBrowsePanel.expandContent( PARENT_CONTENT.getPath() );

        then: "child content appears"
        contentBrowsePanel.exists( CHILD_CONTENT.getName() );
    }

    def "GIVEN a Content on root having no children WHEN listed THEN expander is not shown"()
    {
        given:
        String name = NameHelper.uniqueName( "unstructured" );
        Content unstructured = Content.builder().
            name( name ).
            displayName( "unstructured" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.unstructured() ).
            build();
        addContent( unstructured );

        expect:
        !contentBrowsePanel.isExpanderPresent( ContentPath.from( name ) );
    }

    def "GIVEN a Content with a closed expander WHEN expanded THEN one or more children is listed beneath"()
    {
        expect:
        !contentBrowsePanel.isRowExpanded( PARENT_CONTENT.getName() );

        when:
        filterPanel.typeSearchText( PARENT_CONTENT.getName() );
        contentBrowsePanel.expandContent( PARENT_CONTENT.getPath() );
        List<String> names = contentBrowsePanel.getChildNames()

        then:
        names.size() == 1;
        and:
        names.get( 0 ) == CHILD_CONTENT.getName();

    }

    def "GIVEN a Content with an open expander WHEN closed THEN no children are listed beneath"()
    {
        given:
        contentBrowsePanel.expandContent( PARENT_CONTENT.getPath() );

        when:
        contentBrowsePanel.unExpandContent( PARENT_CONTENT.getPath() );
        saveScreenshot( "test_unexpand_folder" );

        then:
        contentBrowsePanel.getChildNames().size() == 0;
    }

    def "GIVEN a selected Content  WHEN arrow down is typed THEN next row is selected"()
    {
        given:
        contentBrowsePanel.selectContentInTable( PARENT_CONTENT.getName() );
        int before = contentBrowsePanel.getSelectedRowsNumber();
        saveScreenshot( "test_arrow_down_before" );

        when:
        contentBrowsePanel.pressKeyOnRow( PARENT_CONTENT.getPath(), Keys.ARROW_DOWN );
        saveScreenshot( "test_arrow_down_after" );
        then:
        !contentBrowsePanel.isRowSelected( PARENT_CONTENT.getName() ) && contentBrowsePanel.getSelectedRowsNumber() == before;
    }

    def "GIVEN a selected content WHEN arrow up is typed THEN previous row is selected"()
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


        when:
        contentBrowsePanel.pressKeyOnRow( PARENT_CONTENT.getPath(), Keys.ARROW_UP );
        saveScreenshot( "test_arrow_up_after" );

        then:
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

        then: "folder is collapsed"
        !contentBrowsePanel.isRowExpanded( PARENT_CONTENT.getName() );
    }

    def "GIVEN a selected and collapsed content and  WHEN arrow right is typed THEN folder becomes expanded"()
    {
        given: "a selected and collapsed folder"
        contentBrowsePanel.selectContentInTable( PARENT_CONTENT.getName() );

        when: "arrow right pressed"
        contentBrowsePanel.pressKeyOnRow( PARENT_CONTENT.getPath(), Keys.ARROW_RIGHT );
        saveScreenshot( "content_arrow_right" );

        then: "folder is expanded"
        contentBrowsePanel.isRowExpanded( PARENT_CONTENT.getName() );
    }

    def "GIVEN selected content and WHEN hold a shift and arrow down pressed 3-times THEN 4 selected rows appears in the grid "()
    {
        given: "one content has been selected "
        contentBrowsePanel.selectContentInTable( IMPORTED_FOLDER_NAME );
        saveScreenshot( "test_arrow_down_shift_before" );

        when: "arrow down pressed 3 times"
        contentBrowsePanel.holdShiftAndPressArrow( 3, Keys.ARROW_DOWN );
        saveScreenshot( "test_arrow_down_shift_after" );

        then: "n+1 rows are selected in the browse panel"
        contentBrowsePanel.getSelectedRowsNumber() == 4
    }

    def "GIVEN one selected content WHEN hold a shift + arrow up pressed 3-times THEN 4 selected rows appears in the grid "()
    {
        given: "one content has been selected "
        contentBrowsePanel.clickCheckboxAndSelectRow( 4 );
        saveScreenshot( "test_arrow_up_shift_before" );

        when: "arrow up pressed 3 times"
        contentBrowsePanel.holdShiftAndPressArrow( 3, Keys.ARROW_UP );
        saveScreenshot( "test_arrow_up_shift_after" );

        then: "n+1 rows are selected in the browse panel"
        contentBrowsePanel.getSelectedRowsNumber() == 4
    }
}
