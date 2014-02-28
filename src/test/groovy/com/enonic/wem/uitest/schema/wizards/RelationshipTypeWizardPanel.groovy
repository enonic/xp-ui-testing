package com.enonic.wem.uitest.schema.wizards

import com.enonic.autotests.pages.schemamanager.ContentTypeWizardPanel
import com.enonic.autotests.pages.schemamanager.KindOfContentTypes
import com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.schemamanger.ContentType
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.wem.uitest.schema.cfg.LinkRelationship
import spock.lang.Shared

class RelationshipTypeWizardPanel extends BaseGebSpec
{
    @Shared String RELATIONSHIP_WIZARD_TEST = "wizard_test_key"

    def "GIVEN opened relationship wizard WHEN name typed and clicking Save THEN name is present on WizardPanel"( )
    {
        given:
        go "admin"

        String relationshipName = NameHelper.unqiueName( "relationship" );
        String relCFG = LinkRelationship.CFG
        ContentType relationship = ContentType.with().name( relationshipName ).kind( KindOfContentTypes.RELATIONSHIP_TYPE ).configuration( relCFG ).build();
        ContentTypeWizardPanel wizard = contentTypeService.openAddContentTypeWizard( getTestSession(), KindOfContentTypes.RELATIONSHIP_TYPE );
        wizard.doTypeData( relationship )
        getTestSession().put( RELATIONSHIP_WIZARD_TEST, relationship )

        when:
        wizard.doSaveFromToolbar()

        then:
        wizard.getNameInputValue().equals( relationshipName )

    }


    def "GIVEN opened relationship wizard WHEN name typed and clicking Save THEN notification message is present"( )
    {
        given:
        go "admin"

        String relationshipName = NameHelper.unqiueName( "relationship" );
        String relCFG = LinkRelationship.CFG
        ContentType relationship = ContentType.with().name( relationshipName ).kind( KindOfContentTypes.RELATIONSHIP_TYPE ).configuration( relCFG ).build();
        ContentTypeWizardPanel wizard = contentTypeService.openAddContentTypeWizard( getTestSession(), KindOfContentTypes.RELATIONSHIP_TYPE );
        wizard.doTypeData( relationship )

        when:
        wizard.doSaveFromToolbar()

        then:
        wizard.waitNotificationMessage() != null

    }

    def "GIVEN existing relationship-type WHEN it renamed and clicking Save THEN changed name is present on WizardPanel"( )
    {
        given:
        go "admin"
        ContentType relationship = (ContentType) getTestSession().get( RELATIONSHIP_WIZARD_TEST )
        SchemaBrowsePanel browsePanel = NavigatorHelper.openSchemaManager( getTestSession() )

        when:
        String newName = NameHelper.unqiueName( "newname" )
        ContentTypeWizardPanel wizard = browsePanel.doOpenContentTypeForEdit( relationship )
        wizard.clearAndType( wizard.getNameInput(), newName )
        wizard.doSaveFromToolbar()

        then:
        wizard.getNameInputValue().equals( newName )

    }
}
