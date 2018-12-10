package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.CustomSelectorFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

/**
 * Created  on 06.09.2016.*/
class Occurrences_CustomSelector_0_2
    extends Base_InputFields_Occurrences
{
    @Shared
    Content TEST_CONTENT;

    @Shared
    String OPTION_1 = "Option number 1";

    @Shared
    String OPTION_2 = "Option number 2";

    def "WHEN wizard for new 'Custom Selector'-content(0:2) opened THEN option filter input should be present AND content should be valid"()
    {
        when: "wizard for new 'Custom Selector'-content(0:2) opened"
        Content customSelector = buildCustomSelector0_2_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( customSelector.getContentTypeName() );
        CustomSelectorFormViewPanel formViewPanel = new CustomSelectorFormViewPanel( getSession() );

        and:
        wizard.typeDisplayName( customSelector.getDisplayName() ).save();
        saveScreenshot( "custom_selector_not_req" );

        then: "option filter input should be present"
        formViewPanel.isOptionFilterIsDisplayed();

        and: "content is valid, because the selector is not required"
        !formViewPanel.isValidationMessagePresent();
    }

    def "GIVEN wizard for new 'Custom Selector'-content(0:2) is opened WHEN one option has been selected THEN option filter input should be present"()
    {
        given: "wizard for new 'Custom Selector'-content(0:2) is opened"
        TEST_CONTENT = buildCustomSelector0_2_Content( OPTION_1 );
        ContentWizardPanel wizard = selectSitePressNew( TEST_CONTENT.getContentTypeName() );
        CustomSelectorFormViewPanel formViewPanel = new CustomSelectorFormViewPanel( getSession() );

        when: "one option has been selected"
        wizard.typeData( TEST_CONTENT ).save();
        saveScreenshot( "custom_selector_option_selected_not_req" );

        then: "option filter input should be present, because one more option can be selected"
        formViewPanel.isOptionFilterIsDisplayed();

        and: "validation message should not be displayed"
        !formViewPanel.isValidationMessagePresent();
    }

    def "GIVEN existing content with one selected option WHEN content has been opened THEN expected option should be displayed"()
    {
        when: "content has been opened"
        findAndSelectContent( TEST_CONTENT.getName() ).clickToolbarEdit();
        CustomSelectorFormViewPanel formViewPanel = new CustomSelectorFormViewPanel( getSession() );

        then: "expected option should be displayed"
        formViewPanel.getSelectedOptions().size() == 1;

        and:
        formViewPanel.getSelectedOptions().contains( OPTION_1 );
    }

    def "GIVEN existing content with one selected option is opened WHEN one more option has been selected THEN expected selected options should be displayed"()
    {
        given:
        ContentWizardPanel wizard = findAndSelectContent( TEST_CONTENT.getName() ).clickToolbarEdit();
        CustomSelectorFormViewPanel formViewPanel = new CustomSelectorFormViewPanel( getSession() );

        when:
        formViewPanel.selectOption( OPTION_2 );
        wizard.save();
        saveScreenshot( "custom_selector_two_option_selected" );

        then:
        formViewPanel.getSelectedOptions().size() == 2;

        and:
        formViewPanel.getSelectedOptions().contains( OPTION_2 );
    }
}
