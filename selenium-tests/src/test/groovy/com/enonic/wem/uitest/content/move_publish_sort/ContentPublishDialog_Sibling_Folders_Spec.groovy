package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Created  on 4/26/2017.
 * */
@Stepwise
@Ignore
class ContentPublishDialog_Sibling_Folders_Spec
    extends BaseContentSpec
{

    @Shared
    Content FOLDER1

    @Shared
    Content FOLDER2

    def "GIVEN two folders are added WHEN both folders have been selected selected and 'Publish' wizard is opened THEN both folders should be removable in the dialog"()
    {
        given: "folder has been added in root directory:"
        FOLDER1 = buildFolderContent( "folder", "publishing test1" );
        FOLDER2 = buildFolderContent( "folder", "publishing test2" );
        addReadyContent( FOLDER1 );
        addReadyContent( FOLDER2 );

        when: "'Publish' wizard has been opened"
        findAndSelectContent( FOLDER1.getName() )
        findAndSelectContent( FOLDER2.getName() )
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitForDialogLoaded();
        saveScreenshot( "publish_wizard_two_sibling_node" );

        then: "'remove' button should be disabled for both contents"
        contentPublishDialog.isPublishItemRemovable( FOLDER1.getDisplayName() );
        and:
        contentPublishDialog.isPublishItemRemovable( FOLDER2.getDisplayName() );

        and: "status of the first folder should be 'New'"
        contentPublishDialog.getContentStatus( FOLDER1.getDisplayName() ) == ContentStatus.NEW.getValue();

        and: "status  of the second folder should be 'New'"
        contentPublishDialog.getContentStatus( FOLDER2.getDisplayName() ) == ContentStatus.NEW.getValue();
    }

    def "GIVEN two existing folders are selected AND Publish Wizard opened WHEN 'remove' icon has been clicked THEN one folder should be removed in modal dialog"()
    {
        given: "Both folders were selected and 'Publish' button clicked"
        findAndSelectContent( FOLDER1.getName() )
        findAndSelectContent( FOLDER2.getName() )
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitForDialogLoaded();
        when: "'remove' icon has been clicked "
        contentPublishDialog.removeItem( FOLDER2.getDisplayName() );
        saveScreenshot( "publish_wizard_sibling_node_removed" );

        then: "only one folder should be present on the dialog"
        contentPublishDialog.getNamesOfContentsToPublish().size() == 1;

        and: "correct name of folder should be displayed"
        contentPublishDialog.getNamesOfContentsToPublish().get( 0 ).contains( FOLDER1.getName() );
    }

    def "GIVEN two existing folders(New) are selected AND Publish Wizard opened WHEN 'remove' icon has been clicked THEN one folder should be removed from the wizard"()
    {
        given: "Both folders were selected and 'Publish' button clicked"
        findAndSelectContent( FOLDER1.getName() )
        findAndSelectContent( FOLDER2.getName() )
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish().waitForDialogLoaded();
        and: "'remove' icon has been clicked "
        contentPublishDialog.removeItem( FOLDER2.getDisplayName() );

        when: "'Publish Now' button has been pressed and the dialog is closed"
        contentPublishDialog.clickOnPublishNowButton();
        saveScreenshot( "publish_wizard_status_for_removed" );

        then: "the second folder should be 'New'"
        contentBrowsePanel.getContentStatus( FOLDER2.getName() ) == ContentStatus.NEW.getValue();

        and: "the first folder should be 'Published'"
        findAndSelectContent( FOLDER1.getName() );
        saveScreenshot( "publish_wizard_status_for_first_folder" );
        contentBrowsePanel.getContentStatus( FOLDER1.getName() ) == ContentStatus.PUBLISHED.getValue();
    }
}
