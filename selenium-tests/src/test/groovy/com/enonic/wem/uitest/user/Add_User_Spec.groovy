package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
import com.enonic.autotests.vo.usermanager.User
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Add_User_Spec
    extends BaseUsersSpec
{
    @Shared
    User USER;

    def "GIVEN 'System' was expanded AND 'User' is selected  and 'New' pressed WHEN user-data typed but the  password is empty and 'Save' button pressed THEN error notification message should appear"()
    {
        given: "'System' was expanded AND 'User' is selected"
        User userEmptyPassword = buildUser( "user", null );
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.USERS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();

        when: "user-data typed but the  password is empty and 'Save' button pressed"
        String errorMessage = userWizardPanel.typeData( userEmptyPassword ).save().waitNotificationError( Application.EXPLICIT_NORMAL );
        saveScreenshot( "user_error_message1" );

        then: "error notification message should appear"
        errorMessage == UserWizardPanel.PASSWORD_ERROR_MESSAGE;
    }

    def "GIVEN start adding a new user WHEN data typed  and 'Save' button pressed THEN correct notification message is displayed and user listed in the grid"()
    {
        given: "'System' was expanded AND 'User' is selected  and 'New' pressed"
        USER = buildUser( "user", "password" );
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.USERS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();

        when: "correct user-data is typed and 'Save' button pressed"
        String creatingMessage = userWizardPanel.typeData( USER ).save().waitNotificationMessage();

        and: "'Close' button has been pressed"
        userWizardPanel.close( USER.getDisplayName() );
        def isWizardOpened = userWizardPanel.isOpened();
        saveScreenshot( "user_saved" );

        then: "wizard should be closed "
        !isWizardOpened;

        and: "the user should be listed in the grid"
        userBrowseFilterPanel.typeSearchText( USER.getDisplayName() );
        userBrowsePanel.exists( USER.getDisplayName(), true );

        and: "correct notification message is displayed"
        creatingMessage == USER_CREATED_MESSAGE;
    }

    def "GIVEN existing user WHEN user filtered and selected and 'Delete' button was pressed THEN correct notification message appears and user not listed"()
    {
        given: "user's name is typed in the filter input"
        userBrowseFilterPanel.typeSearchText( USER.getDisplayName() );

        when: "user is selected and 'Delete' button has been pressed"
        userBrowsePanel.clickCheckboxAndSelectRow( USER.getDisplayName() ).clickToolbarDelete().doDelete();
        String message = userBrowsePanel.waitNotificationMessage( 2l );
        saveScreenshot( "user_removed_message" );
        userBrowseFilterPanel.clickOnCleanFilter();
        userBrowsePanel.expandUsersFolder( "system" );
        saveScreenshot( "user_removed" );

        then: "the user should not be listed in the grid"
        !userBrowsePanel.exists( USER.getDisplayName(), true );

        and: "correct notification message is displayed"
        message == String.format( USER_DELETING_NOTIFICATION_MESSAGE, USER.getDisplayName() );
    }

    def "GIVEN adding of a new user WHEN data typed  and 'Save' button pressed  AND page refreshed in the browser THEN wizard shown with a correct data"()
    {
        given: "'System' was expanded AND 'User' is selected  and 'New' pressed"
        User refreshWizardUser = buildUser( "user", "password" );
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.USERS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();

        when: "data has been typed and the user saved"
        userWizardPanel.typeData( refreshWizardUser ).save().waitNotificationMessage();

        and: "page has been refreshed in the browser"
        userBrowsePanel.refreshPanelInBrowser();
        saveScreenshot( "user_wizard_refreshed" );

        then: "wizard should be  opened"
        userWizardPanel.isOpened();

        and: "correct display name should be displayed"
        userWizardPanel.getNameInputValue() == refreshWizardUser.getDisplayName();
    }
}
