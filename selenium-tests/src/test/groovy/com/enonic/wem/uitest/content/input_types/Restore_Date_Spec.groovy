package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.browsepanel.detailspanel.VersionHistoryWidget
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

    def "GIVEN date content is saved WHEN date has been changed THEN new 'version item' should appear in the version history panel"()
    {
        given: "date content is saved"
        DATE_CONTENT = buildDate1_1_Content( DATE_V1 );
        ContentWizardPanel wizard = selectSitePressNew( DATE_CONTENT.getContentTypeName() );
        wizard.typeData( DATE_CONTENT ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        when: "the content is opened and date has been updated"
        findAndSelectContent( DATE_CONTENT.getName() ).clickToolbarEdit();
        DateFormViewPanel formViewPanel = new DateFormViewPanel( getSession() );
        formViewPanel.typeDate( DATE_V2 );
        wizard.save().closeBrowserTab().switchToBrowsePanelTab();

        and: "version panel is opened"
        VersionHistoryWidget allContentVersionsView = openVersionPanel();

        then: "number of versions should be increased by one"
        allContentVersionsView.getAllVersions().size() == 3;
    }

    def "GIVEN existing date-content with 3 versions WHEN previous version is reverted THEN correct date should be displayed in the wizard"()
    {
        given: "content with a changed date"
        findAndSelectContent( DATE_CONTENT.getName() );
        VersionHistoryWidget allContentVersionsView = openVersionPanel();

        when: "the previous version is reverted"
        allContentVersionsView.getAllVersions();
        ContentVersionInfoView versionItem = allContentVersionsView.clickOnVersionItem( 1 );
        versionItem.doRevertVersion();
        saveScreenshot( "date_reverted" );

        and: "content is opened"
        contentBrowsePanel.clickToolbarEdit();

        then: "correct date should be present on the wizard"
        DateFormViewPanel formViewPanel = new DateFormViewPanel( getSession() );
        formViewPanel.getDateValue() == DATE_V1;
    }
}
