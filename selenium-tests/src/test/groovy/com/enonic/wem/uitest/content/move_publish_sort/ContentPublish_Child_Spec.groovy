package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * verifies the "XP-4628 Add correct notification messages, when a parent folder with children has been removed or published"  will be fixed
 *
 * */
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
        setup: "parent folder added"
        parentContent = buildFolderContent( "publish", "parent-folder" );
        addContent( parentContent );

        and: "one child content added"
        findAndSelectContent( parentContent.getName() );
        childContent1 = buildFolderContentWithParent( "publish", "child-folder1", parentContent.getName() );
        addContent( childContent1 );

        when: " 'publish' dialog opened and parent content has been published without a child "
        contentBrowsePanel.clickToolbarPublish().includeChildren( false ).clickOnPublishNowButton(); ;
        String message = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );

        then: "child content has 'offline' status"
        filterPanel.typeSearchText( childContent1.getName() );
        contentBrowsePanel.getContentStatus( childContent1.getName() ).equalsIgnoreCase( ContentStatus.OFFLINE.getValue() );

        and: "correct notification message is displayed"
        message == String.format( Application.ONE_CONTENT_PUBLISHED_NOTIFICATION_MESSAGE, parentContent.getDisplayName() );
    }

    def "GIVEN parent 'online' folder with not published child WHEN the parent folder is selected THEN Publish-menu becomes available"()
    {
        given: "parent folder with a not published child"
        filterPanel.typeSearchText( parentContent.getName() );

        when: "the parent folder is selected"
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getName() )

        then: "'Publish' button on the toolbar is disabled"
        !contentBrowsePanel.isPublishButtonEnabled();

        and: "Publish-menu becomes available"
        contentBrowsePanel.isPublishMenuAvailable();
    }

    def "GIVEN parent 'online' folder with not published child WHEN 'publish' menu expanded AND 'Publish Tree' selected THEN 'Publish Dialog' appears and correct name of child is displayed"()
    {
        given: "parent folder with a not published child"
        findAndSelectContent( parentContent.getName() );

        when: "modal dialog has been opened"
        ContentPublishDialog modalDialog = contentBrowsePanel.selectPublishTreeMenuItem();
        List<String> dependantList = modalDialog.getDependantList();

        then: "Dependants should be displayed"
        modalDialog.isDependantsDisplayed();

        and: "one content present in the dependant list"
        dependantList.size() == 1;

        and: "correct name of content displayed in the dependant list"
        dependantList.get( 0 ).contains( childContent1.getName() );
    }

    def "GIVEN parent 'online' folder with not published child WHEN 'Publish Tree' in the context menu selected THEN child content is 'online' now"()
    {
        given: "parent folder with a not published child"
        findAndSelectContent( parentContent.getName() );

        when: "'publish tree' menu item selected"
        ContentPublishDialog modalDialog = contentBrowsePanel.selectPublishTreeMenuItem();

        and: "'publish' button on modal dialog clicked"
        modalDialog.clickOnPublishNowButton();

        then: "child content is 'online' now"
        filterPanel.typeSearchText( childContent1.getName() );
        contentBrowsePanel.getContentStatus( childContent1.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() );
    }

    def "GIVEN existing published parent folder with child WHEN one more child content added into a folder  THEN the child content has a 'Offline' status"()
    {
        given: "add one more content added for the published folder"
        findAndSelectContent( parentContent.getName() );
        childContent2 = buildFolderContentWithParent( "publish", "child-folder2", parentContent.getName() );
        addContent( childContent2 );

        when: "the child content filtered"
        filterPanel.typeSearchText( childContent2.getName() );

        then: "new added content has a 'Offline' status"
        contentBrowsePanel.getContentStatus( childContent2.getName() ).equalsIgnoreCase( ContentStatus.OFFLINE.getValue() );
    }
    //verifies XP-4628
    def "GIVEN existing parent has 'online' and child contents is 'offline' WHEN parent folder selected and 'Delete' button pressed  THEN 'offline' child content removed, but parent folder and 'online' child content are 'Pending delete' "()
    {
        when: "parent 'online'-folder selected and 'Delete' button pressed"
        findAndSelectContent( parentContent.getName() ).clickToolbarDelete().doDelete();
        String message = contentBrowsePanel.waitNotificationMessage()

        and: "parent folder has been expanded"
        contentBrowsePanel.expandItem( parentContent.getPath().toString() );
        saveScreenshot( "parent_child_are_pending" );

        then: "online-parent becomes 'pending-delete'"
        contentBrowsePanel.getContentStatus( parentContent.getName() ).equalsIgnoreCase( ContentStatus.PENDING_DELETE.getValue() );

        and: "online-child becomes 'pending-delete'"
        contentBrowsePanel.getContentStatus( childContent1.getName() ).equalsIgnoreCase( ContentStatus.PENDING_DELETE.getValue() );

        and: "offline-child has been removed"
        !contentBrowsePanel.exists( childContent2.getName() );

        and: "correct notification message is displayed"
        message == "1 item is deleted. 2 items are marked for deletion.";
    }
    //verifies the "XP-4628 Add correct notification messages, when a parent folder with children has been removed or published"  will be fixed
    def "GIVEN existing parent folder with one child and status of both contents are 'PENDING_DELETE' WHEN parent folder selected and 'Publish' button pressed THEN parent folder not listed"()
    {
        when:
        findAndSelectContent( parentContent.getName() ).clickToolbarPublish().clickOnPublishNowButton();
        String message = contentBrowsePanel.waitNotificationMessage();

        then: "parent folder not listed"
        !contentBrowsePanel.exists( parentContent.getName() );

        and: "correct notification message is displayed"
        message == String.format( Application.PENDING_ITEMS_ARE_DELETED, "2" );
    }

}
