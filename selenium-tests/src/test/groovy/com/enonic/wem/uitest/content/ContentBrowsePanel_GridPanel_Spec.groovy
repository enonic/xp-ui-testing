package com.enonic.wem.uitest.content

import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.schema.content.ContentTypeName
import org.openqa.selenium.Keys
import spock.lang.Shared

class ContentBrowsePanel_GridPanel_Spec
    extends BaseContentSpec
{

    @Shared
    String FOLDER_WITH_CHILD = NameHelper.uniqueName( "folder" );

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
        contentBrowsePanel.clickCheckboxAndSelectRow( ContentPath.from( contentNames.get( 0 ) ) );

        then:
        contentBrowsePanel.getSelectedRowsNumber() == 1;
    }

    def "GIVEN a Content selected WHEN spacebar is typed THEN row is no longer selected"()
    {
        given:
        List<String> contentNames = contentBrowsePanel.getContentNamesFromBrowsePanel();
        contentBrowsePanel.clickCheckboxAndSelectRow( ContentPath.from( contentNames.get( 0 ) ) );
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
        contentBrowsePanel.clickCheckboxAndSelectRow( ContentPath.from( contentNames.get( 0 ) ) );

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
        Content folderWithChild = Content.builder().
            name( FOLDER_WITH_CHILD ).
            displayName( "folder-test" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.ROOT ).
            build();
        Content child = Content.builder().
            name( "childfolder" ).
            displayName( "child_folder" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.from( folderWithChild.getName() ) ).
            build();

        contentBrowsePanel.clickToolbarNew().selectContentType( folderWithChild.getContentTypeName() ).typeData(
            folderWithChild ).save().close( folderWithChild.getDisplayName() );
        contentBrowsePanel.clickOnParentCheckbox( folderWithChild.getPath() )
        contentBrowsePanel.clickToolbarNew().selectContentType( child.getContentTypeName() ).typeData( child ).save().close(
            child.getDisplayName() );

        expect:
        contentBrowsePanel.exists( folderWithChild.getPath() ) &&
            contentBrowsePanel.isExpanderPresent( ContentPath.from( FOLDER_WITH_CHILD ) )
    }

    def "GIVEN a parent folder with child WHEN the name of parent typed in the TextSearchField and folder expanded THEN child content appears "()
    {
        given:
        filterPanel.typeSearchText( FOLDER_WITH_CHILD )

        when:
        contentBrowsePanel.expandContent( ContentPath.from( FOLDER_WITH_CHILD ) );
        then:
        ContentPath childPath = ContentPath.from( ContentPath.from( FOLDER_WITH_CHILD ), CHILD_CONTENT_NAME );
        contentBrowsePanel.exists( childPath );
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
        contentBrowsePanel.clickToolbarNew().selectContentType( unstructured.getContentTypeName() ).typeData( unstructured ).save().close(
            unstructured.getDisplayName() );

        expect:
        !contentBrowsePanel.isExpanderPresent( ContentPath.from( name ) );
    }

    def "GIVEN a Content with a closed expander WHEN expanded THEN one or more children is listed beneath"()
    {
        expect:
        !contentBrowsePanel.isRowExpanded( ContentPath.from( FOLDER_WITH_CHILD ).toString() );

        when:
        contentBrowsePanel.expandContent( ContentPath.from( FOLDER_WITH_CHILD ) );

        then:
        contentBrowsePanel.getChildNames( ContentPath.from( FOLDER_WITH_CHILD ) ).size() > 0;
    }

    def "GIVEN a Content with an open expander WHEN closed THEN no children are listed beneath"()
    {
        given:
        contentBrowsePanel.expandContent( ContentPath.from( FOLDER_WITH_CHILD ) );

        when:
        contentBrowsePanel.unExpandContent( ContentPath.from( FOLDER_WITH_CHILD ) );
        TestUtils.saveScreenshot( getTestSession(), "unexpandtest" );

        then:
        contentBrowsePanel.getChildNames( ContentPath.from( FOLDER_WITH_CHILD ) ).size() == 0;
    }


    def "GIVEN a selected Content  WHEN arrow down is typed THEN next row is selected"()
    {
        given:

        contentBrowsePanel.selectContentInTable( ContentPath.from( FOLDER_WITH_CHILD ) );
        int before = contentBrowsePanel.getSelectedRowsNumber();

        when:
        contentBrowsePanel.pressKeyOnRow( ContentPath.from( FOLDER_WITH_CHILD ), Keys.ARROW_DOWN );
        TestUtils.saveScreenshot( getTestSession(), "arrow_down" );
        then:
        !contentBrowsePanel.isRowSelected( ContentPath.from( FOLDER_WITH_CHILD ).toString() ) &&
            contentBrowsePanel.getSelectedRowsNumber() == before;
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
        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close(
            content.getDisplayName() );
        contentBrowsePanel.selectContentInTable( ContentPath.from( FOLDER_WITH_CHILD ) );
        int before = contentBrowsePanel.getSelectedRowsNumber();

        when:
        contentBrowsePanel.pressKeyOnRow( ContentPath.from( FOLDER_WITH_CHILD ), Keys.ARROW_UP );
        TestUtils.saveScreenshot( getTestSession(), "arrow_up" );

        then:
        !contentBrowsePanel.isRowSelected( ContentPath.from( FOLDER_WITH_CHILD ).toString() ) &&
            contentBrowsePanel.getSelectedRowsNumber() == before;
    }

    def "GIVEN a selected and expanded content and  WHEN arrow left is typed THEN folder becomes collapsed"()
    {
        given: "a selected and expanded folder(content)"
        ContentPath path = ContentPath.from( FOLDER_WITH_CHILD );
        contentBrowsePanel.selectContentInTable( path );
        contentBrowsePanel.expandContent( path )

        when: "arrow left typed"
        contentBrowsePanel.pressKeyOnRow( path, Keys.ARROW_LEFT );
        TestUtils.saveScreenshot( getTestSession(), "content_arrow_left" );

        then: "folder is collapsed"
        !contentBrowsePanel.isRowExpanded( path.toString() );
    }

    def "GIVEN a selected and collapsed content and  WHEN arrow right is typed THEN folder becomes expanded"()
    {
        given: "a selected and collapsed folder(content)"
        ContentPath path = ContentPath.from( FOLDER_WITH_CHILD );
        contentBrowsePanel.selectContentInTable( path );

        when: "arrow left typed"
        contentBrowsePanel.pressKeyOnRow( path, Keys.ARROW_RIGHT );
        TestUtils.saveScreenshot( getTestSession(), "content_arrow_right" );

        then: "folder is expanded"
        contentBrowsePanel.isRowExpanded( path.toString() );
    }

    def "GIVEN selected content and WHEN hold a shift and arrow down is typed  3-times THEN 4 selected rows appears in the grid "()
    {
        given: "selected and collapsed folder(content)"
        ContentPath path = ContentPath.from( FOLDER_WITH_CHILD );
        contentBrowsePanel.selectContentInTable( path );

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

    String getTestContentName( List<String> contentNames )
    {
        for ( String name : contentNames )
        {
            if ( name.contains( FOLDER_WITH_CHILD ) )
            {
                return name;
            }
        }
        return null;
    }
}
