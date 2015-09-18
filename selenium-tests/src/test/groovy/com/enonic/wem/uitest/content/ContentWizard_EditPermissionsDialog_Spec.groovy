package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.EditPermissionsDialog
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

    def "setup: add a test folder"()
    {
        when:
        content = buildFolderContent( "folder", "testPermDialog" );
        addContent( content );

        then:
        contentBrowsePanel.exists( content.getName() );
    }

    def "WHEN 'Edit Permissions' button pressed THEN modal dialog appears "()
    {
        when:
        filterPanel.typeSearchText( content.getName() )
        EditPermissionsDialog modalDialog = contentBrowsePanel.clickAndSelectRow(
            content.getName() ).<ContentWizardPanel> clickToolbarEdit().clickOnSecurityTabLink().clickOnEditPermissionsButton();

        then:
        modalDialog.isOpened();
    }

    def "WHEN 'Edit Permissions' dialog opened THEN 'inherit permissions ' checkbox present on dialog and it checked "()
    {
        when:
        filterPanel.typeSearchText( content.getName() )
        EditPermissionsDialog modalDialog = contentBrowsePanel.clickAndSelectRow(
            content.getName() ).<ContentWizardPanel> clickToolbarEdit().clickOnSecurityTabLink().clickOnEditPermissionsButton();

        then:
        modalDialog.isInheritPermissionsCheckboxDisplayed();
        and:
        modalDialog.isInheritCheckBoxChecked();
    }


    def "GIVEN 'Edit Permissions' dialog opened WHEN 'inherit permissions' unchecked  THEN options filter input appears "()
    {
        given:
        filterPanel.typeSearchText( content.getName() );
        EditPermissionsDialog modalDialog = contentBrowsePanel.clickAndSelectRow(
            content.getName() ).<ContentWizardPanel> clickToolbarEdit().clickOnSecurityTabLink().clickOnEditPermissionsButton();

        when:
        modalDialog.setCheckedForInheritCheckbox( false );

        then:
        !modalDialog.isInheritCheckBoxChecked();
        and:
        modalDialog.isOptionsFilterDisplayed();
    }

    def "WHEN 'Edit Permissions' opened THEN two default permissions displayed "()
    {
        when:
        filterPanel.typeSearchText( content.getName() )
        EditPermissionsDialog modalDialog = contentBrowsePanel.clickAndSelectRow(
            content.getName() ).<ContentWizardPanel> clickToolbarEdit().clickOnSecurityTabLink().clickOnEditPermissionsButton();

        List<String> principals = modalDialog.getPrincipalNames();
        then:
        principals.size() == DEFAULT_NUMBER_OF_ACL_ENTRIES;
        and:
        modalDialog.getAclEntries().equals( getExpected() );
    }

    def "GIVEN 'Edit Permissions' opened WHEN new role added THEN new ACL entry with new role and 'Can Read' operations appears"()
    {
        given:
        filterPanel.typeSearchText( content.getName() )
        EditPermissionsDialog modalDialog = contentBrowsePanel.clickAndSelectRow(
            content.getName() ).<ContentWizardPanel> clickToolbarEdit().clickOnSecurityTabLink().clickOnEditPermissionsButton();
        ContentAclEntry entry = ContentAclEntry.builder().principalName( RoleName.SYSTEM_USER_MANAGER.getValue() ).build();

        when: "new acl-entry added"
        modalDialog.setCheckedForInheritCheckbox( false ).addPermission( entry );

        then:
        List<ContentAclEntry> aclEntries = modalDialog.getAclEntries();
        aclEntries.size() == ( DEFAULT_NUMBER_OF_ACL_ENTRIES + 1 );
    }

    def "GIVEN 'Edit Permissions' opened WHEN one acl entry deleted THEN not present on dialog"()
    {
        given:
        filterPanel.typeSearchText( content.getName() )
        EditPermissionsDialog modalDialog = contentBrowsePanel.clickAndSelectRow(
            content.getName() ).<ContentWizardPanel> clickToolbarEdit().clickOnSecurityTabLink().clickOnEditPermissionsButton();
        ContentAclEntry entry = ContentAclEntry.builder().principalName( RoleName.SYSTEM_USER_MANAGER.getValue() ).build();
        modalDialog.setCheckedForInheritCheckbox( false ).addPermission( entry );

        when: "acl-entry removed"
        modalDialog.removeAclEntry( RoleName.SYSTEM_USER_MANAGER.getValue() );

        then:
        List<ContentAclEntry> aclEntries = modalDialog.getAclEntries();
        aclEntries.size() == DEFAULT_NUMBER_OF_ACL_ENTRIES;
    }

    private List<ContentAclEntry> getExpected()
    {
        List<ContentAclEntry> entries = new ArrayList<>();
        String principalPath1 = PrincipalKey.ofRole( RoleName.SYSTEM_ADMIN.getValue() ).toPath().toString();
        ContentAclEntry entry = ContentAclEntry.builder().principalName( principalPath1 ).suite( PermissionSuite.FULL_ACCESS ).build();
        entries.add( entry );
        String principalPath2 = PrincipalKey.ofRole( RoleName.CMS_ADMIN.getValue() ).toPath().toString();
        entry = ContentAclEntry.builder().principalName( principalPath2 ).suite( PermissionSuite.FULL_ACCESS ).build();
        entries.add( entry );

        return entries;
    }
}
