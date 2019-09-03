package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.InsertAnchorModalDialog
import com.enonic.autotests.pages.form.HtmlArea0_1_FormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore

/**
 * Created  on 3/1/2017.
 *
 * verifies XP-4949 HTML Area - Modal dialogs must handle close on Esc
 * */
class HtmlArea_InsertAnchorDialog_Spec
    extends Base_InputFields_Occurrences
{
    def "GIVEN wizard for html-area content is opened WHEN 'Insert Image' button has been clicked THEN modal dialog should be displayed"()
    {
        given: "wizard for html-area content is opened"
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        selectSitePressNew( htmlAreaContent.getContentTypeName() );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );

        when: "'Insert Anchor' button has been clicked"
        InsertAnchorModalDialog insertAnchorDialog = formViewPanel.showToolbarAndClickOnInsertAnchorButton();
        saveScreenshot( "anchor_dialog_opened" );

        then: " modal dialog should be displayed"
        insertAnchorDialog.getHeader() == InsertAnchorModalDialog.HEADER_TEXT;

        and: "'Cancel' button should be displayed"
        insertAnchorDialog.isCancelButtonDisplayed();

        and: "'Insert' button should be displayed"
        insertAnchorDialog.isInsertButtonDisplayed();

        and: "'Anchor' text input should be displayed"
        insertAnchorDialog.isAnchorInputDisplayed();
    }

    def "GIVEN InsertAnchorModalDialog is opened WHEN 'Cancel' button has been clicked THEN modal dialog should not be displayed"()
    {
        given: "InsertImageModalDialog is opened"
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        selectSitePressNew( htmlAreaContent.getContentTypeName() );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        and: "InsertAnchorModalDialog is opened"
        InsertAnchorModalDialog insertAnchorButton = formViewPanel.showToolbarAndClickOnInsertAnchorButton();

        when: "'Cancel' button has been clicked"
        insertAnchorButton.clickOnCancelButton();

        then: "modal dialog should not be displayed"
        insertAnchorButton.waitForClosed();
    }
    // verifies XP-4949 HTML Area - Modal dialogs must handle close on Esc (Test is reimplemented in JS)
    @Ignore
    def "GIVEN  InsertAnchorModalDialog is opened WHEN 'Escape' key has been pressed THEN modal dialog should not be displayed"()
    {
        given: "wizard for html-area content is opened"
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        selectSitePressNew( htmlAreaContent.getContentTypeName() );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        and: "InsertAnchorModalDialog is opened"
        InsertAnchorModalDialog insertAnchorModalDialog = formViewPanel.showToolbarAndClickOnInsertAnchorButton();

        when: "'Escape' key has been clicked"
        insertAnchorModalDialog.pressEscapeKey();

        then: "modal dialog should not be displayed"
        insertAnchorModalDialog.waitForClosed();
    }

    def "GIVEN  InsertAnchorModalDialog is opened WHEN text has been typed AND 'Insert' button pressed THEN modal dialog should not be displayed"()
    {
        given: "wizard for html-area content is opened"
        Content htmlAreaContent = buildHtmlArea0_1_Content( null );
        selectSitePressNew( htmlAreaContent.getContentTypeName() );
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        and: "InsertAnchorModalDialog is opened"
        InsertAnchorModalDialog insertAnchorModalDialog = formViewPanel.showToolbarAndClickOnInsertAnchorButton();

        when: "'Escape' key has been clicked"
        insertAnchorModalDialog.typeText( "test_anchor" );
        saveScreenshot( "anchor_text_typed" );
        and: "'Insert' button has been pressed"
        insertAnchorModalDialog.clickOnInsertButton();

        then: "modal dialog should not be displayed"
        insertAnchorModalDialog.waitForClosed();
    }
}
