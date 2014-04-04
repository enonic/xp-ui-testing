package com.enonic.wem.uitest.schema.browsepanel

import com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.SchemaCfgHelper
import com.enonic.autotests.vo.schemamanger.Mixin
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.wem.uitest.schema.cfg.MixinAddress
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class MixinSpec
    extends BaseGebSpec
{
    @Shared
    String MIXIN_KEY = "mixin";

    @Shared
    SchemaBrowsePanel schemaBrowsePanel;

    def setup()
    {
        go "admin"
        schemaBrowsePanel = NavigatorHelper.openSchemaManager( getTestSession() );
    }


    def "GIVEN BrowsePanel WHEN adding Mixin-address  THEN the new mixin should be listed in the table"()
    {
        given:
        String mixinCFG = MixinAddress.CFG
        Mixin mixin = Mixin.newMixin().name( NameHelper.uniqueName( "addressmixin" ) ).configData( mixinCFG ).build();
        getTestSession().put( MIXIN_KEY, mixin );

        when:
        schemaBrowsePanel.clickToolbarNew().selectKind( mixin.getSchemaKindUI().getValue() ).typeData( mixin ).save().close();

        then:
        schemaBrowsePanel.exists( mixin );

    }


    def "GIVEN BrowsePanel and existing Mixin  WHEN Mixin edited, name changed  Then the Mixin with new name should be listed in the table"()
    {
        given:
        Mixin mixinToEdit = (Mixin) getTestSession().get( MIXIN_KEY );
        Mixin newMixin = cloneMixinWithNewName( mixinToEdit );

        when:
        schemaBrowsePanel.selectRowWithContentType( mixinToEdit.getName(), mixinToEdit.getDisplayNameFromConfig() ).
            clickToolbarEdit().typeData( newMixin ).save().close();
        getTestSession().put( MIXIN_KEY, newMixin );

        then:
        schemaBrowsePanel.exists( newMixin );

    }


    def "GIVEN BrowsePanel and existing Mixin  WHEN Mixin edited, display-name changed  Then the Mixin with new display-name should be listed in the table"()
    {
        given:

        Mixin mixinToEdit = (Mixin) getTestSession().get( MIXIN_KEY );
        Mixin newMixin = cloneMixinWithNewDisplayName( mixinToEdit );

        when:
        schemaBrowsePanel.selectRowWithContentType( mixinToEdit.getName(), mixinToEdit.getDisplayNameFromConfig() ).
            clickToolbarEdit().typeData( newMixin ).save().close();
        getTestSession().put( MIXIN_KEY, newMixin );

        then:
        schemaBrowsePanel.exists( newMixin );
    }

    def "GIVEN BrowsePanel and existing Mixin WHEN Mixin selected and clicking Delete Then Mixin is removed from list"()

    {
        given:
        Mixin mixinToDelete = (Mixin) getTestSession().get( MIXIN_KEY );

        when:
        schemaBrowsePanel.selectRowWithContentType( mixinToDelete.getName(),
                                                    mixinToDelete.getDisplayNameFromConfig() ).clickToolbarDelete().doDelete();

        then:
        !schemaBrowsePanel.exists( mixinToDelete );
    }

    Mixin cloneMixinWithNewDisplayName( Mixin source )
    {
        String newDisplayName = NameHelper.uniqueName( "newdisplayname" );
        String newconfigData = SchemaCfgHelper.changeDisplayName( newDisplayName, source.getConfigData() );
        return Mixin.newMixin().name( source.getName() ).configData( newconfigData ).build();
    }

    Mixin cloneMixinWithNewName( Mixin source )
    {
        String newName = NameHelper.uniqueName( "newname" );
        return Mixin.newMixin().name( newName ).configData( source.getConfigData() ).build();
    }
}
