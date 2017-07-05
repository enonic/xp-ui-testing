package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.InsertLinkModalDialog
import com.enonic.autotests.pages.form.BaseHtmlAreaFormViewPanel
import com.enonic.autotests.pages.form.HtmlArea0_1_FormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

/*
Tasks:
 XP-4746 Add selenium test to verify XP-4698
 xp-ui-testing#4 Check fixed application's bugs and add Selenium tests for each fixed bugs
 */

@Stepwise
class Occurrences_HtmlArea_0_1_Spec
    extends Base_InputFields_Occurrences

{
    @Shared
    String TEST_TEXT = "html area text";

    @Shared
    String DEFAULT_EXPECTED_INNER_HTML = "<p>" + TEST_TEXT + "</p>";

    @Shared
    String NORWEGIAN_TEXT = "Hej og hå så kan det gå"

    def "GIVEN content with html-area was saved AND 'Link' inserted in the area WHEN the content is opened THEN correct link should be present in the html-area"()
    {
        given: "html-area content has been added"
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( htmlAreaContent.getContentTypeName() );
        wizard.typeData( htmlAreaContent );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        and: "Link with norwgian text has been added"
        InsertLinkModalDialog modalDialog = formViewPanel.showToolbarAndClickOnInsertLinkButton();
        modalDialog.clickURLBarItem().typeText( NORWEGIAN_TEXT ).typeURL( "http://enonic.com" ).pressInsertButton().waitForDialogClosed();
        and: "the wizard has been closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        when: "the content is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( htmlAreaContent );
        String text = formViewPanel.getInnerHtml();

        then: "expected link should be present in the editor"
        text.contains( NORWEGIAN_TEXT );
    }

    def "WHEN wizard for HtmlArea(0:1) is opened THEN text area should be present"()
    {
        when: "wizard for HtmlArea(0:1) is opened"
        Content htmlAreaContent = buildHtmlArea0_1_Content( TEST_TEXT );
        selectSitePressNew( htmlAreaContent.getContentTypeName() );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );

        then: "one html-area should be present"
        formViewPanel.getNumberOfAreas() == 1;

        and: "HtmlArea-toolbar should be hidden"
        !formViewPanel.isEditorToolbarVisible();
    }

    def "GIVEN wizard for HtmlArea(0:1) is opened WHEN the editor in edit mode THEN HtmlArea-toolbar should be displayed"()
    {
        given: "start to add a content with type 'HtmlArea 0:1'"
        Content tinyMceContent = buildHtmlArea0_1_Content( TEST_TEXT );
        selectSitePressNew( tinyMceContent.getContentTypeName() );

        when: "the editor in edit mode"
        HtmlArea0_1_FormViewPanel htmlAreaFormViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        htmlAreaFormViewPanel.type( tinyMceContent.getData() );

        then: "HtmlArea-toolbar should be displayed"
        htmlAreaFormViewPanel.isEditorToolbarVisible();
    }

    def "GIVEN existing content with HtmlArea editor (0:1) is opened AND text was typed WHEN content opened again THEN expected text should be present in the editor"()
    {
        given: "existing content with HtmlArea editor (0:1) is opened"
        Content htmlAreaContent = buildHtmlArea0_1_Content( TEST_TEXT );
        ContentWizardPanel wizard = selectSitePressNew( htmlAreaContent.getContentTypeName() );
        and: "text has been typed"
        wizard.typeData( htmlAreaContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content is opened again"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( htmlAreaContent );
        HtmlArea0_1_FormViewPanel htmlAreaFormViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        String text = htmlAreaFormViewPanel.getInnerHtml();

        then: "expected text should be present in the editor"
        text == DEFAULT_EXPECTED_INNER_HTML;
    }

    def "GIVEN wizard for content with HtmlArea editor (0:1) is opened and html-area is empty WHEN content opened for edit THEN text area should be empty"()
    {
        given: "wizard for content with HtmlArea editor (0:1) is opened"
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( htmlAreaContent.getContentTypeName() );
        and: "the content saved, and html-area is empty"
        wizard.typeData( htmlAreaContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "the content is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( htmlAreaContent );
        HtmlArea0_1_FormViewPanel htmlAreaFormViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );

        then: "text area should be empty"
        htmlAreaFormViewPanel.getInnerHtml() == BaseHtmlAreaFormViewPanel.DEFAULT_EMPTY_TEXT_AREA_CONTENT;
    }
    // verifies the XP-4698
    def "GIVEN 'Insert Link' modal dialog is opened WHEN the URL has been typed but the required 'text' field is empty AND 'Insert' button pressed THEN correct validation message appears on the dialog"()
    {
        given: "'Insert Link' modal dialog is opened "
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( htmlAreaContent.getContentTypeName() );
        wizard.typeData( htmlAreaContent );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        InsertLinkModalDialog insertLinkModalDialog = formViewPanel.showToolbarAndClickOnInsertLinkButton();

        when: "the URL has been typed but the required 'text' field is empty AND 'Insert' button pressed"
        insertLinkModalDialog.clickURLBarItem().typeText( "" ).typeURL( "http://enonic.com" ).pressInsertButton();
        saveScreenshot( "validation_msg_text" );

        then: "validation message for the input should appear"
        insertLinkModalDialog.isValidationMessageForTextInputDisplayed();

        and: "correct message is displayed"
        insertLinkModalDialog.getValidationMessageForTextInput() == InsertLinkModalDialog.VALIDATION_MESSAGE;
    }
    // verifies XP-4949 HTML Area - Modal dialogs must handle close on Esc
    def "GIVEN InsertLinkModalDialog is opened WHEN 'Escape' key has been pressed THEN modal dialog should not be displayed"()
    {
        given: "wizard for html-area content is opened"
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        selectSitePressNew( htmlAreaContent.getContentTypeName() );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        and: "InsertLinkModalDialog is opened"
        InsertLinkModalDialog insertLinkModalDialog = formViewPanel.showToolbarAndClickOnInsertLinkButton();

        when: "'Escape' key has been clicked"
        insertLinkModalDialog.pressEscapeKey();

        then: "modal dialog should not be displayed"
        insertLinkModalDialog.waitForDialogClosed();
    }
    // verifies the XP-4698
    def "GIVEN 'Insert Link' modal dialog is opened WHEN the Text has been typed but the required 'URL' field is empty AND 'Insert' button pressed THEN correct validation message appears on the dialog "()
    {
        given: "'Insert Link' modal dialog is opened "
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( htmlAreaContent.getContentTypeName() );
        wizard.typeData( htmlAreaContent );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        InsertLinkModalDialog modalDialog = formViewPanel.showToolbarAndClickOnInsertLinkButton();

        when: "the URL has been typed but the required 'text' field has not AND 'Insert' button has been pressed"
        modalDialog.clickURLBarItem().typeText( "test" ).typeURL( "" ).pressInsertButton();
        saveScreenshot( "validation_msg_url" );

        then: "validation message for the input should be displayed"
        modalDialog.isValidationMessageForUrlInputDisplayed();
    }
}
