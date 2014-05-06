package com.enonic.wem.uitest.schema.wizards

import com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel
import com.enonic.autotests.pages.schemamanager.wizardpanel.RelationshipWizardPanel
import com.enonic.autotests.pages.schemamanager.wizardpanel.SchemaWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.schemamanger.RelationshipType
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.wem.uitest.schema.cfg.LinkRelationship
import spock.lang.Shared
import spock.lang.Stepwise;

@Stepwise
class RelationshipTypeWizardPanel
    extends BaseGebSpec
{
    @Shared
    String RELATIONSHIP_WIZARD_TEST = "wizard_test_key"

    @Shared
    SchemaBrowsePanel schemaBrowsePanel;

    def setup()
    {
        go "admin"
        schemaBrowsePanel = NavigatorHelper.openSchemaManager( getTestSession() );
    }

    def "GIVEN opened relationship wizard WHEN name typed and clicking Save THEN name is present on WizardPanel"()
    {
        given:

        String relationshipName = NameHelper.uniqueName( "relationship" );
        String relCFG = LinkRelationship.CFG;
        RelationshipType relationship = RelationshipType.newRelationshipType().name( relationshipName ).
            configData( relCFG ).build();

        SchemaWizardPanel wizard = schemaBrowsePanel.clickToolbarNew().selectKind( relationship.getSchemaKindUI().getValue() ).
            typeData( relationship );
        getTestSession().put( RELATIONSHIP_WIZARD_TEST, relationship );

        when:
        wizard.save();

        then:
        wizard.getNameInputValue().equals( relationshipName );

    }


    def "GIVEN opened relationship wizard WHEN name typed and clicking Save THEN notification message is present"()
    {
        given:
        String relationshipName = NameHelper.uniqueName( "relationship" );
        String relCFG = LinkRelationship.CFG;
        RelationshipType relationship = RelationshipType.newRelationshipType().name( relationshipName ).configData( relCFG ).build();
        SchemaWizardPanel wizard = schemaBrowsePanel.clickToolbarNew().selectKind( relationship.getSchemaKindUI().getValue() ).
            typeData( relationship );

        when:
        wizard.save();

        then:
        wizard.waitNotificationMessage() != null;

    }

    def "GIVEN existing relationship-type WHEN it renamed and clicking Save THEN changed name is present on WizardPanel"()
    {
        given:
        RelationshipType relationship = (RelationshipType) getTestSession().get( RELATIONSHIP_WIZARD_TEST );

        when:
        String newName = NameHelper.uniqueName( "new-name" );
        RelationshipWizardPanel wizard = schemaBrowsePanel.selectRowWithContentType( relationship.getName(),
                                                                                     relationship.getDisplayNameFromConfig() ).clickToolbarEdit();
        wizard.clearAndType( wizard.getNameInput(), newName );
        wizard.save();

        then:
        wizard.getNameInputValue().equals( newName );

    }
}
