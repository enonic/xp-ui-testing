package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.InsertLinkModalDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.SiteConfiguratorDialog
import com.enonic.autotests.pages.form.SiteFormViewPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class SiteWizard_ConfiguratorDialog_HtmlArea_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;

    @Shared
    String URL = "http://test-site.com";

    @Shared
    String LINK_TEXT = "link-text";

    @Shared
    String CONTENT_TEXT = "content-text";

    @Shared
    String PAGE_CONTROLLER = "Page"

    def "GIVEN creating new Site with configuration and a page-controller WHEN site saved and wizard closed THEN new site should be present"()
    {
        given:
        SITE = buildSiteWithApps( CONTENT_TYPES_NAME_APP );
        when: "data saved and wizard closed"
        contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() ).typeData(
            SITE ).showPageEditor().selectPageDescriptor( PAGE_CONTROLLER ).save().close( SITE.getDisplayName() );

        then: "new site should be present"
        contentBrowsePanel.exists( SITE.getName() );
    }

    def "GIVEN site configurator dialog opened WHEN URL inserted, and changes applied THEN correct text present in HtmlArea"()
    {
        given: "site opened"
        filterPanel.typeSearchText( SITE.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        SiteConfiguratorDialog configurationDialog = formViewPanel.openSiteConfiguration( CONTENT_TYPES_NAME_APP );

        when: "URL inserted, and changes applied"
        InsertLinkModalDialog linkModalDialog = configurationDialog.clickOnHtmlAreaInsertLinkButton();
        sleep( 1000 );
        linkModalDialog.clickURLBarItem().typeURL( URL ).typeText( LINK_TEXT ).pressInsertButton();
        configurationDialog.doApply();
        and: "and configurationDialog opened again"
        configurationDialog = formViewPanel.openSiteConfiguration( CONTENT_TYPES_NAME_APP );

        then: "correct text present in HtmlArea"
        configurationDialog.getTextFromArea().contains( URL );
    }

    def "GIVEN existing site with configured links WHEN preview of site opened THEN correct links present in page-source"()
    {
        given: "existing site with configured links"
        filterPanel.typeSearchText( SITE.getName() );
        ContentWizardPanel contentWizard = contentBrowsePanel.selectContentInTable( SITE.getName() ).clickToolbarEdit();

        when: "preview button pressed"
        contentWizard.clickToolbarPreview();

        then: "correct links present in a page-source"
        String source = TestUtils.getPageSource( getSession(), "Superhero theme" );

        and: "correct text for URL present"
        source.contains( LINK_TEXT );
        and: "correct URL present"
        source.contains( URL )
    }

    def "GIVEN site configurator dialog opened WHEN Content selected, and changes applied THEN correct text present in HtmlArea"()
    {
        given: "site opened"
        filterPanel.typeSearchText( SITE.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        SiteConfiguratorDialog configurationDialog = formViewPanel.openSiteConfiguration( CONTENT_TYPES_NAME_APP );

        when: "Content selected, and changes applied"
        InsertLinkModalDialog linkModalDialog = configurationDialog.clickOnHtmlAreaInsertLinkButton();
        sleep( 1000 );
        linkModalDialog.clickContentBarItem().selectOption( "nord.jpg" ).typeText( CONTENT_TEXT ).pressInsertButton();
        configurationDialog.doApply();
        and: "and configurationDialog opened again"
        configurationDialog = formViewPanel.openSiteConfiguration( CONTENT_TYPES_NAME_APP );

        then: "correct text present in HtmlArea"
        configurationDialog.getTextFromArea().contains( CONTENT_TEXT );
    }
}
