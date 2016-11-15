package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.DateFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class Restore_Date_Spec
    extends Base_InputFields_Occurrences
{

    @Shared
    String DATE_V1 = "2016-05-05";

    @Shared
    String DATE_V2 = "2016-07-07";

    @Shared
    Content DATE_CONTENT

    def "GIVEN existing date content WHEN date is changed THEN new version item appears in the version history panel"()
    {
        given: "new content added"
        DATE_CONTENT = buildDate1_1_Content( DATE_V1 );
        ContentWizardPanel wizard = selectSitePressNew( DATE_CONTENT.getContentTypeName() );
        wizard.typeData( DATE_CONTENT ).save().close( DATE_CONTENT.getDisplayName() );
        contentBrowsePanel.clickOnClearSelection();

        when: "the content opened and date was changed"
        findAndSelectContent( DATE_CONTENT.getName() ).clickToolbarEdit();
        DateFormViewPanel formViewPanel = new DateFormViewPanel( getSession() );
        formViewPanel.typeDate( DATE_V2 );
        wizard.save().close( DATE_CONTENT.getDisplayName() );

        and:
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        then: "number of versions increased by one"
        allContentVersionsView.getAllVersions().size() == 3;
    }

    def "GIVEN existing content with already changed date WHEN previous version restored THEN correct date displayed in the wizard"()
    {
        given: "content with a changed date"
        findAndSelectContent( DATE_CONTENT.getName() );
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "the previous version is restored"
        allContentVersionsView.getAllVersions();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );
        saveScreenshot( "date_restored" );

        and: "content opened"
        contentBrowsePanel.clickToolbarEdit();

        then: "correct date is present on the wizard"
        DateFormViewPanel formViewPanel = new DateFormViewPanel( getSession() );
        formViewPanel.getDateValue() == DATE_V1;
    }
}
