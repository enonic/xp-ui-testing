package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.BaseContentType
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.issue.CreateIssueDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.content.ContentPath
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentPublishDialog_Spec
    extends BaseContentSpec
{
    @Shared
    Content PARENT_FOLDER;

    @Shared
    Content CHILD_FOLDER;



    def "GIVEN wizard for new folder is opened WHEN data typed and 'Publish' button was pressed  THEN correct status of content is displayed on the modal dialog"()
    {
        given:
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName() );
        String displayName = NameHelper.uniqueName( "test" );

        when: "'Publish' button on the wizard has been pressed"
        wizard.typeDisplayName( displayName );
        wizard.showPublishMenu(  ).clickOnMarkAsReadyMenuItem(  );
        ContentPublishDialog dialog = wizard.clickOnMarkAsReadyAndDoPublish(  );
        saveScreenshot( "unsaved_content_status" )

        then: "the content should be displayed with the 'New' status on the publishing-wizard"
        dialog.getContentStatus( displayName ) == ContentStatus.NEW.getValue();
    }

    def "GIVEN folder is added in the root WHEN the folder has been selected and 'Publish' button clicked THEN 'Include child' icon should not be present on the modal dialog"()
    {
        given: "folder has been added in root directory"
        Content folderContent = buildFolderContent( "no_child", "content publish dialog" );
        addContent( folderContent );

        when: "the folder has been selected and 'Publish' button pressed"
        findAndSelectContent( folderContent.getName() );
        contentBrowsePanel.showPublishMenu(  ).clickOnMarkAsReadyMenuItem(  ).pressYesButton(  );
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL );

        then: "'Include Child' button should not be displayed"
        !contentPublishDialog.isIncludeChildTogglerDisplayed();

        and: "'New' status should be displayed"
        contentPublishDialog.getContentStatus( folderContent.getDisplayName() ) == ContentStatus.NEW.getValue();

        and: "'remove' button should be disabled for this content, because only one item is selected."
        !contentPublishDialog.isPublishItemRemovable( folderContent.getDisplayName() )
    }

//    def "GIVEN parent folder with a child WHEN parent content has been selected and 'Publish' button pressed THEN 'Content publish' dialog should appear with expected control elements"()
//    {
//        setup: "parent folder has been added"
//        PARENT_FOLDER = buildFolderContent( "publish_dialog", "content publish dialog" );
//        addContent( PARENT_FOLDER );
//
//        and: "child folder has been added"
//        filterPanel.typeSearchText( PARENT_FOLDER.getName() );
//        contentBrowsePanel.clickCheckboxAndSelectRow( PARENT_FOLDER.getName() )
//        CHILD_FOLDER = buildFolderContentWithParent( "publish_dialog", "child-folder1", PARENT_FOLDER.getName() );
//        addContent( CHILD_FOLDER );
//
//        when: "parent content has been selected and 'Publish' button pressed"
//        contentBrowsePanel.showPublishMenu(  ).clickOnMarkAsReadyMenuItem(  ).pressYesButton(  );
//        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
//            Application.EXPLICIT_NORMAL );
//        and: "'include children' has been clicked"
//        contentPublishDialog.includeChildren( true );
//
//        and: ""
//        contentPublishDialog.clickOnShowDependentItemsLink();
//
//        then: "correct title should be displayed on the dialog"
//        contentPublishDialog.getTitle() == ContentPublishDialog.DIALOG_TITLE
//
//        and: "'Publish' and 'Cancel' buttons should be displayed on the dialog"
//        contentPublishDialog.isPublishButtonEnabled();
//
//
//        and: "'Cancel' button on the top should be displayed"
//        contentPublishDialog.isCancelButtonTopEnabled();
//
//        and: "Dependants list should be displayed, because 'Include child' icon was clicked"
//        contentPublishDialog.isDependantsDisplayed();
//
//        and: "'remove' button should be enabled fot the dependant item"
//        contentPublishDialog.isDependantItemRemovable( CHILD_FOLDER.getName() );
//    }

//    def "GIVEN 'Content Publish' dialog is opened WHEN cancel button on the bottom has been clicked THEN dialog is closing"()
//    {
//        given: "parent content is selected and 'Publish' button pressed"
//        findAndSelectContent( PARENT_FOLDER.getName() );
//        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
//            Application.EXPLICIT_NORMAL );
//
//        when: "button 'Cancel' on the bottom of dialog has been pressed"
//        contentPublishDialog.clickOnCancelBottomButton();
//
//        then: "dialog is closing"
//        !contentPublishDialog.isOpened();
//    }

//    def "GIVEN 'Content Publish' dialog is opened WHEN 'show menu' button has been clicked THEN 'Create Issue' menu item should be present"()
//    {
//        given: "parent content is selected and 'Publish' button pressed"
//        findAndSelectContent( PARENT_FOLDER.getName() );
//        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
//            Application.EXPLICIT_NORMAL );
//
//        when: "'show menu' button has been clicked"
//        contentPublishDialog.showPublishMenu();
//
//        then: "'create issue' menu item should be displayed"
//        contentPublishDialog.isCreateIssueMenuItemDisplayed();
//
//        and: "'schedule' menu item should be displayed"
//        contentPublishDialog.isScheduleMenuItemDisplayed();
//    }
//
//    def "GIVEN 'Content Publish' dialog is opened WHEN 'Create Issue' menu item has been clicked THEN 'Create Issue' dialog should be present"()
//    {
//        given: "parent content is selected and 'Publish' button pressed"
//        findAndSelectContent( PARENT_FOLDER.getName() );
//        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
//            Application.EXPLICIT_NORMAL );
//
//        when: "'Create Issue' menu item has been clicked"
//        CreateIssueDialog dialog = contentPublishDialog.showPublishMenu().clickOnCreateIssueMenuItem();
//
//        then: "'create issue' dialog should be displayed"
//        dialog.waitForOpened();
//    }

//    def "GIVEN 'Content Publish' dialog is opened WHEN the button cancel on the top was clicked THEN dialog is closing"()
//    {
//        given: "parent content selected and 'Publish' button pressed"
//        findAndSelectContent( PARENT_FOLDER.getName() );
//        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
//            Application.EXPLICIT_NORMAL );
//
//        when: "button 'Cancel' on the top of dialog was pressed"
//        contentPublishDialog.clickOnCancelTopButton();
//
//        then: "dialog is closing"
//        !contentPublishDialog.isOpened();
//    }

//    def "GIVEN parent content in root is selected WHEN 'Content Publish' dialog has been opened THEN expected content-name should be present in the dialog"()
//    {
//        given: "parent content in the root is selected"
//        findAndSelectContent( PARENT_FOLDER.getName() );
//
//        when: "'Content Publish' dialog has been opened"
//        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
//            Application.EXPLICIT_NORMAL );
//        List<String> names = contentPublishDialog.getNamesOfContentsToPublish();
//        saveScreenshot( "publish-dialog-opened" );
//
//        then: "only one name of content should be present on the dialog"
//        names.size() == 1;
//
//        and: "expected content-name should be displayed in the dialog"
//        names.get( 0 ).contains( PARENT_FOLDER.getName() );
//    }

//    def "GIVEN parent folder in root is selected AND 'Content publish' dialog is opened WHEN 'include child' icon has been pressed THEN one dependant item should be displayed in the dialog"()
//    {
//        given: "parent folder is selected and 'Publish Dialog' is opened"
//        findAndSelectContent( PARENT_FOLDER.getName() );
//        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
//            Application.EXPLICIT_NORMAL );
//
//        when: "'include child' icon has been pressed"
//        contentPublishDialog.includeChildren( true );
//        and: "Show Dependant Items link has been clicked"
//        contentPublishDialog.clickOnShowDependentItemsLink();
//        List<String> dependant = contentPublishDialog.getDependantList();
//        saveScreenshot( "publish-dialog-dependencies" );
//
//        then: "'Other items that will be published' message should be displayed"
//        contentPublishDialog.getDependenciesListMessage() == ContentPublishDialog.OTHER_ITEMS_WILL_BE_PUBLISHED_TEXT;
//
//        and: "one expected dependant should be present"
//        dependant.size() == 1;
//
//        and: "expected name of the dependency should be displayed"
//        dependant.get( 0 ).contains( CHILD_FOLDER.getName() );
//    }

//    def "GIVEN existing child content WHEN the content has been selected and 'Publish' button pressed THEN name of parent folder should be present in the dialog"()
//    {
//        given: "existing child content"
//        filterPanel.typeSearchText( CHILD_FOLDER.getName() );
//
//        when: "child content has been selected and 'Publish' button pressed"
//        contentBrowsePanel.clickCheckboxAndSelectRow( CHILD_FOLDER.getName() );
//        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
//            Application.EXPLICIT_NORMAL );
//        contentPublishDialog.clickOnShowDependentItemsLink();
//        List<String> dependant = contentPublishDialog.getDependantList();
//        saveScreenshot( "publish-dialog-dependencies-child" );
//
//        then: "The header of 'Dependencies list' should be present"
//        contentPublishDialog.isDependenciesListHeaderDisplayed();
//
//        and: "expected message should be present in the dialog's header"
//        contentPublishDialog.getDependenciesListMessage() == ContentPublishDialog.OTHER_ITEMS_WILL_BE_PUBLISHED_TEXT;
//
//        and: "one dependency should be present in the dialog"
//        dependant.size() == 1;
//
//        and: "name of parent folder should be displayed"
//        dependant.get( 0 ).contains( PARENT_FOLDER.getName() );
//    }

//    def "GIVEN parent content is selected and 'Publish' button clicked WHEN one dependant item has been removed THEN dependants list should not be displayed"()
//    {
//        given: "parent content is selected"
//        findAndSelectContent( PARENT_FOLDER.getName() );
//        and: "Publish dialog is opened"
//        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
//            Application.EXPLICIT_NORMAL );
//        and: "'include children' has been clicked"
//        contentPublishDialog.includeChildren( true );
//        contentPublishDialog.clickOnShowDependentItemsLink();
//
//        when: "'remove' button has been clicked and one dependant removed"
//        contentPublishDialog.removeDependant( CHILD_FOLDER.getName() );
//        saveScreenshot( "dependant_removed" );
//
//        then: "Dependants list-header should not be displayed, because all dependants were removed"
//        !contentPublishDialog.isDependenciesListHeaderDisplayed();
//
//        and: "Dependants list should not be displayed, because all dependants were removed"
//        !contentPublishDialog.isDependantsDisplayed();
//    }

    def "GIVEN existing parent folder is selected and 'PublishDialog' is opened WHEN dependant item has been removed AND 'Publish' button has been pressed THEN only parent folder should be 'Published'"()
    {
        given: "existing parent folder is selected"
        findAndSelectContent( PARENT_FOLDER.getName() );
        and: "the folder is expanded"
        contentBrowsePanel.expandContent( ContentPath.from( PARENT_FOLDER.getName() ) )

        and: "Publish dialog is opened"
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL );
        and: "'include children' has been clicked"
        contentPublishDialog.includeChildren( true );
        contentPublishDialog.clickOnShowDependentItemsLink();

        when: "'remove' button has been clicked and the dependant was removed"
        contentPublishDialog.removeDependant( CHILD_FOLDER.getName() );

        and: "Publish button has been pressed"
        contentPublishDialog.clickOnPublishButton().waitForDialogClosed();
        saveScreenshot( "dependant_not_published" );

        then: "parent folder should be 'Published'"
        contentBrowsePanel.getContentStatus( PARENT_FOLDER.getName() ) == ContentStatus.PUBLISHED.getValue();

        and: "child folder should be 'New', because the dependant item was removed"
        contentBrowsePanel.getContentStatus( CHILD_FOLDER.getName() ) == ContentStatus.NEW.getValue();
    }

    def "GIVEN wizard for new folder is opened WHEN name input is empty THEN publish button should not be displayed on the wizard-toolbar"()
    {
        when:
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName() );

        then: "publish button should not be displayed on the wizard-toolbar"
        !wizard.waitForPublishButtonVisible( Application.EXPLICIT_1 );
    }
}
