package com.enonic.wem.uitest.content.relationship

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ArticleFormView
import com.enonic.autotests.pages.form.RelationshipFormView
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.input_types.Base_InputFields_Occurrences
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Tasks: XP-4948 Add Selenium tests for checking of 'red icon' (invalid content) in wizards*/
@Stepwise
class Occurrences_CustomRelation_0_1_Spec
    extends Base_InputFields_Occurrences
{

    @Shared
    Content TEST_ARTICLE_CONTENT;

    @Shared
    Content RELATIONSHIP_CONTENT;


    def "setup: add a article content "()
    {
        when: "article-content saved"
        TEST_ARTICLE_CONTENT = buildArticle_Content( "articletest", "title", "body" );
        selectSitePressNew( TEST_ARTICLE_CONTENT.getContentTypeName() ).typeData( TEST_ARTICLE_CONTENT ).save().close(
            TEST_ARTICLE_CONTENT.getDisplayName() );
        then: "it should be listed in the grid"
        filterPanel.typeSearchText( TEST_ARTICLE_CONTENT.getName() );
        contentBrowsePanel.exists( TEST_ARTICLE_CONTENT.getName() );
    }

    def "WHEN wizard for adding a content with Custom Relation(0:1) is opened THEN option filter should be present"()
    {
        when: "start to add a content with type 'Custom Relation 0:1'"
        Content relationship = buildCitationRelation0_1_Content( null );
        selectSitePressNew( relationship.getContentTypeName() );
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );
        saveScreenshot( "wizard_custom_rel" );

        then: "wizard with form view should be opened"
        formViewPanel.isOpened();

        and: "option filter should be displayed"
        formViewPanel.isOptionFilterDisplayed();

        and: "number of selcted files is 0"
        formViewPanel.getNumberOfSelectedFiles() == 0;
    }

    def "GIVEN saving of citation without selected article WHEN option was not selected THEN new content should be listed and it is valid"()
    {
        given: "start to add a content with type 'Custom Relation 0:1'"
        Content citation = buildCitationRelation0_1_Content( null );
        ContentWizardPanel wizard = selectSitePressNew( citation.getContentTypeName() );

        when: "'citation' content was saved"
        wizard.typeData( citation ).save().closeBrowserTab().switchToBrowsePanelTab();

        then: "new content should be listed"
        filterPanel.typeSearchText( citation.getName() );
        contentBrowsePanel.exists( citation.getName() );
        saveScreenshot( "citation-added" );

        and: "content should be displayed as valid in the grid"
        !contentBrowsePanel.isContentInvalid( citation.getName() );
    }

    def "GIVEN saving of citation content with one selected article WHEN relationship was saved THEN new content should be listed in the grid"()
    {
        given: "start to add a content with type 'Relation 0:1'"
        RELATIONSHIP_CONTENT = buildCitationRelation0_1_Content( TEST_ARTICLE_CONTENT.getDisplayName() );
        ContentWizardPanel wizard = selectSitePressNew( RELATIONSHIP_CONTENT.getContentTypeName() );

        when: "data typed and the content has been saved"
        wizard.typeData( RELATIONSHIP_CONTENT ).save().closeBrowserTab().switchToBrowsePanelTab();
        then: "new content should be listed in the grid"
        filterPanel.typeSearchText( RELATIONSHIP_CONTENT.getName() );
        contentBrowsePanel.exists( RELATIONSHIP_CONTENT.getName() );
    }

    def "WHEN existing citation content with selected article is opened THEN correct article should be shown in selected options"()
    {
        when: "existing citation content is opened"
        findAndSelectContent( RELATIONSHIP_CONTENT.getName() ).clickToolbarEdit();
        saveScreenshot( "citation-with-article" );
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );

        then: "correct article should be shown in the selected options"
        List<String> names = formViewPanel.getNamesOfSelectedFiles();
        names.get( 0 ).contains( TEST_ARTICLE_CONTENT.getName() );
    }

    def "GIVEN the citation content is selected AND publish button pressed WHEN 'Approve' button pressed THEN citation should be with 'online' status"()
    {
        given: "the citation content is selected and Publish dialog opened"
        ContentPublishDialog contentPublishDialog = findAndSelectContent(
            RELATIONSHIP_CONTENT.getName() ).clickToolbarPublish().waitUntilDialogShown( Application.EXPLICIT_NORMAL );

        when: "content has been published"
        contentPublishDialog.clickOnPublishNowButton();
        String message = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        saveScreenshot( "citation-published" );

        then: "citation should be with 'online' status"
        contentBrowsePanel.getContentStatus( RELATIONSHIP_CONTENT.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() );

        and: "correct notification message should be displayed"
        message == String.format( Application.ONE_CONTENT_PUBLISHED_NOTIFICATION_MESSAGE, RELATIONSHIP_CONTENT.getDisplayName() ) ||
            message.contains( "items are published" );

    }

    private Content buildCitationRelation0_1_Content( String... names )
    {
        PropertyTree data = null;
        if ( names != null )
        {
            data = new PropertyTree();
            data.addStrings( RelationshipFormView.RELATIONSHIPS_PROPERTY, names );
        }
        Content imageSelectorContent = Content.builder().
            name( NameHelper.uniqueName( "citation_rel_" ) ).
            displayName( "citation_rel_0_1" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + ":custom-relationship0_1" ).data( data ).
            build();
        return imageSelectorContent;
    }

    private Content buildArticle_Content( String name, String title, String body )
    {
        PropertyTree data = null;

        if ( title != null && body != null )
        {
            data = new PropertyTree();
            data.addString( ArticleFormView.TITLE, title );
            data.addString( ArticleFormView.BODY, body );
        }
        Content imageSelectorContent = Content.builder().
            name( NameHelper.uniqueName( "article" ) ).
            displayName( name ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + ":article" ).data( data ).
            build();
        return imageSelectorContent;
    }
}
