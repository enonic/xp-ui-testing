package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.TagFormViewPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class Restore_Tags_Spec
    extends Base_InputFields_Occurrences
{

    @Shared
    Content TAG_CONTENT

    def "GIVEN creating new Tag-content 2:5(with 2 tags) WHEN content opened and one tag was removed THEN number of versions is increased"()
    {
        given: "new Tag-content with two tags added"
        TAG_CONTENT = buildTag_2_5_Content( 2 );
        ContentWizardPanel wizard = selectSiteOpenWizard( TAG_CONTENT.getContentTypeName() );
        wizard.typeData( TAG_CONTENT ).save().close( TAG_CONTENT.getDisplayName() );
        contentBrowsePanel.clickOnClearSelection();

        when: "content opened and one tag was removed "
        findAndSelectContent( TAG_CONTENT.getName() ).clickToolbarEdit();
        TagFormViewPanel formViewPanel = new TagFormViewPanel( getSession() );
        formViewPanel.removeLastTag();
        wizard.save().close( TAG_CONTENT.getDisplayName() );

        and:
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        then: "number of versions increased by one"
        allContentVersionsView.getAllVersions().size() == 3;
    }

    def "GIVEN version of tag-content with missed required value is current WHEN valid version of content is restored THEN content has no red icon on the wizard"()
    {
        given: "content with missed required value"
        ContentWizardPanel wizard = findAndSelectContent( TAG_CONTENT.getName() ).clickToolbarEdit();
        contentBrowsePanel.pressAppHomeButton();
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "valid version of content is restored"
        allContentVersionsView.getAllVersions();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );
        TestUtils.saveScreenshot( getSession(), "tag_valid_version" );

        then: "content has no a red icon on the wizard-tab"
        !wizard.isContentInvalid( TAG_CONTENT.getDisplayName() )
    }

    def "GIVEN current version of content is valid AND wizard opened WHEN 'AppHomeButton' clicked and not valid version of content is restored THEN red icon appears on the wizard tab"()
    {
        given: "current version of content is valid"
        ContentWizardPanel wizard = findAndSelectContent( TAG_CONTENT.getName() ).clickToolbarEdit();
        contentBrowsePanel.pressAppHomeButton();
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "not valid version of content is restored"
        allContentVersionsView.getAllVersions();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 0 );
        versionItem.doRestoreVersion( versionItem.getId() );
        TestUtils.saveScreenshot( getSession(), "tag_not_valid_restored" );

        then: "red icon appears on the wizard tab"
        wizard.isContentInvalid( TAG_CONTENT.getDisplayName() )
    }
}