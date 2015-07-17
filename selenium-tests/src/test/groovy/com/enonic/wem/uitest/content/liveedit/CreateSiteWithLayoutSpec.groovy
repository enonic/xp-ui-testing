package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.liveedit.ContextWindow
import com.enonic.autotests.pages.form.liveedit.ImageComponentView
import com.enonic.autotests.pages.form.liveedit.LayoutComponentView
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

import static com.enonic.autotests.utils.SleepHelper.sleep

@Stepwise
class CreateSiteWithLayoutSpec
    extends BaseContentSpec
{

    @Shared
    Content SITE

    @Shared
    String LIVE_EDIT_FRAME_SITE_HEADER = "//h1[text()='Simple Site']";

    @Shared
    String MAIN_REGION_PAGE_DESCRIPTOR_NAME = "main region";


    def "GIVEN creating new Site based on 'Simple site'  WHEN saved and wizard closed THEN new site should be listed"()
    {
        given:
        SITE = buildSimpleSiteWitLayout();
        when: "data typed and saved and wizard closed"
        contentBrowsePanel.clickToolbarNew().selectContentType( SITE.getContentTypeName() ).typeData( SITE ).save().close(
            SITE.getDisplayName() );

        then: " new site should be listed"
        contentBrowsePanel.exists( SITE.getName() );

    }


    def "GIVEN exists on root a site, based on 'Simple site' WHEN site expanded and templates folder selected AND page-template added  THEN new template should be listed beneath a 'Templates' folder"()
    {
        given:
        Content pageTemplate = buildPageTemplate( MAIN_REGION_PAGE_DESCRIPTOR_NAME );
        filterPanel.typeSearchText( SITE.getName() );
        contentBrowsePanel.expandContent( ContentPath.from( SITE.getName() ) );

        when: "'Templates' folder selected and new page-template added"
        contentBrowsePanel.selectContentInTable( "_templates" ).clickToolbarNew().selectContentType(
            pageTemplate.getContentTypeName() ).typeData( pageTemplate ).save().close( pageTemplate.getDisplayName() );
        sleep( 2000 );
        contentBrowsePanel.expandContent( ContentPath.from( SITE.getName() ) );
        contentBrowsePanel.expandContent( ContentPath.from( "_templates" ) );
        TestUtils.saveScreenshot( getSession(), "simple_template" );

        then: " new template should be listed beneath a 'Templates' folder"
        contentBrowsePanel.exists( pageTemplate.getName() );
    }

    @Ignore
    def "GIVEN site opened for edit WHEN 'toggle window' button on toolbar clicked  THEN ContextWindow  appears"()
    {
        given: "site opened for edit"
        ContentWizardPanel contentWizard = contentBrowsePanel.selectContentInTable( SITE.getName() ).clickToolbarEdit();

        when: "button on toolbar clicked"
        ContextWindow contextWindow = contentWizard.showContextWindow();

        then: "context window appears"
        contextWindow.isContextWindowPresent();
    }

    @Ignore
    def "WHEN site opened for edit  AND page template is automatic THEN Live Edit frame should be locked"()
    {
        when: "site opened for edit"
        ContentWizardPanel contentWizard = contentBrowsePanel.selectContentInTable( SITE.getName() ).clickToolbarEdit();

        then: " the 'Live Edit' frame should be locked"
        contentWizard.isLiveEditLocked();
    }

    @Ignore
    def "GIVEN site opened for edit  AND page template is automatic WHEN link 'Unlock' clicked on 'Live Edit' frame  THEN Live Edit frame is unlocked"()
    {
        given: "site opened for edit"
        ContentWizardPanel contentWizard = contentBrowsePanel.selectContentInTable( SITE.getName() ).clickToolbarEdit();

        when:
        contentWizard.unlockLiveEdit();

        then: "the 'Live Edit' frame should be locked"
        !contentWizard.isLiveEditLocked();
    }

    @Ignore
    def "GIVEN site opened for edit WHEN 'layout item'  dragged AND 3 column layout added AND site saved THEN new layout present on the live edit frame"()
    {
        given:
        ContentWizardPanel contentWizard = contentBrowsePanel.selectContentInTable( SITE.getName() ).clickToolbarEdit();
        contentWizard.unlockLiveEdit();
        ContextWindow contextWindow = contentWizard.showContextWindow().clickOnInsertLink();
        TestUtils.saveScreenshot( getSession(), "drag_and_drop" )

        when: "3 column layout dragged into 'live edit' frame and site saved"
        LayoutComponentView layoutComponentView = contextWindow.addLayoutByDragAndDrop( LIVE_EDIT_FRAME_SITE_HEADER );
        TestUtils.saveScreenshot( getSession(), "simple_layoutcomponent" );
        LiveFormPanel liveFormPanel = layoutComponentView.selectLayout( "3-col" );
        TestUtils.saveScreenshot( getSession(), "layout_3col" );
        NavigatorHelper.switchToContentManagerFrame( getSession() );
        contentWizard.save();

        then: "layout component appears in the 'live edit' frame and number of regions is 3"
        NavigatorHelper.switchToLiveEditFrame( getSession() );
        liveFormPanel.isLayoutComponentPresent() && liveFormPanel.getLayoutColumnNumber() == 3;


    }

    @Ignore
    def "GIVEN site opened for edit WHEN 'image' item dragged and dropped on to left region AND site saved THEN layout with one image present on the live edit frame"()
    {
        given:
        ContentWizardPanel contentWizard = contentBrowsePanel.selectContentInTable( SITE.getName() ).clickToolbarEdit();

        ContextWindow contextWindow = contentWizard.showContextWindow().clickOnInsertLink();
        TestUtils.saveScreenshot( getSession(), "insert_left" )

        when: "3 images dragged into 'live edit' frame and site saved"
        ImageComponentView imageComponentView = contextWindow.insertImageByDragAndDrop( "left", LIVE_EDIT_FRAME_SITE_HEADER );
        LiveFormPanel liveFormPanel = imageComponentView.selectImageItemFromList( "bro.jpg" );
        TestUtils.saveScreenshot( getSession(), "left_inserted" );
        NavigatorHelper.switchToContentManagerFrame( getSession() );
        contentWizard.save();

        then: "new image present in the 'live edit' frame"
        NavigatorHelper.switchToLiveEditFrame( getSession() );
        liveFormPanel.getNumberImagesInLayout() == 1;
    }

    @Ignore
    def "GIVEN site opened for edit WHEN 'image' item dragged and dropped on center region AND site saved THEN layout with two images present on the live edit frame"()
    {
        given:
        ContentWizardPanel contentWizard = contentBrowsePanel.selectContentInTable( SITE.getName() ).clickToolbarEdit();

        ContextWindow contextWindow = contentWizard.showContextWindow().clickOnInsertLink();
        TestUtils.saveScreenshot( getSession(), "insert_images1" )

        when: "3 images dragged into 'live edit' frame and site saved"
        ImageComponentView imageComponentView = contextWindow.insertImageByDragAndDrop( "center", LIVE_EDIT_FRAME_SITE_HEADER );
        LiveFormPanel liveFormPanel = imageComponentView.selectImageItemFromList( "telk.png" );
        TestUtils.saveScreenshot( getSession(), "center_iserted" );
        NavigatorHelper.switchToContentManagerFrame( getSession() );
        contentWizard.save();

        then: "new image present in the 'live edit' frame"
        NavigatorHelper.switchToLiveEditFrame( getSession() );
        liveFormPanel.getNumberImagesInLayout() == 2;
    }

    @Ignore
    def "GIVEN site opened for edit WHEN 'image' item dragged and dropped on right region AND site saved THEN layout with 3 images present on the live edit frame"()
    {
        given:
        ContentWizardPanel contentWizard = contentBrowsePanel.selectContentInTable( SITE.getName() ).clickToolbarEdit();

        ContextWindow contextWindow = contentWizard.showContextWindow().clickOnInsertLink();
        TestUtils.saveScreenshot( getSession(), "insert_images1" )

        when: "3 images dragged into 'live edit' frame and site saved"
        ImageComponentView imageComponentView = contextWindow.insertImageByDragAndDrop( "right", LIVE_EDIT_FRAME_SITE_HEADER );
        TestUtils.saveScreenshot( getSession(), "simple_layoutcomponent" );
        LiveFormPanel liveFormPanel = imageComponentView.selectImageItemFromList( "geek.png" );
        TestUtils.saveScreenshot( getSession(), "right_inserted" );
        NavigatorHelper.switchToContentManagerFrame( getSession() );
        contentWizard.save();

        then: "new image present in the 'live edit' frame"
        NavigatorHelper.switchToLiveEditFrame( getSession() );
        liveFormPanel.getNumberImagesInLayout() == 3;
    }


    private Content buildSimpleSiteWitLayout()
    {
        String name = NameHelper.uniqueName( "site" );
        PropertyTree data = new PropertyTree();
        data.addString( "moduleKey", "Simple Site Module" );
        data.addStrings( "description", "simple site " )
        Content site = Content.builder().
            parent( ContentPath.ROOT ).
            name( name ).
            displayName( "simple-site-module-based" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.site() ).data( data ).
            build();
        return site;
    }

    private Content buildPageTemplate( String pageDescriptorName )
    {
        String name = "pagetemplate";

        PropertyTree data = new PropertyTree();
        data.addStrings( "nameInMenu", "item1" );
        data.addStrings( "pageController", pageDescriptorName );

        Content pageTemplate = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "simple-page-template" ).
            parent( ContentPath.from( SITE.getName() ) ).
            contentType( ContentTypeName.pageTemplate() ).data( data ).
            build();
        return pageTemplate;
    }
}