package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditor
import com.enonic.autotests.pages.form.ImageFormViewPanel

/**
 * Created  on 25.10.2016.
 *
 * Task: XP-4328 Add selenium tests for changing of focus in the Image Wizard
 * */
class Restore_Version_Focus_Image_Spec
    extends BaseVersionHistorySpec
{

    def "GIVEN existing image WHEN focus was moved THEN new 'version history item' appeared in the version-view"()
    {
        given:
        findAndSelectContent( IMPORTED_SPUMANS_IMAGE );

        and: "version history panel has been opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        int numberOfVersionsBefore = allContentVersionsView.getAllVersions().size();

        and: "image opened in the wizard"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit().waitUntilWizardOpened();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        formViewPanel.waitUntilImageLoaded();

        when: "focus was moved"
        ImageEditor imageEditor = formViewPanel.clickOnFocusButton();
        imageEditor.doDragAndChangeFocus( -50 );

        and: "changes were applied"
        imageEditor.getToolbar().clickOnApplyButton();

        and: "content saved in the wizard and closed"
        wizard.save().close( IMPORTED_SPUMANS_IMAGE );
        int numberOfVersionsAfter = allContentVersionsView.getAllVersions().size();
        saveScreenshot( "versions_increased_after_focus_moved" );

        then: "number of version is increased"
        numberOfVersionsAfter - numberOfVersionsBefore == 1;
    }

    def "GIVEN existing image with several versions WHEN version with original image is restored THEN button 'reset' is not present on the wizard page "()
    {
        given: "existing image with several versions"
        findAndSelectContent( IMPORTED_SPUMANS_IMAGE );

        and: "version panel opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "version with original image is restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );

        and: "image is opened in the wizard"
        contentBrowsePanel.clickToolbarEdit();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        formViewPanel.waitUntilImageLoaded();
        ImageEditor imageEditor = new ImageEditor( getSession() );


        then: "red circle not displayed on the image-editor"
        !imageEditor.isFocusCircleDisplayed();

        and: "button 'reset' is not present on the wizard page"
        !formViewPanel.isButtonResetPresent();
    }

    def "GIVEN existing image with several versions WHEN version with focused image is restored THEN button 'reset' is present on the wizard page "()
    {

        given: "existing image with several versions"
        findAndSelectContent( IMPORTED_SPUMANS_IMAGE );

        and: "version panel opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "version of image with focus is restored"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 0 );
        versionItem.doRestoreVersion( versionItem.getId() );

        and: "image is opened in the wizard"
        contentBrowsePanel.clickToolbarEdit();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        formViewPanel.waitUntilImageLoaded();
        ImageEditor imageEditor = new ImageEditor( getSession() );
        saveScreenshot( "focused_image_restored" );

        then: "red circle(focus) is displayed"
        imageEditor.isFocusCircleDisplayed();

        and: "button 'reset' is  present on the wizard page"
        formViewPanel.isButtonResetPresent();
    }
}

