package com.enonic.wem.uitest.schema.browsepanel

import com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel
import com.enonic.autotests.pages.schemamanager.SchemaType
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.schemamanger.ContentType
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.wem.uitest.schema.cfg.MixinAddress
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class MixinSpec
    extends BaseGebSpec
{
    @Shared
    String MIXIN_KEY = "mixin"

    @Shared
    SchemaBrowsePanel schemaBrowsePanel

    def setup()
    {
        go "admin"
        schemaBrowsePanel = NavigatorHelper.openSchemaManager( getTestSession() );
    }


    def "GIVEN BrowsePanel WHEN adding Mixin-address  THEN the new mixin should be listed in the table"()
    {
        given:
        String mixinCFG = MixinAddress.CFG
        ContentType mixin = ContentType.with().name( NameHelper.uniqueName( "addressmixin" ) ).schemaType( SchemaType.MIXIN ).configuration(
            mixinCFG ).build();
        getTestSession().put( MIXIN_KEY, mixin );

        when:
        schemaBrowsePanel.clickToolbarNew().selectKind( SchemaType.MIXIN.getValue() ).typeData( mixin ).save().close()

        then:
        schemaBrowsePanel.exists( mixin )

    }


    def "GIVEN BrowsePanel and existing Mixin  WHEN Mixin edited, name changed  Then the Mixin with new name should be listed in the table"()
    {
        given:
        ContentType mixinToEdit = (ContentType) getTestSession().get( MIXIN_KEY );
        ContentType newMixin = mixinToEdit.cloneContentType();
        String newName = NameHelper.uniqueName( "mixinrenamed" );
        newMixin.setName( newName )

        when:
        schemaBrowsePanel.selectRowWithContentType( mixinToEdit.getName(),
                                                    mixinToEdit.getDisplayNameFromConfig() ).clickToolbarEdit().typeData(
            newMixin ).save().close()
        mixinToEdit.setName( newName )

        then:
        schemaBrowsePanel.exists( newMixin )

    }


    def "GIVEN BrowsePanel and existing Mixin  WHEN Mixin edited, display-name changed  Then the Mixin with new display-name should be listed in the table"()
    {
        given:

        ContentType mixinToEdit = (ContentType) getTestSession().get( MIXIN_KEY )
        ContentType newMixin = mixinToEdit.cloneContentType()
        String newDisplayName = "change display name test"
        // set a new display name:
        newMixin.setDisplayNameInConfig( newDisplayName )

        when:
        schemaBrowsePanel.selectRowWithContentType( mixinToEdit.getName(),
                                                    mixinToEdit.getDisplayNameFromConfig() ).clickToolbarEdit().typeData(
            newMixin ).save().close()
        mixinToEdit.setDisplayNameInConfig( newDisplayName )

        then:
        schemaBrowsePanel.exists( newMixin )
    }

    def "GIVEN BrowsePanel and existing Mixin WHEN Mixin selected and clicking Delete Then Mixin is removed from list"()

    {
        given:
        ContentType mixinToDelete = (ContentType) getTestSession().get( MIXIN_KEY )

        when:
        schemaBrowsePanel.selectRowWithContentType( mixinToDelete.getName(),
                                                    mixinToDelete.getDisplayNameFromConfig() ).clickToolbarDelete().doDelete()

        then:
        !schemaBrowsePanel.exists( mixinToDelete )
    }
}
