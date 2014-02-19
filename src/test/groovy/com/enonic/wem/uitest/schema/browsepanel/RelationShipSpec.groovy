package com.enonic.wem.uitest.schema.browsepanel

import com.enonic.autotests.pages.schemamanager.KindOfContentTypes
import com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel
import com.enonic.autotests.services.ContentTypeService
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.schemamanger.ContentType
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.wem.uitest.schema.cfg.LinkRelationship
import spock.lang.Shared

class RelationshipSpec extends BaseGebSpec
{


    def "GIVEN BrowsePanel WHEN adding relationship  THEN the new relationship should be listed in the table"( )
    {
        given:
        go "admin"
        String relCFG = LinkRelationship.CFG
        String relationshipName = NameHelper.unqiueName( "relationship" );
        ContentType relationship = ContentType.with().name( relationshipName ).kind( KindOfContentTypes.RELATIONSHIP_TYPE ).configuration( relCFG ).build();
        relationship.setDisplayNameInConfig( "testrelationship" );

        when:
        SchemaBrowsePanel grid = (SchemaBrowsePanel) contentTypeService.createContentType( getTestSession(), relationship, true );

        then:
        grid.isContentTypePresentInTable( relationship );
    }

    def "GIVEN BrowsePanel and existing relationship  WHEN relationship deleted THEN the this relationship should not be listed in the table"( )
    {
        given:
        go "admin"
        String relCFG = LinkRelationship.CFG
        String relationshipName = NameHelper.unqiueName( "relationship" );
        ContentType relToDelete = ContentType.with().name( relationshipName ).kind( KindOfContentTypes.RELATIONSHIP_TYPE ).configuration( relCFG ).build();
        relToDelete.setDisplayNameInConfig( "relationshiptodelete" );
        SchemaBrowsePanel schemasPage = (SchemaBrowsePanel) contentTypeService.createContentType( getTestSession(), relToDelete, true );

        when:
        contentTypeService.deleteContentType( getTestSession(), relToDelete );

        then:
        !schemasPage.isContentTypePresentInTable( relToDelete );
    }

    def "GIVEN BrowsePanel and existing relationship  WHEN relationship renamed THEN  relationship with new name should be listed in the table"( )
    {
        given:
        go "admin"
        String relCFG = LinkRelationship.CFG
        String relationshipName = NameHelper.unqiueName( "releditname" );
        ContentType relToEdit = ContentType.with().name( relationshipName ).kind( KindOfContentTypes.RELATIONSHIP_TYPE ).configuration( relCFG ).build();
        relToEdit.setDisplayNameInConfig( "relationshiptoeditname" );
        SchemaBrowsePanel schemasPage = (SchemaBrowsePanel) contentTypeService.createContentType( getTestSession(), relToEdit, true );


        when:
        String newName = NameHelper.unqiueName( "newname" );
        ContentType newRelationship = ContentType.with().name( newName ).kind( KindOfContentTypes.RELATIONSHIP_TYPE ).configuration( relCFG ).build();
        contentTypeService.editContentType( getTestSession(), relToEdit, newRelationship )

        then:
        schemasPage.isContentTypePresentInTable( newRelationship );
    }
}

