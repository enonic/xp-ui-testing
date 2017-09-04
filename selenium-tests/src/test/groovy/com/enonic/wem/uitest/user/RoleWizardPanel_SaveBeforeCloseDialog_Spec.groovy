package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.usermanager.wizardpanel.RoleWizardPanel
import com.enonic.autotests.vo.usermanager.Role
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class RoleWizardPanel_SaveBeforeCloseDialog_Spec
    extends BaseUsersSpec
{
    @Shared
    String newDisplayName = "changeDisplayName"

    @Shared
    Role TEST_ROLE

    @Shared
    String NO_BUTTON_PRESSED = "no button selected";

    def "GIVEN role is opened and new changes have been saved WHEN 'Close tab' button has been pressed  THEN SaveBeforeCloseDialog must not appear"()
    {
        given:
        RoleWizardPanel roleWizardPanel = openRoleWizard();
        TEST_ROLE = buildRole( "role", "test-role", "description" );
        roleWizardPanel.typeData( TEST_ROLE ).save();

        when: "'Close tab' button has been pressed"
        SaveBeforeCloseDialog dialog = roleWizardPanel.close( TEST_ROLE.getDisplayName() );
        saveScreenshot( "role-saved-closed" );

        then: "'Save Before Dialog'  should not be shown, because all changes were saved"
        dialog == null;
    }

    def "GIVEN 'role wizard' is opened and changes are not saved WHEN 'Close tab' button has been pressed THEN SaveBeforeCloseDialog must appear"()
    {
        given: "wizard opened data typed and 'Save' pressed AND displayName changed"
        RoleWizardPanel roleWizardPanel = openRoleWizard();
        Role role = buildRole( "role", "test-role", "description" );
        roleWizardPanel.typeData( role ).save();
        roleWizardPanel.typeDisplayName( newDisplayName );

        when: "'Close' button has been pressed"
        SaveBeforeCloseDialog dialog = roleWizardPanel.close( newDisplayName )
        saveScreenshot( "SaveBeforeCloseDialog-role" );

        then: "modal dialog should appear, because changes were not saved"
        dialog != null;
    }

    def "GIVEN role wizard is opened and changes are not saved AND wizard closing WHEN 'Yes' is chosen THEN role is listed in BrowsePanel with it's new name"()
    {
        given: "role wizard is opened and changes are not saved"
        userBrowseFilterPanel.typeSearchText( TEST_ROLE.getName() );
        RoleWizardPanel roleWizardPanel = userBrowsePanel.clickCheckboxAndSelectRole( TEST_ROLE.getName() ).clickToolbarEdit();
        roleWizardPanel.typeDisplayName( newDisplayName ).close( newDisplayName );
        SaveBeforeCloseDialog dialog = new SaveBeforeCloseDialog( getSession() );
        dialog.waitForPresent();

        when: "'Yes' was chosen"
        dialog.clickYesButton();

        then: "Content should be listed in BrowsePanel with it's new name"
        userBrowseFilterPanel.typeSearchText( newDisplayName );
        sleep( 700 );
        saveScreenshot( "saving-role-display-name-changed" );
        userBrowsePanel.exists( TEST_ROLE.getName() );
    }

    def "GIVEN changing of display name of an existing role AND wizard closing WHEN 'No' is chosen THEN content with the new display name should not be present"()
    {
        given:
        userBrowseFilterPanel.typeSearchText( TEST_ROLE.getName() );
        RoleWizardPanel roleWizardPanel = userBrowsePanel.clickCheckboxAndSelectRole( TEST_ROLE.getName() ).clickToolbarEdit();
        roleWizardPanel.typeDisplayName( NO_BUTTON_PRESSED ).close( NO_BUTTON_PRESSED );
        SaveBeforeCloseDialog dialog = new SaveBeforeCloseDialog( getSession() );
        dialog.waitForPresent();

        when: "'No' was chosen"
        dialog.clickNoButton();

        then: "content with the new display name should not be present"
        userBrowseFilterPanel.typeSearchText( NO_BUTTON_PRESSED );
        saveScreenshot( "no-saving-role-display-name-changed" );
        !userBrowsePanel.exists( TEST_ROLE.getName() );
    }

    def "GIVEN changing an existing role and wizard closing WHEN 'Cancel' is chosen THEN wizard is still open"()
    {
        given: "changing of an existing role"
        userBrowseFilterPanel.typeSearchText( TEST_ROLE.getName() );
        RoleWizardPanel roleWizardPanel = userBrowsePanel.clickCheckboxAndSelectRole( TEST_ROLE.getName() ).clickToolbarEdit();
        and: "'Close' button has been pressed"
        SaveBeforeCloseDialog dialog = roleWizardPanel.typeDisplayName( "cancel test" ).close( "cancel test" );

        when: "'Cancel' on the 'save before close' has been pressed"
        dialog.clickCancelButton();
        saveScreenshot( "SaveBeforeCloseDialog-cancel-role" );

        then: "wizard should not be closed"
        roleWizardPanel.isOpened();
    }
}
