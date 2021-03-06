package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
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

    def "GIVEN existing parent folder with child WHEN parent content selected but 'Include child' checkbox unchecked and 'Publish' button on toolbar pressed THEN parent content has 'Online' status but child should not be published"()
    {
        setup: "parent folder has been added"
        parentContent = buildFolderContent( "publish", "parent-folder" );
        addReadyContent( parentContent );

        and: "one child content has been added"
        findAndSelectContent( parentContent.getName() );
        childContent1 = buildFolderContentWithParent( "publish", "child-folder1", parentContent.getName() );
        addReadyContent( childContent1 );

        when: "'publish' dialog is opened and parent content has been published without its child"
        contentBrowsePanel.clickToolbarPublish().includeChildren( false ).clickOnPublishNowButton(); ;
        String message = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );

        then: "child content should be with 'New' status"
        filterPanel.typeSearchText( childContent1.getName() );
        contentBrowsePanel.getContentStatus( childContent1.getName() ).equalsIgnoreCase( ContentStatus.NEW.getValue() );

        and: "expected notification message should be displayed"
        message == String.format( Application.ITEM_IS_PUBLISHED_NOTIFICATION_MESSAGE, parentContent.getName() );
    }

    def "GIVEN existing 'Published' folder with not published child WHEN the parent folder has been clicked THEN Publish-menu becomes available"()
    {
        given: "existing 'Published' folder with not published child"
        filterPanel.typeSearchText( parentContent.getName() );

        when: "the parent folder has been selected"
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getName() )

        then: "'Publish' button on the toolbar should be disabled"
        !contentBrowsePanel.isPublishButtonEnabled();

        and: "Publish-menu becomes available"
        contentBrowsePanel.isPublishMenuAvailable();
    }

    def "GIVEN parent 'Published' folder with one 'New' child WHEN 'publish' menu expanded AND 'Publish Tree' selected THEN 'Publish Dialog' appears and correct name of child should be displayed"()
    {
        given: "parent folder with a not published child"
        findAndSelectContent( parentContent.getName() );

        when: "modal dialog has been opened"
        ContentPublishDialog modalDialog = contentBrowsePanel.selectPublishTreeMenuItem();
        modalDialog.clickOnShowDependentItemsLink();
        List<String> dependantList = modalDialog.getDependantList();

        then: "Dependants should be displayed"
        modalDialog.isDependantsDisplayed();

        and: "one content present in the dependant list"
        dependantList.size() == 1;

        and: "correct name of content displayed in the dependant list"
        dependantList.get( 0 ).contains( childContent1.getName() );
    }

    def "GIVEN parent 'Published' folder with one 'New'-child WHEN 'Publish Tree' in the context menu selected THEN child content should be 'Published' now"()
    {
        given: "parent folder with a not published child"
        findAndSelectContent( parentContent.getName() );

        when: "'publish tree' menu item is selected"
        ContentPublishDialog modalDialog = contentBrowsePanel.selectPublishTreeMenuItem();

        and: "'publish' button on modal dialog has been clicked"
        modalDialog.clickOnPublishNowButton();

        then: "child content should be 'online' now"
        filterPanel.typeSearchText( childContent1.getName() );
        contentBrowsePanel.getContentStatus( childContent1.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );
    }

    def "GIVEN existing published parent folder with a child WHEN one more child content has been added to the parent THEN the child content should be 'New'"()
    {
        given: "add one more content is added to the published folder"
        findAndSelectContent( parentContent.getName() );
        childContent2 = buildFolderContentWithParent( "publish", "child-folder2", parentContent.getName() );
        addContent( childContent2 );

        when: "the name of the child content is typed"
        filterPanel.typeSearchText( childContent2.getName() );

        then: "new added content should be with 'New' status"
        contentBrowsePanel.getContentStatus( childContent2.getName() ).equalsIgnoreCase( ContentStatus.NEW.getValue() );
    }

    def "GIVEN existing 'Published' parent folder and child content is 'New' WHEN the parent folder has been selected and 'Delete' button pressed THEN 'New' child content should be removed, but parent folder and 'Published' child content should be 'Deleted' "()
    {
        when: "parent 'Published'-folder is selected and 'Delete' button pressed AND number of contents has been typed"
        findAndSelectContent( parentContent.getName() ).clickToolbarDelete().clickOnMarkAsDeletedMenuItemAndConfirm( "3" );
        String message = contentBrowsePanel.waitForNotificationMessage();
        sleep( 1000 );

        and: "parent folder has been expanded"
        contentBrowsePanel.expandItem( parentContent.getPath().toString() );
        saveScreenshot( "parent_child_are_deleted" );

        then: "published parent content becomes 'deleted'"
        contentBrowsePanel.getContentStatus( parentContent.getName() ).equalsIgnoreCase( ContentStatus.MARKED_FOR_DELETION.getValue() );

        and: "published child content becomes 'deleted' as well"
        contentBrowsePanel.getContentStatus( childContent1.getName() ).equalsIgnoreCase( ContentStatus.MARKED_FOR_DELETION.getValue() );

        and: "'New' child content should be removed"
        !contentBrowsePanel.exists( childContent2.getName() );

        and: "expected notification message should appear"
        message == "3 items are deleted ( 2 are marked for deletion ).";
    }

    def "GIVEN existing parent folder with a child and status of both contents are 'DELETED' WHEN parent folder has been published THEN parent folder should be removed"()
    {
        when: "'deleted' parent folder has been published"
        findAndSelectContent( parentContent.getName() ).clickToolbarPublish().clickOnPublishNowButton();
        String message = contentBrowsePanel.waitForNotificationMessage();

        then: "the folder should be removed"
        !contentBrowsePanel.exists( parentContent.getName() );

        and: "expected notification message should be displayed"
        message == "2 items are published ( 2 deleted ).";
    }
}
