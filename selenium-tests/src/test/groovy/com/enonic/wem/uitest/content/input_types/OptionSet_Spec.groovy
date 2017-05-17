package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.optionset.OptionSetFormView
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created  on 5/12/2017.
 *
 * Tasks:
 * xp-ui-testing#7 Add Selenium tests for OptionSet
 * */
@Stepwise
class OptionSet_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content OPTION_SET;

    def "GIVEN wizard for OptionSet content is opened WHEN 'radio1' button in the 'single selection' clicked AND 'Save' button pressed THEN red circle should be present on the wizard page"()
    {
        given: "wizard for OptionSet content is opened"
        OPTION_SET = build_OptionSet_Content();
        ContentWizardPanel wizard = selectSitePressNew( OPTION_SET.getContentTypeName() );
        OptionSetFormView optionSetFormView = new OptionSetFormView( getSession() );
        wizard.typeDisplayName( OPTION_SET.getName() );

        when: "first radio button in the 'single selection' is clicked and 'Option Items' are expanded"
        optionSetFormView.getSingleSelectionOptionSet().clickOnFirstRadio();

        and: "'Save' button has been pressed"
        wizard.save();
        saveScreenshot( "opt_set_radio1_empty" );

        then: "red circle should be present on the wizard page, because required inputs are not filled"
        wizard.isContentInvalid();
    }

    def "GIVEN existing 'Option Set' content WHEN required fields are empty THEN the content should be displayed as invalid"()
    {
        when:
        findAndSelectContent( OPTION_SET.getName() );

        then: "the content should be displayed as invalid"
        contentBrowsePanel.isContentInvalid( OPTION_SET.getName() );
    }

    def "GIVEN existing OptionSet content(not valid) is opened WHEN required option in the 'Single Selection' is has been clicked THEN the content should be valid in the wizard"()
    {

        given: "existing OptionSet content(not valid) is opened"
        ContentWizardPanel wizardPanel = findAndSelectContent( OPTION_SET.getName() ).clickToolbarEdit();
        OptionSetFormView optionSetFormView = new OptionSetFormView( getSession() );

        when: "one required option has been clicked"
        optionSetFormView.getSingleSelectionOptionSet().clickOnSecondRadio();

        and: "the content has been saved"
        wizardPanel.save();
        saveScreenshot( "opt_set_is_valid1" );

        then: "red icon should not be displayed on the wizard page"
        !wizardPanel.isContentInvalid();
    }

    def "GIVEN existing 'Option Set' content with selected options WHEN it selected in the grid THEN the content should be displayed as valid"()
    {
        when: "existing 'Option Set' content with selected options"
        findAndSelectContent( OPTION_SET.getName() );

        then: "the content should be displayed as valid"
        !contentBrowsePanel.isContentInvalid( OPTION_SET.getName() );
    }

    def "GIVEN existing valid 'Option Set' content WHEN it opened AND first radio button clicked AND requred input has been filled THEN the content should be displayed as valid"()
    {
        given: "existing 'Option Set' content with selected options"
        ContentWizardPanel wizardPanel = findAndSelectContent( OPTION_SET.getName() ).clickToolbarEdit();
        OptionSetFormView optionSetFormView = new OptionSetFormView( getSession() );

        when:
        optionSetFormView.getSingleSelectionOptionSet().clickOnFirstRadio().typeSetName( "test" );
        and: "the content has been saved"
        wizardPanel.save();

        then: "red icon should not be displayed on the page"
        !wizardPanel.isContentInvalid();
    }

    def "GIVEN existing 'Option Set' content (option was changed) WHEN it selected in the grid THEN the content should be displayed as valid"()
    {
        when: "existing 'Option Set' content with selected options"
        findAndSelectContent( OPTION_SET.getName() );

        then: "the content should be displayed as valid"
        !contentBrowsePanel.isContentInvalid( OPTION_SET.getName() );
    }

    def "GIVEN existing 'Option Set' content is opened WHEN option in 'Multi selection' has been clicked AND it saved without required inputs THEN red icon should be displayed on the wizard page"()
    {
        given: "existing 'Option Set' content is opened"
        ContentWizardPanel wizard = findAndSelectContent( OPTION_SET.getName() ).clickToolbarEdit();
        OptionSetFormView optionSetFormView = new OptionSetFormView( getSession() );
        optionSetFormView.getSingleSelectionOptionSet().clickOnSecondRadio();

        when: "option in 'Multi selection' has been clicked"
        optionSetFormView.getMultiSelectionOptionSet().clickOnCheckbox( 2 );
        and: "it saved without required inputs"
        wizard.save();
        saveScreenshot( "opt_set_multi_invalid" );

        then: "red icon should be displayed on the wizard page"
        wizard.isContentInvalid();
    }

    def "GIVEN existing option set content is opened WHEN image was selected in the multi selection"()
    {
        given: "existing 'Option Set' content is opened"
        ContentWizardPanel wizard = findAndSelectContent( OPTION_SET.getName() ).clickToolbarEdit();
        OptionSetFormView optionSetFormView = new OptionSetFormView( getSession() );

        when: "image was selected in the multi selection"
        optionSetFormView.getMultiSelectionOptionSet().selectImage( "nord" );
        and: "'Save' button pressed"
        wizard.save();
        saveScreenshot( "opt_set_image_selected" );

        then: "red icon should not be displayed"
        !wizard.isContentInvalid()
    }

    private Content build_OptionSet_Content()
    {
        Content tinyMceContent = Content.builder().
            name( NameHelper.uniqueName( "optionset" ) ).
            displayName( "option set content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + ":optionset" ).
            build();
        return tinyMceContent;
    }
}
