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

    def "GIVEN role wizard is opened WHEN all data has been typed THEN 'save' button should be enabled BUT delete button is disabled"()
    {
        given: "role wizard is opened"
        RoleWizardPanel wizardPanel = openRoleWizard();
        Role validUser = buildRole( "valid-role1", "display name", "description" );

        when: "all data typed, but not saved"
        wizardPanel.typeData( validUser );

        then: "'Delete' button should be disabled, because data was not saved yet"
        !wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button should be enabled"
        wizardPanel.isSaveButtonEnabled();
    }

    def "GIVEN role wizard is opened WHEN data has been typed and saved THEN button delete should be enabled"()
    {
        given: "role wizard is opened"
        RoleWizardPanel wizardPanel = openRoleWizard();
        Role validUser = buildRole( "valid-role2", "display name", "description" );

        when: "data has been typed and saved"
        wizardPanel.typeData( validUser ).save();

        then: "'Delete' button should be enabled, because the data is saved"
        wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button should be enabled"
        wizardPanel.isSaveButtonEnabled();
    }
}

