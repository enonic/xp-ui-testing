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

        then: "wait until the dialog closed"
        contentUnPublishDialog.waitForClosed();

        and: "content still has 'online' status"
        contentBrowsePanel.getContentStatus( PARENT_CONTENT.getName() ) == ContentStatus.OFFLINE.getValue();


        and: "publish button on the toolbar is enabled"
        contentBrowsePanel.isPublishButtonEnabled();
    }

    def "GIVEN parent and child content are 'online' WHEN parent content selected and 'Unpublish' menu item selected THEN parent and child contents are 'offline'"()
    {
        given: "parent and child content are 'online'"
        CHILD_CONTENT = buildFolderContentWithParent( "child", "child for unpublishing", PARENT_CONTENT.getName() );
        findAndSelectContent( PARENT_CONTENT.getName() );
        addContent( CHILD_CONTENT );
        contentBrowsePanel.clickToolbarPublish().setIncludeChildCheckbox( true ).clickOnPublishNowButton();

        when: "parent content selected and 'Unpublish' menu item selected"
        contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem().clickOnUnpublishButton();
        boolean isMessageAppeared = contentBrowsePanel.waitExpectedNotificationMessage( "2 items were unpublished",
                                                                                        Application.EXPLICIT_NORMAL );
        contentBrowsePanel.expandContent( PARENT_CONTENT.getPath() );

        then: "parent content becomes 'offline'"
        contentBrowsePanel.getContentStatus( PARENT_CONTENT.getName() ) == ContentStatus.OFFLINE.getValue();

        and: "child content becomes 'offline'"
        contentBrowsePanel.getContentStatus( CHILD_CONTENT.getName() ) == ContentStatus.OFFLINE.getValue();

        and: "correct notification message was shown"
        isMessageAppeared;

        and: "Publish button becomes enabled for the parent content"
        contentBrowsePanel.isPublishButtonEnabled();

        and: "Publish-menu is enabled for the parent content"
        contentBrowsePanel.isPublishMenuAvailable();
    }

    def "GIVEN existing 'pending delete' content WHEN the content has been 'unpublished' THEN content not listed in the grid"()
    {
        given: "existing 'pending delete' content"
        Content content = buildFolderContent( "folder", "unpublish of pending delete content" );
        addContent( content );
        findAndSelectContent( content.getName() ).clickToolbarPublish().clickOnPublishNowButton();
        contentBrowsePanel.clickToolbarDelete().doDelete();

        when: "the content has been 'unpublished'"
        ContentUnpublishDialog contentUnPublishDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();
        contentUnPublishDialog.clickOnUnpublishButton();

        then: "content not listed in the grid"
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
        contentBrowsePanel.clickOnClearSelection();
        filterPanel.clickOnCleanFilter();

        when: "both contents are selected"
        contentBrowsePanel.selectContentInTable( first.getName(), second.getName() );
        contentBrowsePanel.showPublishMenu();
        saveScreenshot( "test_unpublish_item_disabled" );

        then: "Publish-menu is disabled when two 'offline' contents are selected"
        contentBrowsePanel.isPublishMenuAvailable();
    }

}
