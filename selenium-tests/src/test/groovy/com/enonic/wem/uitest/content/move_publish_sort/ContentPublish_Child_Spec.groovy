package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
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

    def "GIVEN existing parent folder with child WHEN parent content selected but 'Include child' checkbox unchecked and 'Publish' button on toolbar pressed THEN parent content has 'Online' status but child not published"()
    {
        setup:
        parentContent = buildFolderContent( "publish", "parent-folder" );
        addContent( parentContent );

        and:
        filterPanel.typeSearchText( parentContent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getName() )
        childContent1 = buildFolderContentWithParent( "publish", "child-folder1", parentContent.getName() );
        addContent( childContent1 );

        when:
        contentBrowsePanel.clickOnClearSelection();
        filterPanel.typeSearchText( parentContent.getName() );
        ContentPublishDialog dialog = contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getName() ).clickToolbarPublish();
        dialog.setIncludeChildCheckbox( false ).clickOnPublishNowButton();

        then:
        filterPanel.typeSearchText( childContent1.getName() );
        contentBrowsePanel.getContentStatus( childContent1.getName() ).equalsIgnoreCase( ContentStatus.OFFLINE.getValue() );
    }

    def "GIVEN parent folder with not published child WHEN publish dialog opened and 'Include child' set to true and 'Publish' pressed  THEN child content has 'Online' status as well"()
    {
        given:
        filterPanel.typeSearchText( parentContent.getName() );
        ContentPublishDialog dialog = contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getName() ).clickToolbarPublish();

        when: "'Include child' set to true and 'Publish now' pressed"
        dialog.setIncludeChildCheckbox( true ).clickOnPublishNowButton();

        then: "child content has 'Online' status as well"
        filterPanel.typeSearchText( childContent1.getName() );
        contentBrowsePanel.getContentStatus( childContent1.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() );
    }

    def "GIVEN parent folder and children are 'published' WHEN publish dialog opened  THEN button 'publish' is disabled"()
    {
        given: "parent folder and children are 'published'"
        filterPanel.typeSearchText( parentContent.getName() );


        when: "publish dialog opened"
        ContentPublishDialog dialog = contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getName() ).clickToolbarPublish();

        then: "button 'publish' is disabled"
        !dialog.isPublishNowButtonEnabled();
    }

    def "GIVEN existing published parent folder with child WHEN one more child content added into a folder  THEN just added child content has a 'Offline' status"()
    {
        setup: "add one more content into the published folder"
        filterPanel.typeSearchText( parentContent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getName() )
        childContent2 = buildFolderContentWithParent( "publish", "child-folder2", parentContent.getName() );
        addContent( childContent2 );

        when:
        contentBrowsePanel.clickOnClearSelection();
        filterPanel.typeSearchText( childContent2.getName() );

        then: "just new added content has a 'Offline' status"
        contentBrowsePanel.getContentStatus( childContent2.getName() ).equalsIgnoreCase( ContentStatus.OFFLINE.getValue() );
    }

    def "GIVEN existing published parent folder with one published child and one 'new' content WHEN  parent folder selected and 'Delete' button pressed  THEN not published child content removed but parent folder and one child content have a 'Pending delete' status"()
    {
        when:
        filterPanel.typeSearchText( parentContent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getName() ).clickToolbarDelete().doDelete();
        contentBrowsePanel.expandItem( parentContent.getPath().toString() );

        then:
        contentBrowsePanel.getContentStatus( parentContent.getName() ).equalsIgnoreCase( ContentStatus.PENDING_DELETE.getValue() );
        and:
        contentBrowsePanel.getContentStatus( childContent1.getName() ).equalsIgnoreCase( ContentStatus.PENDING_DELETE.getValue() );
        and:
        !contentBrowsePanel.exists( childContent2.getName() );
    }

    def "GIVEN existing  parent folder with one child and status of both contents are 'PENDING_DELETE'  WHEN  parent folder selected and 'Publish' button pressed  THEN parent folder not listed"()
    {
        when:
        filterPanel.typeSearchText( parentContent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getName() ).clickToolbarPublish().clickOnPublishNowButton(); ;

        then:
        !contentBrowsePanel.exists( parentContent.getName() );
    }

}
