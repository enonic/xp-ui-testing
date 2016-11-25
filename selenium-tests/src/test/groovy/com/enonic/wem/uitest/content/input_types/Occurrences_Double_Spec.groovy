package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.DoubleFormViewPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class Occurrences_Double_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    String TEST_DOUBLE = "123.4";

    @Shared
    String WRONG_DOUBLE = "-111111111111111111111111";

    def "GIVEN creating of content with type 'double'(not required) WHEN invalid value for long typed THEN input with a red border AND red icon not shown on the wizard tab"()
    {
        given: "creating of  content with type 'Long'"
        Content doubleContent = buildDouble0_0_Content( WRONG_DOUBLE );
        ContentWizardPanel wizard = selectSitePressNew( doubleContent.getContentTypeName() ).waitUntilWizardOpened();
        DoubleFormViewPanel doubleFormViewPanel = new DoubleFormViewPanel( getSession() );

        when: "invalid value for long typed"
        wizard.typeData( doubleContent );
        TestUtils.saveScreenshot( getSession(), "test_double_invalid_not_req" );

        then: "input with a red border"
        !doubleFormViewPanel.isValueInInputValid( 0 );

        and: "red icon not shown on the wizard tab, because this input is not required"
        !wizard.isContentInvalid( doubleContent.getDisplayName() );

        and: "'Publish' button on the wizard-toolbar is enabled, because input is not required"
        wizard.isPublishButtonEnabled();
    }

    def "GIVEN creating of 'Double' content WHEN the content opened THEN correct Double value present in the wizard AND content is valid"()
    {
        given: "add a content with type 'Double'"
        Content doubleContent = buildDouble0_1_Content( TEST_DOUBLE );
        ContentWizardPanel wizard = selectSitePressNew( doubleContent.getContentTypeName() ).waitUntilWizardOpened().typeData(
            doubleContent );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        when: "just created content selected and 'Edit' button clicked"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( doubleContent );
        DoubleFormViewPanel doubleFormViewPanel = new DoubleFormViewPanel( getSession() );
        List<String> values = doubleFormViewPanel.getInputsValues();

        then: "one input with value displayed"
        values.size() == 1;

        and: "actual value in the form view and expected should be equals"
        values.get( 0 ).equals( TEST_DOUBLE );

        and: "red icon not present on the wizard tab"
        !wizard.isContentInvalid( doubleContent.getDisplayName() );

        and: "double input has no a red border"
        doubleFormViewPanel.isValueInInputValid( 0 );
    }
    //verifies the XP-3499  (ConfirmationDialog issue)
    def "GIVEN creating a double content WHEN name typed AND save button pressed THEN 'confirmation' dialog should not appears"()
    {
        given: "adding of content with empty value for double"
        Content doubleContent = buildDouble0_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( doubleContent.getContentTypeName() ).waitUntilWizardOpened().typeData(
            doubleContent );
        ConfirmationDialog dialog = new ConfirmationDialog( getSession() );
        DoubleFormViewPanel doubleFormViewPanel = new DoubleFormViewPanel( getSession() );

        when: "content saved"
        wizard.save();

        then: "validation message appears"
        doubleFormViewPanel.isValidationMessagePresent();

        and: "confirmation dialog should not appears"
        !dialog.isOpened();
    }

    def "GIVEN creating a double content with a required value WHEN name typed AND save button pressed THEN validation message appears"()
    {
        given: "adding of double content with empty value"
        Content doubleContent = buildDouble1_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( doubleContent.getContentTypeName() ).waitUntilWizardOpened().typeData(
            doubleContent );
        DoubleFormViewPanel doubleFormViewPanel = new DoubleFormViewPanel( getSession() );

        when: "content saved"
        wizard.save();
        saveScreenshot( "test_double_save_confirm1" );

        then: "validation message appears"
        doubleFormViewPanel.isValidationMessagePresent();

    }

    def "GIVEN adding of double content(min2 max4)  WHEN all values typed AND content saved THEN validation message is not displayed"()
    {
        given: " adding of double content(min2 max4)"
        Content doubleContent = buildDouble2_4_Content( TEST_DOUBLE, TEST_DOUBLE, TEST_DOUBLE, TEST_DOUBLE );
        ContentWizardPanel wizard = selectSitePressNew( doubleContent.getContentTypeName() ).waitUntilWizardOpened().typeData(
            doubleContent );
        DoubleFormViewPanel doubleFormViewPanel = new DoubleFormViewPanel( getSession() );

        when: "content saved"
        wizard.save();
        saveScreenshot( "test_double_save_confirm2" );

        then: "validation message is not displayed"
        !doubleFormViewPanel.isValidationMessagePresent();
    }

    def "GIVEN adding of double content(min2 max4)  WHEN three values typed  THEN content is valid AND 'add' button is shown"()
    {
        given: "adding of double content(min2 max4)"
        Content doubleContent = buildDouble2_4_Content( TEST_DOUBLE, TEST_DOUBLE, TEST_DOUBLE );
        ContentWizardPanel wizard = selectSitePressNew( doubleContent.getContentTypeName() );
        DoubleFormViewPanel doubleFormViewPanel = new DoubleFormViewPanel( getSession() );
        when: "three values typed"
        wizard.typeData( doubleContent );

        then: "'add' button displayed in form"
        doubleFormViewPanel.isAddButtonPresent();

        and: "validation message is not displayed"
        !doubleFormViewPanel.isValidationMessagePresent();
    }

    def "GIVEN creating of double content WHEN MAX value typed THEN validation message is not displayed"()
    {
        given: "creating of double content"
        Content doubleContent = buildDouble1_1_Content( MAX_SAFE_INTEGER );
        ContentWizardPanel wizard = selectSitePressNew( doubleContent.getContentTypeName() );
        DoubleFormViewPanel doubleFormViewPanel = new DoubleFormViewPanel( getSession() );

        when: "MAX value typed"
        wizard.typeData( doubleContent ).save();
        saveScreenshot( "test_max_double" );

        then: "validation message is not displayed"
        !doubleFormViewPanel.isValidationMessagePresent();
    }

    def "GIVEN creating of double content WHEN value more than MAX typedd THEN validation message is displayed"()
    {
        given:
        Content doubleContent = buildDouble1_1_Content( MORE_MAX_SAFE_INTEGER );
        ContentWizardPanel wizard = selectSitePressNew( doubleContent.getContentTypeName() );
        DoubleFormViewPanel doubleFormViewPanel = new DoubleFormViewPanel( getSession() );

        when: "value more than MAX typed"
        wizard.typeData( doubleContent );
        saveScreenshot( "test_more_max_double" );

        then: "validation message is displayed"
        doubleFormViewPanel.isValidationMessagePresent();

        and: "input has a red border"
        !doubleFormViewPanel.isValueInInputValid( 0 );
    }
}
