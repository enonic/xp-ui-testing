package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.usermanager.User

class Add_User_Spec
    extends BaseUsersSpec
{


    def "GIVEN start adding a new user WHEN data typed and password is empty and 'Save' button pressed THEN error notification message appears"()
    {
        given: "start adding a new user"
        User user = UserTestUtils.buildUser( NameHelper.uniqueName( "user" ), null );

        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.USERS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();

        when: "data typed and user saved"
        String errorMessage = userWizardPanel.typeData( user ).save().waitNotificationError( Application.EXPLICIT_NORMAL )

        then: "new user present beneath a system store"
        errorMessage == UserWizardPanel.PASSWORD_ERROR_MESSAGE;
    }

}
