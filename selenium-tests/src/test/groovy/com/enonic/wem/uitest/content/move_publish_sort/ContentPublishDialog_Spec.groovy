package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentPublishDialog_Spec
    extends BaseContentSpec
{
    @Shared
    Content parentContent;

    @Shared
    Content childContent1;

    //this is tests verifies the  "XP-3824 Unknown status displayed on the publish dialog"
    def "GIVEN creating of new content WHEN data typed and 'Publish' button was pressed  THEN correct status of content is displayed on the modal dialog"()
    {
        given:
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );
        String displayName = NameHelper.uniqueName( "test" );

        when:
        ContentPublishDialog dialog = wizard.typeDisplayName( displayName ).clickOnWizardPublishButton();

        then:
        dialog.getContentStatus( displayName ) == ContentStatus.OFFLINE.getValue();
    }

    def "GIVEN existing folder without child in the root WHEN the folder was selected and 'Publish' button clicked THEN 'Content publish' should appear without 'Include child' icon"()
    {
        given: "existing folder in root"
        Content folderContent = buildFolderContent( "no_child", "content publish dialog" );
        addContent( folderContent );

        when: "the folder selected and 'Publish' button pressed"
        findAndSelectContent( folderContent.getName() )
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL );

        then: "'Include Child' button should not be displayed displayed"
        !contentPublishDialog.isIncludeChildTogglerDisplayed();

        and: "correct status of content is displayed"
        contentPublishDialog.getContentStatus( folderContent.getDisplayName() ) == ContentStatus.OFFLINE.getValue();
    }

    def "GIVEN parent content with a child WHEN the parent content is selected and 'Publish' button clicked THEN 'Content publish' dialog should appear with correct control elements"()
    {
        setup: "add a parent folder"
        parentContent = buildFolderContent( "publish_dialog", "content publish dialog" );
        addContent( parentContent );

        and: "add one child to the parent folder"
        filterPanel.typeSearchText( parentContent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getName() )
        childContent1 = buildFolderContentWithParent( "publish_dialog", "child-folder1", parentContent.getName() );
        addContent( childContent1 );

        when: "parent content is selected and 'Publish' button was pressed"
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL );

        then: "'ContentPublishDialog' dialog should be displayed"
        contentPublishDialog.isOpened();

        and: "dialog should have the correct title"
        contentPublishDialog.getTitle() == ContentPublishDialog.DIALOG_TITLE

        and: "'Publish' and 'Cancel' buttons should be displayed on the dialog"
        contentPublishDialog.isPublishNowButtonEnabled();

        and:
        contentPublishDialog.isCancelButtonBottomEnabled();

        and: "'Cancel' button on the top should be displayed"
        contentPublishDialog.isCancelButtonTopEnabled();

        and: "Dependants list should not be displayed, because 'Include child' icon was not pressed"
        !contentPublishDialog.isDependantsDisplayed();

    }

    def "GIVEN 'Content Publish' dialog shown WHEN the cancel button on the bottom was clicked THEN dialog is closing"()
    {
        given: "parent content is selected and 'Publish' button pressed"
        findAndSelectContent( parentContent.getName() );
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL );

        when: "button 'Cancel' on the bottom of dialog was pressed"
        contentPublishDialog.clickOnCancelBottomButton();

        then: "dialog is closing"
        !contentPublishDialog.isOpened();
    }

    def "GIVEN 'Content Publish' dialog is opened WHEN the button cancel on the top was clicked THEN dialog is closing"()
    {
        given: "parent content selected and 'Publish' button pressed"
        findAndSelectContent( parentContent.getName() );
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL );

        when: "button 'Cancel' on the top of dialog was pressed"
        contentPublishDialog.clickOnCancelTopButton();

        then: "dialog is closing"
        !contentPublishDialog.isOpened();
    }

    def "GIVEN parent content on root is selected WHEN 'Content Publish' dialog opened THEN correct name of content present in the dialog"()
    {
        given: "parent content in the root is selected"
        findAndSelectContent( parentContent.getName() );

        when: "'Content Publish' dialog has been opened"
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL );
        List<String> names = contentPublishDialog.getNamesOfContentsToPublish();
        saveScreenshot( "publish-dialog-opened" );

        then: "only one name of content should be present on the dialog"
        names.size() == 1;

        and: "correct name of content should be displayed on the dialog"
        names.get( 0 ).contains( parentContent.getName() );
    }

    def "GIVEN parent content on root is selected AND 'Content publish' dialog is opened WHEN 'include child' icon was pressed THEN one dependant item should be displayed on the dialog"()
    {
        given: "parent folder is selected and 'Publish' button pressed"
        findAndSelectContent( parentContent.getName() );
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL );

        when: "'include child' icon was pressed"
        contentPublishDialog.includeChildren( true );
        List<String> dependant = contentPublishDialog.getDependantList();
        saveScreenshot( "publish-dialog-dependencies" );

        then: "The header of 'Dependencies list' should be displayed"
        contentPublishDialog.isDependenciesListHeaderDisplayed();

        and: "correct text should be shown in the header"
        contentPublishDialog.getDependenciesListHeader() == ContentPublishDialog.OTHER_ITEMS_WILL_BE_PUBLISHED;

        and: "one correct dependant should be present"
        dependant.size() == 1;

        and: "correct name of the dependency should be displayed"
        dependant.get( 0 ).contains( childContent1.getName() );
    }

    def "GIVEN existing child content WHEN the content was selected and 'Publish' button pressed THEN the name of the parent folder should be present on the dialog"()
    {
        given: "existing child content"
        filterPanel.typeSearchText( childContent1.getName() );

        when: "child content was selected and 'Publish' button pressed"
        contentBrowsePanel.clickCheckboxAndSelectRow( childContent1.getName() );
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL );
        List<String> dependant = contentPublishDialog.getDependantList();
        saveScreenshot( "publish-dialog-dependencies-child" );

        then: "The header of 'Dependencies list' should be present"
        contentPublishDialog.isDependenciesListHeaderDisplayed();

        and: "correct text should be shown in the header"
        contentPublishDialog.getDependenciesListHeader() == ContentPublishDialog.OTHER_ITEMS_WILL_BE_PUBLISHED;

        and: "one dependency should be shown on the dialog"
        dependant.size() == 1;

        and: "name of the parent folder should be displayed"
        dependant.get( 0 ).contains( parentContent.getName() );
    }
}
