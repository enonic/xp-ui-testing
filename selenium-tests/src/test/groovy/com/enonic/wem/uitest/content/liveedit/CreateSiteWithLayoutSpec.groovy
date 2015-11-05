package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.PageComponentsViewDialog
import com.enonic.autotests.pages.form.SiteFormViewPanel
import com.enonic.autotests.pages.form.liveedit.ImageComponentView
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class CreateSiteWithLayoutSpec
    extends BaseContentSpec
{

    @Shared
    Content SITE

    @Shared
    String MAIN_REGION_PAGE_DESCRIPTOR_NAME = "main region";

    @Shared
    Content pageTemplate

    @Shared
    String SITE_DISPLAY_NAME = "layout-page-template";

    @Shared
    String SUPPORTS = ContentTypeName.site().toString();

    def "GIVEN creating new Site based on 'Simple site'  WHEN saved and wizard closed THEN new site should be present"()
    {
        given:
        SITE = buildSimpleSiteWitLayout();
        when: "data typed and saved and wizard closed"
        contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() ).typeData( SITE ).save().close(
            SITE.getDisplayName() );

        then: "new site should be present"
        contentBrowsePanel.exists( SITE.getName() );
    }

    def "GIVEN existing site, based on 'Simple site' app WHEN site expanded and templates folder selected AND page-template added  THEN new template should be present in a 'Templates' folder"()
    {
        given:
        pageTemplate = buildPageTemplate( MAIN_REGION_PAGE_DESCRIPTOR_NAME, SUPPORTS, SITE_DISPLAY_NAME, SITE.getName() );
        filterPanel.typeSearchText( SITE.getName() );
        contentBrowsePanel.expandContent( ContentPath.from( SITE.getName() ) );

        when: "'Templates' folder selected and new page-template added"
        contentBrowsePanel.selectContentInTable( "_templates" ).clickToolbarNew().selectContentType(
            pageTemplate.getContentTypeName() ).showPageEditor().typeData( pageTemplate ).save().close( pageTemplate.getDisplayName() );

        contentBrowsePanel.expandContent( ContentPath.from( SITE.getName() ) );
        contentBrowsePanel.expandContent( ContentPath.from( "_templates" ) );
        TestUtils.saveScreenshot( getSession(), "simple_template" );

        then: "new template should be listed beneath a 'Templates' folder"
        contentBrowsePanel.exists( pageTemplate.getName() );
    }

    def "GIVEN 'Page Components' opened WHEN menu for 'main region' clicked and 'insert' menu-item selected AND 'layout'-item clicked THEN new layout present on the live edit frame"()
    {
        given: "'Page Components' opened"
        filterPanel.typeSearchText( pageTemplate.getName() )
        contentBrowsePanel.selectContentInTable( pageTemplate.getName() ).clickToolbarEdit().showComponentView();
        PageComponentsViewDialog pageComponentsView = new PageComponentsViewDialog( getSession() );

        when: "'Insert/Layout' menu items clicked and layout with 3 columns selected"
        pageComponentsView.openMenu( "main" ).selectMenuItem( "Insert", "Layout" );
        NavigatorHelper.switchToLiveEditFrame( getSession() );
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.getLayoutComponentView().selectLayout( "3-col" );
        TestUtils.saveScreenshot( getSession(), "layout_selected" );

        then: "layout-component appears in the 'live edit' frame and number of regions is 3"
        liveFormPanel.isLayoutComponentPresent();
        and: "tree column present in layout"
        liveFormPanel.getLayoutColumnNumber() == 3;
    }

    def "'Page Components' opened WHEN menu for 'left region' clicked and 'insert' menu-item selected AND 'image'-item clicked THEN new image in the left region inserted"()
    {
        given: "'Page Components' opened"
        filterPanel.typeSearchText( pageTemplate.getName() )
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable(
            pageTemplate.getName() ).clickToolbarEdit().showComponentView();
        PageComponentsViewDialog pageComponentsView = new PageComponentsViewDialog( getSession() );

        when: "menu for 'left region' clicked and 'insert' menu-item selected AND 'image'-item clicked"
        pageComponentsView.openMenu( "left" ).selectMenuItem( "Insert", "Image" );
        ImageComponentView imageComponentView = new ImageComponentView( getSession() );
        imageComponentView.selectImageItemFromList( "telk.png" )

        NavigatorHelper.switchToContentManagerFrame( getSession() );
        wizard.save();
        TestUtils.saveScreenshot( getSession(), "left_inserted" );

        then: "new image inserted in the left-region "
        NavigatorHelper.switchToLiveEditFrame( getSession() );
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.getNumberImagesInLayout() == 1;
    }

    def "GIVEN 'Page Components' opened WHEN menu for 'center region' clicked and 'insert' menu-item selected AND 'image'-item clicked THEN new image inserted in the center-region "()
    {
        given: "'Page Components' opened"
        filterPanel.typeSearchText( pageTemplate.getName() )
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable(
            pageTemplate.getName() ).clickToolbarEdit().showComponentView();
        PageComponentsViewDialog pageComponentsView = new PageComponentsViewDialog( getSession() );

        when: "menu for 'center region' clicked and 'insert' menu-item selected AND 'image'-item clicked"
        pageComponentsView.openMenu( "center" ).selectMenuItem( "Insert", "Image" );
        ImageComponentView imageComponentView = new ImageComponentView( getSession() );
        imageComponentView.selectImageItemFromList( "geek.png" )
        NavigatorHelper.switchToContentManagerFrame( getSession() );
        wizard.save();
        TestUtils.saveScreenshot( getSession(), "center_inserted" );

        then: "new image inserted in the center-region "
        NavigatorHelper.switchToLiveEditFrame( getSession() );
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.getNumberImagesInLayout() == 2;
    }

    def "GIVEN 'Page Components' opened WHEN menu for 'right region' clicked and 'insert' menu-item selected AND 'image'-item clicked THEN new image inserted in the right-region "()
    {
        given: "'Page Components' opened"
        filterPanel.typeSearchText( pageTemplate.getName() )
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable(
            pageTemplate.getName() ).clickToolbarEdit().showComponentView();
        PageComponentsViewDialog pageComponentsView = new PageComponentsViewDialog( getSession() );

        when: "menu for right region clicked and 'insert' menu-item selected AND 'image'-item clicked"
        pageComponentsView.openMenu( "right" ).selectMenuItem( "Insert", "Image" );
        ImageComponentView imageComponentView = new ImageComponentView( getSession() );
        imageComponentView.selectImageItemFromList( "foss.jpg" )
        NavigatorHelper.switchToContentManagerFrame( getSession() );
        wizard.save();
        TestUtils.saveScreenshot( getSession(), "right_inserted" );

        then: "new image inserted to the right-region"
        NavigatorHelper.switchToLiveEditFrame( getSession() );
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        liveFormPanel.getNumberImagesInLayout() == 3;
    }

    def "GIVEN a layout with inserted 3 images 'Page Components' opened WHEN menu for one of them images selected AND 'reset' menu-item selected THEN removed image not present in layout"()
    {
        given: "'Page Components' opened"
        filterPanel.typeSearchText( pageTemplate.getName() )
        contentBrowsePanel.selectContentInTable( pageTemplate.getName() ).clickToolbarEdit().showComponentView();
        PageComponentsViewDialog pageComponentsView = new PageComponentsViewDialog( getSession() );

        when: "menu for image clicked and 'reset' menu-item selected"
        pageComponentsView.openMenu( "telk.png" ).selectMenuItem( "Reset" );
        NavigatorHelper.switchToLiveEditFrame( getSession() );
        LiveFormPanel liveFormPanel = new LiveFormPanel( getSession() );
        TestUtils.saveScreenshot( getSession(), "reset_image" );

        then: "number of images in layout reduced"
        liveFormPanel.getNumberImagesInLayout() == 2;

        and: "but number of components not changed"
        liveFormPanel.getNumberImageComponentsInLayout() == 3;
        and: "image with the required name no longer present on LiveEdit"
        !liveFormPanel.isImagePresent( "telk.png" );
    }


    private Content buildSimpleSiteWitLayout()
    {
        String name = NameHelper.uniqueName( "site" );
        PropertyTree data = new PropertyTree();
        data.addString( SiteFormViewPanel.APP_KEY, "Simple Site App" );
        data.addStrings( "description", "simple site " )
        Content site = Content.builder().
            parent( ContentPath.ROOT ).
            name( name ).
            displayName( "site with layout" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.site() ).data( data ).
            build();
        return site;
    }
}