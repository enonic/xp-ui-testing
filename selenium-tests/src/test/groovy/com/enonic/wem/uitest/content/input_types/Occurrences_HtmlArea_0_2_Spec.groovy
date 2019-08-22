package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.BaseHtmlAreaFormViewPanel
import com.enonic.autotests.pages.form.HtmlArea0_0_FormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Occurrences_HtmlArea_0_2_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    String TEST_TEXT1 = "htmlArea text1";

    @Shared
    String TEST_TEXT2 = "htmlArea text2";

    @Shared
    String DEFAULT_EXPECTED_TEXT1 = "<p>" + TEST_TEXT1 + "</p>";

    @Shared
    String DEFAULT_EXPECTED_TEXT2 = "<p>" + TEST_TEXT2 + "</p>";

    def "WHEN wizard for new htmlArea(0:2) content is opened THEN one text area should be present "()
    {
        when: "wizard for new htmlArea(0:2) content is opened"
        Content htmlAreaContent = buildHtmlArea0_2_Content( 1, TEST_TEXT1 );
        ContentWizardPanel wizard = selectSitePressNew( htmlAreaContent.getContentTypeName() );
        HtmlArea0_0_FormViewPanel formViewPanel = new HtmlArea0_0_FormViewPanel( getSession() );
        wizard.typeDisplayName( htmlAreaContent.getDisplayName() );

        then: "one text area should be present"
        formViewPanel.getNumberOfAreas() == 1;

        and: "'Add' button should be present"
        formViewPanel.isAddButtonPresent();

        and: "content is valid, because area is not required"
        !wizard.isContentInvalid();
    }

    def "GIVEN wizard for htmlArea(0:2) is opened WHEN button 'Add' clicked THEN two text areas should be present and button 'Add' is getting hidden"()
    {
        given: "wizard for htmlArea(0:2) is opened"
        Content htmlAreaContent = buildHtmlArea0_2_Content( 2, TEST_TEXT1 );
        ContentWizardPanel wizard = selectSitePressNew( htmlAreaContent.getContentTypeName() );
        HtmlArea0_0_FormViewPanel formViewPanel = new HtmlArea0_0_FormViewPanel( getSession() );
        wizard.typeName( htmlAreaContent.getName() )

        when: "'Add' button has been clicked"
        formViewPanel.clickOnAddButton();
        saveScreenshot( "add_button_hidden" );

        then: "wait until the button 'add' is getting hidden"
        formViewPanel.waitUntilAddButtonNotVisible()

        and: "two text areas should be present"
        formViewPanel.getNumberOfAreas() == 2;
    }

    def "GIVEN wizard for htmlArea(0:2) is opened AND one more area has been added WHEN one text area was removed THEN one area should be present AND button 'Add' should appear"()
    {
        given: "start to add a content with type 'htmlArea 0:2'"
        Content tinyMceContent = buildHtmlArea0_2_Content( 2, TEST_TEXT1 );
        ContentWizardPanel wizard = selectSitePressNew( tinyMceContent.getContentTypeName() );
        HtmlArea0_0_FormViewPanel formViewPanel = new HtmlArea0_0_FormViewPanel( getSession() );
        wizard.typeName( tinyMceContent.getName() );
        formViewPanel.clickOnAddButton();
        int beforeRemoving = formViewPanel.getNumberOfAreas();

        when: "one html-area was removed"
        formViewPanel.removeLastTextArea();
        saveScreenshot( "one_htmlarea_removed" );

        then: "button 'Add' should be present on the page"
        formViewPanel.isAddButtonPresent();

        and: "one text area should be present"
        formViewPanel.getNumberOfAreas() == 1;

        and:
        beforeRemoving == 2;
    }

    def "GIVEN 'HtmlArea 0:2' content has been added AND two strings were typed WHEN the content is opened THEN both strings saved correctly"()
    {
        given: "content with type 'HtmlArea 0:2' has been added"
        Content tinyMceContent = buildHtmlArea0_2_Content( 2, TEST_TEXT1, TEST_TEXT2 );
        ContentWizardPanel wizard = selectSitePressNew( tinyMceContent.getContentTypeName() );
        wizard.typeData( tinyMceContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        saveScreenshot( "area0_2_saved" )

        when: "the content is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( tinyMceContent );
        HtmlArea0_0_FormViewPanel formViewPanel = new HtmlArea0_0_FormViewPanel( getSession() );
        List<String> strings = formViewPanel.getDataFromCKEAreas();

        then: "expected values should be present in both text areas"
        strings.get( 0 ).contains( DEFAULT_EXPECTED_TEXT1 );
        and:
        strings.get( 1 ).contains( DEFAULT_EXPECTED_TEXT2 );
    }

    private Content buildHtmlArea0_2_Content( long numberOfEditors, String... text )
    {
        PropertyTree data = new PropertyTree();
        if ( text != null || !text.length == 0 )
        {
            data.addLong( BaseHtmlAreaFormViewPanel.NUMBER_OF_EDITORS, numberOfEditors );
            data.addStrings( BaseHtmlAreaFormViewPanel.STRINGS_PROPERTY, text );
        }

        Content tinyMceContent = Content.builder().
            name( NameHelper.uniqueName( "html0_2_" ) ).
            displayName( "html0_2 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + "htmlarea0_2" ).data( data ).
            build();
        return tinyMceContent;
    }
}