package com.enonic.wem.uitest.content.move_publish_sort

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
    String PENDING_DELETE_PUBLISH_MESSAGE = "\"%s\" deleted";

    @Shared
    String ONLINE_DELETED_MESSAGE = "\"%s\" marked for deletion";

    @Shared
    String PUBLISH_MESSAGE = "\"%s\" published"

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
        String message = contentBrowsePanel.selectContentInTable( content.getPath() ).clickToolbarPublish().waitNotificationMessage();
        then:
        contentBrowsePanel.getContentStatus( content.getPath() ) == ContentStatus.ONLINE.getValue();
        message == String.format( PUBLISH_MESSAGE, DISPLAY_NAME );

    }

    def "GIVEN existing root content with 'Online' status  WHEN content selected and 'Delete' button pressed THEN  content with a 'Pending delete' status present"()
    {
        given:
        filterPanel.typeSearchText( content.getName() )

        when:
        contentBrowsePanel.selectContentInTable( content.getPath() ).clickToolbarDelete().doDelete();

        then:
        contentBrowsePanel.getContentStatus( content.getPath() ) == ContentStatus.PENDING_DELETE.getValue();

    }

    def "GIVEN existing root content with 'Pending Delete' status  WHEN content selected and 'Publish' button pressed THEN content not listed in browse panel "()
    {
        when:
        filterPanel.typeSearchText( content.getName() )
        String message = contentBrowsePanel.selectContentInTable( content.getPath() ).clickToolbarPublish().waitNotificationMessage();
        filterPanel.typeSearchText( content.getName() );

        then:
        !contentBrowsePanel.exists( content.getPath() );
        and:
        message == String.format( PENDING_DELETE_PUBLISH_MESSAGE, DISPLAY_NAME );

    }

}
