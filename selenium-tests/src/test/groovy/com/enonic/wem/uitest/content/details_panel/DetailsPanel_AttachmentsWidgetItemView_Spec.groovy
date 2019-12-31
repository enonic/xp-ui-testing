package com.enonic.wem.uitest.content.details_panel

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AttachmentsWidgetItemView
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.wem.uitest.content.BaseContentSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class DetailsPanel_AttachmentsWidgetItemView_Spec
    extends BaseContentSpec
{
    @Shared
    Content SITE;

    def "WHEN image content has been selected THEN attachment's name should be displayed on the widget"()
    {
        when: "image content has been checked"
        findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME );
        and: "details panel has been opened"
        contentBrowsePanel.openContentDetailsPanel();
        AttachmentsWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getAttachmentsWidgetItemView();

        then: "'Attachments Widget' should be displayed"
        view.isDisplayed();
        and: "attachment's name should be displayed"
        view.isHasAttachments();
    }

    def "GIVEN existing image-content WHEN content selected and details panel opened THEN expected attachment name displayed in the widget "()
    {
        when: "image content has been selected"
        findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME );
        contentBrowsePanel.openContentDetailsPanel();
        AttachmentsWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getAttachmentsWidgetItemView();
        List<String> attachmentNames = view.getAttachmentNames();

        then: "expected attachment's name should be displayed in the widget"
        attachmentNames.size() == 1;
        and:
        attachmentNames.get( 0 ) == IMPORTED_IMAGE_BOOK_NAME;
    }

    def "GIVEN existing folder-content WHEN content selected and details panel opened THEN 'This item has no attachments' message should be displayed in the widget"()
    {
        when: "content selected and details panel opened"
        findAndSelectContent( IMPORTED_FOLDER_NAME );
        contentBrowsePanel.openContentDetailsPanel();
        AttachmentsWidgetItemView view = contentBrowsePanel.getContentBrowseItemPanel().getContentDetailsPanel().getAttachmentsWidgetItemView();

        then: "Attachments Widget should be displayed"
        view.isDisplayed();

        and: "the view has no any attachments"
        !view.isHasAttachments();

        and: "'This item has no attachments' message should be displayed in the widget"
        view.getMessage() == AttachmentsWidgetItemView.NO_ATTACHMENTS_MESSAGE;
    }
}
