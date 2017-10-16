package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
import com.enonic.autotests.vo.usermanager.User

class UserWizardPanel_Toolbar_Spec
    extends BaseUsersSpec
{
    def "WHEN user wizard is opened THEN all buttons on toolbar should have correct state"()
    {
        when: "user wizard opened"
        UserWizardPanel wizardPanel = openSystemUserWizard();

        then: "'Delete' button should be disabled"
        !wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button should be disabled"
        !wizardPanel.isSaveButtonEnabled();
    }

    def "GIVEN user wizard is opened WHEN name typed THEN 'save' button should be enabled AND delete button is disabled"()
    {
        given: "user wizard is opened"
        UserWizardPanel wizardPanel = openSystemUserWizard();

        when: "name has been typed"
        wizardPanel.typeDisplayName( "display name" );

        then: "'Delete' button should be disabled"
        !wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button should be disabled, because e-mail and password are empty"
        !wizardPanel.isSaveButtonEnabled();
    }

    def "GIVEN user wizard is opened WHEN all data typed THEN save button should be enabled AND delete button is disabled"()
    {
        given: "user wizard is opened"
        UserWizardPanel wizardPanel = openSystemUserWizard();
        User validUser = buildUser( "valid-user", "password" );

        when: "all data has been typed"
        wizardPanel.typeData( validUser );

        then: "'Delete' button should be disabled"
        !wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button should be enabled"
        wizardPanel.isSaveButtonEnabled();
    }

    def "GIVEN user wizard is opened WHEN data has been typed and saved THEN button delete should be enabled"()
    {
        given: "user wizard is opened"
        UserWizardPanel wizardPanel = openSystemUserWizard();
        User validUser = buildUser( "valid-user", "password" );

        when: "data has been typed and saved"
        wizardPanel.typeData( validUser ).save();

        then: "'Delete' button should be enabled"
        wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button should be enabled"
        wizardPanel.isSaveButtonEnabled();
    }
}
