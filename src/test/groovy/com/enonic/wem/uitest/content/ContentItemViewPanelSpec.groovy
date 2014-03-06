package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ItemViewPanelPage
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.vo.contentmanager.BaseAbstractContent
import com.enonic.autotests.vo.contentmanager.FolderContent
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.uitest.BaseGebSpec

class ContentItemViewPanelSpec
    extends BaseGebSpec
{

    def "GIVEN content App BrowsePanel and existing content WHEN content selected and Open button have clicked THEN title with content display-name showed"()
    {
        given:
        go "admin"
        String contentName = "itemviewtest";
        BaseAbstractContent content = FolderContent.builder().withName( "itemviewtest" ).withDisplayName( "itemviewtest" ).withParent(
            ContentPath.ROOT ).build();
        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() )
        addContent(contentBrowsePanel, content, true)

        when:
        ContentBrowsePanel cmPage = NavigatorHelper.openContentApp( session )
        ItemViewPanelPage contentInfoPage = cmPage.doOpenContent( content )

        then:
        ItemViewPanelPage itemView = new ItemViewPanelPage( getTestSession() )
        itemView.getTitle().equals( contentName );

    }

    //TODO add test for open content with Context menu

//	def""()
//	{
//		
//		ItemViewPanelPage contentInfoPage = cManagerService.doOpenContentUseContextMenu(getTestSession(), content);
//		ItemViewPanelPage itemView = new ItemViewPanelPage(getTestSession())
//		itemView.getTitle().equals(contentName);
//	}
}