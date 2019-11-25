package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.TagFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class Restore_Tags_Spec
    extends Base_InputFields_Occurrences
{

    @Shared
    Content TAG_CONTENT

    def "GIVEN new Tag-content 2:5(with 2 tags) is created WHEN content has been opened and one tag removed THEN number of versions should be increased"()
    {
        given: "new Tag-content with two tags is added"
        TAG_CONTENT = buildTag_2_5_Content( 2 );
        ContentWizardPanel wizard = selectSitePressNew( TAG_CONTENT.getContentTypeName() );
        wizard.typeData( TAG_CONTENT ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "content opened and one tag has been removed "
        findAndSelectContent( TAG_CONTENT.getName() ).clickToolbarEdit();
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );
        formViewPanel.removeLastTag();
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        and:
        AllContentVersionsView allContentVersionsView = openVersionPanel();
        saveScreenshot( "tag_valid_version_browse_panel" );

        then: "number of versions increased by one"
        allContentVersionsView.getAllVersions().size() == 3;
    }

    def "GIVEN tag-content (one required tag is missed) WHEN version with 2 selected tags have been restored THEN content becomes valid"()
    {
        given: "tag-content (one required tag is missed)"
        ContentWizardPanel wizard = findAndSelectContent( TAG_CONTENT.getName() ).clickToolbarEdit();
        wizard.switchToBrowsePanelTab();
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "version with 2 selected tags have been restored"
        allContentVersionsView.getAllVersions();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion();

        then: "number of version is increased"
        saveScreenshot( "tag_valid_version" );
        allContentVersionsView.getAllVersions().size() == 4;
        contentBrowsePanel.switchToBrowserTabByTitle( TAG_CONTENT.getDisplayName() );


        and: "the content becomes valid"
        !wizard.isContentInvalid();
    }

    def "GIVEN tag-content is valid AND it is opened WHEN 'AppHomeButton' clicked AND version with one selected tag has been restored THEN red icon should appear in the wizard tab"()
    {
        given: "current version of content is valid"
        ContentWizardPanel wizard = findAndSelectContent( TAG_CONTENT.getName() ).clickToolbarEdit();
        wizard.switchToBrowsePanelTab();
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "version with one selected tag has been restored"
        allContentVersionsView.getAllVersions();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion();

        then: "number of version is increased to 5"
        allContentVersionsView.getAllVersions().size() == 5;
        contentBrowsePanel.switchToBrowserTabByTitle( TAG_CONTENT.getDisplayName() );
        saveScreenshot( "tag_not_valid_restored" );

        and: "red icon should appear in the wizard tab, the content becomes not valid"
        wizard.isContentInvalid();


    }
}