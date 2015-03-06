package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.form.TextLine1_1_FormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree

class Occurences_TextLine_1_1_Spec
    extends Base_InputFields_Occurences

{

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

    private Content buildTextLine1_1_Content()
    {
        String name = "textline1_1";

        PropertyTree data = new PropertyTree();
        data.addStrings( TextLine1_1_FormViewPanel.TEXT_INPUT_PROPERTY, "test text" );


        Content textLineContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "textline1_1 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":textline1_1" ).data( data ).
            build();
        return textLineContent;
    }

}