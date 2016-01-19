package com.enonic.wem.uitest.user

import com.enonic.autotests.exceptions.AuthenticationException
import com.enonic.autotests.pages.HomePage
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.ChangeUserPasswordDialog
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.contentmanager.security.ContentAclEntry
import com.enonic.autotests.vo.contentmanager.security.PermissionSuite
import com.enonic.autotests.vo.usermanager.RoleName
import com.enonic.autotests.vo.usermanager.User
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.xp.content.ContentPath
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class LoginUserSpec
    extends BaseGebSpec
{

    @Shared
    UserBrowsePanel userBrowsePanel;

    @Shared
    private String USER_NAME = NameHelper.uniqueName( "user" );

    @Shared
    User user

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
        setup: "add a user"
        go "admin"
        userBrowsePanel = NavigatorHelper.openUsersApp( getTestSession() );

        and: "start adding a new user"
        String[] roles = [RoleName.ADMIN_CONSOLE.getValue(), RoleName.CM_APP.getValue()];
        user = User.builder().displayName( USER_NAME ).email( USER_NAME + "@gmail.com" ).password( USER_PASSWORD ).roles(
            roles.toList() ).build();

        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.USERS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();

        when: "data typed and user saved"
        userWizardPanel.typeData( user ).save().close( user.getDisplayName() );
        userBrowsePanel.getFilterPanel().typeSearchText( user.getDisplayName() );

        then: "new user present beneath a system store"
        userBrowsePanel.exists( user.getDisplayName(), true );
    }

    def "WHEN new content with permissions for just created user added THEN Content is listed in BrowsePanel"()
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
        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );

        when: "new content with permissions CAN_READ for user  saved"
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).
            typeData( contentCanWrite ).save().close( contentCanWrite.getDisplayName() );

        then: "content listed in the grid"
        TestUtils.saveScreenshot( getSession(), "login-content1" );
        contentBrowsePanel.exists( contentCanWrite.getName() );
    }

    def "WHEN new content without any permissions for just created user added THEN Content is listed in BrowsePanel"()
    {
        given: "new content without any permissions for just created user added"
        contentCanNotWrite = Content.builder().
            name( NameHelper.uniqueName( "folder-login" ) ).
            displayName( "folder" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.folder() ).
            build();

        go "admin"
        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );

        when: "new content with permissions CAN_READ for user  saved"
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).
            typeData( contentCanNotWrite ).save().close( contentCanNotWrite.getDisplayName() );

        then: "content listed in the grid"
        TestUtils.saveScreenshot( getSession(), "login-content2" );
        contentBrowsePanel.exists( contentCanNotWrite.getName() );
    }


    def "GIVEN just created user 'logged in' WHEN user opened a content with CAN_WRITE permission and typed new 'display name' THEN 'save draft' button is enabled"()
    {
        given: "user manager opened"
        go "admin"
        User user = User.builder().displayName( USER_NAME ).password( USER_PASSWORD ).build();
        getTestSession().setUser( user );
        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
        TestUtils.saveScreenshot( getSession(), "logged_" + USER_NAME );

        when: "user opened a content with CAN_WRITE permission and typed new 'display name'"
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( contentCanWrite.getName() ).clickToolbarEdit();
        wizard.typeDisplayName( "content updated" );
        TestUtils.saveScreenshot( getSession(), "save_enabled" );

        then: "'save draft' button is enabled"
        wizard.isSaveButtonEnabled()
    }

    def "GIVEN just created user 'logged in' WHEN user opened a content without 'CAN_WRITE' permission and typed new 'display name' THEN 'save draft' button is disabled"()
    {
        given: "user manager opened"
        go "admin"
        User user = User.builder().displayName( USER_NAME ).password( USER_PASSWORD ).build();
        getTestSession().setUser( user );
        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
        TestUtils.saveScreenshot( getSession(), "logged_" + USER_NAME );

        when: "user opened a content without 'CAN_WRITE' permission and typed new display name"
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( contentCanNotWrite.getName() ).clickToolbarEdit();
        wizard.typeDisplayName( "content updated" );
        TestUtils.saveScreenshot( getSession(), "save_disabled" );

        then: "'save draft' button is disabled"
        !wizard.isSaveButtonEnabled()
    }

    def "GIVEN user-wizard opened WHEN 'change password' button pressed THEN modal dialog appears"()
    {
        given: "user-wizard opened"
        go "admin"
        getTestSession().setUser( null );
        userBrowsePanel = NavigatorHelper.openUsersApp( getTestSession() );
        userBrowsePanel.expandUsersFolder( "system" );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectUser(
            user.getDisplayName() ).clickToolbarEdit().waitUntilWizardOpened();

        when: "'change password' button pressed"
        ChangeUserPasswordDialog dialog = userWizardPanel.clickOnChangePassword().waitForLoaded( 2 );
        TestUtils.saveScreenshot( getSession(), "change-pass-dialog" );

        then: "modal dialog appears"
        dialog.isOpened();

        and: "'change' and 'cancel' buttons are present"
        dialog.isCancelButtonDisplayed();

        and:
        dialog.isChangeButtonDisplayed();
    }

    def "GIVEN user-wizard opened WHEN changing a password for existing user AND wizard closed THEN user browse panel shown "()
    {
        given: "user-wizard opened"
        go "admin"
        getTestSession().setUser( null );
        userBrowsePanel = NavigatorHelper.openUsersApp( getTestSession() );
        userBrowsePanel.expandUsersFolder( "system" ).clickOnClearSelection();
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectUser(
            user.getDisplayName() ).clickToolbarEdit().waitUntilWizardOpened();

        when: "user's password was changed by administrator"
        ChangeUserPasswordDialog dialog = userWizardPanel.clickOnChangePassword().waitForLoaded( 2 );
        dialog.doChangePassword( NEW_USER_PASSWORD );
        TestUtils.saveScreenshot( getSession(), "password_changed" )
        userWizardPanel.save().close( user.getDisplayName() );

        then: "user-browse panel shown"
        userBrowsePanel.waitUntilPageLoaded( 2 );
    }

    def "WHEN login page opened and old password typed THEN old password should not work for login"()
    {
        when: "login page opened and old password typed"
        go "admin"
        User user = User.builder().displayName( USER_NAME ).password( USER_PASSWORD ).build();
        getTestSession().setUser( user );
        NavigatorHelper.loginAndOpenHomePage( getTestSession() );
        TestUtils.saveScreenshot( getSession(), "old-password" );

        then: "old password should not work for login"
        thrown( AuthenticationException )
    }

    def "WHEN user logged in with the new password THEN home page loaded"()
    {
        when: "login page opened and new password typed"
        go "admin"
        User user = User.builder().displayName( USER_NAME ).password( NEW_USER_PASSWORD ).build();
        getTestSession().setUser( user );
        HomePage home = NavigatorHelper.loginAndOpenHomePage( getTestSession() );
        TestUtils.saveScreenshot( getSession(), "login-new-pass" );

        then: "home page successfully loaded"
        home.isDisplayed();
    }
}
