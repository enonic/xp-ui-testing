package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ConfirmValueDialog
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.ContentUnpublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Ignore
import spock.lang.Shared

/**
 * Created on 24.10.2016.
 * */
class Delete_Published_Parent_Folder_Spec
    extends BaseContentSpec
{
    @Shared
    Content PARENT_FOLDER;

    def "GIVEN existing Ready to publish folder with a child WHEN parent content has been selected and 'Publish' button pressed THEN '2 items are published.' and content get 'Published'"()
    {
        given:
        PARENT_FOLDER = buildFolderContent( "publish", "parent folder" );
        addReadyContent( PARENT_FOLDER );
        findAndSelectContent( PARENT_FOLDER.getName() );
        Content child = buildFolderContent( "child", "child folder" );
        addReadyContent( child );

        when: "Include child has been checked"
        findAndSelectContent( PARENT_FOLDER.getName() ).clickToolbarPublish().includeChildren( true ).clickOnPublishNowButton();
        String message = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        saveScreenshot( "parent_with_child_published" );

        then:
        contentBrowsePanel.getContentStatus( PARENT_FOLDER.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );

        and: "expected notification message should be displayed"
        message == String.format( Application.CONTENTS_PUBLISHED_NOTIFICATION_MESSAGE, "2" );
    }

    def "GIVEN existing 'Published'-parent folder with a child WHEN parent folder has been selected AND 'Unpublish' menu item has been clicked THEN expected notification message appears"()
    {
        given:
        findAndSelectContent( PARENT_FOLDER.getName() );

        when: "'Unpublish' menu item has been selected"
        ContentUnpublishDialog modalDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();
        modalDialog.clickOnUnpublishButton();
        ConfirmValueDialog confirmValueDialog = new ConfirmValueDialog( getSession() );
        and:
        confirmValueDialog.typeNumber( "2" ).clickOnConfirmButton();
        String message = contentBrowsePanel.waitForNotificationMessage();

        then: "expected notification message appears"
        message == String.format( Application.CONTENTS_UNPUBLISHED_NOTIFICATION_MESSAGE, "2" );
    }

    def "GIVEN existing 'Unpublished'-folder has been published WHEN the folder has been selected AND 'Archive...' AND  button has been clicked THEN expected notification message is displayed'"()
    {
        given: "existing Unpublished-folder has been published"
        findAndSelectContent( PARENT_FOLDER.getName() ).clickToolbarPublish().includeChildren( true ).clickOnPublishNowButton();
        sleep( 1000 );

        when: "the folder has been selected AND 'Delete' button clicked"
        contentBrowsePanel.clickToolbarArchive().clickOnDeleteMenuItemAndConfirm( "2" );
        def expectedMessage = "2 items are deleted."

        then: "2 items are deleted. - message should appear"
        contentBrowsePanel.waitExpectedNotificationMessage( expectedMessage, Application.EXPLICIT_NORMAL );
    }

}
