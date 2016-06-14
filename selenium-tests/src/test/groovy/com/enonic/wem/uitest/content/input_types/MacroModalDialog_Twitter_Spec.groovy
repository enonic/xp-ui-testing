package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.MacroModalDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.MacroType
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.TwitterConfigPanel
import com.enonic.autotests.pages.form.HtmlArea0_1_FormViewPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.data.PropertyTree
import spock.lang.Ignore
import spock.lang.Shared

class MacroModalDialog_Twitter_Spec
    extends Base_InputFields_Occurrences

{
    @Shared
    Content HTML_AREA_CONTENT;

    @Shared
    String TEST_TWIT = "https://twitter.com/lashkov_74/status/740477223136813056";

    @Shared
    String LANGUAGE_STRING = "en";

    //this is macro was moved into the 'social applications'
    @Ignore
    def "GIVEN MacroModalDialog opened WHEN twitter macro selected AND data saved THEN correct macro is displayed in the htmlarea"()
    {
        given: "existing content with html-area is opened"
        HTML_AREA_CONTENT = buildHtmlArea0_1_Content( null );
        and: "MacroDialog opened"
        ContentWizardPanel wizard = selectSiteOpenWizard( HTML_AREA_CONTENT.getContentTypeName() ).typeData( HTML_AREA_CONTENT ).save();
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );

        when: "twitter-macro selected from the options"
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();
        PropertyTree data = new PropertyTree();
        data.addString( TwitterConfigPanel.URL_VALUE, TEST_TWIT );
        data.addString( TwitterConfigPanel.LANG_VALUE, LANGUAGE_STRING );
        dialog.selectOption( MacroType.TWITTER ).getMacroConfigPanel().typeData( data );
        dialog.clickInsertButton();
        wizard.save();
        TestUtils.saveScreenshot( getSession(), "test_macro_twit_inserted" );

        then: "correct macro is displayed in the htmlarea"
        formViewPanel.getText().contains( TEST_TWIT );

        and:
        formViewPanel.getText().contains( LANGUAGE_STRING );
    }
    //this is macro was moved into the 'social applications'
    @Ignore
    def "GIVEN MacroModalDialog opened WHEN twitter macro selected AND URL not typed AND 'insert' button clicked THEN error message appears on the modal dialog"()
    {
        given: "existing content with html-area is opened"
        HTML_AREA_CONTENT = buildHtmlArea0_1_Content( null );
        and: "MacroDialog opened"
        selectSiteOpenWizard( HTML_AREA_CONTENT.getContentTypeName() ).typeData( HTML_AREA_CONTENT ).save();
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );

        when: "twitter-macro selected from the options"
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();
        PropertyTree data = new PropertyTree();
        data.addString( TwitterConfigPanel.URL_VALUE, " " );
        dialog.selectOption( MacroType.TWITTER ).getMacroConfigPanel().typeData( data );
        dialog.clickInsertButton();
        TestUtils.saveScreenshot( getSession(), "test_macro_twit_not_valid" );

        then: "modal dialog is not closed"
        dialog.isOpened();

        and: "URL input has a red border"
        ( (TwitterConfigPanel) dialog.getMacroConfigPanel() ).isUrlInputInvalid();

        and:
        ( (TwitterConfigPanel) dialog.getMacroConfigPanel() ).isValidationMessagePresent();

        and: "correct validation message appears"
        ( (TwitterConfigPanel) dialog.getMacroConfigPanel() ).getValidationMessage() == "This field is required"
    }
}
