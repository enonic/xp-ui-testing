package com.enonic.wem.uitest.schema.browsepanel

import com.enonic.autotests.pages.schemamanager.KindOfContentTypes
import com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel
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


    def "GIVEN BrowsePanel WHEN adding Mixin-adress  THEN the new mixin should be listed in the table"()
    {
        given:
        String mixinCFG = MixinAddress.CFG
        ContentType mixin = ContentType.with().name( NameHelper.uniqueName( "adressmixin" ) ).kind(
            KindOfContentTypes.MIXIN ).configuration( mixinCFG ).build();
        getTestSession().put( MIXIN_KEY, mixin );

        when:
        schemaBrowsePanel.clickToolbarNew().selectKind( KindOfContentTypes.MIXIN.getValue() ).typeData( mixin ).save().close()

        then:
        SchemaBrowsePanel grid = new SchemaBrowsePanel( getTestSession() )
        grid.exists( mixin )

    }


    def "GIVEN BrowsePanel and existing Mixin  WHEN Mixin editet, name changed  Then the Mixin whith new name should be listed in the table"()
    {
        given:
        ContentType mixinToEdit = (ContentType) getTestSession().get( MIXIN_KEY );
        ContentType newMixin = mixinToEdit.cloneContentType();
        String newName = NameHelper.uniqueName( "mixinrenamed" );
        newMixin.setName( newName );

        when:
        schemaBrowsePanel.selectRowWithContentType( mixinToEdit.getName(),
                                                    mixinToEdit.getDisplayNameFromConfig() ).clickToolbarEdit().typeData(
            newMixin ).save().close()
        mixinToEdit.setName( newName );

        then:
        schemaBrowsePanel.exists( newMixin );

    }


    def "GIVEN BrowsePanel and existing Mixin  WHEN Mixin editet, display-name changed  Then the Mixin whith new display-name should be listed in the table"()
    {
        given:

        ContentType mixinToEdit = (ContentType) getTestSession().get( MIXIN_KEY );
        ContentType newMixin = mixinToEdit.cloneContentType();
        String newDisplayName = "change display name test";
        // set a new display name:
        newMixin.setDisplayNameInConfig( newDisplayName );

        when:
        schemaBrowsePanel.selectRowWithContentType( mixinToEdit.getName(),
                                                    mixinToEdit.getDisplayNameFromConfig() ).clickToolbarEdit().typeData(
            newMixin ).save().close()
        mixinToEdit.setDisplayNameInConfig( newDisplayName );
        then:
        schemaBrowsePanel.exists( newMixin )
    }

    def "GIVEN BrowsePanel WHEN existing Mixin selected and clicking Delete Then Mixin is removed from list"()

    {
        given:
        ContentType mixinToDelete = (ContentType) getTestSession().get( MIXIN_KEY )

        when:
        schemaBrowsePanel.selectRowWithContentType( mixinToDelete.getName(),
                                                    mixinToDelete.getDisplayNameFromConfig() ).clickToolbarDelete().doDelete()

        then:
        !schemaBrowsePanel.exists( mixinToDelete );
    }
}
