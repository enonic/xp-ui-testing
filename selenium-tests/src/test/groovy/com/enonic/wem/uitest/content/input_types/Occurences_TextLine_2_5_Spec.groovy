package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.TextLine2_5_FormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Ignore
import spock.lang.Shared

class Occurences_TextLine_2_5_Spec
    extends Base_InputFields_Occurences

{
    @Shared
    String FIRST_TEST_TEXT = "first test text 2:5";

    @Shared
    String SECOND_TEST_TEXT = "second test text 2:5";

    @Shared
    String THIRD_TEST_TEXT = "third test text 2:5";

    @Shared
    String FOURTH_TEST_TEXT = "fourth test text 2:5";

    @Shared
    String FIVES_TEST_TEXT = "fives test text 2:5";


    def "WHEN wizard for adding a TextLine-content (2:5) opened THEN 2 text input present "()
    {
        when: "start to add a content with type 'TextLine 2:5'"
        Content textLineContent = buildTextLine2_5_Content();
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            textLineContent.getContentTypeName() )
        TextLine2_5_FormViewPanel formViewPanel = new TextLine2_5_FormViewPanel( getSession() );

        then: "one text input should be displayed in the form view"
        formViewPanel.getNumberOfTextInputs() == 2;
    }

    //XP-121
    @Ignore
    def "WHEN wizard for adding a TextLine-content (2:5) opened THEN button 'Remove' not present on page "()
    {
        when: "start to add a content with type 'TextLine 2:5'"
        Content textLineContent = buildTextLine2_5_Content();
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            textLineContent.getContentTypeName() )
        TextLine2_5_FormViewPanel formViewPanel = new TextLine2_5_FormViewPanel( getSession() );

        then: "button 'Remove' not present on page"
        formViewPanel.getNumberOfDisplayedRemoveButtons() == 0;
    }

    def "WHEN wizard for adding a TextLine-content (2:5) opened THEN button 'Add' present on page "()
    {
        when: "start to add a content with type 'TextLine 2:5'"
        Content textLineContent = buildTextLine2_5_Content();
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            textLineContent.getContentTypeName() )
        TextLine2_5_FormViewPanel formViewPanel = new TextLine2_5_FormViewPanel( getSession() );

        then: "one text input should be displayed on the form view"
        formViewPanel.isAddButtonPresent();
    }

    def "GIVEN TextLine 2:5 wizard opened WHEN three text inputs added  THEN button 'Add' not present on the page "()
    {
        when: "start to add a content with type 'TextLine 2:5'"
        Content textLineContent = buildTextLine2_5_Content();
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            textLineContent.getContentTypeName() )
        TextLine2_5_FormViewPanel formViewPanel = new TextLine2_5_FormViewPanel( getSession() );
        formViewPanel.clickOnAddButton();
        formViewPanel.clickOnAddButton();
        formViewPanel.clickOnAddButton();

        then: "button 'Add' not present on the page "
        !formViewPanel.isAddButtonPresent();
    }

    def "GIVEN TextLine 2:5 wizard opened and 5 inputs showed on the page WHEN one input removed THEN button 'Add' appears on the page"()
    {
        given: "start to add a content with type 'TextLine 2:5' and add 3 text inputs"
        Content textLineContent = buildTextLine2_5_Content();
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE_NAME ).clickToolbarNew().selectContentType(
            textLineContent.getContentTypeName() )
        TextLine2_5_FormViewPanel formViewPanel = new TextLine2_5_FormViewPanel( getSession() );
        formViewPanel.clickOnAddButton();
        formViewPanel.clickOnAddButton();
        formViewPanel.clickOnAddButton();

        when: "one input removed"
        formViewPanel.clickOnLastRemoveButton();

        then: "button 'Add' appears on the page "
        formViewPanel.isAddButtonPresent();
    }


    def "GIVEN creating new Content on root WHEN saved and wizard closed THEN new text line Content should be listed AND saved text showed when content opened for edit"()
    {
        given: "start to add a content with type 'TextLine 2:5'"
        Content textLineContent = buildTextLine2_5_Content();
        ContentWizardPanel contentWizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow(
            SITE_NAME ).clickToolbarNew().selectContentType( textLineContent.getContentTypeName() )
        TextLine2_5_FormViewPanel formViewPanel = new TextLine2_5_FormViewPanel( getSession() );
        formViewPanel.clickOnAddButton( 3 );

        when:
        contentWizardPanel.typeData( textLineContent ).save().close( textLineContent.getDisplayName() );
        filterPanel.typeSearchText( textLineContent.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( textLineContent.getPath() ).clickToolbarEdit();

        then: "actual text in the text line should be equals as expected"
        List<String> valuesFromUI = formViewPanel.getTextLineValues();
        and: "saved strings are present in the Content Wizard"
        valuesFromUI.contains( FIRST_TEST_TEXT );
        and:
        valuesFromUI.contains( SECOND_TEST_TEXT );
        and:
        valuesFromUI.contains( THIRD_TEST_TEXT );
        and:
        valuesFromUI.contains( FOURTH_TEST_TEXT );
        and:
        valuesFromUI.contains( FIVES_TEST_TEXT );
    }


    def "GIVEN creating new TextLine2:5 on root WHEN data typed and 'Save' and  'Publish' are pressed THEN new content with status equals 'Online' listed"()
    {
        given: "start to add a content with type 'TextLine 2:5'"
        Content textLineContent = buildTextLine2_5_Content();
        ContentWizardPanel contentWizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow(
            SITE_NAME ).clickToolbarNew().selectContentType( textLineContent.getContentTypeName() )


        when: "type a data and 'save' and 'publish'"
        contentWizardPanel.typeData( textLineContent ).save().clickOnPublishButton().close( textLineContent.getDisplayName() );
        filterPanel.typeSearchText( textLineContent.getName() );

        then: "content has a 'online' status"
        contentBrowsePanel.getContentStatus( textLineContent.getPath() ).equals( ContentStatus.ONLINE.getValue() )
    }

    def "GIVEN creating new TextLine2:5 on root WHEN required text input is empty and button 'Publish' pressed THEN validation message appears"()
    {
        given: "start to add a content with type 'TextLine 2:5'"
        Content textLineContent = buildTextLine2_5_Content();
        ContentWizardPanel contentWizardPanel = contentBrowsePanel.clickCheckboxAndSelectRow(
            SITE_NAME ).clickToolbarNew().selectContentType( textLineContent.getContentTypeName() );

        when:
        contentWizardPanel.clickOnPublishButton();
        TestUtils.saveScreenshot( getSession(), "tl_2_5_publish" )
        TextLine2_5_FormViewPanel formViewPanel = new TextLine2_5_FormViewPanel( getSession() );

        then: "new content listed in the grid and can be opened for edit"
        formViewPanel.isValidationMessagePresent();
        and:
        formViewPanel.getValidationMessage() == TextLine2_5_FormViewPanel.VALIDATION_MESSAGE;
    }

    private Content buildTextLine2_5_Content()
    {
        String name = "textline2_5";

        PropertyTree data = new PropertyTree();
        data.addString( "0", FIRST_TEST_TEXT );
        data.addString( "1", SECOND_TEST_TEXT );
        data.addString( "2", THIRD_TEST_TEXT );
        data.addString( "3", FOURTH_TEST_TEXT );
        data.addString( "4", FIVES_TEST_TEXT );

        Content textLineContent = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "textline2_5 content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":textline2_5" ).data( data ).
            build();
        return textLineContent;
    }
}