package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.MacroModalDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.MacroType
import com.enonic.autotests.pages.form.HtmlArea0_1_FormViewPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
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
        selectSiteOpenWizard( HTML_AREA_CONTENT.getContentTypeName() ).typeData( HTML_AREA_CONTENT ).save();
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );

        when: " 'insert macro' button on toolbar has been pressed"
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();
        TestUtils.saveScreenshot( getSession(), "test_macro_dialog_shown" );

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

    def "GIVEN MacroModalDialog opened WHEN twitter macro selected THEN correct macro is displayed on the dialog"()
    {
        given: "existing content with html-area is opened"
        findAndSelectContent( HTML_AREA_CONTENT.getName() ).clickToolbarEdit();
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();

        when: "twitter-macro selected from the options"
        dialog.selectOption( MacroType.TWITTER.getValue() );
        TestUtils.saveScreenshot( getSession(), "test_twitter_macro" );

        then: "correct macro is displayed on the dialog"
        dialog.getSelectedMacroDisplayName() == MacroType.TWITTER.getValue();
    }
}
