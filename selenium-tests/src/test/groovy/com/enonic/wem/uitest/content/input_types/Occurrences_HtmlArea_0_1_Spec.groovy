package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.InsertLinkModalDialog
import com.enonic.autotests.pages.form.BaseHtmlAreaFormViewPanel
import com.enonic.autotests.pages.form.HtmlArea0_1_FormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Occurrences_HtmlArea_0_1_Spec
    extends Base_InputFields_Occurrences

{
    @Shared
    String TEST_TEXT = "html area text";

    @Shared
    String EXPECTED_INNER_HTML = "<p>" + TEST_TEXT + "</p>";

    @Shared
    String NORWEGIAN_TEXT = "Hej og hå så kan det gå"

    def "GIVEN creating of content with html-area WHEN link with norwegian text typed THEN correct string is present in the text area "()
    {

        given: "creating of content with html-area"
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( htmlAreaContent.getContentTypeName() );
        wizard.typeData( htmlAreaContent );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        InsertLinkModalDialog modalDialog = formViewPanel.showToolbarAndClickOnInsertLinkButton();
        modalDialog.clickURLBarItem().typeText( NORWEGIAN_TEXT ).typeURL( "http://enonic.com" ).pressInsertButton();
        wizard.save().close( htmlAreaContent.getDisplayName() );


        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( htmlAreaContent );
        String text = formViewPanel.getInnerHtml();

        then: "expected text present in the editor"
        text.contains( NORWEGIAN_TEXT );
    }

    def "WHEN wizard for adding a content with HtmlArea(0:1) opened THEN text area is present"()
    {
        when: "start to add a content with type 'HtmlArea 0:1'"
        Content htmlAreaContent = buildHtmlArea0_1_Content( TEST_TEXT );

        selectSitePressNew( htmlAreaContent.getContentTypeName() );
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
        selectSitePressNew( htmlAreaContent.getContentTypeName() );
        HtmlArea0_1_FormViewPanel htmlAreaFormViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );

        then: "HtmlArea-toolbar is hidden"
        !htmlAreaFormViewPanel.isEditorToolbarVisible();
    }

    def "WHEN wizard opened AND the editor in edit mode THEN HtmlArea toolbar is visible"()
    {
        when: "start to add a content with type 'HtmlArea 0:1'"
        Content tinyMceContent = buildHtmlArea0_1_Content( TEST_TEXT );
        selectSitePressNew( tinyMceContent.getContentTypeName() );
        HtmlArea0_1_FormViewPanel htmlAreaFormViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        htmlAreaFormViewPanel.type( tinyMceContent.getData() );

        then: "HtmlArea-toolbar is visible"
        htmlAreaFormViewPanel.isEditorToolbarVisible();
    }

    def "GIVEN saving of content with HtmlArea editor (0:1) and text typed WHEN content opened for edit THEN expected string is present in the editor "()
    {
        given: "new content with type HtmlArea added'"
        Content htmlAreaContent = buildHtmlArea0_1_Content( TEST_TEXT );
        ContentWizardPanel wizard = selectSitePressNew( htmlAreaContent.getContentTypeName() );
        wizard.typeData( htmlAreaContent ).save().close( htmlAreaContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( htmlAreaContent );
        HtmlArea0_1_FormViewPanel htmlAreaFormViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        String text = htmlAreaFormViewPanel.getInnerHtml();

        then: "expected text present in the editor"
        text == EXPECTED_INNER_HTML;
    }

    def "GIVEN saving of content with HtmlArea editor (0:1) and text not typed WHEN content opened for edit THEN no text present in the editor"()
    {
        given: "new content with type HtmlArea added'"
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( htmlAreaContent.getContentTypeName() );
        wizard.typeData( htmlAreaContent ).save().close( htmlAreaContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( htmlAreaContent );
        HtmlArea0_1_FormViewPanel htmlAreaFormViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );

        then: "text area is empty"
        htmlAreaFormViewPanel.getInnerHtml() == BaseHtmlAreaFormViewPanel.EMPTY_TEXT_AREA_CONTENT;
    }
}
