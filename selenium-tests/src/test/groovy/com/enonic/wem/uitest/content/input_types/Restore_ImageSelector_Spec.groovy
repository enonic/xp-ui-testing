package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.ImageSelectorFormViewPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Restore_ImageSelector_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    Content IMAGE_SELECTOR_CONTENT;

    def "GIVEN creating new ImageSelector-content 2:4 WHEN one image was removed THEN number of versions increased by one"()
    {
        given: "new ImageSelector-content 2:4 added"
        IMAGE_SELECTOR_CONTENT = buildImageSelector2_4_Content( NORD_IMAGE_NAME, BOOK_IMAGE_NAME );
        ContentWizardPanel wizard = selectSiteOpenWizard( IMAGE_SELECTOR_CONTENT.getContentTypeName() );
        wizard.typeData( IMAGE_SELECTOR_CONTENT ).save().close( IMAGE_SELECTOR_CONTENT.getDisplayName() );
        contentBrowsePanel.clickOnClearSelection();

        when: "content opened and one image was removed"
        findAndSelectContent( IMAGE_SELECTOR_CONTENT.getName() ).clickToolbarEdit();
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );
        formViewPanel.clickOnImage( NORD_IMAGE_NAME ).clickOnRemoveButton();
        wizard.save().close( IMAGE_SELECTOR_CONTENT.getDisplayName() );

        and: "version panel is opened"
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        then: "number of versions increased by one"
        allContentVersionsView.getAllVersions().size() == 3;
    }

    def "GIVEN existing content with 3 versions WHEN valid version of content with two images is restored THEN content has no red icon on the wizard"()
    {
        given: "content with a changed date"
        ContentWizardPanel wizard = findAndSelectContent( IMAGE_SELECTOR_CONTENT.getName() ).clickToolbarEdit();
        contentBrowsePanel.pressAppHomeButton();
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "valid version of content with two images is restored"
        allContentVersionsView.getAllVersions();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );
        TestUtils.saveScreenshot( getSession(), "image_selector_valid_version" );

        then: "red icon is not present on the wizard"
        !wizard.isContentInvalid( IMAGE_SELECTOR_CONTENT.getDisplayName() );
    }

    def "GIVEN version of content with two images is restored WHEN content opened THEN two images are displayed on the wizard"()
    {
        when: "version of content with two images is restored"
        findAndSelectContent( IMAGE_SELECTOR_CONTENT.getName() ).clickToolbarEdit();
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );

        then: "two images are displayed on the wizard"
        formViewPanel.getSelectedImages().size() == 2;
    }

    def "GIVEN existing content with 3 versions WHEN version of content with one images is restored THEN redi icon appears on the wizard tab"()
    {
        given: "existing content with 3 versions opened"
        ContentWizardPanel wizard = findAndSelectContent( IMAGE_SELECTOR_CONTENT.getName() ).clickToolbarEdit();
        contentBrowsePanel.pressAppHomeButton();
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "not valid version of content is restored, one required image is missed"
        allContentVersionsView.getAllVersions();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 0 );
        versionItem.doRestoreVersion( versionItem.getId() );
        TestUtils.saveScreenshot( getSession(), "image_selector_not_valid_version" );

        then: "red icon appears on the wizard tab"
        wizard.isContentInvalid( IMAGE_SELECTOR_CONTENT.getDisplayName() );
    }


    def "GIVEN version of content with one images is restored WHEN content opened THEN one image is displayed on the wizard"()
    {
        when: "version of content with one image is restored"
        findAndSelectContent( IMAGE_SELECTOR_CONTENT.getName() ).clickToolbarEdit();
        ImageSelectorFormViewPanel formViewPanel = new ImageSelectorFormViewPanel( getSession() );

        then: "only one image is displayed on the wizard"
        formViewPanel.getSelectedImages().size() == 1;
    }
}
