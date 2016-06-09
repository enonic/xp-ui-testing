package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.MacroModalDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.MacroType
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.TwitterConfigPanel
import com.enonic.autotests.pages.form.HtmlArea0_1_FormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared

class MacroModalDialog_Twitter_Spec
    extends Base_InputFields_Occurrences

{
    @Shared
    Content HTML_AREA_CONTENT;

    @Shared
    String TEST_TWIT = "https://twitter.com/lashkov_74/status/740477223136813056";

    def "GIVEN MacroModalDialog opened WHEN twitter macro selected AND content saved THEN correct macro is displayed on the dialog"()
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
        dialog.selectOption( MacroType.TWITTER ).getMacroConfigPanel().typeData( data );
        dialog.clickInsertButton();
        wizard.save();

        then: "correct macro is displayed on the dialog"
        formViewPanel.getText().contains( TEST_TWIT );
    }

}
