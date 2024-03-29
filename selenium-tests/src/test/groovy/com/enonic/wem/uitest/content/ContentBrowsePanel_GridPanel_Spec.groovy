package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import org.openqa.selenium.Keys
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Ignore
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
        Number rowNumber = contentBrowsePanel.getRowsCount();

        expect: "all rows should be white(unselected)"
        contentBrowsePanel.getSelectedRowsNumber() == 0 && rowNumber > 0;

        and: "refresh button should be present in the tree grid toolbar"
        contentBrowsePanel.isRefreshButtonDisplayed();
    }

    def "GIVEN single content is selected WHEN refresh button has been clicked THEN the row remains selected"()
    {
        given: "one content is selected ( root directory)"
        List<String> contentNames = contentBrowsePanel.getContentNamesFromGrid();
        contentBrowsePanel.clickCheckboxAndSelectRow( contentNames.get( 0 ) );

        when: "refresh button has been pressed"
        contentBrowsePanel.clickOnRefreshButton();
        saveScreenshot( "test_refresh_button_clicked" );

        then: "the row should be selected"
        contentBrowsePanel.getSelectedRowsNumber() == 1;
    }

    def "GIVEN content grid is opened WHEN checkbox for the first content is checked THEN first row should be blue"()
    {
        given: "content grid is opened"
        List<String> contentNames = contentBrowsePanel.getContentNamesFromGrid();

        when: "the first row is checked"
        contentBrowsePanel.clickCheckboxAndSelectRow( contentNames.get( 0 ) );
        saveScreenshot( "test_first_row_selected" );

        then: "first row should be blue"
        contentBrowsePanel.getSelectedRowsNumber() == 1;
    }

    def "GIVEN existing content is selected WHEN space bar has been pressed THEN row is no longer selected"()
    {
        given: "existing content is selected"
        List<String> contentNames = contentBrowsePanel.getContentNamesFromGrid();
        contentBrowsePanel.clickCheckboxAndSelectRow( contentNames.get( 0 ) );
        saveScreenshot( "test_spacebar_before" );

        when: "spacebar was pressed"
        contentBrowsePanel.pressKeyOnRow( ContentPath.from( contentNames.get( 0 ) ), Keys.SPACE );

        then: "row is no longer selected"
        saveScreenshot( "test_spacebar_after" );
        contentBrowsePanel.getSelectedRowsNumber() == 0;
    }

    def "GIVEN existing content is selected WHEN 'Selection Controller' checkbox has been clicked THEN row is no longer selected"()
    {
        given: "existing content is selected"
        List<String> contentNames = contentBrowsePanel.getContentNamesFromGrid();
        contentBrowsePanel.clickCheckboxAndSelectRow( contentNames.get( 0 ) );

        when: "'Selection Controller' checkbox has been clicked"
        contentBrowsePanel.clickOnSelectionController();

        then: "row is no longer selected"
        contentBrowsePanel.getSelectedRowsNumber() == 0;
    }

    def "WHEN 'Selection Controller' has been checked THEN all rows should be selected"()
    {
        when: "'Selection Controller ' has been checked"
        contentBrowsePanel.doSelectAll();
        saveScreenshot( "test_select_all_content" );

        then: "all rows should be selected"
        contentBrowsePanel.getRowsCount() == contentBrowsePanel.getSelectedRowsNumber();

        and: "'Archive' button should be enabled"
        contentBrowsePanel.isArchiveButtonEnabled();

        and: "'New' button should be disabled"
        !contentBrowsePanel.isNewButtonEnabled();
    }

    def "GIVEN 'Selection Controller' is checked WHEN click on the 'Selection Controller' THEN it should be unchecked AND 'Preview', 'Delete' and 'New' buttons should be disabled"()
    {
        given: "'Selection Controller' is checked"
        contentBrowsePanel.clickOnSelectionController();
        saveScreenshot( "test_select_all" );

        when: "'Selection Controller ' has been clicked"
        contentBrowsePanel.clickOnSelectionController();
        saveScreenshot( "test_unselect_all" );

        then: "all rows should be white"
        contentBrowsePanel.getSelectedRowsNumber() == 0;

        and: "'Preview' button should be disabled"
        !contentBrowsePanel.isPreviewButtonEnabled();

        and: "'Archive' button should be disabled"
        !contentBrowsePanel.isArchiveButtonEnabled();

        and: "'New' button should be enabled"
        contentBrowsePanel.isNewButtonEnabled();
    }

    def "GIVEN existing parent folder WHEN expander icon has been clicked THEN child folder gets visible"()
    {
        given: "parent folder has been added and selected"
        PARENT_CONTENT = buildFolderContent( "parentfolder", "folder-test" );
        CHILD_CONTENT = buildFolderContentWithParent( "folder", "child folder", PARENT_CONTENT.getName() );
        addContent( PARENT_CONTENT );
        findAndSelectContent( PARENT_CONTENT.getName() );
        addContent( CHILD_CONTENT );

        when: "'expander icon' has been clicked"
        contentBrowsePanel.expandContent( PARENT_CONTENT.getPath() );

        then: "child content gets visible"
        contentBrowsePanel.exists( CHILD_CONTENT.getName() );

        List<String> childNames = contentBrowsePanel.getChildNames()
        and: "child content should be displayed"
        childNames.size() == 1;
    }

    def "GIVEN existing folder without children EXPECT 'expand icon' should not be displayed for the content"()
    {
        given: "existing folder without children"
        Content folder = buildFolderContent( "folder", "expander-test" )
        addContent( folder );
        filterPanel.typeSearchText( folder.getName() );

        expect: "'expand icon' should not be displayed for the content"
        !contentBrowsePanel.isExpanderPresent( ContentPath.from( folder.getName() ) );
    }

    def "GIVEN existing content is selected WHEN 'arrow down' has been pressed THEN next row should be selected"()
    {
        given:
        contentBrowsePanel.selectContentInGrid( PARENT_CONTENT.getName() );
        int before = contentBrowsePanel.getSelectedRowsNumber();
        saveScreenshot( "test_arrow_down_before" );

        when: "'arrow down' key has been pressed"
        contentBrowsePanel.pressKeyOnRow( PARENT_CONTENT.getPath(), Keys.ARROW_DOWN );
        saveScreenshot( "test_arrow_down_after" );
        then: " next row should be selected"
        !contentBrowsePanel.isRowSelected( PARENT_CONTENT.getName() ) && contentBrowsePanel.getSelectedRowsNumber() == before;
    }

    def "GIVEN existing content is selected WHEN arrow up has been pressed THEN previous row should be selected"()
    {
        given:
        contentBrowsePanel.selectContentInGrid( PARENT_CONTENT.getName() );
        int before = contentBrowsePanel.getSelectedRowsNumber();
        saveScreenshot( "test_arrow_up_before" );

        when: "arrow up has been pressed"
        contentBrowsePanel.pressKeyOnRow( PARENT_CONTENT.getPath(), Keys.ARROW_UP );
        saveScreenshot( "test_arrow_up_after" );

        then: "another row should be selected"
        !contentBrowsePanel.isRowSelected( PARENT_CONTENT.getPath().toString() ) && contentBrowsePanel.getSelectedRowsNumber() == before;
    }

    def "GIVEN existing content is selected and expanded WHEN arrow left has been pressed THEN the folder becomes collapsed"()
    {
        given: "existing content is selected and expanded"
        contentBrowsePanel.selectContentInGrid( PARENT_CONTENT.getName() );
        contentBrowsePanel.expandContent( PARENT_CONTENT.getPath() );
        saveScreenshot( "test_arrow_left_before" );

        when: "arrow left has ben pressed"
        contentBrowsePanel.pressKeyOnRow( PARENT_CONTENT.getPath(), Keys.ARROW_LEFT );
        saveScreenshot( "test_arrow_left_after" );

        then: "folder should be collapsed"
        !contentBrowsePanel.isRowExpanded( PARENT_CONTENT.getName() );
    }

    def "GIVEN existing content is selected and collapsed WHEN arrow right has been pressed THEN folder is getting expanded"()
    {
        given: " existing content is selected and collapsed"
        contentBrowsePanel.waitUntilPageLoaded( Application.EXPLICIT_NORMAL );
        contentBrowsePanel.selectContentInGrid( PARENT_CONTENT.getName() );

        when: "arrow right has been pressed"
        contentBrowsePanel.pressKeyOnRow( PARENT_CONTENT.getPath(), Keys.ARROW_RIGHT );
        saveScreenshot( "content_arrow_right" );

        then: "folder should be expanded"
        contentBrowsePanel.isRowExpanded( PARENT_CONTENT.getName() );
    }

    def "GIVEN one content is selected WHEN hold the 'shift' and arrow down has been pressed 3-times THEN 4 selected rows should be in the grid"()
    {
        given: "one content is selected"
        contentBrowsePanel.waitUntilPageLoaded( Application.EXPLICIT_NORMAL );
        contentBrowsePanel.selectContentInGrid( IMPORTED_FOLDER_NAME );
        saveScreenshot( "test_arrow_down_shift_before" );

        when: "hold the 'shift' and arrow down has been pressed 3-times"
        contentBrowsePanel.holdShiftAndPressArrow( 3, Keys.ARROW_DOWN );
        saveScreenshot( "test_arrow_down_shift_after" );

        then: "4 selected rows should be in the grid"
        contentBrowsePanel.getSelectedRowsNumber() == 4
    }

    def "GIVEN one content is selected WHEN hold the 'shift' + arrow up has been pressed 3-times THEN 4 selected rows should be in the grid "()
    {
        given: "one content has been selected"
        sleep( 500 );
        contentBrowsePanel.waitUntilPageLoaded( Application.EXPLICIT_NORMAL );
        contentBrowsePanel.clickCheckboxAndSelectRow( 4 );
        saveScreenshot( "test_arrow_up_shift_before2" );

        when: "hold the 'shift' + arrow up has been pressed 3-times"
        contentBrowsePanel.holdShiftAndPressArrow( 3, Keys.ARROW_UP );
        saveScreenshot( "test_arrow_up_shift_after" );

        then: "4 rows should be selected in the browse panel"
        contentBrowsePanel.getSelectedRowsNumber() == 4
    }

    def "GIVEN a row with the content is highlighted WHEN highlighting has been removed THEN 'Edit', 'Archive', 'Duplicate 'buttons get disabled"()
    {
        given: "row with the content is highlighted"
        contentBrowsePanel.waitUntilPageLoaded( Application.EXPLICIT_NORMAL );
        sleep( 500 );
        contentBrowsePanel.clickOnRowByName( IMPORTED_FOLDER_NAME );
        sleep( 1000 );

        when: "the row has been unhighlighted"
        contentBrowsePanel.clickOnRowByName( IMPORTED_FOLDER_NAME );
        sleep( 1000 );

        then: "'Edit' button should be disabled"
        !contentBrowsePanel.isEditButtonEnabled();
        and: "'Sort' button should be disabled"
        !contentBrowsePanel.isSortButtonEnabled();
        and: "'Move' button should be disabled"
        !contentBrowsePanel.isMoveButtonEnabled();
        and: "'Archive' button gets disabled"
        !contentBrowsePanel.isArchiveButtonEnabled();
    }
}
