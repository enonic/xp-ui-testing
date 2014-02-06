package com.enonic.wem.uitest.schema.wizards

import com.enonic.autotests.pages.schemamanager.ContentTypeWizardPanel
import com.enonic.autotests.pages.schemamanager.KindOfContentTypes
import com.enonic.autotests.services.ContentTypeService
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared

class MixinWizardSpec extends BaseGebSpec {

	@Shared ContentTypeService contentTypeService = new ContentTypeService();
	
	def "Given schema app and existing mixin, mixin-wizard opened When typing very long name Then input field width increases"(){
		
		given:
		go "admin"
		
		String longName25chars = "longnamelongnamelongnam25";
		ContentTypeWizardPanel wizard = contentTypeService.openAddContentTypeWizard(getTestSession(), KindOfContentTypes.MIXIN);
		int width25 = wizard.doTypeLongNameAndGetInputWidth(longName25chars);
		String longName27chars = "longnamelongnamelongnamqq27";
		
		when:
		int width27 = wizard.doTypeLongNameAndGetInputWidth(longName27chars);
		
		then:
		width27 > width25
	}
}
