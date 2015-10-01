package com.enonic.wem.uitest.user

import com.enonic.autotests.exceptions.AuthenticationException
import com.enonic.autotests.pages.HomePage
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
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
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class LoginUserSpec
    extends BaseGebSpec
{

    @Shared
    UserBrowsePanel userBrowsePanel;

    @Shared
    int randomInt = Math.abs( new Random().nextInt() );

    @Shared
    private String USER_NAME = NameHelper.uniqueName( "user" );

    @Shared
    User user

    @Shared
    String USER_PASSWORD = "1q2w3e";

    @Shared
    String NEW_USER_PASSWORD = "password1";

    @Shared
    Content userContent

    @Shared
    Content contentNoPermissions

    @Ignore
    def "setup: add a test user to the system user store"()
    {
        setup: "add a user"
        go "admin"
        userBrowsePanel = NavigatorHelper.openUserManager( getTestSession() );

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

    @Ignore
    def "WHEN new content with permissions for just created user added THEN Content is listed in BrowsePanel"()
    {
        given:
        ContentAclEntry entry = ContentAclEntry.builder().principalName( USER_NAME ).suite( PermissionSuite.CAN_READ ).build();
        List<ContentAclEntry> aclEntries = new ArrayList<>()
        aclEntries.add( entry );
        userContent = Content.builder().
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
            typeData( userContent ).save().close( userContent.getDisplayName() );

        then: "content listed in the grid"
        TestUtils.saveScreenshot( getSession(), "login-content1" );
        contentBrowsePanel.exists( userContent.getName() );
    }

    @Ignore
    def "WHEN new content without any permissions for just created user added THEN Content is listed in BrowsePanel"()
    {
        given:
        contentNoPermissions = Content.builder().
            name( NameHelper.uniqueName( "folder-login" ) ).
            displayName( "folder" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.folder() ).
            build();

        go "admin"
        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );


        when: "new content with permissions CAN_READ for user  saved"
        contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder().toString() ).
            typeData( contentNoPermissions ).save().close( contentNoPermissions.getDisplayName() );

        then: "content listed in the grid"
        TestUtils.saveScreenshot( getSession(), "login-content2" );
        contentBrowsePanel.exists( contentNoPermissions.getName() );
    }

    @Ignore
    def "WHEN new created user logged in THEN home page with only one application(CM) loaded "()
    {
        when:
        go "admin"
        User user = User.builder().displayName( USER_NAME ).password( USER_PASSWORD ).build();
        getTestSession().setUser( user );
        HomePage home = NavigatorHelper.loginAndOpenHomePage( getTestSession() );
        TestUtils.saveScreenshot( getSession(), "logged" );

        then:
        home.isLoaded();
        and:
        home.isContentManagerDisplayed();
    }

    @Ignore
    def "WHEN new created user logged and opened a CM app THEN only one content should be present in the grid "()
    {
        when: "content manager opened"
        go "admin"
        User user = User.builder().displayName( USER_NAME ).password( USER_PASSWORD ).build();
        getTestSession().setUser( user );
        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
        TestUtils.saveScreenshot( getSession(), "logged_cm" );
        contentBrowsePanel.getFilterPanel().typeSearchText( userContent.getName() );

        then: "content with permissions for current user are shown"
        contentBrowsePanel.exists( userContent.getName() );
        and:
        contentBrowsePanel.getFilterPanel().typeSearchText( contentNoPermissions.getName() );
        !contentBrowsePanel.exists( contentNoPermissions.getName() );
    }

    @Ignore
    def "GIVEN opened user for edit WHEN 'change password' button pressed THEN modal dialog appears"()
    {
        given:
        go "admin"
        getTestSession().setUser( null );
        userBrowsePanel = NavigatorHelper.openUserManager( getTestSession() );
        userBrowsePanel.expandUsersFolder( "system" );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectUser(
            user.getDisplayName() ).clickToolbarEdit().waitUntilWizardOpened();

        when:
        ChangeUserPasswordDialog dialog = userWizardPanel.clickOnChangePassword().waitForLoaded( 2 );
        TestUtils.saveScreenshot( getSession(), "ch-pass-dialog" );
        then:
        dialog.isOpened();

        and: "'change' and 'cancel' buttons a present"
        dialog.isCancelButtonDisplayed();
        and:
        dialog.isChangeButtonDisplayed();
    }

    @Ignore
    def "changing a password for existing user"()
    {
        given:
        go "admin"
        getTestSession().setUser( null );
        userBrowsePanel = NavigatorHelper.openUserManager( getTestSession() );
        userBrowsePanel.expandUsersFolder( "system" ).clickOnClearSelection();
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectUser(
            user.getDisplayName() ).clickToolbarEdit().waitUntilWizardOpened();

        when:
        ChangeUserPasswordDialog dialog = userWizardPanel.clickOnChangePassword().waitForLoaded( 2 );
        dialog.doChangePassword( NEW_USER_PASSWORD );
        TestUtils.saveScreenshot( getSession(), "pass_changed" )
        userWizardPanel.save().close( user.getDisplayName() );

        then:
        userBrowsePanel.waitUntilPageLoaded( 2 );
    }

    @Ignore
    def "WHEN password changed for existing user THEN old password should not work for login"()
    {
        when:
        go "admin"
        User user = User.builder().displayName( USER_NAME ).password( USER_PASSWORD ).build();
        getTestSession().setUser( user );
        NavigatorHelper.loginAndOpenHomePage( getTestSession() );
        TestUtils.saveScreenshot( getSession(), "login-failed" );

        then:
        thrown( AuthenticationException )
    }

    @Ignore
    def "WHEN user logged in with the new password THEN home page loaded"()
    {
        when:
        go "admin"
        User user = User.builder().displayName( USER_NAME ).password( NEW_USER_PASSWORD ).build();
        getTestSession().setUser( user );
        HomePage home = NavigatorHelper.loginAndOpenHomePage( getTestSession() );
        TestUtils.saveScreenshot( getSession(), "login-new-pass" );

        then:
        home.isLoaded();
    }
}
