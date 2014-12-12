package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.browsepanel.ItemsSelectionPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.api.schema.content.ContentTypeName
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class ContentBrowsePanel_ItemsSelectionPanel_DeleteSpec
    extends BaseGebSpec
{
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

    def "GIVEN three selected Content WHEN deleted THEN no SelectionItem-s are displayed"()
    {
        given:

        Content parent = Content.builder().
            parent( ContentPath.ROOT ).
            name( NameHelper.uniqueName( "parent" ) ).
            displayName( "parent" ).
            contentType( ContentTypeName.folder() ).
            build();
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() ).typeData( parent ).save().close();

        List<Content> contentList = new ArrayList<>()
        Content content1 = Content.builder().
            name( NameHelper.uniqueName( "first" ) ).
            displayName( "first" ).
            parent( ContentPath.from( parent.getName() ) ).
            contentType( ContentTypeName.dataMedia() ).
            build();

        ContentWizardPanel wizard = contentBrowsePanel.clickOnParentCheckbox( parent.getPath() ).clickToolbarNew().selectContentType(
            content1.getContentTypeName() );
        wizard.typeData( content1 ).save().close();
        contentList.add( content1 );
        Content content2 = Content.builder().
            parent( ContentPath.from( parent.getName() ) ).
            name( NameHelper.uniqueName( "second" ) ).
            displayName( "second" ).
            contentType( ContentTypeName.dataMedia() ).
            build();

        contentBrowsePanel.clickToolbarNew().selectContentType( content2.getContentTypeName() );
        wizard.typeData( content2 ).save().close();
        contentList.add( content2 );
        Content content3 = Content.builder().
            parent( ContentPath.from( parent.getName() ) ).
            name( NameHelper.uniqueName( "third" ) ).
            displayName( "third" ).
            contentType( ContentTypeName.dataMedia() ).
            build();

        contentBrowsePanel.clickToolbarNew().selectContentType( content3.getContentTypeName() );
        wizard.typeData( content3 ).save().close();
        contentList.add( content3 );



        when:
        contentBrowsePanel.expandContent( content1.getParent() ).selectContentInTable( contentList ).clickToolbarDelete().doDelete();

        then:
        itemsSelectionPanel.getSelectedItemCount() == 0;
    }

}
