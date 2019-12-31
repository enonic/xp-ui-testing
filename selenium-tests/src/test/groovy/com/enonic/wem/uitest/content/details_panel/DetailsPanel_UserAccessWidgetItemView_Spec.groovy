package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.UserAccessWidgetItemView
import com.enonic.autotests.pages.contentmanager.wizardpanel.EditPermissionsDialog
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.security.ContentAclEntry
import com.enonic.autotests.vo.usermanager.SystemUserName
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

class DetailsPanel_UserAccessWidgetItemView_Spec
    extends BaseContentSpec
{
    @Shared
    Content FOLDER_CONTENT;

    def "GIVEN new folder is selected WHEN details panel has been opened THEN UserAccessWidgetItemView should appear"()
    {
        given: "new folder is selected"
        FOLDER_CONTENT = buildFolderContent( "folder", "user access widget" );
        addContent( FOLDER_CONTENT );
        findAndSelectContent( FOLDER_CONTENT.getName() );

        when: "details panel has been opened"
        contentBrowsePanel.openContentDetailsPanel();
        UserAccessWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getUserAccessWidgetItemView();

        then: "'user access' widget should be displayed"
        view.isDisplayed();

        and: "'Edit Permissions' link should be present in the widget"
        view.isEditPermissionsLinkDisplayed();
    }

    def "GIVEN existing executable content with permissions for 'Everyone' WHEN details panel opened THEN expected info should be present"()
    {
        when: "existing executable content with permissions for 'Everyone' is selected"
        findAndSelectContent( EXECUTABLE_BAT );
        contentBrowsePanel.openContentDetailsPanel();

        and: "User Access widget was selected in the details panel"
        UserAccessWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getUserAccessWidgetItemView();
        saveScreenshot( "everyone_ua_widget" );

        then: "'user access' widget should be displayed"
        view.isDisplayed();

        and: "'Edit Permissions' link should be present on the widget"
        view.isEditPermissionsLinkDisplayed();

        and: "'Everyone can read this item' should be displayed"
        view.getEveryoneText() == UserAccessWidgetItemView.EVERYONE_CAN_READ;
    }

    def "GIVEN new 'Acl-entry' was added for existing content WHEN the content has been selected and 'UserAccessWidget' opened THEN new 'acl-entry' should be displayed on the widget"()
    {
        given: "new Acl-entry was added for existing content"
        findAndSelectContent( FOLDER_CONTENT.getName() );
        contentBrowsePanel.openContentDetailsPanel();
        UserAccessWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getUserAccessWidgetItemView();
        ContentAclEntry anonymousEntry = ContentAclEntry.builder().principalName( SystemUserName.SYSTEM_ANONYMOUS.getValue() ).build();
        EditPermissionsDialog modalDialog = view.clickOnEditPermissionsLink();

        when: "content has been selected and widget opened"
        modalDialog.setInheritPermissionsCheckbox( false ).addPermissionByClickingCheckbox( anonymousEntry ).clickOnApply();
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        saveScreenshot( "anonymous_ua_widget" );

        then: "new 'Acl-entry' should be displayed on the widget"
        view.getNamesFromAccessLine( Application.CAN_READ ).contains( "AU" );

        and: "initial 'Acl-entry' should be displayed as well"
        view.getNamesFromAccessLine( Application.FULL_ACCESS ).contains( "SU" );
    }
}
