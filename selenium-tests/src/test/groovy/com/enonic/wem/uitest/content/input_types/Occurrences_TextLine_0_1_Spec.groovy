package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.TextLine0_1_FormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared

class Occurrences_TextLine_0_1_Spec
    extends Base_InputFields_Occurrences

{
    @Shared
    String TEST_TEXT = "test text 0:1";


    def "WHEN wizard for adding a TextLine-content (0:1) opened THEN one text input present "()
    {
        when: "start to add a content with type 'TextLine 0:1'"
        Content textLineContent = buildTextLine0_1_Content();
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            textLineContent.getContentTypeName() )
        TextLine0_1_FormViewPanel formViewPanel = new TextLine0_1_FormViewPanel( getSession() );

        then: "one text input should be displayed in the form view"
        formViewPanel.getNumberOfTextInputs() == 1;
    }

    def "WHEN wizard for adding a TextLine-content (0:1) opened THEN button 'Remove' not present on page "()
    {
        when: "start to add a content with type 'TextLine 0:1'"
        Content textLineContent = buildTextLine0_1_Content();
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            textLineContent.getContentTypeName() )
        TextLine0_1_FormViewPanel formViewPanel = new TextLine0_1_FormViewPanel( getSession() );

        then: "one text input should be displayed in the form view"
        formViewPanel.getNumberOfDisplayedRemoveButtons() == 0;
    }

    def "WHEN wizard for adding a TextLine-content (0:1) opened THEN button 'Add' not present on page "()
    {
        when: "start to add a content with type 'TextLine 0:1'"
        Content textLineContent = buildTextLine0_1_Content();
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            textLineContent.getContentTypeName() )
        TextLine0_1_FormViewPanel formViewPanel = new TextLine0_1_FormViewPanel( getSession() );

        then: "one text input should be displayed in the form view"
        !formViewPanel.isAddButtonPresent();
    }

    def "GIVEN creating new Content on root WHEN saved and wizard closed THEN new text line Content should be listed AND saved text showed when content opened for edit"()
    {
        given: "start to add a content with type 'TextLine 0:1'"
        Content textLineContent = buildTextLine0_1_Content();
        ContentWizardPanel contentWizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow(
            SITE_NAME ).clickToolbarNew().selectContentType( textLineContent.getContentTypeName() )
        TextLine0_1_FormViewPanel formViewPanel = new TextLine0_1_FormViewPanel( getSession() );


        when:
        contentWizardPanel.typeData( textLineContent ).save().close( textLineContent.getDisplayName() );
        filterPanel.typeSearchText( textLineContent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( textLineContent.getPath() ).clickToolbarEdit();

        then: "actual text in the text line should be equals as expected"
        String valueFromUI = formViewPanel.getTextLineValue();
        valueFromUI.equals( TEST_TEXT );
    }

    def "GIVEN creating new TextLine0:1 on root WHEN data typed and 'Save' and  'Publish' are pressed THEN new content with status equals 'Online' listed"()
    {
        given: "start to add a content with type 'TextLine 0:1'"
        Content textLineContent = buildTextLine0_1_Content();
        ContentWizardPanel contentWizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow(
            SITE_NAME ).clickToolbarNew().selectContentType( textLineContent.getContentTypeName() )


        when: "type a data and 'save' and 'publish'"
        contentWizardPanel.typeData( textLineContent ).save().clickOnPublishButton().close( textLineContent.getDisplayName() );
        filterPanel.typeSearchText( textLineContent.getName() );

        then: "content has a 'online' status"
        contentBrowsePanel.getContentStatus( textLineContent.getPath() ).equals( ContentStatus.ONLINE.getValue() )
    }

    private Content buildTextLine0_1_Content()
    {
        String name = "textline0_1";

        PropertyTree data = new PropertyTree();
        data.addString( TextLine0_1_FormViewPanel.TEXT_INPUT_PROPERTY, TEST_TEXT );


        Content textLineContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "textline0_1 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":textline0_1" ).data( data ).
            build();
        return textLineContent;
    }
}