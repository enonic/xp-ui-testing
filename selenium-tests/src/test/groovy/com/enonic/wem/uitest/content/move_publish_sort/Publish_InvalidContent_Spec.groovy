package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Tasks: XP-4948 Add Selenium tests for checking of 'red icon' (invalid content) in wizards
 *
 */
@Stepwise
class Publish_InvalidContent_Spec
    extends BaseContentSpec
{
    @Shared
    Content invalidFolder;

    def "GIVEN wizard for adding of a folder is opened WHEN display name is empty  AND 'Save' button has been pressed THEN 'Publish button should be disabled' AND red icon should be present on the wizard"()
    {
        given: "wizard for adding of a folder is opened"
        invalidFolder = buildFolderWithEmptyDisplayNameContent( "not_valid" );

        when: "display name is empty  AND 'Save' button has been pressed "
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( invalidFolder.getContentTypeName() ).typeData(
            invalidFolder ).save();

        then: "'Publish' button should be disabled"
        !wizard.isPublishButtonEnabled();

        and: "red icon should be displayed on the wizard page"
        wizard.isContentInvalid();
    }

    def "GIVEN existing content without a displayName WHEN it content was selected and 'Publish' button on grid toolbar was pressed THEN 'Publish' button on the dialog should be disabled and warning message should be present on dialog"()
    {
        given: "existing content without a displayName"
        filterPanel.typeSearchText( invalidFolder.getName() );
        saveScreenshot( "publish_invalid_folder" )

        when: "parent content selected and 'Publish' button pressed"
        contentBrowsePanel.clickCheckboxAndSelectRow( invalidFolder.getName() );
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL );

        then: "warning message should be displayed"
        contentPublishDialog.getDialogSubHeader() == ContentPublishDialog.DIALOG_SUBHEADER_INVALID_CONTENT_PUBLISH;

        and: "'publish' button should be disabled"
        !contentPublishDialog.isPublishButtonEnabled();

        and: "dependency list should not be present"
        !contentPublishDialog.isDependenciesListHeaderDisplayed();

        and: "one item should be in the list"
        List<String> itemList = contentPublishDialog.getNamesOfContentsToPublish();
        itemList.size() == 1;

        and: "correct name of content should be shown"
        contentPublishDialog.getNamesOfContentsToPublish().get( 0 ) == invalidFolder.getPath().toString();
    }

    def "GIVEN existing parent folder with not valid child WHEN parent content was selected and 'Publish' button clicked THEN 'Publish' button on the 'Content publish' dialog should be disabled"()
    {
        setup: "parent folder has been added"
        Content parentFolder = buildFolderContent( "folder", "publish not valid content" );
        addContent( parentFolder );

        and: "child folder was added"
        findAndSelectContent( parentFolder.getName() );
        Content childContent = buildFolderContentWithParent( "not_valid", null, parentFolder.getName() );
        contentBrowsePanel.clickToolbarNew().selectContentType( childContent.getContentTypeName() ).typeData( childContent ).save().
            closeBrowserTab().switchToBrowsePanelTab();

        when: "parent content was selected and 'Publish' button pressed"
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL );
        and: "'include child' icon was clicked"
        contentPublishDialog.includeChildren( true );

        then: "'Publish' button on the 'Content publish' dialog should be disabled"
        !contentPublishDialog.isPublishButtonEnabled();

        and: "warning message should be displayed"
        contentPublishDialog.getDialogSubHeader() == ContentPublishDialog.DIALOG_SUBHEADER_INVALID_CONTENT_PUBLISH;
    }
}
