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
 * XP-4222 Add selenium tests for restoring of cropped image
 * */
@Stepwise
class Restore_Version_Image_Spec
    extends BaseVersionHistorySpec
{
    @Shared
    Integer INITIAL_CROP_AREA_HEIGHT;

    @Shared
    Integer CROPPED_IMAGE_HEIGHT;

    def "GIVEN existing folder WHEN display name of the folder changed THEN new 'version history item' appeared in the version-view"()
    {
        given:
        findAndSelectContent( IMPORTED_MAN_IMAGE );

        and: "version history panel has been opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        int numberOfVersionsBefore = allContentVersionsView.getAllVersions().size();

        and: "image opened in the wizard"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );



        when: "handler moved up and image was cropped "
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        INITIAL_CROP_AREA_HEIGHT = imageEditor.getCropAreaHeight();
        imageEditor.doDragCropButtonAndChangeHeightCropArea( -50 );
        CROPPED_IMAGE_HEIGHT = imageEditor.getCropAreaHeight();
        imageEditor.getToolbar().clickOnApplyButton();

        and: "changes saved and wizard closed"
        wizard.save().close( IMPORTED_MAN_IMAGE );
        int numberOfVersionsAfter = allContentVersionsView.getAllVersions().size();
        saveScreenshot( "versions_increased_after_cropping" );

        then: "number of version is increased"
        numberOfVersionsAfter - numberOfVersionsBefore == 1;
    }

    def "GIVEN existing image with several versions WHEN version with with full size is restored THEN button 'reset' is not present on the wizard page "()
    {
        given: "existing image with several versions"
        findAndSelectContent( IMPORTED_MAN_IMAGE );

        and: "version panel opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "version of image with full size is restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );

        and: "image is opened in the wizard"
        contentBrowsePanel.clickToolbarEdit();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        saveScreenshot( "full_size_image_restored" );

        then: "image with full initial size is displayed"
        imageEditor.getCropAreaHeight() == INITIAL_CROP_AREA_HEIGHT;
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

        when: "version of image with full size is restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 0 );
        versionItem.doRestoreVersion( versionItem.getId() );

        and: "image is opened in the wizard"
        contentBrowsePanel.clickToolbarEdit();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        ImageEditor imageEditor = formViewPanel.clickOnCropButton();
        saveScreenshot( "cropped_size_image_restored" );

        then: "image with full initial size is displayed"
        imageEditor.getCropAreaHeight() == CROPPED_IMAGE_HEIGHT;
        imageEditor.getToolbar().clickOnCloseButton();

        and: "button 'reset' is  present on the wizard page"
        formViewPanel.isButtonResetPresent();
    }
}
