package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.VersionHistoryWidget
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.image.ImageEditor
import com.enonic.autotests.pages.form.ImageFormViewPanel
import spock.lang.Ignore

/**
 * Created  on 25.10.2016.
 * */
@Ignore
class Restore_Version_Focus_Image_Spec
    extends BaseVersionHistorySpec
{

    def "GIVEN existing image is opened WHEN focus was moved THEN new 'version history item' should appear in the version-view"()
    {
        given: "existing image is selected"
        findAndSelectContent( IMPORTED_SPUMANS_IMAGE );

        and: "version history panel has been opened"
        VersionHistoryWidget allContentVersionsView = openVersionPanel();
        int numberOfVersionsBefore = allContentVersionsView.getAllVersions().size();

        and: "'Edit' button has been pressed"
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        formViewPanel.waitUntilImageLoaded();

        when: "focus has been moved"
        ImageEditor imageEditor = formViewPanel.clickOnFocusButton();
        imageEditor.doDragAndChangeFocus( -50 );
        saveScreenshot( "image_was_focused" );

        and: "the changes has been applied"
        imageEditor.getToolbar().clickOnApplyButton();

        and: "content has been saved and wizard closed"
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
        sleep( 1000 );
        int numberOfVersionsAfter = allContentVersionsView.getAllVersions().size();
        saveScreenshot( "versions_increased_after_focus_moved" );

        then: "number of version should increase by 1"
        numberOfVersionsAfter - numberOfVersionsBefore == 1;
    }

    def "GIVEN existing image with several versions is selected WHEN version with the original image is restored THEN button 'reset' should not be present on the wizard page "()
    {
        given: "existing image with several versions is selected"
        findAndSelectContent( IMPORTED_SPUMANS_IMAGE );

        and: "version panel is opened"
        VersionHistoryWidget allContentVersionsView = openVersionPanel();

        when: "version with original image has been reverted"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionItem( 1 );
        versionItem.doRevertVersion();

        and: "image has been opened in the wizard"
        contentBrowsePanel.clickToolbarEdit();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        formViewPanel.waitUntilImageLoaded();
        ImageEditor imageEditor = new ImageEditor( getSession() );

        then: "red circle should not be displayed on the image-editor"
        !imageEditor.isFocusCircleDisplayed();

        and: "button 'reset' should not be present in the wizard page"
        !formViewPanel.isButtonResetPresent();
    }

    def "GIVEN existing image with several versions is selected WHEN version with focused image is restored THEN button 'reset' should be present on the wizard page"()
    {
        given: "existing image with several versions"
        findAndSelectContent( IMPORTED_SPUMANS_IMAGE );

        and: "version panel is opened"
        VersionHistoryWidget allContentVersionsView = openVersionPanel();

        when: "focused image has been reverted"
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionItem( 1 );
        versionItem.doRevertVersion();

        and: "image has been opened in the wizard"
        contentBrowsePanel.clickToolbarEdit();
        ImageFormViewPanel formViewPanel = new ImageFormViewPanel( getSession() );
        formViewPanel.waitUntilImageLoaded();
        ImageEditor imageEditor = new ImageEditor( getSession() );
        saveScreenshot( "focused_image_restored" );

        then: "red circle(focus) should be displayed"
        imageEditor.isFocusCircleDisplayed();

        and: "button 'reset' should not be present in the wizard page"
        formViewPanel.isButtonResetPresent();
    }
}

