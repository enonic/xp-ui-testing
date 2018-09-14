package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.liveedit.ImageComponentView
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.content.ContentPath
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared
import spock.lang.Stepwise

/*
one test for task " XP-4368 Add tests to verify the XP-4367'
 */

@Stepwise
class CreateSiteWithLayoutSpec
    extends BaseContentSpec
{
    @Shared
    Content SITE

    @Shared
    Content pageTemplate

    @Shared
    String SITE_DISPLAY_NAME = "layout-page-template";

    @Shared
    String SUPPORTS = ContentTypeName.site().toString();

    @Shared
    String TEST_IMAGE_DISPLAY_NAME = "telk";

    @Shared
    String SECOND_TEST_IMAGE_COMPONENT_NAME = "geek";

    @Shared
    String THIRD_TEST_IMAGE_COMPONENT_NAME = "foss";

    @Shared
    String LAYOUT_NAME = "3-col";

    @Shared
    String TEXT_COMPONENT_TEXT = "test text";

    def "GIVEN creating of new Site based on 'Simple site' WHEN the site is saved and wizard closed THEN new site should be listed int the grid"()
    {
        given:
        SITE = buildSimpleSiteApp();

        when: "data was typed and site saved "
        contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() ).typeData(
            SITE ).save().closeBrowserTab().switchToBrowsePanelTab();

        then: "new site should be listed in the grid"
        filterPanel.typeSearchText( SITE.getName() );
        contentBrowsePanel.exists( SITE.getName() );
    }

    def "GIVEN existing site, based on 'Simple site' app WHEN site was expanded and templates folder selected AND page-template added  THEN new template should be present in a 'Templates' folder"()
    {
        given: "existing site"
        pageTemplate = buildPageTemplate( MAIN_REGION_PAGE_DESCRIPTOR_NAME, SUPPORTS, SITE_DISPLAY_NAME, SITE.getName() );
        filterPanel.typeSearchText( SITE.getName() );
        and: "the site is expanded"
        contentBrowsePanel.expandContent( ContentPath.from( SITE.getName() ) );

        when: "'Templates' folder selected and new page-template added"
        contentBrowsePanel.selectContentInTable( "_templates" ).clickToolbarNew().selectContentType(
            pageTemplate.getContentTypeName() ).typeData( pageTemplate ).closeBrowserTab().switchToBrowsePanelTab();

        contentBrowsePanel.expandContent( ContentPath.from( SITE.getName() ) );
        contentBrowsePanel.expandContent( ContentPath.from( "_templates" ) );
        saveScreenshot( "simple_template" );

        then: "new template should be listed beneath a 'Templates' folder"
        contentBrowsePanel.exists( pageTemplate.getName() );
    }

    def "GIVEN 'Page Components' opened WHEN menu for 'main region' clicked and 'insert' menu-item selected AND 'Text'-item clicked THEN new text present on the live edit frame"()
    {
        given: "'Page Components' opened"
        filterPanel.typeSearchText( pageTemplate.getName() )
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable( pageTemplate.getName() ).clickToolbarEdit();
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();

        when: "Menu is opened AND 'Insert/Text' menu items clicked and text typed"
        pageComponentsView.openMenu( "main" ).selectMenuItem( "Insert", "Text" );
        saveScreenshot( "select_insert_text" );
        wizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.typeTextInTextComponent( TEXT_COMPONENT_TEXT );
        getDriver().switchTo().defaultContent();
        pageComponentsView.doCloseDialog();
        wizard.save();
        saveScreenshot( "text_component_text_saved" );
        wizard.switchToLiveEditFrame();

        then: "text-component should appear in the 'live edit' frame"
        liveFormPanel.isTextComponentPresent();

        and: "correct text should be displayed in the text-component"
        liveFormPanel.getTextFromTextComponents().get( 0 ) == ( TEXT_COMPONENT_TEXT );
    }

    def "GIVEN 'Page Components' is opened WHEN menu for 'main region' is opened and 'insert' menu-item selected AND 'layout'-item clicked THEN new layout should be present on the live edit frame"()
    {
        given: "site is opened and 'Page Components' opened as well"
        ContentWizardPanel wizardPanel = findAndSelectContent( pageTemplate.getName() ).clickToolbarEdit();
        PageComponentsViewDialog pageComponentsView = wizardPanel.showComponentView();

        when: "menu is opened and 'Layout' menu item has been selected"
        pageComponentsView.openMenu( "main" ).selectMenuItem( "Insert", "Layout" );
        saveScreenshot( "select_insert_layout" );
        pageComponentsView.doCloseDialog();
        wizardPanel.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );

        and: "layout with 3 columns has been inserted"
        liveFormPanel.getLayoutComponentView().selectLayout( LAYOUT_NAME );
        saveScreenshot( "page_editor_layout_selected" );

        then: "layout-component should appear in the 'live edit' frame and number of regions should be 3"
        liveFormPanel.isLayoutComponentPresent();

        and: "three columns should be present in the layout"
        liveFormPanel.getLayoutColumnNumber() == 3;
    }

    def "GIVEN existing site with the layout is opened WHEN site is published with its children THEN site should be with 'online' status on the wizard page"()
    {
        given: "existing site with the layout is opened"
        ContentWizardPanel wizard = findAndSelectContent( SITE.getName() ).clickToolbarEdit().waitUntilWizardOpened()
        wizard.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        when: "site is published with its children"
        ContentPublishDialog modalDialog = wizard.clickOnWizardPublishButton().includeChildren( true ).clickOnPublishNowButton();

        then: "publish dialog should be closed"
        modalDialog.waitForDialogClosed();

        and: "correct status should be displayed on the wizard-page"
        wizard.getStatus() == ContentStatus.PUBLISHED.getValue();
    }

    def "GIVEN page-template is opened and 'Page Components' is opened WHEN menu for 'left region' is opened and 'insert' menu-item selected AND 'image'-item clicked THEN new image should be present in the left region"()
    {
        given: "template is opened"
        filterPanel.typeSearchText( pageTemplate.getName() )
        ContentWizardPanel templateWizard = contentBrowsePanel.selectContentInTable( pageTemplate.getName() ).clickToolbarEdit();
        and: "and 'Page Components' view is opened"
        PageComponentsViewDialog pageComponentsView = templateWizard.showComponentView();

        when: "menu for 'left region' is opened and 'insert' menu-item selected AND 'image'-item clicked"
        pageComponentsView.openMenu( "left" ).selectMenuItem( "Insert", "Image" );
        pageComponentsView.doCloseDialog();
        templateWizard.switchToLiveEditFrame();
        ImageComponentView imageComponentView = new ImageComponentView( getSession() );
        imageComponentView.selectImageFromOptions( TEST_IMAGE_DISPLAY_NAME )

        and: "the template should be saved automatically"
        //need to wait for updating of status;
        sleep( 1000 );
        templateWizard.switchToDefaultWindow();
        String statusAfterInsertingImage = templateWizard.getStatus();
        saveScreenshot( "image_in_left_inserted" );

        then: "new image should be present in the left-region "
        templateWizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.getNumberImagesInLayout() == 1;

        and: "template's status is getting 'Modified'"
        statusAfterInsertingImage == ContentStatus.MODIFIED.getValue();
    }

    def "GIVEN template is opened and 'Page Components' is opened WHEN menu for 'center region' is opened and 'insert' menu-item selected AND 'image'-item clicked THEN new image should be present in the center-region "()
    {
        given: "template is opened"
        filterPanel.typeSearchText( pageTemplate.getName() )
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable( pageTemplate.getName() ).clickToolbarEdit();
        and: "and 'Page Components' view is opened"
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();

        when: "menu for 'center region' is opened and 'insert' menu-item selected AND 'image'-item clicked"
        pageComponentsView.openMenu( "center" ).selectMenuItem( "Insert", "Image" );
        pageComponentsView.doCloseDialog();
        wizard.switchToLiveEditFrame();
        ImageComponentView imageComponentView = new ImageComponentView( getSession() );
        imageComponentView.selectImageFromOptions( SECOND_TEST_IMAGE_COMPONENT_NAME );

        and: "the template should be automatically saved"
        wizard.switchToLiveEditFrame();
        saveScreenshot( "center_region_inserted" );

        then: "new image should be present in the center-region "

        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.getNumberImagesInLayout() == 2;
    }

    def "GIVEN 'Page Components' opened WHEN menu for 'right region' is opened and 'insert' menu-item selected AND 'image'-item is clicked THEN new image should be displayed on the right-region"()
    {
        given: "template is opened"
        filterPanel.typeSearchText( pageTemplate.getName() )
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable( pageTemplate.getName() ).clickToolbarEdit();
        and: "and 'Page Components' view is opened"
        PageComponentsViewDialog pageComponentsView = wizard.showComponentView();

        when: "menu for right region is opened and 'insert' menu-item selected AND 'image'-item has been clicked"
        pageComponentsView.openMenu( "right" ).selectMenuItem( "Insert", "Image" );
        pageComponentsView.doCloseDialog();
        wizard.switchToLiveEditFrame();
        ImageComponentView imageComponentView = new ImageComponentView( getSession() );
        imageComponentView.selectImageFromOptions( THIRD_TEST_IMAGE_COMPONENT_NAME );

        and: "the template should be saved automatically"
        saveScreenshot( "right_region_inserted" );

        then: "new image should be displayed on the right-region"
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.getNumberImagesInLayout() == 3;
    }

    def "GIVEN layout with 3 inserted images  WHEN menu for one of them images selected AND 'reset' menu-item selected THEN removed image not present in layout"()
    {
        given: "page template is opened"
        filterPanel.typeSearchText( pageTemplate.getName() )
        ContentWizardPanel wizardPanel = contentBrowsePanel.selectContentInTable( pageTemplate.getName() ).clickToolbarEdit();
        and: "and 'Page Components' view is opened"
        PageComponentsViewDialog pageComponentsView = wizardPanel.showComponentView();

        when: "menu for image was clicked and 'reset' menu-item is selected"
        pageComponentsView.openMenu( TEST_IMAGE_DISPLAY_NAME ).selectMenuItem( "Reset" );
        wizardPanel.switchToLiveEditFrame();

        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        saveScreenshot( "image_was_reset" );

        then: "number of images in layout should be reduced"
        liveFormPanel.getNumberImagesInLayout() == 2;

        and: "but number of components should not be changed"
        liveFormPanel.getNumberImageComponentsInLayout() == 3;
        and: "image with the required name no longer present on LiveEdit"
        !liveFormPanel.isImagePresentInLayout( TEST_IMAGE_DISPLAY_NAME );
    }

    def "GIVEN layout with 3 inserted images  WHEN menu for one of them images selected AND 'duplicate' menu-item selected THEN two images with the same name should be present in the layout"()
    {
        given: "page template is opened"
        filterPanel.typeSearchText( pageTemplate.getName() )
        ContentWizardPanel wizardPanel = contentBrowsePanel.selectContentInTable( pageTemplate.getName() ).clickToolbarEdit();
        and: "and 'Page Components' view is opened"
        PageComponentsViewDialog pageComponentsView = wizardPanel.showComponentView();

        when: "menu for image is opened and 'duplicate' menu-item selected"
        pageComponentsView.openMenu( TEST_IMAGE_DISPLAY_NAME ).selectMenuItem( "Duplicate" );
        wizardPanel.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        saveScreenshot( "image_was_duplicated" );

        then: "number of images in layout should be increased"
        liveFormPanel.getNumberImagesInLayout() == 4;

        and: "but number of components should be increased"
        liveFormPanel.getNumberImageComponentsInLayout() == 4;
        and: "two images with same name should be present in the layout"
        liveFormPanel.getNumberOfImagesByName( TEST_IMAGE_DISPLAY_NAME ) == 2;
    }

    def "GIVEN layout with 4 images AND 'Page Components' view opened WHEN menu for one of them images selected AND 'remove' menu-item selected THEN number of images reduced in the layout"()
    {
        given: "'Page Components' opened"
        filterPanel.typeSearchText( pageTemplate.getName() )
        ContentWizardPanel wizardPanel = contentBrowsePanel.selectContentInTable( pageTemplate.getName() ).clickToolbarEdit();
        PageComponentsViewDialog pageComponentsView = wizardPanel.showComponentView();

        when: "menu for image is opened and 'remove' menu-item selected"
        pageComponentsView.openMenu( TEST_IMAGE_DISPLAY_NAME ).selectMenuItem( "Remove" );
        wizardPanel.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        saveScreenshot( "one_image_was_removed" );

        then: "number of images is reduced in the layout"
        liveFormPanel.getNumberImagesInLayout() == 3;

        and: "but number of components has not changed"
        liveFormPanel.getNumberImageComponentsInLayout() == 3;

        and: "only one image with the name should be present in the layout"
        liveFormPanel.getNumberOfImagesByName( TEST_IMAGE_DISPLAY_NAME ) == 1;
    }
}