package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentUnpublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentUnpublishDialog_Spec
    extends BaseContentSpec
{


    @Shared
    Content PARENT_CONTENT;

    @Shared
    Content CHILD_CONTENT;

    @Shared
    String NEW_DISPLAY_NAME = "new display name";

    def "GIVEN Content BrowsePanel WHEN content without child is selected and 'Publish' button was pressed THEN 'Content publish' dialog should appear without 'Include child' checkbox"()
    {
        given: "content is added"
        PARENT_CONTENT = buildFolderContent( "parent", "content unpublish dialog" );
        addContent( PARENT_CONTENT );
        and: "the content has been published"
        findAndSelectContent( PARENT_CONTENT.getName() ).clickToolbarPublish().clickOnPublishNowButton();

        when: "content selected and 'Unpublish' menu item is clicked"
        ContentUnpublishDialog contentUnPublishDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();

        then: "'ContentUnPublishDialog' dialog should appear"
        contentUnPublishDialog.isOpened();

        and: "'Include Child' checkbox should not be displayed, because the content has no child"
        contentUnPublishDialog.isUnPublishButtonEnabled();

        and: "'Cancel' button on the top should be displayed and enabled"
        contentUnPublishDialog.isCancelButtonTopEnabled();

        and: "'Cancel' button on the bottom should be displayed and enabled"
        contentUnPublishDialog.isCancelButtonBottomEnabled();

        and: "correct header should be present in the dialog"
        contentUnPublishDialog.getHeader() == ContentUnpublishDialog.HEADER_TEXT;

        and: "correct sub header should be present on the dialog"
        contentUnPublishDialog.getSubHeader().contains( ContentUnpublishDialog.SUBHEADER_PART_TEXT );

        and: "status of the content should be 'online' on the dialog"
        contentUnPublishDialog.getContentStatus( PARENT_CONTENT.getDisplayName() ) == ContentStatus.ONLINE.getValue();
    }

    def "GIVEN 'online' content is selected AND ContentUnpublishDialog opened WHEN cancel button on the top was pressed THEN dialog is closing AND status of the content should not be changed"()
    {
        given:
        findAndSelectContent( PARENT_CONTENT.getName() );
        ContentUnpublishDialog contentUnPublishDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();

        when: "content selected and 'Unpublish' menu item is clicked"
        contentUnPublishDialog.clickOnCancelTopButton()

        then: "'ContentUnPublishDialog' dialog is closing"
        !contentUnPublishDialog.isOpened();

        and: "content still has 'online' status"
        contentBrowsePanel.getContentStatus( PARENT_CONTENT.getName() ) == ContentStatus.ONLINE.getValue();

        and: "publish button on the toolbar should be disabled"
        !contentBrowsePanel.isPublishButtonEnabled();
    }

    def "GIVEN 'online' content is selected AND 'ContentUnpublishDialog' opened WHEN cancel button on the bottom was pressed THEN dialog is closing AND status of the content should not be changed"()
    {
        given: "'online' content is selected"
        findAndSelectContent( PARENT_CONTENT.getName() );
        and: "Unpublish menu item was clicked"
        ContentUnpublishDialog contentUnPublishDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();

        when: "cancel button on the bottom was pressed"
        contentUnPublishDialog.clickOnCancelBottomButton()

        then: "'ContentUnPublishDialog' dialog is closing"
        !contentUnPublishDialog.isOpened();

        and: "content still has 'online' status"
        contentBrowsePanel.getContentStatus( PARENT_CONTENT.getName() ) == ContentStatus.ONLINE.getValue();

        and: "publish button on the toolbar should be disabled"
        !contentBrowsePanel.isPublishButtonEnabled();
    }

    def "GIVEN 'modified' content is selected AND ContentUnpublishDialog is opened WHEN 'unpublish' menu item was selected THEN the content is getting 'offline'"()
    {
        given:
        findAndSelectContent( PARENT_CONTENT.getName() ).clickToolbarEditAndSwitchToWizardTab().typeDisplayName(
            NEW_DISPLAY_NAME ).save().closeBrowserTab().switchToBrowsePanelTab();
        ContentUnpublishDialog contentUnPublishDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();

        when: "content selected and 'Unpublish' menu item clicked"
        contentUnPublishDialog.clickOnUnpublishButton();

        then: "wait until the dialog closed"
        contentUnPublishDialog.waitForClosed();
        def message = contentBrowsePanel.waitNotificationMessage();

        and: "the content is getting 'offline'"
        contentBrowsePanel.getContentStatus( PARENT_CONTENT.getName() ) == ContentStatus.OFFLINE.getValue();

        and: "publish button on the toolbar should be enabled"
        contentBrowsePanel.isPublishButtonEnabled();

        and: "correct notification message should appear"
        message == String.format( Application.ONE_CONTENT_UNPUBLISHED_NOTIFICATION_MESSAGE, NEW_DISPLAY_NAME );
    }

    def "GIVEN parent and child content are 'online' WHEN parent content was selected and 'Unpublish' menu item clicked THEN parent and child contents are getting 'offline'"()
    {
        given: "parent and child content are 'online'"
        CHILD_CONTENT = buildFolderContentWithParent( "child", "child for unpublishing", PARENT_CONTENT.getName() );
        findAndSelectContent( PARENT_CONTENT.getName() );
        addContent( CHILD_CONTENT );
        contentBrowsePanel.clickToolbarPublish().includeChildren( true ).clickOnPublishNowButton();

        when: "parent content was selected and 'Unpublish' menu item has been clicked"
        contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem().clickOnUnpublishButton();
        boolean isMessageAppeared = contentBrowsePanel.waitExpectedNotificationMessage( "2 items are unpublished",
                                                                                        Application.EXPLICIT_NORMAL );
        contentBrowsePanel.expandContent( PARENT_CONTENT.getPath() );

        then: "parent content is getting 'offline'"
        contentBrowsePanel.getContentStatus( PARENT_CONTENT.getName() ) == ContentStatus.OFFLINE.getValue();

        and: "child content is getting 'offline'"
        contentBrowsePanel.getContentStatus( CHILD_CONTENT.getName() ) == ContentStatus.OFFLINE.getValue();

        and: "correct notification message should be shown"
        isMessageAppeared;

        and: "Publish button becomes enabled for the parent content"
        contentBrowsePanel.isPublishButtonEnabled();

        and: "Publish-menu for the parent content should be enabled"
        contentBrowsePanel.isPublishMenuAvailable();
    }

    def "GIVEN existing 'pending delete' content WHEN the content has been 'unpublished' THEN content should be deleted from the grid"()
    {
        given: "existing 'pending delete' content"
        Content content = buildFolderContent( "folder", "unpublish of pending delete content" );
        addContent( content );
        findAndSelectContent( content.getName() ).clickToolbarPublish().clickOnPublishNowButton();
        contentBrowsePanel.clickToolbarDelete().doDelete();

        when: "the content has been 'unpublished'"
        ContentUnpublishDialog contentUnPublishDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();
        contentUnPublishDialog.clickOnUnpublishButton();

        then: "content should be deleted from the grid"
        !contentBrowsePanel.exists( content.getName() );
    }
    //test verifies the XP-3584
    def "GIVEN two existing 'offline' contents WHEN both are selected in the BrowsePanel THEN 'Unpublish' menu item should be disabled"()
    {
        given: "first content added"
        Content first = buildFolderContent( "unpublish", "test unpublish menu item" );
        addContent( first );
        and: "the second content added in ROOT"
        Content second = buildFolderContent( "unpublish", "test unpublish menu item" );
        addContent( second );
        Content childForFirst = buildFolderContentWithParent( "child", "child for unpublishing", first.getName() );

        findAndSelectContent( first.getName() );
        and: "child for the first content added"
        addContent( childForFirst );
        contentBrowsePanel.doClearSelection();
        filterPanel.clickOnCleanFilter();

        when: "both contents are selected"
        contentBrowsePanel.selectContentInTable( first.getName(), second.getName() );
        contentBrowsePanel.showPublishMenu();
        saveScreenshot( "test_unpublish_item_disabled" );

        then: "Publish-menu is disabled when two 'offline' contents are selected"
        contentBrowsePanel.isPublishMenuAvailable();
    }
}
