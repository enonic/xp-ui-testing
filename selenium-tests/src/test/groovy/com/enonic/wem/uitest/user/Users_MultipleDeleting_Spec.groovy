package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.UserBrowsePanel
import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
import com.enonic.autotests.vo.usermanager.User
import spock.lang.Shared

/**
 * Created on 8/4/2017.
 *
 * Tasks:xp-ui-testing#75  Add Selenium tests for deleting of multiple users
 * */
class Users_MultipleDeleting_Spec
    extends BaseUsersSpec
{
    @Shared
    User USER1;

    @Shared
    User USER2;

    def "GIVEN two existing users WHEN both users have been selected and  correct notification message is displayed and user listed in the grid"()
    {
        given: "'System' was expanded AND first user has been selected "
        USER1 = buildUser( "user", "password" );
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.SYSTEM.getValue() );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectFolder(
            UserBrowsePanel.BrowseItemType.USERS_FOLDER ).clickToolbarNew().waitUntilWizardOpened();
        userWizardPanel.typeData( USER1 ).save().close( USER1.getDisplayName() );

        and: "second user has been added"
        USER2 = buildUser( "user", "password" );
        userBrowsePanel.clickToolbarNew().waitUntilWizardOpened();
        userWizardPanel.typeData( USER2 ).save().close( USER2.getDisplayName() );
        userBrowsePanel.clickOnRowByName( UserBrowsePanel.BrowseItemType.USER.getValue() );
        userBrowsePanel.clickOnExpander( UserBrowsePanel.BrowseItemType.USER.getValue() );

        when: "correct user-data is typed and 'Save' button pressed"
        userBrowsePanel.clickCheckboxAndSelectRow( USER1.getDisplayName() ).clickCheckboxAndSelectRow( USER2.getDisplayName() );
        userBrowsePanel.clickToolbarDelete().doDelete();

        then:
        !userBrowsePanel.exists( USER1.getDisplayName() );
        and: ""
        !userBrowsePanel.exists( USER2.getDisplayName() );

        //TODO message for this operation is not added yet
        //and:"correct notification message should be displayed"
        //userBrowsePanel.waitExpectedNotificationMessage(  )
    }

}
