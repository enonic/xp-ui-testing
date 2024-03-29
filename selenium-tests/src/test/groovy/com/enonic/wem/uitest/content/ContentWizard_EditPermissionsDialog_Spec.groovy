package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.BaseContentType
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.EditPermissionsDialog
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.security.ContentAclEntry
import com.enonic.autotests.vo.contentmanager.security.PermissionSuite
import com.enonic.autotests.vo.usermanager.RoleName
import com.enonic.xp.security.PrincipalKey
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
@Ignore
class ContentWizard_EditPermissionsDialog_Spec
    extends BaseContentSpec
{

    @Shared
    Content content;

    @Shared
    int DEFAULT_NUMBER_OF_ACL_ENTRIES = 3;

    //verifies xp #4752 Edit Permissions Dialog shows incorrect content's name
    def "GIVEN wizard for new folder is opened WHEN data has been typed and the content saved AND 'Edit Permissions' dialog is opened THEN correct name of folder should be present on the dialog"()
    {
        given: "wizard for new folder is opened"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName() );
        def testName = NameHelper.uniqueName( "folder" );

        when: "data has been typed and the content saved"
        wizard.typeDisplayName( testName ).save();
        and: " 'Edit Permissions' dialog is opened"
        EditPermissionsDialog dialog = wizard.clickOnEditPermissionsButton();

        then: "correct header should be displayed"
        dialog.getHeader() == EditPermissionsDialog.HEADER;
        and: "correct name of folder should be present on the dialog"
        dialog.getContentPath().contains( testName );
    }

    def "WHEN 'Edit Permissions' button on the wizard panel pressed THEN modal dialog appears"()
    {
        given: "new folder content has been added"
        content = buildFolderContent( "folder", "testPermDialog" );
        addContent( content );

        when: "'Edit Permissions' button on the wizard panel pressed"
        EditPermissionsDialog modalDialog = findAndSelectContent(
            content.getName() ).clickToolbarEditAndSwitchToWizardTab().clickOnEditPermissionsButton();
        saveScreenshot( "test_edit_perm_dialog_default" );

        then: "modal dialog should be opened"
        modalDialog.isOpened();

        and: "'inherit permissions ' checkbox present on dialog"
        modalDialog.isInheritPermissionsCheckboxDisplayed();

        and: "the checkbox should be checked"
        modalDialog.isInheritPermissionsCheckBoxChecked();

        and: "'Overwrite child permissions' checkbox should be present"
        modalDialog.isOverwriteChildPermissionsCheckboxDisplayed();

        and: "'Overwrite child permissions' checkbox should be unchecked"
        !modalDialog.isOverwriteChildPermissionsCheckBoxChecked();

        and: "'Apply' button should be disabled"
        !modalDialog.isApplyButtonEnabled();

        and: "'Cancel'-bottom button should be enabled"
        modalDialog.isCancelButtonEnabled();

        and: "'Cancel'-top button should be displayed"
        modalDialog.isCancelButtonTopDisplayed();
    }

    def "GIVEN 'Edit Permissions' dialog is opened WHEN 'inherit permissions' has been unchecked THEN options filter input should appear"()
    {
        given: "content was selected and 'Edit Permissions' dialog is opened"
        EditPermissionsDialog modalDialog = findAndSelectContent(
            content.getName() ).clickToolbarEditAndSwitchToWizardTab().clickOnEditPermissionsButton();

        when: "'inherit permissions' has been unchecked"
        modalDialog.setInheritPermissionsCheckbox( false );

        then: "the checkbox should be unchecked"
        !modalDialog.isInheritPermissionsCheckBoxChecked();

        and: "options filter for principals should appear, when 'Inherit Permissions' was unchecked"
        modalDialog.isPrincipalOptionsFilterDisplayed();
    }

    def "WHEN 'Edit Permissions' is opened THEN three default permissions should be displayed"()
    {
        when: "content selected and 'Edit Permissions' dialog has been opened"
        EditPermissionsDialog modalDialog = findAndSelectContent(
            content.getName() ).clickToolbarEditAndSwitchToWizardTab().clickOnEditPermissionsButton();
        List<String> principals = modalDialog.getPrincipalNames();
        saveScreenshot( "test_default_acl_entries" );

        then: "three default acl-entry should be displayed"
        principals.size() == DEFAULT_NUMBER_OF_ACL_ENTRIES;

        and: "expected acl-entries and actual are equal"
        List<ContentAclEntry> entriesActual = modalDialog.getAclEntries();
        entriesActual.equals( getExpectedDefaultPermissions() );
    }

    def "GIVEN 'Edit Permissions' dialog is opened WHEN new role has been added THEN new ACL entry with the role and 'Can Read' operations should appear on the dialog"()
    {
        given: "'Edit Permissions' dialog is opened"
        ContentWizardPanel wizard = findAndSelectContent( content.getName() ).clickToolbarEditAndSwitchToWizardTab();
        EditPermissionsDialog modalDialog = wizard.clickOnEditPermissionsButton();
        ContentAclEntry entry = ContentAclEntry.builder().principalName( RoleName.SYSTEM_USER_MANAGER.getValue() ).build();

        when: "new Role has been added"
        modalDialog.setInheritPermissionsCheckbox( false ).addPermissionByClickingCheckbox( entry ).clickOnApply();
        sleep( 500 );

        and: "dialog has been opened again"
        modalDialog = wizard.clickOnEditPermissionsButton();

        then: "number of ACL-entries on the modal dialog should be increased"
        List<ContentAclEntry> aclEntriesActual = modalDialog.getAclEntries();
        aclEntriesActual.size() == ( DEFAULT_NUMBER_OF_ACL_ENTRIES + 1 );

        and: "actual entries on the dialog and expected are the same"
        aclEntriesActual.containsAll( getUpdatedPermissions() );
    }

    def "'Edit Permissions' dialog is opened AND one role was removed WHEN try to close the wizard THEN alert dialog should appear"()
    {
        given: "'Edit Permissions' dialog is opened"
        ContentWizardPanel wizard = findAndSelectContent( content.getName() ).clickToolbarEditAndSwitchToWizardTab();
        EditPermissionsDialog modalDialog = wizard.clickOnEditPermissionsButton();

        and: "one Role was removed"
        modalDialog.removeAclEntry( RoleName.SYSTEM_USER_MANAGER.getValue() );

        and: "Apply button has been pressed"
        modalDialog.clickOnApply();
        modalDialog.waitForDialogClosed();
        sleep( 700 );

        when: "shortcut to 'Close' has been pressed"
        wizard.pressCloseKeyboardShortcut();

        then: "'Alert' dialog should not appear"
        !wizard.isAlertPresent();
    }

    def "GIVEN 'Edit Permissions' dialog is opened WHEN one acl entry has been removed THEN number of entries on the modal dialog should be reduced"()
    {
        given: "'Edit Permissions' dialog is opened"
        ContentWizardPanel wizard = findAndSelectContent( content.getName() ).clickToolbarEditAndSwitchToWizardTab();
        EditPermissionsDialog modalDialog = wizard.clickOnEditPermissionsButton();

        when: "one Role has been removed"
        modalDialog.removeAclEntry( RoleName.SYSTEM_ADMIN.getValue() );
        saveScreenshot( "content-wizard-role-was-removed" );
        and: "Apply button has been pressed"
        modalDialog.clickOnApply();
        modalDialog.waitForDialogClosed();

        and: "dialog has been reopened again"
        wizard.clickOnEditPermissionsButton();

        then: "number of entries on the modal dialog should be reduced"
        List<ContentAclEntry> aclEntries = modalDialog.getAclEntries();
        aclEntries.size() == DEFAULT_NUMBER_OF_ACL_ENTRIES - 1;

    }

    private List<ContentAclEntry> getExpectedDefaultPermissions()
    {
        List<ContentAclEntry> entries = new ArrayList<>();
        String principalPath1 = PrincipalKey.ofRole( RoleName.CM_APP.getValue() ).toPath().toString();
        String path = principalPath1.substring( principalPath1.indexOf( "/roles" ) );
        ContentAclEntry entry = ContentAclEntry.builder().principalName( path ).suite( PermissionSuite.CAN_READ ).build();
        entries.add( entry );

        String principalPath2 = PrincipalKey.ofRole( RoleName.SYSTEM_ADMIN.getValue() ).toPath().toString();
        path = principalPath2.substring( principalPath2.indexOf( "/roles" ) );
        entry = ContentAclEntry.builder().principalName( path ).suite( PermissionSuite.FULL_ACCESS ).build();
        entries.add( entry );
        String principalPath3 = PrincipalKey.ofRole( RoleName.CONTENT_MANAGER_ADMINISTRATOR.getValue() ).toPath().toString();
        path = principalPath3.substring( principalPath3.indexOf( "/roles" ) );
        entry = ContentAclEntry.builder().principalName( path ).suite( PermissionSuite.FULL_ACCESS ).build();
        entries.add( entry );
        return entries;
    }

    private List<ContentAclEntry> getUpdatedPermissions()
    {
        List<ContentAclEntry> entries = new ArrayList<>();
        String principalPath1 = PrincipalKey.ofRole( RoleName.CM_APP.getValue() ).toPath().toString();
        String path = principalPath1.substring( principalPath1.indexOf( "/roles" ) );
        ContentAclEntry entry = ContentAclEntry.builder().principalName( path ).suite( PermissionSuite.CAN_READ ).build();
        entries.add( entry );

        String principalPath2 = PrincipalKey.ofRole( RoleName.SYSTEM_ADMIN.getValue() ).toPath().toString();
        path = principalPath2.substring( principalPath2.indexOf( "/roles" ) );
        entry = ContentAclEntry.builder().principalName( path ).suite( PermissionSuite.FULL_ACCESS ).build();
        entries.add( entry );
        String principalPath3 = PrincipalKey.ofRole( RoleName.CONTENT_MANAGER_ADMINISTRATOR.getValue() ).toPath().toString();
        path = principalPath3.substring( principalPath3.indexOf( "/roles" ) );
        entry = ContentAclEntry.builder().principalName( path ).suite( PermissionSuite.FULL_ACCESS ).build();
        entries.add( entry );

        String principalPath4 = PrincipalKey.ofRole( RoleName.SYSTEM_USER_MANAGER.getValue() ).toPath().toString();
        path = principalPath4.substring( principalPath4.indexOf( "/roles" ) );
        entry = ContentAclEntry.builder().principalName( path ).suite( PermissionSuite.CAN_READ ).build();
        entries.add( entry );
        return entries;
    }
}
