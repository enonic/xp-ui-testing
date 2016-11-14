package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.MacroModalDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.MacroType
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.YoutubeConfigPanel
import com.enonic.autotests.pages.form.HtmlArea0_1_FormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.data.PropertyTree
import spock.lang.Ignore
import spock.lang.Shared

class MacroModalDialog_Youtube_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content HTML_AREA_CONTENT;

    @Shared
    String ENONIC_INTRO_URL = "https://www.youtube.com/watch?v=cFfxuWUgcvI";

    //this is macro was moved into the 'social applications'
    @Ignore
    def "GIVEN MacroModalDialog opened WHEN twitter macro selected AND content saved THEN correct macro is displayed in the htmlarea"()
    {
        given: "existing content with html-area is opened"
        HTML_AREA_CONTENT = buildHtmlArea0_1_Content( null );

        and: "MacroDialog opened"
        ContentWizardPanel wizard = selectSiteOpenWizard( HTML_AREA_CONTENT.getContentTypeName() ).typeData( HTML_AREA_CONTENT ).save();
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );

        when: "youtube-macro selected from the options"
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();
        PropertyTree data = new PropertyTree();
        data.addString( YoutubeConfigPanel.URL_VALUE, ENONIC_INTRO_URL );
        dialog.selectOption( MacroType.YOUTUBE ).getMacroConfigPanel().typeData( data );
        dialog.clickInsertButton();
        wizard.save();

        then: "correct macro is displayed in the htmlarea"
        formViewPanel.getInnerHtml().contains( ENONIC_INTRO_URL );
    }
    //this is macro was removed into the 'social applications'
    @Ignore
    def "GIVEN MacroModalDialog opened WHEN twitter macro selected AND URL not typed AND 'insert' button clicked THEN error message appears on the modal dialog"()
    {
        given: "existing content with html-area is opened"
        HTML_AREA_CONTENT = buildHtmlArea0_1_Content( null );
        and: "MacroModalDialog opened"
        selectSiteOpenWizard( HTML_AREA_CONTENT.getContentTypeName() ).typeData( HTML_AREA_CONTENT ).save();
        HtmlArea0_1_FormViewPanel formViewPanel = new HtmlArea0_1_FormViewPanel( getSession() );

        when: "twitter-macro selected from the options"
        MacroModalDialog dialog = formViewPanel.showToolbarAndClickOnInsertMacroButton();
        PropertyTree data = new PropertyTree();
        data.addString( YoutubeConfigPanel.URL_VALUE, " " );
        dialog.selectOption( MacroType.YOUTUBE ).getMacroConfigPanel().typeData( data );
        dialog.clickInsertButton();
        saveScreenshot( "test_macro_youtube_url_empty" );

        then: "modal dialog is not closed"
        dialog.isOpened();

        and: "URL input has a red border"
        ( (YoutubeConfigPanel) dialog.getMacroConfigPanel() ).isUrlInputInvalid();

        and:
        ( (YoutubeConfigPanel) dialog.getMacroConfigPanel() ).isValidationMessagePresent();

        and: "correct validation message appears"
        ( (YoutubeConfigPanel) dialog.getMacroConfigPanel() ).getValidationMessage() == "This field is required"
    }
}