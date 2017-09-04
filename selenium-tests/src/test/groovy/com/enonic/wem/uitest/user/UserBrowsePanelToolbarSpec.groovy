package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel

class UserBrowsePanelToolbarSpec
    extends BaseUsersSpec
{

    def "GIVEN user BrowsePanel is opened WHEN no selected content THEN all button on the grid-toolbar have correct state"()
    {
        expect: "'Delete' button should be disabled"
        !userBrowsePanel.isDeleteButtonEnabled();

        and: "'New' button should be enabled"
        userBrowsePanel.isNewButtonEnabled();

        and: "'Edit' button should be disabled"
        !userBrowsePanel.isEditButtonEnabled();

        and: "'Sync' button should be disabled"
        !userBrowsePanel.isSyncEnabled();
    }

    def "GIVEN user BrowsePanel WHEN 'System' folder selected THEN 'Edit' button should be enabled"()
    {
        when:
        userBrowsePanel.clickCheckboxAndSelectFolder( UserBrowsePanel.UserItemName.SYSTEM );

        then: "Edit button on the toolbar is enabled, when system store is selected"
        userBrowsePanel.isEditButtonEnabled();

        and: "Delete button on the toolbar is disabled, when system store is selected"
        !userBrowsePanel.isDeleteButtonEnabled();

        and: "New button on the toolbar is enabled, when system store is selected"
        userBrowsePanel.isNewButtonEnabled();
    }
}