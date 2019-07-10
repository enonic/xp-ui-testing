package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.TextAreaFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content

class TextArea_Spec
    extends Base_InputFields_Occurrences
{

    def "WHEN new wizard for TextArea-content(not required) is opened AND display name is typed THEN TextArea should be empty AND Publish button should be enabled"()
    {
        when: "wizard is opened(text area is not required )"
        Content textAreaContent = build_TextArea0_1_Content( "test" );
        ContentWizardPanel wizard = selectSitePressNew( textAreaContent.getContentTypeName() );

        and: "only name was typed( the content not saved yet)"
        wizard.typeDisplayName( textAreaContent.getDisplayName() );

        then: "text area should be empty"
        TextAreaFormViewPanel areaFormViewPanel = new TextAreaFormViewPanel( getSession() );
        areaFormViewPanel.getTextAreaValue().isEmpty();

        and: "the content is valid, because the input is not required"
        !wizard.isContentInvalid();

        and: "'Publish' button should be enabled, because the input is not required"
        wizard.isPublishButtonEnabled();
    }

    def "GIVEN new wizard for TextArea-content(required) is opened WHEN display name is typed THEN red icon should be displayed on the wizard page, because the input is required"()
    {
        given: "wizard for TextArea-content(required) is opened"
        Content textAreaContent = build_TextArea1_1_Content( "" );
        ContentWizardPanel wizard = selectSitePressNew( textAreaContent.getContentTypeName() );

        when: "display name has been typed and 'Save' pressed"
        wizard.typeData( textAreaContent ).save();
        TextAreaFormViewPanel areaFormViewPanel = new TextAreaFormViewPanel( getSession() );

        then: "red icon should be displayed on the wizard page, because the input is required"
        wizard.isContentInvalid();

        and: "expected validation message should be displayed"
        areaFormViewPanel.getValidationMessage() == Application.REQUIRED_MESSAGE;
    }

    def "GIVEN wizard for TextArea-content(not required) is opened WHEN content has been published THEN the content should be 'Published' in browse panel"()
    {
        given: "wizard TextArea-content(not required) is opened"
        Content textAreaContent = build_TextArea0_1_Content( "" );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( textAreaContent.getContentTypeName() );

        when: "text area is empty and the content has been published"
        contentWizardPanel.typeData( textAreaContent ).save().clickOnWizardPublishButton().clickOnPublishButton();
        contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        contentWizardPanel.closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( textAreaContent.getName() );

        then: "the content should be 'Published'"
        contentBrowsePanel.getContentStatus( textAreaContent.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() )
    }

    def "GIVEN wizard for TextArea-content(required) is opened WHEN text has been typed and the content has been published THEN the content should be 'Published' in browse panel"()
    {
        given: "wizard TextArea-content(required) is opened"
        Content textAreaContent = build_TextArea1_1_Content( "test text" );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( textAreaContent.getContentTypeName() );

        when: "data has been typed and the content published"
        contentWizardPanel.typeData( textAreaContent ).save().clickOnWizardPublishButton().clickOnPublishButton();
        contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        contentWizardPanel.closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( textAreaContent.getName() );

        then: "the content should be 'Published' in browse panel"
        contentBrowsePanel.getContentStatus( textAreaContent.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() )
    }

    def "GIVEN TextArea-content content with the text is added WHEN the content has been opened THEN expected text should be present in the text area"()
    {
        given: "TextArea-content content with the text was added"
        Content textAreaContent = build_TextArea0_1_Content( "test text" );
        selectSitePressNew( textAreaContent.getContentTypeName() ).typeData( textAreaContent ).save().close(
            textAreaContent.getDisplayName() ); ;

        when: "the content has been opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( textAreaContent );
        TextAreaFormViewPanel areaFormViewPanel = new TextAreaFormViewPanel( getSession() );

        then: "expected text should be present in the text area"
        areaFormViewPanel.getTextAreaValue() == "test text";
    }
}
