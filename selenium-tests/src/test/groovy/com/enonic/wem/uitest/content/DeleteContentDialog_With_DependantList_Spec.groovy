package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class DeleteContentDialog_With_DependantList_Spec
    extends BaseContentSpec
{
    @Shared
    Content PARENT_CONTENT

    @Shared
    Content CHILD_CONTENT

    def "GIVEN parent content with a child WHEN the parent selected and 'Delete' on the toolbar pressed THEN 'Delete Content' dialog with dependant list appeared"()
    {
        given: "parent content added"
        PARENT_CONTENT = buildFolderContent( "folder", "delete with child" );
        addContent( PARENT_CONTENT );

        and: "one child content added"
        CHILD_CONTENT = buildFolderContentWithParent( "child", "dependant child folder", PARENT_CONTENT.getName() );
        findAndSelectContent( PARENT_CONTENT.getName() );
        addContent( CHILD_CONTENT );

        when: "parent content selected and 'Delete' on the toolbar pressed"
        DeleteContentDialog modalDialog = contentBrowsePanel.clickToolbarDelete();

        and: "'Show dependent items' link has been clicked"
        modalDialog.clickOnShowDependentItemsLink();
        List<String> items = modalDialog.getDisplayNamesToDelete();
        List<String> dependantNames = modalDialog.getDependantList();
        saveScreenshot( "delete_dialog_dependant1" );

        then: "'Delete Content' dialog with correct dependant list appears"
        dependantNames.get( 0 ).contains( CHILD_CONTENT.getName() );

        and: "one item present in the dependant list"
        dependantNames.size() == 1;

        and: "only one item to delete should be displayed"
        items.get( 0 ) == PARENT_CONTENT.getDisplayName();

        and: "'Hide Dependant items' link should be displayed"
        modalDialog.isHideDependantItemsLinkDisplayed();

        and: "block of dependant-list 'Other items that will be deleted'  should be displayed"
        modalDialog.isDependantListDisplayed();

    }

    def "GIVEN parent content with a child is opened WHEN 'delete' button on the wizard-toolbar pressed THEN 'Delete Content' dialog with dependant list appeared"()
    {
        given: "parent content with a child is opened"
        ContentWizardPanel wizard = findAndSelectContent( PARENT_CONTENT.getName() ).clickToolbarEditAndSwitchToWizardTab(); ;

        when: "'Delete' button on the wizard-toolbar pressed"
        DeleteContentDialog modalDialog = wizard.clickToolbarDelete();

        and: "'Show dependent items' link has been clicked"
        modalDialog.clickOnShowDependentItemsLink();
        List<String> items = modalDialog.getDisplayNamesToDelete();
        List<String> dependantNames = modalDialog.getDependantList();
        saveScreenshot( "delete_dialog_dependant2" );

        then: "'Delete Content' dialog with correct dependant list appears"
        dependantNames.get( 0 ).contains( CHILD_CONTENT.getName() );

        and: "one item present in the dependant list"
        dependantNames.size() == 1;

        and: "only one item to delete is displayed"
        items.get( 0 ) == PARENT_CONTENT.getDisplayName();

        and: "'Hide Dependant items' link should be displayed"
        modalDialog.isHideDependantItemsLinkDisplayed();

        and: "block of dependant-list 'Other items that will be deleted'  should be displayed"
        modalDialog.isDependantListDisplayed();
    }
}
