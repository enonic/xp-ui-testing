package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.wizardpanel.RoleWizardPanel
import com.enonic.autotests.vo.usermanager.Role

class RoleWizardPanel_Toolbar_Spec
    extends BaseUsersSpec
{
    def "WHEN role wizard opened THEN all buttons on toolbar have correct state"()
    {
        when: "role wizard opened"
        RoleWizardPanel wizardPanel = openRoleWizard();

        then: "'Delete' button enabled"
        !wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button disabled"
        !wizardPanel.isSaveButtonEnabled();
    }

    def "GIVEN role wizard opened WHEN name typed THEN save button is enabled AND delete button is disabled"()
    {
        given: "role wizard opened"
        RoleWizardPanel wizardPanel = openRoleWizard();

        when: "name typed"
        wizardPanel.typeDisplayName( "display name" );

        then: "'Delete' button disabled"
        !wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button enabled"
        wizardPanel.isSaveButtonEnabled();
    }

    def "GIVEN role wizard opened WHEN all data typed THEN save button is enabled AND delete button is disabled"()
    {
        given: "role wizard opened"
        RoleWizardPanel wizardPanel = openRoleWizard();
        Role validUser = buildRole( "valid-role1", "display name", "description" );

        when: "all data typed, but not saved"
        wizardPanel.typeData( validUser );

        then: "'Delete' button disabled"
        !wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button enabled"
        wizardPanel.isSaveButtonEnabled();
    }

    def "GIVEN role wizard opened WHEN data typed and saved THEN button delete is enabled"()
    {
        given: "role wizard opened"
        RoleWizardPanel wizardPanel = openRoleWizard();
        Role validUser = buildRole( "valid-role2", "display name", "description" );

        when: "data typed and saved"
        wizardPanel.typeData( validUser ).save();

        then: "'Delete' button enabled"
        wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button enabled"
        wizardPanel.isSaveButtonEnabled();
    }
}

