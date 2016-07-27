package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.wizardpanel.UserStoreWizardPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.usermanager.UserStore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class UserStore_IdProvider_Spec
    extends BaseUsersSpec
{
    @Shared
    UserStore USERSTORE_WITH_ID_PROVIDER;


    def "GIVEN adding of a new user-store  with a provider WHEN data typed and 'Save' button pressed  THEN store saved AND correct notification message appears"()
    {
        given: "start adding a new user-store"
        USERSTORE_WITH_ID_PROVIDER = buildUserStoreWitIdProvider( "store", "test store", "store with a provider", STANDARD_ID_PROVIDER );
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();

        when: "data typed and user store saved"
        String message = userStoreWizardPanel.typeData( USERSTORE_WITH_ID_PROVIDER ).save().waitNotificationMessage();
        TestUtils.saveScreenshot( getSession(), "test_user_store_provider_selected" );

        then: "correct notification message appears"
        message == "UserStore was created!"
    }

    def "GIVEN existing user store with a id-provider WHEN the store opened THEN correct id-provider displayed"()
    {
        when: "existing store opened"
        UserStoreWizardPanel userStoreWizardPanel = openUserStore( USERSTORE_WITH_ID_PROVIDER.getName() );

        then: "provider is selected"
        userStoreWizardPanel.isIdProviderSelected();

        and: "correct ID provider is displayed"
        TestUtils.saveScreenshot( getSession(), "test_user_store_provider_verify" );
        userStoreWizardPanel.getIdProviderDisplayName() == STANDARD_ID_PROVIDER;
    }

    def "GIVEN existing user store with selected id-provider WHEN 'remove' provider button clicked THEN selector for ID provider appears"()
    {
        given: "existing store opened"
        UserStoreWizardPanel userStoreWizardPanel = openUserStore( USERSTORE_WITH_ID_PROVIDER.getName() );

        when: "provider is selected"
        userStoreWizardPanel.removeIdProvider( STANDARD_ID_PROVIDER );

        then: "selector for ID provider appears"
        TestUtils.saveScreenshot( getSession(), "test_user_store_provider_removed" );
        userStoreWizardPanel.isSelectorForIdProviderDisplayed();
    }

}