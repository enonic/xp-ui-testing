package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.TextLine2_5_FormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Ignore
import spock.lang.Shared

@Ignore
class Occurrences_TextLine_2_5_Spec
    extends Base_InputFields_Occurrences

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


    def "WHEN wizard for new TextLine-content (2:5) is opened THEN 2 text-inputs should be present in the page"()
    {
        when: "wizard is opened"
        Content textLineContent = buildTextLine2_5_Content();
        selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine2_5_FormViewPanel formViewPanel = new TextLine2_5_FormViewPanel( getSession() );

        then: "two text-inputs should be displayed in the form view"
        formViewPanel.getNumberOfTextInputs() == 2;

        and: "button 'Remove' should not be present in the page"
        formViewPanel.getNumberOfDisplayedRemoveButtons() == 0;

        then: "button 'Add' should be present in the page"
        formViewPanel.isAddButtonPresent();
    }

    def "GIVEN wizard for new TextLine-content (2:5) is opened WHEN three text inputs has been added THEN button 'Add' should gets not visible"()
    {
        when: "wizard is opened"
        Content textLineContent = buildTextLine2_5_Content();
        ContentWizardPanel wizard = selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine2_5_FormViewPanel formViewPanel = new TextLine2_5_FormViewPanel( getSession() );
        wizard.typeDisplayName( textLineContent.getDisplayName() );

        and: "3 text-line inputs has been added"
        formViewPanel.clickOnAddButton();
        formViewPanel.clickOnAddButton();
        formViewPanel.clickOnAddButton();
        saveScreenshot( "five_textline_inputs" );

        then: "button 'Add' should not be visible in the page, because 5 inputs are present"
        !formViewPanel.isAddButtonPresent();

        and: "red icon should not be present in the wizard page, because default values are present in all text-lines"
        !wizard.isContentInvalid();
    }

    def "GIVEN TextLine 2:5 wizard is opened WHEN three text inputs have been added THEN default values should be present in all inputs"()
    {
        when: "TextLine 2:5 wizard is opened"
        Content textLineContent = buildTextLine2_5_Content();
        selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine2_5_FormViewPanel formViewPanel = new TextLine2_5_FormViewPanel( getSession() );

        and: "three text inputs were added"
        formViewPanel.clickOnAddButton();
        formViewPanel.clickOnAddButton();
        formViewPanel.clickOnAddButton();
        List<String> values = formViewPanel.getTextLineValues();
        saveScreenshot( "text_line_default_values" );

        then: "default values should be present in all inputs"
        values.size() == 5;
        and:
        values.get( 0 ) == "default text";
        and:
        values.get( 4 ) == "default text";
    }

    def "GIVEN TextLine 2:5 wizard is opened and 3 inputs were added WHEN one input has been removed THEN button 'Add' should appear on the page"()
    {
        given: "start to add a content with type 'TextLine 2:5' and 3 text inputs were added"
        Content textLineContent = buildTextLine2_5_Content();
        selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine2_5_FormViewPanel formViewPanel = new TextLine2_5_FormViewPanel( getSession() );
        formViewPanel.clickOnAddButton();
        formViewPanel.clickOnAddButton();
        formViewPanel.clickOnAddButton();

        when: "one input has been removed"
        formViewPanel.clickOnLastRemoveButton();

        then: "button 'Add' should appear in the page"
        formViewPanel.isAddButtonPresent();
    }

    def "GIVEN new TextLine 2:5 content is savede(text have been typed an all 5 inputs) WHEN the content has been reopened THEN expected text should be displayed in"()
    {
        given: "TextLine 2:5 wizard is opened"
        Content textLineContent = buildTextLine2_5_Content();
        ContentWizardPanel wizard = selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine2_5_FormViewPanel formViewPanel = new TextLine2_5_FormViewPanel( getSession() );
        and: "3 inputs are added:"
        formViewPanel.clickOnAddButton( 3 );

        when: "the content has been saved and reopened"
        wizard.typeData( textLineContent ).save().close( textLineContent.getDisplayName() );
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( textLineContent );

        then: "expected strings should be displayed in the wizard"
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

        and: "red icon should not be displayed in the wizard page"
        !wizard.isContentInvalid();
    }

    def "GIVEN wizard for 'TextLine 2:5' is opened WHEN data has been typed and the content published THEN the content gets 'PUBLISHED' in the browse panel"()
    {
        given: "wizard for 'TextLine 2:5' is opened"
        Content textLineContent = buildTextLine2_5_Content();
        ContentWizardPanel contentWizardPanel = selectSitePressNew( textLineContent.getContentTypeName() );

        when: "data has been typed and the content has been published"
        contentWizardPanel.typeData( textLineContent ).clickOnMarkAsReadyAndDoPublish();
        String publishMessage = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        contentWizardPanel.close( textLineContent.getDisplayName() );
        filterPanel.typeSearchText( textLineContent.getName() );

        then: "content gets 'Published'"
        contentBrowsePanel.getContentStatus( textLineContent.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );
        and: "expected notification message should be shown"
        publishMessage == String.format( Application.ITEM_IS_PUBLISHED_NOTIFICATION_MESSAGE, textLineContent.getName() );
    }

    def "GIVEN wizard for TextLine2:5 is opened WHEN required text-lines have been cleared THEN content gets invalid and the 'Publish' button should be disabled"()
    {
        given: "start to add a content with type 'TextLine 2:5'"
        Content textLineContent = buildTextLine2_5_Content();
        ContentWizardPanel wizard = selectSitePressNew( textLineContent.getContentTypeName() );
        wizard.typeDisplayName( textLineContent.getDisplayName() );
        TextLine2_5_FormViewPanel formViewPanel = new TextLine2_5_FormViewPanel( getSession() );

        when: "default values in text-lines are cleared"
        formViewPanel.clearAllInputs();
        wizard.save();
        saveScreenshot( "text_lines_cleared" );

        then: "'Publish...' menu item should be disabled"
        !wizard.showPublishMenu().isPublishMenuItemEnabled();

        and: "content gets invalid"
        wizard.isContentInvalid();

        and: "expected form validation message should appear:"
        formViewPanel.getFormValidationRecording( 0 ) == "Min 2 valid occurrence(s) required";
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
            contentType( ALL_CONTENT_TYPES_APP_NAME + "textline2_5" ).data( data ).
            build();
        return textLineContent;
    }
}
