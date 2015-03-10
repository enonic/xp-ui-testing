package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.TextLine1_0_FormViewPanel
import com.enonic.autotests.pages.form.TextLine1_1_FormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import com.enonic.xp.data.Value
import spock.lang.Ignore
import spock.lang.Shared

class Occurences_TextLine_1_1_Spec
    extends Base_InputFields_Occurences

{
    @Shared
    String TEST_TEXT = "test text 1:1";


    def "WHEN wizard for adding a TextLine-content (1:1) opened THEN one text input present "()
    {
        when: "start to add a content with type 'TextLine 1:1'"
        Content textLineContent = buildTextLine1_1_Content();
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            textLineContent.getContentTypeName() )
        TextLine1_1_FormViewPanel formViewPanel = new TextLine1_1_FormViewPanel( getSession() );

        then: "one text input should be displayed in the form view"
        formViewPanel.getNumberOfTextInputs() == 1;
    }

    def "WHEN wizard for adding a TextLine-content (1:1) opened THEN button 'Add' not present on page "()
    {
        when: "start to add a content with type 'TextLine 1:1'"
        Content textLineContent = buildTextLine1_1_Content();
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            textLineContent.getContentTypeName() )
        TextLine1_1_FormViewPanel formViewPanel = new TextLine1_1_FormViewPanel( getSession() );

        then: "one text input should be displayed in the form view"
        formViewPanel.getNumberOfDisplayedRemoveButtons() == 0;
    }

    def "GIVEN creating new TextLine1:1 on root WHEN saved and wizard closed THEN new text line Content should be listed  AND  saved text showed when content opened for edit "()
    {
        given: "start to add a content with type 'TextLine 1:1'"
        Content textLineContent = buildTextLine1_1_Content();
        ContentWizardPanel contentWizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow(
            SITE_NAME ).clickToolbarNew().selectContentType( textLineContent.getContentTypeName() )
        TextLine1_1_FormViewPanel formViewPanel = new TextLine1_1_FormViewPanel( getSession() );


        when:
        contentWizardPanel.typeData( textLineContent ).save().close( textLineContent.getDisplayName() );

        then: "new content listed in the grid and can be opened for edit"
        filterPanel.typeSearchText( textLineContent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( textLineContent.getPath() ).clickToolbarEdit();
        String valueFromUI = formViewPanel.getTextLineValue();

        and: "numbers of inputs is 1"
        formViewPanel.getNumberOfTextInputs() == 1;
        and: "saved strings are present in the Content Wizard"
        valueFromUI.contains( TEST_TEXT );
    }


    def "GIVEN creating new TextLine1:1 on root WHEN required text input is empty and button 'Publish' pressed THEN validation message appears"()
    {
        given: "start to add a content with type 'TextLine 1:1'"
        Content textLineContent = buildTextLine1_1_Content();
        ContentWizardPanel contentWizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow(
            SITE_NAME ).clickToolbarNew().selectContentType( textLineContent.getContentTypeName() );

        when:
        contentWizardPanel.clickOnPublishButton();
        TextLine1_1_FormViewPanel formViewPanel = new TextLine1_1_FormViewPanel( getSession() );

        then: "new content listed in the grid and can be opened for edit"
        formViewPanel.isValidationMessagePresent();
        and:
        formViewPanel.getValidationMessage() == TextLine1_1_FormViewPanel.VALIDATION_MESSAGE;
    }

    def "GIVEN creating new TextLine1:1 on root WHEN data typed and 'Save' and  'Publish' are pressed THEN new content with status equals 'Online' listed"()
    {
        given: "start to add a content with type 'TextLine 1:1'"
        Content textLineContent = buildTextLine1_1_Content();
        ContentWizardPanel contentWizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow(
            SITE_NAME ).clickToolbarNew().selectContentType( textLineContent.getContentTypeName() )


        when:
        contentWizardPanel.typeData( textLineContent ).save().clickOnPublishButton().close( textLineContent.getDisplayName() );
        filterPanel.typeSearchText( textLineContent.getName() );

        then:
        contentBrowsePanel.getContentStatus( textLineContent.getPath() ).equals( ContentStatus.ONLINE.getValue() )
    }


    private Content buildTextLine1_1_Content()
    {
        String name = "textline1_1";

        PropertyTree data = new PropertyTree();
        data.addStrings( TextLine1_1_FormViewPanel.TEXT_INPUT_PROPERTY, TEST_TEXT );


        Content textLineContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "textline1_1 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":textline1_1" ).data( data ).
            build();
        return textLineContent;
    }

}