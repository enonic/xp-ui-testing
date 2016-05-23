package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentUnpublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.utils.TestUtils
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

    def "GIVEN existing not published root content WHEN content selected THEN 'Publish'-menu is disabled"()
    {
        given: "existing content in root"
        FOLDER_CONTENT = buildFolderContent( "publish", "publish menu test" );
        addContent( FOLDER_CONTENT );

        when: "the content selected"
        findAndSelectContent( FOLDER_CONTENT.getName() );

        then: "and 'Publish'-menu is disabled"
        !contentBrowsePanel.isPublishMenuAvailable();
    }

    def "GIVEN existing not published root folder WHEN the folder has been published THEN 'Publish'-menu is available AND 'Unpablish' menu item enabled"()
    {
        when: "the folder has been published"
        findAndSelectContent( FOLDER_CONTENT.getName() ).clickToolbarPublish().clickOnPublishNowButton();

        then: " 'Publish'-menu is available "
        contentBrowsePanel.isPublishMenuAvailable();

        and: "AND 'Unpablish' menu-item is enabled"
        contentBrowsePanel.showPublishMenu();
        TestUtils.saveScreenshot( getSession(), "publish_menu_online_content" );
        contentBrowsePanel.isUnPublishMenuItemEnabled();
    }

    def "GIVEN existing published folder WHEN the folder selected AND 'Unpublish' clicked in the menu THEN 'the folder becomes is 'offline' "()
    {
        given: "existing published folder"
        findAndSelectContent( FOLDER_CONTENT.getName() )

        when: "the folder selected AND 'Unpublish' clicked"
        ContentUnpublishDialog modalDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();

        and: "'unpublish' button on the modal dialog pressed"
        modalDialog.clickOnUnpublishButton();
        String message = contentBrowsePanel.waitNotificationMessage( Application.EXPLICIT_NORMAL );

        then: "content has 'offline' status"
        TestUtils.saveScreenshot( getSession(), "content_unpublished" );
        contentBrowsePanel.getContentStatus( FOLDER_CONTENT.getName() ) == ContentStatus.OFFLINE.getValue();

        and: "correct notification message appears"
        message == String.format( Application.UNPUBLISHED_NOTIFICATION_MESSAGE, FOLDER_CONTENT.getDisplayName() );

        and: "'Publish' on the toolbar is enabled now"
        contentBrowsePanel.isPublishButtonEnabled();
    }
}
