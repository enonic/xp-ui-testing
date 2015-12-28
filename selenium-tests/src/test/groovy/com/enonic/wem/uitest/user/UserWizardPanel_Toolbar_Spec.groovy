package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.wizardpanel.UserWizardPanel
import com.enonic.autotests.vo.usermanager.User

class UserWizardPanel_Toolbar_Spec
    extends BaseUsersSpec
{
    def "WHEN user wizard opened THEN all buttons on toolbar have correct state"()
    {
        when: "user wizard opened"
        UserWizardPanel wizardPanel = openSystemUserWizard();

        then: "'Delete' button enabled"
        !wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button disabled"
        !wizardPanel.isSaveButtonEnabled();
    }

    def "GIVEN user wizard opened WHEN name typed THEN save button is enabled AND delete button is disabled"()
    {
        given: "user wizard opened"
        UserWizardPanel wizardPanel = openSystemUserWizard();

        when: "name typed"
        wizardPanel.typeDisplayName( "display name" );

        then: "'Delete' button disabled"
        !wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button enabled"
        wizardPanel.isSaveButtonEnabled();
    }

    def "GIVEN user wizard opened WHEN all data typed THEN save button is enabled AND delete button is disabled"()
    {
        given: "user wizard opened"
        UserWizardPanel wizardPanel = openSystemUserWizard();
        User validUser = buildUser( "valid-user", "password" );

        when: "all data typed"
        wizardPanel.typeData( validUser );

        then: "'Delete' button disabled"
        !wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button enabled"
        wizardPanel.isSaveButtonEnabled();
    }

    def "GIVEN user wizard opened WHEN data typed and saved THEN button delete is enabled"()
    {
        given: "user wizard opened"
        UserWizardPanel wizardPanel = openSystemUserWizard();
        User validUser = buildUser( "valid-user", "password" );

        when: "data typed and saved"
        wizardPanel.typeData( validUser ).save();

        then: "'Delete' button enabled"
        wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button enabled"
        wizardPanel.isSaveButtonEnabled();
    }
}
