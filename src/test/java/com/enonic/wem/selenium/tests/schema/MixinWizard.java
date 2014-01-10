package com.enonic.wem.selenium.tests.schema;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.pages.schemamanager.AddNewContentTypeWizard;
import com.enonic.autotests.pages.schemamanager.KindOfContentTypes;
import com.enonic.autotests.services.ContentTypeService;
import com.enonic.wem.selenium.tests.BaseTest;

public class MixinWizard extends BaseTest
{
	private ContentTypeService contentTypeService = new ContentTypeService();
	
	@Test
	public void open_mixin_wizard_when_typing_very_long_name_then_input_field_width_increases_enough_to_show_full_name()
	{
		String longName25chars = "longnamelongnamelongnam25";
		
		AddNewContentTypeWizard wizard = contentTypeService.openAddContentTypeWizard(getTestSession(), KindOfContentTypes.MIXIN);
		int width25 = wizard.doTypeLongNameAndGetInputWidth(longName25chars);
		
		String longName27chars = "longnamelongnamelongnamqq27";
		int width27 = wizard.doTypeLongNameAndGetInputWidth(longName27chars);
		
		Assert.assertTrue(width27 > width25, "width of input field should be increased if the length of name was increased");

	}
}
