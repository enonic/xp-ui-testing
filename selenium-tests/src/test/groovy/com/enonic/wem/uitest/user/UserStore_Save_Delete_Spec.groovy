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

    def "GIVEN creating new user store WHEN user store saved and wizard closed THEN new user store should be listed"()
    {
        given: "creating new UserStore"
        TEST_USER_STORE = buildUserStore( "us", "save-test-user-store", "save spec" );
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();

        when: "'user store' saved and wizard closed"
        String creatingMessage = userStoreWizardPanel.typeData( TEST_USER_STORE ).save().waitNotificationMessage();
        userStoreWizardPanel.close( TEST_USER_STORE.getDisplayName() );


        then: "new 'user store' should be listed"
        saveScreenshot( "user-store-saved" );
        userBrowsePanel.exists( TEST_USER_STORE.getName() );

        and: "correct notification message should appear"
        creatingMessage == USER_STORE_CREATED_MESSAGE;
    }

    def "GIVEN existing user store WHEN the store opened THEN correct description displayed"()
    {
        when: "existing store opened"
        UserStoreWizardPanel userStoreWizardPanel = openUserStore( TEST_USER_STORE.getName() );

        then: "correct description is displayed"
        saveScreenshot( "test_user_store_description" );
        userStoreWizardPanel.getDescriptionValue() == TEST_USER_STORE.getDescription();
    }
    //  INBOX-279
    @Ignore
    def "GIVEN a existing 'user store' WHEN creating new role with the same name THEN correct notification message appears"()
    {
        given: "creating new 'user store'"
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();

        when: "saved and wizard closed"
        userStoreWizardPanel.typeData( TEST_USER_STORE ).save();
        String errorMessage = userBrowsePanel.waitErrorNotificationMessage( Application.EXPLICIT_NORMAL );

        then: "message that role with it  name already exists"
        errorMessage == String.format( USER_STORE_EXISTS, TEST_USER_STORE.getName() );
    }

    def "GIVEN existing 'user store' WHEN 'user store' selected and 'Delete' button pressed THEN 'user store' not displayed in a grid"()
    {
        given: "existing 'user store'"
        UserStore us = buildUserStore( "us", "save-test-user-store", "store to delete" );
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();
        userStoreWizardPanel.typeData( us ).save().close( us.getDisplayName() );

        when: "user store selected and 'Delete' button pressed"
        userBrowsePanel.clickCheckboxAndSelectRow( us.getName() ).clickToolbarDelete().doDelete();
        String message = userBrowsePanel.waitNotificationMessage( Application.EXPLICIT_NORMAL );
        saveScreenshot( "user-store-deleted" );

        then: "the user store not displayed in a grid"
        !userBrowsePanel.exists( us.getName() );

        and: "correct notification message appears"
        message == String.format( USER_STORE_DELETED_MESSAGE, us.getName() );
    }
    //INBOX-288
    @Ignore
    def "GIVEN existing 'user store' WHEN display name changed THEN 'user store' with new display name should be listed"()
    {
        given: "existing 'user store' opened"
        UserStoreWizardPanel userStoreWizardPanel = openUserStore( TEST_USER_STORE.getName() );

        when: "new name typed and saved, and wizard closed"
        userStoreWizardPanel.typeDisplayName( NEW_DISPLAY_NAME ).save().close( NEW_DISPLAY_NAME );
        userBrowseFilterPanel.typeSearchText( NEW_DISPLAY_NAME );

        then: "role with new display name should be listed"
        saveScreenshot( "us-d-name-changed" );
        userBrowsePanel.exists( TEST_USER_STORE.getName() );
    }

    //app bug? currently impossible to change the UserStore's name, input is disabled now
    @Ignore
    def "GIVEN existing 'user store' WHEN name changed THEN 'user store' with new name should be listed"()
    {
        given: "existing 'user store' opened"
        UserStoreWizardPanel userStoreWizardPanel = openUserStore( TEST_USER_STORE.getName() );

        when: "new name typed and saved, and wizard closed"
        userStoreWizardPanel.typeName( NEW_NAME ).save().close( NEW_DISPLAY_NAME );
        userBrowseFilterPanel.typeSearchText( NEW_NAME );

        then: "'user store' with new name should be listed"
        saveScreenshot( "us-name-changed" );
        userBrowsePanel.exists( NEW_NAME );
    }

    def "GIVEN creating new 'user store' WHEN 'Delete' button on wizard-toolbar has been pressed THEN the 'user store' should not be displayed in the grid"()
    {
        given: "creating new 'user store'"
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

    def "GIVEN 'user store' wizard opened, data saved WHEN HomeButton was pressed THEN new 'user store' should be displayed in the grid"()
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

    def "GIVEN adding of a new user-store WHEN data was typed and 'Save' button pressed AND page was refreshed in the browser THEN wizard should be displayed with a correct data"()
    {
        given: "start adding a new user"
        UserStore refreshWizardUserStore = buildUserStore( "store", "test-refresh-wizard", "description" );
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();

        when: "data typed and user saved"
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
