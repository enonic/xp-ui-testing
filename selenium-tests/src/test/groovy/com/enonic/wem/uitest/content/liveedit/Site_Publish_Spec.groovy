package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.SettingsWizardStepForm
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
import com.enonic.autotests.pages.form.liveedit.PartComponentView
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created on 01.11.2016.
 * */
@Stepwise
class Site_Publish_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;

    @Shared
    String TEST_TEXT = "test text";

    def "GIVEN new site is created WHEN site has been published THEN 'Published' status should be displayed in the grid"()
    {
        given: "data typed and saved and the wizard is closed"
        SITE = buildMyFirstAppSite( "site" );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() ).typeData( SITE );
        wizard.selectPageDescriptor( COUNTRY_REGION_PAGE_CONTROLLER ).switchToDefaultWindow();
        wizard.clickOnMarkAsReadyButton();
        wizard.closeBrowserTab().switchToBrowsePanelTab();

        when: "site has been published"
        findAndSelectContent( SITE.getName() ).clickToolbarPublish().includeChildren( true ).clickOnPublishNowButton();
        saveScreenshot( "site_published" );

        then: "'Published' status should be displayed in the grid"
        contentBrowsePanel.getContentStatus( SITE.getName() ) == ContentStatus.PUBLISHED.getValue();
    }

    def "GIVEN existing 'Published' site WHEN a text component has been added THEN status in the wizard-page gets 'Modified'"()
    {
        given: "existing 'Published' site"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();

        when: "new text component has been inserted"
        pageComponentsView.openMenu( "country" ).selectMenuItem( "Insert", "Text" );
        wizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.typeTextInTextComponent( TEST_TEXT );
        wizard.switchToDefaultWindow();
        pageComponentsView.doCloseDialog();

        and: "site has been saved"
        wizard.save();
        wizard.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        then: "status gets 'Modified'"
        wizard.getStatus() == ContentStatus.MODIFIED.getValue();
    }

    def "GIVEN existing 'modified' site  has been published WHEN text component has been removed THEN status in the wizard-page gets 'Modified'"()
    {
        given: "existing 'modified' site  has been published"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        wizard.clickOnMarkAsReadyAndDoPublish();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();

        when: "text component has been removed"
        pageComponentsView.openMenu( TEST_TEXT ).selectMenuItem( "Remove" );
        pageComponentsView.doCloseDialog();

        and: "site has been saved"
        wizard.save();
        sleep( 500 );
        wizard.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        then: "status gets 'Modified'"
        wizard.getStatus() == ContentStatus.MODIFIED.getValue();
    }

    def "GIVEN existing 'Published' site WHEN display name has been updated THEN the site gets 'Modified'"()
    {
        given: "existing 'modified' site has been published"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        wizard.clickOnMarkAsReadyButton();
        wizard.showPublishMenu().clickOnPublishMenuItem().clickOnPublishNowButton();

        when: "display name has been updated"
        wizard.typeDisplayName( "new name" )

        and: "site has been saved"
        wizard.save();
        wizard.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        then: "status gets 'Modified'"
        wizard.getStatus() == ContentStatus.MODIFIED.getValue();
    }

    def "GIVEN existing 'Published' site WHEN language has been updated THEN status gets 'Modified'"()
    {
        given: "existing 'modified' site  has been published"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        wizard.clickOnMarkAsReadyAndDoPublish();
        saveScreenshot( "site_published_in_wizard" );

        when: "language has been changed"
        SettingsWizardStepForm settings = wizard.clickOnSettingsTabLink();
        settings.waitUntilDisplayed();
        settings.selectLanguage( ENGLISH_LANGUAGE );

        and: "site has been saved"
        wizard.save();
        wizard.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        then: "status gets 'Modified'"
        wizard.getStatus() == ContentStatus.MODIFIED.getValue();
    }

    def "GIVEN existing 'Published' site WHEN new part has been inserted THEN status gets 'Modified'"()
    {
        given: "existing 'modified' site has been published"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        wizard.clickOnMarkAsReadyButton();
        wizard.showPublishMenu().clickOnPublishMenuItem().clickOnPublishNowButton();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();

        when: "new part has been inserted"
        pageComponentsView.openMenu( "country" ).selectMenuItem( "Insert", "Part" );
        pageComponentsView.doCloseDialog();
        wizard.switchToLiveEditFrame();
        PartComponentView partComponentView = new PartComponentView( getSession() );
        partComponentView.selectItem( CITY_CREATION_PART );
        saveScreenshot( "site_modified_in_wizard" );

        and: "switch from Live Edit to the wizard page"
        wizard.switchToDefaultWindow();
        wizard.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        then: "status gets 'Modified'"
        wizard.getStatus() == ContentStatus.MODIFIED.getValue();
    }
}
