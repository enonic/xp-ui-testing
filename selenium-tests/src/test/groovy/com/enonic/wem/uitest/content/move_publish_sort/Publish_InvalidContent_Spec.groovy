package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Tasks: XP-4948 Add Selenium tests for checking of 'red icon' (invalid content) in wizards
 **/
@Stepwise
class Publish_InvalidContent_Spec
    extends BaseContentSpec
{
    @Shared
    Content invalidFolder;

    def "GIVEN wizard for new folder is opened WHEN display name is empty  AND 'Save' button has been pressed THEN 'Publish' button should be disabled AND red icon should be present on the wizard"()
    {
        given: "wizard for new folder is opened"
        invalidFolder = buildFolderWithEmptyDisplayNameContent( "not_valid" );

        when: "display name is empty AND 'Save' button has been pressed"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( invalidFolder.getContentTypeName() ).typeData(
            invalidFolder ).save();

        then: "'Publish...' menu item should be disabled"
        !wizard.showPublishMenu(  ).isPublishMenuItemEnabled(  );

        and: "red icon should be displayed on the wizard page"
        wizard.isContentInvalid();
    }

    def "GIVEN existing content without a displayName WHEN the content has been selected THEN 'Publish' button on the grid-toolbar should be disabled"()
    {
        given: "existing content without a displayName"
        filterPanel.typeSearchText( invalidFolder.getName() );
        saveScreenshot( "publish_invalid_folder" )

        when: "the content has been selected"
        contentBrowsePanel.clickCheckboxAndSelectRow( invalidFolder.getName() );

        then: "'Publish' button on the grid-toolbar should be disabled"
        !contentBrowsePanel.isPublishButtonEnabled();
    }

    def "GIVEN existing parent folder with not valid child WHEN parent content has been selected and 'Publish' button clicked THEN 'Publish' button on the modal dialog should be disabled"()
    {
        setup: "parent folder has been added"
        Content parentFolder = buildFolderContent( "folder", "publish not valid content" );
        addContent( parentFolder );

        and: "child folder has been added"
        findAndSelectContent( parentFolder.getName() );
        Content childContent = buildFolderContentWithParent( "not_valid", null, parentFolder.getName() );
        contentBrowsePanel.clickToolbarNew().selectContentType( childContent.getContentTypeName() ).typeData( childContent ).save().
            closeBrowserTab().switchToBrowsePanelTab();

        when: "parent content has been selected and 'Publish' button pressed"
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL );
        and: "'include child' icon was clicked"
        contentPublishDialog.includeChildren( true );

        then: "'Publish' button on the 'Content publish' dialog should be disabled"
        !contentPublishDialog.isPublishButtonEnabled();

        and: "warning message should be displayed on the modal dialog"
        contentPublishDialog.getDialogSubHeader() == ContentPublishDialog.DIALOG_SUBHEADER_INVALID_CONTENT_PUBLISH;
    }
}
