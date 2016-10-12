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
        given: "one selected Content "
        FIRST_CONTENT = buildFolderContent( "folder", "folder1" );
        addContent( FIRST_CONTENT );
        SECOND_CONTENT = buildFolderContent( "folder", "folder2" );
        addContent( SECOND_CONTENT );
        contentBrowsePanel.selectContentInTable( FIRST_CONTENT.getName() );

        when: "selecting one more"
        contentBrowsePanel.waitUntilPageLoaded( 3 );
        contentBrowsePanel.selectContentInTable( SECOND_CONTENT.getName() );

        then: "two SelectionItem-s are listed"
        itemsSelectionPanel.getSelectedItemCount() == 2;
    }

    def "GIVEN two selected Content WHEN selecting one more THEN three SelectionItem-s are listed"()
    {
        given: "two selected Content"
        THIRD_CONTENT = buildFolderContent( "folder", "folder3" );
        addContent( THIRD_CONTENT );
        List<String> list = new ArrayList<>();
        list.add( FIRST_CONTENT.getName() );
        list.add( SECOND_CONTENT.getName() );
        contentBrowsePanel.selectContentInTable( list );
        int before = itemsSelectionPanel.getSelectedItemCount();
        TestUtils.saveScreenshot( getSession(), "select_2_items" )

        when: "selecting one more"
        contentBrowsePanel.selectContentInTable( THIRD_CONTENT.getName() );

        then: "three SelectionItem-s are listed"
        saveScreenshot( "select_3_items" )
        itemsSelectionPanel.getSelectedItemCount() == before + 1;
    }

    def "GIVEN three selected Content WHEN deselecting one THEN two SelectionItem-s are listed"()
    {
        given: "three selected Content"
        List<String> list = new ArrayList<>();
        list.add( FIRST_CONTENT.getName() );
        list.add( SECOND_CONTENT.getName() );
        list.add( THIRD_CONTENT.getName() );

        contentBrowsePanel.selectContentInTable( list );
        int before = itemsSelectionPanel.getSelectedItemCount();

        when: " deselecting one"
        saveScreenshot( "deselect_before" );
        contentBrowsePanel.deSelectContentInTable( SECOND_CONTENT.getName() );
        saveScreenshot( "deselect_after" );

        then: "two SelectionItem-s are listed"
        itemsSelectionPanel.getSelectedItemCount() == before - 1;
    }
}
