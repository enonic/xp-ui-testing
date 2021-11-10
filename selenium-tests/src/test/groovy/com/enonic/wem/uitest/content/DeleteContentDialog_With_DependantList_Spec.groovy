package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.DeleteContentDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
@Ignore
class DeleteContentDialog_With_DependantList_Spec
    extends BaseContentSpec
{
    @Shared
    Content PARENT_CONTENT

    @Shared
    Content CHILD_CONTENT

    def "GIVEN parent content with a child WHEN the parent selected and 'Delete' on the toolbar pressed THEN 'Delete Content' dialog with dependant list should appear"()
    {
        given: "parent content is added"
        PARENT_CONTENT = buildFolderContent( "folder", "delete with child" );
        addContent( PARENT_CONTENT );

        and: "child content is added"
        CHILD_CONTENT = buildFolderContentWithParent( "child", "dependant child folder", PARENT_CONTENT.getName() );
        findAndSelectContent( PARENT_CONTENT.getName() );
        addContent( CHILD_CONTENT );

        when: "parent content is selected and 'Delete' in the browse toolbar has been pressed"
        DeleteContentDialog modalDialog = contentBrowsePanel.clickToolbarArchive();

        and: "'Hide dependent items' link should be present"
        modalDialog.isHideDependantItemsLinkDisplayed();
        List<String> items = modalDialog.getDisplayNamesToDelete();
        List<String> dependantNames = modalDialog.getDependantList();
        saveScreenshot( "delete_dialog_dependant1" );

        then: "'Delete Content' dialog with expected dependant list appears"
        dependantNames.get( 0 ).contains( CHILD_CONTENT.getName() );

        and: "one item present in the dependant list"
        dependantNames.size() == 1;

        and: "only one item to delete should be displayed"
        items.get( 0 ) == PARENT_CONTENT.getDisplayName();


        and: "block of dependant-list 'Other items that will be deleted'  should be displayed"
        modalDialog.isDependantListDisplayed();

    }

    def "GIVEN parent content with a child is opened WHEN 'delete' button on the wizard-toolbar pressed THEN 'Delete Content' dialog with dependant list appeared"()
    {
        given: "parent content with a child is opened"
        ContentWizardPanel wizard = findAndSelectContent( PARENT_CONTENT.getName() ).clickToolbarEditAndSwitchToWizardTab(); ;

        when: "'Delete' button on the wizard-toolbar pressed"
        DeleteContentDialog modalDialog = wizard.clickToolbarArchive();

        and: "'Hide dependent items' link has been clicked"
        modalDialog.clickOnHideDependentItemsLink();
        List<String> items = modalDialog.getDisplayNamesToDelete();
        List<String> dependantNames = modalDialog.getDependantList();
        saveScreenshot( "delete_dialog_hide_link_clicked" );

        then: "'Show dependent items' link gets visible"
        modalDialog.isShowDependantItemsLinkDisplayed();

        and: "dependant list should be empty"
        dependantNames.size() == 0;

        and: "only one item to delete should be displayed"
        items.get( 0 ) == PARENT_CONTENT.getDisplayName();

        and: "'Hide Dependant items' link should not be displayed"
        !modalDialog.isHideDependantItemsLinkDisplayed();

        and: "message 'Other items that will be deleted'  should not be displayed"
        !modalDialog.isDependantListDisplayed();
    }
}
