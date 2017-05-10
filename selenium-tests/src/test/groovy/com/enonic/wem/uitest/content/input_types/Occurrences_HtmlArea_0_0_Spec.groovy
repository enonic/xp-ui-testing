package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.SourceCodeDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.BaseHtmlAreaFormViewPanel
import com.enonic.autotests.pages.form.HtmlArea0_0_FormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared

/**
 *
 * TASKS:
 * xp-ui-testing#21 Up-to-date tests for SourceCode dialog
 * */
class Occurrences_HtmlArea_0_0_Spec
    extends Base_InputFields_Occurrences

{
    @Shared
    String TEST_TEXT1 = "html area text1";

    @Shared
    String TEST_TEXT2 = "html area text2";

    @Shared
    String TEST_TEXT3 = "html area text3";

    @Shared
    String DEFAULT_EXPECTED_TEXT1 = "<p>" + TEST_TEXT1 + "</p>";

    @Shared
    String DEFAULT_EXPECTED_TEXT2 = "<p>" + TEST_TEXT2 + "</p>";

    @Shared
    String DEFAULT_EXPECTED_TEXT3 = "<p>" + TEST_TEXT3 + "</p>";


    def "WHEN wizard for 'HtmlArea 0:0' content-type is opened THEN html-area should be present AND 'Add' button should be displayed"()
    {
        when: "'HtmlArea 0:0' content-type is selected"
        Content htmlAreaContent = buildHtmlArea0_0_Content( 1, TEST_TEXT1 );
        selectSitePressNew( htmlAreaContent.getContentTypeName() );
        HtmlArea0_0_FormViewPanel formViewPanel = new HtmlArea0_0_FormViewPanel( getSession() );

        then: "one text area should be present"
        formViewPanel.getNumberOfAreas() == 1;
        and: "'Add new area' button should be present"
        formViewPanel.isAddButtonPresent();

        and: "'Source Button button should be displayed"
        formViewPanel.isSourceCodeButtonDisplayed();
    }

    def "GIVEN wizard for htmlArea-content is opened WHEN 'Source Button' has been pressed THEN 'Code Dialog' should appear"()
    {
        given: "wizard for htmlArea-content is opened"
        Content htmlAreaContent = buildHtmlArea0_0_Content( 1, TEST_TEXT1 );
        selectSitePressNew( htmlAreaContent.getContentTypeName() );

        when: "'Source Button' has been pressed"
        HtmlArea0_0_FormViewPanel formViewPanel = new HtmlArea0_0_FormViewPanel( getSession() );
        SourceCodeDialog codeDialog = formViewPanel.clickOnSourceButton();
        saveScreenshot( "code_dialog_htmlarea" );

        then: "'Code Dialog' should be displayed"
        codeDialog.isOpened();
    }

    def "GIVEN HtmlArea(0:0) content with a text was added WHEN the content is opened THEN expected text should be present in the editor"()
    {
        given: "new HtmlArea content was added'"
        Content tinyMceContent = buildHtmlArea0_0_Content( 1, TEST_TEXT1 );
        ContentWizardPanel wizard = selectSitePressNew( tinyMceContent.getContentTypeName() );
        wizard.typeData( tinyMceContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "the content is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tinyMceContent );
        HtmlArea0_0_FormViewPanel formViewPanel = new HtmlArea0_0_FormViewPanel( getSession() );
        List<String> actual = formViewPanel.getInnerHtmlFromAreas();

        then: "expected text should be present in the editor"
        actual.size() == 1;
        and:
        actual.get( 0 ) == DEFAULT_EXPECTED_TEXT1;
    }

    def "GIVEN wizard for HtmlArea(0:0) is opened WHEN button 'Add' was clicked 3 times THEN three text area should be present"()
    {
        given: "wizard for HtmlArea(0:0) is opened"
        Content tinyMceContent = buildHtmlArea0_0_Content( 1, TEST_TEXT1 );
        selectSitePressNew( tinyMceContent.getContentTypeName() );
        HtmlArea0_0_FormViewPanel formViewPanel = new HtmlArea0_0_FormViewPanel( getSession() );

        when: "button 'Add' was clicked 3 times"
        formViewPanel.addEditors( 3 )
        then: "tree text area should be present"
        formViewPanel.getNumberOfAreas() == 3;
        and: "button 'add' is present"
        formViewPanel.isAddButtonPresent();
    }

    def "GIVEN HtmlArea(0:0) content(empty area) was added WHEN content is opened THEN html-area should be empty"()
    {
        given: "new HtmlArea-content was added'"
        Content tinyMceContent = buildHtmlArea0_0_Content( 1, null );
        ContentWizardPanel wizard = selectSitePressNew( tinyMceContent.getContentTypeName() );
        wizard.typeData( tinyMceContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tinyMceContent );
        HtmlArea0_0_FormViewPanel formViewPanel = new HtmlArea0_0_FormViewPanel( getSession() );
        List<String> strings = formViewPanel.getInnerHtmlFromAreas();

        then: "one empty area present"
        strings.size() == 1;
        and: "area should be empty"
        strings.get( 0 ) == BaseHtmlAreaFormViewPanel.DEFAULT_EMPTY_TEXT_AREA_CONTENT;
    }

    def "GIVEN new HtmlArea content with two areas was added WHEN content is opened THEN two editors with correct strings should be present"()
    {
        given: "new HtmlArea content with two areas was added"
        Content tinyMceContent = buildHtmlArea0_0_Content( 2, TEST_TEXT1, TEST_TEXT2 );
        ContentWizardPanel wizard = selectSitePressNew( tinyMceContent.getContentTypeName() );
        wizard.typeData( tinyMceContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "the content is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tinyMceContent );
        saveScreenshot( "html_editor_2" );
        HtmlArea0_0_FormViewPanel formViewPanel = new HtmlArea0_0_FormViewPanel( getSession() );
        List<String> strings = formViewPanel.getInnerHtmlFromAreas();

        then: "two areas with correct text should be displayed"
        strings.size() == 2;
        and:
        strings.contains( DEFAULT_EXPECTED_TEXT1 );
        and:
        strings.contains( DEFAULT_EXPECTED_TEXT2 );
    }

    def "GIVEN new HtmlArea content with three areas was added WHEN the content is opened THEN tree editors with correct strings should be present"()
    {
        given: "new HtmlArea content with three areas was added"
        Content htmlAreaContentContent = buildHtmlArea0_0_Content( 3, TEST_TEXT1, TEST_TEXT2, TEST_TEXT3 );
        ContentWizardPanel wizard = selectSitePressNew( htmlAreaContentContent.getContentTypeName() );
        wizard.typeData( htmlAreaContentContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "the content is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( htmlAreaContentContent );
        saveScreenshot( "area_3" );
        HtmlArea0_0_FormViewPanel formViewPanel = new HtmlArea0_0_FormViewPanel( getSession() );
        List<String> strings = formViewPanel.getInnerHtmlFromAreas();

        then: "three areas with correct text should be present"
        strings.size() == 3;
        and:
        strings.contains( DEFAULT_EXPECTED_TEXT1 );
        and:
        strings.contains( DEFAULT_EXPECTED_TEXT2 );
        and:
        strings.contains( DEFAULT_EXPECTED_TEXT3 );
    }

    private Content buildHtmlArea0_0_Content( long numberOfEditors, String... text )
    {
        PropertyTree data = new PropertyTree(); ;
        if ( text != null && text.length != 0 )
        {
            data.addLong( BaseHtmlAreaFormViewPanel.NUMBER_OF_EDITORS, numberOfEditors );
            data.addStrings( BaseHtmlAreaFormViewPanel.STRINGS_PROPERTY, text );
        }

        Content tinyMceContent = Content.builder().
            name( NameHelper.uniqueName( "htmlarea0_0_" ) ).
            displayName( "html0_0 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + ":htmlarea0_0" ).data( data ).
            build();
        return tinyMceContent;
    }
}
