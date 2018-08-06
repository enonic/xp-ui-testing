package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.*
import com.enonic.autotests.pages.form.HtmlArea0_1_FormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class MacroModalDialog_EmbedIFrame_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content HTML_AREA_CONTENT;

    @Shared
    String ENONIC_IFRAME = "<iframe src='http://www.enonic.com'> enonic</iframe>";

    def "GIVEN MacroModalDialog opened WHEN 'embed iframe' macro selected AND content saved THEN correct macro is displayed in the htmlarea"()
    {
        given: "existing content with html-area is opened"
        HTML_AREA_CONTENT = buildHtmlArea0_1_Content( null );

        and: "MacroDialog opened"
        ContentWizardPanel wizard = selectSitePressNew( HTML_AREA_CONTENT.getContentTypeName() ).typeData( HTML_AREA_CONTENT ).save();
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );

        when: "'embedded code' selected from the options"
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();
        PropertyTree data = new PropertyTree();
        data.addString( TextAreaConfigPanel.TEXT_AREA_VALUE, ENONIC_IFRAME );
        dialog.selectOption( MacroType.EMBED_IFRAME ).getMacroConfigPanel().typeData( data );
        dialog.clickInsertButton();
        wizard.save();
        saveScreenshot( "test_embedded_iframe_macro_inserted" );

        then: "correct macro is displayed in the htmlarea"
        formViewPanel.getTextInCKE().contains( "[embed]" );
        and:
        formViewPanel.getTextInCKE().contains( "http://www.enonic.com" );
    }


    def "GIVEN MacroModalDialog opened WHEN 'embedded code' macro selected AND text not typed AND 'insert' button clicked THEN error message appears on the modal dialog"()
    {
        given: "existing content with html-area is opened"
        findAndSelectContent( HTML_AREA_CONTENT.getName() ).clickToolbarEdit();
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        and: "MacroModalDialog opened"
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();

        when: "'embedded code' selected from the options"
        PropertyTree data = new PropertyTree();
        data.addString( TextAreaConfigPanel.TEXT_AREA_VALUE, " " );
        dialog.selectOption( MacroType.EMBED_IFRAME ).getMacroConfigPanel().typeData( data );
        dialog.clickInsertButton();
        saveScreenshot( "test_macro_embedded_iframe_textarea_empty" );

        then: "modal dialog is not closed"
        dialog.isOpened();

        and: "text area has a red border"
        ( (TextAreaConfigPanel) dialog.getMacroConfigPanel() ).isTextAreaInvalid();

        and:
        ( (TextAreaConfigPanel) dialog.getMacroConfigPanel() ).isValidationMessagePresent();

        and: "correct validation message appears"
        ( (TextAreaConfigPanel) dialog.getMacroConfigPanel() ).getValidationMessage() == "This field is required"
    }

    def "GIVEN MacroModalDialog opened WHEN 'Embed iframe' selected AND text not typed AND 'preview' link on the dialog clicked THEN warning message appears on the modal dialog"()
    {
        given: "existing content with html-area is opened"
        findAndSelectContent( HTML_AREA_CONTENT.getName() ).clickToolbarEditAndSwitchToWizardTab();
        and: "MacroModalDialog opened"
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();

        when: "'Embedded iframe' selected from the options"
        PropertyTree data = new PropertyTree();
        and: "text in the text area not inserted"
        data.addString( TextAreaConfigPanel.TEXT_AREA_VALUE, " " );
        dialog.selectOption( MacroType.EMBED_IFRAME ).getMacroConfigPanel().typeData( data );

        and: "preview tab link clicked"
        MacroPreviewPanel macroPreviewPanel = dialog.clickOnPreviewTabLink();
        saveScreenshot( "test_embedded_iframe_textarea_empty_preview" );

        then: "correct preview message appears"
        macroPreviewPanel.getPreviewMessage() == MacroConfigPanel.NOT_COMPLETE_PREVIEW_MESSAGE;
    }

    def "GIVEN MacroModalDialog opened WHEN 'Embed iframe' selected AND text typed in the textArea AND 'preview' link on the dialog clicked THEN correct info in the preview-content panel appears"()
    {
        given: "existing content with html-area is opened"
        findAndSelectContent( HTML_AREA_CONTENT.getName() ).clickToolbarEditAndSwitchToWizardTab();
        and: "MacroModalDialog opened"
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();

        when: "'Embed iframe' selected from the options"
        PropertyTree data = new PropertyTree();
        and: "text in the text area not inserted"
        data.addString( TextAreaConfigPanel.TEXT_AREA_VALUE, "test text" );
        dialog.selectOption( MacroType.EMBED_IFRAME ).getMacroConfigPanel().typeData( data );

        and: "preview tab link clicked"
        MacroPreviewPanel previewPanel = dialog.clickOnPreviewTabLink();
        saveScreenshot( "test_embedded_iframe_textarea_filled_preview" );

        then: "correct info in the preview-content panel appears"
        previewPanel.getPreviewContentMessage() == MacroPreviewPanel.EXPECTED_IFRAME_MESSAGE_PREVIEW_TAB;
    }

    def "GIVEN MacroModalDialog opened WHEN 'Embed iframe' selected AND correct code typed in the textArea AND 'preview' link on the dialog clicked THEN correct html appears in the preview-content panel"()
    {
        given: "existing content with html-area is opened"
        findAndSelectContent( HTML_AREA_CONTENT.getName() ).clickToolbarEditAndSwitchToWizardTab();
        and: "MacroModalDialog opened"
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();

        when: "'Embed iframe' selected from the options"
        PropertyTree data = new PropertyTree();
        and: "text in the text area not inserted"
        data.addString( TextAreaConfigPanel.TEXT_AREA_VALUE, ENONIC_IFRAME );
        dialog.selectOption( MacroType.EMBED_IFRAME ).getMacroConfigPanel().typeData( data );

        and: "preview tab link clicked"
        MacroPreviewPanel previewPanel = dialog.clickOnPreviewTabLink();
        saveScreenshot( "test_embedded_iframe_enonic_preview" );

        then: "correct info in the preview-content panel appears"
        previewPanel.isIFrameEmbedded( "enonic.com" );
    }
}