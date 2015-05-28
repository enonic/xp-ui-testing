package com.enonic.wem.uitest.content

import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_ItemsSelectionPanel_Spec
    extends BaseContentSpec
{
    @Shared
    Content FIRST_CONTENT;

    @Shared
    Content SECOND_CONTENT;

    @Shared
    Content THIRD_CONTENT;


    def "GIVEN one selected Content WHEN selecting one more THEN two SelectionItem-s are listed"()
    {
        given:
        FIRST_CONTENT = buildFolderContent( "folder", "folder1" );
        addContent( FIRST_CONTENT );
        SECOND_CONTENT = buildFolderContent( "folder", "folder2" );
        addContent( SECOND_CONTENT );
        contentBrowsePanel.selectContentInTable( FIRST_CONTENT.getPath() );

        when:
        contentBrowsePanel.waitUntilPageLoaded( 3 );
        contentBrowsePanel.selectContentInTable( SECOND_CONTENT.getPath() );

        then:
        itemsSelectionPanel.getSelectedItemCount() == 2;
    }

    def "GIVEN two selected Content WHEN selecting one more THEN three SelectionItem-s are listed"()
    {
        given:
        THIRD_CONTENT = buildFolderContent( "folder", "folder3" );
        addContent( THIRD_CONTENT );
        List<Content> list = new ArrayList<>();
        list.add( FIRST_CONTENT );
        list.add( SECOND_CONTENT );
        contentBrowsePanel.selectContentInTable( list );
        int before = itemsSelectionPanel.getSelectedItemCount();
        TestUtils.saveScreenshot( getSession(), "select_2_items" )

        when:
        contentBrowsePanel.selectContentInTable( THIRD_CONTENT.getPath() );

        then:
        TestUtils.saveScreenshot( getSession(), "select_3_items" )
        itemsSelectionPanel.getSelectedItemCount() == before + 1;
    }

    def "GIVEN three selected Content WHEN deselecting one THEN two SelectionItem-s are listed"()
    {
        given:
        List<Content> list = new ArrayList<>();
        list.add( FIRST_CONTENT );
        list.add( SECOND_CONTENT );
        list.add( THIRD_CONTENT );

        contentBrowsePanel.selectContentInTable( list );
        int before = itemsSelectionPanel.getSelectedItemCount();

        when:
        TestUtils.saveScreenshot( getSession(), "deselect_before" );
        contentBrowsePanel.deSelectContentInTable( SECOND_CONTENT.getPath() );
        TestUtils.saveScreenshot( getSession(), "deselect_after" );

        then:
        itemsSelectionPanel.getSelectedItemCount() == before - 1;
    }
}
