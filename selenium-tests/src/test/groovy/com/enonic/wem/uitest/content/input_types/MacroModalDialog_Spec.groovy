package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.MacroModalDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.MacroType
import com.enonic.autotests.pages.form.HtmlArea0_1_FormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class MacroModalDialog_Spec
    extends Base_InputFields_Occurrences

{
    @Shared
    Content HTML_AREA_CONTENT;

    def "GIVEN existing content with html-area is opened WHEN 'insert macro' button on toolbar has been pressed THEN 'Macro'-dialog appears with correct control elements"()
    {
        given: "existing content with html-area is opened"
        HTML_AREA_CONTENT = buildHtmlArea0_1_Content( null );
        and: "MacroDialog opened"
        selectSitePressNew( HTML_AREA_CONTENT.getContentTypeName() ).typeData( HTML_AREA_CONTENT ).save();
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );

        when: " 'insert macro' button on toolbar has been pressed"
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();
        saveScreenshot( "test_macro_dialog_shown" );

        then: "macro dialog has been opened"
        dialog.isOpened();

        and: "'Cancel' button displayed"
        dialog.isCancelButtonDisplayed();

        and: "option filter is displayed"
        dialog.isOptionFilterDisplayed();

        and: "insert-button is displayed"
        dialog.isInsertButtonDisplayed();

        and: "correct header is shown"
        dialog.getHeader() == MacroModalDialog.DIALOG_HEADER_TEXT;
    }

    def "GIVEN MacroModalDialog opened WHEN 'embedded iframe' macro selected THEN correct macro is displayed on the dialog"()
    {
        given: "existing content with html-area is opened"
        findAndSelectContent( HTML_AREA_CONTENT.getName() ).clickToolbarEdit();
        and: "MacroDialog opened"
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();

        when: "'embedded iframe'-macro selected from the options"
        dialog.selectOption( MacroType.EMBED_IFRAME );
        saveScreenshot( "test_embedded_iframe_macro" );

        then: "correct macro is displayed on the dialog"
        dialog.getSelectedMacroDisplayName() == MacroType.EMBED_IFRAME.getValue();

        and: "'configuration' tab link appears"
        dialog.isConfigurationTabLinkPresent();

        and: "'preview' tab link appears"
        dialog.isPreviewTabLinkPresent();

        and: "remove button appears on the dialog"
        dialog.isRemoveMacroButtonPresent();
    }

    def "GIVEN MacroModalDialog opened WHEN 'disable macro' selected THEN correct macro is displayed on the dialog"()
    {
        given: "existing content with html-area is opened"
        findAndSelectContent( HTML_AREA_CONTENT.getName() ).clickToolbarEdit();
        and: "MacroDialog opened"
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();

        when: "'Disable macros' selected from the options"
        dialog.selectOption( MacroType.DISABLE_MACROS );
        saveScreenshot( "test_disable_macro" );

        then: "correct macro is displayed on the dialog"
        dialog.getSelectedMacroDisplayName() == MacroType.DISABLE_MACROS.getValue();

        and: "'configuration' tab link displayed"
        dialog.isConfigurationTabLinkPresent();

        and: "'preview' tab link displayed"
        dialog.isPreviewTabLinkPresent();

        and: "remove button appears on the dialog"
        dialog.isRemoveMacroButtonPresent();
    }
    //  'embedded code' moved to the separated app
    @Ignore
    def "GIVEN MacroModalDialog opened WHEN 'embedded code' macro selected THEN correct macro is displayed on the dialog"()
    {
        given: "existing content with html-area is opened"
        findAndSelectContent( HTML_AREA_CONTENT.getName() ).clickToolbarEdit();
        and: "MacroDialog opened"
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();

        when: "'embedded code'-macro selected from the options"
        dialog.selectOption( MacroType.EMBEDDED_CODE );
        saveScreenshot( "test_embedded_macro" );

        then: "correct macro is displayed on the dialog"
        dialog.getSelectedMacroDisplayName() == MacroType.EMBEDDED_CODE.getValue();

        and: "'configuration' tab link appears"
        dialog.isConfigurationTabLinkPresent();

        and: "'preview' tab link appears"
        dialog.isPreviewTabLinkPresent();

        and: "remove button appears on the dialog"
        dialog.isRemoveMacroButtonPresent()
    }
    // 'no format' moved to the separated app
    @Ignore
    def "GIVEN MacroModalDialog opened WHEN 'No Format macro' macro selected THEN correct macro is displayed on the dialog"()
    {
        given: "existing content with html-area is opened"
        findAndSelectContent( HTML_AREA_CONTENT.getName() ).clickToolbarEdit();
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        and: "MacroDialog opened"
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();

        when: "'no format'-macro selected from the options"
        dialog.selectOption( MacroType.NO_FORMAT );
        saveScreenshot( "test_no_format_macro" );

        then: "correct macro is displayed on the dialog"
        dialog.getSelectedMacroDisplayName() == MacroType.NO_FORMAT.getValue();

        and: "'configuration' tab link appears"
        dialog.isConfigurationTabLinkPresent();

        and: "'preview' tab link appears"
        dialog.isPreviewTabLinkPresent();

        and: "remove button appears on the dialog"
        dialog.isRemoveMacroButtonPresent();
    }

    def "GIVEN MacroModalDialog is opened WHEN 'Cancel' button has been pressed THEN the modal dialog is closed"()
    {
        given: "existing content with html-area is opened"
        findAndSelectContent( HTML_AREA_CONTENT.getName() ).clickToolbarEdit();
        and: "MacroDialog opened"
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();

        when: "'Cancel'-button has been pressed"
        dialog.clickOnCancel();

        then: "the modal dialog is closed"
        dialog.waitForClosed();
    }
}
