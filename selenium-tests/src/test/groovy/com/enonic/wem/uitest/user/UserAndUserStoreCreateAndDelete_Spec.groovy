package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.DeleteUserItemDialog
import com.enonic.autotests.pages.usermanager.wizardpanel.UserStoreWizardPanel
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
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

    def "GIVEN creating new UserStore WHEN display name typed THEN the real name equals as expected"()
    {
        given: "creating new UserStore"
        UserStore userStore = buildUserStore( "us", "test-user-store", "test user store" );
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();

        when: "name typed"
        userStoreWizardPanel.typeData( userStore );

        then: "the real name equals as expected"
        userStoreWizardPanel.getStoreNameInputValue() == userStore.getName();
    }

    def "GIVEN creating new UserStore WHEN data typed and 'Save' button pressed THEN new User Store listed"()
    {
        given:
        TEST_USER_STORE = buildUserStore( "us", "test-user-store", "test user store" );
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();

        when: "name typed and 'Save' pressed"
        userStoreWizardPanel.typeData( TEST_USER_STORE )
        String message = userStoreWizardPanel.save().waitNotificationMessage();
        userStoreWizardPanel.close( TEST_USER_STORE.getDisplayName() );

        then: "new User Store listed, actual name is the same as expected"
        userBrowsePanel.exists( TEST_USER_STORE.getName() );
        and: "correct notification appears"
        USERSTORE_CREATED_MESSAGE == message;

    }

    def "WHEN the empty User Store expanded and 'Users' selected THEN 'Delete' button is disabled "()
    {
        when:
        userBrowsePanel.expandStoreAndSelectUsers( TEST_USER_STORE.getName() );
        TestUtils.saveScreenshot( getSession(), "store_expanded_users" )

        then: "'Delete' button is disabled "
        !userBrowsePanel.isDeleteButtonEnabled();
    }

    def "WHEN the empty User Store expanded and 'Groups' selected THEN 'Delete' button is disabled "()
    {
        when:
        userBrowsePanel.expandStoreAndSelectGroups( TEST_USER_STORE.getName() );
        TestUtils.saveScreenshot( getSession(), "store_expand_roles" )

        then: "'Delete' button is disabled "
        !userBrowsePanel.isDeleteButtonEnabled();
    }

    def "GIVEN existing a User Store WHEN a user added to User Store THEN new user listed beneath a User Store "()
    {
        given:
        user = buildTestUser( USER_NAME );
        UserWizardPanel userWizardPanel = userBrowsePanel.expandStoreAndSelectUsers( TEST_USER_STORE.getName() ).clickToolbarNew();

        when: "name typed and 'Save' pressed"
        userWizardPanel.typeData( user ).save().close( user.getDisplayName() );
        TestUtils.saveScreenshot( getSession(), "user_added_to_store" )

        and:
        userBrowsePanel.clickOnExpander( "users" );

        then: "new user present beneath a store"
        userBrowsePanel.exists( USER_NAME, true );
    }

    def "GIVEN existing a User Store with a user WHEN the User Store selected THEN  the 'Delete' button on toolbar is disabled "()
    {
        when: "select a existing 'User Store' with a user "
        userBrowsePanel.clickCheckboxAndSelectRow( TEST_USER_STORE.getName() );

        then: "button 'Delete' on toolbar is disabled"
        !userBrowsePanel.isDeleteButtonEnabled();
    }

    def "GIVEN selected existing user and 'Delete' on toolbar pressed WHEN 'Yes' button pressed on a confirmation dialog THEN user not listed in the browse panel"()
    {
        given: "user deleted"
        userBrowseFilterPanel.typeSearchText( user.getDisplayName() );
        DeleteUserItemDialog dialog = userBrowsePanel.clickCheckboxAndSelectRow( user.getDisplayName() ).clickToolbarDelete();

        when:
        dialog.doDelete();

        then: "user no longer exists"
        userBrowseFilterPanel.typeSearchText( user.getDisplayName() );
        !userBrowsePanel.exists( USER_NAME, true );
    }


    def "GIVEN existing 'User Store', WHEN 'User Store' selected and 'Delete' button pressed THEN  deleted User Store is no longer listed at root"()
    {
        given: "select a existing 'User Store' "
        userBrowsePanel.clickCheckboxAndSelectRow( TEST_USER_STORE.getName() );

        when: "'Delete' button pressed"
        DeleteUserItemDialog deleteDialog = userBrowsePanel.clickToolbarDelete();
        deleteDialog.doDelete();

        then: "new User Store listed and verify, that real name is the same as expected"
        !userBrowsePanel.exists( TEST_USER_STORE.getName() );
    }


    private User buildTestUser( String userName )
    {
        return User.builder().displayName( userName ).email( userName + "@gmail.com" ).password( "password" ).build();

    }
}