package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.DateFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

/**
 * Created on 16.08.2016.
 *
 *  verifies   the https://youtrack.enonic.net/issue/XP-3885
 * */
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

    def "GIVEN wizard a Date-content is opened WHEN Nansen birth typed AND saved THEN correct value should be displayed in the wizard"()
    {
        given: "wizard for adding a Date opened"
        Content dateContent = buildDate1_1_Content( NANSEN_BIRTH );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( dateContent.getContentTypeName() );

        when: "Date typed and content saved"
        contentWizardPanel.typeData( dateContent ).save().closeBrowserTab().switchToBrowsePanelTab();
        contentBrowsePanel.doClearSelection();

        and: "just created date content opened"
        findAndSelectContent( dateContent.getName() ).clickToolbarEditAndSwitchToWizardTab();
        DateFormViewPanel dateFormViewPanel = new DateFormViewPanel( getSession() );

        then: "actual value and expected are equals"
        dateFormViewPanel.getDateValue() == NANSEN_BIRTH;
    }
    def "GIVEN wizard for adding a Date opened WHEN date in December typed AND saved THEN correct value displayed in the wizard"()
    {
        given: "wizard for adding a Date opened"
        Content dateContent = buildDate1_1_Content( DATE_IN_DECEMBER1 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( dateContent.getContentTypeName() );

        when: "Date in December typed and content saved"
        contentWizardPanel.typeData( dateContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        and: "just created date content opened"
        contentBrowsePanel.doClearSelection();
        findAndSelectContent( dateContent.getName() ).clickToolbarEditAndSwitchToWizardTab();
        DateFormViewPanel dateFormViewPanel = new DateFormViewPanel( getSession() );

        then: "actual value and expected are equals"
        dateFormViewPanel.getDateValue() == DATE_IN_DECEMBER1;
    }

    def "GIVEN wizard for adding a Date opened WHEN second date in December typed AND saved THEN correct value displayed in the wizard"()
    {
        given: "wizard for adding a Date opened"
        Content dateContent = buildDate1_1_Content( DATE_IN_DECEMBER2 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( dateContent.getContentTypeName() );

        when: "Date in December typed and content saved"
        contentWizardPanel.typeData( dateContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        and: "just created date content opened"
        contentBrowsePanel.doClearSelection();
        findAndSelectContent( dateContent.getName() ).clickToolbarEditAndSwitchToWizardTab();
        DateFormViewPanel dateFormViewPanel = new DateFormViewPanel( getSession() );

        then: "actual value and expected are equals"
        dateFormViewPanel.getDateValue() == DATE_IN_DECEMBER2;
    }

    def "GIVEN wizard for adding a Date opened WHEN third date in December typed AND saved THEN correct value displayed in the wizard"()
    {
        given: "wizard for adding a Date opened"
        Content dateContent = buildDate1_1_Content( DATE_IN_DECEMBER3 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( dateContent.getContentTypeName() );

        when: "Date in December typed and content saved"
        contentWizardPanel.typeData( dateContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        and: "just created date content opened"
        contentBrowsePanel.doClearSelection();
        findAndSelectContent( dateContent.getName() ).clickToolbarEditAndSwitchToWizardTab();
        DateFormViewPanel dateFormViewPanel = new DateFormViewPanel( getSession() );

        then: "actual value and expected are equals"
        dateFormViewPanel.getDateValue() == DATE_IN_DECEMBER3;
    }
}
