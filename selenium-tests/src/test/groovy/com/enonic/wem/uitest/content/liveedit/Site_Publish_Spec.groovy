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
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created on 01.11.2016.
 * Task "XP-4368 Add tests to verify the XP-4367'
 * verifies bug XP-4367 Wizard - Status/Publish button state are not updated after modifying a published content
 * */
@Stepwise
class Site_Publish_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;

    @Shared
    String TEST_TEXT = "test text";

    def "GIVEN creating of the new Site with a controller WHEN site has been published THEN 'Online' status should be displayed in the grid"()
    {
        given: "data typed and saved and wizard closed"
        SITE = buildMyFirstAppSite( "site" );
        contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() ).typeData( SITE ).selectPageDescriptor(
            COUNTRY_REGION_PAGE_CONTROLLER ).save().closeBrowserTab().switchToBrowsePanelTab();

        when: "site has been published"
        findAndSelectContent( SITE.getName() ).clickToolbarPublish().setIncludeChildCheckbox( true ).clickOnPublishNowButton();

        then: "'Online' status should be displayed in the grid"
        contentBrowsePanel.getContentStatus( SITE.getName() ) == ContentStatus.ONLINE.getValue();
    }

    def "GIVEN existing 'online' site WHEN text component was added THEN status on the wizard-page is getting 'Modified'"()
    {
        given: "existing 'online' site"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();

        when: "new text component has been inserted"
        pageComponentsView.openMenu( "country" ).selectMenuItem( "Insert", "Text" );
        wizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.typeTextInTextComponent( TEST_TEXT );
        wizard.switchToDefaultWindow();
        pageComponentsView.doCloseDialog();

        and: "site was saved in the wizard"
        wizard.save();
        wizard.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        then: "status on the wizard-page is getting 'Modified'"
        wizard.getStatus() == ContentStatus.MODIFIED.getValue();
    }

    def "GIVEN existing 'online' site WHEN text component was removed THEN status on the wizard-page is getting 'Modified'"()
    {
        given: "existing 'online' site"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        wizard.clickOnWizardPublishButton().setIncludeChildCheckbox( true ).clickOnPublishNowButton();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();

        when: "text component was removed"
        pageComponentsView.openMenu( TEST_TEXT ).selectMenuItem( "Remove" );
        pageComponentsView.doCloseDialog();

        and: "site was saved in the wizard"
        wizard.save();
        wizard.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        then: "status on the wizard-page is getting 'Modified'"
        wizard.getStatus() == ContentStatus.MODIFIED.getValue();
    }

    def "GIVEN existing 'online' site WHEN display name was changed THEN status on the wizard-page is getting 'Modified'"()
    {
        given: "existing 'online' site"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        wizard.clickOnWizardPublishButton().setIncludeChildCheckbox( true ).clickOnPublishNowButton();

        when: "display name was changed"
        wizard.typeDisplayName( "new name" )

        and: "site was saved in the wizard"
        wizard.save();
        wizard.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        then: "status on the wizard-page is getting 'Modified'"
        wizard.getStatus() == ContentStatus.MODIFIED.getValue();
    }

    def "GIVEN existing 'online' site WHEN language was changed THEN status on the wizard-page is getting 'Modified'"()
    {
        given: "existing 'online' site"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        wizard.clickOnWizardPublishButton().setIncludeChildCheckbox( true ).clickOnPublishNowButton();
        saveScreenshot( "site_published_in_wizard" );

        when: "language was changed"
        SettingsWizardStepForm settings = wizard.clickOnSettingsTabLink();
        settings.waitUntilDisplayed();
        settings.selectLanguage( ENGLISH_LANGUAGE );

        and: "site was saved in the wizard"
        wizard.save();
        wizard.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        then: "status on the wizard-page is getting 'Modified'"
        wizard.getStatus() == ContentStatus.MODIFIED.getValue();
    }

    def "GIVEN existing 'online' site WHEN new part was inserted THEN status on the wizard-page is getting 'Modified'"()
    {
        given: "existing 'online' site"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        wizard.clickOnWizardPublishButton().setIncludeChildCheckbox( true ).clickOnPublishNowButton();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();

        when: "new part was inserted"
        pageComponentsView.openMenu( "country" ).selectMenuItem( "Insert", "Part" );
        pageComponentsView.doCloseDialog();
        wizard.switchToLiveEditFrame();
        PartComponentView partComponentView = new PartComponentView( getSession() );
        partComponentView.selectItem( CITY_CREATION_PART );
        saveScreenshot( "site_modified_in_wizard" );

        and: "site saved in the wizard"
        wizard.save();
        wizard.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        then: "status on the wizard-page is getting 'Modified'"
        wizard.getStatus() == ContentStatus.MODIFIED.getValue();
    }

    //TODO implement the Use Case: swap 2 components and verify, that status is getting 'Modified'
}
