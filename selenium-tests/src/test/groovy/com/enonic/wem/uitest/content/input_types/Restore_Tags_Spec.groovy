package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.TagFormViewPanel
import com.enonic.autotests.utils.TestUtils
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore
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

        then: "number of versions is increased"
        allContentVersionsView.getAllVersions().size() == 3;
    }
    //INBOX-419
    @Ignore
    def "GIVEN existing content with already changed date WHEN valid version of content is restored THEN content has no red icon on the wizard"()
    {
        given: "content with a changed date"
        ContentWizardPanel wizard = findAndSelectContent( TAG_CONTENT.getName() ).clickToolbarEdit();
        contentBrowsePanel.pressAppHomeButton();
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "valid version of content is restored"
        allContentVersionsView.getAllVersions();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );
        TestUtils.saveScreenshot( getSession(), "tag_valid_version" );

        then: "content has a red icon on the wizard"
        !wizard.isContentInvalid( TAG_CONTENT.getDisplayName() )
    }
    //INBOX-419
    @Ignore
    def "GIVEN existing content with already changed date WHEN not valid version of content is restored THEN content has no red icon on the wizard"()
    {
        given: "content with a changed date"
        ContentWizardPanel wizard = findAndSelectContent( TAG_CONTENT.getName() ).clickToolbarEdit();
        contentBrowsePanel.pressAppHomeButton();
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "not valid version of content is restored"
        allContentVersionsView.getAllVersions();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 0 );
        versionItem.doRestoreVersion( versionItem.getId() );
        TestUtils.saveScreenshot( getSession(), "tag_not_valid_restored" );

        then: "content has no a red icon on the wizard"
        wizard.isContentInvalid( TAG_CONTENT.getDisplayName() )
    }
}