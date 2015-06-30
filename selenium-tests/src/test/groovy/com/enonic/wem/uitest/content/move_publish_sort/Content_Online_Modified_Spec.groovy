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
        given:
        content = buildFolderContent( "publish", "folder-content" );
        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close(
            content.getDisplayName() );
        when:
        filterPanel.typeSearchText( content.getName() )
        String message = contentBrowsePanel.selectContentInTable(
            content.getName() ).clickToolbarPublish().clickOnPublishNowButton().waitPublishNotificationMessage(
            Application.EXPLICIT_NORMAL );
        then:
        contentBrowsePanel.getContentStatus( content.getName() ) == ContentStatus.ONLINE.getValue();
        and:
        message == String.format( Application.CONTENT_PUBLISHED_NOTIFICATION_MESSAGE, content.getDisplayName() );
    }

    def "GIVEN existing root content with 'Online' status  WHEN content edited THEN  content has got a 'Modified' status"()
    {
        given:
        filterPanel.typeSearchText( content.getName() )
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable( content.getName() ).clickToolbarEdit();
        when:
        wizard.typeDisplayName( NEW_DISPLAY_NAME ).save().close( NEW_DISPLAY_NAME );

        then:
        contentBrowsePanel.getContentStatus( content.getName() ) == ContentStatus.MODIFIED.getValue();

    }

    def "GIVEN existing root content with 'Modified' status  WHEN content selected and 'Publish' button pressed THEN content has got a 'Online' status"()
    {
        when:
        filterPanel.typeSearchText( content.getName() )
        String message = contentBrowsePanel.selectContentInTable(
            content.getName() ).clickToolbarPublish().clickOnPublishNowButton().waitPublishNotificationMessage(
            Application.EXPLICIT_NORMAL );

        then:
        contentBrowsePanel.getContentStatus( content.getName() ) == ContentStatus.ONLINE.getValue();
        and:
        message == String.format( Application.CONTENT_PUBLISHED_NOTIFICATION_MESSAGE, NEW_DISPLAY_NAME );

    }

}
