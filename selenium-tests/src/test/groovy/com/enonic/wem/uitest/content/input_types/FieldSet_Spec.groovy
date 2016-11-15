package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.FieldSetFormViewPanel
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


    def "GIVEN adding a content with a fieldset WHEN all required fields are filled THEN content is valid in the wizard AND content is valid in the grid"()
    {
        given: "start to add a content with a 'fieldset'"
        FIELDSET_CONTENT = build_FieldSet_Content( TEXT_LINE_TEXT, HTML_AREA_TEXT, TEST_DOUBLE, TEST_DOUBLE, TEST_DOUBLE );
        ContentWizardPanel wizard = selectSitePressNew( FIELDSET_CONTENT.getContentTypeName() );

        when: "data typed"
        wizard.typeData( FIELDSET_CONTENT ).save();
        saveScreenshot( "test_fieldset_valid" );

        then: "red icon not present in the wizard-tab"
        !wizard.isContentInvalid( FIELDSET_CONTENT.getDisplayName() );
    }

    def "WHEN existing field set content WHEN all required inputs are filled THEN content displayed as valid in the grid"()
    {
        when: "existing field set content AND all required inputs are filled "
        findAndSelectContent( FIELDSET_CONTENT.getName() );

        then: "content displayed as valid in the grid"
        !contentBrowsePanel.isContentInvalid( FIELDSET_CONTENT.getName() );
    }

    def "GIVEN adding a content with type fieldset WHEN required text in the textline is empty THEN content is not valid in the wizard"()
    {
        given: "start to add a content with a fieldset"
        FIELDSET_CONTENT = build_FieldSet_Content( "", HTML_AREA_TEXT, TEST_DOUBLE, TEST_DOUBLE );
        ContentWizardPanel wizard = selectSitePressNew( FIELDSET_CONTENT.getContentTypeName() );

        when: "data typed"
        wizard.typeData( FIELDSET_CONTENT ).save();
        saveScreenshot( "test_fieldset1" );

        then: "red icon not present in the wizard-tab"
        wizard.isContentInvalid( FIELDSET_CONTENT.getDisplayName() );
    }

    def "GIVEN adding a content with type fieldset WHEN required text in the HTML-area is empty THEN content is not valid in the wizard"()
    {
        given: "start to add a content with a fieldset"
        FIELDSET_CONTENT = build_FieldSet_Content( TEXT_LINE_TEXT, "", TEST_DOUBLE, TEST_DOUBLE );
        ContentWizardPanel wizard = selectSitePressNew( FIELDSET_CONTENT.getContentTypeName() );

        when: "data typed"
        wizard.typeData( FIELDSET_CONTENT ).save();
        saveScreenshot( "test_fieldset2" );

        then: "red icon not present in the wizard-tab"
        wizard.isContentInvalid( FIELDSET_CONTENT.getDisplayName() );
    }

    def "GIVEN adding a content with type fieldset WHEN one required double is empty THEN content is not valid in the wizard"()
    {
        given: "start to add a content with a fieldset "
        FIELDSET_CONTENT = build_FieldSet_Content( TEXT_LINE_TEXT, HTML_AREA_TEXT, "", TEST_DOUBLE );
        ContentWizardPanel wizard = selectSitePressNew( FIELDSET_CONTENT.getContentTypeName() );

        when: "data typed"
        wizard.typeData( FIELDSET_CONTENT ).save();
        saveScreenshot( "test_fieldset3" );

        then: "red icon not present in the wizard-tab"
        wizard.isContentInvalid( FIELDSET_CONTENT.getDisplayName() );
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
            contentType( ALL_CONTENT_TYPES_APP_NAME + ":fieldset" ).data( data ).
            build();
        return textLineContent;
    }


}