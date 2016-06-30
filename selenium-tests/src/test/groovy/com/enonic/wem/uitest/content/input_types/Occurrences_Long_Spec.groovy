package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.LongFormViewPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

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
        ContentWizardPanel wizard = selectSiteOpenWizard( VALID_LONG_CONTENT.getContentTypeName() ).waitUntilWizardOpened()
        wizard.typeData( VALID_LONG_CONTENT ).save().close( VALID_LONG_CONTENT.getDisplayName() ); ;

        when: "just created content selected and 'Edit' button clicked"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( VALID_LONG_CONTENT );
        LongFormViewPanel longFormViewPanel = new LongFormViewPanel( getSession() );

        then: "actual value in the form view and expected are equals"
        longFormViewPanel.getLongValue().equals( TEST_LONG );

        and: "content is valid in the wizard"
        !wizard.isContentInvalid( VALID_LONG_CONTENT.getDisplayName() );

        and: "long input has green border (valid value)"
        longFormViewPanel.isValueInInputValid( 0 );
    }

    def "GIVEN creating of content with type 'long'(not required) WHEN invalid value for long typed THEN input with a red border AND red icon not shown on the wizard tab"()
    {
        given: "creating of  content with type 'Long'"
        Content longContent = buildLong0_1_Content( INVALID_LONG1 );
        ContentWizardPanel wizard = selectSiteOpenWizard( longContent.getContentTypeName() ).waitUntilWizardOpened();
        LongFormViewPanel longFormViewPanel = new LongFormViewPanel( getSession() );

        when: "invalid value for long typed"
        wizard.typeData( longContent );
        TestUtils.saveScreenshot( getSession(), "test_long_invalid_not_req" );

        then: "input with a red border"
        !longFormViewPanel.isValueInInputValid( 0 );

        and: "red icon not shown on the wizard tab, because this input is not required"
        !wizard.isContentInvalid( longContent.getDisplayName() );

        and: "'Publish' button on the wizard-toolbar is enabled, because input is not required"
        wizard.isPublishButtonEnabled();
    }

    def "GIVEN creating of content with type 'long'(required) WHEN invalid value for long typed THEN input with a red border AND red icon shown on the wizard tab"()
    {
        given: "creating of  content with type 'Long'"
        Content longContent = buildLong1_1_Content( INVALID_LONG1 );
        ContentWizardPanel wizard = selectSiteOpenWizard( longContent.getContentTypeName() ).waitUntilWizardOpened();
        LongFormViewPanel longFormViewPanel = new LongFormViewPanel( getSession() );

        when: "invalid value for long typed"
        wizard.typeData( longContent );
        TestUtils.saveScreenshot( getSession(), "test_long_invalid_req" );

        then: "input with a red border"
        !longFormViewPanel.isValueInInputValid( 0 );

        and: "red icon should be shown on the wizard tab, because this input is required"
        wizard.isContentInvalid( longContent.getDisplayName() );

        and: "'Publish' button on the wizard-toolbar is disabled, because input is required"
        !wizard.isPublishButtonEnabled();
    }

    def "GIVEN creating a long content with a required input WHEN name typed AND save button pressed THEN 'confirmation' dialog should not appears"()
    {
        given: "adding of long content with empty value"
        Content longContent = buildLong1_1_Content( null );
        ContentWizardPanel wizard = selectSiteOpenWizard( longContent.getContentTypeName() ).waitUntilWizardOpened().typeData(
            longContent );
        ConfirmationDialog dialog = new ConfirmationDialog( getSession() );

        when: "content saved"
        wizard.save();
        TestUtils.saveScreenshot( getSession(), "test_long_save_confirm1" );

        then: "red icon displayed on the wizard tab"
        wizard.isContentInvalid( longContent.getDisplayName() );

        and: "confirmation dialog should not appears"
        !dialog.isOpened();
    }

    def "GIVEN creating of long content WHEN MAX_SAFE value typed THEN red icon not displayed in the wizard tab"()
    {
        given: "creating of double content"
        Content doubleContent = buildLong1_1_Content( MAX_SAFE_INTEGER );
        ContentWizardPanel wizard = selectSiteOpenWizard( doubleContent.getContentTypeName() ).waitUntilWizardOpened();
        LongFormViewPanel longFormViewPanel = new LongFormViewPanel( getSession() );

        when: "MAX value typed"
        wizard.typeData( doubleContent ).save();
        TestUtils.saveScreenshot( getSession(), "test_max_long1" );

        then: "red icon not displayed in the wizard tab"
        !wizard.isContentInvalid( doubleContent.getDisplayName() );

        and: "input has no a red border"
        longFormViewPanel.isValueInInputValid( 0 );
    }


    def "GIVEN creating of long content WHEN value is more than MAX_SAFE THEN red icon appears in the wizard tab"()
    {
        given: "creating of long content"
        Content longContent = buildLong1_1_Content( MORE_MAX_SAFE_INTEGER );
        ContentWizardPanel wizard = selectSiteOpenWizard( longContent.getContentTypeName() ).waitUntilWizardOpened();
        LongFormViewPanel longFormViewPanel = new LongFormViewPanel( getSession() );

        when: "MAX value typed"
        wizard.typeData( longContent );
        TestUtils.saveScreenshot( getSession(), "test_more_max_long" );

        then: "red icon displayed on the wizard tab"
        wizard.isContentInvalid( longContent.getDisplayName() );

        and: "input has a red border"
        !longFormViewPanel.isValueInInputValid( 0 );
    }

    def "GIVEN creating of long content WHEN MIN_SAFE value typed THEN red icon not displayed in the wizard tab"()
    {
        given: "creating of double content"
        MIN_SAFE_CONTENT = buildLong1_1_Content( MIN_SAFE_INTEGER );
        ContentWizardPanel wizard = selectSiteOpenWizard( MIN_SAFE_CONTENT.getContentTypeName() ).waitUntilWizardOpened();
        LongFormViewPanel longFormViewPanel = new LongFormViewPanel( getSession() );

        when: "MAX value typed"
        wizard.typeData( MIN_SAFE_CONTENT ).save();
        TestUtils.saveScreenshot( getSession(), "test_min_long" );

        then: "red icon not displayed in the wizard tab"
        !wizard.isContentInvalid( MIN_SAFE_CONTENT.getDisplayName() );

        and: "input has no a red border"
        longFormViewPanel.isValueInInputValid( 0 );
    }

    def "GIVEN content with MIN_SAFE_VALUE has been added WHEN content opened THEN correct value displayed on the wizard page"()
    {
        given: "content with MIN_SAFE_VALUE has been added"
        findAndSelectContent( MIN_SAFE_CONTENT.getName() )

        when: "content opened"
        contentBrowsePanel.clickToolbarEdit();
        LongFormViewPanel longFormViewPanel = new LongFormViewPanel( getSession() );
        TestUtils.saveScreenshot( getSession(), "test_long_min_value" );

        then: "correct value displayed on the wizard page"
        longFormViewPanel.getLongValue() == MIN_SAFE_INTEGER
    }

    def "GIVEN content with valid long value WHEN content opened  AND input cleared THEN red icon appears in the wizard tab"()
    {
        given: "content with MIN_SAFE_VALUE has been added"
        findAndSelectContent( MIN_SAFE_CONTENT.getName() )

        when: "content opened  AND input cleared"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        LongFormViewPanel longFormViewPanel = new LongFormViewPanel( getSession() );
        longFormViewPanel.typeLongValue( "" );
        sleep( 500 );
        TestUtils.saveScreenshot( getSession(), "test_long_input_cleared" );

        then: "red icon appears in the wizard tab"
        wizard.isContentInvalid( MIN_SAFE_CONTENT.getDisplayName() );
    }
}
