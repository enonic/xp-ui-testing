package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentUnpublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentBrowsePanel_PublishMenu_Spec
    extends BaseContentSpec
{
    @Shared
    Content FOLDER_CONTENT;

    def "GIVEN existing 'offline' root content WHEN content selected THEN 'Publish'-menu is disabled AND Publish button is enabled"()
    {
        given: "existing content in root"
        FOLDER_CONTENT = buildFolderContent( "publish", "publish menu test" );
        addContent( FOLDER_CONTENT );

        when: "the content selected"
        findAndSelectContent( FOLDER_CONTENT.getName() );
        saveScreenshot( "test_publish_menu_offline_content" );

        then: "and 'Publish'-menu is disabled"
        !contentBrowsePanel.isPublishMenuAvailable();

        and: "Publish button is enabled"
        contentBrowsePanel.isPublishButtonEnabled();
    }

    def "GIVEN existing not published root folder WHEN the folder has been published THEN 'Publish'-menu is available AND 'Unpablish' menu item enabled"()
    {
        when: "the folder has been published"
        findAndSelectContent( FOLDER_CONTENT.getName() ).clickToolbarPublish().clickOnPublishNowButton();

        then: "'Publish'-menu is available "
        contentBrowsePanel.isPublishMenuAvailable();

        and: "AND 'Unpublish' menu-item is enabled"
        contentBrowsePanel.showPublishMenu();
        saveScreenshot( "test_publish_menu_online_content" );
        contentBrowsePanel.isUnPublishMenuItemEnabled();
    }

    def "GIVEN existing 'online' folder WHEN the folder selected AND 'Unpublish' clicked in the menu THEN 'the folder becomes is 'offline' "()
    {
        given: "existing published folder"
        findAndSelectContent( FOLDER_CONTENT.getName() )

        when: "the folder selected AND 'Unpublish' clicked"
        ContentUnpublishDialog modalDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();

        and: "'unpublish' button on the modal dialog pressed"
        modalDialog.clickOnUnpublishButton();
        String message = contentBrowsePanel.waitNotificationMessage( Application.EXPLICIT_NORMAL );

        then: "content has 'offline' status"
        saveScreenshot( "test_content_unpublished" );
        contentBrowsePanel.getContentStatus( FOLDER_CONTENT.getName() ) == ContentStatus.OFFLINE.getValue();

        and: "correct notification message appears"
        message == String.format( Application.UNPUBLISHED_NOTIFICATION_MESSAGE, FOLDER_CONTENT.getDisplayName() );

        and: "'Publish' on the toolbar is enabled now"
        contentBrowsePanel.isPublishButtonEnabled();

        and: "'Publish-menu' on the toolbar becomes disabled"
        !contentBrowsePanel.isPublishMenuAvailable();
    }
}
