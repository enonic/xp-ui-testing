package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.api.schema.content.ContentTypeName
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Ignore
import spock.lang.Shared

class ContentItemViewPanelSpec
    extends BaseGebSpec
{
    @Shared
    ContentBrowsePanel contentBrowsePanel;

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }

    @Ignore
    def "GIVEN content App BrowsePanel and existing content WHEN content selected and Open button have clicked THEN title with content display-name showed"()
    {
        given:
        String contentName = NameHelper.uniqueName( "itemviewtest" );
        Content content = Content.builder().
            name( contentName ).
            displayName( "itemviewtest" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.folder() ).
            build();

        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close(
            content.getDisplayName() );

        when:
        ItemViewPanelPage itemView = contentBrowsePanel.selectRowByContentPath( content.getPath().toString() ).clickToolbarOpen();
        itemView.waitUntilOpened( content.getDisplayName() );

        then:
        itemView.getTitle().equals( content.getDisplayName() );

    }


}