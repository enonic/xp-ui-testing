package com.enonic.wem.uitest.schema.browsepanel

import com.enonic.autotests.pages.schemamanager.KindOfContentTypes
import com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.schemamanger.ContentType
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.wem.uitest.schema.cfg.LinkRelationship
import spock.lang.Shared

class RelationshipSpec
    extends BaseGebSpec
{

    @Shared
    SchemaBrowsePanel schemaBrowsePanel

    def setup()
    {
        go "admin"
        schemaBrowsePanel = NavigatorHelper.openSchemaManager( getTestSession() );
    }


    def "GIVEN BrowsePanel WHEN adding relationship  THEN the new relationship should be listed in the table"()
    {
        given:
        String relCFG = LinkRelationship.CFG
        String relationshipName = NameHelper.uniqueName( "relationship" );
        ContentType relationship = ContentType.with().name( relationshipName ).kind( KindOfContentTypes.RELATIONSHIP_TYPE ).configuration(
            relCFG ).build();
        relationship.setDisplayNameInConfig( "testrelationship" );

        when:
        schemaBrowsePanel.clickToolbarNew().selectKind( KindOfContentTypes.RELATIONSHIP_TYPE.getValue() ).typeData(
            relationship ).save().close()

        then:
        schemaBrowsePanel.exists( relationship );
    }

    def "GIVEN BrowsePanel and existing relationship  WHEN relationship deleted THEN the this relationship should not be listed in the table"()
    {
        given:
        String relCFG = LinkRelationship.CFG
        String relationshipName = NameHelper.uniqueName( "relationship" );
        ContentType relToDelete = ContentType.with().name( relationshipName ).kind( KindOfContentTypes.RELATIONSHIP_TYPE ).configuration(
            relCFG ).build();
        relToDelete.setDisplayNameInConfig( "relationshiptodelete" );
        schemaBrowsePanel.clickToolbarNew().selectKind( KindOfContentTypes.RELATIONSHIP_TYPE.getValue() ).typeData(
            relToDelete ).save().close()

        when:
        schemaBrowsePanel.selectRowWithContentType( relToDelete.getName(),
                                                    relToDelete.getDisplayNameFromConfig() ).clickToolbarDelete().doDelete()

        then:
        !schemaBrowsePanel.exists( relToDelete );
    }

    def "GIVEN BrowsePanel and existing relationship  WHEN relationship renamed THEN  relationship with new name should be listed in the table"()
    {
        given:
        String relCFG = LinkRelationship.CFG
        String relationshipName = NameHelper.uniqueName( "rel-editname" );
        ContentType relToEdit = ContentType.with().name( relationshipName ).kind( KindOfContentTypes.RELATIONSHIP_TYPE ).configuration(
            relCFG ).build();
        relToEdit.setDisplayNameInConfig( "relationshiptoeditname" );
        schemaBrowsePanel.clickToolbarNew().selectKind( KindOfContentTypes.RELATIONSHIP_TYPE.getValue() ).typeData(
            relToEdit ).save().close()


        when:
        String newName = NameHelper.uniqueName( "new-name" );
        ContentType newRelationship = ContentType.with().name( newName ).kind( KindOfContentTypes.RELATIONSHIP_TYPE ).configuration(
            relCFG ).build();
        schemaBrowsePanel.selectRowWithContentType( relToEdit.getName(), relToEdit.getDisplayNameFromConfig() ).clickToolbarEdit().typeData(
            newRelationship ).save().close()

        then:
        schemaBrowsePanel.exists( newRelationship );
    }
}

