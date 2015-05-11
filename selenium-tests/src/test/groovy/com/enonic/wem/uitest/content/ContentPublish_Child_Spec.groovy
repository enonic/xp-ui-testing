package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentPublish_Child_Spec
    extends BaseContentSpec
{

    @Shared
    Content parentContent;

    @Shared
    Content childContent1;

    @Shared
    Content childContent2;

    def "GIVEN existing parent folder with child WHEN parent content selected and 'Publish' button on toolbar pressed THEN child content has 'Online' status as well"()
    {
        setup:
        parentContent = buildFolderContent( "publish", "parent-folder" );
        addContent( parentContent );

        and:
        filterPanel.typeSearchText( parentContent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getPath() )
        childContent1 = buildFolderContent( "publish", "child-folder1", parentContent.getName() );
        addContent( childContent1 );


        when:
        contentBrowsePanel.clickOnClearSelection();
        filterPanel.typeSearchText( parentContent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getPath() ).clickToolbarPublish();

        then:
        filterPanel.typeSearchText( childContent1.getName() );
        contentBrowsePanel.getContentStatus( childContent1.getPath() ) == ContentStatus.ONLINE.getValue();
    }

    def "GIVEN existing published parent folder with child WHEN one more child content added into a folder  THEN just added child content has a 'New' status"()
    {
        setup:
        filterPanel.typeSearchText( parentContent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getPath() )
        childContent2 = buildFolderContent( "publish", "child-folder2", parentContent.getName() );
        addContent( childContent2 );

        when:
        contentBrowsePanel.clickOnClearSelection();
        filterPanel.typeSearchText( childContent2.getName() );

        then:
        contentBrowsePanel.getContentStatus( childContent2.getPath() ) == ContentStatus.NEW.getValue();
    }

    def "GIVEN existing published parent folder with one published child and one 'new' content WHEN  parent folder selected and 'Delete' button pressed  THEN parent folder and one child content have a 'Pending delete' status"()
    {
        when:
        filterPanel.typeSearchText( parentContent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getPath() ).clickToolbarDelete().doDelete();
        contentBrowsePanel.expandItem( parentContent.getPath().toString() );

        then:
        contentBrowsePanel.getContentStatus( parentContent.getPath() ) == ContentStatus.PENDING_DELETE.getValue();
        and:
        contentBrowsePanel.getContentStatus( childContent1.getPath() ) == ContentStatus.PENDING_DELETE.getValue();
        and:
        !contentBrowsePanel.exists( childContent2.getPath() );
    }

    def "GIVEN existing  parent folder with one child and status of both contents are 'PENDING_DELETE'  WHEN  parent folder selected and 'Publish' button pressed  THEN parent folder not listed"()
    {
        when:
        filterPanel.typeSearchText( parentContent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getPath() ).clickToolbarPublish();

        then:
        !contentBrowsePanel.exists( parentContent.getPath() );
    }

    private void addContent( Content content )
    {
        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close(
            content.getDisplayName() );
    }


    public Content buildFolderContent( String name, String displayName, String parentName )
    {
        String generated = NameHelper.uniqueName( name );
        Content content = Content.builder().
            name( generated ).
            displayName( displayName ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.from( parentName ) ).
            build();
        return content;
    }

}
