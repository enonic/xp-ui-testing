package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.SiteFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class SiteFormViewPanel_Applications_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;


    def "GIVEN creating new Site with two configurations  WHEN site saved and wizard closed THEN new site should be present"()
    {
        given:
        SITE = buildSiteWithApps( APP_NAME_1, APP_NAME_2 );
        when: "data saved and wizard closed"
        contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() ).typeData( SITE ).save().close(
            SITE.getDisplayName() );

        then: "new site should be present"
        contentBrowsePanel.exists( SITE.getName() );
    }

    def "GIVEN site with two applications WHEN reordering of applications applied AND site saved THEN new order of applications present in form-panel"()
    {
        given: "site opened opened "
        filterPanel.typeSearchText( SITE.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        LinkedList<String> namesBefore = formViewPanel.getAppDisplayNames();

        when: "reordering of applications applied, site saved and opened again"
        formViewPanel.swapApplications( APP_NAME_1, APP_NAME_2 );
        wizard.save().close( SITE.getDisplayName() );
        contentBrowsePanel.clickToolbarEdit();
        LinkedList<String> namesAfter = formViewPanel.getAppDisplayNames();

        then: "new order of applications present in form-panel"
        namesBefore.getFirst() == namesAfter.getLast();
    }
}
