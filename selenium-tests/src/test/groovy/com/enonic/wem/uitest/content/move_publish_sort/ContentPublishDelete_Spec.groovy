package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentPublishDelete_Spec
    extends BaseContentSpec

{
    @Shared
    String DISPLAY_NAME = "publishDisplayName";

    @Shared
    Content content;

    def "GIVEN existing root content WHEN content selected and 'Publish' button on toolbar pressed THEN notification message appears and  content have got a 'Online' status"()
    {
        given:
        content = buildFolderContent( "publish", DISPLAY_NAME );
        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close(
            content.getDisplayName() );
        when:
        filterPanel.typeSearchText( content.getName() )
        String message = contentBrowsePanel.selectContentInTable(
            content.getName() ).clickToolbarPublish().clickOnPublishNowButton().waitPublishNotificationMessage(
            Application.EXPLICIT_NORMAL );

        then:
        filterPanel.typeSearchText( content.getName() )
        contentBrowsePanel.getContentStatus( content.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() );
        message == String.format( Application.CONTENT_PUBLISHED_NOTIFICATION_MESSAGE, DISPLAY_NAME );

    }

    def "GIVEN existing root content with 'Online' status  WHEN content selected and 'Delete' button pressed THEN  content with a 'Pending delete' status present"()
    {
        given:
        filterPanel.typeSearchText( content.getName() )

        when:
        contentBrowsePanel.selectContentInTable( content.getName() ).clickToolbarDelete().doDelete();
        String message = contentBrowsePanel.waitNotificationMessage();

        then:
        contentBrowsePanel.getContentStatus( content.getName() ).equalsIgnoreCase( ContentStatus.PENDING_DELETE.getValue() );
        and:
        message == String.format( Application.ONLINE_DELETED_MESSAGE, DISPLAY_NAME );

    }

    def "GIVEN existing root content with 'Pending Delete' status  WHEN content selected and 'Publish' button pressed THEN content not listed in browse panel "()
    {
        when:
        filterPanel.typeSearchText( content.getName() )
        String message = contentBrowsePanel.selectContentInTable(
            content.getName() ).clickToolbarPublish().clickOnPublishNowButton().waitPublishNotificationMessage(
            Application.EXPLICIT_NORMAL );
        filterPanel.typeSearchText( content.getName() );

        then:
        !contentBrowsePanel.exists( content.getName() );
        and:
        message == String.format( Application.DELETE_PENDING_MESSAGE, DISPLAY_NAME );
    }
}
