package com.enonic.wem.uitest.content.relationship

import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.RelationshipFormView
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.input_types.Base_InputFields_Occurrences
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Ignore
@Stepwise
class Occurrences_DefaultRelation_2_4_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content TEST_RELATIONSHIP_CONTENT;

    def "GIVEN  wizard for Default Relation(0:1) is opened WHEN one file is selected THEN option filter should be displayed and one selected file should be present "()
    {
        given: "start to add a content with type 'Relation 2:4'"
        Content relationship = buildDefaultRelation2_4_Content( NORD_IMAGE_DISPLAY_NAME );
        ContentWizardPanel wizard = selectSitePressNew( relationship.getContentTypeName() );
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );

        when: "data has been typed"
        wizard.typeData( relationship );

        then: "one selected file should be displayed"
        formViewPanel.getNumberOfSelectedFiles() == 1;
        and: "option filter should be displayed"
        formViewPanel.isOptionFilterDisplayed();
        and: "correct name of selected file should be displayed"
        formViewPanel.getDisplayNamesOfSelectedFiles().get( 0 ).equals( NORD_IMAGE_DISPLAY_NAME );

        and: "content should be displayed as invalid, because only one option selected, but min required is 2"
        wizard.isContentInvalid()
    }

    def "GIVEN saving of content with type Default Relation(2:4) WHEN content saved and opened for edit THEN correct selected file should bedisplayed "()
    {
        given: "saving of a content with type 'Relation 2:4'"
        TEST_RELATIONSHIP_CONTENT = buildDefaultRelation2_4_Content( NORD_IMAGE_DISPLAY_NAME );
        ContentWizardPanel wizard = selectSitePressNew( TEST_RELATIONSHIP_CONTENT.getContentTypeName() );
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );
        wizard.typeData( TEST_RELATIONSHIP_CONTENT ).save().close( TEST_RELATIONSHIP_CONTENT.getDisplayName() );
        contentBrowsePanel.doClearSelection();

        when: "the content is opened"
        findAndSelectContent( TEST_RELATIONSHIP_CONTENT.getName() ).clickToolbarEdit();

        then: "one file should be selected in the form view"
        formViewPanel.getNumberOfSelectedFiles() == 1;
        and: "correct name of selected file should be displayed"
        formViewPanel.getNamesOfSelectedFiles().get( 0 ).contains( NORD_IMAGE_NAME );
    }

    def "GIVEN existing content with one selected option is opened WHEN three options were added THEN 4 selected files should be displayed on the  wizard page"()
    {
        given: "existing content with one selected option is opened"
        ContentWizardPanel wizard = findAndSelectContent( TEST_RELATIONSHIP_CONTENT.getName() ).clickToolbarEdit();
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );
        and: "three options were added"
        TEST_RELATIONSHIP_CONTENT.getData().addStrings( RelationshipFormView.RELATIONSHIPS_PROPERTY, BOOK_IMAGE_DISPLAY_NAME,
                                                        FL_IMAGE_DISPLAY_NAME,
                                                        MAN_IMAGE_DISPLAY_NAME )
        when: "content has been saved"
        formViewPanel.type( TEST_RELATIONSHIP_CONTENT.getData() );
        wizard.save();
        saveScreenshot( "rel_4_opt" )

        then: "4 selected files should be displayed on the  wizard page"
        formViewPanel.getNumberOfSelectedFiles() == 4;
        and: "option filter should not be displayed"
        !formViewPanel.isOptionFilterDisplayed();
    }

    def "GIVEN content with type Default Relation(2:4) and four selected files is opened WHEN one selected option removed THEN option filter appears three options displayed"()
    {
        given: "content with type Default Relation(2:4) and four selected files is opened"
        ContentWizardPanel wizard = findAndSelectContent( TEST_RELATIONSHIP_CONTENT.getName() ).clickToolbarEdit();
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );

        when: "one target was removed"
        formViewPanel.removeSelectedFile( NORD_IMAGE_DISPLAY_NAME );
        wizard.save();
        saveScreenshot( "rel_3_opt" );

        then: "3 selected files should be displayed"
        formViewPanel.getNumberOfSelectedFiles() == 3;
        and: "option filter should appear again"
        formViewPanel.isOptionFilterDisplayed();

        and: "red icon should not be displayed, because three  option are selected, and min required is 2"
        !wizard.isContentInvalid();
    }

    def "WHEN existing content with three selected options opened THEN 'Publish' button is enabled"()
    {
        when: "existing content with three selected options opened"
        ContentWizardPanel wizard = findAndSelectContent( TEST_RELATIONSHIP_CONTENT.getName(  ) ).clickToolbarEdit(  );

        then: "'Publish' button should be enabled, because content is valid"
        wizard.isPublishButtonEnabled();
    }

    def "GIVEN valid content selected in the grid and 'Publish' button on toolbar pressed WHEN content published THEN the content with 'online' status should be displayed"()
    {
        given: "content selected in the grid and opened"
        ContentPublishDialog contentPublishDialog = findAndSelectContent( TEST_RELATIONSHIP_CONTENT.getName() ).clickToolbarPublish();

        when: "'publish' button  was pressed"
        contentPublishDialog.clickOnPublishButton();

        then: "'Published' status should be displayed for this content"
        contentBrowsePanel.getContentStatus( TEST_RELATIONSHIP_CONTENT.getName() ).equalsIgnoreCase( ContentStatus.PUBLISHED.getValue() );
    }

    def "GIVEN content with three selected options opened for edit WHEN two selected options were removed THEN 'Publish' button should be disabled and content is invalid"()
    {
        given: "content with three selected options opened for edit"
        ContentWizardPanel wizard = findAndSelectContent( TEST_RELATIONSHIP_CONTENT.getName() ).clickToolbarEdit();

        when:"two selected options were removed"
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );
        formViewPanel.removeSelectedFile( BOOK_IMAGE_DISPLAY_NAME );
        formViewPanel.removeSelectedFile( MAN_IMAGE_DISPLAY_NAME );
        wizard.save();

        then: "'Publish' button should be disabled, because content is not valid"
        !wizard.isPublishButtonEnabled();

        and: "red icon should be present on the wizard page"
        wizard.isContentInvalid();
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
