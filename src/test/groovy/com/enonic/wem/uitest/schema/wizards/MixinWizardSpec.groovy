package com.enonic.wem.uitest.schema.wizards

import com.enonic.autotests.pages.schemamanager.ContentTypeWizardPanel
import com.enonic.autotests.pages.schemamanager.KindOfContentTypes
import com.enonic.wem.uitest.BaseGebSpec

class MixinWizardSpec extends BaseGebSpec
{

    def "Given schema app, mixin-wizard opened When typing very long name Then input field width increases"( )
    {
        given:
        go "admin"

        String longName25chars = "longnamelongnamelongnam25";
        ContentTypeWizardPanel wizard = contentTypeService.openAddContentTypeWizard( getTestSession(), KindOfContentTypes.MIXIN );
        wizard.doTypeName( longName25chars );
        int width25 = wizard.getInputNameWidth();
        String longName27chars = "longnamelongnamelongnamqq27";

        when:
        wizard.doTypeName( longName27chars );

        then:
        wizard.getInputNameWidth() > width25
    }

    def "Given schema app, mixin-wizard opened When typing a name Then name and title in AppBarTabMenu updated concurrently"( )
    {
        given:
        go "admin"
        String mixinName = "test";
        ContentTypeWizardPanel wizard = contentTypeService.openAddContentTypeWizard( getTestSession(), KindOfContentTypes.MIXIN );
        wizard.doTypeName( mixinName );
        String actualTitle = wizard.getAppBarTabMenuTitle();


        when:
        String mixinNameUpdated = "test" + 5;
        wizard.doTypeName( mixinNameUpdated );
        String newActualTitle = wizard.getAppBarTabMenuTitle();

        then:
        mixinName.equals( actualTitle ) && mixinNameUpdated.equals( newActualTitle )
    }
}
