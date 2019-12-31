package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.TextLine1_1_FormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class Occurrences_TextLine_1_1_Spec
    extends Base_InputFields_Occurrences

{
    @Shared
    String TEST_TEXT = "test text 1:1";
    @Shared
    Content TEST_CONTENT1;

    def "GIVEN 'text line' content is opened (required text input is empty) WHEN the content has been saved THEN red icon should be displayed in the wizard page"()
    {
        given: "'text line' content is saved(required text was not typed)"
        TEST_CONTENT1 = buildTextLine1_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( TEST_CONTENT1.getContentTypeName() ).typeData( TEST_CONTENT1 );
        TextLine1_1_FormViewPanel formViewPanel = new TextLine1_1_FormViewPanel( getSession() );

        when: "content has been saved"
        wizard.save();

        then: "one text input should be displayed in the form view"
        formViewPanel.getNumberOfTextInputs() == 1;

        and: "button 'Add' should not be present in the page"
        !formViewPanel.isAddButtonPresent();

        and: "validation message should be present, because required input is empty"
        formViewPanel.isValidationMessagePresent();

        and: "red icon should be displayed in the wizard page"
        wizard.isContentInvalid();
    }

    def "GIVEN existing text-line content(required input is empty) WHEN the content has been selected THEN the content should be not valid in the browse panel"()
    {
        when: "the content has been selected"
        findAndSelectContent( TEST_CONTENT1.getContentTypeName() );
        saveScreenshot( "textline-not-valid1" );

        then: "the content should be not valid in the browse panel"
        contentBrowsePanel.isContentInvalid( TEST_CONTENT1.getName() );
    }

    def "GIVEN new TextLine1:1 is created (text was typed) WHEN the content has been reopened THEN expected text Content should be present"()
    {
        given: "wizard for creating of TextLine1:1 is opened"
        Content textLineContent = buildTextLine1_1_Content( TEST_TEXT );
        ContentWizardPanel wizard = selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine1_1_FormViewPanel formViewPanel = new TextLine1_1_FormViewPanel( getSession() );

        when: "all required data has been typed and saved:"
        wizard.typeData( textLineContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        and: "new content should be listed in the grid and when it is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( textLineContent );
        String valueFromUI = formViewPanel.getTextLineValue();

        then: "numbers of inputs should be 1"
        formViewPanel.getNumberOfTextInputs() == 1;
        and: "saved string should be present in the wizard page"
        valueFromUI.contains( TEST_TEXT );

        and: "red icon should not be displayed in the wizard page, because the required input is not empty"
        !wizard.isContentInvalid();
    }

    def "GIVEN wizard for creating of TextLine1:1 is opened WHEN all data typed and the content has been published THEN the content's status should be 'Online'"()
    {
        given: "wizard for creating of TextLine1:1 is opened"
        Content textLineContent = buildTextLine1_1_Content( TEST_TEXT );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( textLineContent.getContentTypeName() );

        when: "all data typed and the content has been published"
        contentWizardPanel.typeData( textLineContent ).clickOnMarkAsReadyAndDoPublish(  );
        contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL )
        contentWizardPanel.closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( textLineContent.getName() );

        then: "the content's status should be 'Published' (in the grid)"
        contentBrowsePanel.getContentStatus( textLineContent.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() )
    }

    def "GIVEN wizard for new TextLine1:1 is opened WHEN required text input is empty THEN content is invalid and the 'Publish' button should be disabled"()
    {
        given: "wizard for TextLine1:1 is opened"
        Content textLineContent = buildTextLine1_1_Content( TEST_TEXT );
        ContentWizardPanel wizard = selectSitePressNew( textLineContent.getContentTypeName() );

        when: "only the name is typed but required text input is empty"
        wizard.typeDisplayName( textLineContent.getDisplayName() );
        saveScreenshot( "req_textline_is_empty" );

        then: "'Publish' button should be disabled on the wizard-toolbar"
        !wizard.showPublishMenu(  ).isPublishMenuItemEnabled(  );

        and: "the content should be displayed with red icon on the wizard page"
        wizard.isContentInvalid();
    }
}