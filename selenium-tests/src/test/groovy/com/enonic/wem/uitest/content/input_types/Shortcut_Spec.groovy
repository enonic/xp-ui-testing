package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ShortcutFormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Ignore
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

    def "GIVEN new shortcut wizard is opened WHEN name was typed and saved THEN the content should be not nvalid(in grid)"()
    {
        given:
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( "Shortcut" );
        String name = NameHelper.uniqueName( "shortcut" );

        when: "name and display name has been typed"
        wizard.typeDisplayName( name ).save();
        sleep( 1000 );

        and: "navigate to the grid"
        wizard.switchToBrowsePanelTab();
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );
        findAndSelectContent( name );
        saveScreenshot( "shortcut_is_invalid_in_grid" );

        then: "red icon should be present in the grid"
        contentBrowsePanel.isContentInvalid( name );
    }

    def "GIVEN new shortcut wizard is opened WHEN name has been typed and saved THEN red icon appears in the wizard-tab"()
    {
        given:
        SHORTCUT_CONTENT = buildShortcutWithTarget( "shortcut", null, "shortcut display name", TARGET_1, );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( SHORTCUT_CONTENT.getContentTypeName() );
        ShortcutFormViewPanel formViewPanel = new ShortcutFormViewPanel( getSession() );

        when: "name and display name was typed"
        wizard.typeName( SHORTCUT_CONTENT.getName() ).typeDisplayName( SHORTCUT_CONTENT.getDisplayName() ).save();
        saveScreenshot( "shortcut_validation_message" );

        then: "validation message should be displayed"
        formViewPanel.isValidationMessageDisplayed();

        and: "validation message should be displayed"
        formViewPanel.getValidationMessage() == "This field is required";

        and: "red icon should be present on the wizard-tab"
        wizard.isContentInvalid();
    }

    def "GIVEN existing shortcut is opened WHEN target has been selected THEN red icon should not be displayed in the wizard tab"()
    {
        given: "existing shortcut without selected target is opened"
        ContentWizardPanel wizard = findAndSelectContent( SHORTCUT_CONTENT.getName() ).clickToolbarEdit();
        ShortcutFormViewPanel formViewPanel = new ShortcutFormViewPanel( getSession() );

        when: "target was selected"
        formViewPanel.selectTarget( TARGET_1 );
        saveScreenshot( "shortcut_is_valid" );
        wizard.save();

        then: "validation message should not be displayed"
        !formViewPanel.isValidationMessageDisplayed();

        and: "red icon should not be displayed on the wizard-tab"
        !wizard.isContentInvalid();
    }

    def "GIVEN existing shortcut is opened WHEN target has been removed THEN red icon should appear in the wizard tab"()
    {
        given: "existing shortcut with selected target is opened"
        ContentWizardPanel wizard = findAndSelectContent( SHORTCUT_CONTENT.getName() ).clickToolbarEdit();
        ShortcutFormViewPanel formViewPanel = new ShortcutFormViewPanel( getSession() );

        when: "target has been removed"
        formViewPanel.removeTarget();
        saveScreenshot( "shortcut_target_removed" );

        then: "validation message should be displayed"
        formViewPanel.isValidationMessageDisplayed();

        and: "red icon should appear on the wizard-tab"
        wizard.isContentInvalid();
    }
}
