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

        then: "'Include Child' button should be displayed displayed"
        contentPublishDialog.isIncludeChildTogglerDisplayed();

        and: "correct status of content is displayed"
        contentPublishDialog.getContentStatus( folderContent.getDisplayName() ) == ContentStatus.OFFLINE.getValue();
    }

    def "GIVEN parent content with a child WHEN the parent content selected and 'Publish' button clicked THEN 'Content publish' appears with correct control elements"()
    {
        setup:
        parentContent = buildFolderContent( "publish_dialog", "content publish dialog" );
        addContent( parentContent );

        and:
        filterPanel.typeSearchText( parentContent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( parentContent.getName() )
        childContent1 = buildFolderContentWithParent( "publish_dialog", "child-folder1", parentContent.getName() );
        addContent( childContent1 );

        when: "parent content selected and 'Publish' button pressed"
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL );

        then: "'ContentPublishDialog' dialog displayed"
        contentPublishDialog.isOpened();

        and: "dialog has a correct title"
        contentPublishDialog.getTitle() == ContentPublishDialog.DIALOG_TITLE

        and: "dialog has 'Publish' and 'Cancel' buttons"
        contentPublishDialog.isPublishNowButtonEnabled();

        and:
        contentPublishDialog.isCancelButtonBottomEnabled();

        and:
        contentPublishDialog.isCancelButtonTopEnabled();

        and: "Dependants should not be displayed"
        !contentPublishDialog.isDependantsDisplayed();

    }

    def "GIVEN 'Content Publish' dialog shown WHEN the cancel button on the bottom clicked THEN dialog not present"()
    {
        given: "parent content selected and 'Publish' button pressed"
        findAndSelectContent( parentContent.getName() );
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL );

        when: "button 'Cancel' on the bottom of dialog pressed"
        contentPublishDialog.clickOnCancelBottomButton();

        then: "dialog not present"
        !contentPublishDialog.isOpened();
    }

    def "GIVEN 'Content Publish' dialog shown WHEN the button cancel on the top clicked THEN dialog not present"()
    {
        given: "parent content selected and 'Publish' button pressed"
        findAndSelectContent( parentContent.getName() );
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL );

        when: "button 'Cancel' on the top of dialog pressed"
        contentPublishDialog.clickOnCancelTopButton();

        then: "dialog not present"
        !contentPublishDialog.isOpened();
    }

    def "GIVEN one parent content on root selected WHEN 'Content Publish' dialog opened THEN correct name of content present in the dialog"()
    {
        given: "one parent content in the root selected"
        findAndSelectContent( parentContent.getName() );

        when: "'Content Publish' dialog opened"
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL );
        List<String> names = contentPublishDialog.getNamesOfContentsToPublish();
        saveScreenshot( "publish-dialog-opened" );

        then: "only one name of content displayed"
        names.size() == 1;

        and: "correct name of content should be displayed on the dialog"
        names.get( 0 ).contains( parentContent.getName() );
    }

    def "GIVEN a parent content on root is selected AND 'Content publish' dialog is opened WHEN 'include child' icon was pressed THEN correct text shown in the header of 'dependant list' and one dependant item should be displayed"()
    {
        given: "parent folder is selected and 'Publish' button pressed"
        findAndSelectContent( parentContent.getName() );
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL );

        when: "'include child' icon was pressed"
        contentPublishDialog.includeChildren( true );
        List<String> dependant = contentPublishDialog.getDependantList();
        saveScreenshot( "publish-dialog-dependencies" );

        then: "The header of 'Dependencies list' should appear"
        contentPublishDialog.isDependenciesListHeaderDisplayed();

        and: "correct text should be shown in the header"
        contentPublishDialog.getDependenciesListHeader() == ContentPublishDialog.OTHER_ITEMS_WILL_BE_PUBLISHED;

        and: "one correct dependant should be shown"
        dependant.size() == 1;

        and: "correct name of the dependency is displayed"
        dependant.get( 0 ).contains( childContent1.getName() );
    }

    def "GIVEN existing child content WHEN the content was selected and 'Publish' button pressed THEN correct text should be present in the header of 'dependencies list' and name of the parent folder should be present"()
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

        and: "one correct dependency should be shown "
        dependant.size() == 1;

        and: "name of the parent folder should be present"
        dependant.get( 0 ).contains( parentContent.getName() );
    }
}
