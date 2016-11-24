package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.InsertLinkModalDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.SiteConfiguratorDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.MacroModalDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.MacroType
import com.enonic.autotests.pages.contentmanager.wizardpanel.macro.TextAreaConfigPanel
import com.enonic.autotests.pages.form.SiteFormViewPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.data.PropertyTree
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
    String DOWNLOAD_TEXT = "download-text";

    @Shared
    String EMAIL_TEXT = "email-text";

    @Shared
    String PAGE_CONTROLLER = "Page"

    @Shared
    String EMAIL = "user1@gmail.com";

    @Shared
    String backgroundPart = "man2.jpg.jpeg"

    @Shared
    String IMAGE_NAME = "man2.jpg";

    @Shared
    String PAGE_TITLE = "Home Page";

    @Shared
    String MACRO_TEXT = "test text";

    @Shared
    String EMBEDDED_IFRAME_CODE_RESULT = "[embed]" + MACRO_TEXT + "[/embed]";

    def "GIVEN creating new Site with configuration and a page-controller WHEN site saved and wizard closed THEN new site should be present"()
    {
        given:
        SITE = buildSiteWithApps( APP_CONTENT_TYPES_DISPLAY_NAME );

        when: "data saved and wizard closed"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() )
        wizardPanel.typeData( SITE ).selectPageDescriptor( PAGE_CONTROLLER ).save().close( SITE.getDisplayName() );

        then: "new site should be present"
        contentBrowsePanel.exists( SITE.getName() );
    }

    def "GIVEN site configurator dialog opened WHEN URL inserted, and changes applied THEN correct text present in HtmlArea"()
    {
        given: "site opened"
        filterPanel.typeSearchText( SITE.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        SiteConfiguratorDialog configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );

        when: "URL inserted, and changes applied"
        InsertLinkModalDialog linkModalDialog = configurationDialog.clickOnHtmlAreaInsertLinkButton();
        sleep( 700 );
        TestUtils.saveScreenshot( getSession(), "insert-link-dialog" );
        linkModalDialog.clickURLBarItem().typeURL( URL ).typeText( LINK_TEXT ).pressInsertButton();
        configurationDialog.doApply();

        and: "and configurationDialog opened again"
        configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );
        saveScreenshot( "conf-dialog-with-url" );

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

        then: "page source of new opened tab in a browser is not empty"
        String source = TestUtils.getPageSource( getSession(), PAGE_TITLE );
        source != null;

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
        SiteConfiguratorDialog configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );

        when: "Content selected, and changes applied"
        InsertLinkModalDialog linkModalDialog = configurationDialog.clickOnHtmlAreaInsertLinkButton();
        sleep( 700 );
        linkModalDialog.clickContentBarItem().selectOption( "nord.jpg" ).typeText( CONTENT_TEXT ).pressInsertButton();
        saveScreenshot( "conf-dialog-content" );
        configurationDialog.doApply();

        and: "and configurationDialog opened again"
        configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );
        saveScreenshot( "conf-dialog-with-content" );

        then: "correct text present in HtmlArea"
        configurationDialog.getTextFromArea().contains( CONTENT_TEXT );
    }

    def "GIVEN site configurator dialog opened WHEN Download-resource selected, and changes applied THEN correct text present in HtmlArea"()
    {
        given: "site opened"
        filterPanel.typeSearchText( SITE.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        SiteConfiguratorDialog configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );

        when: "Content selected, and changes applied"
        InsertLinkModalDialog linkModalDialog = configurationDialog.clickOnHtmlAreaInsertLinkButton();
        sleep( 700 );
        linkModalDialog.clickDownloadBarItem().selectOption( "nord.jpg" ).typeText( DOWNLOAD_TEXT ).pressInsertButton();
        saveScreenshot( "conf-dialog-download" );
        configurationDialog.doApply();

        and: "and configurationDialog opened again"
        configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );

        then: "correct text present in HtmlArea"
        configurationDialog.getTextFromArea().contains( DOWNLOAD_TEXT );
    }

    def "GIVEN site configurator dialog opened WHEN Email-link inserted, and changes applied THEN correct text present in HtmlArea"()
    {
        given: "site opened"
        filterPanel.typeSearchText( SITE.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        SiteConfiguratorDialog configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );

        when: "Content selected, and changes applied"
        InsertLinkModalDialog linkModalDialog = configurationDialog.clickOnHtmlAreaInsertLinkButton();
        sleep( 700 );
        saveScreenshot( "conf-dialog-email" );
        linkModalDialog.clickEmailBarItem().typeEmail( EMAIL ).typeSubject( EMAIL_TEXT ).typeText( "send to" ).pressInsertButton();
        configurationDialog.doApply();

        and: "and configurationDialog opened again"
        configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );
        saveScreenshot( "conf-dialog-email-inserted" );

        then: "correct text present in HtmlArea"
        configurationDialog.getTextFromArea().contains( EMAIL_TEXT );
    }

    def "GIVEN site configurator dialog opened WHEN Background image selected THEN correct image file present in a page-source"()
    {
        given: "site opened"
        filterPanel.typeSearchText( SITE.getName() );
        ContentWizardPanel contentWizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        SiteConfiguratorDialog configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );

        when: "background selected and changes applied"
        configurationDialog.selectBackGroundImage( IMAGE_NAME ).doApply();
        sleep( 700 );
        contentWizard.clickToolbarPreview();
        String source = TestUtils.getPageSource( getSession(), PAGE_TITLE );

        then: "correct background present in a page-source"
        source.contains( backgroundPart );
    }

    def "GIVEN site configurator dialog opened WHEN 'embedded iframe' macro inserted, and changes applied THEN correct text present in page sources"()
    {
        given: "site opened"
        filterPanel.typeSearchText( SITE.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        SiteConfiguratorDialog configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );

        and: "'embedded iframe' macro inserted, and changes applied"
        MacroModalDialog macroModalDialog = configurationDialog.showToolbarAndClickOnInsertMacroButton();
        sleep( 500 );
        saveScreenshot( "site_conf_dialog_macro" );
        PropertyTree data = new PropertyTree();
        data.addString( TextAreaConfigPanel.TEXT_AREA_VALUE, EMBEDDED_IFRAME_CODE_RESULT );
        macroModalDialog.selectOption( MacroType.EMBED_IFRAME ).getMacroConfigPanel().typeData( data );
        macroModalDialog.clickInsertButton();
        configurationDialog.doApply();

        when: "preview button pressed"
        wizard.clickToolbarPreview();
        String source = TestUtils.getPageSource( getSession(), PAGE_TITLE );
        saveScreenshot( "site_config_preview" );

        then: "page source of new opened tab in a browser is not empty"
        source != null;

        and: "iframe text is present in the sources"
        source.contains( "iframe" );
    }
}
