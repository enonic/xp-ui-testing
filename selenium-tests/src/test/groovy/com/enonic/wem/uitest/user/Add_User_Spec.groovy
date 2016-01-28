package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.usermanager.User
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Add_User_Spec
    extends BaseUsersSpec
{
    @Shared
    User user;


    def "GIVEN start adding a new user WHEN data typed and password is empty and 'Save' button pressed THEN error notification message appears"()
    {
        given: "start adding a new user"
        User userEmptyPassword = buildUser( "user", null );
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.USERS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();

        when: "data typed and user saved"
        String errorMessage = userWizardPanel.typeData( userEmptyPassword ).save().waitNotificationError( Application.EXPLICIT_NORMAL );
        TestUtils.saveScreenshot( getSession(), "user_error_mess" );

        then: "new user present beneath a system store"
        errorMessage == UserWizardPanel.PASSWORD_ERROR_MESSAGE;
    }

    def "GIVEN start adding a new user WHEN data typed  and 'Save' button pressed THEN notification message appears and user listed in the grid"()
    {
        given: "start adding a new user"
        user = buildUser( "user", "password" );
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.USERS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();

        when: "data typed and user saved"
        String creatingMessage = userWizardPanel.typeData( user ).save().waitNotificationMessage();
        userWizardPanel.close( user.getDisplayName() );
        def isWizardOpened = userWizardPanel.isOpened();
        TestUtils.saveScreenshot( getSession(), "user_saved" );

        then: "new user present beneath a store"
        !isWizardOpened;
        and: "user present beneath a store"
        userBrowseFilterPanel.typeSearchText( user.getDisplayName() );
        userBrowsePanel.exists( user.getDisplayName(), true );

        and: "correct notification message appears"
        creatingMessage == USER_CREATED_MESSAGE;
    }

    def "GIVEN a existing user WHEN user filtered and selected and deleted THEN correct notification message appears and user not listed"()
    {
        given: "user filtered"
        userBrowseFilterPanel.typeSearchText( user.getDisplayName() );

        when: "data typed and user saved"
        userBrowsePanel.clickCheckboxAndSelectRow( user.getDisplayName() ).clickToolbarDelete().doDelete();
        String message = userBrowsePanel.waitNotificationMessage( 2l );
        TestUtils.saveScreenshot( getSession(), "user_removed_message" );
        userBrowseFilterPanel.clickOnCleanFilter();
        userBrowsePanel.expandUsersFolder( "system" );
        TestUtils.saveScreenshot( getSession(), "user_removed" );

        then: "removed user not present beneath a 'Users' folder"
        !userBrowsePanel.exists( user.getDisplayName(), true );
        and: "correct notification message appears"
        message == String.format( USER_DELETING_NOTIFICATION_MESSAGE, user.getDisplayName() );
    }
}
