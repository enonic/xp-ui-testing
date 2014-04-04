package com.enonic.wem.uitest.schema.browsepanel

import com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.SchemaCfgHelper
import com.enonic.autotests.vo.schemamanger.RelationshipType
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.wem.uitest.schema.cfg.LinkRelationship
import spock.lang.Shared

class RelationshipSpec
    extends BaseGebSpec
{

    @Shared
    SchemaBrowsePanel schemaBrowsePanel;

    def setup()
    {
        go "admin"
        schemaBrowsePanel = NavigatorHelper.openSchemaManager( getTestSession() );
    }


    def "GIVEN BrowsePanel WHEN adding Relationship  THEN the new Relationship should be listed in the table"()
    {
        given:
        String relCFG = SchemaCfgHelper.changeDisplayName( "testrelationship", LinkRelationship.CFG );
        String relationshipName = NameHelper.uniqueName( "relationship" );
        RelationshipType relationship = RelationshipType.newRelationshipType().name( relationshipName ).configData( relCFG ).build();

        when:
        schemaBrowsePanel.clickToolbarNew().selectKind( relationship.getSchemaKindUI().getValue() ).typeData( relationship ).save().close();

        then:
        schemaBrowsePanel.exists( relationship );
    }

    def "GIVEN BrowsePanel and existing Relationship  WHEN Relationship deleted THEN the this Relationship should not be listed in the table"()
    {
        given:
        String relCFG = SchemaCfgHelper.changeDisplayName( "relationshiptodelete", LinkRelationship.CFG );
        String relationshipName = NameHelper.uniqueName( "relationship" );
        RelationshipType relToDelete = RelationshipType.newRelationshipType().name( relationshipName ).
            configData( relCFG ).build();

        schemaBrowsePanel.clickToolbarNew().selectKind( relToDelete.getSchemaKindUI().getValue() ).typeData( relToDelete ).save().close();

        when:
        schemaBrowsePanel.selectRowWithContentType( relToDelete.getName(),
                                                    relToDelete.getDisplayNameFromConfig() ).clickToolbarDelete().doDelete();

        then:
        !schemaBrowsePanel.exists( relToDelete );
    }

    def "GIVEN BrowsePanel and existing Relationship  WHEN Relationship renamed THEN  Relationship with new name should be listed in the table"()
    {
        given:
        String relCFG = SchemaCfgHelper.changeDisplayName( "relationship-editname", LinkRelationship.CFG );
        String relationshipName = NameHelper.uniqueName( "rel-editname" );
        RelationshipType relToEdit = RelationshipType.newRelationshipType().name( relationshipName ).
            configData( relCFG ).build();
        schemaBrowsePanel.clickToolbarNew().selectKind( relToEdit.getSchemaKindUI().getValue() ).typeData( relToEdit ).save().close();


        when:
        RelationshipType newRelationship = cloneRelationshipWithNewName( relToEdit );
        schemaBrowsePanel.selectRowWithContentType( relToEdit.getName(), relToEdit.getDisplayNameFromConfig() ).clickToolbarEdit().
            typeData( newRelationship ).save().close();

        then:
        schemaBrowsePanel.exists( newRelationship );
    }

    def "GIVEN BrowsePanel and existing Relationship  WHEN Relationship display-name changed THEN  Relationship with new display-name should be listed in the table"()
    {
        given:
        String relCFG = SchemaCfgHelper.changeDisplayName( "relationship-editdname", LinkRelationship.CFG );
        String relationshipName = NameHelper.uniqueName( "rel-editdname" );
        RelationshipType relToEdit = RelationshipType.newRelationshipType().name( relationshipName ).
            configData( relCFG ).build();
        schemaBrowsePanel.clickToolbarNew().selectKind( relToEdit.getSchemaKindUI().getValue() ).typeData( relToEdit ).save().close();


        when:
        RelationshipType newRelationship = cloneRelationshipNewDisplayName( relToEdit );
        schemaBrowsePanel.selectRowWithContentType( relToEdit.getName(), relToEdit.getDisplayNameFromConfig() ).clickToolbarEdit().
            typeData( newRelationship ).save().close();

        then:
        schemaBrowsePanel.exists( newRelationship );
    }

    RelationshipType cloneRelationshipNewDisplayName( RelationshipType source )
    {
        String newDisplayName = NameHelper.uniqueName( "newdisplayname" );
        String newconfigData = SchemaCfgHelper.changeDisplayName( newDisplayName, source.getConfigData() );
        return RelationshipType.newRelationshipType().name( source.getName() ).configData( newconfigData ).build();
    }

    RelationshipType cloneRelationshipWithNewName( RelationshipType source )
    {
        String newName = NameHelper.uniqueName( "newname" );
        return RelationshipType.newRelationshipType().name( newName ).configData( source.getConfigData() ).build();
    }
}

