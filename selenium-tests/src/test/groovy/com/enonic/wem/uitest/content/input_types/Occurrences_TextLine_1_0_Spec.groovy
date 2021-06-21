package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.TextLine1_0_FormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.data.ValueFactory
import spock.lang.Shared

class Occurrences_TextLine_1_0_Spec
    extends Base_InputFields_Occurrences

{
    @Shared
    String FIRST_TEST_STRING = "text for first input";

    @Shared
    String SECOND_TEST_STRING = "text for second input";

    def "WHEN wizard for adding a TextLine(1:0) content is opened THEN one text input should be present"()
    {
        when: "wizard for adding a TextLine(1:0) content is opened"
        Content textLineContent = buildTextLine1_0_Content( FIRST_TEST_STRING );
        ContentWizardPanel wizard = selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine1_0_FormViewPanel formViewPanel = new TextLine1_0_FormViewPanel( getSession() );

        and: "the display name was typed"
        wizard.typeDisplayName( textLineContent.getDisplayName() );

        then: "one text input should be displayed in the form view "
        formViewPanel.getNumberOfTextInputs() == 1;

        and: "button 'Add' should be displayed under the text input"
        formViewPanel.isAddButtonPresent();

        and: "the 'remove button' should not be displayed on the form view"
        formViewPanel.getNumberOfDisplayedRemoveButtons() == 0;

        and: "red icon should be present, because the required input is empty"
        wizard.isContentInvalid();
    }

    def "GIVEN wizard for adding a TextLine(1:0) content is opened WHEN all required data has been typed THEN red icon should not be displayed on the wizard page"()
    {
        given: "wizard for adding a TextLine(1:0) content is opened"
        Content textLineContent = buildTextLine1_0_Content( FIRST_TEST_STRING );
        ContentWizardPanel wizard = selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine1_0_FormViewPanel formViewPanel = new TextLine1_0_FormViewPanel( getSession() );

        when: "text and name has been typed"
        wizard.typeData( textLineContent ).save();
        saveScreenshot( "textline_valid_content" );

        then: "red icon should not be present, because the required input is not empty"
        !wizard.isContentInvalid();

        and: "validation message should not be present on the page"
        !formViewPanel.isFormValidationMessageDisplayed();
    }

    def "GIVEN wizard for adding a TextLine(1:0) content is opened WHEN  required data was not typed but the content is saved THEN red icon should be displayed on the wizard page"()
    {
        given: "wizard for adding a TextLine(1:0) content is opened"
        Content textLineContent = buildTextLine1_0_Content( FIRST_TEST_STRING );
        ContentWizardPanel wizard = selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine1_0_FormViewPanel formViewPanel = new TextLine1_0_FormViewPanel( getSession() );

        when: "text and name has been typed"
        wizard.typeDisplayName( textLineContent.getDisplayName() ).save();
        saveScreenshot( "textline_not_valid_content" );

        then: "red icon should be present, because the required input is empty"
        wizard.isContentInvalid();

        and: "correct validation message should be present"
        formViewPanel.getFormValidationRecording( 0 ) == String.format( Application.MIN_VALID_OCCURRENCE_REQUIRED, 1 );
    }

    def "GIVEN wizard for content with type TextLine(1:0) is opened WHEN 'Add' button has been pressed and 2 inputs now showed THEN two 'remove' button near the both text input are present  "()
    {
        given: "wizard for content with type TextLine(1:0) is opened"
        Content textLineContent = buildTextLine1_0_Content( FIRST_TEST_STRING );
        selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine1_0_FormViewPanel formViewPanel = new TextLine1_0_FormViewPanel( getSession() );

        when: "'Add' button has been clicked"
        formViewPanel.clickOnAddButton();

        then: "the number of 'remove buttons' should be equals 2 and should be the same as a number of text inputs"
        formViewPanel.getNumberOfDisplayedRemoveButtons() == 2;

        and: "two text inputs should be present"
        formViewPanel.getNumberOfTextInputs() == 2
    }

    def "GIVEN wizard for content with type TextLine(1:0) is opened WHEN button 'Add' was pressed twice and 3 inputs are shown THEN 3 'remove' button should be present"()
    {
        given: "wizard for content with type TextLine(1:0) is opened"
        Content textLineContent = buildTextLine1_0_Content( FIRST_TEST_STRING );
        selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine1_0_FormViewPanel formViewPanel = new TextLine1_0_FormViewPanel( getSession() );

        when: "button 'Add' was pressed twice"
        formViewPanel.clickOnAddButton(); formViewPanel.clickOnAddButton();

        then: "the number of 'remove buttons' should be the same as the number of text inputs"
        formViewPanel.getNumberOfDisplayedRemoveButtons() == 3;

        and:
        formViewPanel.getNumberOfTextInputs() == 3
    }

    def "GIVEN wizard for content with type TextLine(1:0) is opened WHEN 'Add' button was pressed and 2 inputs are shown AND one input was removed THEN no one button 'remove' present and only one text input present"()
    {
        given: "wizard for content with type TextLine(1:0) is opened"
        Content textLineContent = buildTextLine1_0_Content( FIRST_TEST_STRING );
        selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine1_0_FormViewPanel formViewPanel = new TextLine1_0_FormViewPanel( getSession() );
        and: "one more input was added"
        formViewPanel.clickOnAddButton();
        int numberBeforeRemoving = formViewPanel.getNumberOfTextInputs()

        when: "one input has been removed"
        formViewPanel.clickOnLastRemoveButton();

        then: "remove buttons should not be present on the page"
        formViewPanel.getNumberOfDisplayedRemoveButtons() == 0;

        and: "number of inputs is reduced"
        formViewPanel.getNumberOfTextInputs() == 1;

        and:
        numberBeforeRemoving == 2;
    }

    def "GIVEN wizard for content with type TextLine(1:0) is opened WHEN content was saved and wizard closed THEN new content should be listed and 2 saved values should be present when content is opened"()
    {
        given: "wizard for content with type TextLine(1:0) is opened"
        Content textLineContent = buildTextLine1_0_Content( FIRST_TEST_STRING );
        ContentWizardPanel wizard = selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine1_0_FormViewPanel formViewPanel = new TextLine1_0_FormViewPanel( getSession() );
        and: "one more input has been added"
        formViewPanel.clickOnAddButton();
        textLineContent.getData().addProperty( "1", ValueFactory.newString( SECOND_TEST_STRING ) );

        when: "two inputs has been filled and the content saved and wizard closed"
        wizard.typeData( textLineContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        then: "new content should be listed in the grid"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( textLineContent );
        List<String> valuesFromUI = formViewPanel.getTextLineValues();

        and: "numbers of inputs should be 2"
        formViewPanel.getNumberOfTextInputs() == 2;

        and: "required strings should be present on the wizard page"
        valuesFromUI.contains( FIRST_TEST_STRING );

        and:
        valuesFromUI.contains( SECOND_TEST_STRING );

        and: "red icon should not be present on the wizard page, because the required input is not empty"
        !wizard.isContentInvalid();
    }
}
