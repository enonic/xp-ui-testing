package com.enonic.wem.uitest.content.relationship

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.RelationshipFormView
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.input_types.Base_InputFields_Occurrences
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Occurrences_DefaultRelation_0_1_Spec
    extends Base_InputFields_Occurrences

{
    @Shared
    Content TEST_RELATIONSHIP_CONTENT;

    def "WHEN wizard for adding a content with Default Relation(0:1) opened THEN option filter present and there are no selected items"()
    {
        when: "start to add a content with type 'Relation 0:1'"
        Content relationship = buildDefaultRelation0_1_Content( NORD_IMAGE_NAME );
        selectSitePressNew( relationship.getContentTypeName() );
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );

        then: "wizard with form view opened"
        formViewPanel.isOpened();
        and: "option filter is displayed"
        formViewPanel.isOptionFilterDisplayed();
        and: "there are no selected files"
        formViewPanel.getNumberOfSelectedFiles() == 0;
    }

    def "GIVEN  wizard for  Default Relation(0:1) opened WHEN one file selected THEN option filter not present and one selected file displayed "()
    {
        given: "start to add a content with type 'Relation 0:1'"
        Content relationship = buildDefaultRelation0_1_Content( NORD_IMAGE_DISPLAY_NAME );
        ContentWizardPanel wizard = selectSitePressNew( relationship.getContentTypeName() );
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );

        when:
        wizard.typeData( relationship );

        then: "one selected file displayed"
        formViewPanel.getNumberOfSelectedFiles() == 1;
        and: "option filter not displayed"
        !formViewPanel.isOptionFilterDisplayed();
        and: "correct name of selected file displayed"
        formViewPanel.getNamesOfSelectedFiles().get( 0 ).contains( NORD_IMAGE_NAME );
    }

    def "GIVEN saving a content with type Default Relation(0:1) WHEN content saved and opened for edit  THEN correct selected file displayed "()
    {
        given: "saving a content with type 'Relation 0:1'"
        TEST_RELATIONSHIP_CONTENT = buildDefaultRelation0_1_Content( NORD_IMAGE_DISPLAY_NAME );
        ContentWizardPanel wizard = selectSitePressNew( TEST_RELATIONSHIP_CONTENT.getContentTypeName() );
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );
        wizard.typeData( TEST_RELATIONSHIP_CONTENT ).save().closeBrowserTab().switchToBrowsePanelTab();

        when:
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_RELATIONSHIP_CONTENT );

        then: "one file selected in form view"
        formViewPanel.getNumberOfSelectedFiles() == 1;
        and: "option filter not displayed"
        !formViewPanel.isOptionFilterDisplayed();
        and: "correct name of selected file displayed"
        formViewPanel.getNamesOfSelectedFiles().get( 0 ).contains( NORD_IMAGE_NAME );
    }

    def "GIVEN a content with type Default Relation(0:1) and one file selected in the form view WHEN content opened for edit AND selected option removed THEN option filter appears and there are no selected options"()
    {
        given: "content selected in the grid and opened"
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_RELATIONSHIP_CONTENT );
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );

        when:
        formViewPanel.removeSelectedFile( NORD_IMAGE_DISPLAY_NAME );

        then: "one selected file displayed"
        formViewPanel.getNumberOfSelectedFiles() == 0;
        and: "option filter not  displayed"
        formViewPanel.isOptionFilterDisplayed();
    }

    def "GIVEN a content with type Default Relation(0:1) and one file selected in content WHEN content opened for edit AND selected option removed AND content saved and opened again THEN option filter displayed and there are no selected options and content is valid "()
    {
        given: "start to add a content with type 'Relation 0:1'"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_RELATIONSHIP_CONTENT );
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );

        when:
        formViewPanel.removeSelectedFile( NORD_IMAGE_DISPLAY_NAME );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_RELATIONSHIP_CONTENT );

        then: "there are no selected files in the form"
        formViewPanel.getNumberOfSelectedFiles() == 0;
        and: "option filter not  displayed"
        formViewPanel.isOptionFilterDisplayed();
        //TODO add test check for validation in the wizard( when the feature will be implemented)
        //and:
        //!wizard.isContentInvalid( TEST_RELATIONSHIP_CONTENT.getDisplayName() )
    }

    private Content buildDefaultRelation0_1_Content( String... names )
    {
        PropertyTree data = null;
        if ( names != null )
        {
            data = new PropertyTree();
            data.addStrings( RelationshipFormView.RELATIONSHIPS_PROPERTY, names );
        }
        Content imageSelectorContent = Content.builder().
            name( NameHelper.uniqueName( "def_rel_" ) ).
            displayName( "def_rel_0_1" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + ":default_relation0_1" ).data( data ).
            build();
        return imageSelectorContent;
    }

}
