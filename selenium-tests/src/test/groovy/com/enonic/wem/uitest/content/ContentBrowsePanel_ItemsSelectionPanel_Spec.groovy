package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowseItemsSelectionPanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.api.schema.content.ContentTypeName
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_ItemsSelectionPanel_Spec
    extends BaseGebSpec
{
    @Shared
    String CONTENT_1_NAME = NameHelper.uniqueName( "folder" );

    @Shared
    String CONTENT_1_DISPLAY_NAME = "folder1Test"

    @Shared
    String CONTENT_2_NAME = NameHelper.uniqueName( "folder" );

    @Shared
    String CONTENT_2_DISPLAY_NAME = "folder2Test"

    @Shared
    String CONTENT_3_NAME = NameHelper.uniqueName( "folder" );

    @Shared
    String CONTENT_3_DISPLAY_NAME = "folder3Test"

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    @Shared
    ContentBrowseItemsSelectionPanel itemsSelectionPanel;

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
        itemsSelectionPanel = contentBrowsePanel.getItemSelectionPanel();
    }

    def "GIVEN one selected Content WHEN selecting one more THEN two SelectionItem-s are listed"()
    {
        given:
        Content firstContent = Content.builder().
            parent( ContentPath.ROOT ).
            name( CONTENT_1_NAME ).
            displayName( CONTENT_1_DISPLAY_NAME ).
            contentType( ContentTypeName.folder() ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( firstContent.getContentTypeName() ).
            typeData( firstContent ).save().close( firstContent.getDisplayName() );

        Content secondContent = Content.builder().
            parent( ContentPath.ROOT ).
            name( CONTENT_2_NAME ).
            contentType( ContentTypeName.folder() ).
            displayName( CONTENT_2_DISPLAY_NAME ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( secondContent.getContentTypeName() ).
            typeData( secondContent ).save().close( secondContent.getDisplayName() );

        contentBrowsePanel.selectContentInTable( firstContent.getPath() );

        when:

        contentBrowsePanel.waitUntilPageLoaded( 3 );
        contentBrowsePanel.selectContentInTable( secondContent.getPath() );

        then:
        itemsSelectionPanel.getSelectedItemCount() == 2;
    }

    def "GIVEN two selected Content WHEN selecting one more THEN three SelectionItem-s are listed"()
    {
        given:
        Content thirdContent = Content.builder().
            parent( ContentPath.ROOT ).
            name( CONTENT_3_NAME ).
            contentType( ContentTypeName.folder() ).
            displayName( CONTENT_3_DISPLAY_NAME ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( thirdContent.getContentTypeName() ).
            typeData( thirdContent ).save().close( thirdContent.getDisplayName() );

        Content firstContent = Content.builder().
            parent( ContentPath.ROOT ).
            name( CONTENT_1_NAME ).
            contentType( ContentTypeName.folder() ).
            displayName( CONTENT_1_DISPLAY_NAME ).build();
        Content secondContent = Content.builder().
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.folder() ).
            name( CONTENT_2_NAME ).
            displayName( CONTENT_2_DISPLAY_NAME ).build();
        List<Content> list = new ArrayList<>();
        list.add( firstContent );
        list.add( secondContent );

        contentBrowsePanel.selectContentInTable( list );

        int before = itemsSelectionPanel.getSelectedItemCount();

        when:
        contentBrowsePanel.selectContentInTable( thirdContent.getPath() );

        then:
        TestUtils.saveScreenshot( getSession(), "select_3_items" )
        itemsSelectionPanel.getSelectedItemCount() == before + 1;
    }

    def "GIVEN three selected Content WHEN deselecting one THEN two SelectionItem-s are listed"()
    {
        given:
        Content folderContent = Content.builder().
            parent( ContentPath.ROOT ).
            name( CONTENT_1_NAME ).
            contentType( ContentTypeName.folder() ).
            displayName( CONTENT_1_DISPLAY_NAME ).build();

        Content shortcutContent = Content.builder().
            parent( ContentPath.ROOT ).
            name( CONTENT_2_NAME ).
            contentType( ContentTypeName.folder() ).
            displayName( CONTENT_2_DISPLAY_NAME ).build();

        Content folderContent2 = Content.builder().
            parent( ContentPath.ROOT ).
            name( CONTENT_3_NAME ).
            contentType( ContentTypeName.folder() ).
            displayName( CONTENT_3_DISPLAY_NAME ).build();
        List<Content> list = new ArrayList<>();
        list.add( shortcutContent );
        list.add( folderContent );
        list.add( folderContent2 );

        contentBrowsePanel.selectContentInTable( list );
        int before = itemsSelectionPanel.getSelectedItemCount();

        when:
        TestUtils.saveScreenshot( getSession(), "deselect_before" );
        contentBrowsePanel.deSelectContentInTable( folderContent.getPath() );
        TestUtils.saveScreenshot( getSession(), "deselect_after" );

        then:
        itemsSelectionPanel.getSelectedItemCount() == before - 1;
    }
}
