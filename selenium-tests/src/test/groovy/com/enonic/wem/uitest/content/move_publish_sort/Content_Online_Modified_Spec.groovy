package com.enonic.wem.uitest.content.move_publish_sort

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentStatus
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared

class Content_Online_Modified_Spec
    extends BaseContentSpec
{
    @Shared
    String NEW_DISPLAY_NAME = "newDisplayName";

    @Shared
    Content CONTENT;

    def "GIVEN existing root content WHEN content selected and 'Publish' button on toolbar pressed THEN notification message appears and  content have got a 'Online' status"()
    {
        given: "existing content in root"
        CONTENT = buildFolderContent( "publish", "folder-content" );
        addContent( CONTENT );

        when: "the content have been published"
        findAndSelectContent( CONTENT.getName() ).clickToolbarPublish().clickOnPublishNowButton();
        String message = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );

        then: "status of content is 'online'"
        contentBrowsePanel.getContentStatus( CONTENT.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() );

        and: "correct notification message appears"
        message == String.format( Application.CONTENT_PUBLISHED_NOTIFICATION_MESSAGE, CONTENT.getDisplayName() );
    }

    def "GIVEN existing root content with 'Online' status  WHEN content edited THEN content becomes 'Modified' in the BrowsePanel"()
    {
        given: "existing root content with 'Online' status opened for edit"
        ContentWizardPanel wizard = findAndSelectContent( CONTENT.getName() ).clickToolbarEdit(); ;

        when: "new display name typed"
        wizard.typeDisplayName( NEW_DISPLAY_NAME ).save().close( NEW_DISPLAY_NAME );

        then: "content becomes 'Modified' in the BrowsePanel"
        contentBrowsePanel.getContentStatus( CONTENT.getName() ).equalsIgnoreCase( ContentStatus.MODIFIED.getValue() );
    }

    def "GIVEN existing root content with 'Online' status  WHEN content edited THEN content becomes 'Modified' in the Wizard"()
    {
        given: "existing root content with 'Online' status opened for edit"
        Content content = buildFolderContent( "publish", "folder-content" );
        addContent( content );
        ContentWizardPanel wizard = findAndSelectContent( content.getName() ).clickToolbarEdit();
        wizard.clickOnWizardPublishButton().clickOnPublishNowButton();

        when: "new display name typed"
        wizard.typeDisplayName( NEW_DISPLAY_NAME ).save();
        TestUtils.saveScreenshot( getSession(), "test_modified_wizard" );

        then: "status of content is 'modified'"
        wizard.getStatus().equalsIgnoreCase( ContentStatus.MODIFIED.getValue() );

        and: "Publish button is enabled on the wizard-toolbar"
        wizard.isPublishButtonEnabled();

        and: "Publish menu is enabled on the wizard-toolbar"
        wizard.isPublishMenuAvailable();
    }

    def "GIVEN existing root content with 'Modified' status  WHEN content selected and 'Publish' button pressed THEN content has got a 'Online' status"()
    {
        when: "modified content has been published again"
        findAndSelectContent( CONTENT.getName() ).
            clickToolbarPublish().clickOnPublishNowButton();
        String message = contentBrowsePanel.waitPublishNotificationMessage( Application.EXPLICIT_NORMAL );

        then: "status of content is 'online'"
        contentBrowsePanel.getContentStatus( CONTENT.getName() ).equalsIgnoreCase( ContentStatus.ONLINE.getValue() );

        and: "correct notification message appears"
        message == String.format( Application.CONTENT_PUBLISHED_NOTIFICATION_MESSAGE, NEW_DISPLAY_NAME );

        and: "Publish button on the BrowsePanel-toolbar becomes disabled"
        !contentBrowsePanel.isPublishButtonEnabled();

        and: "Publish-menu on the BrowsePanel-toolbar is enabled"
        contentBrowsePanel.isPublishMenuAvailable();
    }

}
