package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.LongFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Occurrences_Long_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    String INVALID_LONG1 = "123.4";

    @Shared
    String MORE_MAX_SAFE_CONTENT;

    @Shared
    String TEST_LONG = "1234567890123456";

    @Shared
    Content VALID_LONG_CONTENT;

    @Shared
    Content MIN_SAFE_CONTENT;

    def "GIVEN long-content(0:1) is saved WHEN just created content is reopened THEN expected 'Long' value should be present in wizard"()
    {
        given: "long-content(0:1) is saved"
        VALID_LONG_CONTENT = buildLong0_1_Content( TEST_LONG );
        ContentWizardPanel wizard = selectSitePressNew( VALID_LONG_CONTENT.getContentTypeName() ).waitUntilWizardOpened()
        wizard.typeData( VALID_LONG_CONTENT ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "just created content selected and 'Edit' button clicked"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( VALID_LONG_CONTENT );
        LongFormViewPanel longFormViewPanel = new LongFormViewPanel( getSession() );

        then: "expected value should be present"
        longFormViewPanel.getLongValue() == TEST_LONG;

        and: "red icon should not be displayed in the wizard page"
        !wizard.isContentInvalid();

        and: "long input has green border (valid value)"
        longFormViewPanel.isValueInInputValid( 0 );
    }

    def "GIVEN wizard for long-content(0:1) is opened WHEN invalid value has been typed THEN input should be displayed with a red border AND red icon should not shown in the wizard tab"()
    {
        given: "wizard for 'long' content(not required) is opened"
        Content longContent = buildLong0_1_Content( INVALID_LONG1 );
        ContentWizardPanel wizard = selectSitePressNew( longContent.getContentTypeName() ).waitUntilWizardOpened();
        LongFormViewPanel longFormViewPanel = new LongFormViewPanel( getSession() );

        when: "invalid value for long has been typed"
        wizard.typeData( longContent );
        saveScreenshot( "test_long_invalid_not_req" );

        then: "input should be displayed with a red border"
        !longFormViewPanel.isValueInInputValid( 0 );

        and: "red icon should not be displayed, because the input is not required"
        !wizard.isContentInvalid();

        and: "form validation message should be displayed"
        !longFormViewPanel.isFormValidationMessageDisplayed();

        and: "'Publish' menu item should be enabled, because input is not required"
        wizard.showPublishMenu().isPublishMenuItemEnabled();
    }

    def "GIVEN new wizard for 'long'(1:1) is opened WHEN not valid value has been typed THEN input with a red border AND red icon shown in the wizard tab"()
    {
        given: "creating of  content with type 'Long'"
        Content longContent = buildLong1_1_Content( INVALID_LONG1 );
        ContentWizardPanel wizard = selectSitePressNew( longContent.getContentTypeName() ).waitUntilWizardOpened();
        LongFormViewPanel longFormViewPanel = new LongFormViewPanel( getSession() );

        when: "not valid value has been typed"
        wizard.typeData( longContent );
        saveScreenshot( "test_long_invalid_req" );

        then: "input should be with red border"
        !longFormViewPanel.isValueInInputValid( 0 );

        and: "content should be displayed with red icon in the wizard page"
        wizard.isContentInvalid();

        and: "input occurrence validation message should be displayed, because this input is required and value is not allowed"
        longFormViewPanel.getOccurrenceValidationRecording( 0 ) == Application.INVALID_VALUE_ENTERED;

        and: "'Publish' menu item should be disabled, because input is required"
        !wizard.showPublishMenu().isPublishMenuItemEnabled();
    }

    def "GIVEN wizard for long content(required) WHEN name has been typed AND 'Save' button has been pressed THEN 'confirmation' dialog should not appear"()
    {
        given: "wizard for a long content with required input"
        Content longContent = buildLong1_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( longContent.getContentTypeName() ).waitUntilWizardOpened().typeData( longContent );
        LongFormViewPanel longFormViewPanel = new LongFormViewPanel( getSession() );
        ConfirmationDialog dialog = new ConfirmationDialog( getSession() );

        when: "Save button has been pressed"
        wizard.save();
        saveScreenshot( "test_long_save_confirm1" );

        then: "validation message should be displayed, because this input is required and input is empty"
        longFormViewPanel.getFormValidationRecording( 0 ) == Application.REQUIRED_MESSAGE;

        and: "confirmation dialog should not appear"
        !dialog.isOpened();

        and: "red icon should be displayed on the wizard page, because the input is required and empty"
        wizard.isContentInvalid();
    }

    def "GIVEN wizard for 'long' content(required) is opened WHEN MAX_SAFE value was typed THEN red icon should not be displayed on the wizard page"()
    {
        given: "wizard for creating of long content(required) is opened"
        Content doubleContent = buildLong1_1_Content( MAX_SAFE_INTEGER );
        ContentWizardPanel wizard = selectSitePressNew( doubleContent.getContentTypeName() ).waitUntilWizardOpened();
        LongFormViewPanel longFormViewPanel = new LongFormViewPanel( getSession() );

        when: "MAX value was typed"
        wizard.typeData( doubleContent ).save();
        saveScreenshot( "test_max_long1" );

        then: "form validation message should not be displayed(the value is allowed)"
        !longFormViewPanel.isFormValidationMessageDisplayed();

        and: "input has no a red border"
        longFormViewPanel.isValueInInputValid( 0 );

        and: "red icon should not be displayed on the wizard page(the value is allowed)"
        !wizard.isContentInvalid();
    }

    def "GIVEN wizard for 'long' content is opened WHEN value is more than MAX_SAFE THEN red icon should be present on the wizard page"()
    {
        given: "wizard for 'long' content(required) is opened"
        Content longContent = buildLong1_1_Content( MORE_MAX_SAFE_INTEGER );
        ContentWizardPanel wizard = selectSitePressNew( longContent.getContentTypeName() ).waitUntilWizardOpened();
        LongFormViewPanel longFormViewPanel = new LongFormViewPanel( getSession() );

        when: "value more than MAX was typed"
        wizard.typeData( longContent );
        saveScreenshot( "test_more_max_long" );

        then: "input should be with red border, because the value is not allowed"
        !longFormViewPanel.isValueInInputValid( 0 );

        and: "Invalid value entered - this message gets visible"
        longFormViewPanel.getOccurrenceValidationRecording( 0 ) == Application.INVALID_VALUE_ENTERED;

        and: "red icon should be present on the wizard page, because the input is required and value is not allowed"
        wizard.isContentInvalid();
    }

    def "GIVEN wizard for 'long' content is opened WHEN 'MIN_SAFE' value has been typed THEN red icon should not be displayed in the wizard page"()
    {
        given: "wizard for long content is opened"
        MIN_SAFE_CONTENT = buildLong1_1_Content( MIN_SAFE_INTEGER );
        ContentWizardPanel wizard = selectSitePressNew( MIN_SAFE_CONTENT.getContentTypeName() ).waitUntilWizardOpened();
        LongFormViewPanel longFormViewPanel = new LongFormViewPanel( getSession() );

        when: "MIN value has been typed"
        wizard.typeData( MIN_SAFE_CONTENT ).save();
        saveScreenshot( "test_min_long" );

        then: "input should be without a red border"
        longFormViewPanel.isValueInInputValid( 0 );

        and: "red icon should not be present in the wizard page, because the value is allowed"
        !wizard.isContentInvalid();
    }

    def "GIVEN existing content with 'MIN_SAFE_VALUE' WHEN content is opened THEN expected long-value should be present"()
    {
        given: "existing content with MIN_SAFE_VALUE is selected "
        findAndSelectContent( MIN_SAFE_CONTENT.getName() )

        when: "content is opened"
        contentBrowsePanel.clickToolbarEdit();
        LongFormViewPanel longFormViewPanel = new LongFormViewPanel( getSession() );
        saveScreenshot( "test_long_min_value" );

        then: "correct value should be present on the wizard page"
        longFormViewPanel.getLongValue() == MIN_SAFE_INTEGER
    }

    def "GIVEN existing content with required input(long) is opened WHEN input has been cleared THEN red icon gets visible in the wizard page"()
    {
        given: "existing content with required input(long)"
        findAndSelectContent( MIN_SAFE_CONTENT.getName() )

        when: "the content is opened"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        LongFormViewPanel longFormViewPanel = new LongFormViewPanel( getSession() );
        and: "required input has been cleared"
        longFormViewPanel.typeLongValue( "" );
        wizard.save();
        sleep( 500 );
        saveScreenshot( "test_long_input_cleared" );

        then: "validation message should be present, because the input is required"
        longFormViewPanel.getFormValidationRecording( 0 ) == Application.REQUIRED_MESSAGE;

        and: "the content gets invalid:"
        wizard.isContentInvalid();
    }
}
