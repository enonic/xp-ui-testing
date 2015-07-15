package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import spock.lang.Ignore

class UserBrowsePanelToolbarSpec
    extends BaseUsersSpec
{

    def "GIVEN user BrowsePanel WHEN no selected content THEN 'Delete' button should be disabled"()
    {
        expect:
        !userBrowsePanel.isDeleteButtonEnabled();
    }

    def "GIVEN user BrowsePanel WHEN no selected content THEN 'New' button should be enabled"()
    {

        expect:
        userBrowsePanel.isNewButtonEnabled();
    }

    def "GIVEN user BrowsePanel WHEN no selected content THEN 'Edit' button should be disabled"()
    {

        expect:
        !userBrowsePanel.isEditButtonEnabled();
    }

    @Ignore
    def "GIVEN user BrowsePanel WHEN no selected content THEN 'Duplicate' button should be disabled"()
    {
        expect:
        !userBrowsePanel.isDuplicateEnabled();
    }

    def "GIVEN user BrowsePanel WHEN no selected content THEN 'Synch' button should be disabled"()
    {
        expect:
        !userBrowsePanel.isSynchEnabled();
    }

    def "GIVEN user BrowsePanel WHEN 'System' folder selected THEN 'Edit' button should be enabled"()
    {
        when:
        userBrowsePanel.clickCheckboxAndSelectFolder( UserBrowsePanel.BrowseItemType.SYSTEM );

        then:
        userBrowsePanel.isEditButtonEnabled();
    }
}