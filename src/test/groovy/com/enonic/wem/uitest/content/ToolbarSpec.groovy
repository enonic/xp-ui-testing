package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentGridPage;
import com.enonic.autotests.services.NavigatorHelper;
import com.enonic.wem.uitest.BaseGebSpec;

class ToolbarSpec extends BaseGebSpec {
	
	def "Given Content BrowsePanel When no selected content Then Delete bytton should be disabled"() 
	{
		given:
		go "admin"

		when:
		ContentGridPage cmPage = NavigatorHelper.openContentApp(getTestSession());

		then:
		!cmPage.isDeleteButtonEnabled();
	}
}
