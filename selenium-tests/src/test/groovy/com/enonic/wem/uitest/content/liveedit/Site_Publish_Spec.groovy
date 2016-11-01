package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
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
        SITE = buildSimpleSiteApp();
        contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() ).typeData( SITE ).selectPageDescriptor(
            MAIN_REGION_PAGE_DESCRIPTOR_NAME ).save().close( SITE.getDisplayName() );

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
}
