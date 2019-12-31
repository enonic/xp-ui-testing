package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditor
import com.enonic.autotests.pages.form.ImageFormViewPanel
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created  on 13.10.2016.
 * Verifies XP-4331 Image Editor - Image not refreshed after being restored one of the its versions
 * */
@Stepwise
class Restore_Version_Crop_Image_Spec
    extends BaseVersionHistorySpec
{
    @Shared
    Integer ORIGINAL_CROP_AREA_HEIGHT;

    @Shared
    Integer CROPPED_IMAGE_HEIGHT;

    def "GIVEN existing image is opened WHEN handler moved up and image has been cropped AND changes applied THEN new 'version history item' appeared in the version-view"()
    {
        given: "existing image"
        findAndSelectContent( IMPORTED_MAN_IMAGE );

        and: "version history panel has been opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        int numberOfVersionsBefore = allContentVersionsView.getAllVersions().size();

        and: "image is opened"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        formViewPanel.waitUntilImageLoaded();

        when: "handler moved up and image has been cropped"
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        ORIGINAL_CROP_AREA_HEIGHT = imageEditor.getCropAreaHeight();
        imageEditor.doDragCropButtonAndChangeHeightCropArea( -50 );
        CROPPED_IMAGE_HEIGHT = imageEditor.getCropAreaHeight();
        imageEditor.getToolbar().clickOnApplyButton();

        and: "changes is saved and wizard closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        int numberOfVersionsAfter = allContentVersionsView.getAllVersions().size();
        saveScreenshot( "versions_increased_after_cropping" );

        then: "number of version should be increased by 1"
        numberOfVersionsAfter - numberOfVersionsBefore == 1;
    }

    def "GIVEN existing cropped image is selected WHEN version with original image is restored THEN button 'reset filter' should not be present in the wizard"()
    {
        given: "existing image with several versions is selected"
        findAndSelectContent( IMPORTED_MAN_IMAGE );

        and: "version panel opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "version with original image has been restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion();

        and: "the image is opened"
        contentBrowsePanel.clickToolbarEdit();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        saveScreenshot( "original_size_image_reverted" );

        then: "original height of the crop area should be restored "
        imageEditor.getCropAreaHeight() == ORIGINAL_CROP_AREA_HEIGHT;
        imageEditor.getToolbar().clickOnCloseButton();

        and: "button 'reset' should not be present on the wizard page"
        !formViewPanel.isButtonResetPresent();
    }

    def "GIVEN existing image with several versions WHEN version with cropped size has been restored THEN button 'reset filter' should appear on the image editor"()
    {
        given: "existing image with several versions is selected"
        findAndSelectContent( IMPORTED_MAN_IMAGE );

        and: "version panel is opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "version of image with cropped size has been restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion();

        and: "the image is opened"
        contentBrowsePanel.clickToolbarEdit();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        //Click on Crop button and open the ImageEditor:
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        saveScreenshot( "cropped_image_restored1" );

        then: "cropped height of crop area should be restored"
        imageEditor.getCropAreaHeight() == CROPPED_IMAGE_HEIGHT;
        //Close the ImageEditor:
        imageEditor.getToolbar().clickOnCloseButton();

        and: "button 'reset' should be present on the image editor"
        formViewPanel.isButtonResetPresent();
    }
    //Verifies bug: XP-4331 Image Editor - Image not refreshed after being restored one of the its versions
    def "GIVEN existing cropped image is opened WHEN original version has been restored THEN 'Reset' button should not be present"()
    {
        given: "existing zoomed image is opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_MAN_IMAGE ).clickToolbarEditAndSwitchToWizardTab();
        wizard.switchToBrowsePanelTab();

        when: "version panel has been opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        and: "original version is restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 3 );
        versionItem.doRestoreVersion();
        saveScreenshot( "image_reverted_to_original" );

        and: "switch to the wizard-tab again"
        contentBrowsePanel.switchToContentWizardTabBySelectedContent()
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );

        then: "Reset button should not be present"
        !formViewPanel.isButtonResetPresent();
    }

    def "GIVEN existing image with several versions is opened WHEN version with cropped image is reverted THEN Reset button should be present on the wizard page"()
    {
        given: "existing image with several versions is opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_MAN_IMAGE ).clickToolbarEdit();
        wizard.switchToBrowsePanelTab();

        when: "version panel has been opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        and: "cropped version has been restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 3 );
        versionItem.doRestoreVersion(  );
        saveScreenshot( "cropped_version_reverted2" );

        and: "switch to the wizard-tab again"
        contentBrowsePanel.switchToContentWizardTabBySelectedContent();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );

        then: "'Reset' button should appear"
        formViewPanel.isButtonResetPresent();
    }
}
