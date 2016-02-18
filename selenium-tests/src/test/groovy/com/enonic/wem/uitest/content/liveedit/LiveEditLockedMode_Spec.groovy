package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.PageTemplateFormViewPanel
import com.enonic.autotests.pages.form.liveedit.ItemViewContextMenu
import com.enonic.autotests.utils.NameHelper
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import com.enonic.xp.content.ContentPath
import com.enonic.xp.data.PropertyTree
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared
import spock.lang.Stepwise

import static com.enonic.autotests.utils.SleepHelper.sleep

@Stepwise
class LiveEditLockedMode_Spec
    extends BaseContentSpec
{
    @Shared
    String SIT_NAME = NameHelper.uniqueName( "lockedmode" );


    def "create a site based on the 'My First App' application"()
    {
        when: "add a site, based on the test application"
        addSite( SIT_NAME );

        then: "test site should be listed"
        filterPanel.typeSearchText( SIT_NAME );
        contentBrowsePanel.exists( SIT_NAME );
    }

    def "GIVEN existing site WHEN site selected and opened for edit THEN 'Page Editor' shown and buttons 'Show Component view'  'Show Inspection panel' are not visible"()
    {
        given: "add a site, based on the test application"
        filterPanel.typeSearchText( SIT_NAME );

        when: "site selected and opened for edit"
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable( SIT_NAME ).clickToolbarEdit();

        then: "Page Editor is shown by default"
        wizard.isLiveEditFrameDisplayed();
        and: "button show-hide the 'Page Editor' is displayed"
        wizard.isShowPageEditorButtonDisplayed();

        and: "'Show Component view' displayed on toolbar"
        wizard.isShowComponentViewButtonDisplayed()

        and: "'Show Inspection panel' displayed on toolbar"
        wizard.isShowInspectionPanelButtonDisplayed();
    }

    def "GIVEN existing site WHEN template added THEN it listed beneath the _templates folder"()
    {
        given: "add a site, based on the test application"
        filterPanel.typeSearchText( SIT_NAME );
        contentBrowsePanel.expandContent( ContentPath.from( SIT_NAME ) );
        Content template = buildPageTemplate( COUNTRY_REGION_PAGE_CONTROLLER, "site", "site-template", SIT_NAME );

        when: "test site should be listed"
        ContentWizardPanel wizard = contentBrowsePanel.selectContentInTable( "_templates" ).clickToolbarNew().selectContentType(
            template.getContentTypeName() ).typeData( template );
        switchToContentStudioWindow();
        wizard.save().close( template.getDisplayName() );
        sleep( 500 );

        then: "new page-template listed"
        filterPanel.typeSearchText( template.getName() );
        contentBrowsePanel.exists( template.getName() );
    }

    def "WHEN site opened for edit AND LiveEdit frame shown  THEN shader is applied to entire page"()
    {
        given: "add a site, based on the test application"
        filterPanel.typeSearchText( SIT_NAME );


        when: "test site should be listed"
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SIT_NAME ).clickToolbarEdit().showPageEditor();
        then: "shader is applied to entire page and LiveEdit locked"
        wizard.isLiveEditLocked();
    }

    def "WHEN site opened for edit AND 'Page Editor' is shown AND right button clicked on the frame THEN context menu for page appears"()
    {
        when: "site opened for edit"
        filterPanel.typeSearchText( SIT_NAME );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SIT_NAME ).clickToolbarEdit();
        ItemViewContextMenu itemViewContextMenu = wizard.showItemViewContextMenu();

        then: "context menu for page appears"
        itemViewContextMenu.isOpened();
    }

    def "GIVEN site opened for edit WHEN the 'Customize' menu item selected THEN LiveEdit not locked"()
    {
        given: "site opened for edit"
        filterPanel.typeSearchText( SIT_NAME );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( SIT_NAME ).clickToolbarEdit();
        ItemViewContextMenu itemViewContextMenu = wizard.showItemViewContextMenu();

        when: "the 'Customize' menu item selected"
        itemViewContextMenu.clickOnCustomizeMenuItem();
        switchToContentStudioWindow();

        then: "LiveEdit not locked"
        !wizard.isLiveEditLocked();
    }

    private void addSite( String name )
    {
        Content site;
        site = buildSite( name );
        contentBrowsePanel.clickToolbarNew().selectContentType( site.getContentTypeName() ).typeData( site ).save().close(
            site.getDisplayName() );
        TestUtils.saveScreenshot( getSession(), NameHelper.uniqueName( "locked mode test" ) );
    }

    private Content buildSite( String siteName )
    {
        PropertyTree data = new PropertyTree();
        data.addString( "applicationKey", "My First App" );
        data.addStrings( "description", "locked mode test" )
        Content site = Content.builder().
            parent( ContentPath.ROOT ).
            name( siteName ).
            displayName( "locked-mode-test" ).
            parent( ContentPath.ROOT ).
            contentType( ContentTypeName.site() ).data( data ).
            build();
        return site;
    }

    protected Content buildPageTemplate( String pageDescriptorName, String supports, String displayName, String parentName )
    {
        String name = "template";
        PropertyTree data = new PropertyTree();
        data.addStrings( PageTemplateFormViewPanel.PAGE_CONTROLLER, pageDescriptorName );
        data.addStrings( PageTemplateFormViewPanel.SUPPORTS, supports );

        Content pageTemplate = Content.builder().
            name( NameHelper.uniqueName( name ) ).
            displayName( displayName ).
            parent( ContentPath.from( parentName ) ).
            contentType( ContentTypeName.pageTemplate() ).data( data ).
            build();
        return pageTemplate;
    }
}
