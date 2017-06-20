package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.wizardpanel.UserStoreWizardPanel
import com.enonic.autotests.vo.usermanager.UserStore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class UserStore_IdProvider_Spec
    extends BaseUsersSpec
{
    @Shared
    UserStore USERSTORE_WITH_ID_PROVIDER;


    def "GIVEN wizard for new user-store is opened WHEN data typed and 'Save' button pressed  THEN store saved AND correct notification message appears"()
    {
        given: "wizard for new user-store with ID-provider is opened"
        USERSTORE_WITH_ID_PROVIDER = buildUserStoreWitIdProvider( "store", "test store", "store with a provider", STANDARD_ID_PROVIDER );
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();

        when: "data typed and user store saved"
        String message = userStoreWizardPanel.typeData( USERSTORE_WITH_ID_PROVIDER ).save().waitNotificationMessage();
        saveScreenshot( "test_user_store_provider_selected" );

        then: "correct notification message should appear"
        message == USER_STORE_CREATED_MESSAGE;
    }

    def "GIVEN existing store with id-provider WHEN the store is opened THEN correct id-provider should be displayed"()
    {
        when: "existing store with id-provider is opened"
        UserStoreWizardPanel userStoreWizardPanel = openUserStore( USERSTORE_WITH_ID_PROVIDER.getName() );

        then: "provider should be present on the wizard page"
        userStoreWizardPanel.isIdProviderSelected();

        and: "correct ID provider should be displayed"
        saveScreenshot( "test_user_store_provider_verify" );
        userStoreWizardPanel.getIdProviderDisplayName() == STANDARD_ID_PROVIDER;
    }

    def "GIVEN existing store with id-provider WHEN 'remove' provider button clicked THEN selector for ID provider should appear"()
    {
        given: "existing store with id-provider is opened"
        UserStoreWizardPanel userStoreWizardPanel = openUserStore( USERSTORE_WITH_ID_PROVIDER.getName() );

        when: "provider has been removed"
        userStoreWizardPanel.removeIdProvider( STANDARD_ID_PROVIDER );

        then: "selector for ID provider should appear"
        saveScreenshot( "test_user_store_provider_removed" );
        userStoreWizardPanel.isSelectorForIdProviderDisplayed();
    }
}