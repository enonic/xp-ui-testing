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
 *
 * TASKS: XP-4222 Add selenium tests for restoring of cropped image
 * Verifies the bug: XP-4331 Image Editor - Image not refreshed after being restored one of the its versions
 * */
@Stepwise
class Restore_Version_Crop_Image_Spec
    extends BaseVersionHistorySpec
{
    @Shared
    Integer ORIGINAL_CROP_AREA_HEIGHT;

    @Shared
    Integer CROPPED_IMAGE_HEIGHT;

    def "GIVEN existing image WHEN handler moved up and image was cropped THEN new 'version history item' appeared in the version-view"()
    {
        given:
        findAndSelectContent( IMPORTED_MAN_IMAGE );

        and: "version history panel has been opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        int numberOfVersionsBefore = allContentVersionsView.getAllVersions().size();

        and: "image opened in the wizard"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEditAndSwitchToWizardTab();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        formViewPanel.waitUntilImageLoaded();

        when: "handler moved up and image was cropped"
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        ORIGINAL_CROP_AREA_HEIGHT = imageEditor.getCropAreaHeight();
        imageEditor.doDragCropButtonAndChangeHeightCropArea( -50 );
        CROPPED_IMAGE_HEIGHT = imageEditor.getCropAreaHeight();
        imageEditor.getToolbar().clickOnApplyButton();

        and: "changes saved and wizard closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        int numberOfVersionsAfter = allContentVersionsView.getAllVersions().size();
        saveScreenshot( "versions_increased_after_cropping" );

        then: "number of version is increased"
        numberOfVersionsAfter - numberOfVersionsBefore == 1;
    }

    def "GIVEN existing image with several versions WHEN version with original image is restored THEN button 'reset' is not present on the wizard page "()
    {
        given: "existing image with several versions"
        findAndSelectContent( IMPORTED_MAN_IMAGE );

        and: "version panel opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "version with original image is restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );

        and: "image is opened in the wizard"
        contentBrowsePanel.clickToolbarEdit();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        saveScreenshot( "original_size_image_restored" );

        then: "height of crop area is correct "
        imageEditor.getCropAreaHeight() == ORIGINAL_CROP_AREA_HEIGHT;
        imageEditor.getToolbar().clickOnCloseButton();

        and: "button 'reset' is not present on the wizard page"
        !formViewPanel.isButtonResetPresent();
    }

    def "GIVEN existing image with several versions WHEN version with cropped size is restored THEN button 'reset' is present on the wizard page "()
    {

        given: "existing image with several versions"
        findAndSelectContent( IMPORTED_MAN_IMAGE );

        and: "version panel opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "version of image with cropped size is restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 0 );
        versionItem.doRestoreVersion( versionItem.getId() );

        and: "image is opened in the wizard"
        contentBrowsePanel.clickToolbarEdit();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        saveScreenshot( "cropped_size_image_restored" );

        then: "height of crop area is correct"
        imageEditor.getCropAreaHeight() == CROPPED_IMAGE_HEIGHT;
        imageEditor.getToolbar().clickOnCloseButton();

        and: "button 'reset' is  present on the wizard page"
        formViewPanel.isButtonResetPresent();
    }
    //Verifies bug: XP-4331 Image Editor - Image not refreshed after being restored one of the its versions
    def "GIVEN existing cropped image is opened WHEN original version is restored THEN image has been updated on the wizard page"()
    {
        given: "existing zoomed image is opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_MAN_IMAGE ).clickToolbarEditAndSwitchToWizardTab();
        wizard.switchToBrowsePanelTab();

        when: "version panel opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        and: "original version is restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );
        saveScreenshot( "image_reverted_to_zoomed" );

        and: "wizard-tab activated again"
        contentBrowsePanel.switchToContentWizardTabBySelectedContent()
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );

        then: "original image is displayed on the wizard"
        !formViewPanel.isButtonResetPresent();
    }

    def "GIVEN existing image is opened WHEN version with cropped image is restored THEN image has been updated on the wizard page"()
    {
        given: "existing zoomed image is opened"
        ContentWizardPanel wizard = findAndSelectContent( IMPORTED_MAN_IMAGE ).clickToolbarEdit();
        wizard.switchToBrowsePanelTab();

        when: "version panel opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        and: "original version is restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 0 );
        versionItem.doRestoreVersion( versionItem.getId() );
        saveScreenshot( "image_reverted_to_zoomed" );

        and: "wizard-tab activated again"
        contentBrowsePanel.switchToContentWizardTabBySelectedContent();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );

        then: "original image is displayed on the wizard"
        formViewPanel.isButtonResetPresent();
    }
}
