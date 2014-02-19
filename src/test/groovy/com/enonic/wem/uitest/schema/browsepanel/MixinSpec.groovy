package com.enonic.wem.uitest.schema.browsepanel

import spock.lang.Shared
import spock.lang.Stepwise
import com.enonic.autotests.pages.schemamanager.KindOfContentTypes
import com.enonic.autotests.pages.schemamanager.SchemaBrowsePanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.vo.schemamanger.ContentType
import com.enonic.wem.uitest.BaseGebSpec
import com.enonic.wem.uitest.schema.cfg.MixinAddress

@Stepwise
class MixinSpec extends BaseGebSpec
{
    @Shared String MIXIN_KEY = "mixin"

    def "GIVEN BrowsePanel WHEN adding Mixin-adress  THEN the new mixin should be listed in the table"( )
    {
        given:
        go "admin"
        String mixinCFG = MixinAddress.CFG
        ContentType mixin = ContentType.with().name( "adressmixin" ).kind( KindOfContentTypes.MIXIN ).configuration( mixinCFG ).build();
        getTestSession().put( MIXIN_KEY, mixin );

        when:
        contentTypeService.createContentType( getTestSession(), mixin, true )

        then:
        SchemaBrowsePanel grid = new SchemaBrowsePanel( getTestSession() )
        grid.isContentTypePresentInTable( mixin )

    }


    def "GIVEN BrowsePanel and existing Mixin  WHEN Mixin editet, name changed  Then the Mixin whith new name should be listed in the table"( )
    {
        given:
        go "admin"

        ContentType ct = (ContentType) getTestSession().get( MIXIN_KEY );
        ContentType newMixin = ct.cloneContentType();
        String newName = NameHelper.unqiueName( "mixinrenamed" );
        newMixin.setName( newName );

        when:
        SchemaBrowsePanel schemasPage = contentTypeService.editContentType( getTestSession(), ct, newMixin );
        ct.setName( newName );

        then:
        schemasPage.isContentTypePresentInTable( newMixin );

    }


    def "GIVEN BrowsePanel and existing Mixin  WHEN Mixin editet, display-name changed  Then the Mixin whith new display-name should be listed in the table"( )
    {
        given:
        go "admin"

        ContentType ct = (ContentType) getTestSession().get( MIXIN_KEY );
        ContentType newMixin = ct.cloneContentType();
        String newDisplayName = "change display name test";
        // set a new display name:
        newMixin.setDisplayNameInConfig( newDisplayName );

        when:
        SchemaBrowsePanel schemasPage = contentTypeService.editContentType( getTestSession(), ct, newMixin )
        ct.setDisplayNameInConfig( newDisplayName );
        then:
        schemasPage.isContentTypePresentInTable( newMixin )
    }

    def "GIVEN BrowsePanel WHEN existing Mixin selected and clicking Delete Then Mixin is removed from list"( )

    {
        given:
        go "admin"
        ContentType mixinToDelete = (ContentType) getTestSession().get( MIXIN_KEY )

        when:
        SchemaBrowsePanel schemasPage = contentTypeService.deleteContentType( getTestSession(), mixinToDelete );

        then:
        !schemasPage.isContentTypePresentInTable( mixinToDelete );
    }
}
