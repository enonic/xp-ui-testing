package com.enonic.wem.uitest.content

/**
 * Tasks:
 * xp-ui-testing#63  Add Selenium tests for 'Show Issue' button on the contentBrowse-toolbar*/
class ContentBrowsePanelToolbarSpec
    extends BaseContentSpec
{

    def "GIVEN Content BrowsePanel WHEN no selected content THEN Delete button should be disabled"()
    {
        expect:
        !contentBrowsePanel.isDeleteButtonEnabled();

        and: "New button should be enabled"
        contentBrowsePanel.isNewButtonEnabled();

        and: "Preview button should be disabled"
        !contentBrowsePanel.isPreviewButtonEnabled();

        and: "Edit button should be disabled"
        !contentBrowsePanel.isEditButtonEnabled();

        and: "Duplicate button should be disabled"
        !contentBrowsePanel.isDuplicateButtonEnabled();

        and: "Sort button should be disabled"
        !contentBrowsePanel.isSortButtonEnabled();

        and: "Publish button should be disabled"
        !contentBrowsePanel.isPublishButtonEnabled();

        and: "Move button should be disabled"
        !contentBrowsePanel.isMoveButtonEnabled();

        and: "'Show Issues' button should be disabled"
        contentBrowsePanel.isShowIssuesButtonDisplayed();

        and: "SU can not has assigned issues"
        !contentBrowsePanel.hasAssignedIssues();
    }

    def "GIVEN Content BrowsePanel WHEN one content selected THEN Publish button should be enabled"()
    {
        when: "one content selected"
        filterPanel.typeSearchText( IMPORTED_FOLDER_NAME );
        contentBrowsePanel.clickCheckboxAndSelectRow( IMPORTED_FOLDER_NAME );

        then: "Publish button should be enabled"
        contentBrowsePanel.isPublishButtonEnabled();

        and: "Sort button should be enabled"
        contentBrowsePanel.isSortButtonEnabled();

        and: "Move button is enabled"
        contentBrowsePanel.isMoveButtonEnabled();

        and: "Edit button should be enabled"
        contentBrowsePanel.isEditButtonEnabled();

        and: "Duplicate button should be enabled"
        contentBrowsePanel.isDuplicateButtonEnabled();

        and: "New button should be enabled"
        contentBrowsePanel.isNewButtonEnabled();

        and: "Delete button should be enabled"
        contentBrowsePanel.isDeleteButtonEnabled();

        and: "Preview button should be disabled, because the content is folder "
        !contentBrowsePanel.isPreviewButtonEnabled();
    }

    def "GIVEN a content that not allowing children WHEN content selected THEN Sort button is  disabled for content types not allowing children"()
    {
        when: "image content selected"
        findAndSelectContent( IMPORTED_IMAGE_BOOK_NAME );
        saveScreenshot( "children_not_allow" );

        then: "sort button is disabled"
        !contentBrowsePanel.isSortButtonEnabled();

        and: "New button is disabled"
        !contentBrowsePanel.isNewButtonEnabled();

        and: "Delete button should be enabled"
        contentBrowsePanel.isDeleteButtonEnabled();
    }
}
