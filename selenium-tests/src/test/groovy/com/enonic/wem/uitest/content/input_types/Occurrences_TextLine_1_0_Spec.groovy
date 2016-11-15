package com.enonic.wem.uitest.content.input_types

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


    def "WHEN wizard for adding a content with type TextLine(1:0) opened THEN one text input present"()
    {
        when: "start to add a content with type 'TextLine 1:0'"
        Content textLineContent = buildTextLine1_0_Content( FIRST_TEST_STRING );
        selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine1_0_FormViewPanel formViewPanel = new TextLine1_0_FormViewPanel( getSession() );

        then: "one text input should be displayed in the form view "
        formViewPanel.getNumberOfTextInputs() == 1;
    }

    def "WHEN wizard for adding a content with type TextLine(1:0) opened THEN 'Add' button under the text input is present"()
    {
        when: "start to add a content with type 'TextLine 1:0'"
        Content textLineContent = buildTextLine1_0_Content( FIRST_TEST_STRING );
        selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine1_0_FormViewPanel formViewPanel = new TextLine1_0_FormViewPanel( getSession() );

        then: "button 'Add' should be displayed under the text input  in 'form view panel'"
        formViewPanel.isAddButtonPresent();
    }

    def "WHEN wizard for adding a content with type TextLine(1:0) opened THEN 'remove' button near the text input is not present"()
    {
        when: "start to add a content with type 'TextLine 1:0'"
        Content textLineContent = buildTextLine1_0_Content( FIRST_TEST_STRING );
        selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine1_0_FormViewPanel formViewPanel = new TextLine1_0_FormViewPanel( getSession() );

        then: "the 'remove button' should not be displayed in the form view"
        formViewPanel.getNumberOfDisplayedRemoveButtons() == 0;
    }

    def "GIVEN wizard for adding a content with type TextLine(1:0) opened WHEN 'Add' button pressed and 2 inputs now showed THEN two 'remove' button near the both text input are present  "()
    {
        given: "start to add a content with type 'TextLine 1:0'"
        Content textLineContent = buildTextLine1_0_Content( FIRST_TEST_STRING );
        selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine1_0_FormViewPanel formViewPanel = new TextLine1_0_FormViewPanel( getSession() );

        when: "'Add' button clicked"
        formViewPanel.clickOnAddButton();

        then: "the number of 'remove buttons' equals 2 and should be the same as a number of text inputs"
        formViewPanel.getNumberOfDisplayedRemoveButtons() == 2;

        and:
        formViewPanel.getNumberOfTextInputs() == 2
    }

    def "GIVEN wizard for adding a content with type TextLine(1:0) opened WHEN button 'Add' twice pressed and 3 inputs showed THEN 3 'remove' button near the all text inputs are present"()
    {
        given: "start to add a content with type 'TextLine 1:0'"
        Content textLineContent = buildTextLine1_0_Content( FIRST_TEST_STRING );
        selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine1_0_FormViewPanel formViewPanel = new TextLine1_0_FormViewPanel( getSession() );

        when: "button 'Add' twice pressed"
        formViewPanel.clickOnAddButton(); formViewPanel.clickOnAddButton();

        then: "the number of 'remove buttons' the same as number of text inputs"
        formViewPanel.getNumberOfDisplayedRemoveButtons() == 3;

        and:
        formViewPanel.getNumberOfTextInputs() == 3
    }

    def "GIVEN wizard for adding a content with type TextLine(1:0) opened WHEN 'Add' button pressed and 2 inputs showed AND one input was removed THEN no one button 'remove' present and only one text input present"()
    {
        given: "start to add a content with type 'TextLine 1:0'"
        Content textLineContent = buildTextLine1_0_Content( FIRST_TEST_STRING );
        selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine1_0_FormViewPanel formViewPanel = new TextLine1_0_FormViewPanel( getSession() );
        formViewPanel.clickOnAddButton();
        int numberBeforeRemoving = formViewPanel.getNumberOfTextInputs()

        when:
        formViewPanel.clickOnLastRemoveButton();

        then: "remove buttons not present on the page"
        formViewPanel.getNumberOfDisplayedRemoveButtons() == 0;

        and: "number of inputs reduced"
        formViewPanel.getNumberOfTextInputs() == 1;

        and:
        numberBeforeRemoving == 2;
    }

    def "GIVEN creating new Content on root WHEN saved and wizard closed THEN new text line Content should be listed  and 2 saved values showed when content opened for edit "()
    {
        given: "start to add a content with type 'TextLine 1:0'"
        Content textLineContent = buildTextLine1_0_Content( FIRST_TEST_STRING );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine1_0_FormViewPanel formViewPanel = new TextLine1_0_FormViewPanel( getSession() );
        formViewPanel.clickOnAddButton();
        textLineContent.getData().addProperty( "1", ValueFactory.newString( SECOND_TEST_STRING ) );

        when: "content saved and wizard closed"
        contentWizardPanel.typeData( textLineContent ).save().close( textLineContent.getDisplayName() );

        then: "new content listed in the grid and can be opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( textLineContent );
        List<String> valuesFromUI = formViewPanel.getTextLineValues();

        and: "numbers of inputs is 2"
        formViewPanel.getNumberOfTextInputs() == 2;

        and: "saved strings are present in the Content Wizard"
        valuesFromUI.contains( FIRST_TEST_STRING );

        and:
        valuesFromUI.contains( SECOND_TEST_STRING );
    }
}