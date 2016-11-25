package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.BaseHtmlAreaFormViewPanel
import com.enonic.autotests.pages.form.HtmlArea0_0_FormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared

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
    String EXPECTED_TEXT1 = "<p>" + TEST_TEXT1 + "</p>";

    @Shared
    String EXPECTED_TEXT2 = "<p>" + TEST_TEXT2 + "</p>";

    @Shared
    String EXPECTED_TEXT3 = "<p>" + TEST_TEXT3 + "</p>";


    def "WHEN wizard for adding a content with HtmlArea(0:0) opened THEN text area is present "()
    {
        when: "start to add a content with type 'HtmlArea 0:0'"
        Content tinyMceContent = buildHtmlArea0_0_Content( 1, TEST_TEXT1 );
        selectSitePressNew( tinyMceContent.getContentTypeName() );
        HtmlArea0_0_FormViewPanel formViewPanel = new HtmlArea0_0_FormViewPanel( getSession() );

        then: "wizard with form view opened"
        formViewPanel.isOpened();
        and: "one text area present"
        formViewPanel.getNumberOfAreas() == 1;
        and:
        formViewPanel.isAddButtonPresent();
    }

    def "GIVEN saving of content with HtmlArea editor  (0:0) and text typed WHEN content opened for edit THEN expected string is present in the editor "()
    {
        given: "new content with type HtmlArea added'"
        Content tinyMceContent = buildHtmlArea0_0_Content( 1, TEST_TEXT1 );
        ContentWizardPanel wizard = selectSitePressNew( tinyMceContent.getContentTypeName() );
        wizard.typeData( tinyMceContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tinyMceContent );
        HtmlArea0_0_FormViewPanel formViewPanel = new HtmlArea0_0_FormViewPanel( getSession() );
        List<String> actual = formViewPanel.getInnerHtmlFromAreas();

        then: "expected text present in the editor"
        actual.size() == 1;
        and:
        actual.get( 0 ) == EXPECTED_TEXT1;
    }

    def "GIVEN wizard for adding a content with HtmlArea(0:0) opened WHEN button 'Add' clicked 3 times THEN tree text area are present"()
    {
        given: "start to add a content with type 'HtmlArea 0:0'"
        Content tinyMceContent = buildHtmlArea0_0_Content( 1, TEST_TEXT1 );
        selectSitePressNew( tinyMceContent.getContentTypeName() );
        HtmlArea0_0_FormViewPanel formViewPanel = new HtmlArea0_0_FormViewPanel( getSession() );

        when: "button 'Add' clicked 3 times"
        formViewPanel.addEditors( 3 )
        then: "tree text area present"
        formViewPanel.getNumberOfAreas() == 3;
        and: "button 'add' is present"
        formViewPanel.isAddButtonPresent();
    }

    def "GIVEN saving of content with HtmlArea editor (0:0) and text not typed WHEN content opened for edit THEN no text present in the editor"()
    {
        given: "new content with type HtmlArea added'"
        Content tinyMceContent = buildHtmlArea0_0_Content( 1, null );
        ContentWizardPanel wizard = selectSitePressNew( tinyMceContent.getContentTypeName() );
        wizard.typeData( tinyMceContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tinyMceContent );
        HtmlArea0_0_FormViewPanel formViewPanel = new HtmlArea0_0_FormViewPanel( getSession() );
        List<String> strings = formViewPanel.getInnerHtmlFromAreas();

        then: "one empty area present"
        strings.size() == 1;
        and:
        strings.get( 0 ) == BaseHtmlAreaFormViewPanel.EMPTY_TEXT_AREA_CONTENT;
    }

    def "GIVEN saving of content with two HtmlArea editor and two strings typed WHEN content opened for edit THEN two editors with correct strings are present"()
    {
        given: "new content with type HtmlArea added'"
        Content tinyMceContent = buildHtmlArea0_0_Content( 2, TEST_TEXT1, TEST_TEXT2 );
        ContentWizardPanel wizard = selectSitePressNew( tinyMceContent.getContentTypeName() );
        wizard.typeData( tinyMceContent ).save().close( tinyMceContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tinyMceContent );
        TestUtils.saveScreenshot( getTestSession(), "html_editor_2" )
        HtmlArea0_0_FormViewPanel formViewPanel = new HtmlArea0_0_FormViewPanel( getSession() );
        List<String> strings = formViewPanel.getInnerHtmlFromAreas();

        then: "two areas with correct text are present"
        strings.size() == 2;
        and:
        strings.contains( EXPECTED_TEXT1 );
        and:
        strings.contains( EXPECTED_TEXT2 );
    }

    def "GIVEN saving of content with tree HtmlArea editor and tree strings typed WHEN content opened for edit THEN tree editors with correct strings are present"()
    {
        given: "new content with type HtmlArea added'"
        Content htmlAreaContentContent = buildHtmlArea0_0_Content( 3, TEST_TEXT1, TEST_TEXT2, TEST_TEXT3 );
        ContentWizardPanel wizard = selectSitePressNew( htmlAreaContentContent.getContentTypeName() );
        wizard.typeData( htmlAreaContentContent ).save().close( htmlAreaContentContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( htmlAreaContentContent );
        TestUtils.saveScreenshot( getTestSession(), "area_3" );
        HtmlArea0_0_FormViewPanel formViewPanel = new HtmlArea0_0_FormViewPanel( getSession() );
        List<String> strings = formViewPanel.getInnerHtmlFromAreas();

        then: "three areas with correct text are present"
        strings.size() == 3;
        and:
        strings.contains( EXPECTED_TEXT1 );
        and:
        strings.contains( EXPECTED_TEXT2 );
        and:
        strings.contains( EXPECTED_TEXT3 );
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
