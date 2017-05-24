package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Stepwise

/**
 * Created on 5/22/2017.
 * Task:
 * xp-ui-testing#51  Add Selenium tests for 'Home button' on the wizard-toolbar
 *
 * Verifies: https://github.com/enonic/xp/issues/4970
 *   404 Error appears when the 'home-button' has been clicked
 * */
@Stepwise
class ContentWizard_HomeButton_Spec
    extends BaseContentSpec
{
    def "GIVEN content wizard is opened EXPECTED 'home button' should be present and clickable"()
    {
        given: "content wizard is opened"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );

        expect: "'home button' should be clickable";
        wizard.isHomeButtonClickable();
    }

    def "GIVEN content wizard is opened WHEN 'home button' has been pressed THEN '404' error should not be present"()
    {
        given: "content wizard is opened"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( ContentTypeName.folder() );

        when: "'home button' has been clicked"
        wizard.clickOnHomeButton();

        then: "tab with 404 should not be present"
        !contentBrowsePanel.is_404_TabWindowPresent();

        and: "two tabs with 'content studio' should be present"
        NavigatorHelper.countWindowTabsByTitle( getSession(), Application.CONTENT_STUDIO_TITLE ) == 2;
    }
}
