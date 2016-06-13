package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.MacroModalDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.MacroType
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.TextAreaConfigPanel
import com.enonic.autotests.pages.form.HtmlArea0_1_FormViewPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared

class MacroModalDialog_EmbeddedIFrame_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content HTML_AREA_CONTENT;

    @Shared
    String TEST_TEXT = "test text";

    @Shared
    String EMBEDDED_IFRAME_RESULT = "[embed]" + TEST_TEXT + "[/embed]"

    def "GIVEN MacroModalDialog opened WHEN 'embedded code' macro selected AND content saved THEN correct macro is displayed in the htmlarea"()
    {
        given: "existing content with html-area is opened"
        HTML_AREA_CONTENT = buildHtmlArea0_1_Content( null );

        and: "MacroDialog opened"
        ContentWizardPanel wizard = selectSiteOpenWizard( HTML_AREA_CONTENT.getContentTypeName() ).typeData( HTML_AREA_CONTENT ).save();
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );

        when: "'embedded code' selected from the options"
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();
        PropertyTree data = new PropertyTree();
        data.addString( TextAreaConfigPanel.TEXT_AREA_VALUE, TEST_TEXT );
        dialog.selectOption( MacroType.EMBEDDED_IFRAME ).getMacroConfigPanel().typeData( data );
        dialog.clickInsertButton();
        wizard.save();
        TestUtils.saveScreenshot( getSession(), "test_embedded_iframe_macro_inserted" );

        then: "correct macro is displayed in the htmlarea"
        formViewPanel.getText().contains( EMBEDDED_IFRAME_RESULT );
    }


    def "GIVEN MacroModalDialog opened WHEN 'embedded code' macro selected AND text not typed AND 'insert' button clicked THEN error message appears on the modal dialog"()
    {
        given: "existing content with html-area is opened"
        HTML_AREA_CONTENT = buildHtmlArea0_1_Content( null );
        and: "MacroModalDialog opened"
        selectSiteOpenWizard( HTML_AREA_CONTENT.getContentTypeName() ).typeData( HTML_AREA_CONTENT ).save();
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );

        when: "'embedded code' selected from the options"
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();
        PropertyTree data = new PropertyTree();
        data.addString( TextAreaConfigPanel.TEXT_AREA_VALUE, " " );
        dialog.selectOption( MacroType.EMBEDDED_IFRAME ).getMacroConfigPanel().typeData( data );
        dialog.clickInsertButton();
        TestUtils.saveScreenshot( getSession(), "test_macro_embedded_iframe_textarea_empty" );

        then: "modal dialog is not closed"
        dialog.isOpened();

        and: "text area has a red border"
        ( (TextAreaConfigPanel) dialog.getMacroConfigPanel() ).isTextAreaInvalid();

        and:
        ( (TextAreaConfigPanel) dialog.getMacroConfigPanel() ).isValidationMessagePresent();

        and:
        and: "correct validation message appears"
        ( (TextAreaConfigPanel) dialog.getMacroConfigPanel() ).getValidationMessage() == "This field is required"
    }
}