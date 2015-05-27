package com.enonic.wem.uitest.content

import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import com.enonic.xp.content.ContentPath
import com.enonic.xp.schema.content.ContentTypeName

class ContentBrowsePanelToolbarSpec
    extends BaseContentSpec
{


    def "GIVEN Content BrowsePanel WHEN no selected content THEN Delete button should be disabled"()
    {
        expect:
        !contentBrowsePanel.isDeleteButtonEnabled();
    }

    def "GIVEN Content BrowsePanel WHEN no selected content THEN New button should be enabled"()
    {
        expect:
        contentBrowsePanel.isNewButtonEnabled();
    }

    def "GIVEN Content BrowsePanel WHEN no selected content THEN Preview button should be disabled"()
    {
        expect:
        !contentBrowsePanel.isPreviewButtonEnabled();
    }

    def "GIVEN Content BrowsePanel WHEN no selected content THEN Edit button should be disabled"()
    {
        expect:
        !contentBrowsePanel.isEditButtonEnabled();
    }

    def "GIVEN Content BrowsePanel WHEN no selected content THEN Duplicate button should be disabled"()
    {
        expect:
        !contentBrowsePanel.isDuplicateButtonEnabled();
    }

    def "GIVEN Content BrowsePanel WHEN no selected content THEN Sort button should be disabled"()
    {
        expect:
        !contentBrowsePanel.isSortButtonEnabled();
    }

    def "GIVEN Content BrowsePanel WHEN no selected content THEN Move button should be disabled"()
    {
        expect:
        !contentBrowsePanel.isMoveButtonEnabled();
    }

    def "GIVEN Content BrowsePanel WHEN no selected content THEN Publish button should be disabled"()
    {
        expect:
        !contentBrowsePanel.isPublishButtonEnabled();
    }

    def "GIVEN Content BrowsePanel WHEN one content selected THEN Publish button should be enabled"()
    {
        when:
        contentBrowsePanel.clickCheckboxAndSelectRow( 0 )

        then:
        contentBrowsePanel.isPublishButtonEnabled();
    }

    def "GIVEN Content BrowsePanel WHEN one content selected THEN Sort button should be enabled"()
    {
        when:
        contentBrowsePanel.clickCheckboxAndSelectRow( 0 )

        then:
        contentBrowsePanel.isSortButtonEnabled();
    }

    def "GIVEN Content BrowsePanel WHEN one content selected THEN Move button should be enabled"()
    {
        when:
        contentBrowsePanel.clickCheckboxAndSelectRow( 0 )

        then:
        contentBrowsePanel.isMoveButtonEnabled();
    }

    def "GIVEN Content BrowsePanel WHEN one content selected THEN Edit button should be enabled"()
    {
        when:
        contentBrowsePanel.clickCheckboxAndSelectRow( 0 )

        then:
        contentBrowsePanel.isEditButtonEnabled();
    }

    def "GIVEN Content BrowsePanel WHEN one content selected THEN Duplicate button should be enabled"()
    {
        when:
        contentBrowsePanel.clickCheckboxAndSelectRow( 0 )

        then:
        contentBrowsePanel.isDuplicateButtonEnabled();
    }

    def "GIVEN Content BrowsePanel WHEN one content selected THEN New button should be enabled"()
    {
        when:
        contentBrowsePanel.clickCheckboxAndSelectRow( 0 )

        then:
        contentBrowsePanel.isNewButtonEnabled();
    }

    def "GIVEN a content that not allowing children WHEN content selected THEN Sort button is  disabled for content types not allowing children"()
    {
        given:
        Content imageContent = Content.builder().
            name( "nord.jpg" ).
            displayName( "nord.jpg" ).
            contentType( ContentTypeName.imageMedia() ).
            parent( ContentPath.from( "all-content-types-images" ) ).
            build();

        when: "image content selected"
        findAndSelectContent( imageContent );
        TestUtils.saveScreenshot( getSession(), "children_allow" );

        then: "sort button is disabled"
        !contentBrowsePanel.isSortButtonEnabled()
    }
}
