package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.InsertLinkModalDialog
import com.enonic.autotests.pages.form.BaseHtmlAreaFormViewPanel
import com.enonic.autotests.pages.form.HtmlArea0_1_FormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore
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

    def "GIVEN content with html-area is saved AND a link inserted in the area WHEN the content has been reopened THEN expected link should be present in the html-area"()
    {
        given: "html-area content has been added"
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( htmlAreaContent.getContentTypeName() );
        wizard.typeData( htmlAreaContent );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        and: "Link with norwegian text has been added"
        InsertLinkModalDialog modalDialog = formViewPanel.showToolbarAndClickOnInsertLinkButton();
        modalDialog.clickURLBarItem().typeText( NORWEGIAN_TEXT ).typeURL( "http://enonic.com" ).pressInsertButton().waitForDialogClosed();
        and: "the wizard has been closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        when: "the content has been reopened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( htmlAreaContent );
        List<String> strings = formViewPanel.getDataFromCKEAreas();

        then: "expected link should be present in the htmlArea"
        strings.get( 0 ).contains( NORWEGIAN_TEXT );
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

    def "GIVEN HtmlArea content(0:1) is saved with a text WHEN content has been reopened THEN expected html code should be present in the editor"()
    {
        given: "HtmlArea content(0:1) is saved with a text"
        Content htmlAreaContent = buildHtmlArea0_1_Content( TEST_TEXT );
        ContentWizardPanel wizard = selectSitePressNew( htmlAreaContent.getContentTypeName() );
        and: "text has been typed"
        wizard.typeData( htmlAreaContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content has been opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( htmlAreaContent );
        HtmlArea0_1_FormViewPanel htmlAreaFormViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        List<String> strings = htmlAreaFormViewPanel.getDataFromCKEAreas();

        then: "expected html code should be present in the htmlArea"
        strings.get( 0 ) == EXPECTED_INNER_HTML;
    }

    def "GIVEN content with empty htmlArea is saved WHEN content has been reopened THEN text area should be empty"()
    {
        given: "HtmlArea content(0:1) is saved without a text"
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( htmlAreaContent.getContentTypeName() );
        and: "the content saved, and html-area is empty"
        wizard.typeData( htmlAreaContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "the content has been opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( htmlAreaContent );
        sleep(500);
        HtmlArea0_1_FormViewPanel htmlAreaFormViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        List<String> strings = htmlAreaFormViewPanel.getDataFromCKEAreas();

        then: "text area should be empty"
        strings.get( 0 ) == "";
    }
    // verifies the XP-4698
    def "GIVEN 'Insert Link' dialog is opened WHEN required 'text' field is not filled in AND 'Insert' button pressed THEN validation message should appear in the dialog"()
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

        then: "validation message should appear"
        insertLinkModalDialog.isValidationMessageForTextInputDisplayed();

        and: "correct message is displayed"
        insertLinkModalDialog.getValidationMessageForTextInput() == InsertLinkModalDialog.VALIDATION_MESSAGE;
    }
    // verifies XP-4949 HTML Area - Modal dialogs must handle close on Esc
    def "GIVEN InsertLinkModalDialog is opened WHEN 'Escape' key has been pressed THEN modal dialog should close"()
    {
        given: "wizard for html-area content is opened"
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        selectSitePressNew( htmlAreaContent.getContentTypeName() );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        and: "InsertLinkModalDialog is opened"
        InsertLinkModalDialog insertLinkModalDialog = formViewPanel.showToolbarAndClickOnInsertLinkButton();
        insertLinkModalDialog.waitForOpened(  );

        when: "'Escape' key has been clicked"
        insertLinkModalDialog.pressEscapeKey();

        then: "modal dialog should not be displayed"
        insertLinkModalDialog.waitForDialogClosed();
    }

    def "GIVEN 'Insert Link' modal dialog is opened WHEN required 'URL' field is empty AND 'Insert' button pressed THEN validation message should appear in the dialog"()
    {
        given: "'Insert Link' modal dialog is opened "
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( htmlAreaContent.getContentTypeName() );
        wizard.typeData( htmlAreaContent );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        InsertLinkModalDialog modalDialog = formViewPanel.showToolbarAndClickOnInsertLinkButton();

        when: "required 'URL' field is empty"
        modalDialog.clickURLBarItem().typeText( "test" ).typeURL( "" ).pressInsertButton();
        saveScreenshot( "validation_msg_url" );

        then: "validation message should be displayed"
        modalDialog.isValidationMessageForUrlInputDisplayed();
    }
}
