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
    String PAGE_TITLE = "Home Page";

    @Shared
    String MACRO_TEXT = "test text";

    @Shared
    String EMBEDDED_IFRAME_CODE_RESULT = "[embed]" + MACRO_TEXT + "[/embed]";

    def "GIVEN creating new Site with configuration and a page-controller WHEN site was saved and wizard closed THEN new site should be present"()
    {
        given:
        SITE = buildSiteWithApps( APP_CONTENT_TYPES_DISPLAY_NAME );

        when: "data was saved and wizard closed"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() )
        wizardPanel.typeData( SITE ).selectPageDescriptor( PAGE_CONTROLLER ).save().closeBrowserTab().switchToBrowsePanelTab();

        then: "new site should be present"
        contentBrowsePanel.exists( SITE.getName() );
    }
    //xp-ui-testing#6 Add selenium tests for allowPath, allowType properties from the ContentSelector
    def "GIVEN site configurator dialog is opened WHEN an image-name was typed but allow type is 'Folder' THEN 'no matching items' message should be displayed"()
    {
        given: "site-configurator dialog is opened"
        findAndSelectContent( SITE.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        SiteConfiguratorDialog configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );

        when: "an image-name has been typed but allow type is 'Folder'"
        configurationDialog.typePostsFilter( IMPORTED_IMAGE_BOOK_DISPLAY_NAME );
        sleep( 700 );
        saveScreenshot( "no_matching_items_allow_type" );

        then: "'no matching items' message should be displayed"
        configurationDialog.isNoMatchingItemsForPostsFolder()
    }

    def "GIVEN site configurator dialog is opened WHEN Link was inserted, and changes applied THEN correct text should be present in HtmlArea"()
    {
        given: "site-configurator dialog is opened"
        findAndSelectContent( SITE.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        SiteConfiguratorDialog configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );

        when: "'Insert Link' dialog is opened and URL inserted"
        InsertLinkModalDialog linkModalDialog = configurationDialog.clickOnHtmlAreaInsertLinkButton();
        sleep( 700 );
        saveScreenshot( "insert-link-dialog" );
        linkModalDialog.clickURLBarItem().typeURL( URL ).typeText( LINK_TEXT ).pressInsertButton();
        and: "all changes were applied"
        configurationDialog.doApply();

        and: "configurationDialog is opened again"
        configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );
        saveScreenshot( "conf-dialog-with-url" );

        then: "correct text should be present in the HtmlArea"
        configurationDialog.getTextFromArea().contains( URL );
    }

    def "GIVEN existing site is opened WHEN preview button was pressed THEN correct links should be present in page-source"()
    {
        given: "existing site is opened"
        ContentWizardPanel contentWizard = findAndSelectContent( SITE.getName() ).clickToolbarEditAndSwitchToWizardTab();

        when: "preview button was pressed"
        contentWizard.clickToolbarPreview();

        then: "page source of new opened tab in a browser is not empty"
        String source = TestUtils.getPageSource( getSession(), PAGE_TITLE );
        source != null;

        and: "correct text for the link should be displayed"
        source.contains( LINK_TEXT );
        and: "correct URL should be present"
        source.contains( URL )
    }

    def "GIVEN site configurator dialog is opened WHEN 'Insert Link' dialog is opened AND folder selected as target THEN correct text should be present in HtmlArea"()
    {
        given: "site configurator dialog is opened"
        findAndSelectContent( SITE.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        and: "site-configurator dialog is opened"
        SiteConfiguratorDialog configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );

        when: "'Insert Link' dialog is opened"
        InsertLinkModalDialog linkModalDialog = configurationDialog.clickOnHtmlAreaInsertLinkButton();
        sleep( 700 );
        and: "select the 'Content' bar item AND select a folder in the 'target' "
        linkModalDialog.clickContentBarItem().selectOption( IMPORTED_IMAGE_NORD_NAME ).typeText(
            CONTENT_TEXT ).pressInsertButton().waitForDialogClosed(); ;
        saveScreenshot( "conf-dialog-content" );
        configurationDialog.doApply();

        and: "configurationDialog is opened again"
        configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );
        saveScreenshot( "conf-dialog-with-content" );

        then: "correct text should be present in HtmlArea"
        configurationDialog.getTextFromArea().contains( CONTENT_TEXT );
    }

    def "GIVEN site configurator dialog opened WHEN Download-resource selected, and changes applied THEN correct text should be present in HtmlArea"()
    {
        given: "site configurator dialog is opened"
        findAndSelectContent( SITE.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        and: "'Site Configurator' dialog is opened"
        SiteConfiguratorDialog configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );

        when: "InsertLinkModalDialog was opened"
        InsertLinkModalDialog linkModalDialog = configurationDialog.clickOnHtmlAreaInsertLinkButton();
        sleep( 700 );
        and: "'download' bar-item has been pressed AND data typed AND 'Insert' button pressed"
        linkModalDialog.clickDownloadBarItem().selectOption( WHALE_IMAGE_DISPLAY_NAME ).typeText(
            DOWNLOAD_TEXT ).pressInsertButton().waitForDialogClosed();
        saveScreenshot( "conf-dialog-download" );
        and: "'Apply' button on the SiteConfiguratorDialog has been pressed"
        configurationDialog.doApply();

        and: "and the configurationDialog is opened again"
        configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );

        then: "correct text present should be displayed in the HtmlArea"
        configurationDialog.getTextFromArea().contains( DOWNLOAD_TEXT );
    }

    def "GIVEN site configurator dialog is opened WHEN Email-link inserted, and changes applied THEN correct text should be present in HtmlArea"()
    {
        given: "site configurator dialog is opened"
        findAndSelectContent( SITE.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        SiteConfiguratorDialog configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );

        when: "InsertLinkModalDialog was opened"
        InsertLinkModalDialog linkModalDialog = configurationDialog.clickOnHtmlAreaInsertLinkButton();
        sleep( 700 );
        saveScreenshot( "conf-dialog-email" );
        and: "download email bar item has been pressed AND data typed AND 'Insert' button pressed"
        linkModalDialog.clickEmailBarItem().typeEmail( EMAIL ).typeSubject( EMAIL_TEXT ).typeText( "send to" ).pressInsertButton();
        and: "'Apply' button on the SiteConfiguratorDialog has been pressed"
        configurationDialog.doApply();

        and: "the configurationDialog is opened again"
        configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );
        saveScreenshot( "conf-dialog-email-inserted" );

        then: "correct text should be present in HtmlArea"
        configurationDialog.getTextFromArea().contains( EMAIL_TEXT );
    }

    def "GIVEN site configurator dialog is opened WHEN Image for the background was selected and changes applied THEN correct image file should be present in a page-source"()
    {
        given: "site configurator dialog is opened"
        ContentWizardPanel contentWizard = findAndSelectContent( SITE.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        SiteConfiguratorDialog configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );

        when: "Image for the background is selected and changes applied"
        configurationDialog.selectBackGroundImage( IMPORTED_MAN2_IMAGE_DISPLAY_NAME ).doApply();
        sleep( 700 );
        contentWizard.clickToolbarPreview();
        String source = TestUtils.getPageSource( getSession(), PAGE_TITLE );

        then: "correct background should be present in a page-source"
        source.contains( backgroundPart );
    }

    def "GIVEN site configurator dialog is opened WHEN 'embedded iframe' macro inserted, and changes applied THEN correct text should be present in page sources"()
    {
        given: "site configurator dialog is opened"
        ContentWizardPanel contentWizard = findAndSelectContent( SITE.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        SiteConfiguratorDialog configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );

        and: "'embedded iframe' macro was inserted, and changes applied"
        MacroModalDialog macroModalDialog = configurationDialog.showToolbarAndClickOnInsertMacroButton();
        sleep( 500 );
        saveScreenshot( "site_conf_dialog_macro" );
        PropertyTree data = new PropertyTree();
        data.addString( TextAreaConfigPanel.TEXT_AREA_VALUE, EMBEDDED_IFRAME_CODE_RESULT );
        macroModalDialog.selectOption( MacroType.EMBED_IFRAME ).getMacroConfigPanel().typeData( data );
        macroModalDialog.clickInsertButton();
        configurationDialog.doApply();

        when: "preview button has been pressed"
        contentWizard.clickToolbarPreview();
        String source = TestUtils.getPageSource( getSession(), PAGE_TITLE );
        saveScreenshot( "site_config_preview" );

        then: "page source of new opened tab in a browser is not empty"
        source != null;

        and: "'iframe' text should be present in the sources"
        source.contains( "iframe" );
    }
}
