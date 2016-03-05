package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.AttachmentsWidgetItemView
import spock.lang.Stepwise

@Stepwise
class DetailsPanel_AttachmentsWidgetItemView_Spec
    extends BaseContentSpec
{
    def "WHEN image content selected and details panel opened THEN AttachmentsWidgetItemView is displayed and has attachments"()
    {
        when: "image content selected"
        findAndSelectContent( IMPORTED_BOOK_IMAGE );
        contentBrowsePanel.clickOnDetailsToggleButton();
        AttachmentsWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getAttachmentsWidgetItemView();

        then: "Attachments Widget is displayed"
        view.isDisplayed();
        and:
        view.isHasAttachments();
    }

    def "GIVEN existing image-content WHEN content selected and details panel opened THEN correct attachment name displayed in the widget "()
    {
        when: "image content selected"
        findAndSelectContent( IMPORTED_BOOK_IMAGE );
        contentBrowsePanel.clickOnDetailsToggleButton();
        AttachmentsWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getAttachmentsWidgetItemView();
        List<String> attachmentNames = view.getAttachmentNames();

        then: "correct attachment name displayed in the widget"
        attachmentNames.size() == 1;
        and:
        attachmentNames.get( 0 ) == IMPORTED_BOOK_IMAGE;
    }

    def "GIVEN existing folder-content WHEN content selected and details panel opened THEN correct message displayed in the widget"()
    {
        when: "content selected and details panel opened"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        contentBrowsePanel.clickOnDetailsToggleButton();
        AttachmentsWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getAttachmentsWidgetItemView();

        then: "Attachments Widget is displayed"
        view.isDisplayed();

        and: "view has no any attachments"
        !view.isHasAttachments();

        and: "correct message displayed in the widget"
        view.getMessage() == AttachmentsWidgetItemView.NO_ATTACHMENTS_MESSAGE;
    }
}
