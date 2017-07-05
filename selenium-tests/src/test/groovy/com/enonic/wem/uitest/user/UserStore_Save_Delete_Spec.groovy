package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.pages.usermanager.wizardpanel.UserStoreWizardPanel
import com.enonic.autotests.vo.usermanager.UserStore
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class UserStore_Save_Delete_Spec
    extends BaseUsersSpec
{
    @Shared
    UserStore TEST_USER_STORE;

    @Shared
    String NEW_DISPLAY_NAME = "new display name";

    @Shared
    String NEW_NAME = "new_name";

    def "GIVEN 'user store' wizard is opened WHEN user store was saved and wizard closed THEN new user store should be listed"()
    {
        given: "'user store' wizard is opened"
        TEST_USER_STORE = buildUserStore( "us", "save-test-user-store", "save spec" );
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();

        when: "'user store' was saved and wizard closed"
        String creatingMessage = userStoreWizardPanel.typeData( TEST_USER_STORE ).save().waitNotificationMessage();
        userStoreWizardPanel.close( TEST_USER_STORE.getDisplayName() );


        then: "new 'user store' should be listed"
        saveScreenshot( "user-store-saved" );
        userBrowsePanel.exists( TEST_USER_STORE.getName() );

        and: "correct notification message should appear"
        creatingMessage == USER_STORE_CREATED_MESSAGE;
    }

    def "GIVEN existing user store WHEN the store was opened THEN correct description should be present"()
    {
        when: "existing store is opened"
        UserStoreWizardPanel userStoreWizardPanel = openUserStore( TEST_USER_STORE.getName() );

        then: "correct description should be present"
        saveScreenshot( "test_user_store_description" );
        userStoreWizardPanel.getDescriptionValue() == TEST_USER_STORE.getDescription();
    }

    def "GIVEN user store wizard is opened WHEN the name that already in use has been typed THEN correct notification message should be present"()
    {
        given: "'user store' wizard is opened"
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();

        when: "the name that already in use has been typed"
        userStoreWizardPanel.typeData( TEST_USER_STORE ).save();
        String errorMessage = userBrowsePanel.waitErrorNotificationMessage( Application.EXPLICIT_NORMAL );

        then: "correct notification message should be present"
        errorMessage == String.format( USER_STORE_EXISTS, TEST_USER_STORE.getName() );
    }

    def "GIVEN existing 'user store' WHEN 'user store' selected and 'Delete' button pressed THEN 'user store' should not be displayed in the grid"()
    {
        given: "existing 'user store'"
        UserStore us = buildUserStore( "us", "save-test-user-store", "store to delete" );
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();
        userStoreWizardPanel.typeData( us ).save().close( us.getDisplayName() );

        when: "user store was selected and 'Delete' button pressed"
        userBrowsePanel.clickCheckboxAndSelectRow( us.getName() ).clickToolbarDelete().doDelete();
        String message = userBrowsePanel.waitNotificationMessage( Application.EXPLICIT_NORMAL );
        saveScreenshot( "user-store-deleted" );

        then: "the user store should not be present in the grid"
        !userBrowsePanel.exists( us.getName() );

        and: "correct notification message appears"
        message == String.format( USER_STORE_DELETED_MESSAGE, us.getName() );
    }
    //Impossible to filter an User store
    @Ignore
    def "GIVEN existing 'user store' WHEN display name was changed THEN 'user store' with new display name should be listed"()
    {
        given: "existing 'user store' is opened"
        UserStoreWizardPanel userStoreWizardPanel = openUserStore( TEST_USER_STORE.getName() );

        when: "new name was typed and saved, and wizard closed"
        userStoreWizardPanel.typeDisplayName( NEW_DISPLAY_NAME ).save().close( NEW_DISPLAY_NAME );
        userBrowseFilterPanel.typeSearchText( NEW_DISPLAY_NAME );

        then: "role with new display name should be listed"
        saveScreenshot( "us-d-name-changed" );
        userBrowsePanel.exists( TEST_USER_STORE.getName() );
    }

    def "GIVEN creating of new 'user store' WHEN 'Delete' button on wizard-toolbar has been pressed THEN the 'user store' should not be displayed in the grid"()
    {
        given: "creating of new 'user store'"
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();
        UserStore userStore = buildUserStore( "user-store", "test-user-store", "delete wizard-toolbar" );
        userStoreWizardPanel.typeData( userStore ).save();
        saveScreenshot( "user-store-saved-in-wizard" );

        when: "'Delete' button on wizard-toolbar has been pressed"
        ConfirmationDialog confirmationDialog = userStoreWizardPanel.clickToolbarDelete();
        and: "deleting is confirmed"
        confirmationDialog.pressYesButton();

        then: "wizard closed and the 'user store' should not be displayed in the grid"
        saveScreenshot( "user-store-deleted-from-wizard" );
        !userBrowsePanel.exists( userStore.getName() );
    }

    def "GIVEN 'user store' wizard is opened, data saved WHEN HomeButton was pressed THEN new 'user store' should be displayed in the grid"()
    {
        given: "'user store' wizard is opened, data is typed and saved"
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();
        UserStore userStore = buildUserStore( "user-store", "test-user-store", "home button" );
        userStoreWizardPanel.typeData( userStore ).save();

        when: "HomeButton was pressed"
        userBrowsePanel.pressAppHomeButton();

        then: "new 'user store' should be displayed in the grid"
        saveScreenshot( "user-store-home-clicked" );
        userBrowsePanel.exists( userStore.getName() );
    }

    def "GIVEN 'user store' wizard is opened WHEN data was typed and 'Save' button pressed AND page was refreshed in the browser THEN wizard should be displayed with a correct data"()
    {
        given: "'user store' wizard is opened"
        UserStore refreshWizardUserStore = buildUserStore( "store", "test-refresh-wizard", "description" );
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();

        when: "data was typed and user saved"
        userStoreWizardPanel.typeData( refreshWizardUserStore ).save().waitNotificationMessage();

        and: "page was refreshed"
        userBrowsePanel.refreshPanelInBrowser();
        sleep( 1000 );
        saveScreenshot( "user_store_wizard_refreshed" );

        then: "wizard should be opened"
        userStoreWizardPanel.isOpened();

        and: "correct display name should be displayed"
        userStoreWizardPanel.getNameInputValue() == refreshWizardUserStore.getName();
    }
}
