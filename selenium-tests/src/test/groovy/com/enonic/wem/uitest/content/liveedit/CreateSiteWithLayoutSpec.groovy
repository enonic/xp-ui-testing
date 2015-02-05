package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.liveedit.ContextWindow
import com.enonic.autotests.pages.form.liveedit.LayoutComponentView
import com.enonic.autotests.pages.form.liveedit.LiveFormPanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.api.content.ContentPath
import com.enonic.wem.api.data.PropertyTree
import com.enonic.wem.api.schema.content.ContentTypeName
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class CreateSiteWithLayoutSpec
    extends BaseGebSpec
{

    @Shared
    ContentBrowsePanel contentBrowsePanel;

    @Shared
    String SITE_NAME;

    @Shared
    String LIVE_EDIT_FRAME_SITE_HEADER = "//h1[text()='Simple Site, with a view and region!']";

    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }


    def "GIVEN creating new Site based on 'Simple site'  WHEN saved and wizard closed THEN new site should be listed"()
    {
        given:
        Content site = buildSimpleSiteWitLayout();
        when: "data typed and saved and wizard closed"
        contentBrowsePanel.clickToolbarNew().selectContentType( site.getContentTypeName() ).typeData( site ).save().close(
            site.getDisplayName() );

        then: " new site should be listed"
        contentBrowsePanel.exists( site.getPath() );

    }


    def "GIVEN exists on root a site, based on 'Simple site' WHEN site expanded and templates folder selected AND page-template added  THEN new template should be listed beneath a 'Templates' folder"()
    {
        given:
        Content pageTemplate = buildPageTemplate();

        when: "site expanded and 'Templates' folder selected and page-template added"
        contentBrowsePanel.expandContent( ContentPath.from( SITE_NAME ) );
        contentBrowsePanel.selectContentInTable( ContentPath.from( SITE_NAME + "/_templates" ) ).clickToolbarNew().selectContentType(
            pageTemplate.getContentTypeName() ).typeData( pageTemplate ).save().close( pageTemplate.getDisplayName() );
        contentBrowsePanel.expandContent( ContentPath.from( SITE_NAME + "/_templates" ) );
        TestUtils.saveScreenshot( getSession(), "xeon_template" );

        then: " new template should be listed beneath a 'Templates' folder"
        contentBrowsePanel.exists( pageTemplate.getPath() );
    }


    def "GIVEN site opened for edit WHEN 'toggle window' button on toolbar clicked  THEN ContextWindow  appears"()
    {
        given: "site based on Xeon opened for edit"
        ContentWizardPanel contentWizard = contentBrowsePanel.selectContentInTable( ContentPath.from( SITE_NAME ) ).clickToolbarEdit();

        when: "button on toolbar clicked"
        ContextWindow contextWindow = contentWizard.showContextWindow();

        then: "context window appears"
        contextWindow.isContextWindowPresent();
    }


    def "GIVEN site opened for edit and context window showed WHEN ContextWindow  opened in live edit AND 3 column layout added AND site saved THEN new layout present on the live edit frame"()
    {
        given:
        ContentWizardPanel contentWizard = contentBrowsePanel.selectContentInTable( ContentPath.from( SITE_NAME ) ).clickToolbarEdit();
        contentWizard.unlockLiveEdit();
        ContextWindow contextWindow = contentWizard.showContextWindow().clickOnInsertLink();
        TestUtils.saveScreenshot( getSession(), "drag_and_drop" )

        when: "3 column layout dragged into 'live edit' frame and site saved"
        LayoutComponentView layoutComponentView = contextWindow.addComponentByDragAndDrop( "layout", null, LIVE_EDIT_FRAME_SITE_HEADER );
        TestUtils.saveScreenshot( getSession(), "simple_layoutcomponent" );
        LiveFormPanel liveFormPanel = layoutComponentView.selectLayout( "3-col" );
        TestUtils.saveScreenshot( getSession(), "layout_3col" );
        NavigatorHelper.switchToContentManagerFrame( getSession() );
        contentWizard.save();

        then: "layout component appears in the 'live edit' frame and number of regions is 3"
        NavigatorHelper.switchToLiveEditFrame( getSession() );
        liveFormPanel.isLayoutComponentPresent() && liveFormPanel.getLayoutColumnNumber() == 3;


    }


    private Content buildSimpleSiteWitLayout()
    {
        SITE_NAME = NameHelper.uniqueName( "site" );
        PropertyTree data = new PropertyTree();
        data.addString( "moduleKey", "Simple Site Module" );
        data.addStrings( "description", "simple site " )
        Content site = Content.builder().
            parent( ContentPath.ROOT ).
            name( SITE_NAME ).
            displayName( "simple-site-module-based" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.site() ).data( data ).
            build();
        return site;
    }

    private Content buildPageTemplate()
    {
        String name = "pagetemplate";

        PropertyTree data = new PropertyTree();
        data.addStrings( "nameInMenu", "item1" );
        data.addStrings( "pageController", "Simple page" );

        Content pageTemplate = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( "simple-page-template" ).
            parent( ContentPath.from( SITE_NAME ) ).
            contentType( ContentTypeName.pageTemplate() ).data( data ).
            build();
        return pageTemplate;
    }
}