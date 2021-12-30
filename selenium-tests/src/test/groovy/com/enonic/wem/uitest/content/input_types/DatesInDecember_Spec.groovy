package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.DateFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore
import spock.lang.Shared

/**
 * Created on 16.08.2016.
 * */
@Ignore
class DatesInDecember_Spec
    extends Base_InputFields_Occurrences
{
    @Shared
    String DATE_IN_DECEMBER1 = "2016-12-31";

    @Shared
    String DATE_IN_DECEMBER2 = "1999-12-31";

    @Shared
    String DATE_IN_DECEMBER3 = "2099-12-31";

    @Shared
    String NANSEN_BIRTH = "1861-10-10";

    def "GIVEN wizard for new Date-content is opened WHEN Nansen birth typed AND saved THEN expected value should be displayed in the wizard"()
    {
        given: "date wizard is opened"
        Content dateContent = buildDate1_1_Content( NANSEN_BIRTH );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( dateContent.getContentTypeName() );

        when: "Date has been typed and the content saved"
        contentWizardPanel.typeData( dateContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        and: "just created content has been reopened"
        findAndSelectContent( dateContent.getName() ).clickToolbarEditAndSwitchToWizardTab();
        DateFormViewPanel dateFormViewPanel = new DateFormViewPanel( getSession() );

        then: "actual date and expected date should be equal"
        dateFormViewPanel.getDateValue() == NANSEN_BIRTH;
    }

    def "GIVEN date wizard is opened WHEN date in December has been typed AND saved THEN expected value should be displayed in the wizard"()
    {
        given: "date wizard is opened"
        Content dateContent = buildDate1_1_Content( DATE_IN_DECEMBER1 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( dateContent.getContentTypeName() );

        when: "Date in December has been typed and content saved"
        contentWizardPanel.typeData( dateContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        and: "just created date content has been reopened"
        contentBrowsePanel.doClearSelection();
        findAndSelectContent( dateContent.getName() ).clickToolbarEditAndSwitchToWizardTab();
        DateFormViewPanel dateFormViewPanel = new DateFormViewPanel( getSession() );

        then: "actual date and expected date should be equal"
        dateFormViewPanel.getDateValue() == DATE_IN_DECEMBER1;
    }

    def "GIVEN date wizard is opened WHEN second date in December typed AND saved THEN expected value should be displayed in the wizard"()
    {
        given: "date wizard is opened"
        Content dateContent = buildDate1_1_Content( DATE_IN_DECEMBER2 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( dateContent.getContentTypeName() );

        when: "Date in December has been typed and content saved"
        contentWizardPanel.typeData( dateContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        and: "just created date content has been reopened"
        contentBrowsePanel.doClearSelection();
        findAndSelectContent( dateContent.getName() ).clickToolbarEditAndSwitchToWizardTab();
        DateFormViewPanel dateFormViewPanel = new DateFormViewPanel( getSession() );

        then: "actual date and expected date should be equal"
        dateFormViewPanel.getDateValue() == DATE_IN_DECEMBER2;
    }

    def "GIVEN date wizard is opened WHEN third date in December typed AND saved THEN expected date should be displayed in the wizard"()
    {
        given: "date wizard is opened"
        Content dateContent = buildDate1_1_Content( DATE_IN_DECEMBER3 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( dateContent.getContentTypeName() );

        when: "Date in December has been saved typed and content saved"
        contentWizardPanel.typeData( dateContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        and: "just created date content has been reopened"
        contentBrowsePanel.doClearSelection();
        findAndSelectContent( dateContent.getName() ).clickToolbarEditAndSwitchToWizardTab();
        DateFormViewPanel dateFormViewPanel = new DateFormViewPanel( getSession() );

        then: "expected date and expected date should be equal"
        dateFormViewPanel.getDateValue() == DATE_IN_DECEMBER3;
    }
}
