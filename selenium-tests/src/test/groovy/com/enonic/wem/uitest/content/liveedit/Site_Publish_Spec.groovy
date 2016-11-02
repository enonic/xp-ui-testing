package com.enonic.wem.uitest.content.liveedit

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

    def "GIVEN creating new Site with selected controller WHEN site has been published THEN 'Online' status is displayed in the grid"()
    {
        given: "data typed and saved and wizard closed"
        SITE = buildMyFirstAppSite( "site" );
        contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() ).typeData( SITE ).selectPageDescriptor(
            COUNTRY_REGION_PAGE_CONTROLLER ).save().close( SITE.getDisplayName() );

        when: "site has been published"
        findAndSelectContent( SITE.getName() ).clickToolbarPublish().setIncludeChildCheckbox( true ).clickOnPublishNowButton();

        then: "'Online' status is displayed in the grid"
        contentBrowsePanel.getContentStatus( SITE.getName() ) == ContentStatus.ONLINE.getValue();
    }

    def "GIVEN existing 'online' site WHEN text component was added THEN status on the wizard-page is getting 'Modified'"()
    {
        given: "existing text component"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();

        when: "new text component has been inserted"
        pageComponentsView.openMenu( "main" ).selectMenuItem( "Insert", "Text" );
        wizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.typeTextInTextComponent( TEST_TEXT );
        wizard.switchToMainWindow();
        pageComponentsView.doCloseDialog();

        and: "site saved in the wizard"
        wizard.save();

        then: "status on the wizard-page is getting 'Modified'"
        wizard.getStatus() == ContentStatus.MODIFIED.getValue();
    }

    def "GIVEN existing 'online' site WHEN text component was removed THEN status on the wizard-page is getting 'Modified'"()
    {
        given: "existing text component"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        wizard.clickOnWizardPublishButton().setIncludeChildCheckbox( true ).clickOnPublishNowButton();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();

        when: "new text component has been inserted"
        pageComponentsView.openMenu( TEST_TEXT ).selectMenuItem( "Remove" );
        pageComponentsView.doCloseDialog();

        and: "site saved in the wizard"
        wizard.save();

        then: "status on the wizard-page is getting 'Modified'"
        wizard.getStatus() == ContentStatus.MODIFIED.getValue();
    }

    def "GIVEN existing 'online' site WHEN display name changed THEN status on the wizard-page is getting 'Modified'"()
    {
        given: "existing text component"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        wizard.clickOnWizardPublishButton().setIncludeChildCheckbox( true ).clickOnPublishNowButton();

        when: "display name changed"
        wizard.typeDisplayName( "new name" )

        and: "site saved in the wizard"
        wizard.save();

        then: "status on the wizard-page is getting 'Modified'"
        wizard.getStatus() == ContentStatus.MODIFIED.getValue();
    }

    def "GIVEN existing 'online' site WHEN language changed THEN status on the wizard-page is getting 'Modified'"()
    {
        given: "existing text component"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        wizard.clickOnWizardPublishButton().setIncludeChildCheckbox( true ).clickOnPublishNowButton();

        when: "display name changed"
        SettingsWizardStepForm settings = wizard.clickOnSettingsTabLink();
        settings.waitUntilDisplayed();
        settings.selectLanguage( ENGLISH_LANGUAGE );

        and: "site saved in the wizard"
        wizard.save();

        then: "status on the wizard-page is getting 'Modified'"
        wizard.getStatus() == ContentStatus.MODIFIED.getValue();
    }

    def "GIVEN existing 'online' site WHEN new part inserted THEN status on the wizard-page is getting 'Modified'"()
    {
        given: "existing text component"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit();
        wizard.clickOnWizardPublishButton().setIncludeChildCheckbox( true ).clickOnPublishNowButton();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();

        when: "display name changed"
        pageComponentsView.openMenu( "country" ).selectMenuItem( "Insert", "Part" );
        pageComponentsView.doCloseDialog();
        wizard.switchToLiveEditFrame();
        PartComponentView partComponentView = new PartComponentView( getSession() );
        partComponentView.selectItem( CITY_CREATION_PART );

        and: "site saved in the wizard"
        wizard.save();

        then: "status on the wizard-page is getting 'Modified'"
        wizard.getStatus() == ContentStatus.MODIFIED.getValue();
    }

    //TODO implement the Use Case: swap 2 components and verify, that status is getting 'Modified'
}
