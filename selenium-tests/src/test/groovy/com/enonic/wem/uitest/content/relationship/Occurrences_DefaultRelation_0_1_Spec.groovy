package com.enonic.wem.uitest.content.relationship

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.RelationshipFormView
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.input_types.Base_InputFields_Occurrences
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Occurrences_DefaultRelation_0_1_Spec
    extends Base_InputFields_Occurrences

{
    @Shared
    Content TEST_RELATIONSHIP_CONTENT;

    @Shared
    String FOLDER_RELATION_NAME = NameHelper.uniqueName( "folder" );

    def "Preconditions: child folder should be added for the site"()
    {
        given: "wizard for new folder is opened"
        ContentWizardPanel wizard = selectSitePressNew( "base:folder" );
        wizard.typeDisplayName( FOLDER_RELATION_NAME );

        when: "child folder has been saved"
        wizard.save();

        then: "notification message appears"
        wizard.waitForNotificationMessage();
    }

    def "GIVEN  wizard for Default Relation(0:1) is opened WHEN one file is selected THEN option filter should be displayed and one selected file should be present "()
    {
        given: "start to add a content with type 'Relation 2:4'"
        Content relationship = buildDefaultRelation0_1_Content( NORD_IMAGE_DISPLAY_NAME );
        ContentWizardPanel wizard = selectSitePressNew( relationship.getContentTypeName() );
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );

        when: "External content's name has been typed in options filter input"
        formViewPanel.typeNameInOptionFilter( NORD_IMAGE_DISPLAY_NAME );
        sleep( 500 );

        then: "No matching items  message should be displayed"
        saveScreenshot( "relation_external_content" );
        formViewPanel.isNoMatchingItemsVisible();

    }

    def "WHEN wizard for adding a content with Default Relation(0:1) is opened THEN option filter should be present and target should not be selected"()
    {
        when: "site was clicked and 'Relation 0:1' content type selected in the New Content Dialog"
        Content relationship = buildDefaultRelation0_1_Content( FOLDER_RELATION_NAME );
        selectSitePressNew( relationship.getContentTypeName() );
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );

        then: "required form should be opened"
        formViewPanel.isOpened();
        and: "option filter should be displayed"
        formViewPanel.isOptionFilterDisplayed();
        and: "target should not be selected"
        formViewPanel.getNumberOfSelectedFiles() == 0;
    }

    def "GIVEN wizard for Default Relation(0:1) is opened WHEN one target has been selected THEN option filter should not be present and one target should be displayed"()
    {
        given: "start to add a content with type 'Relation 0:1'"
        Content relationship = buildDefaultRelation0_1_Content( FOLDER_RELATION_NAME );
        ContentWizardPanel wizard = selectSitePressNew( relationship.getContentTypeName() );
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );

        when: "all correct data was typed"
        wizard.typeData( relationship );

        then: "one selected file should be displayed"
        formViewPanel.getNumberOfSelectedFiles() == 1;
        and: "option filter should not displayed"
        !formViewPanel.isOptionFilterDisplayed();
        and: "correct name of selected file should be displayed"
        formViewPanel.getNamesOfSelectedFiles().get( 0 ).contains( FOLDER_RELATION_NAME );
    }

    def "GIVEN saving a content with type Default Relation(0:1) WHEN content saved and opened for edit  THEN correct selected file displayed "()
    {
        given: "saving a content with type 'Relation 0:1'"
        TEST_RELATIONSHIP_CONTENT = buildDefaultRelation0_1_Content( FOLDER_RELATION_NAME );
        ContentWizardPanel wizard = selectSitePressNew( TEST_RELATIONSHIP_CONTENT.getContentTypeName() );
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );
        wizard.typeData( TEST_RELATIONSHIP_CONTENT ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "the content is opened"
        findAndSelectContent( TEST_RELATIONSHIP_CONTENT.getName() ).clickToolbarEdit();

        then: "one file should be selected in form view"
        formViewPanel.getNumberOfSelectedFiles() == 1;
        and: "option filter should not be displayed"
        !formViewPanel.isOptionFilterDisplayed();
        and: "correct name of the selected file should be displayed"
        formViewPanel.getNamesOfSelectedFiles().get( 0 ).contains( FOLDER_RELATION_NAME );
    }

    def "GIVEN existing content with type Default Relation(0:1) is opened WHEN  selected option was removed THEN option filter should appear"()
    {
        given: "existing content with type Default Relation(0:1) is opened"
        findAndSelectContent( TEST_RELATIONSHIP_CONTENT.getName() ).clickToolbarEdit();
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );

        when: "target has been removed"
        formViewPanel.removeSelectedFile( FOLDER_RELATION_NAME );

        then: "target should not be selected"
        formViewPanel.getNumberOfSelectedFiles() == 0;
        and: "option filter should be displayed on the form"
        formViewPanel.isOptionFilterDisplayed();
    }

    def "GIVEN existing content with type Default Relation(0:1) is opened WHEN selected option was removed AND content was saved and opened again THEN option filter should be displayed "()
    {
        given: "existing content with type Default Relation(0:1) is opened"
        ContentWizardPanel wizard = findAndSelectContent( TEST_RELATIONSHIP_CONTENT.getName() ).clickToolbarEdit();
        RelationshipFormView formViewPanel = new RelationshipFormView( getSession() );

        when: "target has been removed"
        formViewPanel.removeSelectedFile( FOLDER_RELATION_NAME );
        and: "content has been saved and closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        and: "the content is opened"
        contentBrowsePanel.clickToolbarEdit();

        then: "number of selected files should be 0"
        formViewPanel.getNumberOfSelectedFiles() == 0;
        and: "option filter should be displayed"
        formViewPanel.isOptionFilterDisplayed();

        and: "red icon should not be displayed, content is valid, because option is not required"
        !wizard.isContentInvalid();
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

    public Content buildFolderContent( String name, String displayName )
    {
        String generated = NameHelper.uniqueName( name );
        Content content = Content.builder().
            name( generated ).
            displayName( displayName ).
            contentType( ContentTypeName.folder() ).
            parent( ContentPath.ROOT ).
            build();
        return content;
    }

}
