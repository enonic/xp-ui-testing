package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.UserItemName
import com.enonic.autotests.pages.usermanager.wizardpanel.UserStoreWizardPanel
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
import com.enonic.autotests.vo.usermanager.User
import com.enonic.autotests.vo.usermanager.UserStore
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created  on 4/13/2017.
 * Tasks:
 * enonic/xp-ui-testing#26 Add selenium test to verify the xp#4603
 *
 * */
@Stepwise
class Delete_UserStore_With_User
    extends BaseUsersSpec
{
    @Shared
    User USER

    @Shared
    UserStore USER_STORE

    def "GIVEN existing 'user store' AND 'user store' has been expanded AND 'Users' folder selected WHEN new user has been added THEN the user should be displayed in the grid"()
    {
        given: "existing 'user store'"
        USER_STORE = buildUserStore( "us", "test user store", "test for deleting" );
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();
        userStoreWizardPanel.typeData( USER_STORE ).save().close( USER_STORE.getDisplayName() );

        and: "the store is expanded"
        userBrowsePanel.clickOnExpander( USER_STORE.getName() );
        and: "'Users' folder has been selected"
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectFolder( UserItemName.USERS_FOLDER ).clickOnToolbarNew(
            UserItemName.USERS_FOLDER );

        when: "new user has been added in the store"
        USER = buildUser( "user", "password" );
        userWizardPanel.typeData( USER ).save().close( USER.getDisplayName() );

        then: "just created user should be present in the grid"
        userBrowseFilterPanel.typeSearchText( USER.getDisplayName() );
        userBrowsePanel.exists( USER.getDisplayName(), true );
    }

    def "GIVEN existing user store with a user WHEN the store is opened THEN 'Delete' button on the toolbar should be disabled"()
    {
        when: "existing user store with a user is opened"
        UserStoreWizardPanel userStoreWizardPanel = openUserStore( USER_STORE.getName() );
        saveScreenshot( "wizard_user_store_with_user" );

        then: "'Delete' button should be disabled"
        !userStoreWizardPanel.isDeleteButtonEnabled();
    }

    def "GIVEN existing user store with a user WHEN the store is selected THEN 'Delete' button on the grid-toolbar should be disabled"()
    {
        when: "existing user store with a user is opened"
        userBrowsePanel.clickCheckboxAndSelectRow( USER_STORE.getName() );

        then: "'Delete' button should be disabled"
        !userBrowsePanel.isDeleteButtonEnabled();
    }

    def "existing user store with a user WHEN the user has been deleted AND the store is selected THEN 'Delete' button should be enabled"()
    {
        given: "existing user store with user"
        userBrowseFilterPanel.typeSearchText( USER.getDisplayName() );
        userBrowsePanel.clickCheckboxAndSelectRow( USER.getDisplayName() );

        and: "the user has been deleted"
        userBrowsePanel.clickToolbarDelete().doDelete();
        userBrowseFilterPanel.clickOnCleanFilter();

        when: "the 'User Store' is selected"
        userBrowsePanel.clickCheckboxAndSelectRow( USER_STORE.getName() );

        then: "'Delete' button should be enabled"
        userBrowsePanel.isDeleteButtonEnabled();
    }
}
