package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ConfirmationDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.DoubleFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore
import spock.lang.Shared

class Occurrences_Double_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    String TEST_DOUBLE = "123.4";

    @Shared
    String DOUBLE_MAX = "1.7976931348623157e+308";

    @Shared
    String DOUBLE_INVALID = "1.7976931348623159e+308";

    def "GIVEN content type with type 'Double' selected and wizard opened WHEN double value typed and content saved THEN new content with correct Double value  listed "()
    {
        given: "add a content with type 'Double'"
        Content doubleContent = buildDouble0_1_Content( TEST_DOUBLE );
        ContentWizardPanel wizard = selectSiteOpenWizard( doubleContent.getContentTypeName() ).waitUntilWizardOpened().typeData(
            doubleContent );
        wizard.save().close( doubleContent.getDisplayName() ); ;

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
    }

    //verifies the XP-3499  (ConfirmationDialog issue)
    def "GIVEN creating a double content WHEN name typed AND save button pressed THEN 'confirmation' dialog should not appears"()
    {
        given: "adding of content with empty value for double"
        Content doubleContent = buildDouble0_1_Content( null );
        ContentWizardPanel wizard = selectSiteOpenWizard( doubleContent.getContentTypeName() ).waitUntilWizardOpened().typeData(
            doubleContent );
        ConfirmationDialog dialog = new ConfirmationDialog( getSession() );

        when: "content saved"
        wizard.save();

        then: "red icon not displayed on the wizard tab"
        !wizard.isContentInvalid( doubleContent.getDisplayName() );

        and: "confirmation dialog should not appears"
        !dialog.isOpened();
    }

    def "GIVEN creating a double content with a required value WHEN name typed AND save button pressed THEN 'confirmation' dialog should not appears"()
    {
        given: "adding of double content without with empty value"
        Content doubleContent = buildDouble1_1_Content( null );
        ContentWizardPanel wizard = selectSiteOpenWizard( doubleContent.getContentTypeName() ).waitUntilWizardOpened().typeData(
            doubleContent );
        ConfirmationDialog dialog = new ConfirmationDialog( getSession() );

        when: "content saved"
        wizard.save();

        then: "red icon displayed on the wizard tab"
        wizard.isContentInvalid( doubleContent.getDisplayName() );

        and: "confirmation dialog should not appears"
        !dialog.isOpened();
    }

    def "GIVEN adding of double content(min2 max4)  WHEN all values typed AND content saved THEN confirmation dialog not appears AND red icon not displayed on the wizard tab"()
    {
        given: " adding of double content(min2 max4)"
        Content doubleContent = buildDouble2_4_Content( TEST_DOUBLE, TEST_DOUBLE, TEST_DOUBLE, TEST_DOUBLE );
        ContentWizardPanel wizard = selectSiteOpenWizard( doubleContent.getContentTypeName() ).waitUntilWizardOpened().typeData(
            doubleContent );
        ConfirmationDialog dialog = new ConfirmationDialog( getSession() );

        when: "content saved"
        wizard.save();

        then: "confirmation dialog should not appears"
        !dialog.isOpened();

        and: "red icon not displayed on the wizard tab"
        !wizard.isContentInvalid( doubleContent.getDisplayName() );
    }

    def "GIVEN adding of double content(min2 max4)  WHEN three values typed  THEN content is valid AND 'add' button shown"()
    {
        given: "adding of double content(min2 max4)"
        Content doubleContent = buildDouble2_4_Content( TEST_DOUBLE, TEST_DOUBLE, TEST_DOUBLE );
        ContentWizardPanel wizard = selectSiteOpenWizard( doubleContent.getContentTypeName() ).waitUntilWizardOpened()

        when: "three values typed"
        wizard.typeData( doubleContent );

        then: "confirmation dialog should not appears"
        wizard

        and: "red icon not displayed on the wizard tab"
        !wizard.isContentInvalid( doubleContent.getDisplayName() );
    }

    @Ignore
    def "GIVEN creating of double content  WHEN MAX value typed THEN red icon not displayed in the wizard tab"()
    {
        given:
        Content doubleContent = buildDouble1_1_Content( DOUBLE_MAX );
        ContentWizardPanel wizard = selectSiteOpenWizard( doubleContent.getContentTypeName() ).waitUntilWizardOpened();

        when: "MAX value typed"
        wizard.typeData( doubleContent ).save();

        then: "red icon not displayed in the wizard tab"
        !wizard.isContentInvalid( doubleContent.getDisplayName() );
    }

    @Ignore
    def "GIVEN creating of double content  WHEN more than MAX value typed THEN red icon should appears in the wizard tab"()
    {
        given:
        Content doubleContent = buildDouble1_1_Content( DOUBLE_INVALID );
        ContentWizardPanel wizard = selectSiteOpenWizard( doubleContent.getContentTypeName() ).waitUntilWizardOpened();

        when: "MAX value typed"
        wizard.typeData( doubleContent ).save();

        then: "red icon not displayed in the wizard tab"
        wizard.isContentInvalid( doubleContent.getDisplayName() );
    }
}
