package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ImageSelectorFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Tasks: XP-4948 Add Selenium tests for checking of 'red icon' (invalid content) in wizards
 **/
@Stepwise
class Restore_ImageSelector_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content IMAGE_SELECTOR_CONTENT;

    def "GIVEN ImageSelector-content 2:4 with 2 images is created WHEN one image has been removed THEN number of versions should be increased by one"()
    {
        given: "new ImageSelector-content 2:4 has been added"
        IMAGE_SELECTOR_CONTENT = buildImageSelector2_4_Content( NORD_IMAGE_DISPLAY_NAME, BOOK_IMAGE_DISPLAY_NAME );
        ContentWizardPanel wizard = findAndSelectContent( SITE_NAME ).clickToolbarNew().selectContentType(
            IMAGE_SELECTOR_CONTENT.getContentTypeName() );
        wizard.typeData( IMAGE_SELECTOR_CONTENT ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "content is opened and one image has been removed"
        findAndSelectContent( IMAGE_SELECTOR_CONTENT.getName() ).clickToolbarEdit();
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        formViewPanel.clickOnImage( NORD_IMAGE_DISPLAY_NAME ).clickOnRemoveButton();
        sleep( 500 );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        and: "version panel is opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        saveScreenshot( "imgselector_number_versions_should_be_3" );

        then: "number of versions should be increased by one"
        allContentVersionsView.getAllVersions().size() == 3;
    }

    def "GIVEN existing content with image-selector ( missed required image) WHEN version with two images has been restored THEN content becomes valid"()
    {
        given: "version of the content with one missed required image is current"
        ContentWizardPanel wizard = findAndSelectContent( IMAGE_SELECTOR_CONTENT.getName() ).clickToolbarEdit();
        and: "AppHome button has been pressed"
        wizard.switchToBrowsePanelTab();
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "valid version of content with two images is restored"
        allContentVersionsView.getAllVersions();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );
        saveScreenshot( "image_selector_valid_version" );

        then: "the content is valid in the grid as well"
        !contentBrowsePanel.isContentInvalid( IMAGE_SELECTOR_CONTENT.getName() );

        and: "'publish' button on the toolbar should be enabled"
        contentBrowsePanel.isPublishButtonEnabled();

        and: "red icon should not de displayed in the wizard-page"
        contentBrowsePanel.switchToBrowserTabByTitle( IMAGE_SELECTOR_CONTENT.getDisplayName() );
        !wizard.isContentInvalid();
    }

    def "GIVEN content with two images is restored WHEN content has been opened THEN two images should be present on the wizard"()
    {
        when: "version of content with two images is restored"
        findAndSelectContent( IMAGE_SELECTOR_CONTENT.getName() ).clickToolbarEdit();
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );

        then: "two images should be displayed on the wizard"
        formViewPanel.getSelectedImages().size() == 2;
    }

    def "GIVEN content with two images WHEN version of content with one images is restored THEN red icon should appears on the wizard tab"()
    {
        given: "content with two images is selected"
        ContentWizardPanel wizard = findAndSelectContent( IMAGE_SELECTOR_CONTENT.getName() ).clickToolbarEdit();
        wizard.switchToBrowsePanelTab();
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "version with one images has been restored"
        allContentVersionsView.getAllVersions();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 0 );
        versionItem.doRestoreVersion( versionItem.getId() );
        saveScreenshot( "image_selector_not_valid_version" );

        then: "the content should be valid in the grid"
        contentBrowsePanel.isContentInvalid( IMAGE_SELECTOR_CONTENT.getName() );

        and: "red icon should not be displayed on the wizard tab"
        contentBrowsePanel.switchToBrowserTabByTitle( IMAGE_SELECTOR_CONTENT.getDisplayName() );
        wizard.isContentInvalid();
    }

    def "GIVEN version with one image is restored WHEN content has been opened THEN one image should be present on the wizard"()
    {
        when: "content has been opened"
        findAndSelectContent( IMAGE_SELECTOR_CONTENT.getName() ).clickToolbarEdit();
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );

        then: "only one image should be displayed in the wizard"
        formViewPanel.getSelectedImages().size() == 1;
    }
}
