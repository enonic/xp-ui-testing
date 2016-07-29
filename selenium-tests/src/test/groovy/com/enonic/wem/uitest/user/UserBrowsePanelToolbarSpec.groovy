package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel

class UserBrowsePanelToolbarSpec
    extends BaseUsersSpec
{

    def "GIVEN user BrowsePanel WHEN no selected content THEN all button on the grid-toolbar have correct state"()
    {
        expect: "Delete button is disabled"
        !userBrowsePanel.isDeleteButtonEnabled();

        and: "New button is enabled"
        userBrowsePanel.isNewButtonEnabled();

        and: "Edit button is disabled"
        !userBrowsePanel.isEditButtonEnabled();

        and: "Sync button is disabled"
        !userBrowsePanel.isSyncEnabled();
    }

    def "GIVEN user BrowsePanel WHEN 'System' folder selected THEN 'Edit' button should be enabled"()
    {
        when:
        userBrowsePanel.clickCheckboxAndSelectFolder( UserBrowsePanel.BrowseItemType.SYSTEM );

        then: "Edit button on the toolbar is enabled, when system store is selected"
        userBrowsePanel.isEditButtonEnabled();

        and: "Delete button on the toolbar is disabled, when system store is selected"
        !userBrowsePanel.isDeleteButtonEnabled();

        and: "New button on the toolbar is enabled, when system store is selected"
        userBrowsePanel.isNewButtonEnabled();
    }
}