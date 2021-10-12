package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.BaseContentType
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class SitePreview_Spec
    extends BaseSiteSpec
{
    @Shared
    Content MY_SITE


    def "GIVEN existing site is selected WHEN controller is not selected THEN 'Preview' button should be disabled"()
    {
        given:
        MY_SITE = buildMyFirstAppSite( "preview" );
        "data typed and saved and wizard closed"
        addSite( MY_SITE );

        when: "site without a controller has been selected"
        findAndSelectContent( MY_SITE.getName() );

        then: "'Preview' on a BrowseToolbar is disabled"
        !contentBrowsePanel.isPreviewButtonEnabled();
    }

    def "GIVEN existing site is selected WHEN controller is not selected THEN 'Preview' in the ContextMenu should be disabled"()
    {
        when: "site is selected"
        filterPanel.typeSearchText( MY_SITE.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( MY_SITE.getName() );

        then: "'Preview' in the context menu should be disabled"
        contentBrowsePanel.waitUntilItemDisabledInContextMenu( MY_SITE.getName(), "Preview" );
    }

    def "GIVEN existing site is opened WHEN controller is not selected THEN 'Preview' button should be disabled"()
    {
        when: "existing has been opened"
        ContentWizardPanel wizard = findAndSelectContent( MY_SITE.getName() ).clickToolbarEdit();

        then: "'Preview' button should be disabled in the wizard"
        !wizard.isPreviewButtonEnabled()
    }

    def "WHEN existing site is opened AND a controller has been selected THEN 'Preview' in BrowseToolbar gets enabled"()
    {
        given: "existing site without a template is opened"
        ContentWizardPanel wizard = findAndSelectContent( MY_SITE.getName() ).clickToolbarEdit();

        when: "controller has been selected"
        wizard.selectPageDescriptor( COUNTRY_REGION_TITLE ).closeBrowserTab().switchToBrowsePanelTab();
        saveScreenshot( "site-template-preview" );

        then: "'Preview' in the BrowseToolbar should be enabled"
        findAndSelectContent( MY_SITE.getName() );
        contentBrowsePanel.isPreviewButtonEnabled();
    }

    def "WHEN existing site is selected AND context menu has been opened THEN 'Preview' menu item should be enabled"()
    {
        when: "existing site with the page-template was selected AND context menu is shown"
        filterPanel.typeSearchText( MY_SITE.getName() );

        then: "'Preview' menu item should be enabled in the ContextMenu"
        contentBrowsePanel.openContextMenuAndWaitUntilItemEnabled( MY_SITE.getName(), "Preview" );
    }

    def "WHEN existing site with controller has been opened THEN 'Preview' button should be enabled"()
    {
        when: "site is selected"
        filterPanel.typeSearchText( MY_SITE.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( MY_SITE.getName() ).clickToolbarEdit();

        then: "'Preview' button should be enabled on the toolbar wizard"
        wizard.isPreviewButtonEnabled()
    }
    // test for verifying of XP-4123 (Page Editor inaccessible for a folder)
    def "WHEN site's child-folder has been opened THEN 'Show Page Editor' button should be present in the wizard toolbar"()
    {
        given: "existing site with selected page-template has been selected and 'New' button pressed"
        ContentWizardPanel wizard = findAndSelectContent( MY_SITE.getName() ).clickToolbarNew().selectContentType(
            BaseContentType.FOLDER.getDisplayName() );

        when: "child-folder has been added"
        wizard.typeDisplayName( "test-page-editor-toggler" ).save();
        saveScreenshot( "folder-show-page-editor-toggler" );

        then: "'Show Page Editor' button should be present in the wizard-toolbar"
        wizard.isShowPageEditorButtonDisplayed();
    }
}
