package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentUnpublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class ContentWizard_PublishButton_Spec
    extends BaseContentSpec
{
    @Shared
    Content CONTENT;

    def "GIVEN existing 'offline' content WHEN the content opened AND has been 'published' THEN 'online' status for this content is displayed on the wizard"()
    {
        given:
        CONTENT = buildFolderContent( "folder", "unpublish of pending delete content" );
        addContent( CONTENT );

        when: "content has been 'published'"
        ContentWizardPanel wizard = findAndSelectContent( CONTENT.getName() ).clickToolbarEdit();
        wizard.clickOnWizardPublishButton().clickOnPublishNowButton();
        wizard.waitInvisibilityOfSpinner( Application.EXPLICIT_NORMAL );

        then: "folder is 'online' now"
        wizard.getStatus() == ContentStatus.ONLINE.getValue();

        and: "publish button is disabled now"
        !wizard.isPublishButtonEnabled();

        and: "'publish-menu' becomes available now "
        wizard.isPublishMenuAvailable();
    }

    def "GIVEN existing 'online' content WHEN the content opened AND display name changed THEN 'modified' status for this content is displayed on the wizard"()
    {
        given:
        findAndSelectContent( CONTENT.getName() );

        when: "display name changed"
        ContentWizardPanel wizard = findAndSelectContent( CONTENT.getName() ).clickToolbarEdit();
        wizard.typeDisplayName( "new display name" ).save();

        then: "folder is 'modified' now"
        wizard.getStatus() == ContentStatus.MODIFIED.getValue();

        and: "'Publish' menu becomes enabled"
        wizard.isPublishButtonEnabled();

        and: "'publish-menu' is available"
        wizard.isPublishMenuAvailable();
    }

    def "GIVEN existing 'modified' content WHEN the content opened AND 'unpublish' button was pressed THEN 'offline' status for this content is displayed on the wizard"()
    {
        given: "existing 'modified' content"
        findAndSelectContent( CONTENT.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();

        when: "'unpublish' button was pressed"
        ContentUnpublishDialog modalDialog = wizard.showPublishMenu().selectUnPublishMenuItem();
        modalDialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        modalDialog.clickOnUnpublishButton();
        sleep( 1000 );

        then: "folder is 'offline' now"
        wizard.getStatus() == ContentStatus.OFFLINE.getValue();

        and: "'Publish' menu becomes enabled"
        wizard.isPublishButtonEnabled();

        and: "'publish-menu' becomes disabled"
        !wizard.isPublishMenuAvailable();
    }

    def "GIVEN existing 'online' content WHEN 'Delete' button on the wizard-toolbar pressed AND content deleted THEN 'pending delete' status for this content is displayed on the wizard"()
    {
        given: "existing 'online' content"
        findAndSelectContent( CONTENT.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        wizard.clickOnWizardPublishButton().clickOnPublishNowButton();


        when: "'Delete' button on the wizard-toolbar pressed AND content deleted"
        wizard.clickToolbarDelete().doDelete();

        then: "'pending delete' status for this content is displayed on the wizard"
        wizard.getStatus() == ContentStatus.PENDING_DELETE.getValue();

        and: "'Publish' menu becomes enabled"
        wizard.isPublishButtonEnabled();

        and: "'publish-menu' is enabled"
        wizard.isPublishMenuAvailable();
    }

    def "GIVEN existing 'pending delete' content WHEN opened and 'Unpublish' menu item selected THEN the wizard closes AND the content not listed in the grid"()
    {
        given: "existing 'pending delete' content opened"
        findAndSelectContent( CONTENT.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();

        when: "'Unpublish' menu item selected"
        wizard.showPublishMenu().selectUnPublishMenuItem().clickOnUnpublishButton();
        sleep( 1000 );
        saveScreenshot( "test_wizard_unpublish_selected" );

        then: "the wizard closes"
        !wizard.isOpened();

        and: "the content not listed in the grid"
        !contentBrowsePanel.exists( CONTENT.getName() );
    }
}
