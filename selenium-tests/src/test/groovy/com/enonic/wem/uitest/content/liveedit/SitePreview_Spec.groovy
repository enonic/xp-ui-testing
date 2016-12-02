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


    def "GIVEN a existing site without a template WHEN site selected THEN 'Preview' on a BrowseToolbar is disabled"()
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

    def "GIVEN a existing site without a template WHEN site selected THEN 'Preview' in a ContextMenu is disabled"()
    {
        when: "site selected"
        filterPanel.typeSearchText( MY_SITE.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( MY_SITE.getName() );

        then: "'Preview' in the context menu is disabled"
        contentBrowsePanel.waitUntilItemDisabledInContextMenu( MY_SITE.getName(), "Preview" );
    }

    def "GIVEN a existing site without a template WHEN site opened THEN 'Preview' in the toolbar wizard is disabled"()
    {
        when: "site selected and wizard opened"
        ContentWizardPanel wizard = findAndSelectContent( MY_SITE.getName() ).clickToolbarEdit();

        then: "'Preview' in the toolbar wizard is disabled"
        !wizard.isPreviewButtonEnabled()
    }

    def "GIVEN existing site with a template WHEN controller selected THEN 'Preview' on a BrowseToolbar becomes enabled"()
    {
        given: "existing site with a template"
        ContentWizardPanel wizard = findAndSelectContent( MY_SITE.getName() ).clickToolbarEdit();

        when: "controller is selected"
        wizard.selectPageDescriptor( COUNTRY_REGION_TITLE ).save().closeBrowserTab().switchToBrowsePanelTab();
        saveScreenshot( "site-template-preview" );

        then: "'Preview' on the BrowseToolbar is enabled"
        contentBrowsePanel.isPreviewButtonEnabled();
    }

    def "WHEN site with a page template selected THEN 'Preview' menu item in is enabled in the ContextMenu"()
    {
        when:
        filterPanel.typeSearchText( MY_SITE.getName() );

        then: "'Preview' menu item in is enabled in the ContextMenu"
        contentBrowsePanel.openContextMenuAndWaitUntilItemEnabled( MY_SITE.getName(), "Preview" );
    }

    def "WHEN site selected and opened for edit THEN 'Preview' button is enabled on the toolbar wizard"()
    {
        when: "site is selected"
        filterPanel.typeSearchText( MY_SITE.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( MY_SITE.getName() ).clickToolbarEdit();

        then: "'Preview' button is enabled on the toolbar wizard"
        wizard.isPreviewButtonEnabled()
    }
    // test for verifying of XP-4123 (Page Editor inaccessible for a folder)
    def "GIVEN existing site with a selected controller WHEN child-folder has been added to the site THEN 'Show Page Editor' button should be present on the wizard toolbar"()
    {
        given:
        ContentWizardPanel wizard = findAndSelectContent( MY_SITE.getName() ).clickToolbarNew().selectContentType(
            ContentTypeName.folder() );

        when: "child-folder has been added to the site"
        wizard.typeDisplayName( "test-page-editor-toggler" ).save();
        saveScreenshot( "folder-show-page-editor-toggler" );

        then: "'Show Page Editor' button should be present on the wizard-toolbar"
        wizard.isShowPageEditorButtonDisplayed();
    }
}