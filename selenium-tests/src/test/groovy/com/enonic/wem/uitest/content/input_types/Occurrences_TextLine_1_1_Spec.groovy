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

    def "GIVEN opened content wizard WHEN content without required 'text line ' saved THEN wizard has a red icon"()
    {
        given: "new content with type 'text line' added'"
        Content textLineContent = buildTextLine1_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( textLineContent.getContentTypeName() ).typeData( textLineContent );
        TextLine1_1_FormViewPanel formViewPanel = new TextLine1_1_FormViewPanel( getSession() );

        when: "content opened for edit"
        wizard.save();

        then: "content should be invalid, because required field not filled"
        formViewPanel.isValidationMessagePresent();
    }

    def "GIVEN opened content wizard WHEN content without required 'text ' saved and wizard closed THEN grid row with it content has a red icon"()
    {
        given: "new content with type 'text line' added'"
        Content textLineContent = buildTextLine1_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( textLineContent.getContentTypeName() ).typeData( textLineContent );

        when: "content opened for edit"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( textLineContent.getName() );
        saveScreenshot( "textline-not-valid1" )

        then: "content should be invalid, because required field not filled"
        contentBrowsePanel.isContentInvalid( textLineContent.getName() );
    }

    def "WHEN wizard for adding a TextLine-content (1:1) opened THEN one text input present "()
    {
        when: "start to add a content with type 'TextLine 1:1'"
        Content textLineContent = buildTextLine1_1_Content( TEST_TEXT );
        selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine1_1_FormViewPanel formViewPanel = new TextLine1_1_FormViewPanel( getSession() );

        then: "one text input should be displayed in the form view"
        formViewPanel.getNumberOfTextInputs() == 1;
    }

    def "WHEN wizard for adding a TextLine-content (1:1) opened THEN button 'Add' not present on page "()
    {
        when: "start to add a content with type 'TextLine 1:1'"
        Content textLineContent = buildTextLine1_1_Content( TEST_TEXT );
        selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine1_1_FormViewPanel formViewPanel = new TextLine1_1_FormViewPanel( getSession() );

        then: "'Remove' button should not be displayed"
        formViewPanel.getNumberOfDisplayedRemoveButtons() == 0;

        then: "button 'Add' not present on page"
        !formViewPanel.isAddButtonPresent();
    }

    def "GIVEN creating new TextLine1:1 on root WHEN saved and wizard closed THEN new text line Content should be listed  AND  saved text showed when content opened for edit "()
    {
        given: "start to add a content with type 'TextLine 1:1'"
        Content textLineContent = buildTextLine1_1_Content( TEST_TEXT );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( textLineContent.getContentTypeName() );
        TextLine1_1_FormViewPanel formViewPanel = new TextLine1_1_FormViewPanel( getSession() );

        when:
        contentWizardPanel.typeData( textLineContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        then: "new content listed in the grid and can be opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( textLineContent );
        String valueFromUI = formViewPanel.getTextLineValue();

        and: "numbers of inputs is 1"
        formViewPanel.getNumberOfTextInputs() == 1;
        and: "saved strings are present in the Content Wizard"
        valueFromUI.contains( TEST_TEXT );
    }

    def "GIVEN creating new TextLine1:1 on root WHEN data typed and 'Save' and  'Publish' are pressed THEN new content with status equals 'Online' listed"()
    {
        given: "start to add a content with type 'TextLine 1:1'"
        Content textLineContent = buildTextLine1_1_Content( TEST_TEXT );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( textLineContent.getContentTypeName() );

        when:
        contentWizardPanel.typeData( textLineContent ).save().clickOnWizardPublishButton().clickOnPublishNowButton();
        contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL )
        contentWizardPanel.closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( textLineContent.getName() );

        then:
        contentBrowsePanel.getContentStatus( textLineContent.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() )
    }

    def "GIVEN creating new TextLine2:5 on root WHEN required text input is empty THEN content is invalid and the 'Publish' button is disabled"()
    {
        given: "start to add a content with type 'TextLine 1:1'"
        Content textLineContent = buildTextLine1_1_Content( TEST_TEXT );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( textLineContent.getContentTypeName() );

        when:
        contentWizardPanel.typeDisplayName( textLineContent.getDisplayName() );

        then: "'Publish' button is disabled"
        !contentWizardPanel.isPublishButtonEnabled();

        and: "content is invalid"
        contentWizardPanel.isContentInvalid( textLineContent.getDisplayName() );
    }
}