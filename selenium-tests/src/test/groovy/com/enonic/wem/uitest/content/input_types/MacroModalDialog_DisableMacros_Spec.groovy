package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.*
import com.enonic.autotests.pages.form.HtmlArea0_1_FormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.data.PropertyTree
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
@Ignore
class MacroModalDialog_DisableMacros_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content HTML_AREA_CONTENT;

    @Shared
    String TEST_TEXT = "test text";

    @Shared
    String DISABLE_MACROS_RESULT = "[disable]" + TEST_TEXT + "[/disable]"

    def "GIVEN MacroModalDialog is opened WHEN 'Disable macros' macro selected AND content saved THEN expected macro should be displayed in the htmlarea"()
    {
        given: "existing content with html-area is opened"
        HTML_AREA_CONTENT = buildHtmlArea0_1_Content( null );

        and: "MacroDialog is opened"
        ContentWizardPanel wizard = selectSitePressNew( HTML_AREA_CONTENT.getContentTypeName() ).typeData( HTML_AREA_CONTENT ).save();
        HtmlArea0_1_FormViewPanel htmlAreaFormViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );

        when: "'Disable macros' has been selected in the options"
        MacroModalDialog dialog = htmlAreaFormViewPanel.showToolbarAndClickOnInsertMacroButton();
        PropertyTree data = new PropertyTree();
        data.addString( TextAreaConfigPanel.TEXT_AREA_VALUE, TEST_TEXT );
        dialog.selectOption( MacroType.DISABLE_MACROS ).getMacroConfigPanel().typeData( data );
        dialog.clickInsertButton();
        wizard.save();
        saveScreenshot( "test_disable_macros_inserted" );

        then: "expected text should be displayed in the htmlarea"
        htmlAreaFormViewPanel.getTextInCKE().contains( DISABLE_MACROS_RESULT );
    }

    def "GIVEN MacroModalDialog is opened WHEN 'Disable macros' selected AND text has not typed AND 'insert' button clicked THEN error message appears on the modal dialog"()
    {
        given: "existing content with html-area is opened"
        findAndSelectContent( HTML_AREA_CONTENT.getName() ).clickToolbarEditAndSwitchToWizardTab();
        contentBrowsePanel.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        and: "MacroModal Dialog is opened"
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();

        when: "'Disable macros' has been selected in the options"
        PropertyTree data = new PropertyTree();
        data.addString( TextAreaConfigPanel.TEXT_AREA_VALUE, " " );
        dialog.selectOption( MacroType.DISABLE_MACROS ).getMacroConfigPanel().typeData( data );
        dialog.clickInsertButton();
        saveScreenshot( "test_disable_macros_textarea_empty" );

        then: "modal dialog should not be closed, because the required input is empty"
        dialog.isOpened();

        and: "text area has a red border"
        ( (TextAreaConfigPanel) dialog.getMacroConfigPanel() ).isTextAreaInvalid();

        and:
        ( (TextAreaConfigPanel) dialog.getMacroConfigPanel() ).isValidationMessagePresent();

        and: "expected validation message appears"
        ( (TextAreaConfigPanel) dialog.getMacroConfigPanel() ).getValidationMessage() == "This field is required"
    }

    def "GIVEN MacroModalDialog is opened WHEN 'Disable macros' has been selected AND text has not typed AND 'preview' link on the dialog clicked THEN warning message appears on the modal dialog"()
    {
        given: "existing content with html-area is opened"
        findAndSelectContent( HTML_AREA_CONTENT.getName() ).clickToolbarEditAndSwitchToWizardTab();

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

        then: "expected preview warning appears"
        previewPanel.getPreviewMessage() == MacroConfigPanel.NOT_COMPLETE_PREVIEW_MESSAGE;
    }

    def "GIVEN MacroModalDialog opened WHEN 'Disable macros' has been selected AND text has been typed in the textArea AND 'preview' link on the dialog clicked THEN expected text appears in the preview-content panel"()
    {
        given: "existing content with html-area is opened"
        findAndSelectContent( HTML_AREA_CONTENT.getName() ).clickToolbarEdit();
        and: "MacroModalDialog opened"
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();

        when: "'Disable macros' selected in the options"
        PropertyTree data = new PropertyTree();
        and: "text in the text area not inserted"
        data.addString( TextAreaConfigPanel.TEXT_AREA_VALUE, TEST_TEXT );
        dialog.selectOption( MacroType.DISABLE_MACROS ).getMacroConfigPanel().typeData( data );

        and: "preview tab link has been clicked"
        MacroPreviewPanel previewPanel = dialog.clickOnPreviewTabLink();
        saveScreenshot( "test_disable_macros_textarea_filled_preview" );

        then: "expected text should appear in the preview-content panel"
        previewPanel.getPreviewContentMessage() == TEST_TEXT;
    }
}
