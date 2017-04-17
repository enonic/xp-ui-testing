package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.DoubleFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class Occurrences_Double_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    String TEST_DOUBLE = "123.4";

    @Shared
    String WRONG_DOUBLE = "-111111111111111111111111";

    def "GIVEN wizard for 'double' content(not required) is opened WHEN invalid value has been typed THEN input should be with red border BUT red icon should not be shown on the wizard page"()
    {
        given: " wizard for 'double' content(not required) is opened"
        Content doubleContent = buildDouble0_0_Content( WRONG_DOUBLE );
        ContentWizardPanel wizard = selectSitePressNew( doubleContent.getContentTypeName() ).waitUntilWizardOpened();
        DoubleFormViewPanel doubleFormViewPanel = new DoubleFormViewPanel( getSession() );

        when: "invalid long value was typed"
        wizard.typeData( doubleContent );
        saveScreenshot( "test_double_invalid_not_req" );

        then: "input should be with red border"
        !doubleFormViewPanel.isValueInInputValid( 0 );

        and: "'Publish' button on the wizard-toolbar is enabled, because input is not required"
        wizard.isPublishButtonEnabled();

        and: "red icon should not be present on the wizard page, because the value is not required"
        !wizard.isContentInvalid();
    }

    def "GIVEN creating of 'Double' content WHEN the content opened THEN correct Double value should be present on the wizard AND content is valid"()
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

        then: "one input with the value should be displayed"
        values.size() == 1;

        and: "actual value in the form view and expected should be equals"
        values.get( 0 ).equals( TEST_DOUBLE );

        and: "validation message should not be displayed, because content is valid"
        !doubleFormViewPanel.isValidationMessagePresent();

        and: "double input has no a red border"
        doubleFormViewPanel.isValueInInputValid( 0 );

        and: "red icon should not be displayed on the wizard page"
        !wizard.isContentInvalid();
    }
    //verifies the XP-3499  (ConfirmationDialog issue)
    def "GIVEN creating a double content WHEN name typed AND save button pressed THEN 'confirmation' dialog should not be displayed"()
    {
        given: "adding of content with empty value for double"
        Content doubleContent = buildDouble0_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( doubleContent.getContentTypeName() ).waitUntilWizardOpened().typeData(
            doubleContent );
        ConfirmationDialog dialog = new ConfirmationDialog( getSession() );
        DoubleFormViewPanel doubleFormViewPanel = new DoubleFormViewPanel( getSession() );

        when: "content was saved"
        wizard.save();

        then: "validation message should not be displayed"
        !doubleFormViewPanel.isValidationMessagePresent();

        and: "confirmation dialog should not appear"
        !dialog.isOpened();

        and: "red icon should not be displayed on the wizard page, because the input is not required"
        !wizard.isContentInvalid();
    }

    def "GIVEN creating a double content with a required value WHEN name typed AND save button pressed THEN validation message appears"()
    {
        given: "adding of double content with empty value"
        Content doubleContent = buildDouble1_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( doubleContent.getContentTypeName() ).waitUntilWizardOpened().typeData(
            doubleContent );
        DoubleFormViewPanel doubleFormViewPanel = new DoubleFormViewPanel( getSession() );

        when: "content was saved"
        wizard.save();
        saveScreenshot( "test_double_save_confirm1" );

        then: "validation message appears, because required input is empty"
        doubleFormViewPanel.isValidationMessagePresent();

        and: "red icon should be present on the wizard page, because the input is required"
        wizard.isContentInvalid();
    }

    def "GIVEN adding of double content(min2 max4)  WHEN all values were typed AND content was saved THEN validation message should not be displayed"()
    {
        given: "adding of double content(min2 max4)"
        Content doubleContent = buildDouble2_4_Content( TEST_DOUBLE, TEST_DOUBLE, TEST_DOUBLE, TEST_DOUBLE );
        ContentWizardPanel wizard = selectSitePressNew( doubleContent.getContentTypeName() ).waitUntilWizardOpened().typeData(
            doubleContent );
        DoubleFormViewPanel doubleFormViewPanel = new DoubleFormViewPanel( getSession() );

        when: "content was saved"
        wizard.save();
        saveScreenshot( "test_double_save_confirm2" );

        then: "validation message should not be displayed"
        !doubleFormViewPanel.isValidationMessagePresent();

        and: "red icon should not be displayed, because 4 numbers were typed"
        !wizard.isContentInvalid();
    }

    def "GIVEN adding of double content(min2 max4) WHEN three values were typed THEN the content should be valid AND 'add' button should be present"()
    {
        given: "adding of double content(min2 max4)"
        Content doubleContent = buildDouble2_4_Content( TEST_DOUBLE, TEST_DOUBLE, TEST_DOUBLE );
        ContentWizardPanel wizard = selectSitePressNew( doubleContent.getContentTypeName() );
        DoubleFormViewPanel doubleFormViewPanel = new DoubleFormViewPanel( getSession() );
        when: "three values were typed"
        wizard.typeData( doubleContent );

        then: "'add' button should be displayed on the form"
        doubleFormViewPanel.isAddButtonPresent();

        and: "validation message should not be displayed"
        !doubleFormViewPanel.isValidationMessagePresent();

        and: "red icon should not be displayed, because 3 numbers were typed"
        !wizard.isContentInvalid();
    }

    def "GIVEN creating of double content WHEN MAX value typed THEN validation message should not be displayed"()
    {
        given: "creating of double content"
        Content doubleContent = buildDouble1_1_Content( MAX_SAFE_INTEGER );
        ContentWizardPanel wizard = selectSitePressNew( doubleContent.getContentTypeName() );
        DoubleFormViewPanel doubleFormViewPanel = new DoubleFormViewPanel( getSession() );

        when: "MAX value was typed"
        wizard.typeData( doubleContent ).save();
        saveScreenshot( "test_max_double" );

        then: "validation message should not be displayed"
        !doubleFormViewPanel.isValidationMessagePresent();

        and: "red icon should not be displayed, the value is allowed and input is required"
        !wizard.isContentInvalid();
    }

    def "GIVEN creating of double content WHEN value more than MAX was typed THEN validation message should be displayed"()
    {
        given: "creating of double content"
        Content doubleContent = buildDouble1_1_Content( MORE_MAX_SAFE_INTEGER );
        ContentWizardPanel wizard = selectSitePressNew( doubleContent.getContentTypeName() );
        DoubleFormViewPanel doubleFormViewPanel = new DoubleFormViewPanel( getSession() );

        when: "value more than MAX was typed"
        wizard.typeData( doubleContent );
        saveScreenshot( "test_more_max_double" );

        then: "validation message should be displayed"
        doubleFormViewPanel.isValidationMessagePresent();

        and: "input has a red border"
        !doubleFormViewPanel.isValueInInputValid( 0 );

        and: "red icon should be present, the value is not allowed and the input is required"
        wizard.isContentInvalid();
    }
}
