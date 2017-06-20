package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.DeleteUserItemDialog
import com.enonic.autotests.pages.usermanager.wizardpanel.UserStoreWizardPanel
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.usermanager.User
import com.enonic.autotests.vo.usermanager.UserStore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class UserAndUserStoreCreateAndDelete_Spec
    extends BaseUsersSpec
{
    @Shared
    User user;

    @Shared
    private String USER_NAME = NameHelper.uniqueName( "user" );

    @Shared
    UserStore TEST_USER_STORE

    def "GIVEN creating of new UserStore WHEN display name has been typed THEN the actual name and expected should be equals"()
    {
        given: "creating of new UserStore"
        UserStore userStore = buildUserStore( "us", "test-user-store", "test user store" );
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();

        when: "name has been typed"
        userStoreWizardPanel.typeData( userStore );

        then: "the actual name and expected should be equals"
        userStoreWizardPanel.getStoreNameInputValue() == userStore.getName();
    }

    def "GIVEN creating of new UserStore WHEN data typed and 'Save' button pressed THEN new User Store should be listed"()
    {
        given: "creating of new UserStore"
        TEST_USER_STORE = buildUserStore( "us", "test-user-store", "test user store" );
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();

        when: "name typed and 'Save' pressed"
        userStoreWizardPanel.typeData( TEST_USER_STORE )
        String message = userStoreWizardPanel.save().waitNotificationMessage();
        userStoreWizardPanel.close( TEST_USER_STORE.getDisplayName() );

        then: "new User Store should be listed"
        userBrowsePanel.exists( TEST_USER_STORE.getName() );
        and: "correct notification should appear"
        USER_STORE_CREATED_MESSAGE == message;
    }

    def "WHEN the empty User Store expanded and 'Users' selected THEN 'Delete' button is disabled "()
    {
        when:
        userBrowsePanel.expandStoreAndSelectUsers( TEST_USER_STORE.getName() );
        saveScreenshot( "store_expanded_users" )

        then: "'Delete' button is disabled "
        !userBrowsePanel.isDeleteButtonEnabled();
    }

    def "WHEN the empty User Store has been expanded and 'Groups' selected THEN 'Delete' button should be disabled"()
    {
        when: "the empty User Store has been expanded and 'Groups' selected"
        userBrowsePanel.expandStoreAndSelectGroups( TEST_USER_STORE.getName() );
        saveScreenshot( "groups_folder_selected" );

        then: "'Delete' button should be disabled"
        !userBrowsePanel.isDeleteButtonEnabled();
    }

    def "GIVEN existing a User Store WHEN new user has been added to the User Store THEN new user should be listed beneath the 'Users' folder "()
    {
        given: "existing a User Store"
        user = buildTestUser( USER_NAME );
        and: "the User Store is expanded and 'Users' folder selected"
        UserWizardPanel userWizardPanel = userBrowsePanel.expandStoreAndSelectUsers( TEST_USER_STORE.getName() ).clickToolbarNew();

        when: "user-name has been typed and 'Save' pressed"
        userWizardPanel.typeData( user ).save().close( user.getDisplayName() );
        saveScreenshot( "user_added_to_store" )

        and: "'Users' folder has been expanded"
        userBrowsePanel.clickOnExpander( "users" );

        then: "new user should be present beneath the store"
        userBrowsePanel.exists( USER_NAME, true );
    }

    def "GIVEN existing a User Store with a user WHEN the User Store selected THEN  the 'Delete' button should be disabled "()
    {
        when: "select a existing 'User Store' with a user "
        userBrowsePanel.clickCheckboxAndSelectRow( TEST_USER_STORE.getName() );

        then: "button 'Delete' on the toolbar should be disabled"
        !userBrowsePanel.isDeleteButtonEnabled();
    }

    def "GIVEN existing user is selected and 'Delete' on toolbar has been pressed WHEN 'Yes' button has been pressed THEN user should not be listed in the browse panel"()
    {
        given: "user deleted"
        userBrowseFilterPanel.typeSearchText( user.getDisplayName() );
        DeleteUserItemDialog dialog = userBrowsePanel.clickCheckboxAndSelectRow( user.getDisplayName() ).clickToolbarDelete();

        when: "deleting is confirmed"
        dialog.doDelete();

        then: "user should not be listed in the browse panel"
        userBrowseFilterPanel.typeSearchText( user.getDisplayName() );
        !userBrowsePanel.exists( USER_NAME, true );
    }


    def "GIVEN existing 'User Store', WHEN 'User Store' selected and 'Delete' button pressed THEN the User Store should not be listed at the root"()
    {
        given: "select a existing 'User Store' "
        userBrowsePanel.clickCheckboxAndSelectRow( TEST_USER_STORE.getName() );

        when: "'Delete' button has been pressed"
        DeleteUserItemDialog deleteDialog = userBrowsePanel.clickToolbarDelete();

        and: "deleting is confirmed"
        deleteDialog.doDelete();

        then: "the User Store should not be listed at the root"
        !userBrowsePanel.exists( TEST_USER_STORE.getName() );
    }


    private User buildTestUser( String userName )
    {
        return User.builder().displayName( userName ).email( userName + "@gmail.com" ).password( "password" ).build();
    }
}