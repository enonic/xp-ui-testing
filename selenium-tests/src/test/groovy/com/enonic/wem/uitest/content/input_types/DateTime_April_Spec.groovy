package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.DateTimeFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Ignore
import spock.lang.Shared

/**
 * Created  on 17.08.2016.
 * */
@Ignore
class DateTime_April_Spec
    extends Base_InputFields_Occurrences
{

    @Shared
    String DATE_TIME_1 = "2016-03-01 11:11";

    @Shared
    String DATE_TIME_2 = "2016-04-01 00:00"

    @Shared
    String DATE_TIME_3 = "2016-03-31 01:01"

    def "GIVEN wizard for new DateTime with tz is opened WHEN datetime in March has been typed AND saved THEN expected value displayed in the wizard"()
    {
        given: "wizard for adding a DateTime with timezone opened"
        Content dateTimeContent = buildDateTime1_1_Content( DATE_TIME_1 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( dateTimeContent.getContentTypeName() );


        when: "DateTime in March typed and content saved"
        contentWizardPanel.typeData( dateTimeContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        and: "just created datetime content is opened"
        contentBrowsePanel.doClearSelection();
        findAndSelectContent( dateTimeContent.getName() ).clickToolbarEditAndSwitchToWizardTab();
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );

        then: "actual value and expected are equals"
        formViewPanel.getDateTimeValue() == DATE_TIME_1;
    }

    def "GIVEN wizard for adding a DateTime with tz opened WHEN second datetime in April typed AND saved THEN correct value displayed in the wizard"()
    {
        given: "wizard for adding a DateTime with timezone opened"
        Content dateTimeContent = buildDateTime1_1_Content( DATE_TIME_2 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( dateTimeContent.getContentTypeName() );


        when: "DateTime in April typed and content saved"
        contentWizardPanel.typeData( dateTimeContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        and: "just created datetime content is opened"
        contentBrowsePanel.doClearSelection();
        findAndSelectContent( dateTimeContent.getName() ).clickToolbarEditAndSwitchToWizardTab();
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );

        then: "actual value and expected are equals"
        formViewPanel.getDateTimeValue() == DATE_TIME_2;
    }

    def "GIVEN wizard for adding a DateTime with tz is opened WHEN datetime in March typed AND saved THEN expected value displayed in the wizard"()
    {
        given: "wizard for adding a DateTime with timezone opened"
        Content dateTimeContent = buildDateTime1_1_Content( DATE_TIME_3 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( dateTimeContent.getContentTypeName() );

        when: "DateTime in March typed and content saved"
        contentWizardPanel.typeData( dateTimeContent ).save().closeBrowserTab().switchToBrowsePanelTab();

        and: "just created datetime content opened"
        contentBrowsePanel.doClearSelection();
        findAndSelectContent( dateTimeContent.getName() ).clickToolbarEditAndSwitchToWizardTab();
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );

        then: "actual value and expected are equals"
        formViewPanel.getDateTimeValue() == DATE_TIME_3;
    }

}
