package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AttachmentsWidgetItemView
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Stepwise

@Stepwise
class DetailsPanel_AttachmentsWidgetItemView_Spec
    extends BaseContentSpec
{
    def "WHEN image content selected and details panel opened THEN AttachmentsWidgetItemView is displayed and has attachments"()
    {
        when: "image content selected"
        findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME );
        contentBrowsePanel.clickOnDetailsToggleButton();
        AttachmentsWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getAttachmentsWidgetItemView();

        then: "'Attachments Widget' should be displayed"
        view.isDisplayed();
        and:
        view.isHasAttachments();
    }

    def "GIVEN existing image-content WHEN content selected and details panel opened THEN correct attachment name displayed in the widget "()
    {
        when: "image content selected"
        findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME );
        contentBrowsePanel.clickOnDetailsToggleButton();
        AttachmentsWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getAttachmentsWidgetItemView();
        List<String> attachmentNames = view.getAttachmentNames();

        then: "correct attachment name displayed in the widget"
        attachmentNames.size() == 1;
        and:
        attachmentNames.get( 0 ) == IMPORTED_IMAGE_BOOK_NAME;
    }

    def "GIVEN existing folder-content WHEN content selected and details panel opened THEN correct message displayed in the widget"()
    {
        when: "content selected and details panel opened"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        contentBrowsePanel.clickOnDetailsToggleButton();
        AttachmentsWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getAttachmentsWidgetItemView();

        then: "Attachments Widget should be displayed"
        view.isDisplayed();

        and: "the view has no any attachments"
        !view.isHasAttachments();

        and: "correct message should be displayed in the widget"
        view.getMessage() == AttachmentsWidgetItemView.NO_ATTACHMENTS_MESSAGE;
    }
}
