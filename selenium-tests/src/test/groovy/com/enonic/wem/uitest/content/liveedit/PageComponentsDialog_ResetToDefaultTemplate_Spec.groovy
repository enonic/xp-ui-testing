package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.liveedit.ImageComponentView
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.content.ContentPath
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class PageComponentsDialog_ResetToDefaultTemplate_Spec
    extends BaseContentSpec
{
    @Shared
    String SITE_WITH_COMPONENTS_NAME = "page-component-reset";

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


    def "setup:add site with a template"()
    {
        given: "existing Site based on 'My First App'"
        SITE = buildMyFirstAppSite( SITE_WITH_COMPONENTS_NAME );
        addSite( SITE );
        contentBrowsePanel.expandContent( ContentPath.from( SITE.getName() ) );
        PAGE_TEMPLATE = buildPageTemplate( COUNTRY_REGION_PAGE_CONTROLLER, TEMPLATE_SUPPORTS_SITE, TEMPLATE_DISPLAY_NAME,
                                           SITE.getName() );

        when: "'Templates' folder selected and new page-template added"
        addTemplateWithImage( PAGE_TEMPLATE, IMAGE_DISPLAY_NAME_FOR_TEMPLATE )
        sleep( 500 );

        then: "new page-template listed"
        filterPanel.typeSearchText( PAGE_TEMPLATE.getName() );
        contentBrowsePanel.exists( PAGE_TEMPLATE.getName() );
    }


    def "GIVEN the site is opened AND one image was replaced WHEN root element in page component dialog was selected and 'Reset' menu item selected THEN site should be reset to default template"()
    {
        given: "site opened for edit  and site saved"
        filterPanel.typeSearchText( SITE.getName() )
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        wizard.unlockPageEditorAndSwitchToContentStudio().showComponentView();
        saveScreenshot( "image-from-template" );

        and: "and an image has been removed"
        PageComponentsViewDialog pageComponentsView = new PageComponentsViewDialog( getSession() );
        pageComponentsView.openMenu( IMAGE_DISPLAY_NAME_FOR_TEMPLATE ).selectMenuItem( "Remove" );
        pageComponentsView.openMenu( "country" ).selectMenuItem( "Insert", "Image" );
        pageComponentsView.doCloseDialog();
        wizard.switchToLiveEditFrame();

        and: "and new image was inserted"
        ImageComponentView imageComponentView = new ImageComponentView( getSession() );
        imageComponentView.selectImageItemFromList( TEST_IMAGE );

        and: "the content is saved"
        wizard.save();
        saveScreenshot( "new-image-set" );

        when: "root element in 'page component' dialog was selected and 'Reset' menu item selected"
        wizard.showComponentView();
        pageComponentsView.openMenu( PAGE_CONTROLLER_NAME ).selectMenuItem( "Reset" );
        sleep( 4000 );
        saveScreenshot( "image-reset-to-template" );

        then: "site has been reset to default template, image from the template appeared in the page editor"
        wizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.isImagePresent( IMAGE_DISPLAY_NAME_FOR_TEMPLATE );
    }

    def "GIVEN site with 2 image-components WHEN swapping components by DnD THEN components shown correctly"()
    {
        given: "site with 2 image-components"
        filterPanel.typeSearchText( SITE_WITH_COMPONENTS_NAME )
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE.getName() ).clickToolbarEdit();
        wizard.unlockPageEditorAndSwitchToContentStudio().showComponentView();
        LiveFormPanel liveFormPanel = addImageComponent( TEST_IMAGE_SWAP );
        saveScreenshot( "two-images-in-view" );
        LinkedList<String> before = liveFormPanel.getImageNames();

        when: "swapping components by DnD"
        wizard.switchToDefaultWindow();
        wizard.showComponentView();
        PageComponentsViewDialog pageComponentsView = new PageComponentsViewDialog( getSession() );
        pageComponentsView.swapComponents( IMAGE_DISPLAY_NAME_FOR_TEMPLATE, TEST_IMAGE_SWAP );
        wizard.save();
        sleep( 2000 );
        wizard.switchToLiveEditFrame();
        LinkedList<String> after = liveFormPanel.getImageNames();
        saveScreenshot( "page_comp_view_images-swapped" );

        then: "images swapped"
        before.getFirst() == TEST_IMAGE_NAME_SWAP;
        and:
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
        imageComponentView.selectImageItemFromList( imageName );
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
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();
    }
}
