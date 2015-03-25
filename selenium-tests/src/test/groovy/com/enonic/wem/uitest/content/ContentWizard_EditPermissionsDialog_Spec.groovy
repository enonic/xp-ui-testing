package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.EditPermissionsDialog
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.security.ContentAclEntry
import com.enonic.autotests.vo.contentmanager.security.PermissionSuite
import com.enonic.autotests.vo.usermanager.RoleName
import com.enonic.xp.content.ContentPath
import com.enonic.xp.schema.content.ContentTypeName
import com.enonic.xp.security.PrincipalKey
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentWizard_EditPermissionsDialog_Spec
    extends BaseContentSpec
{
    @Shared
    String CONTENT_NAME = NameHelper.uniqueName( "folder" );

    @Shared
    Content content

    def "setup: add a test folder"()
    {
        when:
        content = Content.builder().
            parent( ContentPath.ROOT ).
            name( CONTENT_NAME ).
            displayName( "testPermDialogFolder" ).
            contentType( ContentTypeName.folder() ).
            build();

        contentBrowsePanel.clickToolbarNew().selectContentType( content.getContentTypeName() ).typeData( content ).save().close(
            content.getDisplayName() );

        then:
        contentBrowsePanel.exists( content.getPath() );


    }

    def "WHEN 'Edit Permissions' button pressed THEN modal dialog appears "()
    {
        when:
        filterPanel.typeSearchText( CONTENT_NAME )
        EditPermissionsDialog modalDialog = contentBrowsePanel.clickAndSelectRow(
            CONTENT_NAME ).<ContentWizardPanel> clickToolbarEdit().clickOnSecurityTabLink().clickOnEditPermissionsButton();

        then:
        modalDialog.isOpened();
    }

    def "WHEN 'Edit Permissions' dialog opened THEN 'inherit permissions ' checkbox present on dialog and it checked "()
    {
        when:
        filterPanel.typeSearchText( CONTENT_NAME )
        EditPermissionsDialog modalDialog = contentBrowsePanel.clickAndSelectRow(
            CONTENT_NAME ).<ContentWizardPanel> clickToolbarEdit().clickOnSecurityTabLink().clickOnEditPermissionsButton();

        then:
        modalDialog.isInheritPermissionsCheckboxDisplayed();
        and:
        modalDialog.isInheritCheckBoxChecked();
    }


    def "GIVEN 'Edit Permissions' dialog opened WHEN 'inherit permissions' unchecked  THEN options filter input appears "()
    {
        given:
        filterPanel.typeSearchText( CONTENT_NAME )
        EditPermissionsDialog modalDialog = contentBrowsePanel.clickAndSelectRow(
            CONTENT_NAME ).<ContentWizardPanel> clickToolbarEdit().clickOnSecurityTabLink().clickOnEditPermissionsButton();

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
        filterPanel.typeSearchText( CONTENT_NAME )
        EditPermissionsDialog modalDialog = contentBrowsePanel.clickAndSelectRow(
            CONTENT_NAME ).<ContentWizardPanel> clickToolbarEdit().clickOnSecurityTabLink().clickOnEditPermissionsButton();

        List<String> principals = modalDialog.getPrincipalNames();
        then:
        principals.size() == 2;
        and:
        modalDialog.getAclEntries().equals( getExpected() );
    }

    def "GIVEN 'Edit Permissions' opened WHEN new role added THEN new ACL entry with new role and 'Can Read' operations appears"()
    {
        given:
        filterPanel.typeSearchText( CONTENT_NAME )
        EditPermissionsDialog modalDialog = contentBrowsePanel.clickAndSelectRow(
            CONTENT_NAME ).<ContentWizardPanel> clickToolbarEdit().clickOnSecurityTabLink().clickOnEditPermissionsButton();
        ContentAclEntry entry = ContentAclEntry.builder().principalName( RoleName.SYSTEM_USER_MANAGER.getValue() ).build();

        when: "new acl-entry added"
        modalDialog.setCheckedForInheritCheckbox( false ).addPermission( entry );

        then:
        List<ContentAclEntry> aclEntries = modalDialog.getAclEntries();
        aclEntries.size() == 3;


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
