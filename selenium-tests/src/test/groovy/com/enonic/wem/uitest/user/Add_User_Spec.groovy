package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.usermanager.User
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Add_User_Spec
    extends BaseUsersSpec
{
    @Shared
    User user;

    @Shared
    String DELETING_NOTIFICATION_MESSAGE = "Principal [user:system:%s] deleted!"

    def "GIVEN start adding a new user WHEN data typed and password is empty and 'Save' button pressed THEN error notification message appears"()
    {
        given: "start adding a new user"
        User userEmptyPassword = UserTestUtils.buildUser( NameHelper.uniqueName( "user" ), null );

        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.USERS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();

        when: "data typed and user saved"
        String errorMessage = userWizardPanel.typeData( userEmptyPassword ).save().waitNotificationError( Application.EXPLICIT_NORMAL )

        then: "new user present beneath a system store"
        errorMessage == UserWizardPanel.PASSWORD_ERROR_MESSAGE;
    }

    def "GIVEN start adding a new user WHEN data typed  and 'Save' button pressed THEN notification message appears and user listed in the grid"()
    {
        given: "start adding a new user"
        user = UserTestUtils.buildUser( NameHelper.uniqueName( "user" ), "password" );

        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.USERS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();

        when: "data typed and user saved"
        String creatingMessage = userWizardPanel.typeData( user ).save().waitNotificationMessage();
        userWizardPanel.close( user.getDisplayName() );
        userBrowseFilterPanel.typeSearchText( user.getDisplayName() );

        then: "new user present beneath a store"
        userBrowsePanel.exists( user.getDisplayName(), true );

        and: "new user present beneath a system store"
        creatingMessage == "User was created!";
    }

    def "GIVEN a existing user WHEN user filtered and selected and deleted THEN correct notification message appears and user not listed"()
    {
        given: "user filtered"
        userBrowseFilterPanel.typeSearchText( user.getDisplayName() );

        when: "data typed and user saved"
        userBrowsePanel.clickCheckboxAndSelectRow( user.getDisplayName() ).clickToolbarDelete().doDelete();
        String message = userBrowsePanel.waitNotificationMessage( 2l );
        userBrowseFilterPanel.clickOnCleanFilter();
        userBrowsePanel.expandUsersFolder( "system" )

        then: "removed user not present beneath a 'Users' folder"
        !userBrowsePanel.exists( user.getDisplayName(), true );
        and:
        message == String.format( DELETING_NOTIFICATION_MESSAGE, user.getDisplayName() );

    }

}