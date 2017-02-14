package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ShortcutFormViewPanel
import com.enonic.autotests.utils.NameHelper
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
    String TARGET_1 = "server";

    def " creating of new shortcut WHEN name typed but target not selected AND AppHome button pressed THEN the content displayed in the grid as invalid "()
    {
        given:
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.shortcut() );
        String name = NameHelper.uniqueName( "shortcut" );

        when: "name and display name has been typed"
        wizard.typeDisplayName( name ).save();

        and: "navigate to the grid"
        wizard.switchToBrowsePanelTab();
        findAndSelectContent( name );
        saveScreenshot( "shortcut_in_grid_invalid" );

        then: "red icon should be present on the wizard-tab"
        filterPanel.typeSearchText( name );
        contentBrowsePanel.isContentInvalid( name );
    }

    def "GIVEN creating of new shortcut WHEN name typed but target is not selected THEN red icon is present on the wizard-tab AND validation message appears"()
    {
        given:
        SHORTCUT_CONTENT = buildShortcutWithTarget( "shortcut", null, "shortcut display name", TARGET_1, );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( SHORTCUT_CONTENT.getContentTypeName() );
        ShortcutFormViewPanel formViewPanel = new ShortcutFormViewPanel( getSession() );

        when: "name and display name typed"
        wizard.typeName( SHORTCUT_CONTENT.getName() ).typeDisplayName( SHORTCUT_CONTENT.getDisplayName() ).save();
        saveScreenshot( "shortcut_validation_message" );

        then: "red icon should be present on the wizard-tab"
        formViewPanel.isValidationMessageDisplayed();

        and: "validation message should appear"
        formViewPanel.getValidationMessage() == "This field is required";

        and: "red icon should be present on the wizard-tab"
        wizard.isContentInvalid();
    }

    def "GIVEN existing shortcut without selected target WHEN target was selected THEN red icon should not be displayed on the wizard tab"()
    {
        given:
        ContentWizardPanel wizard = findAndSelectContent( SHORTCUT_CONTENT.getName() ).clickToolbarEdit();
        ShortcutFormViewPanel formViewPanel = new ShortcutFormViewPanel( getSession() );

        when: "target was selected"
        formViewPanel.selectTarget( TARGET_1 );
        saveScreenshot( "shortcut_valid" );
        wizard.save();

        then: "validation message not displayed"
        !formViewPanel.isValidationMessageDisplayed();

        and: "red icon should not be displayed on the wizard-tab"
        !wizard.isContentInvalid();
    }

    def "GIVEN existing shortcut with selected target WHEN target was removed THEN red icon appears  on the wizard tab"()
    {
        given:
        ContentWizardPanel wizard = findAndSelectContent( SHORTCUT_CONTENT.getName() ).clickToolbarEdit();
        ShortcutFormViewPanel formViewPanel = new ShortcutFormViewPanel( getSession() );

        when: "target was removed"
        formViewPanel.removeTarget();
        saveScreenshot( "shortcut_target_removed" );

        then: "validation message should be displayed"
        formViewPanel.isValidationMessageDisplayed();

        and: "red icon should appear on the wizard-tab"
        wizard.isContentInvalid();
    }
}
