package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.NotificationDialog
import com.enonic.autotests.pages.form.optionset.MultiSelectionOptionSetView
import com.enonic.autotests.pages.form.optionset.OptionSetFormView
import com.enonic.autotests.pages.form.optionset.SingleSelectionOptionSetView
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created  on 5/12/2017.
 * */
@Stepwise
class OptionSet_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content OPTION_SET;

    def "GIVEN wizard for OptionSet content is opened WHEN 'Option 1' has been selected in the 'single selection' AND 'Save' button pressed THEN red circle should appear in the wizard page"()
    {
        given: "wizard for OptionSet is opened"
        OPTION_SET = build_OptionSet_Content();
        ContentWizardPanel wizard = selectSitePressNew( OPTION_SET.getContentTypeName() );
        OptionSetFormView optionSetFormView = new OptionSetFormView( getSession() );
        wizard.typeDisplayName( OPTION_SET.getName() );

        when: "'Option 1' has been selected"
        optionSetFormView.getSingleSelectionOptionSet().selectOption( "Option 1" );

        and: "'Save' button has been pressed"
        wizard.save();
        saveScreenshot( "opt_set_radio1_empty" );

        then: "this content is not valid, because required inputs are not filled"
        wizard.isContentInvalid();
    }

    def "WHEN existing 'Option Set' content is selected(required fields are empty) THEN the content should invalid in browse panel"()
    {
        when:
        findAndSelectContent( OPTION_SET.getName() );

        then: "the content should be displayed as invalid"
        contentBrowsePanel.isContentInvalid( OPTION_SET.getName() );
    }

    def "GIVEN existing OptionSet content(not valid) is opened WHEN Option 2 has been selected THEN the content gets valid in the wizard"()
    {
        given: "existing OptionSet content(not valid) is opened"
        ContentWizardPanel wizardPanel = findAndSelectContent( OPTION_SET.getName() ).clickToolbarEdit();
        SingleSelectionOptionSetView singleSelection = new SingleSelectionOptionSetView( getSession() );

        when: "'Option 2' has been selected:"
        singleSelection.expandMenuClickOnMenuItem( 0, "Reset" );
        singleSelection.selectOption( "Option 2" );

        and: "the content has been saved"
        wizardPanel.save();
        saveScreenshot( "opt_set_is_valid1" );

        then: "the content gets valid:"
        !wizardPanel.isContentInvalid();
    }

    def "GIVEN existing valid 'Option Set' has been selected in the grid THEN red icon should not be visible in browse panel"()
    {
        when: "existing 'Option Set' has been selected"
        findAndSelectContent( OPTION_SET.getName() );

        then: "the content should be valid"
        !contentBrowsePanel.isContentInvalid( OPTION_SET.getName() );
    }

    def "WHEN 'Option 1' has been selected AND required input has been filled THEN the content gets valid"()
    {
        given: "existing 'Option Set' content is opened"
        ContentWizardPanel wizardPanel = findAndSelectContent( OPTION_SET.getName() ).clickToolbarEdit();
        SingleSelectionOptionSetView singleSelection = new SingleSelectionOptionSetView( getSession() );

        when: "'Option 1' has been selected"
        singleSelection.expandMenuClickOnMenuItem( 0, "Reset" );
        singleSelection.selectOption( "Option 1" ).typeSetName( "test" );
        and: "the content has been saved"
        wizardPanel.save();

        then: "red icon should not be displayed"
        !wizardPanel.isContentInvalid();
    }

    def "GIVEN existing 'Option Set' content(updated) WHEN it is selected in the grid THEN the content should be valid in browse panel"()
    {
        when: "existing 'Option Set' content(updated) has been selected"
        findAndSelectContent( OPTION_SET.getName() );

        then: "the content should be displayed as valid"
        !contentBrowsePanel.isContentInvalid( OPTION_SET.getName() );
    }

    def "WHEN required checkbox in 'Multi selection' has been unchecked AND saved THEN red icon gets visible, this content gets not valid"()
    {
        given: "existing 'Option Set' is opened"
        ContentWizardPanel wizard = findAndSelectContent( OPTION_SET.getName() ).clickToolbarEdit();
        SingleSelectionOptionSetView singleSelection = new SingleSelectionOptionSetView( getSession() );
        MultiSelectionOptionSetView multiSelection = new MultiSelectionOptionSetView( getSession() );
        NotificationDialog notificationDialog = new NotificationDialog( getSession() );
        singleSelection.expandMenuClickOnMenuItem( 0, "Reset" );
        notificationDialog.waitForOpened();
        notificationDialog.clickOnOkButton();
        singleSelection.selectOption( "Option 2" );

        when: "required checkbox in 'Multi selection' has been unchecked"
        multiSelection.clickOnCheckbox( 2 );
        and: "content saved"
        wizard.save();
        saveScreenshot( "opt_set_multi_invalid" );

        then: "red icon gets visible, this content gets not valid"
        wizard.isContentInvalid();
    }

    def "GIVEN existing 'option set' is opened WHEN image has been selected in the multi selection THEN red icon gets not visible"()
    {
        given: "existing 'Option Set'  is opened"
        ContentWizardPanel wizard = findAndSelectContent( OPTION_SET.getName() ).clickToolbarEdit();
        MultiSelectionOptionSetView multiSelection = new MultiSelectionOptionSetView( getSession() );
        when: "image has been selected"
        multiSelection.selectImage( "nord" );
        and: "'Save' button pressed"
        wizard.save();
        saveScreenshot( "opt_set_image_selected" );
        then: "red icon gets not visible"
        !wizard.isContentInvalid()
    }

    private Content build_OptionSet_Content()
    {
        Content optionSetContent = Content.builder().
            name( NameHelper.uniqueName( "optionset" ) ).
            displayName( "option set content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "optionset" ).
            build();
        return optionSetContent;
    }
}
