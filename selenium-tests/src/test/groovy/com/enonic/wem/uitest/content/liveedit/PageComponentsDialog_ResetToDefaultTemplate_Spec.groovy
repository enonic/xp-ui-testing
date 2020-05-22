package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.liveedit.ImageComponentView
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.content.ContentPath
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class PageComponentsDialog_ResetToDefaultTemplate_Spec
    extends BaseContentSpec
{
    @Shared
    String SITE_WITH_COMPONENTS_NAME = "site-component";

    @Shared
    String PAGE_CONTROLLER_NAME = "Country Region";

    @Shared
    Content SITE;

    @Shared
    Content PAGE_TEMPLATE;

    @Shared
    String TEMPLATE_DISPLAY_NAME = "country template";

    @Shared
    String IMAGE_DISPLAY_NAME_FOR_TEMPLATE = IMPORTED_IMAGE_BOOK_DISPLAY_NAME;

    @Shared
    String IMAGE_NAME_FOR_TEMPLATE = IMPORTED_IMAGE_BOOK_NAME;

    @Shared
    String TEST_IMAGE = IMPORTED_MAN2_IMAGE_DISPLAY_NAME;

    @Shared
    String TEST_IMAGE_SWAP = WHALE_IMAGE_DISPLAY_NAME;

    @Shared
    String TEST_IMAGE_NAME_SWAP = WHALE_IMAGE_NAME;


    def "Preconditions: new site with a template should be added"()
    {
        given: "existing Site based on 'My First App'"
        SITE = buildMyFirstAppSite( SITE_WITH_COMPONENTS_NAME );
        addSite( SITE );
        filterPanel.typeSearchText( SITE.getName() )
        contentBrowsePanel.expandContent( ContentPath.from( SITE.getName() ) );
        PAGE_TEMPLATE = buildPageTemplate( COUNTRY_REGION_PAGE_CONTROLLER, TEMPLATE_SUPPORTS_SITE, TEMPLATE_DISPLAY_NAME, SITE.getName() );

        when: "'Templates' folder selected and new page-template added"
        addTemplateWithImage( PAGE_TEMPLATE, IMAGE_DISPLAY_NAME_FOR_TEMPLATE )
        sleep( 500 );

        then: "new page-template should be listed"
        filterPanel.typeSearchText( PAGE_TEMPLATE.getName() );
        contentBrowsePanel.exists( PAGE_TEMPLATE.getName() );
    }

    def "GIVEN existing site is opened AND one image has been changed WHEN root element in page component dialog has been selected and 'Reset' menu item clicked THEN site should be reset to default template"()
    {
        given: "site opened for edit  and site saved"
        filterPanel.typeSearchText( SITE.getName() )
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        wizard.unlockPageEditorAndSwitchToContentStudio();
        saveScreenshot( "image-reset-to-template" );
        wizard.showComponentView();
        saveScreenshot( "image-from-template" );

        and: "and an image has been removed"
        PageComponentsViewDialog pageComponentsView = new PageComponentsViewDialog( getSession() );
        pageComponentsView.openMenu( IMAGE_DISPLAY_NAME_FOR_TEMPLATE ).selectMenuItem( "Remove" );
        pageComponentsView.openMenu( "country" ).selectMenuItem( "Insert", "Image" );
        pageComponentsView.doCloseDialog();
        wizard.switchToLiveEditFrame();

        and: "new image was inserted"
        ImageComponentView imageComponentView = new ImageComponentView( getSession() );
        imageComponentView.selectImageFromOptions( TEST_IMAGE );

        and: "the content should be automatically saved"
        saveScreenshot( "new-image-set" );
        wizard.switchToDefaultWindow();
        String message1 = contentBrowsePanel.waitForNotificationMessage();

        when: "root element in 'page component' dialog was selected and 'Reset' menu item selected"
        wizard.showComponentView();
        sleep( 1000 );
        pageComponentsView.openMenu( PAGE_CONTROLLER_NAME ).selectMenuItem( "Reset" );
        //String message2 = contentBrowsePanel.waitForNotificationMessage();
        sleep( 3000 );
        saveScreenshot( "image-reset-to-template" );

        then: "site has been reset to default template, image from the template appeared in the page editor"
        wizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.isImagePresent( IMAGE_DISPLAY_NAME_FOR_TEMPLATE );

        and: "expected notification message should be displayed"
        message1 == String.format( Application.CONTENT_SAVED, SITE.getName() );
    }

    def "GIVEN site with 2 image-components is opened WHEN swapping components by DnD THEN components should be displayed in the new order"()
    {
        given: "site with 2 image-components is opened"
        filterPanel.typeSearchText( SITE_WITH_COMPONENTS_NAME )
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        wizard.unlockPageEditorAndSwitchToContentStudio().showComponentView();
        LiveFormPanel liveFormPanel = addImageComponent( TEST_IMAGE_SWAP );
        saveScreenshot( "two-images-in-view" );
        LinkedList<String> before = liveFormPanel.getImageNames();

        when: "swapping of components by DnD"
        wizard.switchToDefaultWindow();
        wizard.showComponentView();
        PageComponentsViewDialog pageComponentsView = new PageComponentsViewDialog( getSession() );
        pageComponentsView.swapComponents( IMAGE_DISPLAY_NAME_FOR_TEMPLATE, TEST_IMAGE_SWAP );
        wizard.save();
        sleep( 2000 );
        wizard.switchToLiveEditFrame();
        LinkedList<String> after = liveFormPanel.getImageNames();
        saveScreenshot( "page_comp_view_images-swapped" );

        then: "images should be swapped"
        before.getFirst() == TEST_IMAGE_NAME_SWAP;
        and: "components should be displayed in the new order"
        after.getFirst() == IMAGE_NAME_FOR_TEMPLATE;
    }

    private LiveFormPanel addImageComponent( String imageName )
    {
        PageComponentsViewDialog pageComponentsView = new PageComponentsViewDialog( getSession() );
        pageComponentsView.openMenu( "country" ).selectMenuItem( "Insert", "Image" );
        pageComponentsView.doCloseDialog();
        ContentWizardPanel wizard = new ContentWizardPanel( getSession() );
        wizard.switchToLiveEditFrame();
        ImageComponentView imageComponentView = new ImageComponentView( getSession() );
        imageComponentView.selectImageFromOptions( imageName );
        sleep( 1000 );
        return new LiveFormPanel( getSession() );
    }

    private void addTemplateWithImage( Content template, String imageName )
    {
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable( "_templates" ).clickToolbarNew().selectContentType(
            template.getContentTypeName() ).showPageEditor().typeData( template );
        wizard.switchToDefaultWindow();
        wizard.showComponentView();
        addImageComponent( imageName );
        wizard.closeBrowserTab().switchToBrowsePanelTab();
    }
}
