package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

class Content_Online_Modified_Spec
    extends BaseContentSpec
{
    @Shared
    String NEW_DISPLAY_NAME = "newDisplayName";

    @Shared
    Content CONTENT;

    def "GIVEN Ready for publishing folder is added WHEN the folder has been selected and 'Publish' button on toolbar pressed THEN expected notification message should appear and content gets 'Published'"()
    {
        given: "new folder id added"
        CONTENT = buildFolderContent( "publish", "folder-content" );
        addReadyContent( CONTENT );

        when: "the content has been published"
        findAndSelectContent( CONTENT.getName() ).clickToolbarPublish().clickOnPublishButton();
        String message = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );

        then: "notification message should appear and content is getting 'Online'"
        contentBrowsePanel.getContentStatus( CONTENT.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );

        and: "correct notification message should be displayed"
        message == String.format( Application.ITEM_IS_PUBLISHED_NOTIFICATION_MESSAGE, CONTENT.getName() );
    }

    def "GIVEN existing 'Published'-folder WHEN the folder has been updated THEN content becomes 'Modified' in the grid"()
    {
        given: "existing root content with 'PUBLISHED' status is opened"
        ContentWizardPanel wizard = findAndSelectContent( CONTENT.getName() ).clickToolbarEditAndSwitchToWizardTab(); ;

        when: "display name has been updated"
        wizard.typeDisplayName( NEW_DISPLAY_NAME ).save().closeBrowserTab().switchToBrowsePanelTab();

        then: "content becomes 'Modified' in the BrowsePanel"
        contentBrowsePanel.getContentStatus( CONTENT.getName() ).equalsIgnoreCase( ContentStatus.MODIFIED.getValue() );
    }

    def "GIVEN existing 'Modified'-folder WHEN the content has been selected and published THEN folder gets 'Published'"()
    {
        when: "modified content has been published"
        findAndSelectContent( CONTENT.getName() ).showPublishMenu().clickOnMarkAsReadyMenuItem();
        contentBrowsePanel.clickToolbarPublish().clickOnPublishButton();
        String message = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );

        then: "content-status is getting 'Published'"
        contentBrowsePanel.getContentStatus( CONTENT.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );

        and: "expected notification message should appear"
        message == String.format( Application.ITEM_IS_PUBLISHED_NOTIFICATION_MESSAGE, CONTENT.getName() );

        and: "Publish button on the BrowsePanel-toolbar becomes disabled"
        !contentBrowsePanel.isPublishButtonEnabled();

        and: "Publish-menu on the BrowsePanel-toolbar should be enabled"
        contentBrowsePanel.isPublishMenuAvailable();
    }
}
