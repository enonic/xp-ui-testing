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
    User USER;


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
        USER = buildUser( "user", "password" );
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.USERS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();

        when: "data typed and user saved"
        String creatingMessage = userWizardPanel.typeData( USER ).save().waitNotificationMessage();
        userWizardPanel.close( USER.getDisplayName() );
        def isWizardOpened = userWizardPanel.isOpened();
        TestUtils.saveScreenshot( getSession(), "user_saved" );

        then: "new user present beneath a store"
        !isWizardOpened;
        and: "user present beneath a store"
        userBrowseFilterPanel.typeSearchText( USER.getDisplayName() );
        userBrowsePanel.exists( USER.getDisplayName(), true );

        and: "correct notification message appears"
        creatingMessage == USER_CREATED_MESSAGE;
    }

    def "GIVEN a existing user WHEN user filtered and selected and deleted THEN correct notification message appears and user not listed"()
    {
        given: "user filtered"
        userBrowseFilterPanel.typeSearchText( USER.getDisplayName() );

        when: "data typed and user saved"
        userBrowsePanel.clickCheckboxAndSelectRow( USER.getDisplayName() ).clickToolbarDelete().doDelete();
        String message = userBrowsePanel.waitNotificationMessage( 2l );
        TestUtils.saveScreenshot( getSession(), "user_removed_message" );
        userBrowseFilterPanel.clickOnCleanFilter();
        userBrowsePanel.expandUsersFolder( "system" );
        TestUtils.saveScreenshot( getSession(), "user_removed" );

        then: "removed user not present beneath a 'Users' folder"
        !userBrowsePanel.exists( USER.getDisplayName(), true );
        and: "correct notification message appears"
        message == String.format( USER_DELETING_NOTIFICATION_MESSAGE, USER.getDisplayName() );
    }

    def "GIVEN adding of a new user WHEN data typed  and 'Save' button pressed  AND page refreshed in the browser THEN wizard shown with a correct data"()
    {
        given: "start adding a new user"
        User refreshWizardUser = buildUser( "user", "password" );
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.USERS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();

        when: "data typed and user saved"
        userWizardPanel.typeData( refreshWizardUser ).save().waitNotificationMessage();
        userBrowsePanel.refreshPanelInBrowser();
        TestUtils.saveScreenshot( getSession(), "user_wizard_refreshed" );

        then: "wizard is opened"
        userWizardPanel.isOpened();

        and: "correct display name displayed"
        userWizardPanel.getNameInputValue() == refreshWizardUser.getDisplayName();
    }
}
