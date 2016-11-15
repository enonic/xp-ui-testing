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

    def "WHEN wizard for adding a 'Custom Selector'-content(0:2) opened THEN option filter input is present, there no any selected options AND content is valid"()
    {
        when: "start to add a content with type 'Custom Selector'"
        Content customSelector = buildCustomSelector0_2_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( customSelector.getContentTypeName() );
        CustomSelectorFormViewPanel formViewPanel = new CustomSelectorFormViewPanel( getSession() );

        and:
        wizard.typeDisplayName( customSelector.getDisplayName() ).save();
        saveScreenshot( "custom_selector_not_req" );

        then: "option filter input is present"
        formViewPanel.isOptionFilterIsDisplayed();

        and: "content is valid, because the value is not required"
        !wizard.isContentInvalid( customSelector.getDisplayName() );
    }

    def "GIVEN wizard for adding a 'Custom Selector'-content(0:2) opened WHEN one option selected THEN option filter input is present"()
    {
        given: "start to add a content with type 'Custom Selector'-content(0:2)"
        TEST_CONTENT = buildCustomSelector0_2_Content( OPTION_1 );
        ContentWizardPanel wizard = selectSitePressNew( TEST_CONTENT.getContentTypeName() );
        CustomSelectorFormViewPanel formViewPanel = new CustomSelectorFormViewPanel( getSession() );

        when:
        wizard.typeData( TEST_CONTENT ).save();
        saveScreenshot( "custom_selector_option_selected_not_req" );

        then: "option filter input is present, because one more option can be selected"
        formViewPanel.isOptionFilterIsDisplayed();

        and: "red icon not present on the wizard's tab"
        !wizard.isContentInvalid( TEST_CONTENT.getDisplayName() );
    }

    def "GIVEN existing content with one selected option WHEN content opened THEN correct selected option is displayed"()
    {
        when: "content opened"
        findAndSelectContent( TEST_CONTENT.getName() ).clickToolbarEdit();
        CustomSelectorFormViewPanel formViewPanel = new CustomSelectorFormViewPanel( getSession() );

        then: "correct selected option is displayed"
        formViewPanel.getSelectedOptions().size() == 1;

        and:
        formViewPanel.getSelectedOptions().contains( OPTION_1 );

    }

    def "GIVEN existing content with one selected option WHEN content opened and new option selcted THEN correct selcted options are displayed"()
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
