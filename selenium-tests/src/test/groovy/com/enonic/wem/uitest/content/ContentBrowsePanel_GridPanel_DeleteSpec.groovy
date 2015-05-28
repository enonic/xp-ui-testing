package com.enonic.wem.uitest.content

import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_GridPanel_DeleteSpec
    extends BaseContentSpec
{

    def "GIVEN existing two contents, WHEN all content selected and delete button pressed THEN the content should not be listed in the table"()
    {
        given:
        Content content1 = buildFolderContent( "deletecontent", "contenttodelete" );
        addContent( content1 );

        Content content2 = buildFolderContent( "deletecontent", "contenttodelete" );
        addContent( content2 );

        List<Content> contentList = new ArrayList<>();
        contentList.add( content1 );
        contentList.add( content2 );

        when:
        contentBrowsePanel.selectContentInTable( contentList ).clickToolbarDelete().doDelete();

        then:
        !contentBrowsePanel.exists( content1.getPath() ) && !contentBrowsePanel.exists( content2.getPath() );
    }

    def "GIVEN a Content on root WHEN deleted THEN deleted content is no longer listed at root"()
    {
        given: "folder content added on the root"
        Content content = buildFolderContent( "deletecontent", "deletecontent" );
        addContent( content );

        when: "just created content selected and 'Delete' button on toolbar  pressed and 'Yes' pressed on confirm dialog "
        contentBrowsePanel.clickCheckboxAndSelectRow( content.getPath() ).clickToolbarDelete().doDelete();

        then: "deleted content is no longer listed at root"
        !contentBrowsePanel.exists( content.getPath() );
    }

    def "GIVEN a Content beneath an existing WHEN deleted THEN deleted Content is no longer listed beneath parent"()
    {
        given: "folder content added at the root and added child content to this folder"
        Content parent = buildFolderContent( "parent", "parent" );
        addContent( parent );

        contentBrowsePanel.clickCheckboxAndSelectRow( parent.getPath() );
        Content contentToDelete = buildFolderContent( "folder", "delete content beneath parent" );
        addContent( contentToDelete );
        List<Content> contentList = new ArrayList<>()
        contentList.add( contentToDelete );

        when: "parent folder expanded and child content selected and 'Delete' button on toolbar pressed"
        contentBrowsePanel.expandContent( parent.getPath() ).selectContentInTable( contentList ).clickToolbarDelete().doDelete();

        then: "deleted Content is no longer listed beneath parent"
        !contentBrowsePanel.exists( contentToDelete.getPath(), true );
    }


    def "GIVEN a one and only Content beneath an existing WHEN deleted THEN expand icon of parent is no longer shown "()
    {
        given: "folder content added at the root and added a child content to this folder"
        Content parent = buildFolderContent( "parent", "expand-icon-test" );
        addContent( parent );

        contentBrowsePanel.clickCheckboxAndSelectRow( parent.getPath() );
        Content content = Content.builder().
            name( NameHelper.uniqueName( "unstructured" ) ).
            displayName( "unstructured" ).
            contentType( ContentTypeName.unstructured() ).
            parent( ContentPath.from( parent.getName() ) ).
            build();
        addContent( content );
        List<Content> contentList = new ArrayList<>();
        contentList.add( content );

        when: "child content deleted"
        contentBrowsePanel.expandContent( parent.getPath() );
        TestUtils.saveScreenshot( getTestSession(), "deletecontentbeneath" )
        contentBrowsePanel.selectContentInTable( contentList ).clickToolbarDelete().doDelete();

        then: "expand icon of parent is no longer shown"
        !contentBrowsePanel.isExpanderPresent( ContentPath.from( parent.getName() ) );
    }

    def "GIVEN a Content on root WHEN deleted THEN New-button is enabled"()
    {
        given: "folder content added at the root"
        Content folder = buildFolderContent( "folder", "folder-to-delete" );
        addContent( folder );

        when: "just created content deleted"
        contentBrowsePanel.selectContentInTable( folder.getPath() ).clickToolbarDelete().doDelete();

        then: "New-button is enabled"
        contentBrowsePanel.isNewButtonEnabled();
    }

    def "GIVEN two Content on root WHEN both deleted THEN New-button is enabled"()
    {
        given: "two folder added at the root"
        Content content1 = buildFolderContent( "folder", "folder-to-delete1" );
        addContent( content1 );

        Content content2 = buildFolderContent( "folder", "folder-to-delete2" );
        addContent( content2 );

        List<Content> contentList = new ArrayList<>();
        contentList.add( content2 );
        contentList.add( content1 );

        when: "both contents selected in the grid and  deleted"
        contentBrowsePanel.selectContentInTable( contentList ).clickToolbarDelete().doDelete();

        then: "New-button is enabled"
        contentBrowsePanel.isNewButtonEnabled();
    }
}
