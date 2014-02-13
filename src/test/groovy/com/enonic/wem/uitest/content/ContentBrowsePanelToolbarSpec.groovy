package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.wem.uitest.BaseGebSpec

class ContentBrowsePanelToolbarSpec
    extends BaseGebSpec
{
	
	def "Given Content BrowsePanel When no selected content Then Delete bytton should be disabled"() 
	{
		given:
		go "admin"

		when:
		ContentBrowsePanel cmPage = NavigatorHelper.openContentApp(getTestSession());

		then:
		!cmPage.isDeleteButtonEnabled();
	}
}
