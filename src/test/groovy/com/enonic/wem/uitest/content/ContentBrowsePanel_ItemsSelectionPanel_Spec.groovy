package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ItemsSelectionPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
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
    String CONTENT_1_NAME = NameHelper.uniqueName( "homepage" );

    @Shared
    String CONTENT_1_DISPALY_NAME = "Homepage"

    @Shared
    String CONTENT_2_NAME = NameHelper.uniqueName( "intranet" );

    @Shared
    String CONTENT_2_DISPALY_NAME = "Intranet"

    @Shared
    String CONTENT_3_NAME = "imagearchive"

    @Shared
    String CONTENT_3_DISPALY_NAME = "Image Archive"

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    @Shared
    ItemsSelectionPanel itemsSelectionPanel;

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
            displayName( CONTENT_1_DISPALY_NAME ).
            contentType( ContentTypeName.page() ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( firstContent.getContentTypeName() ).
            typeData( firstContent ).save().close();

        Content secondContent = Content.builder().
            parent( ContentPath.ROOT ).
            name( CONTENT_2_NAME ).
            contentType( ContentTypeName.page() ).
            displayName( CONTENT_2_DISPALY_NAME ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( secondContent.getContentTypeName() ).
            typeData( secondContent ).save().close();

        contentBrowsePanel.selectContentInTable( firstContent.getPath() );

        when:

        contentBrowsePanel.waituntilPageLoaded( 3 );
        contentBrowsePanel.selectContentInTable( secondContent.getPath() );

        then:
        itemsSelectionPanel.getSeletedItemCount() == 2;
    }

    def "GIVEN two selected Content WHEN selecting one more THEN three SelectionItem-s are listed"()
    {
        given:
        Content firstContent = Content.builder().
            parent( ContentPath.ROOT ).
            name( CONTENT_1_NAME ).
            contentType( ContentTypeName.page() ).
            displayName( CONTENT_1_DISPALY_NAME ).build();
        Content secondContent = Content.builder().
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.page() ).
            name( CONTENT_2_NAME ).
            displayName( CONTENT_2_DISPALY_NAME ).build();
        List<Content> list = new ArrayList<>();
        list.add( firstContent );
        list.add( secondContent );
        contentBrowsePanel.selectContentInTable( list );
        int before = itemsSelectionPanel.getSeletedItemCount();

        when:
        Content thirdContent = Content.builder().
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.page() ).
            name( CONTENT_3_NAME ).
            displayName( CONTENT_3_DISPALY_NAME ).build();
        contentBrowsePanel.selectContentInTable( thirdContent.getPath() );

        then:
        itemsSelectionPanel.getSeletedItemCount() == before + 1;
    }

    def "GIVEN three selected Content WHEN deselecting one THEN two SelectionItem-s are listed"()
    {
        given:
        Content siteHomepage = Content.builder().
            parent( ContentPath.ROOT ).
            name( CONTENT_1_NAME ).
            contentType( ContentTypeName.page() ).
            displayName( CONTENT_1_DISPALY_NAME ).build();

        Content siteIntranet = Content.builder().
            parent( ContentPath.ROOT ).
            name( CONTENT_2_NAME ).
            contentType( ContentTypeName.page() ).
            displayName( CONTENT_2_DISPALY_NAME ).build();

        Content folderBildearkiv = Content.builder().
            parent( ContentPath.ROOT ).
            name( CONTENT_3_NAME ).
            contentType( ContentTypeName.page() ).
            displayName( CONTENT_3_DISPALY_NAME ).build();
        List<Content> list = new ArrayList<>();
        list.add( siteHomepage );
        list.add( siteIntranet );
        list.add( folderBildearkiv );
        contentBrowsePanel.selectContentInTable( list );
        int before = itemsSelectionPanel.getSeletedItemCount();

        when:
        contentBrowsePanel.deSelectContentInTable( folderBildearkiv.getPath() );

        then:
        itemsSelectionPanel.getSeletedItemCount() == before - 1;
    }
}
