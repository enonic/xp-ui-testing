package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.BaseContentType
import com.enonic.autotests.pages.contentmanager.DuplicateContentDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.VersionHistoryWidget
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.PropertiesWidgetItemView
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
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class DetailsPanel_OwnerName_Spec
    extends BaseGebSpec
{
    @Shared
    User TEST_USER;

    @Shared
    String USER_NAME = "owner";

    @Shared
    String USER_PASSWORD = "password123";

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    @Shared
    Content FOLDER_TO_DUPLICATE;

    def setup()
    {
        go "admin"
    }

    def "Preconditions: add a test user to the system user store"()
    {
        setup: "'User' app is opened"
        UserBrowsePanel userBrowsePanel = NavigatorHelper.openUsersApp( getTestSession() );

        and: "build new user with roles"
        String generatedName = NameHelper.uniqueName( USER_NAME );
        String[] roles = [RoleName.ADMIN_CONSOLE.getValue(), RoleName.SYSTEM_ADMIN.getValue(), RoleName.CM_APP.getValue(),];
        TEST_USER = User.builder().displayName( generatedName ).email( generatedName + "@gmail.com" ).password( USER_PASSWORD ).roles(
            roles.toList() ).build();

        userBrowsePanel.clickOnExpander( UserItemName.SYSTEM.getValue() );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickOnRowByName( "users" ).clickOnToolbarNew(
            UserItemName.USERS_FOLDER );

        when: "data typed and new user has been saved"
        userWizardPanel.typeData( TEST_USER ).save().close( TEST_USER.getDisplayName() );
        userBrowsePanel.getFilterPanel().typeSearchText( TEST_USER.getDisplayName() );

        then: "new user should be present beneath the system store"
        userBrowsePanel.exists( TEST_USER.getDisplayName(), true );
    }

    def "GIVEN SU user is logged in WHEN new folder has been added THEN the folder should be listed in the grid"()
    {
        setup: "'SU' is logged in"
        contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );

        when: "new folder is added"
        FOLDER_TO_DUPLICATE = buildFolderContent( "folder", "owner_testing" );
        contentBrowsePanel.clickToolbarNew().selectContentType( FOLDER_TO_DUPLICATE.getContentTypeName() ).typeData(
            FOLDER_TO_DUPLICATE ).save().closeBrowserTab().switchToBrowsePanelTab();

        then: "folder should be listed in the grid"
        contentBrowsePanel.getFilterPanel().typeSearchText( FOLDER_TO_DUPLICATE.getName() );
        contentBrowsePanel.exists( FOLDER_TO_DUPLICATE.getName() );
        resetBrowser();
    }

    def "WHEN just created user duplicated the folder THEN new folder should appear in the grid"()
    {
        setup: "user is 'logged in'"
        getTestSession().setUser( TEST_USER );
        contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );

        when: "the folder has been duplicated"
        contentBrowsePanel.getFilterPanel().typeSearchText( FOLDER_TO_DUPLICATE.getName() );
        contentBrowsePanel.selectContentInTable( FOLDER_TO_DUPLICATE.getName() ).clickToolbarDuplicate();

        and: "Duplicate button in the dialog has been pressed"
        DuplicateContentDialog dialog = new DuplicateContentDialog( getSession() );
        dialog.waitForOpened().clickOnDuplicateButton().waitForClosed();
        contentBrowsePanel.getFilterPanel().typeSearchText( FOLDER_TO_DUPLICATE.getName() + "-copy" );

        then: "new folder should be listed in the grid"
        contentBrowsePanel.exists( FOLDER_TO_DUPLICATE.getName() + "-copy" );
    }

    def "GIVEN test user is logged in and selected a folder WHEN details panel has been opened THEN expected 'user name' should be present in the Properties Widget"()
    {
        setup: "user is 'logged in'"
        getTestSession().setUser( TEST_USER );
        contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );

        and: "folder that was copied by the user is selected"
        contentBrowsePanel.getFilterPanel().typeSearchText( FOLDER_TO_DUPLICATE.getName() + "-copy" );
        contentBrowsePanel.selectContentInTable( FOLDER_TO_DUPLICATE.getName() + "-copy" );

        when: "Details Panel and PropertiesWidget has been opened"
        contentBrowsePanel.openContentDetailsPanel();
        PropertiesWidgetItemView widget = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getPropertiesWidgetItemView();
        saveScreenshot( "copied_folder_user_name" );

        then: "expected 'owner' should be displayed on the properties widget"
        widget.getOwner() == TEST_USER.getDisplayName();
    }

    def "GIVEN test user is logged in and selected a folder WHEN version history has been opened THEN expected owner name should be displayed in the version widget"()
    {
        setup: "user is 'logged in'"
        getTestSession().setUser( TEST_USER );
        contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );

        and: "just added folder is selected"
        contentBrowsePanel.getFilterPanel().typeSearchText( FOLDER_TO_DUPLICATE.getName() + "-copy" );
        contentBrowsePanel.selectContentInTable( FOLDER_TO_DUPLICATE.getName() + "-copy" );
        contentBrowsePanel.openContentDetailsPanel();

        when: "the first 'version history' item has been expanded"
        VersionHistoryWidget allContentVersionsView = contentBrowsePanel.getContentDetailsPanel().openVersionHistory();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionItem( 0 );


        then: "expected 'owner name' should be displayed"
        saveScreenshot( "test_owner_version-history_user_name" );
        versionItem.getOwnerName( 0 ).contains( TEST_USER.getDisplayName() );
    }

    def "WHEN the copy of the existing folder is opened THEN correct owner should be displayed in settings"()
    {
        setup: "user is  'logged in'"
        getTestSession().setUser( TEST_USER );
        contentBrowsePanel = NavigatorHelper.openContentStudioApp( getTestSession() );

        when: "when the copy of the existing content was opened"
        contentBrowsePanel.getFilterPanel().typeSearchText( FOLDER_TO_DUPLICATE.getName() + "-copy" );
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable(
            FOLDER_TO_DUPLICATE.getName() + "-copy" ).clickToolbarEditAndSwitchToWizardTab();
        SettingsWizardStepForm form = wizard.clickOnSettingsTabLink();
        saveScreenshot( "test_owner_wizard" );

        then: "expected 'owner' should be shown in settings"
        form.getOwner() == TEST_USER.getDisplayName();
    }

    public Content buildFolderContent( String name, String displayName )
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
