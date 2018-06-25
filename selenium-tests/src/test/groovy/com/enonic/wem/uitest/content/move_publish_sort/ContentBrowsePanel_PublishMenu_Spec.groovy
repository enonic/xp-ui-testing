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

    def "GIVEN existing 'New' root folder WHEN folder is selected THEN 'Publish'-menu should be enabled AND Publish button should be enabled"()
    {
        given: "existing content in root"
        FOLDER_CONTENT = buildFolderContent( "publish", "publish menu test" );
        addContent( FOLDER_CONTENT );

        when: "the content selected"
        findAndSelectContent( FOLDER_CONTENT.getName() );
        saveScreenshot( "test_publish_menu_offline_content" );

        then: "and 'Publish'-menu(drop down handler) should be enabled, because 'create issue' menu item should be available"
        contentBrowsePanel.isPublishMenuAvailable();

        and: "Publish button should be enabled"
        contentBrowsePanel.isPublishButtonEnabled();
    }

    def "GIVEN existing 'New' folder in the root WHEN the folder has been published THEN 'Publish'-menu is available AND 'Unpablish' menu item enabled"()
    {
        when: "the folder has been published"
        findAndSelectContent( FOLDER_CONTENT.getName() ).clickToolbarPublish().clickOnPublishNowButton();

        then: "'Publish'-menu should be available"
        contentBrowsePanel.isPublishMenuAvailable();

        and: "AND 'Unpublish' menu-item is enabled"
        contentBrowsePanel.showPublishMenu();
        saveScreenshot( "test_publish_menu_online_content" );
        contentBrowsePanel.isUnPublishMenuItemEnabled();

        and: "'Create Issue' menu item should be present and enabled"
        contentBrowsePanel.isCreateIssueMenuItemEnabled();

        and: "'Publish Tree' menu item should be present and disabled"
        !contentBrowsePanel.isPublishTreeMenuItemEnabled();
    }

    def "GIVEN existing 'online' folder WHEN the folder selected AND 'Unpublish' clicked in the menu THEN 'the folder becomes is 'offline' "()
    {
        given: "existing published folder"
        findAndSelectContent( FOLDER_CONTENT.getName() )

        when: "the folder selected AND 'Unpublish' clicked"
        ContentUnpublishDialog modalDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();

        and: "'unpublish' button on the modal dialog pressed"
        modalDialog.clickOnUnpublishButton();
        String message = contentBrowsePanel.waitForNotificationMessage();

        then: "content has 'Unpublished' status"
        saveScreenshot( "test_content_unpublished" );
        contentBrowsePanel.getContentStatus( FOLDER_CONTENT.getName() ) == ContentStatus.UNPUBLISHED.getValue();

        and: "correct notification message appears"
        message == String.format( Application.ONE_CONTENT_UNPUBLISHED_NOTIFICATION_MESSAGE, FOLDER_CONTENT.getName() );

        and: "'Publish' on the toolbar is enabled now"
        contentBrowsePanel.isPublishButtonEnabled();

        and: "'Publish-menu' on the toolbar should be enabled"
        contentBrowsePanel.isPublishMenuAvailable();
    }
}
