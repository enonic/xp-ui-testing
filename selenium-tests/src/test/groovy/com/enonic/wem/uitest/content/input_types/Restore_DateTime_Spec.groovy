package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.VersionHistoryWidget
import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.ContentVersionInfoView
import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.DateTimeFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore
import spock.lang.Shared

@Ignore
class Restore_DateTime_Spec
    extends Base_InputFields_Occurrences
{

    @Shared
    String DATE_TIME_V1 = "2016-05-07 02:00";

    @Shared
    String DATE_TIME_V2 = "2016-07-07 02:00";

    @Shared
    Content DATE_TIME_CONTENT

    def "GIVEN new date content is saved WHEN date has been updated THEN new version item should appear in History Panel"()
    {
        given: "date content is saved"
        DATE_TIME_CONTENT = buildDateTime1_1_Content( DATE_TIME_V1 );
        ContentWizardPanel wizard = selectSitePressNew( DATE_TIME_CONTENT.getContentTypeName() );
        wizard.typeData( DATE_TIME_CONTENT ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "date has been updated:"
        findAndSelectContent( DATE_TIME_CONTENT.getName() ).clickToolbarEdit();
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );
        formViewPanel.typeDateTime( DATE_TIME_V2 );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        and:"version panel is opened"
        VersionHistoryWidget allContentVersionsView = openVersionPanel();

        then: "number of versions should be increased by one"
        allContentVersionsView.getAllVersions().size() == 3;
    }

    def "GIVEN existing content with several versions WHEN previous version has been restored THEN expected date should appear in the wizard"()
    {
        given: "existing content with several versions"
        findAndSelectContent( DATE_TIME_CONTENT.getName() );
        VersionHistoryWidget allContentVersionsView = openVersionPanel();

        when: "the previous version is restored"
        allContentVersionsView.getAllVersions();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionItem( 1 );
        versionItem.doRevertVersion();
        saveScreenshot( "date_time_restored" );

        and: "content is opened"
        contentBrowsePanel.clickToolbarEdit();

        then: "expected date should appear in the wizard"
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );
        formViewPanel.getDateTimeValue() == DATE_TIME_V1;
    }
}
