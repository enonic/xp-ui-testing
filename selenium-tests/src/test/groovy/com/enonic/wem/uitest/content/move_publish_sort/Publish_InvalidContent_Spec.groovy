package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
@Ignore
class Publish_InvalidContent_Spec
    extends BaseContentSpec
{
    @Shared
    Content FOLDER;

    def "GIVEN wizard for new folder is opened WHEN display name is empty  AND 'Save' button has been pressed THEN 'Publish' button should be disabled AND red icon gets visible in the wizard"()
    {
        given: "wizard for new folder is opened"
        FOLDER = buildFolderWithEmptyDisplayNameContent( "not_valid" );

        when: "display name is empty AND 'Save' button has been pressed"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( "Folder" ).typeData( FOLDER );
        wizard.save();

        then: "'Publish...' menu item should be disabled"
        !wizard.showPublishMenu().isPublishMenuItemEnabled();

        and: "red icon should be displayed on the wizard page"
        wizard.isContentInvalid();
    }

    def "GIVEN existing folder (displayName is empty) WHEN the content has been selected THEN 'CREATE ISSUE' button on the grid-toolbar should be displayed"()
    {
        given: "existing content without a displayName"
        filterPanel.typeSearchText( FOLDER.getName() );
        saveScreenshot( "publish_invalid_folder" )

        when: "the content has been selected"
        contentBrowsePanel.clickCheckboxAndSelectRow( FOLDER.getName() );

        then: "'Create Issue' button on the grid-toolbar should be displayed"
        contentBrowsePanel.isCreateTaskButtonDisplayed();
    }

    def "GIVEN existing parent folder with not valid child WHEN parent content has been selected and 'Publish' button clicked THEN 'Publish Now' button in the modal dialog should be disabled"()
    {
        setup: "parent folder has been added"
        Content parentFolder = buildFolderContent( "folder", "publish not valid content" );
        Content childContent = buildFolderContentWithParent( "not_valid", null, parentFolder.getName() );
        addReadyContent( parentFolder );

        and: "not valid child folder has been added"
        findAndSelectContent( parentFolder.getName() );
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType(
            childContent.getContentTypeName() ).typeData( childContent );
        wizardPanel.closeBrowserTab().switchToBrowsePanelTab();

        when: "parent content has been selected and 'Publish' button pressed"
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitForDialogLoaded();
        and: "'include child' icon has been clicked"
        contentPublishDialog.includeChildren( true );

        then: "'Publish Now' button in 'Content publish' dialog should be disabled"
        !contentPublishDialog.isPublishButtonEnabled();

        //and: "warning message should be displayed on the modal dialog"
        //contentPublishDialog.getDialogSubHeader() == ContentPublishDialog.DIALOG_SUBHEADER_INVALID_CONTENT_PUBLISH;
    }
}
