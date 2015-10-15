package com.enonic.wem.uitest.content.liveedit

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
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
        MY_SITE = buildMySite( "preview" );
        "data typed and saved and wizard closed"
        contentBrowsePanel.clickToolbarNew().selectContentType( MY_SITE.getContentTypeName() ).typeData( MY_SITE ).save().close(
            MY_SITE.getDisplayName() );
        TestUtils.saveScreenshot( getSession(), "preview_site_added" );

        when: "site selected"
        filterPanel.typeSearchText( MY_SITE.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( MY_SITE.getName() );

        then: "'Preview' on a BrowseToolbar is disabled"
        !contentBrowsePanel.isPreviewButtonEnabled();
    }

    def "GIVEN a existing site without a template WHEN site selected THEN 'Preview' in a ContextMenu  is disabled"()
    {
        when: "site selected"
        filterPanel.typeSearchText( MY_SITE.getName() );
        contentBrowsePanel.clickCheckboxAndSelectRow( MY_SITE.getName() );

        then: "'Preview' in the context menu is disabled"
        contentBrowsePanel.isItemDisabledInContextMenu( MY_SITE.getName(), "Preview" );
    }

    def "GIVEN a existing site without a template WHEN site opened THEN 'Preview' in the toolbar wizard  is disabled"()
    {
        when: "site selected"
        filterPanel.typeSearchText( MY_SITE.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( MY_SITE.getName() ).clickToolbarEdit();

        then: "'Preview' in the toolbar wizard is disabled"
        !wizard.isPreviewButtonEnabled()
    }

    def "GIVEN a existing site with a template WHEN site selected THEN 'Preview' on a BrowseToolbar is enabled"()
    {
        given:
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( MY_SITE.getName() ).clickToolbarEdit();
        wizard.clickOnLiveToolbarButton().selectPageDescriptor( COUNTRY_REGION_TITLE ).save().close( MY_SITE.getDisplayName() );

        when:
        filterPanel.typeSearchText( MY_SITE.getName() );

        then: "'Preview' on the BrowseToolbar is enabled"
        contentBrowsePanel.isPreviewButtonEnabled();
    }

    def "WHEN site with a page template selected THEN 'Preview' in a ContextMenu is enabled"()
    {
        when:
        filterPanel.typeSearchText( MY_SITE.getName() );

        then: "'Preview' in the context menu is enabled"
        !contentBrowsePanel.isItemDisabledInContextMenu( MY_SITE.getName(), "Preview" );
    }

    def "WHEN site selected and opened for edit THEN 'Preview' on a WizardToolbar is enabled"()
    {
        when: "site selected"
        filterPanel.typeSearchText( MY_SITE.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickCheckboxAndSelectRow( MY_SITE.getName() ).clickToolbarEdit();

        then: "'Preview' in the toolbar wizard is disabled"
        wizard.isPreviewButtonEnabled()
    }
}