package com.enonic.wem.uitest.content.relationship

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ArticleFormView
import com.enonic.autotests.pages.form.RelationshipFormView
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.input_types.Base_InputFields_Occurrences
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared
import spock.lang.Stepwise

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
        selectSiteOpenWizard( TEST_ARTICLE_CONTENT.getContentTypeName() ).typeData( TEST_ARTICLE_CONTENT ).save().close(
            TEST_ARTICLE_CONTENT.getDisplayName() );
        then: "it listed in the grid"
        filterPanel.typeSearchText( TEST_ARTICLE_CONTENT.getName() );
        contentBrowsePanel.exists( TEST_ARTICLE_CONTENT.getName() );
    }


    def "WHEN wizard for adding a content with Custom Relation(0:1) opened THEN option filter present and there are no selected items"()
    {
        when: "start to add a content with type 'Custom Relation 0:1'"
        Content relationship = buildCitationRelation0_1_Content( null );
        selectSiteOpenWizard( relationship.getContentTypeName() );
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );

        then: "wizard with form view opened"
        formViewPanel.isOpened();

        and: "option filter is displayed"
        formViewPanel.isOptionFilterDisplayed();

        and: "there are no selected files"
        formViewPanel.getNumberOfSelectedFiles() == 0;
    }

    def "GIVEN saving of citation without selected article WHEN option not selected THEN new content listed and it is valid"()
    {
        given: "start to add a content with type ' Custom Relation 0:1'"
        Content citation = buildCitationRelation0_1_Content( null );
        ContentWizardPanel wizard = selectSiteOpenWizard( citation.getContentTypeName() );

        when: "wizard with form view opened"
        wizard.typeData( citation ).save().close( citation.getDisplayName() );

        then: "new content listed"
        filterPanel.typeSearchText( citation.getName() );
        contentBrowsePanel.exists( citation.getName() );
        TestUtils.saveScreenshot( getSession(), "citation-added" );

        and: "content is valid"
        !contentBrowsePanel.isContentInvalid( citation.getName() );
    }

    def "GIVEN saving of citation content with one selected article WHEN relationship saved THEN new content listed"()
    {
        given: "start to add a content with type 'Relation 0:1'"
        RELATIONSHIP_CONTENT = buildCitationRelation0_1_Content( TEST_ARTICLE_CONTENT.getDisplayName() );
        ContentWizardPanel wizard = selectSiteOpenWizard( RELATIONSHIP_CONTENT.getContentTypeName() );

        when: "wizard with form view opened"
        wizard.typeData( RELATIONSHIP_CONTENT ).save().close( RELATIONSHIP_CONTENT.getDisplayName() );
        then: "new content listed"
        filterPanel.typeSearchText( RELATIONSHIP_CONTENT.getName() );
        contentBrowsePanel.exists( RELATIONSHIP_CONTENT.getName() );
    }

    def "WHEN the citation content with selected article opened for edit THEN correct article shown in the selected options"()
    {
        when: "citation content opened for edit"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( RELATIONSHIP_CONTENT );
        TestUtils.saveScreenshot( getSession(), "citation-with-article" );
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );

        then: "correct article shown in the selected options"
        List<String> names = formViewPanel.getNamesOfSelectedFiles();
        names.contains( TEST_ARTICLE_CONTENT.getDisplayName() );
    }

    def "GIVEN the citation content with selected article selected in the grid WHEN 'Publish' button pressed THEN citation has a 'online' status"()
    {
        given: "citation content opened for edit"
        filterPanel.typeSearchText( RELATIONSHIP_CONTENT.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( RELATIONSHIP_CONTENT.getName() );
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL );

        when:
        contentPublishDialog.clickOnPublishNowButton();
        String message = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );
        TestUtils.saveScreenshot( getSession(), "citation-published" );

        then: "citation has a 'online' status"
        contentBrowsePanel.getContentStatus( RELATIONSHIP_CONTENT.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() );

        and: "correct notification message appeared"
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
