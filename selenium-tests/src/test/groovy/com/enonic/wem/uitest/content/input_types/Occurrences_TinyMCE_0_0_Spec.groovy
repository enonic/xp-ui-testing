package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.BaseTinyMCEFormViewPanel
import com.enonic.autotests.pages.form.TinyMCE0_0_FormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Ignore
import spock.lang.Shared

class Occurrences_TinyMCE_0_0_Spec
    extends Base_InputFields_Occurrences

{
    @Shared
    String TEST_TEXT1 = "tinyMce text1";

    @Shared
    String TEST_TEXT2 = "tinyMce text2";

    @Shared
    String TEST_TEXT3 = "tinyMce text3";

    @Shared
    String EXPECTED_TEXT1 = "<p>" + TEST_TEXT1 + "</p>";

    @Shared
    String EXPECTED_TEXT2 = "<p>" + TEST_TEXT2 + "</p>";

    @Shared
    String EXPECTED_TEXT3 = "<p>" + TEST_TEXT3 + "</p>";


    def "WHEN wizard for adding a content with TinyMCE(0:0) opened THEN text area is present "()
    {
        when: "start to add a content with type 'TinyMCE 0:0'"
        Content tinyMceContent = buildTinyMce0_0_Content( 1, TEST_TEXT1 );
        openWizard( tinyMceContent.getContentTypeName() );
        TinyMCE0_0_FormViewPanel formViewPanel = new TinyMCE0_0_FormViewPanel( getSession() );

        then: "wizard with form view opened"
        formViewPanel.isOpened();
        and: "text area present"
        formViewPanel.getNumberOfMCE() == 1;
        and:
        formViewPanel.isAddButtonPresent();

    }

    def "GIVEN saving of content with TinyMCE editor  (0:0) and text typed WHEN content opened for edit THEN expected string is present in the editor "()
    {
        given: "new content with type TinyMCE added'"
        Content tinyMceContent = buildTinyMce0_0_Content( 1, TEST_TEXT1 );
        ContentWizardPanel wizard = openWizard( tinyMceContent.getContentTypeName() );
        wizard.typeData( tinyMceContent ).save().close( tinyMceContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tinyMceContent );
        TinyMCE0_0_FormViewPanel formViewPanel = new TinyMCE0_0_FormViewPanel( getSession() );
        List<String> actual = formViewPanel.getTextFromAreas();

        then: "expected text present in the editor"
        actual.size() == 1;
        and:
        actual.get( 0 ) == EXPECTED_TEXT1;

    }

    def "GIVEN wizard for adding a content with TinyMCE(0:0) opened WHEN button 'Add' clicked 3 times THEN tree text area are present"()
    {
        given: "start to add a content with type 'TinyMCE 0:0'"
        Content tinyMceContent = buildTinyMce0_0_Content( 1, TEST_TEXT1 );
        openWizard( tinyMceContent.getContentTypeName() );
        TinyMCE0_0_FormViewPanel formViewPanel = new TinyMCE0_0_FormViewPanel( getSession() );

        when: "button 'Add' clicked 3 times"
        formViewPanel.addEditors( 3 )
        then: "tree text area present"
        formViewPanel.getNumberOfMCE() == 3;
        and: "button 'add' is present"
        formViewPanel.isAddButtonPresent();

    }


    def "GIVEN saving of content with TinyMCE editor (0:1) and text not typed WHEN content opened for edit THEN no text present in the editor"()
    {
        given: "new content with type TinyMCE added'"
        Content tinyMceContent = buildTinyMce0_0_Content( 1, null );
        ContentWizardPanel wizard = openWizard( tinyMceContent.getContentTypeName() );
        wizard.typeData( tinyMceContent ).save().close( tinyMceContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tinyMceContent );
        TinyMCE0_0_FormViewPanel formViewPanel = new TinyMCE0_0_FormViewPanel( getSession() );
        List<String> strings = formViewPanel.getTextFromAreas();

        then: "text area is empty"
        strings.size() == 1;
        and:
        strings.get( 0 ) == BaseTinyMCEFormViewPanel.EMPTY_TEXT_AREA_CONTENT;
    }

    //TODO remove it when bug "INBOX 16"  and XP-532
    @Ignore
    def "GIVEN saving of content with two TinyMCE editor and two strings typed WHEN content opened for edit THEN  two editors with correct strings are present"()
    {
        given: "new content with type TinyMCE added'"
        Content tinyMceContent = buildTinyMce0_0_Content( 2, TEST_TEXT1, TEST_TEXT2 );
        ContentWizardPanel wizard = openWizard( tinyMceContent.getContentTypeName() );
        wizard.typeData( tinyMceContent ).save().close( tinyMceContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tinyMceContent );
        TinyMCE0_0_FormViewPanel formViewPanel = new TinyMCE0_0_FormViewPanel( getSession() );
        List<String> strings = formViewPanel.getTextFromAreas();

        then: "text area is empty"
        strings.size() == 2;
        and:
        strings.contains( EXPECTED_TEXT1 );
        and:
        strings.contains( EXPECTED_TEXT2 );
    }

    //TODO remove it when bug  XP-532
    @Ignore
    def "GIVEN saving of content with tree TinyMCE editor and tree strings typed WHEN content opened for edit THEN  tree editors with correct strings are present"()
    {
        given: "new content with type TinyMCE added'"
        Content tinyMceContent = buildTinyMce0_0_Content( 3, TEST_TEXT1, TEST_TEXT2, TEST_TEXT3 );
        ContentWizardPanel wizard = openWizard( tinyMceContent.getContentTypeName() );
        wizard.typeData( tinyMceContent ).save().close( tinyMceContent.getDisplayName() );

        when: "content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tinyMceContent );
        TinyMCE0_0_FormViewPanel formViewPanel = new TinyMCE0_0_FormViewPanel( getSession() );
        List<String> strings = formViewPanel.getTextFromAreas();

        then: "text area is empty"
        strings.size() == 3;
        and:
        strings.contains( EXPECTED_TEXT1 );
        and:
        strings.contains( EXPECTED_TEXT2 );
        and:
        strings.contains( EXPECTED_TEXT3 );
    }


    private Content buildTinyMce0_0_Content( long numberOfEditors, String... text )
    {
        PropertyTree data = null;
        if ( text != null && text.length != 0 )
        {
            data = new PropertyTree();
            data.addLong( BaseTinyMCEFormViewPanel.NUMBER_OF_EDITORS, numberOfEditors );
            data.addStrings( BaseTinyMCEFormViewPanel.STRINGS_PROPERTY, text );
        }

        Content tinyMceContent = Content.builder().
            name( NameHelper.uniqueName( "mce0_0_" ) ).
            displayName( "mce0_0 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":tiny_mce_0_0" ).data( data ).
            build();
        return tinyMceContent;
    }
}
