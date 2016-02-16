package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.liveedit.ImageComponentView
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
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
    String SITE_WITH_COMPONENTS_NAME = NameHelper.uniqueName( "page-component-reset" );

    @Shared
    Content PAGE_TEMPLATE;

    @Shared
    String TEMPLATE_DISPLAY_NAME = "country template";

    @Shared
    String IMAGE_FOR_TEMPLATE = "book.jpg";

    @Shared
    String TEST_IMAGE = "man2.jpg";

    @Shared
    String TEST_IMAGE_SWAP = "whale.jpg";


    def "setup:add site with a template"()
    {
        given: "existing Site based on 'My First App'"
        Content site = buildMyFirstAppSite( SITE_WITH_COMPONENTS_NAME );
        addSiteBasedOnFirstApp( site );
        contentBrowsePanel.expandContent( ContentPath.from( SITE_WITH_COMPONENTS_NAME ) );
        PAGE_TEMPLATE = buildPageTemplate( COUNTRY_REGION_PAGE_CONTROLLER, TEMPLATE_SUPPORTS_SITE, TEMPLATE_DISPLAY_NAME,
                                           SITE_WITH_COMPONENTS_NAME );

        when: "'Templates' folder selected and new page-template added"
        addTemplateWithImage( PAGE_TEMPLATE, IMAGE_FOR_TEMPLATE )
        sleep( 500 );

        then: "new page-template listed"
        filterPanel.typeSearchText( PAGE_TEMPLATE.getName() );
        contentBrowsePanel.exists( PAGE_TEMPLATE.getName() );
    }


    def "GIVEN opened a site in wizard AND one component was replaced WHEN root element in page component dialog was selected and 'Reset' menu item selected THEN site should be reset to default template"()
    {
        given: "site opened for edit  and site saved"
        filterPanel.typeSearchText( SITE_WITH_COMPONENTS_NAME )
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE_WITH_COMPONENTS_NAME ).clickToolbarEdit();
        wizard.unlockPageEditorAndSwitchToContentStudio().showComponentView();
        TestUtils.saveScreenshot( getSession(), "image-from-template" );

        and: "and one component was replaced"
        PageComponentsViewDialog pageComponentsView = new PageComponentsViewDialog( getSession() );
        pageComponentsView.openMenu( IMAGE_FOR_TEMPLATE ).selectMenuItem( "Remove" );

        pageComponentsView.openMenu( "country" ).selectMenuItem( "Insert", "Image" );
        pageComponentsView.doCloseDialog();
        wizard.switchToLiveEditFrame(  );

        and: "new image inserted"
        ImageComponentView imageComponentView = new ImageComponentView( getSession() );
        imageComponentView.selectImageItemFromList( TEST_IMAGE );
        switchToContentStudioWindow();

        and: "wizard saved"
        wizard.save();
        TestUtils.saveScreenshot( getSession(), "new-image-set" );

        when: "root element in 'page component' dialog was selected and 'Reset' menu item selected"
        wizard.showComponentView();
        pageComponentsView.openMenu( "country template" ).selectMenuItem( "Reset" );
        sleep( 5000 );
        TestUtils.saveScreenshot( getSession(), "image-reset-to-template" );

        then: "site has been reset to default template, image from template appeared in the page editor"
        wizard.switchToLiveEditFrame();
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.isImagePresent( IMAGE_FOR_TEMPLATE );
    }

    def "GIVEN site with 2 image-components WHEN swapping components by DnD THEN components shown correctly"()
    {
        given: "site with 2 image-components"
        filterPanel.typeSearchText( SITE_WITH_COMPONENTS_NAME )
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SITE_WITH_COMPONENTS_NAME ).clickToolbarEdit();
        wizard.unlockPageEditorAndSwitchToContentStudio().showComponentView();
        LiveFormPanel liveFormPanel = addImageComponent( TEST_IMAGE_SWAP );
        TestUtils.saveScreenshot( getSession(), "two-images-in-view" );
        LinkedList<String> before = liveFormPanel.getImageNames();

        when: "swapping components by DnD"
        switchToContentStudioWindow();
        wizard.showComponentView();
        PageComponentsViewDialog pageComponentsView = new PageComponentsViewDialog( getSession() );
        pageComponentsView.swapComponents( IMAGE_FOR_TEMPLATE, TEST_IMAGE_SWAP );
        wizard.save();
        sleep( 2000 );
        wizard.switchToLiveEditFrame();
        LinkedList<String> after = liveFormPanel.getImageNames();

        then: "images swapped"
        before.getFirst() == TEST_IMAGE_SWAP;
        and:
        after.getFirst() == IMAGE_FOR_TEMPLATE;

    }

    private LiveFormPanel addImageComponent( String imageName )
    {
        PageComponentsViewDialog pageComponentsView = new PageComponentsViewDialog( getSession() );
        pageComponentsView.openMenu( "country" ).selectMenuItem( "Insert", "Image" );
        pageComponentsView.doCloseDialog();
        ContentWizardPanel wizard = new ContentWizardPanel(getSession(  ));
        wizard.switchToLiveEditFrame(  );
        ImageComponentView imageComponentView = new ImageComponentView( getSession() );
        imageComponentView.selectImageItemFromList( imageName );
        return new LiveFormPanel( getSession() );
    }

    private void addTemplateWithImage( Content template, String imageName )
    {
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable( "_templates" ).clickToolbarNew().selectContentType(
            template.getContentTypeName() ).showPageEditor().typeData( template );
        switchToContentStudioWindow();
        wizard.showComponentView();
        addImageComponent( imageName );
        switchToContentStudioWindow();
        wizard.save().close( template.getDisplayName() );
    }
}
