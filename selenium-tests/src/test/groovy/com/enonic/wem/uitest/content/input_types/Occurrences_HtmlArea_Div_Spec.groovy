package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.BaseHtmlAreaFormViewPanel
import com.enonic.autotests.pages.form.HtmlArea0_0_FormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared

/**
 * Created on 5/11/2017.
 *
 * Tasks:
 * enonic/xp-ui-testing#46 Create Selenium tests for #4906
 * */
class Occurrences_HtmlArea_Div_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    String TEST_TEXT1 = "htmlArea text1";

    @Shared
    String TEST_TEXT2 = "htmlArea text2";

    @Shared
    String DEFAULT_EXPECTED_TEXT1 = "<div>" + TEST_TEXT1 + "</div>";

    @Shared
    String DEFAULT_EXPECTED_TEXT2 = "<div>" + TEST_TEXT2 + "</div>";

    def "GIVEN htmlarea content type configured with 'div' is wrapper element WHEN content is opened  THEN text should be wrapped by 'div'"()
    {
        given: "'HtmlArea content has been added and 'div' is wrapper element"
        Content tinyMceContent = buildHtmlArea_Div_Content( 2, TEST_TEXT1, TEST_TEXT2 );
        ContentWizardPanel wizard = selectSitePressNew( tinyMceContent.getContentTypeName() );
        wizard.typeData( tinyMceContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "the content is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tinyMceContent );
        HtmlArea0_0_FormViewPanel formViewPanel = new HtmlArea0_0_FormViewPanel( getSession() );
        List<String> strings = formViewPanel.getInnerHtmlFromAreas();

        then: "text should be wrapped by 'div'"
        strings.contains( DEFAULT_EXPECTED_TEXT1 );
        and:
        strings.contains( DEFAULT_EXPECTED_TEXT2 );
    }

    private Content buildHtmlArea_Div_Content( long numberOfEditors, String... text )
    {
        PropertyTree data = new PropertyTree();
        if ( text != null || !text.length == 0 )
        {
            data.addLong( BaseHtmlAreaFormViewPanel.NUMBER_OF_EDITORS, numberOfEditors );
            data.addStrings( BaseHtmlAreaFormViewPanel.STRINGS_PROPERTY, text );
        }

        Content tinyMceContent = Content.builder().
            name( NameHelper.uniqueName( "html_div" ) ).
            displayName( "html_div_content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + ":htmlarea_div" ).data( data ).
            build();
        return tinyMceContent;
    }
}
