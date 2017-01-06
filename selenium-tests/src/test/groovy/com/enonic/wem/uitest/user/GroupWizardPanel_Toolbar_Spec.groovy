package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.usermanager.wizardpanel.GroupWizardPanel
import com.enonic.autotests.vo.usermanager.Group

class GroupWizardPanel_Toolbar_Spec
    extends BaseUsersSpec
{
    def "WHEN group wizard is opened THEN all buttons on toolbar should be in correct state"()
    {
        when: "group wizard is opened"
        GroupWizardPanel wizardPanel = openSystemGroupWizard();

        then: "'Delete' button is enabled"
        !wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button is disabled"
        !wizardPanel.isSaveButtonEnabled();
    }

    def "GIVEN group wizard is opened WHEN name has been typed THEN 'save' button should be enabled AND delete button is disabled"()
    {
        given: "group wizard opened"
        GroupWizardPanel wizardPanel = openSystemGroupWizard();

        when: "name typed"
        wizardPanel.typeDisplayName( "display name" );

        then: "'Delete' button should be disabled"
        !wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button should be enabled"
        wizardPanel.isSaveButtonEnabled();
    }

    def "GIVEN group wizard is opened WHEN all data was typed  but 'Save' was not pressed THEN 'save' button should be enabled AND delete button is disabled"()
    {
        given: "group wizard is opened"
        GroupWizardPanel wizardPanel = openSystemGroupWizard();
        Group validGroup = buildGroup( "valid-group1", "display name", "description" );

        when: "all data typed, but not saved"
        wizardPanel.typeData( validGroup );

        then: "'Delete' button is disabled"
        !wizardPanel.isDeleteButtonEnabled();

        and: "'Save' button is enabled"
        wizardPanel.isSaveButtonEnabled();
    }

    def "GIVEN group wizard opened WHEN data typed and 'Save' button has been pressed THEN button 'delete' is getting enabled"()
    {
        given: "group wizard opened"
        GroupWizardPanel wizardPanel = openSystemGroupWizard();
        Group group = buildGroup( "valid-group2", "display name", "description" );

        when: "data typed and 'Save' button has been pressed"
        wizardPanel.typeData( group ).save();

        then: "button 'delete' is getting enabled"
        wizardPanel.isDeleteButtonEnabled();
    }
}


