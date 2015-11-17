package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.SiteConfiguratorDialog
import com.enonic.autotests.pages.form.SiteFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class SiteConfiguratorDialog_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;

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
}
