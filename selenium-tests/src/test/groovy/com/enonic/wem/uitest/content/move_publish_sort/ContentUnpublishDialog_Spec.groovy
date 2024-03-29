package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ConfirmValueDialog
import com.enonic.autotests.pages.contentmanager.ContentUnpublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
@Ignore
class ContentUnpublishDialog_Spec
    extends BaseContentSpec
{
    @Shared
    Content PARENT_CONTENT;

    @Shared
    Content CHILD_CONTENT;

    @Shared
    String NEW_DISPLAY_NAME = "new display name";

    def "GIVEN content without child is selected WHEN 'Publish' button has been pressed THEN 'Content publish' dialog should be loaded without 'Include child' checkbox"()
    {
        given: "content is added"
        PARENT_CONTENT = buildFolderContent( "parent", "content unpublish dialog" );
        addReadyContent( PARENT_CONTENT );
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
        contentUnPublishDialog.getContentStatus( PARENT_CONTENT.getDisplayName() ) == ContentStatus.PUBLISHED.getValue();
    }

    def "GIVEN Unpublish Dialog is opened WHEN cancel button on the top has been pressed THEN dialog closes AND status of the content should not be changed"()
    {
        given: "'PUBLISHED' content is selected"
        findAndSelectContent( PARENT_CONTENT.getName() );
        ContentUnpublishDialog contentUnPublishDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();

        when: "content selected and 'Unpublish' menu item is clicked"
        contentUnPublishDialog.clickOnCancelTopButton()

        then: "'ContentUnPublishDialog' dialog closes"
        !contentUnPublishDialog.isOpened();

        and: "content has Published status"
        contentBrowsePanel.getContentStatus( PARENT_CONTENT.getName() ) == ContentStatus.PUBLISHED.getValue();

        and: "publish button on the toolbar should be disabled"
        !contentBrowsePanel.isPublishButtonEnabled();
    }

    def "GIVEN 'PUBLISHED' content is selected AND 'UnpublishDialog' is opened WHEN cancel button on the bottom was pressed THEN dialog is closing AND status of the content should not be changed"()
    {
        given: "'online' content is selected"
        findAndSelectContent( PARENT_CONTENT.getName() );
        and: "Unpublish menu item was clicked"
        ContentUnpublishDialog contentUnPublishDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();

        when: "cancel button on the bottom was pressed"
        contentUnPublishDialog.clickOnCancelBottomButton()

        then: "'ContentUnPublishDialog' dialog is closing"
        !contentUnPublishDialog.isOpened();

        and: "content still has 'Published' status"
        contentBrowsePanel.getContentStatus( PARENT_CONTENT.getName() ) == ContentStatus.PUBLISHED.getValue();

        and: "publish button on the toolbar should be disabled"
        !contentBrowsePanel.isPublishButtonEnabled();
    }

    def "GIVEN 'modified' content is selected AND Unpublish Dialog is opened WHEN 'unpublish' menu item has been selected THEN the content gets 'Unpublished'"()
    {
        given:
        findAndSelectContent( PARENT_CONTENT.getName() ).clickToolbarEditAndSwitchToWizardTab().typeDisplayName(
            NEW_DISPLAY_NAME ).save().closeBrowserTab().switchToBrowsePanelTab();
        ContentUnpublishDialog contentUnPublishDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();

        when: "content selected and 'Unpublish' menu item has been clicked"
        contentUnPublishDialog.clickOnUnpublishButton();

        then: "wait until the dialog closed"
        contentUnPublishDialog.waitForClosed();
        def message = contentBrowsePanel.waitForNotificationMessage();

        and: "the content is getting 'Unpublished'"
        contentBrowsePanel.getContentStatus( PARENT_CONTENT.getName() ) == ContentStatus.UNPUBLISHED.getValue();

        and: "publish button on the toolbar should be enabled"
        contentBrowsePanel.isPublishButtonEnabled();

        and: "Message 'Item is unpublished'  should appears "
        message == String.format( Application.ONE_CONTENT_UNPUBLISHED_NOTIFICATION_MESSAGE, PARENT_CONTENT.getName() );
    }

    def "GIVEN parent and child content are 'Published' WHEN parent content has been selected and 'Unpublish' menu item clicked THEN parent and child contents get 'Unpublished'"()
    {
        given: "parent and child content are 'Published'"
        CHILD_CONTENT = buildFolderContentWithParent( "child", "child for unpublishing", PARENT_CONTENT.getName() );
        findAndSelectContent( PARENT_CONTENT.getName() ).clickOnMarkAsReadySingleContent();
        addReadyContent( CHILD_CONTENT );
        and: "both contents should be published"
        contentBrowsePanel.clickToolbarPublish().includeChildren( true ).clickOnPublishNowButton();

        when: "parent content was selected and 'Unpublish' menu item has been clicked"
        contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem().clickOnUnpublishButton();
        ConfirmValueDialog confirmValueDialog = new ConfirmValueDialog( getSession() );
        confirmValueDialog.waitForDialogLoaded();
        confirmValueDialog.typeNumber( "2" ).clickOnConfirmButton();
        boolean isMessageAppeared = contentBrowsePanel.waitExpectedNotificationMessage( "2 items are unpublished",
                                                                                        Application.EXPLICIT_NORMAL );
        contentBrowsePanel.expandContent( PARENT_CONTENT.getPath() );

        then: "parent content gets 'unpublished'"
        contentBrowsePanel.getContentStatus( PARENT_CONTENT.getName() ) == ContentStatus.UNPUBLISHED.getValue();

        and: "child content gets 'unpublished'"
        contentBrowsePanel.getContentStatus( CHILD_CONTENT.getName() ) == ContentStatus.UNPUBLISHED.getValue();

        and: "expected notification message should be shown"
        isMessageAppeared;

        and: "Publish button becomes enabled for the parent content"
        contentBrowsePanel.isPublishButtonEnabled();

        and: "Publish-menu for the parent content should be enabled"
        contentBrowsePanel.isPublishMenuAvailable();
    }

    def "GIVEN existing parent and child are 'published' WHEN the parent is selected and Unpublish dialog is opened THEN dependant item should be displayed in the dialog"()
    {
        given: "existing parent and child has been published"
        findAndSelectContent( PARENT_CONTENT.getName() );
        contentBrowsePanel.clickToolbarPublish().includeChildren( true ).clickOnPublishNowButton();

        when: "the parent is selected and 'Unpublish' dialog is opened"
        ContentUnpublishDialog contentUnPublishDialog = contentBrowsePanel.showPublishMenu().selectUnPublishMenuItem();

        then: "dependant list should be hidden"
        !contentUnPublishDialog.isDependantsDisplayed();
    }

    //test verifies the XP-3584
    def "GIVEN two existing 'New' content WHEN both are selected in the BrowsePanel THEN 'Unpublish' menu item should be disabled"()
    {
        given: "first content is added"
        Content first = buildFolderContent( "unpublish", "test unpublish menu item" );
        addReadyContent( first );
        and: "the second content is added in ROOT"
        Content second = buildFolderContent( "unpublish", "test unpublish menu item" );
        addContent( second );
        Content childForFirst = buildFolderContentWithParent( "child", "child for unpublishing", first.getName() );

        findAndSelectContent( first.getName() );
        and: "child for the first content is added"
        addContent( childForFirst );
        contentBrowsePanel.doClearSelection();
        filterPanel.clickOnCleanFilter();

        when: "both contents are selected"
        contentBrowsePanel.selectContentInGrid( first.getName(), second.getName() );
        contentBrowsePanel.showPublishMenu();
        saveScreenshot( "test_unpublish_item_disabled" );

        then: "Publish-menu should be enabled when two 'New' contents are selected"
        contentBrowsePanel.isPublishMenuAvailable();
    }
}
