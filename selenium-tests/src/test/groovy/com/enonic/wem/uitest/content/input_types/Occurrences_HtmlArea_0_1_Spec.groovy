package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.HtmlArea0_1_FormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Occurrences_HtmlArea_0_1_Spec
    extends Base_InputFields_Occurrences

{
    @Shared
    String TEST_TEXT = "html area text";

    @Shared
    String EXPECTED_TEXT = "<p>" + TEST_TEXT + "</p>";


    def "WHEN wizard for adding a content with HtmlArea(0:1) opened THEN text area is present "()
    {
        when: "start to add a content with type 'HtmlArea 0:1'"
        Content htmlAreaContent = buildHtmlArea0_1_Content( TEST_TEXT );

        selectSiteOpenWizard( htmlAreaContent.getContentTypeName() );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );

        then: "wizard with form view opened"
        formViewPanel.isOpened();
        and: "text area present"
        formViewPanel.isEditorPresent();
    }

    def "WHEN wizard opened AND the editor is not in edit mode THEN HtmlArea toolbar is hidden"()
    {
        when: "start to add a content with type 'HtmlArea 0:1'"
        Content htmlAreaContent = buildHtmlArea0_1_Content( TEST_TEXT );
        selectSiteOpenWizard( htmlAreaContent.getContentTypeName() );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );

        then: "wizard with form view opened"
        !formViewPanel.isEditorToolbarVisible();
    }

    def "WHEN wizard opened AND the editor in edit mode THEN HtmlArea toolbar is visible"()
    {
        when: "start to add a content with type 'HtmlArea 0:1'"
        Content tinyMceContent = buildHtmlArea0_1_Content( TEST_TEXT );
        selectSiteOpenWizard( tinyMceContent.getContentTypeName() );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        formViewPanel.type( tinyMceContent.getData() );

        then: "wizard with form view opened"
        formViewPanel.isEditorToolbarVisible();
    }

    def "GIVEN saving of content with HtmlArea editor  (0:1) and text typed WHEN content opened for edit THEN expected string is present in the editor "()
    {
        given: "new content with type HtmlArea added'"
        Content htmlAreaContent = buildHtmlArea0_1_Content( TEST_TEXT );
        ContentWizardPanel wizard = selectSiteOpenWizard( htmlAreaContent.getContentTypeName() );
        wizard.typeData( htmlAreaContent ).save().close( htmlAreaContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( htmlAreaContent );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        String text = formViewPanel.getText();

        then: "expected text present in the editor"
        text == EXPECTED_TEXT;
    }

    def "GIVEN saving of content with HtmlArea editor (0:1) and text not typed WHEN content opened for edit THEN  no text present in the editor"()
    {
        given: "new content with type HtmlArea added'"
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        ContentWizardPanel wizard = selectSiteOpenWizard( htmlAreaContent.getContentTypeName() );
        wizard.typeData( htmlAreaContent ).save().close( htmlAreaContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( htmlAreaContent );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );

        then: "text area is empty"
        formViewPanel.isTextAreaEmpty();
    }

    private Content buildHtmlArea0_1_Content( String text )
    {
        PropertyTree data = new PropertyTree();
        if ( text != null )
        {
            data.addStrings( HtmlArea0_1_FormViewPanel.STRING_PROPERTY, text );
        }
        Content tinyMceContent = Content.builder().
            name( NameHelper.uniqueName( "html0_1_" ) ).
            displayName( "html0_1 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + ":htmlarea0_1" ).data( data ).
            build();
        return tinyMceContent;
    }
}
