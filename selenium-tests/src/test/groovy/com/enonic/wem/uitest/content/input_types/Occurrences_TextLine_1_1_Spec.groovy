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

    def "GIVEN 'text line' content without required text has been saved WHEN the content is opened THEN red icon should be displayed on the wizard page"()
    {
        given: "'text line' content without required text has been saved"
        Content textLineContent = buildTextLine1_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( textLineContent.getContentTypeName() ).typeData( textLineContent );
        TextLine1_1_FormViewPanel formViewPanel = new TextLine1_1_FormViewPanel( getSession() );

        when: "content is opened"
        wizard.save();

        then: "one text input should be displayed on  the form view"
        formViewPanel.getNumberOfTextInputs() == 1;

        and: "button 'Add' should not be present on the page"
        !formViewPanel.isAddButtonPresent();

        and: "validation message should be present, because required input is empty"
        formViewPanel.isValidationMessagePresent();

        and: "red icon should be displayed on the wizard page"
        wizard.isContentInvalid();
    }

    def "GIVEN wizard for creating of 'text line'(required) is opened WHEN content saved without required text and wizard closed THEN the content should be displayed in the grid as invalid"()
    {
        given: "wizard for creating of 'text line'(required) is opened and data without required text has been typed"
        Content textLineContent = buildTextLine1_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( textLineContent.getContentTypeName() ).typeData( textLineContent );

        when: "the content is saved and wizard closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( textLineContent.getName() );
        saveScreenshot( "textline-not-valid1" )

        then: "the content should be displayed in the grid as invalid, because required input is not filled"
        contentBrowsePanel.isContentInvalid( textLineContent.getName() );
    }

    def "GIVEN wizard for creating of TextLine1:1 is opened WHEN saved and wizard closed THEN new text line Content should be listed  AND  saved text showed when content opened for edit "()
    {
        given: "wizard for creating of TextLine1:1 is opened"
        Content textLineContent = buildTextLine1_1_Content( TEST_TEXT );
        ContentWizardPanel wizard = selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine1_1_FormViewPanel formViewPanel = new TextLine1_1_FormViewPanel( getSession() );

        when: "all required data has been typed"
        wizard.typeData( textLineContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        then: "new content should be listed in the grid and when it is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( textLineContent );
        String valueFromUI = formViewPanel.getTextLineValue();

        and: "numbers of inputs should be 1"
        formViewPanel.getNumberOfTextInputs() == 1;
        and: "saved string should be present on the wizard page"
        valueFromUI.contains( TEST_TEXT );

        and: "red icon should not be displayed on the wizard page, because the required input is not empty"
        !wizard.isContentInvalid();
    }

    def "GIVEN wizard for creating of TextLine1:1 is opened WHEN all data typed and the content has been published THEN the content's status should be 'Online'"()
    {
        given: "wizard for creating of TextLine1:1 is opened"
        Content textLineContent = buildTextLine1_1_Content( TEST_TEXT );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( textLineContent.getContentTypeName() );

        when: "all data typed and the content has been published"
        contentWizardPanel.typeData( textLineContent ).save().clickOnWizardPublishButton().clickOnPublishNowButton();
        contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL )
        contentWizardPanel.closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( textLineContent.getName() );

        then: "the content's status should be 'Published' (in the grid)"
        contentBrowsePanel.getContentStatus( textLineContent.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() )
    }

    def "GIVEN wizard for creating of TextLine1:1 is opened WHEN required text input is empty THEN content is invalid and the 'Publish' button is disabled"()
    {
        given: "wizard for creating of TextLine1:1 is opened"
        Content textLineContent = buildTextLine1_1_Content( TEST_TEXT );
        ContentWizardPanel wizard = selectSitePressNew( textLineContent.getContentTypeName() );

        when: "only the name is typed but required text input is empty"
        wizard.typeDisplayName( textLineContent.getDisplayName() );
        saveScreenshot( "req_textline_is_empty" );

        then: "'Publish' button should be disabled on the wizard-toolbar"
        !wizard.isPublishButtonEnabled();

        and: "the content should be displayed with red icon on the wizard page"
        wizard.isContentInvalid();
    }
}