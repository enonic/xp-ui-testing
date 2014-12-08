package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.WizardPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class UserWizardPanel_TabMenuSpec
    extends BaseGebSpec
{

    @Shared
    UserBrowsePanel userBrowsePanel;

    def setup()
    {
        go "admin"
        userBrowsePanel = NavigatorHelper.openUserManager( getTestSession() );
    }


    def "GIVEN started adding a 'User' and Wizard opened WHEN tab-menu button clicked THEN list of items with one name 'New User' is present"()
    {
        given: "'Users' folder clicked and user wizard opened"
        WizardPanel wizard = userBrowsePanel.clickCheckboxAndSelectRow(
            UserBrowsePanel.BrowseItemType.USERS ).clickToolbarNew().waitUntilWizardOpened();

        when: "AppBarTabMenu on the user wizard clicked and menu items showed"
        wizard.expandTabMenu();

        then: "item with title 'New User' is present on the tab menu "
        wizard.isTabMenuItemPresent( "New User" );

    }
}