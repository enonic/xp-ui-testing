package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.RoleStatisticsPanel
import com.enonic.autotests.pages.usermanager.wizardpanel.UserStoreWizardPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.usermanager.UserStore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class UserBrowsePanel_UserStoreStatisticPanel_Spec
    extends BaseUsersSpec
{
    @Shared
    RoleStatisticsPanel roleStatisticsPanel;

    @Shared
    UserStore TEST_USER_STORE;

    def "WHEN 'System User Store' is selected THEN correct header shown in statistics panel"()
    {
        when: "'System User Store' is selected"
        userBrowsePanel.clickCheckboxAndSelectRow( SYSTEM_USER_STORE_NAME );
        saveScreenshot( "user-store-statistic-panel" );
        roleStatisticsPanel = new RoleStatisticsPanel( getSession() );

        then: "correct info shown in statistics panel"
        roleStatisticsPanel.getItemDisplayName() == SYSTEM_USER_STORE_DISPLAY_NAME;
    }

    def "GIVEN creating of new User Store WHEN user-store has been saved and HomeButton clicked AND the store is selected THEN the store should be shown in the statistics panel"()
    {
        given: "creating new User Store"
        TEST_USER_STORE = buildUserStore( "us", "user-store-app-home", "selections spec" );
        UserStoreWizardPanel userStoreWizardPanel = userBrowsePanel.openUserStoreWizard();
        userStoreWizardPanel.typeData( TEST_USER_STORE );

        when: "user store was saved and HomeButton clicked"
        userStoreWizardPanel.save();
        userBrowsePanel.pressAppHomeButton();

        then: "correct display name should be shown on the statistics panel"
        userBrowsePanel.clickCheckboxAndSelectUserStore( TEST_USER_STORE.getName() );
        roleStatisticsPanel.getItemDisplayName().equals( TEST_USER_STORE.getDisplayName() );
    }

    def "GIVEN existing user store WHEN display name was changed THEN new display name should be displayed in the selections panel"()
    {
        given: "existing user store"
        UserStoreWizardPanel wizard = userBrowsePanel.clickOnRowByName( TEST_USER_STORE.getName() ).clickToolbarEdit();

        when: "display name was changed"
        String newDisplayName = NameHelper.uniqueName( "display-name" );
        wizard.typeDisplayName( newDisplayName ).save().close( newDisplayName );

        then: "new display name should be displayed in the selections panel"
        roleStatisticsPanel.getItemDisplayName().equals( newDisplayName );
    }
}
