package com.enonic.wem.uitest.content.input_types

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

    def "GIVEN creating of 'Long'-content WHEN just created content opened THEN correct 'Long' value present in wizard AND content is valid AND input has a green border "()
    {
        given: "creating of  content with type 'Long'"
        VALID_LONG_CONTENT = buildLong0_1_Content( TEST_LONG );
        ContentWizardPanel wizard = selectSitePressNew( VALID_LONG_CONTENT.getContentTypeName() ).waitUntilWizardOpened()
        wizard.typeData( VALID_LONG_CONTENT ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "just created content selected and 'Edit' button clicked"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( VALID_LONG_CONTENT );
        LongFormViewPanel longFormViewPanel = new LongFormViewPanel( getSession() );

        then: "actual value in the form view and expected are equals"
        longFormViewPanel.getLongValue().equals( TEST_LONG );

        and: "red icon should not be displayed on the wizard page"
        !wizard.isContentInvalid();

        and: "long input has green border (valid value)"
        longFormViewPanel.isValueInInputValid( 0 );
    }

    def "GIVEN creating of content with type 'long'(not required) WHEN invalid value for long typed THEN input should be displayed with a red border AND red icon should not shown on the wizard tab"()
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

        and: "validation message should be displayed"
        longFormViewPanel.isValidationMessagePresent();

        and: "'Publish' menu item should be enabled, because input is not required"
        wizard.showPublishMenu().isPublishMenuItemEnabled(  );
    }

    def "GIVEN new wizard for 'long'(required) is opened WHEN not valid value for long typed THEN input with a red border AND red icon shown on the wizard tab"()
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

        and: "validation message should be displayed, because this input is required and value is not allowed"
        longFormViewPanel.isValidationMessagePresent();

        and: "'Publish' button on the wizard-toolbar is disabled, because input is required"
        !wizard.isPublishButtonEnabled();
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
        longFormViewPanel.isValidationMessagePresent();

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

        then: "validation message should not be displayed(the value is allowed)"
        !longFormViewPanel.isValidationMessagePresent();

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

        and: "red icon should be present on the wizard page, because the input is required and value is not allowed"
        wizard.isContentInvalid();
    }

    def "GIVEN wizard for 'long' content is opened WHEN MIN_SAFE value was typed THEN red icon should not be displayed on the wizard page"()
    {
        given: "wizard for long content is opened"
        MIN_SAFE_CONTENT = buildLong1_1_Content( MIN_SAFE_INTEGER );
        ContentWizardPanel wizard = selectSitePressNew( MIN_SAFE_CONTENT.getContentTypeName() ).waitUntilWizardOpened();
        LongFormViewPanel longFormViewPanel = new LongFormViewPanel( getSession() );

        when: "MIN value was typed"
        wizard.typeData( MIN_SAFE_CONTENT ).save();
        saveScreenshot( "test_min_long" );

        then: "input should be without a red border"
        longFormViewPanel.isValueInInputValid( 0 );

        and: "red icon should not be present on the wizard page, because the value is allowed"
        !wizard.isContentInvalid();
    }

    def "GIVEN existing content with MIN_SAFE_VALUE  WHEN content opened THEN correct value should be displayed on the wizard page"()
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

    def "GIVEN existing content with required input(long) is selected WHEN content was opened AND input has been cleared THEN red icon should be present on the wizard page"()
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
        longFormViewPanel.isValidationMessagePresent();
    }
}
