package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.*
import com.enonic.autotests.pages.form.HtmlArea0_1_FormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class MacroModalDialog_DisableMacros_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content HTML_AREA_CONTENT;

    @Shared
    String TEST_TEXT = "test text";

    @Shared
    String DISABLE_MACROS_RESULT = "[disable]" + TEST_TEXT + "[/disable]"

    def "GIVEN MacroModalDialog opened WHEN 'Disable macros' macro selected AND content saved THEN correct macro is displayed in the htmlarea"()
    {
        given: "existing content with html-area is opened"
        HTML_AREA_CONTENT = buildHtmlArea0_1_Content( null );

        and: "MacroDialog opened"
        ContentWizardPanel wizard = selectSiteOpenWizard( HTML_AREA_CONTENT.getContentTypeName() ).typeData( HTML_AREA_CONTENT ).save();
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );

        when: "'Disable macros' selected from the options"
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();
        PropertyTree data = new PropertyTree();
        data.addString( TextAreaConfigPanel.TEXT_AREA_VALUE, TEST_TEXT );
        dialog.selectOption( MacroType.DISABLE_MACROS ).getMacroConfigPanel().typeData( data );
        dialog.clickInsertButton();
        wizard.save();
        saveScreenshot( "test_disable_macros_inserted" );

        then: "correct macro is displayed in the htmlarea"
        formViewPanel.getText().contains( DISABLE_MACROS_RESULT );
    }

    def "GIVEN MacroModalDialog opened WHEN 'Disable macros' selected AND text not typed AND 'insert' button clicked THEN error message appears on the modal dialog"()
    {
        given: "existing content with html-area is opened"
        findAndSelectContent( HTML_AREA_CONTENT.getName() ).clickToolbarEdit();
        and: "MacroModalDialog opened"
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();

        when: "'Disable macros' selected from the options"
        PropertyTree data = new PropertyTree();
        data.addString( TextAreaConfigPanel.TEXT_AREA_VALUE, " " );
        dialog.selectOption( MacroType.DISABLE_MACROS ).getMacroConfigPanel().typeData( data );
        dialog.clickInsertButton();
        saveScreenshot( "test_disable_macros_textarea_empty" );

        then: "modal dialog is not closed"
        dialog.isOpened();

        and: "text area has a red border"
        ( (TextAreaConfigPanel) dialog.getMacroConfigPanel() ).isTextAreaInvalid();

        and:
        ( (TextAreaConfigPanel) dialog.getMacroConfigPanel() ).isValidationMessagePresent();

        and: "correct validation message appears"
        ( (TextAreaConfigPanel) dialog.getMacroConfigPanel() ).getValidationMessage() == "This field is required"
    }

    def "GIVEN MacroModalDialog opened WHEN 'Disable macros' selected AND text not typed AND 'preview' link on the dialog clicked THEN warning message appears on the modal dialog"()
    {
        given: "existing content with html-area is opened"
        findAndSelectContent( HTML_AREA_CONTENT.getName() ).clickToolbarEdit();
        and: "MacroModalDialog opened"
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();

        when: "'Disable macros' selected from the options"
        PropertyTree data = new PropertyTree();
        and: "text in the text area not inserted"
        data.addString( TextAreaConfigPanel.TEXT_AREA_VALUE, " " );
        dialog.selectOption( MacroType.DISABLE_MACROS ).getMacroConfigPanel().typeData( data );

        and: "preview tab link clicked"
        MacroPreviewPanel previewPanel = dialog.clickOnPreviewTabLink();
        saveScreenshot( "test_disable_macros_textarea_empty_preview" );

        then: "correct preview warning appears"
        previewPanel.getPreviewMessage() == MacroConfigPanel.NOT_COMPLETE_PREVIEW_MESSAGE;
    }

    def "GIVEN MacroModalDialog opened WHEN 'Disable macros' selected AND text typed in the textArea AND 'preview' link on the dialog clicked THEN correct info in the preview-content panel appears"()
    {
        given: "existing content with html-area is opened"
        findAndSelectContent( HTML_AREA_CONTENT.getName() ).clickToolbarEdit();
        and: "MacroModalDialog opened"
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();

        when: "'Disable macros' selected from the options"
        PropertyTree data = new PropertyTree();
        and: "text in the text area not inserted"
        data.addString( TextAreaConfigPanel.TEXT_AREA_VALUE, "test text" );
        dialog.selectOption( MacroType.DISABLE_MACROS ).getMacroConfigPanel().typeData( data );

        and: "preview tab link clicked"
        MacroPreviewPanel previewPanel = dialog.clickOnPreviewTabLink();
        saveScreenshot( "test_disable_macros_textarea_filled_preview" );

        then: "correct info in the preview-content panel appears"
        previewPanel.getPreviewContentMessage() == "test text";
    }
}