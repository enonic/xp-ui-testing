package com.enonic.wem.uitest.user

import com.enonic.autotests.pages.SaveBeforeCloseDialog
import com.enonic.autotests.pages.usermanager.wizardpanel.RoleWizardPanel
import com.enonic.autotests.utils.TestUtils
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

    def "GIVEN a unchanged role WHEN closing THEN SaveBeforeCloseDialog must not appear"()
    {
        given:
        RoleWizardPanel roleWizardPanel = openRoleWizard();
        TEST_ROLE = buildRole( "role", "test-role", "description" );
        roleWizardPanel.typeData( TEST_ROLE ).save();

        when:
        SaveBeforeCloseDialog dialog = roleWizardPanel.close( TEST_ROLE.getDisplayName() );
        TestUtils.saveScreenshot( getSession(), "role-saved-closed" );

        then:
        dialog == null;
    }

    def " GIVEN a changed role WHEN closing THEN SaveBeforeCloseDialog must appear"()
    {
        given: "wizard opened data typed and 'Save' pressed AND displayName changed"
        RoleWizardPanel roleWizardPanel = openRoleWizard();
        Role role = buildRole( "role", "test-role", "description" );
        roleWizardPanel.typeData( role ).save();
        roleWizardPanel.typeDisplayName( newDisplayName );

        when: "'Close' button pressed"
        SaveBeforeCloseDialog dialog = roleWizardPanel.close( newDisplayName )
        TestUtils.saveScreenshot( getSession(), "SaveBeforeCloseDialog-appears-role" );

        then: "modal dialog appears"
        dialog != null;
    }

    def "GIVEN changing name of an existing role and wizard closing WHEN Yes is chosen THEN role is listed in BrowsePanel with it's new name"()
    {
        given:
        userBrowseFilterPanel.typeSearchText( TEST_ROLE.getName() );
        RoleWizardPanel roleWizardPanel = userBrowsePanel.clickCheckboxAndSelectRole( TEST_ROLE.getName() ).clickToolbarEdit();
        roleWizardPanel.typeDisplayName( newDisplayName ).close( newDisplayName );
        SaveBeforeCloseDialog dialog = new SaveBeforeCloseDialog( getSession() );
        dialog.waitForPresent();

        when: "Yes was chosen"
        dialog.clickYesButton();

        then: "Content is listed in BrowsePanel with it's new name"
        userBrowseFilterPanel.typeSearchText( newDisplayName );
        sleep( 700 );
        TestUtils.saveScreenshot( getSession(), "saving-role-display-name-changed" );
        userBrowsePanel.exists( TEST_ROLE.getName() );
    }

    def "GIVEN changing display name of an existing role and wizard closing WHEN No is chosen THEN new display name not saved"()
    {
        given:
        userBrowseFilterPanel.typeSearchText( TEST_ROLE.getName() );
        RoleWizardPanel roleWizardPanel = userBrowsePanel.clickCheckboxAndSelectRole( TEST_ROLE.getName() ).clickToolbarEdit();
        roleWizardPanel.typeDisplayName( NO_BUTTON_PRESSED ).close( NO_BUTTON_PRESSED );
        SaveBeforeCloseDialog dialog = new SaveBeforeCloseDialog( getSession() );
        dialog.waitForPresent();

        when: "No was chosen"
        dialog.clickNoButton();

        then: "new display name not saved"
        userBrowseFilterPanel.typeSearchText( NO_BUTTON_PRESSED );
        TestUtils.saveScreenshot( getSession(), "no-saving-role-display-name-changed" );
        !userBrowsePanel.exists( TEST_ROLE.getName() );
    }

    def "GIVEN changing an existing role and wizard closing WHEN 'Cancel' is chosen THEN wizard is still open"()
    {
        given: "changing an existing role and wizard closing"
        userBrowseFilterPanel.typeSearchText( TEST_ROLE.getName() );
        RoleWizardPanel roleWizardPanel = userBrowsePanel.clickCheckboxAndSelectRole( TEST_ROLE.getName() ).clickToolbarEdit();
        SaveBeforeCloseDialog dialog = roleWizardPanel.typeDisplayName( "cancel test" ).close( "cancel test" );

        when: "'Cancel' is chosen"
        dialog.clickCancelButton();
        TestUtils.saveScreenshot( getSession(), "SaveBeforeCloseDialog-cancel-role" );

        then: "wizard is still open"
        roleWizardPanel.isOpened();
    }
}
