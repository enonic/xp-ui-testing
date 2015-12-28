package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.wizardpanel.GroupWizardPanel
import com.enonic.autotests.vo.usermanager.Group

class GroupWizardPanel_Toolbar_Spec
    extends BaseUsersSpec
{
    def "WHEN group wizard opened THEN all buttons on toolbar have correct state"()
    {
        when: "group wizard opened"
        GroupWizardPanel wizardPanel = openSystemGroupWizard();

        then: "'Delete' button enabled"
        !wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button disabled"
        !wizardPanel.isSaveButtonEnabled();
    }

    def "GIVEN group wizard opened WHEN name typed THEN save button is enabled AND delete button is disabled"()
    {
        given: "group wizard opened"
        GroupWizardPanel wizardPanel = openSystemGroupWizard();

        when: "name typed"
        wizardPanel.typeDisplayName( "display name" );

        then: "'Delete' button disabled"
        !wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button enabled"
        wizardPanel.isSaveButtonEnabled();
    }

    def "GIVEN group wizard opened  WHEN all data typed THEN save button is enabled AND delete button is disabled"()
    {
        given: "group wizard opened"
        GroupWizardPanel wizardPanel = openSystemGroupWizard();
        Group validUser = buildGroup( "valid-group1", "display name", "description" );

        when: "all data typed, but not saved"
        wizardPanel.typeData( validUser );

        then: "'Delete' button disabled"
        !wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button enabled"
        wizardPanel.isSaveButtonEnabled();
    }

    def "GIVEN group wizard opened WHEN data typed and saved THEN button delete is enabled"()
    {
        given: "group wizard opened"
        GroupWizardPanel wizardPanel = openSystemGroupWizard();
        Group validUser = buildGroup( "valid-group2", "display name", "description" );

        when: "data typed and saved"
        wizardPanel.typeData( validUser ).save();

        then: "'Delete' button enabled"
        wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button enabled"
        wizardPanel.isSaveButtonEnabled();
    }
}


