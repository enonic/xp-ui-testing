package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.api.schema.content.ContentTypeName
import com.enonic.wem.uitest.BaseGebSpec
import org.openqa.selenium.Keys
import spock.lang.Shared

class ContentBrowsePanel_GridPanel_Spec
    extends BaseGebSpec
{


    @Shared
    ContentBrowsePanel contentBrowsePanel;

    @Shared
    String FOLDER_WITH_CHILD = NameHelper.uniqueName( "folder" );


    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }

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
        contentBrowsePanel.doClearSelection();

        then:
        contentBrowsePanel.getSelectedRowsNumber() == 0;
    }

    def "GIVEN no Content selected WHEN 'Select all'-link is clicked THEN all rows are selected"()
    {
        given:
        contentBrowsePanel.doClearSelection();

        when:
        int selectedNumber = contentBrowsePanel.doSelectAll();

        then:
        contentBrowsePanel.getRowNumber() == selectedNumber;
    }

    def "GIVEN a Content on root having a child WHEN listed THEN expander is shown"()
    {
        given:
        Content folderWithChild = Content.builder().
            name( FOLDER_WITH_CHILD ).
            displayName( "folderWithChild" ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.ROOT ).
            build();
        Content child = Content.builder().
            name( "child" ).
            displayName( "child" ).
            contentType( ContentTypeName.archiveMedia() ).
            parent( ContentPath.from( folderWithChild.getName() ) ).
            build();

        contentBrowsePanel.clickToolbarNew().selectContentType( folderWithChild.getContentTypeName() ).typeData(
            folderWithChild ).save().close();
        contentBrowsePanel.clickByParentCheckbox( folderWithChild.getPath() )
        contentBrowsePanel.clickToolbarNew().selectContentType( child.getContentTypeName() ).typeData( child ).save().close();

        expect:
        contentBrowsePanel.exists( folderWithChild.getPath() ) &&
            contentBrowsePanel.isExpanderPresent( ContentPath.from( FOLDER_WITH_CHILD ) )
    }

    def "GIVEN a Content on root having no children WHEN listed THEN expander is not shown"()
    {
        given:
        String name = NameHelper.uniqueName( "mediadata" );
        Content mediadata = Content.builder().
            name( name ).
            displayName( "mediadata" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.MEDIA_DATA ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( mediadata.getContentTypeName() ).typeData( mediadata ).save().close();

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


    def "GIVEN a Content selected WHEN arrow down is typed THEN next row is selected"()
    {
        given:

        contentBrowsePanel.selectContentInTable( ContentPath.from( FOLDER_WITH_CHILD ) );
        int before = contentBrowsePanel.getSelectedRowsNumber();

        when:
        contentBrowsePanel.pressKeyOnRow( ContentPath.from( FOLDER_WITH_CHILD ), Keys.ARROW_UP );
        TestUtils.saveScreenshot( getTestSession(), "arrow_up" );
        then:
        !contentBrowsePanel.isRowSelected( ContentPath.from( FOLDER_WITH_CHILD ).toString() ) &&
            contentBrowsePanel.getSelectedRowsNumber() == before;
    }

    def "GIVEN a Content selected WHEN arrow up is typed THEN previous row is selected"()
    {
        given:
        String name = NameHelper.uniqueName( "data" );
        Content data = Content.builder().
            name( name ).
            displayName( "data" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.dataMedia() ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( data.getContentTypeName() ).typeData( data ).save().close();
        contentBrowsePanel.selectContentInTable( ContentPath.from( name ) );
        int before = contentBrowsePanel.getSelectedRowsNumber();

        when:
        contentBrowsePanel.pressKeyOnRow( ContentPath.from( name ), Keys.ARROW_DOWN );
        TestUtils.saveScreenshot( getTestSession(), "arrow_down" );

        then:
        !contentBrowsePanel.isRowSelected( ContentPath.from( name ).toString() ) && contentBrowsePanel.getSelectedRowsNumber() == before;
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
