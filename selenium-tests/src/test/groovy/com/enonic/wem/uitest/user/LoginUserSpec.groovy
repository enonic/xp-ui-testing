package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.HomePage
import com.enonic.autotests.pages.LauncherPanel
import com.enonic.autotests.pages.LoginPage
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.ChangeUserPasswordDialog
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.security.ContentAclEntry
import com.enonic.autotests.vo.contentmanager.security.PermissionSuite
import com.enonic.autotests.vo.usermanager.RoleName
import com.enonic.autotests.vo.usermanager.User
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.xp.content.ContentPath
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Tasks: xp-ui-testing#70 Add Selenium tests to verify #5368(ChangePassword dialog)
 *
 **/

@Stepwise
class LoginUserSpec
    extends BaseGebSpec
{

    @Shared
    UserBrowsePanel userBrowsePanel;

    @Shared
    private String USER_NAME = NameHelper.uniqueName( "user" );

    @Shared
    User USER_ADMIN_CONSOLE

    @Shared
    String USER_PASSWORD = "1q2w3e";

    @Shared
    String NEW_USER_PASSWORD = "password1";

    @Shared
    Content contentCanWrite

    @Shared
    Content contentCanNotWrite

    def "setup: add a test user to the system user store"()
    {
        setup: "'Users' app is opened"
        go "admin"
        userBrowsePanel = NavigatorHelper.openUsersApp( getTestSession() );

        and: "User-wizard is opened"
        String[] roles = [RoleName.ADMIN_CONSOLE.getValue(), RoleName.CM_APP.getValue()];
        USER_ADMIN_CONSOLE = User.builder().displayName( USER_NAME ).email( USER_NAME + "@gmail.com" ).password( USER_PASSWORD ).roles(
            roles.toList() ).build();

        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.USERS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();

        when: "data has been typed and user saved"
        userWizardPanel.typeData( USER_ADMIN_CONSOLE ).save().close( USER_ADMIN_CONSOLE.getDisplayName() );
        userBrowsePanel.getFilterPanel().typeSearchText( USER_ADMIN_CONSOLE.getDisplayName() );

        then: "new user should be present beneath the system store"
        userBrowsePanel.exists( USER_ADMIN_CONSOLE.getDisplayName(), true );
    }

    def "WHEN new content with permissions for just created user was added THEN Content is listed in BrowsePanel"()
    {
        given:
        ContentAclEntry entry = ContentAclEntry.builder().principalName( USER_NAME ).suite( PermissionSuite.CAN_WRITE ).build();
        List<ContentAclEntry> aclEntries = new ArrayList<>()
        aclEntries.add( entry );
        contentCanWrite = Content.builder().
            name( NameHelper.uniqueName( "folder-login" ) ).
            displayName( "folder" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.folder() ).
            aclEntries( aclEntries ).
            build();

        go "admin"
        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );

        when: "new content with CAN_READ-permissions for the user has been saved"
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).
            typeData( contentCanWrite ).save().closeBrowserTab().switchToBrowsePanelTab();

        then: "content should be listed in the grid"
        contentBrowsePanel.exists( contentCanWrite.getName() );
    }

    def "WHEN new content without any permissions for the just created user has been added THEN the content should be listed in the BrowsePanel"()
    {
        given: "new content without any permissions for just created user added"
        contentCanNotWrite = Content.builder().
            name( NameHelper.uniqueName( "folder-login" ) ).
            displayName( "folder" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.folder() ).
            build();

        go "admin"
        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );

        when: "new content with permissions CAN_READ for user has been added"
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).
            typeData( contentCanNotWrite ).save().closeBrowserTab().switchToBrowsePanelTab();

        then: "content should be listed in the grid"
        contentBrowsePanel.exists( contentCanNotWrite.getName() );
    }

    def "GIVEN existing user with the role 'Content Manager App' WHEN user is logged in THEN correct user's display name shown AND 'Applications' and 'Users' links should not be displayed on the launcher"()
    {
        given: "the user is logged in"
        go "admin"
        User user = User.builder().displayName( USER_NAME ).password( USER_PASSWORD ).build();
        getTestSession().setUser( user );

        when: "the user navigated to the 'Home Page'"
        NavigatorHelper.loginAndOpenHomePage( getTestSession() );
        saveScreenshot( "logged_home" + USER_NAME );
        LauncherPanel launcherPanel = new LauncherPanel( getSession() );

        then: "'Applications' link should not be displayed, because the user has no required role"
        !launcherPanel.isApplicationsLinkDisplayed();

        and: "'Users' link should not be displayed, because the user has no required role"
        !launcherPanel.isUsersLinkDisplayed();

        and: "'Home' link should be displayed"
        launcherPanel.isHomeLinkDisplayed();
    }

    def "GIVEN just created user is 'logged in' WHEN user has opened a content with CAN_WRITE permission and typed new 'display name' THEN 'save draft' button should be enabled"()
    {
        given: "navigated to Content Studio"
        go "admin"
        getTestSession().setUser( USER_ADMIN_CONSOLE );
        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );
        saveScreenshot( "logged_" + USER_NAME );

        when: "user has opened a content with CAN_WRITE permission and typed new 'display name'"
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( contentCanWrite.getName() ).clickToolbarEdit();
        wizard.typeDisplayName( "content updated" );
        saveScreenshot( "save_enabled" );

        then: "'save draft' button should be  enabled, because the content has CAN_WRITE permissions for the user"
        wizard.isSaveButtonEnabled()
    }

    def "GIVEN just created user is 'logged in' WHEN user has 'CAN_READ' for the content THEN 'Edit' button should be disabled"()
    {
        given: "just created user is 'logged in'"
        go "admin"
        getTestSession().setUser( USER_ADMIN_CONSOLE );
        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );
        saveScreenshot( "logged_" + USER_NAME );

        when: "user has opened a content with 'CAN_READ' permission and typed new display name"
        contentBrowsePanel.clickCheckboxAndSelectRow( contentCanNotWrite.getName() )
        saveScreenshot( "test_perm_edit_disabled" );

        then: "'Edit' button should be disabled, because the user has no rights to change the content"
        !contentBrowsePanel.isEditButtonEnabled();
    }

    def "GIVEN user-wizard opened WHEN 'change password' button has been pressed THEN 'change password' dialog should appear"()
    {
        given: "admin opens a user in the wizard"
        go "admin"
        getTestSession().setUser( null );
        userBrowsePanel = NavigatorHelper.openUsersApp( getTestSession() );
        userBrowsePanel.expandUsersFolder( "system" );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectUser(
            USER_ADMIN_CONSOLE.getDisplayName() ).clickToolbarEdit().waitUntilWizardOpened();

        when: "'change password' button has been pressed"
        ChangeUserPasswordDialog dialog = userWizardPanel.clickOnChangePassword().waitForLoaded( 2 );
        saveScreenshot( "test_open_change_password_dialog" );

        then: "'change password' dialog should appear"
        dialog.isOpened();

        and: "'cancel' button should be present"
        dialog.isCancelButtonDisplayed();

        and: "'Generate'-password link should be displayed"
        dialog.isGenerateLinkDisplayed();

        and: "Password Input' should be displayed"
        dialog.isPasswordInputDisplayed();

        and: "the input should be empty"
        dialog.getPasswordInputValue() == "";

        and: "'Show password' link should be displayed"
        dialog.isShowPasswordLinkDisplayed();

        and: "'Change' button should be disabled"
        dialog.isChangeButtonDisabled();
    }

    def "GIVEN 'Change Password' dialog is opened WHEN new password typed THEN 'Change Password' button should be enabled"()
    {
        given: "admin opens a user in the wizard"
        go "admin"
        getTestSession().setUser( null );
        userBrowsePanel = NavigatorHelper.openUsersApp( getTestSession() );
        userBrowsePanel.expandUsersFolder( "system" );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectUser(
            USER_ADMIN_CONSOLE.getDisplayName() ).clickToolbarEdit().waitUntilWizardOpened();

        and: "'change password' button has been pressed"
        ChangeUserPasswordDialog dialog = userWizardPanel.clickOnChangePassword().waitForLoaded( 2 );

        when: "new password has been typed"
        dialog.typePassword( "test-password" );
        saveScreenshot( "change-password-enabled" );

        then: "'Change' button should be enabled"
        !dialog.isChangeButtonDisabled();
    }
    //TODO remove it when #5368(ChangePassword dialog) will be fixed
    @Ignore
    def "GIVEN 'Change Password' dialog is opened AND new password has been typed WHEN 'Show' link has been clicked THEN 'Hide' link should be displayed"()
    {
        given: "admin opens a user in the wizard"
        go "admin"
        getTestSession().setUser( null );
        userBrowsePanel = NavigatorHelper.openUsersApp( getTestSession() );
        userBrowsePanel.expandUsersFolder( "system" );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectUser(
            USER_ADMIN_CONSOLE.getDisplayName() ).clickToolbarEdit().waitUntilWizardOpened();

        and: "'change password' button has been pressed"
        ChangeUserPasswordDialog dialog = userWizardPanel.clickOnChangePassword().waitForLoaded( 2 );
        dialog.typePassword( "test-password" );

        when: "'Show' link has been clicked"
        dialog.clickOnShowLink();
        saveScreenshot( "change-password-enabled" );

        then: "'Hide' link should be displayed"
        dialog.isHideTextDisplayed();
    }


    def "GIVEN 'Change Password' dialog is opened WHEN 'Generate' link has been pressed THEN secret text should be inserted in the input"()
    {
        given: "admin opens a user in the wizard"
        go "admin"
        userBrowsePanel = NavigatorHelper.openUsersApp( getTestSession() );
        userBrowsePanel.expandUsersFolder( "system" );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectUser(
            USER_ADMIN_CONSOLE.getDisplayName() ).clickToolbarEdit().waitUntilWizardOpened();

        when: "'change password' button has been pressed"
        ChangeUserPasswordDialog dialog = userWizardPanel.clickOnChangePassword().waitForLoaded( 2 );
        and: "Generate link has been clicked"
        dialog.clickOnGenerateLink();
        saveScreenshot( "change-password-generate" );

        then: "secret text should be inserted in the input"
        dialog.getPasswordInputValue() != "";
    }

    def "GIVEN user-wizard is opened WHEN user's password has been changed AND wizard closed THEN user browse panel should be shown "()
    {
        given: "user-wizard is opened"
        go "admin"
        getTestSession().setUser( null );
        userBrowsePanel = NavigatorHelper.openUsersApp( getTestSession() );
        userBrowsePanel.expandUsersFolder( "system" ).doClearSelection();
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectUser(
            USER_ADMIN_CONSOLE.getDisplayName() ).clickToolbarEdit().waitUntilWizardOpened();

        when: "user's password was changed by the administrator"
        ChangeUserPasswordDialog dialog = userWizardPanel.clickOnChangePassword().waitForLoaded( 2 );
        dialog.doChangePassword( NEW_USER_PASSWORD );
        saveScreenshot( "test_password_for_user_changed" );
        and: "wizard has been closed"
        userWizardPanel.save().close( USER_ADMIN_CONSOLE.getDisplayName() );

        then: "user-browse panel should be shown"
        userBrowsePanel.waitUntilPageLoaded( 2 );
    }

    def "WHEN login page is opened and old password has been typed THEN 'Login failed!' message should be displayed"()
    {
        when: "login page opened and old password has been typed"
        go "admin"
        User user = User.builder().displayName( USER_NAME ).password( USER_PASSWORD ).build();
        getTestSession().setUser( user );
        NavigatorHelper.loginAndOpenHomePage( getTestSession() );
        LoginPage loginPage = new LoginPage( getSession() );
        saveScreenshot( "test_login_old_password" );

        then: "old password should not work for login, error message should be displayed"
        loginPage.isErrorMessageDisplayed();
        and: "error message should be displayed"
        loginPage.getErrorMessage() == LoginPage.LOGIN_FAILED;
    }

    def "WHEN user is 'logged in' with the new password THEN home page should be loaded"()
    {
        when: "login page is opened and new password has been typed"
        go "admin"
        User user = User.builder().displayName( USER_NAME ).password( NEW_USER_PASSWORD ).build();
        getTestSession().setUser( user );
        HomePage home = NavigatorHelper.loginAndOpenHomePage( getTestSession() );
        saveScreenshot( "test_login_with_new_pass" );

        then: "home page should be loaded"
        home.isDisplayed();
    }
}
