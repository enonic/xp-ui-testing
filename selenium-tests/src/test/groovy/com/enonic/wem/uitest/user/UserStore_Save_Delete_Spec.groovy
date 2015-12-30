package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.pages.usermanager.wizardpanel.UserStoreWizardPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.usermanager.UserStore
import spock.lang.Ignore
import spock.lang.Shared

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
        TestUtils.saveScreenshot( getSession(), "user-store-saved" );
        userBrowsePanel.exists( TEST_USER_STORE.getName() );

        and: "correct notification message appears"
        creatingMessage == USER_STORE_CREATED_MESSAGE;
    }
    //app bug    INBOX-279
    @Ignore
    def "GIVEN a existing 'user store' WHEN creating new role with the same name THEN correct notification message appears"()
    {
        given: "creating new 'user store'"
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();

        when: "saved and wizard closed"
        String errorMessage = userStoreWizardPanel.typeData( TEST_USER_STORE ).save().waitErrorNotificationMessage(
            Application.EXPLICIT_NORMAL );

        then: "message that role with it  name already exists"
        errorMessage == String.format( ROLE_EXISTS, TEST_USER_STORE.getName() );
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
        TestUtils.saveScreenshot( getSession(), "user-store-deleted" );

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
        TestUtils.saveScreenshot( getSession(), "us-d-name-changed" );
        userBrowsePanel.exists( TEST_USER_STORE.getName() );
    }

    //app bug
    @Ignore
    def "GIVEN existing 'user store' WHEN name changed THEN 'user store' with new name should be listed"()
    {
        given: "existing 'user store' opened"
        UserStoreWizardPanel userStoreWizardPanel = openUserStore( TEST_USER_STORE.getName() );

        when: "new name typed and saved, and wizard closed"
        userStoreWizardPanel.typeName( NEW_NAME ).save().close( NEW_DISPLAY_NAME );
        userBrowseFilterPanel.typeSearchText( NEW_NAME );

        then: "'user store' with new name should be listed"
        TestUtils.saveScreenshot( getSession(), "us-name-changed" );
        userBrowsePanel.exists( NEW_NAME );
    }

    def "GIVEN creating new 'user store' WHEN data saved and 'Delete' button on wizard-toolbar pressed THEN wizard closed and 'user store' not displayed in grid"()
    {
        given: "creating new 'user store'"
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();
        UserStore userStore = buildUserStore( "user-store", "test-user-store", "delete wizard-toolbar" );
        userStoreWizardPanel.typeData( userStore ).save();
        TestUtils.saveScreenshot( getSession(), "user-store-saved-in-wizard" );

        when: "data saved and 'Delete' button on wizard-toolbar pressed"
        ConfirmationDialog confirmationDialog = userStoreWizardPanel.clickToolbarDelete();
        confirmationDialog.pressYesButton();


        then: "wizard closed and the 'user store' not displayed in a grid"
        TestUtils.saveScreenshot( getSession(), "user-store-deleted-from-wizard" );
        !userBrowsePanel.exists( userStore.getName() );
    }

    def "GIVEN 'user store' wizard opened, data saved WHEN HomeButton pressed THEN new 'user store' displayed in grid"()
    {
        given: "'user store' wizard opened, data saved"
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();
        UserStore userStore = buildUserStore( "user-store", "test-user-store", "home button" );
        userStoreWizardPanel.typeData( userStore ).save();

        when: "HomeButton pressed"
        userBrowsePanel.goToAppHome();

        then: "new 'user store' displayed in grid"
        TestUtils.saveScreenshot( getSession(), "user-store-home-clicked" );
        userBrowsePanel.exists( userStore.getName() );
    }
}
