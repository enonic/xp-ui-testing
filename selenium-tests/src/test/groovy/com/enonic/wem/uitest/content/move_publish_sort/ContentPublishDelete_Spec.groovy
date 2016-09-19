package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog
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

    def "GIVEN existing root content WHEN content selected and 'Publish' button on toolbar pressed THEN notification message appears and content have got 'Online' status"()
    {
        given:
        content = buildFolderContent( "publish", DISPLAY_NAME );
        addContent( content );

        when:
        filterPanel.typeSearchText( content.getName() )
        contentBrowsePanel.selectContentInTable( content.getName() ).clickToolbarPublish().clickOnPublishNowButton();
        String message = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );

        then:
        filterPanel.typeSearchText( content.getName() )
        contentBrowsePanel.getContentStatus( content.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() );
        message == String.format( Application.CONTENT_PUBLISHED_NOTIFICATION_MESSAGE, DISPLAY_NAME );
    }

    def "GIVEN existing root content with 'Online' status WHEN content selected and 'Delete' button pressed THEN content with a 'Pending delete' status present"()
    {
        given:
        filterPanel.typeSearchText( content.getName() )

        when:
        contentBrowsePanel.selectContentInTable( content.getName() ).clickToolbarDelete().doDelete();
        String message = contentBrowsePanel.waitNotificationMessage();

        then:
        contentBrowsePanel.getContentStatus( content.getName() ).equalsIgnoreCase( ContentStatus.PENDING_DELETE.getValue() );
        and:
        message == "1 item was marked for deletion";
    }

    def "GIVEN existing root content with 'Pending Delete' status  WHEN it selected and 'Delete' button pressed THEN checkbox with label 'Instantly delete published items' is checked"()
    {
        when:
        filterPanel.typeSearchText( content.getName() )
        DeleteContentDialog dialog = contentBrowsePanel.selectContentInTable( content.getName() ).clickToolbarDelete();
        saveScreenshot( "test_delete_dialog_checkbox" );

        then:
        dialog.isInstantlyDeleteCheckboxChecked();
    }

    def "GIVEN existing root content with 'Pending Delete' status  WHEN content selected and 'Publish' button pressed THEN content not listed in browse panel"()
    {
        when:
        filterPanel.typeSearchText( content.getName() )
        contentBrowsePanel.selectContentInTable( content.getName() ).clickToolbarPublish().clickOnPublishNowButton();
        String message = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );

        then:
        !contentBrowsePanel.exists( content.getName() );

        and:
        message == "pending item was deleted";
    }
}
