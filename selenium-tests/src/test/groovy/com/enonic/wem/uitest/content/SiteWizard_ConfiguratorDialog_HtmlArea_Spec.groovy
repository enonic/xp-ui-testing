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
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
@Ignore
class SiteWizard_ConfiguratorDialog_HtmlArea_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;

    @Shared
    String URL_LINK = "https://test-link.com";

    @Shared
    String LINK_TEXT = "link-text";

    @Shared
    String CONTENT_TEXT = "content-text";

    @Shared
    String EMAIL_TEXT = "email-text";

    @Shared
    String PAGE_CONTROLLER = "Page";

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

    def "Preconditions: site with controller should be added"()
    {
        given:
        SITE = buildSiteWithApps( APP_CONTENT_TYPES_DISPLAY_NAME );

        when: "data was saved and wizard closed"
        ContentWizardPanel wizardPanel = contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() )
        wizardPanel.typeData( SITE ).selectPageDescriptor( PAGE_CONTROLLER ).closeBrowserTab().switchToBrowsePanelTab();

        then: "new site should be present"
        contentBrowsePanel.getFilterPanel().typeSearchText( SITE.getName() );
        contentBrowsePanel.exists( SITE.getName() );
    }

    def "GIVEN site configurator dialog is opened AND allow type is 'Folder' WHEN an image's name has been typed THEN 'no matching items' message should be displayed"()
    {
        given: "site-configurator dialog is opened"
        findAndSelectContent( SITE.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        SiteConfiguratorDialog configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );

        when: "an image-name has been typed in post options filter input"
        configurationDialog.typePostsFilter( IMPORTED_IMAGE_BOOK_DISPLAY_NAME );
        sleep( 700 );
        saveScreenshot( "no_matching_items_allow_type" );

        then: "'no matching items' message should be displayed"
        configurationDialog.isNoMatchingItemsForPostsFolder()
    }

    def "GIVEN site configurator dialog is opened WHEN Link has been inserted THEN expected text should be present in HtmlArea"()
    {
        given: "site-configurator dialog is opened"
        findAndSelectContent( SITE.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        SiteConfiguratorDialog configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );

        when: "'Insert Link' dialog is opened and URL inserted"
        InsertLinkModalDialog linkModalDialog = configurationDialog.clickOnHtmlAreaInsertLinkButton();
        sleep( 700 );
        saveScreenshot( "insert-link-dialog" );
        linkModalDialog.clickURLBarItem().typeURL( URL_LINK ).typeText( LINK_TEXT ).pressInsertButton();
        and: "all changes were applied"
        configurationDialog.doApply();

        and: "configurationDialog is opened again"
        configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );
        saveScreenshot( "conf-dialog-with-url" );
        String actualText = configurationDialog.getTextFromCKE();

        then: "expected text should be present in the HtmlArea"
        actualText.contains( URL_LINK );
    }

    def "GIVEN existing site is opened WHEN preview button has been pressed THEN correct links should be present in page-source"()
    {
        given: "existing site is opened"
        ContentWizardPanel contentWizard = findAndSelectContent( SITE.getName() ).clickToolbarEditAndSwitchToWizardTab();

        when: "preview button has been pressed"
        contentWizard.clickToolbarPreview();

        then: "page source of new opened tab in a browser is not empty"
        String source = TestUtils.getPageSource( getSession(), "Home Page" );
        source != null;

        and: "correct text for the link should be displayed"
        source.contains( LINK_TEXT );
        and: "correct URL should be present"
        source.contains( URL_LINK )
    }

    def "WHEN try to select external resource in the site configurator THEN 'No matching items' message should appear, because the dialog is limited to current site content"()
    {
        given: "site configurator dialog is opened"
        findAndSelectContent( SITE.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        and: "'Site Configurator' dialog is opened"
        SiteConfiguratorDialog configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );

        and: "InsertLinkModalDialog is opened"
        InsertLinkModalDialog linkModalDialog = configurationDialog.clickOnHtmlAreaInsertLinkButton();
        sleep( 700 );
        when: "try to select external resource in 'Content' tab"
        linkModalDialog.clickContentBarItem().doFilterComboBoxOption( WHALE_IMAGE_DISPLAY_NAME );
        saveScreenshot( "conf-dialog-content-external" );
        then: "'No matching items' message should appear, because the dialog is limited to current site content"
        linkModalDialog.isNoMatchingItemsInComboBox();
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
        and: "select the 'Content' bar item AND select a child folder in the 'target' "
        linkModalDialog.clickContentBarItem().selectComboBoxOption( "Templates" ).typeText(
            CONTENT_TEXT ).pressInsertButton().waitForDialogClosed(); ;
        saveScreenshot( "conf-dialog-content" );
        configurationDialog.doApply();

        and: "configurationDialog is opened again"
        configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );
        saveScreenshot( "conf-dialog-with-content" );

        then: "correct text should be present in HtmlArea"
        configurationDialog.getTextFromCKE().contains( CONTENT_TEXT );
    }

    //Content selector on the Downloads tab of the Insert Link dialog should not be limited to current site #2731
    def "WHEN try to select external resource in Download tab THEN 'No matching items' message should appear, because the dialog is limited to current site content"()
    {
        given: "site configurator dialog is opened"
        findAndSelectContent( SITE.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        and: "'Site Configurator' dialog is opened"
        SiteConfiguratorDialog configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );

        and: "InsertLinkModalDialog is opened"
        InsertLinkModalDialog linkModalDialog = configurationDialog.clickOnHtmlAreaInsertLinkButton();
        linkModalDialog.clickDownloadBarItem();
        sleep( 500 );
        when: "try to select external resource in Download tab"
        linkModalDialog.selectComboBoxOption( WHALE_IMAGE_DISPLAY_NAME );
        sleep( 500 );
        saveScreenshot( "conf-dialog-download" );
        then: "expected content should be in selected option view, because Download tab is not limited to current site content"
        linkModalDialog.getSelectedComboBoxOption() == WHALE_IMAGE_DISPLAY_NAME;
    }

    def "GIVEN site configurator dialog is opened WHEN Email-link inserted THEN expected text should be present in HtmlArea"()
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
        configurationDialog.getTextFromCKE().contains( EMAIL_TEXT );
    }

    def "GIVEN site configurator dialog is opened WHEN Image for site background has been selected THEN expected image should be present in the page-sources"()
    {
        given: "site configurator dialog is opened"
        ContentWizardPanel contentWizard = findAndSelectContent( SITE.getName() ).clickToolbarEditAndSwitchToWizardTab();
        SiteFormViewPanel formViewPanel = new SiteFormViewPanel( getSession() );
        SiteConfiguratorDialog configurationDialog = formViewPanel.openSiteConfigurationDialog( APP_CONTENT_TYPES_DISPLAY_NAME );

        when: "Image for the background has been selected"
        configurationDialog.selectBackGroundImage( IMPORTED_MAN2_IMAGE_DISPLAY_NAME ).doApply();
        sleep( 700 );
        contentWizard.clickToolbarPreview();
        String source = TestUtils.getPageSource( getSession(), PAGE_TITLE );

        then: "correct background should be present in the page-source"
        source.contains( backgroundPart );
    }

    def "GIVEN site configurator dialog is opened WHEN 'embedded iframe' macro inserted THEN expected text should be present in page sources"()
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
