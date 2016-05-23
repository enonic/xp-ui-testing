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
    Content content;

    def "GIVEN existing root content WHEN content selected and 'Publish' button on toolbar pressed THEN notification message appears and  content have got a 'Online' status"()
    {
        given: "existing content in root"
        content = buildFolderContent( "publish", "folder-content" );
        addContent( content );

        when: "the content have been published"
        String message = findAndSelectContent( content.getName() ).
            clickToolbarPublish().clickOnPublishNowButton().waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );

        then: "status of content is 'online'"
        contentBrowsePanel.getContentStatus( content.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() );

        and: "correct notification message appears"
        message == String.format( Application.CONTENT_PUBLISHED_NOTIFICATION_MESSAGE, content.getDisplayName() );
    }

    def "GIVEN existing root content with 'Online' status  WHEN content edited THEN  content has got a 'Modified' status"()
    {
        given: "existing root content with 'Online' status opened for edit"
        ContentWizardPanel wizard = findAndSelectContent( content.getName() ).clickToolbarEdit(); ;

        when: "new display name typed"
        wizard.typeDisplayName( NEW_DISPLAY_NAME ).save().close( NEW_DISPLAY_NAME );

        then: "status of content is 'modified'"
        contentBrowsePanel.getContentStatus( content.getName() ).equalsIgnoreCase( ContentStatus.MODIFIED.getValue() );

    }

    def "GIVEN existing root content with 'Modified' status  WHEN content selected and 'Publish' button pressed THEN content has got a 'Online' status"()
    {
        when: "modified content has been published again"
        findAndSelectContent( content.getName() ).
            clickToolbarPublish().clickOnPublishNowButton();
        String message = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );

        then: "status of content is 'online'"
        contentBrowsePanel.getContentStatus( content.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() );

        and: "correct notification message appears"
        message == String.format( Application.CONTENT_PUBLISHED_NOTIFICATION_MESSAGE, NEW_DISPLAY_NAME );

    }

}
