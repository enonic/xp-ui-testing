package com.enonic.wem.uitest.content.input_types

import com.enonic.autotests.pages.contentmanager.wizardpanel.ContentWizardPanel
import com.enonic.autotests.pages.form.DateTimeFormViewPanel
import com.enonic.autotests.vo.contentmanager.Content
import spock.lang.Shared

/**
 * Created  on 17.08.2016.
 *  verifies   the https://youtrack.enonic.net/issue/XP-3885
 * */
class DateTime_April_Spec
    extends Base_InputFields_Occurrences
{

    @Shared
    String DATE_TIME_1 = "2016-03-01 00:00";

    @Shared
    String DATE_TIME_2 = "2016-04-01 00:00"

    @Shared
    String DATE_TIME_3 = "2016-03-31 01:01"

    def "GIVEN wizard for adding a DateTime with tz opened WHEN datetime in March typed AND saved THEN correct value displayed in the wizard"()
    {
        given: "wizard for adding a DateTime with timezone opened"
        Content dateTimeContent = buildDateTime1_1_Content( DATE_TIME_1 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( dateTimeContent.getContentTypeName() );


        when: "DateTime in March typed and content saved"
        contentWizardPanel.typeData( dateTimeContent ).save().close( dateTimeContent.getDisplayName() );

        and: "just created datetime content opened"
        contentBrowsePanel.clickOnClearSelection();
        findAndSelectContent( dateTimeContent.getName() ).clickToolbarEdit().waitUntilWizardOpened();
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );

        then: "actual value and expected are equals"
        formViewPanel.getDateTimeValue() == DATE_TIME_1;
    }

    def "GIVEN wizard for adding a DateTime with tz opened WHEN second datetime in March typed AND saved THEN correct value displayed in the wizard"()
    {
        given: "wizard for adding a DateTime with timezone opened"
        Content dateTimeContent = buildDateTime1_1_Content( DATE_TIME_2 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( dateTimeContent.getContentTypeName() );


        when: "DateTime in April typed and content saved"
        contentWizardPanel.typeData( dateTimeContent ).save().close( dateTimeContent.getDisplayName() );

        and: "just created datetime content opened"
        contentBrowsePanel.clickOnClearSelection();
        findAndSelectContent( dateTimeContent.getName() ).clickToolbarEdit().waitUntilWizardOpened();
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );

        then: "actual value and expected are equals"
        formViewPanel.getDateTimeValue() == DATE_TIME_2;
    }

    def "GIVEN wizard for adding a DateTime with tz opened WHEN third datetime in March typed AND saved THEN correct value displayed in the wizard"()
    {
        given: "wizard for adding a DateTime with timezone opened"
        Content dateTimeContent = buildDateTime1_1_Content( DATE_TIME_3 );
        ContentWizardPanel contentWizardPanel = selectSitePressNew( dateTimeContent.getContentTypeName() );


        when: "DateTime in March typed and content saved"
        contentWizardPanel.typeData( dateTimeContent ).save().close( dateTimeContent.getDisplayName() );

        and: "just created datetime content opened"
        contentBrowsePanel.clickOnClearSelection();
        findAndSelectContent( dateTimeContent.getName() ).clickToolbarEdit().waitUntilWizardOpened();
        DateTimeFormViewPanel formViewPanel = new DateTimeFormViewPanel( getSession() );

        then: "actual value and expected are equals"
        formViewPanel.getDateTimeValue() == DATE_TIME_3;
    }

}
