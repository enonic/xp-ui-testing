package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.TinyMCE0_1_FormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Occurrences_TinyMCE_0_1_Spec
    extends Base_InputFields_Occurrences

{
    @Shared
    String TEST_TEXT = "tinyMce text";

    @Shared
    String EXPECTED_TEXT = "<p>" + TEST_TEXT + "</p>";


    def "WHEN wizard for adding a content with TinyMCE(0:1) opened THEN text area is present "()
    {
        when: "start to add a content with type 'TinyMCE 0:1'"
        Content tinyMceContent = buildTinyMce0_1_Content( TEST_TEXT );

        selectSiteOpenWizard( tinyMceContent.getContentTypeName() );
        TinyMCE0_1_FormViewPanel formViewPanel = new TinyMCE0_1_FormViewPanel( getSession() );

        then: "wizard with form view opened"
        formViewPanel.isOpened();
        and: "text area present"
        formViewPanel.isEditorPresent();
    }

    def "WHEN wizard opened AND the editor is not in edit mode THEN TinyMCE toolbar is hidden"()
    {
        when: "start to add a content with type 'TinyMCE 0:1'"
        Content tinyMceContent = buildTinyMce0_1_Content( TEST_TEXT );
        selectSiteOpenWizard( tinyMceContent.getContentTypeName() );
        TinyMCE0_1_FormViewPanel formViewPanel = new TinyMCE0_1_FormViewPanel( getSession() );

        then: "wizard with form view opened"
        !formViewPanel.isEditorToolbarVisible();
    }

    def "WHEN wizard opened AND the editor  in edit mode THEN TinyMCE toolbar is visible"()
    {
        when: "start to add a content with type 'TinyMCE 0:1'"
        Content tinyMceContent = buildTinyMce0_1_Content( TEST_TEXT );
        selectSiteOpenWizard( tinyMceContent.getContentTypeName() );
        TinyMCE0_1_FormViewPanel formViewPanel = new TinyMCE0_1_FormViewPanel( getSession() );
        formViewPanel.type( tinyMceContent.getData() );

        then: "wizard with form view opened"
        formViewPanel.isEditorToolbarVisible();
    }

    def "GIVEN saving of content with TinyMCE editor  (0:1) and text typed WHEN content opened for edit THEN expected string is present in the editor "()
    {
        given: "new content with type TinyMCE added'"
        Content tinyMceContent = buildTinyMce0_1_Content( TEST_TEXT );
        ContentWizardPanel wizard = selectSiteOpenWizard( tinyMceContent.getContentTypeName() );
        wizard.typeData( tinyMceContent ).save().close( tinyMceContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tinyMceContent );
        TinyMCE0_1_FormViewPanel formViewPanel = new TinyMCE0_1_FormViewPanel( getSession() );
        String text = formViewPanel.getText();

        then: "expected text present in the editor"
        text == EXPECTED_TEXT;
    }

    def "GIVEN saving of content with TinyMCE editor (0:1) and text not typed WHEN content opened for edit THEN  no text present in the editor"()
    {
        given: "new content with type TinyMCE added'"
        Content tinyMceContent = buildTinyMce0_1_Content( null );
        ContentWizardPanel wizard = selectSiteOpenWizard( tinyMceContent.getContentTypeName() );
        wizard.typeData( tinyMceContent ).save().close( tinyMceContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tinyMceContent );
        TinyMCE0_1_FormViewPanel formViewPanel = new TinyMCE0_1_FormViewPanel( getSession() );

        then: "text area is empty"
        formViewPanel.isTextAreaEmpty();
    }

    private Content buildTinyMce0_1_Content( String text )
    {
        PropertyTree data = new PropertyTree();
        if ( text != null )
        {
            data.addStrings( TinyMCE0_1_FormViewPanel.STRING_PROPERTY, text );
        }
        Content tinyMceContent = Content.builder().
            name( NameHelper.uniqueName( "mce0_1_" ) ).
            displayName( "mce0_1 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":tiny_mce0_1" ).data( data ).
            build();
        return tinyMceContent;
    }
}
