package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.schema.content.ContentTypeName

class ContentBrowsePanel_ItemsSelectionPanel_DeleteSpec
    extends BaseContentSpec
{

    def "GIVEN three selected Content WHEN deleted THEN no SelectionItem-s are displayed"()
    {
        given: "one parent and two child content exist"
        Content parent = buildFolderContent( "folder", "selection test" )
        addContent( parent );
        List<Content> contentList = new ArrayList<>()
        Content content1 = Content.builder().
            name( NameHelper.uniqueName( "first" ) ).
            displayName( "first" ).
            parent( ContentPath.from( parent.getName() ) ).
            contentType( ContentTypeName.shortcut() ).
            build();

        ContentWizardPanel wizard = contentBrowsePanel.clickOnParentCheckbox( parent.getPath() ).clickToolbarNew().selectContentType(
            content1.getContentTypeName() );
        wizard.typeData( content1 ).save().close( content1.getDisplayName() );
        contentList.add( content1 );
        Content content2 = Content.builder().
            parent( ContentPath.from( parent.getName() ) ).
            name( NameHelper.uniqueName( "second" ) ).
            displayName( "second" ).
            contentType( ContentTypeName.shortcut() ).
            build();

        addContent( content2 );
        contentList.add( content2 );
        Content content3 = Content.builder().
            parent( ContentPath.from( parent.getName() ) ).
            name( NameHelper.uniqueName( "third" ) ).
            displayName( "third" ).
            contentType( ContentTypeName.shortcut() ).
            build();

        contentBrowsePanel.clickToolbarNew().selectContentType( content3.getContentTypeName() );
        wizard.typeData( content3 ).save().close( content3.getDisplayName() );
        contentList.add( content3 );

        when: "parent and children are selected and 'Delete button' pressed"
        contentBrowsePanel.expandContent( content1.getParent() );
        TestUtils.saveScreenshot( getTestSession(), "delete-three" );
        contentBrowsePanel.selectContentInTable( contentList ).clickToolbarDelete().doDelete();

        then: "no SelectionItem-s are displayed"
        TestUtils.saveScreenshot( getSession(), "item_select_0" )
        itemsSelectionPanel.getSelectedItemCount() == 0;
    }
}
