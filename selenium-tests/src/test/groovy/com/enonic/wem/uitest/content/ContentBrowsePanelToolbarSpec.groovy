package com.enonic.wem.uitest.content

import com.enonic.autotests.pages.contentmanager.browsepanel.ContentBrowsePanel
import com.enonic.autotests.services.NavigatorHelper
import com.enonic.wem.uitest.BaseGebSpec
import spock.lang.Ignore
import spock.lang.Shared

class ContentBrowsePanelToolbarSpec
    extends BaseGebSpec
{
    @Shared
    ContentBrowsePanel contentBrowsePanel;


    def setup()
    {
        go "admin"
        contentBrowsePanel = NavigatorHelper.openContentApp( getTestSession() );
    }

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
}
