package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditor
import com.enonic.autotests.pages.form.ImageFormViewPanel
import spock.lang.Stepwise

/**
 * Created  on 26.10.2016.
 * Verifies bug: XP-4331 Image Editor - Image not refreshed after being restored one of the its versions
 * */
@Stepwise
class Restore_Version_Zoom_Image_Spec
    extends BaseVersionHistorySpec
{

    def "GIVEN existing image WHEN image has been zoomed AND aplly button pressed THEN new 'version history item' should appear in the widget"()
    {
        given:
        findAndSelectContent( HAND_IMAGE_NAME );

        and: "versions panel is opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        int numberOfVersionsBefore = allContentVersionsView.getAllVersions().size();

        and: "the image is opened in the wizard"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        and: "image was loaded"
        formViewPanel.waitUntilImageLoaded();

        when: "the image has been zoomed"
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        imageEditor.doZoomImage( 70 );

        and: "changes were applied"
        imageEditor.getToolbar().clickOnApplyButton();

        and: "content saved in the wizard and closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        sleep( 1000 );
        int numberOfVersionsAfter = allContentVersionsView.getAllVersions().size();
        saveScreenshot( "versions_increased_after_zoom" );

        then: "number of version should be increased"
        numberOfVersionsAfter - numberOfVersionsBefore == 1;
    }

    def "GIVEN existing image with several versions WHEN version with original image is restored THEN button 'reset filter' should not be present in the Image Editor"()
    {
        given: "existing image with several versions"
        findAndSelectContent( HAND_IMAGE_NAME );

        and: "version panel opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "version with original image has been restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion(  );

        and: "image is opened in the wizard"
        contentBrowsePanel.clickToolbarEdit();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );

        then: "button 'reset' is not present on the wizard page"
        !formViewPanel.isButtonResetPresent();
    }

    def "GIVEN existing image with several versions WHEN version with zoomed image is restored THEN button 'reset filter' should appear"()
    {

        given: "existing image with several versions"
        findAndSelectContent( HAND_IMAGE_NAME );

        and: "version panel opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "version with zoomed image is restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion(  );
        saveScreenshot( "zoomed_image_reverted_to_original" );

        and: "image is opened in the wizard"
        contentBrowsePanel.clickToolbarEdit();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        saveScreenshot( "zoomed_image_restored" );

        then: "button 'reset' is  present on the wizard page"
        formViewPanel.isButtonResetPresent();
    }
    //Verifies the bug: XP-4331 Image Editor - Image not refreshed after being restored one of the its versions
    def "GIVEN existing zoomed image is opened WHEN original version is restored THEN image has been updated on the wizard page"()
    {
        given: "existing zoomed image is opened"
        ContentWizardPanel wizard = findAndSelectContent( HAND_IMAGE_NAME ).clickToolbarEdit();

        and: "AppHome button was pressed"
        wizard.switchToBrowsePanelTab();

        when: "version panel opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        and: "original version is restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion(  );
        saveScreenshot( "image_reverted_to_zoomed" );

        and: "wizard-tab activated again"
        contentBrowsePanel.switchToContentWizardTabBySelectedContent();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );

        then: "original image is displayed on the wizard"
        !formViewPanel.isButtonResetPresent();
    }
    //verifies the XP-4331 Image Editor - Image not refreshed after being restored one of the its versions
    def "GIVEN existing  image is opened WHEN version with zoomed image is restored THEN image has been updated on the wizard page"()
    {
        given: "existing zoomed image is opened"
        ContentWizardPanel wizard = findAndSelectContent( HAND_IMAGE_NAME ).clickToolbarEdit();

        and: "AppHome button was pressed"
        wizard.switchToBrowsePanelTab();

        when: "version panel opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        and: "version with zoomed image is restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion(  );
        saveScreenshot( "image_reverted_to_zoomed" );

        and: "wizard-tab activated again"
        contentBrowsePanel.switchToBrowserTabByTitle( HAND_IMAGE_DISPLAY_NAME );
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );

        then: "original image is displayed on the wizard"
        formViewPanel.isButtonResetPresent();
    }
}
