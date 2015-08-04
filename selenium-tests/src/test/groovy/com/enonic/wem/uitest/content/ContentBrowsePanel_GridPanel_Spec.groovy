package com.enonic.wem.uitest.content

import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
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
    Content parentContent;

    @Shared
    String CHILD_CONTENT_NAME = "childfolder";

    def "GIVEN Content listed on root WHEN no selection THEN all rows are white"()
    {
        given:
        int rowNumber = contentBrowsePanel.getRowNumber();

        expect:
        contentBrowsePanel.getSelectedRowsNumber() == 0 && rowNumber > 0;
    }

    def "GIVEN Content listed on root WHEN first is clicked THEN first row is blue"()
    {
        given:
        List<String> contentNames = contentBrowsePanel.getContentNamesFromBrowsePanel();

        when:
        contentBrowsePanel.clickCheckboxAndSelectRow( contentNames.get( 0 ) );

        then:
        contentBrowsePanel.getSelectedRowsNumber() == 1;
    }

    def "GIVEN a Content selected WHEN spacebar is typed THEN row is no longer selected"()
    {
        given:
        List<String> contentNames = contentBrowsePanel.getContentNamesFromBrowsePanel();
        contentBrowsePanel.clickCheckboxAndSelectRow( contentNames.get( 0 ) );
        TestUtils.saveScreenshot( getTestSession(), "spacebartest1" );

        when:
        contentBrowsePanel.pressKeyOnRow( ContentPath.from( contentNames.get( 0 ) ), Keys.SPACE );

        then:
        TestUtils.saveScreenshot( getTestSession(), "spacebartest2" );
        contentBrowsePanel.getSelectedRowsNumber() == 0;
    }

    def "GIVEN a Content selected WHEN 'Clear selection'-link is clicked THEN row is no longer selected"()
    {
        given:
        List<String> contentNames = contentBrowsePanel.getContentNamesFromBrowsePanel();
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
        TestUtils.saveScreenshot( getTestSession(), "select-all1" );

        then:
        contentBrowsePanel.getRowNumber() == contentBrowsePanel.getSelectedRowsNumber();
    }

    def "GIVEN a Content on root having a child WHEN listed THEN expander is shown"()
    {
        given:
        parentContent = buildFolderContent( "parentfolder", "folder-test" );
        Content child = Content.builder().
            name( CHILD_CONTENT_NAME ).
            displayName( "child_folder" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.from( parentContent.getName() ) ).
            build();
        addContent( parentContent );
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getName() );
        addContent( child );

        expect:
        contentBrowsePanel.exists( parentContent.getName() ) && contentBrowsePanel.isExpanderPresent( parentContent.getName() )
    }

    def "GIVEN a parent folder with child WHEN the name of parent typed in the TextSearchField and folder expanded THEN child content appears "()
    {
        given: "a parent folder with child and filter panel shown"
        contentBrowsePanel.doShowFilterPanel();

        when: "the name of parent typed in the TextSearchField and folder expanded"
        filterPanel.typeSearchText( parentContent.getName() )
        contentBrowsePanel.expandContent( parentContent.getPath() );

        then: "child content appears"
        contentBrowsePanel.exists( CHILD_CONTENT_NAME );
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
        !contentBrowsePanel.isRowExpanded( parentContent.getName() );

        when:
        filterPanel.typeSearchText( parentContent.getName() );
        contentBrowsePanel.expandContent( parentContent.getPath() );
        List<String> names = contentBrowsePanel.getChildNames()

        then:
        names.size() == 1;
        and:
        names.get( 0 ) == CHILD_CONTENT_NAME;

    }

    def "GIVEN a Content with an open expander WHEN closed THEN no children are listed beneath"()
    {
        given:
        contentBrowsePanel.expandContent( parentContent.getPath() );

        when:
        contentBrowsePanel.unExpandContent( parentContent.getPath() );
        TestUtils.saveScreenshot( getTestSession(), "unexpandtest" );

        then:
        contentBrowsePanel.getChildNames().size() == 0;
    }

    def "GIVEN a selected Content  WHEN arrow down is typed THEN next row is selected"()
    {
        given:
        contentBrowsePanel.selectContentInTable( parentContent.getName() );
        int before = contentBrowsePanel.getSelectedRowsNumber();

        when:
        contentBrowsePanel.pressKeyOnRow( parentContent.getPath(), Keys.ARROW_DOWN );
        TestUtils.saveScreenshot( getTestSession(), "arrow_down" );
        then:
        !contentBrowsePanel.isRowSelected( parentContent.getName() ) && contentBrowsePanel.getSelectedRowsNumber() == before;
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
        contentBrowsePanel.selectContentInTable( parentContent.getName() );
        int before = contentBrowsePanel.getSelectedRowsNumber();

        when:
        contentBrowsePanel.pressKeyOnRow( parentContent.getPath(), Keys.ARROW_UP );
        TestUtils.saveScreenshot( getTestSession(), "arrow_up" );

        then:
        !contentBrowsePanel.isRowSelected( parentContent.getPath().toString() ) && contentBrowsePanel.getSelectedRowsNumber() == before;
    }

    def "GIVEN a selected and expanded content and  WHEN arrow left is typed THEN folder becomes collapsed"()
    {
        given: "a selected and expanded folder(content)"
        contentBrowsePanel.selectContentInTable( parentContent.getName() );
        contentBrowsePanel.expandContent( parentContent.getPath() )

        when: "arrow left typed"
        contentBrowsePanel.pressKeyOnRow( parentContent.getPath(), Keys.ARROW_LEFT );
        TestUtils.saveScreenshot( getTestSession(), "content_arrow_left" );

        then: "folder is collapsed"
        !contentBrowsePanel.isRowExpanded( parentContent.getName() );
    }

    def "GIVEN a selected and collapsed content and  WHEN arrow right is typed THEN folder becomes expanded"()
    {
        given: "a selected and collapsed folder"
        contentBrowsePanel.selectContentInTable( parentContent.getName() );

        when: "arrow left typed"
        contentBrowsePanel.pressKeyOnRow( parentContent.getPath(), Keys.ARROW_RIGHT );
        TestUtils.saveScreenshot( getTestSession(), "content_arrow_right" );

        then: "folder is expanded"
        contentBrowsePanel.isRowExpanded( parentContent.getName() );
    }

    def "GIVEN selected content and WHEN hold a shift and arrow down is typed  3-times THEN 4 selected rows appears in the grid "()
    {
        given: "selected and collapsed folder(content)"
        contentBrowsePanel.selectContentInTable( IMPORTED_FOLDER_NAME );

        when: "arrow down typed 3 times"
        contentBrowsePanel.holdShiftAndPressArrow( 3, Keys.ARROW_DOWN );
        TestUtils.saveScreenshot( getTestSession(), "content_arrow_down_shift" );

        then: "n+1 rows are selected in the browse panel"
        contentBrowsePanel.getSelectedRowsNumber() == 4
    }

    def "GIVEN selected content  WHEN hold a shift and arrow up is typed  3-times THEN 4 selected rows appears in the grid "()
    {
        given: "one content  is selected and collapsed "
        contentBrowsePanel.clickCheckboxAndSelectRow( 4 );

        when: "arrow up typed 3 times"
        contentBrowsePanel.holdShiftAndPressArrow( 3, Keys.ARROW_UP );
        TestUtils.saveScreenshot( getTestSession(), "content_arrow_up_shift" );

        then: "n+1 rows are selected in the browse panel"
        contentBrowsePanel.getSelectedRowsNumber() == 4
    }
}
