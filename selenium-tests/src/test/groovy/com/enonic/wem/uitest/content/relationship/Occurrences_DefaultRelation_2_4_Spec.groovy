package com.enonic.wem.uitest.content.relationship

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
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
class Occurrences_DefaultRelation_2_4_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content TEST_RELATIONSHIP_CONTENT;

    def "GIVEN  wizard for  Default Relation(0:1) opened WHEN one file selected THEN option filter displayed and one selected file present "()
    {
        given: "start to add a content with type 'Relation 2:4'"
        Content relationship = buildDefaultRelation2_4_Content( NORD_IMAGE_NAME );
        ContentWizardPanel wizard = selectSiteOpenWizard( relationship.getContentTypeName() );
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );

        when:
        wizard.typeData( relationship );

        then: "one selected file displayed"
        formViewPanel.getNumberOfSelectedFiles() == 1;
        and: "option filter displayed"
        formViewPanel.isOptionFilterDisplayed();
        and: "correct name of selected file displayed"
        formViewPanel.getNamesOfSelectedFiles().get( 0 ).equals( NORD_IMAGE_NAME );
        and: "content is invalid, because only one option selected, but min required is 2"
        wizard.isContentInvalid( relationship.getDisplayName() )
    }

    def "GIVEN saving of content with type Default Relation(2:4) WHEN content saved and opened for edit THEN correct selected file displayed "()
    {
        given: "saving of a content with type 'Relation 2:4'"
        TEST_RELATIONSHIP_CONTENT = buildDefaultRelation2_4_Content( NORD_IMAGE_NAME );
        ContentWizardPanel wizard = selectSiteOpenWizard( TEST_RELATIONSHIP_CONTENT.getContentTypeName() );
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );
        wizard.typeData( TEST_RELATIONSHIP_CONTENT ).save().close( TEST_RELATIONSHIP_CONTENT.getDisplayName() );

        when:
        contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_RELATIONSHIP_CONTENT );

        then: "one file selected in the form view"
        formViewPanel.getNumberOfSelectedFiles() == 1;
        and: "correct name of selected file displayed"
        formViewPanel.getNamesOfSelectedFiles().get( 0 ).equals( NORD_IMAGE_NAME );
    }

    def "GIVEN a content with type Default Relation(2:4) and one file selected in the form view WHEN content opened for edit AND three options added THEN option filter not displayed and there are four selected options"()
    {
        given: "content selected in the grid and opened"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_RELATIONSHIP_CONTENT );
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );
        TEST_RELATIONSHIP_CONTENT.getData().addStrings( RelationshipFormView.RELATIONSHIPS_PROPERTY, BOOK_IMAGE_NAME, FL_IMAGE_NAME,
                                                        MAN_IMAGE_NAME )
        when:
        formViewPanel.type( TEST_RELATIONSHIP_CONTENT.getData() );
        wizard.save();
        TestUtils.saveScreenshot( getSession(), "rel_4_opt" )

        then: "one selected file displayed"
        formViewPanel.getNumberOfSelectedFiles() == 4;
        and: "option filter not  displayed"
        !formViewPanel.isOptionFilterDisplayed();

    }

    def "GIVEN a content with type Default Relation(2:4) and four files selected in the form view WHEN one selected option removed THEN option filter appears three options displayed"()
    {
        given: "content selected in the grid and opened"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_RELATIONSHIP_CONTENT );
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );

        when:
        formViewPanel.removeSelectedFile( NORD_IMAGE_NAME );
        wizard.save();
        TestUtils.saveScreenshot( getSession(), "rel_3_opt" );

        then: "one selected file displayed"
        formViewPanel.getNumberOfSelectedFiles() == 3;
        and: "option filter appears again"
        formViewPanel.isOptionFilterDisplayed();
        and: "content is valid, because three  option are selected, and min required is 2"
        !wizard.isContentInvalid( TEST_RELATIONSHIP_CONTENT.getDisplayName() );
    }

    def "WHEN content with three selected options opened for edit THEN 'Publish' button is enabled"()
    {
        when: "content selected in the grid and opened"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_RELATIONSHIP_CONTENT );

        then: "'Publish' button is enabled, because content is valid"
        wizard.isPublishButtonEnabled();
    }

    def "GIVEN valid content selected in the grid and 'Publish' button on toolbar pressed WHEN content published THEN content with correct status listed"()
    {
        given: "content selected in the grid and opened"
        filterPanel.typeSearchText( TEST_RELATIONSHIP_CONTENT.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( TEST_RELATIONSHIP_CONTENT.getName() );
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitUntilDialogShown(
            Application.EXPLICIT_NORMAL );
        when: "'publish' button on modal dialog pressed"
        contentPublishDialog.clickOnPublishNowButton();
        filterPanel.typeSearchText( TEST_RELATIONSHIP_CONTENT.getName() );

        then: "'Publish' button is enabled, because content is valid"
        contentBrowsePanel.getContentStatus( TEST_RELATIONSHIP_CONTENT.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() );
    }

    def "GIVEN content with three selected options opened for edit WHEN two selected options removed  THEN 'Publish' button is disabled and content is invalid"()
    {
        given: "content with three selected options opened for edit"
        ContentWizardPanel wizard = contentBrowsePanel.selectAndOpenContentFromToolbarMenu( TEST_RELATIONSHIP_CONTENT );
        when:
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );
        formViewPanel.removeSelectedFile( BOOK_IMAGE_NAME );
        formViewPanel.removeSelectedFile( MAN_IMAGE_NAME );
        wizard.save();

        then: "'Publish' button is enabled, because content is not valid"
        !wizard.isPublishButtonEnabled();

        and:
        wizard.isContentInvalid( TEST_RELATIONSHIP_CONTENT.getDisplayName() );
    }

    private Content buildDefaultRelation2_4_Content( String... names )
    {
        PropertyTree data = null;
        if ( names != null )
        {
            data = new PropertyTree();
            data.addStrings( RelationshipFormView.RELATIONSHIPS_PROPERTY, names );
        }
        Content imageSelectorContent = Content.builder().
            name( NameHelper.uniqueName( "def_rel_" ) ).
            displayName( "def_rel_2_4" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ALL_CONTENT_TYPES_APP_NAME + ":default_relation2_4" ).data( data ).
            build();
        return imageSelectorContent;
    }

}
