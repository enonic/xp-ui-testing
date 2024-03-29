package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.CustomSelectorFormViewPanel
import com.enonic.autotests.pages.form.FormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore
import spock.lang.Shared

/**
 * Created  on 08.09.2016.
 * */
@Ignore
class Occurrences_CustomSelector_1_1_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content TEST_CONTENT;

    @Shared
    String OPTION_1 = "Option number 1";


    def "WHEN wizard for new 'Custom Selector'-content(1:1) is opened THEN option filter input is present AND content is not valid"()
    {
        when: "start to add a content with type 'Custom Selector'"
        Content customSelector = buildCustomSelector1_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( customSelector.getContentTypeName() );
        CustomSelectorFormViewPanel formViewPanel = new CustomSelectorFormViewPanel( getSession() );

        and:"content is saved"
        wizard.typeDisplayName( customSelector.getName() ).save();
        saveScreenshot( "custom_selector_req" );

        then: "option filter input should be present"
        formViewPanel.isOptionFilterIsDisplayed();

        and:"red icon should be displayed, because selector is required"
        wizard.isContentInvalid();

        and: "validation message appears"
        formViewPanel.getFormValidationRecording( 0 ) == FormViewPanel.VALIDATION_MESSAGE_OCCURRENCE;
    }

    def "GIVEN wizard for adding a 'Custom Selector'-content(1:1) opened WHEN one option selected THEN option filter input is not displayed, one required option was selected"()
    {
        given: "start to add a content with type 'Custom Selector'-content(0:2)"
        TEST_CONTENT = buildCustomSelector1_1_Content( OPTION_1 );
        ContentWizardPanel wizard = selectSitePressNew( TEST_CONTENT.getContentTypeName() );
        CustomSelectorFormViewPanel formViewPanel = new CustomSelectorFormViewPanel( getSession() );

        when: "option selected and name typed"
        wizard.typeData( TEST_CONTENT ).save();
        saveScreenshot( "custom_selector_option_selected" );

        then: "option filter input is not displayed, one required option was selected"
        !formViewPanel.isOptionFilterIsDisplayed();

        and: "validation message not displayed"
        !formViewPanel.isFormValidationMessageDisplayed();
    }

    def "GIVEN existing content with one selected option WHEN content opened THEN correct selected option is displayed"()
    {
        when:
        findAndSelectContent( TEST_CONTENT.getName() ).clickToolbarEdit();
        CustomSelectorFormViewPanel formViewPanel = new CustomSelectorFormViewPanel( getSession() );

        then:
        formViewPanel.getSelectedOptions().size() == 1;

        and:
        formViewPanel.getSelectedOptions().contains( OPTION_1 );
    }

    def "GIVEN existing content (one selected option) is opened WHEN the option has  been removed THEN 'options filter input' appears"()
    {
        given:
        ContentWizardPanel wizard = findAndSelectContent( TEST_CONTENT.getName() ).clickToolbarEdit();
        CustomSelectorFormViewPanel formViewPanel = new CustomSelectorFormViewPanel( getSession() );

        when:"the option has been removed"
        formViewPanel.removeOption( OPTION_1 );
        wizard.save();
        saveScreenshot( "custom_selector_option_removed" );

        then: "no any selected options should be in the form"
        formViewPanel.getSelectedOptions().size() == 0;

        and: "'options filter input' appears"
        formViewPanel.isOptionFilterIsDisplayed();

        and: "validation message appears, because required option is not selected"
        formViewPanel.getFormValidationRecording( 0 ) == FormViewPanel.VALIDATION_MESSAGE_OCCURRENCE;
    }
}
