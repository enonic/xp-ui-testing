package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.BaseContentType
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.SettingsWizardStepForm
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserItemName
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.autotests.vo.usermanager.RoleName
import com.enonic.autotests.vo.usermanager.User
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.xp.content.ContentPath
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created on 3/6/2017.
 * */
@Stepwise
@Ignore
class ContentWizardPanel_Owner_Removed_Spec
    extends BaseGebSpec

{
    @Shared
    UserBrowsePanel userBrowsePanel;

    @Shared
    private String USER_NAME = NameHelper.uniqueName( "user" );

    @Shared
    String USER_PASSWORD = Application.MEDIUM_PASSWORD;

    @Shared
    User TEST_USER;

    @Shared
    Content TEST_FOLDER;


    def "setup: add new test user in System ID Provider"()
    {
        setup: "'Users' app is opened"
        go "admin"
        userBrowsePanel = NavigatorHelper.openUsersApp( getTestSession() );

        and: "user-wizard is opened"
        String[] roles = [RoleName.ADMIN_CONSOLE.getValue(), RoleName.CM_APP.getValue()];
        TEST_USER = User.builder().displayName( USER_NAME ).email( USER_NAME + "@gmail.com" ).password( USER_PASSWORD ).roles(
            roles.toList() ).build();

        userBrowsePanel.clickOnExpander( UserItemName.SYSTEM.getValue() );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickOnRowByName( "users" ).clickOnToolbarNew( UserItemName.USERS_FOLDER );

        when: "data was typed and user saved"
        userWizardPanel.typeData( TEST_USER ).save().close( TEST_USER.getDisplayName() );
        userBrowsePanel.getFilterPanel().typeSearchText( TEST_USER.getDisplayName() );

        then: "new user should be present beneath the system store"
        userBrowsePanel.exists( TEST_USER.getDisplayName(), true );
    }

    def "GIVEN existing folder is opened WHEN just created user has been select in 'owner' selector THEN expected user's name should be present in the settings"()
    {
        given: "creating of the new folder"
        TEST_FOLDER = buildFolderContent( "folder", "owner test 2" );

        go "admin"
        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( BaseContentType.FOLDER.getDisplayName() );
        SettingsWizardStepForm form = wizard.clickOnSettingsTabLink();

        when: "the user was set as 'owner' in the folder"
        wizard.typeData( TEST_FOLDER );
        form.removeOwner( "Super User" ).selectOwner( TEST_USER.getDisplayName() )
        wizard.save();

        then: "new owner should be displayed on the form"
        form.getOwner() == TEST_USER.getDisplayName();
    }

    def "GIVEN Users app is opened WHEN the user has been removed THEN the user should not be displayed in browse panel"()
    {
        go "admin"
        userBrowsePanel = NavigatorHelper.openUsersApp( getTestSession() );
        userBrowsePanel.getUserBrowseFilterPanel().typeSearchText( TEST_USER.getDisplayName() );
        userBrowsePanel.clickCheckboxAndSelectRow( TEST_USER.getDisplayName() );

        when: "user has been deleted"
        userBrowsePanel.clickToolbarDelete().doDelete();

        then: "the user should not be present"
        !userBrowsePanel.exists( TEST_USER.getDisplayName() );
    }

    def "GIVEN existing folder and owner was deleted WHEN the folder is opened THEN the user should be displayed as 'removed' in the wizard form"()
    {
        go "admin"
        ContentBrowsePanel contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );

        when: "existing folder has been opened"
        contentBrowsePanel.getFilterPanel().typeSearchText( TEST_FOLDER.getName() );
        userBrowsePanel.clickCheckboxAndSelectRow( TEST_FOLDER.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        SettingsWizardStepForm form = wizard.clickOnSettingsTabLink();

        then: "the owner should be displayed as 'removed'"
        form.isOwnerRemoved();
        and: "correct display name should be displayed"
        form.getOwner() == TEST_USER.getDisplayName();
    }

    Content buildFolderContent( String name, String displayName )
    {
        String generated = NameHelper.uniqueName( name );
        Content content = Content.builder().
            name( generated ).
            displayName( displayName ).
            contentType( BaseContentType.FOLDER.getDisplayName() ).
            parent( ContentPath.ROOT ).
            build();
        return content;
    }
}
