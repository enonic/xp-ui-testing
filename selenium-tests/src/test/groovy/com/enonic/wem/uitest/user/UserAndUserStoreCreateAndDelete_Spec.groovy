package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.DeleteUserItemDialog
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowseFilterPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.UserStoreWizardPanel
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.usermanager.User
import com.enonic.autotests.vo.usermanager.UserStore
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class UserAndUserStoreCreateAndDelete_Spec
    extends BaseGebSpec
{
    @Shared
    UserBrowsePanel userBrowsePanel;

    @Shared
    UserBrowseFilterPanel userBrowseFilterPanel;

    @Shared
    User user;

    @Shared
    int randomInt = Math.abs( new Random().nextInt() );

    @Shared
    private String USER_STORE_DISPLAY_NAME = "User Store" + randomInt;

    @Shared
    private String USER_STORE_PATH = "/user-store" + randomInt;

    @Shared
    private String GENERATED_USER_STORE_NAME = "user-store" + randomInt;

    @Shared
    private String USER_NAME = NameHelper.uniqueName( "user" );

    def setup()
    {
        go "admin"
        userBrowsePanel = NavigatorHelper.openUserManager( getTestSession() );
        userBrowseFilterPanel = userBrowsePanel.getUserBrowseFilterPanel();
    }

    def "GIVEN creating new UserStore WHEN display name typed THEN the real name equals as expected"()
    {
        given:
        UserStore userStore = UserTestUtils.buildUserStoreWithDisplayName( USER_STORE_DISPLAY_NAME );
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();

        when: "name typed"
        userStoreWizardPanel.typeData( userStore );

        then: "the real name equals as expected"
        userStoreWizardPanel.getStoreNameInputValue() == GENERATED_USER_STORE_NAME
    }

    def "GIVEN creating new UserStore WHEN display name typed and 'Save' button pressed THEN new User Store listed"()
    {
        given:
        UserStore userStore = UserTestUtils.buildUserStoreWithDisplayName( USER_STORE_DISPLAY_NAME );
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();

        when: "name typed and 'Save' pressed"
        userStoreWizardPanel.typeData( userStore )
        String message = userStoreWizardPanel.save().waitNotificationMessage();
        userStoreWizardPanel.close( userStore.getDisplayName() );

        then: "new User Store listed and verify, that real name is the same as expected"
        userBrowsePanel.exists( GENERATED_USER_STORE_NAME, false );
        and:
        "UserStore was created!" == message;

    }

    def "WHEN the empty User Store expanded and 'Users' selected THEN 'Delete' button is disabled "()
    {
        when:
        userBrowsePanel.expandStoreAndSelectUsers( USER_STORE_PATH );

        then: "'Delete' button is disabled "
        !userBrowsePanel.isDeleteButtonEnabled();
    }

    def "WHEN the empty User Store expanded and 'Groups' selected THEN 'Delete' button is disabled "()
    {
        when:
        userBrowsePanel.expandStoreAndSelectGroups( USER_STORE_PATH );

        then: "'Delete' button is disabled "
        !userBrowsePanel.isDeleteButtonEnabled();
    }

    def "GIVEN existing a User Store WHEN a user added to User Store THEN new user listed beneath a User Store "()
    {
        given:
        user = buildTestUser( USER_NAME );
        UserWizardPanel userWizardPanel = userBrowsePanel.expandStoreAndSelectUsers( USER_STORE_PATH ).clickToolbarNew();

        when: "name typed and 'Save' pressed"
        userWizardPanel.typeData( user ).save().close( user.getDisplayName() );
        userBrowsePanel.expandUsersFolder( USER_STORE_PATH );

        then: "new user present beneath a store"
        userBrowsePanel.exists( USER_NAME, true );
    }

    def "GIVEN existing a User Store with a user WHEN the User Store selected THEN  the 'Delete' button on toolbar is disabled "()
    {
        when: "select a existing 'User Store' with a user "
        userBrowsePanel.clickCheckboxAndSelectRow( USER_STORE_PATH );

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
        userBrowsePanel.clickCheckboxAndSelectRow( USER_STORE_PATH );

        when: "'Delete' button pressed"
        DeleteUserItemDialog deleteDialog = userBrowsePanel.clickToolbarDelete();
        deleteDialog.doDelete();

        then: "new User Store listed and verify, that real name is the same as expected"
        !userBrowsePanel.exists( GENERATED_USER_STORE_NAME, false );
    }


    private User buildTestUser( String userName )
    {
        return User.builder().displayName( userName ).email( USER_NAME + "@gmail.com" ).password( "password" ).build();

    }
}