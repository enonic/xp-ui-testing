package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentUnpublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

/**
 * Created on 24.10.2016.
 *
 * XP-4314 Up-to-date selenium tests for notification messages
 * */
class Delete_Published_Parent_Folder_Spec
    extends BaseContentSpec
{
    @Shared
    Content PARENT_FOLDER;

    def "GIVEN existing root folder with a child WHEN parent content selected and 'Publish' button on toolbar pressed THEN notification message appears and content have got 'Online' status"()
    {
        given:
        PARENT_FOLDER = buildFolderContent( "publish", "parent folder" );
        addContent( PARENT_FOLDER );
        findAndSelectContent( PARENT_FOLDER.getName() );
        Content child = buildFolderContent( "child", "child folder" );
        addContent( child );

        when:
        findAndSelectContent( PARENT_FOLDER.getName() ).clickToolbarPublish().setIncludeChildCheckbox( true ).clickOnPublishNowButton();
        String message = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        saveScreenshot( "parent_with_child_published" );

        then:
        contentBrowsePanel.getContentStatus( PARENT_FOLDER.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() );

        and: "correct notification message is displayed"
        message == String.format( Application.CONTENTS_PUBLISHED_NOTIFICATION_MESSAGE, "2" );
    }

    def "GIVEN existing online-parent folder with a child WHEN parent folder was selected AND 'Unpublish' menu item was selected THEN correct notification message appears "()
    {
        given:
        findAndSelectContent( PARENT_FOLDER.getName() );

        when: "'Unpublish' menu item was selected"
        ContentUnpublishDialog modalDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();
        modalDialog.clickOnUnpublishButton();
        String message = contentBrowsePanel.waitNotificationMessage( Application.EXPLICIT_NORMAL );

        then: "correct notification message appears"
        message == String.format( Application.CONTENTS_UNPUBLISHED_NOTIFICATION_MESSAGE, "2" );
    }

    def "GIVEN existing online-folder with a child WHEN the folder selected AND 'Unpublish menu item clicked THEN correct notification message is displayed'"()
    {
        given: "existing online-folder with a child"
        findAndSelectContent( PARENT_FOLDER.getName() ).clickToolbarPublish().setIncludeChildCheckbox( true ).clickOnPublishNowButton();
        sleep( 1000 );

        when: "the folder selected AND 'Unpublish menu item clicked"
        contentBrowsePanel.clickToolbarDelete().doDelete();
        def expectedMessage = String.format( Application.CONTENTS_MARKED_FOR_DELETION_MESSAGE, "2" );

        then: "correct notification message is displayed'"
        contentBrowsePanel.waitExpectedNotificationMessage( expectedMessage, Application.EXPLICIT_NORMAL );
    }

    def "GIVEN existing pending-folder with a child WHEN the folder selected AND 'Unpublish menu item clicked THEN correct notification message is displayed'"()
    {
        given: "existing online-folder with a child"
        findAndSelectContent( PARENT_FOLDER.getName() ).clickToolbarPublish().setIncludeChildCheckbox( true ).clickOnPublishNowButton();
        sleep( 1000 );

        when: "the folder selected AND 'Unpublish menu item clicked"
        contentBrowsePanel.clickToolbarDelete().doDelete();
        def expectedMessage = String.format( Application.CONTENTS_MARKED_FOR_DELETION_MESSAGE, "2" );

        then: "correct notification message is displayed'"
        contentBrowsePanel.waitExpectedNotificationMessage( expectedMessage, Application.EXPLICIT_NORMAL );
    }
}
