package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent
import com.enonic.autotests.vo.contentmanager.FolderContent
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.uitest.BaseGebSpec
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


    def "GIVEN content App BrowsePanel and existing content WHEN content selected and Open button have clicked THEN title with content display-name showed"()
    {
        given:
        String contentName = NameHelper.unqiueName( "itemviewtest" );
        BaseAbstractContent content = FolderContent.builder().withName( contentName ).withDisplayName( "itemviewtest" ).withParent(
            ContentPath.ROOT ).build();
        addContent( contentBrowsePanel, content, true )

        when:
        ItemViewPanelPage itemView = contentBrowsePanel.selectRowByContentPath( content.getPath().toString() ).clickToolbarOpen()
        itemView.waitUntilOpened( content.getDisplayName(), 1 )

        // ItemViewPanelPage contentInfoPage = cmPage.doOpenContent( content )

        then:
        itemView.getTitle().equals( content.getDisplayName() );

    }


}