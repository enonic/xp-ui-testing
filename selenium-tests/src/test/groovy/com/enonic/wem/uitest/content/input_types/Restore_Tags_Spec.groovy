package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.TagFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

/**
 * Tasks: XP-4948 Add Selenium tests for checking of 'red icon' (invalid content) in wizards
 **/
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

        then: "number of versions increased by one"
        allContentVersionsView.getAllVersions().size() == 3;
    }

    def "GIVEN tag-content (one required tag is missed) WHEN previous version has been restored THEN content becomes valid"()
    {
        given: "tag-content (one required tag is missed)"
        ContentWizardPanel wizard = findAndSelectContent( TAG_CONTENT.getName() ).clickToolbarEdit();
        wizard.switchToBrowsePanelTab();
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "previous version has been restored"
        allContentVersionsView.getAllVersions();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion(  );
        contentBrowsePanel.switchToBrowserTabByTitle( TAG_CONTENT.getDisplayName() );
        saveScreenshot( "tag_valid_version" );

        then: "content becomes valid"
        !wizard.isContentInvalid()
    }

    def "GIVEN tag-content is valid AND wizard is opened WHEN 'AppHomeButton' clicked AND version with not valid content has been restored THEN red icon should appear in the wizard tab"()
    {
        given: "current version of content is valid"
        ContentWizardPanel wizard = findAndSelectContent( TAG_CONTENT.getName() ).clickToolbarEdit();
        wizard.switchToBrowsePanelTab();
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "not valid version of content is restored"
        allContentVersionsView.getAllVersions();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 0 );
        versionItem.doRestoreVersion(  );
        contentBrowsePanel.switchToBrowserTabByTitle( TAG_CONTENT.getDisplayName() );
        saveScreenshot( "tag_not_valid_restored" );

        then: "red icon should appear on the wizard tab, the content becomes not valid"
        wizard.isContentInvalid()
    }
}