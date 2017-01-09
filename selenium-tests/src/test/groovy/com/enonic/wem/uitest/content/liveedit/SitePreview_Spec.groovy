package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.schema.content.ContentTypeName
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class SitePreview_Spec
    extends BaseSiteSpec
{
    @Shared
    Content MY_SITE


    def "GIVEN existing site without a template WHEN site is selected THEN 'Preview' on a BrowseToolbar should be disabled"()
    {
        given:
        MY_SITE = buildMyFirstAppSite( "preview" );
        "data typed and saved and wizard closed"
        addSite( MY_SITE );

        when: "site without a template selected"
        findAndSelectContent( MY_SITE.getName() );

        then: "'Preview' on a BrowseToolbar is disabled"
        !contentBrowsePanel.isPreviewButtonEnabled();
    }

    def "GIVEN existing site without a template WHEN site is selected THEN 'Preview' in the ContextMenu should be disabled"()
    {
        when: "site is selected"
        filterPanel.typeSearchText( MY_SITE.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( MY_SITE.getName() );

        then: "'Preview' in the context menu should be disabled"
        contentBrowsePanel.waitUntilItemDisabledInContextMenu( MY_SITE.getName(), "Preview" );
    }

    def "GIVEN existing site without a template WHEN site is opened THEN 'Preview' in the toolbar wizard should be disabled"()
    {
        when: "site is selected and wizard opened"
        ContentWizardPanel wizard = findAndSelectContent( MY_SITE.getName() ).clickToolbarEdit();

        then: "'Preview' in the toolbar wizard should be disabled"
        !wizard.isPreviewButtonEnabled()
    }

    def "GIVEN existing site without a template WHEN the site is opened AND the controller has been selectedselected THEN 'Preview' on a BrowseToolbar becomes enabled"()
    {
        given: "existing site without a template is opened"
        ContentWizardPanel wizard = findAndSelectContent( MY_SITE.getName() ).clickToolbarEdit();

        when: "controller has been selected"
        wizard.selectPageDescriptor( COUNTRY_REGION_TITLE ).save().closeBrowserTab().switchToBrowsePanelTab();
        saveScreenshot( "site-template-preview" );

        then: "'Preview' on the BrowseToolbar should be enabled"
        contentBrowsePanel.isPreviewButtonEnabled();
    }

    def "WHEN existing site with the page-template was selected AND context menu is shown THEN 'Preview' menu item should be enabled in the ContextMenu"()
    {
        when: "existing site with the page-template was selected AND context menu is shown"
        filterPanel.typeSearchText( MY_SITE.getName() );

        then: "'Preview' menu item should be enabled in the ContextMenu"
        contentBrowsePanel.openContextMenuAndWaitUntilItemEnabled( MY_SITE.getName(), "Preview" );
    }

    def "WHEN existing site with the page-template was selected  THEN 'Preview' button should be enabled on the toolbar wizard"()
    {
        when: "site is selected"
        filterPanel.typeSearchText( MY_SITE.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( MY_SITE.getName() ).clickToolbarEdit();

        then: "'Preview' button should be enabled on the toolbar wizard"
        wizard.isPreviewButtonEnabled()
    }
    // test for verifying of XP-4123 (Page Editor inaccessible for a folder)
    def "GIVEN existing site with a selected controller WHEN child-folder has been added to the site THEN 'Show Page Editor' button should be present on the wizard toolbar"()
    {
        given: "existing site with the page-template was selected and 'New' button pressed"
        ContentWizardPanel wizard = findAndSelectContent( MY_SITE.getName() ).clickToolbarNew().selectContentType(
            ContentTypeName.folder() );

        when: "child-folder has been added for the site"
        wizard.typeDisplayName( "test-page-editor-toggler" ).save();
        saveScreenshot( "folder-show-page-editor-toggler" );

        then: "'Show Page Editor' button should be present on the wizard-toolbar"
        wizard.isShowPageEditorButtonDisplayed();
    }
}