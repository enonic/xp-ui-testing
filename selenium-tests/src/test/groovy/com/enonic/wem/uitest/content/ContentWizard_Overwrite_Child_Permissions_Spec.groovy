package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.EditPermissionsDialog
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.security.ContentAclEntry
import com.enonic.autotests.vo.contentmanager.security.PermissionSuite
import com.enonic.autotests.vo.usermanager.SystemUserName
import spock.lang.Shared

/**
 * Created on 1/31/2017.
 *
 * Tasks:
 * XP-2845 Create selenium tests for "Overwrite child permission" feature(Edit Permissions Dialog)
 *
 * Verifies:
 * XP-4932 Impossible to save changes when 'Overwrite child permissions' was set in true
 * XP-4930 Security wizard-step-form not refreshed in a child content, when permissions were changed in the parent content
 * */
class ContentWizard_Overwrite_Child_Permissions_Spec
    extends BaseContentSpec
{

    @Shared
    Content PARENT_FOLDER;

    @Shared
    Content CHILD_FOLDER;

    //verifies XP-4932 Impossible to save changes when 'Overwrite child permissions' was set to true
    def "GIVEN existing parent folder with a child WHEN 'Edit Permissions' dialog for the parent folder is opened AND 'Overwrite child permissions' was set to true THEN checkbox should be checked when dialog is opened in the second time"()
    {
        given: "parent folder has been added"
        PARENT_FOLDER = buildFolderContent( "folder", "parent folder" );
        addContent( PARENT_FOLDER );

        and: "parent folder is selected"
        findAndSelectContent( PARENT_FOLDER.getName() );

        and: "one child folder was added as well"
        CHILD_FOLDER = buildFolderContent( "folder", "child folder" );
        addContent( CHILD_FOLDER );

        when: "Edit permissions dialog is opened"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        EditPermissionsDialog dialog = wizard.clickOnSecurityTabLink().clickOnEditPermissionsButton();

        and: "'Overwrite child permissions' is checked"
        dialog.setOverwriteChildPermissionsCheckbox( true ).clickOnApply();
        and: "the content is saved"
        wizard.save();
        and: "wizard is closing"
        wizard.executeCloseWizardScript();

        then: "'Alert' with warning message should not be displayed"
        wizard.isAlertPresent();
    }
    //verifies XP-4930 Security wizard-step-form not refreshed in a child content, when permissions were changed in the parent content
    def "GIVEN existing parent folder with a child is opened WHEN new permission as added in the parent AND 'Overwrite' is true THEN the permissions should be updated in the child folder"()
    {
        given: "existing parent folder with a child is opened"
        ContentWizardPanel parent = findAndSelectContent( PARENT_FOLDER.getName() ).clickToolbarEdit()
        parent.switchToBrowsePanelTab();
        contentBrowsePanel.clickOnClearSelection();
        ContentWizardPanel childFolder = findAndSelectContent( CHILD_FOLDER.getName() ).clickToolbarEdit();
        contentBrowsePanel.switchToBrowserTabByTitle( PARENT_FOLDER.getDisplayName() );

        when: " Anonymous acl-entry was added in the parent wizard"
        EditPermissionsDialog dialog = parent.clickOnSecurityTabLink().clickOnEditPermissionsButton();
        ContentAclEntry anonymousEntry = ContentAclEntry.builder().principalName( SystemUserName.SYSTEM_ANONYMOUS.getValue() ).build();
        dialog.setInheritPermissionsCheckbox( false ).setOverwriteChildPermissionsCheckbox( true ).addPermissionByClickingCheckbox(
            anonymousEntry ).clickOnApply();
        and: "parent folder has been saved"
        parent.save();
        and: "navigated to the child wizard-tab"
        contentBrowsePanel.switchToBrowserTabByTitle( CHILD_FOLDER.getDisplayName() );

        then: "the same entry should appear in the child content"
        ContentAclEntry expected = ContentAclEntry.builder().principalName( "/system/users/anonymous" ).suite(
            PermissionSuite.CAN_READ ).build();
        childFolder.getAclEntries().contains( expected );

    }
}
