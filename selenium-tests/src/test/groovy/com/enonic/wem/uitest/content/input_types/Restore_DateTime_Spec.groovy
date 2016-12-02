package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.AllContentVersionsView
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.DateTimeFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

class Restore_DateTime_Spec
    extends Base_InputFields_Occurrences
{

    @Shared
    String DATE_TIME_V1 = "2016-05-07 02:00";

    @Shared
    String DATE_TIME_V2 = "2016-07-07 02:00";

    @Shared
    Content DATE_TIME_CONTENT

    def "GIVEN existing date content WHEN date is changed THEN new version item appears in the version history panel"()
    {
        given: "new content added"
        DATE_TIME_CONTENT = buildDateTime1_1_Content( DATE_TIME_V1 );
        ContentWizardPanel wizard = selectSitePressNew( DATE_TIME_CONTENT.getContentTypeName() );
        wizard.typeData( DATE_TIME_CONTENT ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.clickOnClearSelection();

        when: "the content opened and date was changed"
        findAndSelectContent( DATE_TIME_CONTENT.getName() ).clickToolbarEdit();
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );
        formViewPanel.typeDateTime( DATE_TIME_V2 );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        and:
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        then: "number of versions is increased"
        allContentVersionsView.getAllVersions().size() == 3;
    }

    def "GIVEN existing content with already changed date WHEN previous version restored THEN correct date displayed in the wizard"()
    {
        given: "content with a changed date"
        findAndSelectContent( DATE_TIME_CONTENT.getName() );
        AllContentVersionsView allContentVersionsView = openVersionPanel();

        when: "the previous version is restored"
        allContentVersionsView.getAllVersions();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionAndExpand( 1 );
        versionItem.doRestoreVersion( versionItem.getId() );
        saveScreenshot( "date_time_restored" );

        and: "content opened"
        contentBrowsePanel.clickToolbarEdit();

        then: "correct date is present on the wizard"
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );
        formViewPanel.getDateTimeValue() == DATE_TIME_V1;
    }
}
