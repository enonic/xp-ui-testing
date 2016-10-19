package com.enonic.wem.uitest.content.volume_testing

import com.enonic.autotests.pages.Application
import com.enonic.autotests.pages.contentmanager.ContentPublishDialog
import com.enonic.autotests.utils.TestUtils
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Ignore

class ContentPublishDialog_Performance_Spec
    extends BaseContentSpec
{
    @Ignore
    def "GIVEN 'Select All' clicked AND more than 1000 content are selected  WHEN 'Publish' button pressed AND 'Include child items' is true THEN 'Publish' button on the modal dialog is 'enabled'"()
    {
        given:
        contentBrowsePanel.clickOnSelectAll();
        TestUtils.saveScreenshot( getSession(), "test_performance_all_selected" );

        when:
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish();
        contentPublishDialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        contentPublishDialog.setIncludeChildCheckbox( true );

        then: "'publish' button on the dialog becomes enabled after the required time"
        contentPublishDialog.waitUntilPublishButtonEnabled( Application.EXPLICIT_NORMAL );
    }

    @Ignore
    def "GIVEN 'Select All' clicked AND more than 1000 content are selected  WHEN all content have published THEN correct notification message appears and each content has 'online' status"()
    {
        given:
        contentBrowsePanel.clickOnSelectAll();
        TestUtils.saveScreenshot( getSession(), "test_performance_all_selected" );

        when:
        ContentPublishDialog contentPublishDialog = contentBrowsePanel.clickToolbarPublish();
        contentPublishDialog.waitUntilDialogShown( Application.EXPLICIT_NORMAL );
        contentPublishDialog.setIncludeChildCheckbox( true );
        contentPublishDialog.waitUntilPublishButtonEnabled( Application.EXPLICIT_NORMAL );
        contentPublishDialog.clickOnPublishNowButton();

        then: "modal dialog has been closed"
        contentPublishDialog.waitForDialogClosed();
        and: "correct notification message appears"
        contentBrowsePanel.waitNotificationMessage( Application.EXPLICIT_NORMAL );
    }
}