package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowseFilterPanel
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.GroupWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.usermanager.Group
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class BaseUsersSpec
    extends BaseGebSpec
{
    @Shared
    UserBrowsePanel userBrowsePanel;

    @Shared
    UserBrowseFilterPanel userBrowseFilterPanel;


    def setup()
    {
        go "admin"
        userBrowsePanel = NavigatorHelper.openUserManager( getTestSession() );
        userBrowseFilterPanel = userBrowsePanel.getUserBrowseFilterPanel();
    }

    protected GroupWizardPanel openSystemGroupWizard()
    {
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        return userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.GROUPS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();
    }

    protected Group buildGroup( String name, String displayName, String description )
    {
        String generated = NameHelper.uniqueName( name );
        return Group.builder().displayName( displayName ).name( generated ).description( description ).build();
    }

}
