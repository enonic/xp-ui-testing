package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created  on 3/17/2017.
 *
 * */
@Stepwise
class Publish_Deleted_Content_Spec
    extends BaseContentSpec
{
    @Shared
    Content CONTENT;

    def "Preconditions: new folder should be added and published"()
    {
        given: "new folder has been added"
        CONTENT = buildFolderContent( "folder", "publishing-deleted" );
        addReadyContent( CONTENT );

        when: "the folder has been published"
        findAndSelectContent( CONTENT.getName() ).clickToolbarPublish().clickOnPublishNowButton();

        then: "'Published' status should be displayed"
        contentBrowsePanel.getContentStatus( CONTENT.getName() ) == ContentStatus.PUBLISHED.getValue();
    }

    //verifies issue https://github.com/enonic/app-contentstudio/issues/385
    def "GIVEN existing folder is marked as deleted in the wizard WHEN the folder has been 'published' in the wizard THEN wizard closes AND the content should not be listed in the grid"()
    {
        given: "existing 'deleted' content is opened"
        findAndSelectContent( CONTENT.getName() );
        ContentWizardPanel wizard = contentBrowsePanel.clickToolbarEdit();
        wizard.clickToolbarDelete().clickOnMarkAsDeletedMenuItem();

        when: "'Publish...' button has been clicked"
        wizard.clickOnPublishButton().clickOnPublishNowButton();

        then: "the content should not be displayed in the grid"
        wizard.switchToBrowsePanelTab();
        sleep( 400 );
        !contentBrowsePanel.exists( CONTENT.getName() );
    }
}
