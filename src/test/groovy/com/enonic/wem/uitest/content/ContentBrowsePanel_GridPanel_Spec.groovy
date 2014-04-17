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
    String BILDERAKIV = "bildearkiv";


    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }

    def "GIVEN Content listed on root WHEN no selection THEN all rows are white"()
    {
        given:
        List<String> contentNames = contentBrowsePanel.getContentNamesFromBrowsePanel();

        expect:
        contentBrowsePanel.getSelectedRowsNumber() == 0 && contentNames.size() > 0;

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
        contentBrowsePanel.getContentNamesFromBrowsePanel().size() == selectedNumber;
    }

    def "GIVEN a Content on root having a child WHEN listed THEN expander is shown"()
    {
        given:
        List<String> contentNames = contentBrowsePanel.getContentNamesFromBrowsePanel();

        expect:
        getTestContentName( contentNames ) != null && contentBrowsePanel.isExpanderPresent( ContentPath.from( BILDERAKIV ) )
    }

    def "GIVEN a Content on root having no children WHEN listed THEN expander is not shown"()
    {
        given:
        String name = NameHelper.uniqueName( "mediadata" );
        Content page = Content.builder().
            name( name ).
            displayName( "page" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.MEDIA_DATA ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( page.getContentTypeName() ).typeData( page ).save().close();

        expect:
        !contentBrowsePanel.isExpanderPresent( ContentPath.from( name ) );
    }

    def "GIVEN a Content with a closed expander WHEN expanded THEN one or more children is listed beneath"()
    {
        expect:
        !contentBrowsePanel.isRowExapnded( ContentPath.from( BILDERAKIV ).toString() );

        when:
        contentBrowsePanel.expandContent( ContentPath.from( BILDERAKIV ) );

        then:
        contentBrowsePanel.getChildNames( ContentPath.from( BILDERAKIV ) ).size() > 0;
    }

    def "GIVEN a Content with an open expander WHEN closed THEN no children are listed beneath"()
    {
        given:
        contentBrowsePanel.expandContent( ContentPath.from( BILDERAKIV ) );

        when:
        contentBrowsePanel.unExpandContent( ContentPath.from( BILDERAKIV ) );

        then:
        contentBrowsePanel.getChildNames( ContentPath.from( BILDERAKIV ) ).size() == 0;
    }


    def "GIVEN a Content selected WHEN arrow down is typed THEN next row is selected"()
    {
        given:

        contentBrowsePanel.selectContentInTable( ContentPath.from( BILDERAKIV ) );
        int before = contentBrowsePanel.getSelectedRowsNumber();

        when:
        contentBrowsePanel.pressKeyOnRow( ContentPath.from( BILDERAKIV ), Keys.ARROW_DOWN );

        then:
        !contentBrowsePanel.isRowSelected( ContentPath.from( BILDERAKIV ).toString() ) && contentBrowsePanel.getSelectedRowsNumber() ==
            before;
    }

    def "GIVEN a Content selected WHEN arrow up is typed THEN previous row is selected"()
    {
        given:
        String name = NameHelper.uniqueName( "page" );
        Content page = Content.builder().
            name( name ).
            displayName( "page" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.page() ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( page.getContentTypeName() ).typeData( page ).save().close();
        contentBrowsePanel.selectContentInTable( ContentPath.from( name ) );
        int before = contentBrowsePanel.getSelectedRowsNumber();

        when:
        contentBrowsePanel.pressKeyOnRow( ContentPath.from( name ), Keys.ARROW_UP );

        then:
        !contentBrowsePanel.isRowSelected( ContentPath.from( name ).toString() ) && contentBrowsePanel.getSelectedRowsNumber() == before;
    }

    String getTestContentName( List<String> contentNames )
    {
        for ( String name : contentNames )
        {
            if ( name.contains( BILDERAKIV ) )
            {
                return name;
            }
        }
        return null;
    }
}
