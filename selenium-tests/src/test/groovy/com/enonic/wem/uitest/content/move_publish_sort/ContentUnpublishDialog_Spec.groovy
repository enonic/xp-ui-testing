package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentUnpublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

class ContentUnpublishDialog_Spec
    extends BaseContentSpec
{


    @Shared
    Content PARENT_CONTENT;

    @Shared
    Content CHILD_CONTENT;

    @Shared
    String NEW_DISPLAY_NAME = "new display name";

    def "GIVEN Content BrowsePanel WHEN one content without child selected and 'Publish' button clicked THEN 'Content publish' appears without 'Include child' checkbox"()
    {
        given:
        PARENT_CONTENT = buildFolderContent( "parent", "content unpublish dialog" );
        addContent( PARENT_CONTENT );
        findAndSelectContent( PARENT_CONTENT.getName() ).clickToolbarPublish().clickOnPublishNowButton();

        when: "content selected and 'Unpublish' menu item clicked"
        ContentUnpublishDialog contentUnPublishDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();

        then: "'ContentUnPublishDialog' dialog displayed"
        contentUnPublishDialog.isOpened();

        and: "'Include Child' checkbox not displayed"
        contentUnPublishDialog.isUnPublishButtonEnabled();

        and: "'Cancel' button on the top is displayed and enabled"
        contentUnPublishDialog.isCancelButtonTopEnabled();

        and: "'Cancel' button on the bottom is displayed and enabled"
        contentUnPublishDialog.isCancelButtonBottomEnabled();

        and: "correct header present in the dialog"
        contentUnPublishDialog.getHeader() == ContentUnpublishDialog.HEADER_TEXT;

        and: "correct sub header present in the dialog"
        contentUnPublishDialog.getSubHeader().contains( ContentUnpublishDialog.SUBHEADER_PART_TEXT );

        and: "status of the content in the dialog is 'online'"
        contentUnPublishDialog.getContentStatus( PARENT_CONTENT.getDisplayName() ) == ContentStatus.ONLINE.getValue();
    }

    def "GIVEN 'online' content selected AND ContentUnpublishDialog opened WHEN cancel button on the top pressed THEN dialog closed AND status of the content not changed"()
    {
        given:
        findAndSelectContent( PARENT_CONTENT.getName() );
        ContentUnpublishDialog contentUnPublishDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();

        when: "content selected and 'Unpublish' menu item clicked"
        contentUnPublishDialog.clickOnCancelTopButton()

        then: "'ContentUnPublishDialog' dialog displayed"
        !contentUnPublishDialog.isOpened();

        and: "content still has 'online' status"
        contentBrowsePanel.getContentStatus( PARENT_CONTENT.getName() ) == ContentStatus.ONLINE.getValue();

        and: "publish button on the toolbar is disabled"
        !contentBrowsePanel.isPublishButtonEnabled();
    }

    def "GIVEN 'online' content selected AND 'ContentUnpublishDialog' opened WHEN cancel button on the bottom pressed THEN dialog closed AND status of the content not changed"()
    {
        given:
        findAndSelectContent( PARENT_CONTENT.getName() );
        ContentUnpublishDialog contentUnPublishDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();

        when: "content selected and 'Unpublish' menu item clicked"
        contentUnPublishDialog.clickOnCancelBottomButton()

        then: "'ContentUnPublishDialog' dialog displayed"
        !contentUnPublishDialog.isOpened();

        and: "content still has 'online' status"
        contentBrowsePanel.getContentStatus( PARENT_CONTENT.getName() ) == ContentStatus.ONLINE.getValue();


        and: "publish button on the toolbar is disabled"
        !contentBrowsePanel.isPublishButtonEnabled();
    }

    def "GIVEN 'modified' content selected AND ContentUnpublishDialog opened WHEN 'unpublish' menu item selected THEN the content becomes is 'offline'"()
    {
        given:
        findAndSelectContent( PARENT_CONTENT.getName() ).clickToolbarEdit().typeDisplayName( NEW_DISPLAY_NAME ).save().close(
            NEW_DISPLAY_NAME );
        ContentUnpublishDialog contentUnPublishDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();

        when: "content selected and 'Unpublish' menu item clicked"
        contentUnPublishDialog.clickOnUnpublishButton();

        then: "'ContentUnPublishDialog' dialog displayed"
        !contentUnPublishDialog.isOpened();

        and: "content still has 'online' status"
        contentBrowsePanel.getContentStatus( PARENT_CONTENT.getName() ) == ContentStatus.OFFLINE.getValue();


        and: "publish button on the toolbar is disabled"
        contentBrowsePanel.isPublishButtonEnabled();
    }

    def "GIVEN parent and child content are 'online' WHEN parent content selected and 'Unpublish' menu item selected THEN parent and child contents are 'offline'"()
    {
        given:
        CHILD_CONTENT = buildFolderContentWithParent( "child", "child for unpublishing", PARENT_CONTENT.getName() );
        findAndSelectContent( PARENT_CONTENT.getName() );
        addContent( CHILD_CONTENT );
        contentBrowsePanel.clickToolbarPublish().setIncludeChildCheckbox( true ).clickOnPublishNowButton();

        when:
        contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem().clickOnUnpublishButton();
        boolean isMessageAppeared = contentBrowsePanel.waitExpectedNotificationMessage( "2 items were unpublished",
                                                                                        Application.EXPLICIT_NORMAL );
        contentBrowsePanel.expandContent( PARENT_CONTENT.getPath() );

        then:
        contentBrowsePanel.getContentStatus( PARENT_CONTENT.getName() ) == ContentStatus.OFFLINE.getValue();

        and:
        contentBrowsePanel.getContentStatus( CHILD_CONTENT.getName() ) == ContentStatus.OFFLINE.getValue();

        and:
        isMessageAppeared;
    }

    //TODO :
    //'pending-delete' content selected
    // 'out-of-date' content selected
    // getStatus from the dialog
    // unpublish from the wizard
}
