package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.TextAreaFormViewPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree

class TextArea_Spec
    extends Base_InputFields_Occurrences
{

    def "WHEN wizard for adding a TextArea-content  opened THEN empty TextArea present"()
    {
        when: "wizard opened"
        Content textAreaContent = build_TextArea_Content( "test" );
        selectSiteOpenWizard( textAreaContent.getContentTypeName() );

        then: "empty text area present"
        TextAreaFormViewPanel areaFormViewPanel = new TextAreaFormViewPanel( getSession() );
        areaFormViewPanel.getTextAreaValue().isEmpty();

    }

    def "GIVEN wizard for adding a TextArea-content opened WHEN no any text typed and 'Save' and 'Publish' buttons pressed and wizard closed THEN new content with status 'online' appears "()
    {
        given: "start to add a content with type 'TextArea'"
        Content textAreaContent = build_TextArea_Content( "" );
        ContentWizardPanel contentWizardPanel = selectSiteOpenWizard( textAreaContent.getContentTypeName() );

        when: "type a data and 'save' and 'publish'"
        contentWizardPanel.typeData(
            textAreaContent ).save().clickOnWizardPublishButton().clickOnPublishNowButton().waitPublishNotificationMessage(
            Application.EXPLICIT_NORMAL );
        contentWizardPanel.close( textAreaContent.getDisplayName() );
        filterPanel.typeSearchText( textAreaContent.getName() );

        then: "content has a 'online' status"
        contentBrowsePanel.getContentStatus( textAreaContent.getName() ).equals( ContentStatus.ONLINE.getValue() )
    }

    def "GIVEN wizard for adding a TextArea-content opened WHEN text typed and 'Save' and 'Publish' ,'Close' pressed THEN new content with status 'online' appears "()
    {
        given: "start to add a content with type 'TextArea'"
        Content textAreaContent = build_TextArea_Content( "test text" );
        ContentWizardPanel contentWizardPanel = selectSiteOpenWizard( textAreaContent.getContentTypeName() );

        when: "type a data and 'save' and 'publish'"
        contentWizardPanel.typeData(
            textAreaContent ).save().clickOnWizardPublishButton().clickOnPublishNowButton().waitPublishNotificationMessage(
            Application.EXPLICIT_NORMAL );
        contentWizardPanel.close( textAreaContent.getDisplayName() );
        filterPanel.typeSearchText( textAreaContent.getName() );

        then: "content has a 'online' status"
        contentBrowsePanel.getContentStatus( textAreaContent.getName() ).equals( ContentStatus.ONLINE.getValue() )
    }


    def "GIVEN TextArea-content content with text added WHEN just created content opened THEN correct text in text area present "()
    {
        given: "start to add a content with type 'TextArea'"
        Content textAreaContent = build_TextArea_Content( "test text" );
        selectSiteOpenWizard( textAreaContent.getContentTypeName() ).typeData( textAreaContent ).save().close(
            textAreaContent.getDisplayName() ); ;

        when: "just created content opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( textAreaContent );
        TextAreaFormViewPanel areaFormViewPanel = new TextAreaFormViewPanel( getSession() );

        then: "correct text in the text area present"
        areaFormViewPanel.getTextAreaValue() == "test text";
    }

    private Content build_TextArea_Content( String text )
    {
        PropertyTree data = new PropertyTree();
        if ( !text.isEmpty() )
        {
            data.addString( "text", text );
        }
        Content textLineContent = Content.builder().
            name( NameHelper.uniqueName( "textarea" ) ).
            displayName( "text_area content" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_MODULE_NAME + ":textarea0_1" ).data( data ).
            build();
        return textLineContent;
    }
}
