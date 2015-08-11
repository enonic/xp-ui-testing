package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Publish_InvalidContent_Spec
    extends BaseContentSpec
{
    @Shared
    Content invalidFolder;

    def "GIVEN creating a not valid content WHEN data typed and 'Publish' button on the wizard toolbar pressed THEN notification warning appears and content not published"()
    {
        given:
        invalidFolder = buildFolderWithEmptyDisplayNameContent( "not_valid" );

        when:
        ContentWizardPanel wizrad = contentBrowsePanel.clickToolbarNew().selectContentType( invalidFolder.getContentTypeName() ).typeData(
            invalidFolder );

        then: "notification warning appears"
        wizrad.isContentInvalid( invalidFolder.getDisplayName() );
        and: "'Publish' button is disabled"
        !wizrad.isPublishButtonEnabled();
    }

    def "GIVEN a content without a displayName WHEN it content selected and 'Publish' button on grid toolbar pressed THEN publish dialog appears AND the 'Publish Now' button is disabled and correct header present on dialog"()
    {
        given:
        filterPanel.typeSearchText( invalidFolder.getName() );

        when: "parent content selected and 'Publish' button pressed"
        contentBrowsePanel.clickCheckboxAndSelectRow( invalidFolder.getName() );
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShowed(
            Application.EXPLICIT_NORMAL );

        then: "'invalid' subheader shown"
        contentPublishDialog.getDialogSubHeader() == ContentPublishDialog.DIALOG_SUBHEADER_INVALID_CONTENT_PUBLISH;

        and: "'publish now' button disabled"
        !contentPublishDialog.isPublishNowButtonEnabled();

        and: "dependency list not present"
        !contentPublishDialog.isDependenciesListHeaderDisplayed();
        and: "correct name of content shown"
        contentPublishDialog.getNamesOfContentsToPublish().get( 0 ) == invalidFolder.getPath().toString();
    }

    def "GIVEN a parent folder with not valid child WHEN parent content selected and 'Publish' button clicked THEN 'Content publish' dialog appears and 'Publish Now' button disabled"()
    {
        setup: "add a valid parent folder"
        Content parentFolder = buildFolderContent( "folder", "publish not valid content" );
        addContent( parentFolder );

        and: "add a not valid child folder"
        filterPanel.typeSearchText( parentFolder.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( parentFolder.getName() )
        Content childContent = buildFolderContentWithParent( "not_valid", null, parentFolder.getName() );
        contentBrowsePanel.clickToolbarNew().selectContentType( childContent.getContentTypeName() ).typeData( childContent ).save().close(
            "<Unnamed Folder>" );

        when: "parent content selected and 'Publish' button pressed"
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShowed(
            Application.EXPLICIT_NORMAL );
        contentPublishDialog.setIncludeChildCheckbox( true );

        then: "'Publish Now'  button disabled"
        !contentPublishDialog.isPublishNowButtonEnabled();
        and: "correct subheader present in dialog"
        contentPublishDialog.getDialogSubHeader() == ContentPublishDialog.DIALOG_SUBHEADER_INVALID_CONTENT_PUBLISH;
    }
}
