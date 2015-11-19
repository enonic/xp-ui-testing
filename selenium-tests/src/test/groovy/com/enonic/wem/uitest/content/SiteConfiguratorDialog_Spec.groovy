package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.SiteConfiguratorDialog
import com.enonic.autotests.pages.form.SiteFormViewPanel
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class SiteConfiguratorDialog_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;

    @Shared
    String BACKGROUND_COLOR = "Red"

    @Shared
    String BACKGROUND_RED_COLOR_VALUE = "213, 147, 146"

    def "GIVEN creating new Site with configuration  WHEN site saved and wizard closed THEN new site should be present"()
    {
        given:
        SITE = buildSiteWithApps( SIMPLE_SITE_APP, MY_FIRST_APP );
        when: "data saved and wizard closed"
        contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() ).typeData( SITE ).save().close(
            SITE.getDisplayName() );

        then: "new site should be present"
        contentBrowsePanel.exists( SITE.getName() );
    }

    def "GIVEN just added site opened WHEN the 'edit-button' on the selected application clicked THEN configurator dialog with correct title appears"()
    {
        given: "site opened"
        filterPanel.typeSearchText( SITE.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );

        when: "edit button clicked"
        SiteConfiguratorDialog dialog = formViewPanel.openSiteConfiguration( SIMPLE_SITE_APP );

        then: "dialog is not null"
        dialog != null;

        and: "dialog opened"
        dialog.isOpened();

        and: "correct title is displayed"
        dialog.getTitle() == SIMPLE_SITE_APP;
    }

    def "GIVEN configurator dialog opened WHEN red color for background selected THEN correct background color present in the page-editor"()
    {
        given: "site opened and configurator dialog opened"
        filterPanel.typeSearchText( SITE.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        wizard.showPageEditor();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        SiteConfiguratorDialog dialog = formViewPanel.openSiteConfiguration( SIMPLE_SITE_APP );

        when: "ed color for background selected"
        dialog.selectBackgroundColor( BACKGROUND_COLOR )
        TestUtils.saveScreenshot( getSession(), "page-background-selected" );
        dialog.doApply();
        TestUtils.saveScreenshot( getSession(), "page-background-applied" );

        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        NavigatorHelper.switchToLiveEditFrame( getSession() );

        then: "correct background color present in the page-editor"
        liveFormPanel.getBackgroundColor().contains( BACKGROUND_RED_COLOR_VALUE );
    }
}
