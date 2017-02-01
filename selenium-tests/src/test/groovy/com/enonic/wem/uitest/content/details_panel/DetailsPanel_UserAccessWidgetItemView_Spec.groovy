package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.UserAccessWidgetItemView
import com.enonic.autotests.pages.contentmanager.wizardpanel.EditPermissionsDialog
import com.enonic.autotests.utils.TestUtils
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

    def "GIVEN a new created folder is selected WHEN details panel opened THEN UserAccessWidgetItemView is shown "()
    {
        given: "new created folder is selected"
        FOLDER_CONTENT = buildFolderContent( "folder", "user access widget" );
        addContent( FOLDER_CONTENT );
        findAndSelectContent( FOLDER_CONTENT.getName() );

        when: "details panel opened"
        contentBrowsePanel.clickOnDetailsToggleButton();
        UserAccessWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getUserAccessWidgetItemView();

        then: "widget is displayed"
        view.isDisplayed();

        and: "'Edit Permissions' link is present on the widget"
        view.isEditPermissionsLinkDisplayed();
    }

    def "GIVEN existing content with permissions for 'Everyone' WHEN details panel opened THEN correct info is shown "()
    {
        when: "details panel opened"
        findAndSelectContent( EXECUTABLE_BAT );
        contentBrowsePanel.clickOnDetailsToggleButton();
        UserAccessWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getUserAccessWidgetItemView();
        TestUtils.saveScreenshot( getSession(), "everyone_ua_widget" );

        then: "widget is displayed"
        view.isDisplayed();

        and: "'Edit Permissions' link is present on the widget"
        view.isEditPermissionsLinkDisplayed();

        and: "'Everyone can read this item' present"
        view.getEveryoneText() == UserAccessWidgetItemView.EVERYONE_CAN_READ;
    }

    def "GIVEN new 'Acl-entry' added for existing content WHEN the content selected and 'UserAccessWidgetItemView' opened THEN new 'acl-entry' is displayed on the widget"()
    {
        given: "new Acl-entry added for existing content"
        findAndSelectContent( FOLDER_CONTENT.getName() );
        contentBrowsePanel.clickOnDetailsToggleButton();
        UserAccessWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getUserAccessWidgetItemView();
        ContentAclEntry anonymousEntry = ContentAclEntry.builder().principalName( SystemUserName.SYSTEM_ANONYMOUS.getValue() ).build();
        EditPermissionsDialog modalDialog = view.clickOnEditPermissionsLink();

        when: "content selected and  widget is displayed"
        modalDialog.setInheritPermissionsCheckbox( false ).addPermissionByClickingCheckbox( anonymousEntry ).clickOnApply();
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        saveScreenshot( "anonymous_ua_widget" );

        then: "new 'Acl-entry' is displayed on the widget"
        view.getNamesFromAccessLine( Application.CAN_READ ).contains( "AU" );

        and: "initial 'Acl-entry' displayed as well"
        view.getNamesFromAccessLine( Application.FULL_ACCESS ).contains( "SU" );
    }
}
