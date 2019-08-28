package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

/**
 * Created  on 5/24/2017.
 * */
class Site_With_Invalid_Child_Publish_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;

    @Shared
    Content CHILD;

    def "WHEN site has been added AND not valid child added THEN the site should be present in the grid"()
    {
        given: "site has been added"
        SITE = buildSiteWithApps( APP_CONTENT_TYPES_DISPLAY_NAME );
        CHILD = buildImageSelector1_1_Content( SITE.getName(), null );
        addReadyContent( SITE );

        and: "content with image-selector has been added(image was not selected)"
        ContentWizardPanel wizard = selectSitePressNew( SITE.getName(), CHILD.getContentTypeName() ).typeData( CHILD );
        wizard.save();
        wizard.close( CHILD.getDisplayName() );

        expect: "the site should be present in the grid"
        contentBrowsePanel.exists( SITE.getName() );
    }

    def "GIVEN existing site with not valid child WHEN the site has been selected and Publish Wizard opened AND 'Include child' icon clicked THEN 'Publish' button should be disabled"()
    {
        given: "existing site with not valid child"
        findAndSelectContent( SITE.getName() );

        when: "Publish Wizard is opened"
        ContentPublishDialog publishWizard = contentBrowsePanel.showPublishMenu().clickOnPublishMenuItem();

        and: "'Include child' icon has been clicked"
        publishWizard.includeChildren( true );
        saveScreenshot( "site_with_not_valid_child" );

        then: "'Publish now' button should be disabled, because its child is not valid"
        !publishWizard.isPublishButtonEnabled();

        and: "expected warning should be displayed on the wizard"
        publishWizard.getDialogIsseesMessage() == ContentPublishDialog.DIALOG_ISSUE_MESSAGE_INVALID_CONTENT;
    }

    def "GIVEN existing site with not valid child WHEN the invalid content has been removed from the dialog THEN 'Publish' button should be enabled"()
    {
        given: "existing site with not valid child"
        findAndSelectContent( SITE.getName() );
        ContentPublishDialog publishWizard = contentBrowsePanel.clickToolbarPublish();
        and: "'Include child' icon has been clicked"
        publishWizard.includeChildren( true );

        when: "the invalid content has been removed from the dialog"
        publishWizard.removeDependant( CHILD.getName() );
        sleep( 500 );
        saveScreenshot( "not_valid_child_removed" );

        then: "'Publish' button should be enabled, because child was removed from the items to publish"
        publishWizard.isPublishButtonEnabled();
    }

    def "GIVEN existing site with not valid child  AND Publish wizard opened WHEN 'Include Child' icon has been clicked twice THEN 'Publish' button should be enabled"()
    {
        given: "existing site with not valid child"
        findAndSelectContent( SITE.getName() );
        ContentPublishDialog publishWizard = contentBrowsePanel.clickToolbarPublish();
        and: "'Include child' icon has been clicked"
        publishWizard.includeChildren( true );

        when: "'Include Child' icon has been clicked again and child is excluded"
        publishWizard.includeChildren( false );
        sleep( 500 );
        saveScreenshot( "not_valid_child_unexpanded" );

        then: "'Publish' button should be enabled, because child was not included to publish"
        publishWizard.isPublishButtonEnabled();
    }
}
