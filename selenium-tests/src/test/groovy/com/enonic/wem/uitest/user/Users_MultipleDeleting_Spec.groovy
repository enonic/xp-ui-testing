package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.browsepanel.UserItemName
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
        userBrowsePanel.clickOnExpander( UserItemName.SYSTEM.getValue() );
        UserWizardPanel userWizardPanel = userBrowsePanel.clickCheckboxAndSelectFolder( UserItemName.USERS_FOLDER ).clickOnToolbarNew(
            UserItemName.USERS_FOLDER );
        userWizardPanel.typeData( USER1 ).save().close( USER1.getDisplayName() );

        and: "second user has been added"
        USER2 = buildUser( "user", "password" );
        userBrowsePanel.clickOnToolbarNew( UserItemName.USERS_FOLDER );
        userWizardPanel.typeData( USER2 ).save().close( USER2.getDisplayName() );
        userBrowsePanel.clickOnRowByName( UserItemName.USER.getValue() );
        userBrowsePanel.clickOnExpander( UserItemName.USER.getValue() );

        when: "correct user-data is typed and 'Save' button pressed"
        userBrowsePanel.clickCheckboxAndSelectRow( USER1.getDisplayName() ).clickCheckboxAndSelectRow( USER2.getDisplayName() );
        userBrowsePanel.clickToolbarDelete().doDelete();

        then: "correct notification message should be displayed"
        userBrowsePanel.waitExpectedNotificationMessage( "2 users were deleted", 2 );

        and:
        !userBrowsePanel.exists( USER1.getDisplayName() );
        and: ""
        !userBrowsePanel.exists( USER2.getDisplayName() );
    }
}
