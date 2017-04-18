package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.TextAreaFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content

class TextArea_Spec
    extends Base_InputFields_Occurrences
{

    def "WHEN wizard for adding of TextArea-content(not required) is opened AND display name was typed THEN empty TextArea should be present AND Publish button should be disabled"()
    {
        when: "wizard is opened(text area is not required )"
        Content textAreaContent = build_TextArea0_1_Content( "test" );
        ContentWizardPanel wizard = selectSitePressNew( textAreaContent.getContentTypeName() );

        and: "only name was typed( the content not saved yet)"
        wizard.typeDisplayName( textAreaContent.getDisplayName() );

        then: "empty text area should be present"
        TextAreaFormViewPanel areaFormViewPanel = new TextAreaFormViewPanel( getSession() );
        areaFormViewPanel.getTextAreaValue().isEmpty();

        and: "the content is valid, because the input is not required"
        !wizard.isContentInvalid();

        and: "'Publish' button should be enabled, because the input is not required"
        wizard.isPublishButtonEnabled();
    }

    def "GIVEN wizard for TextArea-content(required) is opened WHEN display name was typed THEN red icon should be displayed on the wizard page, because the input is required"()
    {
        given: "wizard for TextArea-content(required) is opened"
        Content textAreaContent = build_TextArea1_1_Content( "" );
        ContentWizardPanel wizard = selectSitePressNew( textAreaContent.getContentTypeName() );

        when: "display name was typed and 'save' pressed"
        wizard.typeData( textAreaContent ).save();
        TextAreaFormViewPanel areaFormViewPanel = new TextAreaFormViewPanel( getSession() );

        then: "red icon should be displayed on the wizard page, because the input is required"
        wizard.isContentInvalid();

        and: "correct validation message should be displayed"
        areaFormViewPanel.getValidationMessage() == Application.REQUIRED_MESSAGE;
    }

    def "GIVEN wizard for TextArea-content(not required) is opened WHEN text in the area was not typed the content has been published THEN the content with 'online' status should be displayed"()
    {
        given: "wizard TextArea-content(not required) is opened"
        Content textAreaContent = build_TextArea0_1_Content( "" );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( textAreaContent.getContentTypeName() );

        when: "text in the area was not typed and the content has been published"
        contentWizardPanel.typeData( textAreaContent ).save().clickOnWizardPublishButton().clickOnPublishNowButton();
        contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        contentWizardPanel.closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( textAreaContent.getName() );

        then: "the content with 'online' status should be displayed"
        contentBrowsePanel.getContentStatus( textAreaContent.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() )
    }

    def "GIVEN wizard for TextArea-content(required) is opened WHEN text was typed and the content has been published THEN the content with status 'online' should be displayed"()
    {
        given: "wizard TextArea-content(required) is opened"
        Content textAreaContent = build_TextArea1_1_Content( "test text" );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( textAreaContent.getContentTypeName() );

        when: "data has been typed and the content published"
        contentWizardPanel.typeData( textAreaContent ).save().clickOnWizardPublishButton().clickOnPublishNowButton();
        contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        contentWizardPanel.closeBrowserTab().switchToBrowsePanelTab();
        filterPanel.typeSearchText( textAreaContent.getName() );

        then: "the content with 'online' status should be displayed"
        contentBrowsePanel.getContentStatus( textAreaContent.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() )
    }

    def "GIVEN TextArea-content content with the text was added WHEN the content is opened THEN correct text should be present in the text area"()
    {
        given: "TextArea-content content with the text was added"
        Content textAreaContent = build_TextArea0_1_Content( "test text" );
        selectSitePressNew( textAreaContent.getContentTypeName() ).typeData( textAreaContent ).save().close(
            textAreaContent.getDisplayName() ); ;

        when: "the content is opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( textAreaContent );
        TextAreaFormViewPanel areaFormViewPanel = new TextAreaFormViewPanel( getSession() );

        then: "correct text should be present in the text area"
        areaFormViewPanel.getTextAreaValue() == "test text";
    }
}
