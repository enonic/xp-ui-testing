package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.EditPermissionsDialog
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.security.ContentAclEntry
import com.enonic.autotests.vo.contentmanager.security.PermissionSuite
import com.enonic.autotests.vo.usermanager.RoleName
import com.enonic.autotests.vo.usermanager.SystemUserName
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created on 1/31/2017.
 *
 * Tasks:
 * XP-2845 Create selenium tests for "Overwrite child permission" feature(Edit Permissions Dialog)
 * xp-ui-testing#4 Check fixed application's bugs and add Selenium tests for each fixed bugs
 * xp-ui-testing#54 Add selenium tests to verify xp#5063
 *
 * Verifies:
 * XP-4932 Impossible to save changes when 'Overwrite child permissions' was set in true
 * XP-4930 Security wizard-step-form not refreshed in a child content, when permissions were changed in the parent content
 * */
@Stepwise
class ContentWizard_Overwrite_Child_Permissions_Spec
    extends BaseContentSpec
{

    @Shared
    Content PARENT_FOLDER;

    @Shared
    Content CHILD_FOLDER;

    @Shared
    String USER_ADMIN_ROLE = "/roles/system.user.admin";

    //verifies 'Edit Permissions Dialog - 'Overwrite child permissions' checkbox is always disabled #5165"
    def "GIVEN 'Edit Permissions Dialog' is opened WHEN 'Overwrite child permissions' has been clicked THEN 'Apply' button on the dialog should be enabled"()
    {
        given: "Content wizard is opened"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );
        and: "'Edit Permissions Dialog' is opened"
        EditPermissionsDialog dialog = wizard.clickOnAccessTabLink().clickOnEditPermissionsButton();

        when: "'Overwrite child permissions' has been clicked"
        dialog.setOverwriteChildPermissionsCheckbox( true );

        then: "Apply button should be enabled"
        dialog.isApplyButtonEnabled();
    }
    //verifies XP-4932 Impossible to save changes when 'Overwrite child permissions' was set to true
    @Ignore
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
        EditPermissionsDialog dialog = wizard.clickOnAccessTabLink().clickOnEditPermissionsButton();

        and: "'Overwrite child permissions' is checked"
        dialog.setOverwriteChildPermissionsCheckbox( true ).clickOnApply();
        and: "the content is saved"
        wizard.save();
        and: "wizard is closing"
        wizard.executeCloseWizardScript();
        wizard.switchToBrowsePanelTab();

        then: "'Alert' with warning message should not be displayed"
        !wizard.isAlertPresent();
    }
    //verifies XP-4930 Security wizard-step-form not refreshed in a child content, when permissions were changed in the parent content
    def "GIVEN existing parent folder with a child is opened WHEN new permission as added in the parent AND 'Overwrite' is true THEN the permissions should be updated in the child folder"()
    {
        given: "existing parent folder with a child is opened"
        ContentWizardPanel parent = findAndSelectContent( PARENT_FOLDER.getName() ).clickToolbarEdit()
        parent.switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();
        ContentWizardPanel childFolder = findAndSelectContent( CHILD_FOLDER.getName() ).clickToolbarEdit();
        contentBrowsePanel.switchToBrowserTabByTitle( PARENT_FOLDER.getDisplayName() );

        when: " Anonymous acl-entry was added in the parent wizard"
        EditPermissionsDialog dialog = parent.clickOnAccessTabLink().clickOnEditPermissionsButton();
        ContentAclEntry anonymousEntry = ContentAclEntry.builder().principalName( SystemUserName.SYSTEM_ANONYMOUS.getValue() ).build();
        and: "'Overwrite child permissions' set in true for the parent folder AND new permission was added"
        dialog.setInheritPermissionsCheckbox( false ).setOverwriteChildPermissionsCheckbox( true ).addPermissionByClickingCheckbox(
            anonymousEntry ).clickOnApply();
        and: "parent folder has been saved"
        parent.save();
        and: "navigate to the child wizard-tab"
        contentBrowsePanel.switchToBrowserTabByTitle( CHILD_FOLDER.getDisplayName() );
        saveScreenshot( "inherit_perm_true_child_permissions_updated" );

        then: "the same entry should appear in the child content"
        ContentAclEntry expected = ContentAclEntry.builder().principalName( "/system/users/anonymous" ).suite(
            PermissionSuite.CAN_READ ).build();
        childFolder.getAclEntries().contains( expected );
    }

    def "GIVEN existing child folder is opened AND 'Edit Permission' dialog is opened WHEN 'Inherit permissions' set in false AND new Permission was added in the child THEN the permissions should be displayed on the wizard"()
    {
        given: "existing parent folder with a child is opened"
        ContentWizardPanel childWizard = findAndSelectContent( CHILD_FOLDER.getName() ).clickToolbarEdit();
        EditPermissionsDialog dialog = childWizard.clickOnAccessTabLink().clickOnEditPermissionsButton();
        ContentAclEntry userAdminRoleEntry = ContentAclEntry.builder().principalName( RoleName.USER_ADMINISTRATOR.getValue() ).build();

        when: "'Inherit permissions' set in false"
        dialog.setInheritPermissionsCheckbox( false );
        and: "User Administrator role was added"
        dialog.addPermissionByClickingCheckbox( userAdminRoleEntry ).clickOnApply();
        ContentAclEntry expectedEntryFromUI = ContentAclEntry.builder().principalName( USER_ADMIN_ROLE ).suite(
            PermissionSuite.CAN_READ ).build();

        and: "child folder has been saved"
        childWizard.save();
        saveScreenshot( "inherit_perm_false_perm_changed" );

        then: "expected role should be present on the Security form in the wizard"
        childWizard.getAclEntries().contains( expectedEntryFromUI );
    }

    def "GIVEN existing child folder is opened AND 'Edit Permission' dialog is opened WHEN 'Inherit permissions' set in true THEN permissions list should be updated to permissions from the parent folder"()
    {
        given: "existing parent folder with a child is opened"
        ContentWizardPanel childWizard = findAndSelectContent( CHILD_FOLDER.getName() ).clickToolbarEdit();

        and: "Edit Permissions dialog is opened"
        EditPermissionsDialog dialog = childWizard.clickOnAccessTabLink().clickOnEditPermissionsButton();

        when: "'Inherit permissions' set in true"
        dialog.setInheritPermissionsCheckbox( true );
        and: "Apply button has been pressed"
        dialog.clickOnApply();

        and: "the content has been saved"
        ContentAclEntry entryToRemove = ContentAclEntry.builder().principalName( USER_ADMIN_ROLE ).suite(
            PermissionSuite.CAN_READ ).build();
        childWizard.save();
        saveScreenshot( "inherit_perm_true_initial_perm_restored" );

        then: "permissions should be the same as in the parent folder"
        !childWizard.getAclEntries().contains( entryToRemove );
    }
    //verifies xp5400 (Confirmation Dialog should appear)
    def "GIVEN 'Edit Permissions Dialog' is opened WHEN changes is not saved AND 'Cancel'-top button pressed THEN Confirmation Dialog should not appear"()
    {
        given: "Content wizard is opened"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );
        and: "'Edit Permissions Dialog' is opened"
        EditPermissionsDialog editPermissionsButton = wizard.clickOnAccessTabLink().clickOnEditPermissionsButton();
        and: "changes is made"
        editPermissionsButton.setOverwriteChildPermissionsCheckbox( true );

        when: "'Cancel'-top button has been pressed"
        editPermissionsButton.clickOnCancelButtonTop();
        saveScreenshot( "edit_perm_confirmation_dialog" );

        then: "Confirmation Dialog should nod appear"
        ConfirmationDialog confirm = new ConfirmationDialog( getSession() );
        !confirm.isOpened();
    }

    def "GIVEN 'Edit Permissions Dialog' is opened WHEN changes is not saved AND 'Cancel' button pressed THEN Confirmation Dialog should appear"()
    {
        given: "Content wizard is opened"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );
        and: "'Edit Permissions Dialog' is opened"
        EditPermissionsDialog editPermissionsButton = wizard.clickOnAccessTabLink().clickOnEditPermissionsButton();
        and: "changes is made"
        editPermissionsButton.setOverwriteChildPermissionsCheckbox( true );

        when: "'Cancel'-bottom button has been pressed"
        editPermissionsButton.clickOnCancelButton();
        saveScreenshot( "edit_perm_confirmation_dialog" );

        then: "Confirmation Dialog should not appear"
        ConfirmationDialog confirm = new ConfirmationDialog( getSession() );
        !confirm.isOpened();
    }
}
