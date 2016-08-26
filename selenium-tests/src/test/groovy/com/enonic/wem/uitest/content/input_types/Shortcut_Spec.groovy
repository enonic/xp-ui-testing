package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ShortcutFormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created on 26.08.2016.*/
@Stepwise
class Shortcut_Spec
    extends BaseContentSpec
{
    @Shared
    Content SHORTCUT_CONTENT

    @Shared
    String TARGET_1 = "server.bat";

    def " creating of new shortcut WHEN name typed but target not selected AND AppHome button pressed THEN the content displayed in the grid as invalid "()
    {
        given:
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.shortcut() );
        String name = NameHelper.uniqueName( "shortcut" );

        when: "name and display name typed"
        wizard.typeDisplayName( name ).save();

        and: "App Home button clicked"
        contentBrowsePanel.pressAppHomeButton();
        findAndSelectContent( name );
        TestUtils.saveScreenshot( getSession(), "shortcut_in_grid_invalid" );

        then: "red icon is present on the wizard-tab"
        wizard.isContentInvalid( name );
    }

    def "GIVEN creating of new shortcut WHEN name typed but target is not selected THEN red icon is present on the wizard-tab AND validation message appears"()
    {
        given:
        SHORTCUT_CONTENT = buildShortcutWithTarget( "shortcut", null, "shortcut display name", TARGET_1, );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( SHORTCUT_CONTENT.getContentTypeName() );
        ShortcutFormViewPanel formViewPanel = new ShortcutFormViewPanel( getSession() );

        when: "name and display name typed"
        wizard.typeName( SHORTCUT_CONTENT.getName() ).typeDisplayName( SHORTCUT_CONTENT.getDisplayName() ).save();
        TestUtils.saveScreenshot( getSession(), "shortcut_validation_message" );

        then: "red icon is present on the wizard-tab"
        formViewPanel.isValidationMessageDisplayed();

        and: "validation message appears"
        formViewPanel.getValidationMessage() == "This field is required";

        and: "red icon is present on the wizard-tab"
        wizard.isContentInvalid( SHORTCUT_CONTENT.getDisplayName() );
    }

    def "GIVEN existing shortcut without selected target WHEN target selected THEN red icon not displayed on the wizard tab"()
    {
        given:
        ContentWizardPanel wizard = findAndSelectContent( SHORTCUT_CONTENT.getName() ).clickToolbarEdit();
        ShortcutFormViewPanel formViewPanel = new ShortcutFormViewPanel( getSession() );

        when: "target selected"
        formViewPanel.selectTarget( TARGET_1 );
        TestUtils.saveScreenshot( getSession(), "shortcut_valid" );
        wizard.save();

        then: "red icon not displayed on the wizard-tab"
        !wizard.isContentInvalid( SHORTCUT_CONTENT.getDisplayName() );

        and: "validation message not displayed"
        !formViewPanel.isValidationMessageDisplayed();
    }

    def "GIVEN existing shortcut with selected target WHEN target removed THEN red icon appears  on the wizard tab"()
    {
        given:
        ContentWizardPanel wizard = findAndSelectContent( SHORTCUT_CONTENT.getName() ).clickToolbarEdit();
        ShortcutFormViewPanel formViewPanel = new ShortcutFormViewPanel( getSession() );

        when: "name and display name typed"
        formViewPanel.removeTarget();
        TestUtils.saveScreenshot( getSession(), "shortcut_target_removed" );

        then: "red icon appears displayed on the wizard-tab"
        wizard.isContentInvalid( SHORTCUT_CONTENT.getDisplayName() );

        and: "validation message  displayed"
        formViewPanel.isValidationMessageDisplayed();
    }
}
