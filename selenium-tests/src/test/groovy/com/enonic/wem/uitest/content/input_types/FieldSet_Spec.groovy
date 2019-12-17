package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.DoubleFormViewPanel
import com.enonic.autotests.pages.form.FieldSetFormViewPanel
import com.enonic.autotests.pages.form.HtmlArea0_1_FormViewPanel
import com.enonic.autotests.pages.form.TextLine1_1_FormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class FieldSet_Spec
    extends Base_InputFields_Occurrences

{
    @Shared
    String TEXT_LINE_TEXT = "text line text ";

    @Shared
    String HTML_AREA_TEXT = "htmlArea text ";

    @Shared
    Content FIELDSET_CONTENT;

    @Shared
    String TEST_DOUBLE = "123.4";


    def "GIVEN adding a content with a fieldset WHEN all required fields are typed THEN content should be displayed as valid"()
    {
        given: "'fieldset' content with all required values is saved"
        FIELDSET_CONTENT = build_FieldSet_Content( TEXT_LINE_TEXT, HTML_AREA_TEXT, TEST_DOUBLE, TEST_DOUBLE, TEST_DOUBLE );
        ContentWizardPanel wizard = selectSitePressNew( FIELDSET_CONTENT.getContentTypeName() );
        wizard.typeData( FIELDSET_CONTENT ).save();

        when: "wizard was not closed but navigate to the grid"
        wizard.switchToBrowsePanelTab();
        saveScreenshot( "fieldset_should_be_valid" );

        then: "red icon should not be present in the browse panel, because all required inputs is not empty"
        filterPanel.typeSearchText( FIELDSET_CONTENT.getName() );
        !contentBrowsePanel.isContentInvalid( FIELDSET_CONTENT.getName() );
    }

    def "GIVEN wizard for 'fieldset' is opened WHEN required textline is empty THEN validation message should be displayed under the textline"()
    {
        given: "wizard for 'fieldset' is opened"
        FIELDSET_CONTENT = build_FieldSet_Content( "", HTML_AREA_TEXT, TEST_DOUBLE, TEST_DOUBLE );
        ContentWizardPanel wizard = selectSitePressNew( FIELDSET_CONTENT.getContentTypeName() );

        when: "data was typed but required text-line is empty"
        wizard.typeData( FIELDSET_CONTENT ).save();
        saveScreenshot( "fieldset_textline_empty" );
        TextLine1_1_FormViewPanel textLine1_1_formViewPanel = new TextLine1_1_FormViewPanel( getSession() );

        then: "validation message should be displayed under the text-line"
        textLine1_1_formViewPanel.isValidationMessagePresent();

        and: "correct message should be displayed"
        textLine1_1_formViewPanel.getValidationMessage() == Application.REQUIRED_MESSAGE
    }

    def "GIVEN wizard for 'fieldset' is opened WHEN required text in the HTML-area is empty THEN content is not valid in the wizard"()
    {
        given: "wizard for 'fieldset' is opened"
        FIELDSET_CONTENT = build_FieldSet_Content( TEXT_LINE_TEXT, "", TEST_DOUBLE, TEST_DOUBLE );
        ContentWizardPanel wizard = selectSitePressNew( FIELDSET_CONTENT.getContentTypeName() );

        when: "data was typed but the html-area is empty"
        wizard.typeData( FIELDSET_CONTENT ).save();
        saveScreenshot( "fieldset_hmlarea_empty" );
        HtmlArea0_1_FormViewPanel htmlArea0_1_formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );

        then: "correct message should be displayed"
        htmlArea0_1_formViewPanel.getValidationMessage() == Application.REQUIRED_MESSAGE
    }

    def "GIVEN adding a content with type fieldset WHEN one required double is empty THEN content is not valid in the wizard"()
    {
        given: "start to add a content with a fieldset "
        FIELDSET_CONTENT = build_FieldSet_Content( TEXT_LINE_TEXT, HTML_AREA_TEXT, "", TEST_DOUBLE );
        ContentWizardPanel wizard = selectSitePressNew( FIELDSET_CONTENT.getContentTypeName() );

        when: "data was typed but required double value is empty"
        wizard.typeData( FIELDSET_CONTENT ).save();
        DoubleFormViewPanel doubleFormViewPanel = new DoubleFormViewPanel( getSession() );
        saveScreenshot( "fieldset_double_is_empty" );

        then: "correct message should be displayed"
        doubleFormViewPanel.getValidationMessage() == "Min 2 occurrences required";
    }

    protected Content build_FieldSet_Content( String textLine, String areaText, String... values )
    {
        String name = "fieldset";
        PropertyTree data = new PropertyTree();
        data.addString( FieldSetFormViewPanel.TEXT_LINE_VALUE, textLine );
        data.addString( FieldSetFormViewPanel.HTML_AREA_VALUE, areaText );
        data.addStrings( FieldSetFormViewPanel.DOUBLES_VALUE, values );

        Content textLineContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "fieldset content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "fieldset" ).data( data ).
            build();
        return textLineContent;
    }


}