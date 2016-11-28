package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.EditPermissionsDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.SecurityWizardStepForm
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.security.ContentAclEntry
import com.enonic.autotests.vo.contentmanager.security.PermissionSuite
import com.enonic.autotests.vo.usermanager.RoleName
import com.enonic.xp.security.PrincipalKey
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentWizard_EditPermissionsDialog_Spec
    extends BaseContentSpec
{

    @Shared
    Content content;

    @Shared
    int DEFAULT_NUMBER_OF_ACL_ENTRIES = 3;

    def "WHEN 'Edit Permissions' button on the wizard panel pressed THEN modal dialog appears"()
    {
        given: "new folder content has been added"
        content = buildFolderContent( "folder", "testPermDialog" );
        addContent( content );

        when: "'Edit Permissions' button on the wizard panel pressed"
        EditPermissionsDialog modalDialog = findAndSelectContent(
            content.getName() ).clickToolbarEditAndSwitchToWizardTab().clickOnSecurityTabLink().clickOnEditPermissionsButton();
        saveScreenshot( "test_edit_perm_dialog_default" );

        then: "modal dialog appears"
        modalDialog.isOpened();

        and: "'inherit permissions ' checkbox present on dialog"
        modalDialog.isInheritPermissionsCheckboxDisplayed();

        and: "the checkbox is checked"
        modalDialog.isInheritCheckBoxChecked();
    }

    def "GIVEN 'Edit Permissions' dialog opened WHEN 'inherit permissions' unchecked  THEN options filter input appears "()
    {
        given: "content selected and 'Edit Permissions' dialog opened"
        EditPermissionsDialog modalDialog = findAndSelectContent(
            content.getName() ).clickToolbarEditAndSwitchToWizardTab().clickOnSecurityTabLink().clickOnEditPermissionsButton();

        when: "'inherit permissions' has been unchecked"
        modalDialog.setCheckedForInheritCheckbox( false );

        then:
        !modalDialog.isInheritCheckBoxChecked();

        and: "options filter input appears"
        modalDialog.isOptionsFilterDisplayed();
    }

    def "WHEN 'Edit Permissions' opened THEN two default permissions displayed "()
    {
        when: "content selected and 'Edit Permissions' dialog has been opened"
        EditPermissionsDialog modalDialog = findAndSelectContent(
            content.getName() ).clickToolbarEditAndSwitchToWizardTab().clickOnSecurityTabLink().clickOnEditPermissionsButton();
        List<String> principals = modalDialog.getPrincipalNames();
        saveScreenshot( "test_default_acl_entries" );

        then: "two default acl-entry are displayed"
        principals.size() == DEFAULT_NUMBER_OF_ACL_ENTRIES;

        and: "expected acl-entries and actual are equal"
        List<ContentAclEntry> entriesActual = modalDialog.getAclEntries();
        entriesActual.equals( getExpectedDefaultPermissions() );
    }

    def "GIVEN 'Edit Permissions' opened WHEN checkbox selected AND role selected and AND 'Apply' button in the selector pressed THEN new ACL entry with new role and 'Can Read' operations appears"()
    {
        given: "'Edit Permissions' opened"
        SecurityWizardStepForm securityForm = findAndSelectContent(
            content.getName() ).clickToolbarEditAndSwitchToWizardTab().clickOnSecurityTabLink();
        EditPermissionsDialog modalDialog = securityForm.clickOnEditPermissionsButton();
        ContentAclEntry entry = ContentAclEntry.builder().principalName( RoleName.SYSTEM_USER_MANAGER.getValue() ).build();

        when: "new acl-entry added"
        modalDialog.setCheckedForInheritCheckbox( false ).addPermissionByClickingCheckbox( entry ).clickOnApply();
        sleep( 500 );
        modalDialog = securityForm.clickOnEditPermissionsButton();

        then: "number of ACL-entries increased"
        List<ContentAclEntry> aclEntriesActual = modalDialog.getAclEntries();
        aclEntriesActual.size() == ( DEFAULT_NUMBER_OF_ACL_ENTRIES + 1 );

        and: "actual entries and expected are the same"
        aclEntriesActual.containsAll( getUpdatedPermissions() );
    }

    def "GIVEN existing folder with one added ACL-entry AND 'Edit Permissions' opened WHEN one acl entry removed THEN number of entries reduced to default"()
    {
        given: "existing folder with one added ACL-entry"
        SecurityWizardStepForm securityForm = findAndSelectContent(
            content.getName() ).clickToolbarEditAndSwitchToWizardTab().clickOnSecurityTabLink();
        EditPermissionsDialog modalDialog = securityForm.clickOnEditPermissionsButton();
        modalDialog.setCheckedForInheritCheckbox( false );

        when: "one acl-entry removed"
        modalDialog.removeAclEntry( RoleName.SYSTEM_USER_MANAGER.getValue() );
        saveScreenshot( "acl-removed" )
        modalDialog.clickOnApply();

        and: "dialog opened again"
        modalDialog = securityForm.clickOnEditPermissionsButton();

        then: "number of entries reduced to default"
        List<ContentAclEntry> aclEntries = modalDialog.getAclEntries();
        aclEntries.size() == DEFAULT_NUMBER_OF_ACL_ENTRIES;

        and: "actual entries contains expected entries"
        aclEntries.containsAll( getExpectedDefaultPermissions() );
    }

    def "GIVEN 'Edit Permissions' opened WHEN one more role added THEN new ACL entry with new role and 'Can Read' operations appears"()
    {
        given: "content selected and 'Edit Permissions' opened"
        SecurityWizardStepForm securityForm = findAndSelectContent(
            content.getName() ).clickToolbarEditAndSwitchToWizardTab().clickOnSecurityTabLink();
        EditPermissionsDialog modalDialog = securityForm.clickOnEditPermissionsButton();
        ContentAclEntry entry = ContentAclEntry.builder().principalName( RoleName.SYSTEM_USER_MANAGER.getValue() ).build();

        when: "one more acl-entry added"
        modalDialog.setCheckedForInheritCheckbox( false ).addPermission( entry );
        sleep( 500 );
        saveScreenshot( "acl-added" );

        then: "number of ACL-entries increased"
        List<ContentAclEntry> aclEntriesActual = modalDialog.getAclEntries();
        aclEntriesActual.size() == ( DEFAULT_NUMBER_OF_ACL_ENTRIES + 1 );

        and: "actual entries and expected are the same"
        aclEntriesActual.containsAll( getUpdatedPermissions() );
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
